#include <assert.h>
#include <math.h>
#include "sweep.h"
#include "BreakL-fit.h"

#include "../Common/prf.h"

void prepare_acc_stats( // подготавливает данные для быстрого вычисления моментов (x,y)
	double *y,
	int n,
	double *acc_y,
	double *acc_xy,
	double *acc_yy)
{
	int i;
	double sy = 0;
	double sxy = 0;
	double syy = 0;

	for (i = 0;; i++)
	{
		acc_y[i] = sy;
		acc_xy[i] = sxy;
		acc_yy[i] = syy;
		if (i == n)
			break;
		sy += y[i];
		sxy += i * y[i];
		syy += y[i] * y[i];
	}
}

// быстрое вычисление вторых моментов (x,y) на основе предварительных данных
inline void calc_yarr_stat3(
	double *acc_y,
	double *acc_xy,
	double *acc_yy,
	int i0,
	int n,
	double &mx,
	double &my,
	double &mxx,
	double &mxy,
	double &myy)
{
	my = (acc_y[i0 + n] - acc_y[i0]) / n;
	mxy = (acc_xy[i0 + n] - acc_xy[i0]) / n;
	myy = (acc_yy[i0 + n] - acc_yy[i0]) / n;

	double i0d = i0;
	double i0pn = i0 + n;
	mx = i0 + (n - 1) / 2.0;
	mxx = (i0pn * (2 * i0pn - 1) * (i0pn - 1) - i0d * (2 * i0d - 1) * (i0d - 1))
			/ (6 * n);
}

inline double div(double a, double b)
{
	return a ? a / b : 0;
}

inline double dpow2(double v)
{
	return v * v;
}

int BreakL_getLinkDataLength(int linkFlags)
{
	return linkFlags & BREAKL_LINK_FIXLEFT
		? 1
		: 0;
}
int BreakL_getLinkDataOffset(int linkFlags, int desiredData)
{
	return 0;
}

// XXX: похоже, результат improveBySweep не точно удовлетворяет этому критерию, хотя
// математически должен. Поэтому есть подозрение на ошибку в одном из двух.
static double FindTwoLinApproxRMS(double *acc_y, double *acc_xy, double *acc_yy, int i0, int i1, int i2, double y0_fixed, double &y1_free, double y2_fixed)
{
	double ret = 0;
	double mx, my, mxx, mxy, myy;
	calc_yarr_stat3(acc_y, acc_xy, acc_yy, i0, i1 - i0, mx, my, mxx, mxy, myy);
	double vy0 = mxy - i0 * my - y0_fixed * mx + i0 * y0_fixed;
	double vx0 = mxx - 2 * mx * i0 + i0 * i0;
	ret += myy - my * my + dpow2(my - y0_fixed);
	calc_yarr_stat3(acc_y, acc_xy, acc_yy, i1, i2 - i1, mx, my, mxx, mxy, myy);
	double vy2 = mxy - i2 * my - y2_fixed * mx + i2 * y2_fixed;
	double vx2 = mxx - 2 * mx * i2 + i2 * i2;
	ret += myy - my * my + dpow2(my - y2_fixed);
	vx0 /= i1 - i0;
	vx2 /= i2 - i1;
	double y1 = div(vy0 - vy2 + y0_fixed * vx0 + y2_fixed * vx2, vx0 + vx2);
	y1_free = y1;
	return
		ret
		+ (y1 - y0_fixed) / (i1 - i0) * ((y1 - y0_fixed) * vx0 - 2 * vy0)
		+ (y1 - y2_fixed) / (i1 - i2) * (-(y1 - y2_fixed) * vx2 - 2 * vy2);
}

