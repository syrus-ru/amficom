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


/*
 * Class:     com_syrus_AMFICOM_analysis_CoreAnalysisManager
 * Method:    analyse3
 * Signature: ([DDDDDDIII[D)[Lcom/syrus/AMFICOM/analysis/dadara/SimpleReflectogramEventImpl;
 */
JNIEXPORT jobjectArray JNICALL
Java_com_syrus_AMFICOM_analysis_CoreAnalysisManager_analyse3(
	JNIEnv* env,
	jclass obj,
	jdoubleArray y,
	jdouble delta_x,
	jdouble min_level,
	jdouble min_weld,
	jdouble min_connector,
	jdouble min_level_to_find_end,
	jint reflectiveSize,
	jint nonReflectiveSize,
	jint traceLength,
	jdoubleArray noiseObj)
{
	prf_b("analyse3() - enter");

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

	// корректируем параметры
	if (min_level < 0.01)
		min_level =  0.01;
	if (min_weld < 0.01)
		min_weld = 0.01;
	if (min_connector < 0.01)
		min_connector = 0.01;

	prf_b("analyse3() - starting IA");

	// FIXIT: InitialAnalysis changes input array.
	// The original analyse() code both changed input array and used JNI_ABORT, so the result is undeterminable.
	// If it was not expected to change anything in Java arrays, then it should make a local copy. -- FIXIT
	InitialAnalysis *ia = new InitialAnalysis(data, 
		data_l,
		delta_x, 
		min_level, 
		min_weld, 
		min_connector, 
		min_level_to_find_end,
		0,
		0,
		0,
		reflectiveSize, 
		nonReflectiveSize,
		traceLength,
		noiseData);

	prf_b("analyse3() - processing IA results");

	ArrList& ev = ia->getEvents();

	int nEvents = ev.getLength();

	// convert EventParams to SimpleEvent
	SimpleEvent *se = new SimpleEvent[nEvents];
	assert (se);
	int i;

	for (i = 0; i < nEvents; i++)
	{
		EPold2SE((EventParams*)ev[i], se[i]);
		//fprintf(stderr, "event %d type %d begin %d end %d\n", i, se[i].type, se[i].begin, se[i].end);
		//fflush(stderr);
		assert(se[i].begin >= 0);
		assert(se[i].end < sz);
		assert(se[i].end >= se[i].begin);
		assert(se[i].end > se[i].begin); // >, not just >=
		if (i)
		{
			assert(se[i].begin == se[i-1].end);
		}
	}

	prf_b("analyse3() - sending to JNI");
	jobjectArray ret_obj = SimpleEvent_C2J_arr(env, se, nEvents);

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
