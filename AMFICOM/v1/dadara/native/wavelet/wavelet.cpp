#include <assert.h>
#include <math.h>
#include "wavelet.h"

#ifndef M_PI
#define M_PI 3.14159265358979323846
#endif

inline int imin(int a, int b) { return a < b ? a : b; }
inline int imax(int a, int b) { return a > b ? a : b; }

//
// generic wavelet methods
//

void Wavelet::fArr(int s, double *out)
{
	int i;
	for (i = -s; i <= s; i++)
		out[i + s] = f(s, i);
}

double Wavelet::normStep(int s)
{
	int i;
	double sum = 0;
	for (i = -s; i <= s; i++)
		sum += fabs(f(s, i));
	return sum / 2;
}

double Wavelet::normIntCheck(int s)
{
	int i;
	double sum = 0;
	for (i = -s; i <= s; i++)
		sum += f(s, i);
	return sum;
}

double Wavelet::normIntInt(int s)
{
	int i;
	double sum1 = 0;
	double sum2 = 0;
	for (i = -s; i <= s; i++)
		sum2 += sum1 += f(s, i);
	return sum2;
}

double Wavelet::normMx(int s)
{
	int i;
	double sum = 0;
	for (i = -s; i <= s; i++)
		sum += f(s, i) * i;
	return sum;
}

// input range = [0..inLen-1]; out range = [0..iTo-iFrom]
void Wavelet::transform(int s, double *in, int inLen, int iFrom, int iTo, double *out)
{
	double* wData = new double[2 * s + 1];
	assert(wData);
	fArr(s, wData);
	int i;
	for (i = iFrom; i <= iTo; i++)
	{
		double sum = 0;
		int j0 = i - s;
		int j1 = imax(j0, 0);
		int j3 = i + s;
		int j2 = imin(j3, inLen - 1);
		int j = j0;
		for (; j < j1; j++)  sum += in[j1] * wData[j - i + s];
		for (; j < j2; j++)  sum += in[j]  * wData[j - i + s];
		for (; j <= j3; j++) sum += in[j2] * wData[j - i + s];
		out[i - iFrom] = sum;
	}
	delete[] wData;
}

//
// basic Haar methods
//

int HaarWavelet::getMinScale()
{
	return 1;
}
double HaarWavelet::f(int s, int x)
{
	return x <= 0 ? x < 0 ? -1 : 0 : 1;
}

//
// basic Sine methods
//

int SineWavelet::getMinScale()
{
	return 2;
}
double SineWavelet::f(int s, int x)
{
	return sin(M_PI * x / s);
}

// quick Haar methods

// input range = [0..inLen-1]; out range = [0..iTo-iFrom]
void HaarWavelet::transform(int s, double *in, int inLen, int iFrom, int iTo, double *out)
{
	int iL = iFrom - s;
	int iR = iTo + s;
	int iMin = imax(iFrom - s, 0);
	int iMax = imin(iTo + s, inLen - 1);

	// prepare stats
	double *sum = new double[iR - iL + 1]; // i.e., [iFrom - iTo + 2 * s + 1]
	assert(sum);
	int i;
	double acc = 0;
	for (i = iL;   i < iMin; i++) sum[i - iL] = acc += in[iMin];
	for (i = iMin; i < iMax; i++) sum[i - iL] = acc += in[i];
	for (i = iMax; i <= iR; i++)  sum[i - iL] = acc += in[iMax];

	// i == iFrom
	out[0] = sum[iFrom - iL + s] - sum[iFrom - iL] - sum[iFrom - iL - 1];

	// i > iFrom
	for (i = iFrom + 1; i <= iTo; i++)
		out[i - iFrom] = sum[i - iL + s] - sum[i - iL] - sum[i - iL - 1] + sum[i - iL - s - 1];

	delete[] sum;
}


// quick Sine methods

// input range = [0..inLen-1]; out range = [0..iTo-iFrom]
void SineWavelet::transform(int s, double *in, int inLen, int iFrom, int iTo, double *out)
{
	// prepare sine table
	double *sine = new double[s * 2];
	assert(sine);
	int i;
	sine[0] = sine[s] = 0;
	int q = s / 2;
	for (i = 1; i <= q; i++)
	{
		sine[s + s - i] = sine[s + i] = sin(M_PI * i / s);
		sine[i] = sine[s - i] = -sine[s + i];
	}
	// prepare cosine table
	double *cosine = new double[s * 2];
	assert(cosine);
	cosine[0] = -1; cosine[s] = 1;
	for (i = 1; i <= q; i++)
	{
		// odd s: we need to just cacl cosine table
		// even s: we can use sine table
		cosine[s - i] = cosine[s + i] = s % 2 ? cos(M_PI * i / s) : sine[s + q - i];
		cosine[i] = cosine[s + s - i] = -cosine[s + i];
	}

	int iL = iFrom - s;
	int iR = iTo + s;
	int iMin = imax(iFrom - s, 0);
	int iMax = imin(iTo + s, inLen - 1);

	// prepare stats
	double *S = new double[iR - iL + 1];
	assert(S);
	double *C = new double[iR - iL + 1];
	assert(C);
	double aS = 0;
	double aC = 0;
	int ofs = 0;
	for (i = iL;   i < iMin; i++, ofs = (ofs + 1) % (s * 2))
	{
		S[i - iL] = aS += in[iMin] * sine[ofs];
		C[i - iL] = aC += in[iMin] * cosine[ofs];
	}
	for (i = iMin; i < iMax; i++, ofs = (ofs + 1) % (s * 2))
	{
		S[i - iL] = aS += in[i] * sine[ofs];
		C[i - iL] = aC += in[i] * cosine[ofs];
	}
	for (i = iMax; i <= iR; i++, ofs = (ofs + 1) % (s * 2))
	{
		S[i - iL] = aS += in[iMax] * sine[ofs];
		C[i - iL] = aC += in[iMax] * cosine[ofs];
	}

	for (i = iFrom; i <= iTo; i++)
	{
		double vc = cos(M_PI * (i - iFrom) / s);
		double vs = sin(M_PI * (i - iFrom) / s);
		out[i - iFrom] = vc * S[i + s - iL] - vs * C[i + s - iL];
		if (i > 0)
			out[i - iFrom] -= vc * S[i - s - 1 - iL] - vs * C[i - s - 1 - iL];
	}

	delete[] S;
	delete[] C;
	delete[] sine;
	delete[] cosine;
}

//
// class UserWavelet
//

int UserWavelet::getMinScale()
{
	return minScale;
}
double UserWavelet::f(int s, int x)
{
	return (*fPtr)(s, x);
}
UserWavelet::UserWavelet(int minScale, double(*fPtr)(int, int))
{
	this->minScale = minScale;
	this->fPtr = fPtr;
}

