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

static	SineWavelet wavelet; // используемый вейвлет

//#define USE_BASELINE_RANGE

#ifdef USE_BASELINE_RANGE
const double MAX_LINEAR_LOSS_DB_PER_KILOMETER = 0.39;
const double MIN_LINEAR_LOSS_DB_PER_KILOMETER = 0.15;
#endif

#define SEARCH_EOT_BY_WLET
//#define SPLICE_CAN_BE_EOT
//#define NONID_CAN_BE_EOT

#define xsign(f) ((f)>=0?1:-1)
inline double fmin(double a, double b) { return a < b ? a : b; }
inline double fmax(double a, double b) { return a > b ? a : b; }
inline int imin(int a, int b) { return a < b ? a : b; }
inline int imax(int a, int b) { return a > b ? a : b; }
//------------------------------------------------------------------------------------------------------------
// Construction/Destruction
InitialAnalysis::InitialAnalysis(
	double* data,				//точки рефлектограммы
	int data_length,			//число точек
	double delta_x,				//расстояние между точками (м)
	double minimalThreshold,	//минимальная амплитуда события
	double minimalWeld,			//минимальная амплитуда неотражательного события
	double minimalConnector,	//минимальная амплитуда уровень отражательного события
	double minimalEnd,			//минимальный амплитуда отражения в конце волокна
	double min_eot_level,		//минимальный абс. уровень отражения в конце волокна
	double noiseFactor,			// множитель для уровня шума (около 2.0)
	int nonReflectiveSize,		//характерная длина неотражательного события
	double rACrit,				// порог "большого" коннектора
	int rSSmall,				// макс. длина для маленького коннектора
	int rSBig,					// макс. длина для большого коннектора
	double scaleFactor,			// множитель прогрессии масштабов (1.0 .. 2.0 ..)
	int lengthTillZero,			//вычисленная заранее длина ( ==0 -> найти самим)
	double *externalNoise)		//вычисленный заранее шум ( ==0 -> ищем сами)
{
#ifdef DEBUG_INITIAL_ANALYSIS
	logf = fopen(DEBUG_INITIAL_WIN_LOGF, "a");
	assert(logf);
	fprintf(logf, "=== IA invoked\n"
		"len %d deltaX %g minTh %g minWeld %g minConn %g minEnd %g minEotLevel %g noiseFactor %g\n",
		data_length, delta_x, minimalThreshold, minimalWeld, minimalConnector, minimalEnd, min_eot_level, noiseFactor);
	fprintf(logf, "nRefSize %d rACrit %g rSBig %d rSSmall %d lTZ %d extNoise %s\n",
		nonReflectiveSize, rACrit, rSBig, rSSmall, lengthTillZero, externalNoise ? "present" : "absent");
	fflush(logf);
#endif
#ifdef debug_lines
    cou = 0;
    for(int i=0; i<sz; i++){col[i] = -1;}
#endif
	this->delta_x				= delta_x;
	//this->minimalThresholdB		= minimalThreshold; -- игнорируем minimalThreshold
	this->minimalWeld			= minimalWeld;
	this->minimalConnector		= minimalConnector;
    this->minimalEnd			= minimalEnd;
	this->minimalEotLevel		= min_eot_level;
    this->noiseFactor			= noiseFactor;
	this->data_length			= data_length;
	this->data					= data;
    this->rACrit 				= rACrit;
    this->rSSmall				= rSSmall;
    this->rSBig					= rSBig;
    int scaleB					= nonReflectiveSize;

    events = new ArrList();
	if (lengthTillZero <= 0){
		lastPoint = getLastPoint();
    }
	else{
		lastPoint = lengthTillZero - 1;
    }

	noise	= new double[lastPoint + 1];
#ifdef debug_VCL
	debug_f_wlet = new double[lastPoint + 1];
	f_tmp   = new double[lastPoint + 1];
	type	= new double[data_length];
#endif
	// если массив с уровнем шума не задан извне, либо пользователь IA не указал его размер, считаем шум сами
	if (externalNoise == 0 || lengthTillZero <= 0)
	{	prf_b("IA: noise");
		// вычисляем уровень шума
		{ const int sz = lastPoint + 1;
		  //fillNoiseArray(data, data_length, sz, 1 + width/20, noise);
		  fillNoiseArray(data, data_length, sz, 1.0, noiseFactor, noise);
		}
	}
	else
	{	int i;
		for (i = 0; i <= lastPoint; i++)
		{	noise[i] = externalNoise[i] * noiseFactor;
        }
	}
	// noise convolution to wavelet
    WaveletDataConvolution(noise, lastPoint+1, scaleB);

	prf_b("IA: analyse");
#ifdef SEARCH_EOT_BY_WLET
	double *f_wletTEMP	= new double[data_length]; // space for temporal wavelet image parts
#else
	double *f_wletTEMP	= new double[lastPoint + 1]; // space for temporal wavelet image parts
#endif
	performAnalysis(f_wletTEMP, scaleB, scaleFactor);
	delete[] f_wletTEMP;
#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(logf, "IA: f_wletTEMP deleted\n");
#endif
	prf_b("IA: done");
#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(logf, "IA: resulting nEvents = %d\n", (int)(getEvents().getLength()));
#endif
}
//------------------------------------------------------------------------------------------------------------
InitialAnalysis::~InitialAnalysis()
{
#ifdef debug_VCL
	delete[] debug_f_wlet;
	delete[] type;
#endif
	delete[] noise;

    events->disposeAll();

    delete events;
#ifdef DEBUG_INITIAL_ANALYSIS
	fclose(logf);
#endif
}
//------------------------------------------------------------------------------------------------------------
// поиск прекрывающихся событий
// XXX: пересечение - по thr или по weld порогу?
// note: null-ссылки пропускаем (они остаются при перемещении объектов из одного списка в другой)
// проверяем перекрыте с учетом разномасштабности
int InitialAnalysis::splashesOverlap(Splash &spl1, Splash &spl2) {
	// если всплески одного знака - учитываем разность масштабов
	int delta = spl1.sign == spl2.sign 
		? abs(spl1.scale - spl2.scale) // integer abs
		: 0;
	return
		spl1.begin_thr + 1 < spl2.end_thr + delta && spl2.begin_thr + 1 < spl1.end_thr + delta;
}
//--------------------------------------------
int InitialAnalysis::findMinOverlappingSplashIndex(Splash &spl, ArrList &arrList) {
	int j;
	for (j = 0; j < arrList.getLength(); j++) {
		if (arrList[j] && splashesOverlap(spl, *(Splash*)arrList[j]))
			return j;
	}
	return -1;
}
//--------------------------------------------
int InitialAnalysis::findMaxOverlappingSplashIndex(Splash &spl, ArrList &arrList) {
	int j;
	int ret = -1;
	for (j = 0; j < arrList.getLength(); j++) {
		if (arrList[j] && splashesOverlap(spl, *(Splash*)arrList[j]))
			ret = j;
	}
	return ret;
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performAnalysis(double *TEMP0, int scaleB, double scaleFactor)
{	// ======= ПЕРВЫЙ ЭТАП АНАЛИЗА - ПОДГОТОВКА =======
	{	// выполняем вейвлет-преобразование на начальном масштабе, определяем общий наклон, смещаем вейвлет-образ
		// f_wletB - вейвлет-образ функции, scaleB - ширина вейвлета, wn - норма вейвлета
		double wn = getWLetNorma(scaleB);
		performTransformationOnly(data, 0, lastPoint + 1, TEMP0, scaleB, wn);
		calcAverageTilt(TEMP0, scaleB, wn);
	}

	const double resKm = 1e-3 * delta_x; // разрешение, дБ/км

#ifdef USE_BASELINE_RANGE
	double tiltU = -MIN_LINEAR_LOSS_DB_PER_KILOMETER * resKm;
	double tiltL = -MAX_LINEAR_LOSS_DB_PER_KILOMETER * resKm;
	//fprintf(stderr, "tiltU %g tiltL %g average_tilt %g\n", tiltU, tiltL, average_tilt);
	fprintf(stderr, "dbkmU %g dbkmL %g dbkmAvg %g\n", -tiltU / resKm, -tiltL / resKm, -average_tilt / resKm);
	if (tiltU < average_tilt)
		tiltU = average_tilt;
	if (tiltL > average_tilt)
		tiltL = average_tilt;
#endif

#ifdef debug_VCL
	{	int i;
		for (i = 0; i <= lastPoint; i++)
			debug_f_wlet[i] = TEMP0[i];
	}
#endif

#ifdef DEBUG_INITIAL_ANALYSIS
	{	// FIXME: debug dump
		FILE *f = fopen ("noise2.tmp", "w");
		if (f) {
			//double baselineB = getWletBaseline(scaleB, getWLetNorma(scaleB), average_tilt);
			double wlet2dbkmFactor = baselineToTilt(scaleB, getWLetNorma(scaleB), 1.0) / resKm;
			int i;
			for	(i = 0; i <= lastPoint; i++)
				fprintf(f,"%d %g %g %g %g %g\n",
				i,
				data[i],
				TEMP0[i] * wlet2dbkmFactor,
				TEMP0[i] * wlet2dbkmFactor - tiltU / resKm,
				TEMP0[i] * wlet2dbkmFactor - tiltL / resKm,
				noise[i] * wlet2dbkmFactor);
			fclose(f);
		}
	}
#endif

	// < в этой точке TEMP0 не несет нужной информации >

	// ======= ВТОРОЙ ЭТАП АНАЛИЗА - ОПРЕДЕЛЕНИЕ ВСПЛЕСКОВ =======
	ArrList accSpl; // текущий список найденных сварок (пустой)
	int scaleCount;
	for (scaleCount = -1; scaleCount <= 1; scaleCount++) {
		int scale = scaleCount <= 0 ? scaleCount < 0 ?
				  (int)(scaleB / scaleFactor)
				: scaleB
				: (int)(scaleB * scaleFactor);
		if (scale == scaleB && scaleCount != 0)
			continue;
		if (scale < getMinScale())
			continue;
		setShiftedThresholds(scale);// определяем действующие пороги

		double *TEMPnc = TEMP0;
		// < в этой точке TEMPnc не несет используемой информации >

		// проводим поиск всплесков на данном масштабе
		ArrList newSpl;
		performTransformationOnly(data, 0, lastPoint + 1, TEMPnc, scale, getWLetNorma(scale));

#ifdef USE_BASELINE_RANGE
		double baselineU = tiltToBaseline(scale, getWLetNorma(scale), tiltU);
		double baselineL = tiltToBaseline(scale, getWLetNorma(scale), tiltL);
#else
		double baselineU = tiltToBaseline(scale, getWLetNorma(scale), average_tilt);
		double baselineL = tiltToBaseline(scale, getWLetNorma(scale), average_tilt);
#endif

#ifdef DEBUG_INITIAL_ANALYSIS
	{	// FIXME: debug dump
		FILE *f;
		f = fopen ("noiserq.dat", "r");
		if (f != 0) {
			fclose (f);
			char sbuf[32];
			sprintf(sbuf,"noise-%d.tmp", scale);
			FILE *f = fopen (sbuf, "w");
			if (f) {
				int i;
				for	(i = 0; i <= lastPoint; i++)
					fprintf(f,"%d %g %g %g %g\n",
					i, data[i], TEMPnc[i] - baselineU, TEMPnc[i] - baselineL, noise[i]);
				fclose(f);
			}
		}
	}
#endif
		findAllWletSplashes(TEMPnc, baselineU, baselineL, scale, newSpl);
		// < в этой точке TEMPnc не несет используемой информации >
		//fprintf(stderr, "scale %d: %d splashes\n", scale, newSpl.getLength()); fflush(stderr);
		// анализируем, что делать  с каждым найденным всплеском
		int i;
		for (i = 0; i < newSpl.getLength(); i++) {
			Splash *cnSplash = (Splash*)newSpl[i];
			// ищем, с какими всплесками accSpl пересекается текущий cnSplash
			int minAccIndex = findMinOverlappingSplashIndex(*cnSplash, accSpl);
			int maxAccIndex = findMaxOverlappingSplashIndex(*cnSplash, accSpl);
			enum {
				ACTION_IGNORE  = 1,
				ACTION_INSERT  = 2,
				ACTION_REPLACE = 3
			} action = ACTION_IGNORE;
			int replaceIndex = -1;
			if (minAccIndex < 0) { // новый всплеск
				action = ACTION_INSERT;
			} else if (minAccIndex < maxAccIndex) { // пересекает несколько всплесков acc
				action = ACTION_IGNORE;
			} else { // пересекли ровно один всплеск acc
				Splash *caSplash = (Splash*)accSpl[minAccIndex];
				int minBackIndex = findMinOverlappingSplashIndex(*caSplash, newSpl);
				int maxBackIndex = findMaxOverlappingSplashIndex(*caSplash, newSpl);
				if (maxBackIndex > minBackIndex) { // соответствующий всплеск пересекает еще какие-то, кроме нашего
					action = ACTION_IGNORE;
				} else { // связь взаимно-однозначна
					assert(minBackIndex >= 0); // Vit: -1 если в обратную сторону ни с кем не пересеклись  
                    const double exponent = 0.75; // показатель степени для функции поиска оптимального масштаба; лежит в (0;1)
					if (fabs(cnSplash->f_extr) / pow(cnSplash->scale, exponent)
						> fabs(caSplash->f_extr) / pow(caSplash->scale, exponent) ) {
						action = ACTION_REPLACE;
						replaceIndex = minAccIndex;
					}
					else {
						action = ACTION_IGNORE;
					}
				}
			}

			// NB: ACTION_REPLACE и ACTION_INSERT производится только для
			// всплесков, не пересекающихся ни с какими всплесками accSpl
			// и для всплесков, пересекающихся взаимно-однозначно с одним
			// accSpl-всплесков.
			// Это гарантирует, что удаление всплеска из newSpl (при
			// его перемещении в accSpl) не повлияет на обработку других
			// всплесков этого же newSpl.

			if (action == ACTION_REPLACE) {
				// удаляем splash и ставим на его место найденный
				delete (Splash*)accSpl[replaceIndex];
				accSpl.set(replaceIndex, cnSplash);
				// 'убираем' splash из newSpl списка, чтобы не удалять его дважды
				newSpl.set(i, 0);
			}
            else if (action == ACTION_INSERT) {
				// надо найти точку вставки
				int j;
				for (j = 0; j < accSpl.getLength(); j++) {
					if (((Splash*)accSpl[j])->begin_thr > cnSplash->begin_thr)
						break;
				}
				// j - точка вставки
				accSpl.slowInsert(j, cnSplash);
				// 'убираем' splash из newSpl списка, чтобы не удалять его дважды
				newSpl.set(i, 0);
			}
		}
		newSpl.disposeAll();
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
		{
			int i;
			fprintf(stderr, "accSpl dump at scale %d: Total %d splices\n", scale, accSpl.getLength());
			for (i = 0; i < accSpl.getLength(); i++) {
				fprintf(stderr, "spl[%d]: %d - %d  s %+d @ %d -- ampl %g\n",
					i,
					((Splash*)accSpl[i])->begin_thr,
					((Splash*)accSpl[i])->end_thr,
					((Splash*)accSpl[i])->sign,
					((Splash*)accSpl[i])->scale,
					((Splash*)accSpl[i])->f_extr);
			}
			fflush(stderr);
		}
#endif
	} //for (scaleIndex ... 
	if(accSpl.getLength() == 0){
return;}
	// ====== ТРЕТИЙ ЭТАП - ФИЛЬТРАЦИЯ ВСПЛЕСКОВ ======
	// удаляем всплески после спадов при маскировании
	//removedMaskedSplashes(accSpl);
	processMaskedSplashes(accSpl);

#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
		{
			int i;
			fprintf(stderr, "accSpl after removedMaskedSplashes: Total %d splices\n", accSpl.getLength());
			for (i = 0; i < accSpl.getLength(); i++) {
				fprintf(stderr, "spl[%d]: %d - %d  s %+d @ %d -- ampl %g\n",
					i,
					((Splash*)accSpl[i])->begin_thr,
					((Splash*)accSpl[i])->end_thr,
					((Splash*)accSpl[i])->sign,
					((Splash*)accSpl[i])->scale,
					((Splash*)accSpl[i])->f_extr);
			}
			fflush(stderr);
		}
#endif

	// < в этой точке TEMP0 не несет используемой информации >

	// ======= ЧЕТВЕРТЫЙ ЭТАП АНАЛИЗА - ОПРЕДЕЛЕНИЕ СОБЫТИЙ ПО ВСПЛЕСКАМ =======
	findEventsBySplashes(accSpl, scaleB); // по выделенным всплескам определить события (по сути - сгруппировать всплсески)
	processEventsBeginsEnds(TEMP0); // уточнить границы событий (может использовать accSpl через ссылки из EventParams); разрушает TEMP0
	// используем ArrList и его объекты
	accSpl.disposeAll(); // очищаем массив ArrList

	// < в этой точке TEMP0 не несет используемой информации >

	// ====== ПЯТЫЙ ЭТАП АНАЛИЗА - ОБРАБОТКА СОБЫТИЙ =======
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
	fprintf(stderr, "events before processing:\n");
	dumpEventsToStderr();
#endif
#ifdef SEARCH_EOT_BY_WLET
	double *TEMP_forEOT = TEMP0;
	int scaleEOT = scaleB * 10;
	wavelet.transform(scaleEOT, data, data_length, 0, data_length - 1, TEMP_forEOT + 0, getWLetNorma(scaleEOT));

	int eotByFall = -1;
	{
		int i;
		for (i = scaleEOT; i < lastPoint + scaleEOT && i < data_length; i++) {
			if (eotByFall < 0 || TEMP_forEOT[i] < TEMP_forEOT[eotByFall])
				eotByFall = i;
		}
		int tuneFactor = 6; // XXX: tune parameter, suits testDB when is between 3 .. 80
		int iFrom = eotByFall - scaleEOT * tuneFactor;
		if (iFrom < 0) iFrom = 0;
		TEMP_forEOT[iFrom] = 0; // just a visualization code
		bool changeSign = false;
		for (i = iFrom; i < eotByFall; i++)
			if (TEMP_forEOT[i] > 0)
				changeSign = true;
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
		fprintf(stderr, "data_lenth %d lastPoint %d eotByFall %d iFrom %d changeSign %d ",
			data_length, lastPoint, eotByFall, iFrom, changeSign);
#endif
		if (changeSign)
			eotByFall -= scaleEOT;
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
		fprintf(stderr, " new_eotByFall\n", eotByFall);
#endif
	}
	if (eotByFall > lastPoint)
		eotByFall = lastPoint;

#ifdef DEBUG_INITIAL_ANALYSIS
	///*
	{
		int i;
		FILE *f = fopen("image.tmp", "w");
		assert(f);
		for (i = 0; i < data_length; i++)
			fprintf(f, "%d %g %g\n", i, data[i], TEMP_forEOT[i]);
		fclose(f);
	}// */
#endif

    processEndOfTrace(eotByFall);// если ни одного коннектора не будет найдено, то удалятся все события правее softEotLength
#else
	processEndOfTrace(0);
#endif
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
	fprintf(stderr, "events before addLinearPartsBetweenEvents:\n");
	dumpEventsToStderr();
#endif
    addLinearPartsBetweenEvents();
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
	fprintf(stderr, "events after addLinearPartsBetweenEvents:\n");
	dumpEventsToStderr();
#endif
#ifdef NEW_SHORT_LINE_EXCLUSION
    excludeShortLinesBetweenConnectors(data, scaleB); // scaleB  - масштаб вейвлета
    excludeShortLinesBetweenLossAndConnectors(data, scaleB); 
    excludeShortLinesBetweenConnectorAndNonidentified(data, scaleB); // удаляет короткие линейные участки между неидентифицированным событием и коннектором и между коннектором и неидентифицированным событием (то есть порядок не важен)
    excludeShortLineAfterDeadzone(data, scaleB);
    excludeShortLineBeforeEnd(data, scaleB);
#endif
    excludeShortLinesBetweenConnectors(data, scaleB);
    excludeShortLinesBetweenLossAndConnectors(data, scaleB); // scaleB  - масштаб вейвлета
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
	fprintf(stderr, "events after excludeShortXxx:\n");
	dumpEventsToStderr();
#endif
	trimAllEvents(); // поскольку мы искусственно расширяем на одну точку влево и вправо события, то они могут наползать друг на друга на пару точек - это нормально, но мы их подравниваем для красоты и коректности работы программы в яве 
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
	fprintf(stderr, "events after trim:\n");
	dumpEventsToStderr();
#endif
	verifyResults(); // проверяем ошибки
#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(logf, "performAnalysis: done\n");
	fflush(logf);
#endif
}
// -------------------------------------------------------------------------------------------------
//
// ======= ФУНКЦИИ ПЕРВОГО ЭТАПА АНАЛИЗА - ПОДГОТОВКИ =======
//
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::calcAverageTilt(double* fw, int scale, double norma1)
{	double f_wlet_avrg = calcWletMeanValue(fw, lastPoint, -0.5, 0, 500);
	average_tilt = f_wlet_avrg * norma1 / getWLetNorma2(scale);
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
	fprintf(stderr, "calcAverageTilt: scale %d f_wlet_avrg %g af %g\n", scale, f_wlet_avrg, average_tilt);
#endif
}
//------------------------------------------------------------------------------------------------------------
// вычислить среднее значение вейвлет-образа
double InitialAnalysis::calcWletMeanValue(double *fw, int lastPoint, double from, double to, int columns)
{   // возможное затухание находится в пределах [0; -0.5] дБ
	Histogramm* histo = new Histogramm(from, to, columns);
	histo->init(fw, 0, lastPoint + 1);
	double mean_att = histo->getMaximumValue();
	delete histo;
	return mean_att;
}
//------------------------------------------------------------------------------------------------------------
// установить пороги в соответствии с соотношением норм и масштаба используемого вейвлета
void InitialAnalysis::setShiftedThresholds(int scale)
{
//	double f_wlet_avrg = average_factor * getWLetNorma2(scale) / getWLetNorma(scale); // средний наклон
//	double thres_factor = 0.2;// степень влияния общего наклона на сдвиг порогов
//	minimalThreshold1 = minimalThresholdB + fabs(f_wlet_avrg)*thres_factor;
//	if(minimalThreshold1 > 0.9*minimalWeld)
//	{  minimalThreshold1 = 0.9*minimalWeld;
//	}
//#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
//	fprintf(stderr, "scale %d minThreshB %g minThresh1 %g minWeld %g (af %g fwa %g)\n",
//		scale, minimalThresholdB, minimalThreshold1, minimalWeld, average_factor, f_wlet_avrg);
//#endif
//	//minimalWeld += fabs(f_wlet_avrg)*thres_factor;
}
// -------------------------------------------------------------------------------------------------
//
// ======= ФУНКЦИИ ВТОРОГО ЭТАПА АНАЛИЗА - ОПРЕДЕЛЕНИЯ ВСПЛЕСКОВ =======
//
// -------------------------------------------------------------------------------------------------
// Считаем, что minimalWeld < minimalConnector
void InitialAnalysis::findAllWletSplashes(double* f_wletnc, double baseU, double baseL, int wlet_width, ArrList& splashes)
{
	//minimalWeld,		//минимальный уровень неотражательного события
	//minimalConnector,	//минимальный уровень отражательного события

	// всплески (+) анализируем только выше baseU
	// всплески (-) анализируем только ниже baseL
	double baseMid = (baseU + baseL) / 2.0; // амплитуду всплеска считаем относительно этого уровня

	double edge_threshold_factor = 0.4;  // XXX - надо бы это снаружи задавать
    for(int i=1; i <= lastPoint-1; i++)// 1 т.к. i-1 // цикл (1)
    {
		int sign;
		double baseCur;
		if (f_wletnc[i] > baseU) {
			sign = 1;
			baseCur = baseU;
		} else if (f_wletnc[i] < baseL) {
			sign = -1;
			baseCur = baseL;
		} else
	continue;
		if ((f_wletnc[i] - baseCur) * sign < calcThresh(minimalWeld, noise[i]) * edge_threshold_factor)
	continue;
		int bt = i - 1;
		int bw = -1;
		int ew = -1;
		int bc = -1;
		int ec = -1;
		double r_weld = -1;
		double r_conn = -1;
		double r_acrit = -1;
		double f_extrM = f_wletnc[i] - baseMid; // амплитуда по отношению к средней линии
		for (; i <= lastPoint-1; i++) { // цикл (2)
			double effWeld = calcThresh(minimalWeld, noise[i]);
			double effConn = calcThresh(minimalConnector, noise[i]);
			double effACrit = calcThresh(rACrit, noise[i]);

			double ampl = (f_wletnc[i] - baseCur) * sign;
			if (ampl < effWeld * edge_threshold_factor) // стал меньше minTh
		break;

			// отслеживаем начало и конец по weld и conn порогам
			if (ampl >= effWeld) {
				ew = i + 1;
				if (bw == -1)
					bw = i - 1;
			}
			if (ampl >= effConn) {
				ec = i + 1;
				if (bc == -1)
					bc = i - 1;
			}

			// отслеживаем R-параметры
			{ double res = (ampl - effConn) / noise[i];
			  if(res > 0 && res > r_conn) { r_conn = res; }
			}
			{ double res = (ampl - effACrit) / noise[i];
			  if(res > 0 && res > r_acrit) { r_acrit = res; }
			}
			{ double res = (ampl - effWeld) / noise[i];
			  if(res > 0 && res > r_weld) {r_weld = res; }
			}

			// отслеживаем амплитуду всплеска
			if (ampl > f_extrM * sign)
			{ f_extrM = f_wletnc[i] - baseMid;
			}
		}
		int et = i; // на этой точке всплеск уже закончился и, возможно, начался следующий
		assert(et > bt + 1); // трудно поверить, чтобы цикл (2) не выполнился ни разу. Для этого нужен очень странный FPU // FIXME: debug only
		if (et > bt + 1) // но на всякий случай проверяем, чтобы избежать зацикливания (в этом случае i-- делать не надо)
			i--; // ставим точку перед возможным началом следующего всплеска, т.к. в цикле (1) есть пост-операция i++
		if (bw == -1)
	continue; // Weld-порог так и не пересекли - такой всплеск не создаем (XXX: в будущем анализе, возможно, они понадобяться для интерпретации более крупных соседних всплесков)

		if (r_weld < 0)
			assert (r_weld == -1);
		if (r_conn < 0)
			assert (r_conn == -1);
		if (r_acrit < 0)
			assert (r_acrit == -1);

		Splash& spl = (Splash&)(*(new Splash(wlet_width)));
        spl.f_extr = f_extrM;
		spl.sign = sign;
        spl.begin_thr = bt;
		spl.end_thr = et;
		spl.begin_weld = bw;
		spl.end_weld = ew;
		spl.begin_conn = bc;
		spl.end_conn = ec;
		spl.r_acrit = r_acrit;
		spl.r_weld = r_weld;
		spl.r_conn = r_conn;
		spl.base_tilt = baselineToTilt(wlet_width, getWLetNorma(wlet_width), baseCur);

		// добавляем всплеск к списку найденных (в принципе, можем и проигнорировать - но это пока не используется)
        if( spl.begin_thr < spl.end_thr // begin>end только если образ так и не пересёк ни разу верхний порог
	        && spl.begin_weld != -1 // !!!  добавляем только существенные всплески ( если эту проверку убрать, то распознавание коннекторов надо изменить, так как если между двумя коннекторными всплесками вдруг окажется случайный незначимый всплеск вверх, то конннектор распознан НЕ БУДЕТ ! )
           )
        {  splashes.add(&spl);
#ifdef debug_lines
           double begin = spl.begin_thr, end = spl.end_thr, s = spl.sign;
           xs[cou] = begin*delta_x; xe[cou] = begin*delta_x; ys[cou] = -minimalConnector*2*0.9; ye[cou] = minimalConnector*2*1.1; cou++;
           xs[cou] = end*delta_x; xe[cou] = end*delta_x; ys[cou] = -minimalConnector*2*1.1; 	ye[cou] = minimalConnector*2*0.9; col[cou]= 0xFFFFFF; cou++;
           double c = spl.begin_conn ==-1 ? 0xFF0000 : 0x0000FF;
           if(s>0){	xs[cou] = begin*delta_x; xe[cou] = end*delta_x; ys[cou] = minimalConnector*2*1.1;  ye[cou] = minimalConnector*2*0.9;  col[cou]=c; cou++;}
           else   {	xs[cou] = begin*delta_x; xe[cou] = end*delta_x; ys[cou] = -minimalConnector*2*0.9; ye[cou] = -minimalConnector*2*1.1; col[cou]=c; cou++;}
#endif
        }
        else
        {  delete &spl;
        }
	}
    if(splashes.getLength() == 0)
return;

#ifdef debug_lines
    // отображаем пороги
//    xs[cou] = 0; ys[cou] =  minimalThreshold1; xe[cou] = lastPoint*delta_x; ye[cou] =  minimalThreshold1; col[cou] = 0x004444; cou++;
//    xs[cou] = 0; ys[cou] = -minimalThreshold1; xe[cou] = lastPoint*delta_x; ye[cou] = -minimalThreshold1; col[cou] = 0x004444; cou++;
    xs[cou] = 0; ys[cou] =  minimalWeld; 	  xe[cou] = lastPoint*delta_x; ye[cou] =  minimalWeld;      col[cou] = 0x009999; cou++;
    xs[cou] = 0; ys[cou] = -minimalWeld; 	  xe[cou] = lastPoint*delta_x; ye[cou] = -minimalWeld;	   col[cou] = 0x009999; cou++;
    xs[cou] = 0; ys[cou] =  minimalConnector; xe[cou] = lastPoint*delta_x; ye[cou] =  minimalConnector; col[cou] = 0x00FFFF; cou++;
    xs[cou] = 0; ys[cou] = -minimalConnector; xe[cou] = lastPoint*delta_x; ye[cou] = -minimalConnector; col[cou] = 0x00FFFF; cou++;
#endif
}
// -------------------------------------------------------------------------------------------------
//
// ======= ФУНКЦИИ ТРЕТЬЕГО ЭТАПА АНАЛИЗА - ОПРЕДЕЛЕНИЯ СОБЫТИЙ ПО ВСПЛЕСКАМ =======
//
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::processMaskedSplashes(ArrList &accSpl) {
	// @todo вынести эти параметры анализа
	const double A_MAX = 15; // амплитуда насыщения источника звона, дБ
	const double ZVON_RATIO = 0.03; // начальная отн. амплитуда звона, разы; рекомендую 0.01 .. 0.03 .. 0.1
	const double CRIT_DIST = 250.0; // затухание звона в e раз, метры

	int j;
	for (j = 0; j < accSpl.getLength(); j++) {
		Splash *sL = (Splash*)accSpl[j];
		if (sL->sign > 0)
			continue; // пропускаем подъемы
		if (sL->end_conn < 0)
			continue; // спад - не достиг коннекторного порога, игнорируем
		double A0 = fabs(sL->f_extr);
		if (A0 > A_MAX) A0 = A_MAX;
		int k;
		for (k = j + 1; k < accSpl.getLength(); k++) {
			Splash *sR = (Splash*)accSpl[k];
			double A1 = fabs(sR->f_extr);
			double dist = sR->begin_thr - sL->end_conn;
			double LH = pow(10.0, (A1 - A0) / 5.0);
			double RH = ZVON_RATIO * exp(-dist * delta_x / CRIT_DIST);
			if (LH < RH) {
				// помечаем всплеск как маскированный
				sR->setMasked(true);
				// корректируем достоверность всплеска (XXX: так ли?)
				double rMax = -(LH - RH) / RH;
				sR->lowerRFactors(rMax);
			} else {
				// (не изменяем всплеск)
				// корректируем достоверность всплеска
				double rMax = (LH - RH) / RH;
				sR->lowerRFactors(rMax);
				double LMin = pow(10.0, (- A0) / 5.0);
				if (RH < LMin)
					break; // breaks k-loop
			}
		}
	}
}

// -------------------------------------------------------------------------------------------------
//
// ======= ФУНКЦИИ ЧЕТВЕРТОГО ЭТАПА АНАЛИЗА - ОПРЕДЕЛЕНИЯ СОБЫТИЙ ПО ВСПЛЕСКАМ =======
//
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::findEventsBySplashes(ArrList& splashes, int dzMaxDist)
{
	// ищем мёртвую зону
    int i0 = processDeadZone(splashes, dzMaxDist);
	// ищем остальные коннекторы  и сварки
    for(int i = i0; i<splashes.getLength(); i++)
    {
	  EventParams *ep = 0;
	  int len;
  	  // есть ли маскированная область?
	  len = processMaskedToNonId(i, splashes);
	  if (len != 0) {
		  i += len - 1;
	continue;
	  }
	  len = findConnector(i, splashes, ep);
      if(len != 0)// если коннектор был найден
      { i+= len - 1;
	    events->add(ep);
    continue;
      }
	  len = processIfIsNonId(i, splashes);
	  if (len != 0) // если нашли неид. область
      { i+= len - 1;
    continue;
      }
	  Splash* sp1 = (Splash*)splashes[i];
	  if (i + 1 < splashes.getLength()) {
		  Splash* sp2 = (Splash*)splashes[i+1];
		  int dist = abs(sp2->begin_weld - sp1->end_weld);
		  // две сварки "+" и "-" очень близко
		  if( dist<rSSmall			// если всплески очень близко
			  && (sp1->sign>0 && sp2->sign<0) // первый положительный, а второй - отрицательный
			  && ( sp1->begin_weld != -1 && sp2->begin_weld != -1)// и при этом как минимум сварочные
			)
		  {   EventParams *ep = new EventParams;
			  double v1 = fabs(sp1->f_extr);
			  double v2 = fabs(sp2->f_extr);
			  setUnrecognizedParamsBySplashes((EventParams&)*ep, (Splash&)*sp1, (Splash&)*sp2,
				  v1 > v2 ? v1 : v2);
			  events->add(ep);
			  i++;// потому что состоит из двух всплесков
	continue;
		  }
	  }
      // сварка
      if( sp1->begin_weld!= -1 && fabs(sp1->end_weld-sp1->begin_weld)>1) //сварка
      {	EventParams *ep = new EventParams;
        setSpliceParamsBySplash(*ep, *sp1 );
        events->add(ep);
	continue;
      }
    }
}
//-----
void InitialAnalysis::processEventsBeginsEnds(double *TEMP)
{
	int i;
	int pass;
	for (pass = 0; pass < 2; pass++)
	for (i = 0; i < events->getLength(); i++)
	{
		EventParams *ep = (EventParams*) ((*events)[i]);

		// определяем пределы допустимых перемещений границ события
		int minBegin = i > 0
			? ((EventParams*) ((*events)[i - 1]))->end - 1 // разрешено наползание на 1 точку
			: 0;
		int maxEnd = i < events->getLength() - 1
			? ((EventParams*) ((*events)[i + 1]))->begin + 1
			: lastPoint;
		if (minBegin < 0)
			minBegin = 0;
		if (maxEnd > lastPoint)
			maxEnd = lastPoint;

		int type = ep->type;
		if (pass) {
			if (type == EventParams::GAIN || type == EventParams::LOSS) {
				correctSpliceCoords(TEMP, ep->spliceSplash->scale, ep->spliceSplash->base_tilt, ep, minBegin, maxEnd);
			}
		}
		else
		{
			if (type == EventParams::CONNECTOR || type == EventParams::ENDOFTRACE)
				correctConnectorFront(ep);
		}
	}
}
//-------------------------------------------------------------------------------------------------
int InitialAnalysis::processDeadZone(ArrList& splashes, int maxDist)
{
	int i;
	const double DY1 = 3.0; // кандидаты на м.з. - в пределах -3 дБ от абс. макс
	const double DY2 = 2.0; // м.з. длится до лок. максимума, если нет спада более 2 дБ
	// ищем абс. макс.
	double vAbsMax = data[0];
	for (i = 0; i <= lastPoint; i++)
		if (vAbsMax < data[i])
			vAbsMax = data[i];
	// ищем начало м.з.
	for (i = 0; i <= lastPoint; i++)
		if (data[i] > vAbsMax - DY1)
			break;
	// i - начало м.з. (!: м б > lastPoint)
	// ищем первый макс. в м.з.
	for (; i <= lastPoint - 1; i++)
		if (data[i + 1] < data[i])
			break;
	if (i > lastPoint)
		return 0; // м.з. не найдена - XXX: как обрабатывать?

	/* Теперь надо определить точку, заведомо соответствующую м.з., и, по возможности, поправее.
	 * Когда-то был реализован способ (1), но, получив 24/11/2005 от Стаса моделированную кривую,
	 * в которой полка м.з. была наклонная, а ширина м.з. была около 2.5 ширин импульса,
	 * оказалось, что такой способ не годится, т.к. sureMax оказывалась в самом начале м.з.,
	 * а конец м.з. уже не замечался этим алгоритмом.
	 * Тогда был реализован способ (2), который просто искал спад на DY2 после первого макс. в м.з..
	 * Оказалось, что по testDB он ничуть не хуже (отличие - только на rg313, где оба варианта
	 * были одинаково плохи - и то, судя по всему, по внешним причинам), но зато
	 * правильно отрабатывает р/г от Стаса.
	 * Р/г добавлена в testDB как rg0401.
	 */
	/*
	// способ 1. ищем абс. макс. до спада на DY2
	int dzMaxBeg = i;
	int dzMaxEnd = i;
	for (; i <= lastPoint; i++) {
		if (data[i] < data[dzMaxBeg] - DY2)
			break;
		if (data[i] > data[dzMaxBeg]) {
			dzMaxBeg = dzMaxEnd = i;
		} else if (data[i] == data[dzMaxBeg]) {
			if (dzMaxEnd == i - 1)
				dzMaxEnd++;
		}
	}
	int sureMax = dzMaxEnd;
	/*/
	// способ 2. ищем точку спада на DY2
	int dzMaxBeg = i;
	for (; i <= lastPoint; i++) {
		if (data[i] < data[dzMaxBeg] - DY2)
			break;
	}
	int sureMax = i; // XXX
	//*/
	// ищем все спады правее sureMax, отстоящие друг от друга не более чем на maxDist
	int pos = sureMax;
	for (i = 0; i < splashes.getLength(); i++) {
		Splash *sp = (Splash*)splashes[i];
		if (sp->begin_thr < sureMax) {			// пропускаем все всплески, начало которых левее sureMax
			if (sp->sign < 0 && sp->end_thr > pos)	// ... но при этом для спадов отслеживаем их конец
				pos = sp->end_thr;
			continue;
		}
		if (sp->sign > 0 || sp->begin_thr > pos + maxDist) // останавливаемся на подъеме или лин. участке больше maxDist правее sureMax
			break;
		pos = sp->end_thr;
	}
    { EventParams *ep = new EventParams;
      ep->type = EventParams::DEADZONE;
      ep->begin = 0; ep->end = pos;
      if(ep->end > lastPoint){ ep->end = lastPoint;}
      events->add(ep);
    }
	return i;
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::setSpliceParamsBySplash(EventParams& ep, Splash& sp)
{   if(sp.sign>0) { ep.type = EventParams::GAIN; }
    else 		  { ep.type = EventParams::LOSS; }

    ep.begin = sp.begin_thr; // sp.begin_weld; - thr - в расчёте на то, что потом уточним
    ep.end = sp.end_thr; // sp.end_weld+1;
    if(ep.end>lastPoint){ep.end = lastPoint;}
    if(ep.begin<0){ep.begin=0;}

	ep.R = sp.r_weld;

	ep.spliceSplash = &sp;

#ifdef SPLICE_CAN_BE_EOT
	ep.can_be_endoftrace = ep.R > 3 && (fabs(sp.f_extr) > minimalEnd || ep.end >= lastPoint - 1); // XXX: very rough way
#endif
}
//------------------------------------------------------------------------------------------------------------
// Проводим разномасштабный авейвлет-анализ для уточнения положения сварок
// ф-я ПОРТИТ TEMP, так как использует его для хранения образа на другом масштабе
// Уточнение может сужать сварки и расширять в заданных на входе пределах
void InitialAnalysis::correctSpliceCoords(double* TEMP, int scale0, double tilt, EventParams* splice, int minBegin, int maxEnd)
{
	double* f_wlet = TEMP;
	EventParams& ev = *splice;
	// если это не сварка, то выход
    if( !(ev.type == EventParams::GAIN || ev.type == EventParams::LOSS) )
return;
	//prf_b("correctSpliceCoords: enter");
	const double level_factor = 0.15; // уровень от максимума сигнала , при ктором считаем, что сигнал есть
	const double noise_factor = 0.5;  // 0 , если мы не учитываем шум в пределах событий
    const double angle_factor = 1.5; // расширения светового конуса для защиты от низкочастотных помех на больших масштабах
	const double factor = 1.2; // множитель геометрической прогрессии
	const int nscale = 20; // количество разных масштабов
	int width = scale0; // frame-width: ширина окна (относительно границы события), в котором мы проводим дополнительный анализ
	int w_l = ev.begin, w_r = ev.end; // w_l - wavelet_left: границы вейвлет-образа на текущем масштабе

	const int sign = ev.spliceSplash->sign;

	// для первого масштаба не ограничиваемся "световым конусом", т.к. начальные w_l, w_r не соответствуют нашему здешнему критерию
    //int left_cross = (int)(w_l+width*angle_factor), right_cross = (int)(w_r-width*angle_factor); // точки пересечения световым конусом оси ОХ (по сути эквивалентно запоминанию масштаба, при котором это произошло, потому что точка X, где вейвлет пересёк порог, запоминается отдельно)
    int left_cross  = (int) ceil(w_r+width*angle_factor);
	int	right_cross = (int)floor(w_l-width*angle_factor);
    int left_cr2    = (int)floor(w_l-width*angle_factor);
	int	right_cr2   = (int) ceil(w_r+width*angle_factor);

	int i;
#ifdef debug_lines
    int coln=-1,color[]={0xFFFFFF,0x0000FF,0x00FF00,0xFF7733};
    int csz = sizeof(color)/sizeof(int);
#endif
    bool w_lr_ch = false; // цветом !!! при дебаге !!! выделяются участки только на тех масштабах, на которых границы изменились

	assert(w_r <= lastPoint);

    // анализируем при разныех масштабах
	for(int step=0; step<=nscale; step++, width = (int)(width/factor))
    {   //width = (int)(width/factor);//(int )(wlet_width/pow(factor,step) +0.5);// чтобы не накапливать ошибки
    	if(width<=1)
    break;
	    double wn = getWLetNorma(width);
		//assert(w_r <= lastPoint);
		int minL = imax((int) ceil(left_cr2  + width * angle_factor), minBegin);
		int maxR = imin((int)floor(right_cr2 - width * angle_factor), maxEnd);
		//printf("minBegin %d maxEnd %d   w_l %d w_r %d   minL %d maxR %d\n",
		//	minBegin, maxEnd, w_l, w_r, minL, maxR); fflush(stdout);
		performTransformationAndCenter(data, minL, maxR + 1, f_wlet, width, wn, tilt);
		// сначала ищём положение экстремума при данном масштабе
        int i_max = w_l;
        double f_max = f_wlet[i_max];
        for(i=w_l; i<w_r; i++) // saa: <=w_r ?
        {	if( f_wlet[i] * sign > f_max * sign ) // поскольку образ в пределах одного события знакопостояный, то можем работать с модулями
        	{	i_max = i; f_max = f_wlet[i_max];
            }
        }
        // считаем добавку к шуму ( степень немонотонности от пересечения порога до максимума )
		double f_lmax = f_wlet[w_l], df_left = 0, df_right = 0;// df - степень отклонения на немонотонных образах (после пересечения порога ф-я мжет колебаться, вот сепень этого колебания нас и интересует )
        for(i=w_l; i<i_max; i++) // добавки слева от пересечения порога до максимума
        { if( fabs(f_wlet[i])>fabs(f_lmax) ) { f_lmax = f_wlet[i];}// новый максимум  отклонения
          if( df_left<fabs(f_lmax-f_wlet[i]) ) { df_left=fabs(f_lmax-f_wlet[i]);} // новый максимальный уровень падения
        }
		f_lmax = f_wlet[w_r];
        for(i=w_r; i>i_max; i--) // добавки справа от пересечения порога до максимума
        { if(fabs(f_wlet[i])>fabs(f_lmax)) { f_lmax = f_wlet[i];}// новый максимум отклонения
          if(df_right<fabs(f_lmax-f_wlet[i])){ df_right=fabs(f_lmax-f_wlet[i]);} // новый максимальный уровень падения
        }
		const int BUGGY_SHIFT = 0; // было 1; но так границы события могут расширяться и выйти за пределы массива и сломать систему
		// ищем пересечение слева, пытаясь сдвинуть границу влево ( то есть пока i+width<=left_cross )
		// XXX: но только если на этом масштабе сигнал/шум достаточно велик
		if (level_factor * f_max * sign > df_left * 2)
		{
			if(f_wlet[w_l] * sign >= level_factor * f_max * sign)
			{
				//fprintf(stdout, "LLa(%d) %d lim %d\n", width, w_l, minL);
				// расширяем границы
				for(i = w_l - 1; i >= minL; i--)
				{
					if(f_wlet[i] * sign < level_factor * f_max * sign)
	        		{
						w_l=i + 1;
            			w_lr_ch = true;
				break;
					}
				}
				//fprintf(stdout, "LLb(%d) %d\n", width, i);
			}
			else
			{
				//fprintf(stdout, "LRa(%d) %d lim %g\n", width, w_l, left_cross-width*angle_factor);
				// сужаем границы
				for(i=w_l; i<i_max && i<=left_cross-width*angle_factor; i++)
				{
					if(f_wlet[i] * sign >= level_factor * f_max * sign)
	        		{
						w_l=i-BUGGY_SHIFT;//w_l=i;
            			w_lr_ch = true;
				break;
					}
				}
				//fprintf(stdout, "LRb(%d) %d\n", width, i);
			}
			if(w_l+width*angle_factor<left_cross){ left_cross = (int)ceil(w_l+width*angle_factor);}
			if(w_l-width*angle_factor>left_cr2)  { left_cr2  = (int)floor(w_l-width*angle_factor);}
        }
   		// ищем пересечение справа
		// XXX: но только если на этом масштабе сигнал/шум достаточно велик
		if (level_factor * f_max * sign > df_right * 2)
		{
			if(f_wlet[w_r] * sign >= level_factor * f_max * sign)
			{
				//fprintf(stdout, "RRa(%d) %d lim %d\n", width, w_r, maxR);
				// расширяем
				for (i = w_r + 1; i <= maxR; i++)
				{
					if(f_wlet[i] * sign < level_factor * f_max * sign)
					{
				break;
					}
				}
				w_r = i - 1;
				w_lr_ch = true;
				//fprintf(stdout, "RRb(%d) %d\n", width, i);
			}
			else
			{
				//fprintf(stdout, "RLa(%d) %d lim %g\n", width, w_r, right_cross+width*angle_factor);
				// сужаем
				for(i=w_r; i>i_max && i>=right_cross+width*angle_factor; i--)
				{
					if(f_wlet[i] * sign >= level_factor * f_max * sign)
        			{
				break;
					}
				}
				w_r=i+BUGGY_SHIFT;//w_r=i;
				w_lr_ch = true;
				//fprintf(stdout, "RLb(%d) %d\n", width, i);
			}
			if(w_r-width*angle_factor>right_cross){ right_cross = (int)floor(w_r-width*angle_factor);}
			if(w_r+width*angle_factor<right_cr2)  { right_cr2    = (int)ceil(w_r+width*angle_factor);}
		}
#ifdef debug_lines //рисуем вейвлет образы для данного масштаба
		{ coln++;
          for(int i=ev.begin; i<ev.end; i++)
          { double x1=i, y1=f_wlet[i], x2=i+1, y2=f_wlet[i+1];
            xs[cou]=x1*delta_x; ys[cou]=y1; xe[cou]=x2*delta_x; ye[cou]=y2;
            col[cou]=color[coln%csz];
            if(i<w_l || i>w_r || !w_lr_ch) col[cou] = 0x888888;
            cou++;
          }
        }
#endif
		w_lr_ch = false;
    }  // for(int step=0; step<=nscale;...
	//prf_b("correctSpliceCoords: scales done");

	//printf("correctSpliceCoords: scale %d..%d begin %d end %d minBegin %d maxEnd %d -- begin %d end %d\n",
	//	scale0, width, ev.begin, ev.end, minBegin, maxEnd, w_l, w_r);

	ev.begin = w_l;
	ev.end = w_r;
	
	//if( w_l < w_r  )
    //{   double old_left = ev.begin;
    //    double old_right = ev.end;
	//	// можем только сужать события
    //	if(w_l>old_left && w_l<old_right ) { ev.begin = w_l;}
    //	if(w_r<old_right && w_r>old_left)  { ev.end = w_r;}
    //}
/*
#ifdef debug_VCL
    double wn = getWLetNorma(wlet_width);
  	performTransformationAndCenter(data, 0, lastPoint + 1, f_wlet, wlet_width, wn, tilt);
#endif
*/
	//prf_b("correctSpliceCoords: return");
}
// -------------------------------------------------------------------------------------------------
// Посмотреть, есть ли что-то похожее на коннектор , если начать с i-го всплеска, и если есть - обработать и
// создать, изменив значение i и вернув сдвиг; если ничего не нашли, то возвращает 0.
// Сейчас как коннектор опознаем комбинацию конн.вверх + первый конн.вниз,
// а также конн.вверх + последний свар.вниз.
// Поиск ведется в пределах макс. протяженности, зависящей от ампл. конн.вверх,
// и ограничивается любым (>=weld) всплеском вверх.
int InitialAnalysis::findConnector(int i, ArrList& splashes, EventParams *&ep)
{
	int ret = 0;
    Splash* sp1 = (Splash*)splashes[i]; // начальный всплеск
	Splash* sp2 = 0; // конечный всплеск; sp2 == 0 тогда и только тогда, когда ret == 0
	double l12 = 0; // расстояниие между всплесками - должно быть не больше RSBig
	if (sp1->begin_conn == -1 || sp1->sign < 0)
		return ret;
	double distCrit = fabs(sp1->f_extr) > rACrit ? rSBig : rSSmall;

	int splLen = splashes.getLength();
	for (int j = i + 1; j <= splLen; j++) {
		// обрабатываем как реальные всплески, так и виртуальный всплеск вниз в конце волокна
		Splash *tmp1 = j < splLen ? (Splash*)splashes[j] : 0;
		int tmp_begin = tmp1 ? tmp1->begin_thr : lastPoint;
		int tmp_sign = tmp1 ? tmp1->sign : -1;
		bool tmp_hasConnAmpl = tmp1 ? tmp1->begin_conn != -1 : true;
		bool tmp_hasWeldAmpl = tmp1 ? tmp1->begin_weld != -1 : true;

		double ltmp = fabs(tmp_begin - sp1->end_thr);
		if (ltmp > distCrit) // достигли макс. протяжености
	break;
		if (tmp_sign > 0) { // подъем
			if (tmp_hasConnAmpl)
	break; // подъем конн. и выше - это уже не наш коннектор
			else
	continue; // подъем меньше конн. - игнорируем; XXX: вообще говоря, нужнее всего игнорировать фиктивные подъемы, возникающие из-за centerWletImage, а эту в-ну лучше рассчитывать из наклона и ширины в/л, а не брать такую грубую оценку как эта
		}
		// спад в пределах макс. протяженности
		if (tmp_hasConnAmpl) {
			// коннекторной амплитуды
			ret = j - i + 1;
			sp2 = tmp1;
			l12 = ltmp;
	break; // на нем и останавливаемся
		} else if (tmp_hasWeldAmpl) {
			// сварочной амплитуды - кандидат на спад
			ret = j - i + 1;
			sp2 = tmp1;
			l12 = ltmp;
	continue; // не останавливаемся, продолжаем поиск
		}
		// всплески меньшие чем weld, игнорируем
	}
	if (ret == 0)
		return ret;
	ep = new EventParams;
	setConnectorParamsBySplashes((EventParams&)*ep, (Splash&)*sp1, sp2, l12 ); // sp2 may be null
	//correctConnectorFront(ep); // уточняем фронт коннекора
#ifdef debug_lines
	double begin = ep->begin, end = ep->end;
	xs[cou] = begin*delta_x; xe[cou] = end*delta_x; ys[cou] = minimalConnector*2*1.1;  ye[cou] = minimalConnector*2*1.5;  col[cou]=0x00FFFF; cou++;
#endif
	return ret;
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::setConnectorParamsBySplashes(EventParams& ep, Splash& sp1, Splash* sp2p, double l)
{   double r1s, r1b, r2, r3s, r3b, rmin;
    ep.type = EventParams::CONNECTOR;
    ep.begin = sp1.begin_thr;
    if(ep.begin<0){ep.begin=0;}
	
	if (sp2p) {
		ep.end = sp2p->end_thr;
		if(sp2p->begin_conn != -1 && sp2p->sign > 0) {// если это начало нового коннектора
			ep.end = sp2p->begin_thr;// если два коннектора рядом, то конец первого совпадает с началом следующего
		}
		if(ep.end>lastPoint) {
			ep.end = lastPoint;
		}
	} else {
		ep.end = lastPoint;
	}

	r1s = sp1.r_conn;
	r1b = sp1.r_acrit;
	r2  = sp1.r_weld;

    assert(l>=-1);// -1 может быть так как мы искуствнно расширяем на одну точку каждый всплеск (начало ДО уровня, а конец ПОСЛЕ )
	int av_scale = sp2p ? (sp1.scale + sp2p->scale) / 2 : sp1.scale; // используем средний масштаб для определения R3-параметров. XXX: возможно, надо использовать максимальный либо начальный
    r3s = r2*(rSSmall - l)/av_scale;
    r3b = r2*(rSBig - l)/av_scale;
    // может ли этот "коннектор" быть концом волокна?
    if(sp1.sign>0 && sp1.f_extr>= minimalEnd) //знак и амп. всплеска достаточная?
    {
		// ищем абс. макс. на участке ep
		double vMax = data[ep.begin];
		int i;
		for(i = ep.begin; i <= ep.end; i++) {
			if (vMax < data[i])
				vMax = data[i];
		}

		if (vMax > minimalEotLevel) { // абс. уровень достаточен?
			ep.can_be_endoftrace = true;
		}
    }
	double t1 = r1s<r3b ? r1s:r3b, t2 = r3s>r1b ? r3s:r1b;
    double t3 = r2<t2 ? r2:t2;
	rmin = t1<t3 ? t1:t3;
    ep.R = rmin;

	ep.spliceSplash = 0;
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::correctConnectorFront(EventParams* ev)
{	if( ev->type != EventParams::CONNECTOR )// пока не дойдём до коннектора
return;
    int i_begin = ev->begin, i_end = ev->end;
	// ищем максимум
    int i_max = i_begin;// номер макс точки
    double f_max = data[i_max];
    int i;
    for( i=i_begin; i<i_end; i++ )
    {	if(data[i]>f_max) {i_max = i; f_max = data[i];}
    }
    // ищем точку на фронте коннектора такую, что всё слква от неё - меньше, а справа - выше,
	// которая при этом не меньше, чем на 0.02*(max-minLeft) выше абс. мин. minLeft слева от нее

	// допускается, что искомая точка не обязательно должна быть привязана к сетке целых X,
	// в таком случае определяется X с округлением вверх, даже в том случае, если так округленное
	// X дает значение Y, не удовлетворяющее требованию "все справа от X выше Y".
	// Так сделано специально для улучшения работы на резких фронтах в зашумленных коннекторах.
	// Для этого используется переменная f_x (значение функции в дробной координате x)

    int i_x = -1; // x - искомая точка с привязкой к целочисленной сетке
	double f_x = 0; // (undefined if i-x < 0) значение в искомой точке (точка без привязки к целым X)
    double f_cmax = data[i_begin]; // текущий максимум (слева до тек. точки)
    double f_lmin = data[i_begin]; // текущий минимум (слева до тек. точки)
	for( i=i_begin; i<=i_max; i++ )
    {
		if (f_lmin > data[i])
			f_lmin = data[i];
		double thr = fmax(f_cmax, f_lmin + 0.05 * (f_max - f_lmin));
		//fprintf(stderr, "i_x %d f_x %g i %d data[i] %g f_cmax %g f_lmin %g f_max %g thr %g\n",
		//	i_x, f_x, i, data[i], f_cmax, f_lmin, f_max, thr);
		if(data[i] >= thr)
        {	f_cmax = data[i];
            if(i_x == -1)
            {	i_x = i;
				f_x = thr;
            }
        }
        if(	i_x!=-1 && data[i]<f_x )
        {	i_x = -1;
        }
    }
	//fprintf(stderr, "correctConnectorFront: i_begin %d i_end %d i_max %d i_x %d\n",
	//	i_begin, i_end, i_max, i_x); fflush(stderr);
    if( i_x==-1 )
    {	i_x = i_begin;
    }
    double ev_beg_old = ev->begin;
    ev->begin = i_x - 1;
}
// -------------------------------------------------------------------------------------------------
// объединяем маскированные всплески в неид. события
int InitialAnalysis::processMaskedToNonId(int i, ArrList& splashes)
{
	if (! ((Splash*)splashes[i])->masked)
		return 0;
	int j;
	for (j = i + 1; j < splashes.getLength(); j++) {
		if (! ((Splash*)splashes[j])->masked)
			break;
	}
	EventParams &ep = *new EventParams;
	// aMax = 0, т.к. звон не должен интерпретироваться как конец волокна
	setUnrecognizedParamsBySplashes(ep, *(Splash*)splashes[i], *(Splash*)splashes[j - 1], 0);
	events->add(&ep);
	return j - i;
}
// -------------------------------------------------------------------------------------------------
// к этому моменту уже известно, что перед нами не коннектор
int InitialAnalysis::processIfIsNonId(int i, ArrList& splashes)
{
	double mult = 1.0; // множитель для масштаба при определения дистанции связывания событий
	double amplMagn = 5.0; // множитель для определения, что всплеск не связан с неид. соб.,т.к. его ампл. много больше ампл. неид.
    Splash* cur = (Splash*)splashes[i];
	int countPlus = 0;
	int countMinus = 0;
	if (cur->sign > 0)
		countPlus++;
	else
		countMinus++;
	int lastPos = cur->end_thr + (int)(cur->scale * mult); // конец зоны связывания неид. соб.
	int eventEnd = cur->end_thr;
	int eventBegin = cur->begin_thr;
	double ampl = fabs(cur->f_extr); // текущая хар. ампл. неид. соб.
	int j;
	for(j = i + 1; j<splashes.getLength(); j++) {
		EventParams *ep;
		int conLen = findConnector(j, splashes, ep);
		if (conLen > 0) {
			delete ep;
			// распознав коннектор, никак его не испольуем, а только лишь прерываем связывание неид. события
			// XXX: в таком случае, создание коннектора будет запрошено дважды - здесь, и по возврату в findEventsBySplashes
	break;
		}
		cur = (Splash*)splashes[j];
		int begin2 = cur->begin_thr - (int)(cur->scale * mult);
		if (begin2 > lastPos)
	break; // это событие уже далеко от lastPos
		if (fabs(cur->f_extr) > ampl * amplMagn)
	break; // это событие больше по амплитуде, чем наше неид. соб.
		if (cur->sign > 0)
			countPlus++;
		else
			countMinus++;
		ampl = fmax(ampl,fabs(cur->f_extr));
		lastPos = cur->end_thr + (int)(cur->scale * mult);
		eventEnd = cur->end_thr;
	}
	if (countPlus + countMinus < 3)
		return 0; // не связываем события (маленький коннектор будет определен как неид. и без этого кода)

	// создаем неид. событие
	EventParams &ep = *new EventParams;
	setUnrecognizedParamsBySplashes(ep, eventBegin, eventEnd, ampl);
	events->add(&ep);
	return j - i;
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::setUnrecognizedParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2, double aMax )
{
	setUnrecognizedParamsBySplashes(ep, sp1.begin_thr, sp2.end_thr, aMax);
}
void InitialAnalysis::setUnrecognizedParamsBySplashes( EventParams& ep, int begin, int end, double aMax)
{  ep.type = EventParams::UNRECOGNIZED;
   ep.begin = begin;
   if(ep.begin<0){ep.begin=0;}
   ep.end = end;
   if(ep.end>lastPoint){ep.end = lastPoint;}
   ep.spliceSplash = 0;
#ifdef NONID_CAN_BE_EOT
   ep.can_be_endoftrace = aMax > minimalEnd;
#endif
}
// -------------------------------------------------------------------------------------------------
//
// ====== ФУНКЦИИ ПЯТОГО ЭТАПА АНАЛИЗА - ОБРАБОТКИ СОБЫТИЙ =======
//
//------------------------------------------------------------------------------------------------------------
// удалить все события после последнего отражательного и переименовать отражательное в "конец волокна"
void InitialAnalysis::processEndOfTrace(int softEotLength) {
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
	fprintf(stderr, "processEndOfTrace(%d)\n", softEotLength);
	fflush(stderr);
#endif
	int i;
	for(i = events->getLength() - 1; i > 0; i--) {
		EventParams* ev = (EventParams*)(*events)[i];
		if (ev->can_be_endoftrace) {
	break;
		}
#ifdef SEARCH_EOT_BY_WLET
		if (ev->begin < softEotLength) {
	break;
		}
#endif
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
		fprintf(stderr, "processEndOfTrace: removing event #=%d can_be_eot=%d begin=%d\n",
			i, (int)(ev->can_be_endoftrace), (int)(ev->begin));
		fflush(stderr);
#endif
		events->slowRemove(i);
	}
	if (i > 0) {
		((EventParams*)(*events)[i])->type = EventParams::ENDOFTRACE;
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
#ifdef NEW_SHORT_LINE_EXCLUSION
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::excludeShortLinesBetweenConnectorAndNonidentified(double* arr, int sz)
{       sz = sz>4 ? (int)((sz/4.)+0.5) : 1; // берём четверть ширины вейвлета
        if(events->getLength()<2)
return;
        for(int n1=0, n2, n3; n1<events->getLength(); n1++)
        {   // пока не дойдём до коннектора или неидентифицированного
        EventParams* ev1 = (EventParams*)(*events)[n1];
        if(ev1->type != EventParams::CONNECTOR && ev1->type != EventParams::UNRECOGNIZED)
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
        if(ev1->type == EventParams::CONNECTOR && ev3->type != EventParams::UNRECOGNIZED)
    continue;
        if(ev1->type == EventParams::UNRECOGNIZED && ev3->type != EventParams::CONNECTOR)
    continue;
        if(ev2->end - ev2->begin < sz)
        { ev1->end = ev3->begin;
          events->slowRemove(n2);
        }
    }//for
}
//------------------------------------------------------------------------------------------------------------
// удаляет короткий линейный участок (если он есть) после мёртвой зоны(double* arr, int sz)
void InitialAnalysis::excludeShortLineAfterDeadzone(double* arr, int sz){
        sz = (int)(sz/4.+0.5);
        if(sz<=1){sz = 1;}// так читать легче, чем "sz = sz>4 ? (int)((sz/4.)+0.5) : 1;"
        if(events->getLength()<2)
return;
        for(int n1=0, n2, n3; n1<events->getLength(); n1++){
            // пока не дойдём до мёртвой зоны
        EventParams* ev1 = (EventParams*)(*events)[n1];
        if(ev1->type != EventParams::DEADZONE)
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
        if(ev2->end - ev2->begin < sz)
        { ev1->end = ev3->begin;
          events->slowRemove(n2);
        }
    }//for
}
//------------------------------------------------------------------------------------------------------------
// XXX функция не была порверена - не нашлось такой рефлектограммы, чтоб перед самым концом был короткий линейный участок
void InitialAnalysis::excludeShortLineBeforeEnd(double* arr, int sz){
        sz = (int)(sz/4.+0.5);
        if(sz<=1){sz = 1;}
        if(events->getLength()<2)
return;
        for(int n1=0, n2, n3; n1<events->getLength(); n1++){
        EventParams* ev1 = (EventParams*)(*events)[n1];
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
        if(ev3->type != EventParams::ENDOFTRACE)
    continue;
        if(ev2->end - ev2->begin < sz)
        { ev1->end = ev3->begin;
          events->slowRemove(n2);
        }
    }//for
}
//------------------------------------------------------------------------------------------------------------
#endif // NEW_SHORT_LINE_EXCLUSION
//------------------------------------------------------------------------------------------------------------
// удалить небольшие линейные участки между двумя последовательными коннекорами, сдвинув конец первого коннектора
void InitialAnalysis::excludeShortLinesBetweenConnectors(double* arr, int sz)
{   sz = sz>4 ? (int)((sz/4)+0.5) : 1; // берём четверть ширины вейвлета
	if(events->getLength()<2)
return;
	for(int n1=0, n2, n3; n1<events->getLength(); n1++)
	{   // пока не дойдём до коннектора
        EventParams* ev1 = (EventParams*)(*events)[n1];
    	if(ev1->type != EventParams::CONNECTOR)
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
        if(ev3->type != EventParams::CONNECTOR)
    continue;
    	if(ev2->end - ev2->begin < sz)
        { ev1->end = ev3->begin;
          events->slowRemove(n2);
        }
    }//for
}
//------------------------------------------------------------------------------------------------------------
// удалить небольшие линейные участки , возникающие вследствие неточностей анализа между потерями пред коннекторами
// sz - масштаб вейвлета
void InitialAnalysis::excludeShortLinesBetweenLossAndConnectors(double* arr, int sz)
{   if(events->getLength()<2)
return;
    sz = sz>4 ? (int)((sz/4)+0.5) : 1; // берём четверть ширины вейвлета
	for(int n1=0, n2, n3; n1<events->getLength(); n1++)
	{   // пока не дойдём до падения
        EventParams* ev1 = (EventParams*)(*events)[n1];
    	if(ev1->type != EventParams::LOSS)
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
        if(ev3->type != EventParams::CONNECTOR)
    continue;
    	if(ev2->end - ev2->begin <= sz)
        { ev1->end = ev3->begin;
          events->slowRemove(n2);
        }
    }//for
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
        {   if( fabs(ev.begin - prevEnd) > 2)
        	{   bool const GAP_BETWEEN_EVENTS_NOT_TOO_LARGE = false;
            	assert(GAP_BETWEEN_EVENTS_NOT_TOO_LARGE);
            }
        	ev.begin = prevEnd;
        }
		if(ev.end <= ev.begin)
        { ev.end = ev.begin+1;
        }
		if(ev.end > lastPoint)
        { ev.end = lastPoint;
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
		assert(ev.end <= lastPoint);
		assert(ev.end >= ev.begin);
		assert(ev.end > ev.begin); // >, not just >=
		assert(ev.begin == prevEnd || i == 0); // XXX
//#else
/*/
//сюда понатыкать брекпоинтов
		if(!(ev.begin >= 0))
        { int o=0;}
		if(!(ev.end <= lastPoint))
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
// -------------------------------------------------------------------------------------------------
//
// ====== ОБЩИЕ И ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ АНАЛИЗА =======
//
// -------------------------------------------------------------------------------------------------
// чтобы не менять кучу кода, когда меняем алгоритм пересчёта порогов вынесли в отдельную юфункцию
double InitialAnalysis::calcThresh(double thres, double noise)
{	return thres>noise ? thres : noise;// = max(thres, noise)
	//return thres+noise;
}
// -------------------------------------------------------------------------------------------------
double InitialAnalysis::get_wlet_fabs(int s, int x)
{	return fabs( wavelet.f(s,x) );
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::fillNoiseArray(double *y, int data_length, int N, double Neff, double noiseFactor, double *outNoise)
{	findNoiseArray(y, outNoise, data_length, N); // external function from findNoise.cpp
	int i;
	for (i = 0; i < N; i++)
	{	// кто скажет, что sqrt(Neff) в цикле сильно влияет на быстродействие АМФИКОМа, того назову плохим словом.
		outNoise[i] *= noiseFactor * 3 / sqrt(Neff);
	}
}
//------------------------------------------------------------------------------------------------------------
// свёртка входных данных с исользованием уже готового вейвлета
// noise convergence of  0.5*fabs(wavelet)/||wavelet|| function
// wlet_width - ширина вейвлета, с которым производится свёртка ( равна scaleB )
void InitialAnalysis::WaveletDataConvolution(double *data, int dataLength, int wlet_width)
{	double* data_processed = new double[dataLength];
	UserWavelet fabs_wlet(wavelet.getMinScale(), get_wlet_fabs);
	fabs_wlet.transform( wlet_width, data, dataLength, 0, dataLength-1, data_processed, 2*fabs_wlet.normStep(wlet_width));// dataLength-1 - так как нужен инекс, а не количество 
	int i;
    for(i=0; i<dataLength; i++){
    data[i] = data_processed[i];
    }
    delete[] data_processed;
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
void InitialAnalysis::performTransformationAndCenter(double* data, int begin, int end, double* f_wlet, int scale, double norma, double tilt)
{	// transform
	performTransformationOnly(data, begin, end, f_wlet, scale, norma);
	centerWletImageOnly(f_wlet, scale, begin, end, norma, tilt);
#ifdef DEBUG_INITIAL_ANALYSIS
#if 0 // FIXME: slow debug code
	int i;
	char sbuf[64];
	sprintf(sbuf, "wt-%d-%d-%d", scale, begin, end);
	FILE *fp = fopen(sbuf, "w");
	if (fp) {
		for (i = begin; i < end; i++) {
			fprintf(fp, "%d %g\n", i, f_wlet[i]);
		}
		fclose(fp);
	}
#endif
#endif
}
//------------------------------------------------------------------------------------------------------------
double InitialAnalysis::tiltToBaseline(int scale, double norma1, double tilt) {
	return tilt * getWLetNorma2(scale) / norma1;
}
//------------------------------------------------------------------------------------------------------------
double InitialAnalysis::baselineToTilt(int scale, double norma1, double baseline) {
	return baseline / (getWLetNorma2(scale) / norma1);
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::centerWletImageOnly(double* f_wlet, int scale, int begin, int end, double norma1, double tilt)
{
	double f_wlet_avrg = tiltToBaseline(scale, norma1, tilt);
	for(int i=begin; i<end; i++)
    {	f_wlet[i] -= f_wlet_avrg;
    }
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performTransformationOnly(double* data, int begin, int end, double* f_wlet, int freq, double norma)
{	//int len = end - begin;
	assert(begin >= 0);
	assert(end <= lastPoint + 1);
#ifdef SEARCH_EOT_BY_WLET
	wavelet.transform(freq, data, data_length, begin, end - 1, f_wlet + begin, norma); // incl. end-1; excl. lastPoint+1
#else
	wavelet.transform(freq, data, lastPoint + 1, begin, end - 1, f_wlet + begin, norma); // incl. end-1; excl. lastPoint+1
#endif
}
//------------------------------------------------------------------------------------------------------------
int InitialAnalysis::getMinScale() {
	return wavelet.getMinScale();
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
void Splash::lowerRFactors(double rMax) {
	if (r_acrit > rMax)
		r_acrit = rMax;
	if (r_weld > rMax)
		r_weld = rMax;
	if (r_conn > rMax)
		r_conn = rMax;
}
void Splash::setMasked(bool masked) {
	this->masked = masked;
}
//------------------------------------------------------------------------------------------------------------
#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
void InitialAnalysis::dumpEventsToStderr()
{
	fprintf(stderr, "%d events:\n", events->getLength());
	int i;
	for (i = 0; i < events->getLength(); i++) {
		fprintf(stderr, "  event[%d]: type %d location %d - %d\n", i,
			((EventParams*)(*events)[i])->type,
			((EventParams*)(*events)[i])->begin,
			((EventParams*)(*events)[i])->end);
	}
	fflush(stderr);
}
#endif
