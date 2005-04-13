#
# $Id: Makefile,v 1.14 2005/04/13 20:29:08 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME = generalclient_v1
MODULE_VERSION = $(shell date +%Y%m%d)
LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

include ../tools/Makefile.inc

APPCLASSPATH=$(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/corbaportable.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/map.jar:$(LIBDIR)/mapview.jar:$(LIBDIR)/event.jar:$(LIBDIR)/cmserver_interface.jar

$(call checkenve,JAVA_HOME)


$(LIBNAME): .mach
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) -nowarn \
		-classpath $(APPCLASSPATH) \
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
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/CORBA
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/event
	$(RM) .corba


#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

