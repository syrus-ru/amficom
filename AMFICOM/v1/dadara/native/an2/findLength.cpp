#include <assert.h>
#include "findLength.h"
#include "findNoise.h"
//#include <stdio.h> // debug: fprintf, stderr

const int TREAT_ONLY_DUPLICATE_ZEROES = 0;
const double DELTAV = 0.1; // толерантность минимума, можно установить в ноль
const double DELTANOISE = 0; // очень чувствительный подгоночный параметр

// определяет длину рефлектограммы до начала шума (т.е. до конца р/граммы)
// если ничего не может сделать, должно вернуть data_length
// data - входная р/г
// data_length - ее длина
// return: значение >=0, <=data; по возможности не ноль.

// первый метод - по достижению абс. нуля (не используется с 2005-09-10)
int findReflectogramLength0(double *data, int len)
{
	if (len <= 0)
		return 0;

	int width = TREAT_ONLY_DUPLICATE_ZEROES ? 1 : 0;

	// ищем минимальное значение на р/г
	double vmin = data[0];
	int i;
	for (i = 0; i < len - width; i++)
		if (vmin > data[i])
			vmin = data[i];

	// ищем самый большой X-интервал между минимальными значениями
	int lmax = 0; // длина наибольшего интервала
	int xmax = 0; // положение конца наибольшего интервала
	int lastX = 0; // начало текущего интервала
	for (i = 0; i < len - width; i++)
	{
		if (data[i] > vmin + DELTAV)
			continue;

		if (width)
		{
			if (data[i + 1] > vmin + DELTAV)
				continue;
		}
		// найден ноль
		int lcur = i - lastX;
		int xcur = i;
		if (lmax < lcur)
		{
			lmax = lcur;
			xmax = xcur;
		}
		lastX = i;
	}

	// результат = правый край самого большого интервала между минимальными значениями

	if (xmax)
		return xmax; // результат найден
	else
		return len; // результат не найден? //XXX: возможно ли это?
}

// второй метод (опробован 2005-09-10) - по многократному опусканию ниже абс. минимума
int findReflectogramLength1(double *data, int len)
{

	// ищем максимальное значение на р/г
	int imax = 0;
	int i;
	for (i = 0; i < len; i++)
		if (data[imax] < data[i])
			imax = i;

	// ищем минимальное значение справа от максимума
	double vmin = data[imax];
	for (i = imax; i < len; i++)
		if (vmin > data[i])
			vmin = data[i];

	// пытаемся определить по достижению мин. знач.

	const int WIDTH = 20;
	const int THRESH = 5;
	for (i = imax; i < len - WIDTH; i++) {
		int j;
		int c;
		if (data[i] >= vmin + DELTAV)
			continue;
		for (j = 0, c = 0; j < WIDTH; j++) {
			if (data[i + j] < vmin + DELTAV)
				c++;
		}
		if (c > THRESH)
			return i;
	}
	return len;

}

// третий метод (от 2005-09-10) - по многократному опусканию ниже уровня шума.
// Наладка этого метода закончилась осознанием, что настройка этого метода
// должна определяться способностью анализа работать в области шума.
// Чем лучше эти способности, тем дальше можно выдавать "конец волокна"
// при плавном уходе р/г в шум.
int findReflectogramLength(double *data, int len)
{

	// ищем максимальное значение на р/г
	int imax = 0;
	int i;
	for (i = 0; i < len; i++)
		if (data[imax] < data[i])
			imax = i;

	// пытаемся определить длину с помощью шума
	// XXX: performance: рассчитываем шум только для себя. Надо брать его извне.
	// note: performance: скорее всего, большая часть (правая) получаемого массива уровня шума будет не нужна. Это лишние вычисления.
	double *noise = new double[len];
	assert(noise);
	findAbsNoiseArray(data, noise, len, len);

	// XXX: debug
	//FILE *f = 0;
	//f = fopen("noiseL.dat", "w"); assert(f);
	//for (i = 0; i < len; i++) {
	//	if (f) fprintf(f, "%d %g %g %g\n", i, data[i], noise[i]);
	//}
	//if (f) fclose(f);

	int ret = len;
	const int WIDTH = 20;
	const int THRESH = 4; // очень чувствительный подгоночный параметр
	for (i = imax; i < len - WIDTH; i++) {
		int j;
		int c;
		if (data[i] >= noise[i] + DELTANOISE)
			continue;
		for (j = 0, c = 0; j < WIDTH; j++) {
			if (data[i + j] < noise[i] + DELTANOISE)
				c++;
		}
		if (c > THRESH) {
			// коррекция +1 нужна для того, чтобы эта "нулевая" точка
			// оказалась покрытой рабочим участком р/г
			// XXX: из-за пред-фильтрации последняя точка, реально попадающая
			// на вход анализа, может оказаться гораздо выше уровня шума,
			// что иногда сводит на нет эту коррекцию.
			ret = i + 1;
			break;
		}
	}

	delete[] noise;
	return ret;
}
