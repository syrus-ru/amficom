// NeuroNet.cpp: implementation of the NeuroNet class.
//
//////////////////////////////////////////////////////////////////////

#include "NeuroNet.h"
#include <stdio.h>
#include <process.h>

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

NeuroNet::NeuroNet(int ENTERSIGNALDIMENSION)
{
    enterSignalDimension = ENTERSIGNALDIMENSION;
    enterSignal = new double[ENTERSIGNALDIMENSION];
    layers = new Vector();
    dispatcher = new Dispatcher();
	lastLayerDimension = 0;
	firstLayerDimension = 0;

	allWeightsLength = 0;
	allShiftsLength = 0;

	teachingQuality = 0.;

}

NeuroNet::~NeuroNet()
{
//	printf("Deleting...");
	Neuron **n;
	int i;
	int j;
	for(i=0; i<layers->getSize(); i++)
	{
		n = (Neuron **)layers->get(i);
		for(j=0; j<layersDimension[i]; j++)
		{
			delete n[j];
		}
		delete []n;
	}
	delete layers;
	delete []exitSignal;
	delete dispatcher;
	delete []enterSignal;

	if(allWeightsLength != 0)
	{
		delete []allWeights;
	}
	if(allShiftsLength != 0)
	{
		delete []allShifts;
	}
}



void NeuroNet::addLayer(int neuronsNumber)
{
	Neuron **n = new Neuron*[neuronsNumber];
	int i;
	int j;

    if(layers->isEmpty())
    {
		for(i=0; i<neuronsNumber; i++)
        {
			n[i] = new Neuron(1, i+1, enterSignalDimension, dispatcher);
        }
		firstLayerDimension = neuronsNumber;
    }
    else
    {
        for(i=0; i<neuronsNumber; i++)
        {
            n[i] = new Neuron(layers->getSize()+1, i+1, dispatcher);
        }
        Neuron **parents = (Neuron **)(layers->lastElement());

        // Adding of the children to the parent.
        for(i=0; i<lastLayerDimension; i++)
        {
          for(j=0; j<neuronsNumber; j++)
          {
            parents[i]->addChild(n[j]);
          }
        }
      }
      layers->add(n);


	  lastLayerDimension = neuronsNumber;

	  layersDimension[layers->getSize()-1] = lastLayerDimension;
}



void NeuroNet::buildNet()
{
      dispatcher->notify(-10); // command to inicialize all weights
	  exitSignal = new double[lastLayerDimension];
}



double * NeuroNet::getExitSignal()
{
      int layer;
	  int i;

      for(i=0; i<layers->getSize(); i++)
      {
        layer = i+1;
        dispatcher->notify(layer); // command to calculate exit signal
      }

      Neuron **lastLayerNeurons = (Neuron **)(layers->lastElement());

      for(i=0; i<lastLayerDimension; i++)
      {
        exitSignal[i] = lastLayerNeurons[i]->getExitSignal();
      }

      return exitSignal;
}


void NeuroNet::setEnterSignal(double *ENTERSIGNAL)
    {
	    int i;

        for(i=0; i<enterSignalDimension; i++)
        {
          enterSignal[i] = ENTERSIGNAL[i];
        }

		Neuron **firstLayerNeurons = (Neuron **)(layers->get(0));

		for(i=0; i<firstLayerDimension; i++)
		{
			firstLayerNeurons[i]->setActivateSignals(enterSignal);
		}
    }


double *NeuroNet::getExitSignal(double *ENTERSIGNAL)
{
    setEnterSignal(ENTERSIGNAL);
    return getExitSignal();
}



void NeuroNet::correctWeights()
{
    int layer;
 
    for(int i=layers->getSize()-1; i>=0; i--)
    {
		layer = i+1;
		dispatcher->notify(layer+10000); // command to correct weights
    }
    return;
}



void NeuroNet::setNeededExitSignal(double *neededExitSignal)
{
    Neuron **lastLayerNeurons = (Neuron**)(layers->lastElement());

    for(int i=0; i<lastLayerDimension; i++)
    {
        lastLayerNeurons[i]->setNeededExitValue(neededExitSignal[i]);
    }
}



void NeuroNet::teachTheNetwork()
{
      // To be defined by user.
}







/////////////////////////////////////////////////////////////////////////////

double *NeuroNet::getAllWeights()
{
    int l=0;
	int i;
	int j;
	int k;

    Neuron** layerNeuron;

    for(i=0; i<layers->getSize(); i++)
    {
      layerNeuron = (Neuron **)(layers->get(i));
      for(j=0; j<layersDimension[i]; j++)
      {
         l+=layerNeuron[j]->nEnterSinals;
      }
    }

    allWeights = new double[l];
	allWeightsLength = l;

    double *tmp;

    l=0;
    for(i=0; i<layers->getSize(); i++)
    {
      layerNeuron = (Neuron **)(layers->get(i));
      for(j=0; j<layersDimension[i]; j++)
      {
        tmp = layerNeuron[j]->getWeights();
        for(k=0; k<layerNeuron[j]->nEnterSinals; k++)
        {
          allWeights[l] = tmp[k];
          l++;
        }
      }
    }
    return allWeights;
}



