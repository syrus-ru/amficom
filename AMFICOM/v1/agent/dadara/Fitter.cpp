// Fitter.cpp: implementation of the Fitter class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "Fitter.h"
#include "TFitter.h"
#include "dadara.h"

EventParams **ep;
double* data;
int nEvent;

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Fitter::Fitter(EventParams **params, int params_size, double *y, int strategy, double meanAttenuation)
{
	ep = params;
	data = y;
	this->params_size = params_size;
	this->strategy = strategy;
	this->meanAttenuation = meanAttenuation;

	performFitting();
}

Fitter::~Fitter()
{

}

void Fitter::performFitting()
{
	if(strategy >= 0)
	{
#ifdef DEBUG_DADARA
		int l = ep[params_size-1]->end;
		fprintf( dbg_stream, "\n***** NEW REFLECTOGRAMM ANALYSIS *****\n");
		fprintf( dbg_stream, "after initial analysis:\n");
		fprintf( dbg_stream, "\t number of events  = %d\n", params_size);
#endif

		performLinearFitting	(0, params_size);
		performSpliceFitting	(0, params_size);
		performConnectorFitting	(0, params_size);

#ifdef DEBUG_DADARA
		l = ep[params_size-1]->end;
		fprintf( dbg_stream, "after fitting:\n");
		fprintf( dbg_stream, "\t number of events  = %d\n", params_size);
#endif
	}
}

EventParams **Fitter::getEventParams()
{
	return ep;
}

int Fitter::getEventsCount()
{
	return params_size;
}


void Fitter::performLinearFitting(int start, int end)
{
	double dummy1, dummy2, dummy3;
	double arglist[1];

	TFitter *linearFitter = new TFitter(2);
	linearFitter->SetFCN(fcnLinear);

#ifdef DEBUG_DADARA
	fprintf( dbg_stream, "LINEAR FITTING\n");
#endif

	int i;
	for(i = start; i < end; i++)
	{
		if(ep[i]->type == EventParams::LINEAR) // Linear fitting
		{
			nEvent = i;

#ifdef DEBUG_DADARA
			fprintf( dbg_stream, "before fit: #%d (%d-%d)~(%.3f-%.3f)",
				i, ep[i]->begin, ep[i]->end,ep[i]->begin*dbg_delta_x/1e3, ep[i]->end*dbg_delta_x/1e3);
			fprintf( dbg_stream, " att %.3f", ep[i]->a_linear);
			fprintf( dbg_stream, " ampl1p %.3f\n", ep[i]->b_linear);
#endif
			
			// set initial parameters
			linearFitter->SetParameter(
				0,
				"a",
				ep[i]->a_linear,
				0.1,
				ep[i]->a_linear - 3.,
				ep[i]->a_linear + 3.);
			linearFitter->SetParameter(
				1,
				"k",
				ep[i]->b_linear,
				0.01,
				-1.,
				0.);

			// optimize parameters
			linearFitter->ExecuteCommand("MINI", arglist, 0);
			/*if(strategy > 1)
			{
				linearFitter->ExecuteCommand("MINOS", arglist, 0);
				linearFitter->ExecuteCommand("IMProve", arglist, 0);
				linearFitter->ExecuteCommand("MINOS", arglist, 0); 
			}*/

			// save chi2
			arglist[0] = 3;
			linearFitter->ExecuteCommand("CALL FCN", arglist, 1);

			// save resulting parameters
			char name[64]="";
			double verr,vlow,vhigh;
			linearFitter->GetParameter(0,name,ep[i]->a_linear,verr,vlow,vhigh);
			linearFitter->GetParameter(1,name,ep[i]->b_linear,verr,vlow,vhigh);
			linearFitter->GetErrors(0, dummy1, dummy2, ep[i]->a_linearError, dummy3);
			linearFitter->GetErrors(1, dummy1, dummy2, ep[i]->b_linearError, dummy3);
			// to-do: realize which of error parameter is actually needed for AMFICOM purposes

#ifdef DEBUG_DADARA
			fprintf( dbg_stream, "after  fit: #%d (%d-%d)~(%.3f-%.3f)",
				i, ep[i]->begin, ep[i]->end,ep[i]->begin*dbg_delta_x/1e3, ep[i]->end*dbg_delta_x/1e3);
			fprintf( dbg_stream, " att %.3f", ep[i]->a_linear);
			fprintf( dbg_stream, " ampl1p %.3f\n", ep[i]->b_linear);
#endif
		} // if
	} // for

	delete linearFitter;
}


