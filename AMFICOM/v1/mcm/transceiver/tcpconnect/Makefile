OUT=tcpconnect.so
CPP=g++#-3.3.1
CFLAGS=-Wall -W

OBJS=connectWin.o connectX.o etcp.o

$(OUT):	$(OBJS)
	$(CPP) $(CFLAGS) -shared -o $(OUT) $(OBJS)

.cpp.o:
	$(CPP) -c -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux $(CFLAGS) -o $@ $<


.PHONY:	clean

clean:
	rm -f $(OBJS) $(OUT)
