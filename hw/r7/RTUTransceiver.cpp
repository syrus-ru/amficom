//////////////////////////////////////////////////////////////////////
// $Id: RTUTransceiver.cpp,v 1.24 2005/09/25 17:36:59 arseniy Exp $
// 
// Syrus Systems.
// оЅ’ёќѕ-‘≈»ќ…ё≈”Ћ…  √≈ќ‘“
// 2004-2005 ««.
// р“ѕ≈Ћ‘: r7
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.24 $, $Date: 2005/09/25 17:36:59 $
// $Author: arseniy $
//
// RTUTransceiver.cpp: implementation of the RTUTransceiver class.
//
//////////////////////////////////////////////////////////////////////

#include "RTUTransceiver.h"
#include <list>


const char* RTUTransceiver::PARAMETER_NAME_WAVELENGTH = "ref_wvlen";
const char* RTUTransceiver::PARAMETER_NAME_TRACE_LENGTH = "ref_trclen";
const char* RTUTransceiver::PARAMETER_NAME_RESOLUTION = "ref_res";
const char* RTUTransceiver::PARAMETER_NAME_PULSE_WIDTH_LOW_RES = "ref_pulswd_low_res";
const char* RTUTransceiver::PARAMETER_NAME_PULSE_WIDTH_HIGH_RES = "ref_pulswd_high_res";
const char* RTUTransceiver::PARAMETER_NAME_IOR = "ref_ior";
const char* RTUTransceiver::PARAMETER_NAME_SCANS = "ref_scans";
const char* RTUTransceiver::PARAMETER_NAME_FLAG_GAIN_SPLICE_ON = "ref_flag_gain_splice_on";
const char* RTUTransceiver::PARAMETER_NAME_FLAG_LIVE_FIBER_DETECT = "ref_flag_life_fiber_detect";
const char* RTUTransceiver::PARAMETER_NAME_REFLECTOGRAMMA = "reflectogramma";

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

RTUTransceiver::RTUTransceiver(const unsigned int timewait,
								   MeasurementQueueT* measurement_queue,
								   ResultQueueT* result_queue,
								   pthread_mutex_t* tmutex,
								   pthread_mutex_t* rmutex,
								   const unsigned short com_port_number) {
	this->timewait = timewait;

	//ќчереди запросов на измерение и результатов измерений, флаги синхронизации
	this->measurement_queue = measurement_queue;
	this->result_queue = result_queue;
	this->tmutex = tmutex;
	this->rmutex = rmutex;

	if (this->initialize_OTDR_cards()) {

		print_available_parameters(this->otdr_cards[0]);

		this->otauController = new OTAUController(this->timewait, com_port_number);

		this->state = RTU_STATE_INIT_COMPLETED;
	}
	else {
		this->state = RTU_STATE_INIT_FAILED;
	}
}

RTUTransceiver::~RTUTransceiver() {
	int i;

	if (this->state == RTU_STATE_ACUIRING_DATA) {
		for (i = 0; i < this->otdr_cards_number; i++) {
			printf ("RTUTransceiver | stoping measurement on OTDR card %u ...\n", this->otdr_cards[i]);
			QPOTDRAcqStop(this->otdr_cards[i]);
		}
		this->otauController->shutdown();
		pthread_join(this->otauController->get_thread(), NULL);
	}

	if (this->state != RTU_STATE_INIT_FAILED) {
		delete[] this->otdr_cards;
		delete this->otauController;
	}
}

void RTUTransceiver::start() {
	//—оздать и запустить главный поток.
	this->running = 1;
	pthread_attr_t pt_attr;
	pthread_attr_init(&pt_attr);
	pthread_attr_setdetachstate(&pt_attr, PTHREAD_CREATE_JOINABLE);
	pthread_create(&this->thread, &pt_attr, RTUTransceiver::run, (void*)this);
	pthread_attr_destroy(&pt_attr);
	this->state = RTU_STATE_READY;
}



