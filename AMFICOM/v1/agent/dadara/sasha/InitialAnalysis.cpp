// InitialAnalysis.cpp: implementation of the InitialAnalysis class.
//
//////////////////////////////////////////////////////////////////////

#include "InitialAnalysis.h"
#include <math.h>
#include "Histo1d.h"
#include "NeuroAnalyser.h"
#include <stdio.h>

//FILE *stream;

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

InitialAnalysis::InitialAnalysis( double *data_, int data_length_,
								  double delta_x_,
							 	  double minimalThreshold_,
								  double minimalWeld_,
								  double minimalConnector_,
								  double minimalEndingSplash_,
								  double maximalNoise_,
								  int waveletType_, 
								  double formFactor_, 
								  int findEnd_)
{
	
	//stream = fopen( "c:\\dadara.log", "a" );

	pi					= 3.1415926535;
	wn1  =1.;
	wn2  =1.;
	wn3  =1.;
	wn4  =1.;
	wn5  =1.;
	wn6  =1.;
	wn7  =1.;
	wn8  =1.;
	wn9  =1.;
	wn10 =1.;

	numberOfFoundEvents = 0;

	delta_x = delta_x_;

	minimalThreshold	= minimalThreshold_;
	minimalWeld			= minimalWeld_;
	minimalConnector	= minimalConnector_;
	minimalEndingSplash = minimalEndingSplash_;
	maximalNoise        = maximalNoise_;
	waveletType			= waveletType_;
	formFactor			= formFactor_;
	findEnd             = findEnd_;

	data_length			= data_length_;
	data				= data_;


	{ //Inicializing of the arrays;
		noise			= new double[data_length];
		transformation	= new double[data_length];
		eventsTable		= new int[data_length+3];
		sigmaSquared    = new double[data_length];
	}

	// fprintf( stream, "InitialAnalysis parameters aquired: \n");
	// fprintf( stream, "\t minimalThreshold = %3.3f\n", minimalThreshold);
	// fprintf( stream, "\t minimalWeld = %3.3f\n", minimalWeld);
	// fprintf( stream, "\t minimalConnector = %3.3f\n", minimalConnector);
	// fprintf( stream, "\t minimalEndingSplash = %3.3f\n", minimalEndingSplash);
	// fprintf( stream, "\t maximalNoise = %3.3f\n", maximalNoise);
	// fprintf( stream, "\t waveletType = %d\n", waveletType);
	// fprintf( stream, "\t formFactor = %3.3f\n", formFactor);
	// fprintf( stream, "\t findEnd(java event size) = %d\n", findEnd);


	performAnalysis();

	
	// fprintf( stream, "\n");
	// fclose( stream );
}




InitialAnalysis::~InitialAnalysis()
{
	delete []noise;
	delete []eventsTable;
	delete []transformation;
	delete []sigmaSquared;

	for(int i=0; i<numberOfFoundEvents; i++)
	{
		delete ep[i];
	}
	delete []ep;

}




void InitialAnalysis::performAnalysis()
{
	
//	Beep(200, 100);
	/**
	
		
	*/
	correctDataArray();
//	Beep(300, 100);
	setEventSize();

//	Beep(300, 100);
	setNormalizingCoeffs();
	
//	Beep(400, 100);
	performTransformation();
//	Beep(500, 100);
	setNoise();
//	Beep(600, 100);
	shiftToZeroAttenuation();
//	Beep(700, 100);
	setNonZeroTransformation();
	
//	Beep(800, 100);
	setEventsTble();// Realized, but must be checked;
//	Beep(900, 100);
	
	
//	Beep(1000, 100);
	excludeNonRecognizedEvents();// Realized, but not fully and must be checked;
//	Beep(1100, 100);
	correctConnectorsCoords();// Realized, but must be checked;
//	Beep(1200, 100);
	excludeShortEvents(eventSize/2);

	setEventParams();

//	Beep(1300, 100);
	setCorrectEndingSplash();


	setSigmaSquared(); // noise for fitting;
}


void InitialAnalysis::correctDataArray()
{
	// Excluding bad points at the begin of the reflectogramm;
	double tmp;
	if(data[1]<data[0])
	{
		tmp = data[0];
		data[0] = data[1];
		data[1] = tmp;
	}
	if(data[2]<data[1] && data[2]<data[3])
	{
		data[2] = (data[1]+data[3])/2.;
	}

	double minimum = data[300];
	int i;

	for(i=300; i<data_length; i++)
	{
		if(data[i]<minimum)
		{
			minimum = data[i];
		}
	}

	for(i=0; i<data_length; i++)
	{
		data[i] = data[i] - minimum;
	}

	for(i=0; i<302; i++)
	{
		if(data[i]<0.)
		{
			data[i] = 0.;
		}
	}

	lastNonZeroPoint = data_length-1;
	for(i=300; i<data_length; i++)
	{
		if(data[i]<1.)
		{
			lastNonZeroPoint = i;
			break;
		}
	}
	
	if(lastNonZeroPoint+10<data_length)
		lastNonZeroPoint+=10;

	// fprintf( stream, "lastNonZeroPoint = %d out of %d (%.3fkm)\n", lastNonZeroPoint, data_length, delta_x * lastNonZeroPoint / 1000.);

	//for (i = lastNonZeroPoint - 12; i < lastNonZeroPoint + 3; i++)
	//	if (data[i] < 1)
			// fprintf( stream, "\tdata[%d] = %.3f\n", i, data[i]); 
}


