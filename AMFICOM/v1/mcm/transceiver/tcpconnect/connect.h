#ifndef __CONNECT_H__
#define __CONNECT_H__

#include <iostream.h>
#include <winsock2.h>

typedef unsigned int u_int32_t;

#define EMSGSIZE WSAEMSGSIZE
#define WINDOWS

extern char * program_name;

void init ();
int inet_aton (char * cp, in_addr * pin);
void close (SOCKET connected_socket);
void exitConnection(int code);
void bzero(void * b, int n);

#endif