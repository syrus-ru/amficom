#
# $Id: Makefile,v 1.3 2005/08/29 11:09:35 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mapclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/general.jar:$(LIBDIR)/map.jar:$(LIBDIR)/filter.jar

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/event/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/map/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/resource/*.java)


$(LIBNAME):	.mach
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/client/resource/map_ru.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/client/resource/
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
		$(SRCDIR)
