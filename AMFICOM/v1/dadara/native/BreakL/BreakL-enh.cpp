#include <assert.h>
#include "BreakL-enh.h"
#include "BreakL-fit.h"
#include "BreakL-mf.h"
#include "../Common/prf.h"
#include "../Common/ModelF.h"

//#include <stdio.h> // debug

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
static void enhance(double *data, int size, int width, int isUpper)
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

// "�����������" (��� ����������� ������) BreakL
// �������������� ������ BreakL � �� ����� �������� �������������� X-������������
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

	//prf_b("BreakL_Enh: #1");

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

	//prf_b("BreakL_Enh: #2");

	// ��������� �����
	enhance(tmp, len, width, isUpper);

	//prf_b("BreakL_Enh: #3");

	// ����������� � �������
	BreakL_FitI (mf, tmp, width, x0, len - width, 0, 0, 1);

	//prf_b("BreakL_Enh: #4");

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
	BreakL_FitI(mf, yarr, 0, x0, N, 0, 0, 1);
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

// ��� mf ������ ���� ���� BreakL
// ������� ����������� mf ������ ������������ ���� �� � ����� �����
void BreakLReplaceRegion(ModelF &mf, ModelF &that)
{
	int thisN = mf.getNPars() / 2;
	int thatN = that.getNPars() / 2;
	double *thisPd = mf.getP();
	XY *thisP = (XY *)thisPd;
	XY *thatP = (XY *)that.getP();

	int xMin = (int )thatP[0].x;
	int xMax = (int )thatP[thatN - 1].x;
	int i0 = f_BREAKL_GETPOS(thisN, mf.getP(), xMin, -1);
	int i1 = f_BREAKL_GETPOS(thisN, mf.getP(), xMax, -1);
	if (i1 == 0)
		return; // ������ �� ������������ - ������ ������

	// �������������� ������� ������ �������� ��:
	// (1) ���� i0 > 0, �� ����� this[0] .. [i0-1]
	// (2) ���� i0 > 0 � xMin != this.x[0], �� ���� (x=xMin, y=this(xMin))
	// (3) ���� ����� that[0] .. [thatN-1]
	// (4) ���� i1 < thisN, �� ���� (x=xMax, y=this(xMax))
	// (5) ���� i1 < thisN, �� ����� this[i1..thisN-1]

	int Nres = 0;
	if (i0 > 0)
		Nres += i0;
	if (i0 > 0 && xMin != thisP[0].x)
		Nres++;
	Nres += thatN;
	if (i1 < thisN)
		Nres++;
	if (i1 < thisN)
		Nres += thisN - i1;

	double *out = new double[Nres * 2];
	assert(out);
	XY *outP = (XY *)out;
	int k = 0;

	if (!isMonotonous(thisN, thisP))
	{
		fprintf(stderr, "BreakLReplaceRegion: isMonotonous check #1 failed\n"); fflush(stderr);
	}
	if (!isMonotonous(thatN, thatP))
	{
		fprintf(stderr, "BreakLReplaceRegion: isMonotonous check #2 failed\n"); fflush(stderr);
	}

	for (k = 0; k < i0; k++) // (1)
	{
		outP[k] = thisP[k];
	}
	if (i0 > 0 && xMin != thisP[0].x) // (2)
	{
		outP[k].x = xMin;
		outP[k].y = f_BREAKL_GETY(i0, thisN, thisPd, xMin);
		k++;
	}
	int j;
	for (j = 0; j < thatN; j++, k++) // (3)
	{
		outP[k] = thatP[j];
	}
	if (i1 < thisN) // (4)
	{
		outP[k].x = xMax;
		outP[k].y = f_BREAKL_GETY(i1, thisN, thisPd, xMax);
		k++;
	}
	for (j = i1; j < thisN; j++, k++) // (5)
	{
		outP[k] = thisP[j];
	}

	if (!isMonotonous(k, outP))
	{
		fprintf(stderr, "BreakLReplaceRegion: isMonotonous check #3 failed\n"); fflush(stderr);
	}

	//fprintf(stderr, "compare: k %d Nres %d -- %s\n", k, Nres, k==Nres ? "ok" : "ERROR");

	// ��������� ����������
	mf.init(MF_ID_BREAKL, k * 2, out);
}

