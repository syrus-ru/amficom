#if !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
#define AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_

#include <stdio.h>
#include "../Common/EventParams.h"

#include "debug.h"

//#include "EPList2.h"
#include "../Common/ArrList.h"

class Splash;
//---------------------------------------------------------------------------------------------------------------
class InitialAnalysis
{
public:
#ifdef debug_lines
	static const int sz = 1000000;
	int cou;
    double xs[sz], xe[sz], ys[sz], ye[sz], col[sz];//использовалось для отладки при рисованити прямых
#endif
	InitialAnalysis(
		double *data, int data_length,
		double delta_x,
		double minimalThreshold,
		double minimalWeld,
		double minimalConnector,
		double minimalEndingSplash,
		double maximalNoise,
		int waveletType,
		double formFactor,
		int reflectiveSize,
		int nonReflectiveSize,
		int lengthTillZero = 0,
		double *externalNoise = 0); // null to find automatically

	~InitialAnalysis();

	int getEventSize();
	int getEventsCount();
    ArrList& getEvents(){return *events;}
#ifndef debug_VCL
private:
#endif
	double *data;
	int     data_length;
	double  delta_x;

    double* type; // массив в котором прописаны типы уже распознанных точек
	double *noise;

    double* f_wlet;
    double average_factor; // не зависит ни от выбранного масштаба вейвлета, ни от выбранной нормы (а только от ср. наклона на р/г)
#ifdef debug_VCL
    double* f_tmp; //!!! массив для временного хранения обработанной рефлектограммы ( исользуется при отладке )
#endif
    ArrList* events; // список всех событий
//Parameters of the analysis (criteria);
	double minimalThreshold;
	double minimalWeld;
	double minimalConnector;
	double minimalEndingSplash;

    int wlet_width; // базовая ширина вейвлета
    int reflectiveSize;// максимальная ширина коннектора

	int lastNonZeroPoint;
	double wletMeanValue; // среднее значение образа рефлектограммы

	void performAnalysis();
	int getLastPoint();

	// вычислить коэфф "a" и "b" прямой y=ax+b, минимизирующей RMS
    void calc_rms_line(double *arr, int beg, int end, double& a, double& b);// (c) Vit

	// заполнить массив шума (шум не постоянен, поэтому используем массив для описания значеня шума в каждой точке)
	void fillNoiseArray(double *y, int data_length, int N, double Neff, double *outNoise);
	void getNoise(double *noise, int freq);

	// подготовка среднего значения
	double calcWletMeanValue(double* fw, double from, double to, int columns);// вычислить самое популярное значение ф-ции fw
	void calcAverageFactor(double* fw, int scale, double norma1);

	// выполнение вейвлет-преобразования
	void performTransformationOnly(double *y, int begin, int end, double *trans, int freq, double norma);
	void performTransformationAndCenter(double *y, int begin, int end, double *trans, int freq, double norma);
	void centerWletImageOnly(double* f_wlet, int scale, int begin, int end, double norma1);

	// анализ
    void findAllWletSplashes(double* f_wlet, ArrList& splashes);
    void findEventsBySplashes(ArrList&  splashes);
	void SetSpliceParamsBySplash( EventParams& ep, Splash& sp1);
    void SetConnectorParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2);
    void deleteAllEventsAfterLastConnector();
    void addLinearPartsBetweenEvents();
    void correctAllConnectorsFronts(double *arr);
    void correctAllSpliceCoords(); // ф-я ПОРТИТ вейвлет образ !  (так как использует тот же массив для хранения образа на другом масштабе)
    void correctSpliceCoords(int n);
    void excludeShortLinesBetweenConnectors(double* data, int evSizeC);
	void verifyResults();

// Wavelet constants;
private:
    double wn;// норма вейвлета (НОРМА специфическая ! См реализацию ! )
};
//====================================================================================================
class Splash
{ public:
 	int begin; 		// начало всплеска (что превысило минимальный порог)
	int end;        // конец всплеска (первое возвращение ниже порога)
   	int begin_nonoise;  // то же , что и begin, но без добавления к порогу шума
	int end_nonoise;
    int sign; 		// "+1" если всплеск верх, "-1" если всплеск вниз

	double f_extr; 	// значение в точке экстремума
    int x_extr; 	// положение точки эстремума
    double square;  // площадь под всплеском

    Splash(int begin, int end, int begin_nonoise, int end_nonoise, int sign, double f_extr, int x_extr, double square)
    {	this->begin 		= begin;
		this->end 			= end;
        this->begin_nonoise	= begin_nonoise;
		this->end_nonoise	= end_nonoise;
		this->sign 			= sign;
		this->f_extr 		= f_extr;
		this->x_extr 		= x_extr;
        this->square		= square;
    }
};
//====================================================================================================
#endif // !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
