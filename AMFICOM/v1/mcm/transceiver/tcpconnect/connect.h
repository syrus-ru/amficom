#ifndef __CONNECT_H__
#define __CONNECT_H__

#include <iostream.h>

#ifdef _WIN32
#include <winsock2.h>
typedef unsigned int socklen_t;
#endif

#ifdef __unix__
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <string.h>

typedef int SOCKET;
#define INVALID_SOCKET 0xffffffff

#endif

typedef unsigned int u_int32_t;

extern char * program_name;

void init ();
int inet_aton (char * cp, in_addr * pin);
void closeExt (SOCKET connected_socket);
void exitConnection(int code);
void bzero(void * b, int n);

#endif