// Histogramm.cpp: implementation of the Histogramm class.
//
//////////////////////////////////////////////////////////////////////

#include "Histogramm.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Histogramm::Histogramm(double down_limit, double up_limit, int nBins)
{
	if (down_limit > up_limit)
	{
		double tmp = down_limit;
		down_limit = up_limit;
		up_limit = tmp;
	}

	this->up_limit = up_limit;
	this->down_limit = down_limit;
	this->nBins	= nBins;
}

Histogramm::~Histogramm()
{
	delete histo;
}
 
void Histogramm::init(double* data, int data_length, int start, int end)
{
	histo = new double[nBins];

	for (int i = 0; i < nBins; i++)
		histo[i] = 0;

	double deriv_delta = (up_limit - down_limit) / (double)nBins;

	int N;
	for (i = max(0, start); i <= min (end, data_length-1); i++)
	{
		N = (int)((data[i] - down_limit) / deriv_delta + 0.5);
		if (N >= 0 && N < nBins)
			histo[N]++;
	}
}

int Histogramm::getMaximumIndex()
{
	int center = 0;
	double max = histo[0];
	for (int i = 1; i < nBins; i++)
		if (max < histo[i])
		{
			max = histo[i];
			center = i;
		}
	return center;
}

double Histogramm::getMaximumValue()
{
	double deriv_delta = (up_limit - down_limit) / (double)nBins;
	int max_index = getMaximumIndex();
	return down_limit + (max_index + .5) * deriv_delta;
}
