#
# $Id: Makefile,v 1.2 2005/10/09 12:58:13 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := reportclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/report.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/commonclient.jar

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/report/*.java)


$(LIBNAME):	.mach
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
