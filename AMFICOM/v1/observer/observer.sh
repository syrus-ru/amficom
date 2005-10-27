#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=../lib
EXTLIB=../../extlib

EXTCLASSPATH=$EXTLIB/kunststoff.jar:\
$EXTLIB/trove.jar:\
$EXTLIB/jgraphpad.jar

XMLCLASSPATH=$LIB/generalxml.jar:\
$LIB/configurationxml.jar:\
$LIB/mapxml.jar:\
$LIB/schemexml.jar:\
$XMLBEANS_HOME/lib/xbean.jar:\
$XMLBEANS_HOME/lib/jsr173_api.jar

APPCLASSPATH=$LIB/observer.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/event.jar:\
$LIB/resource.jar:\
$LIB/map.jar:\
$LIB/scheme.jar:\
$LIB/filter.jar:\
$LIB/reflectometry.jar:\
$LIB/dadara.jar:\
$LIB/csbridge.jar:\
$LIB/commonclient.jar:\
$LIB/filterclient.jar:\
$LIB/mapclient.jar:\
$LIB/schemeclient.jar:\
$LIB/mapviewclient.jar:\
$LIB/scheduler.jar:\
$LIB/analysis.jar:\
$LIB/leserver_interface.jar:\
$LIB/util.jar

$JAVA -Xmx256m -ea -client -classpath $APPCLASSPATH:$EXTCLASSPATH:$XMLCLASSPATH -Djava.library.path=$LIB com.syrus.AMFICOM.client.observer.Observer

