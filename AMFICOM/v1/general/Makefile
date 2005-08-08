#
# $Id: Makefile,v 1.52 2005/08/08 17:11:05 max Exp $
#
# vim:set ft=make:
#

MODULE_NAME := general

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/general.idl

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

XMLDIR = xml

ifeq ($(shell hostname -s),bass)
XMLSRCDIR = $(SRCDIR)
XMLCLASSDIR = $(CLASSDIR)
else
XMLSRCDIR = $(XMLDIR)/src
XMLCLASSDIR = $(XMLDIR)/classes
endif

# If XMLBEANS_HOME is defined, search in ${XMLBEANS_HOME}/lib
ifeq ($(XMLBEANS_HOME),)
XMLBEANSCLASSPATH = $(EXTLIBDIR)/xbean.jar:$(EXTLIBDIR)/jsr173_api.jar
else
# XMLBEANS_HOME found. Find out whether this is an ant-build distribution
XMLBEANSLIBDIR = $(firstword $(wildcard $(XMLBEANS_HOME)/build/lib))
ifeq ($(XMLBEANSLIBDIR),)
XMLBEANSLIBDIR = $(XMLBEANS_HOME)/lib
endif
XMLBEANSCLASSPATH = $(XMLBEANSLIBDIR)/xbean.jar:$(XMLBEANSLIBDIR)/jsr173_api.jar
endif
LIBNAME_GENERALXML=generalxml.jar
LIBNAME_MAPXML=mapxml.jar

SCOMP=$(shell which scomp 2>/dev/null)
ifeq ($(SCOMP),)
SCOMP=$(XMLBEANS_HOME)/bin/scomp
endif

SCOMPFLAGS = -src $(XMLSRCDIR) -d $(XMLCLASSDIR)

APPCLASSPATH := $(LIBDIR)/util.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar:$(EXTLIBDIR)/xalan.jar:$(EXTLIBDIR)/xbean.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/general/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/security/*.java)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach: .corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/general/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/general/corba/.cvsignore)
	cd $(SRCDIR)/com/syrus/AMFICOM/security/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/security/corba/.cvsignore)
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) \
		$(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/general/corba/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/security/corba/*.java
	touch $@
#### XML ####
.PHONY: xml

.xml: $(LIBNAME_GENERALXML)
	touch $@
.PHONY: xmlinstall
xmlinstall: .xml $(LIBDIR)
	install -m 0644 $(LIBNAME_GENERALXML) $(LIBDIR)
$(LIBNAME_GENERALXML):
	$(RM) -r $(XMLCLASSDIR)/*
	$(SCOMP) $(SCOMPFLAGS) -out $@ \
		$(XMLDIR)/general.xsd
.PHONY: clean
clean: cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/security/corba
	$(RM) .corba

.PHONY: cleanxml xmlclean
cleanxml xmlclean:
	$(RM) $(LIBNAME_GENERALXML)
	$(RM) -r $(XMLSRCDIR)/com/syrus/amficom
	$(RM) -r $(XMLCLASSDIR)/com/syrus/amficom
	$(RM) .xml

#### Install ####
.PHONY: install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/general.idl \
		$(SOURCES)
