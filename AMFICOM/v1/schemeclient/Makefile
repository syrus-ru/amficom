#
# $Id: Makefile,v 1.2 2004/07/23 11:14:06 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME = scheme_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

JAVAC=$(JAVA_HOME)/bin/javac

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)

.mach:	$(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(AMFICOMCLASSPATH):$(DEPRECATEDCLASSPATH):$(EXTCLASSPATH) \
		$(shell find $(SRCDIR) -name '*.java')
	touch $@


$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(shell find $(CLASSDIR) -name '*\.class')
	$(RM) .mach

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile 
