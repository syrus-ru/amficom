#include <memory.h> // memcpy
#include <assert.h>
#include "../BreakL/BreakL-enh.h"
#include "makeThresh.h"
#include "../common/prf.h"

/* ���������� ����������� ��������� ��� ������� Y-������
 * �� ��� ����.
 * ���������� �� 0 <= x <= 1; ���������� ��������: >=0
 * *������* �������� ����������:
 * k(x) * x + k(1-x) * (1-x) = 1
 * k(0) = 0
 * ��������� �������� - �� ����������� ������������
 */
double wei2koeff(double w)
{
	// ��� w < 0.2 ��� ����� ����� ���� - �������� ������
	if (w < 0.2)
		return 0;
	// ���������� ������
	if (w > 0.5)
		return (1 - wei2koeff(1 - w) * (1 - w)) / w;
	double k = -0.5 * (1 - 5 * w + 2 * w * w) / (1 - 2 * w + 2 * w * w);
	if (k < 0)
		k = 0;
	return k;
}

static void makeThCurve(THX *thX, THY *thY, int thXc, int thYc, int isUpper,
		int xMin, int xMax, TTDX *ttdx, TTDY *ttdy, double *yBase, double *yTemp)
{
	int len = xMax - xMin + 1;
	memcpy(yTemp, yBase, sizeof(double) * len); // copy yBase to yTemp
	ChangeArrayByThreshEx (yTemp, thX, thY, thXc, thYc, isUpper, xMin, xMax, 1, ttdx, ttdy);
}

// ���������� ��������� �������� *thAdd ��� ������� �� ��������, ������������ flags,
// �������� �� ��� �������������� ��������� ������ � TTDY
// flags: 0x1: ���������� ������� � ������������� �������
// ttdxFilter: ����� ������ �������, ��������������� DX-������ # ttdxFilterKey
// ��������� iMin, iMax �������������, ����� ���� ������������ ��� ����������� ��� ������������� ttdxFilter
static void calcThAddByThCurve(int thYc, int isUpper,
		int xMin, int xMax, TTDY *ttdy,
		double *yTemp, double *yTgt, double *thAdd, int flags, TTDX *ttdxFilter = 0, int ttdxFilterKey = -1,
		int iMin = 0, int iMax = -1)
{
	int i;
	int len = xMax - xMin + 1;
	int sign = isUpper ? 1 : -1;

	if (iMax < 0)
		iMax = len - 1;

	assert(iMin >= 0);
	assert(iMax < len);

	for (i = 0; i < thYc; i++)
		thAdd[i] = 0;

	for (i = iMin; i <= iMax; i++)
	{
		// ���������� �����, �� ���������� ��� ttdxFilter
		if (ttdxFilter && ttdxFilter[i].thId != ttdxFilterKey)
			continue;
		// ���������� �����, � ������� ����� ��� ������ ���� ������
		double diff = (yTgt[i] - yTemp[i]);
		if (diff * sign < 0)
			continue;
		int thId = ttdy[i].thId;
		assert(thId >= 0);
		assert(thId < thYc);
		double nextWei = ttdy[i].nextWei;
		if (nextWei == 0)
		{
			// ������� ���������� ������������� ������
			// ������������ �����. �����
			if (diff * sign > thAdd[thId] * sign)
				thAdd[thId] = diff;
		}
		else
		{
			// ������� ����� ����� ��������
			if (flags & 0x1)
				continue; // ���� ���������, ���������� ����� �������
			// ������������ �����. �����
			// ���������� ����������� ��������� ������ ������
			double koeff = wei2koeff(1.0 - nextWei);
			if (diff * koeff * sign > thAdd[thId] * sign)
				thAdd[thId] = diff * koeff;
			// ���� ���� ������ ����� - ������������ ������
			// (������ ��� �������� ������)
			if (ttdy[i].nextWei == 0)
				continue;
			assert(thId + 1 < thYc);
			koeff = wei2koeff(nextWei); // ����������� ��������� ������� ������
			if (diff * koeff * sign > thAdd[thId + 1] * sign)
				thAdd[thId + 1] = diff * koeff;
		}
	}
}

