#include <math.h>
#include <assert.h>
#include "BreakL-enh.h"
#include "BreakL-fit.h"
#include "BreakL-mf.h"
#include "../common/ModelF.h"
#include "../common/ArrList.h"
#include "../thresh/thresh.h"

inline int imax(int a, int b) { return a > b ? a : b; }
inline int imin(int a, int b) { return a < b ? a : b; }

//#include "../common/prf.h"
#define prf_b(x) ((void)0)
#define prf_e() ((void)0)

void TTDX::set(int thId)
{
	this->thId = thId;
}
/*
void TTDX::operator= (TTDX &that)
{
	this->thId = that.thId;
}
*/

void TTDY::set(int thId, double nextWei)
{
	this->thId = thId;
	this->nextWei = nextWei;
}
int TTDY::getNearest()
{
	return nextWei > 0.5 ? thId + 1 : thId;
}
/*
void operator= (TTDY &that)
{
	this->thId = that.thId;
	this->nextWei = that.nextWei;
}
*/

/////////////////////////////////////////////////
// �������� enhance  � ���� ��������� - ��� ������������ TTDY � � ������������� TTDY

inline void imaxeq(double &a, double b)
{
	if (b > a)
		a = b;
}
inline void imineq(double &a, double b)
{
	if (b < a)
		a = b;
}
static void enh0p(double *data, int size, int width)
{
	int i;
	for (i = size - width - 1; i >= 0; i--)
	   imaxeq(data[i + width], data[i]);
}
static void enh0m(double *data, int size, int width)
{
	int i;
	for (i = size - width - 1; i >= 0; i--)
	   imineq(data[i + width], data[i]);
}
static void enhanceSimple(double *data, int size, int width, int isUpper)
{
	int i;
	for (i = 1; i <= width; i = i * 2)
	{
		isUpper ? enh0p(data, size, i) : enh0m(data, size, i);
		width -= i;
	}
	while (i)
	{
		if (i & width)
			isUpper ? enh0p(data, size, i) : enh0m(data, size, i);
		i /= 2;
	}
}
#if CANTTDXDY
inline void imaxeqtid(double &a, double b, TTDY &aid, TTDY &bid)
{
	if (b > a)
	{
		a = b;
		aid = bid;
	}
}
inline void imineqtid(double &a, double b, TTDY &aid, TTDY &bid)
{
	if (b < a)
	{
		a = b;
		aid = bid;
	}
}
static void enh0ptid(double *data, int size, int width, TTDY *id)
{
	int i;
	for (i = size - width - 1; i >= 0; i--)
	   imaxeqtid(data[i + width], data[i], id[i + width], id[i]);
}
static void enh0mtid(double *data, int size, int width, TTDY *id)
{
	int i;
	for (i = size - width - 1; i >= 0; i--)
	   imineqtid(data[i + width], data[i], id[i + width], id[i]);
}
static void enhanceWithTTDY(double *data, int size, int width, int isUpper, TTDY *id = 0)
{
	int i;
	for (i = 1; i <= width; i = i * 2)
	{
		if (id)
			isUpper ? enh0ptid(data, size, i, id) : enh0mtid(data, size, i, id);
		else
			isUpper ? enh0p(data, size, i) : enh0m(data, size, i);
		width -= i;
	}
	while (i)
	{
		if (i & width)
		{
			if (id)
				isUpper ? enh0ptid(data, size, i, id) : enh0mtid(data, size, i, id);
			else
				isUpper ? enh0p(data, size, i) : enh0m(data, size, i);
		}
		i /= 2;
	}
}
#endif
/////////////////////////////////////////////////

