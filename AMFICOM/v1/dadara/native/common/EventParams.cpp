// EventParams.cpp: implementation of the EventParams class.
//
//////////////////////////////////////////////////////////////////////

#include "EventParams.h"

//#define DEBUG_EPCF

#include <stdio.h>

const int EventParams::LINEAR = EventParams_LINEAR;
const int EventParams::SPLICE = EventParams_SPLICE;
const int EventParams::CONNECTOR = EventParams_CONNECTOR;
const int EventParams::SINGULARITY = EventParams_SINGULARITY;

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

EventParams::EventParams() {
	this->n = 0;
	this->type = 0;
	this->begin = 0;
	this->end = 0;

// Parameters for linear part
	this->a_linear=0;
	this->a_linearError=0;
	this->b_linear=0;
	this->b_linearError=0;
	this->chi2Linear=0;

// Parameters for the welds
	this->a_weld=0;
	this->a_weldError=0;
	this->b_weld=0;
	this->b_weldError=0;
	this->boost_weld=0;
	this->boost_weldError=0;
	this->center_weld=0;
	this->center_weldError=0;
	this->width_weld=0;
	this->width_weldError=0;
	this->chi2Weld=0;

// Parameters for the Connectors
	this->a1_connector=0;
	this->a1_connectorError=0;
	this->a2_connector=0;
	this->a2_connectorError=0;
	this->aLet_connector=0;
	this->aLet_connectorError=0;
	this->width_connector=0;
	this->width_connectorError=0;
	this->center_connector=0;
	this->center_connectorError=0;
	this->sigma1_connector=0;
	this->sigma1_connectorError=0;
	this->sigma2_connector=0;
	this->sigma2_connectorError=0;
	this->sigmaFit_connector=0;
	this->sigmaFit_connectorError=0;
	this->k_connector=0;
	this->k_connectorError=0;
	this->chi2Connector=0;
}

EventParams::~EventParams() {
//	printf("deleting for n == %d\n", this->n);
}

void EventParams::operator = (const EventParams& ep) {
	this->n = ep.n;
	this->type = ep.type;
	this->begin = ep.begin;
	this->end = ep.end;

	this->a_linear = ep.a_linear;
	this->a_linearError = ep.a_linearError;
	this->b_linear = ep.b_linear;
	this->b_linearError = ep.b_linearError;
	this->chi2Linear = ep.chi2Linear;

	this->a_weld = ep.a_weld;
	this->a_weldError = ep.a_weldError;
	this->b_weld = ep.b_weld;
	this->b_weldError = ep.b_weldError;
	this->boost_weld = ep.boost_weld;
	this->boost_weldError = ep.boost_weldError;
	this->center_weld = ep.center_weld;
	this->center_weldError = ep.center_weldError;
	this->width_weld = ep.width_weld;
	this->width_weldError = ep.width_weldError;
	this->chi2Weld = ep.chi2Weld;

	this->a1_connector = ep.a1_connector;
	this->a1_connectorError = ep.a1_connectorError;
	this->a2_connector = ep.a2_connector;
	this->a2_connectorError = ep.a2_connectorError;
	this->aLet_connector = ep.aLet_connector;
	this->aLet_connectorError = ep.aLet_connectorError;
	this->width_connector = ep.width_connector;
	this->width_connectorError = ep.width_connectorError;
	this->center_connector = ep.center_connector;
	this->center_connectorError = ep.center_connectorError;
	this->sigma1_connector = ep.sigma1_connector;
	this->sigma1_connectorError = ep.sigma1_connectorError;
	this->sigma2_connector = ep.sigma2_connector;
	this->sigma2_connectorError = ep.sigma2_connectorError;
	this->sigmaFit_connector = ep.sigmaFit_connector;
	this->sigmaFit_connectorError = ep.sigmaFit_connectorError;
	this->k_connector = ep.k_connector;
	this->k_connectorError = ep.k_connectorError;
	this->chi2Connector = ep.chi2Connector;
}

int EventParams::operator < (const EventParams& ep) {
	if (this->n < ep.n)
		return 1;
	else
		return 0;
}

int EventParams::operator == (const EventParams* ep) {
	if (this->n == ep->n)
		return 1;
	else
		return 0;
	
}

int EventParams::operator == (const EventParams& ep) {
	if (this->n == ep.n)
		return 1;
	else
		return 0;
	
}

int EventParams::operator != (const EventParams* const &ep) {
	if (this->n != ep->n)
		return 1;
	else
		return 0;
	
}

int EventParams::get_n() {
	return this->n;
}

