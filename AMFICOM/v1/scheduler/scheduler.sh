#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=../lib
EXTLIB=../../extlib

EXTCLASSPATH=$EXTLIB/kunststoff.jar:$EXTLIB/trove.jar

XMLCLASSPATH=$LIB/generalxml.jar:\
$XMLBEANS_HOME/lib/xbean.jar:\
$XMLBEANS_HOME/lib/jsr173_api.jar

APPCLASSPATH=$LIB/scheduler.jar:\
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
$LIB/leserver_interface.jar:\
$LIB/util.jar:\
$LIB/extensions.jar:\
$LIB/extensions_schemas.jar:\
$LIB/resources_schemas.jar

$JAVA -Xmx256m -ea -client -classpath $APPCLASSPATH:${XMLCLASSPATH}:$EXTCLASSPATH com.syrus.AMFICOM.Client.Schedule.Schedule

