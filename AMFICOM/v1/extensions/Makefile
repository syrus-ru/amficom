#
# $Id: Makefile,v 1.8 2005/12/12 09:04:34 bob Exp $
#
# vim:set ft=make:
#

MODULE_NAME := extensions

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

XMLDIR = schemas
include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)
$(call checkenve,SCOMP)
$(call checkenve,SCOMPFLAGS)

LIBNAME_EXTENSIONS_SCHEMAS = $(MODULE_NAME)_schemas.jar
LIBNAME_RESOURCES_SCHEMAS = resources_schemas.jar

DEPENDENCIES = \
util 

EXTCLASSPATH := $(TROVE4JCLASSPATH):$(XMLBEANSCLASSPATH):$(XALANJCLASSPATH)
XMLCLASSPATH := $(LIBDIR)/$(LIBNAMEXML):$(LIBDIR)/$(LIBNAME_EXTENSIONS_SCHEMAS):$(LIBDIR)/$(LIBNAME_RESOURCES_SCHEMAS)

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/extensions/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/resources/*.java) 

.PHONY: xml
xml: $(LIBNAME_EXTENSIONS_SCHEMAS) $(LIBNAME_RESOURCES_SCHEMAS)

$(LIBNAME_EXTENSIONS_SCHEMAS):
	$(RM) -r $(XMLCLASSDIR)/*
	$(SCOMP) $(SCOMPFLAGS) \
		-cp $(XMLBEANSCLASSPATH) \
		-out $@ \
		$(XMLDIR)/$(MODULE_NAME).xsd \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig

$(LIBNAME_RESOURCES_SCHEMAS): $(LIBNAME_EXTENSIONS_SCHEMAS)
	$(RM) -r $(XMLCLASSDIR)/*
	$(SCOMP) $(SCOMPFLAGS) \
		-cp $(XMLBEANSCLASSPATH):$(LIBNAME_EXTENSIONS_SCHEMAS) \
		-out $@ \
		$(XMLDIR)/resources.xsd \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig

.PHONY: xmlclean
xmlclean:
	$(RM) $(LIBNAME_EXTENSIONS_SCHEMAS)
	$(RM) $(LIBNAME_RESOURCES_SCHEMAS)
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

.PHONY:	cleanmach
cleanmach:
	$(RM) -r $(CLASSDIR)/com
	$(RM) .mach


.PHONY: xmlinstall
xmlinstall: $(LIBDIR) $(LIBDIR)/$(LIBNAME_EXTENSIONS_SCHEMAS) $(LIBDIR)/$(LIBNAME_RESOURCES_SCHEMAS)

.PHONY:	install
install: $(LIBDIR) $(LIBDIR)/$(LIBNAME) xmlinstall

$(LIBNAME): .mach
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

.mach: $(CLASSDIR) xml
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

$(LIBDIR)/$(LIBNAME_EXTENSIONS_SCHEMAS): $(LIBNAME_EXTENSIONS_SCHEMAS)
	install -m 0644 -p $(LIBNAME_EXTENSIONS_SCHEMAS) $(LIBDIR)

$(LIBDIR)/$(LIBNAME_RESOURCES_SCHEMAS): $(LIBNAME_RESOURCES_SCHEMAS)
	install -m 0644 -p $(LIBNAME_RESOURCES_SCHEMAS) $(LIBDIR)

$(LIBDIR)/$(LIBNAME): $(LIBNAME)
	install -m 0644 -p $(LIBNAME) $(LIBDIR)
