#include <stdlib.h>
#include <string.h>
#include "T5.h"
#include "crc32.h"
#include "util.h"

short crc;	//word_10058544
char* buffer_to_read = NULL;	//dword_10058534, read_data reads from here
char* buffer_to_write = NULL;	//dword_10058538, write_data writes here
unsigned short total_bytes_written;	//dword_1005853C, counter of bytes, that have been written
unsigned short remaining_bytes_to_read;	//dword_10058540, remaining bytes to read

short decompress_T5(const char* read_file_buffer, unsigned short bytes_read, NTTraceData* trace_data) {
	short ret = 0;
	int zero = 0;
	unsigned char* work_buff = (unsigned char*)malloc(26000);
	if (work_buff == NULL)
		return 350;

	char* cursor = (char*)(read_file_buffer + strlen("<T5>"));
	unsigned short offset1 = 0;
	int size;
	read_int(size, cursor, offset1);
	cursor += 4;
	crc = 0;
	crc32(cursor, size, crc);
	crc32((char*)&zero, 2, crc);
	offset1 = 0;
	short crc_check;
	read_short(crc_check, cursor + size, offset1);
	if (crc != crc_check) {
		free(work_buff);
		trace_data->_40124 = NULL;
		return 315;
	}

	trace_data->_40124 = (char*)malloc(15552);
	if (trace_data->_40124 == NULL) {
		free(work_buff);
		return 350;
	}
	buffer_to_read = cursor;
	buffer_to_write = trace_data->_40124;
	remaining_bytes_to_read = size;
	total_bytes_written = 0;
	if (explode(read_data, write_data, work_buff, NULL) != 0) {
		free(work_buff);
		free(trace_data->_40124);
		trace_data->_40124 = NULL;
		return 317;
	}
	trace_data->_40122 = (unsigned short)(total_bytes_written / 54);
	cursor += (size + 2);
	offset1 = 0;
	read_int((int&)remaining_bytes_to_read, cursor, offset1);
	if (strlen("<T5>") + size + 12 + remaining_bytes_to_read != bytes_read) {
		free(work_buff);
		free(trace_data->_40124);
		trace_data->_40124 = NULL;
		return 318;
	}
	size = remaining_bytes_to_read;
	cursor += 4;
	crc = 0;
	crc32(cursor, size, crc);
	crc32((char*)&zero, 2, crc);
	offset1 = 0;
	read_short(crc_check, cursor + size, offset1);
	if (crc != crc_check) {
		free(work_buff);
		trace_data->_40124 = NULL;
		return 315;
	}
	buffer_to_read = cursor;
	char reflectogramma_buffer[40172];
	buffer_to_write = reflectogramma_buffer;
	total_bytes_written = 0;
	if (explode(read_data, write_data, work_buff, NULL) != 0) {
		free(work_buff);
		free(trace_data->_40124);
		trace_data->_40124 = NULL;
		return 317;
	}

	unsigned short offset2 = 0;
	read_short(trace_data->_00000, reflectogramma_buffer, offset2);
	if (trace_data->_00000 != 5) {
		free(work_buff);
		free(trace_data->_40124);
		trace_data->_40124 = NULL;
		return 319;
	}
	read_int(trace_data->_00002, reflectogramma_buffer, offset2);
	read_short(trace_data->_00006, reflectogramma_buffer, offset2);
	read_short(trace_data->_00008, reflectogramma_buffer, offset2);
	read_short(trace_data->_00010, reflectogramma_buffer, offset2);
	read_short((short&)trace_data->_00012, reflectogramma_buffer, offset2);
	read_short(trace_data->_00014, reflectogramma_buffer, offset2);
	read_short(trace_data->_00016, reflectogramma_buffer, offset2);
	read_short(trace_data->_00018, reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 4; offset1++) {
		read_short((short&)trace_data->_00020[offset1*3], reflectogramma_buffer, offset2);
		read_short((short&)trace_data->_00020[offset1*3 + 1], reflectogramma_buffer, offset2);
		read_short((short&)trace_data->_00020[offset1*3 + 2], reflectogramma_buffer, offset2);
	}
	read_int(trace_data->_00044, reflectogramma_buffer, offset2);
	read_short(trace_data->_00048, reflectogramma_buffer, offset2);
	read_short(trace_data->_00050, reflectogramma_buffer, offset2);
	read_short(trace_data->_00052, reflectogramma_buffer, offset2);
	read_short(trace_data->_00054, reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 4; offset1++) {
		read_short(trace_data->_00056[offset1*3], reflectogramma_buffer, offset2);
		read_short(trace_data->_00056[offset1*3 + 1], reflectogramma_buffer, offset2);
		read_short(trace_data->_00056[offset1*3 + 2], reflectogramma_buffer, offset2);
	}
	read_int(trace_data->_00080, reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 10; offset1++)
		read_char(trace_data->_00084[offset1], reflectogramma_buffer, offset2);
	read_short(trace_data->_00094, reflectogramma_buffer, offset2);
	read_short(trace_data->_00096, reflectogramma_buffer, offset2);
	read_short(trace_data->_00098, reflectogramma_buffer, offset2);
	read_short(trace_data->_00100, reflectogramma_buffer, offset2);
	read_short(trace_data->_00102, reflectogramma_buffer, offset2);
	read_int(trace_data->_00104, reflectogramma_buffer, offset2);
	read_int(trace_data->_00108, reflectogramma_buffer, offset2);
	read_char(trace_data->_00112, reflectogramma_buffer, offset2);
	read_char(trace_data->_00113, reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 3; offset1++)
		read_short(trace_data->_00114[offset1], reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 3; offset1++)
		read_short(trace_data->_00120[offset1], reflectogramma_buffer, offset2);
	read_short(trace_data->_00126, reflectogramma_buffer, offset2);
	read_int(trace_data->_00128, reflectogramma_buffer, offset2);
	read_int(trace_data->_00132, reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 2; offset1++)
		read_int(trace_data->_00136[offset1], reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 2; offset1++)
		read_int(trace_data->_00144[offset1], reflectogramma_buffer, offset2);
	unsigned short k;
	for (k = 0; k < 2; k++)
		for (offset1 = 0; offset1 < 3; offset1++)
			read_int(trace_data->_00152[k*3][offset1], reflectogramma_buffer, offset2);
	for (k = 0; k < 2; k++)
		for (offset1 = 0; offset1 < 3; offset1++)
			read_int(trace_data->_00176[k*3][offset1], reflectogramma_buffer, offset2);
	read_short(trace_data->_00200, reflectogramma_buffer, offset2);
	read_short(trace_data->_00202, reflectogramma_buffer, offset2);
	read_short(trace_data->_00204, reflectogramma_buffer, offset2);
	read_short(trace_data->_00206, reflectogramma_buffer, offset2);
	read_short(trace_data->_00208, reflectogramma_buffer, offset2);
	read_int(trace_data->_00210, reflectogramma_buffer, offset2);
	read_int(trace_data->_00214, reflectogramma_buffer, offset2);
	read_short((short&)trace_data->_00218, reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 4; offset1++)
		read_char(trace_data->_00220[offset1], reflectogramma_buffer, offset2);
	read_short(trace_data->_00224, reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 8; offset1++)
		read_char(trace_data->_00226[offset1], reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 9; offset1++)
		read_short(trace_data->_00234[offset1 + 9], reflectogramma_buffer, offset2);	//+9 - ?
	read_short(trace_data->_00252, reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 64; offset1++) {
		read_short(trace_data->_00254[offset1]._000, reflectogramma_buffer, offset2);
		read_short(trace_data->_00254[offset1]._002, reflectogramma_buffer, offset2);
		read_short(trace_data->_00254[offset1]._004, reflectogramma_buffer, offset2);
		read_int(trace_data->_00254[offset1]._006, reflectogramma_buffer, offset2);
		read_int(trace_data->_00254[offset1]._010, reflectogramma_buffer, offset2);
		read_short(trace_data->_00254[offset1]._014, reflectogramma_buffer, offset2);
		read_short(trace_data->_00254[offset1]._016, reflectogramma_buffer, offset2);
		read_short(trace_data->_00254[offset1]._018, reflectogramma_buffer, offset2);
		read_int(trace_data->_00254[offset1]._020, reflectogramma_buffer, offset2);
		read_int(trace_data->_00254[offset1]._024, reflectogramma_buffer, offset2);
		read_short(trace_data->_00254[offset1]._028, reflectogramma_buffer, offset2);
		read_int(trace_data->_00254[offset1]._030, reflectogramma_buffer, offset2);
		read_int(trace_data->_00254[offset1]._034, reflectogramma_buffer, offset2);
		read_short(trace_data->_00254[offset1]._038, reflectogramma_buffer, offset2);
		read_short(trace_data->_00254[offset1]._040, reflectogramma_buffer, offset2);
		read_short(trace_data->_00254[offset1]._042, reflectogramma_buffer, offset2);
		read_int(trace_data->_00254[offset1]._044, reflectogramma_buffer, offset2);
		for (k = 0; k < 62; k++)
			read_char(trace_data->_00254[offset1]._048[k], reflectogramma_buffer, offset2);
	}
	read_short((short&)trace_data->_07294, reflectogramma_buffer, offset2);
	for (offset1 = 0; offset1 < 16384; offset1++)
		read_short(trace_data->_07296[offset1], reflectogramma_buffer, offset2);

	if (trace_data->_00000 == 5) {
		if (trace_data->_00252 != 0)
			trace_data->_00224 = trace_data->_00224 | 0x0001;
		if (trace_data->_00056[2] != 0)
			trace_data->_00224 = trace_data->_00224 | 0x0002;

		if (trace_data->_00054 >> 8 == 1)
			trace_data->_00224 = trace_data->_00224 | 0x0010;
		else
			if (trace_data->_00054 >> 8 == 2)
				trace_data->_00224 = trace_data->_00224 | 0x0020;
	}
	else
		ret = 319;

	free(work_buff);
	if (ret != 0) {
		free(trace_data->_40124);
		trace_data->_40124 = NULL;
	}
	else
		trace_data->_40124 = (char*)realloc(trace_data->_40124, trace_data->_40122 * 54);

	return ret;
}

U16 read_data(U8* buffer, U16 size, void* param) {
	unsigned short i;
	if (size < remaining_bytes_to_read)
		i = size;
	else 
		i = remaining_bytes_to_read;
	unsigned short ret = i;
	remaining_bytes_to_read = remaining_bytes_to_read - ret;
	if (ret != 0) {
		memcpy(buffer, buffer_to_read, ret);
		buffer_to_read = buffer_to_read + ret;
	}
	return ret;
}

void write_data(U8* buffer, U16 size, void* param) {
	memcpy(buffer_to_write, buffer, size);
	buffer_to_write = buffer_to_write + size;
	total_bytes_written = total_bytes_written + size;
}

