MODULE_NAME = cmserver_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE= $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

HOST=mongol
SERVICE_NAME=mongol

APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/servermisc.jar

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDLJ)

all: $(LIBNAME) $(LIBNAME_INTERFACE)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/cmserver/corba/CMServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/cmserver/corba/CMServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/cmserver/corba/_CMServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/cmserver/corba/CMServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/cmserver/corba/CMServerHolder.class

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/CMServerImpl.java \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/ClientMeasurementServer.java \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/DatabaseContextSetup.java
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) \
		idl/cmserver.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	rm -f $(LIBNAME) $(LIBNAME_INTERFACE)

.PHONY:	cleanmach
cleanmach:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/cmserver/*.class
	rm -f .mach

.PHONY:	cleancorba
cleancorba:
	rm -rf $(SRCDIR)/com/syrus/AMFICOM/cmserver/corba
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/cmserver/corba
	rm -f .corba

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/cmserver.idl \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/*.java


lib: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)