void InitialAnalysis::setEventSize()
{
	eventSize = 0;
	int i;

	int maximumIndex = 4;
	double maximum = data[maximumIndex];
	

	for(i=0; i<300 && i<data_length; i++)
	{
		if(data[i]>maximum)
		{
			maximum = data[i];
			maximumIndex = i;
		}
	}

	eventSize = maximumIndex;

	for(i=maximumIndex; i<data_length; i++)
	{
		if(data[i]<maximum-0.5)
		{
			eventSize = i;
			break;
		}
	}

	eventSize = (int)(eventSize*0.8);

	if(eventSize<2)
	{
		eventSize = 2;
	}

	// fprintf( stream, "setEventSize():\tevent size in library = %d\n", eventSize);

//	printf("event size = %i\n", eventSize);
//	printf("ending point is = %i\n", lastNonZeroPoint);
}



void InitialAnalysis::performTransformation()
{
	int i;
	int j;
	double tmp;

	for(i=0; i<data_length; i++)
	{
		tmp = 0.;
		for(j=i-eventSize; j<=i+eventSize && j<data_length; j++)
		{
			if(j>=0)
			{
				tmp = tmp + data[j]*wLet(j-i);
			}
		}
		transformation[i] = tmp;
	}
}



void InitialAnalysis::setNoise()
{
	int i;

// First, we set that noise is euqal to the first derivative.

	for(i=0; i<data_length-1; i++)
	{
		noise[i] = fabs(data[i]-data[i+1]);
		if(noise[i]>maximalNoise) noise[i] = maximalNoise;
	}
	noise[data_length-1] = 0.;
    
	double EXP;

// Cut of the prompt-peaks with exponent.
	for(i=0; i<lastNonZeroPoint; i++)
	{
		EXP = (exp((double)i/lastNonZeroPoint)-1.)*maximalNoise/(exp(1.)-1.);
		if(noise[i]>EXP) noise[i] = EXP;
	}

	convolutionOfNoise(eventSize*2);
}


void InitialAnalysis::shiftToZeroAttenuation()
{
	meanAttenuation = 0.2*eventSize*delta_x/1000.; //0.2 dB/km;
	
	Histo1d *h1d = new Histo1d(100, -meanAttenuation*2., 0.);
	h1d->CleanIt();
	h1d->FillIt(transformation, 0, lastNonZeroPoint-1);

	meanAttenuation = h1d->getCentralValue();

	delete h1d;

	for(int i=0; i<lastNonZeroPoint; i++)
	{
		transformation[i] = transformation[i]-meanAttenuation;
	}
}


void InitialAnalysis::setNonZeroTransformation()
{
	int i;
	for(i=0; i<data_length; i++)
	{
		if(fabs(transformation[i])<minimalThreshold ||
		   fabs(transformation[i])<noise[i]/2.)
		{
			transformation[i] = 0.;
		}
	}

	//Excluding of the exidental zero points;
	for(i=1; i<data_length-1; i++)
	{
		if(fabs(transformation[i])<0.0001)
		{
			if(fabs(transformation[i-1])>0.0001 && 
		       fabs(transformation[i+1])>0.0001)

			{
					transformation[i] = (transformation[i-1]+transformation[i+1])/2.;
			}
		}
	}
}


