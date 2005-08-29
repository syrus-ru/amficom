//////////////////////////////////////////////////////////////////////
// $Id: RTUTransceiver.h,v 1.8 2005/08/29 18:06:13 arseniy Exp $
// 
// Syrus Systems.
// Научно-технический центр
// 2004-2005 гг.
// Проект: r7
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.8 $, $Date: 2005/08/29 18:06:13 $
// $Author: arseniy $
//
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
#include "OTAUController.h"


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
		const unsigned short com_port_number);
	virtual ~RTUTransceiver();

	void start();
	void shutdown();

	pthread_t get_thread() const;
	RTUState get_state() const;


private:
	static void* run(void* args);
	static int get_wave_index(const int wvlen, const WORD otdr_card);
	static int get_range_index(const double trclen, const WORD otdr_card, const float wave);
	static int get_point_spacing_index(const double res, const WORD otdr_card);
	static int get_pulse_width_index(const short pulswd, const int flag_pulswd_low_res, const WORD otdr_card, const float wave);
	static int ior_is_valid(const double ior, const WORD otdr_card, const float wave);
	static int get_averages_index(const double scans, const WORD otdr_card, const float wave);
	static int get_index_in_array(float val, float* array, int array_size);
	static int get_index_in_array(DWORD val, DWORD* array, int array_size);
	static void print_available_parameters(const WORD otdr_card);

	static const char* PARAMETER_NAME_WAVELENGTH;
	static const char* PARAMETER_NAME_TRACE_LENGTH;
	static const char* PARAMETER_NAME_RESOLUTION;
	static const char* PARAMETER_NAME_PULSE_WIDTH_LOW_RES;
	static const char* PARAMETER_NAME_PULSE_WIDTH_HIGH_RES;
	static const char* PARAMETER_NAME_IOR;
	static const char* PARAMETER_NAME_SCANS;
	static const char* PARAMETER_NAME_FLAG_GAIN_SPLICE_ON;
	static const char* PARAMETER_NAME_FLAG_LIVE_FIBER_DETECT;
	static const char* PARAMETER_NAME_REFLECTOGRAMMA;

	int initialize_OTDR_cards();

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

	OTAUController* otauController;

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
