// RTUTransceiver.cpp: implementation of the RTUTransceiver class.
//
//////////////////////////////////////////////////////////////////////

#include "RTUTransceiver.h"
#include <list>


const char* RTUTransceiver::PARAMETER_NAME_WAVELENGTH = "ref_wvlen";
const char* RTUTransceiver::PARAMETER_NAME_TRACE_LENGTH = "ref_trclen";
const char* RTUTransceiver::PARAMETER_NAME_RESOLUTION = "ref_res";
const char* RTUTransceiver::PARAMETER_NAME_PULSE_WIDTH = "ref_pulswd";
const char* RTUTransceiver::PARAMETER_NAME_IOR = "ref_ior";
const char* RTUTransceiver::PARAMETER_NAME_SCANS = "ref_scans";
const char* RTUTransceiver::PARAMETER_NAME_FLAGS = "ref_flags";
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

	//Очереди запросов на измерение и результатов измерений, флаги синхронизации
	this->measurement_queue = measurement_queue;
	this->result_queue = result_queue;
	this->tmutex = tmutex;
	this->rmutex = rmutex;

	this->com_port_number = com_port_number;
	this->last_used_com_port = NULL;

	if (this->initialize_OTDR_cards()) {

//		print_available_parameters(this->otdr_cards[0]);

		this->initialize_OTAUs();

		this->state = RTU_STATE_INIT_COMPLETED;
	}
	else
		this->state = RTU_STATE_INIT_FAILED;
}

RTUTransceiver::~RTUTransceiver() {
	int i;

	if (this->state == RTU_STATE_ACUIRING_DATA) {
		for (i = 0; i < this->otdr_cards_number; i++) {
			printf ("RTUTransceiver | stoping measurement on OTDR card %u ...\n", this->otdr_cards[i]);
			QPOTDRAcqStop(this->otdr_cards[i]);
		}
	}

	delete[] this->otdr_cards;

	for (i = 0; i < this->com_port_number; i++)
		CloseHandle(this->com_ports[i]);
	delete[] this->otau_numbers;
	delete[] this->com_ports;
}

void RTUTransceiver::start() {
	//Создать и запустить главный поток.
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
	int need_switch_otau;
	ByteArray* previous_address = NULL;
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

			need_switch_otau = 0;
			if (previous_address == NULL || (*local_address) != (*previous_address))
				need_switch_otau = 1;

			if (need_switch_otau) {
				if (! rtuTransceiver->switch_OTAU(local_address->getData(), local_address->getLength())) {
					printf("RTUTransceiver | ERROR: Cannot switch OTAU. Measurement cancelled\n");
					delete measurement_segment;
					continue;
				}
				if (previous_address != NULL)
					delete previous_address;
				previous_address = local_address->clone();
			}

			rtuTransceiver->retrieve_plugin_data(otdr_card_index);

			parameters = measurement_segment->getParameters();
			par_number = measurement_segment->getParnumber();
			if (! rtuTransceiver->set_measurement_parameters(parameters, par_number, otdr_card_index)) {
				printf("RTUTransceiver | ERROR: Incorrect parameters used, measurement canceled!\n");
				delete measurement_segment;
				free(rtuTransceiver->plugin_data);
				continue;
			}

			rtuTransceiver->print_measurement_parameters();

			QPOTDRWaveformHeader* wave_form_header = (QPOTDRWaveformHeader*)malloc(sizeof(QPOTDRWaveformHeader));
			QPOTDRWaveformData* wave_form_data = new QPOTDRWaveformData[MAX_WFM_POINTS];//0xF000
			memset(wave_form_header, 0, sizeof(QPOTDRWaveformHeader));
			memset(wave_form_data, 0, sizeof (MAX_WFM_POINTS * sizeof(QPOTDRWaveformData)));

			printf ("RTUTransceiver | Starting measurements at RTU\n");
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
				printf("RTUTransceiver | Init OTDR card %u - succes\n", cards[i]);
				present_cards.push_back(cards[i]);
			}
			else
				printf("RTUTransceiver | Init OTDR card %u - failure; returned: %d\n", cards[i], init_ret);
		}
	}

	delete[] cards;

	this->otdr_cards_number = present_cards.size();
	if (this->otdr_cards_number == 0) {
		printf("RTUTransceiver | ERROR: None OTDR cards found\n");
			return 0;
	}
	printf("RTUTransceiver | Found %u OTDR cards\n", this->otdr_cards_number);
	this->otdr_cards = new WORD[this->otdr_cards_number];
	i = 0;
	for (list<WORD>::iterator it = present_cards.begin(); it != present_cards.end(); it++)
		this->otdr_cards[i++] = *it;

	return 1;
}


