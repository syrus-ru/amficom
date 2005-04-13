#
# $Id: Makefile,v 1.15 2005/04/13 21:44:18 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME = generalclient_v1
MODULE_VERSION = $(shell date +%Y%m%d)
LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

include ../tools/Makefile.inc

APPCLASSPATH=$(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/corbaportable.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/map.jar:$(LIBDIR)/mapview.jar:$(LIBDIR)/event.jar:$(LIBDIR)/cmserver_interface.jar:$(LIBDIR)/mshserver_interface.jar
EXTCLASSPATH=$(EXTLIBDIR)/kunststoff.jar:$(EXTLIBDIR)/jfreechartK.jar

$(call checkenve,JAVA_HOME)


$(LIBNAME): .mach
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(shell find $(SRCDIR) -name '*.java')
	touch $@

.corba: $(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/AMFICOM.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/admin.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/client.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/report.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/alarm.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/constants.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/CORBA/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@
	

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) -r \
		$(CLASSDIR)/com/syrus/AMFICOM/administration \
		$(CLASSDIR)/com/syrus/AMFICOM/client_ \
		$(CLASSDIR)/com/syrus/AMFICOM/Client \
		$(CLASSDIR)/com/syrus/AMFICOM/cmserver \
		$(CLASSDIR)/com/syrus/AMFICOM/configuration \
		$(CLASSDIR)/com/syrus/AMFICOM/corba \
		$(CLASSDIR)/com/syrus/AMFICOM/general \
		$(CLASSDIR)/com/syrus/AMFICOM/map \
		$(CLASSDIR)/com/syrus/AMFICOM/mapview \
		$(CLASSDIR)/com/syrus/AMFICOM/measurement \
		$(CLASSDIR)/com/syrus/AMFICOM/resource \
		$(CLASSDIR)/com/syrus/AMFICOM/scheme
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r \
		$(CLASSDIR)/com/syrus/AMFICOM/CORBA \
		$(CLASSDIR)/com/syrus/AMFICOM/event \
		$(SRCDIR)/com/syrus/AMFICOM/CORBA \
		$(SRCDIR)/com/syrus/AMFICOM/event
	$(RM) .corba


#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

