#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <string.h>
#include "com_syrus_AMFICOM_kis_Transceiver.h"
#include <akpdefs.h>
#include <ByteArray.h>
#include <Parameter.h>
#include <MeasurementSegment.h>
#include <ResultSegment.h>

const unsigned int MAXLENTASK = 256;
const unsigned int MAXLENREPORT= 34000;
const unsigned int MAXLENPATH= 128;
const char* FIFOROOTPATH = "/tmp";

JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_kis_Transceiver_create(JNIEnv *env, jclass cls, jstring jName) {
	char* fifoName = new char[MAXLENPATH];
	strcpy(fifoName, FIFOROOTPATH);
	strcat(fifoName, "/");
	const char* FifoName = env->GetStringUTFChars(jName, NULL);
	strcat(fifoName, FifoName);
	env->ReleaseStringUTFChars(jName, FifoName);
	strcat(fifoName, getenv("USER"));
	printf("(native - r6 - create) Creating FIFO: %s\n", fifoName);
	mknod(fifoName, S_IFIFO|0600, 0);
	printf("(native - r6 - create) FIFO %s created\n", fifoName);
	delete[] fifoName;
	return 1;
}

JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_kis_Transceiver_open(JNIEnv *env, jclass cls, jint descFifo) {
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_kis_Transceiver_close(JNIEnv *env, jclass cls, jint descFifo) {
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_kis_Transceiver_read1(JNIEnv *env, 
									jclass cls,
								       	jint descFifo, 
									jstring jName) {
	char* fifoName = new char[MAXLENPATH];
	strcpy(fifoName, FIFOROOTPATH);
	strcat(fifoName, "/");
	const char* FifoName = env->GetStringUTFChars(jName, NULL);
	strcat(fifoName, FifoName);
	env->ReleaseStringUTFChars(jName, FifoName);
	strcat(fifoName, getenv("USER"));
	printf("(native - r6 - read) Opening FIFO: %s\n", fifoName);
	FILE* fp;
	if ((fp = fopen(fifoName, "r")) == NULL) {
		delete[] fifoName;
		perror("(native - r6 - read) Cannot open FIFO");
		return JNI_FALSE;
	}
	printf("(native - r6 - read) FIFO %s opened\n", fifoName);

	char* buftoread = new char[MAXLENTASK];
	if ((fread(buftoread, 1, MAXLENTASK, fp)) < 1) {
		delete[] fifoName;
		delete[] buftoread;
		fclose(fp);
		perror("(native - r6 - read) Cannot read array");
		return JNI_FALSE;
	}
	delete[] fifoName;
	fclose(fp);

	jsize length = *(jsize*)buftoread;
	printf("(native - r6 - read) read byte array of length == %d\n", length);
        if(length + INTSIZE > MAXLENTASK) {
                delete[] buftoread;
                printf("(native - r6 - read) ERROR: Inadequate length of received array: %d; must be less or equal %d\n",  length, MAXLENTASK - INTSIZE);
                return JNI_FALSE;
        }

	char* data = new char[length];
	memcpy(data, buftoread + INTSIZE, length);
	delete[] buftoread;

	MeasurementSegment* measurementSegment = new MeasurementSegment(length, data);
	jfieldID fld;

	char* measurement_id = measurementSegment->getMeasurementId()->getData();
	jstring jmeasurement_id = env->NewStringUTF(measurement_id);
	fld = env->GetStaticFieldID(cls, "measurement_id", "Ljava/lang/String;");
	env->SetStaticObjectField(cls, fld, (jobject)jmeasurement_id);

	char* measurement_type_id = measurementSegment->getMeasurementTypeId()->getData();
	jstring jmeasurement_type_id = env->NewStringUTF(measurement_type_id);
	fld = env->GetStaticFieldID(cls, "measurement_type_id", "Ljava/lang/String;");
	env->SetStaticObjectField(cls, fld, (jobject)jmeasurement_type_id);

	char* local_address = measurementSegment->getLocalAddress()->getData();
	jstring jlocal_address = env->NewStringUTF(local_address);
	fld = env->GetStaticFieldID(cls, "local_address", "Ljava/lang/String;");
	env->SetStaticObjectField(cls, fld, (jobject)jlocal_address);

	jsize parnumber = (jsize)measurementSegment->getParnumber();
	Parameter** parameters = measurementSegment->getParameters();

	jobjectArray jpar_names = (jobjectArray)env->NewObjectArray(parnumber, env->FindClass("java/lang/String"), NULL);
	jobjectArray jpar_values =  (jobjectArray)env->NewObjectArray(parnumber, env->FindClass("[B"), NULL);
	jsize s;
	jbyteArray jpar_value;
	for (s = 0; s < parnumber; s++) {
		env->SetObjectArrayElement(jpar_names, s, env->NewStringUTF(parameters[s]->getName()->getData()));
		jpar_value = env->NewByteArray(parameters[s]->getValue()->getLength());
		env->SetByteArrayRegion(jpar_value, 0, parameters[s]->getValue()->getLength(), (jbyte*)parameters[s]->getValue()->getData());
		env->SetObjectArrayElement(jpar_values, s, jpar_value);
	}
	fld = env->GetStaticFieldID(cls, "par_names", "[Ljava/lang/String;");
	env->SetStaticObjectField(cls, fld, jpar_names);
	fld = env->GetStaticFieldID(cls, "par_values", "[[B");
	env->SetStaticObjectField(cls, fld, jpar_values);

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_kis_Transceiver_push1(JNIEnv *env, 
									jclass cls, 
									jint descFifo, 
									jstring jName, 
									jstring jmeasurement_id, 
									jobjectArray jpar_names, 
									jobjectArray jpar_values) {
	unsigned int l;
	char* buffer;
	const char*jbuffer;
//measurement_id
	l = (unsigned int)env->GetStringLength(jmeasurement_id);
	buffer = new char[l];
	jbuffer = env->GetStringUTFChars(jmeasurement_id, NULL);
	memcpy(buffer, jbuffer, l);
	env->ReleaseStringUTFChars(jmeasurement_id, jbuffer);
	ByteArray* bmeasurement_id = new ByteArray(l, buffer);
//Parameters
	jsize names_len = env->GetArrayLength(jpar_names);
	jsize values_len = env->GetArrayLength(jpar_values);
	if (names_len != values_len) {
		printf("(native - r6 - push) ERROR: number of parameter names %d is not equal to number of parameter values %d\n", names_len, values_len);
		return JNI_FALSE;
	}
	jstring jpar_name;
	jbyteArray jpar_value;
	Parameter** parameters = new Parameter*[names_len];
	jsize s;
	for (s = 0; s < names_len; s++) {
		jpar_name = (jstring)(env->GetObjectArrayElement(jpar_names, s));
		l = (unsigned int)env->GetStringLength(jpar_name);
		buffer = new char[l];
		jbuffer = env->GetStringUTFChars(jpar_name, NULL);
		memcpy(buffer, jbuffer, l);
		env->ReleaseStringUTFChars(jpar_name, jbuffer);
		ByteArray* bpar_name = new ByteArray(l, buffer);

		jpar_value = (jbyteArray)(env->GetObjectArrayElement(jpar_values, s));
		l = (unsigned int)env->GetArrayLength(jpar_value);
		buffer = new char[l];
		jbuffer = (const char*)env->GetByteArrayElements(jpar_value, NULL);
		memcpy(buffer, jbuffer, l);
		env->ReleaseByteArrayElements(jpar_value, (jbyte*)jbuffer, 0);
		ByteArray* bpar_value = new ByteArray(l, buffer);

		parameters[(int)s] = new Parameter(bpar_name, bpar_value);
	}

	ResultSegment* resultSegment = new ResultSegment(bmeasurement_id, (unsigned int)names_len, parameters);

	unsigned int length = resultSegment->getLength();
	if (length + INTSIZE > MAXLENREPORT) {
                printf("(native - agent - push) ERROR: Inadequate length of pushing array: %d; must be <= %d\n",  length, MAXLENREPORT - INTSIZE);
		delete resultSegment;
                return JNI_FALSE;
	}

	char* fifoName = new char[MAXLENPATH];
	strcpy(fifoName, FIFOROOTPATH);
	strcat(fifoName, "/");
	const char* FifoName = env->GetStringUTFChars(jName, NULL);
	strcat(fifoName, FifoName);
	env->ReleaseStringUTFChars(jName, FifoName);
	strcat(fifoName, getenv("USER"));
	printf("(native - r6 - push) Opening FIFO: %s\n", fifoName);
	FILE* fp;
	if ((fp = fopen(fifoName, "w")) == NULL) {
		perror("(native - r6 - push) Cannot open FIFO");
		delete resultSegment;
		delete[] fifoName;
		return JNI_FALSE;
	}
	printf("(native - r6 - push) FIFO %s opened\n", fifoName);
	delete[] fifoName;

	char* arr = new char[MAXLENREPORT];
	memcpy(arr, (char*)&length, INTSIZE);
	memcpy(arr + INTSIZE, resultSegment->getData(), length);

	delete resultSegment;

	printf("(native - r6 - push) Transferring array of length: %d\n", length);
/*
	for (i = 0; i < INTSIZE + length; i++)
		printf("arr[%d] == %d\n", i, arr[i]);*/

	if (fwrite(arr, MAXLENREPORT, 1, fp) < 1) {
		fclose(fp);
                delete[] arr;
		perror("(native - r6 - push) Cannot transfer array");
		return JNI_FALSE;
	}
	delete[] arr;
	fclose(fp);
	printf("(native - r6 - push) Successfully transferred array of length %d\n", length);

	return JNI_TRUE;
}
