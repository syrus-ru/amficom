SRC=./src
CLASSES=./classes
IDL=../idl
LIB=../lib
DEVLIB=../devlib

LIBNAME=general.jar
ARCHFILE=general.tar.gz

APPCLASSPATH=$(LIB)/util.jar
ORACLECLASSPATH=$(JDEV_HOME)/jdbc/lib/classes12.jar

DBHOSTNAME=mongol
CONNECTSTRING=mongol

IDL2JAVA=idlj
IDL2JAVA_FLAGS=-fall -td $(SRC) -v
#JAVAC=javac
JAVAC=jikes
JAVAC_CUSTOM_OPTIONS=-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar -extdirs $(JAVA_HOME)/jre/lib/ext +E +Pno-modifier-order +Predundant-modifiers +Pswitchcheck +Pno-naming-convention -nowarn +T=2
JAVAC_OPTIONS=-d $(CLASSES)
JPUB=$(JDEV_HOME)/bin/jpub -J-classpath $(ORACLECLASSPATH)
JPUBOPTIONS=-user=amficom/amficom -url=jdbc:oracle:thin:@$(DBHOSTNAME):1521:$(CONNECTSTRING) -omit_schema_names -package=com.syrus.AMFICOM.measurement.ora -dir=$(SRC) -usertypes=oracle -methods=all
JAR=jar-3.3.1


.PHONY:	clean cleanmach cleancorba arch

#### Machine ####
$(LIBNAME):	mach
	$(JAR) -cf $(LIBNAME) \
		-C $(CLASSES) com/syrus/AMFICOM/general
	cp -vf $(LIBNAME) $(DEVLIB)/

mach:	corba
	$(JAVAC) $(JAVAC_CUSTOM_OPTIONS) $(JAVAC_OPTIONS) -classpath $(CLASSES):$(APPCLASSPATH) \
		$(SRC)/com/syrus/AMFICOM/general/TransferableObject.java \
		$(SRC)/com/syrus/AMFICOM/general/ObjectEntities.java \
		$(SRC)/com/syrus/AMFICOM/general/StorableObject.java \
		$(SRC)/com/syrus/AMFICOM/general/StorableObject_Database.java \
		$(SRC)/com/syrus/AMFICOM/general/StorableObjectType.java \
		$(SRC)/com/syrus/AMFICOM/general/CreateObjectException.java \
		$(SRC)/com/syrus/AMFICOM/general/RetrieveObjectException.java \
		$(SRC)/com/syrus/AMFICOM/general/UpdateObjectException.java \
		$(SRC)/com/syrus/AMFICOM/general/Identifier.java \
		$(SRC)/com/syrus/AMFICOM/general/IdentifierGenerator.java
	touch mach

corba:
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) $(IDL)/general.idl
	$(JAVAC) $(JAVAC_CUSTOM_OPTIONS) $(JAVAC_OPTIONS) -sourcepath $(SRC) \
		$(SRC)/com/syrus/AMFICOM/general/corba/*.java
	touch corba

clean:	cleanmach cleancorba
	rm -f $(LIBNAME)

cleanmach:
	rm -rf $(CLASSES)/com/syrus/AMFICOM/general/*.class
	rm -f mach

cleancorba:
	rm -rf $(SRC)/com/syrus/AMFICOM/general/corba
	rm -rf $(CLASSES)/com/syrus/AMFICOM/general/corba
	rm -f corba

#### Archive ####
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL)/general.idl \
		$(SRC)/com/syrus/AMFICOM/general/*.java
