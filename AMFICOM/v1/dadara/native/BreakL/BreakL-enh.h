#ifndef _BREAKL_ENH_H
#define _BREAKL_ENH_H

#include "../Common/ModelF.h"
#include "../JNI/ThreshArray.h"

// 1: умеем отслеживать перемещение DX&DY-порогов
// 0: не умеем (при этом ничего не работает)
// этот define нужен лишь для того, чтобы пометить те части кода, которые
// отслеживают ttdx/ttdy.
#define CANTTDXDY 1

#if CANTTDXDY
struct TTDX
{
	int thId; // номер порога либо -1 если порог не повлиял
	void set(int thId);
	int get();
	//void operator= (TTDX &that); // use default
};

struct TTDY
{
	int thId; // номер левого порога (-1: отчего-то не определено)
	double nextWei; // вес соседа справа (должен быть строго 0 для самого правого порога)
	void set(int thId, double nextWei);
	int getNearest();
	//void operator= (TTDY &that); // use default
};
#endif

// не используется - остался в эпохе ACXL-порогов
// "размазывает" (для образования порога) BreakL
// поддерживается только BreakL с не очень большими целочисленными X-координатами
void BreakL_Enh (ModelF &mf, int x0, int x1, int width, int isUpper);

// используется с ThreshDX/ThreshDY порогами.
// Размазывая, умеет отслеживать, какие пороги сформировали значение в той или иной точке
// и выдавать сответствующие номер порога по координате точки и типу порога.
// thCheckPosition: x-координата, для которой надо провести поиск ответственного DX/DY-порога
// wannaDXDY: 0: ничего не надо, 1: нужен DX-порог, 2: нужен DY-порог
// возвращаемое значение будет либо -1 (не запрошен либо не найден), либо найденным номером в массиве taX или taY
int BreakL_ChangeByThresh (ModelF &mf, ThreshDXArray &taDX, ThreshDYArray &taDY, int key, int thCheckPosition, int wannaDXDY);
//void BreakL_FixThresh (ModelF &mf, ThreshArray &ta);

#if CANTTDXDY

// Главный метод ThreshDX/DY преобразования.
// autoThresh - режим без сглаживания краев DX-порогов.
// В режиме autoThresh возможно полное отслеживание порогов запрошенных типов:
// ttdxOut != 0: ThreshDX,
// ttdyOut != 0: ThreshDY.
// Без autoThresh пороги отслеживаются не полностью.

// для mf
void ChangeBreakLByThreshEx (ModelF &mf, ThreshDXArray &taX, ThreshDYArray &taY, int key,
			   int xMin, int xMax, int autoThresh, TTDX *ttdxOut, TTDY *ttdyOut);

// для массива
// yArr[0] ~ xMin; yArr[xMax - xMin] ~ xMax
void ChangeArrayByThreshEx (double *yArr, THX *thX, THY *thY, int thNpX, int thNpY, int isUpper,
						   int xMin, int xMax, int autoThresh,
						   TTDX *ttdxOut, TTDY *ttdyOut);

#endif //CANTTDXDY

#endif
