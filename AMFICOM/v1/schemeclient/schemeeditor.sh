#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=../lib
EXTLIB=../../extlib

EXTCLASSPATH=\
$EXTLIB/kunststoff.jar:\
$EXTLIB/trove.jar:\
$EXTLIB/jgraphpad.jar

XMLCLASSPATH=\
$LIB/generalxml.jar:\
$LIB/configurationxml.jar:\
$LIB/mapxml.jar:\
$LIB/schemexml.jar:\
$XMLBEANS_HOME/lib/xbean.jar:\
$XMLBEANS_HOME/lib/jsr173_api.jar

DEPRECATEDCLASSPATH=$ORACLE_HOME/lib/jdev-rt.zip

APPCLASSPATH=\
$LIB/schemeclient.jar:\
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
$LIB/csbridge.jar:\
$LIB/commonclient.jar:\
$LIB/filterclient.jar:\
$LIB/reportclient.jar:\
$LIB/systemserver_interface.jar:\
$LIB/util.jar:\
$LIB/extensions.jar:\
$LIB/extensions_schemas.jar:\
$LIB/resources_schemas.jar

$JAVA -Xmx256m -ea -client -classpath $APPCLASSPATH:$EXTCLASSPATH:$XMLCLASSPATH:$DEPRECATEDCLASSPATH com.syrus.AMFICOM.client_.scheme.SchemeEditor

