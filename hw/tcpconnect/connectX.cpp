#ifdef __unix__

#include "crossplatf.h"
#include <stdio.h>

void close_socket_platf (const SOCKET socket) {
  if (close ((int)socket))
	  show_error("(tcpconnect) close");
}

void show_error(const char* mesg) {
	perror(mesg);
}

void sleep_sec(const unsigned int n_sec) {
	sleep(n_sec);
}

#endif