/**
* Terminates access for all OTAUs on all serial ports notified,
* and sets SIDs for them.
*/
void RTUTransceiver::initialize_OTAUs() {
	printf ("RTUTransceiver | Initializing OTAUs...\n");
	this->com_ports = new HANDLE[this->com_port_number];
	this->otau_numbers = new unsigned int[this->com_port_number];

	const int COM_PORT_ID_SIZE = 10;
	char* com_port_id = new char[COM_PORT_ID_SIZE];
	memset(com_port_id, 0, sizeof(char) * COM_PORT_ID_SIZE);
	int pos = sprintf (com_port_id, "%s", "COM");
	int tmp;
	for (int i = 0; i < this->com_port_number; i++) {
		tmp = pos + sprintf(com_port_id + pos, "%d:", i + 1);

		printf ("RTUTransceiver | Creating handle for %s\n", com_port_id);
		this->com_ports[i] = CreateFile(com_port_id,
										GENERIC_READ | GENERIC_WRITE,
										0,
										NULL,
										OPEN_EXISTING,
										0,
										NULL);
		if (this->com_ports[i] == INVALID_HANDLE_VALUE) {
			printf ("RTUTransceiver | ERROR: Can't create handle for port %s\n", com_port_id);
			continue;
		}

		printf ("RTUTransceiver | Configuring port properties and timeouts for %s", com_port_id);
		printf ("\n");

		this->set_COM_port_properties(this->com_ports[i]);

		unsigned int reply_length;
		char* reply;

		reply_length = COM_PORT_REPLY_LENGTH;
		reply = new char[reply_length];
		memset(reply, 0, reply_length * sizeof(char));

		//Setting new SIDs to OTAUs
		printf("RTUTransceiver | Sending INIT-SYS command to %s\n", com_port_id);
		this->send_switch_command(this->com_ports[i], OTAU_COMMAND_INIT, strlen(OTAU_COMMAND_INIT), reply, reply_length);
		if (reply_length != 0) {
			//Getting OTAUs' headers
			reply_length = COM_PORT_REPLY_LENGTH;
			memset (reply, 0, reply_length * sizeof(char));
			printf("RTUTransceiver | Sending RTRV-HDR command to %s\n", com_port_id);
			this->send_switch_command(this->com_ports[i], OTAU_COMMAND_HDR, strlen(OTAU_COMMAND_HDR), reply, reply_length);

			this->otau_numbers[i] = search_number_of_OTAUs(reply, reply_length);
			printf ("RTUTransceiver | Found %u OTAUs at %s\n", this->otau_numbers[i], com_port_id);
		}
		else {
			printf("RTUTransceiver | No OTAUs found at COM port %s\n", com_port_id);
			this->otau_numbers[i] = 0;
		}

		delete[] reply;
	}
	delete[] com_port_id;
}

/**
* Configure COM port and set read timeout for it.
*/
int RTUTransceiver::set_COM_port_properties (const HANDLE com_port_handle) {
	DCB dcb;

	dcb.DCBlength = sizeof(DCB); // size of DCB

	// Get default COM port settings
	GetCommState(com_port_handle, &dcb);

	// Change the settings
	dcb.BaudRate = CBR_9600;			// Current baud 
	dcb.fBinary = TRUE;					// Binary mode; no EOF check 
	dcb.fParity = FALSE;				// Enable parity checking ?
	dcb.fOutxCtsFlow = FALSE;			// No CTS output flow control 
	dcb.fOutxDsrFlow = FALSE;			// No DSR output flow control 
	dcb.fDtrControl = DTR_CONTROL_ENABLE;// DTR flow control type 
	dcb.fDsrSensitivity = TRUE;			// DSR sensitivity 
	dcb.fTXContinueOnXoff = FALSE;		// XOFF continues Tx 
	dcb.fOutX = FALSE;					// No XON/XOFF out flow control 
	dcb.fInX = FALSE;					// No XON/XOFF in flow control 
	dcb.fErrorChar = FALSE;				// Disable error replacement 
	dcb.fNull = FALSE;					// Disable null stripping 
	dcb.fRtsControl = RTS_CONTROL_ENABLE;// RTS flow control 
	dcb.fAbortOnError = FALSE;			// Do not abort reads/writes on error
	dcb.ByteSize = 8;					// Number of bits/byte, 4-8 
	dcb.Parity = NOPARITY;				// 0-4=no,odd,even,mark,space 
	dcb.StopBits = ONESTOPBIT;			// 0,1,2 = 1, 1.5, 2 

	// Apply new settings
	if (! SetCommState(com_port_handle, &dcb)) {
		printf ("RTUTransceiver | Unable to configure the serial port\n");
		return 0;
	}
	
	COMMTIMEOUTS port_time_outs;
	GetCommTimeouts(com_port_handle, &port_time_outs);
	port_time_outs.ReadTotalTimeoutConstant = COM_PORT_READ_TIMEOUT;
	if (! SetCommTimeouts(com_port_handle, &port_time_outs)) {
		printf ("RTUTransceiver | Unable to set timeouts for the serial port\n");
		return 0;
	}

	return 1;
}

