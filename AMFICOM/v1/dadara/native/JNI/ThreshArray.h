#ifndef _JNI_THRESHARRAY_H
#define _JNI_THRESHARRAY_H

#include <jni.h>

class ThreshArray
{
private:
	JNIEnv *env;
	jobjectArray array;
	int arraySize;
	jfieldID id_x0;
	jfieldID id_x1;
	jfieldID id_type;
	jfieldID id_values;
	jfieldID id_dxL;
	jfieldID id_dxR;

	int UPPER[4]; // <-- this could be static if we had good initialization technique

	int cur_id;
	jobject cur_obj;

	void free_cur();
public:
	ThreshArray(JNIEnv *env, jobjectArray arrray);
	~ThreshArray();
	int getLength();
	int selectId(int id); // boolean: "success"
	int getX0(int id);
	int getX1(int id);
	int getType(int id);
	double getValue(int id, int key);
	int getDxL(int id, int key);
	int getDxR(int id, int key);
	void setX0(int id, int x0);
	void setX1(int id, int x1);
	int isUpper(int key);
};

#endif
