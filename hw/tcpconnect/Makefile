OUT := tcpconnect.a
OUTSO := libtcpconnect.so
AKP := ../akp
AKPA := $(AKP)/akp.a

CXX := g++#-3.4.1
CXXFLAGS := -O3 -Wall -W

CPPFLAGS := -I$(AKP)

AR := ar
ARFLAGS := rcsv

#OBJS := tcpconnect.o \
	connectX.o \
	connectWin.o \
	akptcp.o \
	SocketReader.o \
	SocketWriter.o \
	SocketConnectionAcceptor.o
OBJS := $(patsubst %.cpp,%.o,$(wildcard *.cpp))

ARCHFILE := tcpconnect.tar.gz


.PHONY:	clean arch

$(OUT):	$(OBJS) $(AKPA)
	$(AR) $(ARFLAGS) $@ $(OBJS)

$(OUTSO):	$(OBJS) $(AKPA)
	$(CXX) -o $(OUTSO) -shared $(OBJS) $(AKPA)

#%.o:	%.cpp
#	$(CXX) -c -I$(AKP) $(CXXFLAGS) -o $@ $<

clean:
	$(RM) $(OUT) $(OUTSO) $(OBJS)

arch:
	tar -czvf $(ARCHFILE) *.cpp *.h Makefile
