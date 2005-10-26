//////////////////////////////////////////////////////////////////////
// $Id: RTU.cpp,v 1.7 2005/10/26 15:07:44 arseniy Exp $
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
// RTU.cpp: implementation of the RTU class.
//
//////////////////////////////////////////////////////////////////////

#include <windows.h>
#include "RTU.h"

BOOL initPK7600Cards(OTDRReportListener* otdrReportListener,
			OTDRContainer* otdrContainer,
			const unsigned int timewait);
BOOL initQP1640Cards(OTDRReportListener* otdrReportListener,
			OTDRContainer* otdrContainer,
			const unsigned int timewait);

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

RTU::RTU(const unsigned short comPortNumber, const unsigned int timewait) {
	this->comPortNumber = comPortNumber;

	this->tMutex = (pthread_mutex_t*) malloc(sizeof(pthread_mutex_t));
	pthread_mutex_init(this->tMutex, NULL);
	this->rMutex = (pthread_mutex_t*) malloc(sizeof(pthread_mutex_t));
	pthread_mutex_init(this->rMutex, NULL);

	this->timewait = timewait;

	if (initPK7600Cards(this, this, this->timewait)) {
		this->state = RTU_STATE_OTDR_INITIALIZED;
	} else {
		this->state = RTU_STATE_OTDR_INIT_FAILED;
	}

	if (initQP1640Cards(this, this, this->timewait)) {
		this->state = RTU_STATE_OTDR_INITIALIZED;
	} else {
		this->state = RTU_STATE_OTDR_INIT_FAILED;
	}

	//TODO: Init OTDR cards of other type

	if (this->otdrControllersMap.empty()) {
		printf("RTU | ERROR: Found none OTDR cards. Exiting.\n");
		this->state = RTU_STATE_OTDR_INIT_FAILED;
		return;
	}

	this->initializeCOMPorts();
	if (this->comPortHandlesMap.empty()) {
		printf("RTU | ERROR: Found none COM ports. Exiting.\n");
		this->state = RTU_STATE_COM_PORT_INIT_FAILED;
		return;
	}
	this->state = RTU_STATE_COM_PORT_INITIALIZED;

	printf("RTU | Initialization completed\n");
}

RTU::~RTU() {
	if (this->state > RTU_STATE_COM_PORT_INIT_FAILED) {
		this->freeCOMPorts();
	}

	if (this->state > RTU_STATE_OTDR_INIT_FAILED) {
		this->freeOTDRs();
	}

	this->measurementQueue.clear();
	this->resultQueue.clear();

	pthread_mutex_destroy(this->tMutex);
	free(this->tMutex);
	pthread_mutex_destroy(this->rMutex);
	free(this->rMutex);
}



void RTU::registerOTDRController(const OTDRController* otdrController) {
	this->otdrControllersMap[otdrController->getOTDRId()] = otdrController;
}



OTAUController* RTU::prepareOTAUController(const COMPortId comPortId,
										   const OTAUAddress otauAddress) {
	const OTAUUId otauUId = OTAUController::createOTAUUid(comPortId, otauAddress);
	OTAUController* otauController = (OTAUController*) this->otauControllersMap[otauUId];
	if (otauController != NULL) {
		printf("RTU | Found OTAU in list of ready\n");
		return otauController;
	}
	
	this->otauControllersMap.erase(otauUId);

	const HANDLE comPortHandle = this->comPortHandlesMap[comPortId];
	if (comPortHandle == NULL) {
		printf("RTU | ERROR: COM port %d is not registered\n", comPortId);
		this->comPortHandlesMap.erase(comPortId);
		return NULL;
	}

	otauController = OTAUController::createOTAUController(comPortId,
		comPortHandle,
		otauAddress,
		this->timewait);
	if (otauController != NULL) {
		this->otauControllersMap[otauUId] = otauController;
	}
	return otauController;
}



