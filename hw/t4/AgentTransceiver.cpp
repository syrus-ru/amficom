#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <fcntl.h>
#include "AgentTransceiver.h"
#include <errno.h>

AgentTransceiver::AgentTransceiver(char* kis_id,
				unsigned int timeout,
				MeasurementQueueT* measurementQueue,
				ResultQueueT* resultQueue,
				pthread_mutex_t* tmutex,
				pthread_mutex_t* rmutex) {
//Create task FIFO 
	this->taskFifoName = new char[MAXLENPATH];
	strcpy(this->taskFifoName, (const char*)FIFOROOTPATH);
	strcat(this->taskFifoName, "/task");
	strcat(this->taskFifoName, (const char*)kis_id);
	strcat(this->taskFifoName, (const char*)getenv("USER"));
	mknod(this->taskFifoName, S_IFIFO|0600, 0);

//Create report FIFO
	this->reportFifoName = new char[MAXLENPATH];
	strcpy(this->reportFifoName, (const char*)FIFOROOTPATH);
	strcat(this->reportFifoName, "/report");
	strcat(this->reportFifoName, (const char*)kis_id);
	strcat(this->reportFifoName, (const char*)getenv("USER"));
	mknod(this->reportFifoName, S_IFIFO|0600, 0);

//Set timeout
	this->timeout = timeout;

//Setup Measurement and Result Queues
	this->measurementQueue = measurementQueue;
	this->resultQueue = resultQueue;
	this->tmutex = tmutex;
	this->rmutex = rmutex;

//Create thread
	this->go = 1;
	pthread_attr_t pt_attr;
	pthread_attr_init(&pt_attr);
	pthread_attr_setdetachstate(&pt_attr, PTHREAD_CREATE_JOINABLE);
	pthread_create(&this->thread, &pt_attr, AgentTransceiver::run, (void*)this);
	pthread_attr_destroy(&pt_attr);
}

AgentTransceiver::~AgentTransceiver() {
	printf("AgentTransceiver -- deleting\n");
	delete[] this->taskFifoName;
	delete[] this->reportFifoName;
}

char* AgentTransceiver::getTaskFifoName() const {
	return this->taskFifoName;
}

char* AgentTransceiver::getReportFifoName() const {
	return this->reportFifoName;
}

unsigned int AgentTransceiver::getTimeout() const {
	return this->timeout;
}

pthread_t AgentTransceiver::getThread() const {
	return this->thread;
}

// ################### Main loop -- run() #####################
void* AgentTransceiver::run(void* args) {
	AgentTransceiver* agentTransceiver = (AgentTransceiver*)args;

	int fd;
	timeval tv;
	fd_set in;
	int sel_ret;
	char* buffer;
	unsigned int length;
	char* trdata;
	MeasurementSegment* measurementSegment;
	ResultSegment* resultSegment;

	while (agentTransceiver->go) {
		printf("AgentTransceiver\n");
		//perror("0");

//Open task FIFO
		if ((fd = open(agentTransceiver->taskFifoName, O_RDONLY | O_NONBLOCK)) <= 0) {
			perror("AgentTransceiver -- task | ERROR -- open");
//			sleep(1);
			continue;
		}
		FD_ZERO(&in);
		FD_SET(fd, &in);

//Sleep -- need here for normal processing of 'select' - commented as 'select' already do it
//		sleep(agentTransceiver->timeout);

//Check task FIFO descriptor for read event and read a task
		tv.tv_sec = agentTransceiver->timeout;
		tv.tv_usec = 0;
		sel_ret = select(fd + 1, &in, NULL, NULL, &tv);
		if (sel_ret > 0) {
			buffer = new char[MAXLENTASK];
			if (read(fd, buffer, MAXLENTASK) < 1) {
				perror("AgentTransceiver | ERROR -- read");
				close(fd);
				delete[] buffer;
				continue;
			}
			length = *(unsigned int*)buffer;
			printf("AgentTransceiver |  Read byte array of length %d\n", length);
			if (length + INTSIZE > MAXLENTASK) {
				printf("AgentTransceiver | ERROR: Inadequate length of received array: %d; must be less or equal %d\n", length, MAXLENTASK - INTSIZE);
				close(fd);
				delete[] buffer;
				continue;
			}
			trdata = new char[length];
			memcpy(trdata, buffer + INTSIZE, length);
			delete[] buffer;

//Create and process MeasurementSegment
			measurementSegment = new MeasurementSegment(length, trdata);
			trdata = NULL;
			pthread_mutex_lock(agentTransceiver->tmutex);
			agentTransceiver->measurementQueue->push_front(measurementSegment);
			pthread_mutex_unlock(agentTransceiver->tmutex);
		}
		else
			if (sel_ret < 0) {
				perror("AgentTransceiver | ERROR -- select");
				close(fd);
				continue;
			}

//Close task FIFO
		if (close(fd) < 0)
			perror("AgentTransceiver -- task | ERROR -- close");


//Check if reports are available and push them to report FIFO
//NOTE: deleting resultSegment, which is allocated in another place!!!
		pthread_mutex_lock(agentTransceiver->rmutex);
		if (!agentTransceiver->resultQueue->empty()) {
			pthread_mutex_unlock(agentTransceiver->rmutex);
			resultSegment = agentTransceiver->resultQueue->back();
			length = resultSegment->getLength();
			if (length + INTSIZE > MAXLENREPORT) {
				printf("AgentTransceiver | ERROR: Inadequate length of pushing array: %d; must be <= %d\n", length, MAXLENREPORT - INTSIZE);
				pthread_mutex_lock(agentTransceiver->rmutex);
				agentTransceiver->resultQueue->pop_back();
				pthread_mutex_unlock(agentTransceiver->rmutex);
				delete resultSegment;	//NOTE: Here !!!
				continue;
			}
			buffer = new char[MAXLENREPORT];
			memcpy(buffer, (char*)&length, INTSIZE);
			memcpy(buffer + INTSIZE, resultSegment->getData(), length);

//Open report FIFO
			if ((fd = open(agentTransceiver->reportFifoName, O_WRONLY)) <= 0) {
				perror("AgentTransceiver -- report | ERROR -- open");
				delete[] buffer;
				continue;
			}
			if (write(fd, buffer, MAXLENREPORT) < 1) {
				perror("AgentTransceiver | ERROR -- write");
				close(fd);
				delete[] buffer;
				continue;
			}
			if (close(fd) < 0)
				perror("AgentTransceiver -- report | ERROR -- close");
			delete[] buffer;

//Remove pushed ResultSegment from queue and delete it
			pthread_mutex_lock(agentTransceiver->rmutex);
			agentTransceiver->resultQueue->pop_back();
			pthread_mutex_unlock(agentTransceiver->rmutex);
			delete resultSegment;	//NOTE: Here !!!
			printf("AgentTransceiver | Successfully pushed array of length %d\n", length);
		}
		else
			pthread_mutex_unlock(agentTransceiver->rmutex);
	}

	pthread_exit(0);
	return NULL;
}

void AgentTransceiver::shutdown(AgentTransceiver* agentTransceiver) {
	printf("AgentTransceiver | Shutting down\n");
	agentTransceiver->go = 0;
}

