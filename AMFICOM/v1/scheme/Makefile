#
# $Id: Makefile,v 1.49 2005/11/28 12:36:56 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := scheme

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/scheme.idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)
$(call checkenve,SCOMP)
$(call checkenve,SCOMPFLAGS)

LIBNAME_GENERALXML=generalxml.jar
LIBNAME_MAPXML=mapxml.jar
LIBNAME_SCHEMEXML=schemexml.jar

# Directories that contain both hand-written and generated files.
DIRS_TO_SCRUB = \
	com/syrus/AMFICOM/scheme \
	com/syrus/AMFICOM/scheme/corba

SRCDIRS_TO_SCRUB = $(foreach dir,$(DIRS_TO_SCRUB),$(SRCDIR)/$(dir))

DEPENDENCIES = \
general \
administration \
configuration \
measurement \
map \
resource \
util \
bugs

EXTCLASSPATH := $(TROVE4JCLASSPATH)
XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_SCHEMEXML):$(LIBDIR)/$(LIBNAME_MAPXML):$(LIBDIR)/$(LIBNAME_GENERALXML)


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/scheme/*.java)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach: .corba .xml $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/scheme/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/.cvsignore | grep .java)
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/*.java
	touch $@


#### XML ####
.PHONY: xml
xml: .xml

.xml: $(LIBNAME_SCHEMEXML)
	touch $@

.PHONY: xmlinstall
xmlinstall: .xml $(LIBDIR)
	install -m 0644 $(LIBNAME_SCHEMEXML) $(LIBDIR)

$(LIBNAME_SCHEMEXML): 
	$(SCOMP) $(SCOMPFLAGS) -out $@ \
		$(wildcard $(XMLDIR)/*.xsd) \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig
	$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/general
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/general
	$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/configuration
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/configuration
	$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/map
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/map

.PHONY: clean
clean: cleanmach cleancorba cleanxml
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/scheme/corba
	$(RM) .corba

.PHONY: cleanxml xmlclean
cleanxml xmlclean:
	$(RM) $(LIBNAME_SCHEMEXML)
	if [ -f $(XMLSRCDIR)/com/syrus/AMFICOM/scheme/xml/.cvsignore ]; \
	then \
		cd $(XMLSRCDIR)/com/syrus/AMFICOM/scheme/xml && $(RM) -r `cat .cvsignore`; \
	else \
		$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/scheme/xml; \
	fi
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/scheme/xml
	$(RM) .xml

#### Install ####
.PHONY: install
install: xmlinstall $(LIBNAME) $(LIBDIR)
	install -m 0644 $(LIBNAME) $(LIBDIR)

#### Uninstall ####
.PHONY: uninstall
uninstall: $(LIBDIR)/$(LIBNAME)
	$(RM) $<

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL_SOURCES) \
		$(SOURCES) \
		$(wildcard $(XMLDIR)/*.xsd) \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig

#### Distribution ####
.PHONY: dist
dist: arch
