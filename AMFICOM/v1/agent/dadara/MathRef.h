#ifndef M_PI
#define	M_PI 3.1415926535
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

