#ifndef TRACEREADER_H
#define TRACEREADER_H

#include "NTTraceData.h"
#include "BellcoreStructure.h"

int get_bellcore_data(const char* compdata, const unsigned short compdata_size, unsigned char*& bellcoredata, unsigned int& bellcoredata_size);

int get_bellcore_data(const char* file_name, unsigned char*& bellcoredata, unsigned int& bellcoredata_size);

int fill_bellcore_structure(const char* compdata, const unsigned short compdata_size, BellcoreStructure* bs);

int fill_bellcore_structure(const char* file_name, BellcoreStructure* bs);


void bellcore_from_nttrace(const NTTraceData* nttrace_data, BellcoreStructure* bs);

int file_is_trc(const char* fileName);

short stdecomp(const char* compdata, const unsigned short compdata_size, NTTraceData* nttrace_data);

short ftdecomp(const char* file_name, NTTraceData* nttrace_data);

long get_file_size(FILE* fp);

unsigned short get_number_of_points(NTTraceData* nttrace_data);

void set_misc_data(NTTraceData* nttrace_data);

void set_misc_data(const char* file_name, NTTraceData* nttrace_data);

short get_40114(NTTraceData* nttrace_data, float f);

//-----------------
void print_nttrace_data(NTTraceData* trace_data);
void print_nttrace_data_sub(NTTraceData_Sub* trace_data_sub, void* fpv);
//-----------------
#endif

