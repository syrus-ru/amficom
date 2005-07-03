#!/bin/bash

if [ ! -d "${JAVA_HOME}" ]
then
	echo "JAVA_HOME environment variable is either undefined or invalid."
	exit 1
fi

if [ ! -d "${ORACLE_HOME}" ]
then
	echo "ORACLE_HOME environment variable is either undefined or invalid."
	exit 1
fi

ORACLECLASSPATH=\
"${ORACLE_HOME}"/jdbc/lib/ojdbc14_g.jar:\
"${ORACLE_HOME}"/jdbc/lib/orai18n.jar:\
"${ORACLE_HOME}"/jdbc/lib/classes12.jar:\
"${ORACLE_HOME}"/jdbc/lib/nls_charset12.jar

if [ ! -d "${TROVE4J_HOME}" ]
then
	TROVE4J_HOME='/export/pkg/trove/noarch/trove-1.1b4'
fi
TROVE4JCLASSPATH=\
"${TROVE4J_HOME}"/lib/trove.jar

if [ ! -d "${AMFICOM_HOME}" ]
then
	AMFICOM_HOME='..'
fi
AMFICOMCLASSPATH=\
"${AMFICOM_HOME}"/util/classes:\
"${AMFICOM_HOME}"/idl_v1/classes:\
"${AMFICOM_HOME}"/general_v1/classes:\
"${AMFICOM_HOME}"/admin_v1/classes:\
"${AMFICOM_HOME}"/resource_v1/classes:\
"${AMFICOM_HOME}"/csbridge_v1/classes:\
"${AMFICOM_HOME}"/mscharserver_v1/classes

CMDLINE="${JAVA_HOME}/bin/java -Xloggc:gc -Xmx256m -server -ea -classpath ${AMFICOMCLASSPATH}:${ORACLECLASSPATH}:${TROVE4JCLASSPATH} com.syrus.AMFICOM.mscharserver.MapSchemeAdministrationResourceServer"

if [ -z "${DISPLAY}" ]
then
	${CMDLINE} >/dev/null 2>&1 &
	# Expands to the process ID of the most recently executed background (asynchronous) command.
	echo $! > pid
else
	xterm -e "(${CMDLINE}; read FOO)" >/dev/null 2>&1 &
fi
