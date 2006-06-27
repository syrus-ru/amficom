// Neuron.h: interface for the Neuron class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_NEURON_H__47135107_1209_40EE_97E1_908AB7E80F37__INCLUDED_)
#define AFX_NEURON_H__47135107_1209_40EE_97E1_908AB7E80F37__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "Vector.h"
#include "Dispatcher.h"


class Neuron
{
public:
	Neuron();
	Neuron(int LAYER, int ADDRESS, Dispatcher *DISPATCHER);
	Neuron(int LAYER, int ADDRESS, int nENTERSIGRAL, Dispatcher *DISPATCHER);
	virtual ~Neuron();

public: //Variables definition;

	int layer;    // Location of the neuron in
	int address;  // the total net, layer>=1, address>=1
	int nEnterSinals; // Needed, if the neuron is in the first layer
    double neededExitValue; //Needed, if the neuron is in the last layer
	double error;
	double studyingCoeff;
	double inerciaCoeff;
    double *weights;
    double *deltaWeight;
    double shift;
    double deltaShift;

    double *activateSignals;
    double exitSignal;
    Vector *children;
    Vector *parents;
	Dispatcher *dispatcher;

public: //Functions definition;
	double *getWeights();
	void setWeights(double *WEIGHTS);
	double getShift();
	void setShift(double SHIFT);

	void addParent(Neuron *parent);
	void addChild(Neuron *child);
    void setRandomWeights(double downLimit, double upLimit);
    void setRandomShift(double downLimit, double upLimit);
    void setStudyingCoeff(double coeff);
    void setInerciaCoeff(double coeff);
	void setNeededExitValue(double NEEDEDEXITVALUE);
    void setActivateSignals(double *signals);
	void setActivateSignal(double signal, Neuron *parent);
    void calculateExitSignal();
	double getExitSignal();
	double activationFunction(double arg);
    void printWeights();
	void sendExitSignalToChildren();
	double getRecursiveErrorPart(Neuron *n); // To return recursive error
	void setError(double neededExitValue);
	void setError();
    void correctWeights();
	void operationPerformed(int commandName);



private:
	double rnd(double from, double to);

};



#endif // !defined(AFX_NEURON_H__47135107_1209_40EE_97E1_908AB7E80F37__INCLUDED_)



