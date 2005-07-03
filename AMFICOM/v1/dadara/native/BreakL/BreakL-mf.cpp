#include <math.h>
#include <assert.h>
#include "BreakL-mf.h"
#include "BreakL-enh.h"
#include "../JNI/ThreshArray.h"
#include "../common/pack.h"

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
// 0: ����� x ����� ������ �������
// nEv: ����� x ������ ��� ��������� � ������ �������
// 1 .. nEv-1: ����� x ����� �� ��������� [return-1 .. return)
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
		int xi = (int )x_s;
		if (x_s != xi)
		{
			int k = -1;
			int j;
			for (j = 0; j < N; j++, x_s += step)
			{
				k = f_BREAKL_GETPOS(nEv, pars, x_s, k);
				output[j] = f_BREAKL_GETY(k, nEv, pars, x_s);
			}
		}
		else // ����� ������� �������� - ��� ������ ����������������� ������: ���=1, ���������� �����
		{
			//prf_b("farr_BREAKL: fast: #1");
			int j = 0;
			// ������� �� ������ �������
			while (j < N && xi + j < pars[0])
				output[j++] = pars[1];

			//prf_b("farr_BREAKL: fast: #2");
			// ���� �������
			int k = 0;
			while (j < N && k < nEv - 1)
			{
				//assert(xi + j >= pars[k * 2]);
				// ����� ��������� �������� �����
				int x0 = (int )pars[k * 2];
				int x1 = (int )pars[k * 2 + 2];
				double y0 = pars[k * 2 + 1];
				double a = x1 > x0 ? (pars[k * 2 + 3] - y0) / (x1 - x0) : 0;
				// ������ ������� �����
				int jN = N < x1 - xi ? N : x1 - xi;
				while (j < jN) // 2
					output[j++] = y0 + a * (xi + j - x0);
				// ���� ����� �����
				if (j < N)
				{
					do k++;
					while (k < nEv - 1 && xi + j >= pars[k * 2 + 2]);
				}
			}
			//prf_b("farr_BREAKL: fast: #3");
			// ������� ����� �������
			while (j < N && xi + j <= pars[nEv * 2 - 2])
				output[j++] = pars[nEv * 2 - 1];
			//prf_b("farr_BREAKL: fast: done");
		}
	}
}

