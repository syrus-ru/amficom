//----------------------------------------
#include "InitialAnalysis.h"
#include "../Common/MathRef.h"
#include "Histogramm.h"

#include <assert.h>
#include <stdlib.h>
#include "../An2/findLength.h"
#include "../An2/findNoise.h"

// Коэффициент запаса: эта величина умножается на оценку 3 сигма шума, и используется как добавка к порогам обнаружения.
// Предполагаемое теоретическое значение 1; Практически понадобилось 1 - 4.
// бОльшие значения коэффициента соответствуют меньшей чувствительности.
const double THRESHOLD_TO_NOISE_RATIO = 6;
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

    this->wlet_width			= nonReflectiveSize;

    events_lp = new ArrList();

	performAnalysis();
}
//------------------------------------------------------------------------------------------------------------
InitialAnalysis::~InitialAnalysis()
{	delete[] type;
	delete[] noise;

    delete[] f_wlet;
    events_lp->disposeAll();

    delete events_lp;
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performAnalysis()
{	lastNonZeroPoint = getLastPoint();
    f_wlet	= new double[lastNonZeroPoint];
#ifdef debug_VCL
    f_tmp = new double[lastNonZeroPoint];
#endif
	noise	= new double[lastNonZeroPoint];
    type	= new double[data_length];

    wn = getWLetNorma(wlet_width, waveletType);

    // вычисляем уровень шума по saa и поправку к чувствительности на его основе
    { const int sz = lastNonZeroPoint;
      const int width = wlet_width;
      fillNoiseArray1(data, sz, 1 + width/20, noise);
    }

	// выполняем вейвлет-преобразование
	// f_wlet - вейвлет-образ функции, wlet_width - ширина вейвлета, wn - норма вейвлета
    performTransformation(data, 0, lastNonZeroPoint, f_wlet, wlet_width, wn);

	// вычитаем из коэффициентов преобразования(КП) постоянную составляющую
	centerWletImage(f_wlet);
    { // ищём все всплески вейвлет-образа
      ArrList splashes; // создаем пустой ArrList
      findAllWletSplashes(f_wlet, splashes); // заполняем массив splashes объектами
      findEventsBySplashes(splashes); // по выделенным всплескам определить события (по сути - сгруппировать всплсески)
      // используем ArrList и его объекты
      splashes.disposeAll(); // очищаем массив ArrList
    } // удаляем пустой массив splashes

    deleteAllEventsAfterLastConnector();
    correctAllConnectorsFronts(data);
    correctAllSpliceCoords(data);
    excludeShortLinesBetweenConnectors(data, wlet_width);
    addLinearPartsBetweenEvents();

return;

	// устанавливаем в 0 КП, которые меньше уровня шума или минимального уровня события
	//setNonZeroTransformation(transC, minimalThreshold, 0, lastNonZeroPoint);
	//setNonZeroTransformation(transW, minimalThreshold, 0, lastNonZeroPoint);
	// setNonZeroTransformation_(transW, minimalThreshold, 0, lastNonZeroPoint); // !!! TEST

	// определяем координаты и типы событий по КП
	//findConnectors(transC, transW, 0, lastNonZeroPoint - 1, epVector);

	// sewLinearParts();
	// correctWeldCoords();
	// исключаем события с длиной, меньшей половины характерного размера
	// excludeShortEvents(max(wlet_width/2, 10), max(wlet_width/2, 3), max(wlet_width/2, 4));

	//	sewLinearParts();
	// корректируем конец волокна согласно минимального отражения
	// correctEnd();
	// setEventParams();
	// уточняем координаты начала коннекторов
    //correctAllConnectorsFronts(data, epVector);// by Vit
    //excludeShortLinesBetweenConnectors(data, epVector, wlet_width);// by Vit

//  	correctEnd();// чтобы конец правильно доопределился надо сначала вызвать setEventParams()
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::findEventsBySplashes(ArrList& splashes)
{   double mc = minimalConnector;
    double mw = minimalWeld;
    for(int i=0; i<splashes.getLength()-1; i++)
	{   Splash &sp1 = *(Splash*)splashes[i];
        Splash &sp2 = *(Splash*)splashes[i+1];
    	double dist = sp2.begin - sp1.end;
		// отмечаем , что найден новый коннектор
        if( dist<wlet_width*10				// если всплески близко
        	&& (sp1.sign>0 && sp2.sign<0)   // первый положительный, а второй - отрицательный
            && ( (sp1.square/sp2.square>0.1 && sp1.square/sp2.square<2.0) || events_lp->getLength()==0) // всплески должны быть сравнимы (первый коннектор это мёртвая зона)
            && ( sp1.f_extr>mc+noise[sp1.x_extr] || -sp2.f_extr>mc+noise[sp2.x_extr] ) // если из двух сосоедних всплесков хотя бы один превышает порог коннектора, то это коннектор
          )
        {   EventParams *ep = new EventParams;
            ep->n = events_lp->getLength();
            ep->type = EventParams::CONNECTOR;
            ep->begin = sp1.begin;
            ep->end = sp2.end;
            events_lp->add(ep);
            i++;
        }
        else if( fabs(sp1.f_extr) > mw+noise[sp1.x_extr] )
        {	EventParams *ep = new EventParams;
	        ep->n = events_lp->getLength();
            ep->type = EventParams::SPLICE;
            ep->begin = sp1.begin;
            ep->end = sp1.end;
            events_lp->add(ep);
        }
    }
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::findAllWletSplashes(double* f_wlet, ArrList& splashes)
{   //minimalThreshold,	//минимальный уровень события
	//minimalWeld,		//минимальный уровень неотражательного события
	//minimalConnector,	//минимальный уровень отражательного события
    for(int i=0; i<lastNonZeroPoint; i++)
    {	if(fabs(f_wlet[i])<=minimalThreshold + noise[i])
     continue;
        int begin = i;// запомнминаем, в каком месте мы пересекли порог срабатывания
        int sign = f_wlet[i]>0 ? 1 : -1;
        int i_extr = i;
        double f_extr = f_wlet[i_extr];
        double square = 0;
        for(  ; i<lastNonZeroPoint; i++)
        {  	if( f_wlet[i]*sign <= minimalThreshold + noise[i])// sign учитывает возможность того, что функция может перескочить через коридор
        break;
      		// считаем площадь
            square += fabs(f_wlet[i]); // поскольку потом будем считать отношение, то считаем с точностью до произвольного множителя
            // ищем экстремум
			if( f_wlet[i]*sign > f_extr*sign )
        	{	i_extr = i;
            	f_extr = f_wlet[i_extr];
            }
        }
        int end = i-1;
#ifdef debug_lines
		// !!! тестовые линии
        xs[cou] = begin*delta_x; xe[cou] = begin*delta_x;
        ys[cou] = -minimalConnector*2; ye[cou] = minimalConnector*2; cou++;
        xs[cou] = end*delta_x; xe[cou] = end*delta_x;
        ys[cou] = -minimalConnector*2; ye[cou] = minimalConnector*2; col[cou]= 0xFFFFFF; cou++;
#endif
		// выясняем другие пороги у данного события
        int j;
        for(j=begin; j>=0; j--)
        { if(fabs(f_wlet[j])<=minimalThreshold)
       	break;
        }
		int begin_nonoise = j;
        for(j=end; j<lastNonZeroPoint; j++)
        { if(fabs(f_wlet[j])<=minimalThreshold)
       	break;
        }
        int end_nonoise=j;
#ifdef debug_lines
		// !!! тестовые линии
        xs[cou] = begin_nonoise*delta_x; xe[cou] = begin_nonoise*delta_x;
        ys[cou] = -minimalConnector*2; ye[cou] = minimalConnector*2; col[cou]= 0x00AA77; cou++;
        xs[cou] = end_nonoise*delta_x; xe[cou] = end_nonoise*delta_x;
        ys[cou] = -minimalConnector*2; ye[cou] = minimalConnector*2; col[cou]= 0x0077AA; cou++;
#endif


        Splash* splash = new Splash(begin, end, begin_nonoise, end_nonoise, sign, f_extr, i_extr, square);
        splashes.add(splash);
    }
#ifdef debug_lines
    // !!! тестовые линии
    // отображаем пороги
    xs[cou] = 0; ys[cou] =  minimalThreshold; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] =  minimalThreshold; col[cou] = 0x004444; cou++;
    xs[cou] = 0; ys[cou] = -minimalThreshold; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] = -minimalThreshold; col[cou] = 0x004444; cou++;
    xs[cou] = 0; ys[cou] =  minimalWeld; 	  xe[cou] = lastNonZeroPoint*delta_x; ye[cou] =  minimalWeld;      col[cou] = 0x009999; cou++;
    xs[cou] = 0; ys[cou] = -minimalWeld; 	  xe[cou] = lastNonZeroPoint*delta_x; ye[cou] = -minimalWeld;	   col[cou] = 0x009999; cou++;
	xs[cou] = 0; ys[cou] =  minimalConnector; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] =  minimalConnector; col[cou] = 0x00FFFF; cou++;
    xs[cou] = 0; ys[cou] = -minimalConnector; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] = -minimalConnector; col[cou] = 0x00FFFF; cou++;
