#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <time.h>
#include <errno.h>
//#include "general.h"
#include "RTUTransceiver.h"
#include "TraceReader.h"

//||||||||||||||||||||||||||||||||||||||||||||||||||||||
void printMeasurementSegment(MeasurementSegment* measurementSegment) {
	printf("length: %d\n", measurementSegment->getLength());
	printf("measurement_id: %s\n", measurementSegment->getMeasurementId()->getData());
	printf("measurement_type_id: %s\n", measurementSegment->getMeasurementTypeId()->getData());
	printf("local_address: %s\n", measurementSegment->getLocalAddress()->getData());
	printf("parnumber: %d\n", measurementSegment->getParnumber());

	Parameter* p;
	char* c = new char[8];//char c[8];
	unsigned int i;
	p = (measurementSegment->getParameters())[0];
	for (i = 0; i < sizeof(int); i++)
		c[i] = (p->getValue()->getData())[sizeof(int) - i - 1];
	printf("parameter 1: \'%s\' == %d\n", p->getName()->getData(), *(int*)c);
	p = (measurementSegment->getParameters())[1];
	for (i = 0; i < sizeof(double); i++)
		c[i] = (p->getValue()->getData())[sizeof(double) - i - 1];
	printf("parameter 2: \'%s\' == %f\n", p->getName()->getData(), *(double*)c);
	p = (measurementSegment->getParameters())[2];
	for (i = 0; i < sizeof(double); i++)
		c[i] = (p->getValue()->getData())[sizeof(double) - i - 1];
	printf("parameter 3: \'%s\' == %f\n", p->getName()->getData(), *(double*)c);
	p = (measurementSegment->getParameters())[3];
	for (i = 0; i < sizeof(long64); i++)
		c[i] = (p->getValue()->getData())[sizeof(long64) - i - 1];
	printf("parameter 4: \'%s\' == %d\n", p->getName()->getData(), *(long64*)c);
	p = (measurementSegment->getParameters())[4];
	for (i = 0; i < sizeof(double); i++)
		c[i] = (p->getValue()->getData())[sizeof(double) - i - 1];
	printf("parameter 5: \'%s\' == %f\n", p->getName()->getData(), *(double*)c);
	p = (measurementSegment->getParameters())[5];
	for (i = 0; i < sizeof(double); i++)
		c[i] = (p->getValue()->getData())[sizeof(double) - i - 1];
	printf("parameter 6: \'%s\' == %f\n", p->getName()->getData(), *(double*)c);
	delete[] c;
}

void printBuffer(char* mesgcomm, unsigned int length) {
	printf("length == %d\n", length);
	unsigned int ii;
	for (ii = 0; ii < length; ii++) {
		printf("%02x  ", mesgcomm[ii]);
		if ((ii + 1)%16 == 0)
			printf("\n");
	}
	printf("\n");
}
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

RTUTransceiver::RTUTransceiver(int rtu_sockfd,
				unsigned int timeout,
				MeasurementQueueT* measurementQueue,
				ResultQueueT* resultQueue,
				pthread_mutex_t* tmutex,
				pthread_mutex_t* rmutex) {
	this->rtu_sockfd = rtu_sockfd;
	this->timeout = timeout;
	this->comm_error = 0;
	this->go = 1;

//Set the status of RTU (it may be BUSY!)
	this->rtu_status = RTU_STATUS_FREE;

//Setup Measurement and Result Queues
	this->measurementQueue = measurementQueue;
	this->resultQueue = resultQueue;
	this->tmutex = tmutex;
	this->rmutex = rmutex;

//Create thread
	pthread_attr_t pt_attr;
	pthread_attr_init(&pt_attr);
	pthread_attr_setdetachstate(&pt_attr, PTHREAD_CREATE_JOINABLE);
	pthread_create(&this->thread, &pt_attr, RTUTransceiver::run, (void*)this);
	pthread_attr_destroy(&pt_attr);
}

RTUTransceiver::~RTUTransceiver() {
	printf("RTUTransceiver -- deleting\n");
}

