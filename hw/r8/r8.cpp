//////////////////////////////////////////////////////////////////////
// $Id: r8.cpp,v 1.4 2005/10/13 16:59:44 arseniy Exp $
// 
// Syrus Systems.
// Научно-технический центр
// 2004-2005 гг.
// Проект: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.4 $, $Date: 2005/10/13 16:59:44 $
// $Author: arseniy $
//
// r8.cpp: main function of application.
//
//////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include "RTU.h"
#include "OTDRController.h"
#include "MCMTransceiver.h"

void printUsage(const char* prgName) {
	printf("Usage:\n\t %s <id> <timewait> <max_mcm_timeout> <port> <com_port_number>\n", prgName);
	printf("where\n");
	printf("\t id\t\t - identifier of this RTU in AMFICOM system;\n");
	printf("\t timewait\t - timeout between cycles, sec;\n");
	printf("\t max_mcm_timeout - timeout to disconnect agent, sec;\n");
	printf("\t port\t\t - TCP port for MCM connections;\n");
	printf("\t com_port_number - number of serial ports with optical switches.\n");
}

int main(const int argc, const char* argv[]) {
	char* id;
	unsigned int timewait;
	unsigned int maxMCMTimeout;
	unsigned short port;
	unsigned short comPortNumber;

	if (argc != 6) {
		printUsage(argv[0]);
		return 1;
	}

	id = (char*) argv[1];
	timewait = (unsigned int) atoi(argv[2]);
	maxMCMTimeout = (unsigned int) atoi(argv[3]);
	port = (unsigned short) atoi(argv[4]);
	comPortNumber = (unsigned short) atoi(argv[5]);

	printf("Id: %s, timewait: %d, maxMCMTimeout: %d, port: %d, comPortNumber: %d\n", id, timewait, maxMCMTimeout, port, comPortNumber);

	RTU* rtu = new RTU(comPortNumber, timewait);
	if (rtu->getRTUState() <= RTU_STATE_COM_PORT_INIT_FAILED) {
		delete rtu;
		return 1;
	}
	rtu->start();

	MCMTransceiver* mcmTransceiver = new MCMTransceiver(timewait,
		maxMCMTimeout,
		(SegmentProcessor*) rtu,
		port);
	mcmTransceiver->start();

	char c = 0;
	while (c != 'q') {
		c = _getch();
		Sleep(timewait);
	}

	mcmTransceiver->shutdown();
	pthread_join(mcmTransceiver->getThread(), NULL);

	rtu->shutdown();
	pthread_join(rtu->getThread(), NULL);

	delete mcmTransceiver;
	delete rtu;

	return 1;
}
