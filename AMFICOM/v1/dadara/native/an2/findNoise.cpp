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

const double L10O5 = log(10) / 5;

static double log2add(double v)
{
	return exp(v * L10O5);
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

inline double dy2dB(double y0, double dy)
{
	return y0 + dy + 5.0 * log10(1.0 - exp(-dy * L10O5));
}

inline double dB2dy(double y0, double dB)
{
	double ret = 5.0 * log10(1.0 + exp((dB - y0) * L10O5));
	if (ret > 20.0) // XXX
		ret = 20.0;
	return ret;
}

/*
 * findNoiseArray()
 * ����������� ������ ���� � ����������� �� ����������
 * � ������ ����������� ���������� ������ ���� ��
 * �/�
 * ���. �������� - � ���. ��, �� ��. ~1 �����
 * len2 - ������������ ������������ �������� ����, � � <= size (� �������� �.�.)
 */
void findNoiseArray(double *data, double *outNoise, int size, int len2)
{
	if (len2 <= 0)
		return;

	assert(len2 <= size);
	//if (len2 > size)
	//	len2 = size;

	//prf_b("findNoiseArray: enter");

	const int width = NETTESTWIDTH;
	// mlen ������ ���������� ������
	const int mlen = width * 10;
	// -1 ����� ��� ������������ x-�����.
	const int nsam = mlen - 2 * width - 1;
	const int mofs = mlen / 2 - 1;
	double gist[nsam];

	assert(size > mlen); // XXX

	double *temp = new double[size]; // ����� �������� ����� ���. �/� � ���. �����.
	assert(temp);
	double *out = new double[size]; // ����� - ���
	assert(out);

	double levelPrec0 = log2add(prec0) - 1; // XXX

	//prf_b("findNoiseArray: log2add");

	int i;
	const int effSize = size < len2 + mlen - mofs ? size : len2 + mlen - mofs;

	// �������� � ��������� ��������
	for (i = 0; i < effSize; i++)
		out[i] = log2add(data[i]);

	// ��������� ������ ������ ����
	//prf_b("findNoiseArray: first estimation");
	for (i = 0; i < effSize - mlen + nsam; i++)
	{
		// ������ ������, I2+I0-2I1 - ��� �� �� ������ ��� ��� �����,
		// �� ��� ����������� ��� �������������� �������� ���������
		// �������� �� ������������ �� 3 ������� ������ ��� levelPrec0
		double v0 = out[i];
		double v1 = out[i + width];
		double v2 = out[i + width * 2];
		temp[i] = fabs(v2 + v0 - v1 - v1) + levelPrec0 * v1;
	}

	// ����������
	//prf_b("findNoiseArray: averaging");
	i = 0;
	while(i < effSize - mlen)
	{
		int j;
		// ���������� ��������� ������� ����
		for (j = 0; j < nsam; j++)
		{
			gist[j] = temp[i + j];
		}
		const int M = nsam / 2;
		double dv = destroyAndGetMedian(gist, nsam, M);
		double vout = add2log(dv);
#if 0	// ������ ����� -- ������ ������� � ������ �����
		out[i + mofs] = vout;
		i++;
#elif 0 // ������ ����� -- ������ � ������ ����� �����
		const int step = 4; // ����-� �����������, ~ 4
		for (j = 0; j < step && i + j < effSize - mlen; j++)
			out[i + j + mofs] = vout;
		i += step;
#else	// ������������ ����� - ������� ���� � �� �� �����, ����
		// ������� ����� � �������� +- delta �� �� ���������� ��������
		double delta = dv * 0.1; // �����������, ~ 0.1
		double vL = dv - delta;
		double vH = dv + delta;
		int c1 = 0, c2 = 0; // c1 - ����� �������� ���� vL; c2 - ����� �������� ���� vH
		for (j = 0; j < nsam; j++)
		{
			if (temp[i + j] < vL)
				c1++;
			else if (temp[i + j] > vH)
				c2++;
		}
		do
		{
			out[i + mofs] = vout;
			if (temp[i] < vL)
				c1--;
			else if (temp[i] > vH)
				c2--;
			if (temp[i + nsam] < vL)
				c1++;
			else if (temp[i + nsam] > vH)
				c2++;
			i++;
		} while (i < effSize - mlen && c1 <= M && c2 <= (nsam - M));
#endif
	}
	//prf_b("findNoiseArray: expand & process");

    // ��������� �� ����� ������� - �����
	for (i = 0; i < mofs; i++)
	{
		out[i] = out[mofs];
	}
	// � - ���� ���� - ������
	for (i = size - mlen + mofs; i < len2; i++)
	{
		out[i] = out[size - mlen + mofs - 1];
	}

    // ������ �������� �� ������ ��������������
    {
		// ���� ���. ����. ����������� �/�
        double vMax = data[0];
        for (i = 0; i < len2; i++)
        {
        	if (vMax < data[i])
            	vMax = data[i];
        }
        // ���������� ��������� ������� ����
        if (out[0] > vMax + MAX_VALUE_TO_INITIAL_DB_NOISE)
        	out[0] = vMax + MAX_VALUE_TO_INITIAL_DB_NOISE;
    }

	// ������ ������ ������������� ��������
	for (i = 1; i < len2; i++)
	{
		if (out[i] > out[i - 1])
			out[i] = out[i - 1];
	}

	//prf_b("findNoiseArray: dB2dy");
	// ��������� �������� ������
	for (i = 0; i < len2; i++)
		outNoise[i] = dB2dy(data[i], out[i]);

	//prf_b("findNoiseArray: done");

	delete[] temp;
	delete[] out;
}
