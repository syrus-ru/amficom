// EventParams.cpp: implementation of the EventParams class.
//
//////////////////////////////////////////////////////////////////////

#include "EventParams.h"

#include <stdio.h>

const int EventParams::LINEAR = EventParams_LINEAR;
const int EventParams::GAIN = EventParams_GAIN;
const int EventParams::LOSS = EventParams_LOSS;
const int EventParams::CONNECTOR = EventParams_CONNECTOR;
const int EventParams::DEADZONE = EventParams_DEADZONE;
const int EventParams::ENDOFTRACE = EventParams_ENDOFTRACE;
const int EventParams::UNRECOGNIZED = EventParams_UNRECOGNIZED;

//---------------------------------------------------------------------------------------------------------------
EventParams::EventParams() {
	this->type = 0;
	this->begin = 0;
	this->end = 0;
    this->R  = -1;
    this->can_be_endoftrace = false;
}
//---------------------------------------------------------------------------------------------------------------
EventParams::~EventParams() {
}
//---------------------------------------------------------------------------------------------------------------
void EventParams::operator = (const EventParams& ep) {
	this->type = ep.type;
	this->begin = ep.begin;
	this->end = ep.end;
    this->R = ep.R;
    this->can_be_endoftrace = ep.can_be_endoftrace;
}
//---------------------------------------------------------------------------------------------------------------
