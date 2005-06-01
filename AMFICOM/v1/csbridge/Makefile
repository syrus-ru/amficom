#
# $Id: Makefile,v 1.10 2005/06/01 15:59:42 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME = csbridge_v1
MODULE_VERSION = $(shell date +%Y%m%d)
LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/event.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/map.jar:$(LIBDIR)/mapview.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/leserver_interface.jar:$(LIBDIR)/cmserver_interface.jar:$(LIBDIR)/mshserver_interface.jar:$(LIBDIR)/arserver_interface.jar

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/general/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/administration/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/configuration/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/measurement/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/event/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/resource/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/map/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/scheme/*.java)


$(LIBNAME): .mach
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/general \
		-C $(CLASSDIR) com/syrus/AMFICOM/administration \
		-C $(CLASSDIR) com/syrus/AMFICOM/configuration \
		-C $(CLASSDIR) com/syrus/AMFICOM/measurement \
		-C $(CLASSDIR) com/syrus/AMFICOM/event \
		-C $(CLASSDIR) com/syrus/AMFICOM/resource \
		-C $(CLASSDIR) com/syrus/AMFICOM/map \
		-C $(CLASSDIR) com/syrus/AMFICOM/scheme

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SOURCES)
	touch $@

.corba: $(CLASSDIR)
	touch $@

$(CLASSDIR):
	mkdir -p $@
	

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) -r \
		$(CLASSDIR)/com/syrus/AMFICOM/general \
		$(CLASSDIR)/com/syrus/AMFICOM/administration \
		$(CLASSDIR)/com/syrus/AMFICOM/configuration \
		$(CLASSDIR)/com/syrus/AMFICOM/measurement \
		$(CLASSDIR)/com/syrus/AMFICOM/map \
		$(CLASSDIR)/com/syrus/AMFICOM/mapview \
		$(CLASSDIR)/com/syrus/AMFICOM/scheme
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) .corba


#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

