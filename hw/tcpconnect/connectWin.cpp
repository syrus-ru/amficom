#ifdef _WIN32

#include "crossplatf.h"
#include <stdio.h>

void close_socket_platf(const SOCKET socket) {
  if (closesocket(socket) == SOCKET_ERROR)
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

void sleep_sec(const unsigned int n_sec) {
	Sleep(n_sec * 1000);
}

#endif
