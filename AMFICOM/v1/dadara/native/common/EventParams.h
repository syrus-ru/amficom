// EventParams.h: interface for the EventParams class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_EVENTPARAMS_H__4602CAF5_15BD_4327_AD1F_01E97ED71701__INCLUDED_)
#define AFX_EVENTPARAMS_H__4602CAF5_15BD_4327_AD1F_01E97ED71701__INCLUDED_

#include "MathRef.h"

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#define EventParams_LINEAR 1
#define EventParams_SPLICE 2
#define EventParams_CONNECTOR 3
#define EventParams_SINGULARITY 4

class EventParams
{

public:
	EventParams();
	virtual ~EventParams();
	void operator = (const EventParams& ep);
	int operator < (const EventParams& ep);
	int operator == (const EventParams& ep);
	int operator == (const EventParams* ep);
	int operator != (const EventParams* const &ep);

	static const int LINEAR;
	static const int SPLICE;
	static const int CONNECTOR;
	static const int SINGULARITY;

public:
	int n;
	int type;
	int begin;
	int end;

// Parameters for linear part
	double  a_linear;
	double  a_linearError;
	double  b_linear;
	double  b_linearError;
	double  chi2Linear;

// Parameters for the welds
	double  a_weld;
	double  a_weldError;
	double  b_weld;
	double  b_weldError;
	double  boost_weld;
	double  boost_weldError;
	double  center_weld;
	double  center_weldError;
	double  width_weld;
	double  width_weldError;
	double  chi2Weld;

// Parameters for the Connectors
	double  a1_connector;
	double  a1_connectorError;
	double  a2_connector;
	double  a2_connectorError;
	double  aLet_connector;
	double  aLet_connectorError;
	double  width_connector;
	double  width_connectorError;
	double  center_connector;
	double  center_connectorError;
	double  sigma1_connector;
	double  sigma1_connectorError;
	double  sigma2_connector;
	double  sigma2_connectorError;
	double  sigmaFit_connector;
	double  sigmaFit_connectorError;
	double  k_connector;
	double  k_connectorError;
	double  chi2Connector;

public:
	int get_n();
	EventParams* clone();
	double linearF(int x);
	double weldF(int x);
	double connectorF(int x);

private:
	double expa(double x, double s1, double s2, double part);
	
	
public:
	double connectorFunction(double A1_,
				double A2_,
				double ALet_,
				double width_,
				double sigma1_,
				double sigma2_,
				double k_,
				double center_,
				double sigmaFit_,
				int x);

	double linearFunction(double a, double k, int x);

	double weldFunction(double A_shift_, 
				double centerCoord_,
				double width_,
				double A_base_,
				double k_,
				int x_);



};


#endif // !defined(AFX_EVENTPARAMS_H__4602CAF5_15BD_4327_AD1F_01E97ED71701__INCLUDED_)

