#include <assert.h>
#include <time.h> // clock() for debug
#include <direct.h> // mkdir() for debug
#include <string.h> // min_sig related
#include <stdlib.h> // rand() for many tries

#include "inf.h"

#include "ModelF-fit.h"

#include "../Common/ModelF.h"
#include "../Common/com_syrus_AMFICOM_analysis_dadara_ModelFunction.h"


const int debug = 6;
const int dump_event = 0 && debug >= 10; // dump the event to stdout
const int dump_pos = 2786;//2783;

const int min_use_linear = 1; // использование линейного анализа для L-переменных.
							// значение 0 давно не используется и может не работать

const int Nfits = 5;
const double RAND_MULTIPLIER = 0.1;

const double ERR_INITIAL = 1.0e-5;
const double ERR_INITIAL_1 = ERR_INITIAL;
const double ERR_INITIAL_2 = ERR_INITIAL;

inline double dabs(double v)
{
	return v > 0 ? v : -v;
}

inline double dpow2(double v)
{
	return v * v;
}

/*
int ocenka_za_rabotu(double val)
{
	if (val < 0.0003)
		return 5;
	if (val < 0.003)
		return 4;
	if (val < 0.03)
		return 3;
	if (val < 0.3)
		return 2;
	return 0;
}*/

// #if 0 ... minuit part was here ... #endif

// we use globals for fitting
static double *min_data;

static ModelF *min_mfp;
static double *min_par; // current set of parameters
static double *min_err;

static int *min_pflags; // PAR_FLAG_X, PAR_FLAG_W etc
static int min_npars;
static int min_i0;
static int min_x0;
static int min_length;
static int min_rough = 0;
static int min_prefit = 0;
static void min_lock() {} // unimplemented for now -- FIXIT
static void min_unlock() {} // unimplemented for now -- FIXIT

double fcn(double *pars = min_par)
{
	//check parameters range
	if(min_prefit)
	{
		if (min_mfp->execCmd(MF_CMD_CHECK_RANGE_PREFIT))
			return HUGE;
	}
	else
	{
		if (min_mfp->execCmd(MF_CMD_CHECK_RANGE_FINFIT))
			return HUGE;
	}
	int i;
	for (i = 0; i < min_npars; i++)
	{
		//check X parameters range
		if (min_pflags[i] & MF_PAR_FLAG_X)
		{
			if (pars[i] < min_x0 || pars[i] > min_x0 + min_length - 1)
				return HUGE;
		}
		//check W parameters range
		if (min_pflags[i] & MF_PAR_FLAG_W)
		{
			if (pars[i] <= 0)
				return HUGE;
		}
	}

	double ret;

	if (min_use_linear)
		ret = min_mfp->RMS2LinP (pars, min_data, min_i0, min_x0, min_length, min_rough);
	else
		ret = min_mfp->RMS2P (pars, min_data, min_i0, min_x0, min_length, min_rough);

	if (finite(ret))
		return ret;
	else
	{
		//fprintf (stderr, "RMS: infinity? -- see pars: \n");
		//for (i = 0; i < min_npars; i++)
		//	fprintf(stderr, "  %g", min_par[i]);
		//fprintf(stderr, "\n");
		return HUGE;
	}
}

#if 0
void fcn_dump(double *pars = min_par)
{
	RMS (pars, min_fptr, min_data, min_i0, min_x0, min_length, min_rough, 1);
}
#endif

double fcn_delta(int j, double delta)
{
	double s_par = min_par[j];
	min_par[j] = s_par + delta;
	double ret = fcn();
	min_par[j] = s_par;
	return ret;
}

// returns !=0 if this variable is complete for this iter.
int min_fit_x_ts(int j, int force, double v15, double &val, double &val_n, double dir, double &step)
{
	val_n = fcn_delta(j, step * dir);
	//if (min_rough==1) fprintf(stderr, "x_ts: j %d dir %g step %g\n", j, dir, step);
	if (val_n >= val)
		return 0; // no success
	do
	{
		min_par[j] += step * dir;
		val = val_n;
		step *= v15;
		//fprintf(stderr, ".");
		if (!force)
			return 1; // success
		val_n = fcn_delta(j, step * dir);
	} while (val_n < val);
	return 1; // success
}

