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

}

OTDRModel PK7600OTDRController::getOTDRModel() const {
	return OTDR_MODEL_PK7600;
}

void PK7600OTDRController::printAvailableParameters() const {
	printf("PK7600 card N %d", this->otdrId);
}

tCardType PK7600OTDRController::getCardType() const {
	return this->cardType;
}
