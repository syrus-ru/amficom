// InitialAnalysis.cpp: implementation of the InitialAnalysis class.
//----------------------------------------
#include "InitialAnalysis.h"
#include "../Common/MathRef.h"
#include "Histogramm.h"

#include <assert.h>

#ifdef USE_NEURAL_NETWORK
#include "NeuroAnalyser.h"
#endif

#include "../An2/findLength.h"
#include "../An2/findNoise.h"

#include "../Common/prf.h"

#ifdef DEBUG_INITIAL_ANALYSIS
	#ifndef _WIN32
		#include <time.h>
		#include <sys/time.h>
	#endif
	#include <stdio.h>
#endif

//------------------------------------------------------------------------------------------------------------
// Construction/Destruction
InitialAnalysis::InitialAnalysis(
	double* data,				//точки рефлектограммы
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
#ifdef debug_lines
    cou = 0;
    for(int i=0; i<sz; i++){col[i] = -1;}
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
    eps = NULL;

	performAnalysis();
}
//------------------------------------------------------------------------------------------------------------
InitialAnalysis::~InitialAnalysis()
{
	//delete[] wn_w;
	//delete[] wn_c;
	delete[] type;
	delete[] transC;
	delete[] transW;
	delete[] noise;
    delete[] rnoise;
	delete[] data_woc;

	epVector.clear();

	delete[] eps;
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performAnalysis()
{
    correctDataArray();
	lastNonZeroPoint = getLastPoint();
	transC	 = new double[lastNonZeroPoint];
	transW	 = new double[lastNonZeroPoint];
	noise	 = new double[lastNonZeroPoint];
    rnoise	 = new double[lastNonZeroPoint];
	data_woc = new double[data_length];
    type	 = new double[data_length];
	wn_c = getWLetNorma(evSizeC, waveletType);
	wn_w = getWLetNorma(evSizeW, waveletType);

	// вычисляем уровень шума по Саше
	getNoise(noise, evSizeC);
    // вычисляем уровень шума по saa и поправку к чувствительности на его основе
    { const int sz = lastNonZeroPoint;
      const int width = evSizeW;
      double noiseLevel = findNoise3s(data, sz) + 1.0; // !!! подобрать величину добавки (от 0 до 3)
      fillNoiseArray(data, sz, width, 1 + width / 20, noiseLevel, rnoise);
    }

	// выполняем вейвлет-преобразование
	performTransformation(data, 0, lastNonZeroPoint, transC, evSizeC, wn_c);// transC - transConnector
    performTransformation(data, 0, lastNonZeroPoint, transW, evSizeW, wn_w);// transW - transWeld
    //performTransformation(data, 0, lastNonZeroPoint, transW, evSizeC, wn_c);// !!!  TEST

	// вычитаем из коэффициентов преобразования(КП) постоянную составляющую (среднее затухание)
	meanAttenuation = shiftToZeroAttenuation(transC);
	shiftToZeroAttenuation(transW);
	// устанавливаем в 0 КП, которые меньше уровня шума или минимального уровня события
	setNonZeroTransformation(transC, minimalThreshold, 0, lastNonZeroPoint);
	setNonZeroTransformation(transW, minimalThreshold, 0, lastNonZeroPoint);
	// setNonZeroTransformation_(transW, minimalThreshold, 0, lastNonZeroPoint); // !!! TEST

	// определяем координаты и типы событий по КП
	findConnectors(transC, transW, 0, lastNonZeroPoint - 1, epVector);
    // срезаем все события, интерполируя их прямой по краевым точкам
	excludeAllEvents(epVector, data, data_woc);
    // свёртка с вейвлетом для сварки 
	performTransformation(data_woc, 0, lastNonZeroPoint, transW, evSizeW, wn_w);

	// вычесть из рефлектограммы средний наклон
	shiftToZeroAttenuation(transW);
    // еслди сигнал меньше порога или половины шума - ставим его в ноль
	setNonZeroTransformation(transW, minimalThreshold, 0, lastNonZeroPoint);

	findWelds(transW, epVector);

	sewLinearParts();
	correctWeldCoords();
	// исключаем события с длиной, меньшей половины характерного размера
	excludeShortEvents(max(evSizeW/2, 10), max(evSizeW/2, 3), max(evSizeC/2, 4));

	sewLinearParts();

	// корректируем конец волокна согласно минимального отражения
	// correctEnd();
	// setEventParams();
	// уточняем координаты начала коннекторов
    correctAllConnectorsFronts(data, epVector);// by Vit
    excludeShortLinesBetweenConnectors(data, epVector, evSizeC);// by Vit

    setEventParams();
  	correctEnd();// чтобы конец правильно доопределился надо сначала вызвать setEventParams()
    setEventParams();// чтобы количество параметров совпадало с количеством событий, надо вызвать ещё раз
}
// -------------------------------------------------------------------------------------------------
// added by Vit ( (c) saa ) 
void InitialAnalysis::fillNoiseArray(const double *y, int N, int width, double Neff, double noiseLevel, double *outNoise)
{	int i;
	double acc;
	if (width < 1)
	{	width = 1;
    }
	// первый шаг усреднения - интегрирование
	for (i = 0, acc = 0; i < N; i++)
	{	outNoise[i] = acc;
		acc += y[i];
	}
	// второй шаг усреднения - вычитание
	for (i = 0; i < N - width; i++)
	{	outNoise[i] = outNoise[i + width] - outNoise[i];
	}
	// третий шаг усреднения - сдвиг и деление
	int ofs = width / 2;
	for (i = N - width - 1; i >= 0; i--)
	{	outNoise[i + ofs] = outNoise[i] / width;
	}
	// четвертый шаг - заполнение краев
	for (i = 0; i < ofs; i++)
	{	outNoise[i] = outNoise[ofs];
	}
	for (i = N - width + ofs; i < N; i++)
	{	outNoise[i] = outNoise[N - width + ofs - 1];
	}
	// получили усредненное значение y[] в массиве noise[]
	// рассчитываем шум
	if (Neff < 1)
	{	Neff = 1;
    }
	for (i = 0; i < N; i++)
	{	// кто скажет, что sqrt(Neff) в цикле сильно влияет на
		// быстродействие АМФИКОМа, того назову плохим словом.
		outNoise[i] = 5 * log10(1 +	pow(10.0, (noiseLevel - outNoise[i]) / 5.0) / sqrt(Neff));
	}
}
//------------------------------------------------------------------------------------------------------------
int InitialAnalysis::getEventSize()
{	return evSizeC;
}
//------------------------------------------------------------------------------------------------------------
EventParams **InitialAnalysis::getEventParams()
{	return eps;
}
//------------------------------------------------------------------------------------------------------------
int InitialAnalysis::getEventsCount()
{	return epVector.size();
}
//------------------------------------------------------------------------------------------------------------
double InitialAnalysis::getMeanAttenuation()
{	return meanAttenuation;
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::correctDataArray()
{	// Excluding bad points at the begin of the reflectogramm;
	double tmp;
	if(data[1]<data[0])
	{   tmp = data[0];
		data[0] = data[1];
		data[1] = tmp;
	}
	if(data[2]<data[1] && data[2]<data[3])
	{	data[2] = (data[1]+data[3])/2.;
	}

	double minimum = data[300];
	int i;

	for(i=300; i<data_length; i++)
	{   if(data[i]<minimum)
		{   minimum = data[i];
		}
	}
	for(i=0; i<data_length; i++)
	{	data[i] = data[i] - minimum;
	}
	for(i=0; i<302; i++)
	{	if(data[i]<0.)
		{	data[i] = 0.;
		}
	}
}
//------------------------------------------------------------------------------------------------------------
int InitialAnalysis::getLastPoint()
{   /*int lastPoint = data_length-1;
	for(int i=300; i<data_length; i++)
	{	if(data[i]<1.)
		{   lastPoint = i;
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
//------------------------------------------------------------------------------------------------------------
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
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performTransformation(double* y, int start, int end, double *trans, int freq, double norma)
{   double tmp;
	for(int i = start; i < end; i++)
	{	tmp = 0.;
		for(int j = max(i - freq, 0); j < min(i + freq + 1, end); j++)
		{	tmp = tmp + y[j] * wLet(j - i, freq, norma, waveletType);
        }
		trans[i] = tmp;
	}
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::getNoise(double *noise, int freq)
{	int i;
    // First, we set that noise is euqal to the first derivative.
	for(i=0; i < lastNonZeroPoint-1; i++)
	{	noise[i] = fabs(data[i] - data[i+1]);
		if (noise[i] > maximalNoise)
			noise[i] = maximalNoise;
	}
	noise[lastNonZeroPoint - 1] = 0.;
	double EXP;
    // Cut of the prompt-peaks with exponent.
	for(i=0; i < lastNonZeroPoint; i++)
	{	EXP = (exp((double)i / lastNonZeroPoint) - 1.) * maximalNoise / (exp(1.) - 1.);
		if (noise[i] > EXP)
			noise[i] = EXP;
	}
	convolutionOfNoise(freq);
}
//------------------------------------------------------------------------------------------------------------
double InitialAnalysis::shiftToZeroAttenuation(double *trans)
{	Histogramm* histo = new Histogramm(-0.5, 0, 100);
	//возможное затухание находится в пределах [0; -0.5] дБ
	histo->init(trans, lastNonZeroPoint, 0, lastNonZeroPoint-1);
	double meanAtt = histo->getMaximumValue();
	delete histo;
	for(int i=0; i < lastNonZeroPoint; i++)
	{	trans[i] = trans[i] - meanAtt;
    }
	return meanAtt;
}
//------------------------------------------------------------------------------------------------------------
// если сигнал меньше порога или половины шума - ставим его в ноль 
void InitialAnalysis::setNonZeroTransformation_(double* trans, double threshold, int start, int end)
{	int i;
	for(i = start; i < end; i++)
	{	if(fabs(trans[i]) < max(threshold, noise[i] / 2.))
		{	trans[i] = 0.;
        }
	}
}
//------------------------------------------------------------------------------------------------------------
// если сигнал меньше порога или половины шума - ставим его в ноль 
void InitialAnalysis::setNonZeroTransformation(double* trans, double threshold, int start, int end)
{	int i;
	for(i = start; i < end; i++)
	{	if(fabs(trans[i]) < max(threshold, noise[i] / 2.))
		{	trans[i] = 0.;
        }
	}
//*
    //Excluding of the eссidental zero points;
	for(i = start; i < end - 1; i++) // FIXME: && i < trans.length - 2 -- would like to change acc to Stas's J IA
	{	if(fabs(trans[i]) < threshold)
		{	if(fabs(trans[i-1]) > threshold && fabs(trans[i+1]) > threshold)
			{	trans[i] = (trans[i-1] + trans[i+1]) / 2.;
            }
		}
	}
//*/
}
//------------------------------------------------------------------------------------------------------------
// findConnectors(transC, transW, 0, lastNonZeroPoint - 1, epVector);
void InitialAnalysis::findConnectors(double *trans, double *correct, int start, int end, EPLIST &vector)
{	int halfWidth = evSizeC / 2; // полуширина события характерная для коннектора
	if(halfWidth < 1)
		halfWidth = 1;
	int tp;
    int x1; // начало события
    int x2; // конец события

	int counter = 0;
	for(int i = start; i < end; i += (x2 - x1))
	{	int k1; // середина первого участка
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
		//  коннекторы(в т ч и мёртвая зона): ищем перемену знака "вейвлет-образа"
		while(j < end && sign(trans[i]) == sign(trans[j]))// до пересечения нуля (#define sign(a) (((a) <= 0) ? (((a) < 0) ? (-1) : (0)) : (1)))
		{	if(fabs(trans[j] > fabs(trans[c1]))) // запоминаем, максимум
			{	c1 = j;
            }
			j++;
		}
		k = j + 1;
		c2 = k;
        // ищем ноль
        while(k < end && sign(trans[j]) == sign(trans[k]))
		{	if(fabs(trans[k]) > fabs(trans[c2]))
			{	c2 = k; // запоминаем минимум
            }
			k++;
		}
		s = k + 1;
		c3 = s;
        // ищем минимум на участке до второго пересечения с нулём
        while(s < end && sign(trans[k]) == sign(trans[s]))
		{	if(fabs(trans[c3]) < fabs(trans[s]))
			{	c3 = s;
            }
			s++;
		}
		// по точкам смены знака вейвлет-образа судим о зарактерных точках коннектора
		k1 = i + (int)((j - i) * 0.5 ); // середина первого  фронта
		k2 = j + (int)((k - j) * 0.5 ); // середина "полки"
		k3 = k + (int)((s - k) * 0.5); // середина спада
		x1 = i; //отмечаем начало события
        //  если вейвлет-образ меньше порогового на первом всплеске вверх, то считаем, что это линейный участок
        if( fabs(trans[c1])<minimalThreshold ) // linear part  (обычно = 0.04)
        {	tp = EventParams::LINEAR;
			x2 = j;
        }
        // если есть хотя бы первый всплеск вверх
        else if( trans[c1]>minimalConnector && trans[c2]< -minimalConnector )  // connector
		{	tp = EventParams::CONNECTOR;
			x2 = k;
/* commented by Vit  (визуально ничего не поменялось)
			for (int ii = c2; ii < s; ii++)
			{	if (fabs(correct[ii]) < minimalWeld) // если раньше начинается сварка, то прописываем конец коннектора раньше
				{	x2 = ii;
					break;
				}
            }
*/
			//int infl = findInflectionPoint(correct, c2, s);
			int infl = findFirstAbsMinimumPoint(correct, c2, s);
			int constant = findConstantPoint(correct, c2, s);
			x2 = min(constant, min(infl, x2));
		}
        // если есть узкий небольшой , но всплеск вверх и вниз
		else if( trans[c1]>minimalConnector && fabs(trans[c2])<minimalThreshold &&
        		 trans[c3]<-minimalConnector  && k-j<(int)(halfWidth * 1.5) ) //connector
		{	tp = EventParams::CONNECTOR;
			x2 = s;
			for (int ii = c3; ii < s; ii++)
			{	if (fabs(correct[ii]) < minimalWeld)
				{	x2 = ii;
					break;
				}
            }
			//int infl = findInflectionPoint(correct, c3, s);
			int infl = findFirstAbsMinimumPoint(correct, c3, s);
			int constant = findConstantPoint(correct, c3, s);
			x2 = min(constant, min(infl, x2));
		}
/*		else if( fabs (trans[c1]) > minimalWeld * 0.8 ) //weld
		{	tp = EventParams::SPLICE;
			x2 = j;
		}
//*/		else //linear
		{	tp = EventParams::LINEAR;
			x2 = j;
		}
//*/
		if (x1 < lastNonZeroPoint && tp == EventParams::CONNECTOR)
		{	EventParams ep;
			ep.n = vector.size();
			ep.type = tp;
			ep.begin = x1;
			if (i != 0)
			{	ep.begin = c1 + (int)(0.6 * evSizeW);
				for (int ii = x1; ii < c1; ii++)
					if (fabs(correct[ii]) > minimalConnector)
					{	ep.begin = ii + (int)(0.6 * evSizeW);
						break;
					}
			}
			ep.end = x2 - (int)(0.5 * evSizeW);
			vector.copy_push_back(ep);
			counter++;
            // <added by Vit>
            // помечаем, что уже распознано, как коннектор
            for(int i=ep.begin; i<=ep.end; i++)
            {	type[i] = EventParams::CONNECTOR;
            }
            // </added by Vit>
		}
	}
}
//------------------------------------------------------------------------------------------------------------
// <added by Vit>
// поскольку убрали срезание коннекторов то теперь делаем так : если найдена сварка там,
// где раньше был коннектор, то не считаем это сваркой, а оставляем коннектором
// </added by Vit>
void InitialAnalysis::correctAllConnectorsFronts(double *arr, EPLIST &evnts)
{   if( evnts.size() < 2 )
return;
	int cou1=0;
    EPLIST::iterator lit;
	//cou = 0;
    // last указывает на последний, end указывает на адрес ПОСЛЕ последнего
	for(EPLIST::iterator it1 = evnts.begin(), it2 = evnts.begin(); it1 != (EPLIST::iterator&)evnts.end(); it1++, it2=it1)// it2 оставелен, если друг понадобится раскомментировать закооментированный код в цикле
	{   // пока не дойдём до коннектора
    	if(it1->type != EventParams::CONNECTOR )
    continue;
        else {cou1++;}
		if(cou1 == 1) // первый "коннектор" это мёртвая зона
    continue;
        /*
        // пока не отойдём достаточно далеко
        while( it2 != (EPLIST::iterator&)evnts.begin() && it1->begin-it2->end < 10 )
        {	it2--;
        }
        double a, b;
        calc_rms_line(data, it2->end, it1->begin, a, b);
        // !!! для нужд отрисовки записываем это в массив (кроме как дял отрисовки он пока ни для чего не нужен)
        InitialAnalysis::a[cou] = a; InitialAnalysis::b[cou] = b;
        InitialAnalysis::xs[cou]=it2->end*delta_x; InitialAnalysis::xe[cou]=it1->begin*delta_x;
        cou++;
        // </для нужд отрисовки >
        */
        // ищем точку на фронте коннектора такую, что всё слква от неё - меьше, а справа - не меньше
        int i_begin = it1->begin, i_end = it1->end;
        int i_max = i_begin;// номер макс точки
        double f_max = data[i_max];
		int i;
        for( i=i_begin; i<i_end; i++ ) // ищем максимум
        {	if(data[i]>f_max) {i_max = i; f_max = data[i];}
        }
		int i_x = -1; // x - искомая точка;
        double f_lmax = data[i_begin];
        for( i=i_begin; i<i_max; i++ )
        { 	if(f_lmax <= data[i])
        	{	f_lmax = data[i];
            	if(i_x == -1)
                {	i_x = i;
                }
            }
        	if(	i_x!=-1 && data[i]<data[i_x] )
            {	i_x = -1;
            }
        }
        if( i_x==-1 )
        {	i_x = i_begin;
        }
		else
        { // поднимаем уровень на 0.02 (прививка от плавных подъёмов перед коннектором)
          double f_min = data[i_begin];
          double f_x = f_min + (f_max-f_min)*0.02;
          for( int i=i_x; i<i_max; i++ )
          {	if(data[i]>f_x)
          	{ i_x = i;
          break;
            }
          }
        }
        it1->begin = i_x - 1;
		// правую границу события слева тоже сдвигаем 
        lit = it1;
        lit--;
        lit->end = it1->begin;

#ifdef debug_lines
        /*
        // !!! для нужд отрисовки записываем это в массив (кроме как для отрисовки он пока ни для чего не нужен)
        InitialAnalysis::a[cou] = 1000; InitialAnalysis::b[cou] = data[i_x]-1000*i_x*delta_x;
        InitialAnalysis::xs[cou]=(i_x-1)*delta_x; InitialAnalysis::xe[cou]=(i_x+1)*delta_x;
        cou++;
        */
#endif
    }//for
}
//------------------------------------------------------------------------------------------------------------
// (с) Vit
void InitialAnalysis::excludeShortLinesBetweenConnectors(double* arr, EPLIST& evnts, int szc)
{
	if(evnts.size()<2)
return;
    // last указывает на последний, end указывает на адрес ПОСЛЕ последнего
	EPLIST::iterator it2, it3;
    int cou1 =0;	
	for(EPLIST::iterator it1 = evnts.begin(); it1 != (EPLIST::iterator&)evnts.end(); it1++)
	{   // пока не дойдём до коннектора
    	if(it1->type != EventParams::CONNECTOR)
    continue;
        else {cou1++;}
		if(cou1 == 1) // первый "коннектор" это мёртвая зона
    continue;
        it2 = it1;
        if(it2 == (EPLIST::iterator&)evnts.last())
    break;
        it2++;
        if(it2->type != EventParams::LINEAR)
    continue;
    	it3 = it2;
        if(it3 == (EPLIST::iterator&)evnts.last())
    break;
        it3++;
        if(it3->type != EventParams::CONNECTOR)
    continue;
    	if(it2->end - it2->begin < szc)
        { it1->end = it3->begin;
          //it2->begin = it3->begin;
          evnts.removeNext(it1);
        }
    }//for

#ifdef debug_lines
    for(EPLIST::iterator it1 = evnts.begin(); it1 != (EPLIST::iterator&)evnts.end(); it1++)
	{   int i_x = it1->begin;
        InitialAnalysis::a[cou] = 1000; InitialAnalysis::b[cou] = data[i_x]-1000*i_x*delta_x;
        InitialAnalysis::xs[cou]=(i_x-1)*delta_x; InitialAnalysis::xe[cou]=(i_x+1)*delta_x;
        col[cou] = 0xAAAAAA;
        cou++;
        i_x = it1->end-1;
        InitialAnalysis::a[cou] = 1000; InitialAnalysis::b[cou] = data[i_x]-1000*i_x*delta_x;
        InitialAnalysis::xs[cou]=(i_x-1)*delta_x; InitialAnalysis::xe[cou]=(i_x+1)*delta_x;
        col[cou] = 0x0000AA;
        cou++;
        a[cou] = 0;
        b[cou] = data[i_x];
        xs[cou] = (it1->begin)*delta_x;
        xe[cou] = (it1->end)*delta_x;
        col[cou] = 0x00AA00;
        cou++;
    }

#endif
}
//------------------------------------------------------------------------------------------------------------
// считаем, что расстояние медлу точками _динаковое_
void InitialAnalysis::calc_rms_line(double* arr, int beg, int end, double& a, double& b)
{	if(beg > end) { int temp = end; end = beg; beg = temp;}
    if(beg == end || beg < 0 || end > data_length)
    { a=0; b=0;
return;
    }
    double e1=0, e2=0, e3=0, e4=0;
    int n = end-beg+1;

    for(int i = beg; i <= end; i++)
    {   e1 += (double)i*i;// (double) от переполнения int
        e2 += i;
        e3 += i * arr[i];
        e4 += arr[i];
    }
    a = (n*e3 - e2*e4)/(n*e1 - e2*e2);
    b = (e3 - a*e1)/e2;

    a /= delta_x;
}
//------------------------------------------------------------------------------------------------------------
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
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::correctWeldCoords()
{   double d[2];
	EPLIST::iterator it = epVector.begin();
	for (; it != (EPLIST::iterator &)epVector.end(); it++)
	{	EventParams* ep = &(*it);
		if (ep->type == EventParams::SPLICE)
		{	// передний фронт
			if (it != (EPLIST::iterator &) epVector.begin())
			{	it--;
				EventParams* ep_last = &(*it);
				if (ep_last->type == EventParams::LINEAR &&
					ep_last->end - ep_last->begin > 3)
				{	linearize2point(data, ep_last->begin, ep_last->end, d);
					for(int i = ep->begin; i < (ep->begin + ep->end) / 2; i++)
					{	if(fabs(data[i] - (d[1] + d[0] * i)) > //max(minimalWeld, fabs(2 * noise[i])))
							fabs(2 * noise[i]))
						{	ep->begin = i;
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
			if (it != (EPLIST::iterator &) epVector.end()
				&& it->type == EventParams::LINEAR
				&& it->end - it->begin > 3)
			{	EventParams* ep_next = &(*it);
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
//------------------------------------------------------------------------------------------------------------
// <added by Vit>
// поскольку убрали срезание коннекторов то теперь делаем так : если найдена сварка там,
// где раньше был коннектор, то не считаем это сваркой, а оставляем коннектором
// </added by Vit>
void InitialAnalysis::findWelds(double *trans, EPLIST &vector)
{
	int halfWidth = evSizeW / 2;
	if(halfWidth < 1)
	{	halfWidth = 1;
    }
	int type;
    int x1; // начало события
    int x2; // конец события
	int k1; // середина первого участка
	int k2; // середина второго участка
	int k3; // середина третьего участка
	int c1; // точка максимума первого участка
	int c2; // точка максимума второго участка
	int c3; // точка максимума третьего участка
	int j;  // конец первого участка
	int k;  // конец второго участка
	int s;  // конец третьего участка

	EPLIST::iterator it;
	EPLIST clone; // копия списка событий
	for (it = vector.begin(); it != (EPLIST::iterator&) vector.end(); it++)
	{	clone.copy_push_back(*it);
	}
	vector.clear();
    // по всем событиям
	for (it = clone.begin(); it != (EPLIST::iterator&) clone.end(); it++)
	{	// XXX
		vector.copy_push_back(*it);
		vector.last()->n = vector.size() - 1;
		EventParams *ep1 = &*vector.last();
		it++;
		if (it != (EPLIST::iterator&)clone.end())
		{	int i;
			EventParams* ep_next = &(*it);
			// движемся с конца предыдущего до начала последующего 
			int start = ep1->end;
			int end = ep_next->begin;
			double meanNoise = 0;
			for (i = start; i < end; i++)
			{	meanNoise += fabs(noise[i]);
            }
			// среднее значение шума на участке между последними событиямми
			meanNoise /= (double)(end - start);
            // вычитаем средний наклон 
			setNonZeroTransformation(trans, meanNoise * 0.8, start, end);
			for( i=start; i<end; i+=(x2-x1) )
			{   j = i + 1;
                c1 = j;
				while(sign(trans[i]) == sign(trans[j]) && j < end)
				{	if(fabs(trans[c1]) < fabs(trans[j]))
					{	c1 = j;
                    }
					j++;
				}
				k = j + 1;
				c2 = k;
				while(sign(trans[j]) == sign(trans[k]) && k < end)
				{	if(fabs(trans[c2]) < fabs(trans[k]))
					{	c2 = k;
                    }
					k++;
				}
				s = k + 1;
				c3 = s;
				while(sign(trans[k]) == sign(trans[s]) && s < end)
				{	if(fabs(trans[c3]) < fabs(trans[s]))
					{	c3 = s;
                    }
					s++;
				}
				k1 = (i+j)/2; k2 = (j+k)/2; k3 = (k+s)/2;
				x1 = i;
				double nf = 1; // noise factor
				if( fabs(trans[c1]) < minimalThreshold + nf*rnoise[c1]) // linear part
				{	type = EventParams::LINEAR;
					x2 = j;
				}
				else if(trans[c1] > minimalWeld*0.8 || trans[c1] < -minimalWeld*0.8) // && j - i < 3 * evSizeC) // weld
				{	type = EventParams::SPLICE;
					x2 = j;
				}
				else if(trans[c1] > noise[k1]*3 && trans[c2] < -noise[k2]*3
                		&& trans[c1] > minimalConnector && trans[c2] < -minimalConnector) // reflection (connector, anyway)
				{	type = EventParams::CONNECTOR;
					x2 = k;
				}
				else if(trans[c1] > minimalConnector && trans[c2] < minimalConnector
                		&& fabs((trans[c1]+trans[c2])/(trans[c1]-trans[c2]))<0.5) //reflection
                {	type = EventParams::CONNECTOR;
					x2=k;
				}
				else if(trans[c1] > minimalConnector &&	trans[c3] < minimalConnector
                		&& fabs((trans[c1] + trans[c3]) / (trans[c1] - trans[c3])) < 0.5
                        && k - j < (int)(halfWidth * 1.5))  //reflection
				{	type = EventParams::CONNECTOR;
					x2 = s;
				}
				else //linear
				{	type = EventParams::LINEAR;
					x2 = j;
				}
                // <added by Vit>
				// Если сварка наползает концом на коннектор.
                // Эту проверку надо обязательно делать именно в конце, перед самым созданием события.
                if( x2 >= end   && type == EventParams::SPLICE && ep_next->type == EventParams::CONNECTOR)
				{	type = EventParams::LINEAR;
					x2 = j;
				}
				// если провал начинается не с нуля, то это не сварка , а следствие находящегося слева коннектора
                if( fabs(trans[x1-1]) > 0 )
				{	type = EventParams::LINEAR;
					x2 = j;
				}
                // </added by Vit>
				if (x1 < lastNonZeroPoint)
				{	EventParams ep1;
					ep1.n = vector.size();
					ep1.type = type;
					ep1.begin = x1;
					ep1.end = x2;
					vector.copy_push_back(ep1);
				}
			}//for
		}//if
		it--;
	}//for
	clone.clear();
}
//------------------------------------------------------------------------------------------------------------
// предполагается, что все события - коннекторы, так как ничего другого мы ещё не распознавали
// Но вообще обрежет  всё, что есть ( обрезает линией ) 
void InitialAnalysis::excludeAllEvents(EPLIST &vector, double *data, double *data_woc)
{	int i, j;
	for (i = 0; i < data_length; i++)
	{	data_woc[i] = data[i];
    }
	double delta = 0;
	double* _d = NULL;
	EPLIST::iterator it;
	for (it = vector.begin(); it != (EPLIST::iterator&) vector.end(); it++)
	{	EventParams* ep_last = &(*it);
		it++;
		if ( it != (EPLIST::iterator&)vector.end() )
		{	EventParams* ep1 = &(*it);
            double d[2];
			if (ep_last->end != ep1->begin)
			{	linearize2point(data, ep_last->end, ep1->begin, d); // вычислить коэффициенты прямой
            }
			else
			{	if (ep1 != &(*vector.begin()))
				{	EPLIST::iterator it_2 = it;
					it_2--;
					it_2--;
					EventParams* ep_beforelast = &(*it_2);
					linearize2point(data, ep_beforelast->end, ep1->begin, d);
				}
				else
				{	linearize2point(data, ep_last->begin, ep1->begin, d);
                }
			}
			if (ep_last != &(*vector.begin()))
			{	delta += ((data[ep_last->begin] + _d[0] * (ep_last->end - ep_last->begin)) - data[ep_last->end]);
				for (j = ep_last->end; j < ep1->begin; j++)
				{	data_woc[j] += delta;
                }
			}
			else
			{	for (j = ep_last->begin; j < ep_last->end; j++)
				{	data_woc[j] = data[ep_last->end] + d[0] * (j - ep_last->end);
                }
			}
			_d = d;
			for (j = ep1->begin; j < ep1->end; j++)
			{	data_woc[j] = data[ep1->begin] + d[0] * (j - ep1->begin) + delta;
            }
		}
		it--;
	}
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::sewLinearParts()
{
	EPLIST::iterator it = epVector.begin();
	while (it != (EPLIST::iterator&) epVector.end()) {
		EventParams* ep = &(*it);
		if (ep->type == EventParams::LINEAR) {
			it++;
			if (it != (EPLIST::iterator&) epVector.end()) {
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
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::excludeShortEvents(int linearLength, int weldLength, int connectorLength)
{
	EPLIST::iterator it = epVector.begin();

	if (it != (EPLIST::iterator&) epVector.end())
		it++;
	while (it != (EPLIST::iterator&) epVector.end())
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
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::correctEnd()
{	EPLIST::iterator it;
	int l = (epVector.back()).begin + evSizeC * 2;
	it = epVector.end();
	it--;
	while( it != (EPLIST::iterator&) epVector.begin())
    {	EventParams* ep = &(*it);
 		if( ep->type == EventParams::CONNECTOR
				&& ep->aLet_connector > minimalEndingSplash
				&& ep->a1_connector >= 0
				&& ep->a2_connector >= 0
				&& ep->begin <= lastNonZeroPoint
				&& ep->end - ep->begin > 0.5*evSizeC //!!! 0.5
          )
        {	l = min( lastNonZeroPoint-1, min(ep->end, ep->begin+evSizeC*2) );
			if( l>=lastNonZeroPoint )
			{	l = lastNonZeroPoint-1;
            }
			ep->end = l;
			break;
		}
		else
        {	it --;
			epVector.removeNext(it); // XXX
			continue;
		}
	}
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::setEventParams()
{
	int counter = 0;
	unsigned int i;
	if(eps)
    {	delete[] eps;
    }
	eps = new EventParams *[epVector.size()];

	double *d = new double[2];
	for (EPLIST::iterator it = epVector.begin(); it != (EPLIST::iterator&) epVector.end(); it++)
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
		eps[i]->width_weld = evSizeC*0.9;

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
		// ищем (приблизительно) центр
		double Cm0 = 0;
		double Cm1 = 0;
		double th = i > 0
			? A1 + ALet * 0.7
			: A1 + ALet - 0.5;
		for (j = eps[i]->begin; j < eps[i]->end; j++)
		{	if (data[j] > th)
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
		{	if (data[j] > th)
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
}
//------------------------------------------------------------------------------------------------------------
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
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::convolutionOfNoise(int n_points)
{
	int i, n;
	double meanValue;
	for(i = 0; i < lastNonZeroPoint; i++)
	{	n=0;
		meanValue = 0.;
		for (int j = i; j < min(i + n_points, lastNonZeroPoint); j++)
		{	meanValue += noise[j];
			n++;
		}
		if (n > 0) 
		{	meanValue/=n;
        }
		noise[i] = meanValue;
	}
}
//------------------------------------------------------------------------------------------------------------
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
		//	ep->type = na->getEventType2(window); //
			///////////////////////////////////
		}
	}

	delete []window;
	delete na;
	//siewLinearParts();
}
//------------------------------------------------------------------------------------------------------------
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
//------------------------------------------------------------------------------------------------------------
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
//------------------------------------------------------------------------------------------------------------
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
	if(deviation <0.000001)
        deviation = 1.;
	for(i=0; i<wndSize; i++)
	{
		wnd[i] = wnd[i]/deviation;
	}
}
//------------------------------------------------------------------------------------------------------------
#endif // USE_NEURAL_NETWORK
