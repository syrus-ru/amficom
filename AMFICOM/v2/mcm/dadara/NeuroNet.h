// NeuroNet.h: interface for the NeuroNet class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_NEURONET_H__2EDBB0C4_956E_4331_92BB_85652BA83C3C__INCLUDED_)
#define AFX_NEURONET_H__2EDBB0C4_956E_4331_92BB_85652BA83C3C__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "Vector.h"
#include "Dispatcher.h"
#include "Neuron.h"

class NeuroNet  
{
public:
	NeuroNet(int ENTERSIGNALDIMENSION);
	virtual ~NeuroNet();


public: // Variables definition
	Vector *layers;;
	Dispatcher *dispatcher;;
	int    enterSignalDimension;
	double *enterSignal;
	int    firstLayerDimension;
	int    lastLayerDimension;
	double *exitSignal;
	int    layersDimension[100];
	double *allWeights;
	double *allShifts;
	int allWeightsLength;
	int allShiftsLength;

	double teachingQuality;
	

public:
	void   addLayer(int neuronsNumber);
	void   buildNet();
	double *getExitSignal();
	double *getExitSignal(double *ENTERSIGNAL);
    void   setEnterSignal(double *ENTERSIGNAL);
	void   correctWeights();
	void   setNeededExitSignal(double *neededExitSignal);
	void   teachTheNetwork();
    double *getAllWeights();
    double *getAllShifts();
	void   writeWeightsAndShiftsInSingleFile(const char *fileName);
	bool   readWeightsAndShiftsFromSingleFile(const char* fileName);
	bool   setAllWeights(double *allWeights);
	bool   setAllShifts(double *allShifts);

	void setStudyingCoeff(double coeff);
	void setInerciaCoeff(double coeff);

};

#endif // !defined(AFX_NEURONET_H__2EDBB0C4_956E_4331_92BB_85652BA83C3C__INCLUDED_)
