SRC=./src
CLASSES=./classes
IDL=../idl_v1
LIB=../lib

LIBNAME=general.jar
ARCHFILE=general.tar.gz

APPCLASSPATH=$(LIB)/servermisc.jar:$(LIB)/util.jar

IDL2JAVA=idl2java
IDL2JAVA_FLAGS=-no_examples -no_tie -root_dir $(SRC) -I$(IDL)
#JAVAC=javac
JAVAC=jikes
JAVAC_CUSTOM_OPTIONS=-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar -extdirs $(JAVA_HOME)/jre/lib/ext +E +Pno-modifier-order +Predundant-modifiers +Pswitchcheck +Pno-naming-convention -nowarn +T=2
JAVAC_OPTIONS=-d $(CLASSES)
JAR=jar-3.3.1

$(LIBNAME):	mach
	$(JAR) -cf $(LIBNAME) \
		-C $(CLASSES) com/syrus/AMFICOM/general
	cp -vf $(LIBNAME) $(LIB)/

mach:	corba
	$(JAVAC) $(JAVAC_CUSTOM_OPTIONS) $(JAVAC_OPTIONS) -classpath $(CLASSES):$(APPCLASSPATH) \
		$(SRC)/com/syrus/AMFICOM/general/TransferableObject.java \
		$(SRC)/com/syrus/AMFICOM/general/ObjectEntities.java \
		$(SRC)/com/syrus/AMFICOM/general/Identifier.java \
		$(SRC)/com/syrus/AMFICOM/general/IdentifierGenerator.java

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