int RTUTransceiver::getRTUsockfd() const {
	return this->rtu_sockfd;
}

unsigned int RTUTransceiver::getTimeout() const {
	return this->timeout;
}

pthread_t RTUTransceiver::getThread() const {
	return this->thread;
}

int RTUTransceiver::isCommError() const {
	return this->comm_error;
}

int RTUTransceiver::processRTUMessage(char* mesg) {
	printf("RTUTransceiver | Message from RTU: %s\n", mesg);
	if (!memcmp(mesg, "NO ALARMS", 9)) {
		//printf("++++++++ No alarms\n");
	}
	else
		if (!memcmp(mesg, "TIME SYNC", 9)) {
			this->processTimeSync();
		}
		else
			if(!memcmp(mesg, "POWER UP", 8)) {
				//printf("++++++++ Power up\n");
			}
			else
				return 0;
	return 1;
}

void RTUTransceiver::processTimeSync() {
	tm* t;
	timeval tv;
	if (gettimeofday(&tv, NULL) == 0) {
		char* mesg = new char[RTUCOMMANDMAXSIZE];
		t = localtime(&tv.tv_sec);
		sprintf(mesg, "SSC T=%02d:%02d:%03d D=%04d%02d%02d", t->tm_hour, t->tm_min, t->tm_sec, 1900 + t->tm_year, 1 + t->tm_mon, t->tm_mday);
		printf("RTUTransceiver | Time sync message: %s\n", mesg);
		unsigned int len = strlen(mesg);

		unsigned int send_counter = 0;
		if (!this->sendCommand(mesg, len, send_counter)) {
			printf("RTUTransceiver | ERROR: Cannot send time sync message\n");
		}
		delete[] mesg;
	}
	else
		perror("RTUTransceiver | ERROR -- gettimeofday");
}

int RTUTransceiver::receiveRTUReply(char*& reply, unsigned int& reply_length, int t) {
	fd_set in;
	timeval tv;
	int sel_ret;
	char* buffer;
	unsigned int recv_read;
	int recv_ret;

	FD_ZERO(&in);
	FD_SET(this->rtu_sockfd, &in);
	tv.tv_sec = t;
	tv.tv_usec = 0;

	sel_ret = select(this->rtu_sockfd + 1, &in, NULL, NULL, &tv);

	if (sel_ret == 0)
		return 0;
	if (sel_ret > 0) {
		buffer = new char[RTUHEADERSIZE];
		if (recv(this->rtu_sockfd, buffer, RTUHEADERSIZE, 0) < 0) {
			if (errno == ECONNREFUSED)
				this->comm_error = 1;
			perror("RTUTransceiver | ERROR -- recv");
			printf("RTUTransceiver | ERROR: Cannot read header of reply\n");
			delete[] buffer;
			return -1;
		}
		if (buffer[0] != RTU_CONSTANT) {
			reply_length = RTUHEADERSIZE;
			reply = new char[RTUHEADERSIZE];
			memcpy(reply, buffer, RTUHEADERSIZE);
/*/------------
	unsigned int ii;
	for (ii = 0; ii < RTUHEADERSIZE; ii++) {
		printf("%02x  ", reply[ii]);
		if ((ii + 1)%16 == 0)
			printf("\n");
	}
	printf("\n");
//------------*/
			delete[] buffer;
			return 1;
		}
		else {
			reply_length = (unsigned char)buffer[12] + ((unsigned char)buffer[13]<<8);
			reply = new char[reply_length];
			delete[] buffer;
			recv_read = 0;
			while (recv_read < reply_length) {
				recv_ret = recv(this->rtu_sockfd, reply + recv_read, reply_length - recv_read, 0);
				if (recv_ret < 0) {
					if (errno == ECONNREFUSED)
						this->comm_error = 1;
					perror("RTUTransceiver | ERROR -- recv");
					printf("RTUTransceiver | ERROR: Cannot read body of reply\n");
					delete[] reply;
					return -1;
				}
				recv_read += recv_ret;
			}
			return 2;
		}
	}
	else {
		perror("RTUTRansceiver | ERROR -- select");
		return -1;
	}
}