//########################### Main loop -- run #############################

void* RTUTransceiver::run(void* args) {
	RTUTransceiver* rtuTransceiver = (RTUTransceiver*)args;

	MeasurementSegment* measurement_segment;
	ByteArray* local_address;
//	int need_switch_otau;
//	ByteArray* previous_address = NULL;
	Parameter** parameters = NULL;
	unsigned int par_number = 0;

	//Now use only one OTDR card on a RTU
	unsigned int otdr_card_index = 0;

	while (rtuTransceiver->running) {

		Sleep(rtuTransceiver->timewait * 1000);	//Convert to milliseconds

		pthread_mutex_lock(rtuTransceiver->tmutex);
		printf ("RTUTransceiver | %u measurements in queue\n", rtuTransceiver->measurement_queue->size());
		if (! rtuTransceiver->measurement_queue->empty()) {
			measurement_segment = rtuTransceiver->measurement_queue->back();
			rtuTransceiver->measurement_queue->pop_back();
			pthread_mutex_unlock(rtuTransceiver->tmutex);

			local_address = measurement_segment->getLocalAddress();

			/**
			* Alwais switch OTAU
			*/
//			need_switch_otau = 0;
//			if (previous_address == NULL || (*local_address) != (*previous_address))
//				need_switch_otau = 1;
//
//			if (need_switch_otau) {
//				if (! rtuTransceiver->switch_OTAU(local_address->getData(), local_address->getLength())) {
//					printf("RTUTransceiver | ERROR: Cannot switch OTAU. Measurement cancelled\n");
//					delete measurement_segment;
//					continue;
//				}
//				if (previous_address != NULL)
//					delete previous_address;
//				previous_address = local_address->clone();
//			}
			if (!rtuTransceiver->otauController->start(local_address)) {
				printf("RTUTransceiver | ERROR: Cannot switch OTAU. Measurement cancelled\n");
				delete measurement_segment;
				continue;
			}

			rtuTransceiver->retrieve_plugin_data(otdr_card_index);

			parameters = measurement_segment->getParameters();
			par_number = measurement_segment->getParnumber();
			if (! rtuTransceiver->set_measurement_parameters(parameters, par_number, otdr_card_index)) {
				printf("RTUTransceiver | ERROR: Incorrect parameters used, measurement canceled!\n");
				delete measurement_segment;
				free(rtuTransceiver->plugin_data);

				rtuTransceiver->otauController->shutdown();
				pthread_join(rtuTransceiver->otauController->get_thread(), NULL);

				continue;
			}

			rtuTransceiver->print_measurement_parameters();

			QPOTDRWaveformHeader* wave_form_header = (QPOTDRWaveformHeader*)malloc(sizeof(QPOTDRWaveformHeader));
			QPOTDRWaveformData* wave_form_data = new QPOTDRWaveformData[MAX_WFM_POINTS];//0xF000
			memset(wave_form_header, 0, sizeof(QPOTDRWaveformHeader));
			memset(wave_form_data, 0, sizeof (MAX_WFM_POINTS * sizeof(QPOTDRWaveformData)));

			printf ("RTUTransceiver | Starting measurements on RTU\n");
			HANDLE* events = QPOTDRAcqStart(rtuTransceiver->otdr_cards[otdr_card_index], rtuTransceiver->filter_flags);
			rtuTransceiver->state = RTU_STATE_ACUIRING_DATA;
			int ret;
			do { 
				// Wait for notification event from DAU
				ret = WaitForMultipleObjects(3, events, FALSE, INFINITE);

				// New waveform
				if (ret == WAIT_OBJECT_0 + 1) {
					// Get trace data
					QPOTDRRetrieveWaveformSync(rtuTransceiver->otdr_cards[otdr_card_index], wave_form_header, wave_form_data, false);
				}
				else // Life fiber / critical error
					if (ret == WAIT_OBJECT_0 + 2) {
						printf("RTUTransceiver | ERROR: Life fiber detected or critical error occured. Test aborted\n");
					}

			}
			while (ret == WAIT_OBJECT_0 + 1);
//			ret = WaitForMultipleObjects(3, events, FALSE, INFINITE);//???
			if (ret == WAIT_OBJECT_0) {
				wave_form_header->updateData = 1;
			}

//			if (wave_form_header->updateData) {//???
//				QPOTDRFilterLastWaveform(otdr_cards[otdr_card_index], wave_form_data);
//			}

			QPOTDRAcqStop(rtuTransceiver->otdr_cards[otdr_card_index]);
			rtuTransceiver->otauController->shutdown();
			pthread_join(rtuTransceiver->otauController->get_thread(), NULL);
			rtuTransceiver->state = RTU_STATE_READY;

			ByteArray* bmeasurement_id = measurement_segment->getMeasurementId()->clone();
			unsigned int len = strlen(PARAMETER_NAME_REFLECTOGRAMMA);
			char* databa = new char[len];
			memcpy(databa, PARAMETER_NAME_REFLECTOGRAMMA, len);
			ByteArray* b_name = new ByteArray(len, databa);

			BellcoreStructure* bs = new BellcoreStructure();
			rtuTransceiver->fill_bellcore_structure(bs, wave_form_header, wave_form_data);

			/*Deallocate unused structures and array*/
			free(wave_form_header);
			delete[] wave_form_data;
			free(rtuTransceiver->plugin_data);

			BellcoreWriter* bw = new BellcoreWriter();
			bw->write(bs);
			char* bellcore_data = (char*)bw->get_data();
			int bellcore_data_size = bw->get_data_size();
			delete bw;
			delete bs;

			ByteArray* b_value = new ByteArray(bellcore_data_size, bellcore_data);

			parameters = new Parameter*[1];
			parameters[0] = new Parameter(b_name, b_value);
			ResultSegment* result_segment = new ResultSegment(bmeasurement_id, 1, parameters);

			pthread_mutex_lock(rtuTransceiver->rmutex);
			rtuTransceiver->result_queue->push_front(result_segment);
			pthread_mutex_unlock(rtuTransceiver->rmutex);

			/*Delete measurement segment -- it is processed now*/
			delete measurement_segment;

		}//if (! rtuTransceiver->measurementQueue->empty())
		else {
			pthread_mutex_unlock(rtuTransceiver->tmutex);
		}
	}

	return NULL;
}
//##########################################################################



