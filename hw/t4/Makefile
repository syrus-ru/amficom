OUT := t4

AKP := ../akp
AKPA := $(AKP)/akp.a
TRC := ../trc
TRCA := $(TRC)/tracedata.a

CXX := g++#-3.4.1
CXXFLAGS := -O3 -Wall -W

CPPFLAGS := -I$(AKP) -I$(TRC)

#OBJS=t4.o \
	TransceiverManager.o \
	AgentTransceiver.o \
	RTUTransceiver.o
OBJS := $(patsubst %.cpp,%.o,$(wildcard *.cpp))

ARCHFILE := t4.tar.gz



t4:	$(OBJS) $(AKPA) $(TRCA)
	$(CXX) $(CXXFLAGS) -o $(OUT) $(OBJS) $(AKPA) $(TRCA) -lpthread

#.cpp.o:
#	$(CPP) -c -I$(AKP) -I$(TRC) $(CFLAGS) -o $@ $<

$(AKPA):
	make -C $(AKP)

$(TRCA):
	make -C $(TRC)

.PHONY:	clean arch

clean:
	rm -f $(OUT) $(OBJS)

arch:
	tar -czvf t4.tar.gz *.cpp *.h Makefile
