#include "IntelDataOutputStream.h"
#include <string.h>

IntelDataOutputStream::IntelDataOutputStream(unsigned int size) {
	this->size = size;
	this->data = new unsigned char[this->size];
	this->offset = 0;
}

IntelDataOutputStream::~IntelDataOutputStream() {
	delete[] this->data;
}

int IntelDataOutputStream::write_char(char c) {
	if (this->offset <= this->size - sizeof(char)) {
		*(char*)(this->data + this->offset) = c;
		this->offset += sizeof(char);
		return 1;
	}
	else
		return 0;
}

int IntelDataOutputStream::write_unsigned_char(unsigned char c) {
	if (this->offset <= this->size - sizeof(unsigned char)) {
		*(this->data + this->offset) = c;
		this->offset += sizeof(unsigned char);
		return 1;
	}
	else
		return 0;
}

int IntelDataOutputStream::write_short(short s) {
	if (this->offset <= this->size - sizeof(short)) {
		*(short*)(this->data + this->offset) = s;
		this->offset += sizeof(short);
		return 1;
	}
	else
		return 0;
}

int IntelDataOutputStream::write_unsigned_short(unsigned short s) {
	if (this->offset <= this->size - sizeof(unsigned short)) {
		*(unsigned short*)(this->data + this->offset) = s;
		this->offset += sizeof(unsigned short);
		return 1;
	}
	else
		return 0;
}

int IntelDataOutputStream::write_int(int i) {
	if (this->offset <= this->size - sizeof(int)) {
		*(int*)(this->data + this->offset) = i;
		this->offset += sizeof(int);
		return 1;
	}
	else
		return 0;
}

int IntelDataOutputStream::write_unsigned_int(unsigned int i) {
	if (this->offset <= this->size - sizeof(unsigned int)) {
		*(unsigned int*)(this->data + this->offset) = i;
		this->offset += sizeof(unsigned int);
		return 1;
	}
	else
		return 0;
}

int IntelDataOutputStream::write_string(const char* str) {
	unsigned int str_len = strlen(str);
	if (this->offset <= this->size - str_len - 1) {
		for (unsigned int i = 0; i < str_len; i++)
			*(char*)(this->data + this->offset + i) = str[i];
		this->data[this->offset + str_len] = 0x00;
		this->offset += (str_len + 1);
		return 1;
	}
	else
		return 0;
}

unsigned char* IntelDataOutputStream::get_data() const {
	unsigned char* ret = new unsigned char[this->offset];
	for (unsigned int i = 0; i < this->offset; i++)
		ret[i] = this->data[i];
	return ret;
}

unsigned int IntelDataOutputStream::get_real_size() const {
	return this->offset;
}