void RTUTransceiver::shutdown() {
	printf("RTUTransceiver | Shutting down\n");
	this->running = 0;
}

pthread_t RTUTransceiver::get_thread() const {
	return this->thread;
}

RTUState RTUTransceiver::get_state() const {
	return this->state;
}


/* Initalizes all awailable OTDR cards */
int RTUTransceiver::initialize_OTDR_cards() {
	WORD* cards = new WORD[MAX_CARDS];
	QPOTDRGetCards(cards);

	int i;
	list<WORD> present_cards;
	int init_ret;
	for (i = 0; i < MAX_CARDS; i++) {
		if (cards[i] != 0xFFFF) {
			init_ret = QPOTDRInitialize(cards[i]);
			if (init_ret == 0) {
				printf("RTUTransceiver | Init OTDR card %u - success\n", cards[i]);
				present_cards.push_back(cards[i]);
			}
			else {
				printf("RTUTransceiver | Init OTDR card %u - failure; returned: %d\n", cards[i], init_ret);
			}
		}
	}

	delete[] cards;

	this->otdr_cards_number = present_cards.size();
	if (this->otdr_cards_number == 0) {
		printf("RTUTransceiver | ERROR: None OTDR cards found\n");
		return 0;
	}
	printf("RTUTransceiver | Found %u OTDR card(s)\n", this->otdr_cards_number);
	this->otdr_cards = new WORD[this->otdr_cards_number];
	i = 0;
	for (list<WORD>::iterator it = present_cards.begin(); it != present_cards.end(); it++) {
		this->otdr_cards[i++] = *it;
	}

	return 1;
}

