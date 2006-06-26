#!/bin/bash

. `dirname $0`/amficomsrvenv

XMLCLASSPATH=\
$XMLCLASSPATH:\
$EXTLIB/dom4j.jar:\
$EXTLIB/jaxen.jar:\
$LIB/generalxml.jar:\
$LIB/configurationxml.jar:\
$LIB/mapxml.jar:\
$LIB/schemexml.jar

APPCLASSPATH=\
$LIB/systemserver.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/event.jar:\
$LIB/resource.jar:\
$LIB/report.jar:\
$LIB/map.jar:\
$LIB/scheme.jar:\
$LIB/mapview.jar:\
$LIB/filter.jar:\
$LIB/reflectometry.jar:\
$LIB/csbridge.jar:\
$LIB/util.jar:\

CLASSPATH=\
${APPCLASSPATH}:\
${ORACLECLASSPATH}:\
${TROVECLASSPATH}:\
${XMLCLASSPATH}:\
${MAILCLASSPATH}
${MAPINFOCLASSPATH}

APPNAME="systemserver"
JAVAFLAGS="-agentlib:jdwp=transport=dt_socket,address=8004,server=y,suspend=n"
JAVAFLAGS="${JAVAFLAGS} -Xms128m -Xmx256m -server -ea"
MAIN="com.syrus.AMFICOM.systemserver.SystemServer"
MAINOPTS=""

. `dirname $0`/amficomsrvrc