// "�����������" (��� ����������� ������) BreakL
// �������������� ������ BreakL � �� ����� �������� �������������� X-������������
// ������������ ������ ��� ACXL-��������������
void BreakL_Enh (ModelF &mf, int x0, int x1, int width, int isUpper)
{
	assert(mf.getID() == MF_ID_BREAKL);

	prf_b("BreakL_Enh: start");

	//fprintf(stderr, "BreakL_Enh: x0 %d x1 %d width %d isUpper %d\n",
	//	x0, x1, width, isUpper);
	//fflush(stderr);

	int N = mf.getNPars() / 2;
	if (N < 2)
		return;

	if (x1 <= x0)
		return; // ?

	double *P = mf.getP();

	int R0 = x0 - width;

	int len = x1 - R0 + 1;
	double *tmp = new double[len];
	assert(tmp);

	// ��������� ����. ������
	int j;
	int k;
	for (j = 0, k = 0; j < len; j++)
	{
		int x = R0 + j; // x in [R0 .. x1]
		if (x <= P[k])
		{
			tmp[j] = P[k + 1];
			continue;
		}
		while (k < 2 * N - 2 && x > P[k + 2])
			k += 2;
		if (k < 2 * N - 2)
			tmp[j] = P[k + 1]
				+ (x - P[k]) * (P[k + 3] - P[k + 1]) / (P[k + 2] - P[k]);
		else
			tmp[j] = P[k + 1];
	}

	// ��������� �����
	enhanceWithTTDY(tmp, len, width, isUpper);

	// ����������� � �������
	BreakL_FitI (mf, tmp, width, x0, len - width);

	//fprintf(stderr, "BreakL_Enh: done, input N %d x0 %d x1 %d len %d, output N = %d\n",
	//	N, x0, x1, len, mf.getNPars() / 2);
	//fflush(stderr);

	delete[] tmp;

	prf_b("BreakL_Enh: leave");
}

double *BreakLToArray(double *pars, int npars, int x0, int N) // N > 0, �� [x0..x0+N) �� ������� ������ � �������� ���
{
	double *ret = new double[N];
	assert(ret);
	farr_BREAKL(pars, npars, x0, 1.0, N, ret);
	return ret;
}

void BreakLFromArray(ModelF &mf, int x0, int N, double *yarr)
{
	BreakL_FitI(mf, yarr, 0, x0, N);
}

struct XY
{
	double x;
	double y;
};

int isMonotonous(int N, XY *P) // XXX: debug code, not required is runtime
{
	int i;
	for (i = 1; i < N; i++)
		if (P[i].x < P[i - 1].x)
			return 0;
	return 1;
}

struct UPDATE_REGION
{
	int x0;
	int N;
	int dxthid;
	double *data;
#if CANTTDXDY
	TTDY *tid;
	UPDATE_REGION(int x0, int N, int dxthid, double *data, TTDY *tid)
	{
		this->x0 = x0;
		this->N = N;
		this->data = data;
		this->dxthid = dxthid;
		this->tid = tid;
	}
#else
	UPDATE_REGION(int x0, int N, int dxthid, double *data)
	{
		this->x0 = x0;
		this->N = N;
		this->data = data;
		this->dxthid = dxthid;
	}
#endif
};

static int isat(int x, int xMin, int xMax)
{
	if (x < xMin)
		x = xMin;
	if (x > xMax)
		x = xMax;
	return x;
}