void BreakLSetRegionFromArray(ModelF &mf, int x0, int N, double *yarr)
{
	ModelF that;
	BreakLFromArray(that, x0, N, yarr);
	BreakLReplaceRegion(mf, that);
}

struct COP0
{
	int x0;
	int x1;
	double dy;
	int type;
	int dxL;
	int dxR;
};

#define _FindExtrTemplate(op) \
{\
	int iExtr = x0;\
	double vExtr = mf.calcFun(iExtr);\
	int i;\
	for (i = iExtr + 1; i < x1; i++)\
	{\
		double v = mf.calcFun(i);\
		if (v op vExtr)\
		{\
			vExtr = v;\
			iExtr = i;\
		}\
	}\
	return iExtr;\
}

int findFirstMax(ModelF &mf, int x0, int x1) _FindExtrTemplate(>)
int findFirstMin(ModelF &mf, int x0, int x1) _FindExtrTemplate(<)
int findLastMax(ModelF &mf, int x0, int x1) _FindExtrTemplate(>=)
int findLastMin(ModelF &mf, int x0, int x1) _FindExtrTemplate(<=)

// ��������� ���� � �������, �� ����� ��������� ����� � �� �������� �� ���
// nodeList ������ ���� ���������
// ��������� nodeList, � ����� ������� ���
void BreakL_AddInternalNodesFromTempList(ModelF &mf, int listSize, int *nodeList)
{
	int i;
	int toAdd = 0;
	int Np = mf.getNPars() / 2;
	if (Np == 0)
		return;

	double *pars = mf.getP();
	int p; // ������ ������������ ����
	for (i = 0, p = 0; i < listSize; i++)
	{
		int pos = nodeList[i];

		if (i != 0 && pos == nodeList[i - 1])
			continue;
		int k = f_BREAKL_GETPOS(Np, pars, pos, -1);
		if (k == 0)
			continue; // ����� ����� �� ������ �������
		if (k == Np)
			continue; // ����� ������ �� ����� ������� ���� ��������� � ��������� �����
		int xk = (int )pars[k * 2 - 2];
		if (pos <= xk)
			continue; // ���� ��� ����

		if (p != 0)
		{
			//fprintf(stderr, "i %d p %d pos %d nl[p-1] %d\n",
			//	i, p, pos, nodeList[p - 1]);
			//fflush(stderr);
			assert(pos > nodeList[p - 1]); // ��������� ������������

			if (pos <= nodeList[p - 1])
				continue; // ���������� ������������ ������� ����
		}
		// ���������� ���� � ������ �� ����������
		nodeList[p++] = pos;
	}

	if (p == 0)
		return;

	// ������� ����� ������ ����������
	int newNp = Np + p;
	double *newPars = new double[newNp * 2];
	assert(newPars);

	int q; // ����� � ������ ����������� �����
	// i - ����� � ������ ������� �����
	int w; // ����� � ������ �������� �����
	for (i = 0, q = 0, w = 0; w < newNp; w++)
	{
		if (q < p && pars[2 * i] > nodeList[q])
		{
			int pos = nodeList[q];
			newPars[2 * w] = pos;
			newPars[2 * w + 1] = f_BREAKL_GETY(i, Np, pars, pos);
			q++;
		}
		else
		{
			newPars[2 * w] = pars[2 * i];
			newPars[2 * w + 1] = pars[2 * i + 1];
			i++;
		}
	}

	assert(i == Np); // XXX - �������� � ���������

	//fprintf(stderr, "BreakL_AddInternalNodesFromTempList: p %d Np %d newNp %d\n", p, Np, newNp); fflush(stderr);

	// �������� ���������
	mf.init(MF_ID_BREAKL, newNp * 2, newPars);

	delete[] nodeList;
}

