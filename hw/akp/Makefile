OUT=akp.a
OUTSO=libakp.so
CPP=g++#-3.4.1
CFLAGS=-O3 -Wall -W
AR=ar
ARFLAGS=rcsv
OBJS=ByteArray.o \
	Parameter.o \
	Segment.o \
	KISInfoSegment.o \
	MCMInfoSegment.o \
	MeasurementSegment.o \
	ResultSegment.o
ARCHFILE=akp.tar.gz

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