/**
* Sends command to OTAU by COM port and gets reply.
* replySize is used to send buffer size to function
* and to return the size of message received.
*/
void RTUTransceiver::send_switch_command(const HANDLE com_port_handle,
										 const char* command,
										 const unsigned int command_size,
										 char*& reply,
										 unsigned int& reply_size) {
	printf("RTUTransceiver | Sending command to OTAU\n");
	DWORD bytes_sent;
	WriteFile(com_port_handle, command, command_size, &bytes_sent, NULL);
	if (bytes_sent != command_size)	{
		printf ("RTUTransceiver |ERROR: The number of bytes really sent to COM: %u not equal to command size: %u\n", bytes_sent, command_size);
		return;
	}

	printf("RTUTransceiver | Getting reply from OTAU\n");
	DWORD bytes_received;
	if (! ReadFile(com_port_handle, reply, reply_size,  &bytes_received, NULL)) {
		printf ("RTUTransceiver | Can't read switch response from COM port!\n");
		return;
	}

	if (bytes_received == 0)
		printf ("RTUTransceiver | Nothing received from COM port - mei you switch!\n");
	else
		if (bytes_received > reply_size)
			printf ("RTUTransceiver | ERROR: Size of data, received from COM port, exceeds buffer size!\n");
		else
			printf ("RTUTransceiver | Received data from COM port:\n\n%s",reply,"\n\n");

	reply_size = bytes_received;
}

/**
* Searches the count of different OTAU IDs in the string.
*/
unsigned int RTUTransceiver::search_number_of_OTAUs(char* string, int str_size) {
	const char* substring = "OTAU";
	unsigned int ss_size = strlen(substring);

	short* otau_ids_found = new short[MAX_POSSIBLE_OTAUS];
	memset(otau_ids_found, 0, MAX_POSSIBLE_OTAUS * sizeof(short));

	unsigned int i, j;
	for (i = 0; i < str_size; i++) {
		for (j = 0; j < ss_size; j++) {
			//Comparing the strings by byte
			if (string[i + j] != substring[j])
				break;

			if (j == ss_size - 1) {
				//found the OTAU string
				char id_chars[3];
				memcpy(id_chars, string + (i + j + 1) * sizeof(char), 2);
				id_chars[2] = '\0';

				char* endptr;
				short id = strtol(id_chars, &endptr, 0);
				otau_ids_found[id] = 1;
			}
		}
	}

	unsigned int return_value = 0;
	for (i = 0; i < MAX_POSSIBLE_OTAUS; i++)
		if (otau_ids_found[i] == 1)
			return_value ++;

	delete[] otau_ids_found;

	return return_value;
}

