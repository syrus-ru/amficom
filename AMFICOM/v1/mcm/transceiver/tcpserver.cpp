#include "com_syrus_AMFICOM_mcm_TCPServer.h"
#include "etcp.h"

JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_mcm_TCPServer_getListeningSocket
  (JNIEnv * env, jobject obj,jbyteArray hostName, jbyteArray serviceName)
{
	init();

	jbyte * localServiceChars = env->GetByteArrayElements(serviceName,NULL);
	jsize l = env->GetArrayLength(serviceName);
	char * localService = new char[l];

	memcpy(localService, localServiceChars,l);

	env->ReleaseByteArrayElements(serviceName, localServiceChars,0);

	jbyte * localHostChars = env->GetByteArrayElements(hostName,NULL);
	l = env->GetArrayLength(hostName);
	char * localHost = new char[l];

	memcpy(localHost, localHostChars,l);

	env->ReleaseByteArrayElements(hostName, localHostChars,0);

	SOCKET listened_socket = tcp_server(localHost,localService);

	cout << "***Listening " << localHost << ":" << localService << " for connections with KISs.*** \n";
	
	return listened_socket;
}

JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_mcm_TCPServer_getConnectedSocket(
	JNIEnv * env,
	jobject obj,
	jint listening_socket,
	jbyteArray socket_kis_id)
{
	sockaddr_in peer;
	socklen_t peerlen = sizeof(peer);
	SOCKET accepted_socket = accept(listening_socket,(sockaddr *)&peer,(socklen_t *)&peerlen);
	if (accepted_socket == INVALID_SOCKET)
		return -1;

	cout << "Connection established at socket " << accepted_socket << "\n";


	int kis_id_size;
	int recvResult = recv (accepted_socket,(char *)&kis_id_size,sizeof(int),0);
	if (recvResult < 0)
		cout << "Connection failed while reading KIS_ID_SIZE from socket "
			 << accepted_socket << "\n";
	else
		cout << "Ready to read " << kis_id_size <<" bytes from socket "
			 << accepted_socket << "\n";
		
	jbyte * kis_id = new jbyte[kis_id_size];
	bzero(kis_id,kis_id_size);

	
	recvResult = recv (accepted_socket,(char *)kis_id,kis_id_size,0);
	if (recvResult < 0)
		cout << "Connection failed while reading KIS_ID from socket "
			 << accepted_socket << "\n";
	else
		cout << "for KIS, id = \"" << (char *)kis_id << "\"\n";	
	
	env->SetByteArrayRegion(socket_kis_id,0,kis_id_size,kis_id);
	
	
/*	cout << "Successfully set data to jcharArray\n";
	jchar * charrr = (char *)env->GetCharArrayElements(socket_kis_id,NULL);
	cout << "Checking: "<< (char *) charrr << "\n";
	
	env->ReleaseCharArrayElements(socket_kis_id,charrr,0);*/

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