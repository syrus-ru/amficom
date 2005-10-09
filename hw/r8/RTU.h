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
		/*	������� ����������� ��������������
		 * 	���� - ���������� �������������, �������� - ��������� �� ����������*/
		OTDRControllerMapT otdrControllersMap;

		/*	���������� ��������� ���������������� ������*/
		unsigned short comPortNumber;

		/*	������� �������� �������� ����������.
		 * 	���� - ����� ����������������� �����, �������� - ��������������� ���������*/
		COMPortHandleMapT comPortHandlesMap;

		/*	������� ��������� �� ����������� ��������������.
		 * 	���� - ����� ����������������� �����, �������� - ����� ��������� �� ���� �����*/
		OTAUControllerMapT otauControllersMap;

		/* ������� �������� �� ��������� � ����������� */
		MeasurementQueueT measurementQueue;
		ResultQueueT resultQueue;

		/* ������� ��������� */
		RTUState state;

		/* ����� ������������� */
		pthread_mutex_t* tMutex;
		pthread_mutex_t* rMutex;

		/*	��������������� �������� ��� ���������� ������� */
		pthread_t thread;
		unsigned int timewait;
		int running;

	public:
		RTU(const unsigned short comPortNumber, const unsigned int timewait);
		virtual ~RTU();

		/*	���������������� ����� ������������.
		 * 	���������� ���������� OTDRContainer.*/
		void registerOTDRController(const OTDRController* otdrController);

		/*	�������� � ������������.
		 * 	���������� ���������� SegmentProcessor.*/
		void addTaskSegment(const Segment* segment);

		/*	������ �� �������.
		 * 	���������� ���������� SegmentProcessor.*/
		const Segment* removeReportSegment();

		/*	������ �������� � ������� ��������� ������.
		 * 	���������� ���������� SegmentProcessor.*/
		void reAddReportSegment(const Segment* segment);

		/*	������� ������ � �������������.
		 * 	���������� ���������� OTDRReportListener.*/
		void acceptOTDRReport(const Segment* segment);

		/*	�������� ������� ���������*/
		RTUState getRTUState() const;

		/*	�������� ������ �����*/
		unsigned int getTimewait() const;

		/*	���������� �������*/
		void start();
		void shutdown();
		pthread_t getThread() const;

	private:
		/*	������� ����*/
		static void* run(void* args);

		/*	������ ������ ������� ���������.
		 * 	���������� TRUE, ���� ��� �������� ���������� ��� ������.*/
		static BOOL parseLocalAddress(const ByteArray* localAddress,
			OTDRId& otdrId,
			COMPortId& comPortId,
			OTAUAddress& otauAddress,
			OTAUPortId& otauPortId);

		/*	����������� ���������� ����������� ������������� � ������ ���������.
		 * 	������� ��������:
		 * 	1) �������� � ������ ��� ��������� � ������������������, ���� ������ - �������;
		 * 	2) ���������� ������� �����, �. �. ���������� ������������������� ���������� �������������
		 * 		� ������ ������� �� ������ ���������������� �����,
		 * 		� ������ ������ - ���������������� � ������� ������ �� ����� ����������;
		 * 	3) ���� ���������� �������� ������ �� �������� - ������� NULL.*/
		OTAUController* prepareOTAUController(const COMPortId comPortId,
			const OTAUAddress otauAddress);

		void freeOTDRs();

		BOOL initializeCOMPorts();
		BOOL setCOMPortProperties(const HANDLE comPortHandle);
		void freeCOMPorts();

		/*	���������� ���������*/
		void pushFrontMeasurementSegment(const MeasurementSegment* measurementSegment);
		void pushFrontResultSegment(const ResultSegment* resultSegment);
		const ResultSegment* popBackResultSegment();

};

#endif // !defined(AFX_RTU_H__1A8AC991_4139_45A7_8FD1_D14AAFB1D55E__INCLUDED_)
