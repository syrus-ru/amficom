OUT=libtcpkisconnection.so

CPP=g++#-3.3.1
CFLAGS=-O0 -g -W -Wall -I$(AKP) -I$(TCPCONNECT) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

AKP=../../../HW/akp
AKPA=$(AKP)/akp.a

TCPCONNECT=../../../HW/tcpconnect
TCPCONNECTA=$(TCPCONNECT)/tcpconnect.a

OBJS=tcpkisconnection.o transceiver.o


.PHONY:	clean

$(OUT):	$(OBJS) $(AKPA) $(TCPCONNECTA)
	$(CPP) $(LDFLAGS) -shared -o $(OUT) $(OBJS) $(AKPA) $(TCPCONNECTA)

.cpp.o:
	$(CPP) -c $(CFLAGS) -o $@ $<

$(AKPA):
	$(MAKE) -C $(AKP)

$(TCPCONNECTA):
	$(MAKE) -C $(TCPCONNECT)

clean:
	$(RM) $(OBJS) $(OUT)
