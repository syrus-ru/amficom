#include <stdio.h> // debug only
#include <math.h>
#include <assert.h>
#include "IAsimple.h"

inline int imax(int a, int b)
{
	return a > b ? a : b;
}
inline int imin(int a, int b)
{
	return a < b ? a : b;
}

//const double prec0 = 0.001 * 1.5;

double log2lin(double v)
{
	return exp(v * log(10.0) / 5.0);
}
double lin2log(double v)
{
	if (v < 0)
		return -99; // XXX
	else
		return 5.0 * log10(v);
}

static int isSmallerBy(double a, double b, double dif)
{
	return a < b - dif;
}

static int isBiggerBy(double a, double b, double dif)
{
	return a > b + dif;
}

// xdir: +1: left-to-right; -1: right-to-left
// sign < 0 : negative polarity event
static int findFrontByDer(double *integral, int size, int begin, int end, int xdir, int sign)
{
	int length = end - begin;
	int hw = length / 8;
	if (hw == 0)
		hw = 1;
	int i_from = imax(begin - hw, 0);
	int i_toex = imin(end + hw, size);
	int i;
	double vmin = 0;
	double vmax = 0;
	for (i = i_from + hw; i < i_toex - hw; i++)
	{
		double v = integral[i + hw] - integral[i - hw];
		if (i == i_from + hw || v < vmin)
			vmin = v;
		if (i == i_from + hw || v > vmax)
			vmax = v;
	}
	if (vmin == vmax)
		return begin;
	double vth = (vmax + vmin) / 2.0;
	if (xdir > 0)
	{	// ищем первое превышение
		for (i = i_from + hw; i < i_toex - hw; i++)
		{
			double v = integral[i + hw] - integral[i - hw];
			if (v * sign > vth * sign)
				return i;
		}
		return begin;
	}
	else
	{	// ищем последнее превышение
		int ret = begin;
		for (i = i_from + hw; i < i_toex - hw; i++)
		{
			double v = integral[i + hw] - integral[i - hw];
			if (v * sign > vth * sign)
				ret = i;
		}
		return ret;
	}
}

