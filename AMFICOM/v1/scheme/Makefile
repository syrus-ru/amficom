#
# $Id: Makefile,v 1.18 2005/03/17 13:08:15 bass Exp $
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
_CLASSPATH = $(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/corba.jar:$(LIBDIR)/general.jar:$(LIBDIR)/map.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/util.jar

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-sourcepath $(SRCDIR) \
		-classpath $(_CLASSPATH):$(CLASSDIR) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/scheme/*.java)
	touch $@

.PHONY: idl
idl: .idl

# Just idl-to-java compilation
.idl: $(SRCDIR)
#	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-administration.idl
#	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-configuration.idl
#	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-general.idl
#	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-map.idl
#	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-resource.idl
#	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-vt.idl
	touch $@


.PHONY: corba
corba: .corba

# Compilation of stubs & skeletons
.corba:	$(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-sourcepath $(SRCDIR) \
		-classpath $(_CLASSPATH):$(CLASSES) \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/AbstractSchemePortPackage/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/PathElementPackage/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/SchemeOptimizeInfoPackage/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/SchemePackage/*.java
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
