SRC=./src
CLASSES=./classes
TRC=../trc
JAVAC=javac
JAVAH=javah

all:
	$(JAVAC) -sourcepath $(SRC) -d $(CLASSES) $(SRC)/com/syrus/io/TraceDataReader.java
	$(JAVAH) -classpath $(CLASSES) -d $(TRC) com.syrus.io.TraceDataReader