double fc_BREAKL(double *pars, ModelF &mf, int command, void *extra)
{
	if (command == MF_CMD_ACXL_CHANGE)
	{
		// ��� �������� �������� ����� �����,
		// ������� ����� ����������� ������ � ������� ������� ����������
		assert(pars == mf.getP());

		double dA = ((ACXL_data *)extra)->dA;
		double dL = ((ACXL_data *)extra)->dL;
		int dC = (int )((ACXL_data *)extra)->dC;
		int dX = (int )((ACXL_data *)extra)->dX;

		{ // A-��������������
			int N = mf.getNPars() / 2;
			int i;
			for (i = 0; i < N * 2; i += 2)
			{
				pars[i + 1] += dA; // ����� �����
			}
		}

		{ // L-��������������
			int i;
			int N = mf.getNPars() / 2;
			double *pars = mf.getP();
			double x0 = pars[0];
			double x1 = pars[N * 2 - 2];
			double y0 = pars[1];
			double y1 = pars[N * 2 - 1];

			// ���� ���. ����.
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

			// ���� ���. �������� ����� � ������ �� ����.
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

			// ������������ ����� � ������
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

		{ // CX-��������������
			int N = mf.getNPars() / 2;
			int isUpper = dX > 0;
			dX = isUpper ? dX : -dX;
			int i;
			for (i = 0; i < N * 2; i += 2)
			{
				pars[i + 0] += dC - dX; // ������� �����
			}
			int x0 = (int )pars[0];
			int x1 = (int )pars[N * 2 - 2];
			BreakL_Enh(mf, x0, x1, dX * 2, isUpper); // ���������� ������
			// ������ mf ����������, � �.�. N � ���������� pars
		}
	}
	if (command == MF_CMD_CHANGE_BY_THRESH_AND_FIND_DXDYID)
	{
		void **args = (void** )(void* )extra;
		return
			BreakL_ChangeByThresh(mf,
				*(ThreshDXArray *)args[0],
				*(ThreshDYArray *)args[1],
				*(int *)args[2],
				*(int *)args[3],
				*(int *)args[4]);
	}
	if (command == MF_CMD_CHANGE_BY_THRESH_AND_FIND_TTDXDY)
	{
		void **args = (void**)(void*)extra;

		ThreshDXArray &taX = *(ThreshDXArray*)args[0];
		ThreshDYArray &taY = *(ThreshDYArray*)args[1];
		int key = *(int *)args[2];
		int autoThresh = *(int *)args[3];
		int xMin = *(int*)args[4];
		int xMax = *(int*)args[5];
		TTDX *ttdxOut = (TTDX*)args[6];
		TTDY *ttdyOut = (TTDY*)args[7];

		assert(xMax >= xMin);
		//int xMin = (int )mf.getP()[0];
		//int xMax = (int )mf.getP()[mf.getNPars() - 2];
		int Nx = xMax - xMin + 1;

		ChangeBreakLByThreshEx (mf, taX, taY, key, xMin, xMax, autoThresh, ttdxOut, ttdyOut);

		return 1; // ������������ ���������� �������
	}
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

// I/O options

const double BL_Y_PREC = 1e-4;
inline long d2ly(double y) {
	return (long)floor(y / BL_Y_PREC + 0.5);
}
inline double l2dy(int y) {
	return y * BL_Y_PREC;
}

void fioQ_BREAKL(ModelF &mf)
{
	int nPars = mf.getNPars();
	double *pars = mf.getP();
	int i;
	for (i = 0; i < nPars / 2; i++)
	{
		pars[i * 2] = (int)pars[i * 2];
		pars[i * 2 + 1] = l2dy(d2ly(pars[i * 2 + 1]));
	}
}

void fioW_BREAKL(ModelF &mf, byteOut &bout)
{
	int nPars = mf.getNPars();
	double *pars = mf.getP();
	int i;

	bout.writeLong(nPars); // write number of pars

	// write all X
	int xPrev = 0;
	for (i = 0; i < nPars / 2; i++)
	{
		int xCur = (int)pars[i * 2];
		//bout.writeLong(xCur);
		//bout.writeLong(xCur - xPrev);
		pk_writeLongPlusInc(xCur - xPrev, bout);
		xPrev = xCur;
	}
	// write all Y
	long yPrev = 0;
	for (i = 0; i < nPars / 2; i++)
	{
		long yCur = d2ly(pars[i * 2 + 1]);
		long yDelta = yCur - yPrev;
		//bout.writeLong(yCur);
		//bout.writeLong(yDelta);
		//writeLong2b(yDelta, bout);
		//writeLongBC(yDelta, bout);
		pk_writeLongM3(yDelta, bout);
		yPrev = yCur;
	}
}

int fioR_BREAKL(ModelF &mf, byteIn &bin)
{
	int nPars = (int)bin.readLong(); // read number of pars
	double *pars = new double[nPars ? nPars : 1];
	assert(pars);
	mf.init(mf.getID(), nPars, pars); // set nPars, allocate pars by initialization
	int i;

	// XXX: will fail assertion if end of data will be found

	int xPrev = 0;
	for (i = 0; i < nPars / 2; i++)
	{
		//int xCur = bin.readLong();
		//int xCur = xPrev + bin.readLong();
		int xCur = xPrev + pk_readLongPlusInc(bin);
		pars[i * 2] = xCur;
		xPrev = xCur;
	}
	long yPrev = 0;
	for (i = 0; i < nPars / 2; i++)
	{
		//long yCur = bin.readLong();
		//long yCur = yPrev + bin.readLong();
		//long yCur = yPrev + readLong2b(bin);
		//long yCur = yPrev + readLongBC(bin);
		long yCur = yPrev + pk_readLongM3(bin);
		pars[i * 2 + 1] = l2dy(yCur);
		yPrev = yCur;
	}

	return 0;
}
