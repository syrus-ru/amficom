SRC=./src
CLASSES=./classes
IDL=../idl_v1
LIB=../lib

LIBNAME=measurement.jar
ARCHFILE=measurement.tar.gz

APPCLASSPATH=$(LIB)/util.jar:$(LIB)/general.jar

IDL2JAVA=idl2java
IDL2JAVA_FLAGS=-no_examples -no_tie -root_dir $(SRC) -I$(IDL)
#JAVAC=javac
JAVAC=jikes
JAVAC_CUSTOM_OPTIONS=-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar -extdirs $(JAVA_HOME)/jre/lib/ext +E +Pno-modifier-order +Predundant-modifiers +Pswitchcheck +Pno-naming-convention -nowarn +T=2
JAVAC_OPTIONS=-d $(CLASSES)

mach:	corba
	$(JAVAC) $(JAVAC_CUSTOM_OPTIONS) $(JAVAC_OPTIONS) -classpath $(CLASSES):$(APPCLASSPATH) \
		$(SRC)/com/syrus/AMFICOM/measurement/TemporalPattern.java

corba:
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) $(IDL)/event.idl
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) $(IDL)/measurement.idl
	$(JAVAC) $(JAVAC_CUSTOM_OPTIONS) $(JAVAC_OPTIONS) -classpath $(APPCLASSPATH) -sourcepath $(SRC) \
		$(SRC)/com/syrus/AMFICOM/event/corba/*.java \
		$(SRC)/com/syrus/AMFICOM/measurement/corba/*.java
	touch corba

clean:	cleanmach cleancorba
	rm -f $(LIBNAME)

cleanmach:
	rm -rf $(CLASSES)/com/syrus/AMFICOM/measurement/*.class
	rm -f mach

cleancorba:
	rm -rf $(SRC)/com/syrus/AMFICOM/measurement/corba
	rm -rf $(CLASSES)/com/syrus/AMFICOM/measurement/corba
	rm -rf $(SRC)/com/syrus/AMFICOM/event/corba
	rm -rf $(CLASSES)/com/syrus/AMFICOM/event/corba
	rm -f corba

#### Archive ####
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL)/measurement.idl \
		$(SRC)/com/syrus/AMFICOM/measurement/*.java
