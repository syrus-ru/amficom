MODULE_NAME = configuration_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

APPCLASSPATH = $(LIBDIR)/general.jar:$(LIBDIR)/util.jar

HOST=mongol
SERVICE_NAME=mongol

JAVACFLAGS = -source 1.3
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = -target 1.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/DatabaseConfigurationObjectLoader.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/ConfigurationObjectLoader.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/ConfigurationStorableObjectPool.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/ConfigurationDatabaseContext.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/DomainMember.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MonitoredDomainMember.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/Characterized.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/CharacteristicType.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/CharacteristicTypeDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/EquipmentType.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/EquipmentTypeDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/PortType.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/PortTypeDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MeasurementPortType.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MeasurementPortTypeDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/Characteristic.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/CharacteristicDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/User.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/UserDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/Domain.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/DomainDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/Server.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/ServerDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MCM.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MCMDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/Equipment.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/EquipmentDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/Port.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/PortDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/TransmissionPath.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/TransmissionPathDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/KIS.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/KISDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MeasurementPort.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MeasurementPortDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MonitoredElement.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MonitoredElementDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/DomainCondition.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/LinkedIdsCondition.java
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
	$(RM) -f $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) -rf $(CLASSDIR)/com/syrus/AMFICOM/configuration/*.class
	$(RM) -f .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -rf $(SRCDIR)/com/syrus/AMFICOM/configuration/corba
	$(RM) -rf $(CLASSDIR)/com/syrus/AMFICOM/configuration/corba
	$(RM) -f .corba

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/configuration.idl \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/*.java

#### Compile and install jar to libdir
lib: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)	
