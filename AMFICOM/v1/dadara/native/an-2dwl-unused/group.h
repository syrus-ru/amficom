#ifndef _group_h
#define _group_h

#include "ArrList.h"

struct InEvent;

// ��������� ������� �� ���������� ������� �� �����
// ������ �����, ���������� ������ ���� �� ����������
// �� ������ ��������� �� ���������� - ���� �������������
void group_events(InEvent *ie, int len, ArrList &outEvents);

#endif