void Fitter::performSpliceFitting(int start, int end)
{
	double dummy1, dummy2, dummy3;
	double arglist[1];

	TFitter *weldFitter = new TFitter(5);
	weldFitter->SetFCN(fcnWeld);

#ifdef DEBUG_DADARA
	fprintf( dbg_stream, "SPLICE FITTING\n");
#endif
	int i;
	for(i = start; i < end; i++)
	{
		if(ep[i]->type == EventParams::SPLICE)
		{
			nEvent = i;
			
#ifdef DEBUG_DADARA
			fprintf( dbg_stream, "before fitting: #%d event from %d to %d\n", i, ep[i]->begin, ep[i]->end);
			fprintf( dbg_stream, "\t linear attenuation = %.3f\n", ep[i]->a_weld);
			fprintf( dbg_stream, "\t amplitude of center point = %.3f\n", ep[i]->b_weld);
			fprintf( dbg_stream, "\t center point = %.3f\n", ep[i]->center_weld);
			fprintf( dbg_stream, "\t loss = %.3f\n", ep[i]->boost_weld);
			fprintf( dbg_stream, "\t width = %.3f\n", ep[i]->width_weld);
#endif

			// set initial parameters
			weldFitter->SetParameter(
				0,
				"boost of weld",
				ep[i]->boost_weld,
				0.02,
				-6.,
				6.);
			weldFitter->SetParameter(
				1,
				"width of weld",
				ep[i]->width_weld,
				1.,
				1.,
				(double)(ep[i]->end - ep[i]->begin));			
			weldFitter->SetParameter(
				2,
				"centre of weld",
				ep[i]->center_weld,
				1.,
				(double)(ep[i]->begin),
				(double)(ep[i]->end));
			weldFitter->SetParameter(
				3,
				"base amplitude of weld",
				ep[i]->a_weld,
				0.1,
				ep[i]->a_weld-3.,
				ep[i]->a_weld+3.);
			weldFitter->SetParameter(
				4,
				"k",
				ep[i]->b_weld,
				0.01,
				-1.,
				0.);

			// optimize parameters
			weldFitter->ExecuteCommand("MINI", arglist, 0);
			/*if(strategy > 1)
			{
				weldFitter->ExecuteCommand("MINOS", arglist, 0);
			}*/

			// save chi2
			arglist[0] = 3;
			weldFitter->ExecuteCommand("CALL FCN", arglist, 1);

			// save resulting parameters
			char name[64]="";
			double verr,vlow,vhigh;
			weldFitter->GetParameter(0,name,ep[i]->boost_weld,verr,vlow,vhigh);
			weldFitter->GetParameter(1,name,ep[i]->width_weld,verr,vlow,vhigh);
			weldFitter->GetParameter(2,name,ep[i]->center_weld,verr,vlow,vhigh);
			weldFitter->GetParameter(3,name,ep[i]->a_weld,verr,vlow,vhigh);
			weldFitter->GetParameter(4,name,ep[i]->b_weld,verr,vlow,vhigh);
			weldFitter->GetErrors(0, dummy1, dummy2, ep[i]->boost_weldError, dummy3);
			weldFitter->GetErrors(1, dummy1, dummy2, ep[i]->width_weldError, dummy3);
			weldFitter->GetErrors(2, dummy1, dummy2, ep[i]->center_weldError, dummy3);
			weldFitter->GetErrors(3, dummy1, dummy2, ep[i]->a_weldError, dummy3);
			weldFitter->GetErrors(4, dummy1, dummy2, ep[i]->b_weldError, dummy3);

#ifdef DEBUG_DADARA
			fprintf( dbg_stream, "after fitting: #%d event from %d to %d\n", i, ep[i]->begin, ep[i]->end);
			fprintf( dbg_stream, "\t linear attenuation = %.3f\n", ep[i]->a_weld);
			fprintf( dbg_stream, "\t amplitude of center point = %.3f\n", ep[i]->b_weld);
			fprintf( dbg_stream, "\t center point = %.3f\n", ep[i]->center_weld);
			fprintf( dbg_stream, "\t loss = %.3f\n", ep[i]->boost_weld);
			fprintf( dbg_stream, "\t width = %.3f\n", ep[i]->width_weld);
			fprintf( dbg_stream, "\n");
#endif
		} // if
	} // for
	delete weldFitter;
}

