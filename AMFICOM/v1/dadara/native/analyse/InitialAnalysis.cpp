// InitialAnalysis.cpp: implementation of the InitialAnalysis class.
//
//////////////////////////////////////////////////////////////////////

#include "InitialAnalysis.h"
#include "../Common/MathRef.h"
#include "Histogramm.h"

#include "../Common/assert.h"

#ifdef USE_NEURAL_NETWORK
#include "NeuroAnalyser.h"
#endif

#include "../An2/findLength.h"

#ifdef DEBUG_INITIAL_ANALYSIS
	#ifndef _WIN32
		#include <time.h>
		#include <sys/time.h>
	#endif
	#include <stdio.h>
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

InitialAnalysis::InitialAnalysis(
	double *data,				//точки рефлектограммы
	int data_length,			//число точек
	double delta_x,				//расстояние между точками (м)
	double minimalThreshold,	//минимальный уровень события
	double minimalWeld,			//минимальный уровень неотражательного события
	double minimalConnector,	//минимальный уровень отражательного события
	double minimalEndingSplash,	//минимальный уровень последнего отражения
	double maximalNoise,		//максимальный уровень шума
	int waveletType,			//номер используемого вейвлета
	double formFactor,			//формфактор отражательного события
	int reflectiveSize,			//характерная длина отражательного события
	int nonReflectiveSize)		//характерная длина неотражательного события
{

	/*fprintf(stderr,
		"data_length %d\n"
		"delta_x %g\n"
		"minimalThreshold %g\n"
		"minimalWeld %g\n"
		"minimalConnector %g\n"
		"minimalEndingSplash %g\n"
		"maximalNoise %g\n"
		"waveletType %d\n"
		"formFactor %g\n"
		"reflectiveSize %d\n"
		"nonReflectiveSize %d\n",
		data_length,
		delta_x,
		minimalThreshold,
		minimalWeld,
		minimalConnector,
		minimalEndingSplash,
		maximalNoise,
		waveletType,
		formFactor,
		reflectiveSize,
		nonReflectiveSize);*/


#ifdef DEBUG_INITIAL_ANALYSIS
	#ifdef _WIN32
		this->str = fopen(DEBUG_INITIAL_WIN_LOGF, "a");
		if (this->str == 0)
			this->str = stderr;
		fprintf (this->str, "*** InitialAnalysis::InitialAnalysis: data=%p, data_length=%d\n",
			data,data_length);
	#else
		timeval tv;
		gettimeofday(&tv, NULL);
		tm* t = localtime(&tv.tv_sec);
		const int size = 64; //was: 9 + 15 + 1 + 14 + 1 + 3 + 1...
		char* filename = new char[size];
		sprintf(filename, ".//logs//%04d%02d%02d%02d%02d%02d-dadara-ia.log", 1900 + t->tm_year, 1 + t->tm_mon, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
		filename[size - 1] = 0;
		this->str = fopen(filename, "a");
		delete[] filename;
	#endif
#endif

	this->delta_x				= delta_x;
	this->minimalThreshold		= minimalThreshold;
	this->minimalWeld			= minimalWeld;
	this->minimalConnector		= minimalConnector;
	this->minimalEndingSplash	= minimalEndingSplash;
	this->maximalNoise			= maximalNoise;
	this->waveletType			= waveletType;
	this->formFactor			= formFactor;
	this->data_length			= data_length;
	this->data					= data;
	this->evSizeC				= reflectiveSize;
	this->evSizeW				= nonReflectiveSize;

	performAnalysis();
}




InitialAnalysis::~InitialAnalysis()
{
	//delete[] wn_w;
	//delete[] wn_c;
	delete[] transC;
	delete[] transW;
	delete[] noise;
	delete[] data_woc;

	epVector.clear();

	delete[] eps;

#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(this->str, "### InitialAnalysis::~InitialAnalysis\n");
	if (this->str != stderr && this->str != stdout)
		fclose(this->str);
#endif
}


void InitialAnalysis::performAnalysis()
{
	correctDataArray();

	lastNonZeroPoint = getLastPoint();
	transC	 = new double[lastNonZeroPoint];
	transW	 = new double[lastNonZeroPoint];
	noise	 = new double[lastNonZeroPoint];
	data_woc = new double[data_length];

	//calcEventSize(0.5);

	wn_c = getWLetNorma(evSizeC, waveletType);
	wn_w = getWLetNorma(evSizeW, waveletType);

#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "Reflecting event size = %d\n", evSizeC);
		fprintf(this->str, "Nonreflecting event size = %d\n", evSizeW);
#endif

	// вычисляем уровень шума
	getNoise(noise, evSizeC);

	// выполняем вейвлет-преобразование
	performTransformation(data, 0, lastNonZeroPoint, transC, evSizeC, wn_c);
	performTransformation(data, 0, lastNonZeroPoint, transW, evSizeW, wn_w);

	// вычитаем из коэффициентов преобразования(КП) постоянную составляющую (среднее затухание)
	meanAttenuation = shiftToZeroAttenuation(transC);
	shiftToZeroAttenuation(transW);

	// устанавливаем в 0 КП, которые меньше уровня шума или минимального уровня события
	setNonZeroTransformation(transC, minimalThreshold, 0, lastNonZeroPoint);
	setNonZeroTransformation(transW, minimalThreshold, 0, lastNonZeroPoint);

#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "First transformation performed\nSearching for connectors...");
#endif

	// определяем координаты и типы событий по КП
	findConnectors(
		transC,
		transW,
		0,
		//min(lastNonZeroPoint + evSizeC, data_length - 10), 
		lastNonZeroPoint - 1,
		epVector);
//	correctConnectorCoords();

#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "done\nFound %d connectors\nSubstracting connectors from trace...", epVector.size());
#endif
	excludeConnectors(epVector, data, data_woc);
#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "done\n");
#endif

	performTransformation(data_woc, 0, lastNonZeroPoint, transW, evSizeW, wn_w);
	shiftToZeroAttenuation(transW);
	setNonZeroTransformation(transW, minimalThreshold, 0, lastNonZeroPoint);

#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "Second transformation performed\nSearching for welds...");
#endif
	findWelds(transW, epVector);
	siewLinearParts();
	correctWeldCoords();
#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "done\nTotal %d events found\n", epVector.size());
#endif

	
#ifdef DEBUG_INITIAL_ANALYSIS
	EPLIST::iterator it;
#endif

	// исключаем неидентифицированные события
	//excludeNonRecognizedEvents();

#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "Excluding short events (linear = %d, weld = %d, connector = %d)...", 
			max(evSizeW/2, 10), max(evSizeW/2, 3), max(evSizeC/2, 4));
#endif
	// исключаем события с длиной, меньшей половины характерного размера
	excludeShortEvents(max(evSizeW/2, 10), max(evSizeW/2, 3), max(evSizeC/2, 4));
	siewLinearParts();
#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "done\n");
#endif

	
#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "Setting EventsParams...");
#endif
	setEventParams();
#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "done\n%d events set\nCorrecting end...", epVector.size());
#endif
	// корректируем конец волокна согласно минимального отражения
	correctEnd();
#ifdef DEBUG_INITIAL_ANALYSIS
		fprintf(this->str, "done\nTotal number of events = %d\n", epVector.size());
#endif

//	fclose (f);
#ifdef DEBUG_INITIAL_ANALYSIS
	for (it = epVector.begin(); it != epVector.end(); it++)
	{
		if(it->end <= it->begin)
			fprintf(this->str, "error in event bounds: x1 = %d, x2 = %d\n", it->begin, it->end);
	}
	fclose(this->str);
#endif
}

