#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <netdb.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include "TransceiverManager.h"

TransceiverManager::TransceiverManager(char* kis_id, char* rtu_hostname, unsigned short rtu_port, unsigned int timeout) {
	this->kis_id = kis_id;
	this->rtu_hostname = rtu_hostname;
	this->rtu_port = rtu_port;
	this->timeout = timeout;
	this->measurementQueue = new MeasurementQueueT();
	this->resultQueue = new ResultQueueT();
}

TransceiverManager::~TransceiverManager() {
	printf("TransceiverManager -- deleting\n");
	this->measurementQueue->clear();
	delete this->measurementQueue;
	this->resultQueue->clear();
	delete this->resultQueue;

	pthread_mutex_destroy(this->tmutex);
	pthread_mutex_destroy(this->rmutex);

	if (this->rtu_comm)
		delete this->rtuTransceiver;
	if (this->agent_comm)
		delete this->agentTransceiver;
}

void TransceiverManager::init() {
//Neither agent nor rtu communications are set up now
	this->rtu_comm = 0;
	this->agent_comm = 0;

//Setup terminal
	termios newtty;
	if (tcgetattr(0, &this->savetty) == -1) {
		perror("tcgetattr() failed");
		exit(-1);
	}
	newtty = this->savetty;
	newtty.c_lflag &= ~ICANON;
	newtty.c_lflag &= ~ECHO;
	newtty.c_cc[VMIN] = 1;
	newtty.c_cc[VTIME] = 0;
	if (tcsetattr(0, TCSAFLUSH, &newtty) == -1) {
		perror("tcsetattr() failed");
		this->shutdown();
	}

//Setup measurement and result mutexes
	this->tmutex = (pthread_mutex_t*)malloc(sizeof(pthread_mutex_t));
	this->rmutex = (pthread_mutex_t*)malloc(sizeof(pthread_mutex_t));
	pthread_mutex_init(this->tmutex, NULL);
	pthread_mutex_init(this->rmutex, NULL);

//Setup RTU connection -- create socket, establish connection, create RTUTransceiver and start it. Else exit.
	if (this->setupRTUConnection()) {
		this->rtu_comm = 1;
//Create and start AgentTransceiver
		this->agentTransceiver = new AgentTransceiver(this->kis_id,
								this->timeout,
								this->measurementQueue,
								this->resultQueue,
								this->tmutex,
								this->rmutex);
		this->agent_comm = 1;

//Run myself
		this->go = 1;
		this->run();
	}
	else
		this->shutdown();
}

int TransceiverManager::setupRTUConnection() {
//Create socket and connect it to rtu
	hostent* he;
	if ((he = gethostbyname(this->rtu_hostname)) == NULL) {
		perror("Cannot resolve hostname");
		printf("TransceiverManager | ERROR: Cannot find host: %s\n", this->rtu_hostname);
		return 0;
	}
	sockaddr_in rtu_addr;
	rtu_addr.sin_family = AF_INET;
	rtu_addr.sin_port = htons(this->rtu_port);
	rtu_addr.sin_addr = *(in_addr*)he->h_addr;
	memset(rtu_addr.sin_zero, '\0', 8);
	int rtu_sockfd;
	if ((rtu_sockfd = socket(AF_INET, SOCK_STREAM, 0)) <= 0) {
		perror("TransceiverManager | ERROR: Cannot create socket");
		return 0;
	}
	printf("TransceiverManager | Connecting to address: %s, port: %hd\n", inet_ntoa(rtu_addr.sin_addr), ntohs(rtu_addr.sin_port));
	if (connect(rtu_sockfd, (sockaddr*)&rtu_addr, sizeof(sockaddr)) == -1) {
		perror("Cannot connect socket to RTU");
		printf("TransceiverManager | ERROR: Cannot connect to address: %s, port: %hd\n", inet_ntoa(rtu_addr.sin_addr), ntohs(rtu_addr.sin_port));
		return 0;
	}
	printf("TransceiverManager | Connected to address: %s, port: %hd\n", inet_ntoa(rtu_addr.sin_addr), ntohs(rtu_addr.sin_port));

//Create and start RTUTransceiver
	this->rtuTransceiver = new RTUTransceiver(rtu_sockfd,
						this->timeout,
						this->measurementQueue,
						this->resultQueue,
						this->tmutex,
						this->rmutex);
	return 1;
}

unsigned int TransceiverManager::getTimeout() const {
	return this->timeout;
}

AgentTransceiver* TransceiverManager::getAgentTransceiver() const {
	return this->agentTransceiver;
}

RTUTransceiver* TransceiverManager::getRTUTransceiver() const {
	return this->rtuTransceiver;
}

void TransceiverManager::do_key(char c) {
	if (c == 'q')
		this->shutdown();
}

// ################### Main loop -- run() #####################
void TransceiverManager::run() {
	timeval tv;
	fd_set in;
	char c;

	while(this->go) {
		printf("TransceiverManager\n");

//Process user commands from terminal
		FD_ZERO(&in);
		FD_SET(0, &in);
		tv.tv_sec = this->timeout;
		tv.tv_usec = 0;
		if (select(1, &in, NULL, NULL, &tv) > 0 && read(0, &c, 1) == 1)
			this->do_key(c);

//If RTU communication is available - process tasks and reports
		if (this->rtu_comm) {
//No processing in case of RTU communication error
			if (this->rtuTransceiver->isCommError()) {
				printf("TransceiverManager | RTU communication error\n");
				pthread_join(this->rtuTransceiver->getThread(), NULL);
				delete this->rtuTransceiver;
				this->rtu_comm = 0;
				continue;
			}
		}

//If RTU disconnected - try to reconnect
		else
			if (this->setupRTUConnection()) {
				printf("TransceiverManager | Reset connection with RTU\n");
				this->rtu_comm = 1;
			}
			else
				printf("TransceiverManager | Cannot reset connection with RTU\n");

//Sleep - commented as 'select' already do it
		//sleep(transceiverManager->timeout);
	}
}

void TransceiverManager::shutdown() {
	printf("\t! TransceiverManager | Shutting down !\n");
	tcsetattr(0, TCSAFLUSH, &this->savetty);
	if (this->rtu_comm) {
		RTUTransceiver::shutdown(this->rtuTransceiver);
		pthread_join(this->rtuTransceiver->getThread(), NULL);
	}
	if (this->agent_comm) {
		AgentTransceiver::shutdown(this->agentTransceiver);
		pthread_join(this->agentTransceiver->getThread(), NULL);
	}
	this->go = 0;
}

