#ifdef __unix__

#include "crossplatf.h"
#include <stdio.h>

void close_socket_platf (const SOCKET connected_socket) {
  if (close ((int)connected_socket))
    perror ("Error calling close");
}

void show_error(const char* mesg) {
	perror(mesg);
}

#endif
