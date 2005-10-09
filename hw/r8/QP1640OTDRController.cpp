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

	free(qpPluginData);
}

OTDRModel QP1640OTDRController::getOTDRModel() const {
	return OTDR_MODEL_ONT_UT;
}

void QP1640OTDRController::printAvailableParameters() const {
	printf("QP1640OTDRController | QP1640 card N %d", this->otdrId);
}
