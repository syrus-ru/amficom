// TransceiverManager.h: interface for the TransceiverManager class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_TRANSCEIVERMANAGER_H__38C3A3E6_1BEE_44CC_B5ED_82FB7EE366C1__INCLUDED_)
#define AFX_TRANSCEIVERMANAGER_H__38C3A3E6_1BEE_44CC_B5ED_82FB7EE366C1__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "MCMTransceiver.h"
#include "RTUTransceiver.h"


class TransceiverManager {
public:
	TransceiverManager(char* id,
			const unsigned int timewait,
			const unsigned short port,
//			char* mcm_hostname,
//			const unsigned short mcm_port,
			const unsigned short mcm_com_port_number);
	virtual ~TransceiverManager();

	void start();
	void shutdown();

	pthread_t get_thread() const;

private:
	static void* run(void* args);

	int setup_mcm_connection();
	int setup_rtu_connection();

	MeasurementQueueT* measurement_queue;
	ResultQueueT* result_queue;
	pthread_mutex_t* tmutex;
	pthread_mutex_t* rmutex;
	pthread_t thread;
	unsigned int timewait;
	int running;

	char* id;

	//Приёмо-передатчики для связи с МУИ и КИС.
	MCMTransceiver* mcmTransceiver;
	RTUTransceiver* rtuTransceiver;

	//Если значение > 0, то соответствующий приёмо-передатчик работает нормально
	//и он не NULL. Иначе - связи нет и приёмо-передатчик должен быть NULL.
//	int mcm_comm;
//	int rtu_comm;

};

#endif // !defined(AFX_TRANSCEIVERMANAGER_H__38C3A3E6_1BEE_44CC_B5ED_82FB7EE366C1__INCLUDED_)
