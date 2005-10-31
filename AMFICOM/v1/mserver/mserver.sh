#!/bin/bash


JAVA=java
LIB=$HOME/lib
EXTLIB=$HOME/extlib

ORACLECLASSPATH=$ORACLE_HOME/jdbc/lib/ojdbc14_g.jar:\
$ORACLE_HOME/jdbc/lib/orai18n.jar:\
$ORACLE_HOME/jdbc/lib/classes12.jar:\
$ORACLE_HOME/jdbc/lib/nls_charset12.jar

TROVECLASSPATH=$EXTLIB/trove.jar

XMLCLASSPATH=$EXTLIB/xbean.jar:\
$LIB/generalxml.jar:\
$LIB/configurationxml.jar

APPCLASSPATH=\
$LIB/mserver.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/csbridge.jar:\
$LIB/leserver_interface.jar:\
$LIB/mcm_interface.jar:\
$LIB/util.jar

JAVA="$JAVA -agentlib:jdwp=transport=dt_socket,address=8002,server=y,suspend=n"
$JAVA -Xms128m -Xmx256m -ea -server -classpath $APPCLASSPATH:$ORACLECLASSPATH:$TROVECLASSPATH:$XMLCLASSPATH com.syrus.AMFICOM.mserver.MeasurementServer &
echo $! > `dirname $0`/mserver.pid
