#include "stdafx.h"
#include <process.h>
#include <assert.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

#include "Switcher.h"

// VERBOSITY LEVELS:
// 1: errors only
// 2: errors + communication
// 3: errors + communication + communication debug
#define VERBOSITY 2

#define COM_PORT_READ_TIMEOUT (unsigned long)10000
#define COM_PORT_REPLY_LENGTH (unsigned int)4000
#define MAX_POSSIBLE_OTAUS (unsigned int)100
#define OTAU_COMMAND_INIT_HARD ";INIT-SYS::ALL:A001::0;"
#define OTAU_COMMAND_INIT_SOFT ";INIT-SYS::ALL:A001::1;"
#define OTAU_COMMAND_HDR ";RTRV-HDR:::A002;"
#define OTAU_COMMAND_REANIMATE_CONNECTION ";REPT-STAT:::A003;"
#define OTAU_COMMAND_DISCONNECT "DISC-TACC:1:"
#define OTAU_COMMAND_CONNECT1 "CONN-TACC-OTAU:OTAU"
#define OTAU_COMMAND_BUFFER_SIZE 128

#define OTAU_CTAG "ABCD"
#define OTAU_TAP_OFFSET 100

#define OTAU_REANIMATE_RESPONSE_WAIT_TIMEOUT 50
//#define OTAU_REANIMATE_BETWEEN_CALLS_TIMEOUT 20000
#define OTAU_REANIMATE_BETWEEN_CALLS_TIMEOUT 2000


Switcher::Switcher(int comPort, bool hardInit) {
	initialize_OTAU(comPort, hardInit);
	if (otau_number > 0)
		otau_conn = new bool[otau_number];
	else
		otau_conn = 0;
}

Switcher::~Switcher() {
	CloseHandle(comHandle);
	if (otau_conn)
		delete[] otau_conn;
}

int Switcher::initialize_OTAU(int comPort, bool hardInit) {
//	printf ("Switcher | Initializing OTAU...\n");

	{
		char com_port_id[10];
		sprintf(com_port_id, "%s%d:", "COM", comPort);

//		printf ("Switcher | Creating handle for %s\n", com_port_id);
		this->comHandle = CreateFile(com_port_id,
			GENERIC_READ | GENERIC_WRITE,
			0,
			NULL,
			OPEN_EXISTING,
			0,
			NULL);
		if (this->comHandle == INVALID_HANDLE_VALUE) {
			printf ("Switcher | ERROR: Can't create handle for port %s\n", com_port_id);
			return 1;
		}

//		printf ("Switcher | Configuring port properties and timeouts for %s\n", com_port_id);

		this->set_COM_port_properties(this->comHandle);

		char* reply;

		reply = new char[COM_PORT_REPLY_LENGTH];

//		printf("Switcher | Sending INIT-SYS command to %s\n", com_port_id);
		DWORD bytes = this->send_switch_command(this->comHandle,
			hardInit ? OTAU_COMMAND_INIT_HARD : OTAU_COMMAND_INIT_SOFT,
			reply,
			COM_PORT_REPLY_LENGTH);
		if (bytes != 0) {
			if (hardInit) {
				// in hard mode, wait for one more reply
				do {
					bytes = this->send_switch_command(this->comHandle, "", reply, COM_PORT_REPLY_LENGTH);
				} while (bytes == 0);
			}
			//Getting OTAUs' headers
//			printf("Switcher | Sending RTRV-HDR command to %s\n", com_port_id);
			bytes = this->send_switch_command(this->comHandle, OTAU_COMMAND_HDR, reply, COM_PORT_REPLY_LENGTH);

			this->otau_number = this->search_number_of_OTAUs(reply, bytes);
//			printf ("Switcher | Found %u OTAUs at %s\n", this->otau_numbers[i], com_port_id);
		}
		else {
			printf("Switcher | No OTAUs found at COM port %s\n", com_port_id);
			this->otau_number = 0;
		}

		delete[] reply;
	}
	return 0;
}

/**
* Configure COM port and set read timeout for it.
*/
int Switcher::set_COM_port_properties (const HANDLE com_port_handle) {
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
		printf ("Switcher | Unable to configure the serial port\n");
		return 0;
	}
	
	COMMTIMEOUTS port_time_outs;
	GetCommTimeouts(com_port_handle, &port_time_outs);
	port_time_outs.ReadTotalTimeoutConstant = COM_PORT_READ_TIMEOUT;
	if (! SetCommTimeouts(com_port_handle, &port_time_outs)) {
		printf ("Switcher | Unable to set timeouts for the serial port\n");
		return 0;
	}

	return 1;
}

// reads input, stops when gets ';', timeout, or read error
int readOtauMessage(const HANDLE com_port_handle,
	 char* reply,
	 unsigned int reply_size)
{
#if VERBOSITY > 2
	printf("readOtauMessage: in\n");
#endif
	unsigned int ofs;
	for (ofs = 0; ofs < reply_size; ofs++) {
		DWORD bytes_t = 0;
		if (! ReadFile(com_port_handle, reply + ofs, 1, &bytes_t, NULL))
			break;
		if (bytes_t != 1)
			break;
		if (reply[ofs] == ';')
			break;
	}
#if VERBOSITY > 2
	printf("readOtauMessage: out: %u\n", ofs);
#endif
	return ofs;
}

