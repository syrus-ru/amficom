include ../tools/Makefile.cpp.inc.mk

OUT := akp.$(LIB_SUFFIX)
OUTSO := $(SO_PREFIX)akp.$(SO_SUFFIX)

OBJS := ByteArray.$(OBJ_SUFFIX) \
	Parameter.$(OBJ_SUFFIX) \
	Segment.$(OBJ_SUFFIX) \
	MeasurementSegment.$(OBJ_SUFFIX) \
	ResultSegment.$(OBJ_SUFFIX) \
	KISInfoSegment.$(OBJ_SUFFIX) \
	MCMInfoSegment.$(OBJ_SUFFIX)

$(OUT):	$(OBJS)
	$(AR) $(ARFLAGS) $(AR_OUTFILE_FLAG)$@ $(OBJS)

$(OUTSO):	$(OBJS)
	$(LD) $(LDFLAGS) $(LD_SHARED_FLAG) $(OBJS) $(LIB_SOCKET) $(LD_OUTFILE_FLAG)$@

clean:
	$(RM) $(OUT) $(OUTSO) $(OBJS)