// Dispatcher.cpp: implementation of the Dispatcher class.
//
//////////////////////////////////////////////////////////////////////

#include "Dispatcher.h"
#include <stdio.h>
#include "Neuron.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Dispatcher::Dispatcher()
{
	listeners = new Vector();
}


Dispatcher::~Dispatcher()
{
	delete listeners;
}

void Dispatcher::regist(void *obj)
{
	listeners->add(obj);
}


void Dispatcher::notify(int commandName)
{
	for(int i=0; i<listeners->getSize(); i++)
	{
		((Neuron *)(listeners->get(i)))->operationPerformed(commandName);
	}
}