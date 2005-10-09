// PK7600OTDRController.cpp: implementation of the PK7600OTDRController class.
//
//////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include "PK7600OTDRController.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

PK7600OTDRController::PK7600OTDRController(const OTDRId otdrId,
			const OTDRReportListener* otdrReportListener,
			const unsigned int timewait,
			const tCardType cardType)
				: OTDRController(otdrId,
					otdrReportListener,
					timewait) {
	this->cardType = cardType;
}

PK7600OTDRController::~PK7600OTDRController() {
	printf("PK7600OTDRController | Shutting down card: %d\n", this->otdrId);
	PK7600ShutDown(this->otdrId);
}

void PK7600OTDRController::retrieveOTDRPluginInfo() {
	strcpy(this->otdrPluginInfo->manufacturerName, "Nettest");
	strcpy(this->otdrPluginInfo->modelName, "PK7600");

	pPK7600PluginInfo pk7600PluginInfo = (pPK7600PluginInfo) malloc(sizeof(PK7600OTDRPluginInfo));
	PK7600GetPluginData(this->otdrId, pk7600PluginInfo);
	memcpy(this->otdrPluginInfo->serialNumber, pk7600PluginInfo->SerialNumber, SIZE_SERIAL_NUMBER);
	memcpy(this->otdrPluginInfo->modelNumber, pk7600PluginInfo->ModelNumber, SIZE_MODEL_NUMBER);
	strcpy(this->otdrPluginInfo->partNumber, "0");
	free(pk7600PluginInfo);

	this->otdrPluginInfo->revisionId = PK7600BoardRevision(this->otdrId);
}

OTDRModel PK7600OTDRController::getOTDRModel() const {
	return OTDR_MODEL_PK7600;
}

void PK7600OTDRController::printAvailableParameters() const {
	printf("PK7600OTDRController | PK7600 card N %d", this->otdrId);
}

tCardType PK7600OTDRController::getCardType() const {
	return this->cardType;
}
