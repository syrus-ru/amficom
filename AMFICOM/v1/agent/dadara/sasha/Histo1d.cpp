// Histo1d.cpp: implementation of the Histo1d class.
//
//////////////////////////////////////////////////////////////////////

#include "Histo1d.h"
#include <math.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

double S;

Histo1d::~Histo1d()
{
	delete []histo;
}

Histo1d::Histo1d()
{
}


Histo1d::Histo1d(int nBins1, double downLimit1, double upLimit1)
{
	nBins = nBins1;
	downLimit = downLimit1;
	upLimit = upLimit1;
    setCorrectLimits();
	histo = new int[nBins];
	delta = (upLimit-downLimit)/((double)nBins);
	CleanIt();
}

Histo1d::Histo1d(double delta1, double downLimit1, double upLimit1)
{
	downLimit = downLimit1;
	upLimit = upLimit1;
    setCorrectLimits();
	delta = delta1;

	nBins = (int)((upLimit-downLimit)/(delta) + 0.5);
	histo = new int[nBins];
	CleanIt();
}


bool Histo1d::FillIt(double a)
{
	if(a<=downLimit || a>=upLimit) return false;
	if(fabs(a)<.0001) return false;
	int i=(int)((a-downLimit)/delta + 0.5);
	if(i<0 || i>=nBins) return false;
	histo[i] = histo[i]+1;
    return true;
}

void Histo1d::FillIt(double *a, int begin, int end)
{
	for(int i=begin; i<end; i++)
	{
		FillIt(a[i]);
	}
	return;
}

void Histo1d::CleanIt()
{
	for(int i=0; i<nBins; i++)
	{
		histo[i] = 0;
	}
	return;
}


void Histo1d::setCorrectLimits()
{
	double tmp;
	if(downLimit>upLimit)
	{
		tmp=downLimit;
		downLimit = upLimit;
		upLimit = tmp;
	}
	return;
}


double Histo1d::getCentralValue()
{
	double centralValue=0.;
	int MaxHistoValue=0;
	int MaxHistoValueIndex=0;

	for(int i=0; i<nBins; i++)
	{
		if(MaxHistoValue<histo[i]) 
		{
			MaxHistoValue = histo[i];
			MaxHistoValueIndex = i;
		}
	}

	centralValue = downLimit + (MaxHistoValueIndex+.5)*delta;

	return centralValue;
}

double Histo1d::getHistoWidth(double level)
  {
    if(level<0. || level>1.) return delta;

    double ret=0.;
    int max=0;
    int maxIndex=0;
	int i;
    
    for(i=0; i<nBins; i++)
    {
      if(max<histo[i])
      {
	    max=histo[i];
	    maxIndex=i;
      }
    }

    int LEVEL = (int)(max*level+.5);
    int counter=0;

    for(i=0; i<nBins; i++)
    {
      if(histo[i]>=LEVEL)
      {
        counter++;
      }
    }
    ret = (counter)*delta;
    if(ret<delta) ret=delta;
    return ret;
  }


void Histo1d::FitIt()
{
	return;
}


double Histo1d::gauss(double centre, double sigma, double amplitude, double arg)
{
	double ret = amplitude*exp(-(arg-centre)*(arg-centre)/sigma/sigma);
	return ret;
}

double Histo1d::gauss(double centre, double sigma, double amplitude, int arg)
{
	return gauss(centre, sigma, amplitude, (double)arg);
}

