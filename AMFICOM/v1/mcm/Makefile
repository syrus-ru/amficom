MODULE_NAME = mcm_v1
MODULE_VERSION = $(shell date +%Y%m%d)S

SRCDIR=./src
CLASSDIR=./classes
IDLDIR=./idl
INCIDLDIR=../idl_v1/idl
LIBDIR=../lib
DBHOSTNAME=mongol
CONNECTSTRING=mongol

TRANSCEIVER_DIR=./transceiver
DADARA_DIR=./dadara

LIBNAME=mcm.jar
ARCHFILE=mcm.tar.gz

ORACLECLASSPATH = $(ORACLE_HOME)/jdbc/lib/classes12.jar:$(ORACLE_HOME)/sqlj/lib/translator.jar:$(ORACLE_HOME)/sqlj/lib/runtime12ee.jar
APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/dadara.jar

IDL2JAVA = idl2java
IDL2JAVA_FLAGS = \
	-C \
	-H \
	-no_bind \
	-no_examples \
	-no_tie \
	-narrow_compliance \
	-portable \
	-strict \
	-root_dir $(SRCDIR) \
	-I$(INCIDLDIR)

#JAVAC = javac
JAVAC = jikes
JIKES_FLAGS = \
	-target 1.4.2 \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	-extdirs $(JAVA_HOME)/jre/lib/ext \
	+E \
	+Pmodifier-order \
	+Predundant-modifiers \
	+Pswitchcheck \
	+Pnaming-convention \
	+T=2
JAVAC_FLAGS = \
	-d $(CLASSDIR) \
	-g \
	-O \
	-deprecation
JAVAC_FLAGS := $(JAVAC_FLAGS) $(JIKES_FLAGS)
JAVAC_WARNOFF_FLAG=-nowarn

JAVAH=javah
JAVAH_FLAGS = \
	-classpath $(CLASSDIR) \
	-force

JAR=jar-3.3.1


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.head:	.mach
	$(JAVAH) $(JAVAH_FLAGS) \
		-d $(TRANSCEIVER_DIR) \
		com.syrus.AMFICOM.mcm.Transceiver
	$(JAVAH) $(JAVAH_FLAGS) \
		-d $(TRANSCEIVER_DIR) \
		com.syrus.AMFICOM.analysis.dadara.DadaraAnalysisManager
	touch $@

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVAC_FLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/EvaluationManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/DadaraAnalysisManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisEvaluationProcessor.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisException.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/EvaluationException.java
#		$(SRCDIR)/com/syrus/AMFICOM/mcm/MeasurementControlModule.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/DatabaseSetup.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/Transceiver.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/KISReport.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/MCMImplementation.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/TestProcessor.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/OnetimeTestProcessor.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/PeriodicalTestProcessor.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/ContinuousTestProcessor.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/AnalysisManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/EvaluationManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/DadaraAnalysisManager.java \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/DadaraEvaluationManager.java
	touch $@

.corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) \
		$(IDLDIR)/mcm.idl
	$(JAVAC) $(JAVAC_FLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
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
