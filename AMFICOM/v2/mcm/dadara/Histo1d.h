// Histo1d.h: interface for the Histo1d class.
//
//////////////////////////////////////////////////////////////////////

#ifndef AFX_HISTO1D_H__531C104A_24ED_44C9_B2EA_3984B8CF703B__INCLUDED_
#define AFX_HISTO1D_H__531C104A_24ED_44C9_B2EA_3984B8CF703B__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


//#include "TVirtualFitter.h"
//#include "TFitter.h"
//#include "TMath.h"

class Histo1d  
{
public:
	Histo1d();
	Histo1d(int nBins1, double downLimit1, double upLimit1);
	Histo1d(double delta1, double downLimit1, double upLimit1);
	virtual ~Histo1d();

public:
	double downLimit;
	double upLimit;
	double delta;

    int *histo;
    int nBins;


public:
	bool FillIt(double a);
	void FillIt(double *a, int begin, int end);
	void CleanIt();
	double getCentralValue();
	void FitIt();
    double getHistoWidth(double level);

private:
	void setCorrectLimits();
    double gauss(double centre, double sigma, double amplitude, int arg);
	double gauss(double centre, double sigma, double amplitude, double arg);
	
};











#endif // !defined(AFX_HISTO1D_H__531C104A_24ED_44C9_B2EA_3984B8CF703B__INCLUDED_)