int RTUTransceiver::set_measurement_parameters(Parameter** parameters, unsigned int par_number, unsigned int otdr_card_index) {
	int wvlen = -1;
	double trclen = -1;
	double res = -1;
	short pulswd = -1;
	double ior = -1;
	double scans = -1;
	int flag_pulswd_low_res = 1;
	int flag_gain_splice_on = 0;
	int flag_live_fiber_detect = 0;

	unsigned int i;

	char* par_name;
	ByteArray* bvalue;
	for (i = 0; i < par_number; i++) {
		par_name = parameters[i]->getName()->getData();
		if (strcmp(par_name, PARAMETER_NAME_WAVELENGTH) == 0) {
			bvalue = parameters[i]->getValue()->getReversed();
			wvlen = *(int*)(bvalue->getData());
			delete bvalue;
		}
		else if (strcmp(par_name, PARAMETER_NAME_TRACE_LENGTH) == 0) {
			bvalue = parameters[i]->getValue()->getReversed();
			trclen = *(double*)(bvalue->getData());
			delete bvalue;
		}
		else if (strcmp(par_name, PARAMETER_NAME_RESOLUTION) == 0) {
			bvalue = parameters[i]->getValue()->getReversed();
			res = *(double*)(bvalue->getData());
			delete bvalue;
		}
		else if (strcmp(par_name, PARAMETER_NAME_PULSE_WIDTH_HIGH_RES) == 0) {
			bvalue = parameters[i]->getValue()->getReversed();
			pulswd = *(short*)(bvalue->getData());
			flag_pulswd_low_res = 0;
			delete bvalue;
		}
		else if (strcmp(par_name, PARAMETER_NAME_PULSE_WIDTH_LOW_RES) == 0) {
			bvalue = parameters[i]->getValue()->getReversed();
			pulswd = *(short*)(bvalue->getData());
			flag_pulswd_low_res = 1;
			delete bvalue;
		}
		else if (strcmp(par_name, PARAMETER_NAME_IOR) == 0) {
			bvalue = parameters[i]->getValue()->getReversed();
			ior = *(double*)(bvalue->getData());
			delete bvalue;
		}
		else if (strcmp(par_name, PARAMETER_NAME_SCANS) == 0) {
			bvalue = parameters[i]->getValue()->getReversed();
			scans = *(double*)(bvalue->getData());
			delete bvalue;
		}
		else if (strcmp(par_name, PARAMETER_NAME_FLAG_GAIN_SPLICE_ON) == 0) {
			bvalue = parameters[i]->getValue()->getReversed();
			flag_gain_splice_on = *(int*)(bvalue->getData());
			delete bvalue;
		}
		else if (strcmp(par_name, PARAMETER_NAME_FLAG_LIVE_FIBER_DETECT) == 0) {
			bvalue = parameters[i]->getValue()->getReversed();
			flag_live_fiber_detect = *(int*)(bvalue->getData());
			delete bvalue;
		}
		else {
			printf("RTUTransceiver | Unknown name of parameter: %s\n", par_name);
		}
	}

	int wave_index = get_wave_index(wvlen, this->otdr_cards[otdr_card_index]);
	if (wave_index < 0) {
		printf("RTUTransceiver | ERROR: Wavelength %d not found in array of valid values\n", wvlen);
		return 0;
	}
	this->wave_length = (float)wvlen;

	int range_index = get_range_index(trclen, this->otdr_cards[otdr_card_index], this->wave_length);
	if (range_index < 0) {
		printf("RTUTransceiver | ERROR: Trace length %f not found in array of valid values\n", trclen);
		return 0;
	}
	this->trace_length = (float)trclen;

	int point_spacing_index = get_point_spacing_index(res, this->otdr_cards[otdr_card_index]);
	if (point_spacing_index < 0) {
		printf("RTUTransceiver | ERROR: Resolution %f not found in array of valid values\n", res);
		return 0;
	}
	this->resolution = (float)res;

	int pulse_width_index = get_pulse_width_index(pulswd, flag_pulswd_low_res, this->otdr_cards[otdr_card_index], this->wave_length);
	if (pulse_width_index < 0) {
		printf("RTUTransceiver | ERROR: Pulse width %d not found in array of valid values\n", pulswd);
		return 0;
	}
	this->pulse_width = (DWORD)pulswd;

	if (! ior_is_valid(ior, this->otdr_cards[otdr_card_index], this->wave_length)) {
		printf("RTUTransceiver | ERROR: Index of refraction %f is not valid\n", ior);
		return 0;
	}
	this->index_of_refraction = (float)ior;

	int averages_index = get_averages_index(scans, this->otdr_cards[otdr_card_index], this->wave_length);
	if (averages_index < 0) {
		printf("RTUTransceiver | ERROR: Number of averages %f not found in array of valid values\n", scans);
		return 0;
	}
	this->averages = (DWORD)scans;

	this->filter_flags = 0;
	if (flag_gain_splice_on) {
		this->filter_flags |= GAIN_SPLICE_ON;
	}
	if (flag_live_fiber_detect) {
		this->filter_flags |= LIVE_FIBER_DETECT;
	}

	int set_params_ret = QPOTDRAcqSetParams(this->otdr_cards[otdr_card_index],
								(WORD)(this->averages / this->plugin_data->dwFastScanCount),
								wave_index,
								(DWORD)(this->trace_length * 1000.f / this->resolution/*min (resolution, 4.0f)*/),//???
								point_spacing_index,
								pulse_width_index,
								3 + 1);//???

    switch(set_params_ret) {
		case 0:
			break;
		case -1:
			printf("RTUTransceiver | ERROR: QPOTDRAcqSetParams returned - bad card index\n");
			return 0;
		case -2:
			printf("RTUTransceiver | ERROR: QPOTDRAcqSetParams returned - bad number of averages\n");
			return 0;
		case -3:
			printf("RTUTransceiver | ERROR: QPOTDRAcqSetParams returned - bad wave index\n");
			return 0;
		case -4:
			printf("RTUTransceiver | ERROR: QPOTDRAcqSetParams returned - bad pulse index\n");
			return 0;
		case -5:
			printf("RTUTransceiver | ERROR: QPOTDRAcqSetParams returned - bad range\n");
			return 0;
		case -6:
			printf("RTUTransceiver | ERROR: QPOTDRAcqSetParams returned - bad point spacing index\n");
			return 0;
		default:
			printf("RTUTransceiver | ERROR: QPOTDRAcqSetParams returned - unknown error code\n");
			return 0;
    }

	return 1;
}

