#ifdef _WIN32

#include "crossplatf.h"
#include <stdio.h>

void close_socket_platf(const SOCKET connected_socket) {
  if (closesocket(connected_socket) == SOCKET_ERROR)
    show_error("(tcpconnect) closesocket");
}

void show_error(const char* mesg) {
	printf("%s: ", mesg);
	LPVOID lpMsgBuf;
	FormatMessage(FORMAT_MESSAGE_ALLOCATE_BUFFER |
				FORMAT_MESSAGE_FROM_SYSTEM |
				FORMAT_MESSAGE_IGNORE_INSERTS,
			NULL,
			GetLastError(),
			MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),	//Default language
			(LPTSTR)&lpMsgBuf,
			0,
			NULL);
	printf((LPCTSTR)lpMsgBuf);
	LocalFree(lpMsgBuf);
}

#endif
