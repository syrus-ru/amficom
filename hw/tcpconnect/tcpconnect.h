#ifndef TCPCONNECT_H
#define TCPCONNECT_H

//Type to describe socket file descriptor
typedef int SOCKET;

//Size of header.
//Header is a segment, containing helper data.
//It is added to front of every data array before transmission.
//Now it contains only length of data array.
#define HEADERSIZE 4


//Create and connect socket.
//Return: socket descriptor on success, -1 on error.
SOCKET create_and_connect_socket(const char* host_name, const short port);

//Close socket
void close_socket(const SOCKET sockfd);

//Transmit data array of given size to socket.
//Return: number of transferred bytes (on success it is equal to array size)
unsigned int transmit(const SOCKET sockfd, const char* data, const unsigned int size);

//Create socket on local machine and given port to listen incoming requests
//Return: socket descriptor on success, -1 on error.
SOCKET create_listening_socket(const short port);

//Receive data from socket.
//Waits on incoming event timeout seconds.
//NOTE: Allocates new array of data and saves its size.
//Return: 0 if no incoming events; 1 on successful read; -1 on error.
int receive(const SOCKET sockfd, const int timeout, char*& data, unsigned int& size);

//Read 'size' bytes from socket and store them in buffer 'buffer' (of size 'size').
//Return: number of successfully read bytes.
unsigned int receive_bytes(const SOCKET sockfd, char*& buffer, unsigned int size);

#endif

