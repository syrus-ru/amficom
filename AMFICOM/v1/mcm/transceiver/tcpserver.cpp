#include "com_syrus_AMFICOM_mcm_TCPServer.h"
#include "etcp.h"

/*
 * Java native method declaration :
 * public native int getListeningSocket(String hostName, String serviceName);
 * 
 * Class:     com_syrus_AMFICOM_mcm_TCPServer
 * Method:    getListeningSocket
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_mcm_TCPServer_getListeningSocket
  (JNIEnv * env, jobject obj,jstring hostName, jstring serviceName){
	init();

	const char *service = env->GetStringUTFChars(serviceName, JNI_FALSE); 
	const char *host = env->GetStringUTFChars(hostName, JNI_FALSE); 

	SOCKET listened_socket = tcp_server((char *)host, (char *)service);

	cout << "***Listening " << host << ":" << service << ", socket:" << listened_socket << " for connections with KISs.*** \n";
	
	env->ReleaseStringUTFChars(hostName, host); 
	env->ReleaseStringUTFChars(serviceName, service); 	
	
	return listened_socket;
}

/*
 * Java native method declaration :
 * public native int getConnectedSocket(int listeningSocket, String kisId);
 * 
 * Class:     com_syrus_AMFICOM_mcm_TCPServer
 * Method:    getConnectedSocket
 * Signature: (I[Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_com_syrus_AMFICOM_mcm_TCPServer_getConnectedSocket(
	JNIEnv * env,
	jobject obj,
	jint listening_socket,
	jobjectArray objects){
	sockaddr_in peer;
	socklen_t peerlen = sizeof(peer);
	
	cout << "Try accept socket " << listening_socket << "\n";  
	SOCKET accepted_socket = accept(listening_socket,(sockaddr *)&peer,(socklen_t *)&peerlen);
	if (accepted_socket == INVALID_SOCKET)
		return NULL;

	cout << "Connection established at socket " << accepted_socket << "\n";


	fd_set rfds;
	struct timeval tv;
	FD_ZERO(&rfds);
	FD_SET(accepted_socket, &rfds);
	tv.tv_sec = TIMEOUT;
	tv.tv_usec = 0;
	
	int retval = select(accepted_socket + 1, &rfds, NULL, NULL, &tv);
	if(retval)	{				
		int kis_id_size;
		int recvResult = recv(accepted_socket, (char *)&kis_id_size, sizeof(int), 0);
		if (recvResult != sizeof(int))
			cout << "Connection failed while reading KIS_ID_SIZE from socket "
				 << accepted_socket << "\n";
		else {
			cout << "Ready to read " << kis_id_size <<" bytes from socket "
				 << accepted_socket << "\n";
			
			char *kis_id = new char[kis_id_size + 1];	
			memset(kis_id, 0, kis_id_size + 1);
			
			retval = select(accepted_socket + 1, &rfds, NULL, NULL, &tv);
			
			if (retval){			
				recvResult = recv(accepted_socket, kis_id, kis_id_size, 0);
				if (recvResult != kis_id_size)
					cout << "Connection failed while reading KIS_ID from socket "
						 << accepted_socket << "\n";
				else
					cout << "for KIS, id = \"" << (char *)kis_id << "\"\n";	
			
			    jobject kisId = env->NewStringUTF(kis_id);
			    env->SetObjectArrayElement(objects, 0, kisId);	    		    			    
			}
			
			delete[] kis_id;
		}
	}
	
	return accepted_socket;
}

/*
 * Java native method declaration :
 * public native void shutdownServer(int[] serverSockets);
 * 
 * Class:     com_syrus_AMFICOM_mcm_TCPServer
 * Method:    shutdownServer
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL Java_com_syrus_AMFICOM_mcm_TCPServer_shutdownServer
  (JNIEnv * env, jobject obj, jintArray socketsToClose){
	jint * socketsToCloseInts = env->GetIntArrayElements(socketsToClose,NULL);
	jsize l = env->GetArrayLength(socketsToClose);

	jint * lcSocketsToClose = new jint[l];

	memcpy(lcSocketsToClose, socketsToCloseInts,l);

	env->ReleaseIntArrayElements(socketsToClose, socketsToCloseInts,0);

	try
	{
		for (int i = 0; i < l; i++){
			closeExt((int)lcSocketsToClose[i]);
			cout << "Socket " << lcSocketsToClose[i] << " shutdowned.\n";
		}
		exitConnection(0);
	}
	catch(...)
	{
		cout << "Exception raised while shutdowning sockets.\n";
	}
	delete[]	lcSocketsToClose;
}