int RTUTransceiver::get_wave_index(const int wvlen, const WORD otdr_card) {
	int ret;
	int max_waves = QPOTDRGetMaxWaves(otdr_card);
	if (max_waves > 0) {
		float* waves = new float[MAX_WAVES];
		QPOTDRGetAvailWaves(otdr_card, waves);
		ret = get_index_in_array((float)wvlen, waves, max_waves);
		delete[] waves;
	}
	else {
		printf("RTUTransceiver | ERROR: QPOTDRGetMaxWaves returned error\n");
		ret = -1;
	}
	return ret;
}

int RTUTransceiver::get_range_index(const double trclen, const WORD otdr_card, const float wave) {
	int ret;
	int max_ranges = QPOTDRGetMaxRanges(otdr_card, wave);
	if (max_ranges > 0) {
		float* ranges = new float[MAX_RANGES];
		QPOTDRGetAvailRanges(otdr_card, wave, ranges);
		ret = get_index_in_array((float)trclen, ranges, max_ranges);
		delete[] ranges;
	}
	else {
		printf("RTUTransceiver | ERROR: QPOTDRGetMaxRanges returned error\n");
		ret = -1;
	}
	return ret;
}

int RTUTransceiver::get_point_spacing_index(const double res, const WORD otdr_card) {
	int ret;
	int max_point_spacings = QPOTDRGetMaxPointSpacings(otdr_card);
	if (max_point_spacings > 0) {
		float* point_spacings = new float[MAX_SPACINGS];
		QPOTDRGetAvailSpacings(otdr_card, point_spacings);
		//FIXME: Total number of values is 8, but QPOTDRGetMaxPointSpacings returns 7
		ret = get_index_in_array((float)res, point_spacings, MAX_SPACINGS);
		delete[] point_spacings;
	}
	else {
		printf("RTUTransceiver | ERROR: QPOTDRGetMaxPointSpacings returned error\n");
		ret = -1;
	}
	return ret;
}

