OUT=t4
AKP=../akp
AKPA=$(AKP)/akp.a
TRC=../trc
TRCA=$(TRC)/tracedata.a
CPP=g++#-3.4.1
CFLAGS=-Wall -W -g -ggdb
OBJS=t4.o \
	TransceiverManager.o \
	AgentTransceiver.o \
	RTUTransceiver.o
ARCHFILE=t4.tar.gz

t4:	$(OBJS) $(AKPA) $(TRCA)
	$(CPP) $(CFLAGS) -o $(OUT) $(OBJS) $(AKPA) $(TRCA) -lpthread

.cpp.o:
	$(CPP) -c -I$(AKP) -I$(TRC) $(CFLAGS) -o $@ $<

$(AKPA):
	make -C $(AKP)

$(TRCA):
	make -C $(TRC)

.PHONY:	clean arch

clean:
	rm -f $(OUT) $(OBJS)

arch:
	tar -czvf t4.tar.gz *.cpp *.h Makefile
