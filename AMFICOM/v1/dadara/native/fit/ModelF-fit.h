#ifndef _MODELF_FIT_H
#define _MODELF_FIT_H

class ModelF;

class fit_stat_res
{
public:
	// input only:
	double ok_level_1;
	double ok_level_2;

	// output only:
	int excess_count_1;
	int excess_count_2;
	int nfits;
	double rms_avg;
	double rms_worst;

	fit_stat_res()
	{
		ok_level_1 = 0;
		ok_level_2 = 0;
	}

	fit_stat_res(double level_1, double level_2)
	{
		ok_level_1 = level_1;
		ok_level_2 = level_2;
	}
};

void fit(ModelF &mf, double *y, int i0, int x0, int length, fit_stat_res &stat);

void fit_linear_only(ModelF &mf, double *y, int i0, int x0, int length);

#endif