int InitialAnalysis::getEventSize()
{
	return evSizeC;
}

EventParams **InitialAnalysis::getEventParams()
{
	return eps;
}

int InitialAnalysis::getEventsCount()
{
	return epVector.size();
}

double InitialAnalysis::getMeanAttenuation()
{
	return meanAttenuation;
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
	/*int lastPoint = data_length-1;
	for(int i=300; i<data_length; i++)
	{
		if(data[i]<1.)
		{
			lastPoint = i;
			break;
		}
	}*/
	int lastPoint = findReflectogramLength(data, data_length) - 1;

	if (lastPoint < 0)
		lastPoint = 0;

	if(lastPoint + 10 < data_length)
		lastPoint += 10;

	return lastPoint;
}


/*void InitialAnalysis::calcEventSize(double level)
{
	int eventSize = 0;
	int maximumIndex = 4;
	int i;

	for (i = 0; i < min(300, data_length); i++)
		if(data[i] > data[maximumIndex])
			maximumIndex = i;

	eventSize = maximumIndex;

	for(i = maximumIndex; i < data_length; i++)
	{
		if(data[i] < data[maximumIndex] - level)
		{
			eventSize = i;
			break;
		}
	}

	eventSize = (int)(eventSize*0.6);

	if(eventSize<4)
		eventSize = 4;

	evSizeC = eventSize;
	evSizeW = 3*eventSize/5;
}*/


