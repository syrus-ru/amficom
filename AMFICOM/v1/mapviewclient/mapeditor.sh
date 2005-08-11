#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=../lib
EXTLIB=../../extlib

EXTCLASSPATH=$EXTLIB/kunststoff.jar:\
$EXTLIB/trove.jar:\
$EXTLIB/jgraphpad.jar:\
$EXTLIB/xbean.jar

MAPINFODIR=$EXTLIB/mapinfo
COMMONMAPINFODIR=$MAPINFODIR/common
CLIENTMAPINFODIR=$MAPINFODIR/client
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

APPCLASSPATH=$LIB/mapviewclient.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/resource.jar:\
$LIB/map.jar:\
$LIB/scheme.jar:\
$LIB/mapview.jar:\
$LIB/filter.jar:\
$LIB/csbridge.jar:\
$LIB/commonclient.jar:\
$LIB/filterclient.jar:\
$LIB/mapclient.jar:\
$LIB/mapinfo.jar:\
$LIB/schemeclient.jar:\
$LIB/leserver_interface.jar:\
$LIB/generalxml.jar:\
$LIB/mapxml.jar:\
$LIB/util.jar

$JAVA -Xmx256m -ea -client -classpath $APPCLASSPATH:$EXTCLASSPATH:$MAPINFOCLASSPATH com.syrus.AMFICOM.client.map.editor.MapEditor

