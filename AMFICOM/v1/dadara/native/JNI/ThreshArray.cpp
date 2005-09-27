#include <assert.h>
#include "../common/com_syrus_AMFICOM_analysis_dadara_ThreshDX.h"

#include "ThreshArray.h"
#include "names.h"

// Java object accessors

ThreshArray::ThreshArray(JNIEnv *env, jobjectArray array)
{
	this->env = env;
	this->array = array;
	cur_id = -1;
	cur_obj = 0;
	arraySize = env->GetArrayLength(array);

	jclass clazz = env->FindClass(CL_Thresh);
	assert(clazz);

	id_x0     = env->GetFieldID(clazz, N_Thresh_x0, S_Thresh_x0);
	id_x1     = env->GetFieldID(clazz, N_Thresh_x1, S_Thresh_x1);

	{
		jfieldID id_UPPER = env->GetStaticFieldID(clazz, N_Thresh_IS_KEY_UPPER, S_Thresh_IS_KEY_UPPER);
		jbooleanArray ua = (jbooleanArray )env->GetStaticObjectField(clazz, id_UPPER);
		assert(ua); // ������ ������ ���� ��� ������������������ � Java
		int key;
		for (key = 0; key < 4; key++)
		{
			jboolean data = JNI_FALSE;
			env->GetBooleanArrayRegion(ua, key, 1, &data);
			UPPER[key] = data == JNI_TRUE;
		}
	}

	{
		jfieldID id_CONJ = env->GetStaticFieldID(clazz, N_Thresh_CONJ_KEY, S_Thresh_CONJ_KEY);
		jintArray ca = (jintArray )env->GetStaticObjectField(clazz, id_CONJ);
		assert(ca);
		int key;
		for (key = 0; key < 4; key++)
		{
			jint data = 0;
			env->GetIntArrayRegion(ca, key, 1, &data);
			CONJ[key] = data;
		}
	}

	{
		const char *names[] = { N_Thresh_SOFT_UP, N_Thresh_HARD_UP, N_Thresh_SOFT_DOWN, N_Thresh_HARD_DOWN };
		const char *signs[] = { S_Thresh_SOFT_UP, S_Thresh_HARD_UP, S_Thresh_SOFT_DOWN, S_Thresh_HARD_DOWN };
		int i;
		for (i = 0; i < 4; i++)
		{
			jfieldID fid = env->GetStaticFieldID(clazz, names[i], signs[i]);
			keys[i] = env->GetStaticIntField(clazz, fid);
		}
	}

	roundUp = env->GetMethodID(clazz, N_Thresh_roundUp, S_Thresh_roundUp);
	assert(roundUp);
}

ThreshArray::~ThreshArray()
{
	free_cur();
}


int ThreshArray::isUpper(int key)
{
	return UPPER[key];
}

int ThreshArray::getConjKey(int key)
{
	return CONJ[key];
}

int ThreshArray::getKeySoftUp()
{
	return keys[0];
}
int ThreshArray::getKeyHardUp()
{
	return keys[1];
}
int ThreshArray::getKeySoftDown()
{
	return keys[2];
}
int ThreshArray::getKeyHardDown()
{
	return keys[3];
}

ThreshDYArray::ThreshDYArray(JNIEnv *env, jobjectArray array)
: ThreshArray(env, array)
{
	jclass clazz = env->FindClass(CL_ThreshDY);
	assert(clazz);
	id_typeL  = env->GetFieldID(clazz, N_ThreshDY_typeL, S_ThreshDY_typeL);
	id_values = env->GetFieldID(clazz, N_ThreshDY_values, S_ThreshDY_values);
}

ThreshDXArray::ThreshDXArray(JNIEnv *env, jobjectArray array)
: ThreshArray(env, array)
{
	jclass clazz = env->FindClass(CL_ThreshDX);
	assert(clazz);
	id_dX     = env->GetFieldID(clazz, N_ThreshDX_dX, S_ThreshDX_dX);
	id_isRise = env->GetFieldID(clazz, N_ThreshDX_isRise, S_ThreshDX_isRise);
	id_flags  = env->GetFieldID(clazz, N_ThreshDX_flags, S_ThreshDX_flags);
}

ThreshDXArray::~ThreshDXArray()
{
}

ThreshDYArray::~ThreshDYArray()
{
}

void ThreshArray::free_cur()
{
	if (cur_obj != 0)
	{
		env->DeleteLocalRef(cur_obj);
		cur_obj = 0;
		cur_id = -1;
	}
}

int ThreshArray::getLength()
{
	return arraySize;
}

int ThreshArray::selectId(int id) // boolean: "success"
{
	if (id == cur_id)
		return 1;
	free_cur();
	jobject obj = env->GetObjectArrayElement(array, id);
	if (obj != 0)
	{
		cur_id = id;
		cur_obj = obj;
		return 1;
	}
	else
		return 0;
}

int ThreshArray::getX0(int id)
{
	if (selectId(id))
		return env->GetIntField(cur_obj, id_x0);
	else
		return 0;
}

int ThreshArray::getX1(int id)
{
	if (selectId(id))
		return env->GetIntField(cur_obj, id_x1);
	else
		return 0;
}