void RTU::addTaskSegment(const Segment* segment) {
	switch (segment->getType()) {
		case SEGMENT_MEASUREMENT:
			printf("RTU | Adding measurement segment\n");
			this->pushFrontMeasurementSegment((MeasurementSegment*) segment);
			break;
	}
}

const Segment* RTU::removeReportSegment() {
	return this->popBackResultSegment();
}

void RTU::reAddReportSegment(const Segment* segment) {
	switch (segment->getType()) {
		case SEGMENT_RESULT:
			this->pushFrontResultSegment((ResultSegment*) segment);
			break;
	}
}



void RTU::acceptOTDRReport(const Segment* segment) {
	switch (segment->getType()) {
		case SEGMENT_RESULT:
			this->pushFrontResultSegment((ResultSegment*) segment);
			break;
	}
}



void RTU::pushFrontMeasurementSegment(const MeasurementSegment* measurementSegment) {
	pthread_mutex_lock(this->tMutex);
	this->measurementQueue.push_front(measurementSegment);
	pthread_mutex_unlock(this->tMutex);
}

const ResultSegment* RTU::popBackResultSegment() {
	const ResultSegment* resultSegment;
	pthread_mutex_lock(this->rMutex);
	if (!this->resultQueue.empty()) {
		resultSegment = this->resultQueue.back();
		this->resultQueue.pop_back();
	} else {
		resultSegment = NULL;
	}
	pthread_mutex_unlock(this->rMutex);
	return resultSegment;
}

void RTU::pushFrontResultSegment(const ResultSegment* resultSegment) {
	pthread_mutex_lock(this->rMutex);
	this->resultQueue.push_front(resultSegment);
	pthread_mutex_unlock(this->rMutex);
}

RTUState RTU::getRTUState() const {
	return this->state;
}

unsigned int RTU::getTimewait() const {
	return this->timewait;
}


void RTU::start() {
	this->running = 1;
	pthread_attr_t pt_attr;
	pthread_attr_init(&pt_attr);
	pthread_attr_setdetachstate(&pt_attr, PTHREAD_CREATE_JOINABLE);
	pthread_create(&this->thread, &pt_attr, RTU::run, (void*) this);
	pthread_attr_destroy(&pt_attr);
	this->state = RTU_STATE_RUNNING;
}

void RTU::shutdown() {
	printf("RTU | Shutting down\n");
	this->running = 0;
}

pthread_t RTU::getThread() const {
	return this->thread;
}


//////////////////////////////////////////////////////////////////////
// Главный цикл
//////////////////////////////////////////////////////////////////////

