#ifndef _BREAKL_ENH_H
#define _BREAKL_ENH_H

#include "../Common/ModelF.h"
#include "../JNI/ThreshArray.h"

// 1: ����� ����������� ����������� DX&DY-�������
// 0: �� ����� (��� ���� ������ �� ��������)
// ���� define ����� ���� ��� ����, ����� �������� �� ����� ����, �������
// ����������� ttdx/ttdy.
#define CANTTDXDY 1

#if CANTTDXDY
struct TTDX
{
	int thId; // ����� ������ ���� -1 ���� ����� �� �������
	void set(int thId);
	int get();
	//void operator= (TTDX &that); // use default
};

struct TTDY
{
	int thId; // ����� ������ ������ (-1: ������-�� �� ����������)
	double nextWei; // ��� ������ ������ (������ ���� ������ 0 ��� ������ ������� ������)
	void set(int thId, double nextWei);
	int getNearest();
	//void operator= (TTDY &that); // use default
};
#endif

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

#if CANTTDXDY

// ������� ����� ThreshDX/DY ��������������.
// autoThresh - ����� ��� ����������� ����� DX-�������.
// � ������ autoThresh �������� ������ ������������ ������� ����������� �����:
// ttdxOut != 0: ThreshDX,
// ttdyOut != 0: ThreshDY.
// ��� autoThresh ������ ������������� �� ���������.

// ��� mf
void ChangeBreakLByThreshEx (ModelF &mf, ThreshDXArray &taX, ThreshDYArray &taY, int key,
			   int xMin, int xMax, int autoThresh, TTDX *ttdxOut, TTDY *ttdyOut);

// ��� �������
// yArr[0] ~ xMin; yArr[xMax - xMin] ~ xMax
void ChangeArrayByThreshEx (double *yArr, THX *thX, THY *thY, int thNpX, int thNpY, int isUpper,
						   int xMin, int xMax, int autoThresh,
						   TTDX *ttdxOut, TTDY *ttdyOut);

#endif //CANTTDXDY

#endif
