#!/bin/bash

LIB=$HOME/lib
EXTLIB=$HOME/extlib

ORACLECLASSPATH=\
$ORACLE_HOME/jdbc/lib/ojdbc14_g.jar:\
$ORACLE_HOME/jdbc/lib/orai18n.jar:\
$ORACLE_HOME/jdbc/lib/classes12.jar:\
$ORACLE_HOME/jdbc/lib/nls_charset12.jar

XMLCLASSPATH=\
$EXTLIB/xbean.jar:\
$LIB/generalxml.jar:\
$LIB/configurationxml.jar

TROVECLASSPATH=\
$EXTLIB/trove.jar

APPCLASSPATH=\
$LIB/cmserver.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/resource.jar:\
$LIB/report.jar:\
$LIB/csbridge.jar:\
$LIB/leserver_interface.jar:\
$LIB/mserver_interface.jar:\
$LIB/util.jar

CLASSPATH=\
${APPCLASSPATH}:\
${ORACLECLASSPATH}:\
${TROVECLASSPATH}:\
${XMLCLASSPATH}

APPNAME="cmserver"
JAVAFLAGS="-agentlib:jdwp=transport=dt_socket,address=8003,server=y,suspend=n"
JAVAFLAGS="${JAVAFLAGS} -Xloggc:`dirname $0`/gc -Xms128m -Xmx256m -server -ea"
MAIN="com.syrus.AMFICOM.cmserver.ClientMeasurementServer"
MAINOPTS=""

. `dirname $0`/amficomsrvrc
