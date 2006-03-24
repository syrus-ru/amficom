//////////////////////////////////////////////////////////////////////
// $Id: MCMTransceiver.cpp,v 1.3 2006/03/24 12:29:26 arseniy Exp $
// 
// Syrus Systems.
// оБХЮОП-ФЕИОЙЮЕУЛЙК ГЕОФТ
// 2004-2005 ЗЗ.
// рТПЕЛФ: r8
//////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
// $Revision: 1.3 $, $Date: 2006/03/24 12:29:26 $
// $Author: arseniy $
//
// MCMTransceiver.cpp: implementation of the MCMTransceiver class.
//
//////////////////////////////////////////////////////////////////////

#include "MCMTransceiver.h"
#include "SocketReader.h"
#include "SocketConnectionAcceptor.h"
#include "akptcp.h"
#include <stdio.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

MCMTransceiver::MCMTransceiver(const unsigned int timewait,
							   const unsigned int maxMCMTimeout,
							   SegmentProcessor* segmentProcessor,
							   const unsigned short port) {
	this->segmentProcessor = segmentProcessor;

	this->timewait = timewait;
	this->maxMCMTimeout = maxMCMTimeout;

	//Необходимо для работы с сокетами
	startupWSA();

	this->port = port;

	//Открыть сокет на локальной машине для приёма запросов
	this->sockfd = create_listening_socket(port);
	printf("MCMTransceiver | listening socket: %d\n", this->sockfd);


//	unsigned long icmd = 1;
//	int status = ioctlsocket(this->sockfd, FIONBIO, &icmd);
//	if (status != 0) {
//		show_error("ioctlsocket");
//	}


}

MCMTransceiver::~MCMTransceiver() {
	this->cleanupSocket();
	cleanupWSA();
}

void MCMTransceiver::start() {
	//Создать и запустить главный поток.
	this->running = 1;
	pthread_attr_t pt_attr;
	pthread_attr_init(&pt_attr);
	pthread_attr_setdetachstate(&pt_attr, PTHREAD_CREATE_JOINABLE);
	pthread_create(&this->thread, &pt_attr, MCMTransceiver::run, (void*)this);
	pthread_attr_destroy(&pt_attr);
}


