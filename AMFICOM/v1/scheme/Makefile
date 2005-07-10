#
# $Id: Makefile,v 1.30 2005/07/10 13:41:29 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := scheme

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.bz2

IDL_SOURCES = $(IDLDIR)/scheme.idl

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)


# Directories that contain both hand-written and generated files.
DIRS_TO_SCRUB = \
	com/syrus/AMFICOM/scheme \
	com/syrus/AMFICOM/scheme/corba

SRCDIRS_TO_SCRUB = $(foreach dir,$(DIRS_TO_SCRUB),$(SRCDIR)/$(dir))

APPCLASSPATH = $(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/general.jar:$(LIBDIR)/map.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/util.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/scheme/*.java)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach: .corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/scheme/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/.cvsignore)
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/*.java
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
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/scheme/corba
	$(RM) .corba

#### Install ####
.PHONY: install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Uninstall ####
.PHONY: uninstall
uninstall: $(LIBDIR)/$(LIBNAME)
	$(RM) $<

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/scheme.idl \
		$(SOURCES)

#### Distribution ####
.PHONY: dist
dist: arch
