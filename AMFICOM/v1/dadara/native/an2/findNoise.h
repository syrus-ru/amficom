/*
 * findNoise.h
 */

#ifndef _findNoise_h
#define _findNoise_h

/*
 * findNoiseArray()
 *
 * ������ ������ ������ ���� ��������������.
 * ������� ����� �������� �������� Nettest, ��� �������
 * ������ (GS?) ���������� �����, �������� ��� ����
 * ������� ����. ��� ���� �����������, ��� �����
 * �� ����� ��������� GS ����������� ������� ���� ���
 * ����������� ���� ����, ��� ���������� ��
 * findNoise3s.
 *
 * �� ������� �/� � ����������� ������ ����� ��������
 * ����������� �������� data[i+8]+data[i-8]-data[i]*2
 * � ������������ ��������� �������� ���� �������� ��������.
 * �� ��������� ���� �������� � ���������� ����������
 * ����� �/� � ���� �� �������, �������� ���������������
 * �������� ���. ������ ����. ��� �������� ����� ����
 * �������� �� �������� �������.
 * � ���� �� ������ ��� ������ ����������� �� ������ �
 * ����� �/�, �� ������� �� ����� ���� ���������� ��������� ��������.
 * ����� �������� "������������ �������" � �����������
 * ���������������� ������ ����, ����� ���������� ���������
 * ������ �������� ������ ���� � NetTest GS, �� ������������
 * ��������� ���������� ���� � ������� �������.
 * �������������� ������ ����������� � �������������
 * ��������� � �������� �� �����.
 *
 * �� ����� - �������������� (����).
 * �� ������ - ������ ���. ������� ���� (��) �� ������ 1 �����,
 * ������������� � ��������� �� ����� 0.001 �� �� 20 ��.
 *
 * size ������ ���� ���������� �������.... -- FIXME
 * data - ������� ������[size]
 * len2 - ����� �� ����������� ����� �/�
 * out - ������[len2] ��� ������ ���. �������� ����
 */
void findNoiseArray(double *data, double *outNoise, int size, int len2);

/*
 * �� ��, ��� � findNoiseArray, �� �� ������ �� ������������� ���������,
 * � ����������.
 */
void findAbsNoiseArray(double *data, double *outNoise, int size, int len2);

void findBothNoiseArrays(double *data, int size, int len2, double *outNoiseAbs, double *outNoiseRel);

#endif