MODULE_NAME = configuration_v1

LIBNAME := $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE := $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR = ../idl_v1/idl

HOST = mongol
SERVICE_NAME = mongol

JAVACFLAGS = -source 1.3
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = -target 1.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)


APPCLASSPATH := $(LIBDIR)/administration.jar:$(LIBDIR)/general.jar:$(LIBDIR)/util.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/configuration/*.java)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/configuration.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/configuration/*.class
	rm -f .mach


.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/configuration/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/configuration/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)


#### Test ####

TESTDIR = test
TESTSOURCES := com/syrus/AMFICOM/configuration/CommonConfigurationTest.java \
	com/syrus/AMFICOM/configuration/TestTransmissionPathType.java \
	com/syrus/AMFICOM/configuration/TestTransmissionPath.java \
	com/syrus/AMFICOM/configuration/TestMonitoredElement.java

JUNITCLASSPATH = /mnt/d/eclipse/3.1M4/plugins/org.junit_3.8.1/junit.jar

.PHONY:	test
test: $(LIBNAME)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(LIBNAME):$(APPCLASSPATH):../general_v1/classes:$(JUNITCLASSPATH) \
		$(addprefix $(TESTDIR)/,$(TESTSOURCES))

.PHONY:	cleantest
cleantest:
	$(RM) $(addprefix $(CLASSDIR)/,$(subst .java,*.class,$(TESTSOURCES)))



#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/configuration.idl \
		$(SOURCES)

