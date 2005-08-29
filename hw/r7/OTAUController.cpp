//////////////////////////////////////////////////////////////////////
// $Id: OTAUController.cpp,v 1.2 2005/08/29 18:07:10 arseniy Exp $
// 
// Syrus Systems.
// Ó¡’ﬁŒœ-‘≈»Œ…ﬁ≈”À…  √≈Œ‘“
// 2004-2005 ««.
// “œ≈À‘: r7
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.2 $, $Date: 2005/08/29 18:07:10 $
// $Author: arseniy $
//
// OTAUController.cpp: implementation of the OTAUController class.
//
//////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include <string.h>
#include "OTAUController.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

OTAUController::OTAUController(const unsigned int timewait, unsigned short com_port_number) {
	this->timewait = timewait;
	this->com_port_number = com_port_number;
//	this->last_used_com_port = NULL;
	this->initialize_OTAUs();
}

OTAUController::~OTAUController() {
//	if (this->last_used_com_port != NULL) {
//		this->disconnect_last_used_com_port();
//	}
	for (unsigned int i = 0; i < this->com_port_number; i++){
		CloseHandle(this->com_ports[i]);
	}
	delete[] this->otau_numbers;
	delete[] this->com_ports;
}

/**
* Terminates access for all OTAUs on all serial ports notified,
* and sets SIDs for them.
*/
void OTAUController::initialize_OTAUs() {
	printf ("OTAUController | Initializing OTAUs...\n");
	this->com_ports = new HANDLE[this->com_port_number];
	this->otau_numbers = new unsigned int[this->com_port_number];

	const int COM_PORT_ID_SIZE = 10;
	char* com_port_id = new char[COM_PORT_ID_SIZE];
	memset(com_port_id, 0, sizeof(char) * COM_PORT_ID_SIZE);
	int pos = sprintf (com_port_id, "%s", "COM");
	int tmp;
	for (int i = 0; i < this->com_port_number; i++) {
		tmp = pos + sprintf(com_port_id + pos, "%d:", i + 1);

		printf ("OTAUController | Creating handle for %s\n", com_port_id);
		this->com_ports[i] = CreateFile(com_port_id,
			GENERIC_READ | GENERIC_WRITE,
			0,
			NULL,
			OPEN_EXISTING,
			0,
			NULL);
		if (this->com_ports[i] == INVALID_HANDLE_VALUE) {
			printf ("OTAUController | ERROR: Can't create handle for port %s\n", com_port_id);
			continue;
		}

		printf ("OTAUController | Configuring port properties and timeouts for %s", com_port_id);
		printf ("\n");

		this->set_COM_port_properties(this->com_ports[i]);

		unsigned int reply_length;
		char* reply;

		reply_length = COM_PORT_REPLY_LENGTH;
		reply = new char[reply_length];
		memset(reply, 0, reply_length * sizeof(char));

		//Setting new SIDs to OTAUs
		printf("OTAUController | Sending INIT-SYS command to %s\n", com_port_id);
		this->send_switch_command(this->com_ports[i], OTAU_COMMAND_INIT, strlen(OTAU_COMMAND_INIT), reply, reply_length);
		if (reply_length != 0) {
			//Getting OTAUs' headers
			reply_length = COM_PORT_REPLY_LENGTH;
			memset (reply, 0, reply_length * sizeof(char));
			printf("OTAUController | Sending RTRV-HDR command to %s\n", com_port_id);
			this->send_switch_command(this->com_ports[i], OTAU_COMMAND_HDR, strlen(OTAU_COMMAND_HDR), reply, reply_length);

			this->otau_numbers[i] = this->search_number_of_OTAUs(reply, reply_length);
			printf ("OTAUController | Found %u OTAUs at %s\n", this->otau_numbers[i], com_port_id);
			if (this->otau_numbers[i] >= MAX_POSSIBLE_OTAUS) {
				printf("OTAUController | ERROR: Number of OTAUs on COM port %s exceeds maximum %d -- disabling COM port", com_port_id, MAX_POSSIBLE_OTAUS);
				this->otau_numbers[i] = 0;
			}
		}
		else {
			printf("OTAUController | No OTAUs found at COM port %s\n", com_port_id);
			this->otau_numbers[i] = 0;
		}

		delete[] reply;
	}
	delete[] com_port_id;
}

