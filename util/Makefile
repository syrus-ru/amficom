SRC=./src
CLASSES=./classes
LOAD=../load
LIB=../lib

LIBNAME=util.jar
LIBNAMESERVER=utilserver.jar
ARCHFILE=util.tar.gz

ORACLECLASSPATH=$(JDEV_HOME)/sqlj/lib/translator.jar:$(JDEV_HOME)/sqlj/lib/runtime12.jar:$(JDEV_HOME)/jdbc/lib/classes12.jar
VBCLASSPATH=$(ORACLE_HOME)/lib/aurora_client.jar:$(ORACLE_HOME)/lib/vbjorb.jar
APPCLASSPATH=$(LIB)/io.jar

DBHOSTNAME=mongol
CONNECTSTRING=mongol
#DBHOSTNAME=amficom
#CONNECTSTRING=jamaica

JAVAC=javac
SQLJ=$(JDEV_HOME)/bin/sqlj
SQLJOPTIONS=-compile=false -optcols -u amficom/amficom -url=jdbc:oracle:thin:@$(DBHOSTNAME):1521:$(CONNECTSTRING) -codegen=oraclejdbc -status -d $(CLASSES) -dir $(SRC) -classpath $(ORACLECLASSPATH)
JAR=jar-3.3.1


.PHONY:	clean arch

$(LIBNAME):	misc con corba mail cache
	$(JAR) -cf $(LIBNAME) \
		-C $(CLASSES) com/syrus/util
	cp -f $(LIBNAME) $(LIB)

misc:	log
	$(JAVAC) -d $(CLASSES) -sourcepath $(SRC) $(SRC)/com/syrus/util/MathRef.java
	$(JAVAC) -d $(CLASSES) -classpath $(ORACLECLASSPATH) -sourcepath $(SRC) $(SRC)/com/syrus/util/database/ByteArrayDatabase.java
	$(JAVAC) -d $(CLASSES) $(SRC)/com/syrus/util/database/DatabaseDate.java
	touch misc

con:	log
	$(JAVAC) -d $(CLASSES) -classpath $(CLASSES) $(SRC)/com/syrus/util/database/DatabaseConnection.java
	touch con

corba:	log mail
	$(JAVAC) -d $(CLASSES) -classpath $(CLASSES):$(VBCLASSPATH) $(SRC)/com/syrus/util/corba/*.java
	touch corba

log:
	$(SQLJ) $(SQLJOPTIONS) $(SRC)/com/syrus/util/database/ServerProperties.sqlj
	$(JAVAC) -d $(CLASSES) -classpath $(ORACLECLASSPATH) -sourcepath $(SRC) $(SRC)/com/syrus/util/database/Server.java
	$(JAVAC) -d $(CLASSES) -sourcepath $(SRC) $(SRC)/com/syrus/util/Application.java
	touch log

mail:
	$(JAVAC) -d $(CLASSES) -classpath $(ORACLECLASSPATH):$(APPCLASSPATH) \
		$(SRC)/com/syrus/util/mail/MailIdentity.java \
		$(SRC)/com/syrus/util/mail/SimpleMailer.java \
		$(SRC)/com/syrus/util/sms/ui/JSMSSendFrame.java \
		$(SRC)/com/syrus/util/sms/RSMSUCPProvider.java \
		$(SRC)/com/syrus/util/sms/SMSMailProvider.java \
		$(SRC)/com/syrus/util/sms/SMSProviderFactory.java \
		$(SRC)/com/syrus/util/sms/SMSProvider.java \
		$(SRC)/com/syrus/AMFICOM/corba/portable/common/MessageDeliveryFailedException.java \
		$(SRC)/com/syrus/AMFICOM/corba/portable/common/MessageDeliveryFailedExceptionHelper.java \
		$(SRC)/com/syrus/util/prefs/ui/JPreferencesManagerFrame.java \
		$(SRC)/com/syrus/util/prefs/IIOPConnectionManager.java \
		$(SRC)/com/syrus/util/prefs/JDBCConnectionManager.java \
		$(SRC)/com/syrus/util/prefs/PreferencesManager.java \
		$(SRC)/com/syrus/util/prefs/SMTPConnectionManager.java
	touch mail

cache:
	$(JAVAC) -d $(CLASSES) \
		$(SRC)/com/syrus/util/CacheLock.java \
		$(SRC)/com/syrus/util/CacheLockObject.java \
		$(SRC)/com/syrus/util/LeerCacheLock.java \
		$(SRC)/com/syrus/util/LeerCacheLockObject.java \
		$(SRC)/com/syrus/util/NativeCacheLock.java \
		$(SRC)/com/syrus/util/NIOCacheLock.java
	touch cache

$(LIBNAMESERVER):	$(LIBNAME)
	jar -cf $(LIBNAMESERVER)\
		-C $(CLASSES) com/syrus/util/ByteArray.class \
		-C $(CLASSES) com/syrus/util/Log.class \
		-C $(CLASSES) com/syrus/util/Logger.class \
		-C $(CLASSES) com/syrus/util/ServerLogger.class \
		-C $(CLASSES) com/syrus/util/StolenLogger.class \
		-C $(CLASSES) com/syrus/util/database/ByteArrayDatabase.class \
		-C $(CLASSES) com/syrus/util/database/DatabaseConnection.class \
		-C $(CLASSES) com/syrus/util/database/Server.class \
		-C $(CLASSES) com/syrus/util/database/ServerProperties.class
	cp -f $(LIBNAMESERVER) $(LIB)/
#	$(LOAD)/lp $(LIBNAMESERVER) $(DBHOSTNAME) $(CONNECTSTRING)

clean:
	rm -f $(SRC)/com/syrus/util/database/ServerProperties.java
	rm -f $(SRC)/com/syrus/util/database/ServerProperties.generated.java
	rm -rf $(CLASSES)/com
	rm -f $(LIBNAME)
	rm -f misc con corba log mail cache

arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SRC)/com/syrus/util/*.java \
		$(SRC)/com/syrus/util/database/ByteArrayDatabase.java \
		$(SRC)/com/syrus/util/database/DatabaseConnection.java \
		$(SRC)/com/syrus/util/database/Server.java \
		$(SRC)/com/syrus/util/database/ServerProperties.sqlj \
		$(SRC)/com/syrus/util/corba
