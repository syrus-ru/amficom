#!/bin/bash


JAVA=$JAVA_HOME/bin/java

LIB=$HOME/lib
EXTLIB=$HOME/extlib

TRANSCEIVER=$LIB
DADARA=$LIB

ORACLECLASSPATH=\
$ORACLE_HOME/jdbc/lib/ojdbc14_g.jar:\
$ORACLE_HOME/jdbc/lib/orai18n.jar:\
$ORACLE_HOME/jdbc/lib/classes12.jar:\
$ORACLE_HOME/jdbc/lib/nls_charset12.jar

TROVECLASSPATH=$EXTLIB/trove.jar

XMLCLASSPATH=$EXTLIB/xbean.jar:\
$LIB/generalxml.jar:\
$LIB/configurationxml.jar

APPCLASSPATH=\
$LIB/mcm.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/csbridge.jar:\
$LIB/dadara.jar:\
$LIB/leserver_interface.jar:\
$LIB/mserver_interface.jar:\
$LIB/util.jar


JAVA="$JAVA -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n"
$JAVA -server -ea -Djava.library.path=$TRANSCEIVER:$DADARA -classpath $APPCLASSPATH:$ORACLECLASSPATH:$TROVECLASSPATH:$XMLCLASSPATH com.syrus.AMFICOM.mcm.MeasurementControlModule
# -setup
