#include <stdio.h>
#include "QP1640OTDRController.h"

QP1640OTDRController::QP1640OTDRController(const OTDRId otdrId,
			const OTDRReportListener* otdrReportListener,
			const unsigned int timewait)
				: OTDRController(otdrId,
					otdrReportListener,
					timewait) {
}

QP1640OTDRController::~QP1640OTDRController() {
	printf("QP1640OTDRController | Shutting down card: %d\n", this->otdrId);
}

void QP1640OTDRController::retrieveOTDRPluginInfo() {
	QPPluginData* qpPluginData = (QPPluginData*) malloc(sizeof(QPPluginData));
	QPOTDRDataCollectInfo(this->otdrId, qpPluginData);

	memcpy(this->otdrPluginInfo->manufacturerName, qpPluginData->pluginInfo.szManufacturer, SIZE_MANUFACTORER_NAME);
	memcpy(this->otdrPluginInfo->modelName, qpPluginData->pluginInfo.szModel, SIZE_MODEL_NAME);
	memcpy(this->otdrPluginInfo->serialNumber, qpPluginData->pluginInfo.SerialNumber, SIZE_SERIAL_NUMBER);
	memcpy(this->otdrPluginInfo->modelNumber, qpPluginData->pluginInfo.ModelNumber, SIZE_MODEL_NUMBER);
	memcpy(this->otdrPluginInfo->partNumber, qpPluginData->pluginInfo.szPartNumber, SIZE_PART_NUMBER);
	this->otdrPluginInfo->revisionId = qpPluginData->wRevision;

	this->fastScanCount = qpPluginData->dwFastScanCount;

	free(qpPluginData);
}

void QP1640OTDRController::printAvailableParameters() const {
	printf("QP1640OTDRController | QP1640 card N %d", this->otdrId);
}

OTDRModel QP1640OTDRController::getOTDRModel() const {
	return OTDR_MODEL_ONT_UT;
}

