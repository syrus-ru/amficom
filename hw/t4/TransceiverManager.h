#ifndef TRANSCEIVERMANAGER_H
#define TRANSCEIVERMANAGER_H

#include <termios.h>
#include "AgentTransceiver.h"
#include "RTUTransceiver.h"

class TransceiverManager {
	public:
		TransceiverManager(char* kis_id, char* rtu_hostname, unsigned short rtu_port, unsigned int timeout);
		virtual ~TransceiverManager();
		void init();
		unsigned int getTimeout() const;
		AgentTransceiver* getAgentTransceiver() const;
		RTUTransceiver* getRTUTransceiver() const;
		void do_key(char c);

	private:
		char* kis_id;
		char* rtu_hostname;
		unsigned short rtu_port;
		unsigned int timeout;
		AgentTransceiver* agentTransceiver;
		RTUTransceiver* rtuTransceiver;
		MeasurementQueueT* measurementQueue;
		ResultQueueT* resultQueue;
		pthread_mutex_t* tmutex;
		pthread_mutex_t* rmutex;
		int go;
		termios savetty;
		int rtu_comm;
		int agent_comm;
		int setupRTUConnection();
		void run();
		void shutdown();
};

#endif

