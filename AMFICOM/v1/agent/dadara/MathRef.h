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

#include <math.h>

inline int round (double d);
int findInflectionPoint(double* y, int start, int end);
int findConstantPoint(double* y, int start, int end);
int findFirstAbsMinimumPoint(double* y, int start, int end);
bool isAbsMinimumPoint(double* y, int point);
void linearize2point (double* y, int begin, int end, double *res);
void LSA(double* y, int begin, int end, double* res);
void LSA(double* y, int begin, int end, int shift, double* res);


double* getWLetNorma(int freq, double *n);
double getWLetNorma(int freq, int waveletType);
double   wLet (int arg, int freq, double* norma, int waveletType);
double   wLet (int arg, int freq, double norma, int waveletType);
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

#endif
