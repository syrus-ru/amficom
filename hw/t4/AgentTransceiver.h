#ifndef AGENTTRANSCEIVER_H
#define AGENTTRANSCEIVER_H

#include <pthread.h>
#include "general.h"

class AgentTransceiver {
	public:
		AgentTransceiver(char* kis_id,
				unsigned int timeout,
				MeasurementQueueT* measurementQueue,
				ResultQueueT* resultQueue,
				pthread_mutex_t* tmutex,
				pthread_mutex_t* rmutex);
		virtual ~AgentTransceiver();
		char* getTaskFifoName() const;
		char* getReportFifoName() const;
		unsigned int getTimeout() const;
		pthread_t getThread() const;

		static void* run(void* args);
		static void shutdown(AgentTransceiver* agentTransceiver);

	private:
		char* taskFifoName;
		char* reportFifoName;
		unsigned int timeout;
		MeasurementQueueT* measurementQueue;
		ResultQueueT* resultQueue;
		pthread_mutex_t* tmutex;
		pthread_mutex_t* rmutex;
		pthread_t thread;
		int go;
};

#endif
