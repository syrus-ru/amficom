#include <assert.h>
#include "BreakL-enh.h"
#include "BreakL-fit.h"
#include "BreakL-mf.h"
#include "../common/prf.h"
#include "../common/ModelF.h"
#include "../common/ArrList.h"

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

// "размазывает" (для образования порога) BreakL
// поддерживается только BreakL с не очень большими целочисленными X-координатами
// используется только при ACXL-преобразовании
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

	// заполняем врем. массив
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

	// формируем порог
	enhance(tmp, len, width, isUpper);

	// преобразуем к ломаной
	BreakL_FitI (mf, tmp, width, x0, len - width);

	//fprintf(stderr, "BreakL_Enh: done, input N %d x0 %d x1 %d len %d, output N = %d\n",
	//	N, x0, x1, len, mf.getNPars() / 2);
	//fflush(stderr);

	delete[] tmp;

	prf_b("BreakL_Enh: leave");
}


double *BreakLToArray(double *pars, int npars, int x0, int N) // N > 0, но [x0..x0+N) не обязаны лежать в пределах ООФ
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

// обе mf должны быть типа BreakL
// области определения mf должны пересекаться хотя бы в одной точке
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
		return; // кривые не пересекаются - ошибка вызова

	// результирующая ломаная должна состоять из:
	// (1) если i0 > 0, то узлов this[0] .. [i0-1]
	// (2) если i0 > 0 и xMin != this.x[0], то узла (x=xMin, y=this(xMin))
	// (3) всех узлов that[0] .. [thatN-1]
	// (4) если i1 < thisN, то узла (x=xMax, y=this(xMax))
	// (5) если i1 < thisN, то узлов this[i1..thisN-1]

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

	// сохраняем результаты
	mf.init(MF_ID_BREAKL, k * 2, out);
}

void BreakLSetRegionFromArray(ModelF &mf, int x0, int N, double *yarr)
{
	ModelF that;
	BreakLFromArray(that, x0, N, yarr);
	BreakLReplaceRegion(mf, that);
}

// 1) the caller should delete 'upd' by himself
// 2) return value: 1 if this threshold is responsible for dxCheckPosition, 0 if not
int BreakLUpdateRegionFromArray(ModelF &mf, int x0, int N, double *upd, int isUpper, int dxCheckPosition)
{
	double *data = BreakLToArray(mf.getP(), mf.getNPars(), x0, N);
	int i;

	int rc = 0;
	if (isUpper)
	{
		for (i = 0; i < N; i++)
			if (upd[i] > data[i])
			{
				data[i] = upd[i];
				if (i == dxCheckPosition - x0)
					rc = 1;
			}
	}
	else
	{
		for (i = 0; i < N; i++)
			if (upd[i] < data[i])
			{
				data[i] = upd[i];
				if (i == dxCheckPosition - x0)
					rc = 1;
			}
	}

	BreakLSetRegionFromArray(mf, x0, N, data);
	delete[] data;

	return rc;
}

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

// добавляет узлы в ломаную, не делая повторных узлов и не расширяя ее ООФ
// nodeList должно быть монотонно
// Разрушает nodeList, а затем удаляет его
void BreakL_AddInternalNodesFromTempList(ModelF &mf, int listSize, int *nodeList)
{
	int i;
	int toAdd = 0;
	int Np = mf.getNPars() / 2;
	if (Np == 0)
		return;

	double *pars = mf.getP();
	int p; // индекс добавляемого узла
	for (i = 0, p = 0; i < listSize; i++)
	{
		int pos = nodeList[i];

		if (i != 0 && pos == nodeList[i - 1])
			continue;
		int k = f_BREAKL_GETPOS(Np, pars, pos, -1);
		if (k == 0)
			continue; // точка слева он начала ломаной
		if (k == Np)
			continue; // точка справа от конца ломаной либо совпадает с последним узлом
		int xk = (int )pars[k * 2 - 2];
		if (pos <= xk)
			continue; // узел уже есть

		if (p != 0)
		{
			//fprintf(stderr, "i %d p %d pos %d nl[p-1] %d\n",
			//	i, p, pos, nodeList[p - 1]);
			//fflush(stderr);
			assert(pos > nodeList[p - 1]); // проверяем монотонность

			if (pos <= nodeList[p - 1])
				continue; // игнорируем немонотонные входные узлы
		}
		// записываем узел в список на добавление
		nodeList[p++] = pos;
	}

	if (p == 0)
		return;

	// создаем новый массив параметров
	int newNp = Np + p;
	double *newPars = new double[newNp * 2];
	assert(newPars);

	int q; // номер в списке добавляемых точек
	// i - номер в списке входных узлов
	int w; // номер в списке выходных узлов
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

	assert(i == Np); // XXX - доказать в алгоритме

	//fprintf(stderr, "BreakL_AddInternalNodesFromTempList: p %d Np %d newNp %d\n", p, Np, newNp); fflush(stderr);

	// заменяем параметры
	mf.init(MF_ID_BREAKL, newNp * 2, newPars);

	delete[] nodeList;
}

