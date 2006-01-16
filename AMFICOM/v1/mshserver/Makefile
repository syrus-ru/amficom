#
# $Id: Makefile,v 1.24 2006/01/16 19:09:59 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mscharserver

LIBNAME := $(MODULE_NAME).jar
LIBNAME_INTERFACE := $(MODULE_NAME)_interface.jar
ARCHFILE := $(MODULE_NAME).tar.gz

MAPINFO_LIBDIR=$(EXTLIBDIR)/mapinfo/common

IDL_SOURCES = $(IDLDIR)/mscharserver.idl

HOST = mongol
SERVICE_NAME = mongol

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

DEPENDENCIES = \
util \
general \
resource \
administration \
configuration \
measurement \
map \
scheme \
mapview \
csbridge \
leserver_interface

EXTCLASSPATH := $(TROVE4JCLASSPATH):$(MAPINFO_LIBDIR)/jdom.jar:$(MAPINFO_LIBDIR)/micsys.jar:$(MAPINFO_LIBDIR)/miutil.jar:$(MAPINFO_LIBDIR)/mxj.jar:$(MAPINFO_LIBDIR)/mxjgeom.jar:$(EXTLIBDIR)/dom4j.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/mscharserver/*.java)


all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.PHONY: interface
interface: $(LIBNAME_INTERFACE)

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/mscharserver/corba/MscharServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mscharserver/corba/MscharServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mscharserver/corba/_MscharServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mscharserver/corba/MscharServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mscharserver/corba/MscharServerHolder.class
	$(CP) $@ $(LIBDIR)

.mach: .corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mscharserver/corba
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mscharserver/corba/*.java
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
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/mscharserver/corba
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
		$(IDLDIR)/mscharserver.idl \
		$(SOURCES) \
		mscharserver.properties \
		mscharserver.sh \
		map_descriptor.xml

lib: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)
