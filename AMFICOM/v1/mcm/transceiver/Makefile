OUT=libmcmtransceiver.so
CPP=g++-3.3.1
CFLAGS=-Wall -W
AKP=../../akp
AKPA=$(AKP)/akp.a
OBJS=mcmtransceiver.o

$(OUT):	$(OBJS) $(AKPA)
	$(CPP) $(CFLAGS) -shared -o $(OUT) $(OBJS) $(AKPA)

.cpp.o:
	$(CPP) -c -I$(AKP) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux $(CFLAGS) -o $@ $<

$(AKPA):
	make -C $(AKP)

.PHONY:	clean

clean:
	rm -f $(OBJS) $(OUT)

