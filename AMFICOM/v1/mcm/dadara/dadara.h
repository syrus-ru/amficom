#include "EventParams.h"
#include <stdio.h>


//#define DEBUG_DADARA

void fcnWeld(int &, double *, double &f, double *x, int iflag);
void fcnLinear(int &, double *, double &f, double *x, int iflag);
void fcnConnector(int &, double *, double &f, double *x, int iflag);
double *setParRet(int n_events, EventParams **eventParams, int &Ret_Length);
int checkFittness (int nEvents);
void performLinearFitting(EventParams **ep, int start, int end, int strategy);
void performSpliceFitting(EventParams **ep, int start, int end, int strategy);
void performConnectorFitting(EventParams **ep, int start, int end, int strategy, int eventSize);

EventParams **ep;
double* data;
//double *sigmaSquared;
int nEvent;

double chi2norma;


#ifdef DEBUG_DADARA
FILE* stream;
#endif