// options for min_fit_x
const int MIN_FIT_OPTION_X_ONLY = 1;

int min_fit_x(int maxit, int options = 0)
{
	int it, j;
	//fprintf (stderr, "min_fit_x: maxit %d val_0 %g\n", maxit, fcn());

	// do not use too small errors
	for (j = 0; j < min_npars; j++)
	{
		if (min_err[j] < ERR_INITIAL_2)
		{
			//fprintf (stderr, "Update min_err[%d] from %g to %g\n", j, min_err[j], ERR_INITIAL_2);
			min_err[j] = ERR_INITIAL_2;
		}
	}

	double *par0 = new double[min_npars]; // storage for initial position for dir step
	double *dpar = new double[min_npars]; // storage for direction for dir step
	assert(par0);
	assert(dpar);

	int dir_steps = 0; // counter of dir steps (just for debug information)

	double val_ini = fcn(); // remember for statistics

	for (it = 0; it < maxit; it++)
	{
		int force = it < 1;
		const double v15 = 1.5;
		const double v30a = 3.0;
		const double v30b = 3.0;
		const double vdl = 0.05;
		const double vdm = 3.0;
		const double vpsf = 0.23;

		double val = fcn();
		double val0 = val; // for dir step

		for (j = 0; j < min_npars; j++)
			par0[j] = min_par[j];

		for (j = 0; j < min_npars; j++)
		{
			if (min_use_linear && min_pflags[j] & MF_PAR_FLAG_L)
				continue; // when in linear mode, do not waste time for linear variables

			if (options & MIN_FIT_OPTION_X_ONLY && ~min_pflags[j] & MF_PAR_FLAG_X)
				continue; // when in X-only mode, process only X-type parameters

			double val_l;
			double val_r;
			//fprintf(stderr, "par[%d] = %.10g +- %.10g\n", j, min_par[j], min_err[j]);
			/*if (j==7 && min_rough == 1 && it < 5)
			{
				fprintf(stderr, "it %d par[%d] = %g +- %g; val %g, v_l %g v_r %g (opt %d)\n",
					it, j, min_par[j], min_err[j],
					val, fcn_delta(j, -min_err[j]), fcn_delta(j, min_err[j]),
					options
					);
			}*/
			if (min_pflags[j] & MF_PAR_FLAG_X)
			{
				double step = 1.0;
				if (min_fit_x_ts(j, force, 2.0, val, val_l, -1.0, step)
					|| min_fit_x_ts(j, force, 2.0, val, val_r, 1.0, step))
					continue;
			}
			if (it & 1) // prefer left and right steps intermediately
			{
				if (min_fit_x_ts(j, force, v15, val, val_l, -1.0, min_err[j]))
					continue;
				if (min_fit_x_ts(j, force, v15, val, val_r, 1.0, min_err[j]))
					continue;
			}
			else
			{
				if (min_fit_x_ts(j, force, v15, val, val_r, 1.0, min_err[j]))
					continue;
				if (min_fit_x_ts(j, force, v15, val, val_l, -1.0, min_err[j]))
					continue;
			}
			// now, we know that val_l >= val && val_r >= val

			// treat special cases
			if (val_l == val && val_r == val) // XXX/FIXIT: we should have a loop here if force is true
			{
				min_err[j] *= v30a;
				continue;
			}
			if (val_l == HUGE || val_r == HUGE)
			{
				min_err[j] /= v30b;
				continue;
			}

			// 3 points show the minimum. Use parabolic approx. for the step
			double dl = val_l - val;
			double dr = val_r - val;
			double pos_x = 0.5 * (dl - dr) / (dl + dr);

			double val_p = fcn_delta(j, min_err[j] * pos_x);

			if (val_p < val) // Parabolic step is good (this is the case for about 80%)
			{
				min_par[j] += min_err[j] * pos_x;
				val = val_p;
				double delta = dabs(pos_x);
				if (delta < vdl)
					delta = vdl;
				min_err[j] *= delta * vdm;
			}
			else // Parabolic step failed
			{
				min_err[j] *= vpsf;
			}
		}

		// make dir step

		for (j = 0; j < min_npars; j++)
			dpar[j] = min_par[j] - par0[j]; // find step size

		for (j = 0; j < min_npars; j++)
			par0[j] = min_par[j]; // save pos

		double tau0 = -1; // position before coord-iter.
		//double val0; -- already found
		double tau1 = 0;  // position after coord-iter.
		double val1 = val;
		double tau2, val2; // position during sacn

		int cnt = 0;
		for (;;)
		{
			tau2 = tau1 + 1 + tau1*3;
			for (j = 0; j < min_npars; j++)
				min_par[j] = par0[j] + dpar[j] * tau2;
			val2 = fcn();
			if (val2 < val1)
			{
				tau0 = tau1;
				val0 = val1;
				tau1 = tau2;
				val1 = val2;
				cnt++;
			}
			else
				break;
		}
		/*// why this part do not make things much better? it seems it even worsens convergence!
		if (val0 < val1 || val1 < val2)
		{
			double y20 = val2 - val0;
			double y10 = val1 - val0;
			double y21 = val2 - val1;
			double x10 = tau1 - tau0;
			double x21 = tau2 - tau1;
			double tau_x = 0.5 * (tau2 + tau0 + y20 / (y10/x10 - y21/x21));
			if (0)
				fprintf (stderr, "tau0 % g tau1 %g tau2 %g -- tau_x %g -- ", tau0, tau1, tau2, tau_x);
			if (!finite(tau_x))
				tau_x = tau1;
			//tau_x = (int)tau_x;
			for (j = 0; j < min_npars; j++)
				min_par[j] = par0[j] + dpar[j] * tau_x;
			double val_x = fcn();
			if (val_x < val1) // Parabolic success
			{
				tau1 = tau_x;
				val1 = val_x;
				if (0)
					fprintf(stderr, "success\n");
			}
			else
			{
				if (0)
					fprintf(stderr, "fail\n");
			}
		}*/
		for (j = 0; j < min_npars; j++) // go to best pos (tau1)
			min_par[j] = par0[j] + dpar[j] * tau1;
		dir_steps += cnt;
		val = val1;
	}
	double n_val = fcn(); // this also fills linear parameters in linear mode
	delete[] par0;
	delete[] dpar;

	if (debug >=30)
		fprintf (stderr, "min_fit_x: it %3d sdir %3d n_val %.10g (%3.1f%%)\n", it, dir_steps, n_val, (int)(n_val*1000.0/val_ini)/10.0);
	return it;
}

