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
#ifdef debug_VCL
	double* debug_f_wlet; // вейвлет-образ для отладки
    double* type; // массив в котором прописаны типы уже распознанных точек
    double* f_tmp; //!!! массив для временного хранения обработанной рефлектограммы ( исользуется при отладке )
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
	double *noise;

    double average_factor; // характеризует ср. наклона на р/г, не привязан ни к масштабу вейвлета, ни к выбранной норме

    ArrList* events; // список всех событий

//Parameters of the analysis (criteria);
	double minimalThreshold;
	double minimalWeld;
	double minimalConnector;
    double minimalEnd;
    double noiseFactor;

    int reflectiveSize; // максимальная ширина коннектора
    double rACrit;  	// порог "большого" коннектора
	int rSSmall;		// макс. длина для маленького коннектора
	int rSBig;          // макс. длина для большого коннектора

	int lastPoint;
	double wletMeanValue; // среднее значение образа рефлектограммы

	void performAnalysis(int scaleB);
	int getLastPoint();

	// вычислить коэфф "a" и "b" прямой y=ax+b, минимизирующей RMS
    void calc_rms_line(double *arr, int beg, int end, double& a, double& b);// (c) Vit

	// работа с шумом
	static void fillNoiseArray(double *y, int data_length, int N, double Neff, double NoiseFactor, double *outNoise);
	static void getNoise(double *noise, int freq);
	static double calcThresh(double thres, double noise); // чтобы не менять кучу кода, когда меняем алгоритм пересчёта порогов вынесли в отдельную юфункцию

	// выполнение вейвлет-преобразования
	void performTransformationOnly(double *y, int begin, int end, double *trans, int freq, double norma);
	void performTransformationAndCenter(double *y, int begin, int end, double *trans, int freq, double norma);
	void centerWletImageOnly(double* f_wlet, int scale, int begin, int end, double norma1);

	// расчет R-параметров splash (в момент его обнаружения, т.к. потом будет потерян вейвлет-образ)
	void fillSplashRParameters(Splash &spl, double *f_wlet, int wlet_width);

	// ======= ПЕРВЫЙ ЭТАП АНАЛИЗА - ПОДГОТОВКА =======
	static double calcWletMeanValue(double* fw, int lastPoint, double from, double to, int columns);// вычислить самое популярное значение ф-ции fw
	void calcAverageFactor(double* fw, int scale, double norma1);
	void shiftThresholds(int scale);// изменить границы порогов в соответствии со средним значением вейвлета 

	// ======= ВТОРОЙ ЭТАП АНАЛИЗА - ОПРЕДЕЛЕНИЕ ВСПЛЕСКОВ =======
	void findAllWletSplashes(double* f_wlet, int wlet_width, ArrList& splashes);

	// ======= ТРЕТИЙ ЭТАП АНАЛИЗА - ОПРЕДЕЛЕНИЕ СОБЫТИЙ ПО ВСПЛЕСКАМ =======
    void findEventsBySplashes(double* f_wlet, int wlet_width, ArrList&  splashes);
	int	 processDeadZone(ArrList& splashes);
    int  processIfIsConnector(double* f_wlet, int wlet_width, int i, ArrList& splashes);// посмотреть, есть ли что-то похожее на коннектор , если начать с i-го всплеска, и если есть - обработать и добавить, изменив значение i и вернув сдвиг; если ничего не нашли, то сдвиг равен 0
    void setSpliceParamsBySplash(EventParams& ep, Splash& sp1);
    void setConnectorParamsBySplashes(int wlet_width, EventParams& ep, Splash& sp1, Splash& sp2);
    void setUnrecognizedParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2);
	void correctSpliceCoords(double *f_wletTMP, int wlet_width, EventParams* splice);// ф-я ПОРТИТ вейвлет образ !  (так как использует тот же массив для хранения образа на другом масштабе)
	void correctConnectorFront(EventParams* connector);

	// ====== ЧЕТВЕРТЫЙ ЭТАП АНАЛИЗА - ОБРАБОТКА СОБЫТИЙ =======
    void processEndOfTrace();  // удалить все события после последнего отражательного и переименовать отражательное в "конец волокна"
    void addLinearPartsBetweenEvents();
    void excludeShortLinesBetweenConnectors(double* data, int evSizeC);
    void trimAllEvents(); // из-за расширения всплесков события могу немного наползать друг на друга, выравниваем их
    void verifyResults();
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

	int scale;
	double r_conn;		// max{(f_wlet[i]-minimalConnector)/noise[i]} (<0, если порог не достигнут)
	double r_acrit;		// max{(f_wlet[i]-rACrit)/noise[i]} (<0, если rACrit не достигнут)
	double r_weld;		// max{(f_wlet[i]-minimalWeld)/noise[i]} (<0, если порог не достигнут)

	double f_extr;		// экстремальное значение всплеска
    int sign;  // знак всплеска

	// инициализируем неопределёнными значениями, указав только масштаб обнаружения
    Splash(int scale1)
    {	// NB: begin=end - это дает нам возможность не всегда проверять, определены они или нет
		begin_thr 		= -1;
    	end_thr 		= -1;
		begin_weld		= -1;
    	end_weld 		= -1;
		begin_conn 		= -1;
    	end_conn 		= -1;
        f_extr 			= 0;
    	sign 			= 0;
		scale			= scale1;
		r_conn			= -1;
		r_acrit			= -1;
		r_weld			= -1;
    }
};
//====================================================================================================
#endif // !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
