// InitialAnalysis.h: interface for the InitialAnalysis class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
#define AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_

//#define USE_NEURAL_NETWORK

#include <stdio.h>
//#include <list>
#include <algorithm>

#include "../Common/EventParams.h"

#include "debug.h"

#include "EPList2.h"
//typedef std::list<EventParams> EPLIST;

//#define debug_lines // отрисока вспомогательных линий 
//---------------------------------------------------------------------------------------------------------------
class InitialAnalysis  
{
public:
#ifdef debug_lines
	static const int sz = 1000;
	int cou; double a[sz], b[sz], xs[sz], xe[sz], col[sz]; //!!! использовалось для отладки при рисованити прямых
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
		int nonReflectiveSize);
	~InitialAnalysis();
	int getEventSize();
	EventParams **getEventParams();
	int getEventsCount();
	double getMeanAttenuation();

// !!! private:
	double *data;
	int     data_length;
	double  delta_x;

	double *transC;
	double *transW;
    double* type; // массив в котором прописаны типы уже распознанных точек 
	double *noise;
    double *rnoise; // real noise  в отличие от не пойми-какого noise
	double *data_woc;
	EventParams **eps;
//Parameters of the analysis (criteria);
	double minimalThreshold;
	double minimalWeld;
	double minimalConnector;
	double minimalEndingSplash;
	double maximalNoise;
	int	   waveletType;
	double formFactor;
//Internal parameters;
	int evSizeC; // характерная ширина отражательного события
	int evSizeW; // характерная ширина неотражательного события
	int lastNonZeroPoint;
	double meanAttenuation; // среднее затухание рефлектограммы
	EPLIST epVector;
//Analysis functions;
	void performAnalysis();

	void correctDataArray();
	int getLastPoint();

	// вычислить коэфф "a" и "b" прямой y=ax+b, минимизирующей RMS
    void calc_rms_line(double *arr, int beg, int end, double& a, double& b);// (c) Vit 

	// заполнить массив шума (шум не постоянен, поэтому используем массив для описания значеня шума в каждой точке)
    void fillNoiseArray(const double *y, int N, int width, double Neff, double noiseLevel, double *outNoise);
	void getNoise(double *noise, int freq);

	void performTransformation(double *y, int start, int end, double *trans, int freq, double norma);
	double shiftToZeroAttenuation(double *trans);
	void setNonZeroTransformation(double *trans, double threshold, int start, int end);

    void setNonZeroTransformation_(double *trans, double threshold, int start, int end);

	void findConnectors(double *transC,	double *transW, int start, int end, EPLIST &vector);
	void excludeAllEvents(EPLIST &vector, double *data, double *data_woc);

	void findWelds(double *trans, EPLIST &vector);
	void sewLinearParts();
	void correctWeldCoords();

	void excludeShortEvents(int linearLength, int weldLength, int connectorLength);

	void setEventParams();
	void correctEnd();

	//void calcEventSize(double level); // unused?
	void correctConnectorCoords(); // unused?
    void correctAllConnectorsFronts(double *arr, EPLIST &evnts);// (c) Vit
    void excludeShortLinesBetweenConnectors(double* data, EPLIST &epVector, int evSizeC);// (c) Vit
	void excludeNonRecognizedEvents(); // unused -- NN

//Wavelet constants;
private:
	double wn_w;
	double wn_c;

//Helping functions;
private:
	void getLinearFittingCoefficients(double *data, int from, int to, int shift, double *res);
	void convolutionOfNoise(int n_points);

#ifdef USE_NEURAL_NETWORK
//Neural net functions and variables;
private:
	void setNNWindow(int from, int to, double *window, int networkWindowSize);
	void setNNWindow_(int from, int to, double *window, int networkWindowSize);
	void shiftEventToCentre(double *wnd, int wndSize);
#endif

#ifdef DEBUG_INITIAL_ANALYSIS
private:
	FILE* str;
#endif

};

#endif // !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
