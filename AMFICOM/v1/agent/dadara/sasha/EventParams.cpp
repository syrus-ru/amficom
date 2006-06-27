// EventParams.cpp: implementation of the EventParams class.
//
//////////////////////////////////////////////////////////////////////

#include "EventParams.h"
#include <math.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

EventParams::EventParams()
{
	linearEvent = 0;
	connectorEvent = 0;
	weldEvent = 0;
	pi = 3.1415926535897932384626433832795;

// Parameters for linear part
	a_linear=0;
	a_linearError=0;
	b_linear=0;
	b_linearError=0;
	beginLinear=0;
	endLinear=0;
	chi2Linear=0;
	linearEvent=0;
// Parameters for the welds
	a_weld=0;
	a_weldError=0;
	b_weld=0;
	b_weldError=0;
	boost_weld=0;
	boost_weldError=0;
	center_weld=0;
	center_weldError=0;
	width_weld=0;
	width_weldError=0;
	beginWeld=0;
	endWeld=0;
	chi2Weld=0;
	weldEvent=0;
// Parameters for the Connectors
	a1_connector=0;
	a1_connectorError=0;
	a2_connector=0;
	a2_connectorError=0;
	aLet_connector=0;
	aLet_connectorError=0;
	width_connector=0;
	width_connectorError=0;
	center_connector=0;
	center_connectorError=0;
	sigma1_connector=0;
	sigma1_connectorError=0;
	sigma2_connector=0;
	sigma2_connectorError=0;
	sigmaFit_connector=0;
	sigmaFit_connectorError=0;
	k_connector=0;
	k_connectorError=0;
	beginConnector=0;
	endConnector=0;
	chi2Connector=0;
	connectorEvent=0;

}



EventParams::~EventParams()
{

}



double EventParams::linearF(int x)	
{
		double ret;
		double arg = x-beginLinear;
		ret = a_linear + b_linear*arg;
		return ret;
}


double EventParams::weldF(int x) 
{
		double ret;
		double arg = x-center_weld;
		double halfWidth = width_weld/2.;

		if(arg<-halfWidth) ret = -1.;
		else
			if(arg> halfWidth) ret = 1.;
		else
			ret = sin(3.14159*arg/width_weld);

		ret = ret*boost_weld/2. + a_weld + b_weld*arg;
		return ret;
}


double EventParams::connectorF(int x) 
{
	double ret = 0.;
	double arg;
	double arg1;
	double arg2;

		arg = x-center_connector;
		arg1 = arg+width_connector/2.;
		arg2 = arg-width_connector/2.;
		double tmp;

		if(arg<-width_connector/2.)	
		{
			ret = a1_connector;
		}
		else
		if(arg>=-width_connector/2. && arg<=width_connector/2.)	
		{
			ret = aLet_connector*(1.-exp(-arg1/sigma1_connector)) +
						a1_connector;
		}
		else
		if(arg>width_connector/2.) 
		{
			tmp = a1_connector+aLet_connector*
					(1.-exp(-width_connector/sigma1_connector));
			ret = tmp -	(tmp-a2_connector)*(1. - expa(arg2, sigma2_connector,
								sigmaFit_connector, k_connector));
		}
		else
		ret = 0.;

		return ret;
}



double EventParams::expa(double x, double s1, double s2, double part)	
{
	double ret = 0.;
	double arg1 = x/s1;
	double arg2 = x/s2;
	ret = exp(-arg1)*part + exp(-arg2)*(1.-part);
	return ret;
}




double EventParams::connectorFunction(	double A1_,
										double A2_,
										double ALet_,
										double width_,
										double sigma1_,
										double sigma2_,
										double k_,
										double center_,
										double sigmaFit_,
										int x)
{ 
	 double ret = 0.;
	 double arg  = x-center_;;
	 double arg1 = arg+width_/2.;
	 double arg2 = arg-width_/2.;

     double tmp;

          if(arg<-width_/2.) 
		  {
            ret = A1_;
		  }
          else
		  if(arg>=-width_/2. && arg<=width_/2.) 
		  {
            ret = ALet_*(1.-exp(-arg1/sigma1_)) + A1_;
		  }
          else
          if(arg>width_/2.)
		  {
			tmp = A1_ + ALet_*(1.-exp(-width_/sigma1_));

            ret = (tmp)-(tmp - A2_)*(1.- expa(arg2, sigma2_, sigmaFit_, k_));
		  }
		  else
 		    ret = 0.;

     return ret;
}



double EventParams::weldFunction(double A_shift_, 
		                 double centerCoord_,
						 double width_,
						 double A_base_,
						 double k_,
						 int x_)
{
	double ret;
	double arg = x_-centerCoord_;
	double halfWidth = width_/2.;

	if(arg<-halfWidth) ret = -1.;
	else
	if(arg> halfWidth) ret = 1.;
	else
	ret = sin(pi*arg/width_);

	ret = ret*A_shift_/2. + A_base_ + k_*arg;

    return ret;
}



double EventParams::linearFunction(double A_base_, double k_, int x_)
{
	double ret;
	double arg = x_-beginLinear;
	ret = A_base_ + k_*arg;
	return ret;
}
