#ifndef CROSSPLATF_H
#define CROSSPLATF_H


#ifdef _WIN32

#include <windows.h>

typedef unsigned int uint32_t;
typedef int socklen_t;

#endif


#ifdef __unix__

#include <netdb.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

//Type to describe socket file descriptor
typedef int SOCKET;

#endif

void close_socket_platf(const SOCKET sockfd);

void show_error(const char* mesg);

#endif
