#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=../lib
EXTLIB=../../extlib

EXTCLASSPATH=$EXTLIB/kunststoff.jar:$EXTLIB/trove.jar
APPCLASSPATH=$LIB/scheduler.jar:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/map.jar:\
$LIB/mapview.jar:\
$LIB/scheme.jar:\
$LIB/filter.jar:\
$LIB/resource.jar:\
$LIB/csbridge.jar:\
$LIB/commonclient.jar:\
$LIB/filterclient.jar:\
$LIB/leserver_interface.jar:\
$LIB/cmserver_interface.jar:\
$LIB/util.jar

$JAVA -Xmx256m -client -ea -classpath $APPCLASSPATH:$EXTCLASSPATH com.syrus.AMFICOM.Client.Schedule.Schedule

