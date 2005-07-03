#include <assert.h>
#include <stdio.h>
#include <math.h>
#include <time.h>
#include "rnd.h"

const double PI = 3.1415926535897932384626;
static double dpow2(double v) { return v*v; }

static unsigned long r24s = 6415499; // some initial seed value

//inline long r24()
//{
//	int nb = r24s ^ r24s >> 16 ^ r24s >> 1 ^ r24s >> 3;
//	r24s = (r24s << 8) & 0xffff00 ^ (nb & 0xff);
//	return r24s;
//}

#define r24() ( r24s = (r24s << 8) & 0xffff00 ^ ((r24s ^ r24s >> 16 ^ r24s >> 1 ^ r24s >> 3) & 0xff) )

long rnd24bit()
{
	return r24();
}

void r24_srand(long n)
{
	r24s = n & 0xffffff;
	if (r24s == 0)
		r24s++;
}

double rn01()
{
	double r1 = r24() / 16777215.0; // (0; 1]
	double r2 = r24() / 16777215.0; // (0; 1]
	return sqrt(-2 * log(r1)) * sin(2 * PI * r2); // N(0,1)
}

double ru01()
{
	return r24() / 16777216.0; // (0; 1)
}
