#include <assert.h>
#include "../BreakL/BreakL-enh.h"
#include "makeThresh.h"

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
	if (w > 0.5)
		return (1 - wei2koeff(1 - w) * (1 - w)) / w;
	double k = -0.5 * (1 - 5 * w + 2 * w * w) / (1 - 2 * w + 2 * w * w);
	if (k < 0)
		k = 0;
	return k;
}

// ���������� ��������� �������� ��� ������� �� ��������, ������������ flags
// flags: 0x1: ���������� ������� � ������������� �������
static void calcThAdd(THX *thX, THY *thY, int thXc, int thYc, int isUpper,
		int xMin, int xMax, TTDX *ttdx, TTDY *ttdy,
		double *yBase, double *yTemp, double *yTgt, double *thAdd, int flags)
{
	int i;
	int len = xMax - xMin + 1;
	int sign = isUpper ? 1 : -1;
	for (i = 0; i < len; i++)
		yTemp[i] = yBase[i];
	ChangeArrayByThreshEx (yTemp, thX, thY, thXc, thYc, isUpper, xMin, xMax, 1, 0, ttdy);
	for (i = 0; i < thYc; i++)
		thAdd[i] = 0;
	for (i = 0; i < len; i++)
	{
		int thId = ttdy[i].thId;
		if (thId < 0)
			continue; // FIXME: ������-�� ��� ������ �������� ��� assert(0)
		//assert(thId >= 0);
		assert(thId < thYc);
		if (ttdy[i].nextWei != 0 && (flags & 0x1))
			continue; // ���� ���������, ���������� ������� �������������� ������������ �������
		double diff = yTgt[i] - yTemp[i];
		double koeff = 1.0 - ttdy[i].nextWei;
		if (diff * koeff * sign > thAdd[thId] * sign)
			thAdd[thId] = diff * koeff;
	}
}

static void addThAddToThY(THY *thY, int thYc, double *thAdd)
{
	int i;
	for (i = 0; i < thYc; i++)
		thY[i].dy += thAdd[i];
}

void extendThreshToCover(THX *thX, THY *thY, int thXc, int thYc, int isUpper,
		double *yBase, int xMin, int xMax, double *yTgt)
{
	int len = xMax - xMin + 1;
	int i;
	// *** (���������� ��������) ������������� DX ������ "�� ����� 1"
	for (i = 0; i < thXc; i++)
	{
		const int minWidth = 2; // FIXME: should be 1
		if (thX->dxL < minWidth)
			thX->dxL = minWidth;
		if (thX->dxR < minWidth)
			thX->dxR = minWidth;
	}

	// *** ������������ DY ������
	double *thAdd = new double[thYc ? thYc : 1]; // �������� �� ������ thYc == 0
	double *yTemp = new double[len];
	TTDY *ttdy = new TTDY[len];
	assert(thAdd);
	assert(yTemp);
	assert(ttdy);

	// ������������ ������ �������� ���������� �� �������� � ���������� ������������ DY-�������
	// (������, ��� �� ����� �� � ����������)
	calcThAdd(thX, thY, thXc, thYc, isUpper, xMin, xMax, 0, ttdy,
		yBase, yTemp, yTgt, thAdd, 1);
	addThAddToThY(thY, thYc, thAdd);

	// ������������ ������ �������� ���������� �� ���� ��������
	calcThAdd(thX, thY, thXc, thYc, isUpper, xMin, xMax, 0, ttdy,
		yBase, yTemp, yTgt, thAdd, 0);
	addThAddToThY(thY, thYc, thAdd);

	delete[] ttdy;
	delete[] yTemp;
	delete[] thAdd;
}
