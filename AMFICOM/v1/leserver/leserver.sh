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
$LIB/generalxml.jar

APPCLASSPATH=\
$LIB/leserver.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/event.jar:\
$LIB/scheme.jar:\
$LIB/schemexml.jar:\
$LIB/csbridge.jar:\
$LIB/reflectometry.jar:\
$LIB/util.jar

$JAVA -Xms128m -Xmx256m -server -ea -classpath $APPCLASSPATH:$XMLCLASSPATH:$ORACLECLASSPATH:$TROVECLASSPATH com.syrus.AMFICOM.leserver.LoginEventServer &
echo $! > `dirname $0`/leserver.pid
