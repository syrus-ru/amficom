OUT=tcpconnect.a
OUTSO=libtcpconnect.so

CPP=g++#-3.4.1
CFLAGS=-O -Wall -W

AR=ar
ARFLAGS=rcsv

OBJS=tcpconnect.o
ARCHFILE=tcpconnect.tar.gz

.PHONY:	clean arch

$(OUT):	$(OBJS)
	$(AR) $(ARFLAGS) $(OUT) $(OBJS)

$(OUTSO):	$(OBJS)
	$(CPP) -o $(OUTSO) -shared $(OBJS)

.cpp.o:
	$(CPP) -c $(CFLAGS) -o $@ $<

clean:
	rm -f $(OUT) $(OUTSO) $(OBJS)

arch:
	tar -czvf $(ARCHFILE) *.cpp *.h Makefile
