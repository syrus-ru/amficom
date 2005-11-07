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

static	SineWavelet wavelet; // ������������ �������

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
	double* data,				//����� ��������������
	int data_length,			//����� �����
	double delta_x,				//���������� ����� ������� (�)
	double minimalThreshold,	//����������� ��������� �������
	double minimalWeld,			//����������� ��������� ���������������� �������
	double minimalConnector,	//����������� ��������� ������� �������������� �������
	double minimalEnd,			//����������� ��������� ��������� � ����� �������
	double min_eot_level,		//����������� ���. ������� ��������� � ����� �������
	double noiseFactor,			// ��������� ��� ������ ���� (����� 2.0)
	int nonReflectiveSize,		//����������� ����� ���������������� �������
	double rACrit,				// ����� "��������" ����������
	int rSSmall,				// ����. ����� ��� ���������� ����������
	int rSBig,					// ����. ����� ��� �������� ����������
	double scaleFactor,			// ��������� ���������� ��������� (1.0 .. 2.0 ..)
	int lengthTillZero,			//����������� ������� ����� ( ==0 -> ����� �����)
	double *externalNoise)		//����������� ������� ��� ( ==0 -> ���� ����)
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
	this->minimalThreshold		= minimalThreshold;
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
	// ���� ������ � ������� ���� �� ����� �����, ���� ������������ IA �� ������ ��� ������, ������� ��� ����
	if (externalNoise == 0 || lengthTillZero <= 0)
	{	prf_b("IA: noise");
		// ��������� ������� ����
		{ const int sz = lastPoint + 1;
		  //fillNoiseArray(data, data_length, sz, 1 + width/20, noise);
		  fillNoiseArray(data, data_length, sz, 1.0, noiseFactor, noise);
		}
	}
	else
	{	int i;
		for (i = 0; i < lastPoint; i++)
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
// ����� �������������� �������
// XXX: ����������� - �� thr ��� �� weld ������?
// note: null-������ ���������� (��� �������� ��� ����������� �������� �� ������ ������ � ������)
// ��������� ��������� � ������ �����������������
int InitialAnalysis::splashesOverlap(Splash &spl1, Splash &spl2) {
	// ���� �������� ������ ����� - ��������� �������� ���������
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
void InitialAnalysis::performAnalysis(double *TEMP, int scaleB, double scaleFactor)
{	// ======= ������ ���� ������� - ���������� =======
	{	// ��������� �������-�������������� �� ��������� ��������, ���������� ������, ������� �������-�����
		// f_wletB - �������-����� �������, scaleB - ������ ��������, wn - ����� ��������
		double wn = getWLetNorma(scaleB);
		performTransformationOnly(data, 0, lastPoint + 1, TEMP, scaleB, wn);
		calcAverageFactor(TEMP, scaleB, wn);
		centerWletImageOnly(TEMP, scaleB, 0, lastPoint + 1, wn);// �������� �� ������������� ��������������(��) ���������� ������������
	}
#ifdef debug_VCL
	{	int i;
		for (i = 0; i <= lastPoint; i++)
			debug_f_wlet[i] = TEMP[i];
	}
#endif

	// ������������ ������ �� ������ �������� ������� � ���������� �������� ��������
    shiftThresholds(scaleB);// �������� ������

#ifdef DEBUG_INITIAL_ANALYSIS
	{	// FIXME: debug dump
		FILE *f = fopen ("noise2.tmp", "w");
		if (f) {
			int i;
			for	(i = 0; i <= lastPoint; i++)
				fprintf(f,"%d %g %g %g\n", i, data[i], TEMP[i], noise[i]);
			fclose(f);
		}
	}
#endif
	// ======= ������ ���� ������� - ����������� ��������� =======
	ArrList accSpl; // ������� ������ ��������� ������ (������)
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
		// �������� ����� ��������� �� ������ ��������
		ArrList newSpl;
		performTransformationAndCenter(data, 0, lastPoint + 1, TEMP, scale, getWLetNorma(scale));
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
					fprintf(f,"%d %g %g %g\n", i, data[i], TEMP[i], noise[i]);
				fclose(f);
			}
		}
	}