/*
void InitialAnalysis::setEventsTble()
{
	int i;
	int counter = 0;

	for(i=0; i<lastNonZeroPoint; i++)
	{
//		printf("N of point: %i", i);
		if(!isNonZero(transformation[i])) // linear part of the reflectogramm;
		{
			eventsTable[counter] = 0;
			eventsTable[counter+1] = i;
			while(!isNonZero(transformation[i]) && i<lastNonZeroPoint-1)
			{
				i++;
				if(i>=lastNonZeroPoint)
					break;
			}
			i--;
			eventsTable[counter+2] = i;
			counter +=3;
		}
		else if(isNegative(transformation[i])) // Weld with energy loss;
		{
			double min_ = transformation[i];
			eventsTable[counter] = 3;
			eventsTable[counter+1] = i;
			while(i < lastNonZeroPoint && isNegative(transformation[i]))
			{
				if(min_>transformation[i]) min_ = transformation[i];
				i++;
				if(i>=lastNonZeroPoint)
					break;
			}
			i--;
			eventsTable[counter+2] = i;

			if(fabs(min_)<minimalWeld*.8)
				eventsTable[counter] = 0;
			counter +=3;
		}
		else if(isPositive(transformation[i])) // Weld up or connector;
		{
			int endPositive;
			int beginNegative;
			int endNegative;
			double max_;
			double min_;

			endPositive = i;
			max_ = transformation[i];

			while(endPositive<lastNonZeroPoint && isPositive(transformation[endPositive]))
			{
				if(max_ < transformation[endPositive])
					max_ = transformation[endPositive];
				endPositive++;
				if(endPositive>=lastNonZeroPoint)
					break;
			}
			endPositive--;
			beginNegative = endPositive;
			if(endPositive<lastNonZeroPoint-1)
				beginNegative++;

			while(beginNegative<lastNonZeroPoint && !isNegative(transformation[beginNegative]))
			{
				beginNegative++;
				if(beginNegative>=lastNonZeroPoint)
					break;
			}
			beginNegative--;
			endNegative = beginNegative;
			if(beginNegative<lastNonZeroPoint-1)
				endNegative++;

			min_ = transformation[endNegative];
			while(endNegative<lastNonZeroPoint && isNegative(transformation[endNegative]))
			{
				if(min_ > transformation[endNegative])
					min_ = transformation[endNegative];

				endNegative++;
			}
			endNegative--;

  				if( beginNegative - endPositive < eventSize*2./3 && 
  				    min_ < 0. && max_ > 0. &&
					fabs(max_)>minimalConnector*.8 && fabs(min_)>minimalConnector*.8) //Connector;
					{
						eventsTable[counter] = 4;
						eventsTable[counter+1] = i;
						eventsTable[counter+2] = endNegative;

						counter +=3;
						i = endNegative;
					}
					else									//weld;
					{

						eventsTable[counter] = 3;
						if(fabs(max_)<minimalWeld*.8)
							eventsTable[counter] = 0;
						eventsTable[counter+1] = i;
						eventsTable[counter+2] = endPositive;

						counter +=3;
						i = endPositive;
					}


		}
		if(i>lastNonZeroPoint-eventSize/2) break;
	}
	numberOfFoundEvents = counter/3;
}
*/


int InitialAnalysis::sign(double arg)
{
      if(arg> .0000000000000000001) return 1;
      if(arg<-.0000000000000000001) return -1;
      
	  return 0;
}



void InitialAnalysis::setEventsTble()
{
	int i=0;
	int halfWidth = eventSize/2;
	if(halfWidth<1) halfWidth=1;

	for(i=0; i<data_length; i++) 
		eventsTable[i] = 0;

    int counter = 0;
    int type = 0;

    int x1=0;
    int x2=0;

	int k1=0;
	int k2=0;
	int k3=0;

	int j=0;
	int k=0; 
	int s=0;

	double max1=0;
	double max2=0;
	double max3=0;



    for(i=0; i<lastNonZeroPoint+eventSize && i<data_length-10; i++)
     {
       x1 = i;
       j=i+1;
	   type = 0;

	   max1=0; 
	   max2=0;
	   max3=0;

         while(sign(transformation[x1]) == sign(transformation[j]))
		 {
           j+=1;
            if(j>=data_length-9)
			{
            break;
			}
			if(fabs(max1)<fabs(transformation[j-1])) max1=transformation[j-1];
		 }
		 k=j+1;
         while(sign(transformation[j]) == sign(transformation[k]))
		 {
           k+=1;
            if(k>=data_length-8)
			{
            break;
			}
			if(fabs(max2)<fabs(transformation[k-1])) max2=transformation[k-1];
		 }
		 s=k+1;
         while(sign(transformation[k]) == sign(transformation[s]))
		 {
           s+=1;
            if(s>=data_length-7)
			{
            break;
			}
			if(fabs(max3)<fabs(transformation[s-1])) max3=transformation[s-1];
		 }

		 k1 = (j+i)/2;
         k2 = (k+j)/2;
         k3 = (s+k)/2;

		 
        if(fabs(max1) < minimalThreshold) // linear part
        {
         type = 0;
         x2=j;
        }
        else
		if(max1>minimalConnector && max2<-minimalConnector)  // connector
		{
			type = 4;
             x2=k;
		}
		else
		if(max1>minimalConnector && fabs(max2)<minimalThreshold && max3<-minimalConnector && k-j<(int)(halfWidth*1.5)) //connector
		{
			type = 4;
			x2=s;
		}
		else
		if(max1>noise[k1]*3 && max2<-noise[k2]*3 && 
		   max1>minimalConnector && max2<-minimalConnector) //reflection (connector, anyway)
		{
			type = 4;
			x2=k;
		}
		else
		if(max1>noise[k1]*3 && max3<-noise[k3]*3 &&           //reflection (connector, anyway)
		   max1>minimalConnector && max3<-minimalConnector &&	
		   k-j<(int)(halfWidth*1.5) && fabs(max2)<.00001)
		{
			type = 4;
			x2=s;
		}
		else
        if(max1>minimalConnector && max2<minimalConnector && fabs((max1+max2)/(max1-max2))<.5) //reflection		
		{
			type = 4;
			x2=k;
		}
		else
        if(max1>minimalConnector && max3<minimalConnector && fabs((max1+max3)/(max1-max3))<.5 &&
		   k-j<(int)(halfWidth*1.5))                                           //reflection
		{
			type = 4;
			x2=s;
		}
		else
        if(max1>minimalWeld*.8 || max1<-minimalWeld*.8) //weld
		{
			type = 3;
			x2=j;
		}
		else //linear
		{
			type = 0;
			x2=j;
		}


      if(counter>=data_length) 
		  break;
	  
      eventsTable[counter] = type;
      eventsTable[counter+1] = x1;
      eventsTable[counter+2] = x2;
      counter +=3;
	  numberOfFoundEvents = counter/3;
      i=x2;


	  if(x1>=lastNonZeroPoint || x2>=lastNonZeroPoint)
		  break;
     }

	 return;
}
  


