#
# $Id: Makefile,v 1.17 2005/07/07 16:41:21 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mscharserver

LIBNAME := $(MODULE_NAME).jar
LIBNAME_INTERFACE := $(MODULE_NAME)_interface.jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDLDIR := ../idl/idl

HOST = mongol
SERVICE_NAME = mongol

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,IDLJ)


APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/map.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/mapview.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/leserver_interface.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar:$(EXTLIBDIR)/jdom.jar:$(EXTLIBDIR)/micsys.jar:$(EXTLIBDIR)/miutil.jar:$(EXTLIBDIR)/mxj.jar:$(EXTLIBDIR)/mxjgeom.jar


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

.PHONY: idl
idl: .idl

.idl: $(SRCDIR)
	$(IDLJ) $(IDLJFLAGS) \
		-pkgPrefix general com.syrus.AMFICOM \
		-pkgPrefix security com.syrus.AMFICOM \
		-pkgPrefix resource com.syrus.AMFICOM \
		-pkgPrefix map com.syrus.AMFICOM \
		-pkgPrefix scheme com.syrus.AMFICOM \
		-pkgPrefix mapview com.syrus.AMFICOM \
		-pkgPrefix mscharserver com.syrus.AMFICOM \
		$(IDLDIR)/mscharserver.idl
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
		$(SOURCES)

lib: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)
