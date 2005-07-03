// Histogramm.h: interface for the Histogramm class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_HISTOGRAMM_H__4289F80F_4B49_4101_814D_A9A4783F083D__INCLUDED_)
#define AFX_HISTOGRAMM_H__4289F80F_4B49_4101_814D_A9A4783F083D__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "MathRef.h"

class Histogramm  
{
public:
	Histogramm(double down_limit, double up_limit, int nBins);
	virtual ~Histogramm();

	void init(double* data, int data_length, int start, int end);
	int getMaximumIndex();
	double getMaximumValue();

private:
	double up_limit;
	double down_limit;
	int nBins;
	double* histo;

};

#endif // !defined(AFX_HISTOGRAMM_H__4289F80F_4B49_4101_814D_A9A4783F083D__INCLUDED_)

