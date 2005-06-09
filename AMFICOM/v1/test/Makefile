# Simple Makefile to compile single test class

TEST_CLASS = TestMCM
TEST_PACKAGE = com.syrus.AMFICOM.mcm

JAVA_SUFFIX = .java
CLASS_SUFFIX = .class

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

APPCLASSPATH := $(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/event.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/leserver_interface.jar:$(LIBDIR)/mcm_interface.jar:$(LIBDIR)/util.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar
JUNITCLASSPATH=$(ECLIPSE_HOME)/plugins/org.junit_3.8.1/junit.jar


all:	.mach

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(JUNITCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/$(subst .,/,$(TEST_PACKAGE))/$(TEST_CLASS)$(JAVA_SUFFIX)
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach

.PHONY:	cleanmach
cleanmach:
	$(RM) $(CLASSDIR)/$(subst .,/,$(TEST_PACKAGE))/$(TEST_CLASS)*$(CLASS_SUFFIX)
	$(RM) .mach
