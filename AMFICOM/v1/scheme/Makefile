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


IDL_GENERATED_SCHEMA_SOURCES = $(addprefix $(SRCDIR)/com/syrus/AMFICOM/scheme/corba/, $(shell cat $(SRCDIR)/com/syrus/AMFICOM/scheme/corba/.cvsignore))

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com


.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/scheme/*.java)
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-configuration.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-java.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-map.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-resource.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-vt.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/scheme/corba/*.java
	touch $@


$(CLASSDIR):
	mkdir -p $@


.PHONY:	clean cleanmach cleancorba

clean:	cleanmach cleancorba
	rm -f $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(wildcard $(CLASSDIR)/com/syrus/AMFICOM/scheme/*.class)
	$(RM) .mach


cleancorba:
	$(RM) -r $(subst .java, .class, $(IDL_GENERATED_SCHEMA_SOURCES))
	$(RM) -r $(IDL_GENERATED_SCHEMA_SOURCES)
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/configuration
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/configuration
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/map
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/map
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/resource
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/resource
	$(RM) .corba

#### Install ####
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

