#include <stdlib.h>
#include "median.h"

static int dfcmp(const void *a, const void *b)
{
	const double *x = (const double *)a;
	const double *y = (const double *)b;
	return *x <= *y ? *x < *y ? -1 : 0 : 1;
}

double destroyAndGetMedian(double *gist, int nsam, int pos)
{
	// FIXME: ускорить - есть простые алгоритмы со сложностью O(nsam)
	qsort(gist, nsam, sizeof(double), dfcmp);
	return gist[pos];
}
