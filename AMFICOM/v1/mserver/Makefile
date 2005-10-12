#
# $Id: Makefile,v 1.40 2005/10/12 10:03:32 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mserver

LIBNAME := $(MODULE_NAME).jar
LIBNAME_INTERFACE := $(MODULE_NAME)_interface.jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/mserver.idl

HOST = mongol
SERVICE_NAME = mongol

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

DEPENDENCIES = \
general \
administration \
configuration \
measurement \
csbridge \
leserver_interface \
mcm_interface \
util

EXTCLASSPATH := $(TROVE4JCLASSPATH)


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/mserver/*.java)


all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.PHONY: interface
interface: $(LIBNAME_INTERFACE)

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/mserver/corba/MServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mserver/corba/MServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mserver/corba/_MServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mserver/corba/MServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mserver/corba/MServerHolder.class
	$(CP) $@ $(LIBDIR)

.mach: .corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mserver/corba
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/corba/*.java
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
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/mserver/corba
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
		$(IDLDIR)/mserver.idl \
		$(SOURCES) \
		mserver.properties \
		mserver.sh
