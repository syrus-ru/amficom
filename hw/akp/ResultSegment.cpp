// ResultSegment.cpp: implementation of the ResultSegment class.
//
//////////////////////////////////////////////////////////////////////

#include <stdio.h>
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
	delete[] this->parameters;	//added 15.08.2003
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
	segment1 = (char*)&this->parnumber;
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
	unsigned int i, mile = 1, len;
	char* buffer;

	//measurement_id
	len = *(unsigned int*)(this->data + mile);
	mile += INTSIZE;

	buffer = new char[len + 1];
	for (i = 0; i < len; i++)
		buffer[i] = this->data[i + mile];
	buffer[len] = 0;
	this->measurement_id = new ByteArray(len, buffer);
	mile += i;

	//parameters
	this->parnumber = *(unsigned int*)(this->data + mile);
	mile += INTSIZE;

	this->parameters = new Parameter*[this->parnumber];
	unsigned int parcount = 0;
	while (mile < this->length) {

		//name
		len = *(unsigned int*)(this->data + mile);
		mile += INTSIZE;

		buffer = new char[len + 1];
		for (i = 0; i < len; i++)
			buffer[i] = this->data[i + mile];
		buffer[len] = 0;
		ByteArray* par_name = new ByteArray(len, buffer);
		mile += i;

		//value
		len = *(unsigned int*)(this->data + mile);
		mile += INTSIZE;

		buffer = new char[len];
		for (i = 0; i < len; i++)
			buffer[i] = this->data[i + mile];
		ByteArray* par_value = new ByteArray(len, buffer);
		mile += i;

		//parameter
		this->parameters[parcount] = new Parameter(par_name, par_value);
		parcount ++;
	}
	if (parcount != this->parnumber) {
		printf("ERROR: real number of result parameters %u does not match to nominal %u\n", parcount, this->parnumber);
		this->parnumber = parcount;
	}
}
