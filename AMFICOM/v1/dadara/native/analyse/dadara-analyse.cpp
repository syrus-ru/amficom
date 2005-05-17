#include <assert.h>
#include <jni.h>
#include <math.h>

#include "dadara-analyse.h"
#include "../common/com_syrus_AMFICOM_analysis_CoreAnalysisManager.h"
#include "../analyse/InitialAnalysis.h"

#include "../common/EventParams.h"
#include "../common/EventP.h"
#include "../common/EPold2EPnew.h"

#include "../JNI/Java-bridge.h"

#include "../common/prf.h"

#ifdef DEBUG_DADARA_ANALYSE
	#ifndef _WIN32
		#include <sys/time.h>
		#include <time.h>
	#endif
	FILE* dbg_stream;
#endif

//#define ANALYSE_SIMPLE_DEBUG

/*
 * Class:     com_syrus_AMFICOM_analysis_CoreAnalysisManager
 * Method:    analyse5
 * Signature: ([DDDDDDDIII[D)[Lcom/syrus/AMFICOM/analysis/dadara/ReliabilitySimpleReflectogramEventImpl;
 */
JNIEXPORT jobjectArray JNICALL
Java_com_syrus_AMFICOM_analysis_CoreAnalysisManager_analyse5(
	JNIEnv* env,
	jclass obj,
	jdoubleArray y,
	jdouble delta_x,
	jdouble min_level,
	jdouble min_weld,
	jdouble min_connector,
	jdouble min_end,
	jdouble noiseFactor,
	jint reflectiveSize,
	jint nonReflectiveSize,
	jint traceLength,
	jdoubleArray noiseObj)
{
	prf_b("analyse() - enter");

	// берем в JNI массив y-значений (его надо будет потом освободить)
	double* data    = (double*)env->GetDoubleArrayElements(y, NULL);
	jsize sz = env->GetArrayLength(y);
	int data_l = sz;

	if (traceLength > sz)
		traceLength = sz;
	if (traceLength < 0)
		traceLength = 0;

	// берем в JNI массив noise, если он задан
	double* noiseData = noiseObj ? (double*)env->GetDoubleArrayElements(noiseObj, NULL)
		: 0;
	jsize noiseL = noiseObj ? env->GetArrayLength(noiseObj) : 0;

	if (noiseL < traceLength)
		noiseL = 0;

	//double noiseFactor = THRESHOLD_TO_NOISE_RATIO; // was 2

	// корректируем параметры. FIXME: не корректировать, а проверить валидатором
	if (min_level < 0.01)
		min_level =  0.01;
	if (min_weld < min_level)
		min_weld = min_level;
	if (min_connector < min_weld)
		min_connector = min_weld;
	if (min_end < min_connector)
		min_end = min_connector;

	prf_b("analyse() - starting IA");

	// FIXIT: InitialAnalysis changes input array.
	// The original analyse() code both changed input array and used JNI_ABORT, so the result is undeterminable.
	// If it was not expected to change anything in Java arrays, then it should make a local copy. -- FIXIT
	InitialAnalysis *ia = new InitialAnalysis(
    	data,
		data_l,
		delta_x,
		min_level,
		min_weld,
		min_connector,
		min_end,
		noiseFactor,
		nonReflectiveSize,
		0, // @todo: implement
		reflectiveSize,
		reflectiveSize,
		traceLength,
		noiseData);

	prf_b("analyse() - processing IA results");

	ArrList& ev = ia->getEvents();

	int nEvents = ev.getLength();

	// convert EventParams to ReliabilityEvent
	ReliabilityEvent *se = new ReliabilityEvent[nEvents];
	assert (se);
	int i;

	for (i = 0; i < nEvents; i++)
	{
		EventParams &ep = *(EventParams*)ev[i];
#ifdef ANALYSE_SIMPLE_DEBUG
		fprintf(stderr, "EventParams[%2d] type %2d begin %4d end %4d",
			i, ep.type, ep.begin, ep.end);
		if (ep.type == EventParams::GAIN || ep.type == EventParams::LOSS)
		{
			fprintf(stderr, " -- R %g", ep.R);
		}
		if (ep.type == EventParams::REFLECTIVE)
		{
			fprintf(stderr, " --            R1 %.1f R2 %.1f R3 %.1f",
				ep.R1, ep.R2, ep.R3);
		}
		if (ep.type == EventParams::DEADZONE)
		{
			fprintf(stderr, " --   R2 %.1f",
				ep.R2);
		}
		fprintf(stderr, "\n");
#endif
		EPold2RE(&ep, se[i]);
		assert(se[i].begin >= 0);
		assert(se[i].end < sz);
		assert(se[i].end >= se[i].begin);
		assert(se[i].end > se[i].begin); // >, not just >=
		if (i)
		{
			assert(se[i].begin == se[i-1].end);
		}
	}

#ifdef ANALYSE_SIMPLE_DEBUG
	fflush(stderr);
#endif

	prf_b("analyse() - sending to JNI");
	jobjectArray ret_obj = ReliabilityEvent_C2J_arr(env, se, nEvents);

	delete[] se;
	delete ia;

	// send to JNI

//Освобождение массивов в Java Native Interface
	(env)->ReleaseDoubleArrayElements(y,data,JNI_ABORT);
	if (noiseObj)
		(env)->ReleaseDoubleArrayElements(noiseObj, noiseData,JNI_ABORT);

	prf_e();

	fflush(stderr);

	return ret_obj;
}