void InitialAnalysis::siewLinearParts()
{
	int i;
	int j;
	for(i=0; i<numberOfFoundEvents*3; i+=3)
	{
		if(eventsTable[i] == 0 || eventsTable[i] == 1)
		{
			eventsTable[i] = 0;
			j = i+3;
			while(j<numberOfFoundEvents*3 && (eventsTable[j] == 0 || eventsTable[j] == 1))
			{
				eventsTable[j] = -1;
				j+=3;
			}
			j-=3;
			if(j>i)
			{
				eventsTable[i+2] = eventsTable[j+2];
				i = j;
			}
		}
	}

	int counter = 0;
	for(i=0; i<numberOfFoundEvents*3; i+=3)
	{
		if(eventsTable[i]!=-1)
		{
			counter++;
		}
	}

	int *tmpEventsTable = new int[counter*3];
	int index = 0;
	for(i=0; i<numberOfFoundEvents*3; i+=3)
	{
		if(eventsTable[i]!=-1)
		{
			tmpEventsTable[index]	= eventsTable[i];
			tmpEventsTable[index+1] = eventsTable[i+1];
			tmpEventsTable[index+2] = eventsTable[i+2];
			index+=3;
		}
	}

	numberOfFoundEvents = counter;
	delete []eventsTable;
	eventsTable = tmpEventsTable;

}


void InitialAnalysis::excludeShortEvents(int eventLength)
{
	for(int i=0; i<numberOfFoundEvents*3; i+=3)
	{
		if(eventsTable[i+2]-eventsTable[i+1]<eventLength)
		{
			eventsTable[i] = 0;
			// fprintf( stream, "excludeShortEvents(): %d out of %d excluded: length = %.3f\n", i / 3, numberOfFoundEvents,
//				eventsTable[i+2]-eventsTable[i+1]); 
		}
	}

	siewLinearParts();
}




void InitialAnalysis::excludeNonRecognizedEvents()
{
	int architecture[10];
	int nLayers;

	//reading information about architecture.
	FILE *f = fopen("architecture.net", "r");
	int i;

	if(f == NULL)
	{
		//Beep(300, 300);
		//Beep(600, 300);
		//Beep(900, 300);
		printf("Error: file with architecture of neuro net is not found. Error.");
		return;
	}

	fseek(f, 0L, SEEK_SET);
	fscanf(f, "%i", &nLayers);

	int tmp;

	for(i=0; i<nLayers; i++)
	{
		fscanf(f, "%i", &tmp);
		architecture[i] = tmp;
	}

	fclose(f);



	int networkEventWindowSize = 66;

	double *window = new double[networkEventWindowSize];

	NeuroAnalyser *na = new NeuroAnalyser(networkEventWindowSize, architecture, nLayers);

	char *fileName = "weights.wht";

	int type;

	if(na->readWeightsAndShiftsFromSingleFile(fileName))
	{
		for(int i=3; i<numberOfFoundEvents*3-3; i+=3)
		{
//			if(eventsTable[i] == 4 || eventsTable[i] == 3 || eventsTable[i] == 6)
			if(eventsTable[i] == 3 || eventsTable[i] == 6)
			{
				setNNWindow(eventsTable[i+1], eventsTable[i+2], window, networkEventWindowSize);
				
				type = na->getEventType2(window);
				if(type == 6) 
					type = 3;
				else if(type == 1)
					type = 0;

				eventsTable[i] = type;
			}
		}
	}
	else
	{   
		//Beep(300, 300);
		//Beep(600, 300);
		//Beep(900, 300);
		printf("Weights are not set.");
	}
		
	delete []window;
	delete na;


	siewLinearParts();
}



void InitialAnalysis::correctConnectorsCoords()
{
	int i;
	int k;
	double A1;
	for(i=3; i<numberOfFoundEvents*3; i+=3)
	{
		if(eventsTable[i] == 4) //Connector!
		{
			if(eventsTable[i-3] == 0 && eventsTable[i-1]-eventsTable[i-2]>=3)
			{
				getLinearFittingCoefficients(data, eventsTable[i-2], eventsTable[i-1]);
				A1 = linearFittingParameters[0]+linearFittingParameters[1]*eventsTable[i+1];
			}
			else
			{
				A1 = data[eventsTable[i+1]];
			}

			for(k=eventsTable[i+1]; k<eventsTable[i+2]; k++)
			{
				if(data[k]-A1 > 0.1)
				{
					eventsTable[i+1] = k-3;
					eventsTable[i-1] = k-4;
					if (eventsTable[i-2] >= eventsTable[i-1])
						eventsTable[i-2] = eventsTable[i-1] - 1;
					break;
				}
			}
		}
	}
}



