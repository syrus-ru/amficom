MODULE_NAME = mcm_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

INCIDLDIR=../idl_v1/idl

HOST=mongol
SERVICE_NAME=mongol

TRANSCEIVER_DIR=./transceiver
DADARA_DIR=./dadara

APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/dadara.jar

IDL2JAVAFLAGS = -I$(INCIDLDIR)

JAVAH=javah
JAVAH_FLAGS = \
	-classpath $(CLASSDIR) \
	-force

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

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
		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/EvaluationManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/DadaraAnalysisManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisEvaluationProcessor.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisException.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/EvaluationException.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/MeasurementControlModule.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/DatabaseSetup.java #\
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/Transceiver.java \
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/KISReport.java \
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/MCMImplementation.java \
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/TestProcessor.java \
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/OnetimeTestProcessor.java \
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/PeriodicalTestProcessor.java \
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/ContinuousTestProcessor.java \
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisManager.java \
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/EvaluationManager.java \
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/DadaraAnalysisManager.java \
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/DadaraEvaluationManager.java
	touch $@

.corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVAFLAGS) \
		$(IDLDIR)/mcm.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(VBJCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	rm -f $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/mcm/*.class
	rm -f .mach

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
