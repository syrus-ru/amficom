#include <assert.h>
#include <string.h>
#include "byteStream.h"

//#if sizeof(long) != 4
//#error Unsupported platform. Expect long of four bytes.
//#endif

// --- byteOut ---

byteOut::byteOut() {
	data = 0;
	pos = 0;
	allocated = 0;
}
byteOut::~byteOut() {
	if (allocated)
		delete[] data;
}

void byteOut::ensure(int inc) {
	// is enough?
	if (pos + inc <= allocated)
		return;
	// not enough, need to extend
	int newAlloc = allocated * 3 / 2 + 16;
	if (newAlloc < pos + inc)
		newAlloc = pos + inc;
	unsigned char *newData = new unsigned char[newAlloc];
	assert(newData);
	if (pos)
		memcpy(newData, data, pos);
	if (allocated)
		delete[] data;
	allocated = newAlloc;
	data = newData;
}

void byteOut::writeChar(int v) {
	ensure(1);
	data[pos++] = v;
}
void byteOut::writeShort(int v) {
	ensure(2);
	data[pos++] = (char) (v >> 8);
	data[pos++] = (char) v;
}
void byteOut::write3B(long v) {
	ensure(3);
	data[pos++] = (char) (v >> 16);
	data[pos++] = (char) (v >> 8);
	data[pos++] = (char) v;
}
void byteOut::writeLong(long v) {
	ensure(4);
	data[pos++] = (char) (v >> 24);
	data[pos++] = (char) (v >> 16);
	data[pos++] = (char) (v >> 8);
	data[pos++] = (char) v;
}
int byteOut::getSize() {
	return pos;
}
char *byteOut::getData() {
	return (char*) data;
}

// --- byteIn ---

	char *data;
	int length;
	int pos;

byteIn::byteIn(char *data, int length, int autoDeleteBuffer) {
	this->data = (unsigned char*) data;
	this->length = length;
	this->pos = 0;
	this->autoDeleteBuffer = autoDeleteBuffer;
}
byteIn::~byteIn() {
	if (autoDeleteBuffer && length)
		delete[] data;
}
void byteIn::ensure(int inc) {
	assert(pos + inc <= length);
}
int byteIn::left() {
	return length - pos;
}
char byteIn::readChar() {
	ensure(1);
	return data[pos++] & 0xff;
}
int byteIn::readShort() {
	ensure(2);
	int ret = data[pos] << 8 | data[pos + 1];
	pos += 2;
	return ret & 0xffff;
}
long byteIn::readLong() {
	ensure(4);
	int ret = (long)data[pos] << 24 | (long)data[pos + 1] << 16 | data[pos + 2] << 8 | data[pos + 3];
	pos += 4;
	return ret;
}
long byteIn::read3B() {
	ensure(3);
	long ret = (long)data[pos] << 16 | data[pos + 1] << 8 | data[pos + 2];
	pos += 3;
	return ret & 0xffffffL;
}
void byteIn::unwindChar() {
	pos--;
	assert(pos >= 0);
}
void byteIn::unwindShort() {
	pos -= 2;
	assert(pos >= 0);
}
void byteIn::unwind3B() {
	pos -= 3;
	assert(pos >= 0);
}
void byteIn::unwindLong() {
	pos -= 4;
	assert(pos >= 0);
}
