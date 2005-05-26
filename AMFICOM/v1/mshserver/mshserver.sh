#!/bin/bash


JAVA=$JAVA_HOME/bin/java
LIB=$AMFICOM_HOME/lib
EXTLIB=$AMFICOM_HOME/lib/external

ORACLECLASSPATH=$ORACLE_HOME/jdbc/lib/ojdbc14_g.jar:$ORACLE_HOME/jdbc/lib/orai18n.jar:$ORACLE_HOME/jdbc/lib/classes12.jar:$ORACLE_HOME/jdbc/lib/nls_charset12.jar

EXTCLASSPATH=$EXTLIB/trove.jar:$EXTLIB/jdom.jar:$EXTLIB/micsys.jar:$EXTLIB/miutil.jar:$EXTLIB/mxj.jar

APPCLASSPATH=$LIB/filter.jar:$LIB/mshserver.jar:$LIB/general.jar:$LIB/administration.jar:$LIB/configuration.jar:$LIB/csbridge.jar:$LIB/leserver_interface.jar:$LIB/map.jar:$LIB/scheme.jar:$LIB/util.jar

$JAVA -Xloggc:./gc -Xmx256m -server -ea -classpath $APPCLASSPATH:$ORACLECLASSPATH:$EXTCLASSPATH com.syrus.AMFICOM.mshserver.MapSchemeServer &
# Expands to the process ID of the most recently executed background (asynchronous) command.
echo $! > pid