void BreakL_ChangeByThresh (ModelF &mf, ThreshArray &ta, int key)
{
	const int thNp = ta.getLength();
	if (thNp <= 0)
		return;

	prf_b("BreakL_ChangeByThresh: entered");

	// convert thresholds
	COP0 *th = new COP0[thNp];
	assert(th);
	int j;
	for (j = 0; j < thNp; j++)
	{
		th[j].x0 = ta.getX0(j);
		th[j].x1 = ta.getX1(j);
		th[j].dy = ta.getValue(j, key);
		th[j].type = ta.getType(j);
		th[j].dxL = ta.getDxL(j, key);
		th[j].dxR = ta.getDxR(j, key);
	}

	prf_b("BreakL_ChangeByThresh: add nodes");

	// ��������� ���� � ������ ������ � ����� ���� �������
	// ��� �������� mf
	{
		int *nodeList = new int[thNp * 2];
		assert(nodeList);
		// NB: ������ �� ������ ���� ��� ����������, �� ������ ���� ����������
		for (j = 0; j < thNp; j++)
		{
			nodeList[2 * j] = th[j].x0;
			nodeList[2 * j + 1] = th[j].x1;
		}
		BreakL_AddInternalNodesFromTempList(mf, thNp * 2, nodeList);
	}

	prf_b("BreakL_ChangeByThresh: process AL threshs");

	// process A and L threshs
	{
		XY *mfpars = (XY* )mf.getP();
		int mfNp = mf.getNPars() / 2;

		int curMn; // ����� �������� ���� mf
		int curLTn; // ����� �������� ������ ���� ������ �� �������

		// ��� ��������� A->L, L->A:
		double Yx1L = 1; // �� ��������������, �.�. ������� ������� �� � � ���� L
		double Yx0R = 0; // (�������������� ������ ��� ���������� �����������)

		for (curMn = 0, curLTn = -1; curMn < mfNp; curMn++)
		{
			int curX = (int )mfpars[curMn].x;

			// fix curLTn; keep Yx1_prev and Yx1_cur
			while (curLTn < 0 || curLTn < thNp - 1 && curX >= th[curLTn + 1].x0)
			{
				curLTn++;
				Yx1L = mf.calcFun(th[curLTn].x1);
				if (curLTn < thNp - 1)
					Yx0R = mf.calcFun(th[curLTn + 1].x0);
				else
					Yx0R = 0;
				if (Yx1L == Yx0R)
					Yx1L++; // ������ �� div by zero
			}

			// ����� ��������� ����� curLTn, ������������ �����-����-���-�� �� ����� curX

			// ������������ ������ ������
			if (curX <= th[curLTn].x1 || curLTn == thNp - 1)
			{
				mfpars[curMn].y += th[curLTn].dy;
				continue;
			}

			// ������������ ����� ��������

			//fprintf(stderr, "LTn %d Mn %d 

			int xL = th[curLTn].x1;
			int xR = th[curLTn + 1].x0;

			double dyL = th[curLTn].dy;
			double dyR = th[curLTn + 1].dy;

			int typeL = th[curLTn].type;
			int typeR = th[curLTn + 1].type;

			double wei;

			// ��� A-���� - �������� �������;
			// A-��� � L-��� - L-�������;
			// ��� ������ L-���� �� ����� ���� �����.
			if (typeL || typeR)
				wei = (mfpars[curMn].y - Yx1L) / (Yx0R - Yx1L);
			else
				wei = (double )(curX - xL) / (xR - xL);
			mfpars[curMn].y += dyL + (dyR - dyL) * wei;
		}
	}

	prf_b("BreakL_ChangeByThresh: process DXLR threshd");

	// apply dx-thresholds
	/*{
		int curT;
		for (curT = 1; curT < thNp - 1; curT++)
		{
			int dxL = th[curT].dxL;
			int dxR = th[curT].dxR;

			if (dxL < 0)
				dxL = 0;
			if (dxR < 0)
				dxR = 0;

			if (dxL == 0 && dxR == 0)
				continue;

			int xPrev = th[curT - 1].x1;
			int xNext = th[curT + 1].x0;

			int nBase0 = xNext - xPrev;
			//fprintf(stderr, "Applying dxL/dxR thresholds: curT %d dxL %d dxR %d, nBase %d\n", curT, dxL, dxR, nBase0);

			if (nBase0 <= 0)
				continue;

			int W = dxL + dxR;

			int nEnh1 = nBase0 + W;
			int nEnh2 = nBase0 + W * 2; // "'2': Double-width enh"

			// �������� �������, ������� ���� ���������, �� ������ nBase0 + 2W
			// ��������� ����� ������ �� nBase0 + W, � ��������� ����������� ��� ��������
			// ����������� � �������� ������
			// NB: mf.getP() � mf.getNPars() ����� ���������� ������ �������� � ������ ��������,
			// �.�. �� �������� ����� mf � ����� ��������
			double *arr1b = BreakLToArray(mf.getP(), mf.getNPars(), xPrev - dxL * 2, nEnh2);

			// ��������� ������, ������� ������ �����. ������; �������� ���-� �� ������
			double *arr2 = new double[nEnh2];
			assert(arr2);
			for (j = 0; j < nEnh1; j++)
				arr2[j] = arr1b[j + dxL];
			for (j = nEnh1; j < nEnh2; j++)
				arr2[j] = arr1b[nEnh1 + dxL - 1];

			// ���������� ���������
			int isUpper = ta.isUpper(key);
			enhance(arr2, nEnh2, W, isUpper);

			// ��������� ������� �������
			for (j = 0; j < dxL; j++)
			{
				arr2[j] = (arr2[j] * j + arr1b[j] * (dxL - j)) / (double )dxL;
			}
			for (j = -dxR; j < 0; j++)
			{
				int k = j + nEnh2;
				arr2[k] = (arr2[k] * -j + arr1b[k] * (dxR + j)) / (double )dxR;
			}

			// ������� ��������� ����������� ���������� � ����������
			int sign = isUpper ? 1 : -1;
			for (j = 0; j < nEnh2; j++)
				if (arr2[j] * sign < arr1b[j] * sign)
					arr2[j] = arr1b[j];

			// ��������� � mf
			BreakLSetRegionFromArray(mf, xPrev - dxL * 2, nEnh2, arr2);
			delete[] arr1b;
			delete[] arr2;
			//fprintf(stderr, "#6\n"); fflush(stderr);
		}
	}*/
	{
		int curT;
		for (curT = 1; curT < thNp - 1; curT++)
		{
			int dxL = th[curT].dxL;
			int dxR = th[curT].dxR;

			if (dxL < 0)
				dxL = 0;
			if (dxR < 0)
				dxR = 0;

			if (dxL == 0 && dxR == 0)
				continue;

			int xPrev = th[curT - 1].x1;
			int xNext = th[curT + 1].x0;

			int nBase0 = xNext - xPrev;
			//fprintf(stderr, "Applying dxL/dxR thresholds: curT %d dxL %d dxR %d, nBase %d\n", curT, dxL, dxR, nBase0);

			if (nBase0 <= 0)
				continue;

			int isUpper = ta.isUpper(key);

			int W = dxL + dxR;

			int nEnh1 = nBase0 + W;
			int nEnh2 = nBase0 + W * 2; // "'2': Double-width enh"

			// �������� �������, ������� ���� ���������, �� ������ nBase0 + 2W
			// ��������� ����� ������ �� nBase0 + W, � ��������� ����������� ��� ��������
			// ����������� � �������� ������
			// NB: mf.getP() � mf.getNPars() ����� ���������� ������ �������� � ������ ��������,
			// �.�. �� �������� ����� mf � ����� ��������
			double *arr1 = BreakLToArray(mf.getP(), mf.getNPars(), xPrev - dxL * 2, nEnh2);

			// ��������� nBase0->nEnh2, ������� ������ �����. ������; �������� ���-� �� ������
			double *arr2a = new double[nEnh1];
			assert(arr2a);
			for (j = 0; j < nBase0; j++)
				arr2a[j] = arr1[j + dxL * 2];
			for (j = nBase0; j < nEnh1; j++)
				arr2a[j] = arr2a[nBase0 - 1];

			// ���������� ���������
			enhance(arr2a, nEnh1, W, isUpper);

			// ��������� �������
			if (isUpper) // ��� �������� ������ - ������ �� ����������� � ���. ������
			{
				// ������� ������
				double yA = arr2a[nEnh1 - 1];
				double yB = arr1[nEnh2 - 1];
				for (j = 0; j < dxR; j++)
				{
					double yJ = yA + (yB - yA) * (j + 1) / dxR;
					if (arr1[nEnh2 - dxR + j] >= yJ)
						break;
					else
						arr1[nEnh2 - dxR + j] = yJ;
				}
				// ������� �����
				yA = arr2a[0];
				yB = arr1[0];
				for (j = dxL - 1; j >= 0; j--)
				{
					double yJ = yB + (yA - yB) * j / dxL;
					if (arr1[j] >= yJ)
						break;
					else
						arr1[j] = yJ;
				}
			}
			// ������� ��������� ����������� ���������� � ����������
			int sign = isUpper ? 1 : -1;
			for (j = 0; j < nEnh1; j++)
				if (arr1[dxL + j] * sign < arr2a[j] * sign)
					arr1[dxL + j] = arr2a[j];

			// ��������� � mf
			BreakLSetRegionFromArray(mf, xPrev - dxL * 2, nEnh2, arr1);
			delete[] arr1;
			delete[] arr2a;
			//fprintf(stderr, "#6\n"); fflush(stderr);
		}
	}

	prf_b("BreakL_ChangeByThresh: done");

	//fflush(stderr);

	delete[] th;
}