int ThreshDXArray::getDX(int id, int key)
{
	if (!selectId(id))
		return 0;
	jintArray dx = (jintArray )env->GetObjectField(cur_obj, id_dX);
	if (dx == 0)
		return 0;
	jint data = 0;
	env->GetIntArrayRegion(dx, key, 1, &data);
	return (int )data;
}

int ThreshDXArray::getIsRise(int id)
{
	if (!selectId(id))
		return 0;
	jboolean rise = env->GetBooleanField(cur_obj, id_isRise);
	return rise;
}

void ThreshDXArray::setDX(int id, int key, int value)
{
	if (!selectId(id))
		return;
	jintArray dx = (jintArray )env->GetObjectField(cur_obj, id_dX);
	if (dx == 0)
		return;
	jint data = value;
	env->SetIntArrayRegion(dx, key, 1, &data);
	// ��������� ����� Java-�������
	env->CallVoidMethod(cur_obj, roundUp, key);
}
int ThreshDXArray::hasABCorrFlag(int id)
{
	if (!selectId(id))
		return 0; // some error?
	jbyte flags = env->GetByteField(cur_obj, id_flags);
	return (flags & com_syrus_AMFICOM_analysis_dadara_ThreshDX_FLAG_AB_CORR) != 0;
}

int ThreshDYArray::getTypeL(int id)
{
	if (selectId(id))
		return env->GetBooleanField(cur_obj, id_typeL);
	else
	{
		fprintf(stderr, "getType -- ??\n"); // FIXME
		return 0;
	}
}

double ThreshDYArray::getValue(int id, int key)
{
	if (!selectId(id))
		return 0;
	jdoubleArray values = (jdoubleArray )env->GetObjectField(cur_obj, id_values);
	assert(values);
	double data = 0.0;
	env->GetDoubleArrayRegion(values, key, 1, &data);
	return data;
}

void ThreshDYArray::setValue(int id, int key, double value)
{
	if (!selectId(id))
		return;
	jdoubleArray values = (jdoubleArray )env->GetObjectField(cur_obj, id_values);
	assert(values);
	jdouble data = value;
	env->SetDoubleArrayRegion(values, key, 1, &data);
	// ��������� ����� Java-�������
	env->CallVoidMethod(cur_obj, roundUp, key);
}

// converters

void ThreshDXArrayToTHXArray(ThreshDXArray &taX, int key, THX** thxOut, int *thxOutSize)
{
	const int thNpX = taX.getLength();
	*thxOutSize = thNpX;

	int conjKey = taX.getConjKey(key);
	int isUpper = taX.isUpper(key);

	THX *thX = 0;
	if (thNpX > 0)
	{
		thX = new THX[thNpX];
		assert(thX);
	}
	int j;
	for (j = 0; j < thNpX; j++)
	{
		int isRising = taX.getIsRise(j);
		int leftMode = !!isRising ^ !!isUpper ^ 1;
		thX[j].leftMode = leftMode;
		thX[j].x0 = taX.getX0(j);
		thX[j].x1 = taX.getX1(j);
		thX[j].dxL = leftMode ? -taX.getDX(j, key) : -taX.getDX(j, conjKey);
		thX[j].dxR = leftMode ? taX.getDX(j, conjKey) : taX.getDX(j, key);
	}
	*thxOut = thX;
	// the user will then have to free the array:
	// if (*thxOut != 0) delete[] *thxOut;
}

void ThreshDYArrayToTHYArray(ThreshDYArray &taY, int key, THY** thyOut, int *thyOutSize)
{

	const int thNpY = taY.getLength();
	*thyOutSize = thNpY;

	THY *thY = 0;
	if (thNpY > 0)
	{
		thY = new THY[thNpY];
		assert(thY);
	}
	int j;
	for (j = 0; j < thNpY; j++)
	{
		thY[j].x0 = taY.getX0(j);
		thY[j].x1 = taY.getX1(j);
		thY[j].dy = taY.getValue(j, key);
		thY[j].typeL = taY.getTypeL(j);
	}
	*thyOut = thY;
	// the user will then have to free the array:
	// if (*thyOut != 0) delete[] *thyOut;
}

void ThreshDXUpdateFromTHXArray(ThreshDXArray &taX, int key, THX* thX)
{
	const int thNpX = taX.getLength();

	int conjKey = taX.getConjKey(key);
	int isUpper = taX.isUpper(key);
	int j;
	for (j = 0; j < thNpX; j++)
	{
		int isRising = taX.getIsRise(j);
		int leftMode = !!isRising ^ !!isUpper ^ 1;
		taX.setDX(j, key,     leftMode ? -thX[j].dxL : thX[j].dxR);
		taX.setDX(j, conjKey, leftMode ? thX[j].dxR : -thX[j].dxL);
	}
}

void ThreshDYUpdateFromTHYArray(ThreshDYArray &taY, int key, THY* thY)
{
	const int thNpY = taY.getLength();
	int j;
	for (j = 0; j < thNpY; j++)
	{
		taY.setValue(j, key, thY[j].dy);
	}
}