EventParams* EventParams::clone() {
	EventParams* ep1 = new EventParams();
	*ep1 = *this;

	/*ep1->n = this->n;
	ep1->type = this->type;
	ep1->begin = this->begin;
	ep1->end = this->end;

	ep1->a_linear = this->a_linear;
	ep1->a_linearError = this->a_linearError;
	ep1->b_linear = this->b_linear;
	ep1->b_linearError = this->b_linearError;
	ep1->chi2Linear = this->chi2Linear;

	ep1->a_weld = this->a_weld;
	ep1->a_weldError = this->a_weldError;
	ep1->b_weld = this->b_weld;
	ep1->b_weldError = this->b_weldError;
	ep1->boost_weld = this->boost_weld;
	ep1->boost_weldError = this->boost_weldError;
	ep1->center_weld = this->center_weld;
	ep1->center_weldError = this->center_weldError;
	ep1->width_weld = this->width_weld;
	ep1->width_weldError = this->width_weldError;
	ep1->chi2Weld = this->chi2Weld;

	ep1->a1_connector = this->a1_connector;
	ep1->a1_connectorError = this->a1_connectorError;
	ep1->a2_connector = this->a2_connector;
	ep1->a2_connectorError = this->a2_connectorError;
	ep1->aLet_connector = this->aLet_connector;
	ep1->aLet_connectorError = this->aLet_connectorError;
	ep1->width_connector = this->width_connector;
	ep1->width_connectorError = this->width_connectorError;
	ep1->center_connector = this->center_connector;
	ep1->center_connectorError = this->center_connectorError;
	ep1->sigma1_connector = this->sigma1_connector;
	ep1->sigma1_connectorError = this->sigma1_connectorError;
	ep1->sigma2_connector = this->sigma2_connector;
	ep1->sigma2_connectorError = this->sigma2_connectorError;
	ep1->sigmaFit_connector = this->sigmaFit_connector;
	ep1->sigmaFit_connectorError = this->sigmaFit_connectorError;
	ep1->k_connector = this->k_connector;
	ep1->k_connectorError = this->k_connectorError;
	ep1->chi2Connector = this->chi2Connector;*/

	return ep1;
}

double EventParams::linearF(int x) {
	double ret;
	double arg = x - begin;
	ret = a_linear + b_linear*arg;
	return ret;
}


double EventParams::weldF(int x) {
		double ret;
		double arg = x - this->center_weld;
		double halfWidth = this->width_weld/2.;

		if(arg < -halfWidth)
			ret = -1.;
		else
			if(arg > halfWidth)
				ret = 1.;
			else
				ret = sin(3.14159*arg / this->width_weld);

		ret = ret * this->boost_weld/2. + this->a_weld + this->b_weld*arg;
		return ret;
}


double EventParams::connectorF(int x) {
	double ret = 0.;
	double arg;
	double arg1;
	double arg2;

	arg = x - this->center_connector;
	arg1 = arg + this->width_connector/2.;
	arg2 = arg - this->width_connector/2.;
	double tmp;
	
	if(arg < -this->width_connector/2.) {
		ret = this->a1_connector;
	}
	else
		if(arg >= -this->width_connector/2. && arg <= this->width_connector/2.)	{
			ret = this->aLet_connector*(1. - exp(-arg1/this->sigma1_connector)) + this->a1_connector;
		}
		else
			if(arg > this->width_connector/2.) {
				tmp = this->a1_connector +
					this->aLet_connector *
					(1. - exp(-this->width_connector/this->sigma1_connector));
				/*
				ret = tmp -
				       (tmp - this->a2_connector) *
				       (1. - expa(arg2,
						  this->sigma2_connector,
						  this->sigmaFit_connector,
						  this->k_connector));
				*/
				ret = this->a2_connector +
				       (tmp - this->a2_connector) *
				       expa(arg2,
						  this->sigma2_connector,
						  this->sigmaFit_connector,
						  this->k_connector);
			}
			else
				ret = 0.;
	
	return ret;
}



double EventParams::expa(double x, double s1, double s2, double part) {
	double ret = 0.;
	double arg1 = x/s1;
	double arg2 = x/s2;
	ret = exp(-arg1)*part + exp(-arg2)*(1.-part);
	return ret;
}

double EventParams::connectorFunction(double A1_,
					double A2_,
					double ALet_,
					double width_,
					double sigma1_,
					double sigma2_,
					double k_,
					double center_,
					double sigmaFit_,
					int x) {
	double ret = 0.;
	double arg  = x - center_;
	double arg1 = arg + width_/2.;
	double arg2 = arg - width_/2.;
	
	double tmp;
	
	if(arg < -width_/2.) {
    		ret = A1_;
	}
	else
		if(arg >= -width_/2. && arg <= width_/2.) {
	    		ret = ALet_*(1. - exp(-arg1/sigma1_)) + A1_;
		}
      		else
	      		if(arg > width_/2.) {
				tmp = A1_ + ALet_*(1. - exp(-width_/sigma1_));
				ret = (tmp) - (tmp - A2_)*(1.- expa(arg2, sigma2_, sigmaFit_, k_));
      			}
      			else
	    			ret = 0.;
#ifdef DEBUG_EPCF
	{
		static FILE *epcf;
		if (!epcf) epcf=fopen("c:\\epcf.log","a");
		fprintf(epcf,"DBG_CF: %g %g\n", (double)x, (double)ret);
		fflush (epcf);
		// fclose(epcf) will not be made
	}
#endif
	return ret;
}



double EventParams::weldFunction(double A_shift_,
				double centerCoord_,
				double width_,
				double A_base_,
				double k_,
				int x_) {
	double ret;
	double arg = x_ - centerCoord_;
	double halfWidth = width_/2.;
	
	if(arg<-halfWidth)
		ret = -1.;
	else
		if(arg > halfWidth)
			ret = 1.;
		else
			ret = sin(M_PI*arg/width_);

	ret = ret*A_shift_/2. + A_base_ + k_*arg;

    return ret;
}



double EventParams::linearFunction(double A_base_, double k_, int x_) {
	double ret;
	double arg = x_ - begin;
	ret = A_base_ + k_*arg;
	return ret;
}
