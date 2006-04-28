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
$LIB/mserver.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/csbridge.jar:\
$LIB/leserver_interface.jar:\
$LIB/mcm_interface.jar:\
$LIB/util.jar

CLASSPATH=\
${PATCHCLASSPATH}:\
${APPCLASSPATH}:\
${ORACLECLASSPATH}:\
${TROVECLASSPATH}:\
${XMLCLASSPATH}

APPNAME="mserver"
JAVAFLAGS="-agentlib:jdwp=transport=dt_socket,address=8002,server=y,suspend=n"
JAVAFLAGS="${JAVAFLAGS} -Xms128m -Xmx256m -ea -server"
JAVAFLAGS="${JAVAFLAGS} -Damficom.stack.trace.data.source=none"
MAIN="com.syrus.AMFICOM.mserver.MeasurementServer"
MAINOPTS=""

. `dirname $0`/amficomsrvrc