int RTUTransceiver::get_pulse_width_index(const short pulswd,
										  const int flag_pulswd_low_res,
										  const WORD otdr_card,
										  const float wave) {
	int max_pulse_widths = QPOTDRGetMaxPulses(otdr_card, wave);

	if (max_pulse_widths > 0) {
		DWORD pulse_widths[MAX_PULSES];
		QPOTDRGetAvailPulses(otdr_card, wave, pulse_widths);
		for (unsigned int i = 0; i < max_pulse_widths; i++) {
			if ((pulse_widths[i] >> 16 == pulswd)
				&& ((pulse_widths[i] & 0x00000001) == flag_pulswd_low_res))
				return i;
		}
		return -1;
	}

	printf("RTUTransceiver | ERROR: QPOTDRGetMaxPulses returned error\n");
	return -1;
}

int RTUTransceiver::ior_is_valid(const double ior, const WORD otdr_card, const float wave) {
	float default_ior = QPOTDRGetDefaultIOR(otdr_card, wave);
	return (((int)ior * 10000) == ((int)default_ior * 10000)) ? 1 : 0;
}

int RTUTransceiver::get_averages_index(const double scans, const WORD otdr_card, const float wave) {
	int ret;
	int max_averages = QPOTDRGetMaxAverages(otdr_card, wave);
	if (max_averages > 0) {
		DWORD* avergs = new DWORD[MAX_AVERAGES];
		QPOTDRGetAvailAverages(otdr_card, wave, avergs);
		ret = get_index_in_array((DWORD)scans, avergs, max_averages);
		delete[] avergs;
	}
	else {
		printf("RTUTransceiver | ERROR: QPOTDRGetMaxAverages returned error\n");
		ret = -1;
	}
	return ret;
}


void RTUTransceiver::print_measurement_parameters() const {
	printf("\n***Measurement parameters***\n");

	printf ("\"Wave length\" == %f.\n", this->wave_length);
	printf ("\"Trace length\" == %f.\n", this->trace_length);
	printf ("\"Resolution\" == %f.\n", this->resolution);
	printf ("\"Pulse width\" == %d.\n", this->pulse_width);
	printf ("\"Index of refraction\" == %f.\n", this->index_of_refraction);
	printf ("\"Averages count\" == %d.\n", this->averages);

	printf("\n");
}

void RTUTransceiver::retrieve_plugin_data(unsigned int otdr_card_index) {
	this->plugin_data = (QPPluginData*)malloc(sizeof(QPPluginData));
	memset(this->plugin_data, 0, sizeof(QPPluginData));
	QPOTDRDataCollectInfo(this->otdr_cards[otdr_card_index], this->plugin_data);
}

