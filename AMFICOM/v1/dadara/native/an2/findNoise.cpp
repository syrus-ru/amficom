/*
 * findNoise.cpp
 *
 * ����������� ������ ������ ���� ��������������, ���������
 * ������ "������� ���� �� ������� �� �������".
 * TODO: ��������� ��������� ��� ��������������� ������ �������� �����.
 *
 * �������������� ������������������� ��-�����.
 * ��� ������� �����������:
 * - ����������� ����-����������� �� 8 ������ (NetTest)
 * - ����-���������� �� 0.001 ��
 * - ������� ������� �� �/� (����������� ��������-���������� �������)
 *
 * �� ����� - �������������� (����).
 * �� ������ - ������� ���� (��) �� ������ 3 �����
 */

#include <math.h>
#include <assert.h>
#include <stdlib.h>
#include "findNoise.h"

// ��� ������������ ��������, ��� �������� ������������ 0.001��-��������������
const double prec0 = 0.001 * 1.5;

static int dfcmp(const void *a, const void *b)
{
	const double *x = (const double *)a;
	const double *y = (const double *)b;
	return *x <= *y ? *x < *y ? -1 : 0 : 1;
}

static double log2add(double v)
{
	return exp(v * log(10.0) / 5.0);
}
static double add2log(double v)
{
	return 5.0 * log10(v);
}

double findNoise3s(double *data, int size)
{
	if (size <= 0)
		return 0;

	int i;
	double *out = new double[size];
	assert(out);

	const int width = 8; // XXX: NetTest specific - ������, �� ������� �������� ����������

	int k;
	for (i = 0, k = 0; i < size - width * 2; i++)
	{
		double a = data[i];
		double b = data[i + width];
		double c = data[i + width * 2];

		// ��������� ��� �������� ������ ����� ��������� ���������
		if (a == c) continue;

		// ����������� ������ �/�, ��� ��� ������ ��� ��� 0.001�� �����������
		if (fabs(a - b) < prec0 || fabs(b - c) < prec0)
			continue;

		// ����������� ��������� ������� - ����� ��������� �������
		// note: ��� �������� ������ ������ ����� 2b-a-c
		if (((a < b) ^ (b < c)) == 0)
			continue;

		// ��������� � �������� �������������
		a = log2add(a);
		b = log2add(b);
		c = log2add(c);

		// ���������� ���������� �� ���������� ��� ������ ����
		double dif = fabs(2 * b - a - c);

		out[k++] = dif;
	}

	const int s2 = k;

	//fprintf(stderr, "size %d s2 %d (%.2f%%)\n", size, s2, s2 * 100.0 / size);

	// ������ ������������ �����������
	qsort(out, s2, sizeof(double), dfcmp);

	//for (i = 0; i < s2; i++)
	//	printf("%g %g\n", i * xmax / s2, add2log(out[i]));

	// ���������� ������� 3 �����
	double nlev = add2log(out[s2 / 2] * 1.3);

	delete out;

	return nlev;
}
