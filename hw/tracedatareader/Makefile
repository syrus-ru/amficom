SRC=./src
CLASSES=./classes
TRC=../trc
JAVAC=javac
JAVAH=javah

all: $(CLASSES)
	$(JAVAC) -sourcepath $(SRC) -d $(CLASSES) $(SRC)/com/syrus/io/TraceDataReader.java
	$(JAVAH) -classpath $(CLASSES) -d $(TRC) com.syrus.io.TraceDataReader

$(CLASSES):
	mkdir -p $@
