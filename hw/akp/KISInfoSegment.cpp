// KISInfoSegment.cpp: implementation of the KISInfoSegment class.
//
//////////////////////////////////////////////////////////////////////

#include "KISInfoSegment.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

KISInfoSegment::KISInfoSegment(ByteArray* kis_id) {
	this->kis_id = kis_id;

	this->createSegment();
}

KISInfoSegment::KISInfoSegment(unsigned int length, char* data) {
	this->length = length;
	this->data = data;

	this->parseSegment();
}

KISInfoSegment::~KISInfoSegment() {
	delete this->kis_id;
}

ByteArray* KISInfoSegment::getKISId() const {
	return this->kis_id;
}

void KISInfoSegment::createSegment() {
	unsigned int i;

	this->length = 1 + 
			INTSIZE + this->kis_id->getLength();

	this->data = new char[this->length];
	int mile = 0;
	char* segment1;

	// Segment type
	this->data[mile] = SEGMENT_KIS_INFO;
	mile += 1;

	//kis_id
	segment1 = this->kis_id->getSegment();
	for (i = 0; i < INTSIZE + this->kis_id->getLength(); i++)
		this->data[i + mile] = segment1[i];
	delete[] segment1;
	mile += i;

	//...?
}

void KISInfoSegment::parseSegment() {
	char* p = this->data + 1;

	//kis_id
	this->kis_id = new ByteArray(p);
	p += INTSIZE + this->kis_id->getLength();

	//...?
}

