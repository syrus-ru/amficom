#include "akptcp.h"
#include <stdlib.h>

int receive_segment(SOCKET sockfd, const int timeout, Segment*& segment) {
	char* data;
	unsigned int length;

	int r = receive(sockfd, timeout, data, length);
	switch (r) {
		case 0:
			segment = NULL;
			return 0;
		case 1:
			segment = Segment::createFromData(length, data);
			return (segment != NULL) ? 1 : -1;
		default:
			segment = NULL;
			return -1;
	}
}

int transmit_segment(SOCKET sockfd, Segment* segment) {
	unsigned int length = segment->getLength();
	return (transmit(sockfd, segment->getData(), length) == length);
}
