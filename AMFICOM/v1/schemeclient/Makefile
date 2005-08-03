#
# $Id: Makefile,v 1.7 2005/08/03 09:53:32 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := schemeclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

JAVAC=$(JAVA_HOME)/bin/javac

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/map.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/commonclient.jar:$(LIBDIR)/filterclient.jar
EXTCLASSPATH := $(EXTLIBDIR)/jgraphpad.jar


.mach: $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(DEPRECATEDCLASSPATH) \
		$(shell find $(SRCDIR) -name '*.java')
	touch $@

.PHONY: clean
clean: cleanmach
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(shell find $(CLASSDIR) -name '*\.class')
	$(RM) .mach

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile 
