#include <stdio.h>
#include <libgen.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include "TransceiverManager.h"


int main(int argc, char* argv[]) {
	char* ID;
	char* RTU_HOSTNAME;
	unsigned short RTU_PORT;
	unsigned int KISTIMEWAIT;

	if (argc != 5) {
		printf("Usage: %s <id> <rtu_hostname> <rtu_port> <kistimewait>\n", basename(argv[0]));
		return 0;
	}
	ID = argv[1];
	RTU_HOSTNAME = argv[2];
	RTU_PORT = (unsigned short)atoi(argv[3]);
	KISTIMEWAIT = atoi(argv[4]);

	TransceiverManager* transceiverManager = new TransceiverManager(ID, RTU_HOSTNAME, RTU_PORT, KISTIMEWAIT);
	transceiverManager->init();

	delete transceiverManager;
	return 1;
}

