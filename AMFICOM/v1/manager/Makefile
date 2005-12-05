#
# $Id: Makefile,v 1.9 2005/12/05 18:24:27 bass Exp $
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
csbridge \
commonclient \
filterclient \
resource

EXTCLASSPATH := $(TROVE4JCLASSPATH):$(EXTLIBDIR)/jgraph.jar

$(LIBNAME): .mach
	mkdir -p $(CLASSDIR)/com/syrus/AMFICOM/manager/resources/icons
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/manager/resources/icons/*.[pg][ni][gf] \
		$(CLASSDIR)/com/syrus/AMFICOM/manager/resources/icons
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/manager/resources/*.[pgp][nir][gfo]* \
		$(CLASSDIR)/com/syrus/AMFICOM/manager/resources/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH) \
		$(shell find . -name *java)
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