void* RTU::run(void* args) {
	RTU* rtu = (RTU*) args;
	const unsigned int twms = rtu->timewait * 1000;	//Перевести в миллисекунды

	while (rtu->running) {
		Sleep(twms);

		pthread_mutex_lock(rtu->tMutex);
		printf ("RTU | %u measurements in queue\n", rtu->measurementQueue.size());
		if (!rtu->measurementQueue.empty()) {
			const MeasurementSegment* measurementSegment = rtu->measurementQueue.back();
			rtu->measurementQueue.pop_back();
			pthread_mutex_unlock(rtu->tMutex);

			const ByteArray* localAddress = measurementSegment->getLocalAddress();
			OTDRId otdrId;
			COMPortId comPortId;
			OTAUAddress otauAddress;
			OTAUPortId otauPortId;
			if (!RTU::parseLocalAddress(localAddress, otdrId, comPortId, otauAddress, otauPortId)) {
				printf("RTU | ERROR: Failed parse local address; abort measurement\n");
				delete measurementSegment;
				//TODO: Create special segment to report to MCM.
				continue;
			}

			OTDRController* otdrController = (OTDRController*) rtu->otdrControllersMap[otdrId];
			if (otdrController == NULL) {
				printf("RTU | ERROR: OTDR controller for card %hd not found; abort measurement\n", otdrId);
				rtu->otdrControllersMap.erase(otdrId);
				delete measurementSegment;
				//TODO: Create special segment to report to MCM.
				continue;
			}
			const OTDRState otdrState = otdrController->getState();
			if (otdrState != OTDR_STATE_READY) {
				printf("RTU | State %d of OTDR controller for card %hd is not legal to start acquisition; will try later\n", otdrState, otdrId);
				rtu->pushFrontMeasurementSegment(measurementSegment);
				continue;
			}

			OTAUController* otauController = rtu->prepareOTAUController(comPortId, otauAddress);
			if (otauController == NULL) {
				printf("RTU | ERROR: OTAU controller for COM port %hd and address %hd not found; abort measurement\n", comPortId, otauAddress);
				delete measurementSegment;
				//TODO: Create special segment to report to MCM.
				continue;
			}
			const OTAUState otauState = otauController->getState();
			if (otauState != OTAU_STATE_READY) {
				printf("RTU | State %d of OTAU controller on port %hd and address %hd is not legal to start acquisition; will try later\n", otauState, comPortId, otauAddress);
				rtu->pushFrontMeasurementSegment(measurementSegment);
				continue;
			}

			const Parameter** parameters = (const Parameter**) measurementSegment->getParameters();
			const unsigned int parNumber = measurementSegment->getParnumber();
			if (!otdrController->setMeasurementParameters(parameters, parNumber)) {
				printf("RTU | ERROR: Cannot set measurement parameters; abort measurement\n");
				delete measurementSegment;
				//TODO: Create special segment to report to MCM.
				continue;
			}

			if (!otauController->start(otauPortId)) {
				printf("RTU | ERROR: Cannot switch OTAU. Measurement cancelled\n");
				delete measurementSegment;
				//TODO: Create special segment to report to MCM.
				continue;
			}

			ByteArray* measurementId = measurementSegment->getMeasurementId()->clone();
			otdrController->start(measurementId, otauController);

			delete measurementSegment;

		} else {
			pthread_mutex_unlock(rtu->tMutex);
		}
	}

	return NULL;
}

BOOL RTU::parseLocalAddress(const ByteArray* localAddress,
			OTDRId& otdrId,
			COMPortId& comPortId,
			OTAUAddress& otauAddress,
			OTAUPortId& otauPortId) {
	int ret = sscanf(localAddress->getData(),
		LOCAL_ADDRESS_FORMAT,
		otdrId,
		comPortId,
		otauAddress,
		otauPortId);
	return (ret == 4);
}


//////////////////////////////////////////////////////////////////////
// Управление последовательными портами
//////////////////////////////////////////////////////////////////////
void RTU::freeOTDRs() {
	for (OTDRControllerMapT::iterator it = this->otdrControllersMap.begin();
			it != this->otdrControllersMap.end();
			it++) {
		OTDRControllerMapT::value_type vt = *it;
		OTDRId otdrId = vt.first;
		const OTDRController* otdrController = vt.second;
		delete otdrController;
	}
	this->otdrControllersMap.clear();
}



//////////////////////////////////////////////////////////////////////
// Управление последовательными портами
//////////////////////////////////////////////////////////////////////

BOOL RTU::initializeCOMPorts() {
	const int COM_PORT_FILE_NAME_SIZE = 6;
	const char* COM_PORT_FILE_NAME = "COM";

	char* comPortFileName = new char[COM_PORT_FILE_NAME_SIZE];
	memset(comPortFileName, 0, COM_PORT_FILE_NAME_SIZE * sizeof(char));

	const int pos = sprintf(comPortFileName, "%s", COM_PORT_FILE_NAME);
	//номер порта отсчитывается с единицы
	for (COMPortId comPortId = 1; comPortId <= this->comPortNumber; comPortId++) {
		sprintf(comPortFileName + pos, "%u:", comPortId);
		printf("RTU | Probing port %s\n", comPortFileName);
		const HANDLE comPortHandle  = CreateFile(comPortFileName,
			GENERIC_READ | GENERIC_WRITE,
			0,
			NULL,
			OPEN_EXISTING,
			0,
			NULL);
		if (comPortHandle == INVALID_HANDLE_VALUE) {
			perror("CreateFile");
			printf("RTU | ERROR: Cannot connect to port %s\n", comPortFileName);
			continue;
		}

		printf("RTU | Configuring port properties and timeouts for %s\n", comPortFileName);
		if (this->setCOMPortProperties(comPortHandle)) {
			printf("RTU | Adding port %s\n", comPortFileName);
			this->comPortHandlesMap[comPortId] = comPortHandle;
		} else {
			printf("RTU | Failed to configure port %s\n", comPortFileName);
		}

	}

	delete[] comPortFileName;
	return TRUE;
}

