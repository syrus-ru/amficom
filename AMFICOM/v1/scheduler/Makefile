MODULE_NAME := scheduler
LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

HOST=mongol
SERVICE_NAME=mongol

XMLDIR = schemas
XMLSRCDIR = schemas/src
XMLCLASSSRCDIR = schemas/classes

#ECLIPSE_CLASSPATH=$(shell for line in `cat .classpath | grep classpathentry | grep kind=\"var\" | cut -d '"' -f 4 | sed -r 's/([A-Z0-9]+_[A-Z0-9]+)/\$$\1/'`; do c="$$c:$$line";done;echo $$c)

#ECLIPSE_PROJECT=$(shell  for line in `cat .classpath | grep classpathentry | grep kind=\"src\" | cut -d '"' -f 4 `; do c="$$c:..$$line/bin"; done; echo $$c)

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)
$(call checkenve,SCOMP)
$(call checkenve,SCOMPFLAGS)

LIBNAME_EXTENSIONS_SCHEMAS = $(AMFICOM_HOME)/lib/extensions_schemas.jar
LIBNAME_SCHEDULER_SCHEMAS = $(MODULE_NAME)_schemas.jar

DEPENDENCIES = \
util \
extensions \
general \
administration \
configuration \
measurement \
report \
filter \
reflectometry \
csbridge \
commonclient \
filterclient \
reportclient

EXTCLASSPATH := $(TROVE4JCLASSPATH):$(XMLBEANSCLASSPATH)

SOURCES := $(shell find $(SRCDIR) -name *java)


$(LIBNAME): .mach
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/Client/Schedule/UI/*.[gp][ir][fo]* \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/Schedule/UI
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com \
		-C $(CLASSDIR) xml

.mach:	$(CLASSDIR) xml
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(LIBNAME_EXTENSIONS_SCHEMAS):$(LIBNAME_SCHEDULER_SCHEMAS) \
		$(SOURCES)
	touch $@


.PHONY:	clean
clean:	cleanmach
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) -r $(CLASSDIR)/com
	$(RM) .mach

.PHONY: xml
xml: $(LIBNAME_SCHEDULER_SCHEMAS)

$(LIBNAME_EXTENSIONS_SCHEMAS):
	$(error $(LOGNAME), you forgot to build $@)

$(LIBNAME_SCHEDULER_SCHEMAS): $(LIBNAME_EXTENSIONS_SCHEMAS)
	$(RM) -r $(XMLCLASSDIR)/*
	$(SCOMP) $(SCOMPFLAGS) \
		-cp $(XMLBEANSCLASSPATH):$(LIBNAME_EXTENSIONS_SCHEMAS) \
		-out $@ \
		$(XMLDIR)/$(MODULE_NAME).xsd \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig

.PHONY: xmlclean
xmlclean:
	$(RM) $(LIBNAME_SCHEDULER_SCHEMAS)
	if [ -f $(XMLSRCDIR)/com/syrus/AMFICOM/extensions/.cvsignore ]; \
	then \
		cd $(XMLSRCDIR)/com/syrus/AMFICOM/extensions && $(RM) -r `cat .cvsignore`; \
	else \
		$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/extensions; \
	fi
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/extensions

.PHONY: xmlinstall
xmlinstall: $(LIBDIR) $(LIBDIR)/$(LIBNAME_SCHEDULER_SCHEMAS)

#### Install ####
.PHONY: install
install: $(LIBDIR) $(LIBDIR)/$(LIBNAME) xmlinstall

$(LIBDIR)/$(LIBNAME_SCHEDULER_SCHEMAS): $(LIBNAME_SCHEDULER_SCHEMAS)
	install -m 0644 -p $(LIBNAME_SCHEDULER_SCHEMAS) $(LIBDIR)

$(LIBDIR)/$(LIBNAME): $(LIBNAME)
	install -m 0644 -p $(LIBNAME) $(LIBDIR)

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SOURCES) \
		$(SRCDIR)/com/syrus/AMFICOM/Client/Schedule/UI/parametersPanel.properties \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/Client/General/lang/*.properties) \
		scheduler.properties Application.properties \
		scheduler.sh

.PHONY: pmd
pmd:
#	${JAVA} -cp ${PMD_JARS}:../tools/tools.jar net.sourceforge.pmd.PMD . text rulesets/amficom-pmd-ruleset.xml
	${JAVA} -cp ${PMD_JARS}:../tools/tools.jar net.sourceforge.pmd.PMD . html rulesets/dummystring.xml > pmdlog.html

#${JAVA} -cp ${PMD_JARS} net.sourceforge.pmd.PMD . html ${PMD_RULES} > pmdlog.html


