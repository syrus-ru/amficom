MODULE_NAME := mcm_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE := $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR := ../idl_v1/idl

HOST := mongol
SERVICE_NAME := mongol

TRANSCEIVER_DIR := ./transceiver

include ../tools/Makefile.inc

APPCLASSPATH := $(LIBDIR)/mserver_interface.jar:$(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/dadara.jar

JAVAH := javah
JAVAH_FLAGS := \
	-classpath $(APPCLASSPATH):$(CLASSDIR) \
	-force


$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME):	.mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCM.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCMOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/_MCMStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCMHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCMHolder.class
	$(CP) $(LIBNAME_INTERFACE) $(LIBDIR)

.head:	.mach
	$(JAVAH) $(JAVAH_FLAGS) \
		-d $(TRANSCEIVER_DIR) \
		com.syrus.AMFICOM.mcm.TCPKISConnection
	touch $@

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/util/KISConnectionLRUMap.java \
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
		$(SRCDIR)/com/syrus/AMFICOM/mcm/KISConnection.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/KISConnectionManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/TCPKISConnection.java \
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
	$(IDLJ) $(IDLJFLAGS) idl/mcm.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba cleanhead
	$(RM) $(LIBNAME) $(LIBNAME_INTERFACE)

.PHONY:	cleanmach
cleanmach:
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/mcm/*.class
	$(RM) -r $(CLASSDIR)/com/syrus/util/*.class
	$(RM) .mach

.PHONY:	cleanhead
cleanhead:
	$(RM) \
		$(TRANSCEIVER_DIR)/com_syrus_AMFICOM_mcm_TCPKISConnection.h
	$(RM) .head
	

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mcm/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/mcm/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBNAME_INTERFACE)
	$(CP) $(LIBNAME) $(LIBNAME_INTERFACE) $(LIBDIR)

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		idl/mcm.idl \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/*.java

