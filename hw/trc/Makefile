OUT := tracedata.a
OUTSO := libtracedata.so

CXX := g++#-3.4.1
CXXFLAGS := -W -Wall

CC := gcc#-3.4.1
CFLAGS := -W -Wall

CPPFLAGS := -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

AR := ar
ARFLAGS := rcsv

#OBJS=TraceReader.o \
	T5.o \
	crc32.o \
	util.o \
	DCL.o \
	DCLTable.o \
	BellcoreStructure.o \
	BellcoreWriter.o \
	IntelDataOutputStream.o
OBJS := $(patsubst %.cpp,%.o,$(wildcard *.cpp))

ARCHFILE := trc.tar.gz

$(OUT):		$(OBJS)
	$(AR) $(ARFLAGS) $(OUT) $(OBJS)

$(OUTSO):	$(OBJS)
	$(CPP) $(CFLAGS) -shared -o $(OUTSO) $(OBJS)

TraceReader.o:	NTTraceData.h TraceReader.h T5.h util.h
T5.o:		NTTraceData.h DCL.h T5.h crc32.h util.h
crc32.o:	crc32.h
util.o:		util.h
DCL.o:		DCL.h
DCLTable.o:	DCL.h
BellcoreStructure.o:		BellcoreStructure.h
BellcoreWriter.o:		BellcoreStructure.h IntelDataOutputStream.h BellcoreWriter.h
IntelDataOutputStream.o:	IntelDataOutputStream.h

#.cpp.o:
#	$(CPP) -c $(CFLAGS) -o $@ $<

#.c.o:
#	$(CPP) -c $(CFLAGS) -o $@ $<

.PHONY:	clean arch

clean:
	rm -f $(OBJS) $(OUT) $(OUTSO)

arch:
	tar -czvf $(ARCHFILE) *.cpp *.h Makefile
