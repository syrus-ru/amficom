#include "Histogramm.h"
#include <assert.h>
//----------------------------------------------------------------------------------------------------
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
//----------------------------------------------------------------------------------------------------
Histogramm::~Histogramm()
{	delete histo;
}
//----------------------------------------------------------------------------------------------------
void Histogramm::init(double* data, int start, int end)
{	assert(start>=0 && end>=0);
	histo = new double[nBins];
    int i;
	for (i = 0; i < nBins; i++)
	{	histo[i] = 0;
    }
	double delta = (up_limit - down_limit) / (double)nBins;
	for (i = start; i < end; i++)
	{ 	int N = (int)((data[i] - down_limit) / delta );
		if (N >= 0 && N < nBins)// всё, что не оппало в указанный при создании гистограммы интервал, не учитывается
		{	histo[N]++;
        }
	}
}
//----------------------------------------------------------------------------------------------------
int Histogramm::getMaximumIndex()
{	int center = 0;
	double max = histo[0];
	for (int i = 1; i < nBins; i++)
		if (max < histo[i])
		{	max = histo[i];
			center = i;
		}
	return center;
}
//----------------------------------------------------------------------------------------------------
double Histogramm::getMaximumValue()
{   double delta = (up_limit - down_limit) / (double)nBins;
	int max_index = getMaximumIndex();
	return down_limit + max_index*delta;
}
//----------------------------------------------------------------------------------------------------
