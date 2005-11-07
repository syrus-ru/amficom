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
$LIB/generalxml.jar

TROVECLASSPATH=\
$EXTLIB/trove.jar

MAILCLASSPATH=\
$EXTLIB/activation.jar:\
$EXTLIB/mail.jar

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

CLASSPATH=\
${APPCLASSPATH}:\
${ORACLECLASSPATH}:\
${TROVECLASSPATH}:\
${MAILCLASSPATH}:\
${XMLCLASSPATH}

APPNAME="leserver"
#JAVAFLAGS="-agentlib:jdwp=transport=dt_socket,address=8001,server=y,suspend=n"
JAVAFLAGS="${JAVAFLAGS} -Xms128m -Xmx256m -server -ea"
MAIN="com.syrus.AMFICOM.leserver.LoginEventServer"
MAINOPTS=""

. `dirname $0`/amficomsrvrc