static void improveBySweep(double *acc_y, double *acc_xy, double *acc_yy,
						   double *xarr, double *yarr, int nPts,
						   int linkFlags, const double *linkData)
{
	// улучшаем аппроксимацию по RMS (возможно, при этом где-то будет превышен уровень шума)

	if (nPts <= 0)
		return;

	if (nPts <= 0)
		return;

	double *A = new double[nPts];
	double *B = new double[nPts];
	double *C = new double[nPts];
	double *D = new double[nPts];
	assert(A);
	assert(B);
	assert(C);
	assert(D);

	int k;

	double sum = 0;
	for (k = 0; k < nPts; k++)
	{
		A[k] = B[k] = C[k] = D[k] = 0;
		//double vvv = 0;

		if (k == 0 && (linkFlags & BREAKL_LINK_FIXLEFT))
		{
			B[k] = -1;
			D[k] = linkData[BreakL_getLinkDataOffset(linkFlags, BREAKL_LINK_FIXLEFT)];
			continue;
		}

		if (k != 0)
		{
			int i0 = (int )xarr[k - 1];
			int i1 = (int )xarr[k];
			double mx, my, mxx, mxy, myy;
			calc_yarr_stat3(acc_y, acc_xy, acc_yy, i0, i1 - i0, mx, my, mxx, mxy, myy);
			double vy = mxy - i0 * my;
			double md2 = mxx - 2 * mx * i0 + i0 * i0;
			A[k] = -md2 + (mx - i0) * (i1 - i0);
			B[k] -= md2;
			D[k] += vy * (i1 - i0);
		}
		{
			int i1 = (int )xarr[k];
			int i2 = k == nPts - 1 ? i1 + 1 : (int )xarr[k + 1];
			double mx, my, mxx, mxy, myy;
			calc_yarr_stat3(acc_y, acc_xy, acc_yy, i1, i2 - i1, mx, my, mxx, mxy, myy);
			double vy = mxy - i2 * my;
			double md2 = mxx - 2 * mx * i2 + i2 * i2;
			//assert(md2 >= 0);
			C[k] = -md2 + (-mx + i2) * (i2 - i1);
			B[k] -= md2;
			D[k] -= vy * (i2 - i1);
		}
		//vvv -= w3[k] * B[k];
		//printf("%3d %15g %15g %15g\n", k, vvv, D[k], D[k] - vvv);
	}
	sweep(nPts - 1, A, B, C, D, yarr);

	delete[] A;
	delete[] B;
	delete[] C;
	delete[] D;
}

/*
 * Фитировка ломаной.
 * mf - фитируемый объект,
 * data - массив с данными (используются элементы [0] .. [length-1])
 * x_begin - x-координата начала ломаной и массива
 * length - число фитируемых точек
 * quick - "быстрый" режим - немного жертвуем точностью
 * error1 - постоянная компонента погрешности
 * error2 - компонента погрешности, растущая с уменьшением уровня сигнала (rgdB)
 * maxpoints - уменьшает точность до заданного макс. числа узлов
 * linearOnly - только лин. режим. quick, error1, error2, maxpoints не используются
 * linkFlags - флаги связывания с соседними участками
 * linkData - данные для связывания с соседними участками
 */
