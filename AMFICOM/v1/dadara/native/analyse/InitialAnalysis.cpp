//----------------------------------------
#include "InitialAnalysis.h"
#include "../common/MathRef.h"
#include "Histogramm.h"

#include <math.h>
#include <assert.h>
#include <stdlib.h>
#include "../an2/findLength.h"
#include "../an2/findNoise.h"
#include "../wavelet/wavelet.h"

#include "../common/prf.h"

// Коэффициент запаса: эта величина умножается на оценку 3 сигма шума, и используется как добавка к порогам обнаружения.
// бОльшие значения коэффициента соответствуют меньшей чувствительности.
const double THRESHOLD_TO_NOISE_RATIO = 2;
//------------------------------------------------------------------------------------------------------------
// Construction/Destruction
InitialAnalysis::InitialAnalysis(
	double* data,				//точки рефлектограммы
	int data_length,			//число точек
	double delta_x,				//расстояние между точками (м)
	double minimalThreshold,	//минимальный уровень события
	double minimalWeld,			//минимальный уровень неотражательного события
	double minimalConnector,	//минимальный уровень отражательного события
	int reflectiveSize,			//характерная длина отражательного события
	int nonReflectiveSize,		//характерная длина неотражательного события
	int lengthTillZero,			//вычисленная заранее длина ( ==0 -> найти самим)
	double *externalNoise)		//вычисленный заранее шум ( ==0 -> ищем сами)
{
#ifdef DEBUG_INITIAL_ANALYSIS
	logf = fopen(DEBUG_INITIAL_WIN_LOGF, "a");
	assert(logf);
	fprintf(logf, "=== IA invoked\n"
		"len %d deltaX %g minTh %g minWeld %g minConn %g\n",
		data_length, delta_x, minimalThreshold, minimalWeld, minimalConnector);
	fprintf(logf, "refSize %d nRefSize %d lTZ %d extNoise %s\n",
		reflectiveSize, nonReflectiveSize, lengthTillZero, externalNoise ? "present" : "absent");
#endif

#ifdef debug_lines
    cou = 0;
    for(int i=0; i<sz; i++){col[i] = -1;}
#endif
	this->delta_x				= delta_x;
	this->minimalThreshold		= minimalThreshold;
	this->minimalWeld			= minimalWeld;
	this->minimalConnector		= minimalConnector;
	this->data_length			= data_length;
	this->data					= data;
    this->reflectiveSize		= reflectiveSize;
    this->wlet_width			= nonReflectiveSize;

    events = new ArrList();

	if (lengthTillZero <= 0)
		lastNonZeroPoint = getLastPoint();
	else
		lastNonZeroPoint = lengthTillZero - 1;

	f_wlet	= new double[lastNonZeroPoint];
#ifdef debug_VCL
	f_tmp   = new double[lastNonZeroPoint];
#endif
	noise	= new double[lastNonZeroPoint];
	type	= new double[data_length];

	// если массив с уровнем шума не задан извне,
	// либо пользователь IA не указал его размер,
	// считаем шум сами
	if (externalNoise == 0 || lengthTillZero <= 0)
	{	prf_b("IA: noise");
		// вычисляем уровень шума
		{ const int sz = lastNonZeroPoint;
		  const int width = wlet_width;
		  fillNoiseArray(data, data_length, sz, 1 + width/20, noise);
		}
	}
	else
	{	int i;
		for (i = 0; i < lastNonZeroPoint; i++)
		{	noise[i] = externalNoise[i];// FIXME - не учитывает THRESHOLD_TO_NOISE_RATIO		
        }
	}

	prf_b("IA: analyse");
	performAnalysis();
	prf_b("IA: done");
#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(logf, "IA: resulting nEvents = %d\n", (int)(getEvents().getLength()));
#endif
}
//------------------------------------------------------------------------------------------------------------
InitialAnalysis::~InitialAnalysis()
{	delete[] type;
	delete[] noise;
    delete[] f_wlet;

    events->disposeAll();

    delete events;
#ifdef DEBUG_INITIAL_ANALYSIS
	fclose(logf);
#endif
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performAnalysis()
{
	// выполняем вейвлет-преобразование
	// f_wlet - вейвлет-образ функции, wlet_width - ширина вейвлета, wn - норма вейвлета
    wn = getWLetNorma(wlet_width);
    performTransformationOnly(data, 0, lastNonZeroPoint, f_wlet, wlet_width, wn);
	calcAverageFactor(f_wlet, wlet_width, wn);
	centerWletImageOnly(f_wlet, wlet_width, 0, lastNonZeroPoint, wn);// вычитаем из коэффициентов преобразования(КП) постоянную составляющую

#if 0
	{
		FILE *f = fopen ("noise2.tmp", "w");assert(f);
		int i;
		for (i = 0; i < lastNonZeroPoint; i++)
			fprintf(f,"%d %g %g %g\n", i, data[i], f_wlet[i], noise[i]);
		fclose(f);
	}
#endif
    
	{ // ищём все всплески вейвлет-образа
      ArrList splashes; // создаем пустой ArrList
      findAllWletSplashes(f_wlet, splashes); // заполняем массив splashes объектами
      findEventsBySplashes(splashes); // по выделенным всплескам определить события (по сути - сгруппировать всплсески)
      // используем ArrList и его объекты
      splashes.disposeAll(); // очищаем массив ArrList
    } // удаляем пустой массив splashes

    deleteAllEventsAfterLastConnector();// если ни одного коннектора не будет найдено, то удалятся все события
    correctAllConnectorsFronts(data);
    excludeShortLinesBetweenConnectors(data, wlet_width);
    correctAllSpliceCoords();// поскольку уточнение двигает соседние события, то к этому моменту динейные участки должы уже существовать ( поэтому вызов после addLinearPartsBetweenEvents() )
    addLinearPartsBetweenEvents();
	trimAllEvents();
	verifyResults(); // проверяем ошибки
}
//-------------------------------------------------------------------------------------------------
void InitialAnalysis::findEventsBySplashes(ArrList& splashes)
{   double mc = minimalConnector;
    double mw = minimalWeld;
//* мёртвую зону ищём  чуть иначе
	int shift = 0;
	int begin = -1;
	int end = -1;
 	if( splashes.getLength() <=2 )
return;
	Splash* sp1 = (Splash*)splashes[0];
    Splash* sp2;
    if(sp1->sign<0)
    { begin = 0;
      end  = sp1->end_thr;
    }
    else
    { for(;;)
      {	sp1 = (Splash*)splashes[shift];
        sp2 = (Splash*)splashes[shift+1];
        shift++;
        if(sp2->sign<0)
        { begin = sp1->begin_thr;
          end = sp2->end_thr;
      break;
        }
      }
    }
    if(end !=-1 )
    { EventParams *ep = new EventParams;
      ep->type = EventParams::DEADZONE;
      ep->begin = begin; ep->end = end;
      if(ep->end >= lastNonZeroPoint){ ep->end = lastNonZeroPoint-1;}
      //ep->gain = 0;  ep->gain_thr = 0;
      events->add(ep);
    }
// ищем остальные коннекторы  и сварки 
    for(int i = shift+1; i<splashes.getLength()-1; i++)
	{   sp1 = (Splash*)splashes[i];
        sp2 = (Splash*)splashes[i+1];
    	double dist = sp2->begin_thr - sp1->end_thr;
		// отмечаем , что найден новый коннектор
        if( dist<reflectiveSize*2			// если всплески близко (это же значение используется в SetConnectorParamsBySplashes)
        	&& (sp1->sign>0 && sp2->sign<0) // первый положительный, а второй - отрицательный
			&& ( sp1->begin_conn_n !=-1 )
          )
        {   EventParams *ep = new EventParams;
            SetConnectorParamsBySplashes((EventParams&)*ep, (Splash&)*sp1, (Splash&)*sp2 );
		    events->add(ep);
            i++;// потому что коннектор состоит из двух всплесков
        }
		// две сварки "+" и "-" очень близко 
        else if( dist<reflectiveSize/2			// если всплески очень близко
        	&& (sp1->sign>0 && sp2->sign<0) // первый положительный, а второй - отрицательный
			&& ( sp1->begin_weld_n != -1 && sp2->begin_weld_n != -1)// и при этом сварочные
            && ( sp1->begin_conn_n == -1 )// но это не коннектор
          )
        {   EventParams *ep = new EventParams;
            SetUnrecognizedParamsBySplashes((EventParams&)*ep, (Splash&)*sp1, (Splash&)*sp2 );
		    events->add(ep);
            i++;// потому что состоит из двух всплесков
        }

        else if( sp1->begin_weld_n != -1 && fabs(sp1->end_weld_n-sp1->begin_weld_n)<1) //сварка
        {	EventParams *ep = new EventParams;
			SetSpliceParamsBySplash( (EventParams&)*ep, (Splash&)*sp1 );
            events->add(ep);
        }
    }
}
// -------------------------------------------------------------------------------------------------
// к этому мменту мы предполагаем, что свёртка f_wlet сделана для вейвлета шириной wlet_width и не менялась
void InitialAnalysis::SetSpliceParamsBySplash( EventParams& ep, Splash& sp)
{   if(sp.sign>0) { ep.type = EventParams::GAIN; }
    else 		  { ep.type = EventParams::LOSS; }
    ep.begin = sp.begin_weld;
    ep.end = sp.end_weld+1;
    if(ep.end>=lastNonZeroPoint){ep.end = lastNonZeroPoint-1;}
    if(ep.begin<0){ep.begin=0;}
    double max = -1;
	for(int i=sp.begin_weld_n; i<sp.end_weld_n; i++)
    { double res = (fabs(f_wlet[i])-minimalWeld)/noise[i] - 1;
      if(max<res) max = res;
    }
    ep.R = max;
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::SetConnectorParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2 )
{  ep.type = EventParams::REFLECTIVE;
   ep.begin = sp1.begin_thr;
   if(ep.begin<0){ep.begin=0;}
   ep.end = sp2.end_thr;
   if(ep.end>=lastNonZeroPoint){ep.end = lastNonZeroPoint;}

   double max = -1;
   int i;
   for(i=sp1.begin_conn_n ; i<sp1.end_conn_n; i++)
   { double res = (f_wlet[i]-minimalConnector)/noise[i] - 1;
     if(max<res) max = res;
   }
   ep.R1 = max;
   max = -1;
   for(i=sp2.begin_thr ; i<sp2.end_thr; i++)
   { double res = fabs(f_wlet[i])/minimalThreshold - 1;
     if(max<res) max = res;
   }
   ep.R2 = max;
   double l = sp2.begin_thr - sp1.end_conn_n, l_max = reflectiveSize*2;
   assert(l>=-1);// -1 может быть так как мы искуствнно расширяем на одну точку каждый всплеск (начало ДО уровня, а конец ПОСЛЕ )
   ep.R3 = 2*ep.R2*(l_max-l)/(l+wlet_width)*(wlet_width/l_max);
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::SetUnrecognizedParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2 )
{  ep.type = EventParams::UNRECOGNIZED;
   ep.begin = sp1.begin_thr;
   if(ep.begin<0){ep.begin=0;}
   ep.end = sp2.end_thr;
   if(ep.end>=lastNonZeroPoint){ep.end = lastNonZeroPoint;}
}
// -------------------------------------------------------------------------------------------------
// ВАЖНО: Считаем, что minimalThreshold < minimalWeld < minimalConnector
void InitialAnalysis::findAllWletSplashes(double* f_wlet, ArrList& splashes)
{   //minimalThreshold,	//минимальный уровень события
	//minimalWeld,		//минимальный уровень неотражательного события
	//minimalConnector,	//минимальный уровень отражательного события
    for(int i=1; i<lastNonZeroPoint; i++)// 1 т.к. i-1
    {   if(fabs(f_wlet[i])<=minimalThreshold)
     continue;
		Splash& spl = (Splash&)(*(new Splash()));// раз уж пересекли хотя бы один порог, то объект уже должен быть создан;
        spl.begin_thr = i;
		int sign, sign_cur;
   		if(f_wlet[i]>0) { sign = 1;}
        else            { sign = -1;}
        for(  ; i<lastNonZeroPoint; i++)
        {   if(f_wlet[i]>0) { sign_cur = 1;}
	        else            { sign_cur = -1;}
        	// минимальные на рост
        	if( fabs(f_wlet[i])>= minimalThreshold+noise[i] && spl.begin_thr_n == -1)
            {	spl.begin_thr_n = i-1;
            }
            if( spl.begin_thr_n != -1 && spl.end_thr_n == -1
            	&& ( fabs(f_wlet[i])<= minimalThreshold+noise[i] || sign_cur!=sign || i==lastNonZeroPoint-1)
	          )
            {	spl.end_thr_n = i;
            }
			// сварочные
            if( fabs(f_wlet[i])>= minimalWeld && spl.begin_weld == -1)
            {	spl.begin_weld = i-1;
            }
            if( spl.begin_weld != -1 && spl.end_weld == -1
            	&& ( fabs(f_wlet[i])<= minimalWeld || sign_cur!=sign || i==lastNonZeroPoint-1)
              )
            {	spl.end_weld = i;
            }
            if( fabs(f_wlet[i])>= minimalWeld+noise[i] && spl.begin_weld_n == -1)
            {	spl.begin_weld_n = i-1;
            }
            if( spl.begin_weld_n != -1 && spl.end_weld_n == -1
            	&& (fabs(f_wlet[i])<= minimalWeld+noise[i] || sign_cur!=sign || i==lastNonZeroPoint-1)
              )
            {	spl.end_weld_n = i;
            }
            // коннекторные
            if( fabs(f_wlet[i])>= minimalConnector && spl.begin_conn == -1)
            {	spl.begin_conn = i-1;
            }
            if( spl.begin_conn != -1 && spl.end_conn == -1
            	&& ( fabs(f_wlet[i])<= minimalConnector || sign_cur!=sign || i==lastNonZeroPoint-1)
              )
            {	spl.end_conn = i;
            }
            if( fabs(f_wlet[i])>= minimalConnector+noise[i] && spl.begin_conn_n == -1)
            {	spl.begin_conn_n = i-1;
            }
            if( spl.begin_conn_n != -1 && spl.end_conn_n == -1
            	&& (fabs(f_wlet[i])<= minimalConnector+noise[i] || sign_cur!=sign || i==lastNonZeroPoint-1)
              )
            {	spl.end_conn_n = i;
            }
			// минимальные на спад
            if( fabs(f_wlet[i])<=minimalThreshold || sign_cur!=sign || i==lastNonZeroPoint-1)
        	{	spl.end_thr = i;
     	break;
     		}
        }
		spl.sign = sign;

        spl.begin_thr--;
        if(spl.begin_thr < spl.end_thr)// begin>end только если образ так и не пересёк ни разу верхний порог
        {   splashes.add(&spl);
        }
#ifdef debug_lines
        //xs[cou] = begin*delta_x; xe[cou] = begin_nonoise*delta_x; ys[cou] = -minimalConnector*2; ye[cou] = minimalConnector*2; cou++;
        //xs[cou] = end_nonoise*delta_x; xe[cou] = end_nonoise*delta_x;     ys[cou] = -minimalConnector*2; ye[cou] = minimalConnector*2; col[cou]= 0xFFFFFF; cou++;
        // отображаем пороги
        xs[cou] = 0; ys[cou] =  minimalThreshold; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] =  minimalThreshold; col[cou] = 0x004444; cou++;
        xs[cou] = 0; ys[cou] = -minimalThreshold; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] = -minimalThreshold; col[cou] = 0x004444; cou++;
        xs[cou] = 0; ys[cou] =  minimalWeld; 	  xe[cou] = lastNonZeroPoint*delta_x; ye[cou] =  minimalWeld;      col[cou] = 0x009999; cou++;
        xs[cou] = 0; ys[cou] = -minimalWeld; 	  xe[cou] = lastNonZeroPoint*delta_x; ye[cou] = -minimalWeld;	   col[cou] = 0x009999; cou++;
        xs[cou] = 0; ys[cou] =  minimalConnector; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] =  minimalConnector; col[cou] = 0x00FFFF; cou++;
        xs[cou] = 0; ys[cou] = -minimalConnector; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] = -minimalConnector; col[cou] = 0x00FFFF; cou++;
