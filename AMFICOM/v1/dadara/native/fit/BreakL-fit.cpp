#include <assert.h>
#include <math.h>
#include <stdlib.h> // qsort
#include "sweep.h"
#include "BreakL-fit.h"

#include "../Common/prf.h"

#define USE_CHI2BREAKL 1

static int dfcmp(const void *a, const void *b)
{
	const double *x = (const double *)a;
	const double *y = (const double *)b;
	return *x <= *y ? *x < *y ? -1 : 0 : 1;
}

typedef struct
{
	double *y;
	double *xy;
	double *yy;
	void allocate(int N) // remember to provide N = size + 1
	{
		y = new double[N];
		xy = new double[N];
		yy = new double[N];
		assert(y);
		assert(xy);
		assert(yy);
	};
	void dispose()
	{
		delete[] y;
		delete[] xy;
		delete[] yy;
	};
} ACC_Y;

void prepare_acc_stats( // подготавливает данные для быстрого вычисления моментов (x,y)
	double *y,
	int n,
	ACC_Y &acc)
{
	int i;
	double sy = 0;
	double sxy = 0;
	double syy = 0;

	for (i = 0;; i++)
	{
		acc.y[i] = sy;
		acc.xy[i] = sxy;
		acc.yy[i] = syy;
		if (i == n)
			break;
		sy += y[i];
		sxy += i * y[i];
		syy += y[i] * y[i];
	}
}

