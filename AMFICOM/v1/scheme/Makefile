MODULE_NAME := scheme_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/configuration.jar

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

JAVAC=javac
JAVACFLAGS=-O \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	-d $(CLASSDIR) \
	-deprecation \
	-encoding $(ENCODING) \
	-extdirs $(JAVA_HOME)/jre/lib/ext \
	-g

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
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/scheme/*.java)
	touch $@

.PHONY: idl
idl: .idl

# Just idl-to-java compilation
.idl: $(SRCDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-configuration.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-map.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-resource.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-vt.idl
	touch $@


.PHONY: corba
corba: .corba

# Compilation of stubs & skeletons
.corba:	$(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/configuration
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/map
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/resource
	$(RM) -r $(IDL_GENERATED_SCHEMA_SOURCES)
	$(RM) .idl

.PHONY:	clean
clean: cleanmach
	rm -f $(LIBNAME)

.PHONY:	cleanmach
cleanmach: cleancorba
	$(RM) $(wildcard $(CLASSDIR)/com/syrus/AMFICOM/scheme/*.class)
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/configuration
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/map
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/resource
	$(RM) -r $(IDL_GENERATED_SCHEMA_CLASSES)
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)

#### Archive ####
.PHONY:	arch
arch:
	echo $(IDL_GENERATED_SCHEMA_SOURCES)
	tar -czvf $(ARCHFILE) \
		$(IDLDIR)/scheme-configuration.idl \
		$(IDLDIR)/scheme-java.idl \
		$(IDLDIR)/scheme-map.idl \
		$(IDLDIR)/scheme-resource.idl \
		$(IDLDIR)/scheme-vt.idl \
		Makefile \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/*.java \
		$(filter-out $(wildcard $(IDL_GENERATED_SCHEMA_SOURCES)), $(wildcard $(SRCDIR)/com/syrus/AMFICOM/scheme/corba/*.java))
