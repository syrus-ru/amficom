#ifndef _BREAKL_ENH_H
#define _BREAKL_ENH_H

#include "../Common/ModelF.h"
#include "../JNI/ThreshArray.h"

// "размазывает" (для образования порога) BreakL
// поддерживается только BreakL с не очень большими целочисленными X-координатами
void BreakL_Enh (ModelF &mf, int x0, int x1, int width, int isUpper);

void BreakL_ChangeByThresh (ModelF &mf, ThreshArray &ta, int key);
void BreakL_FixThresh (ModelF &mf, ThreshArray &ta);
//void BreakL_ChangeOther0 (ModelF &mf, int npars, double *pars);

#endif