void InitialAnalysis::performTransformation(double* y, int start, int end, double *trans, int freq, double norma)
{
	double tmp;

	for(int i = start; i < end; i++)
	{
		tmp = 0.;
		for(int j = max(i - freq, 0); j < min(i + freq + 1, end); j++)
			tmp = tmp + y[j] * wLet(j - i, freq, norma, waveletType);
		trans[i] = tmp;
	}
}



void InitialAnalysis::getNoise(double *noise, int freq)
{
	int i;

// First, we set that noise is euqal to the first derivative.

	for(i = 0; i < lastNonZeroPoint - 1; i++)
	{
		noise[i] = fabs(data[i] - data[i+1]);
		if (noise[i] > maximalNoise) 
			noise[i] = maximalNoise;
	}
	noise[lastNonZeroPoint - 1] = 0.;
    
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

	histo->init(trans, lastNonZeroPoint, 0, lastNonZeroPoint-1);
	double meanAtt = histo->getMaximumValue();
	delete histo;

	for(int i=0; i < lastNonZeroPoint; i++)
		trans[i] = trans[i] - meanAtt;

#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(this->str, "mean attenuation = %f\n", meanAtt);
#endif
	return meanAtt;
}


void InitialAnalysis::setNonZeroTransformation(double* trans, double threshold, int start, int end)
{
	int i;
	for(i = start; i < end; i++)
	{
		if(fabs(trans[i]) < max(threshold, noise[i] / 2.))
			trans[i] = 0.;
	}

	//Excluding of the exidental zero points;
	for(i = start; i < end - 1; i++) // FIXME: && i < trans.length - 2 -- would like to change acc to Stas's J IA
	{
		if(fabs(trans[i]) < threshold)
		{
			if(fabs(trans[i-1]) > threshold && 
		       fabs(trans[i+1]) > threshold)
					trans[i] = (trans[i-1] + trans[i+1]) / 2.;
		}
	}
}

