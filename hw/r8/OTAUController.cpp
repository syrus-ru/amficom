//////////////////////////////////////////////////////////////////////
// $Id: OTAUController.cpp,v 1.5 2006/03/24 15:14:35 arseniy Exp $
// 
// Syrus Systems.
// оБХЮОП-ФЕИОЙЮЕУЛЙК ГЕОФТ
// 2004-2005 ЗЗ.
// рТПЕЛФ: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.5 $, $Date: 2006/03/24 15:14:35 $
// $Author: arseniy $
//
// OTAUController.cpp: implementation of the OTAUController class.
//
//////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include <string.h>
#include "OTAUController.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

OTAUController::OTAUController(const COMPortId comPortId,
			const HANDLE comPortHandle,
			const OTAUAddress otauAddress,
			const unsigned int timewait) {
	this->comPortId = comPortId;
	this->comPortHandle = comPortHandle;
	this->otauAddress = otauAddress;

	this->otauUId = createOTAUUid(this->comPortId, this->otauAddress);

	this->timewait = timewait;
	
	this->state = OTAU_STATE_READY;
}

OTAUController::~OTAUController() {
}


COMPortId OTAUController::getCOMPortId() const {
	return this->comPortId;
}

HANDLE OTAUController::getCOMPortHandle() const {
	return this->comPortHandle;
}

OTAUAddress OTAUController::getOTAUAddress() const {
	return this->otauAddress;
}

OTAUUId OTAUController::getOTAUUId() const {
	return this->otauUId;
}

OTAUState OTAUController::getState() const {
	return this->state;
}

BOOL OTAUController::start(const OTAUPortId otauPortId) {
	if (this->state != OTAU_STATE_READY) {
		printf("OTAUController | ERROR: State %d not legal to start data acquisition\n", this->state);
		return FALSE;
	}

	if (otauPortId >= MAX_POSSIBLE_OTAU_PORTS) {
		printf ("OTAUController | OTAU port should be less than %d! Cannot switch OTAU\n", MAX_POSSIBLE_OTAU_PORTS);
		return FALSE;
	}

	this->currentOTAUPort = otauPortId;

	if (!this->switchOTAU()) {
		return FALSE;
	}

	//Создать и запустить главный поток.
	this->running = 1;
	pthread_attr_t pt_attr;
	pthread_attr_init(&pt_attr);
	pthread_attr_setdetachstate(&pt_attr, PTHREAD_CREATE_JOINABLE);
	pthread_create(&this->thread, &pt_attr, OTAUController::run, (void*) this);
	pthread_attr_destroy(&pt_attr);

	/*	Изменить текущее состояние*/
	this->state = OTAU_STATE_RUNNING;

	return TRUE;
}

BOOL OTAUController::switchOTAU() const {
	char* message = new char[OTAU_MESSAGE_SIZE];
	sprintf(message, OTAU_MESSAGE_CONNECT_FORMAT, this->otauAddress, this->currentOTAUPort, this->otauAddress);
	message[OTAU_MESSAGE_SIZE - 1] = 0;

	char* reply = new char[OTAU_REPLY_SIZE];

	printf("OTAUController | Switching OTAU %hd on COM port %hd to port %hd\n", this->otauAddress, this->comPortId, this->currentOTAUPort);
	BOOL ret = OTAUController::sendCOMPortMessage(this->comPortHandle, message, reply, OTAU_REPLY_SIZE);
	delete[] message;
	delete[] reply;

	return ret;
}

void OTAUController::shutdown() {
	this->running = 0;
	printf("OTAUController | Shutting down\n");

	/*	Изменить текущее состояние*/
	this->state = OTAU_STATE_READY;
}




//////////////////////////////////////////////////////////////////////
// Главный цикл
//////////////////////////////////////////////////////////////////////

