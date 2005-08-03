#
# $Id: Makefile,v 1.8 2005/08/03 14:58:59 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := schemeclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

JAVAC=$(JAVA_HOME)/bin/javac

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/map.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/commonclient.jar:$(LIBDIR)/filterclient.jar
EXTCLASSPATH := $(EXTLIBDIR)/jgraphpad.jar


$(LIBNAME):	.mach
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/schematics_ru.properties \
		$(SRCDIR)/com/syrus/AMFICOM/Client/General/Lang/config_ru.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/Client/General/Lang/
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/resource/scheme_messages.properties \
		$(SRCDIR)/com/syrus/AMFICOM/resource/scheme_messages_ru.properties \
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
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(DEPRECATEDCLASSPATH) \
		$(shell find $(SRCDIR) -name '*.java')
	touch $@

.PHONY: clean
clean: cleanmach
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(shell find $(CLASSDIR) -name '*\.class')
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
		$(SRCDIR)