void Fitter::performConnectorFitting(int start, int end)
{
	double dummy1, dummy2, dummy3;
	double arglist[1];

	TFitter *connectorFitter = new TFitter(9);
	connectorFitter->SetFCN(fcnConnector);

#ifdef DEBUG_DADARA
	fprintf( dbg_stream, "CONNECTOR FITTING\n");
	dbg_suppress_cf_messages=1;
#endif
	int i;
	for(i = start; i < end; i++)
	{
		if(ep[i]->type == EventParams::CONNECTOR)
		{
			nEvent = i;

#ifdef DEBUG_DADARA
			fprintf( dbg_stream, "before fit #%d (%d-%d)~(%.3f-%.3f)\n",
				i, ep[i]->begin, ep[i]->end, ep[i]->begin*dbg_delta_x/1e3, ep[i]->end*dbg_delta_x/1e3);
#endif

			// set initial parameters
			connectorFitter->SetParameter(
				0,
				"a1",
				ep[i]->a1_connector,
				0.1,
				ep[i]->a1_connector-1.,
				ep[i]->a1_connector+1.);
			connectorFitter->SetParameter(
				1,
				"a2",
				ep[i]->a2_connector,
				0.1,
				ep[i]->a2_connector-1.,
				ep[i]->a2_connector+1.);
			connectorFitter->SetParameter(
				2,
				"aLet",
				ep[i]->aLet_connector,
				0.1,
				0,
				ep[i]->aLet_connector+1.);
			connectorFitter->SetParameter(
				3,
				"w",
				ep[i]->width_connector,
				1.,
				0, //eventSize*.5,
				ep[i]->width_connector*2);
			connectorFitter->SetParameter(
				4,
				"s1",
				ep[i]->sigma1_connector,
				1.,
				0,//0.01,
				ep[i]->width_connector*3);
			connectorFitter->SetParameter(
				5,
				"s2",
				ep[i]->sigma2_connector,
				1.,
				0,//0.01,
				ep[i]->width_connector*10.);
			connectorFitter->SetParameter(
				6,
				"c",
				ep[i]->center_connector,
				1.,
				((double)ep[i]->begin),
				((double)ep[i]->end));
			connectorFitter->SetParameter(
				7,
				"k",
				ep[i]->k_connector,
				0.01,
				0.,
				.5);
			connectorFitter->SetParameter(
				8,
				"stf",
				ep[i]->sigmaFit_connector,
				1.,
				0.01,
				ep[i]->width_connector*5.);

#ifdef DEBUG_DADARA
			const int Npars=9;
			double Ovalues[Npars], Oerrors[Npars];
			int q;
			for (q=0; q<Npars; q++)
			{
				char name[64]="";
				double value,verr,vlow,vhigh;
				connectorFitter->GetParameter(q,name,value,verr,vlow,vhigh);
				Ovalues[q]=value;
				Oerrors[q]=verr;
			}
#endif

			// optimize parameters
			connectorFitter->ExecuteCommand("MINI", arglist, 0);
			/*
			if(strategy >=1)
				connectorFitter->ExecuteCommand("MIGrad", arglist, 0);

			if(strategy >=2)
				connectorFitter->ExecuteCommand("IMProve", arglist, 0);

			if(strategy >=3)
				connectorFitter->ExecuteCommand("MINOS", arglist, 0);
    		*/
			
			// save chi2
			arglist[0] = 3;
			connectorFitter->ExecuteCommand("CALL FCN", arglist, 1);

			// save resulting parameters
			char name[64]="";
			double verr,vlow,vhigh;
			connectorFitter->GetParameter(0,	name,	  ep[i]->a1_connector,verr,vlow,vhigh);
			connectorFitter->GetParameter(1,	name,	  ep[i]->a2_connector,verr,vlow,vhigh);
			connectorFitter->GetParameter(2,	name,	  ep[i]->aLet_connector,verr,vlow,vhigh);
			connectorFitter->GetParameter(3,	name,	  ep[i]->width_connector,verr,vlow,vhigh);
			connectorFitter->GetParameter(4,	name,	  ep[i]->sigma1_connector,verr,vlow,vhigh);
			connectorFitter->GetParameter(5,	name,	  ep[i]->sigma2_connector,verr,vlow,vhigh);
			connectorFitter->GetParameter(6,	name,	  ep[i]->center_connector,verr,vlow,vhigh);
			connectorFitter->GetParameter(7,	name,	  ep[i]->k_connector,verr,vlow,vhigh);
			connectorFitter->GetParameter(8,	name,	  ep[i]->sigmaFit_connector,verr,vlow,vhigh);
			connectorFitter->GetErrors(0, dummy1, dummy2, ep[i]->a1_connectorError, dummy3);
			connectorFitter->GetErrors(1, dummy1, dummy2, ep[i]->a2_connectorError, dummy3);
			connectorFitter->GetErrors(2, dummy1, dummy2, ep[i]->aLet_connectorError, dummy3);
			connectorFitter->GetErrors(3, dummy1, dummy2, ep[i]->width_connectorError, dummy3);
			connectorFitter->GetErrors(4, dummy1, dummy2, ep[i]->sigma1_connectorError, dummy3);
			connectorFitter->GetErrors(5, dummy1, dummy2, ep[i]->sigma2_connectorError, dummy3);
			connectorFitter->GetErrors(6, dummy1, dummy2, ep[i]->center_connectorError, dummy3);
			connectorFitter->GetErrors(7, dummy1, dummy2, ep[i]->k_connectorError, dummy3);
			connectorFitter->GetErrors(8, dummy1, dummy2, ep[i]->sigmaFit_connectorError, dummy3);

#ifdef DEBUG_DADARA
			fprintf (dbg_stream, "parameters\n");
			fprintf (dbg_stream, "# name\tOvalue\tOerror\tlow\thigh\tNvalue\tNerror\n");
			for (q=0; q<Npars; q++)
			{
				char name[64]="";
				double value,verr,vlow,vhigh;
				connectorFitter->GetParameter(q,name,value,verr,vlow,vhigh);
				fprintf (dbg_stream, "%2d %s\t%g\t%g\t%g\t%g\t%g\t%g\n",
					q, name, Ovalues[q],Oerrors[q], vlow,vhigh, value,verr);
			}
#endif
		} // if
	} // for
#ifdef DEBUG_DADARA
	dbg_suppress_cf_messages=0;
#endif
	delete connectorFitter;
}

