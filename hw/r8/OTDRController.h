//////////////////////////////////////////////////////////////////////
// $Id: OTDRController.h,v 1.7 2005/10/25 14:18:04 arseniy Exp $
// 
// Syrus Systems.
// ������-����������� �����
// 2004-2005 ��.
// ������: r8
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
		/*	������ �� ������� ������ ����������*/
		const OTDRReportListener* otdrReportListener;

		/*	������� ��������� �������� ���������� �������������� */
		OTDRState state;

		/*	��������������� �������� ��� ���������� ������� */
		pthread_t thread;
		unsigned int timewait;
		int running;

	public:
		OTDRController(const OTDRId otdrId,
			const OTDRReportListener* otdrReportListener,
			const unsigned int timewait);
		virtual ~OTDRController();

		/*	�������� �������������� �������� � ��������, ������ ��� �� ������ ����� � ������.
		 * 	����� ���� ������� �������� ������ ��� ������� � ��������� OTDR_STATE_NEW.
		 * 	����� ������ ��������� ������ ���� OTDR_STATE_READY.*/
		void init();

		/*	�������� ���������� ����� ����� �������������*/
		OTDRId getOTDRId() const;

		/*	�������� ������ �������������.
		 * 	����������� � ����������*/
		virtual OTDRModel getOTDRModel() const = 0;

		/*	���������� ��������� ���������.
		 * 	� ������ ������������ �������� ���������� FALSE.*/
		virtual BOOL setMeasurementParameters(const Parameter** parameters, const unsigned int parNumber) = 0;

		/*	�������� ������� ���������*/
		OTDRState getState() const;

		/*	���������� �������*/
		void start();
		void shutdown();
		pthread_t getThread() const;

	private:
		/*	������� �������� � ����� �������������.
		 * 	����������� � ����������.*/
		virtual void retrieveOTDRPluginInfo() = 0;

		/*	����������� ���������� ��������� ���������.
		 * 	����������� � ����������*/
		virtual void printAvailableParameters() const = 0;

		/*	������� ���� ������*/
		static void* run(void* args);

};

#endif // !defined(AFX_OTDRCONTROLLER_H__FF003D9F_71B8_413B_A7ED_FDBFCE325E74__INCLUDED_)
