//////////////////////////////////////////////////////////////////////
// $Id: OTDRController.h,v 1.1 2005/10/06 15:48:55 cvsadmin Exp $
// 
// Syrus Systems.
// оБХЮОП-ФЕИОЙЮЕУЛЙК ГЕОФТ
// 2004-2005 ЗЗ.
// рТПЕЛФ: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.1 $, $Date: 2005/10/06 15:48:55 $
// $Author: cvsadmin $
//
// OTDRController.h: interface for the OTDRController class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_OTDRCONTROLLER_H__FF003D9F_71B8_413B_A7ED_FDBFCE325E74__INCLUDED_)
#define AFX_OTDRCONTROLLER_H__FF003D9F_71B8_413B_A7ED_FDBFCE325E74__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "pthread.h"
#include "OTDRReportListener.h"


typedef unsigned short OTDRId;

enum OTDRState {
	OTDR_STATE_INITIALIZED,
	OTDR_STATE_INIT_FAILED,
	OTDR_STATE_READY,
	OTDR_STATE_ACUIRING_DATA
};

enum OTDRModel {
	OTDR_MODEL_PK7600,
	OTDR_MODEL_ONT_UT
};

class OTDRController {
	protected:
		/*	Номер платы рефлектометра */
		OTDRId otdrId;

	private:
		/*	Ссылка на главный объект приложения*/
		const OTDRReportListener* otdrReportListener;

		/*	Текущее состояние средства управления рефлектометром */
		OTDRState state;

		/*	Вспомогательные величины для управления потоком */
		pthread_t thread;
		unsigned int timewait;
		int running;

	public:
		OTDRController(const OTDRId otdrId,
			const OTDRReportListener* otdrReportListener,
			const unsigned int timewait);
		virtual ~OTDRController();

		/*	Получить уникальный номер платы рефлектометра*/
		OTDRId getOTDRId() const;

		/*	Получить модель рефлектометра.
			Реализована в подклассах*/
		virtual OTDRModel getOTDRModel() const = 0;

		/*	Распечатать допустимые параметры измерений.
			Реализована в подклассах*/
		virtual void printAvailableParameters() const = 0;

		/*	Получить текущее состояние*/
		OTDRState getState() const;

		/*	Управление потоком*/
		void start();
		void shutdown();
		pthread_t getThread() const;

	private:
		/*	Главный цикл потока*/
		static void* run(void* args);

};

#endif // !defined(AFX_OTDRCONTROLLER_H__FF003D9F_71B8_413B_A7ED_FDBFCE325E74__INCLUDED_)
