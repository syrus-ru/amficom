#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "com_syrus_AMFICOM_agent_Transceiver.h"
#include <protocoldefs.h>
#include <ByteArray.h>
#include <Parameter.h>
#include <MeasurementSegment.h>
#include <ResultSegment.h>

const unsigned int MAXLENTASK = 256;
const unsigned int MAXLENREPORT = 34000;//6000;
const int MAXLENPATH = 128;
const char* FIFOROOTPATH = "/tmp";

/*
int main() {
	printf("************\n");
	return 1;
}
*/
JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_agent_Transceiver_call(JNIEnv *env, jclass cls, jstring jName) {
	return 1;
}

JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_agent_Transceiver_read1(JNIEnv *env, 
									jclass cls, 
									jint descFifo, 
									jstring jName) {
	char* fifoName = new char[MAXLENPATH];
	strcpy(fifoName, FIFOROOTPATH);
	strcat(fifoName, "/");
	const char* FifoName = env->GetStringUTFChars(jName, 0);
	strcat(fifoName, FifoName);
	env->ReleaseStringUTFChars(jName, FifoName);
	strcat(fifoName, getenv("USER"));
	printf("(native - agent - read) Opening FIFO: %s\n", fifoName);
	FILE* fp;
	if ((fp = fopen(fifoName, "r")) == NULL) {
		delete[] fifoName;
		perror("(native - agent - read) Cannot open FIFO");
		return JNI_FALSE;
	}
	printf("(native - agent - read) FIFO %s opened\n", fifoName);

	char* buftoread = new char[MAXLENREPORT];
	if ((fread(buftoread, 1, MAXLENREPORT, fp)) < 1) {
		delete[] fifoName;
		delete[] buftoread;
		fclose(fp);
		perror("(native - agent - read) Cannot read array");
		return JNI_FALSE;
	}
	delete[] fifoName;
	fclose(fp);

	unsigned int length = *(jsize*)buftoread;
	printf("(native - agent - read) read byte array of length == %d\n", length);
        if(length + INTSIZE > MAXLENREPORT) {
		delete[] buftoread;
		printf("(native - agent - read) ERROR: Inadequate length of received array: %d; must be less or equal %d\n",  length, MAXLENREPORT - INTSIZE);
		return JNI_FALSE;
	}

	char* data = new char[length];
	memcpy(data, buftoread + INTSIZE, length);
	delete[] buftoread;

	ResultSegment* resultSegment = new ResultSegment(length, data);
	jfieldID fld;

	char* measurement_id = resultSegment->getMeasurementId()->getData();
	jstring jmeasurement_id = env->NewStringUTF(measurement_id);
	fld = env->GetStaticFieldID(cls, "measurementId", "Ljava/lang/String;");
	env->SetStaticObjectField(cls, fld, (jobject)jmeasurement_id);

	jsize parnumber = (jsize)resultSegment->getParnumber();
	Parameter** parameters = resultSegment->getParameters();

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
	fld = env->GetStaticFieldID(cls, "parNames", "[Ljava/lang/String;");
	env->SetStaticObjectField(cls, fld, jpar_names);
	fld = env->GetStaticFieldID(cls, "parValues", "[[B");
	env->SetStaticObjectField(cls, fld, jpar_values);

	delete resultSegment;

	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_agent_Transceiver_push1(JNIEnv *env, 
									jclass cls, 
									jint descFifo, 
									jstring jName, 
									jstring jmeasurement_id, 
									jstring jmeasurement_type_id, 
									jstring jlocal_address, 
									jobjectArray jpar_names, 
									jobjectArray jpar_values) {
	unsigned int l;
	char* buffer;
	const char* jbuffer;
//measurement_id
	l = (unsigned int)env->GetStringLength(jmeasurement_id);
	buffer = new char[l];
	jbuffer = env->GetStringUTFChars(jmeasurement_id, 0);
	memcpy(buffer, jbuffer, l);
	env->ReleaseStringUTFChars(jmeasurement_id, jbuffer);
	ByteArray* bmeasurement_id = new ByteArray(l, buffer);
//measurement_type_id
	l = (unsigned int)env->GetStringLength(jmeasurement_type_id);
	buffer = new char[l];
	jbuffer = env->GetStringUTFChars(jmeasurement_type_id, 0);
	memcpy(buffer, jbuffer, l);
	env->ReleaseStringUTFChars(jmeasurement_type_id, jbuffer);
	ByteArray* bmeasurement_type_id = new ByteArray(l, buffer);
//local_address
	l = (unsigned int)env->GetStringLength(jlocal_address);
	buffer = new char[l];
	jbuffer = env->GetStringUTFChars(jlocal_address, 0);
	memcpy(buffer, jbuffer, l);
	env->ReleaseStringUTFChars(jlocal_address, jbuffer);
	ByteArray* blocal_address = new ByteArray(l, buffer);

//Parameters
	jsize names_len = env->GetArrayLength(jpar_names);
	jsize values_len = env->GetArrayLength(jpar_values);
	if (names_len != values_len) {
		printf("(native - agent - push) ERROR: number of parameter names %d is not equal to number of parameter values %d\n", names_len, values_len);
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
	MeasurementSegment* measurementSegment = new MeasurementSegment(bmeasurement_id, bmeasurement_type_id, blocal_address, (unsigned int)names_len, parameters);

	unsigned int length = measurementSegment->getLength();
	if (length + INTSIZE > MAXLENTASK) {
                printf("(native - agent - push) ERROR: Inadequate length of pushing array: %d; must be <= %d\n",  length, MAXLENTASK - INTSIZE);
		delete measurementSegment;
                return JNI_FALSE;
	}

	char* fifoName = new char[MAXLENPATH];
	strcpy(fifoName, FIFOROOTPATH);
	strcat(fifoName, "/");
	const char* FifoName = env->GetStringUTFChars(jName, 0);
	strcat(fifoName, FifoName);
	env->ReleaseStringUTFChars(jName, FifoName);
	strcat(fifoName, getenv("USER"));
	printf("(native - agent - push) Opening FIFO: %s\n", fifoName);
	FILE* fp;
	if ((fp = fopen(fifoName, "w")) == NULL) {
		perror("(native - agent - push) Cannot open FIFO");
		delete measurementSegment;
		delete[] fifoName;
		return JNI_FALSE;
	}
	printf("(native - agent - push) FIFO %s opened\n", fifoName);
	delete[] fifoName;

	char* arr = new char[MAXLENTASK];
	memcpy(arr, (char*)&length, INTSIZE);
	memcpy(arr + INTSIZE, measurementSegment->getData(), length);

	delete measurementSegment;

	printf("(native - agent - push) Transferring array of length: %d\n", length);
/*/------------
for (l = 0; l < INTSIZE + length; l++)
	printf("[%d] == %d\n", l, arr[l]);
//------------*/
	if (fwrite(arr, MAXLENTASK, 1, fp) < 1) {
		fclose(fp);
		delete[] arr;
		perror("(native - agent - push) Cannot transfer array");
		return JNI_FALSE;
	}
	delete[] arr;
	fclose(fp);
	printf("(native - agent - push) Successfully transferred array of length %d\n", length);

	return JNI_TRUE;
}
