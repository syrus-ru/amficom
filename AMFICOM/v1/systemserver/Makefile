#
# $Id: Makefile,v 1.1.1.1 2006/06/26 13:32:54 cvsadmin Exp $
#
# vim:set ft=make:
#

MODULE_NAME := systemserver

LIBNAME := $(MODULE_NAME).jar
LIBNAME_INTERFACE := $(MODULE_NAME)_interface.jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/systemserver.idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

DEPENDENCIES = \
general \
administration \
configuration \
measurement \
event \
resource \
report \
map \
scheme \
mapview \
csbridge \
reflectometry \
util

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/systemserver/*.java)

all:	$(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(CP) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/systemserver/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/systemserver/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.PHONY: interface
interface: $(LIBNAME_INTERFACE)

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/systemserver/corba
	$(CP) $@ $(LIBDIR)

.mach: .corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLBEANSCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/systemserver/corba
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLBEANSCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/systemserver/corba/*.java
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
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/systemserver/corba
	$(RM) .corba

#### Install ####
.PHONY: install
install: $(LIBNAME) $(LIBNAME_INTERFACE) $(LIBDIR)
	$(CP) $^


