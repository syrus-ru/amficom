#include <assert.h>
#include <jni.h>
#include <windows.h> // for APIENTRY #define
#include <math.h>

#include "IAsimple.h"

#include "../Common/com_syrus_AMFICOM_analysis_AnalysisManager.h"
#include "../Common/com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent.h"

#include "../Common/EventP.h"

#include "../Common/Java-bridge.h"

void EP32EPnew(IAEvent* epo, EventP &epn, double delta_x);

JNIEXPORT jobjectArray JNICALL 
Java_com_syrus_AMFICOM_analysis_AnalysisManager_analyse2(
	JNIEnv* env,
	jclass obj,
	jint waveletType,             //type of the WaveLet transformation applied.
	jdoubleArray y,               //the refl. itself
	jdouble delta_x,              //dx
	jdouble connFallParams,       // Param. to descr. the behav. of conn. at fall
	jdouble min_level,            // ?
	jdouble max_level_noise,      // ?
	jdouble min_level_to_find_end,// ? 
	jdouble min_weld,             // ?
	jdouble min_connector,
	jdoubleArray ret_meanAttenuation,
	jint reflectiveSize, 
	jint nonReflectiveSize)
{

	// берем в JNI массив y-значений (его надо будет потом освободить)
	double* data    = (double*)env->GetDoubleArrayElements(y, NULL);
	jsize sz = env->GetArrayLength(y);
	int data_l = sz;

	// корректируем параметры
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

	ArrList events;

	fprintf(stderr, "DA3: #1\n");

	InitialAnalysis2(
		&events,
		data,
		data_l,
		delta_x,
		min_weld,
		min_connector,
		reflectiveSize, 
		nonReflectiveSize);

	fprintf(stderr, "DA3: #2\n");

	int nEvents = events.getLength();


	// convert EventParams to EventP
	EventP *re = new EventP[nEvents];
	assert (re);
	int i;

	for (i = 0; i < nEvents; i++)
		EP32EPnew((IAEvent *)(events[i]), re[i], delta_x);

	events.disposeAll();

	jobjectArray ret_obj = EventP_C2J_arr(env, re, nEvents, 0);

	delete re;

	fprintf(stderr, "DA3: #3\n");

	// send to JNI

	double meanAtt = 0; // FIXIT

	jsize len = (env)->GetArrayLength(ret_meanAttenuation);
	if (len)
	{
		jdouble *body = (env)->GetDoubleArrayElements(ret_meanAttenuation, 0);
		body[0] = meanAtt;
		(env)->ReleaseDoubleArrayElements(ret_meanAttenuation, body, 0); // copy-back
	}

//Освобождение массивов в Java Native Interface
	(env)->ReleaseDoubleArrayElements(y,data,JNI_ABORT);

	return ret_obj;
}


void EP32EPnew(IAEvent* epo, EventP &epn, double delta_x)
{
	// fill ep
	epn.begin	= epo->begin;
	epn.end		= epo->end;
	epn.delta_x	= delta_x;

	// fill ep.mf, ep.type, ep.mf.pars
	switch (epo->type)
	{
	case IAEvent_LIN:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_LINEAR;
		break;

	case IAEvent_SPL:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_WELD;
		break;

	case IAEvent_CON:
		epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_CONNECTOR;
		break;

	//case EventParams_SINGULARITY:
	//	epn.gentype = com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent_SINGULARITY;
	//	break;

	default:
		fprintf(stderr, "Unknown ep3->type = %d\n", (int )epo->type);
		fflush(stderr);
		assert(0);
	}

	epn.mf.init(MF_ID_BREAKL, 0);
}
