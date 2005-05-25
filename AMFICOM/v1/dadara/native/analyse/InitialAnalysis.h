#if !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
#define AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_

#include <stdio.h>
#include "../common/EventParams.h"

#include "debug.h"

#include "../common/ArrList.h"

class Splash;
//---------------------------------------------------------------------------------------------------------------
class InitialAnalysis
{
private:
#ifdef DEBUG_INITIAL_ANALYSIS
	FILE *logf;
#endif

public:
#ifdef debug_lines
	static const int sz = 1000000;
	int cou;
    double xs[sz], xe[sz], ys[sz], ye[sz], col[sz];//использовалось для отладки при рисованити прямых
#endif
	InitialAnalysis(
		double *data,
        int data_length,
		double delta_x,
		double minimalThreshold,
		double minimalWeld,
		double minimalConnector,
		double minimalEnd,
		double noiseFactor,
		int nonReflectiveSize,
		double rACrit,	// порог "большого" коннектора
		int rSSmall,	// макс. длина для маленького коннектора
		int rSBig,		// макс. длина для большого коннектора
		int lengthTillZero = 0,
		double *externalNoise = 0); // null to find automatically

	~InitialAnalysis();

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
    double minimalEnd;
    double noiseFactor;

    int wlet_width; // базовая ширина вейвлета
    int reflectiveSize; // максимальная ширина коннектора
    double rACrit;  	// порог "большого" коннектора
	int rSSmall;		// макс. длина для маленького коннектора
	int rSBig;          // макс. длина для большого коннектора

	int lastPoint;
	double wletMeanValue; // среднее значение образа рефлектограммы

	void performAnalysis();
	int getLastPoint();

	// вычислить коэфф "a" и "b" прямой y=ax+b, минимизирующей RMS
    void calc_rms_line(double *arr, int beg, int end, double& a, double& b);// (c) Vit

	// заполнить массив шума (шум не постоянен, поэтому используем массив для описания значеня шума в каждой точке)
	void fillNoiseArray(double *y, int data_length, int N, double Neff, double NoiseFactor, double *outNoise);
	void getNoise(double *noise, int freq);
	double calcThresh(double thres, double noise); // чтобы не менять кучу кода, когда меняем алгоритм пересчёта порогов вынесли в отдельную юфункцию

	// подготовка среднего значения
	double calcWletMeanValue(double* fw, double from, double to, int columns);// вычислить самое популярное значение ф-ции fw
	void calcAverageFactor(double* fw, int scale, double norma1);

	// выполнение вейвлет-преобразования
	void performTransformationOnly(double *y, int begin, int end, double *trans, int freq, double norma);
	void performTransformationAndCenter(double *y, int begin, int end, double *trans, int freq, double norma);
	void centerWletImageOnly(double* f_wlet, int scale, int begin, int end, double norma1);

	// анализ
	void shiftThresholds();// изменить границы порогов в соответствии со средним значением вейвлета 
    void findAllWletSplashes(double* f_wlet, ArrList& splashes);
    void findEventsBySplashes(ArrList&  splashes);
	int	 processDeadZone(ArrList& splashes);
    int  processIfIsConnector(int i, ArrList& splashes);// посмотреть, есть ли что-то похожее на коннектор , если начать с i-го всплеска, и если есть - обработать и добавить, изменив значение i и вернув сдвиг; если ничего не нашли, то сдвиг равен 0
    void setSpliceParamsBySplash( EventParams& ep, Splash& sp1);
    void setConnectorParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2);
    void setUnrecognizedParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2);
    void processEndOfTrace();  // удалить все события после последнего отражательного и переименовать отражательное в "конец волокна"
    void addLinearPartsBetweenEvents();
    void correctAllConnectorsFronts(double *arr);
    void correctAllSpliceCoords(); // ф-я ПОРТИТ вейвлет образ !  (так как использует тот же массив для хранения образа на другом масштабе)
    void correctSpliceCoords(int n);
    void excludeShortLinesBetweenConnectors(double* data, int evSizeC);
    void trimAllEvents(); // из-за расширения всплесков события могу немного наползать друг на друга, выравниваем их
    void verifyResults();

// Wavelet constants;
private:
    double wn;// норма вейвлета (НОРМА специфическая ! См реализацию ! )
};
//====================================================================================================
class Splash
{ public:
	int begin_thr;		// первое пересечение минимального порога
    int end_thr;

	int begin_weld;		// первое пересечение сварочного порога
    int end_weld;


	int begin_conn;		// пересечение коннекторного порога
    int end_conn;

	double f_extr;		// экстремальное значение всплеска
    int sign;  // знак всплеска

	// инициализируем неопределёнными значениями ()
    Splash()
    {	begin_thr 		= -1;
    	end_thr 		= -1;
		begin_weld		= -1;
    	end_weld 		= -1;
		begin_conn 		= -1;
    	end_conn 		= -1;
        f_extr 			= 0;
    	sign 			= 0;
    }
};
//====================================================================================================


#endif // !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
