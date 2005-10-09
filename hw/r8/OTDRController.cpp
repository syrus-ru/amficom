//////////////////////////////////////////////////////////////////////
// $Id: OTDRController.cpp,v 1.2 2005/10/09 12:18:34 arseniy Exp $
// 
// Syrus Systems.
// Ó¡’ﬁŒœ-‘≈»Œ…ﬁ≈”À…  √≈Œ‘“
// 2004-2005 ««.
// “œ≈À‘: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.2 $, $Date: 2005/10/09 12:18:34 $
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
	
	this->otdrPluginInfo = (OTDRPluginInfo*) malloc(sizeof(OTDRPluginInfo));
	this->retrieveOTDRPluginInfo();
}

OTDRController::~OTDRController() {
	printf("OTDRController | Deleting OTDR Controller: %d\n", this->otdrId);
	free(this->otdrPluginInfo);
}

OTDRId OTDRController::getOTDRId() const {
	return this->otdrId;
}

OTDRState OTDRController::getState() const {
	return this->state;
}

void OTDRController::start() {
	//—ÓÁ‰‡Ú¸ Ë Á‡ÔÛÒÚËÚ¸ „Î‡‚Ì˚È ÔÓÚÓÍ.
	this->running = 1;
	pthread_attr_t pt_attr;
	pthread_attr_init(&pt_attr);
	pthread_attr_setdetachstate(&pt_attr, PTHREAD_CREATE_JOINABLE);
	pthread_create(&this->thread, &pt_attr, OTDRController::run, (void*)this);
	pthread_attr_destroy(&pt_attr);
	this->state = OTDR_STATE_READY;
}

void OTDRController::shutdown() {
	printf("OTDRController | Shutting down\n");
	this->running = 0;
}

pthread_t OTDRController::getThread() const {
	return this->thread;
}

void* OTDRController::run(void* args) {
	OTDRController* otdrController = (OTDRController*) args;
	const unsigned int twms = otdrController->timewait * 1000;	//œÂÂ‚ÂÒÚË ‚ ÏËÎÎËÒÂÍÛÌ‰˚

	while(otdrController->running) {
		Sleep(twms);

		printf("OTDRController | \n");
	}

	return NULL;
}
