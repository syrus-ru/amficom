#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include "RTUTransceiver.h"
#include "MCMTransceiver.h"


int main(const int argc, const char* argv[]) {
	char* id;
	unsigned int timewait;
	unsigned int max_mcm_timeout;
	unsigned short port;
	unsigned short com_port_number;

	if (argc != 6) {
		printf("Usage:\nr7 <id> <timewait> <max_mcm_timeout> <port> <com_port_number>\n");
		return 1;
	}

	id = (char*)argv[1];
	timewait = (unsigned int)atoi(argv[2]);
	max_mcm_timeout = (unsigned int)atoi(argv[3]);
	port = (unsigned short)atoi(argv[4]);
	com_port_number = (unsigned short)atoi(argv[5]);


	MeasurementQueueT* measurement_queue = new MeasurementQueueT();
	ResultQueueT* result_queue = new ResultQueueT();

	pthread_mutex_t* tmutex = (pthread_mutex_t*)malloc(sizeof(pthread_mutex_t));
	pthread_mutex_init(tmutex, NULL);
	pthread_mutex_t* rmutex = (pthread_mutex_t*)malloc(sizeof(pthread_mutex_t));
	pthread_mutex_init(rmutex, NULL);

	RTUTransceiver* rtuTransceiver = new RTUTransceiver(timewait,
												measurement_queue,
												result_queue,
												tmutex,
												rmutex,
												com_port_number);
	if (rtuTransceiver->get_state() != RTU_STATE_INIT_COMPLETED) {
		printf("ERROR: Cannot initialize RTU transceiver -- exit\n");
		delete rtuTransceiver;
		return -1;
	}
	MCMTransceiver* mcmTransceiver = new MCMTransceiver(timewait,
														max_mcm_timeout,
														measurement_queue,
														result_queue,
														tmutex,
														rmutex,
														port);

	mcmTransceiver->start();
	rtuTransceiver->start();

	char c = 0;
	while (c != 'q') {
		c = _getch();
		Sleep(timewait);
	}

	rtuTransceiver->shutdown();
	pthread_join(rtuTransceiver->get_thread(), NULL);
	mcmTransceiver->shutdown();
	pthread_join(mcmTransceiver->get_thread(), NULL);

	Sleep(timewait);

	measurement_queue->clear();
	delete measurement_queue;
	result_queue->clear();
	delete result_queue;

	pthread_mutex_destroy(tmutex);
	pthread_mutex_destroy(rmutex);

	delete mcmTransceiver;
	delete rtuTransceiver;

	return 1;
}