void InitialAnalysis::setCorrectEndingSplash()
{
	if(findEnd <=0)
	{
		return;
	}

	int i;
	int theEnd;
	
	//int l = ep[numberOfFoundEvents-1]->beginConnector+eventSize*3;
	// fprintf( stream, "end point before correction = %d (%.3fkm)\n", l, delta_x * l / 1000.);
	// fprintf( stream, "number of events before correction = %d\n", numberOfFoundEvents);

	for(i=numberOfFoundEvents-1; i>=0; i--)
	{
		if(ep[i]->connectorEvent==1 && 
			ep[i]->aLet_connector>minimalEndingSplash)
		{
			theEnd = ep[i]->beginConnector+eventSize*3;

			if(theEnd>=lastNonZeroPoint)
				theEnd = lastNonZeroPoint-1;

			ep[i]->endConnector = ep[i]->endLinear = ep[i]->endWeld = theEnd;
			numberOfFoundEvents = i+1;
			break;
		}
	}

	// fprintf( stream, "end point after correction = %d (%.3fkm)\n", theEnd, delta_x * theEnd / 1000.);
	// fprintf( stream, "number of events after correction = %d\n", numberOfFoundEvents);
}


void InitialAnalysis::setEventParams()
{
	int i;
	ep = new EventParams *[numberOfFoundEvents];
	
	for(i=0; i<numberOfFoundEvents; i++) //Creating of the EventParams; setting of the coords and linear fitting;
	{
		ep[i] = new EventParams();

		ep[i]->beginConnector = ep[i]->beginLinear = ep[i]->beginWeld = eventsTable[i*3+1];
		ep[i]->endConnector = ep[i]->endLinear = ep[i]->endWeld = eventsTable[i*3+2];


		if(eventsTable[i*3] == 0)
		{
			ep[i]->linearEvent = 1;
			ep[i]->weldEvent =0;
			ep[i]->connectorEvent = 0;

			getLinearFittingCoefficientsFromBegin(data, ep[i]->beginLinear, ep[i]->endLinear, ep[i]->beginLinear);
			ep[i]->a_linear = linearFittingParameters[0];
			ep[i]->b_linear = linearFittingParameters[1];
		}
		else if(eventsTable[i*3] == 3 || eventsTable[i*3] == 6)
		{
			ep[i]->linearEvent = 0;
			ep[i]->weldEvent =1;
			ep[i]->connectorEvent = 0;

			getLinearFittingCoefficientsFromBegin(data, ep[i]->beginLinear, ep[i]->endLinear, ep[i]->beginLinear);
			ep[i]->a_linear = linearFittingParameters[0];
			ep[i]->b_linear = linearFittingParameters[1];
		}
		else
		{
			ep[i]->linearEvent = 0;
			ep[i]->weldEvent =0;
			ep[i]->connectorEvent = 1;

			getLinearFittingCoefficientsFromBegin(data, ep[i]->beginLinear, ep[i]->endLinear, ep[i]->beginLinear);
			ep[i]->a_linear = linearFittingParameters[0];
			ep[i]->b_linear = linearFittingParameters[1];
		}
	}

	
	for(i=0; i<numberOfFoundEvents; i++) //Setting of the weld params;
	{
			ep[i]->center_weld = (double)(ep[i]->beginWeld+ep[i]->endWeld)/2.;
			ep[i]->width_weld = eventSize*.9;

			double A1;
			double A2; 
			double A3;
			double k;

			if(i>0 && i<numberOfFoundEvents-1 && ep[i-1]->linearEvent == 1 && ep[i+1]->linearEvent == 1)
			{
				A1 = ep[i-1]->linearF(ep[i]->beginWeld);
				A2 = ep[i+1]->linearF(ep[i]->beginWeld);
				A3 = ep[i+1]->linearF(ep[i]->endWeld);
				k = (ep[i-1]->b_linear + ep[i+1]->b_linear)/2.;
			}
			else if(i>0 && i<numberOfFoundEvents-1 && ep[i-1]->linearEvent == 1 && ep[i+1]->linearEvent != 1)
			{
				A1 = ep[i-1]->linearF(ep[i]->beginWeld);
				A2 = data[ep[i]->endWeld];
				A3 = data[ep[i]->endWeld];
				k = ep[i-1]->b_linear;
			}
			else if(i>0 && i<numberOfFoundEvents-1 && ep[i-1]->linearEvent != 1 && ep[i+1]->linearEvent == 1)
			{
				A1 = data[ep[i]->beginWeld];
				A2 = ep[i+1]->linearF(ep[i]->beginWeld);
				A3 = ep[i+1]->linearF(ep[i]->endWeld);
				k = ep[i+1]->b_linear;
			}
			else
			{
				A1 = data[ep[i]->beginWeld];
				A2 = data[ep[i]->endWeld];
				A3 = data[ep[i]->endWeld];
				k = 0.;

			}

			ep[i]->a_weld = (A1+A3)/2.;
			ep[i]->boost_weld = A2-A1;
			ep[i]->b_weld = k;
	}


	for(i=0; i<numberOfFoundEvents; i++) // Setting of the connector params;
	{
		double A1=0.;
		double A2=0.; 
		double ALet=0.;
		double width=0.;
		double centre=0.;
		double sigma1=0.;;
		double sigma2=0.;
		double sigmaFit = 0.;
		int j;

		if(i>0 && ep[i-1]->linearEvent == 1)
		{
			A1 = ep[i-1]->linearF(ep[i]->beginConnector);
		}
		else 
		{
			A1 = data[ep[i]->beginConnector];
		}

		if(i<numberOfFoundEvents-1 && ep[i+1]->linearEvent == 1)
		{
			A2 = ep[i+1]->linearF(ep[i]->endConnector);
		}
		else
		{
			A2 = data[ep[i]->endConnector];
		}

		ALet = A1;

		for(j=ep[i]->beginConnector; j<=ep[i]->endConnector && j<data_length; j++)
		{
			if(ALet<data[j]) ALet = data[j];
		}
		ALet = ALet-A1;

		if(i>0)
		{
			for(j=ep[i]->beginConnector; j<=ep[i]->endConnector && j<data_length; j++)
			{
				if(data[j]>A1+ALet/2.)
				{
					width = width+1.;
					centre = centre + j;
				}
			}
		}
		else
		{
			for(j=ep[i]->beginConnector; j<=ep[i]->endConnector && j<data_length; j++)
			{
				if(data[j]>A1+ALet-0.5)
				{
					width = width+1.;
					centre = centre + j;
				}
			}
		}

		if(width>0.1)
		{
			centre = centre/width;
		}
		else
		{
			centre = (ep[i]->beginConnector+ep[i]->endConnector)/2.;
		}

		sigma1 = (centre - ep[i]->beginConnector)/20.;
		sigma2 = (ep[i]->endConnector - centre)*(1. - formFactor);
		sigmaFit = (ep[i]->endConnector - centre)*formFactor;

		ep[i]->a1_connector = A1;
		ep[i]->a2_connector = A2;
		ep[i]->aLet_connector = ALet;
		ep[i]->width_connector = width;
		ep[i]->center_connector = centre;
		ep[i]->sigma1_connector = sigma1;
		ep[i]->sigma2_connector = sigma2;
		ep[i]->sigmaFit_connector = sigmaFit;
		ep[i]->k_connector = formFactor;

	}


	//coords of the weld:
	for(i=1; i<numberOfFoundEvents-1; i++)
	{
		if(ep[i]->weldEvent==1)
		{
			if(ep[i-1]->linearEvent==1)
			{
				ep[i]->beginWeld-=eventSize;
				if(ep[i]->beginWeld<0)
					ep[i]->beginWeld = 0;
			}
			if(ep[i+1]->linearEvent==1)
			{
				ep[i]->endWeld+=eventSize;
				if(ep[i]->endWeld>=lastNonZeroPoint)
					ep[i]->endWeld = lastNonZeroPoint-1;
			}
		}
	}




}





