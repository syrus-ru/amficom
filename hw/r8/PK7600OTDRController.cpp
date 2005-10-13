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
	memcpy(this->otdrPluginInfo->manufacturerName, "Nettest", SIZE_MANUFACTORER_NAME);
	memcpy(this->otdrPluginInfo->modelName, "PK7600", SIZE_MODEL_NAME);

	pPK7600PluginInfo pk7600PluginInfo = (pPK7600PluginInfo) malloc(sizeof(PK7600OTDRPluginInfo));
	PK7600GetPluginData(this->otdrId, pk7600PluginInfo);
	memcpy(this->otdrPluginInfo->serialNumber, pk7600PluginInfo->SerialNumber, SIZE_SERIAL_NUMBER);
	memcpy(this->otdrPluginInfo->modelNumber, pk7600PluginInfo->ModelNumber, SIZE_MODEL_NUMBER);
	memcpy(this->otdrPluginInfo->partNumber, "0", SIZE_PART_NUMBER);
	free(pk7600PluginInfo);

	this->otdrPluginInfo->revisionId = PK7600BoardRevision(this->otdrId);
}

void PK7600OTDRController::printAvailableParameters() const {
	printf("PK7600OTDRController | PK7600 card N %d, serial number: %s, model number: %s\n", this->otdrId, this->otdrPluginInfo->serialNumber, this->otdrPluginInfo->modelNumber);

	float* wavelengths = new float[PK7600_MAX_WAVES];
	PK7600GetAvailWaves(this->otdrId, wavelengths);

	float* pulsewidths = new float[PK7600_MAX_PULSES];
	PK7600GetAvailPulses(this->otdrId, pulsewidths);

	float *ranges = new float[PK7600_MAX_RANGES];
	PK7600GetAvailRanges(this->otdrId, ranges);

	float* pointSpacings = new float[PK7600_MAX_SPACINGS];
	PK7600GetAvailSpacings(this->otdrId, pointSpacings);

	for (int w = 0; w < PK7600_MAX_WAVES; w++) {
		if (wavelengths[w] == 0) {
			continue;
		}
		printf("Wavelength: %f:\n", wavelengths[w]);
		for (int r = 0; r < PK7600_MAX_RANGES; r++) {
			if (ranges[r] == 0) {
				continue;
			}
			printf("\tRange: %f:\n", ranges[r]);
			const int maxPointSpacingIndex = PK7600GetMaxSpacing(this->otdrId, wavelengths[w], ranges[r], pointSpacings);
			for (int ps = 0; ps <= maxPointSpacingIndex; ps++) {
				printf("\t\tPoint spacing: %f:\n", pointSpacings[ps]);
				const int minPulseWidthIndex = PK7600GetMinPulseWidth(this->otdrId, wavelengths[w], ranges[r], pointSpacings[ps], pulsewidths);
				const int maxPulseWidthIndex = PK7600GetMaxPulseWidth(this->otdrId, wavelengths[w], ranges[r], pointSpacings[ps], pulsewidths);
				for (int pw = minPulseWidthIndex; pw <= maxPulseWidthIndex; pw++) {
					printf("\t\t\tPulse width: %f:\n", pulsewidths[pw]);
				}
			}
		}
	}
}

OTDRModel PK7600OTDRController::getOTDRModel() const {
	return OTDR_MODEL_PK7600;
}

tCardType PK7600OTDRController::getCardType() const {
	return this->cardType;
}

BOOL PK7600OTDRController::setMeasurementParameters0(const ParametersMapT parametersMap) const {
	printf("PK7600OTDRController | Setting measurement parameters");
	
	return FALSE;
}
