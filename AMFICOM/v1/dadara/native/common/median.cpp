#include <assert.h>
#include "median.h"
#include "rnd.h"
//#include "prf.h"

// will return randomized of [0..N]
// XXX: not suitable if N > 2^24
static int qrand(int N)
{
	return rnd24bit() % N;
}

// Здесь реализовано два способа.
// Первый в ~ 5 раз быстрее метода на основе полной сортировки qsort'ом,
// Второй в полтора раза быстрее первого.

/*
// Input: p <= q; 0 <= m <= q-p
// Output: return value of m-th element of A[p]..A[q] in increasing order
static double destroyAndGetMedian1(double *A, int p, int q, int m)
{
	// assert(p<=q);
	// assert(0<=m);
	// assert(m<=q-p);
	// Xassert(exists A[p]);
	// Xassert(exists A[q]);
	//fprintf(stderr, "###\n");
	while (p < q)
	{
		// assert(p<=q);
		// assert(0<=m);
		// assert(m<=q-p);

		// *** place randomized element A[r] to A[p]
		{
			int r = p + qrand(q - p); // will get r = p..q
			// assert(r >= p);
			// assert(r <= q);
			double tmp = A[p];
			A[p] = A[r];
			A[r] = tmp;
		}

		// *** partitioning
		int i = p + 1;
		int j = q;
		// assert(i<=j+1);
		// assert(p<=j);
		while(i <= j)
		{
			// assert(i<=j);
			// assert(j<=q);
			if(A[i] <= A[p])
			{
				i++;
			}
			else
			{
				double tmp;
				tmp = A[i];
				A[i] = A[j];
				A[j] = tmp;
				j--;
			}
		}
		//fprintf(stderr, "p %d q %d m %d i %d j %d if %s\n",p,q,m,i,j, m<i-p?"true":"false");
		// assert(i==j+1);
		// Xassert(ForAny m: p<m<i ==> A[m]<=A[p]);
		// Xassert(ForAny m: i<=m<q ==> A[m]>A[p]);
		if (m < i - p)
		{
			q = j;
			// assert(m<=q-p);
			// *** теперь надо отсеять равные A[p]
			if (p < q)
			{
				j = q;
				i = p + 1;
				for(;;)
				{
					while (i <= j && A[i] == A[p])
						i++;
					while (i <= j && A[j] < A[p])
						j--;
					if (i <= j)
					{
						double tmp = A[i];
						A[i] = A[j];
						A[j] = tmp;
						i++;
						j--;
					}
					else
						break;
				}
				if (m > q - i)
				{
					return A[p];
				}
				else
				{
					p = i;
				}
			}
			// assert(m<=q-p);
		}
		else
		{
			m = m - (i - p);
			p = i;
		}
	}

	return A[p];
}
*/

// Input: p <= q; 0 <= m <= q-p
// Output: return value of m-th element of A[p]..A[q] in increasing order
static double destroyAndGetMedian2(double *A, int p, int q, int m)
{
	// assert(p<=q);
	// assert(0<=m);
	// assert(m<=q-p);
	while (p < q)
	{
		int r = p + qrand(q - p); // will get r = p..q
		//assert(r >= p);
		//assert(r <= q);

		double key = A[r];

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

/*
static int dfcmp(const void *a, const void *b)
{
	const double *x = (const double *)a;
	const double *y = (const double *)b;
	return *x <= *y ? *x < *y ? -1 : 0 : 1;
}

double destroyAndGetMedian(double *gist, int nsam, int pos)
{
	prf_b("destroyAndGetMedian: enter");
	double *cpy = new double[nsam];
	assert(cpy);
	memcpy(cpy,gist,sizeof(double)*nsam);

	double *save = new double[nsam];
	assert(save);
	memcpy(save,gist,sizeof(double)*nsam);

	assert(pos < nsam);
	assert(nsam > 0);
	assert(pos >= 0);
	if (nsam <= 0)
		return 0;
	if (pos < 0)
		pos = 0;
	if (pos > nsam - 1)
		pos = nsam - 1;

	prf_b("destroyAndGetMedian: method 1");
	double ret1 = destroyAndGetMedian1(gist, 0, nsam - 1, pos);
	prf_b("destroyAndGetMedian: method 2");
	double ret2 = destroyAndGetMedian2(save, 0, nsam - 1, pos);
	prf_b("destroyAndGetMedian: qsort");
	qsort(cpy, nsam, sizeof(double), dfcmp);
	prf_b("destroyAndGetMedian: done");

	if (ret1 != cpy[pos] || ret1 != ret2)
	{
		fprintf(stderr, "destroyAndGetMedian: nsam %d pos %d ret1 %g ret2 %g qsort %g\n",
			nsam, pos, ret1, ret2, cpy[pos]);
		int i;
		fprintf(stderr, "### dump:\n");
		for (i = 0; i < nsam; i++)
			fprintf(stderr, "%3d %6g %6g %6g\n", i, save[i], cpy[i], gist[i]);
		fflush(stderr);
		assert (ret1 == cpy[pos]);
		assert (ret2 == cpy[pos]);
	}
	delete[] cpy;
	delete[] save;
	return ret1;
}
*/

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