void BreakL_Fit_int (ModelF &mf, double *data, int x_begin,
	int length, int quick, double error1, double error2, int maxpoints,
	int linearOnly, int linkFlags, double *linkData)
{
	//fprintf(stdout, "BreakL_Fit: i0 %d x0 %d len %d q %d  e1 %g e2 %g mp %d lin %d; mf.nPars %d\n",
	//	i_begin, x_begin, length, quick, error1, error2, maxpoints, linearOnly, mf.getNPars());

	if (length <= 1)
	{
		mf.init(MF_ID_BREAKL, length * 2);
		if (length)
		{
			mf[0] = x_begin;
			mf[1] = data[0];
		}
		return;
	}

	int size = length;

	double *acc_y = new double[size + 1];
	double *acc_xy = new double[size + 1];
	double *acc_yy = new double[size + 1];
	assert(acc_y);
	assert(acc_xy);
	assert(acc_yy);

	prepare_acc_stats(data, size, acc_y, acc_xy, acc_yy);

	double *thresh = new double[size];
	double *ax = new double[size + 1]; // XXX: должно хватать и [size]
	double *ay = new double[size + 1]; // XXX: -//-
	assert(thresh);
	assert(ax);
	assert(ay);

	int i;
	int nPts;
	int k;

	if (!linearOnly)
	{
		for (i = 0; i < size; i++)
		{
			double v = (error2 - data[i]) / 5.0;
			v = 1 + pow(10.0, v);
			thresh[i] = log10(v) * 5.0 + error1;
		}

		// пробуем изменять пороги
		double th_mult = 1.0;
		for(;;)
		{
			// определяем точки с превышением порога, формируем и выводим аппроксимацию
			int i_prev = 0;
			double x0 = i_prev; // note: x0 == i_prev всегда - одну переменную можно убрать
			double y0 = data[i_prev]; // XXX
			double a_prev = 0;
			//printf("%g %g\n", x0, y0);
			nPts = 0;
			ax[nPts] = x0;
			ay[nPts++] = y0;

			for (i = 1; i <= size; i++)
			{
				// пробуем провести прямую с учетом i-й точки
				if (i < size)
				{
					double mx, my, mxx, mxy, myy;
					double a;
					calc_yarr_stat3(acc_y, acc_xy, acc_yy, i_prev, i - i_prev + 1, mx, my, mxx, mxy, myy);
					a = div(
						((mx - x0) * (my - y0) + mxy - mx * my),
						(dpow2(mx - x0) + mxx - mx * mx));
					int j;
					for (j = i_prev; j <= i; j++)
					{
						if (fabs(a * (j - x0) + y0 - data[j])
							>	thresh[j] * th_mult // заданный порог
								+ (fabs(a * (j - x0)) + fabs(y0)) * 1e-14 // добавляем машинную погрешность
								+ 1e-15 // про запас - XXX: нужен ли?
							)
							break;
					}
					if (j > i) // уточненное значение наклона старой кривой
					{
						a_prev = a;
						continue;
					}
				}

				// прямую провести не удалось - заканчиваем предыдущую на i-1
				// и начинаем новую прямую по i-1, i
				i_prev = i - 1;
				y0 = y0 + a_prev * (i_prev - x0);
				x0 = i_prev;
				ax[nPts] = x0;
				ay[nPts++] = y0;

				if (i < size)
				{
					double mx, my, mxx, mxy, myy;
					calc_yarr_stat3(acc_y, acc_xy, acc_yy, i_prev, i - i_prev + 1, mx, my, mxx, mxy, myy);
					a_prev = div(
						((mx - x0) * (my - y0) + mxy - mx * my),
						(dpow2(mx - x0) + mxx - mx * mx));
				}
				//fprintf(stdout, "%g %g\n", x0, y0);
			}

			improveBySweep(acc_y, acc_xy, acc_yy, ax, ay, nPts, linkFlags, linkData);

			if (maxpoints <= 1 || nPts <= maxpoints)
				break;
			th_mult *= 1.5;
			if (th_mult > 1e3)
			{
				fprintf(stderr, "BreakL_Fit: max th_mult reached\n");
				fflush(stderr);
				break;
			}
		}
		//fprintf(stderr, "BreakL_Fit: final th_mult %g, points %d maxpoints %d\n",
		//	th_mult, nPts, maxpoints);
		//fflush(stderr);

		// двигаем i_k для улучшения RMS
		int it;
		for (it = 0; it < 1 && !quick; it++)
		{
			int itSuccess = 0;
			for (k = 1; k < nPts - 1; k++)
			{
				int i0 = (int )ax[k - 1];
				int i1 = (int )ax[k];
				int i2 = (int )ax[k + 1];
				double y0 = ay[k - 1];
				double y1;
				double y2 = ay[k + 1];
				int kSuccess = 0;
				int step;
				const int maxstep = 2; // XXX
				// try step left
				for (step = i1 - i0; step > 0; step /= 2)
				{
					if (step > maxstep)
						step = maxstep;
					if (i1 - step <= i0)
						continue;
					if (FindTwoLinApproxRMS(acc_y, acc_xy, acc_yy, i0, i1 - step, i2, y0, y1, y2)
							< FindTwoLinApproxRMS(acc_y, acc_xy, acc_yy, i0, i1, i2, y0, y1, y2))
					{
						//fprintf(stderr, "step: ev %d i %d -= %d RMS %g -> %g\n", k, i1, step,
						//	FindTwoLinApproxRMS(data, i0, i1, i2, y0, y1, y2),
						//	FindTwoLinApproxRMS(data, i0, i1 - step, i2, y0, y1, y2));
						i1 -= step;
						ax[k] = i1;
						FindTwoLinApproxRMS(acc_y, acc_xy, acc_yy, i0, i1, i2, y0, ay[k], y2);
					}				
				}
				// try step right
				for (step = i2 - i1; step > 0; step /= 2)
				{
					if (step > maxstep)
						step = maxstep;
					if (i1 - step >= i2)
						continue;
					if (FindTwoLinApproxRMS(acc_y, acc_xy, acc_yy, i0, i1 + step, i2, y0, y1, y2)
							< FindTwoLinApproxRMS(acc_y, acc_xy, acc_yy, i0, i1, i2, y0, y1, y2))
					{
						//fprintf(stderr, "step: ev %d i %d += %d RMS %g -> %g\n", k, i1, step,
						//	FindTwoLinApproxRMS(data, i0, i1, i2, y0, y1, y2),
						//	FindTwoLinApproxRMS(data, i0, i1 + step, i2, y0, y1, y2));
						i1 += step;
						ax[k] = i1;
						FindTwoLinApproxRMS(acc_y, acc_xy, acc_yy, i0, i1, i2, y0, ay[k], y2);
					}
				}
			}
			improveBySweep(acc_y, acc_xy, acc_yy, ax, ay, nPts, linkFlags, linkData);
		}

		mf.init(MF_ID_BREAKL, nPts * 2);
		assert(mf.getNPars() == nPts * 2);
	}
	else // linear only mode
	{
		// опредетяем число звеньев
		nPts = mf.getNPars() / 2;

		// проверяем, что массивы ax, ay достаточного размера
		assert (nPts <= size);

		// считываем x-координаты
		for (k = 0; k < nPts; k++)
			ax[k] = mf[k * 2] - x_begin;

		// вычисляем оптимальные y-координаты
		improveBySweep(acc_y, acc_xy, acc_yy, ax, ay, nPts, linkFlags, linkData);
	}

	// сохраняем в mf (число звеньев уже должно совпалать с числом параметров)
	for (k = 0; k < nPts; k++)
	{
		mf[k * 2] = ax[k] + x_begin;
		mf[k * 2 + 1] = ay[k];
	}

	//fprintf(stdout, "BreakL_Fit: done, nPts %d\n", nPts);
	//fflush(stdout);

	delete[] ax;
	delete[] ay;
	delete[] thresh;

	delete[] acc_y;
	delete[] acc_xy;
	delete[] acc_yy;
}

void BreakL_FitLinear (ModelF &mf, double *data0, int i0, int x0, int length, int linkFlags, double *linkData)
{
	BreakL_Fit_int(mf, data0 + i0, x0, length, 0, 0, 0, 0, 1, linkFlags, linkData);
}
void BreakL_Fit (ModelF &mf, double *data0, int i0, int x0, int length, int quick, double error1, double error2, int maxpoints, int linkFlags, double *linkData)
{
	BreakL_Fit_int(mf, data0 + i0, x0, length, quick, error1, error2, maxpoints, 0, linkFlags, linkData);
}

