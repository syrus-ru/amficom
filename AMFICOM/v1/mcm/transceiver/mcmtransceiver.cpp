/*
 * CHANGES:
 * 1. TestSegment -> MeasurementSegment.
 * 2. In Segments: getTestId() -> getMeasurementId().
 * 3. Remove start_time from MeasurementSegment.
 * 4. Of course, remove start_time from ResultSegment.
 * */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/select.h>
#include "com_syrus_AMFICOM_mcm_Transceiver.h"
#include "protocoldefs.h"
#include "ByteArray.h"
#include "Parameter.h"
#include "MeasurementSegment.h"
#include "ResultSegment.h"

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

JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_mcm_Transceiver_transmit(JNIEnv *env,
									jobject obj,
									jstring jmeasurement_id,
									jstring jmeasurement_type_codename,
									jstring jlocal_address,
									jobjectArray jparameter_type_codenames,
									jobjectArray jparameter_values) {
	unsigned int l;
	char* buffer;
	const char* jbuffer;

//measurement_id
	l = env->GetStringUTFLength(jmeasurement_id);
	buffer = new char[l];
	jbuffer = env->GetStringUTFChars(jmeasurement_id, NULL);
	memcpy(buffer, jbuffer, l);
	env->ReleaseStringUTFChars(jmeasurement_id, jbuffer);
	ByteArray* bmeasurement_id = new ByteArray(l, buffer);

//measurement_type_codename
	l = env->GetStringUTFLength(jmeasurement_type_codename);
	buffer = new char[l];
	jbuffer = env->GetStringUTFChars(jmeasurement_type_codename, NULL);
	memcpy(buffer, jbuffer, l);
	env->ReleaseStringUTFChars(jmeasurement_type_codename, jbuffer);
	ByteArray* bmeasurement_type_codename = new ByteArray(l, buffer);

//local_address
	l = env->GetStringUTFLength(jlocal_address);
	buffer = new char[l];
	jbuffer = env->GetStringUTFChars(jlocal_address, NULL);
	memcpy(buffer, jbuffer, l);
	env->ReleaseStringUTFChars(jlocal_address, jbuffer);
	ByteArray* blocal_address = new ByteArray(l, buffer);

//parameters
	jsize parameters_length = env->GetArrayLength(jparameter_type_codenames);
	Parameter** parameters = new Parameter*[parameters_length];
	jstring jparameter_type_codename;
	jbyteArray jparameter_value;
	for (jsize s = 0; s < parameters_length; s++) {
		jparameter_type_codename = (jstring)env->GetObjectArrayElement(jparameter_type_codenames, s);
		l = env->GetStringUTFLength(jparameter_type_codename);
		buffer = new char[l];
		jbuffer = env->GetStringUTFChars(jparameter_type_codename, NULL);
		memcpy(buffer, jbuffer, l);
		env->ReleaseStringUTFChars(jparameter_type_codename, jbuffer);
		ByteArray* bpar_type_codename = new ByteArray(l, buffer);

		jparameter_value = (jbyteArray)env->GetObjectArrayElement(jparameter_values, s);
		l = env->GetArrayLength(jparameter_value);
		buffer = new char[l];
		jbuffer = (const char*)env->GetByteArrayElements(jparameter_value, NULL);
		memcpy(buffer, jbuffer, l);
		env->ReleaseByteArrayElements(jparameter_value, (jbyte*)jbuffer, JNI_ABORT);
		ByteArray* bpar_value = new ByteArray(l, buffer);

		parameters[s] = new Parameter(bpar_type_codename, bpar_value);
	}

	MeasurementSegment* measurementSegment = new MeasurementSegment(bmeasurement_id,
									bmeasurement_type_codename,
									blocal_address,
									parameters_length,
									parameters);

	unsigned int length = measurementSegment->getLength();
	if (length + INTSIZE > MAXLENTASK) {
                printf("(native - agent - transmit) ERROR: Inadequate length of transmitting array: %d; must be <= %d\n",  length, MAXLENTASK - INTSIZE);
		delete measurementSegment;
                return JNI_FALSE;
	}

	jclass transceiver_class = env->GetObjectClass(obj);
	char* fifoName = new char[MAXLENPATH];
	strcpy(fifoName, FIFOROOTPATH);
	strcat(fifoName, "/");
	jfieldID fid = env->GetFieldID(transceiver_class, "taskFileName", "Ljava/lang/String;");
	jstring j_task_file_name = (jstring)env->GetObjectField(obj, fid);
	const char* task_file_name = env->GetStringUTFChars(j_task_file_name, NULL);
	strcat(fifoName, task_file_name);
	env->ReleaseStringUTFChars(j_task_file_name, task_file_name);
	strcat(fifoName, getenv("USER"));
//	printf("(native - agent - transmit) Opening FIFO: %s\n", fifoName);
	FILE* fp;
	if ((fp = fopen(fifoName, "w")) == NULL) {
		perror("(native - agent - transmit) Cannot open FIFO");
		delete measurementSegment;
		delete[] fifoName;
		return JNI_FALSE;
	}
//	printf("(native - agent - transmit) FIFO %s opened\n", fifoName);
	delete[] fifoName;

	char* arr = new char[MAXLENTASK];
	memcpy(arr, (char*)&length, INTSIZE);
	memcpy(arr + INTSIZE, measurementSegment->getData(), length);

	delete measurementSegment;

	printf("(native - agent - transmit) Transmitting array of length: %d\n", length);
/*/------------
for (l = 0; l < INTSIZE + length; l++)
	printf("[%d] == %d\n", l, arr[l]);
//------------*/
	if (fwrite(arr, MAXLENTASK, 1, fp) < 1) {
		fclose(fp);
		delete[] arr;
		perror("(native - agent - transmit) Cannot transmit array");
		return JNI_FALSE;
	}
	delete[] arr;
	fclose(fp);
	printf("(native - agent - transmit) Successfully transmitted array of length %d\n", length);

	return JNI_TRUE;
}