//autoThresh - ����� ��� �������������� ��������� ������� (0,1)
// ttdyOut - null ���� �� �����,
// ttdxOut - null ���� �� �����
void ChangeArrayByThreshEx (double *yArr, THX *thX, THY *thY, int thNpX, int thNpY, int isUpper,
						   int xMin, int xMax, int autoThresh,
						   TTDX *ttdxOut, TTDY *ttdyOut)
{
	prf_b("BreakL_ChangeByThresh: entered");

	int Nx = xMax - xMin + 1;
	int j;

#if CANTTDXDY
	// �������������� TTDY ��������� ���������� ��� ��������� AL-�������, ������ ���������������� �� ����
	// �������������� TTDX ���� ������� ����������������
	if (ttdxOut)
	{
		for (j = 0; j < Nx; j++)
			ttdxOut[j].set(-1);
	}

#endif

	double *unpk = yArr;

	prf_b("BreakL_ChangeByThresh: process AL threshs");

	// process A, L and NI threshs
	if (thNpY)
	{
		// ������� �������������� ���������� � ���/����. �������� � NI-�������
		int i;
		for (i = 0; i < thNpY; i++)
		{
			if (thY[i].type != THY_NI)
				continue;
			int jMin = isat(thY[i].x0, xMin, xMax) - xMin;
			int jMax = isat(thY[i].x1, xMin, xMax) - xMin;
			// ���� ���. ��� ����. ��������
			thY[i].temp = unpk[jMin];
			if (isUpper)
			{
				for (j = jMin + 1; j <= jMax; j++)
					if (thY[i].temp < unpk[j])
						thY[i].temp = unpk[j];
			}
			else
			{
				for (j = jMin + 1; j <= jMax; j++)
					if (thY[i].temp > unpk[j])
						thY[i].temp = unpk[j];
			}
			thY[i].temp += thY[i].dy;
		}

		int curLTn = -1; // ����� �������� ������ ���� ������ �� �������

		// ��� ��������� A->L, L->A:
		double Yx1L = 1; // �� ��������������, �.�. ������� ������� �� � � ���� L
		double Yx0R = 0; // (�������������� ������ ��� ���������� �����������)

		for (i = 0; i < Nx; i++)
		{
			int curX = xMin + i;

			// fix curLTn; keep Yx1_prev and Yx1_cur
			while (curLTn < 0 || curLTn < thNpY - 1 && curX >= thY[curLTn + 1].x0)
			{
				curLTn++;
				Yx1L = unpk[isat(thY[curLTn].x1, xMin, xMax) - xMin];
				if (curLTn < thNpY - 1)
					Yx0R = unpk[isat(thY[curLTn + 1].x0, xMin, xMax) - xMin];
				else
					Yx0R = 0;
				if (Yx1L == Yx0R)
					Yx1L++; // ������ �� div by zero
			}

			// ����� ��������� ����� curLTn, ������������ �����-����-���-�� �� ����� curX

			// ������������ ������ ������
			if (curX <= thY[curLTn].x1 || curLTn == thNpY - 1)
			{
#if CANTTDXDY
				if (ttdyOut)
					ttdyOut[i].set(curLTn, 0); // NB: ��� �����������, ��� ttdyOut[i].thId ����� ������ >= 0
#endif
				if (thY[curLTn].type == THY_NI)
					unpk[i] = thY[curLTn].temp; // ��� NI-������ ���������� ���������� �������
				else
					unpk[i] += thY[curLTn].dy; // ��� dA � dL ������� ������� �������� ������
				continue;
			}

			// ������������ ����� ��������

			int xL = thY[curLTn].x1;
			int xR = thY[curLTn + 1].x0;

			double dyL = thY[curLTn].dy;
			double dyR = thY[curLTn + 1].dy;

			int typL = thY[curLTn].type;
			int typR = thY[curLTn + 1].type;

			double wei;

			// ��� A-���� - �������� �������;
			// A-��� � L-��� - L-�������;
			// ��� ������ L-���� �� ����� ���� �����.
			// NI-����� ������ ����������� � ������� ��������, ������� ����� �� ��� �� �������
			if (typL == THY_DL || typR == THY_DL)
				wei = (unpk[i] - Yx1L) / (Yx0R - Yx1L);
			else
				wei = (double )(curX - xL) / (xR - xL);

			//fprintf(stderr, "AL threshs: LTn %d Mn %d curX %d xL %d xR %d dyL %g dyR %g tL %d tR %d wei %g\n",
			//	curLTn, curMn, curX, xL, xR, dyL, dyR, typeL, typeR, wei);

			unpk[i] += dyL + (dyR - dyL) * wei;
#if CANTTDXDY
			if (ttdyOut)
				//TTDY[i] = wei > .5 ? curLTn + 1 : curLTn;
				ttdyOut[i].set(curLTn, wei);
#endif
		}
	}
	else
	{ // no DY-thresholds defined at all
		if (ttdyOut)
		{
#if CANTTDXDY
			for (j = 0; j < Nx; j++)
				ttdyOut[j].thId = -1; // XXX
#endif
		}
	}

	prf_b("BreakL_ChangeByThresh: process DXLR threshs");

	// apply dx-thresholds
	if (thNpX)
	{
		ArrList updateRegions;
		int curT;
		for (curT = 0; curT < thNpX; curT++)
		{
			int xBegin = thX[curT].x0;
			int xEnd = thX[curT].x1;

			// ��������� �������� DX-������ (��� ����� ����������)
#if CANTTDXDY
			if (ttdxOut)
			{
				int jMin = imax(xBegin, xMin);
				int jMax = imin(xEnd, xMax);
				for (j = jMin; j <= jMax; j++)
					ttdxOut[j - xMin].set(curT);
			}
#endif

			int dxL = thX[curT].dxL;
			int dxR = thX[curT].dxR;

			if (dxL < 0)
				dxL = 0;
			if (dxR < 0)
				dxR = 0;

			if (dxL == 0 && dxR == 0)
				continue;

			int nBase0 = xEnd - xBegin + 1;
			//fprintf(stderr, "Applying dxL/dxR thresholds: curT %d dxL %d dxR %d, nBase %d; leftMode %d upper %d\n",
			//	curT, dxL, dxR, nBase0, thX[curT].leftMode, isUpper);

			if (nBase0 <= 0)
				continue;

			int W = dxL + dxR;

			int nEnh1 = nBase0 + W;
			int nEnh2 = nBase0 + W * 2; // "'2': Double-width enh"

			// �������� �������, ������� ���� ���������, �� ������ nBase0 + 2W
			// ��������� ����� ������ �� nBase0 + W, � ��������� ����������� ��� ��������
			// ����������� � �������� ������

			// XXX: ���� �� ����������?
			double *arr2io = new double[nEnh2];
			assert(arr2io);
#if CANTTDXDY
			TTDY *tid2io = 0;
			if (ttdyOut)
			{
				tid2io = new TTDY[nEnh2];
				assert(tid2io);
			}
#endif
			{
				int xL = xBegin - dxL * 2;
				int xR = xL + nEnh2;
				for (j = 0; j + xL < xMin; j++)
				{
					arr2io[j] = unpk[0];
#if CANTTDXDY
					if (ttdyOut)
						tid2io[j] = ttdyOut[0];
#endif
				}
				for (; j + xL < xR && j + xL <= xMax; j++)
				{
					arr2io[j] = unpk[j + xL - xMin];
#if CANTTDXDY
					if (ttdyOut)
						tid2io[j] = ttdyOut[j + xL - xMin];
#endif
				}
				for (; j + xL < xR; j++)
				{
					arr2io[j] = unpk[xMax - xMin - 1];
#if CANTTDXDY
					if (ttdyOut)
						tid2io[j] = ttdyOut[xMax - xMin - 1];
#endif
				}
			}

			// ��������� nBase0->nEnh2, ������� ������ �����. ������; �������� ���-� �� ������

			double *arr1t = new double[nEnh1];
			assert(arr1t);
#if CANTTDXDY
			TTDY *tid1t = 0;
			if (ttdyOut)
			{
				tid1t = new TTDY[nEnh1];
				assert(tid1t);
			}
#endif
			for (j = 0; j < nBase0; j++)
			{
				arr1t[j] = arr2io[j + dxL * 2];
#if CANTTDXDY
				if (ttdyOut)
					tid1t[j] = tid2io[j + dxL * 2];
#endif
			}
			for (j = nBase0; j < nEnh1; j++)
			{
				arr1t[j] = arr1t[nBase0 - 1];
#if CANTTDXDY
				if (ttdyOut)
					tid1t[j] = tid1t[nBase0 - 1];
#endif
			}

			// ���������� ���������
#if CANTTDXDY
			enhanceWithTTDY(arr1t, nEnh1, W, isUpper, ttdyOut ? tid1t : 0);
#else
			enhanceSimple(arr1t, nEnh1, W, isUpper);
#endif
			// ��������� ���������� �� arr1t � arr2io; ����� ���������� ����
			{
				double sign = isUpper ? 1.0 : -1.0;
				int leftMode = thX[curT].leftMode;
				int x0 = leftMode ? 0 : dxL;
				int x1 = leftMode ? nBase0 + dxL : nEnh1;
				for (j = x0; j < x1; j++)
				{
					arr2io[dxL + j] = arr1t[j];
#if CANTTDXDY
					if (ttdyOut)
						tid2io[dxL + j] = tid1t[j];
#endif
				}
				// ���������� ����, ����� �� ���� ��������
#if CANTTDXDY
				if (!autoThresh) // � �������������� ������ ��������� ������� ����������� �� ������
#endif
				{
					if (leftMode)
					{
						double y0 = arr1t[0];
						double y1 = arr2io[0];
						if (y1 * sign > y0 * sign) // XXX: � ��������� ������� ����� ���������� ������������� �� �����������
							y1 = y0;
						for (j = 1; j < dxL; j++)
						{
							double yt = y0 + (y1 - y0) * (double )j / dxL;
							if (arr2io[dxL - j] * sign < yt * sign)
							{
								arr2io[dxL - j] = yt;
#if CANTTDXDY
								if (ttdyOut)
									tid2io[dxL - j] = j > dxL * 0.5 ? tid2io[0] : tid1t[0];
#endif
							}
							else
								break;
						}
					}
					else
					{
						double y0 = arr1t[nEnh1 - 1];
						double y1 = arr2io[nEnh2 - 1];
						if (y1 * sign > y0 * sign) // XXX
							y1 = y0;
						for (j = 1; j < dxR; j++)
						{
							double yt = y0 + (y1 - y0) * (double )j / dxR;
							if (arr2io[nEnh2 - 1 - dxR + j] * sign < yt * sign)
							{
								arr2io[nEnh2 - 1 - dxR + j] = yt;
#if CANTTDXDY
								if (ttdyOut)
									tid2io[nEnh2 - 1 - dxR + j] = j > dxR * 0.5 ? tid2io[nEnh2 - 1] : tid1t[nEnh1 - 1];
#endif
							}
							else
								break;
						}
					}
				}
			}

			// ��������� ����������
#if CANTTDXDY
			UPDATE_REGION *ur = new UPDATE_REGION(xBegin - dxL * 2, nEnh2, curT, arr2io, ttdyOut ? tid2io : 0);
#else
			UPDATE_REGION *ur = new UPDATE_REGION(xBegin - dxL * 2, nEnh2, curT, arr2io);
#endif
			assert(ur);
			updateRegions.add(ur);
			delete[] arr1t;
#if CANTTDXDY
			if (ttdyOut)
				delete[] tid1t;
#endif
			//fprintf(stderr, "#6\n"); fflush(stderr);
		}

		// ��������� ��������������� ���������
		prf_b("BreakL_ChangeByThresh: committing DX thresholds: calc XRmax");
		int i;
		int XRmax = 0;
		for (i = 0; i < updateRegions.getLength(); i++)
		{
			UPDATE_REGION *urp = (UPDATE_REGION *)updateRegions[i];
			int x0 = urp->x0;
			int N = urp->N;
			int XR = x0 + N;
			if (XRmax < XR)
				XRmax = XR;
		}
		prf_b("BreakL_ChangeByThresh: committing DX thresholds: update array");
		for (i = 0; i < updateRegions.getLength(); i++)
		{
			UPDATE_REGION *urp = (UPDATE_REGION *)updateRegions[i];
			int x0 = urp->x0;
			int N = urp->N;
			double *upd = urp->data;
#if CANTTDXDY
			TTDY *updtid = urp->tid;
#endif
			{
				assert(x0 + N <= XRmax);
				double *data = unpk - xMin + x0;
#if CANTTDXDY
				// XXX: ���� ��������� ����� ��������� ��-�����-����, � ������ �������� ������ � ������� iMin
				// XXX: �.�., �������������� �������������� ����������� int � ���������: p+i+j==p+j+i
				TTDY *tids = ttdyOut ? ttdyOut - xMin + x0 : 0;
				TTDX *tidx = ttdxOut ? ttdxOut - xMin + x0 : 0;
#endif
				int i;
				int iMin = x0 < xMin ? xMin - x0 : 0;
				int iMax = x0 + N >= xMax + 1 ? xMax + 1 - x0 : N;

				if (isUpper)
				{
					for (i = iMin; i < iMax; i++)
						if (upd[i] > data[i])
						{
							data[i] = upd[i];
#if CANTTDXDY
							if (ttdyOut)
								tids[i] = updtid[i];
							if (ttdxOut)
								tidx[i].set(urp->dxthid);
#endif
						}
				}
				else
				{
					for (i = iMin; i < iMax; i++)
						if (upd[i] < data[i])
						{
							data[i] = upd[i];
#if CANTTDXDY
							if (ttdyOut)
								tids[i] = updtid[i];
							if (ttdxOut)
								tidx[i].set(urp->dxthid);
#endif
						}
				}
			}
			delete[] urp->data;
#if CANTTDXDY
			if (ttdyOut)
				delete[] urp->tid;
#endif
			delete urp;
		}
		prf_b("BreakL_ChangeByThresh: committing DX thresholds: done");
	}

}

