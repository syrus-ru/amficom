#include <assert.h>
#include <stdio.h>
#include <math.h>
#include <string.h> // memcpy

#include "studentDF.h"
#include "ArrList.h"
#include "Analyse.h"
#include "../Common/prf.h"

int InEvent::f_xSort(const void **a, const void **b)
{
	const InEvent* aa = *(const InEvent **)a;
	const InEvent* bb = *(const InEvent **)b;
	int diff = aa->x0 - bb->x0;
	return diff <= 0 ? diff < 0 ? -1 : 0 : 1;
}

void InEvent::initAsRgStart()
{
	type = Event_Type_SOMETHING;
	x0 = 0;
	scale = 0;
	value = 50; // XXX
	begin = 0;
	end = 0;
	dup = 0;
	special = 1;
}


#define Event InEvent

const double p_level = 1e-4;
const int MAXSIZE = 65536;
const double noise_base =  .001;  // addition to noise
//const double thresh_base = .005; // addition for threshold
const double thresh_base = .005; // addition for threshold

const int MAX_EVENTS = 300; // ����. ����� ������������ ������� (�������), ������ � �������; -1: �� ���������� // FIXIT

const double scale_mask_other_from = 1.0;
const double scale_mask_other_to = 3.0;
const double scale_mask_same_from = 2.0; // XXX: 1.0 .. 3.0
const double value_mask_other = 0.75; // 0.67 .. 0.7 .. 0.8 .. {1 ~ +inf}

double inv_stud(int i, double p)
{
	return -inv_studentDF((double )i, p * 2.0 / 2.0);
}

inline int imax(int a, int b)
{
	return a > b ? a : b;
}
inline int imin(int a, int b)
{
	return a < b ? a : b;
}
#define iabs abs
inline double dmax(double a, double b)
{
	return a > b ? a : b;
}
inline double dpow2(double v)
{
	return v * v;
}

