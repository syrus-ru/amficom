SRC=./src
CLASSES=./classes
IDL=./idl
LOAD=../load
LIB=../lib
LIB8I=../lib8i
EXTLIB=../extlib
#ORACLE_HOME=/mnt/d/oracle/product/9.0.2

VBCLASSPATH=$(ORACLE_HOME)/lib/vbjapp.jar:$(ORACLE_HOME)/lib/vbjorb.jar
#VBCLASSPATH=$(EXTLIB)/ora8i/vbjapp.jar:$(EXTLIB)/ora8i/vbjorb.jar:$(EXTLIB)/ora8i/aurora_client.jar
MYCLASSPATH=$(LIB)/ServerInterface_Survey.jar:$(LIB)/ServerInterface_Alarm.jar:$(LIB)/ServerInterface_General.jar:$(LIB)/server.jar:$(LIB)/util.jar:$(LIB)/utilserver.jar
#MYCLASSPATH=$(LIB)/ServerInterface_Survey.jar:$(LIB)/ServerInterface_Alarm.jar:$(LIB)/ServerInterface_General.jar:$(LIB)/AMFICOMServerObject.jar:$(LIB8I)/util.jar
SQLJCLASSPATH=$(JDEV_HOME)/sqlj/lib/translator.jar:$(JDEV_HOME)/sqlj/lib/runtime12.jar:$(JDEV_HOME)/jdbc/lib/classes12.jar
#SQLJCLASSPATH=$(EXTLIB)/ora8i/translator.zip:$(EXTLIB)/ora8i/runtime12.zip:$(EXTLIB)/ora8i/classes12.zip

#SERVERHOSTNAME=mgts
#CONNECTSTRING=asia
#SERVERHOSTNAME=ararat
#CONNECTSTRING=ararat
SERVERHOSTNAME=research
CONNECTSTRING=shaitan
#SERVERHOSTNAME=amficom
#CONNECTSTRING=jamaica
#SERVERHOSTNAME=mousebuster
#CONNECTSTRING=andrei

IDL2JAVA=idl2java
IDL2JAVAOPTIONS=-no_examples -no_tie -root_dir $(SRC)
JAVAC=javac
SQLJ=sqlj
SQLJOPTIONS=-optcols -compiler-executable=$(JAVAC) -codegen=oraclejdbc -ser2class -status -u amficom/amficom -d $(CLASSES) -dir $(SRC) -url=jdbc:oracle:thin:@$(SERVERHOSTNAME):1521:$(CONNECTSTRING) -classpath $(CLASSES):$(MYCLASSPATH):$(SQLJCLASSPATH)
JAR=jar-3.3.1

LIBNAME=kisserver.jar
ARCHFILE=amficomkis.tar.gz


.PHONY:	clean cleanmach lp arch drop

$(LIBNAME):	impl
	$(JAR) -cf ./$(LIBNAME)\
		-C $(CLASSES) com/syrus/AMFICOM/server/measurement\
		-C $(CLASSES) com/syrus/AMFICOM/server/event\
		-C $(CLASSES) com/syrus/AMFICOM/CORBA
	cp -f ./$(LIBNAME) $(LIB)

impl:	mach agsets
	$(JAVAC) -d $(CLASSES) -sourcepath $(SRC) -classpath $(VBCLASSPATH):$(MYCLASSPATH):$(SQLJCLASSPATH) $(SRC)/com/syrus/AMFICOM/server/measurement/AMFICOMKISImplementation.java
	touch impl

mach:	corba
	$(SQLJ) $(SQLJOPTIONS)\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Action.java\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Analysis.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/CriteriaSet.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Evaluation.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Etalon.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/ThresholdSet.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/TestArgumentSet.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/SetParameter.java\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Modeling.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Argument.java\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Parameter.java\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Test.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Result.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/AnalysisType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/EvaluationType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/TestType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/EtalonType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/ModelingType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/ParameterType.java\
		$(SRC)/com/syrus/AMFICOM/server/measurement/TestSetup.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/EventSourceType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/AlarmType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/EventSource.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/Event.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/Alarm.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/Alerting.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/RuleParser.java
	touch mach