void* OTAUController::run(void* args) {
	OTAUController* otauController = (OTAUController*) args;

	char* message = new char[OTAU_MESSAGE_SIZE];
	sprintf(message, OTAU_MESSAGE_HOLD_CONNECTION_FORMAT, otauController->otauAddress);
	char* reply = new char[OTAU_REPLY_SIZE];

	const unsigned int twms = otauController->timewait * 1000;	//Перевести в миллисекунды

	unsigned int holdConnectionMessageSentTimeout = 0; //Секунды
	while (otauController->running) {
		if (holdConnectionMessageSentTimeout >= OTAU_HOLD_PORT_TIMEOUT) {
			printf("OTAUController | Sending hold connection command to OTAU %hd on COM port %hd\n", otauController->otauAddress, otauController->comPortId);
			OTAUController::sendCOMPortMessage(otauController->comPortHandle,
				message,
				reply,
				OTAU_REPLY_SIZE);
			holdConnectionMessageSentTimeout = 0;
		}
		holdConnectionMessageSentTimeout += otauController->timewait;
		Sleep(twms);
	}

	sprintf(message, OTAU_MESSAGE_DISCONNECT_FORMAT, otauController->otauAddress);
	printf("OTAUController | Sending disconnect command to OTAU %hd on COM port %hd\n", otauController->otauAddress, otauController->comPortId);
	OTAUController::sendCOMPortMessage(otauController->comPortHandle, message, reply, OTAU_REPLY_SIZE);
	
	delete[] message;
	delete[] reply;
	return NULL;
}




pthread_t OTAUController::getThread() const {
	return this->thread;
}

OTAUUId OTAUController::createOTAUUid(const COMPortId comPortId, const OTAUAddress otauAddress) {
	return (comPortId << 16) + otauAddress;
}

BOOL OTAUController::sendCOMPortMessage(const HANDLE cpHandle,
										const char* message,
										char* reply,
										unsigned int replySize) {
	unsigned int messageSize = strlen(message);
	DWORD bytesSent;
	printf("OTAUController::sendCOMPortMessage | Sending message: %s\n", message);
	WriteFile(cpHandle, message, messageSize, &bytesSent, NULL);
	if (bytesSent != messageSize) {
		printf("OTAUController::sendCOMPortMessage | ERROR: Number of bytes sent %d not is equal to size of message %d\n", bytesSent, messageSize);
		return FALSE;
	}

	if (OTAUController::readCOMPortReply(cpHandle, reply, replySize)) {
		printf("OTAUController::sendCOMPortMessage | Received from COM port: %s\n", reply);
		return TRUE;
	}
	return FALSE;
}

BOOL OTAUController::readCOMPortReply(const HANDLE cpHandle,
									  char* reply,
									  unsigned int replySize) {
	for (unsigned int offset = 0; offset < replySize; offset++) {
		DWORD bytesReceived = 0;
		if (!ReadFile(cpHandle, reply + offset, 1, &bytesReceived, NULL)) {
			perror("ReadFile");
			printf("OTAUController::readCOMPortReply | ERROR: Cannot read from COM port\n");
			return FALSE;
		}
		if (bytesReceived != 1) {
			perror("ReadFile");
			printf("OTAUController::readCOMPortReply | ERROR: Read from COM port not 1 byte: %d\n", bytesReceived);
			return FALSE;
		}
		if (reply[offset] == OTAU_MESSAGE_TERMINATE_CHARACTER) {
			return TRUE;
		}
	}
	printf("OTAUController::readCOMPortReply | ERROR: Probably illegal reply: %s\n", reply);
	return FALSE;
}

OTAUController* OTAUController::createOTAUController(const COMPortId cpId,
													 const HANDLE cpHandle,
													 const OTAUAddress address,
													 const unsigned int timewait) {
	char* message = new char[OTAU_MESSAGE_SIZE];
	sprintf(message, OTAU_MESSAGE_INIT_FORMAT, address);
	message[OTAU_MESSAGE_SIZE - 1] = 0;

	char* reply = new char[OTAU_REPLY_SIZE];

	const BOOL ret = OTAUController::sendCOMPortMessage(cpHandle, message, reply, OTAU_REPLY_SIZE);
	delete[] message;
	delete[] reply;

	return ret ? new OTAUController(cpId, cpHandle, address, timewait) : NULL;
}
