#ifndef _pack_h
#define _pack_h

#include "byteStream.h"

// �������������� signed long � long-������, � �������� ���� ��������� � ������� ��� (��� ���������� ��������)
long pk_ls2u(long v);
long pk_lu2s(long x);

// �������� ������������� long - �������� ������ 255 ��� ����, �������� >=255 - ��� 0xff � long
void pk_writeLongPlusInc(long delta, byteOut &bout);
long pk_readLongPlusInc(byteIn &bin);

// ��������� ��������� signed long �����: � 1..5 ����
void pk_writeLongM3(long delta, byteOut &bout);
long pk_readLongM3(byteIn &bin);

#endif
