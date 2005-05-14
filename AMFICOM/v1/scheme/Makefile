#
# $Id: Makefile,v 1.20 2005/05/14 09:50:16 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := scheme_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.bz2

IDLDIR=../idl_v1/idl

ENCODING = KOI8-R
JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

# Directories that contain both hand-written and generated files.
DIRS_TO_SCRUB = \
	com/syrus/AMFICOM/scheme \
	com/syrus/AMFICOM/scheme/corba

SRCDIRS_TO_SCRUB = $(foreach dir,$(DIRS_TO_SCRUB),$(SRCDIR)/$(dir))
APPCLASSPATH = $(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/general.jar:$(LIBDIR)/map.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/util.jar

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/scheme/*.java)
	touch $@

.PHONY: idl
idl: .idl

# Just idl-to-java compilation
.idl: $(SRCDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme.idl
	touch $@


.PHONY: corba
corba: .corba

# Compilation of stubs & skeletons
.corba:	$(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-sourcepath $(SRCDIR) \
		-classpath $(APPCLASSPATH) \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/*.java
	touch $@

$(SRCDIR):
$(CLASSDIR):
$(LIBDIR):
	mkdir -p $@

.PHONY: idlclean
idlclean:
	$(RM) .idl

.PHONY:	clean
clean: cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(subst $$,\$$,$(wildcard $(CLASSDIR)/com/syrus/AMFICOM/scheme/*.class))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) .corba
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/scheme/corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Uninstall ####
.PHONY: uninstall
uninstall: $(LIBDIR)/$(LIBNAME)
	$(RM) $<

#### Distribution ####
.PHONY: dist
dist:
	-ln -s \
		$(IDLDIR)/scheme-administration.idl \
		$(IDLDIR)/scheme-configuration.idl \
		$(IDLDIR)/scheme-general.idl \
		$(IDLDIR)/scheme-map.idl \
		$(IDLDIR)/scheme-resource.idl \
		$(IDLDIR)/scheme-vt.idl \
		.
	tar ch \
		scheme-administration.idl \
		scheme-configuration.idl \
		scheme-general.idl \
		scheme-map.idl \
		scheme-resource.idl \
		scheme-vt.idl \
		Makefile \
		README \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/*.java \
		| bzip2 -9 >$(ARCHFILE)
	$(RM) *.idl

#### Archive ####
.PHONY:	arch
arch: dist
