#ifndef _IASIMPLE_H
#define _IASIMPLE_H

#include "ArrList.h"

struct IAEvent
{
	IAEvent(int begin_, int end_, int type_)
		: begin(begin_), end(end_), type(type_) {}
	int begin;
	int end; // inclusive
	int type;
};


const int IAEvent_LIN = 0;
const int IAEvent_SPL = 1;
const int IAEvent_CON = 2;

void InitialAnalysis2(
		ArrList *events, // of type IAEvent
		double *data, // data
		int data_length,
		double delta_x, // dx, meters
		//double minimalThreshold,
		double minSplice, // min splice size, dB
		double minRefl, // min reflective, dB
		//double minimalEndingSplash,
		//double maximalNoise,
		//double noise3sLevel, // noise 3 sigma level (dB)
		//int waveletType,
		//double formFactor,
		int reflSize,
		int spliceSize);


#endif

/*
class IAENode
{
private:
	IAENode *next;
	IAENode *prev;
public:
	IAEvent data;

	IAEList();
	~IAEList();
	IAENode *getNext();
	IAENode *getPrev();
	void remove();
};

class IAEList
{
private:
	IAENode *head;
	IAENode *tail;
public:
	IAEList();
	~IAEList();
	int getLength();
	int isEmpty();
	void checkForConsistency();
	IAENode *begin();
	IAENode *end();
};
*/

