MODULE_NAME = measurement_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

HOST = mongol
SERVICE_NAME = mongol

JAVACFLAGS = -source 1.3
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = -target 1.2

JPUB = $(ORACLE_HOME)/bin/jpub
JPUB_FLAGS = \
	-compile=false \
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

APPCLASSPATH := $(LIBDIR)/configuration.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/general.jar:$(LIBDIR)/util.jar


SOURCES := com/syrus/AMFICOM/resource/LangModelMeasurement.java \
		com/syrus/AMFICOM/measurement/DatabaseMeasurementObjectLoader.java \
		com/syrus/AMFICOM/measurement/MeasurementObjectLoader.java \
		com/syrus/AMFICOM/measurement/MeasurementStorableObjectPool.java \
		com/syrus/AMFICOM/measurement/MeasurementDatabaseContext.java \
		com/syrus/AMFICOM/measurement/ActionType.java \
		com/syrus/AMFICOM/measurement/MeasurementType.java \
		com/syrus/AMFICOM/measurement/MeasurementTypeDatabase.java \
		com/syrus/AMFICOM/measurement/AnalysisType.java \
		com/syrus/AMFICOM/measurement/AnalysisTypeDatabase.java \
		com/syrus/AMFICOM/measurement/EvaluationType.java \
		com/syrus/AMFICOM/measurement/EvaluationTypeDatabase.java \
		com/syrus/AMFICOM/measurement/ModelingType.java \
		com/syrus/AMFICOM/measurement/ModelingTypeDatabase.java \
		com/syrus/AMFICOM/measurement/SetParameter.java \
		com/syrus/AMFICOM/measurement/Set.java \
		com/syrus/AMFICOM/measurement/SetDatabase.java \
		com/syrus/AMFICOM/measurement/Result.java \
		com/syrus/AMFICOM/measurement/ResultDatabase.java \
		com/syrus/AMFICOM/measurement/Modeling.java \
		com/syrus/AMFICOM/measurement/ModelingDatabase.java \
		com/syrus/AMFICOM/measurement/MeasurementSetup.java \
		com/syrus/AMFICOM/measurement/MeasurementSetupDatabase.java \
		com/syrus/AMFICOM/measurement/Action.java \
		com/syrus/AMFICOM/measurement/Measurement.java \
		com/syrus/AMFICOM/measurement/MeasurementDatabase.java \
		com/syrus/AMFICOM/measurement/Analysis.java \
		com/syrus/AMFICOM/measurement/AnalysisDatabase.java \
		com/syrus/AMFICOM/measurement/Evaluation.java \
		com/syrus/AMFICOM/measurement/EvaluationDatabase.java \
		com/syrus/AMFICOM/measurement/TemporalPattern.java \
		com/syrus/AMFICOM/measurement/TemporalPatternDatabase.java \
		com/syrus/AMFICOM/measurement/Test.java \
		com/syrus/AMFICOM/measurement/TestDatabase.java \
		com/syrus/AMFICOM/measurement/TemporalCondition.java \
		com/syrus/AMFICOM/measurement/LinkedIdsConditionImpl.java \
		com/syrus/AMFICOM/measurement/DomainCondition.java \
		com/syrus/AMFICOM/measurement/StringFieldConditionImpl.java \
		com/syrus/AMFICOM/measurement/ResultCondition.java \
		com/syrus/AMFICOM/measurement/ResultSortCondition.java 


$(LIBNAME): .mach
	cp \
		$(SRCDIR)/com/syrus/AMFICOM/resource/measurement.properties \
		$(SRCDIR)/com/syrus/AMFICOM/resource/measurement_ru.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/resource/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/measurement \
		-C $(CLASSDIR) com/syrus/AMFICOM/resource

.mach:	.corba .ora $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(JDBCCLASSPATH):$(CLASSDIR) \
		$(addprefix $(SRCDIR)/,$(SOURCES))
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
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/measurement.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/corba/*.java 
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleanora cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(addprefix $(CLASSDIR)/,$(subst .java,.class,$(SOURCES)))
	$(RM) .mach

.PHONY:	cleanora
cleanora:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/measurement/ora
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/measurement/ora
	$(RM) .ora

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/measurement/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/measurement/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/measurement.idl \
		$(addprefix $(SRCDIR)/,$(SOURCES))
		
.PHONY: pmd
pmd:
	${JAVA} -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/measurement text $(PMD_RULES) 

