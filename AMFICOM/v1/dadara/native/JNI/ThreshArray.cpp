#include <assert.h>

#include "Thresharray.h"
#include "names.h"

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
		assert(ua); // массив должен быть уже проинициализирован в Java
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
}

ThreshArray::~ThreshArray()
{
	free_cur();
}

ThreshDXArray::~ThreshDXArray()
{
}

ThreshDYArray::~ThreshDYArray()
{
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

int ThreshDXArray::getDX(int id, int key)
{
	if (!selectId(id))
		return 0;
	jintArray dx = (jintArray )env->GetObjectField(cur_obj, id_dX);
	if (dx == 0)
		return 0;
	long data = 0;
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

int ThreshArray::isUpper(int key)
{
	return UPPER[key];
}

int ThreshArray::getConjKey(int key)
{
	return CONJ[key];
}