int RTUTransceiver::switch_OTAU(char* local_address, int la_length) {
	printf("RTUTransceiver | Switching OTAU...\n");

	//Switching fiber	
	int com_port;
	int otau_id;
	int otau_port;

	printf("RTUTransceiver | Getting new fiber address parameters...\n");
	if (! parse_local_address(local_address, la_length, com_port,otau_id, otau_port))
		return 0;

	if ((com_port <= 0) || (com_port > this->com_port_number)) {
		printf ("RTUTransceiver | Incorrect value of COM port!\n");
		return 0;
	}
	if ((otau_id < 0) || (otau_id > this->otau_numbers[com_port - 1])) {
		printf ("RTUTransceiver | Incorrect value of OTAU ID!\n");
		return 0;
	}

	if (this->last_used_com_port != NULL) {
		unsigned int reply_length = COM_PORT_REPLY_LENGTH;
		char* reply = new char[reply_length];
		memset(reply, 0, reply_length * sizeof(char));

		//Trying to disconnect OTAU Test Access Path
		this->send_switch_command(this->last_used_com_port, OTAU_COMMAND_DISCONNECT, strlen(OTAU_COMMAND_DISCONNECT), reply, reply_length);

		delete[] reply;
	}

	char* mesgcomm = new char[OTAU_COMMAND_CONNECT_LENGTH];
	memset(mesgcomm, 0, OTAU_COMMAND_CONNECT_LENGTH);

	int j = sprintf (mesgcomm, "%s", OTAU_COMMAND_CONNECT1);
	
	if (otau_id < 10) {
		j += sprintf(mesgcomm + j, "%s", "0");
		j += sprintf(mesgcomm + j,"%d", otau_id);
	}
	else
		if ((10 < otau_id) && (otau_id < 100)) {
			j += sprintf (mesgcomm + j, "%d", otau_id);
	}
	else {
		printf ("RTUTransceiver | OTAU SID should be less than 100! Cannot switch OTAU\n");
		delete[] mesgcomm;
		return 0;
	}

	j += sprintf (mesgcomm + j, "%s", ":");

	if (otau_port < 100) {
		j += sprintf (mesgcomm + j, "%d", otau_port);
	}
	else {
		printf ("RTUTransceiver | OTAU port should be less than 100! Cannot switch OTAU\n");
		delete[] mesgcomm;
		return 0;
	}

	sprintf(mesgcomm + j, "%s", OTAU_COMMAND_CONNECT2);

	unsigned int msg_length = strlen(mesgcomm);

	HANDLE sp = this->com_ports[com_port - 1];

	unsigned int reply_length = COM_PORT_REPLY_LENGTH;
	char* reply = new char[reply_length];
	memset(reply, 0, reply_length * sizeof(char));

	printf("RTUTransceiver | Sending command to OTAU: %s\n", mesgcomm);
	this->send_switch_command(sp, mesgcomm, msg_length, reply, reply_length);
	this->last_used_com_port = sp;

	delete[] reply;
	delete[] mesgcomm;

	return 1;
}

int RTUTransceiver::parse_local_address(char* local_address,
										unsigned int la_length,
										int& com_port,
										int& otau_id,
										int& otau_port) {
	int separ1_posit = -1;
	int separ2_posit = -1;

	for (unsigned int i = 0; i < la_length; i++) {
		if ((local_address[i] == ':') || (local_address[i] == '-')) {
			if (separ1_posit == -1)
				separ1_posit = i;
			else {
				separ2_posit = i;
				break;
			}
		}
	}

	if (separ2_posit == -1) {
		printf("Wrong address format. Should be \"<Serial port ID>:<OTAU ID>:<OTAU port>\"\n");
		return 0;
	}

	char* com_port_chars = new char[separ1_posit + 1];
	char* otau_id_chars = new char[separ2_posit - separ1_posit];
	char* otau_port_chars = new char[la_length - separ2_posit];

	memcpy(com_port_chars, local_address, separ1_posit);
	com_port_chars[separ1_posit] = '\0';

	memcpy(otau_id_chars, local_address + separ1_posit + 1, separ2_posit - separ1_posit - 1);
	otau_id_chars[separ2_posit - separ1_posit - 1] = '\0';

	memcpy(otau_port_chars, local_address + separ2_posit + 1, la_length - separ2_posit - 1);
	otau_port_chars[la_length - separ2_posit - 1] = '\0';

	char* endptr;
	com_port = strtol(com_port_chars, &endptr, 0);
	otau_id = strtol(otau_id_chars, &endptr, 0);
	otau_port = strtol(otau_port_chars, &endptr, 0);

	delete[] com_port_chars;
	delete[] otau_id_chars;
	delete[] otau_port_chars;

	return 1;
}

