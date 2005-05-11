#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=$HOME/lib
EXTLIB=$HOME/extlib
ORACLECLASSPATH=$ORACLE_HOME/jdbc/lib/ojdbc14_g.jar:$ORACLE_HOME/jdbc/lib/orai18n.jar:$ORACLE_HOME/jdbc/lib/classes12.jar:$ORACLE_HOME/jdbc/lib/nls_charset12.jar
TROVECLASSPATH=$EXTLIB/trove.jar
APPCLASSPATH=$LIB/mserver.jar:$LIB/general.jar:$LIB/administration.jar:$LIB/configuration.jar:$LIB/measurement.jar:$LIB/csbridge.jar:$LIB/leserver_interface.jar:$LIB/mcm_interface.jar:$LIB/util.jar

exec $JAVA -server -classpath $APPCLASSPATH:$ORACLECLASSPATH:$TROVECLASSPATH com.syrus.AMFICOM.mserver.MeasurementServer
