SRC=./src
CLASSDIR=./classes
LIB=$(HOME)/work/AMFICOM_v1/lib
QPLIB=/mnt/e/Java/Nettest/2.0/lib/rtu
TRANSCEIVER=../transceiver

QPCLASSPATH=$(QPLIB)/QuestFiberServer.jar:$(QPLIB)/QuestFiberServerClientInterface.jar:$(QPLIB)/QuestFiberServerRMI.jar:$(QPLIB)/QuestFiberGUI.jar
APPCLASSPATH=$(LIB)/util.jar
JAVAC=javac
RMIC=rmic
JAR=jar
JAVAH=javah

LIBNAME=r6.jar
LIBSTUBNAME=stub.jar
ARCHFILE=r6.tar.gz


.PHONY:	setup clean cleanr6 cleanstub arch

$(LIBNAME):	$(LIBSTUBNAME) .r6
	$(JAR) -cvf ./$@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/Dispatcher.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/Dispatcher\$$Command.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/OperationEvent.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/OperationListener.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/kis/Report.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/kis/Task.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/kis/TaskReceiver.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/kis/TaskReporter.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/kis/Transceiver.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/kis/r6/QueryEvent.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/kis/r6/ReportComposer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/kis/r6/R6.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/kis/r6/Session.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/kis/r6/TaskDistinguisher.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/kis/r6/WorkRequestSubmitter.class \
		-C $(CLASSDIR) com/gnnettest/questfiber/gui/events \
		-C $(CLASSDIR) com/gnnettest/questfiber/rmi \
		-C $(CLASSDIR) com/gnnettest/questfiber/work
	$(JAR) -xvf $(LIB)/util.jar \
		com/syrus/util/ByteArray.class \
		com/syrus/io/IniFile.class \
		com/syrus/io/BellcoreStructure.class \
		com/syrus/io/BellcoreStructure\$$Cksum.class \
		com/syrus/io/BellcoreStructure\$$DataPts.class \
		com/syrus/io/BellcoreStructure\$$FxdParams.class \
		com/syrus/io/BellcoreStructure\$$GenParams.class \
		com/syrus/io/BellcoreStructure\$$KeyEvents.class \
		com/syrus/io/BellcoreStructure\$$LnkParams.class \
		com/syrus/io/BellcoreStructure\$$Map.class \
		com/syrus/io/BellcoreStructure\$$Special.class \
		com/syrus/io/BellcoreStructure\$$SupParams.class \
		com/syrus/io/BellcoreWriter.class \
		com/syrus/io/IntelDataOutputStream.class \
		com/syrus/io/IntelDataInputStream.class
	$(JAR) -uvf ./$(LIBNAME) com
	rm -rf com

.r6:	$(CLASSDIR)
	$(JAVAC) -d $(CLASSDIR) -sourcepath $(SRC) -classpath $(APPCLASSPATH):$(QPCLASSPATH) $(SRC)/com/syrus/AMFICOM/kis/r6/R6.java
	$(JAVAH) -d $(TRANSCEIVER) -classpath $(CLASSDIR) com.syrus.AMFICOM.kis.Transceiver
	touch $@

$(LIBSTUBNAME):	.stub
	$(JAR) -cvf $@ \
		-C $(CLASSDIR) com/gnnettest/questfiber/rmi \
		-C $(CLASSDIR) com/gnnettest/questfiber/work

.stub:	$(CLASSDIR)
	$(JAVAC) -d $(CLASSDIR) -classpath $(QPCLASSPATH) $(SRC)/com/gnnettest/questfiber/rmi/DomainManagerRMIServant.java
	$(RMIC) -d $(CLASSDIR) -classpath $(QPCLASSPATH) com.gnnettest.questfiber.rmi.DomainManagerRMIServant
	$(JAVAC) -d $(CLASSDIR) -sourcepath $(SRC) -classpath $(QPCLASSPATH) $(SRC)/com/gnnettest/questfiber/work/WorkRequestDatabase.java
	$(JAVAC) -d $(CLASSDIR) -sourcepath $(SRC) -classpath $(QPCLASSPATH) $(SRC)/com/gnnettest/questfiber/work/SystemDefaultDatabase.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

clean:	cleanr6 cleanstub

cleanr6:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM
	rm -f .r6 $(LIBNAME)

cleanstub:
	rm -rf $(CLASSDIR)/com/gnnettest/questfiber
	rm -f .stub $(LIBSTUBNAME)

setup:	$(CLASSDIR)
	$(JAVAC) -d $(CLASSDIR) -classpath $(QPCLASSPATH) $(SRC)/com/syrus/AMFICOM/kis/r6/setup/Setup.java

arch:
	tar -czvf $(ARCHFILE) \
		Makefile rc AmficomR6.properties delalarms.sql meid.txt \
		$(SRC) \
		$(TRANSCEIVER)/*.cpp $(TRANSCEIVER)/*.h $(TRANSCEIVER)/Makefile
