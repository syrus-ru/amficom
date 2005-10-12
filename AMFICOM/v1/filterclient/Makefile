#
# $Id: Makefile,v 1.10 2005/10/12 10:03:30 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := filterclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

DEPENDENCIES = \
general \
administration \
configuration \
measurement \
resource \
filter \
commonclient \
util

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/filterclient/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/filter/UI/*.java)


all: $(LIBNAME)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
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
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SOURCES)
