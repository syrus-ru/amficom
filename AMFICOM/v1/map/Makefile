#
# $Id: Makefile,v 1.54 2005/12/08 14:26:27 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := map

LIBNAME := $(MODULE_NAME).jar
LIBNAMEXML := $(MODULE_NAME)xml.jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/$(MODULE_NAME).idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,TROVE4JCLASSPATH)
$(call checkenve,XMLBEANSCLASSPATH)
$(call checkenve,SCOMP)
$(call checkenve,SCOMPFLAGS)

GENERALXML = $(LIBDIR)/generalxml.jar
CONFIGURATIONXML = $(LIBDIR)/configurationxml.jar

DEPENDENCIES = \
administration \
general \
configuration \
resource \
util \
bugs

EXTCLASSPATH := $(TROVE4JCLASSPATH):$(XMLBEANSCLASSPATH)
XMLCLASSPATH := $(LIBDIR)/$(LIBNAMEXML):$(GENERALXML):$(CONFIGURATIONXML)

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/map/*.java)
XSD_SOURCES := $(wildcard $(XMLDIR)/*.xsd)
XSD_CONFIG := $(XMLDIR)/$(MODULE_NAME).xsdconfig


$(LIBNAME): .mach
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

.mach: $(CLASSDIR) xml .corba
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl xml
	$(JAVAC) \
		$(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/corba/*.java
	touch $@

.PHONY: xml
xml: $(LIBNAMEXML)

$(GENERALXML) $(CONFIGURATIONXML):
	$(error $(LOGNAME), you forgot to build $@)

$(LIBNAMEXML): $(XSD_SOURCES) $(XSD_CONFIG) $(GENERALXML) $(CONFIGURATIONXML)
	$(RM) -r $(XMLCLASSDIR)/*
	$(SCOMP) $(SCOMPFLAGS) \
		-cp $(XMLBEANSCLASSPATH):$(GENERALXML):$(CONFIGURATIONXML) \
		-out $@ \
		$(XSD_SOURCES) \
		$(XSD_CONFIG)

.PHONY: xmlclean
xmlclean:
	$(RM) $(LIBNAMEXML)
	if [ -f $(XMLSRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/xml/.cvsignore ]; \
	then \
		cd $(XMLSRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/xml && $(RM) -r `cat .cvsignore`; \
	else \
		$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/xml; \
	fi
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/xml

.PHONY: clean
clean: cleanmach cleancorba xmlclean
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES))) \
		.mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/corba
	$(RM) .corba

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/corba/.cvsignore | grep .java)
	$(RM) .idl


.PHONY: xmlinstall
xmlinstall: $(LIBDIR) $(LIBDIR)/$(LIBNAMEXML)

.PHONY: install
install: $(LIBDIR) $(LIBDIR)/$(LIBNAME) xmlinstall

$(LIBDIR)/$(LIBNAMEXML): $(LIBNAMEXML)
	install -m 0644 -p $(LIBNAMEXML) $(LIBDIR)

$(LIBDIR)/$(LIBNAME): $(LIBNAME)
	install -m 0644 -p $(LIBNAME) $(LIBDIR)

.PHONY: xmluninstall
xmluninstall:
	$(RM) $(LIBDIR)/$(LIBNAMEXML)

.PHONY: uninstall
uninstall: xmluninstall
	$(RM) $(LIBDIR)/$(LIBNAME)

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL_SOURCES) \
		$(SOURCES) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/*.properties) \
		$(wildcard $(XMLDIR)/*.xsd) \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig

.PHONY: pmd
pmd:
	$(JAVA) -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/$(MODULE_NAME) text $(PMD_RULES) 
