// InitialAnalysis.h: interface for the InitialAnalysis class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
#define AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_

//#define USE_NEURAL_NETWORK

#include <stdio.h>
#include <list>
#include <algorithm>

#include "../Common/EventParams.h"

#include "debug.h"

#include "EPList2.h"

//typedef std::list<EventParams> EPLIST;

class InitialAnalysis  
{
public:
	
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
	virtual ~InitialAnalysis();

public:
	int getEventSize();
	EventParams **getEventParams();
	int getEventsCount();
	double getMeanAttenuation();

private:
	double *data;
	int     data_length;
	double  delta_x;

	double *transC;
	double *transW;
	double *noise;
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
	int evSizeC; // характерна€ ширина отражательного событи€
	int evSizeW; // характерна€ ширина неотражательного событи€
	int lastNonZeroPoint;
	double meanAttenuation; // среднее затухание рефлектограммы
	EPLIST epVector;

//Analysis functions;
private:
	void performAnalysis();

	void correctDataArray();
	int getLastPoint();

	void getNoise(double *noise, int freq);

	void performTransformation(double *y, int start, int end, double *trans, int freq, double norma);
	double shiftToZeroAttenuation(double *trans);
	void setNonZeroTransformation(double *trans, double threshold, int start, int end);

	void findConnectors(double *transC,	double *transW, int start, int end, EPLIST &vector);
	void excludeConnectors(EPLIST &vector, double *data, double *data_woc);

	void findWelds(double *trans, EPLIST &vector);
	void siewLinearParts();
	void correctWeldCoords();

	void excludeShortEvents(int linearLength, int weldLength, int connectorLength);

	void setEventParams();
	void correctEnd();

	//void calcEventSize(double level); // unused?
	void correctConnectorCoords(); // unused?
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

