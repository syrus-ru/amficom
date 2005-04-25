// EventParams.cpp: implementation of the EventParams class.
//
//////////////////////////////////////////////////////////////////////

#include "EventParams.h"

#include <stdio.h>

const int EventParams::LINEAR = EventParams_LINEAR;
const int EventParams::GAIN = EventParams_GAIN;
const int EventParams::LOSS = EventParams_LOSS;
const int EventParams::REFLECTIVE = EventParams_REFLECTIVE;
const int EventParams::DEADZONE = EventParams_DEADZONE;
const int EventParams::ENDOFTRACE = EventParams_ENDOFTRACE;
const int EventParams::UNRECOGNIZED = EventParams_UNRECOGNIZED;

//---------------------------------------------------------------------------------------------------------------
EventParams::EventParams() {
	this->type = 0;
	this->begin = 0;
	this->end = 0;
	//
    this->R  = -1;
    this->R1 = -1;
    this->R2 = -1;
    this->R3 = -1;
}
//---------------------------------------------------------------------------------------------------------------
EventParams::~EventParams() {
}
//---------------------------------------------------------------------------------------------------------------
void EventParams::operator = (const EventParams& ep) {
	this->type = ep.type;
	this->begin = ep.begin;
	this->end = ep.end;
    //
    this->R = ep.R;
    this->R1 = ep.R1;
    this->R2 = ep.R2;
    this->R3 = ep.R3;
}
//---------------------------------------------------------------------------------------------------------------
