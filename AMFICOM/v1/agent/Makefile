MODULE_NAME = agent_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

TRANSCEIVERDIR=./transceiver
DADARADIR=./dadara

APPCLASSPATH=$(LIBDIR)/kisserver.jar:$(LIBDIR)/corba.jar:$(LIBDIR)/util.jar:$(LIBDIR)/dadara.jar

#JAVAC=javac
#JAVACFLAGS = source 1.3
#SUNJAVACFLAGS = -target 1.2
#JIKESFLAGS = -target 1.2

JAVAH=javah

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)


$(LIBNAME):	agent
	$(JAR) -cf $@ -C $(CLASSDIR) com/

agent:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(AURORACLASSPATH):$(VBJCLASSPATH) \
			$(SRCDIR)/com/syrus/AMFICOM/agent/Agent.java \
			$(SRCDIR)/com/syrus/AMFICOM/agent/TaskReceiver.java \
			$(SRCDIR)/com/syrus/AMFICOM/agent/ReportReceiver.java \
			$(SRCDIR)/com/syrus/AMFICOM/agent/TestContainer.java \
			$(SRCDIR)/com/syrus/AMFICOM/agent/Transceiver.java \
			$(SRCDIR)/com/syrus/AMFICOM/agent/ParametersDatabase.java \
			$(SRCDIR)/com/syrus/AMFICOM/agent/AnalysisManager.java \
			$(SRCDIR)/com/syrus/AMFICOM/agent/EvaluationManager.java \
			$(SRCDIR)/com/syrus/AMFICOM/agent/DadaraAnalysisManager.java \
			$(SRCDIR)/com/syrus/AMFICOM/agent/DadaraEvaluationManager.java \
			$(SRCDIR)/com/syrus/AMFICOM/RISDConnection.java
	$(JAVAH) -classpath $(CLASSDIR) -d $(TRANSCEIVERDIR) com.syrus.AMFICOM.agent.Transceiver
	$(JAVAH) -classpath $(CLASSDIR) -d $(DADARADIR) com.syrus.AMFICOM.agent.DadaraAnalysisManager
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:
	rm -rf $(CLASSDIR)/com
	rm -f agent $(LIBNAME)

.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile rc agent.properties \
		src \
		$(TRANSCEIVERDIR)/*.cpp $(TRANSCEIVERDIR)/*.h $(TRANSCEIVERDIR)/Makefile \
		$(DADARADIR)/*.cpp $(DADARADIR)/*.cxx $(DADARADIR)/*.h $(DADARADIR)/Makefile $(DADARADIR)/architecture.net $(DADARADIR)/weights.wht


