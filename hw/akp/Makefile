OUT := akp.a
OUTSO := libakp.so

CXX := g++#-3.4.1
CXXFLAGS := -O3 -Wall -W

AR := ar
ARFLAGS := rcsv

OBJS := ByteArray.o \
	Parameter.o \
	Segment.o \
	KISInfoSegment.o \
	MCMInfoSegment.o \
	MeasurementSegment.o \
	ResultSegment.o
#OBJS := $(patsubst %.cpp,%.o,$(wildcard *.cpp))

ARCHFILE := akp.tar.gz

all:	$(OUT)

$(OUT):	$(OBJS)
	$(AR) $(ARFLAGS) $(OUT) $(OBJS)

$(OUTSO):	$(OBJS)
	$(CXX) -o $(OUTSO) -shared $(OBJS)

#%.o:	%.cpp
#	$(CXX) -c $(CXXFLAGS) -o $@ $<

.PHONY:	clean
clean:
	$(RM) $(OUT) $(OUTSO) $(OBJS)

.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) *.cpp *.h Makefile
