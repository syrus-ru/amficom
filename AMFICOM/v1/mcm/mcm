#!/bin/bash


#JAVA=$JAVA_HOME/bin/java
JAVA=/usr/java/jdk1.4.2-jfluid/bin/java
LIB=$HOME/lib
TRANSCEIVER=$LIB
ORACLECLASSPATH=$ORACLE_HOME/jdbc/lib/ojdbc14_g.jar:$ORACLE_HOME/jdbc/lib/orai18n.jar:$ORACLE_HOME/jdbc/lib/classes12.jar:$ORACLE_HOME/jdbc/lib/nls_charset12.jar
APPCLASSPATH=$LIB/mcm.jar:$LIB/general.jar:$LIB/administration.jar:$LIB/configuration.jar:$LIB/measurement.jar:$LIB/csbridge.jar:$LIB/dadara.jar:$LIB/leserver_interface.jar:$LIB/mserver_interface.jar:$LIB/util.jar

$JAVA -server -ea -Djava.library.path=$TRANSCEIVER -classpath $APPCLASSPATH:$ORACLECLASSPATH com.syrus.AMFICOM.mcm.MeasurementControlModule
