// MeasurementSegment.cpp: implementation of the MeasurementSegment class.
//
//////////////////////////////////////////////////////////////////////
#include <stdio.h>
#include "crossplatf.h"
#include "MeasurementSegment.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

MeasurementSegment::MeasurementSegment(ByteArray* measurement_id,
				ByteArray* measurement_type_id,
				ByteArray* local_address,
				unsigned int parnumber, Parameter** parameters) {
	this->measurement_id = measurement_id;
	this->measurement_type_id = measurement_type_id;
	this->local_address = local_address;
	this->parnumber = parnumber;
	this->parameters = parameters;

	this->createSegment();
}

MeasurementSegment::MeasurementSegment(unsigned int length, char* data) {
	this->length = length;
	this->data = data;

	this->parseSegment();
}

MeasurementSegment::~MeasurementSegment() {
	delete this->measurement_id;
	delete this->measurement_type_id;
	delete this->local_address;
	unsigned int i;
	for (i = 0; i < this->parnumber; i++)
		delete this->parameters[i];
	delete[] this->parameters;
}

ByteArray* MeasurementSegment::getMeasurementId () const {
	return this->measurement_id;
}

ByteArray* MeasurementSegment::getMeasurementTypeId() const {
	return this->measurement_type_id;
}

ByteArray* MeasurementSegment::getLocalAddress() const {
	return this->local_address;
}

unsigned int MeasurementSegment::getParnumber() const {
	return this->parnumber;
}

Parameter** MeasurementSegment::getParameters() const {
	return this->parameters;
}

void MeasurementSegment::createSegment() {
	unsigned int i, parslen = 0;
	for (i = 0; i < this->parnumber; i++)
		parslen += this->parameters[i]->getLength();

	this->length = 1 + 
			INTSIZE + this->measurement_id->getLength() +
			INTSIZE + this->measurement_type_id->getLength() +
			INTSIZE + this->local_address->getLength() +
			INTSIZE +
			parslen;
	this->data = new char[this->length];
	int mile = 0;
	char* segment1;

	// Segment type
	this->data[mile] = SEGMENT_MEASUREMENT;
	mile += 1;

	//measurement_id
	segment1 = this->measurement_id->getSegment();
	for (i = 0; i < INTSIZE + this->measurement_id->getLength(); i++)
		this->data[i + mile] = segment1[i];
	delete[] segment1;
	mile += i;

	//measurement_type_id
	segment1 = this->measurement_type_id->getSegment();
	for (i = 0; i < INTSIZE + this->measurement_type_id->getLength(); i++)
		this->data[i + mile] = segment1[i];
	delete[] segment1;
	mile += i;

	//local_address
	segment1 = this->local_address->getSegment();
	for (i = 0; i < INTSIZE + this->local_address->getLength(); i++)
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

void MeasurementSegment::parseSegment() {
	char* p = this->data + 1;

	//measurement_id
	this->measurement_id = new ByteArray(p);
	p += INTSIZE + this->measurement_id->getLength();

	//measurement_type_id
	this->measurement_type_id = new ByteArray(p);
	p += INTSIZE + this->measurement_type_id->getLength();

	//local_address
	this->local_address = new ByteArray(p);
	p += INTSIZE + this->local_address->getLength();

	//parameters
	this->parnumber = ntohl(*(uint32_t*) p);
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
		printf("ERROR: real number of measurement parameters %u does not match to nominal %u\n", parcount, this->parnumber);
		this->parnumber = parcount;
	}
}
