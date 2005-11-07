#!/bin/bash

echo "*** Mscharserver v1.0.0 ``Bobruysk''"

LIB=$HOME/lib
EXTLIB=$HOME/extlib

ORACLECLASSPATH=\
$ORACLE_HOME/jdbc/lib/ojdbc14_g.jar:\
$ORACLE_HOME/jdbc/lib/orai18n.jar:\
$ORACLE_HOME/jdbc/lib/classes12.jar:\
$ORACLE_HOME/jdbc/lib/nls_charset12.jar

TROVECLASSPATH=\
$EXTLIB/trove.jar

XMLCLASSPATH=\
$EXTLIB/xbean.jar:\
$EXTLIB/dom4j-1.6.jar:\
$EXTLIB/jaxen-1.1-beta-6.jar:\
$LIB/generalxml.jar:\
$LIB/configurationxml.jar:\
$LIB/mapxml.jar:\
$LIB/schemexml.jar

MAPINFOCLASSPATH=\
$EXTLIB/jdom.jar:\
$EXTLIB/micsys.jar:\
$EXTLIB/miutil.jar:\
$EXTLIB/mxj.jar:\
$EXTLIB/commons-logging.jar:\
$EXTLIB/mxjgeom.jar:\
$EXTLIB/miappletsup.jar:\
$EXTLIB/xercesImpl.jar:\
$EXTLIB/mxjtabdp.jar:\
$EXTLIB/mistyles.jar

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
