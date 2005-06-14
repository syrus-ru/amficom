#include <jni.h>
#include <math.h> // sqrt()

#include "../common/com_syrus_AMFICOM_analysis_dadara_ModelFunction.h"
#include "../common/com_syrus_AMFICOM_analysis_dadara_SimpleReflectogramEvent.h"
#include "../common/com_syrus_AMFICOM_analysis_CoreAnalysisManager.h"
#include "../common/com_syrus_AMFICOM_analysis_dadara_Wavelet.h"

#include "Java-bridge.h"

#include "../common/assert1.h"

#include "../common/ModelF.h"
#include "../common/EventP.h"
#include "../common/SimpleEvent.h"

#include "../common/prf.h"

#include "names.h"
#include "ThreshArray.h"

#include "../common/median.h"

#include "../fit/ModelF-fit.h"
#include "../BreakL/BreakL-fit.h"
#include "../BreakL/BreakL-enh.h" // :-( for TTDX/TTDY

#include "../an2/findLength.h"
#include "../an2/findNoise.h"

#include "../thresh/thresh.h"
#include "../thresh/makeThresh.h"

#include "../common/MathRef.h"
#include "../wavelet/wavelet.h"

inline int imax(int a, int b) { return a > b ? a : b; }
inline int imin(int a, int b) { return a < b ? a : b; }

/*
 *
 * general JNI related util
 *
 */

static int get_arr(JNIEnv *env, jdoubleArray array, double **pp);
static void release_arr(JNIEnv* env, jdoubleArray array, double *pp);
static void get_arr_region(JNIEnv *env, jdoubleArray array, double **pp, int start, int len);
static void release_arr_region(JNIEnv *env, jdoubleArray array, double *pp);

int get_arr(JNIEnv *env, jdoubleArray array, double **pp) // open array double[]
{
	jsize sz = env->GetArrayLength(array);
	*pp = env->GetDoubleArrayElements(array, 0);
	assert(*pp);
	return sz;
}
void release_arr(JNIEnv* env, jdoubleArray array, double *pp) // close array double[]
{
	env->ReleaseDoubleArrayElements(array, pp, 0);
}

void get_arr_region(JNIEnv *env, jdoubleArray array, double **pp, int start, int len) // open array double[] region
{
	*pp = new double[len];
	assert(*pp);
	env->GetDoubleArrayRegion(array, start, len, *pp);
}
void release_arr_region(JNIEnv *env, jdoubleArray array, double *pp)
{
	delete[] pp;
}

/*
 *
 * ModelF part
 *
 */

// J ModelFunction -> C ModelF
// возвращает:
//   == 0  ok, 
//   <  0  inCorrect (or empty) mf;
//   >  0  some problem
// в случае ошибки JNI дает assertion fail
int ModelF_J2C(JNIEnv *env, jobject obj, ModelF &mf)
{
	if (obj == 0)
		return 1;
	jclass clazz = env->GetObjectClass(obj);
	assert (clazz);

	// инициализируем mf
	jdoubleArray array = (jdoubleArray )env->GetObjectField(obj, env->GetFieldID(clazz, "pars", "[D"));
	jsize sz = array ? env->GetArrayLength(array) : 0;
	mf.init(
		env->GetIntField(obj, env->GetFieldID(clazz, N_shapeID, S_shapeID)),
		sz);
	if (!mf.isCorrect())
		return -1;

	// провер€ем, что на входе дано ровно столько параметров, сколько надо
	if (sz != mf.getNPars())
		return 1;

	if (sz)
	{
		// копируем параметры mf
		double *pp = env->GetDoubleArrayElements(array, 0);
		int i;
		for (i = 0; i < sz; i++)
			mf[i] = pp[i];
		env->ReleaseDoubleArrayElements(array, pp, 0);
	}

	return 0;
}

// C ModelF -> J ModelFunction
// в случае ошибки дает assertion fail
jobject ModelF_C2J(JNIEnv *env, ModelF &mf)
{
	// создаем J объект ModelFunction
	jclass mf_clazz = env->FindClass(CL_mf);
	assert (mf_clazz);
	jmethodID mf_initID = env->GetMethodID(mf_clazz, "<init>", "()V");
	assert (mf_initID);
	jobject mf_obj = env->NewObject(mf_clazz, mf_initID);
	// there was the bug (mf_obj was 0) but it disappeared by itself
	assert (mf_obj);

	// заполн€ем J объект из mf
	ModelF_C2J_update(env, mf, mf_obj);

	return mf_obj;
}

