// InitialAnalysis.h: interface for the InitialAnalysis class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
#define AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "EventParams.h"

class InitialAnalysis  
{
public:
	InitialAnalysis(double *data_, int data_length_,
					double delta_x_,
				  	double minimalThreshold_,
					double minimalWeld_,
					double minimalConnector_,
					double minimalEndingSplash_,
					double maximalNoise_,
					int waveletType_,
					double formFactor_, 
					int findEnd_);
	virtual ~InitialAnalysis();

public:
	double *sigmaSquared;
	double *noise;
	double *data;
	double *transformation;
	int    *eventsTable;
	EventParams **ep;
	int     data_length;
	double  delta_x;


//Parameters of the analysis (criteria);
	double minimalThreshold;
	double minimalWeld;
	double minimalConnector;
	double minimalEndingSplash;
	double maximalNoise;
	int	   waveletType;
	double formFactor;
	int    findEnd;

//Internal parameters;
	int eventSize;
	int lastNonZeroPoint;
	int numberOfFoundEvents;
	double meanAttenuation;

//Helping parameters;
	double linearFittingParameters[2];

//Constants;
	double pi;


//Analysis functions;
private:
	void performAnalysis();
	void correctDataArray();
	void setEventSize();
	void performTransformation();
	void setNoise();
	void shiftToZeroAttenuation();
	void setNonZeroTransformation();
	void setEventsTble();
	void excludeShortEvents(int eventLength);
	void siewLinearParts();
	void excludeNonRecognizedEvents();
	void correctConnectorsCoords();
	void setEventParams();
	void setCorrectEndingSplash();
	void setSigmaSquared();

public:
	int  *getEventsTable();
	

//Wavelet functions;
private:
	double   wLet(int arg);
	double  wLet1(int arg);
	double  wLet2(int arg);
	double  wLet3(int arg);
	double  wLet4(int arg);
	double  wLet5(int arg);
	double  wLet6(int arg);
	double  wLet7(int arg);
	double  wLet8(int arg);
	double  wLet9(int arg);
	double wLet10(int arg);
	
	void setNormalizingCoeffs();

//Wavelet constants;
private:
	double wn1;
	double wn2;
	double wn3;
	double wn4;
	double wn5;
	double wn6;
	double wn7;
	double wn8;
	double wn9;
	double wn10;

//Helping functions;
public:
	void getLinearFittingCoefficients(double *data_, int from, int to);
	void getLinearFittingCoefficientsFromBegin(double *data_, int from, int to, int begin);
	bool isPositive(double arg);
	bool isNegative(double arg);
	bool isNonZero(double arg);
	void smoothArray(double *array, int from, int to, int pointsToSmooth, int arrayLength);
	void convolutionOfNoise(int n_points);
	double maximum(double a, double b);
	double minimum(double a, double b);
	int sign(double arg);

//Neural net functions and variables;
private:
	void setNNWindow(int from, int to, double *window, int networkWindowSize);
	void setNNWindow_(int from, int to, double *window, int networkWindowSize);
	void shiftEventToCentre(double *wnd, int wndSize);
};

#endif // !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