int *InitialAnalysis::getEventsTable()
{
	return eventsTable;
}

//------------------------------------------
double InitialAnalysis::wLet1(int arg)
{
	return (sin(arg*pi/eventSize))/wn1;
}


double InitialAnalysis::wLet2(int arg)
{
	return (sin(arg*pi/eventSize)*fabs((double)arg))/wn2;
}


double InitialAnalysis::wLet3(int arg)
{
	return (sin(arg*pi/eventSize)/(1. + fabs((double)arg)))/wn3;
}


double InitialAnalysis::wLet4(int arg)
{
	return (sin(arg*pi/eventSize)/(1. + sqrt(fabs((double)arg))))/wn4;
}


double InitialAnalysis::wLet5(int arg)
{
	if(arg<0) return -1./wn5;
	if(arg>0) return 1./wn5;
	return 0.;
}


double InitialAnalysis::wLet6(int arg)
{
	return (((double)arg)/eventSize)/wn6;
}


double InitialAnalysis::wLet7(int arg)
{
	if(arg>0) return  (double)(eventSize-arg)/wn7;
	if(arg<0) return -(double)(eventSize+arg)/wn7;
	return 0.;
}


double InitialAnalysis::wLet8(int arg)
{
	return (sin(arg*pi/eventSize) + ((double)arg)/eventSize/2.)/wn8;
}


double InitialAnalysis::wLet9(int arg)
{
	return (sin(arg*pi/eventSize/2.))/wn9;
}


double InitialAnalysis::wLet10(int arg)
{
	return (sin(arg*pi/eventSize/2.) - ((double)arg)/eventSize/2.)/wn10;
}

void InitialAnalysis::setNormalizingCoeffs()
{
	double n1 = 0.;
	double n2 = 0.;
	double n3 = 0.;
	double n4 = 0.;
	double n5 = 0.;
	double n6 = 0.;
	double n7 = 0.;
	double n8 = 0.;
	double n9 = 0.;
	double n10= 0.;


	for(int i=-eventSize; i<=eventSize; i++)
	{
		n1 = n1 + fabs(wLet1(i));
		n2 = n2 + fabs(wLet2(i));
		n3 = n3 + fabs(wLet3(i));
		n4 = n4 + fabs(wLet4(i));
		n5 = n5 + fabs(wLet5(i));

		n6 = n6 + fabs(wLet6(i));
		n7 = n7 + fabs(wLet7(i));
		n8 = n8 + fabs(wLet8(i));
		n9 = n9 + fabs(wLet9(i));
		n10= n10+ fabs(wLet10(i));
	}
	wn1 = n1/2.;
	wn2 = n2/2.;
	wn3 = n3/2.;
	wn4 = n4/2.;
	wn5 = n5/2.;
	wn6 = n6/2.;
	wn7 = n7/2.;
	wn8 = n8/2.;
	wn9 = n9/2.;
	wn10= n10/2.;
}


