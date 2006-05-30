MODULE := trc
CXX_SOURCES := T5 \
	crc32 \
	util \
	DCL \
	DCLTable \
	BellcoreStructure \
	BellcoreWriter \
	IntelDataOutputStream
USE_JNI := 1

include ../tools/Makefile.cpp.inc.mk

TraceReader$(OBJ_SUFFIX):			NTTraceData.h TraceReader.h T5.h util.h
T5$(OBJ_SUFFIX):					NTTraceData.h DCL.h T5.h crc32.h util.h
crc32$(OBJ_SUFFIX):				crc32.h
util$(OBJ_SUFFIX):					util.h
DCL$(OBJ_SUFFIX):					DCL.h
DCLTable$(OBJ_SUFFIX):				DCL.h
BellcoreStructure$(OBJ_SUFFIX):		BellcoreStructure.h
BellcoreWriter$(OBJ_SUFFIX):			BellcoreStructure.h IntelDataOutputStream.h BellcoreWriter.h
IntelDataOutputStream$(OBJ_SUFFIX):	IntelDataOutputStream.h

.PHONY:	all
all:	$(OUT_LIBAR)
