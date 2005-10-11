#
# $Id: Makefile,v 1.13 2005/10/11 14:11:33 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := analysis
LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)


APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/dadara.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/reflectometry.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/commonclient.jar:$(LIBDIR)/filterclient.jar


SOURCES := $(shell find $(SRCDIR) -name *.java)

$(LIBNAME): .mach
	$(CP) $(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/analysis_ru.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/Lang
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
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
.PHONY: install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SOURCES) \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/analysis_ru.properties \
		Application.properties analysis.properties \
		analysis.sh
