#
# $Id: Makefile,v 1.10 2004/12/29 10:52:30 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME = generalclient_v1
MODULE_VERSION = $(shell date +%Y%m%d)
LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

include ../tools/Makefile.inc

APPCLASSPATH=$(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/corbaportable.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/map.jar

$(call checkenve,JAVA_HOME)


$(LIBNAME): .mach
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) -nowarn \
		-classpath $(APPCLASSPATH):$(DEPRECATEDCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(shell find $(SRCDIR) -name '*.java')
	touch $@


.PHONY:	clean

clean:
	$(RM) -r $(CLASSDIR)/com
	$(RM) $(LIBNAME)


#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)

