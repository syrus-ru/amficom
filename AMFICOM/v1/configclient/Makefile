MODULE_NAME = config_v1
MODULE_VERSION = $(shell date +%Y%m%d)

SRCDIR=./src
CLASSDIR=./classes
IDLDIR=../idl_v1/idl
LIBDIR=../lib
EXTLIBDIR=../../extlib

USER=amficom
PASSWORD=amficom
DBHOSTNAME=mongol
CONNECTSTRING=mongol

LIBNAME=configureclient.jar
ARCHFILE=configureclient.tar.gz

SRC_SUFFIXES = \
	.java \
	.generated.java

CLASS_SUFFIXES = \
	.class \
	$$%.class \
	_SJProfile0.class \
	_SJProfileKeys.class

ORACLECLASSPATH = $(ORACLE_HOME)/jdbc/lib/classes12.jar:$(ORACLE_HOME)/sqlj/lib/translator.jar:$(ORACLE_HOME)/sqlj/lib/runtime12ee.jar
APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/generalclient.jar
EXTCLASSPATH = $(EXTLIBDIR)/jgraphpad.jar:$(EXTLIBDIR)/jfreechartK.jar:$(EXTLIBDIR)/ofx.spatialfx.jar

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

SUNJAVACFLAGS = -target 1.4.2

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
	-source 1.4 \
	-g \
	-O \
	-deprecation


include Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)


$(LIBNAME): client
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.PHONY:	client
client:	.client
.client:	$(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) -nowarn \
		-classpath $(AMFICOMCLASSPATH)$(AURORACLASSPATH):$(VBJCLASSPATH):$(JDBCCLASSPATH):$(SQLJCLASSPATH):$(EXTCLASSPATH):$(DEPRECATEDCLASSPATH):$(CLASSDIR) \
		$(shell find $(SRCDIR)/com/syrus/AMFICOM/Client -name '*.java')
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanclient
	$(RM) -f $(LIBNAME)

.PHONY:	cleanclient
cleanclient:
	$(RM) \
		$(foreach file, $(filter-out $(foreach class, $(subst $(SRCDIR),$(CLASSDIR), $(subst .sqlj,, $(shell find $(SRCDIR) -name '*\.sqlj'))), $(foreach class_suffix, $(CLASS_SUFFIXES), $(class)$(class_suffix))), $(shell find $(CLASSDIR) -name '*\.class')), '$(file)') \
		.client
