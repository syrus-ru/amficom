#include <jni.h>
#include <math.h> // sqrt()

#include "Java-bridge.h"

#include "../Common/assert.h"

#include "ModelF.h"
#include "EventP.h"

#include "prf.h"

#include "../fit/ModelF-fit.h"
#include "../fit/BreakL-fit.h"

#include "../An2/findNoise.h"

#include "com_syrus_AMFICOM_analysis_dadara_ModelFunction.h"
#include "com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent.h"
#include "com_syrus_AMFICOM_analysis_CoreAnalysisManager.h"

const char *CL_mf		= "com/syrus/AMFICOM/analysis/dadara/ModelFunction";
const char *S_mf		= "Lcom/syrus/AMFICOM/analysis/dadara/ModelFunction;";
const char *N_mf		= "mf";
const char *S_shapeID	= "I";
const char *N_shapeID	= "shapeID";
const char *S_pars		= "[D";
const char *N_pars		= "pars";
const char *CL_ep	= "com/syrus/AMFICOM/analysis/dadara/ReflectogramEvent";
const char *S_ep	= "Lcom/syrus/AMFICOM/analysis/dadara/ReflectogramEvent;";

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
//   != 0  error;
// в случае ошибки JNI дает assertion fail
int ModelF_J2C(JNIEnv *env, jobject obj, ModelF &mf)
{
	if (obj == 0)
		return 1;
	jclass clazz = env->GetObjectClass(obj);
	assert (clazz);

	// инициализируем mf
	jdoubleArray array = (jdoubleArray )env->GetObjectField(obj, env->GetFieldID(clazz, "pars", "[D"));
	jsize sz = env->GetArrayLength(array);
	mf.init(
		env->GetIntField(obj, env->GetFieldID(clazz, N_shapeID, S_shapeID)),
		sz);
	if (!mf.isCorrect())
		return -1;

	// проверяем, что на входе дано ровно столько параметров, сколько надо
	if (sz != mf.getNPars())
		return 1;

	// копируем параметры mf
	double *pp = env->GetDoubleArrayElements(array, 0);
	int i;
	for (i = 0; i < sz; i++)
		mf[i] = pp[i];
	env->ReleaseDoubleArrayElements(array, pp, 0);

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

	// заполняем J объект из mf
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

	// заполняем ModelFunction.shapeID
	int shapeID = mf.getID();
	env->SetIntField(mf_obj, env->GetFieldID(mf_clazz, N_shapeID, S_shapeID), shapeID);

	// check for existing pars[]
	jfieldID pars_fid = env->GetFieldID(mf_clazz, N_pars, S_pars);
	jdoubleArray arr = (jdoubleArray )env->GetObjectField(mf_obj, pars_fid);

	// ЕСЛИ pars[] не существует ИЛИ его размер отличен от npars,
	// ТО создаем новый pars[]
	if (arr == 0 || env->GetArrayLength(arr) != npars)
	{
		// create new J array for pars, link it to ModelFunction (old J array pars[] will go to GC)
		arr = env->NewDoubleArray((jsize)npars);
		assert(arr);
		env->SetObjectField(mf_obj, env->GetFieldID(mf_clazz, N_pars, S_pars), arr);
	}

	// заполняем массив pars[]
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

	double ACXL[4] = { dA, dC, dX, dL };

	mf.execCmd(MF_CMD_ACXL_CHANGE, (void *)ACXL);

	ModelF_C2J_update(env, mf, obj);
	prf_e();
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
	ModelF mf;
	if (ModelF_J2C(env, obj, mf))
		assert(0);

	jdoubleArray arr = env->NewDoubleArray((jsize)length);
	assert(arr);

	double *pp = env->GetDoubleArrayElements(arr, 0);
	assert (pp);
	int i;
	for (i = 0; i < length; i++)
		pp[i] = mf.calcFun(x0 + i * step);
	env->ReleaseDoubleArrayElements(arr, pp, 0);

	return arr;
}

const int ERROR_MODE_DEFAULT = 0;
const int ERROR_MODE_UNINOISE = 2;
const int ERROR_MODE_VARNOISE = 3;

static void nFit
  (JNIEnv *env, jobject obj, jdoubleArray y, jint begin, jint end, jint fitMode,
  jint linkFlagsJ, jdouble linkData0,
  jint errorMode, jdouble error1, jdouble error2, jint maxpoints, jdoubleArray noise)
{
	prf_b("nFit - enter");
	ModelF mf;
	if (ModelF_J2C(env, obj, mf))
		assert(0);

	double *yy;
	get_arr_region(env, y, &yy, begin, end - begin + 1);

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
				// линейная фитировка - данные о погрешности не нужны
				BreakL_FitL(mf, yy, 0, begin, end - begin + 1, linkFlags, linkData);
			}
			else
			{
				// полная фитировка, проводим ломаную заново
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
							0, noiseData);
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
	  ERROR_MODE_DEFAULT, 0.0, 0.0, 0, 0);
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
	  ERROR_MODE_UNINOISE, errorR, errorA, maxPoints, 0);
}

/*
 * Class:     com_syrus_AMFICOM_analysis_dadara_ModelFunction
 * Method:    nFit3
 * Signature: ([DIIIID[D)V
 */
JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_analysis_dadara_ModelFunction_nFit3
  (JNIEnv *env, jobject obj, jdoubleArray y, jint begin, jint end, jint fitMode, jint linkFlagsJ, jdouble linkData0,
  jdoubleArray noiseArray)
{
  nFit(env, obj, y, begin, end, fitMode, linkFlagsJ, linkData0,
	  ERROR_MODE_VARNOISE, 0.0, 0.0, 0, noiseArray);
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

/*
 * calcNoise part
 */

JNIEXPORT jdouble JNICALL Java_com_syrus_AMFICOM_analysis_CoreAnalysisManager_nCalcNoise3s
  (JNIEnv *env, jclass cls, jdoubleArray arr)
{
	double *yy;
	int size  = get_arr(env, arr, &yy);

	double ret = findNoise3s(yy, size);

	release_arr(env, arr, yy);

	return ret;
}

JNIEXPORT jdoubleArray JNICALL Java_com_syrus_AMFICOM_analysis_CoreAnalysisManager_nCalcNoiseArray
  (JNIEnv *env, jclass cls, jdoubleArray inArr)
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
	jdoubleArray outArr = env->NewDoubleArray((jsize )size);
	assert(outArr);
	get_arr(env, outArr, &noise);

	// process
	findNoiseArray(yy, noise, size);

	// release arrays
	release_arr(env, inArr, yy);
	release_arr(env, outArr, noise);

	return outArr;
}