struct THX
{
	int x0;
	int x1;
	int dxL;
	int dxR;
	int leftMode;
};

struct THY
{
	int x0;
	int x1;
	double dy;
	int typeL;
};

struct UPDATE_REGION
{
	int x0;
	int N;
	int dxthid;
	double *data;
	UPDATE_REGION(int x0, int N, int dxthid, double *data)
	{
		this->x0 = x0;
		this->N = N;
		this->data = data;
		this->dxthid = dxthid;
	}
};

// Основное назначение функции соответствует названию - преобразовать м.ф. к ее порогу.
// Вспомогательное назначение - поиск соответствующего координате DX-порога dXCheckPosition.
// параметр dXCheckPosition: x-координата, для которой надо провести поиск ответственного DX-порога
// возвращаемое значение будет либо -1, либо найденным номером в массиве taX
int BreakL_ChangeByThresh (ModelF &mf, ThreshDXArray &taX, ThreshDYArray &taY, int key, int dXCheckPosition)
{
	prf_b("BreakL_ChangeByThresh: entered");

	const int thNpX = taX.getLength();
	const int thNpY = taY.getLength();

	int conjKey = taX.getConjKey(key);
	int isUpper = taX.isUpper(key);

	// convert thresholds
	THX *thX = 0;
	if (thNpX > 0)
	{
		thX = new THX[thNpX];
		assert(thX);
	}
	THY *thY = 0;
	if (thNpY > 0)
	{
		thY = new THY[thNpY];
		assert(thY);
	}
	int j;
	for (j = 0; j < thNpX; j++)
	{
		int isRising = taX.getIsRise(j);
		int leftMode = !!isRising ^ !!isUpper ^ 1;
		thX[j].leftMode = leftMode;
		thX[j].x0 = taX.getX0(j);
		thX[j].x1 = taX.getX1(j);
		thX[j].dxL = leftMode ? -taX.getDX(j, key) : taX.getDX(j, conjKey);
		thX[j].dxR = leftMode ? -taX.getDX(j, conjKey) : taX.getDX(j, key);
	}
	for (j = 0; j < thNpY; j++)
	{
		thY[j].x0 = taY.getX0(j);
		thY[j].x1 = taY.getX1(j);
		thY[j].dy = taY.getValue(j, key);
		thY[j].typeL = taY.getTypeL(j);
	}

	prf_b("BreakL_ChangeByThresh: add nodes");

	// добавляем узлы в точках начала и конца всех событий
	// это изменяет mf
	// XXX: предполагается, что у каждого X-порога есть два смежных Y-порога,
	// поэтому достаточно расставить точки только Y-порогов
	{
		int *nodeList = new int[thNpY * 2];
		assert(nodeList);
		// NB: список не обязан быть без повторений, но должен быть монотонным
		for (j = 0; j < thNpY; j++)
		{
			nodeList[2 * j] = thY[j].x0;
			nodeList[2 * j + 1] = thY[j].x1;
		}
		BreakL_AddInternalNodesFromTempList(mf, thNpY * 2, nodeList);
	}

	prf_b("BreakL_ChangeByThresh: process AL threshs");

	// process A and L threshs
	{
		XY *mfpars = (XY* )mf.getP();
		int mfNp = mf.getNPars() / 2;

		int curMn; // номер текущего узла mf
		int curLTn; // номер текущего порога либо левого из текущих

		// для переходов A->L, L->A:
		double Yx1L = 1; // не инициализируем, т.к. крайние события не м б типа L
		double Yx0R = 0; // (инициализируем только для успокоения компилятора)

		for (curMn = 0, curLTn = -1; curMn < mfNp; curMn++)
		{
			int curX = (int )mfpars[curMn].x;

			// fix curLTn; keep Yx1_prev and Yx1_cur
			while (curLTn < 0 || curLTn < thNpY - 1 && curX >= thY[curLTn + 1].x0)
			{
				curLTn++;
				Yx1L = mf.calcFun(thY[curLTn].x1);
				if (curLTn < thNpY - 1)
					Yx0R = mf.calcFun(thY[curLTn + 1].x0);
				else
					Yx0R = 0;
				if (Yx1L == Yx0R)
					Yx1L++; // защита от div by zero
			}

			// нашли последний порог curLTn, начинающийся левее-либо-там-же от точки curX

			// пространство внутри порога
			if (curX <= thY[curLTn].x1 || curLTn == thNpY - 1)
			{
				mfpars[curMn].y += thY[curLTn].dy;
				continue;
			}

			// пространство между порогами

			int xL = thY[curLTn].x1;
			int xR = thY[curLTn + 1].x0;

			double dyL = thY[curLTn].dy;
			double dyR = thY[curLTn + 1].dy;

			int typeL = thY[curLTn].typeL;
			int typeR = thY[curLTn + 1].typeL;

			double wei;

			// для A-типа - линейный переход;
			// A-тип и L-тип - L-переход;
			// два порога L-типа не могут быть рядом.
			if (typeL || typeR)
				wei = (mfpars[curMn].y - Yx1L) / (Yx0R - Yx1L);
			else
				wei = (double )(curX - xL) / (xR - xL);

			//fprintf(stderr, "AL threshs: LTn %d Mn %d curX %d xL %d xR %d dyL %g dyR %g tL %d tR %d wei %g\n",
			//	curLTn, curMn, curX, xL, xR, dyL, dyR, typeL, typeR, wei);

			mfpars[curMn].y += dyL + (dyR - dyL) * wei;
		}
	}

	prf_b("BreakL_ChangeByThresh: process DXLR threshd");

	int rcDXID = -1;

	// apply dx-thresholds
	{
		ArrList updateRegions;
		int curT;
		for (curT = 0; curT < thNpX; curT++)
		{
			int dxL = thX[curT].dxL;
			int dxR = thX[curT].dxR;

			if (dxL < 0)
				dxL = 0;
			if (dxR < 0)
				dxR = 0;

			if (dxL == 0 && dxR == 0)
				continue;

			int xBegin = thX[curT].x0;
			int xEnd = thX[curT].x1;

			int nBase0 = xEnd - xBegin + 1;
			//fprintf(stderr, "Applying dxL/dxR thresholds: curT %d dxL %d dxR %d, nBase %d; leftMode %d upper %d\n",
			//	curT, dxL, dxR, nBase0, thX[curT].leftMode, isUpper);

			if (nBase0 <= 0)
				continue;

			int W = dxL + dxR;

			int nEnh1 = nBase0 + W;
			int nEnh2 = nBase0 + W * 2; // "'2': Double-width enh"

			// получаем событие, которое надо расширить, на ширине nBase0 + 2W
			// расширять будем только на nBase0 + W, а остальное понадобится для контроля
			// пересечения с исходной кривой
			// NB: mf.getP() и mf.getNPars() могут возвращать разные значения в разные итерации,
			// т.к. мы изменяем длину mf в конце итерации
			double *arr2io = BreakLToArray(mf.getP(), mf.getNPars(), xBegin - dxL * 2, nEnh2);

			// расширяем nBase0->nEnh2, забивая справа гориз. прямой; исходный вар-т не меняем
			double *arr1t = new double[nEnh1];
			assert(arr1t);
			for (j = 0; j < nBase0; j++)
				arr1t[j] = arr2io[j + dxL * 2];
			for (j = nBase0; j < nEnh1; j++)
				arr1t[j] = arr1t[nBase0 - 1];

			// собственно расширяем
			enhance(arr1t, nEnh1, W, isUpper);

			// сглаживаем стык
			{
				double sign = isUpper ? 1.0 : -1.0;
				int leftMode = thX[curT].leftMode;
				int x0 = leftMode ? 0 : dxL;
				int x1 = leftMode ? nBase0 + dxL : nEnh1;
				for (j = x0; j < x1; j++)
				{
						arr2io[dxL + j] = arr1t[j];
				}
				if (leftMode)
				{
					double y0 = arr1t[0];
					double y1 = arr2io[0];
					if (y1 * sign > y0 * sign) // XXX: в некоторых случаях лучше продолжить горизонтально до пересечения
						y1 = y0;
					for (j = 1; j < dxL; j++)
					{
						double yt = y0 + (y1 - y0) * (double )j / dxL;
						if (arr2io[dxL - j] * sign < yt * sign)
							arr2io[dxL - j] = yt;
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
							arr2io[nEnh2 - 1 - dxR + j] = yt;
						else
							break;
					}
				}
			}

			// планируем сохранение в mf
			UPDATE_REGION *ur = new UPDATE_REGION(xBegin - dxL * 2, nEnh2, curT, arr2io);
			assert(ur);
			updateRegions.add(ur);
			delete[] arr1t;
			//fprintf(stderr, "#6\n"); fflush(stderr);
		}
		// сохраняем в mf запланированные изменения
		prf_b("BreakL_ChangeByThresh: committing DX thresholds");
		int i;
		for (i = 0; i < updateRegions.getLength(); i++)
		{
			UPDATE_REGION *urp = (UPDATE_REGION *)updateRegions[i];
			if (BreakLUpdateRegionFromArray(mf, urp->x0, urp->N, urp->data, isUpper, dXCheckPosition))
				rcDXID = urp->dxthid;
			delete[] urp->data;
			delete urp;
		}
	}

	prf_b("BreakL_ChangeByThresh: done");

	//fflush(stderr);

	if (thNpX > 0)
		delete[] thX;
	if (thNpY > 0)
		delete[] thY;

	return rcDXID;
}
