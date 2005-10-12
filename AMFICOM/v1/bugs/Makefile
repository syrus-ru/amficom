#
# $Id: Makefile,v 1.5 2005/10/12 10:03:32 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := bugs

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

DEPENDENCIES =

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/bugs/*.java)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach: $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath "$(APPCLASSPATH)" \
		$(SOURCES)
	touch $@



.PHONY: clean
clean: cleanmach
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach


#### Install ####
.PHONY: install
install: $(LIBNAME) $(LIBDIR)
	install -m 0644 $(LIBNAME) $(LIBDIR)


#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SOURCES)

.javadocs: $(JAVADOCDIR)
	$(JAVADOC) \
		$(JAVADOCFLAGS) \
		-sourcepath $(SRCDIR) \
		-classpath "$(APPCLASSPATH)" \
		-subpackages "com.syrus.AMFICOM.bugs"
	touch $@
