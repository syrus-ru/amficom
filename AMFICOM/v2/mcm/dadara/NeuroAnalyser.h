// NeuroAnalyser.h: interface for the NeuroAnalyser class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_NEUROANALYSER_H__62BF66B3_3C0F_4257_A36A_C7DFA9AF9D69__INCLUDED_)
#define AFX_NEUROANALYSER_H__62BF66B3_3C0F_4257_A36A_C7DFA9AF9D69__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "NeuroNet.h"

class NeuroAnalyser : public NeuroNet  
{
public:
	NeuroAnalyser(int ENTERSIGNALDIMENSION);
	NeuroAnalyser(int ENTERSIGNALDIMENSION, int *NEURONS_PER_LAYER, int LAYERS);
	virtual ~NeuroAnalyser();


public:

	double couplingUp;
	double couplingDown;

	double couplingUp2;
	double couplingDown2;

	int connector;
	double connKey[2];

	int weldUp;
	double weldUpKey[2];

	int weldDown;
	double weldDownKey[2];

	int linear;
	double linearKey[2];

	int unrecognized;

public:
	int getEnterSignalDimension();
	int getEventType(double *data);
	int getEventType2(double *data);
};

void catchEndingChar(void  *key);

#endif // !defined(AFX_NEUROANALYSER_H__62BF66B3_3C0F_4257_A36A_C7DFA9AF9D69__INCLUDED_)
