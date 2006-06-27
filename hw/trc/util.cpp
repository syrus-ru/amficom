#include "util.h"

void read_char(char& ret, char* buffer, unsigned short& offset) {
	ret = buffer[offset];
	offset = offset + 1;
}

void read_short(short& ret, char* buffer, unsigned short& offset) {
	ret = ((unsigned char)buffer[offset + 1]<<8) +
		(unsigned char)buffer[offset];
	offset = offset + 2;
}

void read_int(int& ret, char* buffer, unsigned short& offset) {
	ret = ((unsigned char)buffer[offset+3]<<24) +
		((unsigned char)buffer[offset+2]<<16) +
		((unsigned char)buffer[offset+1]<<8) +
		(unsigned char)buffer[offset];
	offset = offset + 4;
}

