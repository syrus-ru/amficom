#include "EventParams.h"
#include "TFitter.h"

void fcnWeld(int &, double *, double &f, double *x, int iflag);
void fcnLinear(int &, double *, double &f, double *x, int iflag);
void fcnConnector(int &, double *, double &f, double *x, int iflag);
double *setParRet(int n_events, EventParams **eventParams, int &Ret_Length);


EventParams **ep;
double* data;
double *sigmaSquared;
int nEvent;

double chi2norma;
