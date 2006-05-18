#
# $Id: Makefile,v 1.10 2006/03/13 08:56:35 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := manager

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

XMLDIR = schemas
XMLSRCDIR = schemas/src
XMLCLASSSRCDIR = schemas/classes

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)
$(call checkenve,SCOMP)
$(call checkenve,SCOMPFLAGS)

GENERALXML = $(LIBDIR)/generalxml.jar
LIBNAME_EXTENSIONS_SCHEMAS = $(AMFICOM_HOME)/lib/extensions_schemas.jar
LIBNAME_MANAGER_SCHEMAS = $(MODULE_NAME)_schemas.jar

DEPENDENCIES = \
util \
general \
administration \
configuration \
measurement \
event \
filter \
reflectometry \
csbridge \
extensions \
commonclient \
filterclient \
resource

XMLCLASSPATH := $(LIBNAME_MANAGER_SCHEMAS):$(LIBNAME_EXTENSIONS_SCHEMAS):$(GENERALXML)
EXTCLASSPATH := $(TROVE4JCLASSPATH):$(EXTLIBDIR)/jgraph.jar

SOURCES := $(shell find $(SRCDIR) -name *java)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	$(CLASSDIR) $(LIBNAME_MANAGER_SCHEMAS)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(XMLBEANSCLASSPATH) \
		$(SOURCES)
	touch $@

.PHONY: xml
xml: $(LIBNAME_MANAGER_SCHEMAS)

$(LIBNAME_EXTENSIONS_SCHEMAS):
	$(error $(LOGNAME), you forgot to build $@)

$(LIBNAME_MANAGER_SCHEMAS): $(LIBNAME_EXTENSIONS_SCHEMAS)
	$(RM) -r $(XMLCLASSDIR)/*
	$(SCOMP) $(SCOMPFLAGS) \
		-cp $(XMLBEANSCLASSPATH):$(LIBNAME_EXTENSIONS_SCHEMAS) \
		-out $@ \
		$(XMLDIR)/$(MODULE_NAME).xsd \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig

.PHONY: xmlclean
xmlclean:
	$(RM) $(LIBNAME_MANAGER_SCHEMAS)
	if [ -f $(XMLSRCDIR)/com/syrus/AMFICOM/extensions/.cvsignore ]; \
	then \
		cd $(XMLSRCDIR)/com/syrus/AMFICOM/extensions && $(RM) -r `cat .cvsignore`; \
	else \
		$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/extensions; \
	fi
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/extensions

.PHONY: clean
clean: cleanmach xmlclean
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) -r $(CLASSDIR)/com
	$(RM) .mach


.PHONY: xmlinstall
xmlinstall: $(LIBDIR) $(LIBDIR)/$(LIBNAME_MANAGER_SCHEMAS)

.PHONY: install
install: $(LIBDIR) $(LIBDIR)/$(LIBNAME) xmlinstall

$(LIBDIR)/$(LIBNAME_MANAGER_SCHEMAS): $(LIBNAME_MANAGER_SCHEMAS)
	install -m 0644 -p $(LIBNAME_MANAGER_SCHEMAS) $(LIBDIR)

$(LIBDIR)/$(LIBNAME): $(LIBNAME)
	install -m 0644 -p $(LIBNAME) $(LIBDIR)