// update: C ModelF -> existing JModelFunction
// в случае ошибки дает assertion fail
void ModelF_C2J_update(JNIEnv *env, ModelF &mf, jobject mf_obj)
{
	jclass mf_clazz = env->FindClass(CL_mf);
	assert (mf_clazz);

	int npars = mf.getNPars();

	// заполн€ем ModelFunction.shapeID
	int shapeID = mf.getID();
	env->SetIntField(mf_obj, env->GetFieldID(mf_clazz, N_shapeID, S_shapeID), shapeID);

	// check for existing pars[]
	jfieldID pars_fid = env->GetFieldID(mf_clazz, N_pars, S_pars);
	jdoubleArray arr = (jdoubleArray )env->GetObjectField(mf_obj, pars_fid);

	// ≈—Ћ» pars[] не существует »Ћ» его размер отличен от npars,
	// “ќ создаем новый pars[]
	if (arr == 0 || env->GetArrayLength(arr) != npars)
	{
		// create new J array for pars, link it to ModelFunction (old J array pars[] will go to GC)
		arr = env->NewDoubleArray((jsize)npars);
		assert(arr);
		env->SetObjectField(mf_obj, env->GetFieldID(mf_clazz, N_pars, S_pars), arr);
	}

	// заполн€ем массив pars[]
	double *pp = env->GetDoubleArrayElements(arr, 0);
	assert (pp);
	int i;
	for (i = 0; i < npars; i++)
		pp[i] = mf[i];
	env->ReleaseDoubleArrayElements(arr, pp, 0);
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nInitAsLinear
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nInitAsLinear
  (JNIEnv *env, jobject obj)
{
	ModelF mf(MF_ID_LIN);
	mf.zeroPars();

	ModelF_C2J_update(env, mf, obj);
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nSetAsLinear
 * Signature: (IDID)V
 */
JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nSetAsLinear
  (JNIEnv *env, jobject obj, jint x1, jdouble y1, jint x2, jdouble y2)
{
	ModelF mf(MF_ID_LIN);
	mf.zeroPars();

	double args[4] = { x1, y1, x2, y2 };

	mf.execCmd(MF_CMD_LIN_SET_BY_X1Y1X2Y2, args);

	ModelF_C2J_update(env, mf, obj);
}


/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nChangeByACXL
 * Signature: (DDDD)V
 */
JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nChangeByACXL
  (JNIEnv *env, jobject obj, jdouble dA, jdouble dC, jdouble dX, jdouble dL)
{
	prf_b("MF_nACXL");
	ModelF mf;
	if (ModelF_J2C(env, obj, mf))
		assert(0);

	ACXL_data ACXL = { dA, dC, dX, dL };

	mf.execCmd(MF_CMD_ACXL_CHANGE, (void *)&ACXL);

	ModelF_C2J_update(env, mf, obj);
	prf_e();
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nChangeByThresh
 * Signature: ([Lcom/syrus/AMFICOM/analysis/dadara/ThreshDX;[Lcom/syrus/AMFICOM/analysis/dadara/ThreshDY;I)V
 */
JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nChangeByThresh
  (JNIEnv *env, jobject obj, jobjectArray threshArrDX, jobjectArray threshArrDY, jint key)
{
	prf_b("nChangeByThresh");
	ModelF mf;
	if (ModelF_J2C(env, obj, mf))
		assert(0);
	ThreshDXArray taDX(env, threshArrDX);
	ThreshDYArray taDY(env, threshArrDY);
	int key_ = key;
	int xID = 0;
	int wannaID = 0;
	void *args[5] = { &taDX, &taDY, &key_, &xID, &wannaID};
	mf.execCmd(MF_CMD_CHANGE_BY_THRESH_AND_FIND_DXDYID, args);
	ModelF_C2J_update(env, mf, obj);
	prf_e();
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nFindResponsibleThreshDXDYID
 * Signature: ([Lcom/syrus/AMFICOM/analysis/dadara/ThreshDX;[Lcom/syrus/AMFICOM/analysis/dadara/ThreshDY;III)I
 */
JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nFindResponsibleThreshDXDYID
  (JNIEnv *env, jobject obj, jobjectArray threshArrDX, jobjectArray threshArrDY, jint key, jint x, jint xThType)
{
	prf_b("nFindResponsibleThreshDXID");
	ModelF mf;
	if (ModelF_J2C(env, obj, mf))
		assert(0);
	ThreshDXArray taDX(env, threshArrDX);
	ThreshDYArray taDY(env, threshArrDY);
	int key_ = key;
	int xID = x;
	int xType = xThType;
	void *args[5] = { &taDX, &taDY, &key_, &xID, &xType};
	int rcID = (int )mf.execCmd(MF_CMD_CHANGE_BY_THRESH_AND_FIND_DXDYID, args);
	// NB: do NOT update mf back to Java because mf is now changed but Java mf should not be modified
	prf_e();
	return rcID;
}


/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nFindResponsibleThreshDXArray
 * Signature: ([Lcom/syrus/AMFICOM/analysis/dadara/ThreshDX;[Lcom/syrus/AMFICOM/analysis/dadara/ThreshDY;III)[I
 */
JNIEXPORT jintArray JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nFindResponsibleThreshDXArray
  (JNIEnv *env, jobject obj, jobjectArray threshArrDX, jobjectArray threshArrDY, jint keyj, jint xMinj, jint xMaxj)
{
	prf_b("nFindResponsibleThreshDXArray");
	// получаем входные данные
	ModelF mf;
	if (ModelF_J2C(env, obj, mf))
		assert(0);
	ThreshDXArray taDX(env, threshArrDX);
	ThreshDYArray taDY(env, threshArrDY);
	int key = keyj;
	int xMin = xMinj;
	int xMax = xMaxj;
	int autoThresh = 0; // нормальный (полный) режим генерации порогов, неточное определение DY-порогов на сглаженных кра€х DX
	assert(xMax >= xMin);
	int N = xMax - xMin + 1;

	// создаем выходные массивы
	jintArray jaX  = env->NewIntArray((jsize)N);
	assert(jaX);

	// формируем временные массивы
	TTDX *ttdx = new TTDX[N];
	assert(ttdx);

	// выполн€ем преобразование
	void *args[8] = { &taDX, &taDY, &key, &autoThresh, &xMin, &xMax, ttdx, 0 };
	int rcID = (int )mf.execCmd(MF_CMD_CHANGE_BY_THRESH_AND_FIND_TTDXDY, args);
	assert(rcID); // 0 - команда не поддерживаетс€. FIXME: - в принципе, тут надо просто вернуть null в Java

	// определ€ем leftmost и rightmost пороги каждого участка
	jint *dX  = env->GetIntArrayElements(jaX,  0);

	int i;
	for (i = 0; i < N; i++)
	{
		dX[i] = ttdx[i].thId;
	}

	// освобождаем временные массивы и Java-объекты
	delete[] ttdx;
	env->ReleaseIntArrayElements(jaX,  dX,  0);

	// NB: do NOT update mf back to Java because mf is now changed but Java mf should not be modified
	prf_e();
	return jaX;
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nRMS
 * Signature: ([DII)D
 */
JNIEXPORT jdouble JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nRMS
  (JNIEnv *env, jobject obj, jdoubleArray y, jint begin, jint end)  // end point is included
{
	ModelF mf;
	if (ModelF_J2C(env, obj, mf))
		assert(0);

	double *yy;
	int size  = get_arr(env, y, &yy);
	assert (end + 1 <= size);

	double ret = sqrt(mf.RMS2(yy, begin, begin, end - begin + 1));

	release_arr(env,y,yy);

	//get_arr_region(env, y, &yy, begin, end - begin + 1);
	//release_arr_region(env,y,yy);

	return ret;
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nGetAttr
 * Signature: (Ljava/lang/String;D)D
 */
JNIEXPORT jdouble JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nGetAttr
  (JNIEnv *env, jobject obj, jstring name_j, jdouble default_value)
{
	ModelF mf;
	if (ModelF_J2C(env, obj, mf))
		assert(0);

	// XXX: non-7bit attr names are not supported due to UTF
	const char *name_c = env->GetStringUTFChars(name_j, 0); // 'char*'; MSVC disliked 'jbyte*' (?)
	assert (name_c);

	double ret = mf.getAttr(name_c, default_value);

	env->ReleaseStringUTFChars(name_j, name_c);

	return ret;
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nF
 * Signature: (D)D
 */
JNIEXPORT jdouble JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nF
  (JNIEnv *env, jobject obj, jdouble x)
{
	prf_b("nF");
	ModelF mf;
	if (ModelF_J2C(env, obj, mf))
		assert(0);

	double ret = mf.calcFun(x);
	prf_e();
	return ret;
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nFArray
 * Signature: (DDI)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nFArray
  (JNIEnv *env, jobject obj, jdouble x0, jdouble step, jint length)
{
	prf_b("nFArray");
	ModelF mf;
	if (ModelF_J2C(env, obj, mf))
		assert(0);

	assert(length >= 0);

	jdoubleArray arr = env->NewDoubleArray((jsize)length);
	assert(arr);

	if (length > 0)
	{
		double *pp = env->GetDoubleArrayElements(arr, 0);
		assert (pp);

		mf.calcFunArray(x0, step, length, pp);

		env->ReleaseDoubleArrayElements(arr, pp, 0);
	}

	prf_e();
	return arr;
}

const int ERROR_MODE_DEFAULT = 0;
const int ERROR_MODE_UNINOISE = 2;
const int ERROR_MODE_VARNOISE = 3;

static void nFit
  (JNIEnv *env, jobject obj, jdoubleArray y, jint begin, jint end, jint fitMode,
  jint linkFlagsJ, jdouble linkData0,
  jint errorMode, jdouble error1, jdouble error2, jint maxpoints, jdoubleArray noise, jintArray xStop)
{
	prf_b("nFit - enter");
	ModelF mf;
	if (ModelF_J2C(env, obj, mf) > 0)
		assert(0);

	double *yy;
	get_arr_region(env, y, &yy, begin, end - begin + 1);

	int xStopLen = xStop ? env->GetArrayLength(xStop) : 0;
	int *xStopArr = 0;
	if (xStopLen) {
		xStopArr = new int[xStopLen];
		assert(xStopArr);
		jint *xStopArrJ = env->GetIntArrayElements(xStop, 0);
		int i;
		for (i = 0; i < xStopLen; i++)
			xStopArr[i] = xStopArrJ[i];
		env->ReleaseIntArrayElements(xStop, xStopArrJ, 0);
	}

	fit_stat_res stat;

	assert(BREAKL_LINK_FIXLEFT == com_syrus_AMFICOM_analysis_dadara_ModelFunction_LINK_FIXLEFT);
	int linkFlags = linkFlagsJ; // вообще-то, здесь неплохо бы сделать преобразование
	double linkData[1] = { linkData0 };

	int linear = 0;

	switch(fitMode)
	{
	case com_syrus_AMFICOM_analysis_dadara_ModelFunction_FITMODE_SET_LINEAR:
		mf.init(MF_ID_LIN);
		break;

	case com_syrus_AMFICOM_analysis_dadara_ModelFunction_FITMODE_SET_BREAKL:
		mf.init(MF_ID_BREAKL);
		break;

	case com_syrus_AMFICOM_analysis_dadara_ModelFunction_FITMODE_VARY_ALL:
		break;

	case com_syrus_AMFICOM_analysis_dadara_ModelFunction_FITMODE_VARY_LIN:
		linear = 1;
		break;

	default:
		assert(0);
	}

	assert(mf.isCorrect()); // FITMODE_VARY_* режимы недопустимы при фитировке пустого mf

	if (mf.hasFixedNumberOfPars())
	{
		// фиксированное число параметров - данные о погрешности не нужны
		prf_b("nFit: fixed npars");
		//fprintf(stderr, "nfit: fixed npars: fitMode %d ID %d\n", fitMode, mf.getID()); // FIXIT
		if (linear)
			fit_linear_only(mf, yy, 0, begin, end - begin + 1);
		else
			fit(mf, yy, 0, begin, end - begin + 1, stat);
	}
	else
	{
		prf_b("nFit: var npars");
		if (mf.getID() == MF_ID_BREAKL)
		{
			if (linear)
			{
				// линейна€ фитировка - данные о погрешности не нужны
				BreakL_FitL(mf, yy, 0, begin, end - begin + 1, linkFlags, linkData);
			}
			else
			{
				// полна€ фитировка, проводим ломаную заново
				// используем данные о требуемой точности
				switch(errorMode)
				{
				case ERROR_MODE_DEFAULT:
					error1 = 0;
					error2 = -999;
					maxpoints = 0;
					// fall through
				case ERROR_MODE_UNINOISE:
					BreakL_Fit(mf, yy, 0, begin, end - begin + 1, linkFlags, linkData,
						0, error1, error2, maxpoints);
					break;
				case ERROR_MODE_VARNOISE:
					{
						double *noiseData;
						get_arr_region(env, noise, &noiseData, begin, end - begin + 1);
						BreakL_Fit2(mf, yy, 0, begin, end - begin + 1, linkFlags, linkData,
							0, noiseData, xStopArr, xStopLen);
						release_arr_region(env, noise, noiseData);
					}
					break;
				default:
					assert(0);
				}
			}
		}
		else
			fprintf(stderr, "Don't know how to fit, ID = %d\n", (int )mf.getID());
	}
	prf_b("nFit: cleanup");

	release_arr_region(env,y,yy);
	if (xStopArr)
		delete[] xStopArr;

	ModelF_C2J_update(env, mf, obj);
	prf_e();
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nFit1
 * Signature: ([DIIIID)V
 */
JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nFit1
  (JNIEnv *env, jobject obj, jdoubleArray y, jint begin, jint end, jint fitMode, jint linkFlagsJ, jdouble linkData0)
{
  nFit(env, obj, y, begin, end, fitMode, linkFlagsJ, linkData0,
	  ERROR_MODE_DEFAULT, 0.0, 0.0, 0, 0, 0);
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nFit2
 * Signature: ([DIIIIDDDI)V
 */
JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nFit2
  (JNIEnv *env, jobject obj, jdoubleArray y, jint begin, jint end, jint fitMode, jint linkFlagsJ, jdouble linkData0,
  jdouble errorR, jdouble errorA, jint maxPoints)
{
  nFit(env, obj, y, begin, end, fitMode, linkFlagsJ, linkData0,
	  ERROR_MODE_UNINOISE, errorR, errorA, maxPoints, 0, 0);
}


/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nFit4
 * Signature: ([DIIIID[D[I)V
 */
JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nFit4
  (JNIEnv *env, jobject obj, jdoubleArray y, jint begin, jint end, jint fitMode, jint linkFlagsJ, jdouble linkData0,
  jdoubleArray noiseArray, jintArray xStops)
{
  nFit(env, obj, y, begin, end, fitMode, linkFlagsJ, linkData0,
	  ERROR_MODE_VARNOISE, 0.0, 0.0, 0, noiseArray, xStops);
}


/*
 *
 * EventP part
 *
 */

/*
// converts ReflectogramEvent to EventP
// returns 0 if success, then out, out.mf and out.mf.entry are all valid
int EventP_J2C(JNIEnv *env, jobject obj, EventP &out)
{
	// copy ReflectogramEvent fields
	assert (obj);
	jclass clazz = env->GetObjectClass(obj);
	assert (clazz);
	out.gentype	= env->GetIntField(obj, env->GetFieldID(clazz, "type", "I"));
	out.begin	= env->GetIntField(obj, env->GetFieldID(clazz, "begin", "I"));
	out.end		= env->GetIntField(obj, env->GetFieldID(clazz, "end", "I"));
	out.delta_x	= env->GetDoubleField(obj, env->GetFieldID(clazz, "deltaX", "D"));

	// copy ModelFunction fields
	obj = env->GetObjectField(obj, env->GetFieldID(clazz, N_mf, S_mf));
	assert (obj);

    return ModelF_J2C(env, obj, out.mf));
}
*/

// creates Java object ReflectogramEvent from C structure EventP
// assertion fails if not success
jobject EventP_C2J(JNIEnv *env, EventP &ep)
{
	// create EventP object
	jclass ep_clazz = env->FindClass(CL_ep);
	assert (ep_clazz);
	jmethodID ep_initID = env->GetMethodID(ep_clazz, "<init>", "()V");
	assert (ep_initID);
	jobject ep_obj = env->NewObject(ep_clazz, ep_initID);
	assert (ep_obj);

	// fill primitive fields of EventP
	env->SetIntField(ep_obj, env->GetFieldID(ep_clazz, "eventType", "I"), ep.gentype);
	env->SetIntField(ep_obj, env->GetFieldID(ep_clazz, "thresholdType", "I"), ep.gentype);
	env->SetIntField(ep_obj, env->GetFieldID(ep_clazz, "begin", "I"), ep.begin);
	env->SetIntField(ep_obj, env->GetFieldID(ep_clazz, "end", "I"), ep.end);
	env->SetDoubleField(ep_obj, env->GetFieldID(ep_clazz, "deltaX", "D"), ep.delta_x);

	// create and fill ModelFunction object
	jobject mf_obj = ModelF_C2J(env, ep.mf);
	// add ModelFunction object to EventP
	env->SetObjectField(ep_obj, env->GetFieldID(ep_clazz, N_mf, S_mf), mf_obj);

	// return resulting EventP object
	return ep_obj;
}

jobjectArray EventP_C2J_arr(JNIEnv *env, EventP *ep, int number, FILE *logf)
{
	assert(number >= 0);

	  if (logf) fprintf(logf,"EventP_C2J_arr: enter; ep %p number of events = %d\n", ep, number);

	// create template EventP object
	jclass clazz = env->FindClass(CL_ep);
	jobjectArray oa = env->NewObjectArray(number, clazz, 0);
	assert (oa);

	// set objects
	int i;
	for (i = 0; i < number; i++)
	{
		//fprintf(stderr, "EventP_C2J_arr: i %d, ep + i %p; ep[i].mf.entry %d\n", i, ep + i, ep[i].mf.entry);
		jobject obj = EventP_C2J(env, ep[i]);
		assert (obj);
		env->SetObjectArrayElement(oa, i, obj);
	}

	  if (logf) fprintf(logf,"EventP_C2J_arr: exit\n");
	return oa;
}

/*
// builds C EventP[] array from Java ReflectogramEvent[] array
// returns null if array is empty
// assertion fails if some problem
// please remember to delete[] the resulting array
EventP *EventP_J2Cnew_arr(JNIEnv *env, jobjectArray array)
{
	jsize length = env->GetArrayLength(array);
	if (length == 0)
		return 0;
	EventP *ep = new EventP[length];
	assert (ep);
	int i;
	for (i = 0; i < length; i++)
	{
		jobject obj = env->GetObjectArrayElement(array, i);
		assert(obj);
		int rc = EventP_J2C(env, obj, ep[i]);
		assert(rc == 0);
	}
	return ep;
}
*/

jobject createObject(JNIEnv *env, jclass clazz)
{
	assert (clazz);
	jmethodID initID = env->GetMethodID(clazz, "<init>", "()V");
	assert (initID);
	jobject obj = env->NewObject(clazz, initID);
	assert (obj);
	return obj;
}

/*
 * SimpleEvent part
 */
jobject SimpleEvent_C2J(JNIEnv *env, SimpleEvent &se, jclass clazz) // clazz is a desired target class name
{
	// create object
	jobject obj = createObject(env, clazz);

	// fill fields
	env->SetIntField(obj, env->GetFieldID(clazz, N_SE_type, S_SE_type), se.type);
	env->SetIntField(obj, env->GetFieldID(clazz, N_SE_begin, S_SE_begin), se.begin);
	env->SetIntField(obj, env->GetFieldID(clazz, N_SE_end, S_SE_end), se.end);

	return obj;
}

jobjectArray SimpleEvent_C2J_arr(JNIEnv *env, SimpleEvent *se, int number)
{
	assert(number >= 0);

	// create SimpleEvent array
	jclass clazz = env->FindClass(CL_se);
	assert(clazz);
	jobjectArray oa = env->NewObjectArray(number, clazz, 0);
	assert (oa);

	// set objects
	int i;
	for (i = 0; i < number; i++)
	{
		jobject obj = SimpleEvent_C2J(env, se[i], clazz);
		assert (obj);
		env->SetObjectArrayElement(oa, i, obj);
	}

	return oa;
}

/*
 * ReliabilityEvent part
 */

// C ReliabilityEvent -> J ReliabilitySimpleReflectogramEventImpl
// assertion fails if not success
jobject ReliabilityEvent_C2J(JNIEnv *env, ReliabilityEvent &re, jclass clazz)
{
	// create object and fill SimpleReflectogramEventImpl fields
	jobject obj = SimpleEvent_C2J(env, re, clazz);

	// fill ReliabilitySimpleReflectogramEventImpl fields
	env->SetDoubleField(obj, env->GetFieldID(clazz, N_RE_reliability, S_RE_reliability), re.reliability);

	return obj;
}

// C ReliabilityEvent[] -> J ReliabilitySimpleReflectogramEventImpl[]
// assertion fails if not success
jobjectArray ReliabilityEvent_C2J_arr(JNIEnv *env, ReliabilityEvent *re, int number)
{
	assert(number >= 0);

	// create ReliabilityEvent array
	jclass clazz = env->FindClass(CL_re);
	assert(clazz);
	jobjectArray oa = env->NewObjectArray(number, clazz, 0);
	assert (oa);

	// set objects
	int i;
	for (i = 0; i < number; i++)
	{
		jobject obj = ReliabilityEvent_C2J(env, re[i], clazz);
		assert (obj);
		env->SetObjectArrayElement(oa, i, obj);
	}

	return oa;
}

JNIEXPORT jdoubleArray JNICALL Java_com_syrus_AMFICOM_analysis_CoreAnalysisManager_nCalcNoiseArray
  (JNIEnv *env, jclass cls, jdoubleArray inArr, jint length)
{
	// get input J array, create output J array, get output J array
	double *yy;
	double *noise;
	int size = get_arr(env, inArr, &yy);
	if (size == 0)
	{
		release_arr(env, inArr, yy);
		return 0;
	}
	prf_b("JNI: nCalcNoiseArray");
	jdoubleArray outArr = env->NewDoubleArray((jsize )size);
	assert(outArr);
	get_arr(env, outArr, &noise);

	if (length <= 0 || length > size)
		length = size;

	// process
	findNoiseArray(yy, noise, size, length);

	// release arrays
	release_arr(env, inArr, yy);
	release_arr(env, outArr, noise);
	prf_e();

	return outArr;
}

JNIEXPORT jdoubleArray JNICALL
Java_com_syrus_AMFICOM_analysis_CoreAnalysisManager_gauss(
     JNIEnv* env,
	 jclass,
	 jdoubleArray y,
	 jdouble center,
	 jdouble amplitude,
	 jdouble sigma)
{
	prf_b("JNI: gauss");
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
	prf_e();

	return ret;
}

/*
 * Class:     com_syrus_AMFICOM_analysis_CoreAnalysisManager
 * Method:    nMedian
 * Signature: ([DI)D
 */
JNIEXPORT jdouble JNICALL Java_com_syrus_AMFICOM_analysis_CoreAnalysisManager_nMedian
  (JNIEnv *env, jclass cls, jdoubleArray arr, jint pos)
{
	jsize nsam = env->GetArrayLength(arr);
	if (nsam == 0)
		return 0;

	prf_b("JNI: nMedian");
	jdouble *gist = new jdouble[nsam];
	assert(gist);

	env->GetDoubleArrayRegion(arr, 0, nsam, gist);

	// we assume binary compatibility of double and jdouble
	double ret = destroyAndGetMedian(gist, nsam, pos);
	delete[] gist;
	prf_e();

	return ret;
}

/*
 * Class:     com_syrus_AMFICOM_analysis_CoreAnalysisManager
 * Method:    nCalcTraceLength
 * Signature: ([D)I
 */
JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_analysis_CoreAnalysisManager_nCalcTraceLength
  (JNIEnv *env, jclass cls, jdoubleArray y)
{
	prf_b("JNI: nCalcTraceLength");
	// get J array
	double *yy;
	int size = get_arr(env, y, &yy);

	// process
	int ret = findReflectogramLength(yy, size);

	// release J array
	release_arr(env, y, yy);
	prf_e();

	return ret;
}

/*
 * Class:     com_syrus_AMFICOM_analysis_CoreAnalysisManager
 * Method:    nExtendThreshToCoverCurve
 * Signature: ([D[D[Lcom/syrus/AMFICOM/analysis/dadara/ThreshDX;[Lcom/syrus/AMFICOM/analysis/dadara/ThreshDY;IID)V
 */
JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_analysis_CoreAnalysisManager_nExtendThreshToCoverCurve
  (JNIEnv *env, jclass cls, jdoubleArray jdayBase, jdoubleArray jdayCover,
  jobjectArray thDX, jobjectArray thDY, jint keySoft, jint keyHard,
  jdouble dyFactor)
{
	prf_b("JNI: nExtendThreshToCoverCurve - enter");
	prf_b("JNI: nExtendThreshToCoverCurve - get from java");

	// get arrays
	double *yBase;
	int baseSize = get_arr(env, jdayBase, &yBase);
	double *yCover;
	int coverSize = get_arr(env, jdayCover, &yCover);

	// get thresholds
	ThreshDXArray THDXA(env, thDX);
	ThreshDYArray THDYA(env, thDY);

	prf_b("JNI: nExtendThreshToCoverCurve - convert soft thresholds");

	// convert thresholds
	THX *thx;
	THY *thy;
	int thxSize;
	int thySize;
	ThreshDXArrayToTHXArray(THDXA, keySoft, &thx, &thxSize);
	ThreshDYArrayToTHYArray(THDYA, keySoft, &thy, &thySize);

	prf_b("JNI: nExtendThreshToCoverCurve - process soft thresholds");

	int xMin = 0;
	int xMax = imin(baseSize, coverSize) - 1;

	extendThreshToCover(thx, thy, thxSize, thySize, THDXA.isUpper(keySoft),
		yBase, xMin, xMax, yCover, dyFactor);

	prf_b("JNI: nExtendThreshToCoverCurve - convert soft thresholds back");
	ThreshDXUpdateFromTHXArray(THDXA, keySoft, thx);
	ThreshDYUpdateFromTHYArray(THDYA, keySoft, thy);

	prf_b("JNI: nExtendThreshToCoverCurve - make hard thresholds");
	{
		THX *thxh;
		THY *thyh;
		// мы точно знаем, что число порогов не зависит от key, т.ч. th(x,y)hSize не нужен
		ThreshDXArrayToTHXArray(THDXA, keyHard, &thxh, &thxSize);
		ThreshDYArrayToTHYArray(THDYA, keyHard, &thyh, &thySize);
		int i;
		double ratioX = 1.5;
		double ratioY = 1.5;
		for (i = 0; i < thxSize; i++)
		{
			// we should round away from zero, not away from -inf
			int lSign = thxh[i].dxL >= 0 ? 1 : -1;
			int rSign = thxh[i].dxR >= 0 ? 1 : -1;
			thxh[i].dxL = lSign * (int )ceil(thx[i].dxL * lSign * ratioX);
			thxh[i].dxR = rSign * (int )ceil(thx[i].dxR * rSign * ratioX);
		}
		for (i = 0; i < thySize; i++)
		{
			thyh[i].dy = thy[i].dy * ratioY;
		}
		ThreshDXUpdateFromTHXArray(THDXA, keyHard, thxh);
		ThreshDYUpdateFromTHYArray(THDYA, keyHard, thyh);
	}

	prf_b("JNI: nExtendThreshToCoverCurve - release data");

	// release data
	if (thx != 0) delete[] thx;
	if (thy != 0) delete[] thy;
	release_arr(env, jdayBase, yBase);
	release_arr(env, jdayCover, yCover);

	prf_e();
}

static Wavelet *createWavelet(int type)
{
	switch(type)
	{
	case com_syrus_AMFICOM_analysis_dadara_Wavelet_TYPE_SINX:
		return new SineWavelet();
	case com_syrus_AMFICOM_analysis_dadara_Wavelet_TYPE_ABSXSINX:
		return new UserWavelet(2, wLet_SINXABSX);
	default:
		assert(0);
		return new HaarWavelet(); // as default, use HaarWavelet
	}
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_Wavelet
 * Method:    nMakeTransform
 * Signature: (II[DIID)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_syrus_AMFICOM_analysis_dadara_Wavelet_nMakeTransform
  (JNIEnv *env, jclass cls, jint type, jint scale, jdoubleArray input, jint iFrom, jint iTo, jdouble norm)
{
	prf_b("nMakeTransform");

	int len = iTo - iFrom + 1;

	double *yIn;
	int inSize = get_arr(env, input, &yIn);

	jdoubleArray output = (env)->NewDoubleArray(len);
	assert(output);
	double *wOut;
	get_arr(env, output, &wOut);

	Wavelet *wavelet = createWavelet(type);
	wavelet->transform(scale, yIn, inSize, iFrom, iTo, wOut, norm);
	delete wavelet;

	release_arr(env, input, yIn);
	release_arr(env, output, wOut);

	prf_e();
	return output;
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_Wavelet
 * Method:    nGetNormStep
 * Signature: (II)D
 */
JNIEXPORT jdouble JNICALL Java_com_syrus_AMFICOM_analysis_dadara_Wavelet_nGetNormStep
  (JNIEnv *env, jclass cls, jint type, jint scale)
{
	prf_b("nGetNormStep");
	Wavelet *wavelet = createWavelet(type);
	double ret = wavelet->normStep(scale);
	delete wavelet;
	prf_e();
	fprintf(stderr, "nGetNormStep: type %d scale %d ret %g\n", (int )type, (int )scale, ret);
	fflush(stderr);
	return ret;
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_Wavelet
 * Method:    nGetNormMx
 * Signature: (II)D
 */
JNIEXPORT jdouble JNICALL Java_com_syrus_AMFICOM_analysis_dadara_Wavelet_nGetNormMx
  (JNIEnv *env, jclass cls, jint type, jint scale)
{
	prf_b("nGetNormMx");
	Wavelet *wavelet = createWavelet(type);
	double ret = wavelet->normMx(scale);
	delete wavelet;
	prf_e();
	fprintf(stderr, "nGetNormMx: type %d scale %d ret %g\n", (int )type, (int )scale, ret);
	fflush(stderr);
	return ret;
}
