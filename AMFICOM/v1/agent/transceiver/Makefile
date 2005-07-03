OUT=libagenttransceiver.so
CPP=g++#-3.3.1
CFLAGS=-Wall -W -Wno-unused-parameter -g
OBJS=agenttransceiver.o
AKP=../../../HW/akp
AKPA=$(AKP)/akp.a

$(OUT):	$(OBJS) $(AKPA)
	$(CPP) $(CFLAGS) -shared -o $(OUT) $(OBJS) $(AKPA)

.cpp.o:
	$(CPP) -c -I$(AKP) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux $(CFLAGS) -o $@ $<

.PHONY:	clean arch

clean:
	rm -f $(OBJS) $(OUT)
