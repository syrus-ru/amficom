#include "MathRef.h"
#include <assert.h>
//------------------------------------------------------------------------------------------------------------
int findFirstAbsMinimumPoint(double* y, int start, int end)
{	for (int i = start + 1; i < end - 1; i++)
	{	if (isAbsMinimumPoint(y, i))
return i;
	}
	return end;
}
//------------------------------------------------------------------------------------------------------------
bool isAbsMinimumPoint(double* y, int point)
{	if (fabs(y[point-1]) > fabs(y[point]) && fabs(y[point+1]) > fabs(y[point]))
		return true;
	return false;
}
//------------------------------------------------------------------------------------------------------------
int findConstantPoint(double* y, int start, int end)
{	for (int i = start + 1; i < end - 1; i++)
	{	if (fabs(y[i] - y[i-1]) < 0.001	&& fabs(y[i+1] - y[i]) < 0.001)
return i;
    }
	return end;
}
//------------------------------------------------------------------------------------------------------------
void linearize2point (double* y, int begin, int end, double *res)
{	res[0] = (y[end] - y[begin]) / (double)(end - begin);
	res[1] = y[begin] - res[0] * (double)begin;
}
//------------------------------------------------------------------------------------------------------------
inline double wLet1(int arg, int width, double norma)
{	return (sin(arg*M_PI/width))/norma;
}
//------------------------------------------------------------------------------------------------------------
inline double wLet2(int arg, int freq, double norma)
{	return (sin(arg*M_PI/freq)*fabs((double)arg))/norma;
}
//------------------------------------------------------------------------------------------------------------
inline double wLet3(int arg, int freq, double norma)
{	return (sin(arg*M_PI/freq)/(1. + fabs((double)arg)))/norma;
}
//------------------------------------------------------------------------------------------------------------
inline double wLet4(int arg, int freq, double norma)
{	return (sin(arg*M_PI/freq)/(1. + sqrt(fabs((double)arg))))/norma;
}
//------------------------------------------------------------------------------------------------------------
inline double wLet5(int arg, int freq, double norma)
{	if(arg<0) return -1./norma;
	if(arg>0) return 1./norma;
	return 0.;
}
//------------------------------------------------------------------------------------------------------------
inline double wLet6(int arg, int freq, double norma)
{	return (((double)arg)/freq)/norma;
}
//------------------------------------------------------------------------------------------------------------
inline double wLet7(int arg, int freq, double norma)
{	if(arg>0) return  (double)(freq-arg)/norma;
	if(arg<0) return -(double)(freq+arg)/norma;
	return 0.;
}
//------------------------------------------------------------------------------------------------------------
inline double wLet8(int arg, int freq, double norma)
{	return (sin(arg*M_PI/freq) + ((double)arg)/freq/2.)/norma;
}
//------------------------------------------------------------------------------------------------------------
inline double wLet9(int arg, int freq, double norma)
{	return (sin(arg*M_PI/freq/2.))/norma;
}
//------------------------------------------------------------------------------------------------------------
inline double wLet10(int arg, int freq, double norma)
{	return (sin(arg*M_PI/freq/2.) - ((double)arg)/freq/2.)/norma;
}
//------------------------------------------------------------------------------------------------------------
double getWLetNorma(int freq)
{	double n = 0;
	for(int i = -freq; i <= freq; i++)
    {	n = n + fabs(wLet1(i, freq, 1.));
    }
	n /= 2.;
	return n;
}
//------------------------------------------------------------------------------------------------------------
double getWLetNorma2(int freq)
{	double s = 0;
	for(int i = -freq; i <= freq; i++)
    {	s = s + wLet1(i, freq, 1.) * i;
    }
	s /= 2.;
	return s;
}
//------------------------------------------------------------------------------------------------------------
// waveletType ÏÅÐÅÏÈÑÒÜ êàê function* !!!
double wLet(int arg, int width, double norma)
{	return  wLet1(arg, width, norma);//!!! îò waveletType ÍÈ×ÅÃÎ ÍÅ ÇÀÂÈÑÈÒ 
}
//------------------------------------------------------------------------------------------------------------

////////////

double wLet_SINX(int scale, int x)
{	return sin(x * M_PI / scale);
}

double wLet_SINXABSX(int scale, int x)
{	return sin(x * M_PI / scale) * fabs(x);
}