void print_pars()
{
	int i;
	fprintf (stderr, "pars:\t");
	for (i = 0; i < min_npars; i++)
		fprintf(stderr, "  %g", min_par[i]);
	fprintf (stderr, "\n");
}

void min_fit()
{
	int i;

	int t0 = clock();

	if (0)
	{
		for (i = 0; i < 10000; i++)
			fcn();
		fprintf(stderr, "T = %d\n", clock() - t0);
		return;
	}

	min_prefit = 1;
	min_mfp->execCmdP(min_par, MF_CMD_FIX_RANGE_PREFIT);

	min_fit_x(2);

	double val;

	// very rough part

	if (debug >=20)
		print_pars();

	min_rough = 0;
	if (debug >= 20)
		fprintf(stderr, "very rough mode\n");

	min_fit_x(20);
	val = fcn();
	for (i = 0; i < 15; i++) // XXX: const
	{
		double oval = val;
		min_fit_x(20);
		val = fcn();
		if (val > oval * 0.9 || val < 1.0e-5) // XXX: const
			break;
	}
	
	if (debug >= 20)
		print_pars();

	//fprintf(stderr, "T = %d\n", clock() - t0);

	// special actions
	//if (MF_cmd(min_par, min_entry, MF_CMD_SECONDFIT))
	//{
	//	fprintf(stderr, "SECONDFIT\n");
	//	min_fit_x(20);
	//}
	//soften_pars();

	//fprintf(stderr, "T = %d\n", clock() - t0);

	// rough part

	min_rough = 1;
	if (debug >= 20)
		fprintf(stderr, "rough mode\n");

	min_fit_x(3, MIN_FIT_OPTION_X_ONLY);

	//min_fit_x(1);
	
	val = fcn();
	for (i = 0; i < 15; i++) // XXX: const
	{
		double oval = val;
		min_fit_x(5);
		val = fcn();
		if (val > oval * 0.85 || val < 1.0e-5) // XXX: const
			break;
	}

	//fprintf(stderr, "T = %d\n", clock() - t0);

	if (debug >= 20)
		print_pars();

	min_rough = 0;
	if (debug >= 20)
		fprintf(stderr, "precise mode\n");

	min_fit_x(3, MIN_FIT_OPTION_X_ONLY);

	//soften_pars();

	// fine part

	min_prefit = 0;

	val = fcn();
	for (i = 0; i < 25; i++) // XXX: const
	{
		double oval = val;
		min_fit_x(5);
		val = fcn();
		if (val > oval * 0.97 || val < 1.0e-4) // XXX
			break;
		if (val < 0.01 && val > oval * 0.9) // XXX
			break;
	}

	if (debug >= 20)
		print_pars();

	//fprintf(stderr, "T = %d\n", clock() - t0);
}

