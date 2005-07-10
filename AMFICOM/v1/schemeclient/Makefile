#
# $Id: Makefile,v 1.6 2005/07/10 13:41:28 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := schemeclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

JAVAC=$(JAVA_HOME)/bin/javac

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)

.mach: $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(AMFICOMCLASSPATH):$(DEPRECATEDCLASSPATH):$(EXTCLASSPATH) \
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