void ChangeBreakLByThreshEx (ModelF &mf, ThreshDXArray &taX, ThreshDYArray &taY, int key,
						   int xMin, int xMax, int autoThresh,
						   TTDX *ttdxOut, TTDY *ttdyOut)
{
	int Nx = xMax - xMin + 1;
	// convert thresholds
	prf_b("ChangeBreakLByThreshEx: convert thresholds");
	int thNpX;
	int thNpY;
	THX *thX;
	THY *thY;
	ThreshDXArrayToTHXArray(taX, key, &thX, &thNpX);
	ThreshDYArrayToTHYArray(taY, key, &thY, &thNpY);

	// unpack BreakL
	prf_b("ChangeBreakLByThreshEx: unpacking BreakL");
	double *unpk = BreakLToArray(mf.getP(), mf.getNPars(), xMin, Nx);

	// process
	prf_b("ChangeBreakLByThreshEx: call ChangeArrayByThreshEx");
	int isUpper = taX.isUpper(key);
	ChangeArrayByThreshEx (unpk, thX, thY, thNpX, thNpY, isUpper,
						   xMin, xMax, autoThresh,
						   ttdxOut, ttdyOut);

	// pack
	prf_b("ChangeBreakLByThreshEx: BreakLFromArray");
	BreakLFromArray(mf, xMin, Nx, unpk);
	delete[] unpk;

	prf_b("ChangeBreakLByThreshEx: done");

	//fflush(stderr);

	if (thNpX > 0)
		delete[] thX;
	if (thNpY > 0)
		delete[] thY;
}

