SRCDIR=./src
CLASSDIR=./classes
IDLDIR=../idl_v1/idl
LIB=../lib

LIBNAME=measurement.jar
ARCHFILE=measurement.tar.gz

APPCLASSPATH=$(LIB)/util.jar:$(LIB)/general.jar

IDL2JAVA=idl2java
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
#JAVAC=javac
JAVAC=jikes
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
	-g \
	-O \
	-deprecation

JAVAC_FLAGS := $(JAVAC_FLAGS) $(JIKES_FLAGS)

mach:	corba $(CLASSDIR)
	$(JAVAC) $(JAVAC_FLAGS) -classpath $(CLASSDIR):$(APPCLASSPATH) \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/TemporalPattern.java

corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) $(IDLDIR)/event.idl
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) $(IDLDIR)/measurement.idl
	$(JAVAC) $(JAVAC_FLAGS) -nowarn -classpath $(APPCLASSPATH) -sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/event/corba/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/corba/*.java
	touch corba

$(CLASSDIR):
	mkdir -p $@

clean:	cleanmach cleancorba
	rm -f $(LIBNAME)

cleanmach:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/measurement/*.class
	rm -f mach

cleancorba:
	rm -rf $(SRCDIR)/com/syrus/AMFICOM/measurement/corba
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/measurement/corba
	rm -rf $(SRCDIR)/com/syrus/AMFICOM/event/corba
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/event/corba
	rm -f corba

#### Archive ####
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/measurement.idl \
		$(SRCDIR)/com/syrus/AMFICOM/measurement/*.java
