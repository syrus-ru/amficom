#ifndef RTUTRANSCEIVER_H
#define RTUTRANSCEIVER_H

#include <pthread.h>
#include <netinet/in.h>
#include "general.h"

class RTUTransceiver {
	public:
		RTUTransceiver(int rtu_sockfd,
				unsigned int timeout,
				MeasurementQueueT* measurementQueue,
				ResultQueueT* resultQueue,
				pthread_mutex_t* tmutex,
				pthread_mutex_t* rmutex);
		virtual ~RTUTransceiver();
		int getRTUsockfd() const;
		unsigned int getTimeout() const;
		pthread_t getThread() const;
		int isCommError() const;

		static void* run(void* args);
		static void shutdown(RTUTransceiver* rtuTransceiver);
	private:
		static const char RTU_CONSTANT = 0x00;
		int rtu_sockfd;
		unsigned int timeout;
		MeasurementQueueT* measurementQueue;
		ResultQueueT* resultQueue;
		pthread_mutex_t* tmutex;
		pthread_mutex_t* rmutex;
		pthread_t thread;
		int go;
		int comm_error;
		enum RTUStatus {RTU_STATUS_FREE,
				RTU_STATUS_SWITCHED,
				RTU_STATUS_STARTED,
				RTU_STATUS_BUSY,
				RTU_STATUS_READY,
				RTU_STATUS_EOS} rtu_status;

		int processRTUMessage(char* mesg);
		void processTimeSync();
		void setMeasurementParameters(Parameter** pars, int& wvlen, double& trclen,
			       	double& res, int& pulswd, double& ior, double& scans) const ;
		int receiveRTUReply(char*& reply, unsigned int& reply_length, int t);
		int sendCommand(char* command, unsigned int length, unsigned int& send_counter);
};

#endif
