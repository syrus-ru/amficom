// TransceiverManager.cpp: implementation of the TransceiverManager class.
//
//////////////////////////////////////////////////////////////////////

#include "TransceiverManager.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

TransceiverManager::TransceiverManager(char* id,
			const unsigned int timewait,
			const unsigned short port,
//			char* mcm_hostname,
//			const unsigned short mcm_port,
			const unsigned short otau_com_port_number) {
	this->id = id;
	this->timewait = timewait;

	this->measurement_queue = new MeasurementQueueT();
	this->result_queue = new ResultQueueT();

	this->tmutex = (pthread_mutex_t*)malloc(sizeof(pthread_mutex_t));
	pthread_mutex_init(this->tmutex, NULL);
	this->rmutex = (pthread_mutex_t*)malloc(sizeof(pthread_mutex_t));
	pthread_mutex_init(this->rmutex, NULL);

//	this->mcm_comm = 0;
//	this->rtu_comm = 0;

	this->mcmTransceiver = new MCMTransceiver(this->timewait,
										this->measurement_queue,
										this->result_queue,
										this->tmutex,
										this->rmutex,
										port/*,
										mcm_hostname,
										mcm_port*/);
	this->rtuTransceiver = new RTUTransceiver(this->timewait,
										this->measurement_queue,
										this->result_queue,
										this->tmutex,
										this->rmutex);
}

TransceiverManager::~TransceiverManager() {
	this->measurement_queue->clear();
	delete this->measurement_queue;
	this->result_queue->clear();
	delete this->result_queue;

	pthread_mutex_destroy(this->tmutex);
	pthread_mutex_destroy(this->rmutex);

//	if (this->mcm_comm)
		delete this->mcmTransceiver;
//	if (this->rtu_comm)
		delete this->rtuTransceiver;
}

void TransceiverManager::start() {
	//Создать и запустить потоки приёмо-передатчиков
	this->mcmTransceiver->start();
	this->rtuTransceiver->start();

	//Создать и запустить главный поток.
	this->running = 1;
	pthread_attr_t pt_attr;
	pthread_attr_init(&pt_attr);
	pthread_attr_setdetachstate(&pt_attr, PTHREAD_CREATE_JOINABLE);
	pthread_create(&this->thread, &pt_attr, TransceiverManager::run, (void*)this);
	pthread_attr_destroy(&pt_attr);
}


//########################### Main loop -- run #############################
void* TransceiverManager::run(void* args) {
	TransceiverManager* transceiverManager = (TransceiverManager*)args;
	while (transceiverManager->running) {
		Sleep(transceiverManager->timewait);
	}

	return NULL;
}
//##########################################################################


void TransceiverManager::shutdown() {
	printf("TransceiverManager | shutting down\n");

//	if (this->rtu_comm) {
		this->rtuTransceiver->shutdown();
		pthread_join(this->rtuTransceiver->get_thread(), NULL);
//	}
//	if (this->mcm_comm) {
		this->mcmTransceiver->shutdown();
		pthread_join(this->mcmTransceiver->get_thread(), NULL);
//	}

	this->running = 0;
}

pthread_t TransceiverManager::get_thread() const {
	return this->thread;
}
