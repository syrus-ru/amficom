#
# $Id: Makefile,v 1.15 2005/07/04 11:57:47 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := leserver

LIBNAME := $(MODULE_NAME).jar
LIBNAME_INTERFACE := $(MODULE_NAME)_interface.jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDLDIR := ../idl/idl

HOST = mongol
SERVICE_NAME = mongol

JAVAC=javac

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,IDLJ)


APPCLASSPATH := $(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/event.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/util.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/leserver/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/security/*.java)


all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
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
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServerHolder.class
	$(CP) $@ $(LIBDIR)

.mach: .corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idl
idl: .idl

.idl: $(SRCDIR)
	$(IDLJ) $(IDLJFLAGS) \
		-pkgPrefix general com.syrus.AMFICOM \
		-pkgPrefix security com.syrus.AMFICOM \
		-pkgPrefix administration com.syrus.AMFICOM \
		-pkgPrefix event com.syrus.AMFICOM \
		-pkgPrefix leserver com.syrus.AMFICOM \
		$(IDLDIR)/leserver.idl
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/leserver/corba
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/leserver/corba/*.java
	touch $@

$(SRCDIR):
$(CLASSDIR):
$(LIBDIR):
	mkdir -p $@

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
		$(SOURCES)