double InitialAnalysis::wLet(int arg)
{
	if(waveletType == 1) return wLet1(arg);
	if(waveletType == 2) return wLet2(arg);
	if(waveletType == 3) return wLet3(arg);
	if(waveletType == 4) return wLet4(arg);
	if(waveletType == 5) return wLet5(arg);
	if(waveletType == 6) return wLet6(arg);
	if(waveletType == 7) return wLet7(arg);
	if(waveletType == 8) return wLet8(arg);
	if(waveletType == 9) return wLet9(arg);
	if(waveletType == 10)return wLet10(arg);

	else return wLet1(arg);
}


void InitialAnalysis::getLinearFittingCoefficients(double *data_, int from, int to)
{
    double alfa=0., beta=0., gamma=0., dzeta=0., n=0.;
	int i;

    for(i=from; i<=to; i++)
   {

       beta = beta - data_[i]*((double)i);
       alfa = alfa + ((double)i)*((double)i);
       gamma = gamma + ((double)i);
       dzeta = dzeta - data_[i];
       n = n + 1.;
   }
    double a_ = (n*beta/gamma - dzeta)/(gamma - n*alfa/gamma);
    double b_ = -(alfa*a_ + beta)/gamma;

	linearFittingParameters[0] = b_;
	linearFittingParameters[1] = a_;

//	dispersia = 0;
//	n=0.;
//
//	for(i=begin; i<=end; i++)
//	{
//		dispersia += ((y[i]-(a_*i+b_))*(y[i]-(a_*i+b_)));
//		n=n+1.;
//	}
//
//	  dispersia/=n;
//	  dispersia = sqrt(dispersia);
//
//	  error = dispersia/sqrt(n);

}



void InitialAnalysis::getLinearFittingCoefficientsFromBegin(double *data_, int from, int to, int begin)
{
    double alfa=0., beta=0., gamma=0., dzeta=0., n=0.;
	int i;

    for(i=from; i<=to; i++)
   {

       beta = beta - data_[i]*((double)(i-begin));
       alfa = alfa + ((double)(i-begin))*((double)(i-begin));
       gamma = gamma + ((double)(i-begin));
       dzeta = dzeta - data_[i];
       n = n + 1.;
   }
    double a_ = (n*beta/gamma - dzeta)/(gamma - n*alfa/gamma);
    double b_ = -(alfa*a_ + beta)/gamma;

	linearFittingParameters[0] = b_;
	linearFittingParameters[1] = a_;

//	dispersia = 0;
//	n=0.;
//
//	for(i=begin; i<=end; i++)
//	{
//		dispersia += ((y[i]-(a_*i+b_))*(y[i]-(a_*i+b_)));
//		n=n+1.;
//	}
//
//	  dispersia/=n;
//	  dispersia = sqrt(dispersia);
//
//	  error = dispersia/sqrt(n);

}



bool InitialAnalysis::isPositive(double arg)
{
	if(arg>0.001) return true;
	return false;
}


bool InitialAnalysis::isNegative(double arg)
{
	if(arg<-0.001) return true;
	return false;
}


bool InitialAnalysis::isNonZero(double arg)
{
	if(fabs(arg)>0.001) return true;
	return false;
}


void InitialAnalysis::smoothArray(double *array, int from, int to, 
								  int pointsToSmooth, int arrayLength)
{
	int i;
	int j;
	double tmp;
	int norma;
	double *newArray = new double[arrayLength];

	for(i=from; i<=to && i<arrayLength; i++)
	{
		tmp = 0.;
		norma = 0;
		for(j=i-pointsToSmooth; j<=i+pointsToSmooth && j<arrayLength && j<=to; j++)
		{
			if(j>=from && j<=to)
			{
				tmp = tmp + array[j];
				norma ++;
			}
		}
		if(norma>0)
		{
			newArray[i] = tmp/norma;
		}
	}

	for(i=0; i<=from; i++)
	{
		newArray[i] = array[i];
	}
	for(i=to; i<arrayLength; i++)
	{
		newArray[i] = array[i];
	}

	delete []array;
	array = newArray;
}



double InitialAnalysis::maximum(double a, double b)
{
	if(a>b) return a;
	return b;
}


double InitialAnalysis::minimum(double a, double b)
{
	if(a<b) return a;
	return b;
}




void InitialAnalysis::convolutionOfNoise(int n_points)
{
	int i;
	int j;

	int n;
	double meanValue;

	for(i=0; i<data_length; i++)
	{
		n=0;
		meanValue = 0.;
		for(j=i; j<i+n_points && j<data_length; j++)
		{
			meanValue += noise[j];
			n++;
		}
		if(n>0) meanValue/=n;

		noise[i] = meanValue;
	}
}



