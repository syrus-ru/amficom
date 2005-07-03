#!/bin/bash

JAVA=$JAVA_HOME/bin/java
LIB=../lib
EXTLIB=../../extlib

EXTCLASSPATH=$EXTLIB/kunststoff.jar:$EXTLIB/trove.jar:$EXTLIB/jdev-rt.jar
APPCLASSPATH=../analysis_v1/classes:\
$LIB/general.jar:\
$LIB/administration.jar:\
$LIB/configuration.jar:\
$LIB/measurement.jar:\
$LIB/resource.jar:\
$LIB/map.jar:\
$LIB/scheme.jar:\
$LIB/mapview.jar:\
$LIB/filter.jar:\
$LIB/dadara.jar:\
$LIB/csbridge.jar:\
$LIB/commonclient.jar:\
$LIB/filterclient.jar:\
$LIB/leserver_interface.jar:\
$LIB/cmserver_interface.jar:\
$LIB/util.jar

$JAVA -Xmx256m -client -ea -Djava.library.path=$LIB -classpath $APPCLASSPATH:$EXTCLASSPATH com.syrus.AMFICOM.Client.Analysis.Reflectometry.AnalyseExt

