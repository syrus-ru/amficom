#include "assert.h"
#include <jni.h>
#include <windows.h> // for APIENTRY #define
#include <math.h>

#include "dadara-analyse.h"
#include "../Common/com_syrus_AMFICOM_analysis_CoreAnalysisManager.h"
#include "../analyse/InitialAnalysis.h"

#include "../Common/EventParams.h"
#include "../Common/EventP.h"
#include "../Common/EPold2EPnew.h"

#include "../Common/Java-bridge.h"

#ifdef DEBUG_DADARA_ANALYSE
	#ifndef _WIN32
		#include <sys/time.h>
		#include <time.h>
	#endif
	FILE* dbg_stream;
#endif

JNIEXPORT jobjectArray JNICALL 
Java_com_syrus_AMFICOM_analysis_CoreAnalysisManager_analyse2(
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

#ifdef DEBUG_DADARA_ANALYSE

	#ifdef _WIN32
		dbg_stream = fopen(DEBUG_DADARA_WIN_LOGF, "a");
		if (dbg_stream == 0)
			dbg_stream = stderr;
		setvbuf(dbg_stream, 0, 0, _IONBF); // no buffering
		fprintf (dbg_stream, "# logfile opened in AnalysisManager_analyse\n");
	#else
		timeval tv;
		gettimeofday(&tv, NULL);
		tm* t = localtime(&tv.tv_sec);
		const int size = 64; // was: 9 + 6 + 1 + 14 + 1 + 3 + 1 ...
		char* filename = new char[size];
		sprintf(filename, ".//logs//%04d%02d%02d%02d%02d%02d-dadara-analyse.log", 1900 + t->tm_year, 1 + t->tm_mon, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
		filename[size - 1] = 0;
		dbg_stream = fopen(filename, "a");
		delete[] filename;
		if (dbg_stream == 0)
			dbg_stream = stderr;
	#endif

	fprintf (dbg_stream, "\t""waveletType %d\n", (int)waveletType);
	fprintf (dbg_stream, "\t""delta_x %g\n", (double)delta_x);
	fprintf (dbg_stream, "\t""connFallParams %g\n", (double)connFallParams);
	fprintf (dbg_stream, "\t""min_level %g\n", (double)min_level);
	fprintf (dbg_stream, "\t""max_level_noise %g\n", (double)max_level_noise);
	fprintf (dbg_stream, "\t""min_level_to_find_end %g\n", (double)min_level_to_find_end);
	fprintf (dbg_stream, "\t""min_weld %g\n", (double)min_weld);
	fprintf (dbg_stream, "\t""min_connector %g\n", (double)min_connector);
	//fprintf (dbg_stream, "\t""strategy %d\n", (int)strategy);
#endif

	// берем в JNI массив y-значений (его надо будет потом освободить)
	double* data    = (double*)env->GetDoubleArrayElements(y, NULL);
	jsize sz = env->GetArrayLength(y);
	int data_l = sz;

#ifdef DEBUG_DADARA_ANALYSE
	fprintf (dbg_stream, "\tsz %d\n", (int)sz);
	if (0)
	{
		int i;
		for (i=0; i<sz; i++) fprintf (dbg_stream, "%g ", (double)data[i]);
		fprintf (dbg_stream, "\n");
	}
#endif

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

#ifdef DEBUG_DADARA_ANALYSE
	fprintf(dbg_stream, "#0\n");
#endif

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
		max_level_noise,
		waveletType,
		connFallParams,
		reflectiveSize, 
		nonReflectiveSize);

#ifdef DEBUG_DADARA_ANALYSE
	fprintf(dbg_stream, "#1\n");
#endif

	EventParams **ep = ia->getEventParams();
	// No mem leakage 'cause ep data is owned by IA and will be deleted when deleting IA

#ifdef DEBUG_DADARA_ANALYSE
	fprintf(dbg_stream, "#2\n");
#endif


	int nEvents = ia->getEventsCount();

#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "ia->getEventsCount = %d\n", nEvents);
#endif

	// convert EventParams to EventP
	EventP *re = new EventP[nEvents];
	assert (re);
	int i;

#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "# re = %p\n", re);
#endif

	for (i = 0; i < nEvents; i++)
	{
		EPold2EPnew(ep[i], re[i], delta_x);

#ifdef DEBUG_DADARA_ANALYSE
		fprintf (dbg_stream, "event [%d]: begin %d end %d gentype %d mf.ID %d\n", i, re[i].begin, re[i].end, re[i].gentype, re[i].mf.getID());
		int k;
		for (k = 0; k < re[i].mf.getNPars(); k++)
			fprintf(dbg_stream, "\t""pars[%d]: %g\n", k, re[i].mf[k]);

		fprintf (dbg_stream, "\t""Y0 %g\n", re[i].mf.calcFun(re[i].begin));
		fprintf (dbg_stream, "\t""Y1 %g\n", re[i].mf.calcFun(re[i].end));
#endif
	}

#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "# converted to Cnew\n");
	jobjectArray ret_obj = EventP_C2J_arr(env, re, nEvents, dbg_stream);
	fprintf( dbg_stream, "# converted to Java, ret_obj = %p\n", ret_obj);
#else
	jobjectArray ret_obj = EventP_C2J_arr(env, re, nEvents, 0);
#endif

	delete[] re;

#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "# re freed\n");
#endif

	// send to JNI
	double meanAtt = ia->getMeanAttenuation();

	delete ia;

#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "returning to java %d events, meanAtt=%g \n", nEvents, meanAtt);
#endif

	jsize len = (env)->GetArrayLength(ret_meanAttenuation);

	if (len)
	{
		jdouble *body = (env)->GetDoubleArrayElements(ret_meanAttenuation, 0);
		body[0] = meanAtt;
		(env)->ReleaseDoubleArrayElements(ret_meanAttenuation, body, 0); // copy-back
	}

//Освобождение массивов в Java Native Interface
	(env)->ReleaseDoubleArrayElements(y,data,JNI_ABORT);

#ifdef DEBUG_DADARA_ANALYSE
	fprintf( dbg_stream, "# logfile closed\n");
	if (dbg_stream != stderr && dbg_stream != stdout)
		fclose( dbg_stream );
#endif

	return ret_obj;
}

