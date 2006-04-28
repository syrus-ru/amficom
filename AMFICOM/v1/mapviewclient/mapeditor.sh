#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=../lib
EXTLIB=../../extlib

EXTCLASSPATH=$EXTLIB/kunststoff.jar:\
$EXTLIB/trove.jar:\
$EXTLIB/jgraphpad.jar:\
$EXTLIB/dom4j.jar:\
$EXTLIB/jaxen.jar

MAPINFODIR=$EXTLIB/mapinfo
COMMONMAPINFODIR=$MAPINFODIR/common
CLIENTMAPINFODIR=$MAPINFODIR/client

ORACLECLASSPATH=\
$ORACLE_HOME/jdbc/lib/ojdbc14_g.jar:\
$ORACLE_HOME/jdbc/lib/orai18n.jar:\
$ORACLE_HOME/jdbc/lib/classes12.jar:\
$ORACLE_HOME/jdbc/lib/nls_charset12.jar


MAPINFOCLASSPATH=$COMMONMAPINFODIR/jdom.jar:\
$COMMONMAPINFODIR/commons-logging.jar:\
$COMMONMAPINFODIR/micsys.jar:\
$COMMONMAPINFODIR/miutil.jar:\
$COMMONMAPINFODIR/mxj.jar:\
$COMMONMAPINFODIR/mxjgeom.jar:\
$COMMONMAPINFODIR/mxjtabdp.jar:\
$COMMONMAPINFODIR/mistyles.jar:\
$COMMONMAPINFODIR/xercesImpl.jar:\
$CLIENTMAPINFODIR/rjsclient.jar

SPATIALFXCLASSPATH=$EXTLIB/ofx.spatialfx.jar

XMLCLASSPATH=$LIB/generalxml.jar:\
$LIB/configurationxml.jar:\
$LIB/mapxml.jar:\
$LIB/schemexml.jar:\
$XMLBEANS_HOME/lib/xbean.jar:\
$XMLBEANS_HOME/lib/jsr173_api.jar

APPCLASSPATH=$LIB/mapviewclient.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/event.jar:\
$LIB/resource.jar:\
$LIB/map.jar:\
$LIB/scheme.jar:\
$LIB/mapview.jar:\
$LIB/filter.jar:\
$LIB/reflectometry.jar:\
$LIB/csbridge.jar:\
$LIB/commonclient.jar:\
$LIB/filterclient.jar:\
$LIB/reportclient.jar:\
$LIB/mapclient.jar:\
$LIB/mapinfo.jar:\
$LIB/spatialfx.jar:\
$LIB/schemeclient.jar:\
$LIB/leserver_interface.jar:\
$LIB/mscharserver_interface.jar:\
$LIB/util.jar:\
$LIB/extensions.jar:\
$LIB/extensions_schemas.jar:\
$LIB/resources_schemas.jar

$JAVA -Xmx256m -ea -client -classpath $APPCLASSPATH:$XMLCLASSPATH:$EXTCLASSPATH:$ORACLECLASSPATH:$MAPINFOCLASSPATH:$SPATIALFXCLASSPATH com.syrus.AMFICOM.client.map.editor.MapEditor

