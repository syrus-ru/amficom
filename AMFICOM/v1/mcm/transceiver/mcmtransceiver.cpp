/*
 * CHANGES:
 * 1. TestSegment -> MeasurementSegment.
 * 2. In Segments: getTestId() -> getMeasurementId().
 * 3. Remove start_time from MeasurementSegment.
 * 4. Of course, remove start_time from ResultSegment.
 * */


#include <string.h>
#include <stdlib.h>
#include <iostream.h>

#include "com_syrus_AMFICOM_mcm_Transceiver.h"
#include "protocoldefs.h"
#include "ByteArray.h"
#include "Parameter.h"
#include "MeasurementSegment.h"
#include "ResultSegment.h"
#include "etcp.h"

//const unsigned int MAXLENTASK = 256;
//const unsigned int MAXLENREPORT = 34000;//6000;
//const int MAXLENPATH = 128;
//const char* FIFOROOTPATH = "/tmp";

/*
int main() {
	printf("************\n");
	return 1;
}
*/

JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_mcm_Transceiver_transmit(JNIEnv *env,
									jobject obj,
									jint serv_socket,
									jstring jmeasurement_id,
									jstring jmeasurement_type_codename,
									jstring jlocal_address,
									jobjectArray jparameter_type_codenames,
									jobjectArray jparameter_values) {
										
	cout << "(native - agent - transmit) enter point.\n";
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

	unsigned int length = measurementSegment->getLength();

	char* arr = new char[length + INTSIZE];
	memcpy(arr, (char*)&length, INTSIZE);
	memcpy(arr + INTSIZE, measurementSegment->getData(), length);

	delete measurementSegment;

	cout << "(native - agent - transmit) Transmitting array of length: " << length << "\n";

	if (send(serv_socket, arr, length + INTSIZE, 0) != length + INTSIZE)
	{
		cout << "(native - agent - transmit) Transfer failed\n";
		delete[] arr;
		return JNI_FALSE;
	}

	cout << "(native - agent - transmit) Transfer succeeded\n";

	delete[] arr;

	return JNI_TRUE;
}

JNIEXPORT jobject JNICALL Java_com_syrus_AMFICOM_mcm_Transceiver_receive(
	JNIEnv *env,
	jobject obj,
	jint serv_socket){
	jclass transceiver_class = env->GetObjectClass(obj);

	cout << "(native - agent - receive)  Getting length of array to receive.\n";
	
	unsigned int length = 0;
	
	fd_set rfds;
	struct timeval tv;
	FD_ZERO(&rfds);
	FD_SET(serv_socket, &rfds);
	tv.tv_sec = TIMEOUT;
	tv.tv_usec = 0;
	
	int retval = select(serv_socket + 1, &rfds, NULL, NULL, &tv);
	if(retval)	{	
		int recvlenght = recv(serv_socket, (char *)&length, INTSIZE, 0);			
		if (recvlenght == 0)
			return NULL;
		if (recvlenght != INTSIZE)	{
			cout << "(native - agent - receive)  Failed getting length (" << recvlenght << ").\n";
			return NULL;
		}

		char* buftoread = new char[length];
		retval = select(serv_socket + 1, &rfds, NULL, NULL, &tv);
		
		if (retval){
			unsigned int receiveCode = recv(serv_socket, buftoread, length, 0);
			cout << "(native - agent - receive)  Received array of length " << receiveCode <<".\n";
			
			if (receiveCode != length) {
				cout << "(native - agent - receive)  Failed getting array of length " << length <<".\n";
				cout << "(native - agent - receive)  The declared data array length ("
					 << length
					 <<") and\n"
					 << "the length of the received array (" 
					 << receiveCode
					 <<") are NOT EQUAL!\n";			
				if (receiveCode < 0)
					cout << "Reason: the socket was gracefully closed.\n";
				else
					cout << "Reason: connection error.\n";
				return NULL;	
			}	
			
			char* data = new char[length];
			ResultSegment* resultSegment = new ResultSegment(length, buftoread);
		
			char* measurement_id = resultSegment->getMeasurementId()->getData();
			jstring j_measurement_id = env->NewStringUTF(measurement_id);
			
			jsize parnumber = (jsize)resultSegment->getParnumber();
			Parameter** parameters = resultSegment->getParameters();
		
			jobjectArray j_par_codenames = (jobjectArray)env->NewObjectArray(parnumber, env->FindClass("java/lang/String"), NULL);
			jobjectArray j_par_values =  (jobjectArray)env->NewObjectArray(parnumber, env->FindClass("[B"), NULL);
			jbyteArray jpar_value;
			for (jsize s = 0; s < parnumber; s++)
			{
				env->SetObjectArrayElement(j_par_codenames, s, env->NewStringUTF(parameters[s]->getName()->getData()));
				jpar_value = env->NewByteArray(parameters[s]->getValue()->getLength());
				env->SetByteArrayRegion(jpar_value, 0, parameters[s]->getValue()->getLength(), (jbyte*)parameters[s]->getValue()->getData());
				env->SetObjectArrayElement(j_par_values, s, jpar_value);
				env->DeleteLocalRef(jpar_value);
			}
		
			
			delete[] buftoread;
			delete resultSegment;
		
			jclass kis_report_class = env->FindClass("com/syrus/AMFICOM/mcm/KISReport");
			jmethodID constructor_id = env->GetMethodID(kis_report_class, "<init>", "(Ljava/lang/String;[Ljava/lang/String;[[B)V");
			jobject j_kis_report = env->NewObject(kis_report_class, constructor_id, j_measurement_id,
												j_par_codenames,
												j_par_values);
			return j_kis_report;
		}
		else 
			return NULL;
	} else 
		return NULL;
}

/*JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_mcm_Transceiver_init_server(
	JNIEnv *env,
	jobject obj)
{
	init();

	jclass transceiver_class = env->GetObjectClass(obj);
	jfieldID fid = env->GetFieldID(transceiver_class, "localPort", "Ljava/lang/String;");
	jstring local_port_strpointer = (jstring)env->GetObjectField(obj, fid);
	int l = env->GetStringUTFLength(local_port_strpointer);

	char * localPort = new char[l];

	const char* local_port_chars = env->GetStringUTFChars(local_port_strpointer, NULL);//must be released
	memcpy(localPort, local_port_chars,l);
	env->ReleaseStringUTFChars(local_port_strpointer, local_port_chars);

	SOCKET listened_socket = tcp_server("127.0.0.1",localPort);

	cout << "Listening socket " << listened_socket << " for port " << localPort << "\n";
	
	sockaddr_in peer;
	int peerlen = sizeof(peer);
	SOCKET accepted_socket = accept(listened_socket,(sockaddr *)&peer,&peerlen);
	if (accepted_socket == INVALID_SOCKET)
		return -1;

	cout << "Connection established at socket " << accepted_socket << "\n";
	return accepted_socket;
}*/

/*JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_mcm_Transceiver_shutdown_server(
	JNIEnv *env,
	jobject obj,
	jint serv_socket)
{
	try
	{
		close(serv_socket);
		exitConnection(0);

		cout << "Socket " << serv_socket << " shutdowned.\n";
		return true;
	}
	catch(...)
	{
	}

	return false;
}*/
