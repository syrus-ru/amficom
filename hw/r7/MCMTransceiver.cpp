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
							   const unsigned int max_mcm_timeout,
							   MeasurementQueueT* measurement_queue,
							   ResultQueueT* result_queue,
							   pthread_mutex_t* tmutex,
							   pthread_mutex_t* rmutex,
							   const unsigned short port) {
	this->timewait = timewait;
	this->max_mcm_timeout = max_mcm_timeout;

	//Очереди запросов на измерение и результатов измерений, флаги синхронизации
	this->measurement_queue = measurement_queue;
	this->result_queue = result_queue;
	this->tmutex = tmutex;
	this->rmutex = rmutex;

	//Необходимо для работы с сокетами
	startup_WSA();

	this->port = port;

	//Открыть сокет на локальной машине для приёма запросов
	this->sockfd = create_listening_socket(port);
	printf("MCMTransceiver | listening socket: %d\n", this->sockfd);


//	unsigned long icmd = 1;
//	int status = ioctlsocket(this->sockfd, FIONBIO, &icmd);
//	if (status != 0)
//		show_error("ioctlsocket");


}

MCMTransceiver::~MCMTransceiver() {
	this->cleanup_socket();
	cleanup_WSA();
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
	MCMTransceiver* mcmTransceiver = (MCMTransceiver*)args;

	SocketConnectionAcceptor* sca = new SocketConnectionAcceptor(mcmTransceiver->sockfd);
	SOCKET csockfd;

	fd_set in;
	timeval tv;
	int sel_ret;

	unsigned int max_empty_count = (unsigned int)(mcmTransceiver->max_mcm_timeout / mcmTransceiver->timewait);
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
				printf("MCMTransceiver.run | Nothing to receive\n");
				empty_count ++;
				if (empty_count >= max_empty_count) {
					printf("MCMTransceiver.run | Timeout %u sec passed, closing communication\n", mcmTransceiver->max_mcm_timeout);
					sca->close_communication();
					empty_count = 0;
				}
			}
			else  {
				if (sel_ret > 0) {
					empty_count = 0;
					rec_ret = receive_segment(csockfd, mcmTransceiver->timewait, segment);
					switch (rec_ret) {
						case RSS_OK:
							mcmTransceiver->process_segment(segment);
							break;
						case RSS_INVALID_SOCKET:
							break;
						case RSS_CANNOT_READ_HEADER:
						case RSS_CANNOT_READ_DATA:
						case RSS_ILLEGAL_HEADER:
							sca->close_communication();
							break;
						default:
							printf("MCMTransceiver.run | Illegal return value of receive_segment: %d\n", rec_ret);
					}
				}
				else {
					show_error("select");
					sleep_sec(mcmTransceiver->timewait);
				}
			}


			//Transmitter part
			pthread_mutex_lock(mcmTransceiver->rmutex);
			if (! mcmTransceiver->result_queue->empty()) {
				FD_ZERO(&in);
				FD_SET(csockfd, &in);
				tv.tv_sec = mcmTransceiver->timewait;
				tv.tv_usec = 0;

				sel_ret = select(csockfd + 1, NULL, &in, NULL, &tv);

				if (sel_ret > 0) {
					segment = (Segment*)mcmTransceiver->result_queue->back();
					mcmTransceiver->result_queue->pop_back();
					pthread_mutex_unlock(mcmTransceiver->rmutex);
					wrt_ret = transmit_segment(csockfd, mcmTransceiver->timewait, segment);
					switch (wrt_ret) {
						case WSS_OK:
							printf("MCMTransceiver | Successfully transferred result segment\n");
							delete (ResultSegment*)segment;
							break;
						case WSS_CANNOT_WRITE_DATA:
							sca->close_communication();
						case WSS_INVALID_SOCKET:
							printf("MCMTransceiver | Cannot transmit result segment -- pushing it back to queue\n");
							pthread_mutex_lock(mcmTransceiver->rmutex);
							mcmTransceiver->result_queue->push_back((ResultSegment*)segment);
							pthread_mutex_unlock(mcmTransceiver->rmutex);
							break;
						default:
							printf("MCMTransceiver.run | Illegal return value of transmit_segment: %d\n", wrt_ret);
					}
				}
				else {
					if (sel_ret == 0) {
						pthread_mutex_unlock(mcmTransceiver->rmutex);
						printf("MCMTransceiver.run | Cannot transmit now\n");
					}
					else {
						show_error("select");
						pthread_mutex_unlock(mcmTransceiver->rmutex);
						sleep_sec(mcmTransceiver->timewait);
					}
				}

			}//if (! mcmTransceiver->result_queue->empty())
			else {
				pthread_mutex_unlock(mcmTransceiver->rmutex);
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
/*
void* MCMTransceiver::run(void* args) {
	MCMTransceiver* mcmTransceiver = (MCMTransceiver*)args;

	int r;
	Segment* segment = NULL;
	while (mcmTransceiver->running) {

		if (mcmTransceiver->sockfd != SOCKET_INVALID) {

			//Receiver part
			r = receive_segment(mcmTransceiver->sockfd, mcmTransceiver->timewait, segment, 1);
			if (r == 1) {
				switch (segment->getType()) {
					case SEGMENT_MEASUREMENT:
						printf("MCMTransceiver | Received measurement segment -- adding to queue\n");
						pthread_mutex_lock(mcmTransceiver->tmutex);
						mcmTransceiver->measurement_queue->push_front((MeasurementSegment*)segment);
						pthread_mutex_unlock(mcmTransceiver->tmutex);
						break;
					default:
						printf("MCMTransceiver | Nothing to do with segment of type %d\n", segment->getType());
				}
			}
			else {
				if (r == 0)
					printf("MCMTransceiver | Nothing received\n");
				else {
					printf("MCMTransceiver | receive returned error\n");
					mcmTransceiver->cleanup_socket();
				}
			}

			//Transmitter part
			pthread_mutex_lock(mcmTransceiver->rmutex);
			if (! mcmTransceiver->result_queue->empty()) {
				segment = (Segment*)mcmTransceiver->result_queue->back();
				mcmTransceiver->result_queue->pop_back();
				pthread_mutex_unlock(mcmTransceiver->rmutex);
				if (transmit_segment(mcmTransceiver->sockfd, segment)) {
					printf("MCMTransceiver | Successfully transferred result segment\n");
					delete (ResultSegment*)segment;
				}
				else {
					printf("MCMTransceiver | Cannot transmit result segment -- pushing it back to queue\n");
					pthread_mutex_lock(mcmTransceiver->rmutex);
					mcmTransceiver->result_queue->push_back((ResultSegment*)segment);
					pthread_mutex_unlock(mcmTransceiver->rmutex);
				}
			}//if (! mcmTransceiver->result_queue->empty())
			else {
				pthread_mutex_unlock(mcmTransceiver->rmutex);
				printf("MCMTransceiver | No results in queue\n");
			}

		}//if (this->sockfd != SOCKET_INVALID)
		else {
			printf("MCMTrancseiver | local socket is invalid; trying to create a new one\n");
			mcmTransceiver->sockfd = create_listening_socket(mcmTransceiver->port);
		}//else if (this->sockfd != SOCKET_INVALID)
	
	}

	return NULL;
}*/
//##########################################################################


void MCMTransceiver::process_segment(Segment* segment) const {
	int segment_type = segment->getType();
	switch (segment_type) {
		case SEGMENT_MEASUREMENT:
			printf("MCMTransceiver | Received measurement segment -- adding to queue\n");
			pthread_mutex_lock(this->tmutex);
			this->measurement_queue->push_front((MeasurementSegment*)segment);
			pthread_mutex_unlock(this->tmutex);
			break;
		default:
			printf("MCMTransceiver | Nothing to do with segment of type %d\n", segment_type);
	}
}

void MCMTransceiver::shutdown() {
	printf("MCMTransceiver | Shutting down\n");
	this->running = 0;
}

pthread_t MCMTransceiver::get_thread() const {
	return this->thread;
}

unsigned int MCMTransceiver::get_timewait() const {
	return this->timewait;
}

unsigned int MCMTransceiver::get_max_mcm_timeout() const {
	return this->max_mcm_timeout;
}

void MCMTransceiver::cleanup_socket() {
	close_socket(this->sockfd);
	this->sockfd = INVALID_SOCKET;
}

int MCMTransceiver::startup_WSA() {
	WSAData wsadata;
	return (WSAStartup(MAKEWORD(2, 2), &wsadata) == 0);
}

void MCMTransceiver::cleanup_WSA() {
	WSACleanup();
}
