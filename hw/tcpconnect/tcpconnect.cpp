#include <string.h>
#include <stdio.h>
#include "tcpconnect.h"

#define HOST_NAME_LENGTH 64
#define DEBUG


SOCKET create_listening_socket(const short port) {
	SOCKET sockfd;
	if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) <= 0) {
#ifdef DEBUG
		show_error("(tcpconnect) socket");
#endif
		return INVALID_SOCKET;
	}

	char yes=1;
	if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int)) == -1) {
#ifdef DEBUG
		show_error("(tcpconnect) setsockopt");
#endif
		close_socket(sockfd);
		return INVALID_SOCKET;
	}

	sockaddr_in local_addr;
	local_addr.sin_family = AF_INET;
	local_addr.sin_port = htons(port);
	local_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	memset(local_addr.sin_zero, 0, 8);
#ifdef DEBUG
	printf("(tcpconnect) Listening on address: %s, port: %hu\n", inet_ntoa(local_addr.sin_addr), ntohs(local_addr.sin_port));
#endif

	if (bind(sockfd, (sockaddr*)&local_addr, sizeof(sockaddr)) != 0) {
#ifdef DEBUG
		show_error("(tcpconnect) bind");
#endif
		close_socket(sockfd);
		return INVALID_SOCKET;
	}

	if (listen(sockfd, 5) != 0) {
#ifdef DEBUG
		show_error("(tcpconnect) listen");
#endif
		close_socket(sockfd);
		return INVALID_SOCKET;
	}

	return sockfd;
}


SOCKET create_connected_socket(const char* host_name, const short port) {
	//Resolve remote host
	hostent* he;
	if ((he = gethostbyname(host_name)) == NULL) {
#ifdef DEBUG
		show_error("(tcpconnect) gethostbyname");
		printf("(tcpconnect) ERROR: Cannot find host: %s\n", host_name);
#endif
		return INVALID_SOCKET;
	}

	//Prepare struct sockaddr_in for remote address
	sockaddr_in remote_addr;
	remote_addr.sin_family = AF_INET;
	remote_addr.sin_port = htons(port);
	remote_addr.sin_addr = *(in_addr*)he->h_addr;
	memset(remote_addr.sin_zero, '\0', 8);

	//Create socket
	SOCKET sockfd;
	if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) <= 0) {
#ifdef DEBUG
		show_error("(tcpconnect) socket");
#endif
		return INVALID_SOCKET;
	}

	//Connect socket to remote address
#ifdef DEBUG
	printf("(tcpconnect) Connecting to address: %s, port: %hu\n", inet_ntoa(remote_addr.sin_addr), ntohs(remote_addr.sin_port));
#endif
	if (connect(sockfd, (sockaddr*)&remote_addr, sizeof(sockaddr)) == -1) {
#ifdef DEBUG
		show_error("(tcpconnect) connect");
		printf("(tcpconnect) ERROR: Cannot connect to address: %s, port: %hu\n", inet_ntoa(remote_addr.sin_addr), ntohs(remote_addr.sin_port));
#endif
		close_socket(sockfd);
		return INVALID_SOCKET;
	}
#ifdef DEBUG
	printf("(tcpconnect) Connected to address: %s, port: %hu\n", inet_ntoa(remote_addr.sin_addr), ntohs(remote_addr.sin_port));
#endif

	return sockfd;
}

void close_socket(const SOCKET sockfd) {
#ifdef DEBUG
	printf("(tcpconnect) Closing socket: %d\n", (int)sockfd);
#endif
	close_socket_platf(sockfd);

}

unsigned int receive_bytes(const SOCKET sockfd, char*& buffer, unsigned int size) {
	unsigned int n_read = 0;
	int recv_ret;
#ifdef DEBUG
	printf("(tcpconnect) Reading %u bytes from socket\n", size);
#endif
	while (n_read < size) {
		recv_ret = recv(sockfd, buffer + n_read, size - n_read, 0);
		if (recv_ret < 0) {
#ifdef DEBUG
			show_error("(tcpconnect) recv");
#endif
			return 0;
		}
		n_read += recv_ret;
	}
	return n_read;
}
