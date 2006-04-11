#
# $Id: Makefile,v 1.19 2005/12/09 09:19:42 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := schemeclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

DEPENDENCIES = \
util \
general \
administration \
configuration \
measurement \
resource \
report \
map \
scheme \
mapview \
filter \
csbridge \
commonclient \
filterclient \
reportclient \
mapclient

XMLCLASSPATH := $(LIBDIR)/generalxml.jar:$(LIBDIR)/configurationxml.jar:$(LIBDIR)/schemexml.jar
EXTCLASSPATH := $(EXTLIBDIR)/jgraphpad.jar

SOURCES := $(shell find $(SRCDIR) -name '*.java')


$(LIBNAME):	.mach
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/schematics_ru.properties \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/config_ru.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/Lang/
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/resource/SchemeMessages.properties \
		$(SRCDIR)/com/syrus/AMFICOM/resource/SchemeMessages_ru.properties \
		$(SRCDIR)/com/syrus/AMFICOM/resource/EquipmentTypeCodenames.properties \
		$(SRCDIR)/com/syrus/AMFICOM/resource/EquipmentTypeCodenames_ru.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/resource/
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/client_/scheme/graph/messages.properties \
		$(SRCDIR)/com/syrus/AMFICOM/client_/scheme/graph/messages_ru.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/client_/scheme/graph/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach: $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(DEPRECATEDCLASSPATH) \
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
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/schematics_ru.properties \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/config_ru.properties \
		$(SRCDIR)/com/syrus/AMFICOM/resource/SchemeMessages.properties \
		$(SRCDIR)/com/syrus/AMFICOM/resource/SchemeMessages_ru.properties \
		$(SRCDIR)/com/syrus/AMFICOM/resource/EquipmentTypeCodenames.properties \
		$(SRCDIR)/com/syrus/AMFICOM/resource/EquipmentTypeCodenames_ru.properties \
		$(SRCDIR)/com/syrus/AMFICOM/client_/scheme/graph/messages.properties \
		$(SRCDIR)/com/syrus/AMFICOM/client_/scheme/graph/messages_ru.properties \
		scheme.properties \
		schemeeditor.sh
