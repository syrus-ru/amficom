#include <assert.h>
#include "median.h"
#include "rnd.h"
//#include "prf.h"

// will return randomized of [0..N]
// XXX: not suitable if N > 2^24
inline int qrand(int N)
{
	return rnd24bit() % N;
}

// Input: p <= q; 0 <= m <= q-p
// Output: return value of m-th element of A[p]..A[q] in increasing order
static double destroyAndGetMedian2(double *A, int p, int q, int m)
{
	// assert(p<=q);
	// assert(0<=m);
	// assert(m<=q-p);
	while (p < q)
	{
		double key = A[p + qrand(q - p)]; // will get A[r], r = p..q

		int i = p; // A[?<i] are < key
		int j = q; // A[?>j] are > key
		int x = p; // A[i<=?<x] are treated as == key
		int y = q; // A[y<?<=j] are treated as == key
		// A[x<=?<=y] are not processed yet

		// three-part partitioning
		while (x <= y)
		{
			while (x <= y && A[x] <= key)
			{
				if (A[x] < key)
					A[i++] = A[x++];
				else
					x++;
			}
			while (x <= y && A[y] >= key)
			{
				if (A[y] > key)
					A[j--] = A[y--];
				else
					y--;
			}
			if (x <= y)
			{
				double tmp = A[x++];
				A[i++] = A[y--];
				A[j--] = tmp;
			}
		}
		if (p + m < i)
		{
			q = i - 1;
		}
		else if (p + m > j)
		{
			m -= j + 1 - p;
			p = j + 1;
		}
		else
			return key;
	}
	return A[p];
}

double destroyAndGetMedian(double *gist, int nsam, int pos)
{
	assert(pos < nsam);
	assert(nsam > 0);
	assert(pos >= 0);

	if (nsam <= 0)
		return 0;
	if (pos < 0)
		pos = 0;
	if (pos > nsam - 1)
		pos = nsam - 1;

	double ret = destroyAndGetMedian2(gist, 0, nsam - 1, pos);

	return ret;
}
