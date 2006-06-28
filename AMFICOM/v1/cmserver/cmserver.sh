#!/bin/bash

. `dirname $0`/amficomsrvenv

XMLCLASSPATH=\
$XMLCLASSPATH:\
$LIB/configurationxml.jar

PATCHCLASSPATH=\
$PATCHLIB/csbridge.jar:\
$PATCHLIB/cmserver.jar:\
$PATCHLIB/general.jar:\
$PATCHLIB/util.jar

APPCLASSPATH=\
$LIB/cmserver.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/event.jar:\
$LIB/resource.jar:\
$LIB/report.jar:\
$LIB/csbridge.jar:\
$LIB/reflectometry.jar:\
$LIB/leserver_interface.jar:\
$LIB/mserver_interface.jar:\
$LIB/util.jar

CLASSPATH=\
${PATCHCLASSPATH}:\
${APPCLASSPATH}:\
${ORACLECLASSPATH}:\
${TROVECLASSPATH}:\
${XMLCLASSPATH}

APPNAME="cmserver"
JAVAFLAGS="-agentlib:jdwp=transport=dt_socket,address=8003,server=y,suspend=n"
JAVAFLAGS="${JAVAFLAGS} -Xloggc:`dirname $0`/gc -Xms128m -Xmx256m -server -ea"
JAVAFLAGS="${JAVAFLAGS} -Damficom.stack.trace.data.source=none"
MAIN="com.syrus.AMFICOM.cmserver.ClientMeasurementServer"
MAINOPTS=""

. `dirname $0`/amficomsrvrc
