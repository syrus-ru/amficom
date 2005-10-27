#
# $Id: Makefile,v 1.1 2005/10/27 10:58:11 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := observer

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)

DEPENDENCIES = \
util \
general \
administration \
measurement \
event \
scheme \
filter \
reflectometry \
dadara \
csbridge \
commonclient \
filterclient \
mapclient \
schemeclient \
mapviewclient \
scheduler \
analysis

XMLCLASSPATH := $(LIBDIR)/generalxml.jar
EXTCLASSPATH := $(EXTLIBDIR)/jgraphpad.jar

SOURCES := $(shell find $(SRCDIR) -name '*.java')


$(LIBNAME):	.mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach: $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH) \
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
		$(SOURCES) \
		Map.properties mapeditor.properties \
		mapeditor.sh
