//////////////////////////////////////////////////////////////////////
// $Id: OTAUController.h,v 1.3 2005/08/29 18:15:44 arseniy Exp $
// 
// Syrus Systems.
// Научно-технический центр
// 2004-2005 гг.
// Проект: r7
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.3 $, $Date: 2005/08/29 18:15:44 $
// $Author: arseniy $
//
// OTAUController.h: interface for the OTAUController class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_OTAUCONTROLLER_H__7EEB79A6_E914_4858_A43D_998DB8E48756__INCLUDED_)
#define AFX_OTAUCONTROLLER_H__7EEB79A6_E914_4858_A43D_998DB8E48756__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <windows.h>
#include "pthread.h"
#include "ByteArray.h"


#define COM_PORT_READ_TIMEOUT (unsigned long) 10000
#define COM_PORT_REPLY_LENGTH (unsigned int) 4000
#define MAX_POSSIBLE_OTAUS (unsigned int) 100
#define MAX_POSSIBLE_OTAU_PORTS (unsigned int) 100
#define OTAU_COMMAND_INIT ";INIT-SYS::ALL:WXYZ1::0;"
#define OTAU_COMMAND_HDR ";RTRV-HDR:::ABCD;"
#define OTAU_COMMAND_DISCONNECT "DISC-TACC:1:1:1;"
#define OTAU_COMMAND_CONNECT1 "CONN-TACC-OTAU:OTAU"
#define OTAU_COMMAND_CONNECT2 ":ABCD:1;"
#define OTAU_COMMAND_CONNECT_LENGTH 50
#define OTAU_COMMAND_REANIMATE_CONNECTION ";REPT-STAT:::ABCD;"
#define OTAU_HOLD_PORT_TIMEOUT (unsigned int) 50 //sec

class OTAUController {
public:
	OTAUController(const unsigned int timewait, unsigned short com_port_number);
	virtual ~OTAUController();

	int start(ByteArray* local_address);
	void shutdown();

	pthread_t get_thread() const;

private:
	static void* run(void* args);
	void initialize_OTAUs();
	int set_COM_port_properties(const HANDLE com_port_handle);
	void send_switch_command(const HANDLE com_port_handle,
		const char* command,
		const unsigned int command_size,
		char*& reply,
		unsigned int& reply_size);
	unsigned int search_number_of_OTAUs(const char* string, unsigned int str_size);
	int parse_local_address(const char* local_address, unsigned int la_length);
	int is_local_address_valid() const;
	int switch_OTAU();

	unsigned short com_port_number;
	HANDLE* com_ports;
	unsigned int* otau_numbers;

	unsigned short com_port;
	unsigned short otau_id;
	unsigned short otau_port;

	pthread_t thread;
	unsigned int timewait;
	int running;
};

#endif // !defined(AFX_OTAUCONTROLLER_H__7EEB79A6_E914_4858_A43D_998DB8E48756__INCLUDED_)
