#include "EventParams.h"
#include <stdio.h>

#define DEBUG_DADARA

double *setParRet(int n_events, EventParams **eventParams, int &Ret_Length);
void getEventParams(double *ep_data, int ep_data_length, EventParams **ep);

#ifdef DEBUG_DADARA
extern FILE* dbg_stream;
extern double dbg_delta_x; // globally saved delta_x
extern int dbg_suppress_cf_messages;
#endif