/**
* Configure COM port and set read timeout for it.
*/
int OTAUController::set_COM_port_properties (const HANDLE com_port_handle) {
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
		printf ("OTAUController | Unable to configure the serial port\n");
		return 0;
	}
	
	COMMTIMEOUTS port_time_outs;
	GetCommTimeouts(com_port_handle, &port_time_outs);
	port_time_outs.ReadTotalTimeoutConstant = COM_PORT_READ_TIMEOUT;
	if (! SetCommTimeouts(com_port_handle, &port_time_outs)) {
		printf ("OTAUController | Unable to set timeouts for the serial port\n");
		return 0;
	}

	return 1;
}

/**
* Sends command to OTAU by COM port and gets reply.
* replySize is used to send buffer size to function
* and to return the size of message received.
*/
void OTAUController::send_switch_command(const HANDLE com_port_handle,
										 const char* command,
										 const unsigned int command_size,
										 char*& reply,
										 unsigned int& reply_size) {
	printf("OTAUController | Sending command to OTAU\n");
	DWORD bytes_sent;
	WriteFile(com_port_handle, command, command_size, &bytes_sent, NULL);
	if (bytes_sent != command_size)	{
		printf ("OTAUController | ERROR: The number of bytes really sent to COM: %u not equal to command size: %u\n", bytes_sent, command_size);
		perror("WriteFile");
		return;
	}

	printf("OTAUController | Getting reply from OTAU\n");
	DWORD bytes_received;
	if (! ReadFile(com_port_handle, reply, reply_size,  &bytes_received, NULL)) {
		printf ("OTAUController | Can't read switch response from COM port!\n");
		return;
	}

	if (bytes_received == 0) {
		printf ("OTAUController | Nothing received from COM port - mei you switch!\n");
	}
	else {
		if (bytes_received > reply_size) {
			printf ("OTAUController | ERROR: Size of data, received from COM port, exceeds buffer size!\n");
		}
		else {
			printf ("OTAUController | Received data from COM port:\n\n%s\n",reply);
		}
	}

	reply_size = bytes_received;
}

/**
* Searches the count of different OTAU IDs in the string.
*/
unsigned int OTAUController::search_number_of_OTAUs(const char* string, unsigned int str_size) {
	const char* substring = "OTAU";
	unsigned int ss_size = strlen(substring);

	char* otau_ids_found = new char[MAX_POSSIBLE_OTAUS];
	memset(otau_ids_found, 0, MAX_POSSIBLE_OTAUS * sizeof(char));

	unsigned int i, j;
	for (i = 0; i < str_size; i++) {
		for (j = 0; j < ss_size; j++) {
			//Comparing the strings by byte
			if (string[i + j] != substring[j]) {
				break;
			}

			if (j == ss_size - 1) {
				//found the OTAU string
				char id_chars[3];
				memcpy(id_chars, string + (i + j + 1) * sizeof(char), 2);
				id_chars[2] = '\0';

				char* endptr;
				long id = strtol(id_chars, &endptr, 0);
				otau_ids_found[id] = 1;
			}
		}
	}

	unsigned int return_value = 0;
	for (i = 0; i < MAX_POSSIBLE_OTAUS; i++) {
		if (otau_ids_found[i]) {
			return_value ++;
		}
	}

	delete[] otau_ids_found;

	return return_value;
}



