// SocketReader.cpp: implementation of the SocketReader class.
//
//////////////////////////////////////////////////////////////////////

#include "SocketReader.h"
#include <stdio.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

SocketReader::SocketReader(SOCKET sockfd, unsigned int bytes_to_read, char* buffer) {
	if (sockfd != INVALID_SOCKET && bytes_to_read > 0 && buffer != NULL) {
		this->sockfd = sockfd;
		this->bytes_left_to_read = bytes_to_read;
		this->buffer_pointer = buffer;
		this->failed = 0;
	}
	else {
		printf("SocketReader | Wrong parameters\n");
		this->failed = 1;
	}
}

SocketReader::~SocketReader() {
}

int SocketReader::has_more_data_to_read() const {
	return (this->bytes_left_to_read > 0);
}

void SocketReader::read() {
	if (! this->is_failed() && this->has_more_data_to_read()) {
		int n_recv = recv(this->sockfd, this->buffer_pointer, this->bytes_left_to_read, 0);
		if (n_recv > 0) {
			if ((unsigned int)n_recv <= this->bytes_left_to_read) {
				this->bytes_left_to_read -= n_recv;
				this->buffer_pointer += n_recv;
			}
			else {
				printf("SocketReader.read | ERROR: Read more then buffer length\n");
				this->failed = 1;
			}
		}
		else {
			show_error("SocketReader.read | Cannot read from socket");
			this->failed = 1;
		}
	}
}

int SocketReader::is_failed() const {
	return this->failed;
}