// ������������ ������ � ����� L-������� �� ������ ��������� ���� ������.
// ����� ����� ���������, ������ � ����� L-������ ��������� �� ���������
// ���������, � ����� � ������ ������� ������� - �� ����� ������������ � A �� L �����
// � ����� �������� ���� ����� ������� ��� ����� ��������� �������
void BreakL_FixThresh (ModelF &mf, ThreshArray &ta)
{
	int N = ta.getLength();
	//fprintf(stderr, "breakL_FixThresh: N = %d\n", N);
	int tid;
	for (tid = 1; tid < N - 1; tid++) // ������� ������ �� ����� ���� L-����
	{
		//fprintf(stderr, "tid %d: type %d\n", tid, (int )ta.getType(tid));
		if (ta.getType(tid) == 0)
			continue;

		int x0 = ta.getX0(tid);
		int x1 = ta.getX1(tid) + 1;
		if (x0 >= x1)
		{
			fprintf(stderr, "tid %d: x0 >= x1\n", tid);
			fflush(stderr);
			continue;
		}

		int iMax = findFirstMax(mf, x0, x1);

		int iMinL = findLastMin(mf, x0, iMax);
		int iMinR = findFirstMin(mf, iMax, x1);

		// ���������
		ta.setX0(tid, iMax);
		ta.setX1(tid, iMax);

		// ������ ���� �������������� �������� ������ �������� �������
		//assert(tid > 0);
		//assert(tid + 1 < N);
		ta.setX1(tid - 1, iMinL);
		ta.setX0(tid + 1, iMinR);
		//fprintf(stderr, "tid %d: x0 %d x1 %d iMinL %d iMaxL %d iMaxR %d iMinR %d\n",
		//	tid, x0, x1, iMinL, iMaxL, iMaxR, iMinR);
	}
	//fflush(stderr);
}
