MODULE_NAME = measurement_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

APPCLASSPATH = $(LIBDIR)/configuration.jar:$(LIBDIR)/general.jar:$(LIBDIR)/util.jar

HOST=mongol
SERVICE_NAME=mongol

JAVACFLAGS = -source 1.3
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = -target 1.2

JPUB = $(ORACLE_HOME)/bin/jpub
JPUB_FLAGS = \
	-J-classpath $(JDBCCLASSPATH):$(SQLJCLASSPATH) \
	-user=amficom/amficom \
	-url=jdbc:oracle:thin:@$(HOST):1521:$(SERVICE_NAME) \
	-omit_schema_names \
	-package=com.syrus.AMFICOM.measurement.ora \
	-dir=$(SRCDIR) \
	-usertypes=oracle \
	-methods=all


include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)


IDL2JAVA=$(JAVA_HOME)/bin/idlj
IDL2JAVAFLAGS = \
	-fall \
	-i $(IDLDIR) \
	-td $(SRCDIR) \
	-v


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba .ora $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(JDBCCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/resource/LangModelMeasurement.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/MeasurementObjectTypePool.java \
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
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(JDBCCLASSPATH):$(SQLJCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/ora/*.java
	touch $@
	

.corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVAFLAGS) $(IDLDIR)/event.idl
	$(IDL2JAVA) $(IDL2JAVAFLAGS) $(IDLDIR)/measurement.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
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
