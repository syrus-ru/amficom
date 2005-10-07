#
# $Id: Makefile,v 1.1 2005/10/07 09:30:44 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := reflectometry

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)


APPCLASSPATH := 


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/reflectometry/*.java)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/reflectometry

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		$(SOURCES)
	touch $@

.PHONY:	clean
clean:	cleanmach
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

#### Install ####
.PHONY:	install
install:	$(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SOURCES)