/**
* Sends command to OTAU by COM port and gets reply.
* replySize is used to send buffer size to function
* and to return the size of message received.
*/
DWORD Switcher::send_switch_command(const HANDLE com_port_handle,
		 const char* command,
		 char* reply,
		 unsigned int reply_size) {
	unsigned int command_size = strlen(command);
//	printf("Switcher | Sending command to OTAU\n");
#if VERBOSITY > 1
	printf("< %d: '%s'\n", command_size, command);
#endif
	DWORD bytes_sent;
	WriteFile(com_port_handle, command, command_size, &bytes_sent, NULL);
	if (bytes_sent != command_size)	{
		printf ("Switcher |ERROR: The number of bytes really sent to COM: %u not equal to command size: %u\n", bytes_sent, command_size);
		return 0;
	}

//	printf("Switcher | Getting reply from OTAU\n");
//	DWORD bytes_received;
//	if (! ReadFile(com_port_handle, reply, reply_size,  &bytes_received, NULL)) {
//		printf ("Switcher | Can't read switch response from COM port!\n");
//		return;
//	}
	DWORD bytes_received;
	bytes_received = readOtauMessage(com_port_handle, reply, reply_size);

#if VERBOSITY > 1
	printf("> %d\n", bytes_received);
#endif
	//printf("> %d: '", bytes_received);
	//fwrite(reply, bytes_received, 1, stdout);
	//printf(" '\n");

	if (bytes_received > reply_size)
		printf ("Switcher | ERROR: Size of data, received from COM port, exceeds buffer size!\n");

	return bytes_received;
}

/**
* Searches the count of different OTAU IDs in the string.
*/
int Switcher::search_number_of_OTAUs(char* string, int str_size) {
	const char* substring = "OTAU";
	int ss_size = strlen(substring);

	short* otau_ids_found = new short[MAX_POSSIBLE_OTAUS];
	memset(otau_ids_found, 0, MAX_POSSIBLE_OTAUS * sizeof(short));

	int i, j;
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

				int id = atoi(id_chars);
				if (id >= 0 && id < MAX_POSSIBLE_OTAUS)
					otau_ids_found[id] = 1;
			}
		}
	}

	int return_value = 0;
	for (i = 0; i < MAX_POSSIBLE_OTAUS; i++)
		if (otau_ids_found[i] == 1)
			return_value ++;

	delete[] otau_ids_found;

	return return_value;
}

int Switcher::switch_OTAU(int otau_id, int otau_port) {
//	printf("Switcher | Switching OTAU...\n");

	char* mesgcomm = new char[OTAU_COMMAND_BUFFER_SIZE];

	if(this->otau_conn[otau_id]) {
		// connected, need to disconnect
		char* reply = new char[COM_PORT_REPLY_LENGTH];

		sprintf(mesgcomm, "%s%d:%s;",
			OTAU_COMMAND_DISCONNECT,
			OTAU_TAP_OFFSET + otau_id,
			OTAU_CTAG);

		this->send_switch_command(this->comHandle, mesgcomm, reply, COM_PORT_REPLY_LENGTH);

		// ignore reply

		delete[] reply;
	}

	this->otau_conn[otau_id] = 0;

	if (otau_id >= 100) {
		printf ("Switcher | OTAU SID should be less than 100! Cannot switch OTAU\n");
		return 0;
	}
	if (otau_port >= 100) {
		printf ("Switcher | OTAU port should be less than 100! Cannot switch OTAU\n");
		return 0;
	}

	if (otau_port < 0) {
		return 1; // leave unconnected
	}

	sprintf(mesgcomm, "%s%.2d:%d:%s:%d;",
		OTAU_COMMAND_CONNECT1,
		otau_id,
		otau_port,
		OTAU_CTAG,
		OTAU_TAP_OFFSET + otau_id);

	char* reply = new char[COM_PORT_REPLY_LENGTH];
//	printf("Switcher | Sending command to OTAU: %s\n", mesgcomm);
	DWORD bytes = this->send_switch_command(comHandle, mesgcomm, reply, COM_PORT_REPLY_LENGTH);
	//this->last_used_com_port = sp;

	this->otau_conn[otau_id] = 1;

	delete[] reply;
	delete[] mesgcomm;

	return bytes > 0;
}

int Switcher::repeatStatus() {
	unsigned int reply_length = COM_PORT_REPLY_LENGTH;
	char* reply = new char[reply_length];
	memset(reply, 0, reply_length * sizeof(char));

//	printf("Switcher | Sending REPT-STAT command.\n");
	this->send_switch_command(
		this->comHandle,
		OTAU_COMMAND_REANIMATE_CONNECTION,
		reply,
		reply_length);

	delete[] reply;
	return reply_length > 0;
}

int Switcher::getNumberOfOtaus() {
	return otau_number;
}