void InitialAnalysis2(
	ArrList *events,
	double *dataIn, // data
	int size,
	double delta_x, // dx, meters
	//double minimalThreshold,
	double minSplice, // min splice size, dB
	double minRefl, // min reflective, dB
	//double minimalEndingSplash,
	//double maximalNoise,
	//double noise3sLevel, // noise 3 sigma level, dB
	//int waveletType,
	//double formFactor,
	int reflSize,
	int spliceSize)
{
	fprintf(stderr, "IA2: #1\n");

	if (size <= 0)
		return; // пустой список событий - ничего не создаем

	const int avWidth = 10; // XXX -- reflectiveSize ?
	const double maxLPS = 0.5 * 0.001 * delta_x; // XXX: 0.5 dB/km

	int i;

	double *avd = new double[size];
	assert(avd);
	double *tarr = new double[size];
	assert(tarr);
	double *integral = new double[size];
	assert(integral);

	// переводим к лин. представлению
	for (i = 0; i < size; i++)
		tarr[i] = log2lin(dataIn[i]);

	fprintf(stderr, "IA2: #2\n");

	// сглаживаем (прямоугольником)
	{
		const int width = avWidth;
		for (i = 0; i < size; i++)
		{
			int j;
			int cnt = 0;
			double sum = 0;
			for (j = i - width + width / 2; j < i + width / 2; j++)
			{
				if (j < 0 || j >= size)
					continue;
				sum += tarr[j];
				cnt++;
			}
			avd[i] = sum / cnt;
		}
	}

	// приводим к лог. представлению
	for (i = 0; i < size; i++)
		avd[i] = lin2log(avd[i]);

	// рассчитываем интеграл от усредненной кривой в лог. предст.
	integral[0] = avd[0];
	for (i = 1; i < size; i++)
		integral[i] = integral[i - 1] + avd[i];

	ArrList iaeList;

	int findDeadZone = 1; // сейчас ищем мертвую зону

	fprintf(stderr, "IA2: #3\n");

	// сканируем
	int sw0 = spliceSize * 2;
	for (i = 5; i < size; i++) // XXX: 5
	{
		int evtype = 0;
		int j;
		double v0;

		// ищем + или - фронт на участке [i..j)
		if (!findDeadZone)
		{
			for (j = i; j < i + sw0 && j < size; j++)
			{
				if (isSmallerBy(avd[j], avd[i], minSplice + maxLPS * (j - i)))
				{
					evtype = 1;
					break;
				}
				if (isBiggerBy(avd[j], avd[i], minSplice))
				{
					evtype = 2;
					break;
				}
				if (isBiggerBy(avd[j], avd[i], minRefl))
				{
					evtype = 2;
					break;
				}
			}
			if (evtype == 0)
				continue;
			v0 = avd[i];
		}
		else
		{
			j = i; // DeadZone
			evtype = 1; // предполагаем + знак
			v0 = avd[i] - 1; // XXX
		}

		double vmax = avd[j]; // max - т для событий 2,3
		double vminrel = avd[j] + maxLPS * (j - i); // приведенный мин. - т для событий 1

		int D0 = j - i; // смещ. до заведомо-начала
		int D1 = j - i; // смещ. до конца события

		// если событие типа 2 (вверх), то пытаемся определить его амплитуду и длину
		// если длина есть - то это коннектор, типа 3
		if (evtype == 2)
		{
			for (j = i + D0; j < i + reflSize && j < size; j++)
			{
				double v1 = avd[j];

				if (v1 > v0 * 0.2 + vmax * 0.8)
					D1 = j - i;

				if (v1 >= vmax)
					vmax = v1;

				if (v1 < v0 * 0.75 + vmax * 0.25)
				{
					D1 = j - i;
					evtype = 3;
					break;
				}
			}
		}

		// если событие типа 1 (вниз) - пытаемся определить амплитуду
		if (evtype == 1)
		{
			for (j = i + D0; j < i + spliceSize && j < size; j++)
			{
				double v1rel = avd[j] + maxLPS * (j - i);
				if (v1rel < vminrel)
				{
					D1 = j - i;
					vminrel = v1rel;
				}
			}
		}

		// уточняем начало событий
		int sign = evtype == 1 ? -1 : +1;
		int w2 = evtype == 3 ? D0 : D1;

		int ev_begin = findFrontByDer(integral, size, i, i + w2, 1, sign);
		int ev_end = findFrontByDer(integral, size, i + D1, i + D0, -1, sign);


		// событие найдено
		iaeList.add(new IAEvent(
			ev_begin,
			ev_end,
			evtype == 3 ? IAEvent_CON : IAEvent_SPL));

		i += D1;
	}

	fprintf(stderr, "IA2: #4\n");

	// расставляем лин. события, заполняя выходной массив
	int lastEnd = 0;
	for (i = 0; i < iaeList.getLength(); i++)
	{
		fprintf(stderr, "IA2: #4.1 [%d]\n", i);
		IAEvent *ce = (IAEvent *)iaeList[i];
		if (i > 0 && ce->begin > lastEnd)
		{
			fprintf(stderr, "IA2: #4.1.1\n");
			events->add(new IAEvent(
				lastEnd,
				ce->begin,
				IAEvent_LIN));
			fprintf(stderr, "IA2: #4.1.2\n");
		}
		lastEnd = ce->end;
		fprintf(stderr, "IA2: #4.2\n");
		events->add(iaeList[i]);
		fprintf(stderr, "IA2: #4.3\n");
		iaeList.set(i, (void* )0);
		fprintf(stderr, "IA2: #4.4\n");
	}

	fprintf(stderr, "IA2: #5\n");

	iaeList.disposeAll();

	delete[] integral;
	delete[] tarr;
	delete[] avd;
}