void InitialAnalysis::findConnectors(double *trans, double *correct, int start, int end, EPLIST &vector)
{
	int halfWidth = evSizeC / 2;
	if(halfWidth < 1) 
		halfWidth = 1;

	int type;

    int x1; // начало события
    int x2; // конец события

	int counter = 0;

	for(int i = start; i < end; i += (x2 - x1))
	{
		int k1; // середина первого участка
		int k2; // середина второго участка
		int k3; // середина третьего участка
		int c1; // точка максимума первого участка
		int c2; // точка максимума второго участка
		int c3; // точка максимума третьего участка
		int j; // конец первого участка
		int k; // конец второго участка
		int s; // конец третьего участка

		j = i + 1;
		c1 = j;
		while(j < end && sign(trans[i]) == sign(trans[j]))
		{
			if(fabs(trans[c1]) < fabs(trans[j]))
				c1 = j;
			j++;
		}
		k = j + 1;
		c2 = k;
        while(k < end && sign(trans[j]) == sign(trans[k]))
		{
			if(fabs(trans[c2]) < fabs(trans[k]))
				c2 = k;
			k++;
		}
		s = k + 1;
		c3 = s;
        while(s < end && sign(trans[k]) == sign(trans[s]))
		{
			if(fabs(trans[c3]) < fabs(trans[s]))
				c3 = s;
			s++;
		}
		k1 = (j + i) / 2;
		k2 = (k + j) / 2;
		k3 = (s + k) / 2;

		x1 = i;
        if(fabs(trans[c1]) < minimalThreshold) // linear part
        {
			type = EventParams::LINEAR;
			x2 = j;
        }
        else if(trans[c1] > minimalConnector && 
				trans[c2] < -minimalConnector)  // connector
		{
			type = EventParams::CONNECTOR;
			x2 = k;
			for (int ii = c2; ii < s; ii++)
				if (fabs(correct[ii]) < minimalWeld)
				{
					x2 = ii;
					break;
				}
			//int infl = findInflectionPoint(correct, c2, s);
			int infl = findFirstAbsMinimumPoint(correct, c2, s);
			int constant = findConstantPoint(correct, c2, s);
			x2 = min(constant, min(infl, x2));
		}
		else if(trans[c1] > minimalConnector && 
				fabs(trans[c2]) < minimalThreshold && 
				trans[c3] < -minimalConnector && 
				k - j < (int)(halfWidth * 1.5)) //connector
		{
			type = EventParams::CONNECTOR;
			x2 = s;
			for (int ii = c3; ii < s; ii++)
				if (fabs(correct[ii]) < minimalWeld)
				{
					x2 = ii;
					break;
				}

			//int infl = findInflectionPoint(correct, c3, s);
			int infl = findFirstAbsMinimumPoint(correct, c3, s);
			int constant = findConstantPoint(correct, c3, s);
			x2 = min(constant, min(infl, x2));
		}
		else if(trans[c1] > minimalWeld * .8 || 
				trans[c1] < -minimalWeld * .8) //weld
		{
			type = EventParams::SPLICE;
			x2 = j;
		}
		else //linear
		{
			type = EventParams::LINEAR;
			x2 = j;
		}

		if (x1 < lastNonZeroPoint && type == EventParams::CONNECTOR)
		{
			EventParams ep;
			ep.n = vector.size();
			ep.type = type;
			ep.begin = x1;
			if (i != 0)
			{
				ep.begin = c1 + (int)(0.6 * evSizeW);
				for (int ii = x1; ii < c1; ii++)
					if (fabs(correct[ii]) > minimalConnector)
					{
						ep.begin = ii + (int)(0.6 * evSizeW);
						break;
					}
			}
			ep.end = x2 - (int)(0.5 * evSizeW);
			vector.copy_push_back(ep);

			counter++;
		}
#ifdef DEBUG_INITIAL_ANALYSIS
	if (x2 <= x1)
		fprintf(this->str, "(!!!!!!!!!Error setting ep.begin = %d; ep.end =  %d\n", x1, x2);
#endif
	}
}

/*void InitialAnalysis::correctConnectorCoords()
{
	EPLIST::iterator it;
	for (it = epVector.begin(); it != epVector.end(); it++)
	{
		EventParams* ep = &*it;
		it++;
		if (it != epVector.end())
		{
			EventParams* ep_next = &(*it);
			if (ep_next->begin - ep->end < 1.5 * evSizeW)
			{
				ep_next->begin -= (int)min(0.5 * evSizeW, 2 * (ep_next->begin - ep->end) / 5);
				ep->end = ep_next->begin;
			}
		}
		it--;
	}
}*/