int RTUTransceiver::sendCommand(char* command, unsigned int length, unsigned int& send_counter) {

//Compose command buffer
	char* buffer = new char[RTUHEADERSIZE + length];
	memset(buffer, 0x00, RTUHEADERSIZE);
	buffer[0] = RTU_CONSTANT;
	buffer[6] = send_counter;
	buffer[13] = length/256;
	buffer[12] = length - buffer[13]*256;
	memcpy(buffer + RTUHEADERSIZE, command, length);
//------------
	unsigned int ii;
	for (ii = 0; ii < RTUHEADERSIZE + length; ii++) {
		printf("%02x  ", buffer[ii]);
		if ((ii + 1)%16 == 0)
			printf("\n");
	}
	printf("\n");
//------------

//Send command to RTU
	if (send(this->rtu_sockfd, buffer, RTUHEADERSIZE + length, 0) == -1) {
		if (errno == ECONNREFUSED)
			this->comm_error = 1;
		perror("RTUTransceiver -- task | ERROR -- send");
		printf("RTUTransceiver | ERROR: Cannot send command\n");
		delete[] buffer;
		return -1;
	}
	delete[] buffer;
	send_counter++;
	printf("RTUTransceiver | Command is sent\n");
	return 1;
}

void RTUTransceiver::setMeasurementParameters(Parameter* pars[], int& wvlen, double& trclen,
			       	double& res, int& pulswd, double& ior, double& scans) const {
	unsigned int len, i;
	char* value;
	char* c = new char[8];

//Set wavelength
	len = pars[0]->getValue()->getLength();
	value = pars[0]->getValue()->getData();
	for (i = 0; i < len; i++)
		c[i] = value[len - i - 1];
	wvlen = *(int*)c;
	if (wvlen != 1625)
		wvlen = 1625;

//Set trace length
	len = pars[1]->getValue()->getLength();
	value = pars[1]->getValue()->getData();
	for (i = 0; i < len; i++)
		c[i] = value[len - i - 1];
	trclen = (*(double*)c)/1000;
	if (trclen < 12)
		trclen = 8;
	else
		if (trclen >= 12 && trclen < 24)
			trclen = 16;
		else
			if (trclen >= 24 && trclen < 48)
				trclen = 32;
			else
				if (trclen >= 48 && trclen < 96)
					trclen = 64;
				else
					if (trclen >= 96 && trclen < 192)
						trclen = 128;
					else
						trclen = 256;

//Set resolution
	len = pars[2]->getValue()->getLength();
	value = pars[2]->getValue()->getData();
	for (i = 0; i < len; i++)
		c[i] = value[len - i - 1];
	res = *(double*)c;
	if (res < 0.75)
		res = 0.5;
	else
		if (res >= 0.75 && res < 1.5)
			res = 1;
		else 
			if (res >= 1.5 && res < 3)
				res = 2;
			else
				if (res >= 3 && res < 6)
					res = 4;
				else
					if (res >= 6 && res < 12)
						res = 8;
					else
						res = 16;

//Set pulse width
	len = pars[3]->getValue()->getLength();
	value = pars[3]->getValue()->getData();
	for (i = 0; i < len; i++)
		c[i] = value[len - i - 1];
	pulswd = *(int*)c;
	if (pulswd < 75)
		pulswd = 50;
	else
		if (pulswd >= 75 && pulswd < 175)
			pulswd = 100;
		else
			if (pulswd >= 175 && pulswd < 375)
				pulswd = 250;
			else
				if (pulswd >= 375 && pulswd < 750)
					pulswd = 500;
				else
					if (pulswd >= 750 && pulswd < 1500)
						pulswd = 1000;
					else
						if (pulswd >= 1500 && pulswd < 3000)
							pulswd = 2000;
						else
							if (pulswd >= 3000 && pulswd < 6000)
								pulswd = 4000;
							else
								if (pulswd >= 6000 && pulswd < 9000)
									pulswd = 8000;
								else
									if (pulswd >= 9000 && pulswd < 15000)
										pulswd = 10000;
									else
										pulswd = 20000;

//Set index of refraction
	len = pars[4]->getValue()->getLength();
	value = pars[4]->getValue()->getData();
	for (i = 0; i < len; i++)
		c[i] = value[len - i - 1];
	ior = *(double*)c;
	if (ior != 1.467)
		ior = 1.467;

//Set number of averages
	len = pars[5]->getValue()->getLength();
	value = pars[5]->getValue()->getData();
	for (i = 0; i < len; i++)
		c[i] = value[len - i - 1];
	scans = (*(double*)c)/1000;
	if (scans < 6)
		scans = 12;
	else
		if (scans >= 6 && scans < 12)
			scans = 13;
		else
			if (scans >= 12 && scans < 24)
				scans = 14;
			else
				if (scans >= 24 && scans < 48)
					scans = 15;
				else
					if (scans >= 48 && scans < 96)
						scans = 16;
					else
						if (scans >= 96 && scans < 192)
							scans = 17;
						else
							scans = 18;
}

