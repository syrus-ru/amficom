SRC=./src
CLASSES=./classes
IDL=../idl
LIB=../lib
DEVLIB=../devlib

LIBNAME=configuration.jar
ARCHFILE=configuration.tar.gz

APPCLASSPATH=$(LIB)/util.jar:$(DEVLIB)/general.jar
ORACLECLASSPATH=$(JDEV_HOME)/jdbc/lib/classes12.jar

DBHOSTNAME=mongol
CONNECTSTRING=mongol

IDL2JAVA=idlj
IDL2JAVA_FLAGS=-fall -i $(IDL) -td $(SRC) -v
#JAVAC=javac
JAVAC=jikes
JIKES_OPTIONS=-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar -extdirs $(JAVA_HOME)/jre/lib/ext +E +Pno-modifier-order +Predundant-modifiers +Pswitchcheck +Pno-naming-convention -nowarn +T=2
JAVAC_OPTIONS=-d $(CLASSES)
JAR=jar-3.3.1


.PHONY:	clean cleanmach cleancorba arch

#### Machine ####
$(LIBNAME):	mach
	$(JAR) -cf $(LIBNAME) \
		-C $(CLASSES) com/syrus/AMFICOM/configuration
	cp -vf $(LIBNAME) $(DEVLIB)/

mach:	corba
	$(JAVAC) $(JIKES_OPTIONS) $(JAVAC_OPTIONS) -classpath $(CLASSES):$(APPCLASSPATH) \
		$(SRC)/com/syrus/AMFICOM/configuration/ConfigurationDatabaseContext.java \
		$(SRC)/com/syrus/AMFICOM/configuration/DomainMember.java \
		$(SRC)/com/syrus/AMFICOM/configuration/Characteristic.java \
		$(SRC)/com/syrus/AMFICOM/configuration/Characteristic_Database.java \
		$(SRC)/com/syrus/AMFICOM/configuration/KIS.java \
		$(SRC)/com/syrus/AMFICOM/configuration/KIS_Database.java \ 
		$(SRC)/com/syrus/AMFICOM/configuration/MonitoredElement.java \
		$(SRC)/com/syrus/AMFICOM/configuration/MonitoredElement_Database.java
	touch mach

corba:
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) $(IDL)/configuration.idl
	$(JAVAC) $(JIKES_OPTIONS) $(JAVAC_OPTIONS) -sourcepath $(SRC) -classpath $(APPCLASSPATH) \
		$(SRC)/com/syrus/AMFICOM/configuration/corba/*.java
	touch corba

clean:	cleanmach cleancorba
	rm -f $(LIBNAME)

cleanmach:
	rm -rf $(CLASSES)/com/syrus/AMFICOM/configuration/*.class
	rm -f mach

cleancorba:
	rm -rf $(SRC)/com/syrus/AMFICOM/configuration/corba
	rm -rf $(CLASSES)/com/syrus/AMFICOM/configuration/corba
	rm -f corba


#### Archive ####
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL)/configuration.idl
		$(SRC)/com/syrus/AMFICOM/configuration/*.java
