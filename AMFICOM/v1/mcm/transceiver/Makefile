OUT := libmcmtransceiver.so

AKP := ../../../HW/akp
AKPA := $(AKP)/akp.a

TCPCONNECT := ../../../HW/tcpconnect
TCPCONNECTA := $(TCPCONNECT)/tcpconnect.a

CXX := g++#-3.3.1
CXXFLAGS := -O3 -W -Wall 

CPPFLAGS := -I$(AKP) -I$(TCPCONNECT) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

OBJS := tcpkisconnection.o


.PHONY:	clean

$(OUT):	$(OBJS) $(AKPA) $(TCPCONNECTA)
	$(CXX) $(LDFLAGS) -shared -o $(OUT) $(OBJS) $(AKPA) $(TCPCONNECTA)

#.cpp.o:
#	$(CXX) -c $(CFLAGS) -o $@ $<

$(AKPA):
	$(MAKE) -C $(AKP)

$(TCPCONNECTA):
	$(MAKE) -C $(TCPCONNECT)

clean:
	$(RM) $(OBJS) $(OUT)
