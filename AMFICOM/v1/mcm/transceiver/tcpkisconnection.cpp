#include <string.h>

#include "com_syrus_AMFICOM_mcm_TCPKISConnection.h"
#include "MeasurementSegment.h"
#include "ResultSegment.h"
#include "akptcp.h"

#define FIELDNAME_KIS_HOST_NAME "kisHostName"
#define FIELDNAME_KIS_TCP_PORT "kisTCPPort"
#define FIELDNAME_KIS_TCP_SOCKET "kisTCPSocket"
#define FIELDNAME_INITIAL_TIME_TO_SLEEP "initialTimeToSleep"
#define FIELDNAME_KIS_REPORT "kisReport"


jobject create_kis_report(JNIEnv *env, ResultSegment* result_segment);


JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_mcm_TCPKISConnection_establishSocketConnection(JNIEnv *env, jobject obj) {
	jclass cls = env->GetObjectClass(obj);
	jfieldID fid;

	jstring jkisHostName;
	const char* kis_host_name;

	short kis_port;

	SOCKET kis_socket;


	fid = env->GetFieldID(cls, FIELDNAME_KIS_HOST_NAME, "Ljava/lang/String;");
	jkisHostName = (jstring)env->GetObjectField(obj, fid);
	kis_host_name = env->GetStringUTFChars(jkisHostName, NULL);

	fid = env->GetFieldID(cls, FIELDNAME_KIS_TCP_PORT, "S");
	kis_port = (short)env->GetShortField(obj, fid);

	kis_socket = create_connected_socket(kis_host_name, kis_port);
	env->ReleaseStringUTFChars(jkisHostName, kis_host_name);

	if (kis_socket == INVALID_SOCKET)
		kis_socket = (SOCKET)com_syrus_AMFICOM_mcm_TCPKISConnection_KIS_TCP_SOCKET_DISCONNECTED;
	return (jint)kis_socket;
}

JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_mcm_TCPKISConnection_dropSocketConnection(JNIEnv *env, jobject obj) {
	jclass cls = env->GetObjectClass(obj);
	jfieldID fid = env->GetFieldID(cls, FIELDNAME_KIS_TCP_SOCKET, "I");
	SOCKET kis_socket = (SOCKET)env->GetIntField(obj, fid);
	close_socket(kis_socket);
}

JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_mcm_TCPKISConnection_transmitMeasurementBySocket(JNIEnv *env,
		jobject obj,
		jstring jmeasurement_id,
		jstring jmeasurement_type_codename,
		jstring jlocal_address,
		jobjectArray jparameter_type_codenames,
		jobjectArray jparameter_values,
		jlong jtimewait) {

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

	MeasurementSegment* measurement_segment = new MeasurementSegment(bmeasurement_id,
			bmeasurement_type_codename,
			blocal_address,
			parameters_length,
			parameters);

	unsigned int timewait = (unsigned int)(jtimewait / 1000);

	jclass cls = env->GetObjectClass(obj);
	jfieldID fid = env->GetFieldID(cls, FIELDNAME_KIS_TCP_SOCKET, "I");
	SOCKET kis_socket = (SOCKET)env->GetIntField(obj, fid);

	WriteSegmentStatus wrt_ret = transmit_segment(kis_socket, timewait, measurement_segment);
	switch (wrt_ret) {
		case WSS_OK:
			printf("Successfully transferred measurement segment\n");
			break;
		case WSS_INVALID_SOCKET:
		case WSS_CANNOT_WRITE_DATA:
			printf("Cannot transmit measurement segment\n");
			break;
		default:
			printf("Illegal return value of transmit_segment: %d\n", wrt_ret);
	}

	delete measurement_segment;

	return (wrt_ret == WSS_OK) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_mcm_TCPKISConnection_receiveKISReportFromSocket(JNIEnv *env, jobject obj, jlong jtimewait) {
	jclass cls = env->GetObjectClass(obj);
	jfieldID fid;

	unsigned int timewait = (unsigned int)(jtimewait / 1000);

	fid = env->GetFieldID(cls, FIELDNAME_KIS_TCP_SOCKET, "I");
	SOCKET kis_socket = (SOCKET)env->GetIntField(obj, fid);

	fd_set in;
	timeval tv;
	int sel_ret;

	FD_ZERO(&in);
	FD_SET(kis_socket, &in);
	tv.tv_sec = timewait;
	tv.tv_usec = 0;

	sel_ret = select(kis_socket + 1, &in, NULL, NULL, &tv);

	if (sel_ret == 0) {
		printf("Nothing to receive\n");
		return JNI_TRUE;
	}

	ReadSegmentStatus rec_ret;
	Segment* segment = NULL;
//	jobject j_kis_report;
	if (sel_ret > 0) {
		rec_ret = receive_segment(kis_socket, timewait, segment);
		switch (rec_ret) {
			case RSS_OK:
				if (segment->getType() == SEGMENT_RESULT) {
					printf("Received result segment\n");
					jobject j_kis_report = create_kis_report(env, (ResultSegment*)segment);
					fid = env->GetFieldID(cls, FIELDNAME_KIS_REPORT, "Lcom/syrus/AMFICOM/mcm/KISReport;");
					env->SetObjectField(obj, fid, j_kis_report);	
				}
				else {
					printf("Nothing to do with segment of type %d\n", segment->getType());
				}
				delete segment;
				return JNI_TRUE;
			case RSS_INVALID_SOCKET:
			case RSS_CANNOT_READ_HEADER:
			case RSS_CANNOT_READ_DATA:
			case RSS_ILLEGAL_HEADER:
				printf("Cannot receive segment\n");
				return JNI_FALSE;
			default:
				printf("Illegal return value of receive_segment: %d\n", rec_ret);
				return JNI_FALSE;
		}
	}

	show_error("select");
	return JNI_FALSE;
}

jobject create_kis_report(JNIEnv *env, ResultSegment* result_segment) {
	char* measurement_id = result_segment->getMeasurementId()->getData();
	jstring j_measurement_id = env->NewStringUTF(measurement_id);

	jsize parnumber = (jsize)result_segment->getParnumber();
	Parameter** parameters = result_segment->getParameters();

	jobjectArray j_par_codenames = (jobjectArray)env->NewObjectArray(parnumber, env->FindClass("java/lang/String"), NULL);
	jobjectArray j_par_values= (jobjectArray)env->NewObjectArray(parnumber, env->FindClass("[B"), NULL);
	jbyteArray jpar_value;
	for (jsize s = 0; s < parnumber; s++) {
		env->SetObjectArrayElement(j_par_codenames, s, env->NewStringUTF(parameters[s]->getName()->getData()));
		jpar_value = env->NewByteArray(parameters[s]->getValue()->getLength());
		env->SetByteArrayRegion(jpar_value, 0, parameters[s]->getValue()->getLength(), (jbyte*)parameters[s]->getValue()->getData());
		env->SetObjectArrayElement(j_par_values, s, jpar_value);
		env->DeleteLocalRef(jpar_value);
	}

	jclass kis_report_class = env->FindClass("com/syrus/AMFICOM/mcm/KISReport");
	jmethodID constructor_id = env->GetMethodID(kis_report_class, "<init>", "(Ljava/lang/String;[Ljava/lang/String;[[B)V");
	jobject j_kis_report = env->NewObject(kis_report_class,
			constructor_id,
			j_measurement_id,
			j_par_codenames,
			j_par_values);

	return j_kis_report;
}