void prepare_acc_stats( // �������������� ������ ��� �������� ���������� �������� (x,y)
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

inline void calc_yarr_stat2( // ������� ���������� ������ �������� (x,y) �� ������ ��������������� ������
	int i0,
	int n,
	double &mx,
	double &my,
	double &mxy,
	double &myy,
	double *acc_y,
	double *acc_xy,
	double *acc_yy)
{
	my = (acc_y[i0 + n] - acc_y[i0]) / n;
	mxy = (acc_xy[i0 + n] - acc_xy[i0]) / n;
	myy = (acc_yy[i0 + n] - acc_yy[i0]) / n;

	mx = i0 + (n - 1) / 2.0;
}

// ������� ������ ������� Event �� ��������������
// ����������� ���������:
//   p_level - �������� ����������
//   scale_* - ������� ��������
//   noise_base, thresh_base - ������
void analyse_fill_events(double *data, int size, ArrList &al)
{
	int i;

	prf_b("analyse_fill_events: enter");

	fprintf(stderr, "analise_fill_events: %d points got for processing\n", size);
	fflush(stderr);

	// ��������� ������ ���������
	int nscale;
	int *scales;
	{
		// XXX: ���������
		int scale_from = 3; // ����. �������� (����� ������ ��������� �� 4-5)
		int scale_to = 256; // ����. ��������, ��� ���� ���� ���-�� ����������, ���� �������� �����
		int scale_div = 8; // 4 .. 20

		scales = new int[scale_to - scale_from + 1]; // �������� ����������� ������
		assert(scales);
		nscale = 0;
		for (i = scale_from; i < scale_to; i += 1 + i / scale_div)
			scales[nscale++] = i;
	}

	// ��� �� i; ���� ����� �������, � ������ ��� ����, ����� �������� �����,
	// ������� �� ���� �������.
	// � ��������, � ������� ����� ������� ��� � ���� �������� ������
	// � ������ res_img ������� ����������, �� �����.
	const int d_i = 1;

	// �������� ����� ��� mwavelet �����
	double *res_img_rw = new double[nscale * size];
	assert(res_img_rw);
	const double *res_img = res_img_rw; // readonly ������ (XXX - ���������� res_img � res_img_rw �������)

	// calc stats
	//prf_b("analyse_fill_events: preprocess stat");
	double *acc_y = new double[size + 1]; assert(acc_y);
	double *acc_xy = new double[size + 1]; assert(acc_xy);
	double *acc_yy = new double[size + 1]; assert(acc_yy);
	prepare_acc_stats(data, size, acc_y, acc_xy, acc_yy);

	// make mwavelet transform
	fprintf(stderr, "making mwavelet transform\n"); // XXX
	fflush(stderr);
	prf_b("analyse_fill_events: make mwavelet transform");

	//int scale;
	//for (scale = scale_min; scale < scale_mx1; scale++)
	int scale_id;
	for (scale_id = 0; scale_id < nscale; scale_id++)
	{
		int scalev = scales[scale_id];

		// ������������ �������� ������; ������������ ������ ������� ������� // XXX: ������� ��� ������
		prf_b("analyse_fill_events: make mwavelet transform: init levels");
		const int nlev = 1;
		double levels[nlev];
		for (i = 0; i < nlev; i++)
			levels[i] = inv_stud(2 * scalev - 4, p_level * pow(5.0, -i)); // XXX

		// ��������� ���� �����, ����� �� ����� ���������� ���� ������ ��������� ��������
		prf_b("analyse_fill_events: make mwavelet transform: zero image");
		for (i = 0; i < size; i++)
			res_img_rw[scale_id * size + i] = 0;

		// �����. �������
		prf_b("analyse_fill_events: make mwavelet transform: prepare to scan");
		const double scale2 = scalev * scalev;
		const double rdelta = 12.0 / (scale2 - 1); // ����� 1.0 / (mxx - mx*mx)
		//const double rDx = sqrt(delta);
		const double rD0 = sqrt(1.0 + 3.0 * (double )scale2 / (scale2 - 1));
		const double s2scale = sqrt(2.0 / scalev);
		const double ss_mult = (double )scalev / (2 * scalev - 4);

		prf_b("analyse_fill_events: make mwavelet transform: scan i");
		for (i = scalev; i < size - scalev; i += d_i)
		{
			double ss = 0;
			double mx, my, mxy, myy;
			double a, b, cov;
			double x_c = i - 0.5;

			//prf_b("analyse_fill_events: make mwavelet transform: scan i: stat l+r");

			// left-side
			calc_yarr_stat2(i - scalev, scalev, mx, my, mxy, myy, acc_y, acc_xy, acc_yy);

			cov = mxy - my * mx;
			a = cov * rdelta;
			b = my - a * mx;

			//double g1 = a * rDx;
			double h1 = b + a * x_c;
			ss += myy - my * my - cov * a;

			// right-side
			calc_yarr_stat2(i, scalev, mx, my, mxy, myy, acc_y, acc_xy, acc_yy);

			cov = mxy - my * mx;
			a = cov * rdelta;
			b = my - a * mx;

			//prf_b("analyse_fill_events: make mwavelet transform: scan i: after stat");

			//double g2 = a * rDx;
			double h2 = b + a * x_c;
			ss += myy - my * my - cov * a;

			if (ss < 0)
				ss = 0;
			else
				ss = sqrt(ss * ss_mult);

			double div = (ss + noise_base) * s2scale + thresh_base;
			// ��� ����. ���� �� �����,
			// �������� t1 � t2 ����� t-�������������� � 2 * scalev - 4 ����. �������:
			//double t1 = fabs(g2 - g1) / div;
			//double t2 = fabs(h2 - h1) / div;
			// �� ���� ���������� � levels[0]
			// �� ���������� ������ t2
			if (fabs(h2 - h1) > levels[0] * div * rD0)
			{
				int j;
				for (j = 0; j < d_i; j++)
					res_img_rw[scale_id * size + i + j] = h2 - h1;
			}
		}
		prf_b("analyse_fill_events: make mwavelet transform: scan i done");
	}

	delete[] acc_y;
	delete[] acc_xy;
	delete[] acc_yy;

	// �������� ����� ��� ������ ��� �������� ��������� ������ �����������
	prf_b("analyse_fill_events: prepare filt_img");
	double *filt_img = new double[nscale * size]; // ����� ����� ����������� ��������
	assert(filt_img);
	memcpy(filt_img, res_img, sizeof(double) * nscale * size);

	// ������� ��������� �������� ���� res_max/res_min; �� ����� ��������������� ����������� filt_img
	prf_b("analyse_fill_events: prepare res max/min cache");
	fprintf(stderr, "analyse_fill_events: prepare res max/min cache\n");
	fflush(stderr);

	int *res_max_index = new int[size];
	double *res_max_value = new double[size];
	assert(res_max_index);
	assert(res_max_value);

	int *res_min_index = new int[size];
	double *res_min_value = new double[size];
	assert(res_min_index);
	assert(res_min_value);

	for (i = 0; i < size; i++)
	{
		res_min_index[i] = res_max_index[i] = -1; // ��������� ���������� ����������
		res_min_value[i] = res_max_value[i] = 0;  // �������������� ��������
	}

	// process mwavelet image, fill al

	fprintf(stderr, "analyse_fill_events: processing mwavelet image\n");
	fflush(stderr);
	prf_b("analyse_fill_events: process mwavelet image");

	// ������������:
	// �� ������ �������� (�������������) �� ������������� ����� ��� ������ i
	// ���. ���������: ������������� ��.
	// ���� maskp[i] == 0, �� res_max_*[i] �� ����� ��������;
	// ���� maskn[i] == 0, �� res_min_*[i] �� ����� ��������.
	// ��� ������������ ������ ����� ������������ ���������������;
	// ��� ������������ ������� ����� - � ������������ � �������� filt_img.
	// maskp - ����� �� ������������� �������
	// maskn - ����� �� ������������� �������
	int *maskp = new int[size];
	int *maskn = new int[size];
	assert(maskp);
	assert(maskn);
	for (i = 0; i < size; i++)
		maskp[i] = maskn[i] = nscale; // ����. �������� + 1

	// TODO: ������ ����. ����� ���������� ����� �����?
	int max_event_counter = MAX_EVENTS;
	while(max_event_counter--)
	{
		prf_b("analyse_fill_events: process mwavelet image: fill res_max cache");

		for (i = 0; i < size; i++)
		{
			if (res_max_index[i] < 0) // ���� max-������ ���� �������
			{
				// ���� ����. �������� res_img �� ���� ��������� ��� ������ ����������
				double vmax = 0;
				int imaxsi = 0;
				for (scale_id = 0; scale_id < maskp[i]; scale_id++)
				{
					double val = filt_img[scale_id * size + i];
					if (val > vmax)
					{
						vmax = val;
						imaxsi = scale_id;
					}
				}

				res_max_index[i] = imaxsi;
				res_max_value[i] = vmax;

				if (vmax == 0) // ���� ��������� ���, �� ������ ������ ����� ������������
					maskp[i] = 0; // res_max_index ������ �� ����� ��������
			}

			if (res_min_index[i] < 0) // ���� min-������ ���� �������
			{
				// ���� ���. �������� res_img �� ���� ��������� ��� ������ ����������
				double vmin = 0;
				int iminsi = 0;
				for (scale_id = 0; scale_id < maskn[i]; scale_id++)
				{
					double val = filt_img[scale_id * size + i];
					if (val < vmin)
					{
						vmin = val;
						iminsi = scale_id;
					}
				}

				res_min_index[i] = iminsi;
				res_min_value[i] = vmin;

				if (vmin == 0) // ���� �������� ���, �� ������ ������ ����� ������������
					maskn[i] = 0; // res_max_index ������ �� ����� ��������
			}
		}

		prf_b("analyse_fill_events: process mwavelet image: find event");
		//prf_b("analyse_fill_events: process mwavelet image: find max(abs)");

		// find val with max abs; preserve the sign of val
		double vmax = 0;
		int imaxi; // i0 ��������� ���. ����.
		int imaxsi;// scale_id ����� ���������
		// ���� ����. �������� filt_img � ���������
		// i = 0 .. size-1; scale = scale_min .. masks[i]
		// � �������������� res max/min ����
		for (i = 0; i < size; i++)
		{
			if (maskp[i])
			{
				if (res_max_value[i] > fabs(vmax))
				{
					imaxi = i;
					imaxsi = res_max_index[i];
					vmax = res_img[imaxsi * size + i];
					assert(imaxsi < maskp[imaxi]); //killme
				}
			}
			if (maskn[i])
			{
				if (-res_min_value[i] > fabs(vmax))
				{
					imaxi = i;
					imaxsi = res_min_index[i];
					vmax = res_img[imaxsi * size + i];
					assert(imaxsi < maskn[imaxi]); //killme
				}
			}
		}
		//prf_b("analyse_fill_events: process mwavelet image: after find max");

		if (vmax * 0.1 == 0)
			break;

		assert(imaxsi < maskp[imaxi] || imaxsi < maskn[imaxi]);

		// ���� ������ �� ���������� ���������

		//prf_b("analyse_fill_events: process mwavelet image: scan to top");

		double vth = vmax * 0.4; // XXX: ��������, ������� � �������� ����� ������� ���������
		int ici = imaxi;
		for (scale_id = imaxsi - 1; scale_id >= 0; scale_id--)
		{
			// ���� �������� �� ����� �������� � ��������� ici pm maxdi
			int maxdi = scales[scale_id + 1] - scales[scale_id] + d_i;
			int ici_new = -1; // ���� ���������� ����. �� ����� �������
			double vth_cur = vth;
			for (i = ici - maxdi; i <= ici + maxdi; i++)
			{
				if (i < 0 || i >= size)
					continue;
				// ���� ��� ���������, �� ������� � � ������������� �������(?)
				//if (scale_id >= maski[i])
				//	continue;
				if (res_img[scale_id * size + i] / vth_cur > 1.0)
				{
					ici_new = i;
					vth_cur = res_img[scale_id * size + i];
				}
			}
			if (ici_new < 0)
				break;
			ici = ici_new;
		}
		scale_id++;
		int icsi = scale_id;
		double vc = res_img[icsi * size + ici];

		// ������� ������������
		//prf_b("analyse_fill_events: process mwavelet image: event found");

		// ����������, ��������� �� �������� �����. � �������� ������������� s-����� �������� �����
		// ���� ���, ��������, ��������� ���� ��������� ����� � � ����� �������,
		// ����� ��������� � ���. �����
		int shift_to_catch = 0;
		for(;;)
		{
			int di = iabs(imaxi - ici - shift_to_catch);
			int scale_c = (int )((di - 0) * scale_mask_same_from);
			if (scale_c < 0)
				scale_c = 0;
			if (scales[imaxsi] >= scale_c)
				break;
			shift_to_catch += imaxi > ici ? 1 : -1;
		}

		// � ������� �������� ��

		fprintf (stderr,"max abs: %g at %d, %d(%d)  ==>  %g at %d, %d(%d) ( %d .. %d ) - maskp %d maskn %d shift %d\n",
			vmax, imaxi, imaxsi, scales[imaxsi], vc, ici, icsi, scales[icsi], ici - scales[icsi], ici + scales[icsi],
			maskp[ici], maskn[ici], shift_to_catch);

		// ���� �� �����������, �� �������� ������� � ���. ������

		{
			int x0 = ici;
			int scale = scales[icsi];
			int j;
			for (j = al.getLength() - 1; j >= 0; j--) // �������� �������� � ����� // XXX: ������� ����
			{
				Event &that = *(Event *)al[j];
				if (that.x0 == x0 && that.scale == scale)
				{
					that.dup = 1;
					break;
				}
			}

			if (j < 0) // ���� ������ �������� �� �����, �� ���������
			{
				Event *ev = new Event;
				ev->type = Event_Type_SOMETHING;
				ev->x0 = x0;
				ev->scale = scale;
				ev->value = vc;
				ev->begin = x0 - scale; //+ imin(shift_to_catch, 0); -- ��������� ��� ��������� ����������
				ev->end = x0 + scale; //+ imax(shift_to_catch, 0);
				ev->dup = 0;
				ev->special = 0;

				al.add(ev);
			}
		}

		prf_b("analyse_fill_events: process mwavelet image: impose mask");

		// ��������� �����, filt_img, �������� ������� ����������������� �������� ���� res_max
		int i_from = imax(ici - scales[nscale - 1], 0);
		int i_to   = imin(ici + scales[nscale - 1], size - 1);
		int *mask_s = vmax > 0 ? maskp : maskn;
		int *mask_o = vmax > 0 ? maskn : maskp;
		int *res_s_index = vmax > 0 ? res_max_index : res_min_index;
		int *res_o_index = vmax > 0 ? res_min_index : res_max_index;
		for (i = i_from; i <= i_to; i++)
		{
			int di1 = iabs(i - ici);
			int di2 = iabs(i - ici - shift_to_catch);
			int dimin = imin(di1, di2);
			int dimax = imax(di1, di2);

			// s-����� (�� ��� �� ����)
			int scale_c = (int )((dimin - 1) * scale_mask_same_from); // �������� ��������� ����� (dimin - 1)
			//if (scale_c < 0) -- ���� ��������� ������ ����, �� ������ ��������� �� �����
			//	scale_c = 0;
			while (mask_s[i] >= 1 && scales[mask_s[i] - 1] >= scale_c)
				mask_s[i]--;
			// ���� ��������� ���������� ��������� �� ��������� ����� �����, ��� ���� ����� �����������
			if (res_s_index[i] >= mask_s[i]) // �������� ������ ������������ ��� mask_s[i] = 0, �� ��� �� �� ��� �� ��������
				res_s_index[i] = -1;

			// o-����� (�� ������ ����)
			int scale_a = (int )((dimin - 1) * scale_mask_other_from); // �������� ��������� ����� (dimin - 1)
			int scale_b = (int )((dimax + 1) * scale_mask_other_to); // �������� ������ ����� (dimax + 1)
			if (scale_a < 0)
				scale_a = 0;
			int sid;
			for (sid = 0; sid < mask_o[i]; sid++)
			{
				if (filt_img[sid * size + i] / vmax >= 0)
					continue; // �� �� ���� ���� ����
				if (filt_img[sid * size + i] / vmax < -value_mask_other)
					continue; // ������� ����� �� ������ - �� ���������
				int s_val = scales[sid];
				if (s_val < scale_a)
					continue;
				if (s_val > scale_b)
					continue; // break ����������� � �������, �� ������� � ������� ����
				/*if (i == 620) // FIXIT: debug
				{
					fprintf(stderr, "ici %d i %d sid(scale) %d(%d) res_img %g filt_old %g %c\n",
						ici,
						i,
						sid, scales[sid],
						res_img[sid * size + i],
						filt_img[sid * size + i],
						res_o_index[i] == sid ? 'F' : '.');
				}*/
				if (res_o_index[i] == sid)
					res_o_index[i] = -1; // ���� ��������� ���� ����������������
				filt_img[sid * size + i] = 0;
			}
		}
		fflush(stdout);

		prf_b("analyse_fill_events: process mwavelet image: next");
	}

	if (max_event_counter == 0)
	{
		fprintf(stderr, "Warning: MAX_EVENTS limit reached\n");
	}

	prf_b("analyse_fill_events: freeing memory");

	delete[] res_max_index;
	delete[] res_max_value;

	delete[] res_min_index;
	delete[] res_min_value;

	delete[] filt_img;

	delete[] maskp;
	delete[] maskn;

	delete[] res_img_rw;

	delete[] scales;

	// sort and return events

	fprintf(stderr, "analyse_fill_events: sorting events\n");
	fflush(stderr);

	al.qsort(Event::f_xSort);

	for (i = 0; i < al.getLength(); i++)
	{
		Event &ev = *(Event *)al[i];
		fprintf(
			stderr,
			"Ev[%d]: %g at x0 %d scale %d ( %d .. %d )\n",
			i,
			ev.value,
			ev.x0,
			ev.scale,
			ev.begin,
			ev.end
		);
	}

	fprintf(stderr, "analyse_fill_events: done; al.length = %d\n",al.getLength());
	fflush(stderr);

	prf_b("analyse_fill_events: done");
}
