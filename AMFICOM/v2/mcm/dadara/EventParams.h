// EventParams.h: interface for the EventParams class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_EVENTPARAMS_H__4602CAF5_15BD_4327_AD1F_01E97ED71701__INCLUDED_)
#define AFX_EVENTPARAMS_H__4602CAF5_15BD_4327_AD1F_01E97ED71701__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class EventParams  
{
public:
	EventParams();
	virtual ~EventParams();

public:
	double pi;

// Parameters for linear part
	double  a_linear;
	double  a_linearError;
	double  b_linear;
	double  b_linearError;
	int     beginLinear;
	int     endLinear;
	double  chi2Linear;
	int     linearEvent;
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
	int     beginWeld;
	int     endWeld;
	double  chi2Weld;
	int     weldEvent;
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
	int     beginConnector;
	int     endConnector;
	double  chi2Connector;
	int     connectorEvent;

public:
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

    double linearFunction(	 double A_base_, double k_, int x_);

	double weldFunction(	 double A_shift_, 
							 double centerCoord_,
							 double width_,
							 double A_base_,
							 double k_,
							 int x_);



};

#endif // !defined(AFX_EVENTPARAMS_H__4602CAF5_15BD_4327_AD1F_01E97ED71701__INCLUDED_)
