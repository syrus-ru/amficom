VERSION=2.0
OUT=libdadara.so
OUTR=libdadarar.so
CC=g++#-3.3.1
CFLAGS=-O -Wall #-Wno-unused-variable
LFLAGS=-shared -O -L$(ROOTSYS)/lib -lCore -lCint -ldl -rdynamic
OBJS=dadara.o \
	EventParams.o \
	Fitter.o \
	Histogramm.o \
	Dispatcher.o \
	InitialAnalysis.o \
	NeuroAnalyser.o \
	NeuroNet.o \
	Neuron.o \
	Vector.o \
	MathRef.o \
	TFitter.o \
	TMinuit.o

$(OUT):	$(OBJS)
	$(CC) -o $(OUT) $(OBJS) $(LFLAGS)

dadara.o:
	$(CC) -c -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux $(shell root-config --cflags) $(CFLAGS) -o $@ dadara.cpp

.cpp.o:
	$(CC) -c $(CFLAGS) -o $@ $<

TFitter.o:
	$(CC) -c $(CFLAGS) $(shell root-config --cflags) TFitter.cxx

TMinuit.o:
	$(CC) -c $(CFLAGS) $(shell root-config --cflags) TMinuit.cxx


.PHONY: clean arch

clean:
	rm -f $(OBJS) $(OUT)

arch:
	tar -czvf dadara.tar.gz *.cpp *.cxx *.h Makefile weights.wht architecture.net