// фитируем кривую
// FIXIT: пока реализована только фитировка кривых с <= чем фиксированным числом параметров
// x=x0...x0+length-1; index=i0...i0+length-1
void fit(ModelF &mf, double *y, int i0, int x0, int length, fit_stat_res &stat)
{
	int npars = mf.getNPars();
	if (npars == 0 || npars > MF_MAX_FIXED_PARS || !mf.isCorrect())
		return;

	int i;

	// если все параметры линейны, то не тратим время на накладные расходы:
	// многократную фитировку, грубый/точный режимы, и т.д.
	// а сразу запускаем алгоритм линейной фитировки
	// (экономим до 20 раз!)
	for (i = 0; i < npars; i++)
		if (!(mf.getFlags(i) & MF_PAR_FLAG_L))
			break;
	if (i == npars)
	{
		fit_linear_only(mf, y, i0, x0, length);
		return;
	}

	min_lock();
	//min_fptr = funcs[mf.entry].fptr;
	min_data = y;
	min_i0 = i0;
	min_x0 = x0;
	min_length = length;
	min_mfp = &mf;
	min_npars = npars;
	min_par = 0; //mf.pars; -- will be initalized later!
	min_rough = 0;
	min_err = new double[npars];
	min_pflags = new int[npars];

	for (i = 0; i < npars; i++)
		min_err[i] = ERR_INITIAL_1;

	for (i = 0; i < npars; i++)
		min_pflags[i] = mf.getFlags(i);

	FILE *fpp = stdout;

	if (dump_event && x0 == dump_pos)
	{
		//fprintf (fpp, "fit: Optimizing, i0 %d x0 %d entry %d\n", i0, x0, entry);
		fprintf (fpp, "fit: Optimizing, i0 %d x0 %d ID %d\n", i0, x0, min_mfp->getID());
		fflush(stderr);

		fprintf (fpp, "initial pars:\n");
		for (i = 0; i < min_npars; i++)
			fprintf (fpp, "  %.16g", mf[i]);
		fprintf (fpp, "\n");

		fprintf (fpp, "event length: %d\n", length);
		fprintf (fpp, "event dump:\n");
		for (i = 0; i < length; i++)
			fprintf (fpp, "%g\n", y[i0 + i]);
	}

	int t0 = clock();

	static int ticks_total;

	if (x0 == 0)
		ticks_total = 0;

	double best_val = -1; // < 0 means "not initalized"
	double worst_val = -1;
	double avg_val = 0;
	stat.excess_count_1 = 0;
	stat.excess_count_2 = 0;

	double markv[Nfits];

	//char marks[Nfits + 1];
	int ntry;
	for (ntry = 0; ntry < Nfits; ntry++)
	{
		double pars[MF_MAX_FIXED_PARS];
		for (i = 0; i < min_npars; i++)
			pars[i] = mf[i] + rand() * RAND_MULTIPLIER / RAND_MAX; // XXX
		min_par = pars;

		min_fit();

		double val = mf.RMS2P(pars, min_data, min_i0, min_x0, min_length, 0);
		//int mark = ocenka_za_rabotu(val);
		//marks[ntry] = '0' + mark;
		markv[ntry] = val;
		if (debug >= 10)
		{
			fprintf(stderr, "fit: try %d: RMS %g\n", ntry, val);
		}
		if (val < best_val || best_val < 0)
		{
			best_val = val;
			for (i = 0; i < min_npars; i++)
				mf[i] = pars[i];
		}
		if (val > worst_val)
			worst_val = val;

		avg_val += val;

		if (val > stat.ok_level_1)
			stat.excess_count_1++;

		if (val > stat.ok_level_2)
			stat.excess_count_2++;
	}
	//marks[Nfits] = 0;
	min_par = mf.getP();
	avg_val /= Nfits;
	stat.nfits = Nfits;

	if (debug >= 8)
	{
		fprintf (stderr, "final pars:\n");
		for (i = 0; i < min_npars; i++)
			fprintf (stderr, "  %g", mf[i]);
		fprintf (stderr, "\n");
	}

	if (debug >= 5)
	{
		double thresh = best_val * 2.0; // XXX
		if (worst_val > thresh)
		{
			int j;
			int nbad = 0;
			for (j = 0; j < Nfits; j++)
				if (markv[j] > thresh)
					nbad++;
			fprintf(stderr, "Warning: fit: nbad %d/%d x0 %d best_val / worst_val = %g\n", nbad, (int )Nfits, x0, best_val / worst_val);
			fflush(stderr);
		}
	}

	int t1 = clock();
	ticks_total += t1 - t0;

	if (debug >= 10)
	{
		fprintf(stdout, "fit: ID %d x0 %d -- Done; dt %d total ticks %d; val %g\n",
			mf.getID(), x0, t1-t0, ticks_total,
			mf.RMS2(min_data, min_i0, min_x0, min_length, 0));
		fflush(stdout);
	}
	/*if (debug >= 0)
	{
		if (ocenka_za_rabotu(worst_val) <= 2)
		{
			fprintf(stderr, "fit: entry %d x0 %d -- Warning: marks are %s dt %d\n",
				entry, x0, marks, t1-t0);
			fflush(stderr);
		}
	}*/

	delete[] min_err;
	delete[] min_pflags;

	stat.rms_avg = avg_val;
	stat.rms_worst = worst_val;

	//return best_val > stat.ok_level_1; //ocenka_za_rabotu(worst_val) <= 2;
}

// Фитировка линейных параметров.
// Только для функций с фиксированным числом аргументов,
// сигнатурой и линейными параметрами. Для других не делает ничего.
void fit_linear_only(ModelF &mf, double *y, int i0, int x0, int length)
{
	/*
	int npars = mf.getNPars();
	int i;
	fprintf(stderr, "fit_linear_only(i0 %d x0 %d)\n", i0, x0);

	fprintf(stderr, "ID %d pars before =", mf.getID());
	for (i = 0; i < npars; i++)
		fprintf(stderr, " %g", mf[i]);
	fprintf(stderr, "\n");
	*/
	
	mf.RMS2Lin(y, i0, x0, length, 0);
	
	/*
	fprintf(stderr, "ID %d pars after  =", mf.getID());
	for (i = 0; i < npars; i++)
		fprintf(stderr, " %g", mf[i]);
	fprintf(stderr, "\n");
	*/

	//fflush(stderr); // XXX

}