void InitialAnalysis::correctWeldCoords()
{
	double d[2];

	EPLIST::iterator it = epVector.begin();
	for (; it != epVector.end(); it++)
	{
		EventParams* ep = &(*it);
		if (ep->type == EventParams::SPLICE)
		{
			// передний фронт
			if (it != epVector.begin())
			{
				it--;
				EventParams* ep_last = &(*it);
				if (ep_last->type == EventParams::LINEAR &&
					ep_last->end - ep_last->begin > 3)
				{
					linearize2point(data, ep_last->begin, ep_last->end, d);
					for(int i = ep->begin; i < (ep->begin + ep->end) / 2; i++)
					{
						if(fabs(data[i] - (d[1] + d[0] * i)) > //max(minimalWeld, fabs(2 * noise[i])))
							fabs(2 * noise[i]))
						{
							ep->begin = i;
							if (ep->end - ep->begin < evSizeW)
								ep->begin = ep->end - evSizeW;
							ep_last->end = ep->begin;
							if (ep_last->end <= ep_last->begin)
								ep_last->begin = ep_last->end - 1;
							break;
						}
					}
				}
				it++;
			}

			// задний фронт 
			it++;
			if (it != epVector.end()
				&& it->type == EventParams::LINEAR 
				&& it->end - it->begin > 3)
			{
				EventParams* ep_next = &(*it);
				if (ep_next->type == EventParams::LINEAR &&
					ep_next->end - ep_next->begin > 3)
				{
					linearize2point(data, ep_next->begin, ep_next->end, d);
					for(int i = ep->end; i > ep->begin; i--)
					{
						if (fabs(data[i] - (d[1] + d[0] * i)) > //max(minimalWeld, fabs(2 * noise[i])))
							fabs(2 * noise[i]))
						{
							ep->end = i;
							if (ep->end - ep->begin < evSizeW)
								ep->end = ep->begin + evSizeW;
							ep_next->begin = ep->end;
							if (ep_next->end <= ep_next->begin)
								ep_next->begin = ep_next->end - 1;
							break;
						}
					}
				}
			}
			it--;
		}
	}
}


void InitialAnalysis::findWelds(double *trans, EPLIST &vector)
{
	int halfWidth = evSizeW / 2;
	if(halfWidth < 1) 
		halfWidth = 1;

	int type;

    int x1; // начало события
    int x2; // конец события

	int k1; // середина первого участка
	int k2; // середина второго участка
	int k3; // середина третьего участка
	int c1; // точка максимума первого участка
	int c2; // точка максимума второго участка
	int c3; // точка максимума третьего участка
	int j; // конец первого участка
	int k; // конец второго участка
	int s; // конец третьего участка

	EPLIST::iterator it;
	EPLIST clone;
	for (it = vector.begin(); it != vector.end(); it++)
	{
		clone.copy_push_back(*it);
	}

	vector.clear();

	for (it = clone.begin(); it != clone.end(); it++)
	{
		// XXX
		vector.copy_push_back(*it);
		vector.last()->n = vector.size() - 1;
		EventParams *ep1 = &*vector.last();
		it++;
		if (it != clone.end())
		{
			int i;
			EventParams* ep_next = &(*it);
			int start = ep1->end;
			int end = ep_next->begin;

			double meanNoise = 0;
			for (i = start; i < end; i++)
				meanNoise += fabs(noise[i]);
			meanNoise /= (double)(end - start);

			setNonZeroTransformation(trans, meanNoise * 0.8, start, end);

			for(i = start; i < end; i += (x2 - x1))
			{
				j = i + 1;
				c1 = j;
				while(sign(trans[i]) == sign(trans[j]) && j < end)
				{
					if(fabs(trans[c1]) < fabs(trans[j]))
						c1 = j;
					j++;
				}
				k = j + 1;
				c2 = k;
				while(sign(trans[j]) == sign(trans[k]) && k < end)
				{
					if(fabs(trans[c2]) < fabs(trans[k])) 
						c2 = k;
					k++;
				}
				s = k + 1;
				c3 = s;
				while(sign(trans[k]) == sign(trans[s]) && s < end)
				{
					if(fabs(trans[c3]) < fabs(trans[s]))
						c3 = s;
					s++;
				}
				k1 = (j + i) / 2;
				k2 = (k + j) / 2;
				k3 = (s + k) / 2;

				x1 = i;
				if(fabs(trans[c1]) < minimalThreshold) // linear part
				{
					type = EventParams::LINEAR;
					x2 = j;
				}
				else if(trans[c1] > minimalWeld * .8 || 
						trans[c1] < -minimalWeld * .8) //weld
						//&& j - i < 3 * evSizeC)
				{
					type = EventParams::SPLICE;
					x2 = j;
				}
				else if(trans[c1] > noise[k1] * 3 && 
						trans[c2] < -noise[k2] * 3 && 
						trans[c1] > minimalConnector && 
						trans[c2] < -minimalConnector) //reflection (connector, anyway)
				{
					type = EventParams::CONNECTOR;
					x2 = k;
				}
				else if(trans[c1] > minimalConnector && 
						trans[c2] < minimalConnector && 
						fabs((trans[c1] + trans[c2]) / (trans[c1] - trans[c2])) < .5) //reflection		
				{
					type = EventParams::CONNECTOR;
					x2=k;
				}
				else if(trans[c1] > minimalConnector && 
						trans[c3] < minimalConnector && 
						fabs((trans[c1] + trans[c3]) / (trans[c1] - trans[c3])) < .5 &&
						k - j < (int)(halfWidth * 1.5))  //reflection
				{
					type = EventParams::CONNECTOR;
					x2 = s;
				}
				else //linear
				{
					type = EventParams::LINEAR;
					x2 = j;
				}

				if (x1 < lastNonZeroPoint)
				{
					EventParams ep1;
					ep1.n = vector.size();
					ep1.type = type;
					ep1.begin = x1;
					ep1.end = x2;
					vector.copy_push_back(ep1);
				}

#ifdef DEBUG_INITIAL_ANALYSIS
	if (x2 <= x1)
		fprintf(this->str, "(!!!!!!!!!Error setting ep.begin = %d; ep.end =  %d\n", x1, x2);
#endif
			}//for
		}//if
		it--;
	}//for
	clone.clear();
}


