#include "crc32.h"

void crc32(char* buffer, short size, short& old_crc) {
	char c;
	char c1;
	char c2;
	for (short i = 0; i < size; i++) {
		c = buffer[i];
		for (short j = 0; j < 8; j++) {
			c1 = (c < 0)?1:0;
			c = c1 + (c << 1);
			c2 = (old_crc < 0)?1:0;
			old_crc = c1 + old_crc * 2;
			if (c2 != 0)
				old_crc = old_crc ^ 0x1021;
		}
	}
}

