/*
 * findNoise.cpp
 */

/*
 * findNoise3s()
 *
 * Определение общего уровня шума рефлектограммы, используя
 * модель "уровень шума не зависит от сигнала".
 * TODO: Численный множитель для результирующего уровня расчитан плохо.
 *
 * Предполагается рефлектометрическая дБ-шкала.
 * При анализе учитывается:
 * - возможность пред-сглаживания по 8 точкам (NetTest)
 * - пред-округление до 0.001 дБ
 * - наличие событий на р/г (отбрасываем локально-монотонные участки)
 *
 * На входе - рефлектограмма (ргдБ).
 * на выходе - уровень шума (дБ) по уровню 3 сигма
 */

#include <math.h>
#include <assert.h>
#include <stdlib.h>
#include "findNoise.h"

#include "../Common/prf.h"

// Для отбрасывания участков, где точность определяется 0.001дБ-представлением
const double prec0 = 0.001 * 1.5;

static int dfcmp(const void *a, const void *b)
{
	const double *x = (const double *)a;
	const double *y = (const double *)b;
	return *x <= *y ? *x < *y ? -1 : 0 : 1;
}

static double log2add(double v)
{
	return exp(v * log(10.0) / 5.0);
}
static double add2log(double v)
{
	return 5.0 * log10(v);
}

double findNoise3s(double *data, int size)
{
	if (size <= 0)
		return 0;

	int i;
	double *out = new double[size];
	assert(out);

	const int width = 8; // XXX: NetTest specific - ширина, на которой исчезают корреляции

	int k;
	for (i = 0, k = 0; i < size - width * 2; i++)
	{
		double a = data[i];
		double b = data[i + width];
		double c = data[i + width * 2];

		// уменьшаем вес участков режима счета единичных импульсов
		if (a == c) continue;

		// отбрасываем начало р/г, где шум меньше чем шум 0.001дБ квантования
		if (fabs(a - b) < prec0 || fabs(b - c) < prec0)
			continue;

		// отбрасываем монтонные участки - чтобы исключить события
		// note: это завышает оценку ошибки через 2b-a-c
		if (((a < b) ^ (b < c)) == 0)
			continue;

		// переходим к единицам интенсивности
		a = log2add(a);
		b = log2add(b);
		c = log2add(c);

		// используем отклонение от линейности как оценку шума
		double dif = fabs(2 * b - a - c);

		out[k++] = dif;
	}

	const int s2 = k;

	//fprintf(stderr, "size %d s2 %d (%.2f%%)\n", size, s2, s2 * 100.0 / size);

	// строим интегральную гистограмму
	qsort(out, s2, sizeof(double), dfcmp);

	//for (i = 0; i < s2; i++)
	//	printf("%g %g\n", i * xmax / s2, add2log(out[i]));

	// определяем уровень 3 сигма
	double nlev = add2log(out[s2 / 2] * 1.3);

	delete[] out;

	return nlev;
}

static double dy2dB(double y0, double dy)
{
	return y0 + dy + 5.0 * log10(1.0 - pow(10.0, -dy / 5.0));
}

static double dB2dy(double y0, double dB)
{
	double ret = 5.0 * log10(1.0 + pow(10.0, (dB - y0) / 5.0));
	if (ret > 20.0) // XXX
		ret = 20.0;
	return ret;
}

static double destroyAndGetMedian(double *gist, int nsam, int pos)
{
	// FIXME: ускорить - есть простые алгоритмы со сложностью O(nsam)
	qsort(gist, nsam, sizeof(double), dfcmp);
	return gist[pos];
}

/*
 * findNoiseArray()
 * определение уровня шума в зависимости от координаты
 * с учетом возможности уменьшения уровня шума на
 * р/г
 * вых. значение - в отн. дБ, по ур. ~1(?) сигма
 */
void findNoiseArray(double *data, double *out, int size)
{
	prf_b("findNoise: findNoiseArray: enter");
	assert(size > 0);
	double *temp = new double[size]; // здесь временно будет сглаженная р/г
	assert(temp);

	const int width = 8;
	const int mlen = width * 10;
	// -1 здесь для выравнивания x-коорд.
	const int nsam = mlen - 2 * width - 1;
	int mofs = mlen / 2 - 1;
	double gist[nsam];

	assert(size > mlen); // XXX
	int i;
	for (i = 0; i < size - mlen; i++)
	{
		int j;
		// определяем уровень шума
		for (j = 0; j < nsam; j ++)
		{
			double v0 = data[i + j];
			double v1 = data[i + j + width];
			double v2 = data[i + j + width * 2];
			gist[j] = fabs(v2 + v0 - v1 - v1) + 0.001; // XXX
		}
		prf_b("findNoise: findNoiseArray: #1.2");
		double dv = destroyAndGetMedian(gist, nsam, nsam / 2);
		prf_b("findNoise: findNoiseArray: #1.3");

		// определяем среднее значение кривой
		for (j = 0; j < nsam; j++)
			gist[j] = data[i + j + width];
		prf_b("findNoise: findNoiseArray: #2.2");
		double y0 = destroyAndGetMedian(gist, nsam, nsam / 2);
		prf_b("findNoise: findNoiseArray: #2.3");
		temp[i + mofs] = y0;

		//int io = i + mofs;
		//fprintf(stdout, "%d %g %g\n", io, dv, dy2dB(ya[io], dv));
		out[i + mofs] = dy2dB(y0, dv);
	}
	for (i = 0; i < mofs; i++)
	{
		out[i] = out[mofs];
		temp[i] = temp[mofs];
	}
	for (i = size - mlen + mofs; i < size; i++)
	{
		out[i] = out[size - mlen + mofs - 1];
		temp[i] = temp[size - mlen + mofs - 1];
	}

	// строим кривую кумулятивного минимума
	for (i = 1; i < size; i++)
	{
		if (out[i] > out[i - 1])
			out[i] = out[i - 1];
		//fprintf(stdout,"%d %g\n", i, ya[i] - out[i]);
		//fprintf(stdout,"%d %g\n", i, out[i]);
	}

	// формируем выходной массив
	for (i = 0; i < size; i++)
	{
		out[i] = dB2dy(temp[i], out[i]);
		//fprintf(stdout,"%d %g\n", i, out[i]);
	}

	delete[] temp;
	prf_e();
}