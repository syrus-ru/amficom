// XXX - look through, add comments and remove debugs; also need to rename
#include <assert.h>
#include <stdio.h> // fprintf / debug
#include "mat-solve.h"

// let's try gaussian solve tequnique

/* double max_eigenvalue (int N, double *A, double *T_ = 0)
{
	double *T;
	if (T_ == 0)
	{
		T = new double[N*N];
	}
	else
		T = T_;

	double eigen = XXX;

	int i,j;
	for (i = 0; i < N; i++)
		for (j = 0; j < N; j++)
			T[i*N + j] = A[i*N + j];

	...

	if (T_ == 0)
		delete[] T;
	return eigen;
} */

inline double dabs(double v) { return v >= 0 ? v : -v; }
inline void dswap(double &a, double &b) { double tmp = a; a = b; b = tmp; }

// solve A * x_out = b with gaussian tequnique. In case of singularity, add random :)
void mat_solvex(int N, const double *A_in, const double *b_in, double *x_out)
{
	int i, j, k;

	// temporal storage for A and b

	double *Ab_storage = new double [N*N + N]; // united storage for both A and b
	double *A = Ab_storage;
	double *b = Ab_storage + N*N;

	for (i = 0; i < N; i++)
		for (j = 0; j < N; j++)
			A[i*N + j] = A_in[i*N + j];

	for (i = 0; i < N; i++)
		b[i] = b_in[i];

	// gaussian turn to triangular shape

	for (k = 0; k < N; k++)
	{
		/*printf("k %d dump:\n", k);
		for (j = 0; j < N; j++)
		{
			for (i= 0; i < N; i++)
				printf("%5g ", A[j*N+i]);
			printf("       %5g\n",b[j]);
		}
		printf("\n");*/

		// find best value
		double v_best = 0;
		int n_best = k;
		for (j = k; j < N; j++)
			if (dabs(A[j*N + k]) > v_best)
				v_best = dabs(A[j*N + k]), n_best = j;
		if (v_best == 0)
		{
			// singular matrix: set corresponding parameter to zero
			//fprintf(stderr, "singular matrix\n"); // FIXIT: debug printf
			A[k*N + k] = 1.0;
			for (i = k + 1; i < N; i++)
				A[k*N + i] = 0.0;
			b[k] = 0.0;
			n_best = k;
		}
		// use row number n_best (swap rows)
		if (n_best != k)
		{
			for (i = 0; i < N; i++)
				dswap(A[k*N + i], A[n_best*N + i]);
			dswap(b[k], b[n_best]);
		}

		// some sanity check. FIXIT: remove; debug only; may fail due to rounding error
		for (i = 0; i < k; i++)
		{
			if (A[k*N + i])
			{
				printf("assert: k %d i %d A %g\n", k, i, A[k*N + i]);
				assert (A[k*N + i] == 0);
			}
		}

		// normalize row
		double mult = 1.0 / A[k*N + k];
		A[k*N + k] = 1.0;
		for (i = k+1; i < N; i++)
			A[k*N + i] *= mult;
		b[k] *= mult;

		// subtract rows
		for (j = k+1; j < N; j++)
		{
			double mult = A[j*N + k];
			for (i = k; i < N; i++)
				A[j*N + i] -= A[k*N + i] * mult;
			b[j] -= b[k] * mult;
		}
	}

	// scan down-to-top

	for (k = N-1; k >= 0; k--)
	{
		for (j = k+1; j < N; j++)
		{
			b[k] -= A[k*N + j] * b[j];
			A[k*N + j] = 0; // not nesessary
		}
	}

	// fill result

	for (i = 0; i < N; i++)
		x_out[i] = b[i];

	delete[] Ab_storage;

}

