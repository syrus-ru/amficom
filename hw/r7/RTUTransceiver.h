// RTUTransceiver.h: interface for the RTUTransceiver class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_RTUTRANSCEIVER_H__E38F0B53_467A_428E_9C2E_9E309E38AFBF__INCLUDED_)
#define AFX_RTUTRANSCEIVER_H__E38F0B53_467A_428E_9C2E_9E309E38AFBF__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "tcpconnect.h"
#include "r7general.h"
#include "pthread.h"
#include "qpotdr.h"
#include "BellcoreStructure.h"
#include "BellcoreWriter.h"



#define COM_PORT_READ_TIMEOUT (unsigned long)10000
#define COM_PORT_REPLY_LENGTH (unsigned int)4000
#define MAX_POSSIBLE_OTAUS (unsigned int)100
#define OTAU_COMMAND_INIT ";INIT-SYS::ALL:WXYZ1::0;"
#define OTAU_COMMAND_HDR ";RTRV-HDR:::ABCD;"
#define OTAU_COMMAND_DISCONNECT "DISC-TACC:1:1:1;"
#define OTAU_COMMAND_CONNECT1 "CONN-TACC-OTAU:OTAU"
#define OTAU_COMMAND_CONNECT2 ":ABCD:1;"
#define OTAU_COMMAND_CONNECT_LENGTH 50


enum RTUState {
	RTU_STATE_INIT_COMPLETED,
	RTU_STATE_INIT_FAILED,
	RTU_STATE_READY,
	RTU_STATE_ACUIRING_DATA
};

class RTUTransceiver {
public:
	RTUTransceiver(const unsigned int timewait,
		MeasurementQueueT* measurement_queue,
		ResultQueueT* result_queue,
		pthread_mutex_t* tmutex,
		pthread_mutex_t* rmutex,
		const unsigned short otau_com_port_number);
	virtual ~RTUTransceiver();

	void start();
	void shutdown();

	pthread_t get_thread() const;
	unsigned int get_timewait() const;
	RTUState get_state() const;


private:
	static void* run(void* args);
	static int get_wave_index(const int wvlen, const WORD otdr_card);
	static int get_range_index(const double trclen, const WORD otdr_card, const float wave);
	static int get_point_spacing_index(const double res, const WORD otdr_card);
	static int get_pulse_width_index(const int pulswd, const WORD otdr_card, const float wave);
	static int ior_is_valid(const double ior, const WORD otdr_card, const float wave);
	static int get_averages_index(const double scans, const WORD otdr_card, const float wave);
	static int get_index_in_array(float val, float* array, int array_size);
	static int get_index_in_array(DWORD val, DWORD* array, int array_size);
	static void print_available_parameters(const WORD otdr_card);

	static const char* PARAMETER_NAME_WAVELENGTH;
	static const char* PARAMETER_NAME_TRACE_LENGTH;
	static const char* PARAMETER_NAME_RESOLUTION;
	static const char* PARAMETER_NAME_PULSE_WIDTH;
	static const char* PARAMETER_NAME_IOR;
	static const char* PARAMETER_NAME_SCANS;
	static const char* PARAMETER_NAME_FLAGS;
	static const char* PARAMETER_NAME_REFLECTOGRAMMA;

	int initialize_OTDR_cards();
	void initialize_OTAUs();
	int set_COM_port_properties(const HANDLE com_port_handle);
	void send_switch_command(const HANDLE com_port_handle,
							const char* command,
							const unsigned int command_size,
							char*& reply,
							unsigned int& reply_size);
	unsigned int search_number_of_OTAUs(char* string, int str_size);
	int switch_OTAU(char* local_address, int la_length);
	int parse_local_address(char* local_address,
							unsigned int la_length,
							int& com_port,
							int& otau_id,
							int& otau_port);
	
	int set_measurement_parameters(Parameter** parameters, unsigned int par_number, unsigned int otdr_card_index);
	void print_measurement_parameters() const;
	void retrieve_plugin_data(unsigned int otdr_card_index);
	void fill_bellcore_structure(BellcoreStructure*& bs, QPOTDRWaveformHeader* wave_form_header,QPOTDRWaveformData*  wave_form_data) const;
	

	MeasurementQueueT* measurement_queue;
	ResultQueueT* result_queue;
	pthread_mutex_t* tmutex;
	pthread_mutex_t* rmutex;
	pthread_t thread;
	unsigned int timewait;
	int running;

	RTUState state;

	WORD* otdr_cards;
	unsigned int otdr_cards_number;

	unsigned short com_port_number;
	HANDLE* com_ports;
	unsigned int* otau_numbers;
	HANDLE last_used_com_port;

	float wave_length;
	float trace_length;
	float resolution;
	DWORD pulse_width;
	float index_of_refraction;
	DWORD averages;
	DWORD filter_flags;
	QPPluginData* plugin_data;
};

#endif // !defined(AFX_RTUTRANSCEIVER_H__E38F0B53_467A_428E_9C2E_9E309E38AFBF__INCLUDED_)
