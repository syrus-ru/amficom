// NeuroAnalyser.cpp: implementation of the NeuroAnalyser class.
//
//////////////////////////////////////////////////////////////////////

#include "NeuroAnalyser.h"

//#include "SamplesGenerator.h"
//#include "EventSample.h"

//#include <windows.h>
//#include <process.h>    /* _beginthread, _endthread */


//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

NeuroAnalyser::NeuroAnalyser(int ENTERSIGNALDIMENSION):NeuroNet(ENTERSIGNALDIMENSION)
{
	couplingUp = 0.9;
	couplingDown = 0.1;

	couplingUp2 = 0.66666666;
	couplingDown2 = 0.33333333;

	connector = 4;
	connKey[0] = 1.;
	connKey[1] = 1.;

	weldUp = 3;
	weldUpKey[0] = 0.;
	weldUpKey[1] = 1.;

	weldDown = 6;
	weldDownKey[0] = 1.;
	weldDownKey[1] = 0.;

	linear = 0;
	linearKey[0] = 0.;
	linearKey[1] = 0.;

	unrecognized = 1;

		//Creating of the net;
		addLayer(30);
//		addLayer(3);
//		addLayer(13);
		addLayer(2);
		buildNet();
}

NeuroAnalyser::NeuroAnalyser(int ENTERSIGNALDIMENSION, int *NEURONS_PER_LAYER, int LAYERS):NeuroNet(ENTERSIGNALDIMENSION)
{
	couplingUp = 0.9;
	couplingDown = 0.1;

	couplingUp2 = 0.6;
	couplingDown2 = 0.4;

	connector = 4;
	connKey[0] = 1.;
	connKey[1] = 1.;

	weldUp = 3;
	weldUpKey[0] = 0.;
	weldUpKey[1] = 1.;

	weldDown = 6;
	weldDownKey[0] = 1.;
	weldDownKey[1] = 0.;

	linear = 0;
	linearKey[0] = 0.;
	linearKey[1] = 0.;

	unrecognized = 1;

	//Creating of the net;
	for(int i=0; i<LAYERS; i++)
	{
		addLayer(NEURONS_PER_LAYER[i]);
	}
	buildNet();

}



NeuroAnalyser::~NeuroAnalyser()
{

}


int NeuroAnalyser::getEnterSignalDimension()
{
    return enterSignalDimension;
}


int NeuroAnalyser::getEventType(double *data)
  {

    double *exit = getExitSignal(data);

    if(exit[0]>couplingUp &&
       exit[1]>couplingUp)
    {
      return connector;
    }
    if(exit[0]<couplingDown &&
       exit[1]>couplingUp)
    {
      return weldUp;
    }
    if(exit[0]>couplingUp &&
       exit[1]<couplingDown)
    {
      return weldDown;
    }
    if(exit[0]<couplingDown &&
       exit[1]<couplingDown)
    {
      return linear;
    }

    return unrecognized;
  }



int NeuroAnalyser::getEventType2(double *data)
{

    double *exit = getExitSignal(data);

    if(exit[0]>couplingUp2 &&
       exit[1]>couplingUp2)
    {
      return connector;
    }
    if(exit[0]<couplingDown2 &&
       exit[1]>couplingUp2)
    {
      return weldUp;
    }
    if(exit[0]>couplingUp2 &&
       exit[1]<couplingDown2)
    {
      return weldDown;
    }
    if(exit[0]<couplingDown2 &&
       exit[1]<couplingDown2)
    {
      return linear;
    }

    return unrecognized;
}  


/*
void catchEndingChar(void *key)
{
	char c = getch();
	key = &c;
}
*/