void RTUTransceiver::fill_bellcore_structure(BellcoreStructure*& bs, QPOTDRWaveformHeader* wave_form_header,QPOTDRWaveformData*  wave_form_data) const {
	int offset = wave_form_header->FPOffset >> 16;
	int i;

	bs->add_field_gen_params("Cable ID",
				"Fiber ID",
				0,
				(short)this->wave_length,
				"Originating location",
				"Terminating location",
				"Cable code",
				"DF",
				"Operator",
				"QuestProbe OTAU");


	char sr[5];
	memset(sr, 0, 5);
	sprintf(sr, "%d", this->plugin_data->wRevision);
	bs->add_field_sup_params(this->plugin_data->pluginInfo.szManufacturer,
				this->plugin_data->pluginInfo.szModel, //"Nettest QuestProbe"
				this->plugin_data->pluginInfo.SerialNumber,
				this->plugin_data->pluginInfo.ModelNumber,
				this->plugin_data->pluginInfo.szPartNumber,
				sr,
				"Other");


	//Get the number of 100-nanosecond intervals since 1.01.1601
	SYSTEMTIME sysTime;
	GetSystemTime(&sysTime);
	FILETIME fileTime;
	SystemTimeToFileTime(&sysTime,&fileTime);
	ULARGE_INTEGER * time = (ULARGE_INTEGER *) (&fileTime);
	//Get the same value for time 00:00 1.01.1970
	SYSTEMTIME sysTime1970;
	sysTime1970.wYear = 1970;
	sysTime1970.wMonth = 1;
	sysTime1970.wDay = 1;
	sysTime1970.wHour = 0;
	sysTime1970.wMinute = 0;
	sysTime1970.wSecond = 0;
	sysTime1970.wMilliseconds = 0;
	FILETIME fileTime1970;
	SystemTimeToFileTime(&sysTime1970,&fileTime1970);
	ULARGE_INTEGER * time1970 = (ULARGE_INTEGER *) (&fileTime1970);
	//Calculate difference between theese two dates -- current time since 00:00 1.01.1970 in seconds
	unsigned long dts = (unsigned long)(time->QuadPart/10000000 - time1970->QuadPart/10000000);

	short tpw = 1;
	short* pwu = new short[tpw];
	pwu[0] = (short)this->pulse_width;
	int* ds = new int[tpw];
	ds[0] = (int)(10000. * this->resolution * this->index_of_refraction * 100. / 3.);//10000. - ???
	int* nppw = new int[tpw];
	nppw[0] = (long)((float)(this->pulse_width) * 3. / (this->index_of_refraction * this->resolution * 10.));

	bs->add_field_fxd_params(dts,
				"mt",
				(short)(this->wave_length * 10),
				0,//offset
				tpw,
				pwu,
				ds,
				nppw,
				this->index_of_refraction * 100000,
				this->averages,
				(int)(this->trace_length * 1000.f * this->index_of_refraction * 100. / 3.));

	delete[] pwu;
	delete[] ds;
	delete[] nppw;


	int np = wave_form_header->NumPts - offset;

	int tndp = np;
	short tsf = 1;
	int* tps = new int[tsf];
	tps[0] = np;
	short* sf = new short[tsf];
	sf[0] = 1000;
	unsigned short** dsf = new unsigned short*[tsf];
	dsf[0] = new unsigned short[np];
	for (i = 0; i < np; i++) {
		dsf[0][i] = 65535 - wave_form_data[i + offset];
	}
	bs->add_field_data_pts(tndp,
				tsf,
				tps,
				sf,
				dsf);
	delete[] tps;
	delete[] sf;
	//!!! Don't delete dsf - it will be deleted in the destructor of BellcoreStructure


	bs->add_field_cksum(0);


	bs->add_field_map();

}


int RTUTransceiver::get_index_in_array(float val, float* array, int array_size) {
	int ret = -1;
	if (array != NULL) {
		for (int i = 0; i < array_size; i++) {
			if (val == array[i]) {
				ret = i;
				break;
			}
		}
	}
	return ret;
}

