// RTU.h: interface for the RTU class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_RTU_H__1A8AC991_4139_45A7_8FD1_D14AAFB1D55E__INCLUDED_)
#define AFX_RTU_H__1A8AC991_4139_45A7_8FD1_D14AAFB1D55E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <deque>
#include <map>
#include "pthread.h"
#include "MeasurementSegment.h"
#include "ResultSegment.h"
#include "OTDRContainer.h"
#include "SegmentProcessor.h"
#include "OTDRReportListener.h"
#include "OTAUController.h"


#define LOCAL_ADDRESS_FORMAT "%hd:%hd:%hd:%hd"

using namespace std;
typedef deque<const MeasurementSegment*> MeasurementQueueT;
typedef deque<const ResultSegment*> ResultQueueT;
typedef map<const OTDRId, const OTDRController*> OTDRControllerMapT;
typedef map<const COMPortId, HANDLE> COMPortHandleMapT;
typedef map<const OTAUUId, const OTAUController*> OTAUControllerMapT;


enum RTUState {
	RTU_STATE_OTDR_INIT_FAILED,
	RTU_STATE_OTDR_INITIALIZED,
	RTU_STATE_COM_PORT_INIT_FAILED,
	RTU_STATE_COM_PORT_INITIALIZED,
	RTU_STATE_OTAU_DISCOVERED,
	RTU_STATE_RUNNING
};

class RTU : public OTDRContainer, public SegmentProcessor, public OTDRReportListener {
	private:
		/*	Таблица смотрителей рефлектометров
		 * 	Ключ - уникальный идентификатор, величина - указатель на смотритель*/
		OTDRControllerMapT otdrControllersMap;

		/*	Количество имеющихся последовательных портов*/
		unsigned short comPortNumber;

		/*	Таблица открытых файловых указателей.
		 * 	Ключ - номер последовательного порта, величина - соответствующий указатель*/
		COMPortHandleMapT comPortHandlesMap;

		/*	Таблица смотрящих за оптическими преключателями.
		 * 	Ключ - номер последовательного порта, величина - набор смотрящих на этом порту*/
		OTAUControllerMapT otauControllersMap;

		/* Очереди запросов на измерение и результатов */
		MeasurementQueueT measurementQueue;
		ResultQueueT resultQueue;

		/* Текущее состояние */
		RTUState state;

		/* Флаги синхронизации */
		pthread_mutex_t* tMutex;
		pthread_mutex_t* rMutex;

		/*	Вспомогательные величины для управления потоком */
		pthread_t thread;
		unsigned int timewait;
		int running;

	public:
		RTU(const unsigned short comPortNumber, const unsigned int timewait);
		virtual ~RTU();

		/*	Зарегистрировать новый рефлектометр.
		 * 	Реализация интерфейса OTDRContainer.*/
		void registerOTDRController(const OTDRController* otdrController);

		/*	Добавить в рассмотрение.
		 * 	Реализация интерфейса SegmentProcessor.*/
		void addTaskSegment(const Segment* segment);

		/*	Вынуть из очереди.
		 * 	Реализация интерфейса SegmentProcessor.*/
		const Segment* removeReportSegment();

		/*	Заново добавить в очередь удалённный прежде.
		 * 	Реализация интерфейса SegmentProcessor.*/
		void reAddReportSegment(const Segment* segment);

		/*	Принять данные с рефлектометра.
		 * 	Реализация интерфейса OTDRReportListener.*/
		void acceptOTDRReport(const Segment* segment);

		/*	Получить текущее состояние*/
		RTUState getRTUState() const;

		/*	Получить период цикла*/
		unsigned int getTimewait() const;

		/*	Управление потоком*/
		void start();
		void shutdown();
		pthread_t getThread() const;

	private:
		/*	Главный цикл*/
		static void* run(void* args);

		/*	Разбор строки местной адресации.
		 * 	Возвращает TRUE, если все величины распознаны без ошибок.*/
		static BOOL parseLocalAddress(const ByteArray* localAddress,
			OTDRId& otdrId,
			COMPortId& comPortId,
			OTAUAddress& otauAddress,
			OTAUPortId& otauPortId);

		/*	Приготовить смотритель оптического переключателя к началу измерений.
		 * 	Порядок действий:
		 * 	1) поискать в списке уже созданных и зарегистрированных, если найден - вернуть;
		 * 	2) попытаться создать новый, т. е. попытаться проинициализировать оптический переключатель
		 * 		с данным адресом на данном последовательном порту,
		 * 		в случае успеха - зарегистрировать и вернуть ссылку на новый смотритель;
		 * 	3) если предыдущие действия успеха не принесли - вернуть NULL.*/
		OTAUController* prepareOTAUController(const COMPortId comPortId, const OTAUAddress otauAddress);

		void freeOTDRs();

		BOOL initializeCOMPorts();
		BOOL setCOMPortProperties(const HANDLE comPortHandle);
		void freeCOMPorts();

		/*	Управление очередями*/
		void pushFrontMeasurementSegment(const MeasurementSegment* measurementSegment);
		void pushFrontResultSegment(const ResultSegment* resultSegment);
		const ResultSegment* popBackResultSegment();

};

#endif // !defined(AFX_RTU_H__1A8AC991_4139_45A7_8FD1_D14AAFB1D55E__INCLUDED_)
