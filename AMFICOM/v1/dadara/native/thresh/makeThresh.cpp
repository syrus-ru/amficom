#include <assert.h>
#include "../BreakL/BreakL-enh.h"
#include "makeThresh.h"

// определяем расчетную поправку для порогов по участкам, определяемых flags
// flags: 0x1: пропускать участки с неоднозначным порогом
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
			continue; // FIXME: вообще-то это больше подходит под assert(0)
		//assert(thId >= 0);
		assert(thId < thYc);
		if (ttdy[i].nextWei != 0 && (flags & 0x1))
			continue; // если запрошено, пропускаем участки неоднозначного соответствия порогам
		double diff = yTgt[i] - yTemp[i];
		if (diff * sign > thAdd[thId] * sign)
			thAdd[thId] = diff;
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
	// *** (упрощенный алгоритм) устанавливаем DX пороги "не менее 1"
	for (i = 0; i < thXc; i++)
	{
		const int minWidth = 2; // FIXME: should be 1
		if (thX->dxL < minWidth)
			thX->dxL = minWidth;
		if (thX->dxR < minWidth)
			thX->dxR = minWidth;
	}

	// *** корректируем DY пороги
	double *thAdd = new double[thYc ? thYc : 1]; // проверка на случай thYc == 0
	double *yTemp = new double[len];
	TTDY *ttdy = new TTDY[len];
	assert(thAdd);
	assert(yTemp);
	assert(ttdy);

	// корректируем пороги согласно превышению на участках с однозначно определенным DY-порогом
	calcThAdd(thX, thY, thXc, thYc, isUpper, xMin, xMax, 0, ttdy,
		yBase, yTemp, yTgt, thAdd, 0);
	addThAddToThY(thY, thYc, thAdd);

	// корректируем пороги согласно превышению на всех участках
	calcThAdd(thX, thY, thXc, thYc, isUpper, xMin, xMax, 0, ttdy,
		yBase, yTemp, yTgt, thAdd, 1);
	addThAddToThY(thY, thYc, thAdd);

	delete[] ttdy;
	delete[] yTemp;
	delete[] thAdd;
}
