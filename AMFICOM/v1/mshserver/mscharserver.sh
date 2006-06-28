#!/bin/bash

echo "*** Mscharserver v1.0.0 ``Bobruysk''"

. `dirname $0`/amficomsrvenv

XMLCLASSPATH=\
$XMLCLASSPATH:\
$EXTLIB/dom4j.jar:\
$EXTLIB/jaxen.jar:\
$LIB/configurationxml.jar:\
$LIB/mapxml.jar:\
$LIB/schemexml.jar

APPCLASSPATH=\
$LIB/filter.jar:\
$LIB/mscharserver.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/csbridge.jar:\
$LIB/leserver_interface.jar:\
$LIB/map.jar:\
$LIB/scheme.jar:\
$LIB/util.jar:\
$LIB/resource.jar:\
$LIB/mapview.jar

CLASSPATH=\
${APPCLASSPATH}:\
${ORACLECLASSPATH}:\
${TROVECLASSPATH}:\
${XMLCLASSPATH}:\
${MAPINFOCLASSPATH}

APPNAME="mscharserver"
JAVAFLAGS="-agentlib:jdwp=transport=dt_socket,address=8004,server=y,suspend=n"
JAVAFLAGS="${JAVAFLAGS} -Xms128m -Xmx256m -server -ea"
MAIN="com.syrus.AMFICOM.mscharserver.MapSchemeAdministrationResourceServer"
MAINOPTS=""

. `dirname $0`/amficomsrvrc
