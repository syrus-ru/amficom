SRC=./src
CLASSES=./classes
TRANSCEIVER=./transceiver
DADARA=./dadara
LIB=../lib

ORBCLASSPATH=$(ORACLE_HOME)/lib/aurora_client.jar:$(ORACLE_HOME)/lib/vbjorb.jar:$(ORACLE_HOME)/lib/vbjapp.jar
APPCLASSPATH=$(LIB)/kisserver.jar:$(LIB)/ServerInterface_General.jar:$(LIB)/util.jar:$(LIB)/io.jar:$(LIB)/dadara.jar

JAVAC=javac
JAVAH=javah
JAR=jar-3.3.1

LIBNAME=agent.jar
ARCHFILE=agent.tar.gz


.PHONY:	clean arch

$(LIBNAME):	agent
	$(JAR) -cf ./$(LIBNAME) -C $(CLASSES) com/
	cp -vf ./$(LIBNAME) $(LIB)/

agent:
	$(JAVAC) -classpath $(APPCLASSPATH):$(ORBCLASSPATH) -sourcepath $(SRC) -d $(CLASSES) $(SRC)/com/syrus/AMFICOM/agent/Agent.java
	$(JAVAH) -classpath $(CLASSES) -d $(TRANSCEIVER) com.syrus.AMFICOM.agent.Transceiver
	$(JAVAH) -classpath $(CLASSES) -d $(DADARA) com.syrus.AMFICOM.analysis.dadara.DadaraAnalysisManager
	touch agent

clean:
	rm -rf $(CLASSES)/com
	rm -f agent $(LIBNAME)

arch:
	tar -czvf $(ARCHFILE) \
		Makefile rc agent.properties \
		src \
		$(TRANSCEIVER)/*.cpp $(TRANSCEIVER)/*.h $(TRANSCEIVER)/Makefile \
		$(DADARA)/*.cpp $(DADARA)/*.cxx $(DADARA)/*.h $(DADARA)/Makefile $(DADARA)/architecture.net $(DADARA)/weights.wht


