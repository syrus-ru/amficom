VERSION=2.0
OUT=libdadara.so
OUTR=libdadarar.so
CC=g++#-3.3.1
CFLAGS=-O -Wall #-Wno-unused-variable
LFLAGS=-shared -O -L$(ROOTSYS)/lib -lCore -lCint -ldl -rdynamic
#OBJS=dadara.o EventParams.o Histogramm.o Dispatcher.o InitialAnalysis.o NeuroAnalyser.o NeuroNet.o Neuron.o Vector.o MathRef.o TFitter.o TMinuit.o
OBJS=dadara.o EventParams.o Histo1d.o Dispatcher.o InitialAnalysis.o NeuroAnalyser.o NeuroNet.o Neuron.o Vector.o TFitter.o TMinuit.o

$(OUT):	$(OBJS)
	$(CC) -o $(OUT) $(OBJS) $(LFLAGS)

#com_syrus_AMFICOM_analysis_dadara_AnalysResult.h
#com_syrus_AMFICOM_analysis_dadara_DadaraAnalysisManager.h
dadara.o:	dadara.h com_syrus_AMFICOM_analysis_dadara_DadaraAnalysisManager.h EventParams.h NeuroAnalyser.h Histo1d.h InitialAnalysis.h NeuroNet.h Vector.h Dispatcher.h Neuron.h TFitter.h dadara.cpp
	$(CC) -c $(CFLAGS) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux $(shell root-config --cflags) dadara.cpp

EventParams.o:	EventParams.h EventParams.cpp
	$(CC) -c $(CFLAGS) EventParams.cpp

Histo1d.o:	Histo1d.h Histo1d.cpp
	$(CC) -c $(CFLAGS) Histo1d.cpp

Dispatcher.o:	Dispatcher.h Neuron.h Vector.h Dispatcher.cpp
	$(CC) -c $(CFLAGS) Dispatcher.cpp

InitialAnalysis.o:	InitialAnalysis.h EventParams.h NeuroAnalyser.h Histo1d.h InitialAnalysis.cpp
	$(CC) -c $(CFLAGS) InitialAnalysis.cpp

NeuroAnalyser.o:	NeuroAnalyser.h NeuroNet.h Vector.h Dispatcher.h Neuron.h NeuroAnalyser.cpp
	$(CC) -c $(CFLAGS) NeuroAnalyser.cpp

NeuroNet.o:	NeuroNet.h Vector.h Dispatcher.h Neuron.h NeuroNet.cpp
	$(CC) -c $(CFLAGS) NeuroNet.cpp

Neuron.o:	Neuron.h Vector.h Dispatcher.h Neuron.cpp
	$(CC) -c $(CFLAGS) Neuron.cpp

Vector.o:	Vector.h Vector.cpp
	$(CC) -c $(CFLAGS) Vector.cpp

#MathRef.o:	MathRef.h MathRef.cpp
#	$(CC) -c $(CFLAGS) MathRef.cpp

TFitter.o:
	$(CC) -c $(CFLAGS) $(shell root-config --cflags) TFitter.cxx

TMinuit.o:
	$(CC) -c $(CFLAGS) $(shell root-config --cflags) TMinuit.cxx


.PHONY: clean arch

clean:
	rm -f $(OBJS) $(OUT)

arch:
	tar -czvf dadara.tar.gz *.cpp *.cxx *.h Makefile weights.wht architecture.net

