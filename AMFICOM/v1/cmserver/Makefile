MODULE_NAME = cmserver_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE= $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

HOST=mongol
SERVICE_NAME=mongol

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,IDLJ)

APPCLASSPATH := $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/event.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/mcm_interface.jar

all: $(LIBNAME) $(LIBNAME_INTERFACE)


SOURCES := com/syrus/AMFICOM/cmserver/CMServerConfigurationObjectLoader.java \
	com/syrus/AMFICOM/cmserver/CMServerMeasurementObjectLoader.java \
	com/syrus/AMFICOM/cmserver/DatabaseContextSetup.java \
	com/syrus/AMFICOM/cmserver/CMGeneralReceive.java \
	com/syrus/AMFICOM/cmserver/CMAdministrationReceive.java \
	com/syrus/AMFICOM/cmserver/CMConfigurationReceive.java \
	com/syrus/AMFICOM/cmserver/CMMeasurementReceive.java \
	com/syrus/AMFICOM/cmserver/CMGeneralTransmit.java \
	com/syrus/AMFICOM/cmserver/CMAdministrationTransmit.java \
	com/syrus/AMFICOM/cmserver/CMConfigurationTransmit.java \
	com/syrus/AMFICOM/cmserver/CMMeasurementTransmit.java \
	com/syrus/AMFICOM/cmserver/CMServerImpl.java \
	com/syrus/AMFICOM/cmserver/ClientMeasurementServer.java


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
		$(addprefix $(SRCDIR)/,$(SOURCES))
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) \
		$(IDLDIR)/cmserver.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME) $(LIBNAME_INTERFACE)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(addprefix $(CLASSDIR)/,$(subst .java,.class,$(SOURCES)))
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/cmserver/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/cmserver/corba
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
		$(IDLDIR)/cmserver.idl \
		$(addprefix $(SRCDIR)/,$(SOURCES))