void ChangeBreakLByThreshABCorrEx (ModelF &mf, ThreshDXArray &taX, ThreshDYArray &taY, int key,
						   int xMin, int xMax, int autoThresh,
						   TTDX *ttdxOut, TTDY *ttdyOut)
{
	// make threshold curve
	ChangeBreakLByThreshEx(mf, taX, taY, key, xMin, xMax, autoThresh, ttdxOut, ttdyOut);

	// make correction (ttdx/ttdy need not to be changed)
	if (!taX.isUpper(key))
	{
		int nPts = mf.getNPars() / 2;
		double *pars = mf.getP();

		int j;
		for (j = 0; j < taX.getLength(); j++)
		{
			if (!taX.getIsRise(j) || !taX.hasABCorrFlag(j))
				continue;
			// ���� ����, ����������� � ������� ������
			int x0 = taX.getX0(j);
			int k = -1;
			int p;
			for (p = 2; p < nPts - 2; p++) // ����� ���� ��������� � ���� ���������
			{
				if (pars[p * 2] == x0)
				{
					k = p;
					break;
				}
			}
			//fprintf(stderr, "%d @ %4d %2d: %d\n", key, x0, j, k); // FIXME: debug
			if (k < 0)
				continue;
			// ��������� �������
			int xL = (int)pars[(k - 1) * 2];
			int xR = (int)pars[(k + 1) * 2];
			int xVL = (int)pars[(k - 2) * 2];
			int xVR = (int)pars[(k + 2) * 2];
			double yL = pars[(k - 1) * 2 + 1];
			double y0 = pars[ k      * 2 + 1];
			double yR = pars[(k + 1) * 2 + 1];
			double yVL = pars[(k - 2) * 2 + 1];
			double yVR = pars[(k + 2) * 2 + 1];
			if (y0 > yL && yR == y0 && yVR > yR && xVR > xR && xL > xVL)
			{
				// ���������� ������� (k-2,k-1) � (k+1,k+2) �� �� �����������
				// ���� ��� ������������ �� ������� x in [x[k-1], x[k+1]],
				// �� �������� ����������� ��������� ����� ���, ����� �������
				// ��� ����������� ������ ����� k.
				// ��������� ����� ����������� ����� �� ������� �� ����� ����� x,
				// ����� ������������ ��� ����� ��� ����������� ����������
				// ����������� ������ ������. � ������ �������, ��� ����� ������
				// ������������� ��� �����: k-1, k, k+1, � ������, ����� ��� 1-2
				// ����� ���������� ��� ����������� �����������, � 1-2 �������.
				double aL = (yVL - yL) / (xVL - xL);
				double aR = (yVR - yR) / (xVR - xR);
				if (aR > aL)
				{
					double xCross = (yL - yR + aR * (xR - xL)) / (aR - aL) + xL;
					int xC1 = (int)floor(xCross);
					int xC2 = (int)ceil(xCross);
					double yC1 = yL + (xC1 - xL) * aL;
					double yC2 = yR + (xC2 - xR) * aR;
					if (xC1 >= xL && xC2 <= xR) // ����������� �� ������� xL .. xR
					{
						int thirdLeft = xC1 > xL + 1; // �������� ������ ����� ����� ��� ������?
						//fprintf(stderr, "... UPDATE [%d %d - %d %d]\n", xL, xC1, xC2, xR); // FIXME: debug
						// ����� ����� �����
						pars[(k - 1) * 2] = xC1;
						pars[(k - 1) * 2 + 1] = yC1;
						int s0;
						int s1 = k + 2;
						if (xC2 > xC1)
						{
							// ����� ������ �����
							pars[k * 2] = xC2;
							pars[k * 2 + 1] = yC2;
							s0 = k + 1;
						}
						else
						{
							s0 = k;
						}
						// ������� ������ �����: [s1...] -> [s0...]
						while (s1 < nPts)
						{
							pars[s0 * 2] = pars[s1 * 2];
							pars[s0 * 2 + 1] = pars[s1 * 2 + 1];
							s0++;
							s1++;
						}
						nPts -= s1 - s0;
					}
				}
			}
		}
		//fflush(stderr); // FIXME: debug

		// ���� ���������� ����� �����, ������������ mf
		if (mf.getNPars() / 2 != nPts)
		{
			//fprintf(stderr, "changing nPts from %d to %d\n", mf.getNPars() / 2, nPts); // FIXME: debug
			//fflush(stderr); // FIXME: debug
			double *newPars = new double[nPts * 2];
			assert(newPars);
			for (j = 0; j < nPts * 2; j++)
			{
				newPars[j] = pars[j];
			}
			mf.init(mf.getID(), nPts * 2, newPars);
		}
	}
}

