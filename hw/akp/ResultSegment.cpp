// ResultSegment.cpp: implementation of the ResultSegment class.
//
//////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include "crossplatf.h"
#include "ResultSegment.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

ResultSegment::ResultSegment(ByteArray* measurement_id,
					 unsigned int parnumber, Parameter** parameters) {
	this->measurement_id = measurement_id;
	this->parnumber = parnumber;
	this->parameters = parameters;

	this->createSegment();
}

ResultSegment::ResultSegment(unsigned int length, char* data) {
	this->length = length;
	this->data = data;

	this->parseSegment();
}

ResultSegment::~ResultSegment() {
	delete this->measurement_id;
	unsigned int i;
	for (i = 0; i < this->parnumber; i++)
		delete this->parameters[i];
	delete[] this->parameters;
}

ByteArray* ResultSegment::getMeasurementId () const {
	return this->measurement_id;
}

unsigned int ResultSegment::getParnumber() const {
	return this->parnumber;
}

Parameter** ResultSegment::getParameters() const {
	return this->parameters;
}

void ResultSegment::createSegment() {
	unsigned int i, parslen = 0;
	for (i = 0; i < this->parnumber; i++)
		parslen += this->parameters[i]->getLength();

	this->length = 1 + 
			INTSIZE + this->measurement_id->getLength() +
			INTSIZE +
			parslen;

	this->data = new char[this->length];
	int mile = 0;
	char* segment1;

	// Segment type
	this->data[mile] = SEGMENT_RESULT;
	mile += 1;

	//measurement_id
	segment1 = this->measurement_id->getSegment();
	for (i = 0; i < INTSIZE + this->measurement_id->getLength(); i++)
		this->data[i + mile] = segment1[i];
	delete[] segment1;
	mile += i;

	//parameters
	uint32_t nparnumber = htonl(this->parnumber);
	segment1 = (char*) &nparnumber;
	for (i = 0; i < INTSIZE; i++)
		this->data[i + mile] = segment1[i];
	mile += i;

	unsigned int j;
	for (j = 0; j < this->parnumber; j++) {
		segment1 = this->parameters[j]->getSegment();
		for (i = 0; i < this->parameters[j]->getLength(); i++)
			this->data[i + mile] = segment1[i];
		delete[] segment1;
		mile += i;
	}
}

void ResultSegment::parseSegment() {
	char* p = this->data + 1;

	//measurement_id
	this->measurement_id = new ByteArray(p);
	p += INTSIZE + this->measurement_id->getLength();

	//parameters
	uint_frame uiframe;
	for (unsigned int i = 0; i < sizeof(uint32_t); i++)
		uiframe.bytes[i] = p[i];
	this->parnumber = ntohl(uiframe.value);
	p += INTSIZE;

	this->parameters = new Parameter*[this->parnumber];
	unsigned int parcount = 0;
	while (p < this->data + this->length) {
		//name
		ByteArray* par_name = new ByteArray(p);
		p += INTSIZE + par_name->getLength();

		//value
		ByteArray* par_value = new ByteArray(p);
		p += INTSIZE + par_value->getLength();

		//parameter
		this->parameters[parcount] = new Parameter(par_name, par_value);
		parcount++;
	}
	if (parcount != this->parnumber) {
		printf("ERROR: real number of result parameters %u does not match to nominal %u\n", parcount, this->parnumber);
		this->parnumber = parcount;
	}
}