agsets:	corba
	$(SQLJ) $(SQLJOPTIONS) $(SRC)/com/syrus/AMFICOM/server/measurement/AgentSettings.sqlj
	touch agsets

corba:
	$(IDL2JAVA) $(IDL2JAVAOPTIONS) $(IDL)/KIS.idl
	$(IDL2JAVA) $(IDL2JAVAOPTIONS) $(IDL)/AMFICOMKIS.idl
	$(JAVAC) -d $(CLASSES) -sourcepath $(SRC) -classpath $(VBCLASSPATH):$(MYCLASSPATH) $(SRC)/com/syrus/AMFICOM/CORBA/AMFICOMKIS.java
	$(JAVAC) -d $(CLASSES) -sourcepath $(SRC) -classpath $(VBCLASSPATH):$(MYCLASSPATH) $(SRC)/com/syrus/AMFICOM/CORBA/AMFICOMKISHelper.java
	$(JAVAC) -d $(CLASSES) -sourcepath $(SRC) -classpath $(VBCLASSPATH):$(MYCLASSPATH) $(SRC)/com/syrus/AMFICOM/CORBA/_AMFICOMKISImplBase.java
	$(JAVAC) -d $(CLASSES) -sourcepath $(SRC) -classpath $(VBCLASSPATH):$(MYCLASSPATH) $(SRC)/com/syrus/AMFICOM/CORBA/KIS/*.java
	touch corba

lp:     $(LIBNAME)
#	$(LOAD)/lp ./$(LIBNAME) $(SERVERHOSTNAME) $(CONNECTSTRING)

drop:
	dropjava -user amficom/amficom@$(CONNECTSTRING) -verbose $(LIBNAME)

misc:
	$(JAVAC) -d $(CLASSES) -sourcepath $(SRC) -classpath $(SQLJCLASSPATH):$(VBCLASSPATH) $(SRC)/com/syrus/AMFICOM/server/load/ConnectionManager.java
	touch misc

clean:	cleanmach
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/AgentSettings.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/AgentSettings.generated.java
	rm -rf $(CLASSES)/com
	rm -f ./$(LIBNAME)
	rm -rf $(SRC)/com/syrus/AMFICOM/CORBA/
	rm -f corba misc impl agsets

cleanmach:
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Test.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Test.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/TestArgumentSet.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/TestArgumentSet.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Result.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Result.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/CriteriaSet.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/CriteriaSet.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Analysis.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Analysis.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/ThresholdSet.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/ThresholdSet.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Evaluation.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Evaluation.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Modeling.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Modeling.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Etalon.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/Etalon.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/AnalysisType.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/AnalysisType.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/EvaluationType.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/EvaluationType.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/ModelingType.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/ModelingType.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/TestType.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/TestType.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/EtalonType.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/EtalonType.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/TestSetup.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/measurement/TestSetup.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/EventSourceType.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/EventSourceType.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/AlarmType.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/AlarmType.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/EventSource.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/EventSource.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/Event.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/Event.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/Alarm.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/Alarm.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/Alerting.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/event/Alerting.generated.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/load/TestPopulator.java
	rm -f $(SRC)/com/syrus/AMFICOM/server/load/TestPopulator.generated.java
	rm -f mach


arch:
	tar -czvf $(ARCHFILE) \
		Makefile rc specification.txt \
		$(IDL) \
		$(SRC)/com/syrus/AMFICOM/server/measurement/Action.java\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Analysis.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/CriteriaSet.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Evaluation.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Etalon.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/ThresholdSet.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/TestArgumentSet.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/SetParameter.java\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Modeling.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Argument.java\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Parameter.java\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Test.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/Result.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/AnalysisType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/EvaluationType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/TestType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/EtalonType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/ModelingType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/measurement/ParameterType.java\
		$(SRC)/com/syrus/AMFICOM/server/measurement/TestSetup.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/EventSourceType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/AlarmType.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/EventSource.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/Event.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/Alarm.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/Alerting.sqlj\
		$(SRC)/com/syrus/AMFICOM/server/event/RuleParser.java
