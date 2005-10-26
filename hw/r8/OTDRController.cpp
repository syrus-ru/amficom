//////////////////////////////////////////////////////////////////////
// $Id: OTDRController.cpp,v 1.7 2005/10/26 15:07:44 arseniy Exp $
// 
// Syrus Systems.
// оБХЮОП-ФЕИОЙЮЕУЛЙК ГЕОФТ
// 2004-2005 ЗЗ.
// рТПЕЛФ: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.7 $, $Date: 2005/10/26 15:07:44 $
// $Author: arseniy $
//
// Implementation of the OTDRController class.
//
//////////////////////////////////////////////////////////////////////

#include "OTDRController.h"
#include "BellcoreWriter.h"
#include "ResultSegment.h"
#include <windows.h>
#include <string.h>
#include <stdio.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

OTDRController::OTDRController(const OTDRId otdrId,
			OTDRReportListener* otdrReportListener,
			const unsigned int timewait) {
	this->otdrId = otdrId;
	this->otdrReportListener = otdrReportListener;
	this->timewait = timewait;

	this->state = OTDR_STATE_NEW;
}

OTDRController::~OTDRController() {
	printf("OTDRController | Deleting OTDR Controller: %d\n", this->otdrId);

	/*	Очистить структуру со сведениями о плате рефлектометра*/
	free(this->otdrPluginInfo);	
}

void OTDRController::init() {
	if (this->state == OTDR_STATE_NEW) {
		/*	Достать сведения о плате рефлектометра.*/
		this->otdrPluginInfo = (OTDRPluginInfo*) malloc(sizeof(OTDRPluginInfo));
		this->retrieveOTDRPluginInfo();

//		/*	Вывести допустимые параметры измерений.*/
//		this->printAvailableParameters();

		/*	Изменить текущее состояние*/
		this->state = OTDR_STATE_READY;
	} else {
		printf("OTDRController | ERROR: Cannot call init in state %d\n", this->state);
	}
}

OTDRId OTDRController::getOTDRId() const {
	return this->otdrId;
}

OTDRState OTDRController::getState() const {
	return this->state;
}

void OTDRController::start(ByteArray* measurementId, OTAUController* otauController) {
	if (this->state != OTDR_STATE_READY) {
		printf("OTDRController | ERROR: State %d of controller %d not legal to start data acquisition\n", this->state, this->otdrId);
		return;
	}

	this->currentMeasurementId = measurementId;
	this->currentOTAUController = otauController;

	/*	Создать и запустить главный поток.*/
	pthread_attr_t pt_attr;
	pthread_attr_init(&pt_attr);
	pthread_attr_setdetachstate(&pt_attr, PTHREAD_CREATE_JOINABLE);
	pthread_create(&this->thread, &pt_attr, OTDRController::run, (void*) this);
	pthread_attr_destroy(&pt_attr);

	/*	Изменить текущее состояние*/
	this->state = OTDR_STATE_ACUIRING_DATA;
}

void OTDRController::shutdown() {
	printf("OTDRController | Shutting down OTDR controller %d\n", this->otdrId);

	this->currentOTAUController->shutdown();

	/*	Изменить текущее состояние*/
	this->state = OTDR_STATE_READY;
}

pthread_t OTDRController::getThread() const {
	return this->thread;
}

void* OTDRController::run(void* args) {
	OTDRController* otdrController = (OTDRController*) args;

	BellcoreStructure* bellcoreStructure = otdrController->runMeasurement();
	if (bellcoreStructure == NULL) {
		otdrController->shutdown();
		return NULL;
	}

	const unsigned int nameLength = strlen(PARAMETER_NAME_REFLECTOGRAMMA);
	char* nameData = new char[nameLength];
	memcpy(nameData, PARAMETER_NAME_REFLECTOGRAMMA, nameLength);
	ByteArray* baName = new ByteArray(nameLength, nameData);

	BellcoreWriter* bellcoreWriter = new BellcoreWriter();
	bellcoreWriter->write(bellcoreStructure);
	char* valueData = (char*) bellcoreWriter->get_data();
	int valueLength = bellcoreWriter->get_data_size();
	ByteArray* baValue = new ByteArray(valueLength, valueData);

	delete bellcoreWriter;
	delete bellcoreStructure;

	Parameter** resultParameters = new Parameter*[1];
	resultParameters[0] = new Parameter(baName, baValue);

	const ResultSegment* resultSegment = new ResultSegment(otdrController->currentMeasurementId, 1, resultParameters);
	OTDRReportListener* otdrReportListener = otdrController->otdrReportListener;
	otdrReportListener->acceptOTDRReport(resultSegment);

	otdrController->shutdown();
	return NULL;
}
