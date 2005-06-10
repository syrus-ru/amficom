#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=$HOME/lib
EXTLIB=$HOME/extlib

ORACLECLASSPATH=$ORACLE_HOME/jdbc/lib/ojdbc14_g.jar:$ORACLE_HOME/jdbc/lib/orai18n.jar:$ORACLE_HOME/jdbc/lib/classes12.jar:$ORACLE_HOME/jdbc/lib/nls_charset12.jar

EXTCLASSPATH=$EXTLIB/trove.jar:$EXTLIB/jdom.jar:$EXTLIB/micsys.jar:$EXTLIB/miutil.jar:$EXTLIB/mxj.jar:$EXTLIB/commons-logging.jar:$EXTLIB/mxjgeom.jar:$EXTLIB/miappletsup.jar:$EXTLIB/xercesImpl.jar:$EXTLIB/mxjtabdp.jar:$EXTLIB/xmlbeans.jar:$EXTLIB/mistyles.jar

APPCLASSPATH=$LIB/filter.jar:$LIB/mscharserver.jar:$LIB/general.jar:$LIB/administration.jar:$LIB/configuration.jar:$LIB/csbridge.jar:$LIB/leserver_interface.jar:$LIB/map.jar:$LIB/scheme.jar:$LIB/util.jar:$LIB/resource.jar:$LIB/mapview.jar:x


$JAVA -Xloggc:./gc -Xmx256m -server -ea -classpath $APPCLASSPATH:$ORACLECLASSPATH:$EXTCLASSPATH com.syrus.AMFICOM.mscharserver.MapSchemeAdministrationResourceServer &
# Expands to the process ID of the most recently executed background (asynchronous) command.
echo $! > mscharserver.pid
