#ifndef _BREAKL_ENH_H
#define _BREAKL_ENH_H

#include "../Common/ModelF.h"
#include "../JNI/ThreshArray.h"

// "�����������" (��� ����������� ������) BreakL
// �������������� ������ BreakL � �� ����� �������� �������������� X-������������
void BreakL_Enh (ModelF &mf, int x0, int x1, int width, int isUpper);

void BreakL_ChangeByThresh (ModelF &mf, ThreshDXArray &taDX, ThreshDYArray &taDY, int key);
//void BreakL_FixThresh (ModelF &mf, ThreshArray &ta);

#endif

