#include "akptcp.h"
#include "SocketReader.h"
#include "SocketWriter.h"
#include <stdio.h>
#include <string.h>


ReadSegmentStatus receive_segment(const SOCKET sockfd, const unsigned int timewait, Segment*& segment) {
	if (sockfd == INVALID_SOCKET) {
		printf("(akptcp) Socket is INVALID_SOCKET\n");
		return RSS_INVALID_SOCKET;
	}

	char* buffer;
	SocketReader* sr;
	uint32_t nlength;
	unsigned int length;

	//Read header
	buffer = new char[HEADERSIZE];
	sr = new SocketReader(sockfd, HEADERSIZE, buffer);
	while (! sr->is_failed() && sr->has_more_data_to_read()) {
		sr->read();
		if (sr->has_more_data_to_read())
			sleep_sec(timewait);
	}
	if (sr->is_failed() && sr->has_more_data_to_read()) {
		printf("(akptcp) Cannot read header of segment\n");
		delete[] buffer;
		delete sr;
		return RSS_CANNOT_READ_HEADER;
	}
	delete sr;

	//Get length of data from header
	nlength = *(uint32_t*)buffer;
	delete[] buffer;
	length = ntohl(nlength);
	if (length > MAX_SEGMENT_LENGTH || length == 0) {
		printf("(akptcp) Illegal length of segment: %u; must be in (0, %u]. Header of segment is illegal\n", length, MAX_SEGMENT_LENGTH);
		return RSS_ILLEGAL_HEADER;
	}

	//Read data
	buffer = new char[length];
	sr = new SocketReader(sockfd, length, buffer);
	while (! sr->is_failed() && sr->has_more_data_to_read()) {
		sr->read();
		if (sr->has_more_data_to_read())
			sleep_sec(timewait);
	}
	if (sr->is_failed() && sr->has_more_data_to_read()) {
		printf("(akptcp) Cannot read data of segment\n");
		delete[] buffer;
		delete sr;
		return RSS_CANNOT_READ_DATA;
	}
	delete sr;

	segment = Segment::createFromData(length, buffer);

	return RSS_OK;
}

WriteSegmentStatus transmit_segment(SOCKET sockfd, const unsigned int timewait, Segment* segment) {
	if (sockfd == INVALID_SOCKET) {
		printf("(akptcp) Socket is INVALID_SOCKET\n");
		return WSS_INVALID_SOCKET;
	}

	unsigned int segment_length = segment->getLength();
	uint32_t nlength = htonl(segment_length);
	unsigned int data_length = HEADERSIZE + segment_length;
	char* data = new char[data_length];
	memcpy(data, (char*)&nlength, sizeof(unsigned int));
	memcpy(data + HEADERSIZE, segment->getData(), segment_length);

	SocketWriter* sw = new SocketWriter(sockfd, data_length, data);
	while (! sw->is_failed() && sw->has_more_data_to_write()) {
		sw->write();
		if (sw->has_more_data_to_write())
			sleep_sec(timewait);
	}
	if (sw->is_failed() && sw->has_more_data_to_write()) {
		printf("(akptcp) Cannot write segment\n");
		delete[] data;
		delete sw;
		return WSS_CANNOT_WRITE_DATA;
	}
	delete[] data;
	delete sw;

	return WSS_OK;
}
