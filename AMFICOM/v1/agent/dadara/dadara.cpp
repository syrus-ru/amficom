#include "dadara.h"
#include "com_syrus_AMFICOM_agent_DadaraAnalysisManager.h"
//#include "com_syrus_AMFICOM_analysis_AnalysisManager.h"
#include "InitialAnalysis.h"
#include "Fitter.h"
#include <math.h>

#ifdef DEBUG_DADARA
#include <sys/time.h>
#include <time.h>
FILE* dbg_stream;
double dbg_delta_x; // globally saved delta_x
int dbg_suppress_cf_messages=0;
#endif

JNIEXPORT jdoubleArray JNICALL 
//Java_com_syrus_AMFICOM_analysis_AnalysisManager_analyse(JNIEnv* env, jclass obj,
Java_com_syrus_AMFICOM_agent_DadaraAnalysisManager_ana(JNIEnv *env, jobject obj,
		jint waveletType,             //type of the WaveLet transformation applied.
		jdoubleArray y,               //the refl. itself
		jdouble delta_x,              //dx
		jdouble connFallParams,       // Param. to descr. the behav. of conn. at fall
		jdouble min_level,            // ?
		jdouble max_level_noise,      // ?
		jdouble min_level_to_find_end,// ?
		jdouble min_weld,             // ?
		jdouble min_connector,
		jint strategy,
		jint reflectiveSize,
		jint nonReflectiveSize)
{

#ifdef DEBUG_DADARA
	timeval tv;
	gettimeofday(&tv, NULL);
	tm* t = localtime(&tv.tv_sec);
	const int size = 9 + 6 + 1 + 14 + 1 + 3 + 1;
	char* filename = new char[size];
	sprintf(filename, ".//logs//%04d%02d%02d%02d%02d%02d-dadara.log", 1900 + t->tm_year, 1 + t->tm_mon, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
	filename[size - 1] = 0;
	dbg_stream = fopen(filename, "a");
	delete[] filename;

	fprintf (dbg_stream, "# logfile opened in AnalysisManager_analyse\n");
	dbg_delta_x = delta_x;

	fprintf (dbg_stream, "\t""waveletType %d\n", (int)waveletType);
	fprintf (dbg_stream, "\t""delta_x %g\n", (double)delta_x);
	fprintf (dbg_stream, "\t""connFallParams %g\n", (double)connFallParams);
	fprintf (dbg_stream, "\t""min_level %g\n", (double)min_level);
	fprintf (dbg_stream, "\t""max_level_noise %g\n", (double)max_level_noise);
	fprintf (dbg_stream, "\t""min_level_to_find_end %g\n", (double)min_level_to_find_end);
	fprintf (dbg_stream, "\t""min_weld %g\n", (double)min_weld);
	fprintf (dbg_stream, "\t""min_connector %g\n", (double)min_connector);
	fprintf (dbg_stream, "\t""strategy %d\n", (int)strategy);
	
#endif


//Работа с Java Native Interface
	double* data    = (double*)env->GetDoubleArrayElements(y, NULL);
	jsize sz = env->GetArrayLength(y);	
	int data_l = sz;
	jdoubleArray ret;

#ifdef DEBUG_DADARA
	fprintf (dbg_stream, "\tsz %d\n", (int)sz);
	if (0)
	{
		int i;
		for (i=0; i<sz; i++) fprintf (dbg_stream, "%g ", (double)data[i]);
		fprintf (dbg_stream, "\n");
	}
#endif

	if (min_level <= 0.01)
		min_level =  0.01;		// typ. 0.04
	if (max_level_noise < 0.05)
		max_level_noise = 0.05;
	if (max_level_noise > 5.)
		max_level_noise = 5.;	// typ. 2.0  (input was=5)
	if (connFallParams > 0.49)
		connFallParams = 0.49;	// typ. 0.3
	if (connFallParams < 0.001)
		connFallParams = 0.001;
	if (min_weld < 0.01)
		min_weld = 0.01;		// typ. 0.4
	if (min_connector < 0.05)
		min_connector = 0.05;	// typ. 0.2
	if (min_level_to_find_end < 1.)
		min_level_to_find_end = 1.; //  typ. 6.0

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
		reflectiveSize, 
		nonReflectiveSize);

	EventParams **ep = ia->getEventParams();
	int nEvents = ia->getEventsCount();
	
	if (strategy >= 0)
	{
		double meanAttenuation = ia->getMeanAttenuation();
		Fitter *fitter = new Fitter(ep, nEvents, data, strategy, meanAttenuation);
		ep = fitter->getEventParams();
	}


//----
printf("$$$$$$$ deleting ia $$$$$\n");
//----
	delete ia;

	int Ret_Length;
	double *RET = setParRet(nEvents, ep, Ret_Length);
//----
printf("$$$$$$$ setParRet done $$$$$\n");
//----
	ret = (env)->NewDoubleArray(Ret_Length);

#ifdef DEBUG_DADARA
	fprintf( dbg_stream, "return to java: %d (38*%d) doubles \n", Ret_Length, Ret_Length/38);
#endif

//Освобождение массивов в Java Native Interface
	(env)->ReleaseDoubleArrayElements(y,data,JNI_ABORT);
	(env)->SetDoubleArrayRegion(ret,0,Ret_Length,RET);
//(env)->ReleaseDoubleArrayElements(ret,final_ret_struct,JNI_ABORT);


#ifdef DEBUG_DADARA
	{
		int i;
		fprintf (dbg_stream, "analysis return (%d) is: ", Ret_Length);
		for (i=0; i<Ret_Length; i++)
		{
			fprintf (dbg_stream, "%g ", RET[i]);
		}
		fprintf (dbg_stream, "\n");
	}
	fprintf( dbg_stream, "# logfile closed\n");
	fclose( dbg_stream );
#endif

	delete[] RET;
	return ret;
}
/*
JNIEXPORT jdoubleArray JNICALL 
Java_com_syrus_AMFICOM_analysis_AnalysisManager_fit(
	JNIEnv* env, 
	jclass obj,
	jdoubleArray y,
	jdouble delta_x,
	jdoubleArray params_data,
	jint strategy,
	jdouble meanAttenuation)
{
#ifdef DEBUG_DADARA
	timeval tv;
	gettimeofday(&tv, NULL);
	tm* t = localtime(&tv.tv_sec);
	const int size = 9 + 6 + 1 + 14 + 1 + 3 + 1;
	char* filename = new char[size];
	sprintf(filename, ".//logs//%04d%02d%02d%02d%02d%02d-dadara.log", 1900 + t->tm_year, 1 + t->tm_mon, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
	filename[size - 1] = 0;
	dbg_stream = fopen(filename, "a");
	delete[] filename;

	fprintf (dbg_stream, "# logfile opened in AnalysisManager_analyse\n");
	dbg_delta_x = delta_x;

	fprintf (dbg_stream, "\t""delta_x %g\n", (double)delta_x);
	fprintf (dbg_stream, "\t""strategy %d\n", (int)strategy);
	
#endif


	double* data = (double*)env->GetDoubleArrayElements(y, NULL);
//	jsize size = env->GetArrayLength(y);	
	double* ep_data = (double*)env->GetDoubleArrayElements(params_data, NULL);
	jsize params_size = env->GetArrayLength(params_data);	
	int nEvents = params_size / 38;

	EventParams **ep = new EventParams *[nEvents];
	getEventParams(ep_data, params_size, ep);

	Fitter *fitter = new Fitter(ep, nEvents, data, strategy, (double)meanAttenuation);
	ep = fitter->getEventParams();

	jdoubleArray ret;
	int Ret_Length;
	double *RET = setParRet(nEvents, ep, Ret_Length);
	ret = (env)->NewDoubleArray(Ret_Length);

	//Освобождение массивов в Java Native Interface
	(env)->ReleaseDoubleArrayElements(y,data,JNI_ABORT);
	(env)->SetDoubleArrayRegion(ret,0,Ret_Length,RET);

#ifdef DEBUG_DADARA
	{
		int i;
		fprintf (dbg_stream, "analysis return (%d) is: ", Ret_Length);
		for (i=0; i<Ret_Length; i++)
		{
			fprintf (dbg_stream, "%g ", RET[i]);
		}
		fprintf (dbg_stream, "\n");
	}
	fprintf( dbg_stream, "# logfile closed\n");
	fclose( dbg_stream );
#endif

	delete fitter;
	delete[] ep;
	delete[] RET;
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
		
		ret[i]	  = eVp->type;
		ret[i+1]  = eVp->begin;
		ret[i+2]  = eVp->end;
//linear
	    ret[i+3]  = eVp->a_linear;
		ret[i+4]  = eVp->a_linearError;
	    ret[i+5]  = eVp->b_linear;
		ret[i+6]  = eVp->b_linearError;
	    ret[i+7]  = eVp->chi2Linear;
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

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void getEventParams(double *ep_data, int ep_data_length, EventParams **ep)
{
	int counter = 0;
	EventParams *eVp;
	for(int i = 0; i < ep_data_length; i+=38)
	{
		eVp = new EventParams();
		
		eVp->type					=	(int)ep_data[i];
		eVp->begin					=	(int)ep_data[i+1];
		eVp->end					=	(int)ep_data[i+2];
//linear
	    eVp->a_linear				=	ep_data[i+3];
		eVp->a_linearError			=	ep_data[i+4];
	    eVp->b_linear				=	ep_data[i+5];
		eVp->b_linearError			=	ep_data[i+6];
	    eVp->chi2Linear				=	ep_data[i+7];
//weld
	    eVp->a_weld					=	ep_data[i+8];
		eVp->a_weldError			=	ep_data[i+9];
	    eVp->b_weld					=	ep_data[i+10];
		eVp->b_weldError			=	ep_data[i+11];
	    eVp->boost_weld				=	ep_data[i+12];
		eVp->boost_weldError		=	ep_data[i+13];
	    eVp->center_weld			=	ep_data[i+14];
		eVp->center_weldError		=	ep_data[i+15];
	    eVp->width_weld				=	ep_data[i+16];
		eVp->width_weldError		=	ep_data[i+17];
	    eVp->chi2Weld				=	ep_data[i+18];
//connector
		eVp->a1_connector			=	ep_data[i+19];
		eVp->a1_connectorError		=	ep_data[i+20];
		eVp->a2_connector			=	ep_data[i+21];
		eVp->a2_connectorError		=	ep_data[i+22];
		eVp->aLet_connector			=	ep_data[i+23];
		eVp->aLet_connectorError	=	ep_data[i+24];
		eVp->width_connector		=	ep_data[i+25];
		eVp->width_connectorError	=	ep_data[i+26];
		eVp->center_connector		=	ep_data[i+27];
		eVp->center_connectorError	=	ep_data[i+28];
		eVp->sigma1_connector		=	ep_data[i+29];
		eVp->sigma1_connectorError	=	ep_data[i+30];
		eVp->sigma2_connector		=	ep_data[i+31];
		eVp->sigma2_connectorError	=	ep_data[i+32];
		eVp->sigmaFit_connector		=	ep_data[i+33];
		eVp->sigmaFit_connectorError=	ep_data[i+34];
		eVp->k_connector			=	ep_data[i+35];
		eVp->k_connectorError		=	ep_data[i+36];
		eVp->chi2Connector			=	ep_data[i+37];

		ep[counter++] = eVp;
	}

}

