#include "com_syrus_AMFICOM_mcm_TCPServer.h"
#include "etcp.h"

JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_mcm_TCPServer_getListeningSocket
  (JNIEnv * env, jobject obj, jcharArray serviceName)
{
	init();

	unsigned short * localServiceChars = env->GetCharArrayElements(serviceName,NULL);
	jsize l = env->GetArrayLength(serviceName);
	char * localService = new char[l];

	memcpy(localService, localServiceChars,l);

	env->ReleaseCharArrayElements(serviceName, localServiceChars,0);

	SOCKET listened_socket = tcp_server("127.0.0.1",localService);

	cout << "Listening socket " << listened_socket << " for port " << localService << "\n";
	
	return listened_socket;
}

JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_mcm_TCPServer_getConnectedSocket(
	JNIEnv * env,
	jobject obj,
	jint listening_socket,
	jcharArray socket_kis_id)
{
	sockaddr_in peer;
	socklen_t peerlen = sizeof(peer);
	SOCKET accepted_socket = accept(listening_socket,(sockaddr *)&peer,(socklen_t *)&peerlen);
	if (accepted_socket == INVALID_SOCKET)
		return -1;

	cout << "Connection established at socket " << accepted_socket << "\n";

	unsigned short * kis_id = env->GetCharArrayElements(socket_kis_id,NULL);
	jsize l = env->GetArrayLength(socket_kis_id);

	bzero(kis_id,l);

	int recvResult = recv (accepted_socket,(char *)kis_id,l,0);
	if (recvResult < 0)
		cout << "Connection failed while reading KIS_ID from socket "
			 << accepted_socket << "\n";

	env->ReleaseCharArrayElements(socket_kis_id, kis_id,0);		


	return accepted_socket;
}

JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_mcm_TCPServer_shutdown_1server
  (JNIEnv * env, jobject obj, jintArray socketsToClose)
{
	jint * socketsToCloseInts = env->GetIntArrayElements(socketsToClose,NULL);
	jsize l = env->GetArrayLength(socketsToClose);

	jint * lcSocketsToClose = new jint[l];

	memcpy(lcSocketsToClose, socketsToCloseInts,l);

	env->ReleaseIntArrayElements(socketsToClose, socketsToCloseInts,0);

	try
	{
		for (int i = 0; i < l; i++)
		{
			closeExt((int)lcSocketsToClose[i]);
			cout << "Socket " << lcSocketsToClose[i] << " shutdowned.\n";
		}
		exitConnection(0);
	}
	catch(...)
	{
		cout << "Exception raised while shutdowning sockets.\n";
	}
}