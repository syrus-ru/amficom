#
# $Id: Makefile,v 1.10 2005/10/12 10:03:31 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mapviewclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)

DEPENDENCIES = \
util \
general \
administration \
configuration \
measurement \
report \
resource \
map \
scheme \
mapview \
filter \
csbridge \
commonclient \
filterclient \
reportclient \
mapclient \
schemeclient

XMLCLASSPATH := $(LIBDIR)/generalxml.jar:$(LIBDIR)/configurationxml.jar:$(LIBDIR)/mapxml.jar:$(LIBDIR)/schemexml.jar
EXTCLASSPATH := $(EXTLIBDIR)/jgraphpad.jar

SOURCES := $(shell find $(SRCDIR) -name '*.java')


$(LIBNAME):	.mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach: $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(EXTCLASSPATH) \
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
