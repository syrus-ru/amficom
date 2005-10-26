// PK7600OTDRController.cpp: implementation of the PK7600OTDRController class.
//
//////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include "PK7600OTDRController.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

PK7600OTDRController::PK7600OTDRController(const OTDRId otdrId,
			OTDRReportListener* otdrReportListener,
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

BOOL PK7600OTDRController::setMeasurementParameters(const Parameter** parameters, const unsigned int parNumber) {
	printf("PK7600OTDRController | Setting measurement parameters");

	int waveLength = -1;
	double traceLength = -1;
	double resolution = -1;
	short pulseWidth = -1;
	double ior = -1;
	double scans = -1;
	short smoothFilter = -1;
	
	char* parameterName;
	ByteArray* bValue;
	for (unsigned int i = 0; i < parNumber; i++) {
		parameterName = parameters[i]->getName()->getData();
		if (strcmp(parameterName, PARAMETER_NAME_WAVELENGTH) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			waveLength = *(int*) bValue->getData();
			delete bValue;
		} else if (strcmp(parameterName, PARAMETER_NAME_TRACE_LENGTH) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			traceLength = *(double*) bValue->getData();
			delete bValue;
		} else if (strcmp(parameterName, PARAMETER_NAME_RESOLUTION) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			resolution = *(double*) bValue->getData();
			delete bValue;
		} else if (strcmp(parameterName, PARAMETER_NAME_PULSE_WIDTH) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			pulseWidth = *(short*) bValue->getData();
			delete bValue;
		} else if (strcmp(parameterName, PARAMETER_NAME_IOR) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			ior = *(double*) bValue->getData();
			delete bValue;
		} else if (strcmp(parameterName, PARAMETER_NAME_SCANS) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			scans = *(double*) bValue->getData();
			delete bValue;
		} else if (strcmp(parameterName, PARAMETER_NAME_SMOOTH_FILTER) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			smoothFilter = *(short*) bValue->getData();
			delete bValue;
		} else {
			printf("PK7600OTDRController | Unknown name of parameter: %s\n", parameterName);
		}
	}

	if (waveLength < 0) {
		printf("PK7600OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_WAVELENGTH);
		return FALSE;
	}
	if (traceLength < 0) {
		printf("PK7600OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_TRACE_LENGTH);
		return FALSE;
	}
	if (resolution < 0) {
		printf("PK7600OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_RESOLUTION);
		return FALSE;
	}
	if (pulseWidth < 0) {
		printf("PK7600OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_PULSE_WIDTH);
		return FALSE;
	}
	if (ior < 0) {
		printf("PK7600OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_IOR);
		return FALSE;
	}
	if (scans < 0) {
		printf("PK7600OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_SCANS);
		return FALSE;
	}
	if (smoothFilter < 0) {
		printf("PK7600OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_SMOOTH_FILTER);
		return FALSE;
	}

	this->waveLengthM = (unsigned short) waveLength;
	this->traceLengthM = (float) traceLength;
	this->resolutionM = (float) resolution;
	this->pulseWidthM = (float) pulseWidth;
	this->iorM = ior;
	this->scansM = (unsigned short) scans;
	this->smoothFilterM = smoothFilter;

	int code = PK7600AcqSetParamsEx(this->otdrId,
			this->scansM,
			this->waveLengthM,
			this->traceLengthM,
			this->resolutionM,
			this->pulseWidthM,
			this->smoothFilterM);
	if (code == 0) {
		return TRUE;
	}
	printf("PK7600OTDRController | ERROR: Cannot set measurement parameters -- ");
	switch (code) {
		case PK7600_ERR_BAD_AVERAGES:
			printf("bad averages\n");
			break;
		case PK7600_ERR_BAD_COMBO:
			printf("bad combo\n");
			break;
		case PK7600_ERR_BAD_DATA:
			printf("bad data\n");
			break;
		case PK7600_ERR_SAFETY_VIOL:
			printf("bad savety violation\n");
			break;
		case PK7600_ERR_OUT_OF_RANGE:
			printf("bad out of range\n");
			break;
		default:
			printf("unknown error code\n");
	}
	return FALSE;
}

BellcoreStructure* PK7600OTDRController::runMeasurement() const {
	unsigned long maxPoints;
	switch (this->cardType) {
		case PK7600_ISA_CARD:
			maxPoints = PK7600_MAX_WFM_POINTS;
			break;
		case PK7600_PCI_CARD:
			maxPoints = PK7600_PCI_MAX_WFM_POINTS;
			break;
		default:
			printf("PK7600OTDRController | ERROR: OTDR %d -- unknown card type %d\n", this->otdrId, this->cardType);
			return NULL;
	}

	HANDLE* events = PK7600AcqStart(this->otdrId, NULL);
	int ret, cnt = 0, stat = 0;
	do {
		ret = WaitForSingleObject(*events, 1500l);
		if (ret == WAIT_OBJECT_0 || ret == WAIT_TIMEOUT) {
			PK7600AcqCheckStatus(this->otdrId, &cnt);
			printf("PK7600OTDRController | OTDR %d, averages remaining: %d\n", this->otdrId, cnt);
		} else if (ret == WAIT_OBJECT_0 + 2) {
			PK7600AcqStop(this->otdrId);
			printf("PK7600OTDRController | ERROR: OTDR %d, all data is clipped\n", this->otdrId);
		} else if (ret != 0 && ret != WAIT_TIMEOUT) {
			printf("PK7600OTDRController | ERROR: OTDR %d, acquisition failed\n", this->otdrId);
			return NULL;
		}
	} while (ret != WAIT_OBJECT_0 && ret != (WAIT_OBJECT_0 + 2));

	OTDRWaveformHeader* waveFormHeader = (OTDRWaveformHeader*) malloc(sizeof(OTDRWaveformHeader));
	OTDRWaveformData* waveFormData = new OTDRWaveformData[maxPoints];

	if (ret == WAIT_OBJECT_0) {
		PK7600AcqStop(this->otdrId);
		stat = PK7600RetrieveWaveform(this->otdrId, waveFormHeader, waveFormData);
	} else if (ret == WAIT_OBJECT_0 + 2) {
		PK7600AcqStop(this->otdrId);
		free(waveFormHeader);
		delete[] waveFormData;
		return NULL;
	}

	if (stat != 0) {
		printf("PK7600OTDRController | ERROR: OTDR %d, cannot retrieve waveform, code: %d\n", this->otdrId, stat);
		free(waveFormHeader);
		delete[] waveFormData;
		return NULL;
	}

	BellcoreStructure* bellcoreStructure =  new BellcoreStructure();
	//this->fillBellcoreStructure(bellcoreStructure, waveFormHeader, waveFormData);

	free(waveFormHeader);
	delete[] waveFormData;

	return bellcoreStructure;
}

void PK7600OTDRController::fillBellcoreStructure(BellcoreStructure* bellcoreStructure,
							OTDRWaveformHeader* waveFormHeader,
							OTDRWaveformData*  waveFormData) const {
}

tCardType PK7600OTDRController::getCardType() const {
	return this->cardType;
}
