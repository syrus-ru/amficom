#include "dadara.h"
#include "com_syrus_AMFICOM_analysis_dadara_DadaraAnalysisManager.h"
//#include "com_syrus_AMFICOM_analysis_AnalysisManager.h"
#include "InitialAnalysis.h"
#include "TFitter.h"
#include <math.h>

#ifdef DEBUG_DADARA
#include <sys/time.h>
#include <time.h>
#endif

JNIEXPORT jdoubleArray JNICALL 
//Java_com_syrus_AMFICOM_analysis_AnalysisManager_analyse(JNIEnv* env, jclass cls,
Java_com_syrus_AMFICOM_analysis_dadara_DadaraAnalysisManager_ana(JNIEnv* env, jobject obj,
		      jint waveletType,             //type of the WaveLet transformation applied.
		      jdoubleArray y,               //the refl. itself
		      jdouble delta_x,              //dx
		      jint  findEnd,                //char. event size
		      jdouble connFallParams,       // Param. to descr. the behav. of conn. at fall
		      jdouble min_level,            // ?
		      jdouble max_level_noise,      // ?
		      jdouble min_level_to_find_end,// ? 
		      jdouble min_weld,             // ?
		      jdouble min_connector,
		      jint strategy) //set sratrategy  4 - smart fitting.
{

#ifdef DEBUG_DADARA
	timeval tv;
	gettimeofday(&tv, NULL);
	tm* t = localtime(&tv.tv_sec);
	const int size = 9 + 6 + 1 + 14 + 1 + 3 + 1;
	char* filename = new char[size];
	sprintf(filename, ".//logs//%04d%02d%02d%02d%02d%02d-dadara.log", 1900 + t->tm_year, 1 + t->tm_mon, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
	filename[size - 1] = 0;
	stream = fopen(filename, "a");
	delete[] filename;
#endif

//Работа с Java Native Interface
	data    = (double*)env->GetDoubleArrayElements(y, NULL);
	jsize sz = env->GetArrayLength(y);	
	int data_l = sz;
	jdoubleArray ret;
	chi2norma = 0.003;

	if(min_level < 0.01) 
		min_level = 0.01;
	if(max_level_noise < 0.05) 
		max_level_noise = 0.05;
	if(max_level_noise > 2.) 
		max_level_noise = 2.;
	if(connFallParams > 0.49) 
		connFallParams = 0.49;
	if(connFallParams < 0.001)
		connFallParams = 0.001;
	if(min_weld < 0.01)
		min_weld = 0.01;
	if(min_connector < 0.05)
		min_connector = 0.05;
	if(min_level_to_find_end < 1.) 
		min_level_to_find_end = 1.;

	InitialAnalysis *ia = new InitialAnalysis(data, 
		data_l,
		delta_x, 
		min_level, 
		min_weld, 
		min_connector, 
		min_level_to_find_end,
		max_level_noise,
		waveletType,
		connFallParams, 
		findEnd);

	ep = ia->getEventParams();
	int nEvents = ia->getEventsCount();
	int eventSize = ia->getEventSize();

#ifdef DEBUG_DADARA
	int l = ep[nEvents-1]->begin+eventSize*3;
	fprintf( stream, "\n***** NEW REFLECTOGRAMM ANALYSIS *****\n");
	fprintf( stream, "after initial analysis:\n");
	fprintf( stream, "\t number of events  = %d\n", nEvents);
	fprintf( stream, "\t last point = %d (%.3fkm)\n", l, delta_x * l / 1000.);
#endif

	if(strategy >= 0)	
	{
		int counter = 0;
		while (counter < 1)
		{
			performLinearFitting	(ep, 0, nEvents, strategy);
			performSpliceFitting	(ep, 0, nEvents, strategy);
			performConnectorFitting	(ep, 0, nEvents, strategy, eventSize);

			if (checkFittness(nEvents) == 0)
			{
				counter = 3;
			}
			else
			{

#ifdef DEBUG_DADARA
				fprintf( stream, "@error: fitness is bad, try to refit for %d time\n", counter+1);
#endif
			}
			counter++;
		}
	}

#ifdef DEBUG_DADARA
	l = ep[nEvents-1]->begin+eventSize*3;
	fprintf( stream, "after fitting:\n");
	fprintf( stream, "\t number of events  = %d\n", nEvents);
	fprintf( stream, "\t last point = %d (%.3fkm)\n", l, delta_x * l / 1000.);
#endif


	int Ret_Length;
	double *RET = setParRet(nEvents, ep, Ret_Length); 
	ret = (env)->NewDoubleArray(Ret_Length);
	delete ia;

#ifdef DEBUG_DADARA
	fprintf( stream, "return to java: %d (38*%d) doubles \n", Ret_Length, Ret_Length/38);
#endif

//Освобождение массивов в Java Native Interface
	(env)->ReleaseDoubleArrayElements(y,data,JNI_ABORT);
	(env)->SetDoubleArrayRegion(ret,0,Ret_Length,RET);
//(env)->ReleaseDoubleArrayElements(ret,final_ret_struct,JNI_ABORT);


#ifdef DEBUG_DADARA
	fprintf( stream, "\n");
	fclose( stream );
#endif

	return ret;
}



int checkFittness (int nEvents)
{
	for(int i=0; i<nEvents; i++)
	{
		if(ep[i]->type == EventParams::LINEAR)
		{
			if (ep[i]->a_linear == 0)
				return -1;
		}
		else if(ep[i]->type == EventParams::CONNECTOR)
		{
			if (ep[i]->a1_connector < -1000 ||
				ep[i]->a2_connector < -1000 ||
				ep[i]->a1_connector > 1000 ||
				ep[i]->a2_connector > 1000)
				return -1;
		}

	}
	return 0;
}

void performLinearFitting(EventParams **ep, int start, int end, int strategy)
{
	double emptyParam1, emptyParam2, emptyParam3;
	double arglist[1];

	TFitter *linearFitter = new TFitter(2);
	linearFitter->SetFCN(fcnLinear);

#ifdef DEBUG_DADARA
	fprintf( stream, "LINEAR FITTING\n");
#endif
	int i;
	for(i = start; i < end; i++)
	{
		nEvent = i;
		if(ep[i]->type == EventParams::LINEAR) // Linear fitting //	
		{

#ifdef DEBUG_DADARA
			fprintf( stream, "before fitting: #%d event from %d to %d\n", i, ep[i]->begin, ep[i]->end);
			fprintf( stream, "\t attenuation = %.3f\n", ep[i]->a_linear);
			fprintf( stream, "\t amplitude of first point = %.3f\n", ep[i]->b_linear);
#endif
			linearFitter->SetParameter(0, 
				"a", 
				ep[i]->a_linear,
				0.1,
				ep[i]->a_linear - 3.,
				ep[i]->a_linear + 3.);
			linearFitter->SetParameter(1,
				"k", 
				ep[i]->b_linear,
				0.01,
				-1., 
				0.);

			arglist[0] = 1;
			linearFitter->ExecuteCommand("CALL FCN", arglist, 1);

			arglist[0] = -1;
			linearFitter->ExecuteCommand("SET PRINT", arglist, 1);
			/*  arglist[0] = 0;
			linearFitter->ExecuteCommand("MINI", arglist, 0);
			linearFitter->ExecuteCommand("MINOS", arglist, 0);
			if(strategy > 1)
			{
				linearFitter->ExecuteCommand("IMProve", arglist, 0);
				linearFitter->ExecuteCommand("MINOS", arglist, 0); 
			}	*/

			arglist[0] = 3;
			linearFitter->ExecuteCommand("CALL FCN", arglist, 1);

			linearFitter->GetErrors(0, emptyParam1, emptyParam2, ep[i]->a_linearError, emptyParam3);
			linearFitter->GetErrors(1, emptyParam1, emptyParam2, ep[i]->b_linearError, emptyParam3); 

#ifdef DEBUG_DADARA
			fprintf( stream, "after fitting: #%d event from %d to %d\n", i, ep[i]->begin, ep[i]->end);
			fprintf( stream, "\t attenuation = %.3f\n", ep[i]->a_linear);
			fprintf( stream, "\t amplitude of first point = %.3f\n", ep[i]->b_linear);
			fprintf( stream, "\n");
#endif
		}
	}
	delete linearFitter;
}


void performSpliceFitting(EventParams **ep, int start, int end, int strategy)
{
	double emptyParam1, emptyParam2, emptyParam3;
	double arglist[1];

	TFitter *weldFitter = new TFitter(5);
	weldFitter->SetFCN(fcnWeld);

#ifdef DEBUG_DADARA
	fprintf( stream, "SPLICE FITTING\n");
#endif
	int i;
	for(i = start; i < end; i++)
	{
		nEvent = i;
		if(ep[i]->type == EventParams::SPLICE)
		{

#ifdef DEBUG_DADARA
			fprintf( stream, "before fitting: #%d event from %d to %d\n", i, ep[i]->begin, ep[i]->end);
			fprintf( stream, "\t linear attenuation = %.3f\n", ep[i]->a_weld);
			fprintf( stream, "\t amplitude of center point = %.3f\n", ep[i]->b_weld);
			fprintf( stream, "\t center point = %.3f\n", ep[i]->center_weld);
			fprintf( stream, "\t loss = %.3f\n", ep[i]->boost_weld);
			fprintf( stream, "\t width = %.3f\n", ep[i]->width_weld);
#endif

			weldFitter->SetParameter(0, 
				"boost of weld", 
				ep[i]->boost_weld,
				0.02,
				-6., 
				6.);
			weldFitter->SetParameter(1,
				"width of weld",
				ep[i]->width_weld,
				1.,
				1.,
				(double)(ep[i]->end - ep[i]->begin));
			weldFitter->SetParameter(2,
				"centre of weld",
				ep[i]->center_weld,
				1.,
				(double)(ep[i]->begin),
				(double)(ep[i]->end));
			weldFitter->SetParameter(3,
				"base amplitude of weld",
				ep[i]->a_weld,
				0.1,
				ep[i]->a_weld-3.,
				ep[i]->a_weld+3.);
			weldFitter->SetParameter(4, 
				"k",
				ep[i]->b_weld,
				0.01,
				-1.,
				0.);

			arglist[0] = 1;
			weldFitter->ExecuteCommand("CALL FCN", arglist, 1);
					
			arglist[0] = -1;
			weldFitter->ExecuteCommand("SET PRINT", arglist, 1);
				
			arglist[0] = 0;
//			weldFitter->FixParameter(1);
//			weldFitter->FixParameter(2);
			weldFitter->ExecuteCommand("MINI", arglist, 0);

			if(strategy > 1)
			{
				weldFitter->ExecuteCommand("MINI", arglist, 0);
				weldFitter->ExecuteCommand("MINOS", arglist, 0);
			}

			arglist[0] = 3;
			weldFitter->ExecuteCommand("CALL FCN", arglist, 1);

			weldFitter->GetErrors(0, emptyParam1, emptyParam2, ep[i]->boost_weldError, emptyParam3);
			weldFitter->GetErrors(1, emptyParam1, emptyParam2, ep[i]->width_weldError, emptyParam3);
			weldFitter->GetErrors(2, emptyParam1, emptyParam2, ep[i]->center_weldError, emptyParam3);
			weldFitter->GetErrors(3, emptyParam1, emptyParam2, ep[i]->a_weldError, emptyParam3);
			weldFitter->GetErrors(4, emptyParam1, emptyParam2, ep[i]->b_weldError, emptyParam3);

#ifdef DEBUG_DADARA
			fprintf( stream, "after fitting: #%d event from %d to %d\n", i, ep[i]->begin, ep[i]->end);
			fprintf( stream, "\t linear attenuation = %.3f\n", ep[i]->a_weld);
			fprintf( stream, "\t amplitude of center point = %.3f\n", ep[i]->b_weld);
			fprintf( stream, "\t center point = %.3f\n", ep[i]->center_weld);
			fprintf( stream, "\t loss = %.3f\n", ep[i]->boost_weld);
			fprintf( stream, "\t width = %.3f\n", ep[i]->width_weld);
			fprintf( stream, "\n");
#endif
		}
	}
	delete weldFitter;
}

void performConnectorFitting(EventParams **ep, int start, int end, int strategy, int eventSize)
{
	double emptyParam1, emptyParam2, emptyParam3;
	double arglist[1];

	TFitter *connectorFitter = new TFitter(9);
	connectorFitter->SetFCN(fcnConnector);

#ifdef DEBUG_DADARA
	fprintf( stream, "CONNECTOR FITTING\n");
#endif
	int i;
	for(i = start; i < end; i++)
	{
		nEvent = i;
		if(ep[i]->type == EventParams::CONNECTOR)
		{

#ifdef DEBUG_DADARA
			fprintf( stream, "before fitting: #%d event from %d to %d\n", i, ep[i]->begin, ep[i]->end);
			fprintf( stream, "\t first point amplitude = %.3f\n", ep[i]->a1_connector);
			fprintf( stream, "\t last point amplitude = %.3f\n", ep[i]->a2_connector);
			fprintf( stream, "\t center point amplitude = %.3f\n", ep[i]->aLet_connector);
			fprintf( stream, "\t center point = %.3f\n", ep[i]->center_connector);
			fprintf( stream, "\t width = %.3f\n", ep[i]->width_connector);
#endif
			connectorFitter->SetParameter(0, 
				"a1",
				ep[i]->a1_connector,
				0.1,
				ep[i]->a1_connector-1.,
				ep[i]->a1_connector+1.);
			connectorFitter->SetParameter(1, 
				"a2",
		        ep[i]->a2_connector,
				0.1,
				ep[i]->a2_connector-1.,
				ep[i]->a2_connector+1.);
			connectorFitter->SetParameter(2, 
				"aLet",
				ep[i]->aLet_connector,
				0.1,
//				min_level,
				0,
				ep[i]->aLet_connector+1.);
			connectorFitter->SetParameter(3, 
				"w",
				ep[i]->width_connector,
				1.,
				eventSize*.5,
				ep[i]->width_connector*2);
			connectorFitter->SetParameter(4, 
				"s1",
		        ep[i]->sigma1_connector,
				1.,
				0.01,
				ep[i]->width_connector);
			connectorFitter->SetParameter(5, 
				"s2",
				ep[i]->sigma2_connector,
				1.,
				0.01,
				ep[i]->width_connector*5.);
			connectorFitter->SetParameter(6, 
				"c",
				ep[i]->center_connector, 
				1.,
				((double)ep[i]->begin), 
				((double)ep[i]->end));
			connectorFitter->SetParameter(7,
				"k",
				ep[i]->k_connector,  
				0.01,
				0., 
				.5);
			connectorFitter->SetParameter(8,
				"stf",
				ep[i]->sigmaFit_connector, 
				1.,
				0.01, 
				ep[i]->width_connector*5.);

			arglist[0] = 1;
			connectorFitter->ExecuteCommand("CALL FCN", arglist, 1);

			arglist[0] = -1;
			connectorFitter->ExecuteCommand("SET PRINT", arglist, 1);
					
			arglist[0] = 0;
			connectorFitter->ExecuteCommand("MINI", arglist, 0);

			if(strategy >=1)
				connectorFitter->ExecuteCommand("MIGrad", arglist, 0);

       		if(strategy >=2)
				connectorFitter->ExecuteCommand("IMProve", arglist, 0);

			if(strategy >=3)
				connectorFitter->ExecuteCommand("MINOS", arglist, 0);
       	
			if(strategy >=4)
				connectorFitter->ExecuteCommand("MINOS", arglist, 0);
    
			arglist[0] = 3;
				connectorFitter->ExecuteCommand("CALL FCN", arglist, 1);

			connectorFitter->GetErrors(0, emptyParam1, emptyParam2, ep[i]->a1_connectorError, emptyParam3);
			connectorFitter->GetErrors(1, emptyParam1, emptyParam2, ep[i]->a2_connectorError, emptyParam3);
			connectorFitter->GetErrors(2, emptyParam1, emptyParam2, ep[i]->aLet_connectorError, emptyParam3);
			connectorFitter->GetErrors(3, emptyParam1, emptyParam2, ep[i]->width_connectorError, emptyParam3);
			connectorFitter->GetErrors(4, emptyParam1, emptyParam2, ep[i]->sigma1_connectorError, emptyParam3);
			connectorFitter->GetErrors(5, emptyParam1, emptyParam2, ep[i]->sigma2_connectorError, emptyParam3);
			connectorFitter->GetErrors(6, emptyParam1, emptyParam2, ep[i]->center_connectorError, emptyParam3);
			connectorFitter->GetErrors(7, emptyParam1, emptyParam2, ep[i]->k_connectorError, emptyParam3);
			connectorFitter->GetErrors(8, emptyParam1, emptyParam2, ep[i]->sigmaFit_connectorError, emptyParam3);


#ifdef DEBUG_DADARA
			fprintf( stream, "after fitting: #%d event from %d to %d\n", i, ep[i]->begin, ep[i]->end);
			fprintf( stream, "\t first point amplitude = %.3f\n", ep[i]->a1_connector);
			fprintf( stream, "\t last point amplitude = %.3f\n", ep[i]->a2_connector);
			fprintf( stream, "\t center point amplitude = %.3f\n", ep[i]->aLet_connector);
			fprintf( stream, "\t center point = %.3f\n", ep[i]->center_connector);
			fprintf( stream, "\t width = %.3f\n", ep[i]->width_connector);
			fprintf( stream, "\n");
#endif
		}
	}
	delete connectorFitter;
}

/*
double *setParRet(int n_events, EventParams **ep, int &Ret_Length)
{
	int i;
	int ret_size = n_events*44;
	int counter = 0;
	
    double *ret = new double[ret_size];

	Ret_Length = ret_size;

	EventParams *eVp;


	for(i=0; i<ret_size; i+=44)
	{
		eVp = ep[counter];
//linear
	     ret[i]   = eVp->a_linear;
		 ret[i+1] = eVp->a_linearError;
	     ret[i+2] = eVp->b_linear;
		 ret[i+3] = eVp->b_linearError;
	     ret[i+4] = eVp->begin;
		 ret[i+5] = eVp->end;
	     ret[i+6] = eVp->chi2Linear;
		 if(eVp->type == EventParams::LINEAR)
			ret[i+7] = 1.;
		 else
			ret[i+7] = 0.;
//weld
	     ret[i+8]  = eVp->a_weld;
		 ret[i+9]  = eVp->a_weldError;
	     ret[i+10] = eVp->b_weld;
		 ret[i+11] = eVp->b_weldError;
	     ret[i+12] = eVp->boost_weld;
		 ret[i+13] = eVp->boost_weldError;
	     ret[i+14] = eVp->center_weld;
		 ret[i+15] = eVp->center_weldError;
	     ret[i+16] = eVp->width_weld;
		 ret[i+17] = eVp->width_weldError;
	     ret[i+18] = eVp->begin;
		 ret[i+19] = eVp->end;
	     ret[i+20] = eVp->chi2Weld;
		 if(eVp->type == EventParams::SPLICE)
			ret[i+21] = 1.;
		 else
			ret[i+21] = 0.;

//connector
		 
		 ret[i+22] = eVp->a1_connector;
		 ret[i+23] = eVp->a1_connectorError;
		 ret[i+24] = eVp->a2_connector;
		 ret[i+25] = eVp->a2_connectorError;
		 ret[i+26] = eVp->aLet_connector;
		 ret[i+27] = eVp->aLet_connectorError;
		 ret[i+28] = eVp->width_connector;
		 ret[i+29] = eVp->width_connectorError;
		 ret[i+30] = eVp->center_connector;
		 ret[i+31] = eVp->center_connectorError;
		 ret[i+32] = eVp->sigma1_connector;
		 ret[i+33] = eVp->sigma1_connectorError;
		 ret[i+34] = eVp->sigma2_connector;
		 ret[i+35] = eVp->sigma2_connectorError;
		 ret[i+36] = eVp->sigmaFit_connector;
		 ret[i+37] = eVp->sigmaFit_connectorError;
		 ret[i+38] = eVp->k_connector;
		 ret[i+39] = eVp->k_connectorError;
		 ret[i+40] = eVp->begin;
		 ret[i+41] = eVp->end;
		 ret[i+42] = eVp->chi2Connector;
		 if(eVp->type == EventParams::CONNECTOR)
			ret[i+43] = 1.;
		 else
			ret[i+43] = 0.;


		 counter = counter+1;


	}

	return ret;
}


*/


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
double *setParRet(int n_events, EventParams **ep, int &Ret_Length)
{
	int i;
	int ret_size = n_events*38;
	
	double *ret = new double[ret_size];

	Ret_Length = ret_size;

	int counter = 0;
	EventParams *eVp;
	for(i=0; i<ret_size; i+=38)
	{
		eVp = ep[counter++];
		
		ret[i] = eVp->type;
		ret[i+1] = eVp->begin;
		ret[i+2] = eVp->end;
//linear
	    ret[i+3]   = eVp->a_linear;
		ret[i+4] = eVp->a_linearError;
	    ret[i+5] = eVp->b_linear;
		ret[i+6] = eVp->b_linearError;
	    ret[i+7] = eVp->chi2Linear;
//weld
	    ret[i+8]  = eVp->a_weld;
		ret[i+9]  = eVp->a_weldError;
	    ret[i+10] = eVp->b_weld;
		ret[i+11] = eVp->b_weldError;
	    ret[i+12] = eVp->boost_weld;
		ret[i+13] = eVp->boost_weldError;
	    ret[i+14] = eVp->center_weld;
		ret[i+15] = eVp->center_weldError;
	    ret[i+16] = eVp->width_weld;
		ret[i+17] = eVp->width_weldError;
	    ret[i+18] = eVp->chi2Weld;
//connector
		ret[i+19] = eVp->a1_connector;
		ret[i+20] = eVp->a1_connectorError;
		ret[i+21] = eVp->a2_connector;
		ret[i+22] = eVp->a2_connectorError;
		ret[i+23] = eVp->aLet_connector;
		ret[i+24] = eVp->aLet_connectorError;
		ret[i+25] = eVp->width_connector;
		ret[i+26] = eVp->width_connectorError;
		ret[i+27] = eVp->center_connector;
		ret[i+28] = eVp->center_connectorError;
		ret[i+29] = eVp->sigma1_connector;
		ret[i+30] = eVp->sigma1_connectorError;
		ret[i+31] = eVp->sigma2_connector;
		ret[i+32] = eVp->sigma2_connectorError;
		ret[i+33] = eVp->sigmaFit_connector;
		ret[i+34] = eVp->sigmaFit_connectorError;
		ret[i+35] = eVp->k_connector;
		ret[i+36] = eVp->k_connectorError;
		ret[i+37] = eVp->chi2Connector;
	}

	return ret;
}

void fcnLinear(int &, double *, double &chi2, double *par, int iflag)
{
	double a, k, chisq;

	a = par[0];
	k = par[1];
	chisq = 0.;

	double sq;

	for(int i = ep[nEvent]->begin; i <= ep[nEvent]->end; i++)
	{
		sq = ep[nEvent]->linearFunction(a, k, i) - data[i];

		chisq = chisq + sq*sq/chi2norma;
	}


    if(iflag == 3)
	{
		ep[nEvent]->a_linear  = a;
		ep[nEvent]->b_linear  = k;
		ep[nEvent]->chi2Linear= chisq / ((double)(ep[nEvent]->end - ep[nEvent]->begin)); 
	}

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

		chisq = chisq + sq*sq/chi2norma;
	}

    if(iflag == 3)
	{
		ep[nEvent]->a_weld      = a_base;
		ep[nEvent]->boost_weld  = a_shift;
		ep[nEvent]->center_weld = c;
		ep[nEvent]->b_weld      = k;
		ep[nEvent]->width_weld  = w;
		ep[nEvent]->chi2Weld    = chisq/((double)(ep[nEvent]->end - ep[nEvent]->begin));
	}
     chi2 = chisq;
}




void fcnConnector(int &, double *, double &chi2, double *par, int iflag)
{
	double a1, a2, aLet, w, s1, s2, c, k, stf, chisq;

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

		chisq = chisq + sq*sq/chi2norma;
//			chisq = chisq + sq*sq/sigmaSquared[i];
//			chisq = chisq + fabs(sq);
	}

	if(iflag == 3)
	{
//		ep[nEvent]->a1_connector   = a1;
		ep[nEvent]->a2_connector   = a2;
//		ep[nEvent]->aLet_connector = aLet;
		ep[nEvent]->center_connector = c;
		ep[nEvent]->k_connector      = k;
		ep[nEvent]->width_connector  = w;
		ep[nEvent]->sigma1_connector = s1;
		ep[nEvent]->sigma2_connector = s2;
		ep[nEvent]->sigmaFit_connector = stf;
		ep[nEvent]->chi2Connector = chisq/((double)(ep[nEvent]->end - ep[nEvent]->begin));
	}
	chi2 = chisq;
}
