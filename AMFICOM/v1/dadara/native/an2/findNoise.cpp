/*
 * findNoise.cpp
 */

/*
 * findNoise3s()
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

#include "../common/median.h"
#include "../common/prf.h"

#include <stdio.h>

// ��� ������������ ��������, ��� �������� ������������ 0.001��-��������������
const double prec0 = 0.001 * 1.5;

// ��������� ������� ���� �� ��������� � ����. ������ �������, ��
const double MAX_VALUE_TO_INITIAL_DB_NOISE = -10; // -5..-10..-15

// ������ ������ ����������� ������ ���� NetTest'��
const int NETTESTWIDTH = 16;

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

	delete[] out;

	return nlev;
}

static double dy2dB(double y0, double dy)
{
	return y0 + dy + 5.0 * log10(1.0 - pow(10.0, -dy / 5.0));
}

static double dB2dy(double y0, double dB)
{
	double ret = 5.0 * log10(1.0 + pow(10.0, (dB - y0) / 5.0));
	if (ret > 20.0) // XXX
		ret = 20.0;
	return ret;
}

/*
 * findNoiseArray()
 * ����������� ������ ���� � ����������� �� ����������
 * � ������ ����������� ���������� ������ ���� ��
 * �/�
 * ���. �������� - � ���. ��, �� ��. ~1(?) �����
 */
void findNoiseArray(double *data, double *out, int size)
{
	assert(size > 0);
	double *temp = new double[size]; // ����� �������� ����� ���������� �/�
	assert(temp);

	prf_b("findNoiseArray: enter");

	const int width = NETTESTWIDTH;
	// mlen ������ ���������� ������
	const int mlen = width * 10;
	// -1 ����� ��� ������������ x-�����.
	const int nsam = mlen - 2 * width - 1;
	int mofs = mlen / 2 - 1;
	double gist[nsam];

	prf_b("findNoiseArray: #1");

	assert(size > mlen); // XXX
	int i;
	for (i = 0; i < size - mlen; i++)
	{
		int j;
		// ���������� ������� ����
		for (j = 0; j < nsam; j ++)
		{
			double v0 = data[i + j];
			double v1 = data[i + j + width];
			double v2 = data[i + j + width * 2];
			gist[j] = fabs(v2 + v0 - v1 - v1);
		}
		double dv = destroyAndGetMedian(gist, nsam, nsam / 2) + 0.001; // XXX: 0.001

		// ���������� ������� �������� ������
		for (j = 0; j < nsam; j++)
			gist[j] = data[i + j + width];
		double y0 = destroyAndGetMedian(gist, nsam, nsam / 2);
		temp[i + mofs] = y0;

		//int io = i + mofs;
		//fprintf(stdout, "%d %g %g\n", io, dv, dy2dB(ya[io], dv));
		out[i + mofs] = dy2dB(y0, dv);
	}
	prf_b("findNoiseArray: #2");

    // ��������� �� ����� �������
	for (i = 0; i < mofs; i++)
	{
		out[i] = out[mofs];
		temp[i] = temp[mofs];
	}
	for (i = size - mlen + mofs; i < size; i++)
	{
		out[i] = out[size - mlen + mofs - 1];
		temp[i] = temp[size - mlen + mofs - 1];
	}

    // ������ �������� �� ������ ��������������
    {
		// ���� ���. ����. ����������� �/�
        double vMax = temp[0];
        for (i = 0; i < size; i++)
        {
        	if (vMax < temp[i])
            	vMax = temp[i];
        }
        // ���������� ��������� ������� ����
        if (out[0] > vMax + MAX_VALUE_TO_INITIAL_DB_NOISE)
        	out[0] = vMax + MAX_VALUE_TO_INITIAL_DB_NOISE;
    }

	// ������ ������ ������������� ��������
	for (i = 1; i < size; i++)
	{
		if (out[i] > out[i - 1])
			out[i] = out[i - 1];
		//fprintf(stdout,"%d %g\n", i, ya[i] - out[i]);
		//fprintf(stdout,"%d %g\n", i, out[i]);
	}

	// ��������� �������� ������
	for (i = 0; i < size; i++)
	{
		out[i] = dB2dy(temp[i], out[i]);
		//fprintf(stdout,"%d %g\n", i, out[i]);
	}

	prf_b("findNoiseArray: done");

	FILE *f = fopen ("noise.tmp", "w");
	assert(f);
	for (i = 0; i < size; i++)
		fprintf(f, "%d %g %g %g\n", i, data[i], out[i], temp[i]);
	fclose(f);

	delete[] temp;
	prf_b("findNoiseArray: exiting");
}
