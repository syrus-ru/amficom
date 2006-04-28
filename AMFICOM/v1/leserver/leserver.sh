#!/bin/bash

. `dirname $0`/amficomsrvenv

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
JAVAFLAGS="-agentlib:jdwp=transport=dt_socket,address=7999,server=y,suspend=n"
JAVAFLAGS="${JAVAFLAGS} -Dcom.sun.management.jmxremote=true"
JAVAFLAGS="${JAVAFLAGS} -Xms128m -Xmx256m -server -ea"
MAIN="com.syrus.AMFICOM.leserver.LoginEventServer"
MAINOPTS=""

. `dirname $0`/amficomsrvrc
