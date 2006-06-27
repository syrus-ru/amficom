DBHOSTNAME=
CONNECTSTRING=

CLASSES=./classes
SRC=./src
LIB=../lib
DEVLIB=../devlib

APPCLASSPATH=$(CLASSES):$(DEVLIB)/ServerInterface_Util.jar:$(LIB)/util.jar
ORACLECLASSPATH=$(JDEV_HOME)/sqlj/lib/translator.jar:$(JDEV_HOME)/sqlj/lib/runtime12.jar:$(JDEV_HOME)/jdbc/lib/classes12.jar

JAVAC=javac
SQLJ=$(JDEV_HOME)/bin/sqlj
SQLJOPTIONS=-optcols -user=amficom/amficom -url=jdbc:oracle:thin:@$(DBHOSTNAME):1521:$(CONNECTSTRING) -compiler-executable=$(JAVAC) -codegen=oracle -status -d $(CLASSES) -dir $(SRC) -classpath $(ORACLECLASSPATH):$(APPCLASSPATH)

all:
	$(JAVAC) -d $(CLASSES) -classpath $(APPCLASSPATH):$(ORACLECLASSPATH) $(SRC)/hlam/Setup.java