int OTAUController::start(ByteArray* local_address) {
	const char* la_data = local_address->getData();
	const int la_length = local_address->getLength();

	printf("OTAUController | Getting new fiber address parameters...\n");
	if (!this->parse_local_address(la_data, la_length)) {
		return 0;
	}

	if (!this->is_local_address_valid()) {
		return 0;
	}

//	this->check_last_used_com_port();

	if (!this->switch_OTAU()) {
		return 0;
	}

	//—ÓÁ‰‡Ú¸ Ë Á‡ÔÛÒÚËÚ¸ „Î‡‚Ì˚È ÔÓÚÓÍ.
	this->running = 1;
	pthread_attr_t pt_attr;
	pthread_attr_init(&pt_attr);
	pthread_attr_setdetachstate(&pt_attr, PTHREAD_CREATE_JOINABLE);
	pthread_create(&this->thread, &pt_attr, OTAUController::run, (void*)this);
	pthread_attr_destroy(&pt_attr);
	return 1;
}

void* OTAUController::run(void* args) {
	OTAUController* otauController = (OTAUController*) args;
	HANDLE com_port_handle = otauController->com_ports[otauController->com_port - 1];

	unsigned int reply_length = COM_PORT_REPLY_LENGTH;
	char* reply = new char[reply_length];
	memset(reply, 0, reply_length * sizeof(char));

	unsigned int send_reanimate_command_timeout = 0; //sec
	while (otauController->running) {
		if (send_reanimate_command_timeout >= OTAU_HOLD_PORT_TIMEOUT) {
			printf("OTAUController | Sending command to OTAU: %s\n", OTAU_COMMAND_REANIMATE_CONNECTION);
			otauController->send_switch_command(com_port_handle,
				OTAU_COMMAND_REANIMATE_CONNECTION,
				strlen(OTAU_COMMAND_REANIMATE_CONNECTION),
				reply,
				reply_length);
			send_reanimate_command_timeout = 0;
		}
		send_reanimate_command_timeout += otauController->timewait;
		Sleep(otauController->timewait * 1000);	//Convert to milliseconds
	}

	otauController->send_switch_command(com_port_handle,
		OTAU_COMMAND_DISCONNECT,
		strlen(OTAU_COMMAND_DISCONNECT),
		reply,
		reply_length);

	delete[] reply;
	return NULL;
}

void OTAUController::shutdown() {
	printf("OTAUController | Shutting down\n");
	this->running = 0;
}


int OTAUController::parse_local_address(const char* local_address, unsigned int la_length) {
	int separ_pos1 = -1;
	int separ_pos2 = -1;

	for (unsigned int i = 0; i < la_length; i++) {
		if ((local_address[i] == ':') || (local_address[i] == '-')) {
			if (separ_pos1 == -1) {
				separ_pos1 = i;
			}
			else {
				separ_pos2 = i;
				break;
			}
		}
	}

	if (separ_pos2 == -1) {
		printf("OTAUController | Wrong address format. Should be \"<Serial port ID>:<OTAU ID>:<OTAU port>\"\n");
		return 0;
	}

	char* com_port_chars = new char[separ_pos1 + 1];
	char* otau_id_chars = new char[separ_pos2 - separ_pos1];
	char* otau_port_chars = new char[la_length - separ_pos2];

	memcpy(com_port_chars, local_address, separ_pos1);
	com_port_chars[separ_pos1] = '\0';

	memcpy(otau_id_chars, local_address + separ_pos1 + 1, separ_pos2 - separ_pos1 - 1);
	otau_id_chars[separ_pos2 - separ_pos1 - 1] = '\0';

	memcpy(otau_port_chars, local_address + separ_pos2 + 1, la_length - separ_pos2 - 1);
	otau_port_chars[la_length - separ_pos2 - 1] = '\0';

	char* endptr;
	this->com_port = (short) strtol(com_port_chars, &endptr, 0);
	this->otau_id = (short) strtol(otau_id_chars, &endptr, 0);
	this->otau_port = (short) strtol(otau_port_chars, &endptr, 0);

	delete[] com_port_chars;
	delete[] otau_id_chars;
	delete[] otau_port_chars;

	return 1;
}

