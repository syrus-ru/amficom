#include "com_syrus_AMFICOM_mcm_Transceiver.h"
#include "ByteArray.h"
#include "Parameter.h"
#include "ResultSegment.h"
#include "tcpconnect.h"

#define FIELDNAME_TCP_PORT "tcpPort"
#define FIELDNAME_TCP_SOCKET "tcpSocket"
#define FIELDNAME_INITIAL_TIME_TO_SLEEP "initialTimeToSleep"
#define FIELDNAME_KIS_REPORT "kisReport"

JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_mcm_Transceiver_setupTCPInterface(JNIEnv *env, jobject obj) {
	jclass cls = env->GetObjectClass(obj);
	jfieldID fid;

	short port;

	SOCKET sockfd;

	fid = env->GetFieldID(cls, FIELDNAME_TCP_PORT, "S");
	port = (short)env->GetShortField(obj, fid);

	sockfd = create_listening_socket(port);

	if (sockfd <= 0)
		sockfd = (SOCKET)com_syrus_AMFICOM_mcm_Transceiver_TCP_SOCKET_DISCONNECTED;
	return (jint)sockfd;
}

JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_mcm_Transceiver_downTCPInterface(JNIEnv *env, jobject obj) {
	jclass cls = env->GetObjectClass(obj);
	jfieldID fid = env->GetFieldID(cls, FIELDNAME_TCP_SOCKET, "I");
	SOCKET sockfd = (SOCKET)env->GetIntField(obj, fid);
	close_socket(sockfd);
}

JNIEXPORT jboolean JNICALL Java_com_syrus_AMFICOM_mcm_Transceiver_receive(JNIEnv *env, jobject obj) {
	jclass cls = env->GetObjectClass(obj);
	jfieldID fid;

	fid = env->GetFieldID(cls, FIELDNAME_TCP_SOCKET, "I");
	SOCKET sockfd = (SOCKET)env->GetIntField(obj, fid);

	fid = env->GetFieldID(cls, FIELDNAME_INITIAL_TIME_TO_SLEEP, "J");
	int timeout = (int)(env->GetLongField(obj, fid) / 1000);

	char* data;
	unsigned int length;
	int rec_ret = receive(sockfd, timeout, data, length);

	if (rec_ret == 0)
		return JNI_TRUE;

	if (rec_ret < 0)
		return JNI_FALSE;

	ResultSegment* resultSegment = new ResultSegment(length, data);

	char* measurement_id = resultSegment->getMeasurementId()->getData();
	jstring j_measurement_id = env->NewStringUTF(measurement_id);

	jsize parnumber = (jsize)resultSegment->getParnumber();
	Parameter** parameters = resultSegment->getParameters();

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

	delete resultSegment;

	jclass kis_report_class = env->FindClass("com/syrus/AMFICOM/mcm/KISReport");
	jmethodID constructor_id = env->GetMethodID(kis_report_class, "<init>", "(Ljava/lang/String;[Ljava/lang/String;[[B)V");
	jobject j_kis_report = env->NewObject(kis_report_class,
			constructor_id,
			j_measurement_id,
			j_par_codenames,
			j_par_values);

	fid = env->GetFieldID(cls, FIELDNAME_KIS_REPORT, "Lcom/syrus/AMFICOM/mcm/KISReport;");
	env->SetObjectField(obj, fid, j_kis_report);

	return JNI_TRUE;
}

