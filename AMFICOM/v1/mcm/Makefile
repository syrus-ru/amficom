MODULE_NAME = mcm_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE= $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

INCIDLDIR=../idl_v1/idl

HOST=mongol
SERVICE_NAME=mongol

TRANSCEIVER_DIR=./transceiver
DADARA_DIR=./dadara

APPCLASSPATH = $(LIBDIR)/mserver_interface.jar:$(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/dadara.jar

JAVAH=javah
JAVAH_FLAGS = \
	-classpath $(APPCLASSPATH):$(CLASSDIR) \
	-force

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)


IDL2JAVA=$(JAVA_HOME)/bin/idlj
IDL2JAVAFLAGS = \
	-fall \
	-i $(IDLDIR) \
	-i $(INCIDLDIR) \
	-td $(SRCDIR) \
	-v

#JAVAC = $(JAVA_HOME)/bin/javac
#JAVACFLAGS = \
	-O \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	-d $(CLASSDIR) \
	-deprecation \
	-encoding $(ENCODING) \
	-extdirs $(JAVA_HOME)/jre/lib/ext \
	-g


all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCM.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCMOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/_MCMStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCMHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCMHolder.class

.head:	.mach
	$(JAVAH) $(JAVAH_FLAGS) \
		-d $(TRANSCEIVER_DIR) \
		com.syrus.AMFICOM.mcm.Transceiver
	$(JAVAH) $(JAVAH_FLAGS) \
		-d $(DADARA_DIR) \
		com.syrus.AMFICOM.mcm.DadaraAnalysisManager
	touch $@

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/DatabaseContextSetup.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/MCMConfigurationObjectLoader.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/MCMMeasurementObjectLoader.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/TestProcessor.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/OnetimeTestProcessor.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/PeriodicalTestProcessor.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/ContinuousTestProcessor.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/MeasurementControlModule.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/MCMImplementation.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/KISReport.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/Transceiver.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisException.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/EvaluationException.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/MeasurementException.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/TestProcessingException.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/EvaluationManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/DadaraAnalysisManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisEvaluationProcessor.java
	touch $@

.corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVAFLAGS) \
		$(IDLDIR)/mcm.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	rm -f $(LIBNAME) $(LIBNAME_INTERFACE)

.PHONY:	cleanmach
cleanmach:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/mcm/*.class
	rm -f .mach

.PHONY:	cleanhead
cleanhead:
	$(RM) -f \
		$(TRANSCEIVER_DIR)/com_syrus_AMFICOM_mcm_Transceiver.h \
		$(DADARA_DIR)/com_syrus_AMFICOM_mcm_DadaraAnalysisManager.h \
		.head
	

.PHONY:	cleancorba
cleancorba:
	rm -rf $(SRCDIR)/com/syrus/AMFICOM/mcm/corba
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/mcm/corba
	rm -f .corba

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/mcm.idl \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/*.java
