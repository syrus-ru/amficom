MODULE_NAME = mserver_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE= $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

INCIDLDIR=../idl_v1/idl

HOST=mongol
SERVICE_NAME=mongol

APPCLASSPATH = $(LIBDIR)/mcm_interface.jar:$(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/servermisc.jar

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)

IDL2JAVA=$(JAVA_HOME)/bin/idlj
IDL2JAVAFLAGS = \
	-fall \
	-i $(IDLDIR) \
	-i $(INCIDLDIR) \
	-td $(SRCDIR) \
	-v


all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/mserver/corba/MServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mserver/corba/MServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mserver/corba/_MServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mserver/corba/MServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/mserver/corba/MServerHolder.class

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/DatabaseContextSetup.java \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/MeasurementServer.java \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/MServerImplementation.java
	touch $@

.corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVAFLAGS) \
		$(IDLDIR)/mserver.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	rm -f $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/mserver/*.class
	rm -f .mach

.PHONY:	cleancorba
cleancorba:
	rm -rf $(SRCDIR)/com/syrus/AMFICOM/mserver/corba
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/mserver/corba
	rm -f .corba

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/mserver.idl \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/*.java