double *NeuroNet::getAllShifts()
{
     int l=0;
     int i;
     int j;
     Neuron **layerNeuron;

     for(i=0; i<layers->getSize(); i++)
     {
       l+=layersDimension[i];
     }

     allShifts = new double[l];
	 allShiftsLength = l;

     l=0;

     for(i=0; i<layers->getSize(); i++)
     {
       layerNeuron = (Neuron **)(layers->get(i));
       for(j=0; j<layersDimension[i]; j++)
       {
         allShifts[l] = layerNeuron[j]->getShift();
         l++;
       }
     }
     return allShifts;
}




void NeuroNet::writeWeightsAndShiftsInSingleFile(const char *fileName)
    {
      getAllShifts();
      getAllWeights();
	  int i;

	  FILE *f = fopen(fileName, "w+");
	  //Writing of weights;
	  fprintf(f, "%i\n", allWeightsLength);
	  for(i=0; i<allWeightsLength; i++)
	  {
		  fprintf(f, "%f\n", allWeights[i]);
	  }
	  fprintf(f, "%i\n", allShiftsLength);
	  for(i=0; i<allShiftsLength; i++)
	  {
		  fprintf(f, "%f\n", allShifts[i]);
	  }

	  fprintf(f, "The quality of the teaching is %f\n", teachingQuality);


	  fclose(f);
    }



bool NeuroNet::readWeightsAndShiftsFromSingleFile(const char* fileName)
{
	FILE *f = fopen(fileName, "r");
	int i;

	float tmp;

	if(f == NULL) 
	{
		printf("File with weights does not exist.\n");
		return false;
	}

	fseek(f, 0L, SEEK_SET);

	fscanf(f, "%i", &allWeightsLength);
	allWeights = new double[allWeightsLength];
	for(i=0; i<allWeightsLength; i++)
	{
		fscanf(f, "%f", &tmp);
		allWeights[i] = tmp;
	}
	
	fscanf(f, "%i", &allShiftsLength);
	allShifts = new double[allShiftsLength];
	for(i=0; i<allShiftsLength; i++)
	{
		fscanf(f, "%f", &tmp);
		allShifts[i] = tmp;
	}

	fclose(f);

	if(!setAllWeights(allWeights)) return false;
	if(!setAllShifts(allShifts)) return false;

	
	return true;
}


bool NeuroNet::setAllWeights(double *allWeights)
    {
      double *tmp;
      Neuron **layerNeuron;

      int l=0;
	  int i, j, k;
      for(i=0; i<layers->getSize(); i++)
      {
        layerNeuron = (Neuron**)(layers->get(i));
        for(j=0; j<layersDimension[i]; j++)
        {
          tmp = new double[layerNeuron[j]->nEnterSinals];
          for(k=0; k<layerNeuron[j]->nEnterSinals; k++)
          {
            if(l>allWeightsLength-1)
            {
              printf("Error setting weights. Check the dimension's length'.\n");
			  return false;
            }
            tmp[k] = allWeights[l];
            l++;
          }
          layerNeuron[j]->setWeights(tmp);
		  delete []tmp;
        }
      }
//      printf("All weights are set succesfully\n");
      return true;
    }


bool NeuroNet::setAllShifts(double *allShifts)
    {
      Neuron **layerNeuron;

      int l=0;
	  int i;
      for(i=0; i<layers->getSize(); i++)
      {
        layerNeuron = (Neuron **)(layers->get(i));
        for(int j=0; j<layersDimension[i]; j++)
        {
            if(l>allShiftsLength-1)
            {
              printf("Error setting shifts. Check the dimension's length'.\n");
			  return false;
            }
            layerNeuron[j]->setShift(allShifts[l]);
            l++;
        }
      }
//      printf("All shifts are set succesfully\n");
      return true;
    }



void NeuroNet::setStudyingCoeff(double coeff)
{
	int i, j;
	Neuron **layerNeuron;
	for(i=0; i<layers->getSize(); i++)
	{
		layerNeuron = (Neuron**)(layers->get(i));
		for(j=0; j<layersDimension[i]; j++)
		{
			layerNeuron[j]->setStudyingCoeff(coeff);
		}
	}
}


void NeuroNet::setInerciaCoeff(double coeff)
{
	int i, j;
	Neuron **layerNeuron;
	for(i=0; i<layers->getSize(); i++)
	{
		layerNeuron = (Neuron**)(layers->get(i));
		for(j=0; j<layersDimension[i]; j++)
		{
			layerNeuron[j]->setInerciaCoeff(coeff);
		}
	}

}
