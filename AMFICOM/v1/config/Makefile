MODULE_NAME = config_v1
MODULE_VERSION = $(shell date +%Y%m%d)

SRCDIR=./src
CLASSDIR=./classes
IDLDIR=../idl_v1/idl
LIBDIR=../lib
EXTLIBDIR=../../extlib

DBHOSTNAME=mongol
CONNECTSTRING=mongol
USER=amficom
PASSWORD=amficom

LIBNAME=configuration.jar
ARCHFILE=configuration.tar.gz

ORACLECLASSPATH = $(ORACLE_HOME)/jdbc/lib/classes12.jar:$(ORACLE_HOME)/sqlj/lib/translator.jar:$(ORACLE_HOME)/sqlj/lib/runtime12ee.jar
APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar
EXTCLASSPATH = $(EXTLIBDIR)/jgraphpad.jar:$(EXTLIBDIR)/jfreechartK.jar:$(EXTLIBDIR)/ofx.spatialfx.jar

IDL2JAVA_FLAGS = \
	-C \
	-H \
	-no_bind \
	-no_examples \
	-no_tie \
	-narrow_compliance \
	-portable \
	-strict \
	-root_dir $(SRCDIR) \
	-I$(IDLDIR)

SUNJAVAC_FLAGS = -target 1.4.2

JIKES_FLAGS = \
	-target 1.4.2 \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	-extdirs $(JAVA_HOME)/jre/lib/ext \
	+E \
	+Pmodifier-order \
	+Predundant-modifiers \
	+Pswitchcheck \
	+Pnaming-convention \
	+T=2
JAVAC_FLAGS = \
	-d $(CLASSDIR) \
	-source 1.4 \
	-g \
	-O \
	-deprecation

JAVAC_FLAGS := $(JIKES_FLAGS) $(JAVAC_FLAGS)

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVAC_FLAGS) \
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
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) \
		$(IDLDIR)/configuration.idl
	$(JAVAC) $(JAVAC_FLAGS) -nowarn \
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
