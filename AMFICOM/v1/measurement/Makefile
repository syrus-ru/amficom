SRCDIR=./src
CLASSDIR=./classes
IDLDIR=../idl_v1/idl
LIBDIR=../lib
DBHOSTNAME=mongol
CONNECTSTRING=mongol

LIBNAME=measurement.jar
ARCHFILE=measurement.tar.gz

ORACLECLASSPATH = $(ORACLE_HOME)/jdbc/lib/classes12.jar:$(ORACLE_HOME)/sqlj/lib/translator.jar:$(ORACLE_HOME)/sqlj/lib/runtime12ee.jar
APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar

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

JAR=jar-3.3.1


$(LIBNAME): mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.PHONY:	mach
mach:	.mach
.mach:	.corba .ora $(CLASSDIR)
	$(JAVAC) $(JAVAC_FLAGS) \
		-classpath $(APPCLASSPATH):$(ORACLECLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/MeasurementDatabaseContext.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/TemporalPattern.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/TemporalPattern_Database.java
	touch $@

.PHONY:	ora
ora:	.ora
.ora:	$(CLASSDIR)
	$(JPUB) $(JPUB_FLAGS) \
		-sql=CRONSTRINGARRAY:CronStringArray
	$(JAVAC) $(JAVAC_FLAGS) -nowarn \
		-classpath $(ORACLECLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM//measurement/ora/*.java
	touch $@
	

.PHONY:	corba
corba:	.corba
.corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) \
		$(IDLDIR)/event.idl \
		$(IDLDIR)/measurement.idl
	$(JAVAC) $(JAVAC_FLAGS) -nowarn \
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
