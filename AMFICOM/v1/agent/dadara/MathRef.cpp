#include "MathRef.h"

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