//########################### Main loop -- run #############################
//TODO: Сделать отрубание МУИ по прошествии некоторого промежутка времени.
void* MCMTransceiver::run(void* args) {
	const MCMTransceiver* mcmTransceiver = (MCMTransceiver*) args;

	SocketConnectionAcceptor* sca = new SocketConnectionAcceptor(mcmTransceiver->sockfd);
	SOCKET csockfd;

	fd_set in;
	timeval tv;
	int sel_ret;

	unsigned int max_empty_count = (unsigned int)(mcmTransceiver->maxMCMTimeout / mcmTransceiver->timewait);
	unsigned int empty_count = 0;

	ReadSegmentStatus rec_ret;
	WriteSegmentStatus wrt_ret;
	Segment* segment;

	while (mcmTransceiver->running) {
		if (sca->communication_available()) {
			
			csockfd = sca->get_communication_socket();

			//Receiver part
			//Check for available segment to read
			FD_ZERO(&in);
			FD_SET(csockfd, &in);
			tv.tv_sec = mcmTransceiver->timewait;
			tv.tv_usec = 0;

			sel_ret = select(csockfd + 1, &in, NULL, NULL, &tv);

			if (sel_ret == 0) {
				printf("MCMTransceiver | Nothing to receive\n");
				empty_count ++;
				if (empty_count >= max_empty_count) {
					printf("MCMTransceiver | Timeout %u sec passed, closing communication\n", mcmTransceiver->maxMCMTimeout);
					sca->close_communication();
					empty_count = 0;
				}
			}
			else  {
				if (sel_ret > 0) {
					printf("MCMTransceiver | Receiving segment\n");
					empty_count = 0;
					rec_ret = receive_segment(csockfd, mcmTransceiver->timewait, segment);
					switch (rec_ret) {
						case RSS_OK:
							mcmTransceiver->segmentProcessor->addTaskSegment(segment);
							break;
						case RSS_INVALID_SOCKET:
							break;
						case RSS_CANNOT_READ_HEADER:
						case RSS_CANNOT_READ_DATA:
						case RSS_ILLEGAL_HEADER:
							sca->close_communication();
							break;
						default:
							printf("MCMTransceiver | Illegal return value of receive_segment: %d\n", rec_ret);
					}
				}
				else {
					show_error("select");
					sleep_sec(mcmTransceiver->timewait);
				}
			}


			//Transmitter part
			const Segment* resultSegment = mcmTransceiver->segmentProcessor->removeReportSegment();
			if (resultSegment != NULL) {
				FD_ZERO(&in);
				FD_SET(csockfd, &in);
				tv.tv_sec = mcmTransceiver->timewait;
				tv.tv_usec = 0;

				sel_ret = select(csockfd + 1, NULL, &in, NULL, &tv);
				if (sel_ret > 0) {
					wrt_ret = transmit_segment(csockfd,
						mcmTransceiver->timewait,
						(Segment*) resultSegment);
					switch (wrt_ret) {
						case WSS_OK:
							printf("MCMTransceiver | Successfully transferred result segment\n");
							delete resultSegment;
							break;
						case WSS_CANNOT_WRITE_DATA:
							sca->close_communication();
						case WSS_INVALID_SOCKET:
							printf("MCMTransceiver | Cannot transmit result segment -- pushing it back to queue\n");
							mcmTransceiver->segmentProcessor->reAddReportSegment(resultSegment);
							break;
						default:
							printf("MCMTransceiver | Illegal return value of transmit_segment: %d\n", wrt_ret);
					}
				}
				else {
					if (sel_ret == 0) {
						printf("MCMTransceiver | Cannot transmit now\n");
					}
					else {
						show_error("select");
						sleep_sec(mcmTransceiver->timewait);
					}
					printf("MCMTransceiver | pushing result segment back to queue\n");
					mcmTransceiver->segmentProcessor->reAddReportSegment(resultSegment);
				}
			}//if (segment != NULL)
			else {
				printf("MCMTransceiver | No results in queue\n");
			}
		}
		else {
			//Wait for connection from MCM
			sca->accept_connection(mcmTransceiver->timewait);
		}
	}

	return NULL;
}

//##########################################################################

void MCMTransceiver::shutdown() {
	printf("MCMTransceiver | Shutting down\n");
	this->running = 0;
}

pthread_t MCMTransceiver::getThread() const {
	return this->thread;
}

unsigned int MCMTransceiver::getMaxMCMTimeout() const {
	return this->maxMCMTimeout;
}

void MCMTransceiver::cleanupSocket() {
	close_socket(this->sockfd);
	this->sockfd = INVALID_SOCKET;
}

int MCMTransceiver::startupWSA() {
	WSAData wsadata;
	return (WSAStartup(MAKEWORD(2, 2), &wsadata) == 0);
}

void MCMTransceiver::cleanupWSA() {
	WSACleanup();
}

/*
void MCMTransceiver::printMeasurementSegment(MeasurementSegment* measurement_segment) {
	int i;

	char* data = measurement_segment->getData();
	unsigned int length = measurement_segment->getLength();
	for (i = 0; i < length; i++)
		printf("[%d] == %d\n", i, data[i]);

	char* measurement_id = measurement_segment->getMeasurementId()->getData();
	char* measurement_type_id = measurement_segment->getMeasurementTypeId()->getData();
	char* local_address = measurement_segment->getLocalAddress()->getData();
	unsigned int parnumber = measurement_segment->getParnumber();
	printf("measurement_id: '%s'\nmeasurement_type_id: '%s'\nlocal_address: '%s'\nparnumber: %d\n",
		measurement_id,
		measurement_type_id,
		local_address,
		parnumber);

	Parameter** parameters = measurement_segment->getParameters();
	Parameter* par;
	char* par_name;
	char* par_value;
	unsigned int value_size;
	for (i = 0; i < parnumber; i++) {
		par = parameters[i];
		par_name = par->getName()->getData();
		par_value = par->getValue()->getData();
		value_size = par->getValue()->getLength();
		printf("\tname: '%s'\n", par_name);
		printf("\tvalue:");
		for (int j = 0; j < value_size; j++)
			printf(" %d", par_value[j]);
		printf("\n");
	}
}*/