#if CANTTDXDY
// �������� ���������� ������� ������������� �������� - ������������� �.�. � �� ������.
// ��������������� ���������� - ����� ���������������� ���������� DX/DY-������ thCheckPosition.
// thCheckPosition: x-����������, ��� ������� ���� �������� ����� �������������� DX/DY-������
// wannaDXDY: 0: ������ �� ����, 1: ����� DX-�����, 2: ����� DY-�����
// ������������ �������� ����� ���� -1 (�� �������� ���� �� ������), ���� ��������� ������� � ������� taX ��� taY
int BreakL_ChangeByThresh (ModelF &mf, ThreshDXArray &taX, ThreshDYArray &taY, int key, int thCheckPosition, int wannaDXDY)
{
	prf_b("Outer BreakL_ChangeByThresh: enter");

	// FIXME: ������������, ��� ��������� � �������� ���� mf ������������� ����� ��������������
	int xMin = (int )mf.getP()[0];
	int xMax = (int )mf.getP()[mf.getNPars() - 2];
	int Nx = xMax - xMin + 1;

	TTDX *ttdxOut = 0;
	TTDY *ttdyOut = 0;
	if (wannaDXDY == 1)
	{
		ttdxOut = new TTDX[Nx];
		assert(ttdxOut);
	}
	if (wannaDXDY == 2)
	{
		ttdyOut = new TTDY[Nx];
		assert(ttdyOut);
	}

	prf_b("Outer BreakL_ChangeByThresh: go inside");
	ChangeBreakLByThreshABCorrEx (mf, taX, taY, key, xMin, xMax, 0, ttdxOut, ttdyOut);

	prf_b("Outer BreakL_ChangeByThresh: back from inside");

	int ret = -1;
	if (thCheckPosition >= xMin && thCheckPosition <= xMax)
	{
		if (wannaDXDY == 1)
			ret = ttdxOut[thCheckPosition - xMin].thId;
		if (wannaDXDY == 2)
			ret = ttdyOut[thCheckPosition - xMin].getNearest();
	}
	prf_b("Outer BreakL_ChangeByThresh: make ret code");

	if (ttdxOut)
		delete[] ttdxOut;
	if (ttdyOut)
		delete[] ttdyOut;

	prf_b("Outer BreakL_ChangeByThresh: done");
	return ret;
}
#endif