void InitialAnalysis::excludeConnectors(EPLIST &vector, double *data, double *data_woc)
{
	int i;
	int j;
	for (i = 0; i < data_length; i++)
		data_woc[i] = data[i];

	double delta = 0;
	double *_d = NULL;

	EPLIST::iterator it;
	for (it = vector.begin(); it != vector.end(); it++) 
	{

		EventParams* ep_last = &(*it);
		it++;
		if (it != vector.end())
		{
			EventParams* ep1 = &(*it);

			double d[2];
			if (ep_last->end != ep1->begin)
				linearize2point(data, ep_last->end, ep1->begin, d);
			else
			{
				if (ep1 != &(*vector.begin()))
				{
					EPLIST::iterator it_2 = it;
					it_2--;
					it_2--;
					EventParams* ep_beforelast = &(*it_2);
					linearize2point(data, ep_beforelast->end, ep1->begin, d);
				}
				else
					linearize2point(data, ep_last->begin, ep1->begin, d);
			}

			if (ep_last != &(*vector.begin()))
			{
				delta += ((data[ep_last->begin] + _d[0] * (ep_last->end - ep_last->begin)) - data[ep_last->end]);
				for (j = ep_last->end; j < ep1->begin; j++)
					data_woc[j] += delta;
			}
			else
			{
				for (j = ep_last->begin; j < ep_last->end; j++)
					data_woc[j] = data[ep_last->end] + d[0] * (j - ep_last->end);
			}
			_d = d;

			for (j = ep1->begin; j < ep1->end; j++)
				data_woc[j] = data[ep1->begin] + d[0] * (j - ep1->begin) + delta;
		}
		it--;
	}
}

void InitialAnalysis::siewLinearParts()
{
	EPLIST::iterator it = epVector.begin();
	while (it != epVector.end()) {
		EventParams* ep = &(*it);
		if (ep->type == EventParams::LINEAR) {
			it++;
			if (it != epVector.end()) {
				EventParams* ep_next = &(*it);
				if (ep_next->type == EventParams::LINEAR) {
					ep->end = ep_next->end;
					it--;
					epVector.removeNext(it);
					continue;
				}
				if (ep->end > ep_next->begin) {
					ep->end = ep_next->begin;
					if (ep->begin > ep->end)
						ep->begin = ep->end;
				}
			}
			it--;
		}
		it++;
	}
}

