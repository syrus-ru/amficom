#ifndef _WAVELET_H
#define _WAVELET_H

class Wavelet
{
public:
	virtual int getMinScale() = 0;
	virtual double f(int s, int x) = 0; // x = -s .. +s
	virtual void fArr(int s, double *out); // fill array of 2s+1 elements
	virtual double normStep(int s);
	virtual double normIntCheck(int s); // should be zero
	virtual double normIntInt(int s);
	virtual double normMx(int s);
	virtual void transform(int s, double *in, int inLen, int iFrom, int iTo, double *out); // input range = [0..inLen-1]; out range = [0..iTo-iFrom]
};

class HaarWavelet : public Wavelet
{
public:
	int getMinScale();
	double f(int s, int x);
	void transform(int s, double *in, int inLen, int iFrom, int iTo, double *out);
};

class SineWavelet : public Wavelet
{
public:
	int getMinScale();
	double f(int s, int x);
	void transform(int s, double *in, int inLen, int iFrom, int iTo, double *out);
};

class UserWavelet : public Wavelet
{
private:
	int minScale;
	double(*fPtr)(int s, int x);
public:
	int getMinScale();
	double f(int s, int x);
	UserWavelet(int minScale, double(*fPtr)(int s, int x));
};

#endif
