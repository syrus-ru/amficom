OUT=libmcmtransceiver.so
CPP=g++#-3.3.1
CFLAGS=-Wall -W

AKP=akp
AKPA=$(AKP)/akp.a

TCPCONNECT=tcpconnect
TCPCONNECTA=$(TCPCONNECT)/tcpconnect.a

OBJS=mcmtransceiver.o tcpserver.o

$(OUT):	$(OBJS) $(AKPA) $(TCPCONNECTA)
	$(CPP) $(CFLAGS) -shared -o $(OUT) $(OBJS) $(AKPA) $(TCPCONNECTA)

.cpp.o:
	$(CPP) -c -I$(AKP) -I$(TCPCONNECT) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux $(CFLAGS) -o $@ $<

$(AKPA):
	make -C $(AKP)

$(TCPCONNECTA):
	make -C $(TCPCONNECT)

.PHONY:	clean

clean:
	rm -f $(OBJS) $(OUT)

