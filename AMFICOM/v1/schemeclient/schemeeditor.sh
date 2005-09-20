#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=../lib
EXTLIB=../../extlib

EXTCLASSPATH=$EXTLIB/kunststoff.jar:\
$EXTLIB/trove.jar:\
$EXTLIB/jgraphpad.jar
XMLCLASSPATH=$XMLBEANS_HOME/build/lib/xbean.jar
DEPRECATEDCLASSPATH=$ORACLE_HOME/lib/jdev-rt.zip
APPCLASSPATH=$LIB/schemeclient.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/resource.jar:\
$LIB/map.jar:\
$LIB/scheme.jar:\
$LIB/filter.jar:\
$LIB/csbridge.jar:\
$LIB/commonclient.jar:\
$LIB/filterclient.jar:\
$LIB/leserver_interface.jar:\
$LIB/util.jar

$JAVA -Xmx256m -ea -client -classpath $APPCLASSPATH:$EXTCLASSPATH:$XMLCLASSPATH:$DEPRECATEDCLASSPATH com.syrus.AMFICOM.client_.scheme.SchemeEditor