void fcnLinear(int &, double *, double &chi2, double *par, int iflag)
{
	double a = par[0];
	double k = par[1];
	double chisq = 0.;
	double sq;

	for(int i = ep[nEvent]->begin; i <= ep[nEvent]->end; i++)
	{
		sq = ep[nEvent]->linearFunction(a, k, i) - data[i];

		chisq = chisq + sq*sq;
	}

    if(iflag == 3)
		ep[nEvent]->chi2Linear = chisq / ((double)(ep[nEvent]->end - ep[nEvent]->begin)); 

	chi2 = chisq;
}




void fcnWeld(int &, double *, double &chi2, double *par, int iflag)
{
	double a_shift, w, c, a_base, k, chisq;

	a_shift = par[0];
	w = par[1];
	c = par[2];
	a_base = par[3];
	k = par[4];
    chisq = 0.;

	double sq;

	for(int i = ep[nEvent]->begin; i <= ep[nEvent]->end; i++)
	{
		sq = (ep[nEvent]->weldFunction(a_shift, c, w, a_base, k, i)-data[i]);

		chisq = chisq + sq*sq;
	}

    if(iflag == 3)
		ep[nEvent]->chi2Weld = chisq / (double) (ep[nEvent]->end - ep[nEvent]->begin);

	chi2 = chisq;
}

void fcnConnector(int &, double *, double &chi2, double *par, int iflag)
{
	double a1, a2, aLet, w, s1, s2, c, k, stf, chisq;

#ifdef DEBUG_DADARA
	//fprintf (dbg_stream, "fcnConnector: iflag=%d\n",iflag);
#endif

	a1 = par[0];
	a2 = par[1];
	aLet = par[2];
	w = par[3];
	s1 = par[4];
	s2 = par[5];
	c = par[6];
	k = par[7];
	stf = par[8];
	chisq = 0.;

	int begin = ep[nEvent]->begin;

	if(nEvent == 0) 
		begin += 1; // For dead zone;

	double sq;

	for(int i = begin; i <= ep[nEvent]->end; i++)
	{
		sq = (ep[nEvent]->connectorFunction(a1,a2,aLet,w,s1,s2,k,c,stf,i) - data[i]);
		chisq = chisq + sq*sq;
//			chisq = chisq + sq*sq/sigmaSquared[i];
//			chisq = chisq + fabs(sq);
	}
	//fprintf(dbg_stream,"Total chisq = %g\n",(double)chisq);

#ifdef DEBUG_DADARA
	if (begin==6340)
	{
		FILE *ftmp = fopen ("c:\\dadara_fcnConnector.log", "w");
		for(int i = begin; i <= ep[nEvent]->end; i++)
		{
			fprintf (ftmp, "%i %g %g\n",
				i,
				(double)data[i],
				(double)ep[nEvent]->connectorFunction(a1,a2,aLet,w,s1,s2,k,c,stf,i)
				);
		}
		fclose (ftmp);
	}
#endif

	if(iflag == 3)
		ep[nEvent]->chi2Connector = chisq / (double)(ep[nEvent]->end - ep[nEvent]->begin);

	chi2 = chisq;
}

