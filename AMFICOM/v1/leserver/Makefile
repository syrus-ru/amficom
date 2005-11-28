#
# $Id: Makefile,v 1.29 2005/11/28 12:29:07 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := leserver

LIBNAME := $(MODULE_NAME).jar
LIBNAME_INTERFACE := $(MODULE_NAME)_interface.jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/leserver.idl

HOST = mongol
SERVICE_NAME = mongol

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)


LIBNAME_GENERALXML = generalxml.jar

DEPENDENCIES = \
general \
administration \
configuration \
measurement \
event \
map \
scheme \
csbridge \
reflectometry \
util

EXTCLASSPATH := $(TROVE4JCLASSPATH):$(JAFCLASSPATH):$(JAVAMAILCLASSPATH)
XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_GENERALXML)


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/leserver/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/security/*.java)


all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(CP) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/leserver/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/leserver/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.PHONY: interface
interface: $(LIBNAME_INTERFACE)

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/_LoginServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServerHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/_EventServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServerHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServerPackage
	$(CP) $@ $(LIBDIR)

.mach: .corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/leserver/corba
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/leserver/corba/*.java
	touch $@

.PHONY: clean
clean: cleanmach cleancorba
	$(RM) $(LIBNAME) $(LIBNAME_INTERFACE)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/leserver/corba
	$(RM) .corba

#### Install ####
.PHONY: install
install: $(LIBNAME) $(LIBNAME_INTERFACE) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/leserver.idl \
		$(SOURCES) \
		leserver.properties \
		leserver.sh
