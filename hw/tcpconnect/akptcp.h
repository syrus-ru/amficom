#ifndef AKPTCP_H
#define AKPTCP_H


#include "Segment.h"
#include "tcpconnect.h"

enum ReadSegmentStatus {
	RSS_OK,
	RSS_INVALID_SOCKET,
	RSS_CANNOT_READ_HEADER,
	RSS_ILLEGAL_HEADER,
	RSS_CANNOT_READ_DATA
};

ReadSegmentStatus receive_segment(SOCKET sockfd, const unsigned int timewait, Segment*& segment);

enum WriteSegmentStatus {
	WSS_OK,
	WSS_INVALID_SOCKET,
	WSS_CANNOT_WRITE_DATA
};

WriteSegmentStatus transmit_segment(SOCKET sockfd, const unsigned int timewait, Segment* segment);


#endif
