#include "MathRef.h"

int findInflectionPoint(double* y, int start, int end)
{
	for (int i = start + 1; i < end - 1; i++)
		if (sign(y[i] - y[i-1]) != sign(y[i+1] - y[i]))
			return i;
	return end;
}

int findFirstAbsMinimumPoint(double* y, int start, int end)
{
	for (int i = start + 1; i < end - 1; i++)
		if (isAbsMinimumPoint(y, i))
			return i;
	return end;
}

bool isAbsMinimumPoint(double* y, int point)
{
	if (fabs(y[point-1]) > fabs(y[point]) && fabs(y[point+1]) > fabs(y[point]))
		return true;
	return false;
}

int findConstantPoint(double* y, int start, int end)
{
	for (int i = start + 1; i < end - 1; i++)
		if (fabs(y[i] - y[i-1]) < 0.001
			&& fabs(y[i+1] - y[i]) < 0.001)
		return i;
	return end;
}

void linearize2point (double* y, int begin, int end, double *res)
{
	res[0] = (y[end] - y[begin]) / (double)(end - begin);
	res[1] = y[begin] - res[0] * (double)begin;
}


double* getWLetNorma(int freq, double *n)
{
	int i;
	for (i = 0; i < 10; i++)
		n[i] = 0;

	for(i = -freq; i <= freq; i++)
	{
		n[0] = n[0] + fabs(wLet1(i, freq, 1.));
		n[1] = n[1] + fabs(wLet2(i, freq, 1.));
		n[2] = n[2] + fabs(wLet3(i, freq, 1.));
		n[3] = n[3] + fabs(wLet4(i, freq, 1.));
		n[4] = n[4] + fabs(wLet5(i, freq, 1.));
		n[5] = n[5] + fabs(wLet6(i, freq, 1.));
		n[6] = n[6] + fabs(wLet7(i, freq, 1.));
		n[7] = n[7] + fabs(wLet8(i, freq, 1.));
		n[8] = n[8] + fabs(wLet9(i, freq, 1.));
		n[9] = n[9] + fabs(wLet10(i, freq, 1.));
	}
	for (i = 0; i < 10; i++)
		n[i] /= 2.;

	return n;
}

double getWLetNorma(int freq, int waveletType)
{
	int i;
	double n = 0;
	switch (waveletType)
	{
		case  0:
			for(i = -freq; i <= freq; i++)
				n = n + fabs(wLet1(i, freq, 1.));
			break;
		case  1:
			for(i = -freq; i <= freq; i++)
				n = n + fabs(wLet2(i, freq, 1.));
			break;
		case  2:
			for(i = -freq; i <= freq; i++)
				n = n + fabs(wLet3(i, freq, 1.));
			break;
		case  3:
			for(i = -freq; i <= freq; i++)
				n = n + fabs(wLet4(i, freq, 1.));
			break;
		case  4:
			for(i = -freq; i <= freq; i++)
				n = n + fabs(wLet5(i, freq, 1.));
			break;
		case  5:
			for(i = -freq; i <= freq; i++)
				n = n + fabs(wLet6(i, freq, 1.));
			break;
		case  6:
			for(i = -freq; i <= freq; i++)
				n = n + fabs(wLet7(i, freq, 1.));
			break;
		case  7:
			for(i = -freq; i <= freq; i++)
				n = n + fabs(wLet8(i, freq, 1.));
			break;
		case  8:
			for(i = -freq; i <= freq; i++)
				n = n + fabs(wLet9(i, freq, 1.));
			break;
		case  9:
			for(i = -freq; i <= freq; i++)
				n = n + fabs(wLet10(i, freq, 1.));
			break;
		default:
			for(i = -freq; i <= freq; i++)
				n = n + fabs(wLet1(i, freq, 1.));
			break;
	}
	n /= 2.;
	return n;
}


double wLet(int arg, int freq, double norma, int waveletType)
{
	switch (waveletType)
	{
		case  0: return  wLet1(arg, freq, norma);
		case  1: return  wLet2(arg, freq, norma);
		case  2: return  wLet3(arg, freq, norma);
		case  3: return  wLet4(arg, freq, norma);
		case  4: return  wLet5(arg, freq, norma);
		case  5: return  wLet6(arg, freq, norma);
		case  6: return  wLet7(arg, freq, norma);
		case  7: return  wLet8(arg, freq, norma);
		case  8: return  wLet9(arg, freq, norma);
		case  9: return wLet10(arg, freq, norma);
		default: return  wLet1(arg, freq, norma);
	}
}

double wLet(int arg, int freq, double* norma, int waveletType)
{
	switch (waveletType)
	{
		case  0: return  wLet1(arg, freq, norma[0]);
		case  1: return  wLet2(arg, freq, norma[1]);
		case  2: return  wLet3(arg, freq, norma[2]);
		case  3: return  wLet4(arg, freq, norma[3]);
		case  4: return  wLet5(arg, freq, norma[4]);
		case  5: return  wLet6(arg, freq, norma[5]);
		case  6: return  wLet7(arg, freq, norma[6]);
		case  7: return  wLet8(arg, freq, norma[7]);
		case  8: return  wLet9(arg, freq, norma[8]);
		case  9: return wLet10(arg, freq, norma[9]);
		default: return  wLet1(arg, freq, norma[0]);
	}
}

void LSA(double* y, int begin, int end, double* res)
{
	LSA(y, begin, end, 0, res);
}

void LSA(double* y, int begin, int end, int shift, double* res)
{
	double alfa=0;
	double beta=0;
	double gamma=0;
	double dzeta=0;
	double d=0;

	if(begin < 0)
		begin=0;
	if(end < 1)
		end=1;

	for(int i = begin; i<=end; i++)
	{
		d = (double)(i - shift);
		beta = beta - y[i] * d;
		alfa = alfa + d * d;
		gamma = gamma + d;
		dzeta = dzeta - y[i];
	}
	double n = (double)(end - begin + 1);
	
	res[0] = (n*beta/gamma - dzeta)/(gamma - n*alfa/gamma);
	res[1] = -(alfa*res[0] + beta)/gamma;
}

int round (double d)
{
	return (int)(d + 0.5);
}

double wLet1(int arg, int freq, double norma)
{
	return (sin(arg*M_PI/freq))/norma;
}

double wLet2(int arg, int freq, double norma)
{
	return (sin(arg*M_PI/freq)*fabs((double)arg))/norma;
}

double wLet3(int arg, int freq, double norma)
{
	return (sin(arg*M_PI/freq)/(1. + fabs((double)arg)))/norma;
}

double wLet4(int arg, int freq, double norma)
{
	return (sin(arg*M_PI/freq)/(1. + sqrt(fabs((double)arg))))/norma;
}

double wLet5(int arg, int freq, double norma)
{
	if(arg<0) return -1./norma;
	if(arg>0) return 1./norma;
	return 0.;
}

double wLet6(int arg, int freq, double norma)
{
	return (((double)arg)/freq)/norma;
}

double wLet7(int arg, int freq, double norma)
{
	if(arg>0) return  (double)(freq-arg)/norma;
	if(arg<0) return -(double)(freq+arg)/norma;
	return 0.;
}

double wLet8(int arg, int freq, double norma)
{
	return (sin(arg*M_PI/freq) + ((double)arg)/freq/2.)/norma;
}

double wLet9(int arg, int freq, double norma)
{
	return (sin(arg*M_PI/freq/2.))/norma;
}

double wLet10(int arg, int freq, double norma)
{
	return (sin(arg*M_PI/freq/2.) - ((double)arg)/freq/2.)/norma;
}