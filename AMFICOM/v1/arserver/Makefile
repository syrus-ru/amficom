MiODULE_NAME = arserver_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE= $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

#APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/event.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/mcm_interface.jar
APPCLASSPATH = $(LIBDIR)/general.jar:$(LIBDIR)/resource.jar

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,IDLJ)

all: $(LIBNAME_INTERFACE)

#d$(LIBNAME): .mach
#	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_INTERFACE):	.corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/rsserver/corba/ARServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/rsserver/corba/ARServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/rsserver/corba/_ARServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/rsserver/corba/ARServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/rsserver/corba/ARServerHolder.class

#.mach:	.corba $(CLASSDIR)
#	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/CMServerConfigurationObjectLoader.java \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/CMServerMeasurementObjectLoader.java \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/DatabaseContextSetup.java \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/CMConfigurationReceive.java \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/CMConfigurationMeasurementReceive.java \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/CMServerImpl.java \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/ClientMeasurementServer.java 
#	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) \
		$(IDLDIR)/arserver.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/arserver/corba/*.java
	touch $@

#$(CLASSDIR):
#	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME_INTERFACE)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(wildcard $(CLASSDIR)/com/syrus/AMFICOM/arserver/*.class)
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/arserver/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/arserver/corba
	$(RM) .corba

#### Install ####
#.PHONY:	install
#install: $(LIBNAME) $(LIBNAME_INTERFACE)
#	$(CP) $(LIBNAME) $(LIBNAME_INTERFACE) $(LIBDIR)

#### Archive ####
#.PHONY:	arch
#arch:
#	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/cmserver.idl \
		$(SRCDIR)/com/syrus/AMFICOM/cmserver/*.java