BOOL RTU::setCOMPortProperties(const HANDLE comPortHandle) {
	DCB dcb;

	dcb.DCBlength = sizeof(DCB); // size of DCB

	// Получить предопределённые по умолчанию настройки.
	GetCommState(comPortHandle, &dcb);

	// Создать новые настройки
	dcb.BaudRate = CBR_9600;			// Current baud 
	dcb.fBinary = TRUE;					// Binary mode; no EOF check 
	dcb.fParity = FALSE;				// Enable parity checking ?
	dcb.fOutxCtsFlow = FALSE;			// No CTS output flow control 
	dcb.fOutxDsrFlow = FALSE;			// No DSR output flow control 
	dcb.fDtrControl = DTR_CONTROL_ENABLE;// DTR flow control type 
	dcb.fDsrSensitivity = TRUE;			// DSR sensitivity 
	dcb.fTXContinueOnXoff = FALSE;		// XOFF continues Tx 
	dcb.fOutX = FALSE;					// No XON/XOFF out flow control 
	dcb.fInX = FALSE;					// No XON/XOFF in flow control 
	dcb.fErrorChar = FALSE;				// Disable error replacement 
	dcb.fNull = FALSE;					// Disable null stripping 
	dcb.fRtsControl = RTS_CONTROL_ENABLE;// RTS flow control 
	dcb.fAbortOnError = FALSE;			// Do not abort reads/writes on error
	dcb.ByteSize = 8;					// Number of bits/byte, 4-8 
	dcb.Parity = NOPARITY;				// 0-4=no,odd,even,mark,space 
	dcb.StopBits = ONESTOPBIT;			// 0,1,2 = 1, 1.5, 2 

	// Применить новые настройки
	if (!SetCommState(comPortHandle, &dcb)) {
		printf ("OTAUController | Unable to configure the serial port\n");
		return FALSE;
	}
	
	COMMTIMEOUTS port_time_outs;
	GetCommTimeouts(comPortHandle, &port_time_outs);
	port_time_outs.ReadTotalTimeoutConstant = COM_PORT_READ_TIMEOUT;
	if (!SetCommTimeouts(comPortHandle, &port_time_outs)) {
		printf ("OTAUController | Unable to set timeouts for the serial port\n");
		return FALSE;
	}

	return TRUE;
}

void RTU::freeCOMPorts() {
	for (COMPortHandleMapT::iterator it = this->comPortHandlesMap.begin();
			it != this->comPortHandlesMap.end();
			it++) {
		COMPortHandleMapT::value_type vt = *it;
		const COMPortId comPortId = vt.first;
		HANDLE comPortHandle = vt.second;
		printf("RTU | Closing COM port: %d\n", comPortId);
		CloseHandle(comPortHandle);
	}
	for (OTAUControllerMapT::iterator ocmIt = this->otauControllersMap.begin();
			ocmIt != this->otauControllersMap.end();
			ocmIt++) {
		OTAUControllerMapT::value_type vt = *ocmIt;
		const OTAUUId otauUId = vt.first;
		const OTAUController* otauController = vt.second;
		printf("Deleting OTAU Controller: %hd on COM port %hd\n",
			otauController->getOTAUAddress(),
			otauController->getCOMPortId());
		delete otauController;
	}
}

