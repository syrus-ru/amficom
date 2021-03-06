#include <math.h>

#ifndef M_PI
#define	M_PI 3.1415926535897932384626433832795028841971693993751058209749445923078164062862
#endif

#ifndef min
#define min(a,b)            (((a) < (b)) ? (a) : (b))
#endif

#ifndef max
#define max(a,b)            (((a) > (b)) ? (a) : (b))
#endif

#ifndef sign
#define sign(a)            (((a) <= 0) ? (((a) < 0) ? (-1) : (0)) : (1))
#endif

#ifndef _mathref_h_
#define _mathref_h_

int findConstantPoint(double* y, int start, int end);
int findFirstAbsMinimumPoint(double* y, int start, int end);
bool isAbsMinimumPoint(double* y, int point);
void linearize2point (double* y, int begin, int end, double *res);

double getWLetNorma(int freq); // ????? ?? ????????
double getWLetNorma2(int freq); // ????? ?? ???????
double  wLet  (int arg, int width, double norma);
/*
double  wLet1 (int arg, int freq, double norma);
double  wLet2 (int arg, int freq, double norma);
double  wLet3 (int arg, int freq, double norma);
double  wLet4 (int arg, int freq, double norma);
double  wLet5 (int arg, int freq, double norma);
double  wLet6 (int arg, int freq, double norma);
double  wLet7 (int arg, int freq, double norma);
double  wLet8 (int arg, int freq, double norma);
double  wLet9 (int arg, int freq, double norma);
double wLet10 (int arg, int freq, double norma);
*/

double wLet_SINX(int scale, int x);
double wLet_SINXABSX(int scale, int x);

#endif
