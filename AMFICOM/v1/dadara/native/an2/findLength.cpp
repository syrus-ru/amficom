#include "findLength.h"

const int TREAT_ONLY_DUPLICATE_ZEROES = 1;

// определяет длину рефлектограммы до начала шума (т.е. до конца р/граммы)
// если ничего не может сделать, должно вернуть data_length
// data - входная р/г
// data_length - ее длина
// return: значение >=0, <=data; по возможности не ноль.
int findReflectogramLength(double *data, int len)
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
		if (data[i] != vmin)
			continue;
		if (width)
		{
			if (data[i + 1] != vmin)
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
