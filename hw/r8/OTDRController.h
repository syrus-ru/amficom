//////////////////////////////////////////////////////////////////////
// $Id: OTDRController.h,v 1.2 2005/10/09 12:18:34 arseniy Exp $
// 
// Syrus Systems.
// ������-����������� �����
// 2004-2005 ��.
// ������: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.2 $, $Date: 2005/10/09 12:18:34 $
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

#include "pthread.h"
#include "OTDRReportListener.h"


typedef unsigned short OTDRId;

#define SIZE_SERIAL_NUMBER 32
#define SIZE_MODEL_NUMBER 32

typedef struct {
	char manufacturerName[20];					//�������������
	char modelName[16];							//�������� ������
	char serialNumber[SIZE_SERIAL_NUMBER];		//�������� �����
	char modelNumber[SIZE_MODEL_NUMBER];		//����� ������
	char partNumber[16];						//����� ������
	unsigned short revisionId;					//������ ��
} OTDRPluginInfo;

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

		/*	�������� ���������� ����� ����� �������������*/
		OTDRId getOTDRId() const;

		/*	�������� ������ �������������.
			����������� � ����������*/
		virtual OTDRModel getOTDRModel() const = 0;

		/*	����������� ���������� ��������� ���������.
			����������� � ����������*/
		virtual void printAvailableParameters() const = 0;

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

		/*	������� ���� ������*/
		static void* run(void* args);

};

#endif // !defined(AFX_OTDRCONTROLLER_H__FF003D9F_71B8_413B_A7ED_FDBFCE325E74__INCLUDED_)
