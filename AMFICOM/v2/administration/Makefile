SRC=./src
CLASSES=./classes
IDL=../idl
LIB=../lib
DEVLIB=../devlib



LIBNAME=administration.jar
ARCHFILE=administration.tar.gz

APPCLASSPATH=$(LIB)/util.jar:$(DEVLIB)/general.jar
ORACLECLASSPATH=$(JDEV_HOME)/jdbc/lib/classes12.jar

DBHOSTNAME=mongol
CONNECTSTRING=mongol

IDL2JAVA=idlj
IDL2JAVA_FLAGS=-fall -i $(IDL) -td $(SRC) -v
#JAVAC=javac
JAVAC=jikes
JAVAC_CUSTOM_OPTIONS=-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar -extdirs $(JAVA_HOME)/jre/lib/ext +E +Pno-modifier-order +Predundant-modifiers +Pswitchcheck +Pno-naming-convention -nowarn +T=2
JAVAC_OPTIONS=-d $(CLASSES)
JAR=jar-3.3.1


.PHONY:	clean cleanmach cleancorba arch

#### Machine ####
$(LIBNAME):	mach
	$(JAR) -cf $(LIBNAME) \
		-C $(CLASSES) com/syrus/AMFICOM/administration
	cp -vf $(LIBNAME) $(DEVLIB)/

mach:	corba
	$(JAVAC) $(JAVAC_CUSTOM_OPTIONS) $(JAVAC_OPTIONS) -classpath $(CLASSES):$(APPCLASSPATH) \
		$(SRC)/com/syrus/AMFICOM/administration/Server.java \
		$(SRC)/com/syrus/AMFICOM/administration/Server_Database.java \
		$(SRC)/com/syrus/AMFICOM/administration/MCM.java \
		$(SRC)/com/syrus/AMFICOM/administration/MCM_Database.java \
		$(SRC)/com/syrus/AMFICOM/administration/User.java \
		$(SRC)/com/syrus/AMFICOM/administration/User_Database.java
	touch mach

corba:
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) $(IDL)/administration.idl
	$(JAVAC) $(JAVAC_CUSTOM_OPTIONS) $(JAVAC_OPTIONS) -sourcepath $(SRC) -classpath $(APPCLASSPATH) \
		$(SRC)/com/syrus/AMFICOM/administration/corba/*.java
	touch corba

clean:	cleanmach cleancorba
	rm -f $(LIBNAME)

cleanmach:
	rm -rf $(CLASSES)/com/syrus/AMFICOM/administration/*.class
	rm -f mach

cleancorba:
	rm -rf $(SRC)/com/syrus/AMFICOM/administration/corba
	rm -rf $(CLASSES)/com/syrus/AMFICOM/administration/corba
	rm -f corba


#### Archive ####
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL)/administration.idl \
		$(SRC)/com/syrus/AMFICOM/administration/*.java
