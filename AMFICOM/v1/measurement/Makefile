MODULE_NAME = measurement_v1
MODULE_VERSION = $(shell date +%Y%m%d)S

SRCDIR=./src
CLASSDIR=./classes
IDLDIR=../idl_v1/idl
LIBDIR=../lib
DBHOSTNAME=mongol
CONNECTSTRING=mongol

LIBNAME=measurement.jar
ARCHFILE=measurement.tar.gz

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)


ORACLECLASSPATH = $(ORACLE_HOME)/jdbc/lib/classes12.jar:$(ORACLE_HOME)/sqlj/lib/translator.jar:$(ORACLE_HOME)/sqlj/lib/runtime12ee.jar
APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/configuration.jar


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
	-I$(IDLDIR)

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
JAVAC_WARNOFF_FLAG=

JPUB = $(ORACLE_HOME)/bin/jpub
JPUB_FLAGS = \
	-J-classpath $(ORACLECLASSPATH) \
	-user=amficom/amficom \
	-url=jdbc:oracle:thin:@$(DBHOSTNAME):1521:$(CONNECTSTRING) \
	-omit_schema_names \
	-package=com.syrus.AMFICOM.measurement.ora \
	-dir=$(SRCDIR) \
	-usertypes=oracle \
	-methods=all

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba .ora $(CLASSDIR)
	$(JAVAC) $(JAVAC_FLAGS) \
		-classpath $(APPCLASSPATH):$(ORACLECLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/resource/LangModelMeasurement.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/MeasurementDatabaseContext.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/ParameterType.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/ParameterTypeDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/ActionType.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/MeasurementType.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/MeasurementTypeDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/AnalysisType.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/AnalysisTypeDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/EvaluationType.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/EvaluationTypeDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/SetParameter.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/Set.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/SetDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/Result.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/ResultDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/MeasurementSetup.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/MeasurementSetupDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/Action.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/Measurement.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/MeasurementDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/Analysis.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/AnalysisDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/Evaluation.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/EvaluationDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/TemporalPattern.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/TemporalPatternDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/Test.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/TestDatabase.java
	touch $@

.ora:	$(CLASSDIR)
	$(JPUB) $(JPUB_FLAGS) \
		-sql=CRONSTRINGARRAY:CronStringArray
	$(JAVAC) $(JAVAC_FLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(ORACLECLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/ora/*.java
	touch $@
	

.corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) \
		$(IDLDIR)/event.idl \
		$(IDLDIR)/measurement.idl
	$(JAVAC) $(JAVAC_FLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/event/corba/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleanora cleancorba
	rm -f $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/measurement/*.class
	rm -f .mach

.PHONY:	cleanora
cleanora:
	rm -rf $(SRCDIR)/com/syrus/AMFICOM/measurement/ora
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/measurement/ora
	rm -f .ora

.PHONY:	cleancorba
cleancorba:
	rm -rf $(SRCDIR)/com/syrus/AMFICOM/measurement/corba
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/measurement/corba
	rm -rf $(SRCDIR)/com/syrus/AMFICOM/event/corba
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/event/corba
	rm -f .corba

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/measurement.idl \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/*.java
		
.PHONY: pmd
pmd:
	${JAVA} -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/measurement text $(PMD_RULES) 