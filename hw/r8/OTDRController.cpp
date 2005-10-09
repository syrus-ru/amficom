//////////////////////////////////////////////////////////////////////
// $Id: OTDRController.cpp,v 1.4 2005/10/09 13:47:50 arseniy Exp $
// 
// Syrus Systems.
// ������-����������� �����
// 2004-2005 ��.
// ������: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.4 $, $Date: 2005/10/09 13:47:50 $
// $Author: arseniy $
//
// Implementation of the OTDRController class.
//
//////////////////////////////////////////////////////////////////////

#include "OTDRController.h"
#include <windows.h>
#include <string.h>
#include <stdio.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

OTDRController::OTDRController(const OTDRId otdrId,
			const OTDRReportListener* otdrReportListener,
			const unsigned int timewait) {
	this->otdrId = otdrId;
	this->otdrReportListener = otdrReportListener;
	this->timewait = timewait;

	this->state = OTDR_STATE_READY;
}

OTDRController::~OTDRController() {
	printf("OTDRController | Deleting OTDR Controller: %d\n", this->otdrId);
}

OTDRId OTDRController::getOTDRId() const {
	return this->otdrId;
}

OTDRState OTDRController::getState() const {
	return this->state;
}

void OTDRController::start() {
	if (this->state != OTDR_STATE_READY) {
		printf("OTDRController | ERROR: State %d not legal to start data acquisition\n", this->state);
		return;
	}

	/*	������� �������� � ����� �������������.*/
	this->otdrPluginInfo = (OTDRPluginInfo*) malloc(sizeof(OTDRPluginInfo));
	this->retrieveOTDRPluginInfo();

	/*	������� � ��������� ������� �����.*/
	this->running = 1;
	pthread_attr_t pt_attr;
	pthread_attr_init(&pt_attr);
	pthread_attr_setdetachstate(&pt_attr, PTHREAD_CREATE_JOINABLE);
	pthread_create(&this->thread, &pt_attr, OTDRController::run, (void*)this);
	pthread_attr_destroy(&pt_attr);

	/*	�������� ������� ���������*/
	this->state = OTDR_STATE_ACUIRING_DATA;
}

void OTDRController::shutdown() {
	this->running = 0;
	printf("OTDRController | Shutting down\n");

	/*	�������� ��������� �� ���������� � ����� �������������*/
	free(this->otdrPluginInfo);

	/*	�������� ������� ���������*/
	this->state = OTDR_STATE_READY;
}

pthread_t OTDRController::getThread() const {
	return this->thread;
}

void* OTDRController::run(void* args) {
	OTDRController* otdrController = (OTDRController*) args;
	const unsigned int twms = otdrController->timewait * 1000;	//��������� � ������������

	while(otdrController->running) {
		Sleep(twms);

		printf("OTDRController | \n");
	}

	return NULL;
}
