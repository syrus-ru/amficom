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
$(call checkenve,IDL2JAVA)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/ConfigurationDatabaseContext.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/CharacteristicType.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/CharacteristicTypeDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/DomainMember.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/Characterized.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/Characteristic.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/CharacteristicDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/Equipment.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/EquipmentDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/KIS.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/KISDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MCM.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MCMDatabase.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MonitoredElement.java \
		$(SRCDIR)/com/syrus/AMFICOM/configuration/MonitoredElementDatabase.java
	touch $@

.corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVAFLAGS) \
		$(IDLDIR)/configuration.idl
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
