#include <string.h>

#include "com_syrus_AMFICOM_mcm_TCPKISConnection.h"
#include "ByteArray.h"
#include "Parameter.h"
#include "MeasurementSegment.h"
#include "tcpconnect.h"

#define FIELDNAME_KIS_HOST_NAME "kisHostName"
#define FIELDNAME_KIS_TCP_PORT "kisTCPPort"
#define FIELDNAME_KIS_TCP_SOCKET "kisTCPSocket"

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

	kis_socket = create_and_connect_socket(kis_host_name, kis_port);
	env->ReleaseStringUTFChars(jkisHostName, kis_host_name);

	if (kis_socket <= 0)
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
	for (jsize s = 0; s < parameters_length; s++)
	{
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

	jclass cls = env->GetObjectClass(obj);
	jfieldID fid = env->GetFieldID(cls, FIELDNAME_KIS_TCP_SOCKET, "I");
	SOCKET kis_socket = (SOCKET)env->GetIntField(obj, fid);

	unsigned int length = measurementSegment->getLength();
	unsigned int n_trans = transmit(kis_socket, measurementSegment->getData(), length);

	delete measurementSegment;

	return (n_trans == length) ? JNI_TRUE : JNI_FALSE;
}

