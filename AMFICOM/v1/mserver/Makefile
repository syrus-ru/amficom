MODULE_NAME = mserver_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE= $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

HOST=mongol
SERVICE_NAME=mongol

APPCLASSPATH = $(LIBDIR)/mcm_interface.jar:$(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/event.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

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
	$(CP) $(LIBNAME_INTERFACE) $(LIBDIR)

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/DatabaseContextSetup.java \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/MServerMeasurementObjectLoader.java \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/MeasurementServer.java \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/MServerImplementation.java \
		$(SRCDIR)/com/syrus/AMFICOM/setup/MeasurementServerSetup.java
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) idl/mserver.idl
#
#	cpp -I../idl_v1/idl idl/mserver.idl idl/tmp_mserver.idl
#	$(IDLJ) $(IDLJFLAGS) idl/tmp_mserver.idl
#	$(RM) -r \
		idl/tmp_mserver.idl \
		$(SRCDIR)/com/syrus/AMFICOM/configuration \
		$(SRCDIR)/com/syrus/AMFICOM/CORBA \
		$(SRCDIR)/com/syrus/AMFICOM/event \
		$(SRCDIR)/com/syrus/AMFICOM/general \
		$(SRCDIR)/com/syrus/AMFICOM/measurement
#
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME) $(LIBNAME_INTERFACE)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(wildcard $(CLASSDIR)/com/syrus/AMFICOM/mserver/*.class)
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mserver/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/mserver/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBNAME_INTERFACE)
	$(CP) $(LIBNAME) $(LIBNAME_INTERFACE) $(LIBDIR)

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		idl/mserver.idl \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/*.java

