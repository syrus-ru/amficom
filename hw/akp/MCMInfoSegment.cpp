// MCMInfoSegment.cpp: implementation of the MCMInfoSegment class.
//
//////////////////////////////////////////////////////////////////////

#include "MCMInfoSegment.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

MCMInfoSegment::MCMInfoSegment(ByteArray* mcm_id) {
	this->mcm_id = mcm_id;

	this->createSegment();
}

MCMInfoSegment::MCMInfoSegment(unsigned int length, char* data) {
	this->length = length;
	this->data = data;

	this->parseSegment();
}

MCMInfoSegment::~MCMInfoSegment() {
	delete this->mcm_id;
}

ByteArray* MCMInfoSegment::getMCMId() const {
	return this->mcm_id;
}

void MCMInfoSegment::createSegment() {
	unsigned int i;

	this->length = 1 + 
			INTSIZE + this->mcm_id->getLength();

	this->data = new char[this->length];
	int mile = 0;
	char* segment1;

	// Segment type
	this->data[mile] = SEGMENT_MCM_INFO;
	mile += 1;

	//mcm_id
	segment1 = this->mcm_id->getSegment();
	for (i = 0; i < INTSIZE + this->mcm_id->getLength(); i++)
		this->data[i + mile] = segment1[i];
	delete[] segment1;
	mile += i;

	//...?
}

void MCMInfoSegment::parseSegment() {
	unsigned int i, mile = 1, len;
	char* buffer;

	//mcm_id
	len = *(unsigned int*)(this->data + mile);
	mile += INTSIZE;

	buffer = new char[len + 1];
	for (i = 0; i < len; i++)
		buffer[i] = this->data[i + mile];
	buffer[len] = 0;
	this->mcm_id = new ByteArray(len, buffer);
	mile += i;

	//...?
}