int RTUTransceiver::get_index_in_array(DWORD val, DWORD* array, int array_size) {
	int ret = -1;
	if (array != NULL) {
		for (int i = 0; i < array_size; i++) {
			if (val == array[i]) {
				ret = i;
				break;
			}
		}
	}
	return ret;
}



void RTUTransceiver::print_available_parameters(const WORD otdr_card) {
	int max_waves = QPOTDRGetMaxWaves(otdr_card);
	if (max_waves <= 0) {
		printf("RTUTransceiver | ERROR: QPOTDRGetMaxWaves returned error\n");
		return;
	}

	int i;
	float* waves = new float[MAX_WAVES];
	QPOTDRGetAvailWaves(otdr_card, waves);
	for (i = 0; i < max_waves; i++) {
		//Wavelength
		printf("Wave length: %f\n", waves[i]);
		int j;

		//Ranges
		int max_ranges = QPOTDRGetMaxRanges(otdr_card, waves[i]);
		if (max_ranges <= 0) {
			printf("RTUTransceiver | ERROR: QPOTDRGetMaxRanges returned error\n");
			delete[] waves;
			return;
		}
		float* ranges = new float[MAX_RANGES];
		QPOTDRGetAvailRanges(otdr_card, waves[i], ranges);
		for (j = 0; j < max_ranges; j++)
			printf("\tRange: %f\n", ranges[j]);
		delete[] ranges;

		//Pulse width (for low and high resolution modes)
		int max_pulse_widths = QPOTDRGetMaxPulses(otdr_card, waves[i]);
		if (max_pulse_widths <= 0) {
			printf("RTUTransceiver | ERROR: QPOTDRGetMaxPulses returned error\n");
			delete[] waves;
			return;
		}
		DWORD* pulse_widths = new DWORD[MAX_PULSES];
		QPOTDRGetAvailPulses(otdr_card, waves[i], pulse_widths);
		for (j = 0; j < max_pulse_widths; j++) {
			const int flag_pulswd_low_res = pulse_widths[j] & 0x00000001;
			if (flag_pulswd_low_res) {
				const short pulswd = pulse_widths[j] >> 16;
				printf("\tPulse witdh low res\t%u\n", pulswd);
			}
		}
		for (j = 0; j < max_pulse_widths; j++) {
			const int flag_pulswd_low_res = pulse_widths[j] & 0x00000001;
			if (!flag_pulswd_low_res) {
				const short pulswd = pulse_widths[j] >> 16;
				printf("\tPulse witdh high res\t%u\n", pulswd);
			}
		}
		delete[] pulse_widths;

		//Index of refraction
		float ior = QPOTDRGetDefaultIOR(otdr_card, waves[i]);
		printf("\t Index of refraction: %f\n", ior);

		//Number of averages
		int max_averages = QPOTDRGetMaxAverages(otdr_card, waves[i]);
		if (max_averages <= 0) {
			printf("RTUTransceiver | ERROR: QPOTDRGetMaxAverages returned error\n");
			delete[] waves;
			return;
		}
		DWORD* avergs = new DWORD[MAX_AVERAGES];
		QPOTDRGetAvailAverages(otdr_card, waves[i], avergs);
		for (j = 0; j < max_averages; j++)
			printf("\tAverage: %u\n", avergs[j]);
		delete[] avergs;
	}

	delete[] waves;

	//Point spacing
	int max_point_spacings = QPOTDRGetMaxPointSpacings(otdr_card);
	if (max_point_spacings <= 0) {
		printf("RTUTransceiver | ERROR: QPOTDRGetMaxPointSpacings returned error\n");
		return;
	}
	float* point_spacings = new float[MAX_SPACINGS];
	QPOTDRGetAvailSpacings(otdr_card, point_spacings);
	//FIXME: Total number of values is 8, but QPOTDRGetMaxPointSpacings returns 7
	for (i = 0; i < MAX_SPACINGS; i++)
		printf("Point spacing: %f\n", point_spacings[i]);
	delete[] point_spacings;
}
