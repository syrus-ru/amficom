// MCMTransceiver.h: interface for the MCMTransceiver class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_MCMTRANSCEIVER_H__B961CE12_6882_48C2_8E50_9B0BCA542A18__INCLUDED_)
#define AFX_MCMTRANSCEIVER_H__B961CE12_6882_48C2_8E50_9B0BCA542A18__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "tcpconnect.h"
#include "r7general.h"
#include "pthread.h"


class MCMTransceiver {
public:
	MCMTransceiver(const unsigned int timewait,
			const unsigned int max_mcm_timeout,
			MeasurementQueueT* measurement_queue,
			ResultQueueT* result_queue,
			pthread_mutex_t* tmutex,
			pthread_mutex_t* rmutex,
			const unsigned short port);
	virtual ~MCMTransceiver();

	void start();
	void shutdown();

	pthread_t get_thread() const;
	unsigned int get_timewait() const;
	unsigned int get_max_mcm_timeout() const;
	

private:
	static void* run(void* args);

	void process_segment(Segment* segment) const;
	void cleanup_socket();
	static int startup_WSA();
	static void cleanup_WSA();

	MeasurementQueueT* measurement_queue;
	ResultQueueT* result_queue;
	pthread_mutex_t* tmutex;
	pthread_mutex_t* rmutex;
	pthread_t thread;
	unsigned int timewait;
	unsigned int max_mcm_timeout;
	int running;

	SOCKET sockfd;
	unsigned short port;
};

#endif // !defined(AFX_MCMTRANSCEIVER_H__B961CE12_6882_48C2_8E50_9B0BCA542A18__INCLUDED_)
