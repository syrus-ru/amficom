MODULE_NAME = mcm_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE= $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

HOST=mongol
SERVICE_NAME=mongol

TRANSCEIVER_DIR=./transceiver
DADARA_DIR=./dadara

include ../tools/Makefile.inc

APPCLASSPATH = $(LIBDIR)/mserver_interface.jar:$(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/dadara.jar

JAVAH=javah
JAVAH_FLAGS = \
	-classpath $(APPCLASSPATH):$(CLASSDIR) \
	-force


$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .head
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
		com.syrus.AMFICOM.mcm.Transceiver
	$(JAVAH) $(JAVAH_FLAGS) \
		-d $(TRANSCEIVER_DIR) \
		com.syrus.AMFICOM.mcm.TCPServer
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
		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisEvaluationProcessor.java\
		$(SRCDIR)/com/syrus/AMFICOM/mcm/TCPServer.java
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
	rm -f $(LIBNAME) $(LIBNAME_INTERFACE)

.PHONY:	cleanmach
cleanmach:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/mcm/*.class
	rm -f .mach

.PHONY:	cleanhead
cleanhead:
	$(RM) -f \
		$(TRANSCEIVER_DIR)/com_syrus_AMFICOM_mcm_Transceiver.h \
		$(TRANSCEIVER_DIR)/com_syrus_AMFICOM_mcm_TCPServer.h \		
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
		idl/mcm.idl \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/*.java

lib: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)	
