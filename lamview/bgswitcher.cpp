#include "stdafx.h"
#include <assert.h>
#include <windows.h>
#include <process.h>
#include "BgSwitcher.h"
#include "Switcher.h"

void switcherDemon(void*arg);

BgSwitcher::BgSwitcher(int com_port, int otau_id, bool hardInitMode)
{
	isStarted = false;
	toStop = false;
	isError = false;
	this->hardInitMode = hardInitMode;
	this->comPort = com_port;
	this->otauId = otau_id;
	desiredPort = SWITCHER_PORT_UNDEFINED;
	activePort = SWITCHER_PORT_UNDEFINED;

	_beginthread(switcherDemon,0,(void*)this);

	while (!isError && !isStarted)
		sleep();
}
BgSwitcher::~BgSwitcher()
{
	finalize();
}
void BgSwitcher::sleep()
{
	Sleep(BGSWITCHER_SLEEP_TIME);
}
void BgSwitcher::finalize()
{
	toStop = true;
	while(!isError && isStarted)
		sleep();
}
void BgSwitcher::c_switchTo(int port)
{
	c_switchToAsync(port);
	c_wait();
}
void BgSwitcher::c_switchToAsync(int port)
{
	assert(SWITCHER_PORT_UNDEFINED != port);
	c_wait(); // ensure that prev. request is complete
	if (desiredPort == port)
		return; // need not to switch
	desiredPort = port;
}
void BgSwitcher::c_wait()
{
	while (!isError && desiredPort != activePort)
		sleep();
}
bool BgSwitcher::c_isError()
{
	return isError;
}

void BgSwitcher::s_sleep()
{
	sleep();
}

bool BgSwitcher::s_getHardInitMode() { return hardInitMode; }
int BgSwitcher::s_getComPort() { return comPort; }
int BgSwitcher::s_getOtauId() { return otauId; }
int BgSwitcher::s_getDesiredPort() { return desiredPort; }
bool BgSwitcher::s_getToStop() { return toStop; }
void BgSwitcher::s_setStarted(bool v) { isStarted = v; }
void BgSwitcher::s_setActivePort(int v) { activePort = v; }
void BgSwitcher::s_setError() { isError = true; }

void switcherDemon(void*arg)
{
	BgSwitcher *bs = (BgSwitcher*) arg;

	// initialize

	Switcher *sw = new Switcher(bs->s_getComPort(), bs->s_getHardInitMode());

	if (sw->getNumberOfOtaus() == 0) // todo: check for errors
	{
		delete sw;
		bs->s_setError();
		return;
	}

	int activePort = SWITCHER_PORT_UNDEFINED;
	bs->s_setActivePort(activePort);

	bs->s_setStarted(true);

	int reptStatusCounter = 0;
	// main loop

	for(;;)
	{
		bs->s_sleep();
		if (bs->s_getToStop())
		{
			delete sw;
			bs->s_setStarted(false);
			return;
		}
		int desiredPort = bs->s_getDesiredPort();
		if (desiredPort != activePort)
		{
			if (! sw->switch_OTAU(0, desiredPort))
			{
				delete sw;
				bs->s_setError();
				return;
			}
			activePort = desiredPort;
			bs->s_setActivePort(activePort);
			reptStatusCounter = 0;
		}
		if (activePort != SWITCHER_PORT_DISCONNECTED &&
			activePort != SWITCHER_PORT_UNDEFINED &&
			reptStatusCounter++ > BGSWITCHER_N_SLEEPS_TO_REANIMATE)
		{
			if (! sw->repeatStatus())
			{
				delete sw;
				bs->s_setError();
				return;
			}
			reptStatusCounter = 0;
		}
	}
}