BOOL QP1640OTDRController::setMeasurementParameters(const Parameter** parameters, const unsigned int parNumber) {
	printf("QP1640OTDRController | Setting measurement parameters");

	int waveLength = -1;
	double traceLength = -1;
	double resolution = -1;
	short pulseWidth = -1;
	double ior = -1;
	double scans = -1;
	int flagPulseWidthLowRes = 1;
	int flagGainSpliceOn = 0;
	int flagLiveFiberDetect = 0;

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
		} else if (strcmp(parameterName, PARAMETER_NAME_PULSE_WIDTH_HIGH_RES) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			pulseWidth = *(short*) bValue->getData();
			flagPulseWidthLowRes = 0;
			delete bValue;
		} else if (strcmp(parameterName, PARAMETER_NAME_PULSE_WIDTH_LOW_RES) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			pulseWidth = *(short*) bValue->getData();
			flagPulseWidthLowRes = 1;
			delete bValue;
		} else if (strcmp(parameterName, PARAMETER_NAME_IOR) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			ior = *(double*) bValue->getData();
			delete bValue;
		} else if (strcmp(parameterName, PARAMETER_NAME_SCANS) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			scans = *(double*) bValue->getData();
			delete bValue;
		} else if (strcmp(parameterName, PARAMETER_NAME_FLAG_GAIN_SPLICE_ON) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			flagGainSpliceOn = *(int*)(bValue->getData());
			delete bValue;
		} else if (strcmp(parameterName, PARAMETER_NAME_FLAG_LIVE_FIBER_DETECT) == 0) {
			bValue = parameters[i]->getValue()->getReversed();
			flagLiveFiberDetect = *(int*)(bValue->getData());
			delete bValue;
		} else {
			printf("QP1640OTDRController | Unknown name of parameter: %s\n", parameterName);
		}
	}

	if (waveLength < 0) {
		printf("QP1640OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_WAVELENGTH);
		return FALSE;
	}
	if (traceLength < 0) {
		printf("QP1640OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_TRACE_LENGTH);
		return FALSE;
	}
	if (resolution < 0) {
		printf("QP1640OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_RESOLUTION);
		return FALSE;
	}
	if (pulseWidth < 0) {
		printf("QP1640OTDRController | ERROR: Neither parameter %s nor parameter %s found\n",
				PARAMETER_NAME_PULSE_WIDTH_HIGH_RES,
				PARAMETER_NAME_PULSE_WIDTH_LOW_RES);
		return FALSE;
	}
	if (ior < 0) {
		printf("QP1640OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_IOR);
		return FALSE;
	}
	if (scans < 0) {
		printf("QP1640OTDRController | ERROR: Parameter %s not found\n", PARAMETER_NAME_SCANS);
		return FALSE;
	}

	const int waveIndex = this->getWaveIndex(waveLength);
	if (waveIndex < 0) {
		printf("QP1640OTDRController | ERROR: Wavelength %d not found in array of valid values\n", waveLength);
		return FALSE;
	}
	this->waveLengthM = (float) waveLength;

	const int rangeIndex = this->getRangeIndex(traceLength, this->waveLengthM);
	if (rangeIndex < 0) {
		printf("QP1640OTDRController | ERROR: Trace length %f not found in array of valid values\n", traceLength);
		return FALSE;
	}
	this->traceLengthM = (float) traceLength;

	const int pointSpacingIndex = this->getPointSpacingIndex(resolution);
	if (pointSpacingIndex < 0) {
		printf("QP1640OTDRController | ERROR: Resolution %f not found in array of valid values\n", resolution);
		return FALSE;
	}
	this->resolutionM = (float) resolution;

	const int pulseWidthIndex = this->getPulseWidthIndex(pulseWidth, flagPulseWidthLowRes, this->waveLengthM);
	if (pulseWidthIndex < 0) {
		printf("QP1640OTDRController | ERROR: Pulse width %d not found in array of valid values\n", pulseWidth);
		return FALSE;
	}
	this->pulseWidthM = (DWORD) pulseWidth;

	if (!this->iorIsValid(ior, this->waveLengthM)) {
		printf("QP1640OTDRController | ERROR: Index of refraction %f is not valid\n", ior);
		return FALSE;
	}
	this->iorM = (float) ior;

	int averagesIndex = this->getAveragesIndex(scans, this->waveLengthM);
	if (averagesIndex < 0) {
		printf("QP1640OTDRController | ERROR: Number of averages %f not found in array of valid values\n", scans);
		return FALSE;
	}
	this->scansM = (DWORD) scans;

	this->filterFlagsM = 0;
	if (flagGainSpliceOn) {
		this->filterFlagsM |= GAIN_SPLICE_ON;
	}
	if (flagLiveFiberDetect) {
		this->filterFlagsM |= LIVE_FIBER_DETECT;
	}

	int code = QPOTDRAcqSetParams(this->otdrId,
							(WORD) (this->scansM / this->fastScanCount),
							waveIndex,
							(DWORD) (this->traceLengthM * 1000.f / this->resolutionM/*min (resolution, 4.0f)*/),//???
							pointSpacingIndex,
							pulseWidthIndex,
							3 + 1);//???
	if (code == 0) {
		return TRUE;
	}
	printf("QP1640OTDRController | ERROR: Cannot set measurement parameters -- ");
	switch (code) {
		case -1:
			printf("bad card index\n");
			break;
		case -2:
			printf("bad number of averages\n");
			break;
		case -3:
			printf("bad wave index\n");
			break;
		case -4:
			printf("bad pulse index\n");
			break;
		case -5:
			printf("bad range\n");
			break;
		case -6:
			printf("bad point spacing index\n");
			break;
		default:
			printf("unknown error code\n");
	}
	return FALSE;
}

int QP1640OTDRController::getWaveIndex(const int waveLength) {
	int ret;
	int maxWaves = QPOTDRGetMaxWaves(this->otdrId);
	if (maxWaves > 0) {
		float* waves = new float[MAX_WAVES];
		QPOTDRGetAvailWaves(this->otdrId, waves);
		ret = getIndexInArray((float) waveLength, waves, maxWaves);
		delete[] waves;
	} else {
		printf("QP1640OTDRController | ERROR: QPOTDRGetMaxWaves returned error\n");
		ret = -1;
	}
	return ret;
}

int QP1640OTDRController::getRangeIndex(const double traceLength, const float wave) {
	int ret;
	int maxRanges = QPOTDRGetMaxRanges(this->otdrId, wave);
	if (maxRanges > 0) {
		float* ranges = new float[MAX_RANGES];
		QPOTDRGetAvailRanges(this->otdrId, wave, ranges);
		ret = getIndexInArray((float) traceLength, ranges, maxRanges);
		delete[] ranges;
	} else {
		printf("QP1640OTDRController | ERROR: QPOTDRGetMaxRanges returned error\n");
		ret = -1;
	}
	return ret;
}

int QP1640OTDRController::getPointSpacingIndex(const double resolution) {
	int ret;
	int maxPointSpacings = QPOTDRGetMaxPointSpacings(this->otdrId);
	if (maxPointSpacings > 0) {
		float* pointSpacings = new float[MAX_SPACINGS];
		QPOTDRGetAvailSpacings(this->otdrId, pointSpacings);
		//FIXME: Total number of values is 8, but QPOTDRGetMaxPointSpacings returns 7
		ret = getIndexInArray((float) resolution, pointSpacings, MAX_SPACINGS);
		delete[] pointSpacings;
	} else {
		printf("QP1640OTDRController | ERROR: QPOTDRGetMaxPointSpacings returned error\n");
		ret = -1;
	}
	return ret;
}

int QP1640OTDRController::getPulseWidthIndex(const short pulseWidth, const int flagPulseWidthLowRes, const float wave) {
	int maxPulseWidths = QPOTDRGetMaxPulses(this->otdrId, wave);

	if (maxPulseWidths > 0) {
		DWORD pulseWidths[MAX_PULSES];
		QPOTDRGetAvailPulses(this->otdrId, wave, pulseWidths);
		for (unsigned int i = 0; i < maxPulseWidths; i++) {
			if ((pulseWidths[i] >> 16 == pulseWidth)
				&& ((pulseWidths[i] & 0x00000001) == flagPulseWidthLowRes))
				return i;
		}
		return -1;
	}

	printf("QP1640OTDRController | ERROR: QPOTDRGetMaxPulses returned error\n");
	return -1;
}

BOOL QP1640OTDRController::iorIsValid(const double ior, const float wave) {
	float defaultIOR = QPOTDRGetDefaultIOR(this->otdrId, wave);
	return (((int)ior * 10000) == ((int)defaultIOR * 10000)) ? 1 : 0;
}

int QP1640OTDRController::getAveragesIndex(const double scans, const float wave) {
	int ret;
	int maxAverages = QPOTDRGetMaxAverages(this->otdrId, wave);
	if (maxAverages > 0) {
		DWORD* avergs = new DWORD[MAX_AVERAGES];
		QPOTDRGetAvailAverages(this->otdrId, wave, avergs);
		ret = getIndexInArray((DWORD) scans, avergs, maxAverages);
		delete[] avergs;
	} else {
		printf("QP1640OTDRController | ERROR: QPOTDRGetMaxAverages returned error\n");
		ret = -1;
	}
	return ret;
}

int QP1640OTDRController::getIndexInArray(float val, float* array, int arraySize) {
	int ret = -1;
	if (array != NULL) {
		for (int i = 0; i < arraySize; i++) {
			if (val == array[i]) {
				ret = i;
				break;
			}
		}
	}
	return ret;
}

int QP1640OTDRController::getIndexInArray(DWORD val, DWORD* array, int arraySize) {
	int ret = -1;
	if (array != NULL) {
		for (int i = 0; i < arraySize; i++) {
			if (val == array[i]) {
				ret = i;
				break;
			}
		}
	}
	return ret;
}