// ################### Main loop -- run() #####################
void* RTUTransceiver::run(void* args) {
	RTUTransceiver* rtuTransceiver = (RTUTransceiver*)args;

	int rec_ret;
	unsigned int rec_empty_count = 0;
	char* mesgcomm;
	char*mesgcomm_sav;
	unsigned int length;
	unsigned int send_counter;
	MeasurementSegment* measurementSegment;
	Parameter** parameters;
	int wvlen, pulswd;
	double trclen, res, ior, scans;
	int busy_timeout;
	char* databa;
	ByteArray* bname;
	ByteArray* bvalue;
	ByteArray* bmeasurement_id;
	ResultSegment* resultSegment;

	while (rtuTransceiver->go) {
		printf("RTUTransceiver\n");

	/*
	 * Sleep - commented as 'select' already do it
	 */
		//sleep(rtuTransceiver->timeout);

	/*
	 * Shutdown in case of communication error
	 */
		if (rtuTransceiver->comm_error) {
			printf("RTUTransceiver | Must shut down due to communication error\n");
			shutdown(rtuTransceiver);
		}

	/*
	 * Run when communication with RTU is okey
	 */
		switch (rtuTransceiver->rtu_status) {

		/*
		 * FREE -- RTU isn't measuring now
		 */
			case RTU_STATUS_FREE:
				rec_ret = rtuTransceiver->receiveRTUReply(mesgcomm, length, rtuTransceiver->timeout);
				switch (rec_ret) {
					case 0:
						rec_empty_count++;
						if (rec_empty_count >= RTUMAXTIMEOUT/rtuTransceiver->timeout) {
							printf("RTUTransceiver | ERROR: Communication lost\n");
							rtuTransceiver->comm_error = 1;
							continue;
						}
						break;
					case 1:
						rec_empty_count = 0;
						rtuTransceiver->processRTUMessage(mesgcomm);
						delete[] mesgcomm;
						break;
					case 2:
						printf("RTUTransceiver | ERROR: Received reply on command\n");
						delete[] mesgcomm;
						break;
					default:
						printf("RTUTRansceiver | ERROR: cannot read RTU message\n");
						continue;
				}
				
			/*
			 * Check for measurements in Task Queue
			 */
				pthread_mutex_lock(rtuTransceiver->tmutex);
				if (!rtuTransceiver->measurementQueue->empty()) {
					pthread_mutex_unlock(rtuTransceiver->tmutex);

				/*
				 * Get the oldest task
				 */
					measurementSegment = rtuTransceiver->measurementQueue->back();
					printMeasurementSegment(measurementSegment);

				/*
				 * Prepare and send command to OTAU
				 */
					ByteArray* local_address = measurementSegment->getLocalAddress();
					length = local_address->getLength();
					mesgcomm = new char[length];
					memcpy(mesgcomm, local_address->getData(), length);
					printf("RTUTransceiver | Switch command: \'%s\'\n", mesgcomm);
					mesgcomm_sav = new char[length];
					memcpy(mesgcomm_sav, mesgcomm, length);
					send_counter = 0;
					if (!rtuTransceiver->sendCommand(mesgcomm, length, send_counter)) {
						printf("RTUTransceiver | ERROR: Cannot send switch command\n");
						delete[] mesgcomm;
						delete[] mesgcomm_sav;
						continue;
					}
					delete[] mesgcomm;
					rtuTransceiver->rtu_status = RTU_STATUS_SWITCHED;
				}
				else
					pthread_mutex_unlock(rtuTransceiver->tmutex);
				break;

		/*
		 * SWITCHED -- RTU has received the command to switch OTAU
		 */
			case RTU_STATUS_SWITCHED:

			/*
			 * Receive the reply on command to OTAU
			 */
				rec_ret = rtuTransceiver->receiveRTUReply(mesgcomm, length, rtuTransceiver->timeout);
				switch (rec_ret) {
					case 0:
						printf("RTUTransceiver | No reply on switch command\n");
						continue;
					case 1:
						rtuTransceiver->processRTUMessage(mesgcomm);
						delete[] mesgcomm;
						continue;
					case 2:
						break;
					default:
						printf("RTUTransceiver | ERROR: Cannot read reply on switch command\n");
							delete[] mesgcomm_sav;
						rtuTransceiver->rtu_status = RTU_STATUS_FREE;
						continue;
				}
				printf("RTUTransceiver | Reply on switch command: \'%s\'\n", mesgcomm);

			/*
			 * Compare this reply with the standart one
			 */
				if (memcmp(mesgcomm, mesgcomm_sav, length)) {
					printf("RTUTransceiver | ERROR: Non standart reply!\n");
					delete[] mesgcomm_sav;
					delete[] mesgcomm;
					pthread_mutex_lock(rtuTransceiver->tmutex);
					rtuTransceiver->measurementQueue->pop_back();
					pthread_mutex_unlock(rtuTransceiver->tmutex);
					delete measurementSegment;
					rtuTransceiver->rtu_status = RTU_STATUS_FREE;
					continue;
				}
				delete[] mesgcomm_sav;
				delete[] mesgcomm;

			/*
			 * Prepare and send a command to begin measurement
			 */
				parameters = measurementSegment->getParameters();
				rtuTransceiver->setMeasurementParameters(parameters, wvlen, trclen, res, pulswd, ior, scans);
				mesgcomm = new char[RTUCOMMANDMAXSIZE];
				sprintf(mesgcomm, "IN,WV=%4d,RR=%.0f/%.1f,PW=%d,NX=%6.4f,SS=%.0f%c", wvlen, trclen, res, pulswd, ior, scans, 0x00);
				printf("RTUTransceiver | Measurement command: \'%s\'\n", mesgcomm);
				length = strlen(mesgcomm);
				mesgcomm_sav = new char[length];
				memcpy(mesgcomm_sav, mesgcomm, length);
				if (!rtuTransceiver->sendCommand(mesgcomm, length, send_counter)) {
					printf("RTUTransceiver | ERROR: Cannot send measurement command\n");
					delete[] mesgcomm;
					delete[] mesgcomm_sav;
					continue;
				}
				delete[] mesgcomm;
				rtuTransceiver->rtu_status = RTU_STATUS_STARTED;
				break;

		/*
		 * STARTED -- RTU has received the command to begin measurement
		 */
			case RTU_STATUS_STARTED:

			/*
			 * Receive the reply on the measurement command
			 */
				rec_ret = rtuTransceiver->receiveRTUReply(mesgcomm, length, rtuTransceiver->timeout);
				switch (rec_ret) {
					case 0:
						printf("RTUTransceiver | No reply on measurement command\n");
						continue;
					case 1:
						rtuTransceiver->processRTUMessage(mesgcomm);
						delete[] mesgcomm;
						continue;
					case 2:
						break;
					default:
						printf("RTUTransceiver | ERROR: Cannot read reply on measurement command\n");
						delete[] mesgcomm_sav;
						rtuTransceiver->rtu_status = RTU_STATUS_FREE;
						continue;
				}
				printf("RTUTransceiver | Reply on measurement command: \'%s\'\n", mesgcomm);
			/*
			 * Compare somehow with mesgcomm_sav
			 * (hz kak)
			 */
				delete[] mesgcomm_sav;
				delete[] mesgcomm;
				pthread_mutex_lock(rtuTransceiver->tmutex);
				rtuTransceiver->measurementQueue->pop_back();
				pthread_mutex_unlock(rtuTransceiver->tmutex);

			/*
			 * Prepare and send the first ST command
			 */
				length = 2;
				mesgcomm = new char[length];
				mesgcomm[0] = 'S';
				mesgcomm[1] = 'T';
				printf("RTUTransceiver | Sending ST command\n");
				if (!rtuTransceiver->sendCommand(mesgcomm, length, send_counter)) {
					printf("RTUTransceiver | ERROR: Cannot send ST command\n");
					delete[] mesgcomm;
					delete measurementSegment;
					rtuTransceiver->rtu_status = RTU_STATUS_FREE;
					continue;
				}
				delete[] mesgcomm;
				rtuTransceiver->rtu_status = RTU_STATUS_BUSY;
				break;

		/*
		 * BUSY -- measurement is in progress
		 */
			case RTU_STATUS_BUSY:

			/*
			 * Receive the reply on the first ST command
			 */
				rec_ret = rtuTransceiver->receiveRTUReply(mesgcomm, length, rtuTransceiver->timeout);
				switch (rec_ret) {
					case 0:
						printf("RTUTransceiver | No reply on ST command\n");
						continue;
					case 1:
						rtuTransceiver->processRTUMessage(mesgcomm);
						delete[] mesgcomm;
						continue;
					case 2:
						break;
					default:
						printf("RTUTransceiver | ERROR: Cannot read reply on ST command\n");
						delete measurementSegment;
						rtuTransceiver->rtu_status = RTU_STATUS_FREE;
						continue;
				}
				printf("RTUTransceiver | Reply on ST command: \'%s\'\n", mesgcomm);

			/*
			 * If 'No Trace' -- send one more ST command
			 */
				if (!memcmp(mesgcomm, "ST=No Trace", 11)) {
					busy_timeout = atoi((const char*)(mesgcomm + 13));
					if (busy_timeout == 0)
						busy_timeout = rtuTransceiver->timeout;
					delete[] mesgcomm;
					sleep(busy_timeout);
					length = 2;
					mesgcomm = new char[length];
					mesgcomm[0] = 'S';
					mesgcomm[1] = 'T';
					printf("RTUTransceiver | Sending ST command\n");
					if (!rtuTransceiver->sendCommand(mesgcomm, length, send_counter)) {
						printf("RTUTransceiver | ERROR: Cannot send ST command\n");
						delete[] mesgcomm;
						delete measurementSegment;
						rtuTransceiver->rtu_status = RTU_STATUS_FREE;
						continue;
					}
					delete[] mesgcomm;
				}
				else

				/*
				 * If 'Trace Ready' -- send TX command and switch status to READY
				 */
					if (!memcmp(mesgcomm, "ST=Trace Ready", 11)) {
						printf("RTUTransceiver | Trace ready!\n");
						delete[] mesgcomm;
						length = 2;
						mesgcomm = new char[length];
						mesgcomm[0] = 'T';
						mesgcomm[1] = 'X';
						printf("RTUTransceiver | Sending TX command\n");
						if (!rtuTransceiver->sendCommand(mesgcomm, length, send_counter)) {
							printf("RTUTransceiver | ERROR: Cannot send TX command\n");
							delete[] mesgcomm;
							delete measurementSegment;
							rtuTransceiver->rtu_status = RTU_STATUS_FREE;
							continue;
						}
						rtuTransceiver->rtu_status = RTU_STATUS_READY;
						delete[] mesgcomm;
					}
					else {
						printf("RTUTransceiver | ERROR: Incorrect reply on TX command\n");
						delete[] mesgcomm;
					}
				break;

		/*
		 * READY -- measurement is complete
		 */
			case RTU_STATUS_READY:

			/*
			 * Receive reply on TX command -- reflectogramm
			 */
				rec_ret = rtuTransceiver->receiveRTUReply(mesgcomm, length, rtuTransceiver->timeout);
				switch (rec_ret) {
					case 0:
						printf("RTUTransceiver | No reply on TX command\n");
						continue;
					case 1:
						rtuTransceiver->processRTUMessage(mesgcomm);
						delete[] mesgcomm;
						continue;
					case 2:
						break;
					default:
						printf("RTUTransceiver | ERROR: Cannot read reply on TX command \n");
						delete measurementSegment;
						rtuTransceiver->rtu_status = RTU_STATUS_FREE;
						continue;
				}

			/*
			 * Create ResultSegment and put it in front of the Report Queue
			 */
				bmeasurement_id = measurementSegment->getMeasurementId()->clone();
				databa = new char[14];//databa = new char[13];
				sprintf(databa, "reflectogramma");//sprintf(databa, "reflectogramm");
				bname = new ByteArray(14, databa);//bname = new ByteArray(13, databa);

				unsigned int b_s;
				get_bellcore_data(mesgcomm+3, length-3, (unsigned char*&)databa, b_s);
/*/---------
FILE* fp = fopen("t.sor", "wb");
fwrite(databa, 1, b_s, fp);
fclose(fp);
//---------*/

				delete[] mesgcomm;
				bvalue = new ByteArray(b_s, databa);
				parameters = new Parameter*[1];
				parameters[0] = new Parameter(bname, bvalue);
				resultSegment = new ResultSegment(bmeasurement_id,
								1,
								parameters);
				pthread_mutex_lock(rtuTransceiver->rmutex);
				rtuTransceiver->resultQueue->push_front(resultSegment);
				pthread_mutex_unlock(rtuTransceiver->rmutex);

			/*
			 * Delete measurementSegment -- it is processed now
			 * NOTE: this was allocated in another place!
			 */
				delete measurementSegment;

			/*
			 * Prepare and send EOS command
			 */
				length = 3;
				mesgcomm = new char[length];
				mesgcomm[0] = 'E';
				mesgcomm[1] = 'O';
				mesgcomm[2] = 'S';
				printf("RTUTransceiver | Sending EOS command\n");
				if (!rtuTransceiver->sendCommand(mesgcomm, length, send_counter)) {
					printf("RTUTransceiver | ERROR: Cannot send EOS command\n");
					delete[] mesgcomm;
					rtuTransceiver->rtu_status = RTU_STATUS_FREE;
					continue;
				}
				delete[] mesgcomm;
				rtuTransceiver->rtu_status = RTU_STATUS_EOS;
				break;

		/*
		 * EOS -- Command EOS is sent
		 */
			case RTU_STATUS_EOS:

			/*
			 * Receive reply on EOS command
			 */

				rec_ret = rtuTransceiver->receiveRTUReply(mesgcomm, length, rtuTransceiver->timeout);
				switch (rec_ret) {
					case 0:
						printf("RTUTransceiver | No reply on EOS command\n");
						continue;
					case 1:
						rtuTransceiver->processRTUMessage(mesgcomm);
						delete[] mesgcomm;
						continue;
					case 2:
						printf("RTUTransceiver | Reply on EOS command: \'%s\'\n", mesgcomm);
						delete[] mesgcomm;
						break;
					default:
						printf("RTUTransceiver | ERROR: Cannot read reply on EOS command\n");
						rtuTransceiver->rtu_status = RTU_STATUS_FREE;
						continue;
				}

			/*
			 * Ready for the next measurement now
			 */
				rtuTransceiver->rtu_status = RTU_STATUS_FREE;
				break;
		} //switch (rtuTransceiver->rtu_status)

	}
	pthread_exit(NULL);
	return NULL;
}

void RTUTransceiver::shutdown(RTUTransceiver* rtuTransceiver) {
	printf("RTUTransceiver | Shutting down\n");
	rtuTransceiver->go = 0;
	close(rtuTransceiver->rtu_sockfd);
}