int OTAUController::is_local_address_valid() const {
	printf("COM port: %d, OTAU: %d, OTAUs on COM port: %d\n", this->com_port, this->otau_id, this->otau_numbers[this->com_port - 1]);
	if ((this->com_port < 1) || (this->com_port > this->com_port_number)) {
		printf ("OTAUController | %d -- Incorrect value of COM port! Must be in [%u; %u]\n", this->com_port, 1, this->com_port_number);
		return 0;
	}
	if (this->otau_numbers[this->com_port - 1] == 0) {
		printf ("OTAUController | No OTAUs on COM port %d\n", this->com_port);
		return 0;
	}
	else {
		if ((this->otau_id < 0) || (this->otau_id > this->otau_numbers[this->com_port - 1] - 1)) {
			printf ("OTAUController | %d -- Incorrect value of OTAU ID! Must be in [%u; %u] for COM%d\n", this->otau_id, 0, this->otau_numbers[this->com_port - 1] - 1, this->com_port);
			return 0;
		}
	}
	if (this->otau_port >= MAX_POSSIBLE_OTAU_PORTS) {
		printf ("OTAUController | OTAU port should be less than %d! Cannot switch OTAU\n", MAX_POSSIBLE_OTAU_PORTS);
		return 0;
	}
	return 1;
}

//void OTAUController::check_last_used_com_port() {
//	HANDLE com_port_handle = this->com_ports[this->com_port - 1];
//	if (this->last_used_com_port != NULL && this->last_used_com_port != com_port_handle) {
//		this->disconnect_last_used_com_port();
//	}
//	this->last_used_com_port = com_port_handle;
//}

//void OTAUController::disconnect_last_used_com_port() {
//	unsigned int reply_length = COM_PORT_REPLY_LENGTH;
//	char* reply = new char[reply_length];
//	memset(reply, 0, reply_length * sizeof(char));

	//Try to disconnect OTAU Test Access Path
//	this->send_switch_command(this->last_used_com_port,
//		OTAU_COMMAND_DISCONNECT,
//		strlen(OTAU_COMMAND_DISCONNECT),
//		reply,
//		reply_length);

//	delete[] reply;
//}

int OTAUController::switch_OTAU() {
	char* switch_command = new char[OTAU_COMMAND_CONNECT_LENGTH];
	memset(switch_command, 0, OTAU_COMMAND_CONNECT_LENGTH);

	int j = sprintf(switch_command, "%s", OTAU_COMMAND_CONNECT1);
	
	if (this->otau_id < 10) {
		j += sprintf(switch_command + j, "%s", "0");
		j += sprintf(switch_command + j,"%d", this->otau_id);
	}
	else {
		if ((10 < this->otau_id) && (this->otau_id < 100)) {
			j += sprintf (switch_command + j, "%d", this->otau_id);
		}
		else {
			printf ("OTAUController | OTAU SID should be less than 100! Cannot switch OTAU\n");
			delete[] switch_command;
			return 0;
		}
	}

	j += sprintf (switch_command + j, "%s", ":");
	j += sprintf (switch_command + j, "%d", this->otau_port);

	sprintf(switch_command + j, "%s", OTAU_COMMAND_CONNECT2);


	HANDLE com_port_handle = this->com_ports[this->com_port - 1];

	unsigned int reply_length = COM_PORT_REPLY_LENGTH;
	char* reply = new char[reply_length];
	memset(reply, 0, reply_length * sizeof(char));

	printf("OTAUController | Sending command to OTAU: %s\n", switch_command);
	this->send_switch_command(com_port_handle,
		switch_command,
		strlen(switch_command),
		reply,
		reply_length);

	delete[] reply;
	delete[] switch_command;
	return (reply_length > 0);
}


pthread_t OTAUController::get_thread() const {
	return this->thread;
}
