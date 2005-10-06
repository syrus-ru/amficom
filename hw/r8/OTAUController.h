//////////////////////////////////////////////////////////////////////
// $Id: OTAUController.h,v 1.1.1.1 2005/10/06 15:48:55 cvsadmin Exp $
// 
// Syrus Systems.
// ������-����������� �����
// 2004-2005 ��.
// ������: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.1.1.1 $, $Date: 2005/10/06 15:48:55 $
// $Author: cvsadmin $
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


/*	����� ����������������� �����. ������������� �� �������.*/
typedef unsigned short COMPortId;

/*	����� ����������� �������������. ������������� �� ����.*/
typedef unsigned short OTAUAddress;

/*	���������� ������������� ����������� �������������.
	������� �� ���� ������ - ������ ����������������� ����� � ������ �������������.*/
typedef unsigned int OTAUUId;

/*	����� ����� �� ���������� �������������. ������������� �� �������.*/
typedef unsigned short OTAUPortId;


#define COM_PORT_READ_TIMEOUT (unsigned long) 10000

#define MAX_POSSIBLE_OTAU_PORTS (unsigned int) 100

#define OTAU_MESSAGE_TERMINATE_CHARACTER ';'

#define OTAU_MESSAGE_SIZE (unsigned int) 40
//#define OTAU_MESSAGE_INIT_SIZE (unsigned int) 30
//#define OTAU_MESSAGE_CONNECT_SIZE (unsigned int) 35
//#define OTAU_MESSAGE_HOLD_CONNECTION_SIZE (unsigned int) 25
//#define OTAU_MESSAGE_DISCONNECT_SIZE (unsigned int) 25

#define OTAU_MESSAGE_INIT_FORMAT ";INIT-SYS:OTAU%02hd:ALL:ABCD::1;"
#define OTAU_MESSAGE_CONNECT_FORMAT ";CONN-TACC-OTAU:OTAU%02hd:%02hd:ABCD:%02hd;"
#define OTAU_MESSAGE_HOLD_CONNECTION_FORMAT ";REPT-STAT:OTAU%02hd::ABCD;"
#define OTAU_MESSAGE_DISCONNECT_FORMAT ";DISC-TACC:PRST:%02hd:ABCD;"

#define OTAU_HOLD_PORT_TIMEOUT (unsigned int) 50 //sec

#define OTAU_REPLY_SIZE (unsigned int) 100


enum OTAUState {
	OTAU_READY,
	OTAU_RUNNING
};

class OTAUController {
	private:
		COMPortId comPortId;
		HANDLE comPortHandle;
		OTAUAddress otauAddress;
		OTAUUId otauUId;

		OTAUPortId currentOTAUPort;

		OTAUState state;

		pthread_t thread;
		unsigned int timewait;
		int running;

	public:
		OTAUController(const COMPortId comPortId,
			const HANDLE comPortHandle,
			const OTAUAddress otauAddress,
			const unsigned int timewait);
		virtual ~OTAUController();

		/*	�������� ����� ����������������� ����� ����������� �������������*/
		COMPortId getCOMPortId() const;

		/*	�������� �������� ��������� ����������������� ����� ����������� �������������*/
		HANDLE getCOMPortHandle() const;

		/*	�������� ����� ����������� �������������*/
		OTAUAddress getOTAUAddress()const;

		/*	�������� ���������� ������������� ����������� �������������*/
		OTAUUId getOTAUUId() const;

		/*	�������� ������� ���������*/
		OTAUState getState() const;

		/*	���������� �������*/
		BOOL start(const OTAUPortId otauPortId);
		void shutdown();
		pthread_t getThread() const;

		/*	��������� ���������� ������������� ����������� �������������
			�� ��� ������ � ������ ��� ����������������� �����.*/
		static OTAUUId createOTAUUid(const COMPortId comPortId, const OTAUAddress otauAddress);

		/*	���������� ������� ����� ���������� ����������� �������������.
			���� ������������� ���������, ������������ ������ �� ����� ����������.
			����� - NULL.*/
		static OTAUController* createOTAUController(const COMPortId cpId,
			const HANDLE cpHandle,
			const OTAUAddress otauAddress,
			const unsigned int timewait);

	private:
		/*	����������� �� �������� ����*/
		BOOL switchOTAU() const;

		/*	������� ����*/
		static void* run(void* args);

		/*	������� ��������� -- ������, ���������� ����*/
		static BOOL sendCOMPortMessage(const HANDLE cpHandle,
			const char* message,
			char* reply,
			unsigned int replySize);

		/*	������� �����.*/
		static BOOL readCOMPortReply(const HANDLE cpHandle, char* reply, unsigned int replySize);

};

#endif // !defined(AFX_OTAUCONTROLLER_H__7EEB79A6_E914_4858_A43D_998DB8E48756__INCLUDED_)
