#!/bin/bash

if [ $# -gt 3 ];
then
	echo "Usage `basename $0` <dest_host> <user> <dest_directory>"
	exit
fi

DEST_HOST=mongol
USER=amficom
DEST_DIR=\~/lib

if [ $# -eq 3 ];
then
	DEST_DIR=$3
fi

if [ $# -ge 2 ];
then
	USER=$2
fi

if [ $# -ge 1 ];
then
	DEST_HOST=$1
fi


LIBDIR=$HOME/work/AMFICOM_v1/lib

FILES="util.jar general.jar administration.jar configuration.jar measurement.jar event.jar csbridge.jar leserver_interface.jar leserver.jar mcm_interface.jar mcm.jar mserver_interface.jar mserver.jar cmserver_interface.jar cmserver.jar dadara.jar"

#FILES="$FILES libmcmtransceiver.so"

STR=
for file in $FILES ;
do
	STR="$STR $LIBDIR/$file"
done

echo "Copying files:"
echo $STR
echo "	to host $DEST_HOST"
echo "	as user $USER"
echo "	to remote directory $DEST_DIR"

scp $STR $USER@$DEST_HOST:$DEST_DIR
