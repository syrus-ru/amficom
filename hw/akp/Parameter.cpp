// Parameter.cpp: implementation of the Parameter class.
//
//////////////////////////////////////////////////////////////////////

#include "Parameter.h"
#include "akpdefs.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Parameter::Parameter(ByteArray* name, ByteArray* value){
	this->name = name;
	this->value = value;
	this->length = INTSIZE + name->getLength() + INTSIZE + value->getLength();
}

Parameter::~Parameter() {
	delete this->name;
	delete this->value;
}

char* Parameter::getSegment() const {
	char* segment = new char[this->length];
	unsigned int i;
	char* segment1 = this->name->getSegment();
	unsigned int mile = INTSIZE + this->name->getLength();
	for (i = 0; i < mile; i++)
		segment[i] = segment1[i];
	delete[] segment1;
	segment1 = this->value->getSegment();
	for (i = mile; i < this->length; i++)
		segment[i] = segment1[i - mile];
	delete[] segment1;
	return segment;
}

unsigned int Parameter::getLength() const {
	return this->length;
}

ByteArray* Parameter::getName() const {
	return this->name;
}

ByteArray* Parameter::getValue() const {
	return this->value;
}

int operator == (const Parameter& p1, const Parameter& p2) {
	if(&p1 == &p2)
		return 1;
	return *(p1.getName()) == *(p2.getName()) && *(p1.getValue()) == *(p2.getValue());
}

int operator != (const Parameter& p1, const Parameter& p2) {
	if (&p1 == &p2)
		return 0;
	return *(p1.getName()) != *(p2.getName()) || *(p1.getValue()) != *(p2.getValue());
}

