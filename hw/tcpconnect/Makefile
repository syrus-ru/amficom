OUT=tcpconnect.a
OUTSO=libtcpconnect.so
AKP=../akp
AKPA=$(AKP)/akp.a
CPP=g++#-3.4.1
CFLAGS=-O -Wall -W

AR=ar
ARFLAGS=rcsv

OBJS=tcpconnect.o connectX.o connectWin.o akptcp.o
ARCHFILE=tcpconnect.tar.gz

.PHONY:	clean arch

$(OUT):	$(OBJS) $(AKPA)
	$(AR) $(ARFLAGS) $(OUT) $(OBJS)

$(OUTSO):	$(OBJS) $(AKPA)
	$(CPP) -o $(OUTSO) -shared $(OBJS) $(AKPA)

.cpp.o:
	$(CPP) -c -I$(AKP) $(CFLAGS) -o $@ $<

clean:
	rm -f $(OUT) $(OUTSO) $(OBJS)

arch:
	tar -czvf $(ARCHFILE) *.cpp *.h Makefile
