// AgentStub.cpp : Defines the entry point for the console application.
//

//#include "stdafx.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "ByteArray.h"
#include "Parameter.h"
#include "Segment.h"
#include "TestSegment.h"
#include "ResultSegment.h"
/*
HANDLE call(char* PipeName) {
	char* pipeName = (char*)calloc(128, 1);
	strcpy(pipeName, "\\\\.\\pipe\\");
	strcat(pipeName, PipeName);
	printf("Calling %s\n", pipeName);
	if (!WaitNamedPipe(pipeName, PIPEWAIT)) {
		free(pipeName);
		ShowError();
		return 0;
	}
	HANDLE hPipe = CreateFile(pipeName,
							  GENERIC_READ | GENERIC_WRITE,
							  0,
							  NULL,
							  OPEN_EXISTING,
							  FILE_ATTRIBUTE_NORMAL,
							  NULL);
	free(pipeName);
	if (hPipe == INVALID_HANDLE_VALUE) {
		ShowError();
		return 0;
	}
	return hPipe;
}

int write(HANDLE hPipe, char* data, long datalength) {
	if (datalength + 4 > MAXLENTASK) {
		printf("Illegal length of data: %d, must be <= %d\n", datalength, MAXLENTASK - 4);
		return 0;
	}
	char* blen = longtobytes(datalength);
	char* arr = (char*)calloc(MAXLENTASK, 1);
	int i;
	for (i = 0; i < 4; i++)
		arr[i] = blen[i];
	free(blen);
	for (i = 0; i < datalength; i++) 
		arr[i+4] = data[i];

	DWORD byteswritten;
	if (!WriteFile(hPipe, arr, MAXLENTASK, &byteswritten, NULL)) {
		free(arr);
		ShowError();
		return 0;
	}
	free(arr);
	
	return 1;
}

int close(HANDLE hPipe) {
	FlushFileBuffers(hPipe);
	CloseHandle(hPipe);
	return 1;
}
*/
int main(int argc, char* argv[]) {
	unsigned int i;

	ByteArray* test_id = new ByteArray(strlen("test1"), "test1");
	long64 start_time = 0;//1047922089728;
	ByteArray* test_type_id = new ByteArray(strlen("trace_and_analyse"), "trace_and_analyse");
	ByteArray* local_address = new ByteArray(strlen("ME-199058001202-20030205125200-00000"), "ME-199058001202-20030205125200-00000");
	int wvlen = 1550;
	double trclen = 70;
	double res = 4.0;
	long64 pulswd = 1;
	double ior = 1.467;
	double scans = 4096;

	Parameter* parameters[6];
	parameters[0] = new Parameter(new ByteArray(strlen("ref_wvlen"), "ref_wvlen"), new ByteArray(sizeof(int), (char*)&wvlen));
	parameters[1] = new Parameter(new ByteArray(strlen("ref_trclen"), "ref_trclen"), new ByteArray(sizeof(double), (char*)&trclen));
	parameters[2] = new Parameter(new ByteArray(strlen("ref_res"), "ref_res"), new ByteArray(sizeof(double), (char*)&res));
	parameters[3] = new Parameter(new ByteArray(strlen("ref_pulswd"), "ref_pulswd"), new ByteArray(sizeof(long64), (char*)&pulswd));
	parameters[4] = new Parameter(new ByteArray(strlen("ref_ior"), "ref_ior"), new ByteArray(sizeof(double), (char*)&ior));
	parameters[5] = new Parameter(new ByteArray(strlen("ref_scans"), "ref_scans"), new ByteArray(sizeof(double), (char*)&scans));

	printf("INTSIZE: %d\n", INTSIZE);
	printf("test_id.length: %d\n", test_id->getLength());
	printf("long64: %d\n", sizeof(long64));
	printf("test_type_id.length: %d\n", test_type_id->getLength());
	printf("local_address.length: %d\n", local_address->getLength());

	TestSegment* testSegment = new TestSegment(test_id, start_time, test_type_id, local_address, 6, parameters);

	unsigned int testlength = testSegment->getLength();
	printf("testlength == %d\n", testlength);
	char* testdata = testSegment->getData();
	for (i = 0; i < testlength; i++)
		printf("testdata[%d] == %d\n", i, testdata[i]);

	char* testdata1 = (char*)calloc(testlength, 1);
	for (i = 0; i < testlength; i++)
		testdata1[i] = testdata[i];
	TestSegment* testSegment1 = new TestSegment(testlength, testdata1);
	printf("test_id: %s\n", testSegment1->getTestId()->getData());
	printf("start_time: %13d\n", testSegment1->getStartTime());
	printf("test_type_id: %s\n", testSegment1->getTestTypeId()->getData());
	printf("local_address: %s\n", testSegment1->getLocalAddress()->getData());
	Parameter** pars = testSegment1->getParameters();
	printf("par[%d].name == %s, par[%d].value == %d\n", 0, pars[0]->getName()->getData(), 0, *(int*)(pars[0]->getValue()->getData()));
	printf("par[%d].name == %s, par[%d].value == %f\n", 1, pars[1]->getName()->getData(), 1, *(double*)(pars[1]->getValue()->getData()));
	printf("par[%d].name == %s, par[%d].value == %f\n", 2, pars[2]->getName()->getData(), 2, *(double*)(pars[2]->getValue()->getData()));
	printf("par[%d].name == %s, par[%d].value == %5d\n", 3, pars[3]->getName()->getData(), 3, *(long64*)(pars[3]->getValue()->getData()));
	printf("par[%d].name == %s, par[%d].value == %f\n", 4, pars[4]->getName()->getData(), 4, *(double*)(pars[4]->getValue()->getData()));
	printf("par[%d].name == %s, par[%d].value == %f\n", 5, pars[5]->getName()->getData(), 5, *(double*)(pars[5]->getValue()->getData()));
//----------

//----------

	delete testSegment;
	delete testSegment1;

	test_id = new ByteArray(strlen("test1"), "test1");
	parameters[0] = new Parameter(new ByteArray(strlen("ref_wvlen"), "ref_wvlen"), new ByteArray(sizeof(int), (char*)&wvlen));
	parameters[1] = new Parameter(new ByteArray(strlen("ref_trclen"), "ref_trclen"), new ByteArray(sizeof(double), (char*)&trclen));
	parameters[2] = new Parameter(new ByteArray(strlen("ref_res"), "ref_res"), new ByteArray(sizeof(double), (char*)&res));
	parameters[3] = new Parameter(new ByteArray(strlen("ref_pulswd"), "ref_pulswd"), new ByteArray(sizeof(long64), (char*)&pulswd));
	parameters[4] = new Parameter(new ByteArray(strlen("ref_ior"), "ref_ior"), new ByteArray(sizeof(double), (char*)&ior));
	parameters[5] = new Parameter(new ByteArray(strlen("ref_scans"), "ref_scans"), new ByteArray(sizeof(double), (char*)&scans));

	ResultSegment* resultSegment = new ResultSegment(test_id, start_time, 6, parameters);
	unsigned int resultlength = resultSegment->getLength();;
	printf("resultlength == %d\n", resultlength);
	char* resultdata = resultSegment->getData();
	char* resultdata1 = (char*)calloc(resultlength , 1);
	for (i = 0; i < resultlength ; i++)
		resultdata1[i] = resultdata[i];
	ResultSegment* resultSegment1 = new ResultSegment(resultlength, resultdata1);
	printf("test_id: %s\n", resultSegment1->getTestId()->getData());
	printf("start_time: %I64d\n", resultSegment1->getStartTime());
	pars = resultSegment1->getParameters();
	printf("par[%d].name == %s, par[%d].value == %d\n", 0, pars[0]->getName()->getData(), 0, *(int*)(pars[0]->getValue()->getData()));
	printf("par[%d].name == %s, par[%d].value == %f\n", 1, pars[1]->getName()->getData(), 1, *(double*)(pars[1]->getValue()->getData()));
	printf("par[%d].name == %s, par[%d].value == %f\n", 2, pars[2]->getName()->getData(), 2, *(double*)(pars[2]->getValue()->getData()));
	printf("par[%d].name == %s, par[%d].value == %5d\n", 3, pars[3]->getName()->getData(), 3, *(long64*)(pars[3]->getValue()->getData()));
	printf("par[%d].name == %s, par[%d].value == %f\n", 4, pars[4]->getName()->getData(), 4, *(double*)(pars[4]->getValue()->getData()));
	printf("par[%d].name == %s, par[%d].value == %f\n", 5, pars[5]->getName()->getData(), 5, *(double*)(pars[5]->getValue()->getData()));
	
	delete resultSegment;
	delete resultSegment1;

	return 0;
}
/*
void ShowError() {
	LPVOID lpMsgBuf;
	FormatMessage(FORMAT_MESSAGE_ALLOCATE_BUFFER |
					FORMAT_MESSAGE_FROM_SYSTEM |
					FORMAT_MESSAGE_IGNORE_INSERTS,
				  NULL,
				  GetLastError(),
				  MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
				  (LPTSTR) &lpMsgBuf,
				  0,
				  NULL);
	printf((LPTSTR)lpMsgBuf);
	LocalFree(lpMsgBuf);
}

char* longtobytes(long l) {
	char* b = (char*)calloc(4, 1);
	b[3] = l/(256*256*256);
	b[2] = (l - b[3]*256*256*256)/(256*256);
	b[1] = (l - b[3]*256*256*256 - b[2]*256*256)/256;
	b[0] = l - b[3]*256*256*256 - b[2]*256*256 - b[1]*256;
	return b;
}

long getstringlength(const char* str) {
	return (long)strlen(str);
}*/
