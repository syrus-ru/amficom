// SocketWriter.cpp: implementation of the SocketWriter class.
//
//////////////////////////////////////////////////////////////////////

#include "SocketWriter.h"
#include <stdio.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

SocketWriter::SocketWriter(SOCKET sockfd, unsigned int data_length, char* data) {
	if (sockfd != INVALID_SOCKET && data_length > 0 && data != NULL) {
		this->sockfd = sockfd;
		this->bytes_left_to_write = data_length;
		this->buffer_pointer = data;
		this->failed = 0;
	}
	else {
		printf("SocketWriter | Wrong parameters\n");
		this->failed = 1;
	}
}

SocketWriter::~SocketWriter() {
}

int SocketWriter::has_more_data_to_write() const {
	return (this->bytes_left_to_write > 0);
}

void SocketWriter::write() {
	if (! this->is_failed() && this->has_more_data_to_write()) {
		int n_send = send(this->sockfd, this->buffer_pointer, this->bytes_left_to_write, 0);
		if (n_send > 0) {
			this->bytes_left_to_write -= n_send;
			this->buffer_pointer += n_send;
		}
		else {
			show_error("SocketWriter.write | Cannot write to socket");
			this->failed = 1;
		}
	}
}

int SocketWriter::is_failed() const {
	return this->failed;
}
