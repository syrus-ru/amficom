/*- Прогонка

  Решение методом прогонки трехдиагональной системы
                - b[k] * res[k] + c[k] * res[k+1] = d[k], k = 0
a[k] * res[k-1] - b[k] * res[k] + c[k] * res[k+1] = d[k], k = 1 .. num-1
a[k] * res[k-1] - b[k] * res[k]                   = d[k], k = num

  или

b[0] * res[0] = d[0],

  если num = 0

 2002 г - исходная версия

 2004 г:
	добавлена assert-проверка успеха new (раньше был mynewh)
	все неопределенности 0/0 разрешаются как 0
	исправлена ошибка при num == 0

*/

//#include <stdio.h>
#include <assert.h>
#include "sweep.h"

inline double div(double a, double b)
{
return a ? a / b : 0;
}

void sweep
 (
 int num,
 const double *a, const double *b, const double *c, const double *d,
 double *res
 )
 // все массивы: double [num+1]
{
	if (num == 0)
	{
		*res = -div(*d, *b);
		return;
	}
	double *p,*q,*pt,*qt;
	p=new double[num+1];
	q=new double[num+1];
	assert(p);
	assert(q);
	// p[0] и q[0] не используются
	double pc,qc; // current values
	pc=div(*c, *b); qc=-div(*d, *b);
	int i;
	pt=p; qt=q;
	for (i=1; i<num; i++) {
	  *++pt=pc; *++qt=qc;
	  a++; b++; c++; d++;
	  double tmp;
	  tmp = *b - *a * pc;
	  pc = div(*c, tmp); qc = div(*a*qc - *d, tmp);
	}
	*++pt=pc; *++qt=qc;
	a++; b++; d++;
	res+=num;
	double xc = div( *qt*(*a)-*d, *b-*pt*(*a) );
	for (i=0; i<num; i++) {
	  *(res--) = xc;
	  xc=xc*(*(pt--)) + *(qt--);
	}
	*res=xc;
	delete[] q;
	delete[] p;
}

