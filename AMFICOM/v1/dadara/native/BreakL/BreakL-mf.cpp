#include <assert.h>
#include "BreakL-mf.h"
#include "BreakL-enh.h"
#include "../JNI/ThreshArray.h"

static int bsearchmh(double *pars, int npairs, double key)
{
	int L = 0;
	int R = npairs;
	while (R > L)
	{
		int C = (R + L) / 2;
		if (pars[C * 2] > key)
			R = C;
		else
			L = C + 1;
	}
	return L;
}

// return:
// 0: точка x левее начала ломаной
// nEv: точка x правее или совпадает с концом ломаной
// 1 .. nEv-1: точка x лежит на интервале [return-1 .. return)
int f_BREAKL_GETPOS(int nEv, double *pars, double x, int k0) // k0 == -1: bsearch
{
	int k;
	if (k0 < 0 && nEv > 8)
	{
		k = bsearchmh(pars, nEv, x);
	}
	else
	{
		for (k = k0; k < nEv; k++)
		{
			if (pars[k * 2] > x)
				break;
		}
	}
	return k;
}

double f_BREAKL_GETY(int k, int nEv, double *pars, double x)
{
	if (k == 0)
		return pars[k * 2 + 1];

	if (k == nEv)
		return pars[(nEv - 1) * 2 + 1];

	double x0 = pars[k * 2 - 2];
	double y0 = pars[k * 2 - 1];
	double x1 = pars[k * 2 + 0];
	double y1 = pars[k * 2 + 1];

	if (x0 == x1)
		return (y0 + y1) / 2;

	return
		(x - x0) / (x1 - x0) * (y1 - y0) + y0;
}

double f_BREAKL(double *pars, int npars, double x, double *, int)
{
	//prf_b("f_BREAKL");
	int nEv = npars / 2;
	int k = f_BREAKL_GETPOS(nEv, pars, x, -1);
	return f_BREAKL_GETY(k, nEv, pars, x);
}

void farr_BREAKL(double *pars, int npars, double x_s, double step, int N, double *output)
{
	int nEv = npars / 2;

	if (step != 1)
	{
		int j;
		for (j = 0; j < N; j++, x_s += step)
		{
			int k = f_BREAKL_GETPOS(nEv, pars, x_s, -1);
			output[j] = f_BREAKL_GETY(k, nEv, pars, x_s);
		}
	}
	else // step == 1
	{
		int k = -1;
		int j;
		for (j = 0; j < N; j++, x_s += step)
		{
			k = f_BREAKL_GETPOS(nEv, pars, x_s, k);
			output[j] = f_BREAKL_GETY(k, nEv, pars, x_s);
		}
	}
}

double fc_BREAKL(double *pars, ModelF &mf, int command, void *extra)
{
	//prf_b("fc_BREAKL");
	if (command == MF_CMD_ACXL_CHANGE)
	{
		// эта операция изменяет число узлов,
		// поэтому может выполняться только с главным набором параметров
		assert(pars == mf.getP());

		double dA = ((ACXL_data *)extra)->dA;
		double dL = ((ACXL_data *)extra)->dL;
		int dC = (int )((ACXL_data *)extra)->dC;
		int dX = (int )((ACXL_data *)extra)->dX;

		{ // A-преобразование
			int N = mf.getNPars() / 2;
			int i;
			for (i = 0; i < N * 2; i += 2)
			{
				pars[i + 1] += dA; // сдвиг вверх
			}
		}

		{ // L-преобразование
			int i;
			int N = mf.getNPars() / 2;
			double *pars = mf.getP();
			double x0 = pars[0];
			double x1 = pars[N * 2 - 2];
			double y0 = pars[1];
			double y1 = pars[N * 2 - 1];

			// ищем абс. макс.
			double ymax = y0;
			int imax = 0;
			for (i = 0; i < N * 2; i += 2)
			{
				if (pars[i + 1] > ymax)
				{
					ymax = pars[i + 1];
					imax = i;
				}
			}

			// ищем мин. значение слева и справа от макс.
			double yminL = ymax;
			double yminR = ymax;
			for (i = 0; i < imax; i += 2)
			{
				if (pars[i + 1] < yminL)
					yminL = pars[i + 1];
			}
			for (i = imax; i < N * 2; i += 2)
			{
				if (pars[i + 1] < yminR)
					yminR = pars[i + 1];
			}

			// масштабируем слева и справа
			if (yminL < ymax)
			{
				double ratio = (ymax + dL - yminL) / (ymax - yminL);
				for (i = 0; i < imax; i += 2)
					pars[i + 1] = yminL + (pars[i + 1] - yminL) * ratio;
			}
			if (yminR < ymax)
			{
				double ratio = (ymax + dL - yminR) / (ymax - yminR);
				for (i = imax; i < N * 2; i += 2)
					pars[i + 1] = yminR + (pars[i + 1] - yminR) * ratio;
			}
		}

		{ // CX-преобразование
			int N = mf.getNPars() / 2;
			int isUpper = dX > 0;
			dX = isUpper ? dX : -dX;
			int i;
			for (i = 0; i < N * 2; i += 2)
			{
				pars[i + 0] += dC - dX; // смещаем влево
			}
			int x0 = (int )pars[0];
			int x1 = (int )pars[N * 2 - 2];
			BreakL_Enh(mf, x0, x1, dX * 2, isUpper); // раздвигаем вправо
			// теперь mf изменилась, в т.ч. N и размещение pars
		}
	}
	if (command == MF_CMD_CHANGE_BY_THRESH)
	{
		void **args = (void** )(void* )extra;
		BreakL_ChangeByThresh(mf, *(ThreshArray *)args[1], *(int *)args[0]);
	}
	if (command == MF_CMD_FIX_THRESH)
	{
		BreakL_FixThresh(mf, *(ThreshArray *)extra);
	}
	//prf_e();
	return 0;
}

double a_noiseSuppressionLength_BREAKL(double *pars, int npars)
{
	int N = npars / 2;
	int i;
	double ret = 0;
	for (i = 0; i < N - 1; i++)
	{
		double L1 = pars[i * 2 + 2] - pars[i * 2 + 0];
		double L2 = pars[i * 2 + 4] - pars[i * 2 + 2];
		if (L1 < 1)
			L1 = 1;
		if (L2 < 1)
			L2 = 1;
		double nef = (L1 + L2) / 2.0; // XXX: (L1 + L2) / 4.0 ??
		if (nef < 1)
			nef = 1;
		if (i == 0 || ret < nef)
			ret = nef;
	}
	return ret;
}
