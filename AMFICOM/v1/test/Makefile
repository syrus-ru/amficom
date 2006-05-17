# Simple Makefile to compile single test class

MODULE_NAME := test
ARCHFILE := $(MODULE_NAME).tar.gz

#TEST_CLASS = Setup3
#TEST_PACKAGE = com.syrus.AMFICOM.setup
#TEST_CLASS=TestIdentifier
#TEST_PACKAGE=com.syrus.AMFICOM.general
#TEST_CLASS=TestLRUMap
#TEST_PACKAGE=com.syrus.util
#TEST_CLASS=TestSystemUser
#TEST_PACKAGE=com.syrus.AMFICOM.administration
#TEST_CLASS=SetupEquipmentType
TEST_CLASS=ConvertEquipmentType
TEST_PACKAGE=com.syrus.AMFICOM.configuration

JAVA_SUFFIX = .java
CLASS_SUFFIX = .class

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/event.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/reflectometry.jar:$(LIBDIR)/leserver_interface.jar:$(LIBDIR)/mserver_interface.jar:$(LIBDIR)/cmserver_interface.jar:$(LIBDIR)/mcm_interface.jar:$(LIBDIR)/commonclient.jar:$(LIBDIR)/util.jar
XMLCLASSPATH := $(LIBDIR)/configurationxml.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar
JUNITCLASSPATH=$(ECLIPSE_HOME)/plugins/org.junit_3.8.1/junit.jar


all:	.mach
	mkdir -p $(CLASSDIR)/com/syrus/AMFICOM/setup
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/setup/*.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/setup

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(JUNITCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/$(subst .,/,$(TEST_PACKAGE))/$(TEST_CLASS)$(JAVA_SUFFIX)
	touch $@


.PHONY:	clean
clean:	cleanmach

.PHONY:	cleanmach
cleanmach:
#	$(RM) $(CLASSDIR)/$(subst .,/,$(TEST_PACKAGE))/$(TEST_CLASS)*$(CLASS_SUFFIX)
	$(RM) -r $(CLASSDIR)/com
	$(RM) .mach


#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SRCDIR) \
		test.properties \
		runtest
