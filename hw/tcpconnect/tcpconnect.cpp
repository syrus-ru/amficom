#include <string.h>
#include <stdio.h>
#include "tcpconnect.h"

#define HOST_NAME_LENGTH 64
#define DEBUG


SOCKET create_and_connect_socket(const char* host_name, const short port) {
	//Resolve remote host
	hostent* he;
	if ((he = gethostbyname(host_name)) == NULL) {
#ifdef DEBUG
		show_error("(tcpconnect) gethostbyname");
		printf("(tcpconnect) ERROR: Cannot find host: %s\n", host_name);
#endif
		return SOCKET_INVALID;
	}

	//Prepare struct sockaddr_in for remote address
	sockaddr_in remote_addr;
	remote_addr.sin_family = AF_INET;
	remote_addr.sin_port = htons(port);
	remote_addr.sin_addr = *(in_addr*)he->h_addr;
	memset(remote_addr.sin_zero, '\0', 8);

	//Create socket
	int sockfd;
	if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) <= 0) {
#ifdef DEBUG
		show_error("(tcpconnect) socket");
#endif
		return SOCKET_INVALID;
	}

	//Connect socket to remote address
#ifdef DEBUG
	printf("(tcpconnect) Connecting to address: %s, port: %hd\n", inet_ntoa(remote_addr.sin_addr), ntohs(remote_addr.sin_port));
#endif
	if (connect(sockfd, (sockaddr*)&remote_addr, sizeof(sockaddr)) == -1) {
#ifdef DEBUG
		show_error("(tcpconnect) connect");
		printf("(tcpconnect) ERROR: Cannot connect to address: %s, port: %hd\n", inet_ntoa(remote_addr.sin_addr), ntohs(remote_addr.sin_port));
#endif
		return SOCKET_INVALID;
	}
#ifdef DEBUG
	printf("(tcpconnect) Connected to address: %s, port: %hd\n", inet_ntoa(remote_addr.sin_addr), ntohs(remote_addr.sin_port));
#endif

	return (SOCKET)sockfd;
}

void close_socket(const SOCKET sockfd) {
#ifdef DEBUG
	printf("(tcpconnect) Closing socket\n");
#endif
	close_socket_platf(sockfd);

}

unsigned int transmit(const SOCKET sockfd, const char* data, const unsigned int size) {
	unsigned int segment_size = HEADERSIZE + size;

	char* data_t = new char[segment_size];
	uint32_t nsize = htonl(size);
	memcpy(data_t, (char*)&nsize, sizeof(unsigned int));
	memcpy(data_t + HEADERSIZE, data, size);

	int send_ret;
	unsigned int n_trans = 0;

	while (n_trans < segment_size) {
		send_ret = send((int)sockfd, data_t + n_trans, segment_size - n_trans, 0);
		if (send_ret < 0) {
#ifdef DEBUG
			show_error("(tcpconnect) send");
#endif
			break;
		}
		n_trans += send_ret;
	}

	if (n_trans <= HEADERSIZE)
		n_trans = 0;
	else
		n_trans -= HEADERSIZE;
	return n_trans;
}

SOCKET create_listening_socket(const short port) {
	int sockfd;
	if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) <= 0) {
#ifdef DEBUG
		show_error("(tcpconnect) socket");
#endif
		return SOCKET_INVALID;
	}

	char yes=1;
	if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int)) == -1) {
#ifdef DEBUG
		show_error("(tcpconnect) setsockopt");
#endif
		return SOCKET_INVALID;
	}

	sockaddr_in local_addr;
	local_addr.sin_family = AF_INET;
	local_addr.sin_port = htons(port);
	local_addr.sin_addr.s_addr = INADDR_ANY;
	memset(local_addr.sin_zero, '\0', 8);
#ifdef DEBUG
	printf("(tcpconnect) listening on address: %s, port: %hd\n", inet_ntoa(local_addr.sin_addr), ntohs(local_addr.sin_port));
#endif

	if (bind(sockfd, (sockaddr*)&local_addr, sizeof(sockaddr)) != 0) {
#ifdef DEBUG
		show_error("(tcpconnect) bind");
#endif
		return SOCKET_INVALID;
	}

	if (listen(sockfd, 5) != 0) {
#ifdef DEBUG
		show_error("(tcpconnect) listen");
#endif
		return SOCKET_INVALID;
	}

	return (SOCKET)sockfd;
}

int receive(const SOCKET sockfd, const int timeout, char*& data, unsigned int& size) {
	fd_set in;
	timeval tv;
	int sel_ret;

	FD_ZERO(&in);
	FD_SET(sockfd, &in);
	tv.tv_sec = timeout;
	tv.tv_usec = 0;

	sel_ret = select(sockfd + 1, &in, NULL, NULL, &tv);

	if (sel_ret == 0)
		return 0;

	int temp_sockfd;
	sockaddr_in remote_addr;
	socklen_t addrlen;
	uint32_t nsize;
	if (sel_ret > 0) {

		if((temp_sockfd = accept(sockfd, (sockaddr*)&remote_addr, &addrlen)) < 0) {
#ifdef DEBUG
			show_error("(tcpconnect) accept");
#endif
			return -1;
		}

		data = new char[HEADERSIZE];
		if (receive_bytes(temp_sockfd, data, HEADERSIZE) != HEADERSIZE) {
#ifdef DEBUG
			printf("(tcpconnect) Cannot read header of segment\n");
#endif
			delete[] data;
			return -1;
		}

		nsize = *(uint32_t*)data;
		delete[] data;
		size = ntohl(nsize);
		data = new char[size];
		if (receive_bytes(temp_sockfd, data, size) != size) {
#ifdef DEBUG
			printf("(tcpconnect) Cannot read data of segment\n");
#endif
			delete[] data;
			return -1;
		}
#ifdef DEBUG
		printf("(tcpconnect) Read data segment of size %d\n", size);
#endif
		return 1;
	}
	else {
#ifdef DEBUG
		show_error("(tcpconnect) select");
#endif
		return -1;
	}
}

unsigned int receive_bytes(const SOCKET sockfd, char*& buffer, unsigned int size) {
	unsigned int n_read = 0;
	int recv_ret;
#ifdef DEBUG
	printf("(tcpconnect) Reading %d bytes from socket\n", size);
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
