#!/bin/bash

. `dirname $0`/amficomsrvenv

TRANSCEIVER=$LIB
DADARA=$LIB

XMLCLASSPATH=\
$XMLCLASSPATH:\
$LIB/configurationxml.jar

APPCLASSPATH=\
$LIB/mcm.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/event.jar:\
$LIB/csbridge.jar:\
$LIB/reflectometry.jar:\
$LIB/dadara.jar:\
$LIB/leserver_interface.jar:\
$LIB/mserver_interface.jar:\
$LIB/util.jar

CLASSPATH=\
${APPCLASSPATH}:\
${ORACLECLASSPATH}:\
${TROVECLASSPATH}:\
${XMLCLASSPATH}

APPNAME="mcm"
JAVAFLAGS="-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n"
JAVAFLAGS="${JAVAFLAGS} -Xms128m -Xmx256m -server -ea -Djava.library.path=${TRANSCEIVER}:${DADARA}"
MAIN="com.syrus.AMFICOM.mcm.MeasurementControlModule"
#MAINOPTS="-setup"

. `dirname $0`/amficomsrvrc