int RTUTransceiver::set_measurement_parameters(Parameter** parameters, unsigned int par_number, unsigned int otdr_card_index) {
	int wvlen = -1;
	double trclen = -1;
	double res = -1;
	int pulswd = -1;
	double ior = -1;
	double scans = -1;
	int flags = -1;

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
		else
			if (strcmp(par_name, PARAMETER_NAME_TRACE_LENGTH) == 0) {
				bvalue = parameters[i]->getValue()->getReversed();
				trclen = *(double*)(bvalue->getData()) / 1000.;
				delete bvalue;
			}
			else
				if (strcmp(par_name, PARAMETER_NAME_RESOLUTION) == 0) {
					bvalue = parameters[i]->getValue()->getReversed();
					res = *(double*)(bvalue->getData());
					delete bvalue;
				}
				else
					if (strcmp(par_name, PARAMETER_NAME_PULSE_WIDTH) == 0) {
						bvalue = parameters[i]->getValue()->getReversed();
						pulswd = *(int*)(bvalue->getData());
						delete bvalue;
					}
					else
						if (strcmp(par_name, PARAMETER_NAME_IOR) == 0) {
							bvalue = parameters[i]->getValue()->getReversed();
							ior = *(double*)(bvalue->getData());
							delete bvalue;
						}
						else
							if (strcmp(par_name, PARAMETER_NAME_SCANS) == 0) {
								bvalue = parameters[i]->getValue()->getReversed();
								scans = *(double*)(bvalue->getData());
								delete bvalue;
							}
							else
								if (strcmp(par_name, PARAMETER_NAME_FLAGS) == 0) {
									bvalue = parameters[i]->getValue()->getReversed();
									flags = *(int*)(bvalue->getData());
									delete bvalue;
								}
								else
									printf("RTUTransceiver | Unknown name of parameter: %s\n", par_name);
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

	int pulse_width_index = get_pulse_width_index(pulswd, this->otdr_cards[otdr_card_index], this->wave_length);
	if (point_spacing_index < 0) {
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
	if (flags & 0x00000001)
		this->filter_flags |= REAL_TIME_SCAN;
	if (flags & 0x00000002)
		this->filter_flags |= LIVE_FIBER_DETECT;
	if (flags & 0x00000004)
		this->filter_flags |= GAIN_SPLICE_ON;

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
		ret = get_index_in_array((float)res, point_spacings, max_point_spacings);
		delete[] point_spacings;
	}
	else {
		printf("RTUTransceiver | ERROR: QPOTDRGetMaxPointSpacings returned error\n");
		ret = -1;
	}
	return ret;
}

int RTUTransceiver::get_pulse_width_index(const int pulswd, const WORD otdr_card, const float wave) {
	int ret;
	int max_pulse_widths = QPOTDRGetMaxPulses(otdr_card, wave);
	if (max_pulse_widths > 0) {
		DWORD* pulse_widths = new DWORD[MAX_PULSES];
		QPOTDRGetAvailPulses(otdr_card, wave, pulse_widths);
		ret = get_index_in_array((DWORD)pulswd, pulse_widths, max_pulse_widths);
		delete[] pulse_widths;
	}
	else {
		printf("RTUTransceiver | ERROR: QPOTDRGetMaxPulses returned error\n");
		ret = -1;
	}
	return ret;
}

int RTUTransceiver::ior_is_valid(const double ior, const WORD otdr_card, const float wave) {
	float default_ior = QPOTDRGetDefaultIOR(otdr_card, wave);
	printf("RTUTransceiver | Default IOR == %f\n", default_ior);
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
	unsigned long dts = (unsigned long)(time->QuadPart - time1970->QuadPart)/10000000;

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
	for (i = 0; i < np; i++)
		dsf[0][i] = 65535 - wave_form_data[i + offset];
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
		printf("Wave length: %f\n", waves[i]);
		int j;

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

		int max_pulse_widths = QPOTDRGetMaxPulses(otdr_card, waves[i]);
		if (max_pulse_widths <= 0) {
			printf("RTUTransceiver | ERROR: QPOTDRGetMaxPulses returned error\n");
			delete[] waves;
			return;
		}
		DWORD* pulse_widths = new DWORD[MAX_PULSES];
		QPOTDRGetAvailPulses(otdr_card, waves[i], pulse_widths);
		for (j = 0; j < max_pulse_widths; j++)
			printf("\tPulse width: %u\n", pulse_widths[j]);
		delete[] pulse_widths;

		float ior = QPOTDRGetDefaultIOR(otdr_card, waves[i]);
		printf("\t Index of refraction: %f\n", ior);

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

	int max_point_spacings = QPOTDRGetMaxPointSpacings(otdr_card);
	if (max_point_spacings <= 0) {
		printf("RTUTransceiver | ERROR: QPOTDRGetMaxPointSpacings returned error\n");
		return;
	}
	float* point_spacings = new float[MAX_SPACINGS];
	QPOTDRGetAvailSpacings(otdr_card, point_spacings);
	for (i = 0; i < max_point_spacings; i++)
		printf("Point spacing: %f\n", point_spacings[i]);
	delete[] point_spacings;
}
