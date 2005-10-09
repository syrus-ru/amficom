#
# $Id: Makefile,v 1.9 2005/10/09 12:58:13 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mapviewclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/report.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/map.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/mapview.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/commonclient.jar:$(LIBDIR)/filterclient.jar:$(LIBDIR)/reportclient.jar:$(LIBDIR)/mapclient.jar:$(LIBDIR)/schemeclient.jar
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
