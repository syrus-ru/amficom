#include <memory.h> // memcpy
#include <assert.h>
#include "../BreakL/BreakL-enh.h"
#include "makeThresh.h"
#include "../common/prf.h"

/* Определяет коэффициент коррекции для данного Y-порога
 * по его весу.
 * Определена на 0 <= x <= 1; допустимые значения: >=0
 * *Должна* обладать свойствами:
 * k(x) * x + k(1-x) * (1-x) = 1
 * k(0) = 0
 * Остальные свойства - из соображений практичности
 */
double wei2koeff(double w)
{
	// при w < 0.2 все равно будет ноль - ускоряем расчет
	if (w < 0.2)
		return 0;
	// собственно расчет
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

// определяем расчетную поправку *thAdd для порогов по участкам, определяемых flags,
// опираясь на уже сформированную пороговую кривую и TTDY
// flags: 0x1: пропускать участки с неоднозначным порогом
// ttdxFilter: брать только участки, соответствующие DX-порогу # ttdxFilterKey
// параметры iMin, iMax необязательны, могут быть использованы для оптимизации при использовании ttdxFilter
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
		// пропускаем точки, не попадающие под ttdxFilter
		if (ttdxFilter && ttdxFilter[i].thId != ttdxFilterKey)
			continue;
		// пропускаем точки, в которых порог уже сейчас шире кривой
		double diff = (yTgt[i] - yTemp[i]);
		if (diff * sign < 0)
			continue;
		int thId = ttdy[i].thId;
		assert(thId >= 0);
		assert(thId < thYc);
		double nextWei = ttdy[i].nextWei;
		if (nextWei == 0)
		{
			// участок однозначно определенного порога
			// корректируем соотв. порог
			if (diff * sign > thAdd[thId] * sign)
				thAdd[thId] = diff;
		}
		else
		{
			// участок между двумя порогами
			if (flags & 0x1)
				continue; // если запрошено, пропускаем такие участки
			// корректируем соотв. порог
			// определяем коэффициент коррекции левого порога
			double koeff = wei2koeff(1.0 - nextWei);
			if (diff * koeff * sign > thAdd[thId] * sign)
				thAdd[thId] = diff * koeff;
			// если есть правый порог - корректируем правый
			// (сейчас эта проверка лишняя)
			if (ttdy[i].nextWei == 0)
				continue;
			assert(thId + 1 < thYc);
			koeff = wei2koeff(nextWei); // коэффициент коррекции правого порога
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

	// *** сначала устанавливаем DX пороги "не менее 1"
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

	// при работе начинаем с больших DX и минимальных DY,
	// а затем уменьшаем DX и увеличиваем DY
	// Чтобы не заходить ниже начальных DX/DY, надо сохранить
	// начальное DX, но не обязательно помнить начальное DY

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

	// для ускорения работы, мы будем вычислять самую левую и самую
	// правую точки, на которую влияет каждый DX-порог
	int *ttdxMin = new int[thXc ? thXc : 1];
	int *ttdxMax = new int[thXc ? thXc : 1];
	assert(ttdxMin);
	assert(ttdxMax);

	const int maxDX = 10; // FIXME

	prf_b("extendThreshToCover: processing maxDX");

	int i;
	for (i = 0; i < thXc; i++)
	{
		thXA[i] = maxDX; // запоминаем нач. DX (предполагается их использование с maxeq с thXOrig)
		extendTHX(thXOrig[i], thXT[i], maxDX); // устанавливаем нач. DX
	}

	// формируем нач. пороги (в две итерации, чтобы поточнее)
	makeThCurve(thXT, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yTemp);
	calcThAddByThCurve(thYc, isUpper, xMin, xMax, ttdy, yTemp, yTgt, thAdd, 0x1);
	addThAddToThY(thY, thYc, thAdd);
	makeThCurve(thXT, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yTemp);
	calcThAddByThCurve(thYc, isUpper, xMin, xMax, ttdy, yTemp, yTgt, thAdd, 0x0);
	addThAddToThY(thY, thYc, thAdd);

	// строим текущую пороговую кривую
	makeThCurve(thXT, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yPrev);

	prf_b("extendThreshToCover: starting curDX loop");

	int curDX;
	for (curDX = maxDX - 1; curDX >= 0; curDX--)
	{
		// устанавливаем пробные DX
		for (i = 0; i < thXc; i++)
			extendTHX(thXOrig[i], thXT[i], curDX);

		// формируем пороговую кривую
		// (ttdx не сохраняем, т.к. нам как раз нужны те ttdx, которые соотв. предыдущему порогу)
		makeThCurve(thXT, thY, thXc, thYc, isUpper, xMin, xMax, 0, ttdy, yBase, yTemp);
		// рассчитываем ориентировочные поправки к DY
		calcThAddByThCurve(thYc, isUpper, xMin, xMax, ttdy, yTemp, yTgt, thAdd, 0x0);
		int k;
		// инициализируем таблицы минимальной и максимальной позиций, на которые влияет каждый DX-порог
		// эти таблицы используются для ускорения работы
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
				continue; // если такое уменьшение уже недопустимо, то его рассматривать бесполезно
			double S2 = 0;
			for (i = 0; i < len; i++)
			{
				if (ttdx[i].thId != k)
					continue;
				//assert ((yPrev[i] - yTemp[i]) * sign >= 0); // FIXME - debug only
				S2 += (yPrev[i] - yTemp[i]) * sign;
			}
			// ищем ориентировочные ThAdd, соответствующее DX-порогу k
			calcThAddByThCurve(thYc, isUpper, xMin, xMax, ttdy, yTemp, yTgt, thAdd, 0, ttdx, k);//, ttdxMin[k], ttdxMax[k]);
			// оцениваем изменение площади пороговой кривой при таком ThAdd
			double S1 = 0;
			for (i = 0; i < thYc; i++)
			{
				// XXX: уточнить оценку?
				// оценка основана на предположении линейного перехода Y в промежутках между порогами
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
		// новые DX-пороги сформированы в thXA.
		// обновляем пороговую кривую и ttdx
		for (i = 0; i < thXc; i++)
			extendTHX(thXOrig[i], thXT[i], thXA[i]);
		makeThCurve(thXT, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yPrev);
	}

	prf_b("extendThreshToCover: processing final DX");

	//for (i = 0; i < thXc; i++)
	//	fprintf(stderr, "Finally-1: up %d k %d (x %5d): dxOrig.dxL %2d dxOrig.dxR %2d dxNew %d\n", isUpper, i, thXOrig[i].x0, thXOrig[i].dxL, thXOrig[i].dxR, thXA[i]);

	for (i = 0; i < thXc; i++)
		extendTHX(thXOrig[i], thXOrig[i], thXA[i]);

	// формируем окончательные DY пороги (в две итерации)
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

	THX *thXTemp = new THX[thXc ? thXc : 1]; // рабочие копии DX-порогов (будут уменьшаться)
	double *thAdd = new double[thYc ? thYc : 1]; // массив с добавками для DY
	double *yCurr = new double[len]; // буфер для текущей пороговой кривой
	double *yPrev = new double[len]; // буфер для предыдущей пороговой кривой
	TTDX *ttdx = new TTDX[len]; // номера соотв. каждой точке DX-порогов (м.б. -1)
	TTDY *ttdy = new TTDY[len]; // номера соотв. каждой точке DY-порогов (в принципе, не д б -1)
	assert(thXTemp);
	assert(thAdd);
	assert(yCurr);
	assert(yPrev);
	assert(ttdx);
	assert(ttdy);

	const int maxDX = 10; // макс. / начальная ширина DX

	extendTHX(thXOrig, thXTemp, thXc, maxMX); // устанавливаем нач. DX

	calcThAdd(thXTemp, thY, thXc, thYc, isUpper, xMin, xMax, ttdy, // рассчитываем нач. DY (сразу по всем участкам)
		yBase, yCurr, yTgt, thAdd, 0);
	addThAddToThY(thY, thYc, thAdd); // устанавливаем нач. DY

	// формируем пороговую кривую для нач. (DX,DY)
	makeThCurve(thXTemp, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yPrev);

	// пробуем уменьшать DX
	int curDX;
	for (curDX = maxDX - 1; curDX >= 0; curDX--)
	{
		extendTHX(thXOrig, thXTemp, thXc, maxMX); // формируем DX пороги шириной max(orig,cur)
		makeThCurve(thXTemp, thY, thXc, thYc, isUpper, xMin, xMax, ttdx, ttdy, yBase, yCurr);
		...
	}
	// корректируем пороги согласно превышению на участках с однозначно определенным DY-порогом
	// (похоже, это не столь уж и необходимо)
	calcThAdd(thX, thY, thXc, thYc, isUpper, xMin, xMax, ttdy,
		yBase, yTemp, yTgt, thAdd, 1);
	addThAddToThY(thY, thYc, thAdd);

	// корректируем пороги согласно превышению на всех участках
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
  defP = by-DXc,DY; // отклонение; вероятно, сейчас нули

  while (delta = 9; delta >= 0; delta--)
  {
     // ? - assert(defP[each i] == 0)
     DXt = min(DXi, delta);
	 defT = by-DXt,DY; // отклонение - возможно, больше нуля
	 foreach(i) if (defT[i] > defP[i])
	 {
	    nDX = ttdx_by_DXc[i];
		if (nDX == -1)
		{
		 assert(0); // не знаем что это значит
		 continue;
		}
	 }



  }
*/