void InitialAnalysis::excludeShortEvents(int linearLength, int weldLength, int connectorLength) 
{
	EPLIST::iterator it = epVector.begin();

	if (it != epVector.end())
		it++;
	while (it != epVector.end())
	{
		bool key = false;
		EventParams* ep = &(*it);

		if (ep->type == EventParams::LINEAR)
		{ 
			if (ep->end - ep->begin <= linearLength)
				key = true; 
		}
		else if (ep->type == EventParams::SPLICE)
		{ 
			if (ep->end - ep->begin <= weldLength)
				key = true; 
		}
		else if (ep->type == EventParams::CONNECTOR)
		{
			if (ep->end - ep->begin <= connectorLength)	
				key = true; 
		}

		if (key)
		{
			// find prev and next ep's
			EPLIST::iterator it_2;
			it_2 = it; it_2--;
			EventParams* ep_prev = &*it_2;
			it_2 = it; it_2++;
			if (!it_2.isNull())
			{
				EventParams* ep_next = &*it_2;
				if (ep_next->type == EventParams::LINEAR)
					ep_next->begin = ep_prev->end;
				else if (ep_prev->type == EventParams::LINEAR)
					ep_prev->end = ep_next->begin;
				else if(ep_next->type == EventParams::SPLICE)
					ep_next->begin = ep_prev->end;
				else
					ep_prev->end = ep_next->begin;
			}
			it--;
			epVector.removeNext(it);
		}
		it++;
	}
}

void InitialAnalysis::correctEnd()
{
	EPLIST::iterator it;

	int l = (epVector.back()).begin + evSizeC * 2;
#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(this->str, "end point before correction = %d (%.3fkm)\n", l, delta_x * l / 1000.);
	fprintf(this->str, "number of events before correction = %d\n", epVector.size());
#endif

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
			epVector.removeNext(it); // XXX
			continue;
		}
	}

#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(this->str, "end point after correction = %d (%.3fkm)\n", l, delta_x * l / 1000.);
	fprintf(this->str, "number of events after correction = %d\n", epVector.size());
#endif
}

