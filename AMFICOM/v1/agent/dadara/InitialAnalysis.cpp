// InitialAnalysis.cpp: implementation of the InitialAnalysis class.
//
//////////////////////////////////////////////////////////////////////

#include "InitialAnalysis.h"
#include "MathRef.h"
#include "NeuroAnalyser.h"
#include "Histogramm.h"

#ifdef DEBUG_INITIAL_ANALYSIS
#include <time.h>
#include <sys/time.h>
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

InitialAnalysis::InitialAnalysis( double *data,					//точки рефлектограммы
								  int data_length,				//число точек
								  double delta_x,				//расстояние между точками (м)
							 	  double minimalThreshold,		//минимальный уровень события
								  double minimalWeld,			//минимальный уровень неотражательного события
								  double minimalConnector,		//минимальный уровень отражательного события
								  double minimalEndingSplash,	//минимальный уровень последнего отражения
								  double maximalNoise,			//максимальный уровень шума
								  int waveletType,				//номер используемого вейвлета
								  double formFactor,			//формфактор отражательного события
								  int findEnd)					//характерная длина события, определенная в java (реально не используется)
{
#ifdef DEBUG_INITIAL_ANALYSIS
	timeval tv;
	gettimeofday(&tv, NULL);
	tm* t = localtime(&tv.tv_sec);
	int size = 9 + 15 + 1 + 14 + 1 + 3 + 1;
	char* filename = new char[size];
	sprintf(filename, ".//logs//%04d%02d%02d%02d%02d%02d-InitialAnalysis.log", 1900 + t->tm_year, 1 + t->tm_mon, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
	filename[size - 1] = 0;
	this->str = fopen(filename, "a");
	delete[] filename;
#endif

	this->delta_x				= delta_x;
	this->minimalThreshold		= minimalThreshold;
	this->minimalWeld			= minimalWeld;
	this->minimalConnector		= minimalConnector;
	this->minimalEndingSplash	= minimalEndingSplash;
	this->maximalNoise			= maximalNoise;
	this->waveletType			= waveletType;
	this->formFactor			= formFactor;
	this->findEnd				= findEnd;
	this->data_length			= data_length;
	this->data					= data;


	{ //Inicializing of the arrays;
		wn_c	= new double[10];
		wn_w	= new double[10];
		transC	= new double[data_length];
		transW	= new double[data_length];
		noise	= new double[data_length];
	}

	performAnalysis();
}




InitialAnalysis::~InitialAnalysis()
{
	delete[] wn_w;
	delete[] wn_c;
	delete[] transC;
	delete[] transW;
	delete[] noise;

	epVector.clear();

	delete[] ep;

#ifdef DEBUG_INITIAL_ANALYSIS
	fclose(this->str);
#endif
}


void InitialAnalysis::performAnalysis()
{
	correctDataArray();

	lastNonZeroPoint = getLastPoint();
	
	calcEventSize();

	getNormalizingCoeffs(wn_c, evSizeC);
	getNormalizingCoeffs(wn_w, evSizeW);
#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "Reflecting event size = %d\n", evSizeC);
		fprintf(this->str, "Nonreflecting event size = %d\n", evSizeW);
#endif

//	FILE * f = fopen("c:\\io.log", "w");

	// вычисляем уровень шума
	getNoise(noise, evSizeC);

/*	double *denoised = new double[data_length];
	for (int i = 0; i < data_length; i++)
		denoised[i] = data[i] - noise[i];
*/
	// выполняем вейвлет-преобразование
	performTransformation(data, data_length, transC, evSizeC, wn_c);
	performTransformation(data, data_length, transW, evSizeW, wn_w);

//	delete[] denoised;

	// вычитаем из коэффициентов преобразования(КП) постоянную составляющую (среднее затухание)
	meanAttenuation = shiftToZeroAttenuation(transC);
	shiftToZeroAttenuation(transW);

	// устанавливаем в 0 КП, которые меньше уровня шума или минимального уровня события
	setNonZeroTransformation(transC, minimalThreshold);
	setNonZeroTransformation(transW, minimalThreshold);
	// определяем координаты и типы событий по КП
	createEventParams(
		transC,
		evSizeC,
		0, 
		min(lastNonZeroPoint + evSizeC, data_length - 10), 
		epVector);

#ifdef DEBUG_INITIAL_ANALYSIS
	EPLIST::iterator it;
#endif
/*	fprintf(f, "\n(0) after createEventParams()\n");
	for (it = epVector.begin(); it != epVector.end(); it++)
	{
		fprintf(f, "#%d, type = %d, begin = %d, end = %d\n", it->n, it->type, it->begin, it->end);
	}*/

	// корректируем координаты отражательных событий
	correctConnectorsCoords();
/*	fprintf(f, "\n(1) after correctConnectorsCoords()\n");
	for (it = epVector.begin(); it != epVector.end(); it++)
	{
		fprintf(f, "#%d, type = %d, begin = %d, end = %d\n", it->n, it->type, it->begin, it->end);
	}*/

	findAdditionalWelds(transW, evSizeW, epVector);

/*	fprintf(f, "\n(2) after findAdditionalWelds()\n");
	for (it = epVector.begin(); it != epVector.end(); it++)
	{
		fprintf(f, "#%d, type = %d, begin = %d, end = %d\n", it->n, it->type, it->begin, it->end);
	}*/

	// исключаем неидентифицированные события
	excludeNonRecognizedEvents();

	/*fprintf(f, "\n(1) after correctConnectorsCoords()\n");
	for (it = epVector.begin(); it != epVector.end(); it++)
	{
		fprintf(f, "#%d, type = %d, begin = %d, end = %d\n", it->n, it->type, it->begin, it->end);
	}*/

	// исключаем события с длиной, меньшей половины характерного размера
	excludeShortEvents(max(evSizeC/2, 10), max(evSizeW/2, 8), max(evSizeC/2, 10));
	siewLinearParts();
	excludeShortEvents(max(evSizeC/2, 10), max(evSizeW/2, 8), max(evSizeC/2, 10));
	siewLinearParts();

/*	fprintf(f, "\n(3) after excludeShortEvents()\n");
	for (it = epVector.begin(); it != epVector.end(); it++)
	{
		fprintf(f, "#%d, type = %d, begin = %d, end = %d\n", it->n, it->type, it->begin, it->end);
	}*/

	setEventParams();
printf("-------------------------\n");
	// корректируем конец волокна согласно минимального отражения
	correctEnd();
printf("*************************\n");
//	fclose (f);
#ifdef DEBUG_INITIAL_ANALYSIS
	for (it = epVector.begin(); it != epVector.end(); it++)
	{
		if(it->end <= it->begin)
			fprintf(this->str, "error in event bounds: x1 = %d, x2 = %d\n", it->begin, it->end);
	}
#endif
}

int InitialAnalysis::getEventSize()
{
	return evSizeC;
}

EventParams **InitialAnalysis::getEventParams()
{
	return ep;
}

int InitialAnalysis::getEventsCount()
{
	return epVector.size();
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
}

int InitialAnalysis::getLastPoint()
{
	int lastPoint = data_length-1;
	for(int i=300; i<data_length; i++)
	{
		if(data[i]<1.)
		{
			lastPoint = i;
			break;
		}
	}
	
	if(lastPoint + 10 < data_length)
		lastPoint += 10;	
	return lastPoint;
}


void InitialAnalysis::calcEventSize()
{
	int eventSize = 0;
	int maximumIndex = 4;
	int i;

	double maximum = data[maximumIndex];
	
	for (i = 0; i < min(300, data_length); i++)
	{
		if(data[i]>maximum)
		{
			maximum = data[i];
			maximumIndex = i;
		}
	}

	eventSize = maximumIndex;

	for(i = maximumIndex; i < data_length; i++)
	{
		if(data[i] < maximum - 0.5)
		{
			eventSize = i;
			break;
		}
	}

	eventSize = (int)(eventSize*0.8);

	if(eventSize<4)
		eventSize = 4;

	evSizeC = eventSize;
	evSizeW = 2*eventSize/5;
}


void InitialAnalysis::performTransformation(double* y, double y_length, double *trans, int freq, double* norma)
{
	double tmp;

	for(int i = 0; i < y_length; i++)
	{
		tmp = 0.;
		for(int j = i - freq; j < min(i + freq + 1, y_length); j++)
		{
			if(j>=0)
				tmp = tmp + y[j] * wLet(j - i, freq, norma);
		}
		trans[i] = tmp;
	}
}



void InitialAnalysis::getNoise(double *noise, int freq)
{
	int i;

// First, we set that noise is euqal to the first derivative.

	for(i = 0; i < data_length - 1; i++)
	{
		noise[i] = fabs(data[i] - data[i+1]);
		if (noise[i] > maximalNoise) 
			noise[i] = maximalNoise;
	}
	noise[data_length-1] = 0.;
    
	double EXP;

// Cut of the prompt-peaks with exponent.
	for(i=0; i < lastNonZeroPoint; i++)
	{
		EXP = (exp((double)i / lastNonZeroPoint) - 1.) * maximalNoise / (exp(1.) - 1.);
		if (noise[i] > EXP) 
			noise[i] = EXP;
	}

	convolutionOfNoise(freq);
}


double InitialAnalysis::shiftToZeroAttenuation(double *trans)
{
	Histogramm* histo = new Histogramm(-0.5, 0, 100);
	//возможное затухание находится в пределах [0; -0.5] дБ

	histo->init(trans, data_length, 0, lastNonZeroPoint-1);
	double meanAtt = histo->getMaximumValue();
	delete histo;

	for(int i=0; i<lastNonZeroPoint; i++)
		trans[i] = trans[i] - meanAtt;

#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(this->str, "mean attenuation = %f\n", meanAtt);
#endif
	return meanAtt;
}


void InitialAnalysis::setNonZeroTransformation(double* trans, double threshold)
{
	int i;
	for(i = 0; i < data_length; i++)
	{
		if(fabs(trans[i]) < max(threshold, noise[i] / 2.))
			trans[i] = 0.;
	}

	//Excluding of the exidental zero points;
	for(i = 1; i < data_length - 1; i++)
	{
		if(fabs(trans[i]) < threshold)
		{
			if(fabs(trans[i-1]) > threshold && 
		       fabs(trans[i+1]) > threshold)
					trans[i] = (trans[i-1] + trans[i+1]) / 2.;
		}
	}
}


void InitialAnalysis::createEventParams(double *trans, int evSize, int start, int end, EPLIST &vector)
{
	int halfWidth = evSize / 2;
	if(halfWidth < 1) 
		halfWidth = 1;

	int type;

    int x1; // начало события
    int x2; // конец события

	int k1; // середина первого участка
	int k2; // середина второго участка
	int k3; // середина третьего участка

	int i;
	int j;
	int k; 
	int s;

	double max1;
	double max2;
	double max3;

	for(i = start; i < end; i += (x2 - x1))
//	for(i = 0; i < min (lastNonZeroPoint + 2, data_length - 10); i += (x2 - x1))
	{
		max1 = 0;
		max2 = 0;
		max3 = 0;

		j = i + 1;
		while(sign(trans[i]) == sign(trans[j]) && j < end)
		{
			if(fabs(max1) < fabs(trans[j]))
				max1 = trans[j];
			j++;
		}
		k = j + 1;
        while(sign(trans[j]) == sign(trans[k]) && k < end)
		{
			if(fabs(max2) < fabs(trans[k])) 
				max2 = trans[k];
			k++;
		}
		s = k + 1;
        while(sign(trans[k]) == sign(trans[s]) && s < end)
		{
			if(fabs(max3) < fabs(trans[s]))
				max3 = trans[s];
			s++;
		}
		k1 = (j + i) / 2;
		k2 = (k + j) / 2;
		k3 = (s + k) / 2;

		x1 = i;
        if(fabs(max1) < minimalThreshold) // linear part
        {
			type = EventParams::LINEAR;
			x2 = j;
        }
        else if(max1 > minimalConnector && 
				max2 < -minimalConnector)  // connector
		{
			type = EventParams::CONNECTOR;
			x2 = k;
		}
		else if(max1 > minimalConnector && 
				fabs(max2) < minimalThreshold && 
				max3 < -minimalConnector && 
				k - j < (int)(halfWidth * 1.5)) //connector
		{
			type = EventParams::CONNECTOR;
			x2 = s;
		}
		else if(max1 > noise[k1] * 3 && 
				max2 < -noise[k2] * 3 && 
				max1 > minimalConnector && 
				max2 < -minimalConnector) //reflection (connector, anyway)
		{
			type = EventParams::CONNECTOR;
			x2 = k;
		}
		else if(max1 > noise[k1] * 3 && 
				max3 < -noise[k3] * 3 &&
				max1 > minimalConnector && 
				max3 < -minimalConnector &&	
				k - j < (int)(halfWidth * 1.5) && 
				fabs(max2) < .00001)//reflection (connector, anyway)
		{
			type = EventParams::CONNECTOR;
			x2 = s;
		}
		else if(max1 > minimalConnector && 
				max2 < minimalConnector && 
				fabs((max1 + max2) / (max1 - max2)) < .5) //reflection		
		{
			type = EventParams::CONNECTOR;
			x2=k;
		}
		else if(max1 > minimalConnector && 
				max3 < minimalConnector && 
				fabs((max1 + max3) / (max1 - max3)) < .5 &&
				k - j < (int)(halfWidth * 1.5))  //reflection
		{
			type = EventParams::CONNECTOR;
			x2 = s;
		}
		else if(max1 > minimalWeld * .8 || 
				max1 < -minimalWeld * .8) //weld
		{
			type = EventParams::SPLICE;
			x2 = j;
		}
		else //linear
		{
			type = EventParams::LINEAR;
			x2 = j;
		}

		if (x1 < lastNonZeroPoint)
		{
			EventParams *ep = new EventParams();
			ep->n = vector.size();
			ep->type = type;
			ep->begin = x1;
			ep->end = x2;
			vector.push_back(*ep);
		}
#ifdef DEBUG_INITIAL_ANALYSIS
	if (x2 <= x1)
		fprintf(this->str, "(!!!!!!!!!Error setting ep.begin = %d; ep.end =  %d\n", x1, x2);
#endif
	}
}

void InitialAnalysis::findAdditionalWelds(double *trans, int evSize, EPLIST &vector)
{
	int counter = 1000;
	int del_count = 0;
	EPLIST tmpVector;
	EPLIST::iterator it;

//printf("(0) vector size: %d\n", vector.size());

	it = vector.begin();
	while (it != vector.end()) {
		EventParams *ep = &(*it);
//printf("vector size: %d, n: %d\n", vector.size(), ep->n);
		// ищем сварки на широких сварных участках
		if (ep->type == EventParams::SPLICE && ep->end - ep->begin > evSize) {
			createEventParams(trans, evSize, ep->begin, ep->end, tmpVector);
//printf("tmpVector size: %d\n", tmpVector.size());

			if (tmpVector.size() > 1) {
				for (EPLIST::iterator in = tmpVector.begin(); in != tmpVector.end(); in++) {
					in->n = counter++;
					EventParams* ep1 = (*in).clone();
//printf("inserting n: %d\n", ep1->n);
					vector.insert(it, *ep1);
				}

				it ++;
				vector.remove(*ep);
				del_count++;
				tmpVector.clear();
				continue;
			}
			else {
				tmpVector.clear();
			}
		}
		it ++;
	}

/*
	for (it = vector.begin(); it != vector.end(); it++)
	{
		EventParams *ep = &(*it);
//printf("vector size: %d, n: %d\n", vector.size(), ep->n);
		// ищем сварки на широких сварных участках
		if (ep->type == EventParams::SPLICE &&
			ep->end - ep->begin > evSize)
		{
			createEventParams(trans, evSize, ep->begin, ep->end, tmpVector);
//printf("tmpVector size: %d\n", tmpVector.size());

			if (tmpVector.size() > 1)
			{
				for (EPLIST::iterator in = tmpVector.begin(); in != tmpVector.end(); in++)
				{
					in->n = counter++;
					EventParams* ep1 = (*in).clone();
printf("inserting n: %d\n", ep1->n);
					vector.insert(it, *ep1);
				}

				//vector.insert(it, tmpVector.begin(), tmpVector.end());
				vector.erase(it);//remove (vector.begin(), vector.end(), ep);
				del_count++;

			}
			tmpVector.clear();
		
		}*/
	/*	// ищем сварки на длинных линейных участках
		else if (ep->type == EventParams::LINEAR &&
				 ep->end - ep->begin > 3 * evSize)
		{
			createEventParams(trans, evSize, ep->begin, ep->end, tmpVector);
			if (tmpVector.size() > 1)
			{
				for (EPLIST::iterator in = tmpVector.begin(); in != tmpVector.end(); in++)
					in->n = counter++;

				vector.insert(it, tmpVector.begin(), tmpVector.end());
				remove (vector.begin(), vector.end(), ep);
				del_count++;
			}
			tmpVector.clear();
		}/
		// ищем сварки перед коненкторами
		else if (ep->type == EventParams::LINEAR)
		{
			it++;
			if (it != vector.end() && it->type == EventParams::CONNECTOR)
			{
				createEventParams(trans, evSize, ep->begin, ep->end, tmpVector);
				if (tmpVector.size() > 1)
				{
					for (EPLIST::iterator in = tmpVector.begin(); in != tmpVector.end(); in++)
						in->n = counter++;

					vector.insert(it, tmpVector.begin(), tmpVector.end());
					remove (vector.begin(), vector.end(), ep);
					del_count++;
				}
				tmpVector.clear();
			}
			it--;
		}
		// ищем сварки после коненкторов
		else if (ep->type == EventParams::CONNECTOR)
		{
			it++;
			if (it != epVector.end() && it->type == EventParams::LINEAR)
			{
				EventParams *ep_next = &(*it);
				createEventParams(trans, evSize, ep_next->begin, ep_next->end, tmpVector);
				if (tmpVector.size() > 1)
				{
					for (EPLIST::iterator in = tmpVector.begin(); in != tmpVector.end(); in++)
						in->n = counter++;

					vector.insert(it, tmpVector.begin(), tmpVector.end());
					remove (vector.begin(), vector.end(), ep_next);
					del_count++;
				}
				tmpVector.clear();
			}
			it--;
		}*/
		
/*	}*/

	fixEndEvents(del_count);

	counter = 0;
	for (it = vector.begin(); it != vector.end(); it++)
		it->n = counter++;
	
	siewLinearParts();

	counter = 0;
	for (it = vector.begin(); it != vector.end(); it++)
		it->n = counter++;
}
  
void InitialAnalysis::siewLinearParts() {
	EPLIST::iterator it = epVector.begin();
	while (it != epVector.end()) {
		EventParams* ep = &(*it);
		if (ep->type == EventParams::LINEAR) {
			it ++;
			if (it != epVector.end()) {
				EventParams* ep_next = &(*it);
				if (ep_next->type == EventParams::LINEAR) {
					ep->end = ep_next->end;
					it --;
					epVector.remove(*ep_next);
					continue;
				}
				if (ep->end > ep_next->begin) {
					ep->end = ep_next->begin;
					if (ep->begin > ep->end)
						ep->begin = ep->end;
				}
			}
			it --;
		}
		it ++;
	}

/*
	int counter;
	while (true)
	{
		counter = 0;
		for (EPLIST::iterator it = epVector.begin(); it != epVector.end(); it++)
		{
			EventParams *ep = &(*it);
			if (ep->type == EventParams::LINEAR)
			{
				it++;
				if (it != epVector.end())
				{
					EventParams* ep_next = &(*it);
					if (ep_next->type == EventParams::LINEAR)
					{
						ep->end = ep_next->end;
						epVector.remove(*ep_next);
						counter++;
					}
					else if (ep->end > ep_next->begin)
					{
						ep->end = ep_next->begin;
						if (ep->begin > ep->end)
							ep->begin = ep->end;
					}
				}
				it--;
			}
		}
		if (counter == 0)
			break;
		else
			fixEndEvents(counter);
	}*/
}

void InitialAnalysis::fixEndEvents(int count)
{/*
	if (count == 0)
		return;

	EPLIST::iterator it = epVector.end();
	it--;
	for (; it != epVector.begin(); it--)
	{
		if (count > 0)
		{
			epVector.pop_back();
			count--;
		}
		else
			break;
	}*/
}

void InitialAnalysis::excludeShortEvents(int linearLength, int weldLength, int connectorLength) {
	int counter = 0;
	bool key;
	EPLIST::iterator it = epVector.begin();
	while (it != epVector.end()) {
		EventParams* ep = &(*it);
		key = false;

		if (ep->type == EventParams::LINEAR) {
			if (ep->end - ep->begin < linearLength)
					key = true;
		}
		else
			if (ep->type == EventParams::SPLICE) {
				if (ep->end - ep->begin < weldLength)
					key = true;
			}
			else
				if (ep->type == EventParams::CONNECTOR) {
					if (ep->end - ep->begin < connectorLength)
						key = true;
				}

		if (key) {
			it --;
			EventParams* ep_last = &(*it);
			it ++;
			it ++;
			EventParams* ep_next = &(*it);
			if (it != epVector.end()) {
				int delta = ep->end - ep->begin;
				ep_last->end += delta;
				ep_next->begin = ep_last->end;
			}
			else {
				ep_last->end = ep->end;
			}
			it --;

			it ++;
			epVector.remove(*ep);
			counter++;
			key = false;
			continue;
		}
		key = false;
		it ++;
	}
	fixEndEvents(counter);

/*
	int counter = 0;
	bool key;
	EPLIST::iterator it = epVector.begin();
	for (it++; it != epVector.end(); it++)
	{
		EventParams* ep = &(*it);
		key = false;

		if (ep->type == EventParams::LINEAR)
		{
			if (ep->end - ep->begin < linearLength)
					key = true;
		}
		else if (ep->type == EventParams::SPLICE)
		{
			if (ep->end - ep->begin < weldLength)
					key = true;
		}
		else if (ep->type == EventParams::CONNECTOR)
		{
			if (ep->end - ep->begin < connectorLength)
					key = true;
		}

		if (key)
		{
			it--;
			EventParams* ep_last = &(*it);
			it++;
			it++;
			EventParams* ep_next = &(*it);
			if (it != epVector.end())
			{
				int delta = ep->end - ep->begin;
				ep_last->end += delta;
				ep_next->begin = ep_last->end;
			}
			else
			{
				ep_last->end = ep->end;
			}
			it--;
			epVector.remove(*ep);
			counter++;
		}
		key = false;
	}
	fixEndEvents(counter);*/

	//siewLinearParts();
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

	if(na->readWeightsAndShiftsFromSingleFile(fileName))
	{
		for (EPLIST::iterator it = epVector.begin(); it != epVector.end(); it++)
		{
			EventParams* ep = &(*it);
			setNNWindow(ep->begin, ep->end, window, networkEventWindowSize);
			
			///////////////////////////////////
		//	ep->type = na->getEventType2(window); ///// 
			///////////////////////////////////
		}
	}
		
	delete []window;
	delete na;

	//siewLinearParts();
}

void InitialAnalysis::correctConnectorsCoords()
{
	double A1;
	double d[2];

	EPLIST::iterator it = epVector.begin();
	for (it++; it != epVector.end(); it++)
	{
		EventParams* ep = &(*it);
		if (ep->type == EventParams::CONNECTOR)
		{
			// передний фронт коннектора
			it--;
			EventParams* ep_last = &(*it);
			if (ep_last->type == EventParams::LINEAR &&
				ep_last->end - ep_last->begin > 3)
			{
				getLinearFittingCoefficients(data, ep_last->begin, ep_last->end, 0, d);
				A1 = d[0] + d[1] * ep->begin;			
			}
			else
			{
				A1 = data[ep->begin];
			}
			it++;

			for(int i = ep->begin; i < ep->end; i++)
			{
				if(data[i] - A1 > fabs(3 * noise[i]))
				{
					ep->begin = i - 3;
					ep_last->end = i - 3;
					// inserted by Stas to correct error x2 < x1
					if (ep_last->end <= ep_last->begin)
					{
						#ifdef DEBUG_INITIAL_ANALYSIS
							fprintf (this->str, "correctConnectorsCoords():\n\t correcting end from %d to %d\n", ep_last->begin, ep_last->end-1);
						#endif

						ep_last->begin = ep_last->end - 1;
					}
					break;
				}
			}

			// задний фронт коннектора
	/*		it++;
			if (it != epVector.end()
				//&& it->type == EventParams::LINEAR 
				//&& it->end - it->begin > 3)
				)
			{
				fprintf(f, "\nevent = %d\n", ep->n);

				EventParams* ep_next = &(*it);
				for(int i = ep->end; i > ep->begin; i--)
				{
					double d[2];
					getLinearFittingCoefficients(data, i, ep_next->end, i, d);
					fprintf(f, "data[%d] = %f, d[0] = %f\n", i, data[i],d[0]);
					if (data[i] - d[0] > minimalWeld + fabs(2 * noise[i]))
					{
						fprintf(f, "abs(***) = %f > %f; ep->end = %d\n", data[i] - d[0], minimalWeld + fabs(2 * noise[i]), i + 3);
						ep->end = i + 3;
						ep_next->begin = i + 3;
						ep_next->a_linear = d[0];
						ep_next->b_linear = d[1];
						break;
					}
				}
			}
			it--;*/
		}
	}
}

void InitialAnalysis::correctEnd()
{
	EPLIST::iterator it;
	if(findEnd <=0)
		return;
	
	int l = (epVector.back()).begin + evSizeC * 2;
#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(this->str, "end point before correction = %d (%.3fkm)\n", l, delta_x * l / 1000.);
	fprintf(this->str, "number of events before correction = %d\n", epVector.size());
#endif

	int counter = 0;
	it = epVector.end();
	it --;
	while (it != epVector.begin()) {
		EventParams* ep = &(*it);
#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "event #%d, has type %d\n", ep->n, ep->type);
#endif
		if (ep->type == EventParams::CONNECTOR
				&& ep->aLet_connector > minimalEndingSplash
				&& ep->a1_connector >= 0
				&& ep->a2_connector >= 0
				&& ep->begin <= lastNonZeroPoint
				&& ep->end - ep->begin > evSizeC) {
#ifdef DEBUG_INITIAL_ANALYSIS
			fprintf(this->str, "last event = #%d, with start = %d, end = %d \n", ep->n, ep->begin, ep->end);
			fprintf(this->str, "\taLet = %f, a1 = %f, a2 = %f, minEnd = %f \n", ep->aLet_connector, ep->a1_connector, ep->a2_connector, minimalEndingSplash);
#endif
			l = min (lastNonZeroPoint - 1, min (ep->end, ep->begin + evSizeC * 2));
			if (l >= lastNonZeroPoint)
				l = lastNonZeroPoint-1;
			ep->end = l;
			break;
		}
		else {
			it --;
			epVector.remove(*ep);
			counter ++;
			continue;
		}
	}
	fixEndEvents(counter);
/*
	int counter = 0;
	it = epVector.end();
	it--;
	for (; it != epVector.begin(); it--)
	{
		EventParams* ep = &(*it);
#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "event #%d, has type %d\n", ep->n, ep->type);
#endif
		if (ep->type == EventParams::CONNECTOR &&
			ep->aLet_connector > minimalEndingSplash &&
			ep->a1_connector >= 0 &&
			ep->a2_connector >= 0 &&
			ep->begin <= lastNonZeroPoint)// && 
//			ep->end <= lastNonZeroPoint)
		{
#ifdef DEBUG_INITIAL_ANALYSIS
			fprintf(this->str, "last event = #%d, with start = %d, end = %d \n", ep->n, ep->begin, ep->end);
			fprintf(this->str, "\taLet = %f, a1 = %f, a2 = %f, minEnd = %f \n", ep->aLet_connector, ep->a1_connector, ep->a2_connector, minimalEndingSplash);
#endif

			l = min (lastNonZeroPoint - 1, min (ep->end, ep->begin + evSizeC * 2));

			if (l >= lastNonZeroPoint)
				l = lastNonZeroPoint-1;
			ep->end = l;
			break;
		}
		else
		{
			epVector.remove(*ep);
			counter++;
		}
	}
	fixEndEvents(counter);
*/
#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "end point after correction = %d (%.3fkm)\n", l, delta_x * l / 1000.);
		fprintf(this->str, "number of events after correction = %d\n", epVector.size());
#endif
}


void InitialAnalysis::setEventParams()
{
	int counter = 0;
	unsigned int i;
	ep = new EventParams *[epVector.size()];
	
	double *d = new double[2];
	for (EPLIST::iterator it = epVector.begin(); it != epVector.end(); it++)
	{
		getLinearFittingCoefficients(data, it->begin, it->end, it->begin, d);
		it->a_linear = d[0];
		it->b_linear = d[1];
		ep[counter++] = &(*it);
	}
	delete[] d;

	for(i = 0; i < epVector.size(); i++) //Setting of the weld params;
	{

		ep[i]->center_weld = (double)(ep[i]->begin+ep[i]->end)/2.;
		ep[i]->width_weld = evSizeC*.9;

		double A1;
		double A2; 
		double A3;
		double k;

		if(i>0 && i<epVector.size()-1 && ep[i-1]->type == EventParams::LINEAR && ep[i+1]->type == EventParams::LINEAR)
		{
			A1 = ep[i-1]->linearF(ep[i]->begin);
			A2 = ep[i+1]->linearF(ep[i]->begin);
			A3 = ep[i+1]->linearF(ep[i]->end);
			k = (ep[i-1]->b_linear + ep[i+1]->b_linear)/2.;
		}
		else if(i>0 && i<epVector.size()-1 && ep[i-1]->type == EventParams::LINEAR && ep[i+1]->type != EventParams::LINEAR)
		{
			A1 = ep[i-1]->linearF(ep[i]->begin);
			A2 = data[ep[i]->end];
			A3 = data[ep[i]->end];
			k = ep[i-1]->b_linear;
		}
		else if(i>0 && i<epVector.size()-1 && ep[i-1]->type != EventParams::LINEAR && ep[i+1]->type == EventParams::LINEAR)
		{
			A1 = data[ep[i]->begin];
			A2 = ep[i+1]->linearF(ep[i]->begin);
			A3 = ep[i+1]->linearF(ep[i]->end);
			k = ep[i+1]->b_linear;
		}
		else
		{
			A1 = data[ep[i]->begin];
			A2 = data[ep[i]->end];
			A3 = data[ep[i]->end];
			k = 0.;
		}

		ep[i]->a_weld = (A1+A3)/2.;
		ep[i]->boost_weld = A2-A1;
		ep[i]->b_weld = k;
	}

	for(i = 0; i < epVector.size(); i++) // Setting of the connector params;
	{
		double A1=0.;
		double A2=0.;
		double ALet=0.;
		double width=0.;
		double width_40=0.;
		double width_70=0.;
		double width_90=0.;
		int st = 0;
		double centre=0.;
		double sigma1=0.;
		double sigma2=0.;
		double sigmaFit = 0.;
		int j;

		if(i > 0 && ep[i-1]->type == EventParams::LINEAR)
		{
			A1 = ep[i-1]->linearF(ep[i]->begin);
		}
		else 
		{
			A1 = data[ep[i]->begin];
		}

		if(i < epVector.size() - 1 && ep[i+1]->type == EventParams::LINEAR)
			A2 = ep[i+1]->linearF(ep[i]->end);
		else
			A2 = data[ep[i]->end];
	
		ALet = A1;
		for(j = ep[i]->begin; j <= ep[i]->end; j++)
		{
			if (ALet < data[j]) 
				ALet = data[j];
		}
		ALet = ALet - A1;
		
		st = 0;
		if(i > 0) // not deadzone
		{
			for(j = ep[i]->begin; j <= ep[i]->end; j++)
			{
				if(data[j] > A1 + ALet *.9)
					width_90++;
				if(data[j] > A1 + ALet *.7)
					width_70++;
				if(data[j] > A1 + ALet *.4)
				{
					width_40++;
					if (st == 0)
						st = j - 1;
				}
			}
			if (width_40 - width_70 < 7)
				width = width_70;
			else
				width = width_90;
			centre = st + width / 2;
		}
		else	// deadzone
		{
			for(j = ep[i]->begin; j < ep[i]->end; j++)
			{
				if(data[j] > A1 + ALet - 0.5)
				{
					width++;
					centre = centre + j;
				}
			}
			centre = centre/width - 1;
		}


	/*	if(width>0.1)
		{
			centre = centre/width;
		}
		else
//		if (width < 0.1)
		{
			centre = (ep[i]->begin+ep[i]->end)/2.;
		}*/

		sigma1 = (centre - ep[i]->begin)/20.;
		sigma2 = (ep[i]->end - centre)*(1. - formFactor);
		sigmaFit = (ep[i]->end - centre)*formFactor;

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
}

void InitialAnalysis::getNormalizingCoeffs(double *wn, int freq)
{
	double* n = new double[10];
	int i;
	for (i = 0; i < 10; i++)
	{
		wn[i] = 1.;
		n[i] = 0;
	}

	for(i = -freq; i <= freq; i++)
	{
		n[0] = n[0] + fabs(wLet1(i, freq, 1.));
		n[1] = n[1] + fabs(wLet2(i, freq, 1.));
		n[2] = n[2] + fabs(wLet3(i, freq, 1.));
		n[3] = n[3] + fabs(wLet4(i, freq, 1.));
		n[4] = n[4] + fabs(wLet5(i, freq, 1.));

		n[5] = n[5] + fabs(wLet6(i, freq, 1.));
		n[6] = n[6] + fabs(wLet7(i, freq, 1.));
		n[7] = n[7] + fabs(wLet8(i, freq, 1.));
		n[8] = n[8] + fabs(wLet9(i, freq, 1.));
		n[9] = n[9] + fabs(wLet10(i, freq, 1.));
	}
	for (i = 0; i < 10; i++)
		wn[i] = n[i]/2.;
	
	delete n;
}


double InitialAnalysis::wLet(int arg, int freq, double* norma)
{
	switch (waveletType)
	{
		case  1: return  wLet1(arg, freq, norma[0]);
		case  2: return  wLet2(arg, freq, norma[1]);
		case  3: return  wLet3(arg, freq, norma[2]);
		case  4: return  wLet4(arg, freq, norma[3]);
		case  5: return  wLet5(arg, freq, norma[4]);
		case  6: return  wLet6(arg, freq, norma[5]);
		case  7: return  wLet7(arg, freq, norma[6]);
		case  8: return  wLet8(arg, freq, norma[7]);
		case  9: return  wLet9(arg, freq, norma[8]);
		case 10: return wLet10(arg, freq, norma[9]);
		default: return  wLet1(arg, freq, norma[0]);
	}
}

void InitialAnalysis::getLinearFittingCoefficients(double *data, int from, int to, int shift, double *res)
{
    double alfa = 0., beta = 0., gamma = 0., dzeta = 0., n = 0., d = 0.;
    int i;
    for (i = from; i <= to; i++)
	{
		d = (double)(i - shift);
		beta = beta - data[i] * d;
		alfa = alfa + d * d;
		gamma = gamma + d;
		dzeta = dzeta - data[i];
		n = n + 1.;
	}
    double a = (n * beta / gamma - dzeta) / (gamma - n * alfa / gamma);
    double b = - (alfa * a + beta) / gamma;

	res[0] = b;
	res[1] = a;

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

void InitialAnalysis::convolutionOfNoise(int n_points)
{
	int n;
	double meanValue;

	int i;
	for(i = 0; i < data_length; i++)
	{
		n=0;
		meanValue = 0.;
		for (int j = i; j < min(i + n_points, data_length); j++)
		{
			meanValue += noise[j];
			n++;
		}
		if (n > 0) 
			meanValue/=n;

		noise[i] = meanValue;
	}
}



void InitialAnalysis::setNNWindow(int from, int to, double *window, int eventWindowSize)
{

	int dist = evSizeC*2;
	int delta = dist-(to-from);

	if(delta > 0)
	{
		from = from - (int)(delta/2.);
		to   = to   + (int)(delta/2.);
	}
	else if(to-from>evSizeC*2.8)
	{
		to = from + (int)(evSizeC*2.8);
	}

	
	double dIndex = (double)(to - from)/eventWindowSize;
	
	double counter = from;


	int i;
	for(i=0; i<eventWindowSize; i++)
	{
		window[i] = data[(int)(counter+0.5)] - meanAttenuation*(counter-from)/evSizeC;
		counter += dIndex;

	}

	shiftEventToCentre(window, eventWindowSize);
	return;
}



void InitialAnalysis::setNNWindow_(int from, int to, double *window, int eventWindowSize)
{
	int dist = (int)(evSizeC*2.5);
	int delta = dist-(to-from);

	if(delta > 0)
	{
		from = from - (int)(delta/2.);
		to   = to   + (int)(delta/2.);
	}
//	else if(to-from>evSizeC*2.8)
//	{
//		to = from + (int)(evSizeC*2.8);
//	}
	
	
//	int myFrom = from;
//	int myTo = to;
	
	double dIndex = (double)(to - from)/eventWindowSize;
	
	double counter = from;


	int i;
	for(i=0; i<eventWindowSize; i++)
	{
		window[i] = data[(int)(counter+0.5)] - meanAttenuation*(counter-from)/evSizeC;
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

				A2 = data[eventsTable[i+2]] - meanAttenuation*evSizeC;
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

				A2 = data[eventsTable[i+2]] - meanAttenuation*evSizeC;
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
