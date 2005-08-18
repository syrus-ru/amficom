#!/bin/bash

echo "*** Mscharserver v1.0.0 ``Bobruysk''"


JAVA=$JAVA_HOME/bin/java
LIB=$HOME/lib
EXTLIB=$HOME/extlib

ORACLECLASSPATH=$ORACLE_HOME/jdbc/lib/ojdbc14_g.jar:$ORACLE_HOME/jdbc/lib/orai18n.jar:$ORACLE_HOME/jdbc/lib/classes12.jar:$ORACLE_HOME/jdbc/lib/nls_charset12.jar

EXTCLASSPATH=$EXTLIB/trove.jar:$EXTLIB/jdom.jar:$EXTLIB/micsys.jar:$EXTLIB/miutil.jar:$EXTLIB/mxj.jar:$EXTLIB/commons-logging.jar:$EXTLIB/mxjgeom.jar:$EXTLIB/miappletsup.jar:$EXTLIB/xercesImpl.jar:$EXTLIB/mxjtabdp.jar:$EXTLIB/xbean.jar:$EXTLIB/mistyles.jar:$EXTLIB/dom4j-1.6.jar:$EXTLIB/jaxen-1.1-beta-6.jar

APPCLASSPATH=$LIB/filter.jar:$LIB/mscharserver.jar:$LIB/general.jar:$LIB/administration.jar:$LIB/configuration.jar:$LIB/csbridge.jar:$LIB/leserver_interface.jar:$LIB/map.jar:$LIB/scheme.jar:$LIB/util.jar:$LIB/resource.jar:$LIB/mapview.jar


JAVA="$JAVA -agentlib:jdwp=transport=dt_socket,address=8004,server=y,suspend=n"
$JAVA -Xmx256m -server -ea -classpath $APPCLASSPATH:$ORACLECLASSPATH:$EXTCLASSPATH com.syrus.AMFICOM.mscharserver.MapSchemeAdministrationResourceServer &
# Expands to the process ID of the most recently executed background (asynchronous) command.
echo $! > `dirname $0`/mscharserver.pid
