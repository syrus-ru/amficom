MODULE_NAME := scheme_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.bz2

IDLDIR=../idl_v1/idl

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
IDL_GENERATED_SCHEMA_SOURCES = $(foreach \
	srcdir,\
	$(SRCDIRS_TO_SCRUB),\
	$(addprefix \
		$(srcdir)/,\
		$(shell cat $(srcdir)/.cvsignore)))
IDL_GENERATED_SCHEMA_CLASSES = $(subst \
	$(SRCDIR),\
	$(CLASSDIR),\
	$(foreach \
		srcdir,\
		$(SRCDIRS_TO_SCRUB),\
		$(addprefix \
			$(srcdir)/,\
			$(subst \
				.java,\
				.class,\
				$(shell cat $(srcdir)/.cvsignore)))))

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(AMFICOMCLASSPATH):$(CLASSDIR) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/scheme/*.java)
	touch $@

.PHONY: idl
idl: .idl

# Just idl-to-java compilation
.idl: $(SRCDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-administration.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-configuration.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-general.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-map.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-resource.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-vt.idl
	touch $@


.PHONY: corba
corba: .corba

# Compilation of stubs & skeletons
.corba:	$(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(AMFICOMCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/*.java
	touch $@

$(SRCDIR):
$(CLASSDIR):
$(LIBDIR):
	mkdir -p $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/administration
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/configuration
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/general
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/map
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/resource
	$(RM) -r $(IDL_GENERATED_SCHEMA_SOURCES)
	$(RM) .idl

.PHONY:	clean
clean: cleanmach
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach: cleancorba
	$(RM) $(wildcard $(CLASSDIR)/com/syrus/AMFICOM/scheme/*.class)
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/administration
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/configuration
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/map
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/resource
	$(RM) -r $(IDL_GENERATED_SCHEMA_CLASSES)
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $<

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
		$(filter-out $(wildcard $(IDL_GENERATED_SCHEMA_SOURCES)), $(wildcard $(SRCDIR)/com/syrus/AMFICOM/scheme/corba/*.java)) \
		| bzip2 -9 >$(ARCHFILE)
	$(RM) *.idl

#### Archive ####
.PHONY:	arch
arch: dist
