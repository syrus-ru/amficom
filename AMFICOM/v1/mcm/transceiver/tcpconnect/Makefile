OUT=tcpconnect.a
#CPP=g++#-3.3.1
#AR=ar
ARFLAGS=rcsv

CFLAGS=-O0 -g -w -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
CXXFLAGS=-O0 -g -w -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

OBJS=connectWin.o connectX.o etcp.o

$(OUT):		$(OBJS)
	$(AR) $(ARFLAGS) $(OUT) $(OBJS)

.PHONY:	clean

clean:
	$(RM) $(OBJS) $(OUT)
