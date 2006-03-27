//////////////////////////////////////////////////////////////////////
// $Id: OTAUController.h,v 1.4 2006/03/27 08:53:19 arseniy Exp $
// 
// Syrus Systems.
// оБХЮОП-ФЕИОЙЮЕУЛЙК ГЕОФТ
// 2004-2005 ЗЗ.
// рТПЕЛФ: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.4 $, $Date: 2006/03/27 08:53:19 $
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


/*	Номер последовательного порта. Отсчитывается от единицы.*/
typedef unsigned short COMPortId;

/*	Адрес оптического переключателя. Отсчитывается от нуля.*/
typedef unsigned short OTAUAddress;

/*	Уникальный идентификатор оптического переключателя.
	Состоит из двух частей - номера последовательного порта и адреса переключателя.*/
typedef unsigned int OTAUUId;

/*	Номер порта на оптическом переключателе. Отсчитывается от единицы.*/
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
#define OTAU_MESSAGE_DISCONNECT_FORMAT ";DISC-TACC:01:%02hd:ABCD;"

#define OTAU_HOLD_PORT_TIMEOUT (unsigned int) 50 //sec

#define OTAU_REPLY_SIZE (unsigned int) 100


enum OTAUState {
	OTAU_STATE_READY,
	OTAU_STATE_RUNNING
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

		/*	Получить номер последовательного порта оптического переключателя*/
		COMPortId getCOMPortId() const;

		/*	Получить файловый описатель последовательного порта оптического переключателя*/
		HANDLE getCOMPortHandle() const;

		/*	Получить адрес оптического переключателя*/
		OTAUAddress getOTAUAddress()const;

		/*	Получить уникальный идентификатор оптического переключателя*/
		OTAUUId getOTAUUId() const;

		/*	Получить текущее состояние*/
		OTAUState getState() const;

		/*	Управление потоком*/
		BOOL start(const OTAUPortId otauPortId);
		void shutdown();
		pthread_t getThread() const;

		/*	Вычислить уникальный идентификатор оптического переключателя
		 * 	по его адресу и номеру его последовательного порта.*/
		static OTAUUId createOTAUUid(const COMPortId comPortId, const OTAUAddress otauAddress);

		/*	Попытаться создать новый смотритель оптического переключателя.
		 * 	Если переключатель обнаружен, возвращается ссылка на новый смотритель.
		 * 	Иначе - NULL.*/
		static OTAUController* createOTAUController(const COMPortId cpId,
			const HANDLE cpHandle,
			const OTAUAddress otauAddress,
			const unsigned int timewait);

	private:
		/*	Переключить на заданный порт*/
		BOOL switchOTAU() const;

		/*	Главный цикл*/
		static void* run(void* args);

		/*	Послать сообщение -- строку, оконченную нулём*/
		static BOOL sendCOMPortMessage(const HANDLE cpHandle,
			const char* message,
			char* reply,
			unsigned int replySize);

		/*	Принять ответ.*/
		static BOOL readCOMPortReply(const HANDLE cpHandle, char* reply, unsigned int replySize);

};

#endif // !defined(AFX_OTAUCONTROLLER_H__7EEB79A6_E914_4858_A43D_998DB8E48756__INCLUDED_)
