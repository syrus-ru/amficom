/*
 * findNoise.cpp
 */

#include <math.h>
#include <assert.h>
#include "findNoise.h"
#include "../common/median.h"
#include "../common/prf.h"

#define prf_b(x) ((void)0)
#define prf_e() ((void)0)

// Уровень шума квантования входной р/гs
const double prec0 = 0.001 * 1.5;

// начальный уровень шума по отношению к макс. уровню сигнала, дБ
const double MAX_VALUE_TO_INITIAL_DB_NOISE = -10; // -5..-10..-15

// оценка ширины сглаживания белого шума NetTest'ом
const int NETTESTWIDTH = 16;

// для ускорения перевода дБ в интенсивность и обратно
const double L10O5 = log(10) / 5;

static double log2add(double v)
{
	return exp(v * L10O5);
}
static double add2log(double v)
{
	return 5.0 * log10(v);
}

inline double dy2dB(double y0, double dy)
{
	return y0 + dy + 5.0 * log10(1.0 - exp(-dy * L10O5));
}

// таблица экспонент для приближенного расчета
double exptable[100];
int exptableok = 0;
static void init_exp()
{
	if (exptableok)
		return;
	exptableok = 1;
	int i;
	for (i = 0; i < 100; i++)
		exptable[i] = exp(i * -0.1);
}

// 'rough' dB2dy
inline double rdB2dy(double y0, double dB)
{
#if 0	// точная формула
	double ret = 5.0 * log10(1.0 + exp((dB - y0) * L10O5));
#else	// приближенный расчет (отн. погрешность ~2%)
	double tmp = (dB - y0) * L10O5;
	// exp(x), x=-10..0 с точностью +- 1%
	{
		int id = (int) (tmp * -10);
		if (id > 0 && id < 100)
			tmp = exptable[id] * (1.0 + tmp - id * -0.1);
		else
			tmp = exp(tmp);
	}

	// log(1+x), x~0: +- 1%
	if (tmp > 0.15)
		tmp = log(1.0 + tmp);
	else
		tmp = tmp * (1 - tmp / 2);

	double ret = tmp / L10O5;

	// проверить расчет можно так:
	//double ret0 = 5.0 * log10(1.0 + exp((dB - y0) * L10O5));
	//assert(fabs(ret - ret0) < ret0 * 0.02);
#endif
	if (ret > 20.0) // XXX
		ret = 20.0;
	return ret;
}

/*
 * findNoiseArray()
 * определение уровня шума в зависимости от координаты
 * с учетом возможности уменьшения уровня шума на
 * р/г
 * вых. значение - в отн. дБ, по ур. ~1 сигма
 * len2 - интересующий пользователя интервал шума, д б <= size (кр. желательно чтобы включал м.з.)
 */
