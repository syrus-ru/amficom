#include <assert.h>
#include "../BreakL/BreakL-enh.h"
#include "makeThresh.h"

/* ќпредел€ет коэффициент коррекции дл€ данного Y-порога
 * по его весу.
 * ќпределена на 0 <= x <= 1; допустимые значени€: >=0
 * *ƒолжна* обладать свойствами:
 * k(x) * x + k(1-x) * (1-x) = 1
 * k(0) = 0
 * ќстальные свойства - из соображений практичности
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

// определ€ем расчетную поправку дл€ порогов по участкам, определ€емых flags
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
			continue; // если запрошено, пропускаем участки неоднозначного соответстви€ порогам
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
	// (похоже, это не столь уж и необходимо)
	calcThAdd(thX, thY, thXc, thYc, isUpper, xMin, xMax, 0, ttdy,
		yBase, yTemp, yTgt, thAdd, 1);
	addThAddToThY(thY, thYc, thAdd);

	// корректируем пороги согласно превышению на всех участках
	calcThAdd(thX, thY, thXc, thYc, isUpper, xMin, xMax, 0, ttdy,
		yBase, yTemp, yTgt, thAdd, 0);
	addThAddToThY(thY, thYc, thAdd);

	delete[] ttdy;
	delete[] yTemp;
	delete[] thAdd;
}
