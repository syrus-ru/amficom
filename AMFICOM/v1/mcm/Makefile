#
# $Id: Makefile,v 1.71 2005/12/09 10:00:02 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mcm

LIBNAME := $(MODULE_NAME).jar
LIBNAME_INTERFACE := $(MODULE_NAME)_interface.jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/mcm.idl

HOST = mongol
SERVICE_NAME = mongol

TRANSCEIVER_DIR := ./transceiver

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)


LIBNAME_GENERALXML=generalxml.jar

JAVAH := javah
JAVAH_FLAGS := \
	-classpath $(APPCLASSPATH):$(CLASSDIR) \
	-force

DEPENDENCIES = \
general \
administration \
configuration \
measurement \
event \
csbridge \
reflectometry \
dadara \
leserver_interface \
mserver_interface \
util

EXTCLASSPATH := $(TROVE4JCLASSPATH)
XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_GENERALXML)


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/mcm/*.java)


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
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mcm/corba
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mcm/corba/*.java
	touch $@

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
		$(SOURCES) \
		mcm.properties \
		mcm.sh
