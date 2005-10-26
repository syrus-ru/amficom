//////////////////////////////////////////////////////////////////////
// $Id: OTDRController.h,v 1.8 2005/10/26 15:07:44 arseniy Exp $
// 
// Syrus Systems.
// ������-����������� �����
// 2004-2005 ��.
// ������: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.8 $, $Date: 2005/10/26 15:07:44 $
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
#include "BellcoreStructure.h"
#include "OTAUController.h"


typedef unsigned short OTDRId;
using namespace std;

#define SIZE_MANUFACTORER_NAME 20
#define SIZE_MODEL_NAME 16
#define SIZE_SERIAL_NUMBER 32
#define SIZE_MODEL_NUMBER 32
#define SIZE_PART_NUMBER 16

#define PARAMETER_NAME_REFLECTOGRAMMA (const char*) "reflectogramma"

typedef struct {
	char manufacturerName[SIZE_MANUFACTORER_NAME];	//�������������
	char modelName[SIZE_MODEL_NAME];				//�������� ������
	char serialNumber[SIZE_SERIAL_NUMBER];			//�������� �����
	char modelNumber[SIZE_MODEL_NUMBER];			//����� ������
	char partNumber[SIZE_PART_NUMBER];				//����� ������
	unsigned short revisionId;						//������ ��
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
		/*	����� ����� ������������� */
		OTDRId otdrId;

		/*	��������� �� ���������, ���������� �������� � ����� �������������.*/
		OTDRPluginInfo* otdrPluginInfo;

	private:
		/*	������ �� ������� ������ ����������.*/
		OTDRReportListener* otdrReportListener;

		/*	������� ��������� �������� ���������� ��������������.*/
		OTDRState state;

		/*	��������������� �������� ��� ���������� �������.*/
		pthread_t thread;
		unsigned int timewait;

		/*	������������� �������� ���������.
		 * 	������ �������� ����� �������� ������������ �������������.
		 * 	������ ��������� ������ � ��������� ����������.*/
		ByteArray* currentMeasurementId;

		/*	������ �� �������� ���������� ���������� �������������� ��� ������� ���������.*/
		OTAUController* currentOTAUController;

	public:
		OTDRController(const OTDRId otdrId,
			OTDRReportListener* otdrReportListener,
			const unsigned int timewait);
		virtual ~OTDRController();

		/*	�������� �������������� �������� � ��������, ������ ��� �� ������ ����� � ������.
		 * 	����� ���� ������� �������� ������ ��� ������� � ��������� OTDR_STATE_NEW.
		 * 	����� ������ ��������� ������ ���� OTDR_STATE_READY.*/
		void init();

		/*	�������� ���������� ����� ����� �������������.*/
		OTDRId getOTDRId() const;

		/*	�������� ������ �������������.
		 * 	����������� � ����������.*/
		virtual OTDRModel getOTDRModel() const = 0;

		/*	���������� ��������� ���������.
		 * 	� ������ ������������ �������� ���������� FALSE.*/
		virtual BOOL setMeasurementParameters(const Parameter** parameters, const unsigned int parNumber) = 0;

		/*	�������� ������� ���������.*/
		OTDRState getState() const;

		/*	���������� �������.*/
		void start(ByteArray* measurementId, OTAUController* otauController);
		void shutdown();
		pthread_t getThread() const;

	private:
		/*	������� �������� � ����� �������������.
		 * 	����������� � ����������.*/
		virtual void retrieveOTDRPluginInfo() = 0;

		/*	����������� ���������� ��������� ���������.
		 * 	����������� � ����������.*/
		virtual void printAvailableParameters() const = 0;

		/*	������� ���� ������.*/
		static void* run(void* args);

		/*	�������� ���������.
		 * 	� ������ ������ ���������� ������ ��������� ������ ������. ����� - NULL.
		 * 	����������� � ����������.*/
		virtual BellcoreStructure* runMeasurement() const = 0;
};

#endif // !defined(AFX_OTDRCONTROLLER_H__FF003D9F_71B8_413B_A7ED_FDBFCE325E74__INCLUDED_)
