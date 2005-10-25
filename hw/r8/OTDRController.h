//////////////////////////////////////////////////////////////////////
// $Id: OTDRController.h,v 1.7 2005/10/25 14:18:04 arseniy Exp $
// 
// Syrus Systems.
// оБХЮОП-ФЕИОЙЮЕУЛЙК ГЕОФТ
// 2004-2005 ЗЗ.
// рТПЕЛФ: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.7 $, $Date: 2005/10/25 14:18:04 $
// $Author: arseniy $
//
// OTDRController.h: interface for the OTDRController class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_OTDRCONTROLLER_H__FF003D9F_71B8_413B_A7ED_FDBFCE325E74__INCLUDED_)
#define AFX_OTDRCONTROLLER_H__FF003D9F_71B8_413B_A7ED_FDBFCE325E74__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <windows.h>
#include <map>
#include "pthread.h"
#include "OTDRReportListener.h"
#include "Parameter.h"


typedef unsigned short OTDRId;
using namespace std;
typedef map<const char*, const ByteArray*> ParametersMapT;

#define SIZE_MANUFACTORER_NAME 20
#define SIZE_MODEL_NAME 16
#define SIZE_SERIAL_NUMBER 32
#define SIZE_MODEL_NUMBER 32
#define SIZE_PART_NUMBER 16

typedef struct {
	char manufacturerName[SIZE_MANUFACTORER_NAME];	//Производитель
	char modelName[SIZE_MODEL_NAME];				//Название модели
	char serialNumber[SIZE_SERIAL_NUMBER];			//Серийный номер
	char modelNumber[SIZE_MODEL_NUMBER];			//Номер модели
	char partNumber[SIZE_PART_NUMBER];				//Номер партии
	unsigned short revisionId;						//Версия ПО
} OTDRPluginInfo;

enum OTDRState {
	OTDR_STATE_NEW,
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

		/*	Указатель на структуру, содержащую сведения о плате рефлектометра.*/
		OTDRPluginInfo* otdrPluginInfo;

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

		/*	Провести дополнительные действия с объектом, прежде чем он станет готов к работе.
		 * 	Вызов этой функции возможен только для объекта в состоянии OTDR_STATE_NEW.
		 * 	После вызова состояние должно быть OTDR_STATE_READY.*/
		void init();

		/*	Получить уникальный номер платы рефлектометра*/
		OTDRId getOTDRId() const;

		/*	Получить модель рефлектометра.
		 * 	Реализована в подклассах*/
		virtual OTDRModel getOTDRModel() const = 0;

		/*	Установить параметры измерения.
		 * 	В случае неправильных значений возвращает FALSE.*/
		virtual BOOL setMeasurementParameters(const Parameter** parameters, const unsigned int parNumber) = 0;

		/*	Получить текущее состояние*/
		OTDRState getState() const;

		/*	Управление потоком*/
		void start();
		void shutdown();
		pthread_t getThread() const;

	private:
		/*	Достать сведения о плате рефлектометра.
		 * 	Реализована в подклассах.*/
		virtual void retrieveOTDRPluginInfo() = 0;

		/*	Распечатать допустимые параметры измерений.
		 * 	Реализована в подклассах*/
		virtual void printAvailableParameters() const = 0;

		/*	Главный цикл потока*/
		static void* run(void* args);

};

#endif // !defined(AFX_OTDRCONTROLLER_H__FF003D9F_71B8_413B_A7ED_FDBFCE325E74__INCLUDED_)
