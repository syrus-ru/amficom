// Segment.cpp: implementation of the Segment class.
//
//////////////////////////////////////////////////////////////////////

#include "KISInfoSegment.h"
#include "MCMInfoSegment.h"
#include "MeasurementSegment.h"
#include "ResultSegment.h"
#include <stdlib.h>
#include <stdio.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Segment::Segment() {
}

Segment::~Segment() {
	delete[] this->data;
}

Segment* Segment::createFromData(unsigned int length, char* data) {
	Segment* segment;
	switch ((int)data[0]) {
		case SEGMENT_KIS_INFO:
			segment = new KISInfoSegment(length, data);
			break;
		case SEGMENT_MCM_INFO:
			segment = new MCMInfoSegment(length, data);
			break;
		case SEGMENT_MEASUREMENT:
			segment = new MeasurementSegment(length, data);
			break;
		case SEGMENT_RESULT:
			segment = new ResultSegment(length, data);
			break;
		default:
			printf("createFromData | Illegal type of segment: %d\n", data[0]);
			segment = NULL;
	}
	return segment;
}
/*
SegmentType Segment::getSegmentType(char* data) {
	SegmentType segmentType;
	switch ((int)data[0]) {
		case SEGMENT_KIS_INFO:
			segmentType = SEGMENT_KIS_INFO;
		case SEGMENT_MCM_INFO:
			segmentType = SEGMENT_MCM_INFO;
		case SEGMENT_MEASUREMENT:
			segmentType = SEGMENT_MEASUREMENT;
		case SEGMENT_RESULT:
			segmentType = SEGMENT_RESULT;
		default:
			segmentType = SEGMENT_UNKNOWN;
	}
	return segmentType;
}
*/
SegmentType Segment::getType() const {
	SegmentType ret;
	switch ((int)this->data[0]) {
		case SEGMENT_KIS_INFO:
			ret = SEGMENT_KIS_INFO;
		case SEGMENT_MCM_INFO:
			ret = SEGMENT_MCM_INFO;
		case SEGMENT_MEASUREMENT:
			ret = SEGMENT_MEASUREMENT;
		case SEGMENT_RESULT:
			ret = SEGMENT_RESULT;
		default:
			ret = SEGMENT_UNKNOWN;
	}
	return ret;
}

char* Segment::getData() const {
	return this->data;
}

unsigned int Segment::getLength() const {
	return this->length;
}