JNIEXPORT jobject JNICALL Java_com_syrus_AMFICOM_mcm_Transceiver_receive(JNIEnv *env, jobject obj) {
	jfieldID fid;
	jclass transceiver_class = env->GetObjectClass(obj);

	char* fifoName = new char[MAXLENPATH];
	strcpy(fifoName, FIFOROOTPATH);
	strcat(fifoName, "/");
	fid = env->GetFieldID(transceiver_class, "reportFileName", "Ljava/lang/String;");
	jstring j_report_file_name = (jstring)env->GetObjectField(obj, fid);
	const char* report_file_name = env->GetStringUTFChars(j_report_file_name, NULL);
	strcat(fifoName, report_file_name);
	env->ReleaseStringUTFChars(j_report_file_name, report_file_name);
	strcat(fifoName, getenv("USER"));
//	printf("(native - agent - receive) Opening FIFO: %s\n", fifoName);

	fid = env->GetFieldID(transceiver_class, "initialTimeToSleep", "J");
	unsigned int kis_tick_time = (unsigned int)(((long)env->GetLongField(obj, fid))/1000);
	int fd;
	fd_set in;
	timeval tv;
	int sel_ret;
	char* buftoread;

	if ((fd = open(fifoName, O_RDONLY | O_NONBLOCK)) <= 0) {
		delete[] fifoName;
		perror("(native - agent - receive) Cannot open FIFO");
		sleep(kis_tick_time);
		return NULL;
	}
//	printf("(native - agent - receive) FIFO %s opened\n", fifoName);

	FD_ZERO(&in);
	FD_SET(fd, &in);
	tv.tv_sec = (long)kis_tick_time;
	tv.tv_usec = 0;
	sel_ret = select(fd + 1, &in, NULL, NULL, &tv);
	if (sel_ret == 0) {
		delete[] fifoName;
		close(fd);
		return NULL;
	}
	else
		if (sel_ret > 0) {
			buftoread = new char[MAXLENREPORT];
			if ((read(fd, buftoread, MAXLENREPORT)) < 1) {
				delete[] fifoName;
				delete[] buftoread;
				close(fd);
				perror("(native - agent - receive) Cannot read array");
				sleep(kis_tick_time);
				return NULL;
			}
		}
		else {
			delete[] fifoName;
			close(fd);
			perror("(native - agent - receive) select");
			sleep(kis_tick_time);
			return NULL;
		}
	delete[] fifoName;
	close(fd);

	unsigned int length = *(jsize*)buftoread;
	printf("(native - agent - receive) Received byte array of length == %d\n", length);
        if(length + INTSIZE > MAXLENREPORT) {
		delete[] buftoread;
		printf("(native - agent - receive) ERROR: Inadequate length of received array: %d; must be less or equal %d\n",  length, MAXLENREPORT - INTSIZE);
		return NULL;
	}

	char* data = new char[length];
	memcpy(data, buftoread + INTSIZE, length);
	delete[] buftoread;

	ResultSegment* resultSegment = new ResultSegment(length, data);

	char* measurement_id = resultSegment->getMeasurementId()->getData();
	jstring j_measurement_id = env->NewStringUTF(measurement_id);
	
	jsize parnumber = (jsize)resultSegment->getParnumber();
	Parameter** parameters = resultSegment->getParameters();

	jobjectArray j_par_codenames = (jobjectArray)env->NewObjectArray(parnumber, env->FindClass("java/lang/String"), NULL);
	jobjectArray j_par_values =  (jobjectArray)env->NewObjectArray(parnumber, env->FindClass("[B"), NULL);
	jbyteArray jpar_value;
	for (jsize s = 0; s < parnumber; s++) {
		env->SetObjectArrayElement(j_par_codenames, s, env->NewStringUTF(parameters[s]->getName()->getData()));
		jpar_value = env->NewByteArray(parameters[s]->getValue()->getLength());
		env->SetByteArrayRegion(jpar_value, 0, parameters[s]->getValue()->getLength(), (jbyte*)parameters[s]->getValue()->getData());
		env->SetObjectArrayElement(j_par_values, s, jpar_value);
		env->DeleteLocalRef(jpar_value);
	}

	delete resultSegment;

	jclass kis_report_class = env->FindClass("com/syrus/AMFICOM/mcm/KISReport");
	jmethodID constructor_id = env->GetMethodID(kis_report_class, "<init>", "(Ljava/lang/String;[Ljava/lang/String;[[B)V");
	jobject j_kis_report = env->NewObject(kis_report_class, constructor_id, j_measurement_id,
										j_par_codenames,
										j_par_values);
	return j_kis_report;
}

