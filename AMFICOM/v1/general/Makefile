#
# $Id: Makefile,v 1.36 2005/03/04 19:15:57 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = general_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/util.jar

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/general/*.java)
	touch $@

.PHONY: idl
idl: .idl

# Just idl-to-java compilation
.idl: $(SRCDIR)
#	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/general-vt.idl
	touch $@


.PHONY: corba
corba: .corba

# Compilation of stubs & skeletons
.corba:	$(CLASSDIR) .idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/general.idl
	$(JAVAC) \
		$(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/general/corba/*.java
	touch $@

$(SRCDIR):
$(CLASSDIR):
$(LIBDIR):
	mkdir -p $@


.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/general && $(RM) -r $(shell cat $(SRCDIR)/com/syrus/AMFICOM/general/.cvsignore)
	cd $(SRCDIR)/com/syrus/AMFICOM/general/corba && $(RM) -r $(shell cat $(SRCDIR)/com/syrus/AMFICOM/general/corba/.cvsignore)
	$(RM) .idl

.PHONY:	clean
clean:	cleanmach
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach: cleancorba
	$(RM) $(subst $$,\$$,$(wildcard $(CLASSDIR)/com/syrus/AMFICOM/general/*.class))
	$(RM) .mach

.PHONY: cleandeprecatedcorba
cleandeprecatedcorba:
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/CORBA/General
	$(RM) -r $(SCRDIR)/com/syrus/AMFICOM/CORBA/General

.PHONY:	cleancorba
cleancorba: idlclean cleandeprecatedcorba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Test ####
TESTDIR = test
TESTSOURCES := com/syrus/AMFICOM/general/DefaultIdentifierGeneratorServer.java \
		com/syrus/AMFICOM/general/CommonGeneralTest.java \
		com/syrus/AMFICOM/general/TestParameterType.java
JUNITCLASSPATH = /mnt/d/eclipse/3.1M4/plugins/org.junit_3.8.1/junit.jar

.PHONY:	test
test: $(LIBNAME)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(LIBNAME):$(APPCLASSPATH):$(JUNITCLASSPATH) \
		$(addprefix $(TESTDIR)/,$(TESTSOURCES))

.PHONY:	cleantest
cleantest:
	$(RM) $(addprefix $(CLASSDIR)/,$(subst .java,*.class,$(TESTSOURCES)))


#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/general.idl \
		$(SRCDIR)/com/syrus/AMFICOM/general/*.java