void findNoiseArray(double *data, double *outNoise, int size, int len2)
{
	if (len2 <= 0)
		return;

	assert(len2 <= size);

	prf_b("findNoiseArray: enter");

	const int width = NETTESTWIDTH;
	// mlen должно получиться четным
	const int mlen = width * 10;
	// -1 здесь для выравнивания x-коорд.
	const int nsam = mlen - 2 * width - 1;
	const int mofs = mlen / 2 - 1;
	double gist[nsam];

	int i;

	if (size < mlen)
	{
		// Нештатная ситуация - р/г так коротка, что шум определить нельзя
		// В таком случае в качестве уровня шума выдаем везде prec0 -- это
		// не очень здорово, но достаточно просто.
		for (i = 0; i < len2; i++)
			outNoise[i] = prec0;
		prf_b("findNoiseArray: done/ too short trace");
		return;
	}

	assert(size > mlen);

	// два временных массива
	double *temp = new double[size];
	double *out = new double[size];
	assert(temp);
	assert(out);

	double levelPrec0 = log2add(prec0) - 1;
	const int effSize = size < len2 + mlen - mofs ? size : len2 + mlen - mofs;

	prf_b("findNoiseArray: log2add");

	// приводим к линейному масштабу
	for (i = 0; i < effSize; i++)
	{
		out[i] = log2add(data[i]);
	}

	// начальная оценка уровня шума
	prf_b("findNoiseArray: first estimation");
	for (i = 0; i < effSize - mlen + nsam; i++)
	{
		// строго говоря, I2+I0-2I1 - это не то совсем что нам нужно,
		// но при характерных для рефлектометрии степенях затухания
		// поправка на нелинейность на 3 порядка меньше чем levelPrec0
		double v0 = out[i];
		double v1 = out[i + width];
		double v2 = out[i + width * 2];
		temp[i] = fabs(v2 + v0 - v1 - v1) + levelPrec0 * v1;
	}

	// усреднение
	prf_b("findNoiseArray: averaging");
	i = 0;
	while(i < effSize - mlen)
	{
		int j;
		// определяем начальный уровень шума
		for (j = 0; j < nsam; j++)
		{
			gist[j] = temp[i + j];
		}
		const int M = nsam / 2;
		double dv = destroyAndGetMedian(gist, nsam, M);
		double vout = add2log(dv);
#if 0	// точный метод -- расчет медианы в каждой точке
		out[i + mofs] = vout;
		i++;
#elif 0 // грубый метод -- расчет в каждой энной точке
		const int step = 4; // коэф-т загрубления, ~ 4
		for (j = 0; j < step && i + j < effSize - mlen; j++)
			out[i + j + mofs] = vout;
		i += step;
#else	// приближенный метод - выводим одно и то же число, пока
		// медиана лежит в пределах +- delta от ее начального значения
		double delta = dv * 0.05; // погрешность, ~ 0.05; быстродействие не страдает и при ~ 0.01
		double vL = dv - delta;
		double vH = dv + delta;
		int c1 = 0, c2 = 0; // c1 - число значений ниже vL; c2 - число значений выше vH
		for (j = 0; j < nsam; j++)
		{
			if (temp[i + j] < vL)
				c1++;
			else if (temp[i + j] > vH)
				c2++;
		}
		do
		{
			out[i + mofs] = vout;
			if (temp[i] < vL)
				c1--;
			else if (temp[i] > vH)
				c2--;
			if (temp[i + nsam] < vL)
				c1++;
			else if (temp[i + nsam] > vH)
				c2++;
			i++;
		}
#if 0	// учитываем и увеличение, и уменьшение уровня шума
		while (i < effSize - mlen && c1 <= M && c2 <= (nsam - M));
#else	// учитываем только уменьшение уровня шума (все равно далее строится кумул. минимум)
		while (i < effSize - mlen && c1 <= M);
#endif
#endif
	}
	prf_b("findNoiseArray: expand & process");

    // расширяем до краев массива - влево
	for (i = 0; i < mofs; i++)
	{
		out[i] = out[mofs];
	}
	// и - если надо - вправо
	for (i = size - mlen + mofs; i < len2; i++)
	{
		out[i] = out[size - mlen + mofs - 1];
	}

    // делаем поправку на начало рефлектограммы
    {
		// ищем абс. макс. р/г
        double vMax = data[0];
        for (i = 0; i < len2; i++)
        {
        	if (vMax < data[i])
            	vMax = data[i];
        }
        // поправляем начальный уровень шума
        if (out[0] > vMax + MAX_VALUE_TO_INITIAL_DB_NOISE)
        	out[0] = vMax + MAX_VALUE_TO_INITIAL_DB_NOISE;
    }

	// строим кривую кумулятивного минимума
	for (i = 1; i < len2; i++)
	{
		if (out[i] > out[i - 1])
			out[i] = out[i - 1];
	}

	prf_b("findNoiseArray: init exp");
	init_exp();
	prf_b("findNoiseArray: dB2dy");
	// формируем выходной массив
	for (i = 0; i < len2; i++)
		outNoise[i] = rdB2dy(data[i], out[i]);

	prf_b("findNoiseArray: done");

	delete[] temp;
	delete[] out;
}
