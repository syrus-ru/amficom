#
# $Id: Makefile,v 1.62 2005/07/03 19:16:32 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mcm_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE := $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

HOST = mongol
SERVICE_NAME = mongol

JAVAC=javac

TRANSCEIVER_DIR := ./transceiver

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,IDLJ)

JAVAH := javah
JAVAH_FLAGS := \
	-classpath $(APPCLASSPATH):$(CLASSDIR) \
	-force


APPCLASSPATH := $(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/dadara.jar:$(LIBDIR)/leserver_interface.jar:$(LIBDIR)/mserver_interface.jar:$(LIBDIR)/util.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/mcm/*.java) \
	$(SRCDIR)/com/syrus/util/KISConnectionLRUMap.java


all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.PHONY: interface
interface: $(LIBNAME_INTERFACE)

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCM.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCMOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/_MCMStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCMHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mcm/corba/MCMHolder.class
	$(CP) $@ $(LIBDIR)

.head: .mach
	$(JAVAH) $(JAVAH_FLAGS) \
		-d $(TRANSCEIVER_DIR) \
		com.syrus.AMFICOM.mcm.TCPKISConnection
	touch $@

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
		-pkgPrefix configuration com.syrus.AMFICOM \
		-pkgPrefix measurement com.syrus.AMFICOM \
		-pkgPrefix mcm com.syrus.AMFICOM \
		$(IDLDIR)/mcm.idl
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mcm/corba
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/corba/*.java
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

.PHONY: cleanhead
cleanhead:
	$(RM) \
		$(TRANSCEIVER_DIR)/com_syrus_AMFICOM_mcm_TCPKISConnection.h
	$(RM) .head
	

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/mcm/corba
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
		$(IDLDIR)/mcm.idl \
		$(SOURCES)
