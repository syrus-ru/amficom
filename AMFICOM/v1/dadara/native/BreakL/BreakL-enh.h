#ifndef _BREAKL_ENH_H
#define _BREAKL_ENH_H

#include "../Common/ModelF.h"
#include "../JNI/ThreshArray.h"

// �� ������������ - ������� � ����� ACXL-�������
// "�����������" (��� ����������� ������) BreakL
// �������������� ������ BreakL � �� ����� �������� �������������� X-������������
void BreakL_Enh (ModelF &mf, int x0, int x1, int width, int isUpper);

// ������������ � ThreshDX/ThreshDY ��������.
// ����������, ����� �����������, ����� ������ ������������ �������� � ��� ��� ���� �����
// � �������� �������������� ����� ������ �� ���������� ����� � ���� ������.
// thCheckPosition: x-����������, ��� ������� ���� �������� ����� �������������� DX/DY-������
// wannaDXDY: 0: ������ �� ����, 1: ����� DX-�����, 2: ����� DY-�����
// ������������ �������� ����� ���� -1 (�� �������� ���� �� ������), ���� ��������� ������� � ������� taX ��� taY
int BreakL_ChangeByThresh (ModelF &mf, ThreshDXArray &taDX, ThreshDYArray &taDY, int key, int thCheckPosition, int wannaDXDY);
//void BreakL_FixThresh (ModelF &mf, ThreshArray &ta);

#endif

