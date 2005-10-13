#
# $Id: Makefile,v 1.4 2005/10/13 09:37:44 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := reportclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)


DEPENDENCIES = \
general \
report \
resource \
commonclient \
util


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/report/*.java)


$(LIBNAME):	.mach
	$(CP) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/report/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/client/report
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach: $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(APPCLASSPATH) \
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
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^


#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SOURCES)
