VERSION=2.0
OUT=libdadara.so
CC=g++-3.3.1
CFLAGS=-O -Wall #-Wno-unused-variable
LFLAGS=-shared -O -L$(shell root-config --libdir) -lCore -lCint -ldl -rdynamic
OBJS=dadara.o \
	EventParams.o \
	Histo1d.o \
	Dispatcher.o \
	InitialAnalysis.o \
	NeuroAnalyser.o \
	NeuroNet.o Neuron.o \
	Vector.o \
	TFitter.o \
	TMinuit.o
ARCHFILE=dadara.tar.gz

$(OUT):	$(OBJS)
	$(CC) -o $(OUT) $(OBJS) $(LFLAGS)

dadara.o:	dadara.h com_syrus_AMFICOM_analysis_dadara_DadaraAnalysisManager.h EventParams.h InitialAnalysis.h TFitter.h dadara.cpp
	$(CC) -c $(CFLAGS) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux $(shell root-config --cflags) dadara.cpp

EventParams.o:	EventParams.h EventParams.cpp
	$(CC) -c $(CFLAGS) EventParams.cpp

Histo1d.o:	Histo1d.h Histo1d.cpp
	$(CC) -c $(CFLAGS) Histo1d.cpp

Dispatcher.o:	Dispatcher.h Neuron.h Dispatcher.cpp
	$(CC) -c $(CFLAGS) Dispatcher.cpp

InitialAnalysis.o:	InitialAnalysis.h NeuroAnalyser.h Histo1d.h InitialAnalysis.cpp
	$(CC) -c $(CFLAGS) InitialAnalysis.cpp

NeuroAnalyser.o:	NeuroAnalyser.h NeuroAnalyser.cpp
	$(CC) -c $(CFLAGS) NeuroAnalyser.cpp

NeuroNet.o:	NeuroNet.h NeuroNet.cpp
	$(CC) -c $(CFLAGS) NeuroNet.cpp

Neuron.o:	Neuron.h Neuron.cpp
	$(CC) -c $(CFLAGS) Neuron.cpp

Vector.o:	Vector.h Vector.cpp
	$(CC) -c $(CFLAGS) Vector.cpp

TFitter.o:
	$(CC) -c $(CFLAGS) $(shell root-config --cflags) TFitter.cxx

TMinuit.o:
	$(CC) -c $(CFLAGS) $(shell root-config --cflags) TMinuit.cxx


.PHONY: clean arch now

clean:
	rm -f $(OBJS) $(OUT)

arch:
	tar -czvf $(ARCHFILE) *.cpp *.cxx *.h Makefile weights.wht architecture.net

now:
	$(CC) $(shell root-config --cflags) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -L$(ROOTSYS)/lib -lCint -lCore -ldl -rdynamic dadara.cpp ConnectorParams.cpp EventParams.cpp HelpingFunctions.cpp Histo1d.cpp LinearParams.cpp initWaveletAnal.cpp WeldParams.cpp TFitter.cxx TMinuit.cxx -o dadara.bin