static void calcThAdd(THX *thX, THY *thY, int thXc, int thYc, int isUpper,
		int xMin, int xMax, TTDY *ttdyTemp,
		double *yBase, double *yTemp, double *yTgt, double *thAdd, int flags)
{
	makeThCurve(thX, thY, thXc, thYc, isUpper, xMin, xMax, 0, ttdyTemp, yBase, yTemp);
	calcThAddByThCurve(thYc, isUpper, xMin, xMax, ttdyTemp, yTemp, yTgt, thAdd, flags);
}

static void addThAddToThY(THY *thY, int thYc, double *thAdd)
{
	int i;
	for (i = 0; i < thYc; i++)
		thY[i].dy += thAdd[i];
}

static void extendTHX(THX &src, THX &dest, int widthMin)
{
	if (&src != &dest)
		dest = src; // copying structure
	if (dest.dxL < widthMin)
		dest.dxL = widthMin;
	if (dest.dxR < widthMin)
		dest.dxR = widthMin;
}
static void extendTHX(THX *src, THX *dest, int N, int widthMin)
{
	int i;
	for (i = 0; i < N; i++)
		extendTHX(src[i], dest[i], widthMin);
}
void extendThreshToCover(THX *thXOrig, THY *thY, int thXc, int thYc, int isUpper,
		double *yBase, int xMin, int xMax, double *yTgt)
{
	prf_b("extendThreshToCover: enter");
	int len = xMax - xMin + 1;
	int sign = isUpper ? 1 : -1;

	// *** ������� ������������� DX ������ "�� ����� 1"
	/*
	for (i = 0; i < thXc; i++)
	{
		const int minWidth = 2; // FIXME: should be 1
		if (thXOrig->dxL < minWidth)
			thXOrig->dxL = minWidth;
		if (thXOrig->dxR < minWidth)
			thXOrig->dxR = minWidth;
	}
	*/

	// ��� ������ �������� � ������� DX � ����������� DY,
	// � ����� ��������� DX � ����������� DY
	// ����� �� �������� ���� ��������� DX/DY, ���� ���������
	// ��������� DX, �� �� ����������� ������� ��������� DY

	THX *thXT = new THX[thXc ? thXc : 1];
	int *thXA = new int[thXc ? thXc : 1];
	double *yPrev = new double[len];
	double *yTemp = new double[len];
	double *thAdd = new double[thYc ? thYc : 1];
	TTDX *ttdx = new TTDX[len];
	TTDY *ttdy = new TTDY[len];
	assert(thXA);
	assert(thXT);
	assert(yPrev);
	assert(yTemp);
	assert(thAdd);
	assert(ttdx);
	assert(ttdy);

	// ��� ��������� ������, �� ����� ��������� ����� ����� � �����
	// ������ �����, �� ������� ������ ������ DX-�����
	int *ttdxMin = new int[thXc ? thXc : 1];
	int *ttdxMax = new int[thXc ? thXc : 1];
	assert(ttdxMin);
	assert(ttdxMax);

	const int maxDX = 10; // FIXME

	prf_b("extendThreshToCover: processing maxDX");

	int i;
	for (i = 0; i < thXc; i++)
	{
		thXA[i] = maxDX; // ���������� ���. DX (�������������� �� ������������� � maxeq � thXOrig)
		extendTHX(thXOrig[i], thXT[i], maxDX); // ������������� ���. DX
	}

	// ��������� ���. ������ (� ��� ��������, ����� ��������)
	makeThCurve(thXT, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yTemp);
	calcThAddByThCurve(thYc, isUpper, xMin, xMax, ttdy, yTemp, yTgt, thAdd, 0x1);
	addThAddToThY(thY, thYc, thAdd);
	makeThCurve(thXT, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yTemp);
	calcThAddByThCurve(thYc, isUpper, xMin, xMax, ttdy, yTemp, yTgt, thAdd, 0x0);
	addThAddToThY(thY, thYc, thAdd);

	// ������ ������� ��������� ������
	makeThCurve(thXT, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yPrev);

	prf_b("extendThreshToCover: starting curDX loop");

	int curDX;
	for (curDX = maxDX - 1; curDX >= 0; curDX--)
	{
		// ������������� ������� DX
		for (i = 0; i < thXc; i++)
			extendTHX(thXOrig[i], thXT[i], curDX);

		// ��������� ��������� ������
		// (ttdx �� ���������, �.�. ��� ��� ��� ����� �� ttdx, ������� �����. ����������� ������)
		makeThCurve(thXT, thY, thXc, thYc, isUpper, xMin, xMax, 0, ttdy, yBase, yTemp);
		// ������������ ��������������� �������� � DY
		calcThAddByThCurve(thYc, isUpper, xMin, xMax, ttdy, yTemp, yTgt, thAdd, 0x0);
		int k;
		// �������������� ������� ����������� � ������������ �������, �� ������� ������ ������ DX-�����
		// ��� ������� ������������ ��� ��������� ������
		for (k = 0; k < thXc; k++)
			ttdxMin[k] = len;
		for (k = 0; k < thXc; k++)
			ttdxMax[k] = 0;
		for (i = 0; i < len; i++)
		{
			int tid = ttdx[i].thId;
			if (tid < 0)
				continue;
			assert(tid < thXc);
			ttdxMax[tid] = tid;
			if (ttdxMin[tid] > tid)
				ttdxMin[tid] = tid;
		}
		for (k = 0; k < thXc; k++)
		{
			if (thXOrig[k].dxL > curDX && thXOrig[k].dxR > curDX)
				continue; // ���� ����� ���������� ��� �����������, �� ��� ������������� ����������
			double S2 = 0;
			for (i = 0; i < len; i++)
			{
				if (ttdx[i].thId != k)
					continue;
				//assert ((yPrev[i] - yTemp[i]) * sign >= 0); // FIXME - debug only
				S2 += (yPrev[i] - yTemp[i]) * sign;
			}
			// ���� ��������������� ThAdd, ��������������� DX-������ k
			calcThAddByThCurve(thYc, isUpper, xMin, xMax, ttdy, yTemp, yTgt, thAdd, 0, ttdx, k);//, ttdxMin[k], ttdxMax[k]);
			// ��������� ��������� ������� ��������� ������ ��� ����� ThAdd
			double S1 = 0;
			for (i = 0; i < thYc; i++)
			{
				// XXX: �������� ������?
				// ������ �������� �� ������������� ��������� �������� Y � ����������� ����� ��������
				int xL = i > 0
					? (thY[i - 1].x1 + thY[i].x0 + 1) / 2
					: thY[i].x0;
				int xR = i < thYc - 1
					? (thY[i].x1 + thY[i + 1].x0 + 1) / 2
					: thY[i].x1;
				S1 += (xR - xL) * thAdd[i] * sign;
			}
			int accept = S1 < S2;
			/*if (accept && 1)
				fprintf (stderr,
					"isUpper %d curDX %2d k %2d (x %5d) S1 %13g S2 %13g %s (DX change = %2d)\n",
					isUpper, curDX, k, thXOrig[k].x0, S1, S2,
					accept ? "...accept" : "ignore...",
					accept ? curDX - thXA[k] : 0
					);*/
			if (accept)
				thXA[k] = curDX;
		}
		// ����� DX-������ ������������ � thXA.
		// ��������� ��������� ������ � ttdx
		for (i = 0; i < thXc; i++)
			extendTHX(thXOrig[i], thXT[i], thXA[i]);
		makeThCurve(thXT, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yPrev);
	}

	prf_b("extendThreshToCover: processing final DX");

	//for (i = 0; i < thXc; i++)
	//	fprintf(stderr, "Finally-1: up %d k %d (x %5d): dxOrig.dxL %2d dxOrig.dxR %2d dxNew %d\n", isUpper, i, thXOrig[i].x0, thXOrig[i].dxL, thXOrig[i].dxR, thXA[i]);

	for (i = 0; i < thXc; i++)
		extendTHX(thXOrig[i], thXOrig[i], thXA[i]);

	// ��������� ������������� DY ������ (� ��� ��������)
	makeThCurve(thXOrig, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yTemp);
	calcThAddByThCurve(thYc, isUpper, xMin, xMax, ttdy, yTemp, yTgt, thAdd, 0x1);
	addThAddToThY(thY, thYc, thAdd);
	makeThCurve(thXOrig, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yTemp);
	calcThAddByThCurve(thYc, isUpper, xMin, xMax, ttdy, yTemp, yTgt, thAdd, 0x0);
	addThAddToThY(thY, thYc, thAdd);

	//fprintf (stderr, "extendThreshToCover: done\n");
	//fflush(stderr);

	prf_b("extendThreshToCover: done");

	delete[] thXA;
	delete[] thXT;
	delete[] yPrev;
	delete[] yTemp;
	delete[] thAdd;
	delete[] ttdx;
	delete[] ttdy;
	delete[] ttdxMin;
	delete[] ttdxMax;
}
/*

	THX *thXTemp = new THX[thXc ? thXc : 1]; // ������� ����� DX-������� (����� �����������)
	double *thAdd = new double[thYc ? thYc : 1]; // ������ � ��������� ��� DY
	double *yCurr = new double[len]; // ����� ��� ������� ��������� ������
	double *yPrev = new double[len]; // ����� ��� ���������� ��������� ������
	TTDX *ttdx = new TTDX[len]; // ������ �����. ������ ����� DX-������� (�.�. -1)
	TTDY *ttdy = new TTDY[len]; // ������ �����. ������ ����� DY-������� (� ��������, �� � � -1)
	assert(thXTemp);
	assert(thAdd);
	assert(yCurr);
	assert(yPrev);
	assert(ttdx);
	assert(ttdy);

	const int maxDX = 10; // ����. / ��������� ������ DX

	extendTHX(thXOrig, thXTemp, thXc, maxMX); // ������������� ���. DX

	calcThAdd(thXTemp, thY, thXc, thYc, isUpper, xMin, xMax, ttdy, // ������������ ���. DY (����� �� ���� ��������)
		yBase, yCurr, yTgt, thAdd, 0);
	addThAddToThY(thY, thYc, thAdd); // ������������� ���. DY

	// ��������� ��������� ������ ��� ���. (DX,DY)
	makeThCurve(thXTemp, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yPrev);

	// ������� ��������� DX
	int curDX;
	for (curDX = maxDX - 1; curDX >= 0; curDX--)
	{
		extendTHX(thXOrig, thXTemp, thXc, maxMX); // ��������� DX ������ ������� max(orig,cur)
		makeThCurve(thXTemp, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yCurr);
		...
	}
	// ������������ ������ �������� ���������� �� �������� � ���������� ������������ DY-�������
	// (������, ��� �� ����� �� � ����������)
	calcThAdd(thX, thY, thXc, thYc, isUpper, xMin, xMax, ttdy,
		yBase, yTemp, yTgt, thAdd, 1);
	addThAddToThY(thY, thYc, thAdd);

	// ������������ ������ �������� ���������� �� ���� ��������
	calcThAdd(thX, thY, thXc, thYc, isUpper, xMin, xMax, ttdy,
		yBase, yTemp, yTgt, thAdd, 0);
	addThAddToThY(thY, thYc, thAdd);

	makeThCurve(thX, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yTemp);

	delete[] thXTemp;
	delete[] thAdd;
	delete[] yCurr;
	delete[] yPrev;
	delete[] ttdx;
	delete[] ttdy;
}
/*

  DXi, DY.

  DXc = min(DXi, 10); DY = auto-by-DXc;
  defP = by-DXc,DY; // ����������; ��������, ������ ����

  while (delta = 9; delta >= 0; delta--)
  {
     // ? - assert(defP[each i] == 0)
     DXt = min(DXi, delta);
	 defT = by-DXt,DY; // ���������� - ��������, ������ ����
	 foreach(i) if (defT[i] > defP[i])
	 {
	    nDX = ttdx_by_DXc[i];
		if (nDX == -1)
		{
		 assert(0); // �� ����� ��� ��� ������
		 continue;
		}
	 }



  }
*/