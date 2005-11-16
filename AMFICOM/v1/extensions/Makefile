#
# $Id: Makefile,v 1.5 2005/11/16 13:27:47 bass Exp $
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

.PHONY: xml
xml: .xml

.xml: $(LIBNAME_RESOURCES_SCHEMAS) $(LIBNAME_EXTENSIONS_SCHEMAS)
	touch $@

.PHONY: xmlinstall
xmlinstall: .xml $(LIBDIR)
	install -m 0644 $(LIBNAME_EXTENSIONS_SCHEMAS) $(LIBDIR)
	install -m 0644 $(LIBNAME_RESOURCES_SCHEMAS) $(LIBDIR)

$(LIBNAME_EXTENSIONS_SCHEMAS):
	$(RM) -r $(XMLCLASSDIR)/*
	$(SCOMP) $(SCOMPFLAGS) -out $@ \
		$(XMLDIR)/$(MODULE_NAME).xsd \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig

$(LIBNAME_RESOURCES_SCHEMAS): 
	$(RM) -r $(XMLCLASSDIR)/*
	$(SCOMP) $(SCOMPFLAGS) -out $@ \
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
	$(RM) .xml
