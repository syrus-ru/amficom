MODULE := akp

CXX_SOURCES := ByteArray \
	Parameter \
	Segment \
	MeasurementSegment \
	ResultSegment \
	KISInfoSegment \
	MCMInfoSegment
CXXFLAGS = -fPIC
ADD_LIBARS = $(LIBAR_SOCKET)

include ../tools/Makefile.cpp.inc.mk

.PHONY:	all
all:	$(OUT_LIBAR)