#endif
		findAllWletSplashes(TEMP, scale, newSpl);
		//fprintf(stderr, "scale %d: %d splashes\n", scale, newSpl.getLength()); fflush(stderr);
		// �����������, ��� ������  � ������ ��������� ���������
		int i;
		for (i = 0; i < newSpl.getLength(); i++) {
			Splash *cnSplash = (Splash*)newSpl[i];
			// ����, � ������ ���������� accSpl ������������ ������� cnSplash
			int minAccIndex = findMinOverlappingSplashIndex(*cnSplash, accSpl);
			int maxAccIndex = findMaxOverlappingSplashIndex(*cnSplash, accSpl);
			enum {
				ACTION_IGNORE  = 1,
				ACTION_INSERT  = 2,
				ACTION_REPLACE = 3
			} action = ACTION_IGNORE;
			int replaceIndex = -1;
			if (minAccIndex < 0) { // ����� �������
				action = ACTION_INSERT;
			} else if (minAccIndex < maxAccIndex) { // ���������� ��������� ��������� acc
				action = ACTION_IGNORE;
			} else { // ��������� ����� ���� ������� acc
				Splash *caSplash = (Splash*)accSpl[minAccIndex];
				int minBackIndex = findMinOverlappingSplashIndex(*caSplash, newSpl);
				int maxBackIndex = findMaxOverlappingSplashIndex(*caSplash, newSpl);
				if (maxBackIndex > minBackIndex) { // ��������������� ������� ���������� ��� �����-��, ����� ������
					action = ACTION_IGNORE;
				} else { // ����� �������-����������
					assert(minBackIndex >= 0); // Vit: -1 ���� � �������� ������� �� � ��� �� �����������  
                    const double exponent = 0.75; // ���������� ������� ��� ������� ������ ������������ ��������; ����� � (0;1)
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
			/*if (cnSplash->begin_thr < 2840 && cnSplash->end_thr > 2840) {
				fprintf(stderr, "HIT2840: begin %d end %d f_extr %g ai %d-%d action %d\n",
					cnSplash->begin_thr,
					cnSplash->end_thr,
					cnSplash->f_extr,
					minAccIndex,
					maxAccIndex,
					(int)action);

			}*/
			// NB: ACTION_REPLACE � ACTION_INSERT ������������ ������ ���
			// ���������, �� �������������� �� � ������ ���������� accSpl
			// � ��� ���������, �������������� �������-���������� � �����
			// accSpl-���������.
			// ��� �����������, ��� �������� �������� �� newSpl (���
			// ��� ����������� � accSpl) �� �������� �� ��������� ������
			// ��������� ����� �� newSpl.

			if (action == ACTION_REPLACE) {
				// ������� splash � ������ �� ��� ����� ���������
				delete (Splash*)accSpl[replaceIndex];
				accSpl.set(replaceIndex, cnSplash);
				// '�������' splash �� newSpl ������, ����� �� ������� ��� ������
				newSpl.set(i, 0);
			}
            else if (action == ACTION_INSERT) {
				// ���� ����� ����� �������
				int j;
				for (j = 0; j < accSpl.getLength(); j++) {
					if (((Splash*)accSpl[j])->begin_thr > cnSplash->begin_thr)
						break;
				}
				// j - ����� �������
				accSpl.slowInsert(j, cnSplash);
				// '�������' splash �� newSpl ������, ����� �� ������� ��� ������
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

	// ====== ������ ���� - ���������� ��������� ======
	// ������� �������� ����� ������ ��� ������������
	//removedMaskedSplashes(accSpl);
	processMaskedSplashes(accSpl);

#ifdef DEBUG_INITIAL_ANALYSIS_STDERR
		{
			int i;
			fprintf(stderr, "accSpl acter removedMaskedSplashes: Total %d splices\n", accSpl.getLength());
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

	// ======= ��������� ���� ������� - ����������� ������� �� ��������� =======
	findEventsBySplashes(TEMP, accSpl, scaleB); // �� ���������� ��������� ���������� ������� (�� ���� - ������������� ���������)
	processEventsBeginsEnds(TEMP); // �������� ������� ������� (����� ������������ accSpl ����� ������ �� EventParams)
	// ���������� ArrList � ��� �������
	accSpl.disposeAll(); // ������� ������ ArrList

	// ====== ����� ���� ������� - ��������� ������� =======
#ifdef SEARCH_EOT_BY_WLET
	int scaleEOT = scaleB * 10;
	wavelet.transform(scaleEOT, data, data_length, 0, data_length - 1, TEMP + 0, getWLetNorma(scaleEOT));

	int eotByFall = -1;
	{
		int i;
		for (i = 0; i < lastPoint + scaleEOT && i < data_length; i++) {
			if (eotByFall < 0 || TEMP[i] < TEMP[eotByFall])
				eotByFall = i;
		}
		int tuneFactor = 6; // XXX: tune parameter, suits testDB when is between 3 .. 80
		int iFrom = eotByFall - scaleEOT * tuneFactor;
		if (iFrom < 0) iFrom = 0;
		TEMP[iFrom] = 0; // just a visualization code
		bool changeSign = false;
		for (i = iFrom; i < eotByFall; i++)
			if (TEMP[i] > 0)
				changeSign = true;
		if (changeSign)
			eotByFall -= scaleEOT;
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
			fprintf(f, "%d %g %g\n", i, data[i], TEMP[i]);
		fclose(f);
	}// */
#endif

    processEndOfTrace(eotByFall);// ���� �� ������ ���������� �� ����� �������, �� �������� ��� ������� ������ softEotLength
#else
	processEndOfTrace(0);
#endif
    addLinearPartsBetweenEvents();
    excludeShortLinesBetweenConnectors(data, scaleB);
    excludeShortLinesBetweenLossAndConnectors(data, scaleB); // scaleB  - ������� ��������
	trimAllEvents(); // ��������� �� ������������ ��������� �� ���� ����� ����� � ������ �������, �� ��� ����� ��������� ���� �� ����� �� ���� ����� - ��� ���������, �� �� �� ������������ ��� ������� � ����������� ������ ��������� � ��� 
	verifyResults(); // ��������� ������
#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(logf, "performAnalysis: done\n");
	fflush(logf);
#endif
}
// -------------------------------------------------------------------------------------------------
//
// ======= ������� ������� ����� ������� - ���������� =======
//
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::calcAverageFactor(double* fw, int scale, double norma1)
{	double f_wlet_avrg = calcWletMeanValue(fw, lastPoint, -0.5, 0, 500);
	average_factor = f_wlet_avrg * norma1 / getWLetNorma2(scale);
}
//------------------------------------------------------------------------------------------------------------
// ��������� ������� �������� �������-������
double InitialAnalysis::calcWletMeanValue(double *fw, int lastPoint, double from, double to, int columns)
{   // ��������� ��������� ��������� � �������� [0; -0.5] ��
	Histogramm* histo = new Histogramm(from, to, columns);
	histo->init(fw, 0, lastPoint + 1);
	double mean_att = histo->getMaximumValue();
	delete histo;
	return mean_att;
}
//------------------------------------------------------------------------------------------------------------
// �������� ������ � ������������ � ������������ ���� � �������� ������������� ��������
void InitialAnalysis::shiftThresholds(int scale)
{   double f_wlet_avrg = average_factor * getWLetNorma2(scale) / getWLetNorma(scale); // ������� ������
	double thres_factor = 0.2;// ������� ������� ������ ������� �� ����� �������
    minimalThreshold += fabs(f_wlet_avrg)*thres_factor;
    if(minimalThreshold > 0.9*minimalWeld)
    {  minimalThreshold = 0.9*minimalWeld;
    }
	//minimalWeld += fabs(f_wlet_avrg)*thres_factor;
}
// -------------------------------------------------------------------------------------------------
//
// ======= ������� ������� ����� ������� - ����������� ��������� =======
//
// -------------------------------------------------------------------------------------------------
// �����: �������, ��� minimalThreshold < minimalWeld < minimalConnector
void InitialAnalysis::findAllWletSplashes(double* f_wlet, int wlet_width, ArrList& splashes)
{   //minimalThreshold,	//����������� ������� �������
	//minimalWeld,		//����������� ������� ���������������� �������
	//minimalConnector,	//����������� ������� �������������� �������
	double minimal_threshold_noise_factor = 0.4;  // XXX - ���� �� ��� ������� ��������
    for(int i=1; i <= lastPoint-1; i++)// 1 �.�. i-1 // ���� (1)
    {	if (fabs(f_wlet[i]) < calcThresh(minimalThreshold,noise[i]*minimal_threshold_noise_factor))
	continue;
		int bt = i - 1;
		int bw = -1;
		int ew = -1;
		int bc = -1;
		int ec = -1;
		double f_extr = f_wlet[i];
		int sign = xsign(f_wlet[i]);
		for (; i <= lastPoint-1; i++) { // ���� (2)
			if (f_wlet[i] * sign < 0)	// ����� �����
		break;
			if (fabs(f_wlet[i]) < calcThresh(minimalThreshold,noise[i]*minimal_threshold_noise_factor)) // ���� ������ minTh
		break;
			if (fabs(f_wlet[i]) >= calcThresh(minimalWeld, noise[i])) {
				ew = i + 1;
				if (bw == -1)
					bw = i - 1;
			}
			if (fabs(f_wlet[i]) >= calcThresh(minimalConnector, noise[i])) {
				ec = i + 1;
				if (bc == -1)
					bc = i - 1;
			}
			if (fabs(f_wlet[i]) > fabs(f_extr))
				f_extr = f_wlet[i];
		}
		int et = i; // �� ���� ����� ������� ��� ���������� �, ��������, ������� ���������
		assert(et > bt + 1); // ������ ��������, ����� ���� (2) �� ���������� �� ����. ��� ����� ����� ����� �������� FPU // FIXME: debug only
		if (et > bt + 1) // �� �� ������ ������ ���������, ����� �������� ������������ (� ���� ������ i-- ������ �� ����)
			i--; // ������ ����� ����� ��������� ������� ���������� ��������, �.�. � ����� (1) ���� ����-�������� i++
		if (bw == -1)
	continue; // Weld-����� ��� � �� ��������� - ����� ������� �� ������� (XXX: � ������� �������, ��������, ��� ������������ ��� ������������� ����� ������� �������� ���������)
		Splash& spl = (Splash&)(*(new Splash(wlet_width)));
        spl.f_extr = f_extr;
		spl.sign = sign;
        spl.begin_thr = bt;
		spl.end_thr = et;
		spl.begin_weld = bw;
		spl.end_weld = ew;
		spl.begin_conn = bc;
		spl.end_conn = ec;

		fillSplashRParameters(spl, f_wlet, wlet_width);
        if( spl.begin_thr < spl.end_thr // begin>end ������ ���� ����� ��� � �� ������ �� ���� ������� �����
	        && spl.begin_weld != -1 // !!!  ��������� ������ ������������ �������� ( ���� ��� �������� ������, �� ������������� ����������� ���� ��������, ��� ��� ���� ����� ����� ������������� ���������� ����� �������� ��������� ���������� ������� �����, �� ���������� ��������� �� ����� ! )
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
        {  delete &spl; // ���� ����� �� ������, �� ����� ������ ������, ��� ��� �������� ������ �� spl, �������  ���� ��������� � splashes 
        }
	}
    if(splashes.getLength() == 0)
return;
	// ���������� ��������� ��������� ������� ���� ��� ��� �� �� ������� ����� �� ���� � ����� ������� ����� �� ������ ���� ���� ���������� � ����� �� ����� ���������
	/*
	Splash* splend = (Splash*)splashes[splashes.getLength()-1];
    if(splend->sign > 0)
    {   Splash* spl = new Splash(wlet_width);
    	spl->begin_thr 		= lastPoint+1; spl->begin_weld 	= lastPoint+1; spl->begin_conn 	= lastPoint+1;
        spl->end_thr 		= lastPoint+2; spl->end_weld 	= lastPoint+2; spl->end_conn 	= lastPoint+2;
		//spl->f_extr			= 0;
		spl->sign			= -1;
		// fillSplashRParameters() �� ��������, �.�. ��� ����������, �� � �� � ����
        splashes.add(spl);
    }
	*/
#ifdef debug_lines
    // ���������� ������
    xs[cou] = 0; ys[cou] =  minimalThreshold; xe[cou] = lastPoint*delta_x; ye[cou] =  minimalThreshold; col[cou] = 0x004444; cou++;
    xs[cou] = 0; ys[cou] = -minimalThreshold; xe[cou] = lastPoint*delta_x; ye[cou] = -minimalThreshold; col[cou] = 0x004444; cou++;
    xs[cou] = 0; ys[cou] =  minimalWeld; 	  xe[cou] = lastPoint*delta_x; ye[cou] =  minimalWeld;      col[cou] = 0x009999; cou++;
    xs[cou] = 0; ys[cou] = -minimalWeld; 	  xe[cou] = lastPoint*delta_x; ye[cou] = -minimalWeld;	   col[cou] = 0x009999; cou++;
    xs[cou] = 0; ys[cou] =  minimalConnector; xe[cou] = lastPoint*delta_x; ye[cou] =  minimalConnector; col[cou] = 0x00FFFF; cou++;
    xs[cou] = 0; ys[cou] = -minimalConnector; xe[cou] = lastPoint*delta_x; ye[cou] = -minimalConnector; col[cou] = 0x00FFFF; cou++;
#endif
}
// -------------------------------------------------------------------------------------------------
//
// ======= ������� �������� ����� ������� - ����������� ������� �� ��������� =======
//
// -------------------------------------------------------------------------------------------------
/*void InitialAnalysis::removedMaskedSplashes(ArrList &accSpl)
{
	int i;
	for (i = 1; i < accSpl.getLength(); i++) {
		if (((Splash*)accSpl[i - 1])->sign > 0)
			continue; // ���������� �������
		double A0 = fabs(((Splash*)accSpl[i - 1])->f_extr);
		double A1 = fabs(((Splash*)accSpl[i])->f_extr);
		if (((Splash*)accSpl[i - 1])->end_conn < 0)
			continue; // ���� - �� ������ ������������� ������, ����������

		double dist = ((Splash*)accSpl[i])->begin_thr - ((Splash*)accSpl[i - 1])->end_conn;

		//fprintf(stderr, "i %d A0 %g A1 %g dist %g: ", i, A0, A1, dist);

		// @todo ������� ��� ��������� �������
		const double A_MAX = 15; // ��������� ��������� ��������� �����, ��
		const double ZVON_RATIO = 0.03; // ��������� ���. ��������� �����, ����; ���������� 0.01 .. 0.03 .. 0.1
		const double CRIT_DIST = 250.0; // ��������� ����� � e ���, �����

		if (A0 > A_MAX) A0 = A_MAX;

		double LH = pow(10.0, (A1 - A0) / 5.0);
		double RH = ZVON_RATIO * exp(-dist * delta_x / CRIT_DIST);
		if (LH < RH) {
			// ������� �������
			//fprintf(stderr, " removed");
			accSpl.slowRemove(i);
			i--;
		} else {
			// (��������� �������)
			// ������������ ������������� ��������
			double rMax = (LH - RH) / RH;
			((Splash*)accSpl[i])->lowerRFactors(rMax);
			//fprintf(stderr, " rMax = %g", rMax);
		}
		//fprintf(stderr, "\n");
	}
}*/

void InitialAnalysis::processMaskedSplashes(ArrList &accSpl) {
	// @todo ������� ��� ��������� �������
	const double A_MAX = 15; // ��������� ��������� ��������� �����, ��
	const double ZVON_RATIO = 0.03; // ��������� ���. ��������� �����, ����; ���������� 0.01 .. 0.03 .. 0.1
	const double CRIT_DIST = 250.0; // ��������� ����� � e ���, �����

	int j;
	for (j = 0; j < accSpl.getLength(); j++) {
		Splash *sL = (Splash*)accSpl[j];
		if (sL->sign > 0)
			continue; // ���������� �������
		if (sL->end_conn < 0)
			continue; // ���� - �� ������ ������������� ������, ����������
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
				// �������� ������� ��� �������������
				sR->setMasked(true);
				// ������������ ������������� �������� (XXX: ��� ��?)
				double rMax = -(LH - RH) / RH;
				sR->lowerRFactors(rMax);
			} else {
				// (�� �������� �������)
				// ������������ ������������� ��������
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
// ======= ������� ���������� ����� ������� - ����������� ������� �� ��������� =======
//
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::findEventsBySplashes(double *f_wletTEMP, ArrList& splashes, int dzMaxDist)
{//* ������ ���� ����  ���� �����
    if( splashes.getLength() <=2 ) // FIXME: ������� ���
return;
    int i0 = processDeadZone(splashes, dzMaxDist);// ���� ������ ����
	// ���� ��������� ����������  � ������
    for(int i = i0; i<splashes.getLength(); i++)
    {
	  EventParams *ep = 0;
	  int len;
  	  // ���� �� ������������� �������?
	  len = processMaskedToNonId(i, splashes);
	  if (len != 0) {
		  i += len - 1;
	continue;
	  }
	  len = findConnector(i, splashes, ep);
      if(len != 0)// ���� ��������� ��� ������
      { i+= len - 1;
	    events->add(ep);
    continue;
      }
	  len = processIfIsNonId(i, splashes);
	  if (len != 0) // ���� ����� ����. �������
      { i+= len - 1;
    continue;
      }
	  Splash* sp1 = (Splash*)splashes[i];
	  if (i + 1 < splashes.getLength()) {
		  Splash* sp2 = (Splash*)splashes[i+1];
		  int dist = abs(sp2->begin_weld - sp1->end_weld);
		  // ��� ������ "+" � "-" ����� ������
		  if( dist<rSSmall			// ���� �������� ����� ������
			  && (sp1->sign>0 && sp2->sign<0) // ������ �������������, � ������ - �������������
			  && ( sp1->begin_weld != -1 && sp2->begin_weld != -1)// � ��� ���� ��� ������� ���������
			)
		  {   EventParams *ep = new EventParams;
			  double v1 = fabs(sp1->f_extr);
			  double v2 = fabs(sp2->f_extr);
			  setUnrecognizedParamsBySplashes((EventParams&)*ep, (Splash&)*sp1, (Splash&)*sp2,
				  v1 > v2 ? v1 : v2);
			  events->add(ep);
			  i++;// ������ ��� ������� �� ���� ���������
	continue;
		  }
	  }
      // ������
      if( sp1->begin_weld!= -1 && fabs(sp1->end_weld-sp1->begin_weld)>1) //������
      {	EventParams *ep = new EventParams;
        setSpliceParamsBySplash(*ep, *sp1 );
        //correctSpliceCoords(f_wletTEMP, ep->spliceSplash->scale, ep);
        events->add(ep);
	continue;
      }
    }
}
//-----
void InitialAnalysis::processEventsBeginsEnds(double *f_wletTEMP)
{
	int i;
	int pass;
	for (pass = 0; pass < 2; pass++)
	for (i = 0; i < events->getLength(); i++)
	{
		EventParams *ep = (EventParams*) ((*events)[i]);

		// ���������� ������� ���������� ����������� ������ �������
		int minBegin = i > 0
			? ((EventParams*) ((*events)[i - 1]))->end - 1 // ��������� ���������� �� 1 �����
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
			if (type == EventParams::GAIN || type == EventParams::LOSS)
				correctSpliceCoords(f_wletTEMP, ep->spliceSplash->scale, ep, minBegin, maxEnd);
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
	const double DY1 = 3.0; // ��������� �� �.�. - � �������� -3 �� �� ���. ����
	const double DY2 = 2.0; // �.�. ������ �� ���. ���������, ���� ��� ����� ����� 2 ��
	// ���� ���. ����.
	double vAbsMax = data[0];
	for (i = 0; i <= lastPoint; i++)
		if (vAbsMax < data[i])
			vAbsMax = data[i];
	// ���� ������ �.�.
	for (i = 0; i <= lastPoint; i++)
		if (data[i] > vAbsMax - DY1)
			break;
	// i - ������ �.�. (!: � � > lastPoint)
	// ���� ������ ����. � �.�.
	for (; i <= lastPoint - 1; i++)
		if (data[i + 1] < data[i])
			break;
	if (i > lastPoint)
		return 0; // �.�. �� ������� - XXX: ��� ������������?
	// ���� ���. ����. �� ����� �� DY2
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
	// ���� ��� ����� ������ sureMax, ��������� ���� �� ����� �� ����� ��� �� maxDist
	int pos = sureMax;
	for (i = 0; i < splashes.getLength(); i++) {
		Splash *sp = (Splash*)splashes[i];
		if (sp->begin_thr < sureMax) {			// ���������� ��� ��������, ������ ������� ����� sureMax
			if (sp->sign < 0 && sp->end_thr > pos)	// ... �� ��� ���� ��� ������ ����������� �� �����
				pos = sp->end_thr;
			continue;
		}
		if (sp->sign > 0 || sp->begin_thr > pos + maxDist) // ��������������� �� ������� ��� ���. ������� ������ maxDist ������ sureMax
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

    ep.begin = sp.begin_thr; // sp.begin_weld; - thr - � ������� �� ��, ��� ����� �������
    ep.end = sp.end_thr; // sp.end_weld+1;
    if(ep.end>lastPoint){ep.end = lastPoint;}
    if(ep.begin<0){ep.begin=0;}

    //// remove this
	//double max = -1;
	//for(int i=sp.begin_weld; i<sp.end_weld; i++)
    //{ double res = (fabs(f_wlet[i])-minimalWeld)/noise[i];
    //  if(max<res){ max = res;}
    //}
    //ep.R = max;
	ep.R = sp.r_weld;

	ep.spliceSplash = &sp;

#ifdef SPLICE_CAN_BE_EOT
	ep.can_be_endoftrace = ep.R > 3 && (fabs(sp.f_extr) > minimalEnd || ep.end >= lastPoint - 1); // XXX: very rough way
#endif
}
//------------------------------------------------------------------------------------------------------------
// �������� ��������������� ��������-������ ��� ��������� ��������� ������
// �-� ������ f_wlet - ������� ����� !  (��� ��� ���������� ��� �� ������ ��� �������� ������ �� ������ ��������)
// ��������� ����� ������ ������ � ��������� � �������� �� ����� ��������
void InitialAnalysis::correctSpliceCoords(double* f_wlet /*TEMP space*/, int scale0, EventParams* splice, int minBegin, int maxEnd)
{
	EventParams& ev = *splice;
	// ���� ��� �� ������, �� �����
    if( !(ev.type == EventParams::GAIN || ev.type == EventParams::LOSS) )
return;
	//prf_b("correctSpliceCoords: enter");
	const double level_factor = 0.15; // ������� �� ��������� ������� , ��� ������ �������, ��� ������ ����
	const double noise_factor = 0.5;  // 0 , ���� �� �� ��������� ��� � �������� �������
    const double angle_factor = 1.5; // ���������� ��������� ������ ��� ������ �� �������������� ����� �� ������� ���������
	const double factor = 1.2; // ��������� �������������� ����������
	const int nscale = 20; // ���������� ������ ���������
	int width = scale0; // frame-width: ������ ���� (������������ ������� �������), � ������� �� �������� �������������� ������
	int w_l = ev.begin, w_r = ev.end; // w_l - wavelet_left: ������� �������-������ �� ������� ��������

	const int sign = ev.spliceSplash->sign;

	// ��� ������� �������� �� �������������� "�������� �������", �.�. ��������� w_l, w_r �� ������������� ������ �������� ��������
    //int left_cross = (int)(w_l+width*angle_factor), right_cross = (int)(w_r-width*angle_factor); // ����� ����������� �������� ������� ��� �� (�� ���� ������������ ����������� ��������, ��� ������� ��� ���������, ������ ��� ����� X, ��� ������� ������ �����, ������������ ��������)
    int left_cross  = (int) ceil(w_r+width*angle_factor);
	int	right_cross = (int)floor(w_l-width*angle_factor);
    int left_cr2    = (int)floor(w_l-width*angle_factor);
	int	right_cr2   = (int) ceil(w_r+width*angle_factor);

	int i;
#ifdef debug_lines
    int coln=-1,color[]={0xFFFFFF,0x0000FF,0x00FF00,0xFF7733};
    int csz = sizeof(color)/sizeof(int);
#endif
    bool w_lr_ch = false; // ������ !!! ��� ������ !!! ���������� ������� ������ �� ��� ���������, �� ������� ������� ����������

	assert(w_r <= lastPoint);

    // ����������� ��� ������� ���������
	for(int step=0; step<=nscale; step++, width = (int)(width/factor))
    {   //width = (int)(width/factor);//(int )(wlet_width/pow(factor,step) +0.5);// ����� �� ����������� ������
    	if(width<=1)
    break;
	    double wn = getWLetNorma(width);
		//assert(w_r <= lastPoint);
		//performTransformationAndCenter(data, w_l, w_r+1, f_wlet, width, wn);
		int minL = imax((int) ceil(left_cr2  + width * angle_factor), minBegin);
		int maxR = imin((int)floor(right_cr2 - width * angle_factor), maxEnd);
		//printf("minBegin %d maxEnd %d   w_l %d w_r %d   minL %d maxR %d\n",
		//	minBegin, maxEnd, w_l, w_r, minL, maxR); fflush(stdout);
		performTransformationAndCenter(data, minL, maxR + 1, f_wlet, width, wn);
		// ������� ���� ��������� ���������� ��� ������ ��������
        int i_max = w_l;
        double f_max = f_wlet[i_max];
        for(i=w_l; i<w_r; i++) // saa: <=w_r ?
        {	if( f_wlet[i] * sign > f_max * sign ) // ��������� ����� � �������� ������ ������� ��������������, �� ����� �������� � ��������
        	{	i_max = i; f_max = f_wlet[i_max];
            }
        }
        // ������� ������� � ���� ( ������� �������������� �� ����������� ������ �� ��������� )
		double f_lmax = f_wlet[w_l], df_left = 0, df_right = 0;// df - ������� ���������� �� ������������ ������� (����� ����������� ������ �-� ���� ����������, ��� ������ ����� ��������� ��� � ���������� )
        for(i=w_l; i<i_max; i++) // ������� ����� �� ����������� ������ �� ���������
        { if( fabs(f_wlet[i])>fabs(f_lmax) ) { f_lmax = f_wlet[i];}// ����� ��������  ����������
          if( df_left<fabs(f_lmax-f_wlet[i]) ) { df_left=fabs(f_lmax-f_wlet[i]);} // ����� ������������ ������� �������
        }
		f_lmax = f_wlet[w_r];
        for(i=w_r; i>i_max; i--) // ������� ������ �� ����������� ������ �� ���������
        { if(fabs(f_wlet[i])>fabs(f_lmax)) { f_lmax = f_wlet[i];}// ����� �������� ����������
          if(df_right<fabs(f_lmax-f_wlet[i])){ df_right=fabs(f_lmax-f_wlet[i]);} // ����� ������������ ������� �������
        }
		const int BUGGY_SHIFT = 0; // ���� 1; �� ��� ������� ������� ����� ����������� � ����� �� ������� ������� � ������� �������
		// ���� ����������� �����, ������� �������� ������� ����� ( �� ���� ���� i+width<=left_cross )
		// XXX: �� ������ ���� �� ���� �������� ������/��� ���������� �����
		if (level_factor * f_max * sign > df_left * 2)
		{
			if(f_wlet[w_l] * sign >= level_factor * f_max * sign)
			{
				//fprintf(stdout, "LLa(%d) %d lim %d\n", width, w_l, minL);
				// ��������� �������
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
				// ������ �������
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
   		// ���� ����������� ������
		// XXX: �� ������ ���� �� ���� �������� ������/��� ���������� �����
		if (level_factor * f_max * sign > df_right * 2)
		{
			if(f_wlet[w_r] * sign >= level_factor * f_max * sign)
			{
				//fprintf(stdout, "RRa(%d) %d lim %d\n", width, w_r, maxR);
				// ���������
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
				// ������
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
#ifdef debug_lines //������ ������� ������ ��� ������� ��������
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
	//	// ����� ������ ������ �������
    //	if(w_l>old_left && w_l<old_right ) { ev.begin = w_l;}
    //	if(w_r<old_right && w_r>old_left)  { ev.end = w_r;}
    //}
/*
#ifdef debug_VCL
    double wn = getWLetNorma(wlet_width);
  	performTransformationAndCenter(data, 0, lastPoint + 1, f_wlet, wlet_width, wn);
#endif
*/
	//prf_b("correctSpliceCoords: return");
}
// -------------------------------------------------------------------------------------------------
// ����������, ���� �� ���-�� ������� �� ��������� , ���� ������ � i-�� ��������, � ���� ���� - ���������� �
// �������, ������� �������� i � ������ �����; ���� ������ �� �����, �� ���������� 0.
// ������ ��� ��������� �������� ���������� ����.����� + ������ ����.����,
// � ����� ����.����� + ��������� ����.����.
// ����� ������� � �������� ����. �������������, ��������� �� ����. ����.�����,
// � �������������� ����� (>=weld) ��������� �����.
int InitialAnalysis::findConnector(int i, ArrList& splashes, EventParams *&ep)
{
	int ret = 0;
    Splash* sp1 = (Splash*)splashes[i]; // ��������� �������
	Splash* sp2 = 0; // �������� �������; sp2 == 0 ����� � ������ �����, ����� ret == 0
	double l12 = 0; // ����������� ����� ���������� - ������ ���� �� ������ RSBig
	if (sp1->begin_conn == -1 || sp1->sign < 0)
		return ret;
	double distCrit = fabs(sp1->f_extr) > rACrit ? rSBig : rSSmall;

	for (int j = i + 1; j < splashes.getLength(); j++) {
		Splash *tmp = (Splash*)splashes[j];
		double ltmp = fabs(tmp->begin_thr - sp1->end_thr);
		if (ltmp > distCrit) // �������� ����. ������������
	break;
		if (tmp->sign > 0) { // ������
			if (tmp->begin_conn != -1)
	break; // ������ ����. � ���� - ��� ��� �� ��� ���������
			else
	continue; // ������ ������ ����. - ����������; XXX: ������ ������, ������ ����� ������������ ��������� �������, ����������� ��-�� centerWletImage, � ��� �-�� ����� ������������ �� ������� � ������ �/�, � �� ����� ����� ������ ������ ��� ���
		}
		// ���� � �������� ����. �������������
		if (tmp->begin_conn != -1) {
			// ������������ ���������
			ret = j - i + 1;
			sp2 = tmp;
			l12 = ltmp;
	break; // �� ��� � ���������������
		} else if (tmp->begin_weld != -1) {
			// ��������� ��������� - �������� �� ����
			ret = j - i + 1;
			sp2 = tmp;
			l12 = ltmp;
	continue; // �� ���������������, ���������� �����
		}
		// �������� ������� ��� weld, ����������
	}
	if (ret == 0)
		return ret;
	ep = new EventParams;
	setConnectorParamsBySplashes((EventParams&)*ep, (Splash&)*sp1, (Splash&)*sp2, l12 );
	//correctConnectorFront(ep); // �������� ����� ���������
#ifdef debug_lines
	double begin = ep->begin, end = ep->end;
	xs[cou] = begin*delta_x; xe[cou] = end*delta_x; ys[cou] = minimalConnector*2*1.1;  ye[cou] = minimalConnector*2*1.5;  col[cou]=0x00FFFF; cou++;
#endif
	return ret;
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::setConnectorParamsBySplashes(EventParams& ep, Splash& sp1, Splash& sp2, double l)
{   double r1s, r1b, r2, r3s, r3b, rmin;
    ep.type = EventParams::CONNECTOR;
    ep.begin = sp1.begin_thr;
    if(ep.begin<0){ep.begin=0;}
    ep.end = sp2.end_thr;
    if(sp2.begin_conn != -1 && sp2.sign > 0)// ���� ��� ������ ������ ����������
    { ep.end = sp2.begin_thr;// ���� ��� ���������� �����, �� ����� ������� ��������� � ������� ����������
    }
    if(ep.end>lastPoint)
    { ep.end = lastPoint;
    }
	//// remove this
    //double max1 = -1, max2 = -1, max3 = -1;
    //int i;
    //for(i=sp1.begin_conn ; i<sp1.end_conn; i++)
    //{ double res = (f_wlet[i]-minimalConnector)/noise[i]; // WONDER: ����� f_wlet - �� �������� ��������?
    //  if(max1<res) { max1 = res;}
    //  res = (f_wlet[i]-rACrit)/noise[i];
    //  if(max2<res) { max2 = res;}
    //  res = (f_wlet[i]-minimalWeld)/noise[i];
    //  if(max3<res) {max3 = res;}
    //}
    //r1s = max1;
    //r1b = max2;
    //r2  = max3;

	r1s = sp1.r_conn;
	r1b = sp1.r_acrit;
	r2  = sp1.r_weld;

    assert(l>=-1);// -1 ����� ���� ��� ��� �� ���������� ��������� �� ���� ����� ������ ������� (������ �� ������, � ����� ����� )
	int av_scale = (sp1.scale + sp2.scale) / 2; // ���������� ������� ������� ��� ����������� R3-����������. XXX: ��������, ���� ������������ ������������ ���� ���������
    r3s = r2*(rSSmall - l)/av_scale;
    r3b = r2*(rSBig - l)/av_scale;
    // ����� �� ���� "���������" ���� ������ �������?
    if(sp1.sign>0 && sp1.f_extr>= minimalEnd) //���� � ���. �������� �����������?
    {
		// ���� ���. ����. �� ������� ep
		double vMax = data[ep.begin];
		int i;
		for(i = ep.begin; i <= ep.end; i++) {
			if (vMax < data[i])
				vMax = data[i];
		}

		if (vMax > minimalEotLevel) { // ���. ������� ����������?
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
{	if( ev->type != EventParams::CONNECTOR )// ���� �� ����� �� ����������
return;
    int i_begin = ev->begin, i_end = ev->end;
	// ���� ��������
    int i_max = i_begin;// ����� ���� �����
    double f_max = data[i_max];
    int i;
    for( i=i_begin; i<i_end; i++ )
    {	if(data[i]>f_max) {i_max = i; f_max = data[i];}
    }
    // ���� ����� �� ������ ���������� �����, ��� �� ����� �� �� - ������, � ������ - ����,
	// ������� ��� ���� �� ������, ��� �� 0.02*(max-minLeft) ���� ���. ���. minLeft ����� �� ���

	// �����������, ��� ������� ����� �� ����������� ������ ���� ��������� � ����� ����� X,
	// � ����� ������ ������������ X � ����������� �����, ���� � ��� ������, ���� ��� �����������
	// X ���� �������� Y, �� ��������������� ���������� "��� ������ �� X ���� Y".
	// ��� ������� ���������� ��� ��������� ������ �� ������ ������� � ����������� �����������.
	// ��� ����� ������������ ���������� f_x (�������� ������� � ������� ���������� x)

    int i_x = -1; // x - ������� ����� � ��������� � ������������� �����
	double f_x = 0; // (undefined if i-x < 0) �������� � ������� ����� (����� ��� �������� � ����� X)
    double f_cmax = data[i_begin]; // ������� �������� (����� �� ���. �����)
    double f_lmin = data[i_begin]; // ������� ������� (����� �� ���. �����)
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
// ���������� ������������� �������� � ����. �������
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
	// aMax = 0, �.�. ���� �� ������ ������������������ ��� ����� �������
	setUnrecognizedParamsBySplashes(ep, *(Splash*)splashes[i], *(Splash*)splashes[j - 1], 0);
	events->add(&ep);
	return j - i;
}
// -------------------------------------------------------------------------------------------------
// � ����� ������� ��� ��������, ��� ����� ���� �� ���������
int InitialAnalysis::processIfIsNonId(int i, ArrList& splashes)
{
	double mult = 1.0; // ��������� ��� �������� ��� ����������� ��������� ���������� �������
	double amplMagn = 5.0; // ��������� ��� �����������, ��� ������� �� ������ � ����. ���.,�.�. ��� ����. ����� ������ ����. ����.
    Splash* cur = (Splash*)splashes[i];
	int countPlus = 0;
	int countMinus = 0;
	if (cur->sign > 0)
		countPlus++;
	else
		countMinus++;
	int lastPos = cur->end_thr + (int)(cur->scale * mult); // ����� ���� ���������� ����. ���.
	int eventEnd = cur->end_thr;
	int eventBegin = cur->begin_thr;
	double ampl = fabs(cur->f_extr); // ������� ���. ����. ����. ���.
	int j;
	for(j = i + 1; j<splashes.getLength(); j++) {
		EventParams *ep;
		int conLen = findConnector(j, splashes, ep);
		if (conLen > 0) {
			delete ep;
			// ��������� ���������, ����� ��� �� ���������, � ������ ���� ��������� ���������� ����. �������
			// XXX: � ����� ������, �������� ���������� ����� ��������� ������ - �����, � �� �������� � findEventsBySplashes
	break;
		}
		cur = (Splash*)splashes[j];
		int begin2 = cur->begin_thr - (int)(cur->scale * mult);
		if (begin2 > lastPos)
	break; // ��� ������� ��� ������ �� lastPos
		if (fabs(cur->f_extr) > ampl * amplMagn)
	break; // ��� ������� ������ �� ���������, ��� ���� ����. ���.
		if (cur->sign > 0)
			countPlus++;
		else
			countMinus++;
		ampl = fmax(ampl,fabs(cur->f_extr));
		lastPos = cur->end_thr + (int)(cur->scale * mult);
		eventEnd = cur->end_thr;
	}
	if (countPlus + countMinus < 3)
		return 0; // �� ��������� ������� (��������� ��������� ����� ��������� ��� ����. � ��� ����� ����)

	// ������� ����. �������
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
// ====== ������� ������ ����� ������� - ��������� ������� =======
//
//------------------------------------------------------------------------------------------------------------
// ������� ��� ������� ����� ���������� �������������� � ������������� ������������� � "����� �������"
void InitialAnalysis::processEndOfTrace(int softEotLength) {
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
		events->slowRemove(i);
	}
	if (i != 0) {
		((EventParams*)(*events)[i])->type = EventParams::ENDOFTRACE;
	}
}
//------------------------------------------------------------------------------------------------------------
// �����: ������������ ��� �������� ������� ��� ��� ������ ! (����� ����� ����������� ��������)
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
// ������� ��������� �������� ������� ����� ����� ����������������� �����������, ������� ����� ������� ����������
void InitialAnalysis::excludeShortLinesBetweenConnectors(double* arr, int sz)
{   sz = sz>4 ? (int)((sz/4)+0.5) : 1; // ���� �������� ������ ��������
	if(events->getLength()<2)
return;
	for(int n1=0, n2, n3; n1<events->getLength(); n1++)
	{   // ���� �� ����� �� ����������
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
// ������� ��������� �������� ������� , ����������� ���������� ����������� ������� ����� �������� ���� ������������
// sz - ������� ��������
void InitialAnalysis::excludeShortLinesBetweenLossAndConnectors(double* arr, int sz)
{   if(events->getLength()<2)
return;
    sz = sz>4 ? (int)((sz/4)+0.5) : 1; // ���� �������� ������ ��������
	for(int n1=0, n2, n3; n1<events->getLength(); n1++)
	{   // ���� �� ����� �� �������
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
// ��-�� ���������� ��������� ������� ���� ������� ��������� ���� �� �����, ����������� ��
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
//���� ���������� �����������
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
// ====== ����� � ��������������� ������� ������� =======
//
// -------------------------------------------------------------------------------------------------
// ����� �� ������ ���� ����, ����� ������ �������� ��������� ������� ������� � ��������� ��������
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
	{	// ��� ������, ��� sqrt(Neff) � ����� ������ ������ �� �������������� ��������, ���� ������ ������ ������.
		outNoise[i] *= noiseFactor * 3 / sqrt(Neff);
	}
}
//------------------------------------------------------------------------------------------------------------
// ������ ������� ������ � ������������� ��� �������� ��������
// noise convergence of  0.5*fabs(wavelet)/||wavelet|| function
// wlet_width - ������ ��������, � ������� ������������ ������ ( ����� scaleB )
void InitialAnalysis::WaveletDataConvolution(double *data, int dataLength, int wlet_width)
{	double* data_processed = new double[dataLength];
	UserWavelet fabs_wlet(wavelet.getMinScale(), get_wlet_fabs);
	fabs_wlet.transform( wlet_width, data, dataLength, 0, dataLength-1, data_processed, 2*fabs_wlet.normStep(wlet_width));// dataLength-1 - ��� ��� ����� �����, � �� ���������� 
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
	//if(lastPoint + 10 < data_length) lastPoint += 10; �� �������, ����� ��� ������ ���� 
    return lastPoint;
}
//------------------------------------------------------------------------------------------------------------
// f- �������� �-���,
// f_wlet - �������-�����
void InitialAnalysis::performTransformationAndCenter(double* data, int begin, int end, double* f_wlet, int scale, double norma)
{	// transform
	performTransformationOnly(data, begin, end, f_wlet, scale, norma);
	centerWletImageOnly(f_wlet, scale, begin, end, norma);
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
void InitialAnalysis::centerWletImageOnly(double* f_wlet, int scale, int begin, int end, double norma1)
{   // shift (calcAverageFactor must be performed by now!)
	double f_wlet_avrg = average_factor * getWLetNorma2(scale) / norma1;
	for(int i=begin; i<end; i++)
    {	f_wlet[i] -= f_wlet_avrg;
    }
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performTransformationOnly(double* data, int begin, int end, double* f_wlet, int freq, double norma)
{	//int len = end - begin;
	assert(begin >= 0);
	assert(end <= lastPoint + 1);
	wavelet.transform(freq, data, lastPoint + 1, begin, end - 1, f_wlet + begin, norma); // incl. end-1; excl. lastPoint+1
	//wavelet.transform(freq, data, data_length, begin, end - 1, f_wlet + begin, norma); // incl. end-1; excl. lastPoint+1
}
//------------------------------------------------------------------------------------------------------------
int InitialAnalysis::getMinScale() {
	return wavelet.getMinScale();
}
//------------------------------------------------------------------------------------------------------------
// �������, ��� ���������� ����� ������� _���������_
void InitialAnalysis::calc_rms_line(double* arr, int beg, int end, double& a, double& b)
{	if(beg > end) { int temp = end; end = beg; beg = temp;}
    if(beg == end || beg < 0 || end > data_length)
    { a=0; b=0;
return;
    }
    double e1=0, e2=0, e3=0, e4=0;
    int n = end-beg+1;

    for(int i = beg; i <= end; i++)
    {   e1 += (double)i*i;// (double) �� ������������ int
        e2 += i;
        e3 += i * arr[i];
        e4 += arr[i];
    }
    a = (n*e3 - e2*e4)/(n*e1 - e2*e2);
    b = (e3 - a*e1)/e2;

    a /= delta_x;
}
//------------------------------------------------------------------------------------------------------------
// ������ ���������� ����� ���������� ��������� ���������� splash (begin/end)
void InitialAnalysis::fillSplashRParameters(Splash &spl, double *f_wlet, int wlet_width)
{
	int sign = spl.sign;
	// ��� ������������ �������
	spl.r_acrit = spl.r_conn = spl.r_weld = -1;
    int i;
    for(i=spl.begin_conn; i<spl.end_conn; i++) // ���� begin_conn � end_conn �� ����������, �� ������ �� ������
    { double res;
	  res = (f_wlet[i]*sign-minimalConnector)/noise[i];
      if(spl.r_conn<res) { spl.r_conn = res;}
      res = (f_wlet[i]*sign-rACrit)/noise[i];
      if(spl.r_acrit<res) { spl.r_acrit = res;}
    }
	// ��� ��������� �������
    for(i=spl.begin_weld; i<spl.end_weld; i++) // ���� begin_conn � end_conn �� ����������, �� ������ �� ������
    { double res;
      res = (f_wlet[i]*sign-minimalWeld)/noise[i];
      if(spl.r_weld<res) {spl.r_weld = res;}
    }
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
