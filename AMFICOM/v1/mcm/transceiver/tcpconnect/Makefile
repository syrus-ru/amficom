OUT=tcpconnect.a
CPP=g++#-3.3.1
AR=ar
ARFLAGS=rcsv

OBJS=connectWin.o connectX.o etcp.o

$(OUT):		$(OBJS)
	$(AR) $(ARFLAGS) $(OUT) $(OBJS)

.cpp.o:
	$(CPP) -c -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux $(CFLAGS) -o $@ $<


.PHONY:	clean

clean:
	rm -f $(OBJS) $(OUT)