void InitialAnalysis::setNNWindow(int from, int to, double *window, int eventWindowSize)
{

	int dist = eventSize*2;
	int delta = dist-(to-from);

	if(delta > 0)
	{
		from = from - (int)(delta/2.);
		to   = to   + (int)(delta/2.);
	}
	else if(to-from>eventSize*2.8)
	{
		to = from + (int)(eventSize*2.8);
	}
	
	
//	int myFrom = from;
//	int myTo = to;
	
	double dIndex = (double)(to - from)/eventWindowSize;
	
	double counter = from;


	for(int i=0; i<eventWindowSize; i++)
	{
		window[i] = data[(int)(counter+0.5)] - meanAttenuation*(counter-from)/eventSize;
		counter += dIndex;

	}

	shiftEventToCentre(window, eventWindowSize);
	return;
}



void InitialAnalysis::setNNWindow_(int from, int to, double *window, int eventWindowSize)
{
	int dist = (int)(eventSize*2.5);
	int delta = dist-(to-from);

	if(delta > 0)
	{
		from = from - (int)(delta/2.);
		to   = to   + (int)(delta/2.);
	}
//	else if(to-from>eventSize*2.8)
//	{
//		to = from + (int)(eventSize*2.8);
//	}
	
	
//	int myFrom = from;
//	int myTo = to;
	
	double dIndex = (double)(to - from)/eventWindowSize;
	
	double counter = from;


	for(int i=0; i<eventWindowSize; i++)
	{
		window[i] = data[(int)(counter+0.5)] - meanAttenuation*(counter-from)/eventSize;
		counter += dIndex;

	}

	shiftEventToCentre(window, eventWindowSize);
	return;
}


void InitialAnalysis::shiftEventToCentre(double *wnd, int wndSize)
{
	double summ = 0.;
	int i;

	for(i=0; i<wndSize; i++)
	{
		summ = summ + wnd[i];
	}

	summ = summ/wndSize;

	for(i=0; i<wndSize; i++)
	{
		wnd[i] = wnd[i] - summ;
	}

	double deviation = 0;

	for(i=0; i<wndSize; i++)
	{
		deviation = deviation + fabs(wnd[i]);
	}

	deviation = deviation/wndSize;
	if(deviation <0.000001) deviation = 1.;

	for(i=0; i<wndSize; i++)
	{
		wnd[i] = wnd[i]/deviation;
	}
}


void InitialAnalysis::setSigmaSquared()
{
	for(int i=0; i<data_length-1; i++)
	{
		sigmaSquared[i] = fabs(data[i+1]-data[i]);

		if(sigmaSquared[i]<minimalThreshold)
		{
			sigmaSquared[i] = minimalThreshold;
		}
		else if(sigmaSquared[i]>maximalNoise)
		{
			sigmaSquared[i]=maximalNoise;
		}

		sigmaSquared[i] = sigmaSquared[i]*sigmaSquared[i];
	}
	sigmaSquared[data_length-1] = 1.;
}



/*
void InitialAnalysis::excludeNonRecognizedEvents()
{
	int i;

	for(i=0; i<numberOfFoundEvents*3; i+=3)
	{
		if(eventsTable[i] == 3 || eventsTable[i] == 6) //case of weld;
		{
			double A1;
			double A2; 

			if(i/3>0 && i/3<numberOfFoundEvents-1 && eventsTable[i-3] == 0 && eventsTable[i+3] == 0)
			{
				getLinearFittingCoefficients(data, eventsTable[i-2], eventsTable[i-1]);
				A1 = linearFittingParameters[0] + linearFittingParameters[1]*eventsTable[i+1];

				getLinearFittingCoefficients(data, eventsTable[i+4], eventsTable[i+5]);
				A2 = linearFittingParameters[0] + linearFittingParameters[1]*eventsTable[i+1];
			}
			else if(i/3>0 && i/3<numberOfFoundEvents-1 && eventsTable[i-3] == 0 && eventsTable[i+3] != 0)
			{
				getLinearFittingCoefficients(data, eventsTable[i-2], eventsTable[i-1]);
				A1 = linearFittingParameters[0] + linearFittingParameters[1]*eventsTable[i+1];

				A2 = data[eventsTable[i+2]] - meanAttenuation*eventSize;
			}
			else if(i/3>0 && i/3<numberOfFoundEvents-1 && eventsTable[i-3] != 0 && eventsTable[i+3] == 0)
			{
				A1 = data[eventsTable[i+1]];

				getLinearFittingCoefficients(data, eventsTable[i+4], eventsTable[i+5]);
				A2 = linearFittingParameters[0] + linearFittingParameters[1]*eventsTable[i+1];
			}
			else
			{
				A1 = data[eventsTable[i+1]];

				A2 = data[eventsTable[i+2]] - meanAttenuation*eventSize;
			}

			if(fabs(A1-A2)<minimalWeld)
			{
				eventsTable[i] = 0;
			}


		}
	}

	siewLinearParts();
}
*/
