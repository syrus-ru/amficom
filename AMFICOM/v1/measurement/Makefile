#
# $Id: Makefile,v 1.77 2005/11/28 13:36:41 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := measurement

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/measurement.idl

HOST = aldan
SERVICE_NAME = hodja

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


include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,XMLBEANSCLASSPATH)


LIBNAME_GENERALXML=generalxml.jar

DEPENDENCIES = \
configuration \
administration \
general \
reflectometry \
util

EXTCLASSPATH := $(TROVE4JCLASSPATH)
XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_GENERALXML)


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/measurement/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/reflectometry/*.java)


$(LIBNAME):	.mach
	$(CP) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/measurement/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/measurement/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/measurement \
		-C $(CLASSDIR) com/syrus/AMFICOM/reflectometry

.mach:	.corba .ora $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(EXTCLASSPATH):$(JDBCCLASSPATH):$(CLASSDIR) \
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

.PHONY:	idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/measurement/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/measurement/corba/.cvsignore | grep .java)
	$(RM) .idl

.PHONY:	corba
corba:	.corba

.corba:	$(CLASSDIR) .idl .ora
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(JDBCCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/corba/*.java 
	touch $@

.PHONY:	clean
clean:	cleanmach cleanora cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY:	cleanora
cleanora:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/measurement/ora
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/measurement/ora
	$(RM) .ora

.PHONY:	cleancorba
cleancorba:	idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/measurement/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install:	$(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/measurement.idl \
		$(SOURCES) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/measurement/*.properties)

.PHONY:	pmd
pmd:
	${JAVA} -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/measurement text $(PMD_RULES) 
