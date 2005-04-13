// EventParams (old) to EventP (new) converter.
// Used with InitialAnalysis

#ifndef _EPOLD2EPNEW_H
#define _EPOLD2EPNEW_H

#include "EventParams.h"
#include "EventP.h"
#include "SimpleEvent.h"

// unused since event has no mf
void EPold2EPnew(EventParams* epo, EventP &epn, double delta_x);

// this is currently used
void EPold2SE(EventParams* epo, SimpleEvent &epn);

#endif