#endif
	}
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::calcAverageFactor(double* fw, int scale, double norma1)
{	double f_wlet_avrg = calcWletMeanValue(fw, -0.5, 0, 500);
	average_factor = f_wlet_avrg * norma1 / getWLetNorma2(scale);
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::fillNoiseArray(double *y, int data_length, int N, double Neff, double *outNoise)
{	findNoiseArray(y, outNoise, data_length, N); // external function from findNoise.cpp
	int i;
	if (true || Neff < 1) // кажется, без true никак
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
	{	lastPoint = 0;
    }
	//if(lastPoint + 10 < data_length) lastPoint += 10; не понятно, зачем это вообще надо 
    return lastPoint;
}
//------------------------------------------------------------------------------------------------------------
// f- исходная ф-ция,
// f_wlet - вейвлет-образ
void InitialAnalysis::performTransformationAndCenter(double* f, int begin, int end, double* f_wlet, int scale, double norma)
{	// transform
	performTransformationOnly(f, begin, end, f_wlet, scale, norma);
	centerWletImageOnly(f_wlet, scale, begin, end, norma);
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::centerWletImageOnly(double* f_wlet, int scale, int begin, int end, double norma1)
{   // shift (calcAverageFactor must be performed by now!)
	double f_wlet_avrg = average_factor * getWLetNorma2(scale) / norma1;
	for(int i=begin; i<end; i++)
    {	f_wlet[i] -= f_wlet_avrg;
    }
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performTransformationOnly(double* f, int begin, int end, double* f_wlet, int freq, double norma)
{
	int len = end - begin;
	SineWavelet wavelet;
	wavelet.transform(freq, f, lastNonZeroPoint, begin, end - 1, f_wlet + begin, norma);
}
//------------------------------------------------------------------------------------------------------------
// вычислить среднее значение вейвлет-образа 
double InitialAnalysis::calcWletMeanValue(double *fw, double from, double to, int columns)
{   //возможное затухание находится в пределах [0; -0.5] дБ
	Histogramm* histo = new Histogramm(from, to, columns);
	histo->init(fw, 0, lastNonZeroPoint-1);
	double mean_att = histo->getMaximumValue();
	delete histo;
	return mean_att;
}
//------------------------------------------------------------------------------------------------------------
// поскольку убрали срезание коннекторов то теперь делаем так : если найдена сварка там,
// где раньше был коннектор, то не считаем это сваркой, а оставляем коннектором
// Предполагается, что все события идут ПО ПОРЯДКУ ОДИН ЗА ДРУГИМ !
void InitialAnalysis::correctAllConnectorsFronts(double *arr)
{	if( events->getLength() < 2 )
return;
	// у первого коннектора ( мёртвой зоны) корректировать нечего (считаем, что перед мёртвой зоной ничего нет )
	for(int n=1; n<events->getLength(); n++)
    {   EventParams* ev1 = (EventParams*)(*events)[n];
        if( ev1->type != EventParams::REFLECTIVE )// пока не дойдём до коннектора
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
        EventParams* ev2 = (EventParams*)(*events)[n-1];  // мы помим, что "for(int n=1...."
        // двигаем только если границы "цепляются"
        if(fabs(ev2->end - ev1_beg_old)<2){ ev2->end = ev1->begin; }// вместо <2, можно было просто проверить на равенство, но я почему-то так не сделал ...  

    }
}
//------------------------------------------------------------------------------------------------------------
// проводим разномасштабный авейвлет-анализ для уточнения положения сварок
void InitialAnalysis::correctAllSpliceCoords()
{	for(int n=1; n<events->getLength(); n++)
    {	EventParams* ev = (EventParams*)(*events)[n];
    	if(ev->type == EventParams::GAIN || ev->type == EventParams::LOSS)
        {	correctSpliceCoords(n);
        }
    }
}
//------------------------------------------------------------------------------------------------------------
// ф-я ПОРТИТ вейвлет образ !  (так как использует тот же массив для хранения образа на другом масштабе)
// Уточнение может только сужать сварки, но никак не расширять
void InitialAnalysis::correctSpliceCoords(int n)
{	EventParams* ev_lp = (EventParams*)(*events)[n];
    EventParams& ev = *ev_lp;
	// если это не сварка, то выход
    if( !(ev.type == EventParams::GAIN || ev.type == EventParams::LOSS) )
return;
	//prf_b("correctSpliceCoords: enter");
	const double noise_factor = 0;  // 0 , если мы не учитываем шум в пределах событий
    const double angle_factor = 1.8; // расширения светового конуса для защиты от низкочастотных помех на больших масштабах
	const double factor = 1.2; // множитель геометрической прогрессии
	const int nscale = 20; // количество разных масштабов
	int width = wlet_width; // frame-width: ширина окна (относительно границы события), в котором мы проводим дополнительный анализ
	int w_l = ev.begin, w_r = ev.end; // w_l - wavelet_left: границы вейвлет-образа на текущем масштабе
    int left_cross = (int )(w_l+width*angle_factor), right_cross = (int )(w_r-width*angle_factor); // точки пересечения световым конусом оси ОХ (по сути эквивалентно запоминанию масштаба, при котором это произошло, потому что точка X, где вейвлет пересёк порог, запоминается отдельно)

	int i;
#ifdef debug_lines
    int coln=-1,a=0xFFFFFF,b=0x0000FF,c=0x00FF00,d=0xFF7733,color[]={a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d};
#endif
    // анализируем при разныех масштабах
	for(int step=0; step<=nscale; step++)
    {   width = (int)(width/factor);//(int )(wlet_width/pow(factor,step) +0.5);// чтобы не накапливать ошибки
    	if(width<=1)
    break;
		//prf_b("correctSpliceCoords: getWLetNorma");
	    wn = getWLetNorma(width);
		//prf_b("correctSpliceCoords: performTransformationAndCenter");
#ifdef debug_lines
		performTransformationAndCenter(data, ev.begin, ev.end+1, f_wlet, width, wn);
#else
		performTransformationAndCenter(data, w_l, w_r+1, f_wlet, width, wn);
#endif
		//prf_b("correctSpliceCoords: processing");
		// считаем добавку к шуму ( степень немонотонности от пересечения порога до максимума )
		// сначала ищём положение экстремума при данном масштабе
        int i_max = w_l;
        double f_max = f_wlet[i_max];
        for(i=w_l; i<w_r; i++)
        {	if( fabs(f_wlet[i])>fabs(f_max) ) // поскольку образ в пределах одного события знакопостояный, то можем работать с модулями
        	{	i_max = i; f_max = f_wlet[i_max];
            }
        }
		double f_lmax = f_wlet[w_l], df_left = 0, df_right = 0;
        for(i=w_l; i<i_max; i++) // добавки слева от пересечения порога до максимума
        { if( fabs(f_wlet[i])>fabs(f_lmax) ) { f_lmax = f_wlet[i];}// новый максимум  отклонения
          if( df_left<fabs(f_lmax-f_wlet[i]) ) { df_left=fabs(f_lmax-f_wlet[i]);} // новый максимальный уровень падения
        }
		f_lmax = f_wlet[w_r];
        for(i=w_r; i>i_max; i--) // добавки справа от пересечения порога до максимума
        { if(fabs(f_wlet[i])>fabs(f_lmax)) { f_lmax = f_wlet[i];}// новый максимум отклонения
          if(df_right<fabs(f_lmax-f_wlet[i])){ df_right=fabs(f_lmax-f_wlet[i]);} // новый максимальный уровень падения
        }

		// ищем пересечение слева, пытаясь сдвинуть границу влево ( то есть пока i+width<=left_cross )
        for(i=w_l; i<w_r && i+width*angle_factor<=left_cross; i++)
        {	if(fabs(f_wlet[i])>=minimalWeld+noise[i]*noise_factor+df_left)
        	{	w_l=i-1;//w_l=i;
	            if(w_l+width*angle_factor<left_cross){ left_cross = (int )(w_l+width*angle_factor);}
        break;
            }
        }
   		// ищем пересечение справа
        for(int j=w_r; j>w_l && j-width*angle_factor>=right_cross; j--) // j-width>=right_cross - условие минимума в повёрнутой на 45 СК
        {	if(fabs(f_wlet[j])>=minimalWeld+noise[j]*noise_factor+df_right)
        	{	w_r=j+1;//w_r=j;
	            if(w_r-width*angle_factor>right_cross)
                { right_cross = (int )(w_r-width*angle_factor);}
        break;
            }
        }
#ifdef debug_lines //рисуем вейвлет образы для данного масштаба
		{ coln++;
          for(int i=ev.begin; i<ev.end; i++)
          { double x1=i, y1=f_wlet[i], x2=i+1, y2=f_wlet[i+1];
            xs[cou]=x1*delta_x; ys[cou]=y1; xe[cou]=x2*delta_x; ye[cou]=y2;
            col[cou]=color[coln];
            if(i<w_l || i>w_r) col[cou] = 0x888888;
            cou++;
          }
        }
#endif
    }
	//prf_b("correctSpliceCoords: scales done");
	if( w_l < w_r  )
    {   double old_left = ev.begin;
        double old_right = ev.end;
		// можем только сужать события
    	if(w_l>old_left && w_l<old_right ) { ev.begin = w_l;}
    	if(w_r<old_right && w_r>old_left)  { ev.end = w_r;}
    }
#ifdef debug_VCL
    wn = getWLetNorma(wlet_width);
  	performTransformationAndCenter(data, 0, lastNonZeroPoint, f_wlet, wlet_width, wn);
#endif
	//prf_b("correctSpliceCoords: return");
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::excludeShortLinesBetweenConnectors(double* arr, int szc)
{   if(events->getLength()<2)
return;
    int cou1 =0;
	for(int n1=0, n2, n3; n1<events->getLength(); n1++)
	{   // пока не дойдём до коннектора
        EventParams* ev1 = (EventParams*)(*events)[n1];
    	if(ev1->type != EventParams::REFLECTIVE)
    continue;
        else {cou1++;}
		if(cou1 == 1) // первый "коннектор" это мёртвая зона
    continue;
        n2 = n1+1;
        if(n2 >= events->getLength())
    break;
		EventParams* ev2 = (EventParams*)(*events)[n2];
        if(ev2->type != EventParams::LINEAR)
    continue;
        n3 = n2+1;
        if(n3 >= events->getLength())
    break;
	    EventParams* ev3 = (EventParams*)(*events)[n3];
        if(ev3->type != EventParams::REFLECTIVE)
    continue;
    	if(ev2->end - ev2->begin < szc)
        { ev1->end = ev3->begin;
          events->slowRemove(n2); 
        }
    }//for
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::deleteAllEventsAfterLastConnector()
{   for(int i=events->getLength()-1; i>0; i--)
	{   EventParams* ev = (EventParams*)(*events)[i];
    	if( ev->type != EventParams::REFLECTIVE)
        {   events->slowRemove(i);
        }
        else
     	{
    break;
        }
    }
}
//------------------------------------------------------------------------------------------------------------
// ВАЖНО: предполагаем что линейных событий ещё нет ВООБЩЕ ! (иначе будет неправильно работать)
void InitialAnalysis::addLinearPartsBetweenEvents()
{   ArrList* events_new = new ArrList();
	for(int i=0; i<events->getLength(); i++)
    {   EventParams *cur = (EventParams*)(*events)[i];
        if (i>0)
        { EventParams *prev = (EventParams*)(*events)[i-1];
          if(prev->end < cur->begin)
          { EventParams *ep = new EventParams();
            ep->type = EventParams::LINEAR;
            ep->begin = prev->end;
            ep->end = cur->begin;
            events_new->add(ep);
          }
        }
        events_new->add(cur);
    }
    delete events;
    events = events_new;
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
// из-за расширения всплесков события могу немного наползать друг на друга, выравниваем их
void InitialAnalysis::trimAllEvents()
{	int prevEnd = 0;
	for(int i=0; i<events->getLength(); i++)
    {	EventParams& ev = *(EventParams*)(*events)[i];
		if(ev.begin < 0)
        { ev.begin = 0;
        }
		if(ev.begin != prevEnd && i != 0)
        {   if( fabs(ev.begin = prevEnd) > 2)
        	{   bool const GAP_BETWEEN_EVENTS_NOT_TOO_LARGE = false;
            	assert(GAP_BETWEEN_EVENTS_NOT_TOO_LARGE);
            }
        	ev.begin = prevEnd;
        }
		if(ev.end <= ev.begin)
        { ev.end = ev.begin+1;
        }
		if(ev.end >= lastNonZeroPoint)
        { ev.end = lastNonZeroPoint-1;
        }
		prevEnd = ev.end;
    }
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::verifyResults()
{	int prevEnd = 0;
	for(int i=0; i<events->getLength(); i++)
    {	EventParams& ev = *(EventParams*)(*events)[i];
//*//#ifndef debug_VCL
		assert(ev.begin >= 0);
		assert(ev.end < lastNonZeroPoint);
		assert(ev.end >= ev.begin);
		assert(ev.end > ev.begin); // >, not just >=
		assert(ev.begin == prevEnd || i == 0); // XXX
//#else
/*/
//сюда понатыкать брекпоинтов
		if(!(ev.begin >= 0))
        { int o=0;}
		if(!(ev.end < lastNonZeroPoint))
        { int o=0;}
		if(!(ev.end >= ev.begin))
        { int o=0;}
		if(!(ev.end > ev.begin)) // >, not just >=
        { int o=0;}
		if(!(ev.begin == prevEnd || i == 0)) // XXX
        { int o=0;}
//*/
//#endif
		prevEnd = ev.end;

    }
}
