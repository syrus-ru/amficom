IDL=./idl
SRC=./src
CLASSES=./classes
TRANSCEIVER=./transceiver
DADARA=./dadara
LIB=../lib
DEVLIB=../devlib
INCIDL=../measurement/idl

LIBNAME=mcm.jar
LIBNAME_IF=mcm_interface.jar
SERVER_IF=$(DEVLIB)/measurementserver_interface.jar
ARCHFILE=mcm.tar.gz

APPCLASSPATH=$(SERVER_IF):$(DEVLIB)/measurement.jar:$(DEVLIB)/ServerInterface_General.jar:$(LIB)/util.jar:$(LIB)/io.jar:$(LIB)/server.jar
ORACLECLASSPATH=$(JDEV_HOME)/jdbc/lib/classes12.jar

IDL2JAVA=idlj
IDL2JAVA_FLAGS=-fall -i $(INCIDL) -td $(SRC) -v
JAVAC=javac
JAVAH=javah
JAR=jar-3.3.1


.PHONY:	clean cleanmcm cleancorba arch

$(LIBNAME):	mcm
	$(JAR) -cf ./$(LIBNAME) \
		-C $(CLASSES) com/syrus/AMFICOM/mcm/AnalysisManager.class \
		-C $(CLASSES) com/syrus/AMFICOM/mcm/EvaluationManager.class \
		-C $(CLASSES) com/syrus/AMFICOM/mcm/KISReport.class \
		-C $(CLASSES) com/syrus/AMFICOM/mcm/MCMImplementation.class \
		-C $(CLASSES) com/syrus/AMFICOM/mcm/MeasurementControlModule.class \
		-C $(CLASSES) com/syrus/AMFICOM/mcm/OnetimeTestProcessor.class \
		-C $(CLASSES) com/syrus/AMFICOM/mcm/PeriodicalTestProcessor.class \
		-C $(CLASSES) com/syrus/AMFICOM/mcm/TestProcessor.class \
		-C $(CLASSES) com/syrus/AMFICOM/mcm/TimetableTestProcessor.class \
		-C $(CLASSES) com/syrus/AMFICOM/mcm/Transceiver.class \
		-C $(CLASSES) com/syrus/AMFICOM/analysis/dadara
	cp -vf ./$(LIBNAME) $(DEVLIB)/

mcm:	$(LIBNAME_IF) $(SERVER_IF)
	$(JAVAC) -d $(CLASSES) -classpath $(LIBNAME_IF):$(APPCLASSPATH) -sourcepath $(SRC) $(SRC)/com/syrus/AMFICOM/mcm/MeasurementControlModule.java
	$(JAVAH) -force -d $(TRANSCEIVER) -classpath $(CLASSES):$(DEVLIB)/measurement.jar com.syrus.AMFICOM.mcm.Transceiver
	$(JAVAH) -force -d $(DADARA) -classpath $(CLASSES) com.syrus.AMFICOM.analysis.dadara.DadaraAnalysisManager
	touch mcm

$(LIBNAME_IF):	corba
	$(JAR) -cf ./$(LIBNAME_IF) \
		-C $(CLASSES) com/syrus/AMFICOM/mcm/corba
	cp -vf ./$(LIBNAME_IF) $(DEVLIB)/

corba:
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) $(IDL)/mcm.idl
	$(JAVAC) -d $(CLASSES) -classpath $(APPCLASSPATH) -sourcepath $(SRC) $(SRC)/com/syrus/AMFICOM/mcm/corba/*.java
	touch corba

clean:	cleancorba cleanmcm

cleanmcm:
	rm -f $(CLASSES)/com/syrus/AMFICOM/mcm/*.class
	rm -rf $(CLASSES)/com/syrus/AMFICOM/analysis
	rm -f mcm $(LIBNAME)

cleancorba:
	rm -rf $(CLASSES)/com/syrus/AMFICOM/mcm/corba
	rm -rf $(SRC)/com/syrus/AMFICOM/mcm/corba
	rm -f corba $(LIBNAME_IF)

arch:
	tar -czvf $(ARCHFILE) \
		Makefile rc mcm.properties general.properties \
		$(IDL) \
		$(TRANSCEIVER)/*.cpp $(TRANSCEIVER)/*.h $(TRANSCEIVER)/Makefile \
		$(DADARA)/*.cpp $(DADARA)/*.cxx $(DADARA)/*.h $(DADARA)/Makefile $(DADARA)/architecture.net $(DADARA)/weights.wht \
		$(SRC)/com/syrus/AMFICOM/mcm/*.java $(SRC)/com/syrus/AMFICOM/analysis/dadara/*.java
