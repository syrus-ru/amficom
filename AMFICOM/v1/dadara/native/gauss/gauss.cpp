#include <math.h>
#include "../Common/ModelF.h"
#include "../fit/ModelF-fit.h"
#include "../Common/com_syrus_AMFICOM_analysis_CoreAnalysisManager.h"

//#include <stdio.h>

JNIEXPORT jdoubleArray JNICALL
Java_com_syrus_AMFICOM_analysis_CoreAnalysisManager_gauss(
     JNIEnv* env,
	 jclass,
	 jdoubleArray y,
	 jdouble center,
	 jdouble amplitude,
	 jdouble sigma)
{
	jdouble* data = (env)->GetDoubleArrayElements(y,NULL);
	jsize sz = (env)->GetArrayLength(y);
	int data_l = sz;

	ModelF mf(MF_ID_GAUSS);

	mf.getP()[MF_PARID_GAUSS_AMPLITUDE] = 1.0;
	mf.getP()[MF_PARID_GAUSS_CENTER] = data_l / 2.0;
	mf.getP()[MF_PARID_GAUSS_SIGMA] = data_l / 2.0;

	fit_stat_res stat;
	fit(mf, data, 0, 0, data_l, stat);

	//fprintf(stderr, "stat: RMS_worst/RMS_avg = %g\n", stat.rms_worst / stat.rms_avg);

	jdoubleArray ret = (env)->NewDoubleArray(3);

	double RET[3];
	RET[0] = mf.getP()[MF_PARID_GAUSS_CENTER];
	RET[1] = mf.getP()[MF_PARID_GAUSS_AMPLITUDE];
	RET[2] = mf.getP()[MF_PARID_GAUSS_SIGMA] * sqrt(2.0);

	//fprintf(stderr, "gauss: data_l %d center %g sigma %g ampl %g\n",
	//	data_l, RET[0], RET[2], RET[1]);
	//fflush(stderr);

	(env)->ReleaseDoubleArrayElements(y,data,JNI_ABORT);
	(env)->SetDoubleArrayRegion(ret,0,3,RET);

	return ret;
}
