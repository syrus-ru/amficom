// SocketConnectionAcceptor.cpp: implementation of the SocketConnectionAcceptor class.
//
//////////////////////////////////////////////////////////////////////

#include "SocketConnectionAcceptor.h"
#include <stdio.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

SocketConnectionAcceptor::SocketConnectionAcceptor(SOCKET lsockfd) {
	if (lsockfd != INVALID_SOCKET)
		this->lsockfd = lsockfd;
	else
		printf("SocketConnectionAcceptor | Listening socket is INVALID_SOCKET\n");
	this->csockfd = INVALID_SOCKET;
}

SocketConnectionAcceptor::~SocketConnectionAcceptor() {
	if (this->communication_available())
		this->close_communication();
}

void SocketConnectionAcceptor::accept_connection(const unsigned int timewait) {
	fd_set in;
	timeval tv;
	int sel_ret;

	sockaddr_in remote_addr;
	socklen_t remote_addr_len = sizeof(remote_addr);

	FD_ZERO(&in);
	FD_SET(this->lsockfd, &in);
	tv.tv_sec = timewait;
	tv.tv_usec = 0;

	sel_ret = select(this->lsockfd + 1, &in, NULL, NULL, &tv);

	if (sel_ret == 0) {
		printf("SocketConnectionAcceptor.accept_connection | Nothing to accept\n");
	}
	else 
		if (sel_ret > 0) {
			this->csockfd = accept(this->lsockfd, (sockaddr*)&remote_addr, &remote_addr_len);
			if (this->csockfd > 0)
				printf("Accepted new connection from %s:%hu\n", inet_ntoa(remote_addr.sin_addr), ntohs(remote_addr.sin_port));
			else {
				show_error("SocketConnectionAcceptor.accept_connection | accept");
				this->csockfd = INVALID_SOCKET;
			}
		}
		else {
			show_error("SocketConnectionAcceptor.accept_connection | select");
			sleep_sec(timewait);
		}


	
}

SOCKET SocketConnectionAcceptor::get_communication_socket() const {
	return this->csockfd;
}

int SocketConnectionAcceptor::communication_available() const {
	return (this->csockfd != INVALID_SOCKET);
}

void SocketConnectionAcceptor::close_communication() {
	close_socket(this->csockfd);
	this->csockfd = INVALID_SOCKET;
}
