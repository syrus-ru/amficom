#
# $Id: Makefile,v 1.29 2005/02/07 12:13:20 arseniy Exp $
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
$(call checkenve,ORACLE_HOME)

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

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/general.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/general-vt.idl
	$(JAVAC) \
		$(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/general/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(wildcard $(CLASSDIR)/com/syrus/AMFICOM/general/*.class)
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	cd $(SRCDIR)/com/syrus/AMFICOM/general && $(RM) -r $(shell cat $(SRCDIR)/com/syrus/AMFICOM/general/.cvsignore)
	cd $(SRCDIR)/com/syrus/AMFICOM/general/corba && $(RM) -r $(shell cat $(SRCDIR)/com/syrus/AMFICOM/general/corba/.cvsignore)
	$(RM) -r $(SCRDIR)/com/syrus/AMFICOM/CORBA/General
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/CORBA/General
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)

#### Test ####
TESTDIR = test
TESTSOURCES := com/syrus/AMFICOM/general/DefaultIdentifierGeneratorServer.java
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

