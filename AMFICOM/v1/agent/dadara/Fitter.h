// Fitter.h: interface for the Fitter class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_FITTER_H__6E4C53E6_B7A8_4441_90F7_09EB98555528__INCLUDED_)
#define AFX_FITTER_H__6E4C53E6_B7A8_4441_90F7_09EB98555528__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "EventParams.h"
#include <stdio.h>


class Fitter  
{
public:
	Fitter(EventParams **params, int params_size, double *data, int strategy, double meanAttenuation);
	virtual ~Fitter();

	EventParams **getEventParams();
	int getEventsCount();

private:
	int strategy;
	int params_size;
	double meanAttenuation;

	void performFitting();
	void performLinearFitting(int start, int end);
	void performSpliceFitting(int start, int end);
	void performConnectorFitting(int start, int end);
};

extern EventParams **ep;
extern double* data;
extern int nEvent;

void fcnWeld(int &, double *, double &f, double *x, int iflag);
void fcnLinear(int &, double *, double &f, double *x, int iflag);
void fcnConnector(int &, double *, double &f, double *x, int iflag);

#endif // !defined(AFX_FITTER_H__6E4C53E6_B7A8_4441_90F7_09EB98555528__INCLUDED_)
