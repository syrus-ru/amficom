OUT=libr6transceiver.so
CPP=g++#-3.3.1
CFLAGS=-Wall -W -Wno-unused-parameter
AKP=$(HOME)/work/HW/akp
AKPA=$(AKP)/akp.a
OBJS=r6transceiver.o

$(OUT): $(OBJS) $(AKPA)
	$(CPP) $(CFLAGS) -shared -o $(OUT) $(OBJS) $(AKPA)

.cpp.o:
	$(CPP) -c -I$(AKP) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux $(CFLAGS) -o $@ $<

.PHONY:	clean

clean:
	rm -f $(OBJS) $(OUT)