// быстрое вычисление вторых моментов (x,y) на основе предварительных данных
inline void calc_yarr_stat3(
	ACC_Y &acc,
	int i0,
	int n,
	double &mx,
	double &my,
	double &mxx,
	double &mxy,
	double &myy)
{
	my = (acc.y[i0 + n] - acc.y[i0]) / n;
	mxy = (acc.xy[i0 + n] - acc.xy[i0]) / n;
	myy = (acc.yy[i0 + n] - acc.yy[i0]) / n;

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

// суммарный квадрат отклонения на промежутке [i0, i1)
static double FindFixedLineSumS(ACC_Y &acc, int i0, int i1, double y0, double y1)
{
	int N = i1 - i0;
	if (N == 0)
		return 0;

	double mx, my, mxx, mxy, myy;
	calc_yarr_stat3(acc, i0, N, mx, my, mxx, mxy, myy);

	double a = (y1 - y0) / N;
	double b = y0 - a * i0;
	return N * (dpow2(b) + dpow2(a) * mxx + myy
	            + 2.0 * (a * b * mx - b * my - a * mxy));
}

// на двухзвенной ломаной выбирает и устанавливает опт. значение y центральной точки,
// возвращает суммарный квадрат отклонения на промежутке [i0, i2)
static double FindTwoLinApproxSumS(ACC_Y &acc, int i0, int i1, int i2, double y0_fixed, double &y1_free, double y2_fixed)
{
	double ret = 0;
	double mx, my, mxx, mxy, myy;

	calc_yarr_stat3(acc, i0, i1 - i0, mx, my, mxx, mxy, myy);
	double vy0 = mxy - i0 * my - y0_fixed * mx + i0 * y0_fixed;
	double vx0 = mxx - 2 * mx * i0 + i0 * i0;
	ret += (i1 - i0) * (myy - my * my + dpow2(my - y0_fixed));

	calc_yarr_stat3(acc, i1, i2 - i1, mx, my, mxx, mxy, myy);
	double vy2 = mxy - i2 * my - y2_fixed * mx + i2 * y2_fixed;
	double vx2 = mxx - 2 * mx * i2 + i2 * i2;
	ret += (i2 - i1) * (myy - my * my + dpow2(my - y2_fixed));

	vx0 /= i1 - i0;
	vx2 /= i2 - i1;
	double y1 = div(vy0 - vy2 + y0_fixed * vx0 + y2_fixed * vx2, vx0 + vx2);
	y1_free = y1;

	return
		ret
		+ ((y1 - y0_fixed) * vx0 - 2 * vy0) * (y1 - y0_fixed)
		+ ((y1 - y2_fixed) * vx2 + 2 * vy2) * (y1 - y2_fixed);
}

static void improveBySweep(ACC_Y &acc,
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
			calc_yarr_stat3(acc, i0, i1 - i0, mx, my, mxx, mxy, myy);
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
			calc_yarr_stat3(acc, i1, i2 - i1, mx, my, mxx, mxy, myy);
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

typedef struct
{
	double a0;
	double a1;
	double a2;
	void reset()
	{
		a0 = a1 = a2 = 0.0;
	};
} ACC_XI2;

void update_acc_xi2(ACC_XI2 &acc_xi2, double dx, double y0, double f, double th)
{
	// добавляем маш. погрешность
	th += 1e-15 + (fabs(f - y0) + fabs(y0)) * 1e-14;
	double th2_here = th * th;
	//double xi_here = dpow2((a * dx + y0 - f) / th);
	double xi_here_a0 = dpow2(y0 - f) / th2_here;
	double xi_here_a1 = 2 * dx * (y0 - f) / th2_here;
	double xi_here_a2 = dpow2(dx) / th2_here;
	acc_xi2.a0 += xi_here_a0;
	acc_xi2.a1 += xi_here_a1;
	acc_xi2.a2 += xi_here_a2;
}

/*
 * Фитировка ломаной.
 * mf - фитируемый объект,
 * data - массив с данными (используются элементы [0] .. [length-1])
 * x_begin - x-координата начала ломаной и массива
 * length - число фитируемых точек
 * quick - "быстрый" режим - немного жертвуем точностью
 * error1 - постоянная компонента погрешности (используется тольео если noise == 0)
 * error2 - компонента погрешности, растущая с уменьшением уровня сигнала (rgdB) (используется тольео если noise == 0)
 * maxpoints - уменьшает точность до заданного макс. числа узлов
 * linearOnly - только лин. режим. quick, error1, error2, maxpoints не используются
 * linkFlags - флаги связывания с соседними участками
 * linkData - данные для связывания с соседними участками
 * noise - величина погрешности (дБ) - если не null, то вместо нее используются error1, error2
 */
void BreakL_Fit_int (ModelF &mf, double *data, int x_begin,
	int length, int quick, double error1, double error2, int maxpoints,
	int linearOnly, int linkFlags, double *linkData, double *noise)
{
	//fprintf(stdout, "BreakL_Fit: i0 %d x0 %d len %d q %d  e1 %g e2 %g mp %d lin %d; mf.nPars %d\n",
	//	i_begin, x_begin, length, quick, error1, error2, maxpoints, linearOnly, mf.getNPars());

	prf_b("BreakL_Fit_int: enter");

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

	ACC_Y acc;
	acc.allocate(size + 1);

	prepare_acc_stats(data, size, acc);

	double *thresh = new double[size];
	double *ax = new double[size + 1]; // XXX: должно хватать и [size]
	double *ay = new double[size + 1]; // XXX: -//-
	assert(thresh);
	assert(ax);
	assert(ay);

	int i;
	int nPts;
	int k;

	prf_b("BreakL_Fit_int: prepared");

	if (!linearOnly)
	{
		prf_b("BreakL_Fit_int: !linearOnly: init thresholds");

		if (noise)
		{
			for (i = 0; i < size; i++)
				thresh[i] = noise[i];
		}
		else
		{
			for (i = 0; i < size; i++)
			{
				double v = (error2 - data[i]) / 5.0;
				thresh[i] = log10(1.0 + pow(10.0, v)) * 5.0 + error1;
			}
		}

		prf_b("BreakL_Fit_int: !linearOnly: draw BreakL");

		double th_mult = 1.0; // пробуем изменять пороги
		for(;;)
		{
			//prf_b("BreakL_Fit_int: !linearOnly: draw BreakL: try");

			// определяем точки с превышением порога, формируем и выводим аппроксимацию
			int i_prev = 0;
			double x0 = i_prev; // note: x0 == i_prev всегда - одну переменную можно убрать
			double y0 = data[i_prev]; // XXX
			double a_prev = 0;
#if USE_CHI2BREAKL
			ACC_XI2 acc_xi2;
			acc_xi2.reset();
#endif
			//printf("%g %g\n", x0, y0);
			nPts = 0;
			ax[nPts] = x0;
			ay[nPts++] = y0;
			//update_acc_xi2(...); - не нужен, т.к. начинаем строго из точки р/г

			for (i = 1; i <= size; i++)
			{
				// пробуем провести прямую с учетом i-й точки
				if (i < size)
				{
					double mx, my, mxx, mxy, myy;
					double a;
					calc_yarr_stat3(acc, i_prev, i - i_prev + 1, mx, my, mxx, mxy, myy);
					a = div(
						((mx - x0) * (my - y0) + mxy - mx * my),
						(dpow2(mx - x0) + mxx - mx * mx));

					// проверяем, удовлетворяет ли нас такая точность
					int fail = 0;
//--------------------------
#if USE_CHI2BREAKL
					update_acc_xi2(acc_xi2, i - x0, y0, data[i], thresh[i] * th_mult);

					double acc = acc_xi2.a0 + acc_xi2.a1 * a + acc_xi2.a2 * a * a;
					int count = i - i_prev + 1;

					// XXX: двойной цикл - самая медленная часть алгоритма (~70% времени)
					// сильно оптимизировать его не удается,
					// но можно попробовать переделать алгоритм, чтобы
					// не каждый раз проводить новую прямую,
					// а сначала попробовать старую, с учетом её хи квадрат;
					// или использовать разложение зависимости хи-квадрат
					// от a в виде A a^2 + B a + C
					/*int j;
					for (j = i_prev; j <= i; j++)
					{
						double dif = a * (j - x0) + y0 - data[j];
						double th = 
							thresh[j] * th_mult // заданный порог
							+ machineError;  // добавляем машинную погрешность
						// усложненный критерий - на основе хи-квадрат
						acc += dpow2(dif / th);
					}*/
					count -= 2;
					if (count > 0)
					{
						double xi_av = acc * 9.0 / 2.0 / count;
						count = count / 8 + 1; // XXX: nettest specific
						if (count <= 1)
							fail = xi_av > 5.4;
						else if (count == 2)
							fail = xi_av > 3.5;
						else if (count == 3)
							fail = xi_av > 2.7;
						else if (count <= 5)
							fail = xi_av > 2.0;
						else if (count <= 10)
							fail = xi_av > 1.5;
						else if (count <= 30)
							fail = xi_av > 1.0;
						else
							fail = xi_av > 0.75; // xi_av <= 100
					}
					else
						fail = 0; // прямая проведена по двум точкам
#else
//--------------------------
					// XXX: двойной цикл - самая медленная часть алгоритма (~70% времени)
					int j;
					for (j = i_prev; j <= i; j++)
					{
						double dif = a * (j - x0) + y0 - data[j];
						double th = 
							thresh[j] * th_mult // заданный порог
							+ machineError;  // добавляем машинную погрешность
						// простой критерий - выход за предел
						if (dif > th || dif < -th)
							fail = 1;
					}
#endif
//--------------------------
					if (!fail) // уточненное значение наклона старой кривой
					{
						a_prev = a;
						continue;
					}
				}

				//prf_b("BreakL_Fit_int: !linearOnly: draw BreakL: try: i: start new line");

				// прямую провести не удалось - заканчиваем предыдущую на i-1
				// и начинаем новую прямую по i-1, i
				i_prev = i - 1;
				y0 = y0 + a_prev * (i_prev - x0);
				x0 = i_prev;
				ax[nPts] = x0;
				ay[nPts++] = y0;
				if (i < size)
				{
#if USE_CHI2BREAKL
					acc_xi2.reset();
					update_acc_xi2(acc_xi2, 0, y0, data[i_prev], thresh[i_prev]);
					update_acc_xi2(acc_xi2, 1, y0, data[i], thresh[i]);
#endif
					double mx, my, mxx, mxy, myy;
					calc_yarr_stat3(acc, i_prev, i - i_prev + 1, mx, my, mxx, mxy, myy);
					a_prev = div(
						((mx - x0) * (my - y0) + mxy - mx * my),
						(dpow2(mx - x0) + mxx - mx * mx));
				}
				//fprintf(stdout, "%g %g\n", x0, y0);
				//prf_b("BreakL_Fit_int: !linearOnly: draw BreakL: try: i: done");
			}

			//prf_b("BreakL_Fit_int: !linearOnly: draw BreakL: try: impoving");

			improveBySweep(acc, ax, ay, nPts, linkFlags, linkData);

			if (maxpoints <= 1 || nPts <= maxpoints)
				break;
			th_mult *= 1.5;
			if (th_mult > 1e3)
			{
				fprintf(stderr, "BreakL_Fit: max th_mult reached\n");
				fflush(stderr);
				break;
			}
			//prf_b("BreakL_Fit_int: !linearOnly: draw BreakL: try: next");

		}
		//fprintf(stderr, "BreakL_Fit: final th_mult %g, points %d maxpoints %d\n",
		//	th_mult, nPts, maxpoints);
		//fflush(stderr);

		if (!quick) { prf_b("BreakL_Fit_int: !linearOnly: remove afterPeaks"); }

		// Убираем пост-всплески:
		// просматриваем все смежные тройки звеньев.
		// если продолжение первого и третьего до взаимного пересечения
		// с выкидываением второго дают лучшую RMS аппроксимацию,
		// то так и делаем.
		// Пробуем также улучшить разбиение
		// с уменьшением числа звеньев еще одним способом.
		int it;
		for (it = 0; it < 1 && !quick && 1; it++)
		{
			for (k = 0; k < nPts - 3; k++)
			{
				//fprintf(stderr, "(%d)\n", k);
				int i0 = (int )ax[k];
				int i1 = (int )ax[k + 1];
				int i2 = (int )ax[k + 2];
				int i3 = (int )ax[k + 3];
				double y0 = ay[k];
				double y1 = ay[k + 1];
				double y2 = ay[k + 2];
				double y3 = ay[k + 3];

				// точность аппроксимации старой кривой
				double sOld = FindFixedLineSumS(acc, i0, i1, y0, y1)
					        + FindFixedLineSumS(acc, i1, i2, y1, y2)
					        + FindFixedLineSumS(acc, i2, i3, y2, y3);

				// новые кривые и их точность
				int iN = 0;
				double yN = 0;
				double sN = sOld + 1.0; // "invalid"

				// определяем координаты iN, yN точки пересечения и соотв. точность
				double a1 = (y1 - y0) / (i1 - i0);
				double a2 = (y3 - y2) / (i3 - i2);
				if (fabs(a1 - a2) > fabs(a1 + a2) * 0.01) // XXX: constant
				{
					double t = y2 - y0 + a1 * i0 - a2 * i2;
					t = t / (a1 - a2);
					iN = (int )(t + 0.5);
					if (iN > i0 && iN < i3)
					{
						// ^^^ быть может, стоить отбрасывать также
						// и случаи (iN == i0 + 1 || iN == i3 - 1) ?
						yN = fabs(a1) < fabs(a2) // берем наименее наклонную кривую
							? a1 * (iN - i0) + y0
							: a2 * (iN - i2) + y2;

						// считаем сумму квадратов новой кривой
						sN = FindFixedLineSumS(acc, i0, iN, y0, yN)
						   + FindFixedLineSumS(acc, iN, i3, yN, y3);
					}
				}

				// определяем параметры варианта укорочения первого отрезка
				int iA = i0 + (i1 - i0) * 2 / 3;
				double yA = 0;
				double sA = sOld + 1.0;
				if (iA > i0 && iA < i1)
				{
					sA = FindTwoLinApproxSumS(acc, i0, iA, i3, y0, yA, y3);
					//yA = a1 * (iA - i0) + y0;
					//sA = FindFixedLineSumS(acc, i0, iA, y0, yA)
					//   + FindFixedLineSumS(acc, iA, i3, yA, y3);
				}

				// выбираем наиболее подходящий способ улучшения
				int shift = 0;
				if (sN < sOld && sN <= sA)
				{
					//fprintf(stderr, "rem-N: # %d-%d x(%d:%d:%d:%d -> %d): RMS %g -> %g\n",
					//	k, k + 3, i0, i1, i2, i3, iN, sOld, sN);
					//fflush(stderr);
					ax[k + 1] = iN;
					ay[k + 1] = yN;
					shift = 1;
				}
				else if (sA < sOld)
				{
					//fprintf(stderr, "rem-A: # %d-%d x(%d:%d:%d:%d -> %d): RMS %g -> %g\n",
					//	k, k + 3, i0, i1, i2, i3, iA, sOld, sA);
					fflush(stderr);
					ax[k + 1] = iA;
					ay[k + 1] = yA;
					shift = 1;
				}
				if (shift)
				{
					// укорачиваем массив звеньев сдвигом
					int p;
					for (p = k + 2; p < nPts - 1; p++)
					{
						ax[p] = ax[p + 1];
						ay[p] = ay[p + 1];
					}
					nPts--;
				}
			}
		}

		if (!quick) { prf_b("BreakL_Fit_int: !linearOnly: move i_k"); }

		// двигаем i_k для улучшения RMS
		for (it = 0; it < 2 && !quick; it++) // XXX: it < 2? it < 1?
		{
			int itSuccess = 0;
			for (k = 1; k < nPts - 1; k++)
			{
				int i0 = (int )ax[k - 1];
				int i1 = (int )ax[k];
				int i2 = (int )ax[k + 1];
				double y0 = ay[k - 1];
				double y2 = ay[k + 1];
				int kSuccess = 0; // XXX: optimization variables are not used
				int step;
				const int maxstep = 2000; // XXX
				const int initialStepDiv = 3;
				// try step left
				for (step = (i1 - i0) / initialStepDiv; step > 0; step /= 2)
				{
					if (step > maxstep)
						step = maxstep;
					if (i1 - step <= i0)
					{
						fprintf(stderr, "XXX: L#1\n"); fflush(stderr);
						continue;
					}
					double y1o, y1n;
					if (FindTwoLinApproxSumS(acc, i0, i1 - step, i2, y0, y1n, y2)
							< FindTwoLinApproxSumS(acc, i0, i1, i2, y0, y1o, y2))
					{
						//double temp1, temp2;
						//fprintf(stderr, "step: ev %d i %d -= %d S %g -> %g\n", k, i1, step,
						//	FindTwoLinApproxSumS(acc, i0, i1, i2, y0, temp1, y2),
						//	FindTwoLinApproxSumS(acc, i0, i1 - step, i2, y0, temp2, y2));
						i1 -= step;
						ax[k] = i1;
						ay[k] = y1n;
					}
				}
				// try step right
				for (step = (i2 - i1) / initialStepDiv; step > 0; step /= 2)
				{
					if (step > maxstep)
						step = maxstep;
					if (i1 + step >= i2)
					{
						fprintf(stderr, "XXX: R#1 %d+%d >= %d\n", i1, step, i2); fflush(stderr);
						continue;
					}
					double y1o, y1n;
					if (FindTwoLinApproxSumS(acc, i0, i1 + step, i2, y0, y1n, y2)
							< FindTwoLinApproxSumS(acc, i0, i1, i2, y0, y1o, y2))
					{
						//double temp1, temp2;
						//fprintf(stderr, "step: ev %d i %d += %d S %g -> %g\n", k, i1, step,
						//	FindTwoLinApproxSumS(acc, i0, i1, i2, y0, temp1, y2),
						//	FindTwoLinApproxSumS(acc, i0, i1 + step, i2, y0, temp2, y2));
						i1 += step;
						ax[k] = i1;
						ay[k] = y1n;
					}
				}
			}
			improveBySweep(acc, ax, ay, nPts, linkFlags, linkData);
		}

		prf_b("BreakL_Fit_int: !linearOnly: done");

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
		improveBySweep(acc, ax, ay, nPts, linkFlags, linkData);
	}

	// сохраняем в mf (число звеньев уже должно совпалать с числом параметров)
	for (k = 0; k < nPts; k++)
	{
		mf[k * 2] = ax[k] + x_begin;
		mf[k * 2 + 1] = ay[k];
	}

	fprintf(stdout, "BreakL_Fit: done, nPts %d\n", nPts);
	fflush(stdout);

	prf_b("BreakL_Fit_int: leaving");

	delete[] ax;
	delete[] ay;
	delete[] thresh;

	acc.dispose();
}

void BreakL_FitL (ModelF &mf, double *data0, int i0, int x0, int length, int linkFlags, double *linkData)
{
	BreakL_Fit_int(mf, data0 + i0, x0, length, 0, 0.0, 0.0, 0, 1, linkFlags, linkData, 0);
}

void BreakL_FitI (ModelF &mf, double *data0, int i0, int x0, int length, int linkFlags, double *linkData,
	int quick)
{
	BreakL_Fit_int(mf, data0 + i0, x0, length, quick, 0.0, 0.0, 0, 0, linkFlags, linkData, 0);
}

void BreakL_Fit  (ModelF &mf, double *data0, int i0, int x0, int length, int linkFlags, double *linkData,
	int quick, double error1, double error2, int maxpoints)
{
	BreakL_Fit_int(mf, data0 + i0, x0, length, quick, error1, error2, maxpoints, 0, linkFlags, linkData, 0);
}

void BreakL_Fit2 (ModelF &mf, double *data0, int i0, int x0, int length, int linkFlags, double *linkData,
	int quick, double* error)
{
	BreakL_Fit_int(mf, data0 + i0, x0, length, quick, 0.0, 0.0, 0, 0, linkFlags, linkData, error + i0);
}
