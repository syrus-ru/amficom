#
# $Id: Makefile,v 1.21 2006/04/24 08:44:23 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := analysis
LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)


DEPENDENCIES = \
util \
extensions \
dadara \
general \
administration \
configuration \
measurement \
resource \
report \
filter \
reflectometry \
scheme \
csbridge \
commonclient \
filterclient \
reportclient \
mapclient

SOURCES := $(shell find $(SRCDIR) -name *.java)

$(LIBNAME): .mach
	$(CP) $(wildcard $(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/Lang
	mkdir -p $(CLASSDIR)/com/syrus/AMFICOM/resource
	$(CP) $(wildcard $(SRCDIR)/com/syrus/AMFICOM/resource/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/resource
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com \
		-C $(CLASSDIR) xml


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
		$(wildcard resources/xml/*.xml) \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/analysis_ru.properties \
		analysis.properties \
		analysis.sh
