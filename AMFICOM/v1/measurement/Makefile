MODULE_NAME = measurement_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

HOST = aldan
SERVICE_NAME = hodja

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

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

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/measurement/*.java) \
		$(SRCDIR)/com/syrus/AMFICOM/resource/LangModelMeasurement.java 


$(LIBNAME): .mach
	$(CP) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/resource/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/resource/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/measurement \
		-C $(CLASSDIR) com/syrus/AMFICOM/resource

.mach:	.corba .ora $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(JDBCCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
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
	$(RM) $(wildcard $(CLASSDIR)/com/syrus/AMFICOM/measurement/*.class) \
		$(CLASSDIR)/com/syrus/AMFICOM/resource/LangModelMeasurement.class
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
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^



#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/measurement.idl \
		$(SOURCES)
		
.PHONY: pmd
pmd:
	${JAVA} -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/measurement text $(PMD_RULES) 

