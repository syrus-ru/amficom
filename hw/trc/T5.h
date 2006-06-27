#ifndef T5_H
#define T5_H

#include "NTTraceData.h"
#include "DCL.h"

//sub_1000B5D0
short decompress_T5(const char* read_file_buffer, unsigned short bytes_read, NTTraceData* trace_data);

//sub_1000C9F0
U16 read_data(U8* buffer, U16 size, void* param);

//sub_1000CAA0
void write_data(U8* buffer, U16 size, void* param);

#endif
