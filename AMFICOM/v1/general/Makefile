MODULE_NAME = general_v1
MODULE_VERSION = $(shell date +%Y%m%d)

SRCDIR=./src
CLASSDIR=./classes
IDLDIR=../idl_v1/idl
LIBDIR=../lib
EXTLIBDIR=../../extlib

DBHOSTNAME=research
CONNECTSTRING=shaitan
USER = amficom
PASSWORD = amficom

LIBNAME = general.jar
ARCHFILE=general.tar.gz

ORACLECLASSPATH = $(ORACLE_HOME)/jdbc/lib/classes12.jar:$(ORACLE_HOME)/sqlj/lib/translator.jar:$(ORACLE_HOME)/sqlj/lib/runtime12ee.jar
APPCLASSPATH = $(LIBDIR)/util.jar

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)

IDL2JAVA = idl2java
IDL2JAVAFLAGS = \
	-C \
	-H \
	-no_bind \
	-no_examples \
	-no_tie \
	-narrow_compliance \
	-portable \
	-strict \
	-root_dir $(SRCDIR) \
	-I$(IDLDIR)

#JAVAC = javac
JAVAC = jikes
JIKESFLAGS = \
	-target 1.4.2 \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	-extdirs $(JAVA_HOME)/jre/lib/ext \
	+E \
	+Pmodifier-order \
	+Predundant-modifiers \
	+Pswitchcheck \
	+Pnaming-convention \
	+T=2
JAVACFLAGS = \
	-d $(CLASSDIR) \
	-g \
	-O \
	-deprecation
JAVACFLAGS := $(JAVACFLAGS) $(JIKESFLAGS)
JAVAC_WARNOFF_FLAG=-nowarn


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/general/*.java
	touch $@

.corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVAFLAGS) \
		$(IDLDIR)/general.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/general/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	rm -f $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/general/*.class
	rm -f .mach

.PHONY:	cleancorba
cleancorba:
	rm -rf $(SRCDIR)/com/syrus/AMFICOM/general/corba
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/general/corba
	rm -f .corba

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/general.idl \
		$(SRCDIR)/com/syrus/AMFICOM/general/*.java
