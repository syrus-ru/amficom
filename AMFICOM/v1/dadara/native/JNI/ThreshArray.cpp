#include <assert.h>

#include "Thresharray.h"
#include "names.h"

ThreshArray::ThreshArray(JNIEnv *env, jobjectArray array)
{
	this->env = env;
	this->array = array;
	cur_id = -1;
	cur_obj = 0;
	jclass clazz = env->FindClass(CL_Thresh);
	assert(clazz);
	arraySize = env->GetArrayLength(array);

	id_x0     = env->GetFieldID(clazz, N_Thresh_x0, S_Thresh_x0);
	id_x1     = env->GetFieldID(clazz, N_Thresh_x1, S_Thresh_x1);
	id_type   = env->GetFieldID(clazz, N_Thresh_type, S_Thresh_type);
	id_values = env->GetFieldID(clazz, N_Thresh_values, S_Thresh_values);
	id_dxL    = env->GetFieldID(clazz, N_Thresh_dxL, S_Thresh_dxL);
	id_dxR    = env->GetFieldID(clazz, N_Thresh_dxR, S_Thresh_dxR);

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

ThreshArray::~ThreshArray()
{
	free_cur();
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

int ThreshArray::getType(int id)
{
	if (selectId(id))
		return env->GetIntField(cur_obj, id_type);
	else
	{
		fprintf(stderr, "getType -- ??\n"); // FIXME
		return 0;
	}
}

double ThreshArray::getValue(int id, int key)
{
	if (!selectId(id))
		return 0;
	jdoubleArray values = (jdoubleArray )env->GetObjectField(cur_obj, id_values);
	assert(values);
	double data = 0.0;
	env->GetDoubleArrayRegion(values, key, 1, &data);
	return data;
}

int ThreshArray::getDxL(int id, int key)
{
	if (!selectId(id))
		return 0;
	jintArray dx = (jintArray )env->GetObjectField(cur_obj, id_dxL);
	if (dx == 0)
		return 0;
	long data = 0;
	env->GetIntArrayRegion(dx, key, 1, &data);
	return (int )data;
}

int ThreshArray::getDxR(int id, int key)
{
	if (!selectId(id))
		return 0;
	jintArray dx = (jintArray )env->GetObjectField(cur_obj, id_dxR);
	if (dx == 0)
		return 0;
	long data = 0;
	env->GetIntArrayRegion(dx, key, 1, &data);
	return (int )data;
}

void ThreshArray::setX0(int id, int x0)
{
	if (selectId(id))
		env->SetIntField(cur_obj, id_x0, x0);
}

void ThreshArray::setX1(int id, int x1)
{
	if (selectId(id))
		env->SetIntField(cur_obj, id_x1, x1);
}
int ThreshArray::isUpper(int key)
{
	return UPPER[key];
}
