#ifndef _BREAKL_ENH_H
#define _BREAKL_ENH_H

#include "../Common/ModelF.h"
#include "../JNI/ThreshArray.h"

// "�����������" (��� ����������� ������) BreakL
// �������������� ������ BreakL � �� ����� �������� �������������� X-������������
void BreakL_Enh (ModelF &mf, int x0, int x1, int width, int isUpper);

void BreakL_ChangeByThresh (ModelF &mf, ThreshArray &ta, int key);
void BreakL_FixThresh (ModelF &mf, ThreshArray &ta);
//void BreakL_ChangeOther0 (ModelF &mf, int npars, double *pars);

#endif

