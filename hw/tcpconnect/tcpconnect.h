#ifndef TCPCONNECT_H
#define TCPCONNECT_H

#include "crossplatf.h"

//Size of header.
//Header is a segment, containing helper data.
//It is added to front of every data array before transmission.
//Now it contains only length of a data array.
#define HEADERSIZE 4



//Create socket on local machine and given port to listen incoming requests
//Return: socket descriptor on success, -1 on error.
SOCKET create_listening_socket(const short port);

//Create and connect socket.
//Return: socket descriptor on success, -1 on error.
SOCKET create_connected_socket(const char* host_name, const short port);

//Close socket
void close_socket(const SOCKET sockfd);

//Read 'size' bytes from socket and store them in buffer 'buffer' (of size 'size').
//Return: number of successfully read bytes.
unsigned int receive_bytes(const SOCKET sockfd, char*& buffer, unsigned int size);

#endif

