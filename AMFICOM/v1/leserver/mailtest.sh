#!/bin/bash

AMFICOM_HOME=`echo ${AMFICOM_HOME} | sed "s|~|${HOME}|"`

java -classpath \
${AMFICOM_HOME}/util/classes:\
${AMFICOM_HOME}/leserver/classes:\
${JAVAMAIL_HOME}/mail.jar:\
${JAF_HOME}/activation.jar \
com.syrus.AMFICOM.leserver.MailTest

echo "Press any key to continue..."
read foo
