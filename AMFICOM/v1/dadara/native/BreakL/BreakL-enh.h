#ifndef _BREAKL_ENH_H
#define _BREAKL_ENH_H

#include "../Common/ModelF.h"
#include "../JNI/ThreshArray.h"

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

#endif

