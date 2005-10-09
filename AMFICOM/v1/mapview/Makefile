#
# $Id: Makefile,v 1.22 2005/10/09 12:58:13 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mapview

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/mapview.idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)

LIBNAME_GENERALXML=generalxml.jar
LIBNAME_MAPXML=mapxml.jar
LIBNAME_SCHEMEXML=schemexml.jar

APPCLASSPATH := $(LIBDIR)/resource.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/map.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/util.jar
XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_SCHEMEXML):$(LIBDIR)/$(LIBNAME_MAPXML):$(LIBDIR)/$(LIBNAME_GENERALXML)
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/mapview/*.java)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach: .corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/mapview/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/mapview/corba/.cvsignore)
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mapview/corba/*.java
	touch $@

.PHONY: clean
clean: cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/mapview/corba
	$(RM) .corba

#### Install ####
.PHONY: install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/mapview.idl \
		$(SOURCES)

.PHONY: pmd
pmd:
	${JAVA} -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/map text $(PMD_RULES) 
