// EventParams (old) to EventP (new) converter.
// Used with InitialAnalysis

#ifndef _EPOLD2EPNEW_H
#define _EPOLD2EPNEW_H

#include "EventParams.h"
#include "EventP.h"

void EPold2EPnew(EventParams* epo, EventP &epn, double delta_x);

#endif

