MODULE := akp

CXX_SOURCES := ByteArray \
	Parameter \
	Segment \
	MeasurementSegment \
	ResultSegment \
	KISInfoSegment \
	MCMInfoSegment
ADD_LIBARS = $(LIBAR_SOCKET)

include ../tools/Makefile.cpp.inc.mk

.PHONY:	all
all:	$(OUT_LIBAR) $(OUT_SO)

.PHONY:	clean
clean:
	$(RM) $(OUT_LIBAR) $(OUT_SO) $(OBJS)