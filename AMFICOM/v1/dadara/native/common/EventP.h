// EventP.h

// Declares Event Parameters structure EventP

#ifndef _EventP_H
#define _EventP_H

#include "ModelF.h"

struct EventP
{
    int begin;
    int end;
	int gentype; // LINEAR / SPLICE / CONNECTOR, see ..._dadara_ReflectogramEvent.h
	double delta_x;
    ModelF mf;
};

#endif

