// ByteArray.cpp: implementation of the ByteArray class.
//
//////////////////////////////////////////////////////////////////////

#include "akpdefs.h"
#include "ByteArray.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

ByteArray::ByteArray(unsigned int length, char* data) {
	this->length = length;
	this->data = data;
}

ByteArray::ByteArray(unsigned int length) {
	this->length = length;
	this->data = new char[length];
}

ByteArray::~ByteArray() {
	delete[] this->data;	//added 14.08.2003
}

unsigned int ByteArray::getLength() const {
	return this->length;
}

char* ByteArray::getSegment() const {
	char* segment = new char[INTSIZE + this->length];
	unsigned int i;
	for (i = 0; i < INTSIZE; i++)
		segment[i] = ((char*)&this->length)[i];
	for (i = 0; i < this->length; i++)
		segment[INTSIZE + i] = this->data[i];
	return segment;
}

char* ByteArray::getData() const {
	return this->data;
}

ByteArray* ByteArray::clone() const {
	char* data1 = new char[this->length];
	unsigned int i;
	for (i = 0; i < this->length; i++)
		data1[i] = this->data[i];
	return new ByteArray(this->length, data1);
}

ByteArray* ByteArray::getReversed() const {
	char* data1 = new char[this->length];
	unsigned int i;
	for (i = 0; i < this->length; i++)
		data1[i] = this->data[this->length - 1 - i];
	return new ByteArray(this->length, data1);
}

int operator == (const ByteArray& ba1, const ByteArray& ba2) {
	unsigned int l = ba1.getLength();
	if (l != ba2.getLength())
		return 0;
	unsigned int i;
	char* data1 = ba1.getData();
	char* data2 = ba2.getData();
	for (i = 0; i < l; i++)
		if (data1[i] != data2[i])
			return 0;
	return 1;
}

int operator != (const ByteArray& ba1, const ByteArray& ba2) {
	unsigned int l = ba1.getLength();
	if (l != ba2.getLength())
		return 1;
	unsigned int i;
	char* data1 = ba1.getData();
	char* data2 = ba2.getData();
	for (i = 0; i < l; i++)
		if (data1[i] != data2[i])
			return 1;
	return 0;
}
