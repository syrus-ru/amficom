#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=../lib
EXTLIB=../../extlib

EXTCLASSPATH=$EXTLIB/kunststoff.jar:$EXTLIB/trove.jar

XMLCLASSPATH=$LIB/generalxml.jar:\
$XMLBEANS_HOME/lib/xbean.jar:\
$XMLBEANS_HOME/lib/jsr173_api.jar

APPCLASSPATH=$LIB/analysis.jar:\
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
$LIB/dadara.jar:\
$LIB/commonclient.jar:\
$LIB/filterclient.jar:\
$LIB/reportclient.jar:\
$LIB/mapclient.jar:\
$LIB/leserver_interface.jar:\
$LIB/util.jar

#JAVA="$JAVA -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"
$JAVA -Xmx256m -client -ea -classpath $APPCLASSPATH:${XMLCLASSPATH}:$EXTCLASSPATH -Djava.library.path=$LIB com.syrus.AMFICOM.Client.Analysis.Reflectometry.AnalyseExt