#endif
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::centerWletImage(double* fw)
{	f_wlet_avrg = calcWletMeanValue(fw, -0.5, 0, 100);
	for(int i=0; i<lastNonZeroPoint; i++)
    {	f_wlet[i] -= f_wlet_avrg;
    }
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
void InitialAnalysis::fillNoiseArray1(double *y, int N, double Neff, double *outNoise)
{	findNoiseArray(y, outNoise, N); // external function from findNoise.cpp
	int i;
	if (Neff < 1)
	{	Neff = 1;
	}
	for (i = 0; i < N; i++)
	{	// кто скажет, что sqrt(Neff) в цикле сильно влияет на
		// быстродействие АМФИКОМа, того назову плохим словом.
		outNoise[i] *= THRESHOLD_TO_NOISE_RATIO * 3 / sqrt(Neff);
	}
}
//------------------------------------------------------------------------------------------------------------
int InitialAnalysis::getLastPoint()
{   int lastPoint = findReflectogramLength(data, data_length) - 1;
	if (lastPoint < 0)
		lastPoint = 0;
	//if(lastPoint + 10 < data_length) lastPoint += 10; не понятно, зачем это вообзе надо 
    return lastPoint;
}
//------------------------------------------------------------------------------------------------------------
// f- исходня ф-ция,
// f_wlet - вейвлет-образ
void InitialAnalysis::performTransformation(double* f, int begin, int end, double* f_wlet, int freq, double norma)
{   double tmp;
	for(int i=begin; i<end; i++)
	{	tmp = 0.;
		for(int j = max(i-freq, 0); j < min(i+freq+1, end); j++)
		{	tmp = tmp + f[j]*wLet(j-i, freq, norma, waveletType);
        }
		f_wlet[i] = tmp;
	}
}
//------------------------------------------------------------------------------------------------------------
// вычислить среднее значение вейвлет-образа 
double InitialAnalysis::calcWletMeanValue(double *f, double from, double to, int columns)
{   //возможное затухание находится в пределах [0; -0.5] дБ
	Histogramm* histo = new Histogramm(from, to, columns);
	histo->init(f, lastNonZeroPoint, 0, lastNonZeroPoint-1);
	double mean_att = histo->getMaximumValue();
	delete histo;
	return mean_att;
}
//------------------------------------------------------------------------------------------------------------
// поскольку убрали срезание коннекторов то теперь делаем так : если найдена сварка там,
// где раньше был коннектор, то не считаем это сваркой, а оставляем коннектором
// Предполагается, что все события идут ПО ПОРЯДКУ ОДИН ЗА ДРУГИМ !
void InitialAnalysis::correctAllConnectorsFronts(double *arr)
{	if( events_lp->getLength() < 2 )
return;
	for(int n=0; n<events_lp->getLength(); n++)
    {   EventParams* ev1 = (EventParams*)(*events_lp)[n];
        if( ev1->type != EventParams::CONNECTOR )// пока не дойдём до коннектора
    continue;
    	// ищем точку на фронте коннектора такую, что всё слква от неё - меьше, а справа - не меньше
        int i_begin = ev1->begin, i_end = ev1->end;
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
        double ev1_beg_old = ev1->begin;
        ev1->begin = i_x - 1;
		// правую границу события слева (если оно есть)тоже сдвигаем
        if(n>0)
        { EventParams* ev2 = (EventParams*)(*events_lp)[n-1];
          // двигаем только если границы "цепляются"
          if(fabs(ev2->end - ev1_beg_old)<2){ ev2->end = ev1->begin; }
        }
    }
}
//------------------------------------------------------------------------------------------------------------
// проводим разномасштабный авейвлет-анализ для уточнения положения сварок  
void InitialAnalysis::correctAllSpliceCoords(double *arr)
{
#ifdef debug_lines
	//return; // !!!
	int coln = -1;
    for(double width = wlet_width; width>wlet_width/8.1; width/=2.)
    {   if(width == wlet_width)
    continue;
    	coln++;
    	int color[] = {0xFFFFFF, 0x0000FF, 0x00FF00, 0xFF7733};
    	wn = getWLetNorma(width, waveletType);
    	performTransformation(data, 0, lastNonZeroPoint, f_wlet, width, wn);
    	for(int n=1; n<events_lp->getLength(); n++)
        {   EventParams* ev = (EventParams*)(*events_lp)[n];
        	int i_beg = ev->begin-wlet_width, i_end = ev->end+wlet_width;
            if(i_beg<0){ i_beg=0; }
            if(i_end>lastNonZeroPoint-2) { i_end=lastNonZeroPoint-2; }
            for(int i=i_beg; i<i_end; i++)
            {   double x1 = i, y1 = f_wlet[i];
            	double x2 = i+1, y2 = f_wlet[i+1];
                xs[cou] = x1*delta_x;
                xe[cou] = x2*delta_x;
                ys[cou] = y1;
                ye[cou] = y2;
                col[cou] = color[coln];
                cou++;
            }
        }
    }
	wn = getWLetNorma(wlet_width, waveletType);
	performTransformation(data, 0, lastNonZeroPoint, f_wlet, wlet_width, wn);
#endif
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::excludeShortLinesBetweenConnectors(double* arr, int szc)
{   if(events_lp->getLength()<2)
return;
    int cou1 =0;
	for(int n1=0, n2, n3; n1<events_lp->getLength(); n1++)
	{   // пока не дойдём до коннектора
        EventParams* ev1 = (EventParams*)(*events_lp)[n1];
    	if(ev1->type != EventParams::CONNECTOR)
    continue;
        else {cou1++;}
		if(cou1 == 1) // первый "коннектор" это мёртвая зона
    continue;
        n2 = n1+1;
        if(n2 >= events_lp->getLength())
    break;
		EventParams* ev2 = (EventParams*)(*events_lp)[n2];
        if(ev2->type != EventParams::LINEAR)
    continue;
        n3 = n2+1;
        if(n3 >= events_lp->getLength())
    break;
	    EventParams* ev3 = (EventParams*)(*events_lp)[n3];
        if(ev3->type != EventParams::CONNECTOR)
    continue;
    	if(ev2->end - ev2->begin < szc)
        { ev1->end = ev3->begin;
          events_lp->slowRemove(n2); 
        }
    }//for
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::deleteAllEventsAfterLastConnector()
{   for(int i=events_lp->getLength()-1; i>0; i--)
	{   EventParams* ev = (EventParams*)(*events_lp)[i];
    	if( ev->type != EventParams::CONNECTOR)
        {   events_lp->slowRemove(i);
        }
        else
     	{
    break;
        }
    }
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::addLinearPartsBetweenEvents()
{   ArrList* events = new ArrList();
	for(int i=0; i<events_lp->getLength(); i++)
    {   EventParams *cur = (EventParams*)(*events_lp)[i];
        if (i>0)
        { EventParams *prev = (EventParams*)(*events_lp)[i-1];
          if(prev->end < cur->begin)
          { EventParams *ep = new EventParams();
            ep->type = EventParams::LINEAR;
            ep->begin = prev->end;
            ep->end = cur->begin;
            events->add(ep);
          }
        }
        events->add(cur);
    }
    delete events_lp;
    events_lp = events;
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
/*
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
*/
//------------------------------------------------------------------------------------------------------------
/*
void InitialAnalysis::correctEnd()
{
	EPLIST::iterator it;
	int l = (epVector.back()).begin + wlet_width * 2;
	it = epVector.end();
	it--;
	while( it != (EPLIST::iterator&) epVector.begin())
    {	EventParams* ep = &(*it);
 		if( ep->type == EventParams::CONNECTOR
				&& ep->aLet_connector > minimalEndingSplash
				&& ep->a1_connector >= 0
				&& ep->a2_connector >= 0
				&& ep->begin <= lastNonZeroPoint
				&& ep->end - ep->begin > 0.5*wlet_width //!!! 0.5
          )
        {	l = min( lastNonZeroPoint-1, min(ep->end, ep->begin+wlet_width*2) );
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
*/
//------------------------------------------------------------------------------------------------------------
