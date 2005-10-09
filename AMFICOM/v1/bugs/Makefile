#
# $Id: Makefile,v 1.3 2005/10/09 12:58:12 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := bugs

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/bugs/*.java)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach: $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
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
