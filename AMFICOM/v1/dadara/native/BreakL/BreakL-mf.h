#ifndef _BREAKL_BREAKL_MF_H
#define _BREAKL_BREAKL_MF_H

#include "../Common/ModelF.h"

double f_BREAKL(double *pars, int npars, double x, double *, int);
void farr_BREAKL(double *pars, int npars, double x_s, double step, int N, double *output);
double fc_BREAKL(double *pars, ModelF &mf, int command, void *extra);
double a_noiseSuppressionLength_BREAKL(double *pars, int npars);
int f_BREAKL_GETPOS(int nEv, double *pars, double x, int k0); // k0 == -1: bsearch
double f_BREAKL_GETY(int k, int nEv, double *pars, double x);

#endif
