#include <assert.h>
#include "findLength.h"
#include "findNoise.h"
//#include <stdio.h> // debug: fprintf, stderr

const int TREAT_ONLY_DUPLICATE_ZEROES = 0;
const double DELTAV = 0.1; // ������������� ��������, ����� ���������� � ����
const double DELTANOISE = 0; // ����� �������������� ����������� ��������

// ���������� ����� �������������� �� ������ ���� (�.�. �� ����� �/������)
// ���� ������ �� ����� �������, ������ ������� data_length
// data - ������� �/�
// data_length - �� �����
// return: �������� >=0, <=data; �� ����������� �� ����.

// ������ ����� - �� ���������� ���. ���� (�� ������������ � 2005-09-10)
int findReflectogramLength0(double *data, int len)
{
	if (len <= 0)
		return 0;

	int width = TREAT_ONLY_DUPLICATE_ZEROES ? 1 : 0;

	// ���� ����������� �������� �� �/�
	double vmin = data[0];
	int i;
	for (i = 0; i < len - width; i++)
		if (vmin > data[i])
			vmin = data[i];

	// ���� ����� ������� X-�������� ����� ������������ ����������
	int lmax = 0; // ����� ����������� ���������
	int xmax = 0; // ��������� ����� ����������� ���������
	int lastX = 0; // ������ �������� ���������
	for (i = 0; i < len - width; i++)
	{
		if (data[i] > vmin + DELTAV)
			continue;

		if (width)
		{
			if (data[i + 1] > vmin + DELTAV)
				continue;
		}
		// ������ ����
		int lcur = i - lastX;
		int xcur = i;
		if (lmax < lcur)
		{
			lmax = lcur;
			xmax = xcur;
		}
		lastX = i;
	}

	// ��������� = ������ ���� ������ �������� ��������� ����� ������������ ����������

	if (xmax)
		return xmax; // ��������� ������
	else
		return len; // ��������� �� ������? //XXX: �������� �� ���?
}

// ������ ����� (��������� 2005-09-10) - �� ������������� ��������� ���� ���. ��������
int findReflectogramLength1(double *data, int len)
{

	// ���� ������������ �������� �� �/�
	int imax = 0;
	int i;
	for (i = 0; i < len; i++)
		if (data[imax] < data[i])
			imax = i;

	// ���� ����������� �������� ������ �� ���������
	double vmin = data[imax];
	for (i = imax; i < len; i++)
		if (vmin > data[i])
			vmin = data[i];

	// �������� ���������� �� ���������� ���. ����.

	const int WIDTH = 20;
	const int THRESH = 5;
	for (i = imax; i < len - WIDTH; i++) {
		int j;
		int c;
		if (data[i] >= vmin + DELTAV)
			continue;
		for (j = 0, c = 0; j < WIDTH; j++) {
			if (data[i + j] < vmin + DELTAV)
				c++;
		}
		if (c > THRESH)
			return i;
	}
	return len;

}

// ������ ����� (�� 2005-09-10) - �� ������������� ��������� ���� ������ ����.
// ������� ����� ������ ����������� ����������, ��� ��������� ����� ������
// ������ ������������ ������������ ������� �������� � ������� ����.
// ��� ����� ��� �����������, ��� ������ ����� �������� "����� �������"
// ��� ������� ����� �/� � ���.
int findReflectogramLength(double *data, int len)
{

	// ���� ������������ �������� �� �/�
	int imax = 0;
	int i;
	for (i = 0; i < len; i++)
		if (data[imax] < data[i])
			imax = i;

	// �������� ���������� ����� � ������� ����
	// XXX: performance: ������������ ��� ������ ��� ����. ���� ����� ��� �����.
	// note: performance: ������ �����, ������� ����� (������) ����������� ������� ������ ���� ����� �� �����. ��� ������ ����������.
	double *noise = new double[len];
	assert(noise);
	findAbsNoiseArray(data, noise, len, len);

	// XXX: debug
	//FILE *f = 0;
	//f = fopen("noiseL.dat", "w"); assert(f);
	//for (i = 0; i < len; i++) {
	//	if (f) fprintf(f, "%d %g %g %g\n", i, data[i], noise[i]);
	//}
	//if (f) fclose(f);

	int ret = len;
	const int WIDTH = 20;
	const int THRESH = 4; // ����� �������������� ����������� ��������
	for (i = imax; i < len - WIDTH; i++) {
		int j;
		int c;
		if (data[i] >= noise[i] + DELTANOISE)
			continue;
		for (j = 0, c = 0; j < WIDTH; j++) {
			if (data[i + j] < noise[i] + DELTANOISE)
				c++;
		}
		if (c > THRESH) {
			ret = i;
			break;
		}
	}

	delete[] noise;
	return ret;
}
