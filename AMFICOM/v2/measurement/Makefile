IDL=../idl
SRC=./src
CLASSES=./classes
LIB=../lib
DEVLIB=../devlib

LIBNAME=measurement.jar
ARCHFILE=measurement.tar.gz

APPCLASSPATH=$(LIB)/util.jar:$(DEVLIB)/general.jar:$(DEVLIB)/configuration.jar:$(DEVLIB)/administration.jar
ORACLECLASSPATH=$(JDEV_HOME)/jdbc/lib/classes12.jar:$(JDEV_HOME)/sqlj/lib/translator.jar:$(JDEV_HOME)/sqlj/lib/runtime12.jar

DBHOSTNAME=mongol
CONNECTSTRING=mongol

IDL2JAVA=idlj
IDL2JAVA_FLAGS=-fall -i $(IDL) -td $(SRC) -v
#JAVAC=javac
JAVAC=jikes
JAVAC_CUSTOM_OPTIONS=-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar -extdirs $(JAVA_HOME)/jre/lib/ext +E +Pno-modifier-order +Predundant-modifiers +Pswitchcheck +Pno-naming-convention -nowarn +T=2
JAVAC_OPTIONS=-d $(CLASSES)
#SQLJ=$(JDEV_HOME)/bin/sqlj
#SQLJOPTIONS=-optcols -u amficom/amficom -url=jdbc:oracle:thin:@$(DBHOSTNAME):1521:$(CONNECTSTRING) -compiler-executable='$(JAVAC) $(JAVAC_CUSTOM_OPTIONS)' -codegen=oracle -status -d $(CLASSES) -dir $(SRC) -classpath $(ORACLECLASSPATH):$(APPCLASSPATH):$(CLASSES)
JPUB=$(JDEV_HOME)/bin/jpub -J-classpath $(ORACLECLASSPATH)
JPUBOPTIONS=-user=amficom/amficom -url=jdbc:oracle:thin:@$(DBHOSTNAME):1521:$(CONNECTSTRING) -omit_schema_names -package=com.syrus.AMFICOM.measurement.ora -dir=$(SRC) -usertypes=oracle -methods=all
JAR=jar-3.3.1


.PHONY:	clean cleanmach cleancustom cleancorba clean_general arch

#### Machine ####
$(LIBNAME):	mach
	$(JAR) -cf $(LIBNAME) \
		-C $(CLASSES) com/syrus/AMFICOM/measurement \
		-C $(CLASSES) com/syrus/AMFICOM/event
	cp -vf $(LIBNAME) $(DEVLIB)/

mach:	corba custom
	$(JAVAC) $(JAVAC_CUSTOM_OPTIONS) $(JAVAC_OPTIONS) -classpath $(CLASSES):$(ORACLECLASSPATH):$(APPCLASSPATH) \
		$(SRC)/com/syrus/AMFICOM/measurement/MeasurementDatabaseContext.java \
		$(SRC)/com/syrus/AMFICOM/measurement/SetParameter.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Action.java \
		$(SRC)/com/syrus/AMFICOM/measurement/PTTemporalTemplate.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Set.java \
		$(SRC)/com/syrus/AMFICOM/measurement/MeasurementSetup.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Result.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Measurement.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Analysis.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Evaluation.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Test.java \
		$(SRC)/com/syrus/AMFICOM/measurement/ParameterType.java \
		$(SRC)/com/syrus/AMFICOM/measurement/ActionType.java \
		$(SRC)/com/syrus/AMFICOM/measurement/MeasurementType.java \
		$(SRC)/com/syrus/AMFICOM/measurement/AnalysisType.java \
		$(SRC)/com/syrus/AMFICOM/measurement/EvaluationType.java \
		$(SRC)/com/syrus/AMFICOM/measurement/PTTemporalTemplate_Database.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Set_Database.java \
		$(SRC)/com/syrus/AMFICOM/measurement/MeasurementSetup_Database.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Result_Database.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Measurement_Database.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Analysis_Database.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Evaluation_Database.java \
		$(SRC)/com/syrus/AMFICOM/measurement/Test_Database.java \
		$(SRC)/com/syrus/AMFICOM/measurement/ParameterType_Database.java \
		$(SRC)/com/syrus/AMFICOM/measurement/MeasurementType_Database.java \
		$(SRC)/com/syrus/AMFICOM/measurement/AnalysisType_Database.java \
		$(SRC)/com/syrus/AMFICOM/measurement/EvaluationType_Database.java
	touch mach

custom:
	$(JPUB) $(JPUBOPTIONS) -sql=TIMEQUANTUM:TimeQuantum,TIMEQUANTUMARRAY:TimeQuantumArray,DAYTIME:DayTime,DAYTIMEARRAY:DayTimeArray,TEMPORALTEMPLATE:TemporalTemplate,TIMESTAMPARRAY:TimeStampArray
	$(JAVAC) $(JAVAC_CUSTOM_OPTIONS) $(JAVAC_OPTIONS) -classpath $(ORACLECLASSPATH) -sourcepath $(SRC) \
		$(SRC)/com/syrus/AMFICOM/measurement/ora/*.java
	touch custom

corba:
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) $(IDL)/event.idl
	$(IDL2JAVA) $(IDL2JAVA_FLAGS) $(IDL)/measurement.idl
	$(JAVAC) $(JAVAC_CUSTOM_OPTIONS) $(JAVAC_OPTIONS) -sourcepath $(SRC) -classpath $(APPCLASSPATH) \
		$(SRC)/com/syrus/AMFICOM/event/corba/*.java \
		$(SRC)/com/syrus/AMFICOM/measurement/corba/*.java
	touch corba

clean:	cleanmach cleancustom cleancorba
	rm -f $(LIBNAME)

cleanmach:
	rm -rf $(CLASSES)/com/syrus/AMFICOM/measurement/*.class
	rm -f mach

cleancustom:
	rm -rf $(SRC)/com/syrus/AMFICOM/measurement/ora
	rm -rf $(CLASSES)/com/syrus/AMFICOM/measurement/ora
	rm -f custom

cleancorba:
	rm -rf $(SRC)/com/syrus/AMFICOM/measurement/corba
	rm -rf $(SRC)/com/syrus/AMFICOM/event/corba
	rm -rf $(CLASSES)/com/syrus/AMFICOM/measurement/corba
	rm -rf $(CLASSES)/com/syrus/AMFICOM/event/corba
	rm -f corba


#### Archive ####
arch:
	tar -czvf $(ARCHFILE) \
		savec.mk saver $(SRC)/hlam \
		Makefile \
		$(IDL)/measurement.idl \
		database \
		$(SRC)/com/syrus/AMFICOM/measurement/*.java
