MODULE_NAME = configuration_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

APPCLASSPATH = $(LIBDIR)/administration.jar:$(LIBDIR)/general.jar:$(LIBDIR)/util.jar

HOST=mongol
SERVICE_NAME=mongol

JAVACFLAGS = -source 1.3
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = -target 1.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)


SOURCES := com/syrus/AMFICOM/configuration/DatabaseConfigurationObjectLoader.java \
		com/syrus/AMFICOM/configuration/ConfigurationObjectLoader.java \
		com/syrus/AMFICOM/configuration/ConfigurationStorableObjectPool.java \
		com/syrus/AMFICOM/configuration/ConfigurationDatabaseContext.java \
		com/syrus/AMFICOM/configuration/MonitoredDomainMember.java \
		com/syrus/AMFICOM/configuration/AbstractLinkType.java \
		com/syrus/AMFICOM/configuration/CableLinkType.java \
		com/syrus/AMFICOM/configuration/CableLinkTypeDatabase.java \
		com/syrus/AMFICOM/configuration/CableThread.java \
		com/syrus/AMFICOM/configuration/CableThreadDatabase.java \
		com/syrus/AMFICOM/configuration/CableThreadType.java \
		com/syrus/AMFICOM/configuration/CableThreadTypeDatabase.java \
		com/syrus/AMFICOM/configuration/Equipment.java \
		com/syrus/AMFICOM/configuration/EquipmentDatabase.java \
		com/syrus/AMFICOM/configuration/EquipmentType.java \
		com/syrus/AMFICOM/configuration/EquipmentTypeDatabase.java \
		com/syrus/AMFICOM/configuration/KIS.java \
		com/syrus/AMFICOM/configuration/KISDatabase.java \
		com/syrus/AMFICOM/configuration/Link.java \
		com/syrus/AMFICOM/configuration/LinkDatabase.java \
		com/syrus/AMFICOM/configuration/LinkType.java \
		com/syrus/AMFICOM/configuration/LinkTypeDatabase.java \
		com/syrus/AMFICOM/configuration/MeasurementPort.java \
		com/syrus/AMFICOM/configuration/MeasurementPortDatabase.java \
		com/syrus/AMFICOM/configuration/MeasurementPortType.java \
		com/syrus/AMFICOM/configuration/MeasurementPortTypeDatabase.java \
		com/syrus/AMFICOM/configuration/MonitoredElement.java \
		com/syrus/AMFICOM/configuration/MonitoredElementDatabase.java \
		com/syrus/AMFICOM/configuration/Port.java \
		com/syrus/AMFICOM/configuration/PortDatabase.java \
		com/syrus/AMFICOM/configuration/PortType.java \
		com/syrus/AMFICOM/configuration/PortTypeDatabase.java \
		com/syrus/AMFICOM/configuration/TransmissionPath.java \
		com/syrus/AMFICOM/configuration/TransmissionPathDatabase.java \
		com/syrus/AMFICOM/configuration/TransmissionPathType.java \
		com/syrus/AMFICOM/configuration/TransmissionPathTypeDatabase.java \
		com/syrus/AMFICOM/configuration/StringFieldCondition.java \
		com/syrus/AMFICOM/configuration/LinkedIdsConditionImpl.java


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(addprefix $(SRCDIR)/,$(SOURCES))
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
	$(RM) $(addprefix $(CLASSDIR)/,$(subst .java,.class,$(SOURCES)))
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/configuration/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/configuration/corba
	$(RM) .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/configuration.idl \
		$(addprefix $(SRCDIR)/,$(SOURCES))

