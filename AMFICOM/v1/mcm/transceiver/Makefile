#LDLIBS=/usr/lib/libc.a
OUT=libmcmtransceiver.so
#CPP=g++#-3.3.1
CFLAGS=-O0 -g -w -I$(AKP) -I$(TCPCONNECT) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
CXXFLAGS=-O0 -g -w -I$(AKP) -I$(TCPCONNECT) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

AKP=../../akp
AKPA=$(AKP)/akp.a

TCPCONNECT=tcpconnect
TCPCONNECTA=$(TCPCONNECT)/tcpconnect.a

OBJS=mcmtransceiver.o tcpserver.o

$(OUT):	$(OBJS) $(AKPA) $(TCPCONNECTA)
	$(CXX) $(LDFLAGS) -shared -o $(OUT) $(OBJS) $(AKPA) $(TCPCONNECTA) $(LDLIBS)

$(AKPA):
	$(MAKE) -C $(AKP)

$(TCPCONNECTA):
	$(MAKE) -C $(TCPCONNECT)

.PHONY:	clean

clean:
	$(RM) $(OBJS) $(OUT)
	$(MAKE) -C $(TCPCONNECT) $@
