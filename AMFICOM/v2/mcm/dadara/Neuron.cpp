// Neuron.cpp: implementation of the Neuron class.
//
//////////////////////////////////////////////////////////////////////
#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <math.h>
#include "Neuron.h"



//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Neuron::Neuron(int LAYER, int ADDRESS, Dispatcher *DISPATCHER)
{
	layer=LAYER;    // Location of the neuron in
	address=ADDRESS;  // the total net, layer>=1, address>=1

	nEnterSinals=0; // Needed, if the neuron is in the first layer
	neededExitValue = 0.; //Needed, if the neuron is in the last layer

	error=0.;
	studyingCoeff = 0.5;
	inerciaCoeff = 0.1;

	shift=0.;
	deltaShift=0.;

	exitSignal=0.;
	children = new Vector();
	parents = new Vector();

	dispatcher = DISPATCHER;

	srand( (unsigned)time( NULL ) );

	dispatcher->regist(this);
}


Neuron::Neuron(int LAYER, int ADDRESS, int nENTERSIGRAL, Dispatcher *DISPATCHER)
{
	layer=LAYER;    // Location of the neuron in
	address=ADDRESS;  // the total net, layer>=1, address>=1

	nEnterSinals=nENTERSIGRAL; // Needed, if the neuron is in the first layer
	neededExitValue = 0.; //Needed, if the neuron is in the last layer

	error=0.;
	studyingCoeff = 0.5;
	inerciaCoeff = 0.1;

	shift=0.;
	deltaShift=0.;

	exitSignal=0.;
	children = new Vector();
	parents = new Vector();

	dispatcher = DISPATCHER;

	srand( (unsigned)time( NULL ) );

	dispatcher->regist(this);
}

Neuron::~Neuron()
{
	delete []weights;
	delete []deltaWeight;
	delete children;
	delete parents;
	delete []activateSignals;
}



double *Neuron::getWeights()
{
	return weights;
}


void Neuron::setWeights(double *WEIGHTS)
{
	for(int i=0; i<nEnterSinals; i++)
	{
		weights[i] = WEIGHTS[i];
	}
}


double Neuron::getShift()
{
	return shift;
}


void Neuron::setShift(double SHIFT)
{
	shift = SHIFT;
}

void Neuron::addParent(Neuron *parent)
{
    Neuron *n;

    for(int i=0; i<parents->getSize(); i++)
    {
      n = (Neuron *)(parents->get(i));
      if(n == parent) return;
    }
    parents->add(parent);
    parent->addChild(this);
}


void Neuron::addChild(Neuron *child)
{
    Neuron *n;

    for(int i=0; i<children->getSize(); i++)
    {
      n = (Neuron *)(children->get(i));
      if(n == child) return;
    }
    children->add(child);
    child->addParent(this);


}


double Neuron::rnd(double from, double to)
{
	double ret = (double)rand()/((double)RAND_MAX);
	
	ret = (to-from)*ret + from;
	return ret;
}


void Neuron::setRandomWeights(double downLimit, double upLimit)
{
    for(int i=0; i<nEnterSinals; i++)
    {
      weights[i] = rnd(downLimit, upLimit);
    }
}


void Neuron::setRandomShift(double downLimit, double upLimit)
{
    shift = rnd(downLimit, upLimit);
}


void Neuron::setStudyingCoeff(double coeff)
{
	if(coeff>1.) coeff=1.;
	else if(coeff<.001) coeff = 0.001;

	studyingCoeff = coeff;
}


void Neuron::setInerciaCoeff(double coeff)
{
	if(coeff>1.) coeff=1.;
	else if(coeff<.001) coeff = 0.001;

    inerciaCoeff = coeff;
}


void Neuron::setNeededExitValue(double NEEDEDEXITVALUE)
{
    neededExitValue = NEEDEDEXITVALUE;
}


void Neuron::setActivateSignals(double *signals)
{
	for(int i=0; i<nEnterSinals; i++)
	{
		activateSignals[i] = signals[i];
	}
}


void Neuron::setActivateSignal(double signal, Neuron *parent)
{
    for(int i=0; i<parents->getSize(); i++)
    {
      if(((Neuron *)parents->get(i)) == parent)
      {
        activateSignals[i] = signal;
        return;
      }
    }
    return;
}


void Neuron::calculateExitSignal()
{
    double tmp=0.;
    for(int i=0; i<nEnterSinals; i++)
    {
      tmp = tmp + (weights[i]*activateSignals[i]);
    }
      tmp = tmp + shift;

    exitSignal = activationFunction(tmp);
    return;
}



double Neuron::getExitSignal()
{
    return exitSignal;
}


double Neuron::activationFunction(double arg)
{
    double expFactor = 1.;

    return 1./(1.+exp(-expFactor*arg));
}


void Neuron::printWeights()
{
    printf("Printing of the weights of the neuron:");

    for(int i=0; i<nEnterSinals; i++)
    {
		printf("Neuron N %i : weight = %f", (i+1), weights[i]);
      
    }
}


void Neuron::sendExitSignalToChildren()
{
    for(int i=0; i<children->getSize(); i++)
    {
      ((Neuron *)children->get(i))->setActivateSignal(exitSignal, this);
    }
}



void Neuron::setError(double neededExitValue) // Works for EXPONENTIAL SIGMOIDAL activation function ONLY!!!!!
{
    if(children->isEmpty())
    {
      error = (neededExitValue-exitSignal)*
                    exitSignal*(1.-exitSignal);
    }
    else
    {
      setError();
    }
}


void Neuron::setError() // Works for EXPONENTIAL SIGMOIDAL activation function ONLY!!!!!
{
    double err=0.;
    Neuron *child;
    for(int i=0; i<children->getSize(); i++)
    {
      child = (Neuron *)(children->get(i));
      err = err + child->getRecursiveErrorPart(this);
    }
    error = exitSignal*(1.-exitSignal)*err;
}



double Neuron::getRecursiveErrorPart(Neuron *n) // To return recursive error (for EXPONENTIAL SIGMOIDAL activation function ONLY !!!)
{
    for(int j=0; j<parents->getSize(); j++)
    {
		if((Neuron *)(parents->get(j)) == n)
		{
			return (weights[j]*error);
		}
    }
    return 0.;
 }


void Neuron::correctWeights()
  {
    setError(neededExitValue);

    for(int i=0; i<nEnterSinals; i++)
    {
      deltaWeight[i] =(deltaWeight[i]*inerciaCoeff +
                       activateSignals[i]*error*(1.-inerciaCoeff))*studyingCoeff;

      weights[i] = weights[i] + deltaWeight[i];
    }
    deltaShift = (deltaShift*inerciaCoeff +
                  error*(1.-inerciaCoeff))*studyingCoeff;
    shift = shift + deltaShift;
  }



void Neuron::operationPerformed(int commandName)
{
    if(commandName == -10) // Inicialization of the weights
    {                                                        
      if(layer == 1)
      {
        weights = new double[nEnterSinals];
        activateSignals = new double[nEnterSinals];
        deltaWeight = new double[nEnterSinals];
      }
      else
      {
        nEnterSinals = parents->getSize();
        weights = new double[nEnterSinals];
        activateSignals = new double[nEnterSinals];
        deltaWeight = new double[nEnterSinals];
      }
      setRandomWeights(-1., 1.);
	  setRandomShift(-1., 1.);
    }
	
    else  // to calculate exit signal
    if(commandName == layer)
    {
        calculateExitSignal();
        sendExitSignalToChildren();
    } 
    else // to correct veights
    if(commandName == layer + 10000)
    {
      correctWeights();
    }

}