void InitialAnalysis::setEventParams()
{
	int counter = 0;
	unsigned int i;

	eps = new EventParams *[epVector.size()];

	double *d = new double[2];
#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(this->str,"InitialAnalysis::setEventParams(): epVector.size() = %d; eps = %p; d = %p\n",(int)epVector.size(),eps, d);
#endif

	for (EPLIST::iterator it = epVector.begin(); it != epVector.end(); it++)
	{
		getLinearFittingCoefficients(data, it->begin, it->end, it->begin, d);
		it->a_linear = d[0];
		it->b_linear = d[1];
		eps[counter++] = &(*it);
	}

	delete[] d;

	for(i = 0; i < epVector.size(); i++) //Setting of the weld params;
	{
		eps[i]->center_weld = (double)(eps[i]->begin+eps[i]->end)/2.;
		eps[i]->width_weld = evSizeC*.9;

		double A1;
		double A2; 
		double A3;
		double k;

		if(i>0 && i<epVector.size()-1 && eps[i-1]->type == EventParams::LINEAR && eps[i+1]->type == EventParams::LINEAR)
		{
			A1 = eps[i-1]->linearF(eps[i]->begin);
			A2 = eps[i+1]->linearF(eps[i]->begin);
			A3 = eps[i+1]->linearF(eps[i]->end);
			k = (eps[i-1]->b_linear + eps[i+1]->b_linear)/2.;
		}
		else if(i>0 && i<epVector.size()-1 && eps[i-1]->type == EventParams::LINEAR && eps[i+1]->type != EventParams::LINEAR)
		{
			A1 = eps[i-1]->linearF(eps[i]->begin);
			A2 = data[eps[i]->end];
			A3 = data[eps[i]->end];
			k = eps[i-1]->b_linear;
		}
		else if(i>0 && i<epVector.size()-1 && eps[i-1]->type != EventParams::LINEAR && eps[i+1]->type == EventParams::LINEAR)
		{
			A1 = data[eps[i]->begin];
			A2 = eps[i+1]->linearF(eps[i]->begin);
			A3 = eps[i+1]->linearF(eps[i]->end);
			k = eps[i+1]->b_linear;
		}
		else
		{
			A1 = data[eps[i]->begin];
			A2 = data[eps[i]->end];
			A3 = data[eps[i]->end];
			k = 0.;
		}

		eps[i]->a_weld = (A1+A3)/2.;
		eps[i]->boost_weld = A2-A1;
		eps[i]->b_weld = k;
	}

	for(i = 0; i < epVector.size(); i++) // Setting of the connector params;
	{
		double A1=0.;
		double A2=0.;
		double ALet=0.;
		//double width=0.;
		//double width_40=0.;
		//double width_70=0.;
		//double width_90=0.;
		//int st = 0;
		//double centre=0.;
		double sigma1=0.;
		double sigma2=0.;
		double sigmaFit = 0.;
		int j;

		if(i > 0 && eps[i-1]->type == EventParams::LINEAR)
		{
			A1 = eps[i-1]->linearF(eps[i]->begin);
		}
		else 
		{
			A1 = data[eps[i]->begin];
		}

		if(i < epVector.size() - 1 && eps[i+1]->type == EventParams::LINEAR)
			A2 = eps[i+1]->linearF(eps[i]->end);
		else
			A2 = data[eps[i]->end];
	
		ALet = A1;
		for(j = eps[i]->begin; j <= eps[i]->end; j++)
		{
			if (ALet < data[j]) 
				ALet = data[j];
		}
		ALet = ALet - A1;
		
		/*
		st = 0;
		if(i > 0) // not deadzone
		{
			for(j = eps[i]->begin; j <= eps[i]->end; j++)
			{
				if(data[j] > A1 + ALet *.9)
					width_90++;
				if(data[j] > A1 + ALet *.7)
				{
					width_70++;
					if (front < 0)
						front = j;
					tail = j;
				}
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
			for(j = eps[i]->begin; j < eps[i]->end; j++)
			{
				if(data[j] > A1 + ALet - 0.5)
				{
					width++;
					centre = centre + j;
					if (front < 0)
						front = j;
					tail = j;
				}
			}
			centre = centre/width - 1;
		}
		
		if (front < eps[i]->begin)
			front = eps[i]->begin;
		if (tail < 0 || tail > eps[i]->end)
			tail = eps[i]->end;
		*/

		// ищем (приблизительно) центр
		double Cm0 = 0;
		double Cm1 = 0;
		double th = i > 0
			? A1 + ALet * 0.7
			: A1 + ALet - 0.5;
		for (j = eps[i]->begin; j < eps[i]->end; j++)
		{
			if (data[j] > th)
			{
				Cm0++;
				Cm1 += j;
			}
		}
		double center = Cm0 != 0
			? Cm1 / Cm0
			: (eps[i]->begin + eps[i]->end) / 2.0;

		// определяем фронт и спад, окончательное положение "центра" положим посередине
		double Wc1 = 0;
		double Wc2 = 0;
		for (j = eps[i]->begin; j < eps[i]->end; j++)
		{
			if (data[j] > th)
			{
				if (j < center)
					Wc1++;
				else
					Wc2++;
			}
		}
		double front = center - Wc1;
		double tail = center + Wc2;
		double centre = center; // XXX

			
		sigma1 = (centre - eps[i]->begin)/20.;
		sigma2 = (eps[i]->end - centre)*(1. - formFactor);
		sigmaFit = (eps[i]->end - centre)*formFactor;

		eps[i]->a1_connector = A1;
		eps[i]->a2_connector = A2;
		eps[i]->aLet_connector = ALet;

		//eps[i]->width_connector = width;
		//eps[i]->center_connector = centre;
		
		//fprintf(stderr, "width_40 %g width_70 %g width_90 %g width %g\n", width_40, width_70, width_90, width); // FIXIT
		//fflush(stderr); // FIXIT

		eps[i]->center_connector = (front + tail) / 2.0;
		eps[i]->width_connector = tail - front;

		eps[i]->sigma1_connector = sigma1;
		eps[i]->sigma2_connector = sigma2;
		eps[i]->sigmaFit_connector = sigmaFit;
		eps[i]->k_connector = formFactor;
	}

#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(this->str,"InitialAnalysis::setEventParams(): exit\n");
#endif
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
    double a = (n * beta / gamma - dzeta) / (gamma - n * alfa / gamma); // == cov(dx,y)/D(dx)
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
	for(i = 0; i < lastNonZeroPoint; i++)
	{
		n=0;
		meanValue = 0.;
		for (int j = i; j < min(i + n_points, lastNonZeroPoint); j++)
		{
			meanValue += noise[j];
			n++;
		}
		if (n > 0) 
			meanValue/=n;

		noise[i] = meanValue;
	}
}


#ifdef USE_NEURAL_NETWORK

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

#endif // USE_NEURAL_NETWORK

