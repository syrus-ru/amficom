#ifndef AKPTCP_H
#define AKPTCP_H


#include "Segment.h"
#include "tcpconnect.h"

int receive_segment(SOCKET sockfd, const int timeout, Segment*& segment);

int transmit_segment(SOCKET sockfd, Segment* segment);


#endif
