#ifndef _JNI_THRESHARRAY_H
#define _JNI_THRESHARRAY_H

#include <jni.h>
#include "../thresh/thresh.h"

class ThreshArray
{
protected:
	JNIEnv *env;
	jobjectArray array;
	int arraySize;

	int cur_id;
	jobject cur_obj;
	jmethodID roundUp;

private:
	jfieldID id_x0;
	jfieldID id_x1;

	int UPPER[4]; // <-- this could be static if we had good initialization technique
	int CONJ[4]; // <-- this could be static
	int keys[4]; // <-- this could be static

	void free_cur();
public:
	ThreshArray(JNIEnv *env, jobjectArray arrray);
	virtual ~ThreshArray();
	int getLength(); // number of thresholds in array
	int selectId(int id); // boolean: "success"
	int getX0(int id);
	int getX1(int id);
	int isUpper(int key);
	int getConjKey(int key);
	int getKeySoftUp();
	int getKeyHardUp();
	int getKeySoftDown();
	int getKeyHardDown();
};

class ThreshDYArray : public ThreshArray
{
private:
	jfieldID id_type;
	jfieldID id_values;
public:
	ThreshDYArray(JNIEnv *env, jobjectArray array);
	~ThreshDYArray();
	int getType(int id);
	double getValue(int id, int key);
	void setValue(int id, int key, double value);
};

class ThreshDXArray : public ThreshArray
{
private:
	jfieldID id_dX;
	jfieldID id_isRise;
	jfieldID id_flags;
public:
	ThreshDXArray(JNIEnv *env, jobjectArray array);
	~ThreshDXArray();
	int getDX(int id, int key);
	int getIsRise(int id);
	void setDX(int id, int key, int value);
	int hasABCorrFlag(int id);
};

void ThreshDXArrayToTHXArray(ThreshDXArray &thXi, int key, THX** thxOut, int *thxOutSize);
void ThreshDYArrayToTHYArray(ThreshDYArray &thYi, int key, THY** thyOut, int *thyOutSize);

void ThreshDXUpdateFromTHXArray(ThreshDXArray &taX, int key, THX* thX);
void ThreshDYUpdateFromTHYArray(ThreshDYArray &taY, int key, THY* thY);

#endif
