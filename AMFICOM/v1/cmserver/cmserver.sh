#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=$HOME/lib
EXTLIB=$HOME/extlib

ORACLECLASSPATH=$ORACLE_HOME/jdbc/lib/ojdbc14_g.jar:\
$ORACLE_HOME/jdbc/lib/orai18n.jar:\
$ORACLE_HOME/jdbc/lib/classes12.jar:\
$ORACLE_HOME/jdbc/lib/nls_charset12.jar

XMLCLASSPATH=$EXTLIB/xbean.jar:\
$LIB/generalxml.jar:\
$LIB/configurationxml.jar

TROVECLASSPATH=$EXTLIB/trove.jar

APPCLASSPATH=\
$LIB/cmserver.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/csbridge.jar:\
$LIB/leserver_interface.jar:\
$LIB/mserver_interface.jar:\
$LIB/util.jar

JAVA="$JAVA -agentlib:jdwp=transport=dt_socket,address=8003,server=y,suspend=n"
$JAVA -Xloggc:./gc -Xmx256m -server -ea -classpath $APPCLASSPATH:$ORACLECLASSPATH:$TROVECLASSPATH:$XMLCLASSPATH com.syrus.AMFICOM.cmserver.ClientMeasurementServer &
# Expands to the process ID of the most recently executed background (asynchronous) command.
echo $! > `dirname $0`/cmserver.pid
