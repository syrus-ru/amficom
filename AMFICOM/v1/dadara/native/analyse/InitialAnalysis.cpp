//----------------------------------------
#include "InitialAnalysis.h"
#include "../Common/MathRef.h"
#include "Histogramm.h"

#include <math.h>
#include <assert.h>
#include <stdlib.h>
#include "../An2/findLength.h"
#include "../An2/findNoise.h"

#include "../common/prf.h"

/*


  TODO (from saa to Vit, 2005-03-03)

95% ������� ������� �������� centerWletImage, ����������� � ����� �� ��������� � correctAllSpliceCoords
������� �� ��� ���-�� ���������. �� ��� ������, ���� 2 ��������:
  �) ���������� f_wlet_avrg ��� ���� ������������ ��������� ���� ���,
     � �� �� ���� �� ������ ������;
  �) ������������ f_wlet_avrg ��� ������� �������� �� ������ ���������� f_wlet_avrg.
     (��� ����� ���������� ������ ���������� �����. ����� ��������)

-----   -----   -----   ------  ----
count   ticks   %tick   %rdtsc  name
-----   -----   -----   ------  ----
   12       0    0.0%    0.00%   correctSpliceCoords: enter
  240       0    0.0%    0.13%   correctSpliceCoords: getWLetNorma
  240       0    0.0%    0.47%   correctSpliceCoords: performTransformation
  240      15    3.0%    9.01%   correctSpliceCoords: centerWletImage
  240       0    0.0%    0.06%   correctSpliceCoords: processing
   12       0    0.0%    0.00%   correctSpliceCoords: scales done
   12       0    0.0%    0.00%   correctSpliceCoords: return

*/

// ����������� ������: ��� �������� ���������� �� ������ 3 ����� ����, � ������������ ��� ������� � ������� �����������.
// �������������� ������������� �������� 1; ����������� ������������ 1 - 4.
// ������� �������� ������������ ������������� ������� ����������������.
const double THRESHOLD_TO_NOISE_RATIO = 2;
//------------------------------------------------------------------------------------------------------------
// Construction/Destruction
InitialAnalysis::InitialAnalysis(
	double* data,				//����� ��������������
	int data_length,			//����� �����
	double delta_x,				//���������� ����� ������� (�)
	double minimalThreshold,	//����������� ������� �������
	double minimalWeld,			//����������� ������� ���������������� �������
	double minimalConnector,	//����������� ������� �������������� �������
	double minimalEndingSplash,	//����������� ������� ���������� ���������
	double maximalNoise,		// <unused>
	int waveletType,			// <unused>
	double formFactor,			// <unused>
	int reflectiveSize,			//����������� ����� �������������� �������
	int nonReflectiveSize,		//����������� ����� ���������������� �������
	int lengthTillZero,			//����������� ������� ����� ( ==0 -> ����� �����)
	double *externalNoise)		//����������� ������� ��� ( ==0 -> ���� ����)
{
#ifdef debug_lines
    cou = 0;
    for(int i=0; i<sz; i++){col[i] = -1;}
#endif
	this->delta_x				= delta_x;
	this->minimalThreshold		= minimalThreshold;
	this->minimalWeld			= minimalWeld;
	this->minimalConnector		= minimalConnector;
	this->minimalEndingSplash	= minimalEndingSplash;
	this->data_length			= data_length;
	this->data					= data;
    this->reflectiveSize		= reflectiveSize;
    this->wlet_width			= nonReflectiveSize;

    events = new ArrList();

	if (lengthTillZero <= 0)
		lastNonZeroPoint = getLastPoint();
	else
		lastNonZeroPoint = lengthTillZero - 1;

	f_wlet	= new double[lastNonZeroPoint];
#ifdef debug_VCL
	f_tmp   = new double[lastNonZeroPoint];
#endif
	noise	= new double[lastNonZeroPoint];
	type	= new double[data_length];

	// ���� ������ � ������� ���� �� ����� �����,
	// ���� ������������ IA �� ������ ��� ������,
	// ������� ��� ����
	if (externalNoise == 0 || lengthTillZero <= 0)
	{
		prf_b("IA: noise");
		// ��������� ������� ����
		{ const int sz = lastNonZeroPoint;
		  const int width = wlet_width;
		  fillNoiseArray(data, data_length, sz, 1 + width/20, noise);
		}
	}
	else
	{
		int i;
		for (i = 0; i < lastNonZeroPoint; i++)
			noise[i] = externalNoise[i];
	}

	prf_b("IA: analyse");
	performAnalysis();
	prf_b("IA: done");
}
//------------------------------------------------------------------------------------------------------------
InitialAnalysis::~InitialAnalysis()
{	delete[] type;
	delete[] noise;
    delete[] f_wlet;

    events->disposeAll();

    delete events;
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performAnalysis()
{
	// ��������� �������-��������������
	// f_wlet - �������-����� �������, wlet_width - ������ ��������, wn - ����� ��������
    wn = getWLetNorma(wlet_width);
    performTransformationOnly(data, 0, lastNonZeroPoint, f_wlet, wlet_width, wn);
	calcAverageFactor(f_wlet, wlet_width);
	centerWletImageOnly(f_wlet, wlet_width, 0, lastNonZeroPoint);// �������� �� ������������� ��������������(��) ���������� ������������
    { // ���� ��� �������� �������-������
      ArrList splashes; // ������� ������ ArrList
      findAllWletSplashes(f_wlet, splashes); // ��������� ������ splashes ���������
      findEventsBySplashes(splashes); // �� ���������� ��������� ���������� ������� (�� ���� - ������������� ���������)
      // ���������� ArrList � ��� �������
      splashes.disposeAll(); // ������� ������ ArrList
    } // ������� ������ ������ splashes

    deleteAllEventsAfterLastConnector();// ���� �� ������ ���������� �� ����� �������, �� �������� ��� �������  
    correctAllConnectorsFronts(data);
    excludeShortLinesBetweenConnectors(data, wlet_width);
    addLinearPartsBetweenEvents();
    correctAllSpliceCoords();// ��������� ��������� ������� �������� �������, �� � ����� ������� �������� ������� ����� ��� ������������ ( ������� ����� ����� addLinearPartsBetweenEvents() )
}
//-------------------------------------------------------------------------------------------------
void InitialAnalysis::findEventsBySplashes(ArrList& splashes)
{   double mc = minimalConnector;
    double mw = minimalWeld;
    for(int i=0; i<splashes.getLength()-1; i++)
	{   Splash &sp1 = *(Splash*)splashes[i];
        Splash &sp2 = *(Splash*)splashes[i+1];
    	double dist = sp2.begin - sp1.end;
		// �������� , ��� ������ ����� ���������
        if( dist<reflectiveSize*2			// ���� �������� ������
        	&& (sp1.sign>0 && sp2.sign<0)   // ������ �������������, � ������ - �������������
            && ( (sp1.square/sp2.square>0.1 && sp1.square/sp2.square<2.0) || events->getLength()==0) // �������� ������ ���� �������� (������ ��������� ��� ������ ����)
			&& ( sp1.f_extr>mc+noise[sp1.x_extr] ) // ���� �� ���� ��������� ��������� ������  ��������� ����� ����������, �� ��� ���������
            //&& ( sp1.f_extr>mc+noise[sp1.x_extr] && -sp2.f_extr>mc+noise[sp2.x_extr] ) // ���� �� ���� ��������� ��������� ���� �� ���� ��������� ����� ����������, �� ��� ���������
          )
        {   EventParams *ep = new EventParams;
            ep->n = events->getLength();
            ep->type = EventParams::CONNECTOR;
            ep->begin = sp1.begin_nonoise;
            ep->end = sp2.end_nonoise;
            events->add(ep);
            i++;
        }
        else if( fabs(sp1.f_extr) > mw+noise[sp1.x_extr] )
        {	EventParams *ep = new EventParams;
	        ep->n = events->getLength();
            ep->type = EventParams::SPLICE;
            ep->begin = sp1.begin_nonoise-1;
            ep->end = sp1.end_nonoise+1;
            events->add(ep);
        }
    }
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::findAllWletSplashes(double* f_wlet, ArrList& splashes)
{   //minimalThreshold,	//����������� ������� �������
	//minimalWeld,		//����������� ������� ���������������� �������
	//minimalConnector,	//����������� ������� �������������� �������
    for(int i=0; i<lastNonZeroPoint; i++)
    {   if(fabs(f_wlet[i])<=minimalThreshold)
     continue;
        int begin_nonoise = i;// ������������, � ����� ����� �� ��������� ����� ������������
        int sign = f_wlet[i]>0 ? 1 : -1;
        int i_extr = i;
        double f_extr = f_wlet[i_extr];
        double area = 0;
        for(  ; i<lastNonZeroPoint; i++)
        {  	if( f_wlet[i]*sign <= minimalThreshold)// �� fabs, ��� ��� sign ��������� ����������� ����, ��� ������� ����� ����������� ����� �������
        break;
      		// ������� �������
            area += fabs(f_wlet[i]); // ��������� ����� ����� ������� ���������, �� ������� � ��������� �� ������������� ���������
            // ���� ���������
			if( f_wlet[i]*sign > f_extr*sign )
        	{	i_extr = i;
            	f_extr = f_wlet[i_extr];
            }
        }
        int end_nonoise = i-1;
#ifdef debug_lines
        xs[cou] = begin_nonoise*delta_x; xe[cou] = begin_nonoise*delta_x; ys[cou] = -minimalConnector*2; ye[cou] = minimalConnector*2; cou++;
        xs[cou] = end_nonoise*delta_x; xe[cou] = end_nonoise*delta_x;     ys[cou] = -minimalConnector*2; ye[cou] = minimalConnector*2; col[cou]= 0xFFFFFF; cou++;
#endif
		// �������� ������ ������ � ������� ������� (������� ���������� ������ "������+���" ����� ������ ������ ������ ������� ����������� ������)
        // ��� ���� ���� ����� ���������� ��������� ��� ������� �����, �� ��� ��-���� ��������� �� ���� �������
        int j;
        for(j=begin_nonoise; j<end_nonoise; j++)
        { if(fabs(f_wlet[j])>=minimalThreshold+noise[j])
       	break;
        }
		int begin = j;
        for(j=end_nonoise; j<begin_nonoise; j--)
        { if(fabs(f_wlet[j])>=minimalThreshold+noise[j])
       	break;
        }
        int end=j;
        if(begin <= end)// begin>end ������ ���� ����� ��� � �� ������ �� ���� ������� �����
        {	Splash* splash = new Splash(begin, end, begin_nonoise, end_nonoise, sign, f_extr, i_extr, area);
        	splashes.add(splash);
        }
    }
#ifdef debug_lines
    // ���������� ������
    xs[cou] = 0; ys[cou] =  minimalThreshold; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] =  minimalThreshold; col[cou] = 0x004444; cou++;
    xs[cou] = 0; ys[cou] = -minimalThreshold; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] = -minimalThreshold; col[cou] = 0x004444; cou++;
    xs[cou] = 0; ys[cou] =  minimalWeld; 	  xe[cou] = lastNonZeroPoint*delta_x; ye[cou] =  minimalWeld;      col[cou] = 0x009999; cou++;
    xs[cou] = 0; ys[cou] = -minimalWeld; 	  xe[cou] = lastNonZeroPoint*delta_x; ye[cou] = -minimalWeld;	   col[cou] = 0x009999; cou++;
	xs[cou] = 0; ys[cou] =  minimalConnector; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] =  minimalConnector; col[cou] = 0x00FFFF; cou++;
    xs[cou] = 0; ys[cou] = -minimalConnector; xe[cou] = lastNonZeroPoint*delta_x; ye[cou] = -minimalConnector; col[cou] = 0x00FFFF; cou++;
#endif
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::calcAverageFactor(double* fw, int scale)
{
	double f_wlet_avrg = calcWletMeanValue(fw, -0.5, 0, 500);
	average_factor = f_wlet_avrg / getWLetNorma2(scale);
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::fillNoiseArray(double *y, int data_length, int N, double Neff, double *outNoise)
{	findNoiseArray(y, outNoise, data_length, N); // external function from findNoise.cpp
	int i;
	if (true || Neff < 1) // �������, ��� true �����
	{	Neff = 1;
	}
	for (i = 0; i < N; i++)
	{	// ��� ������, ��� sqrt(Neff) � ����� ������ ������ ��
		// �������������� ��������, ���� ������ ������ ������.
		outNoise[i] *= THRESHOLD_TO_NOISE_RATIO * 3 / sqrt(Neff);
	}
}
//------------------------------------------------------------------------------------------------------------
int InitialAnalysis::getLastPoint()
{   int lastPoint = findReflectogramLength(data, data_length) - 1;
	if (lastPoint < 0)
		lastPoint = 0;
	//if(lastPoint + 10 < data_length) lastPoint += 10; �� �������, ����� ��� ������ ���� 
    return lastPoint;
}
//------------------------------------------------------------------------------------------------------------
// f- �������� �-���,
// f_wlet - �������-�����
void InitialAnalysis::performTransformationAndCenter(double* f, int begin, int end, double* f_wlet, int scale, double norma)
{
	// transform
	performTransformationOnly(f, begin, end, f_wlet, scale, norma);
	centerWletImageOnly(f_wlet, scale, begin, end);
}

void InitialAnalysis::centerWletImageOnly(double* f_wlet, int scale, int begin, int end)
{
	// shift (calcAverageFactor must be performed by now!)
	double f_wlet_avrg = average_factor * getWLetNorma2(scale);
	for(int i=begin; i<end; i++)
    {	f_wlet[i] -= f_wlet_avrg;
    }
}
//------------------------------------------------------------------------------------------------------------
// f- �������� �-���,
// f_wlet - �������-�����
void InitialAnalysis::performTransformationOnly(double* f, int begin, int end, double* f_wlet, int freq, double norma)
{	double tmp;
	int i;
	double *wLetData = new double[freq * 2 + 1];
	assert(wLetData);
	for (i = -freq; i <= freq; i++)
	{	wLetData[i+freq] = wLet(i, freq, norma);
    }
	for (i=begin; i<end; i++)
	{	tmp = 0;
		int jL = i-freq;
		int jMin = max(i-freq, 0);
		int jMax = min(i+freq+1, end);
		int jR = i+freq+1;
		int j;
		for (j = jL;   j < jMin; j++)	tmp += f[jMin]   * wLetData[j-i+freq];
		for (j = jMin; j < jMax; j++)	tmp += f[j]	     * wLetData[j-i+freq];
		for (j = jMax; j < jR;   j++)	tmp += f[jMax-1] * wLetData[j-i+freq];
		f_wlet[i] = tmp;
	}
	delete[] wLetData;
}
//------------------------------------------------------------------------------------------------------------
// ��������� ������� �������� �������-������ 
double InitialAnalysis::calcWletMeanValue(double *fw, double from, double to, int columns)
{   //��������� ��������� ��������� � �������� [0; -0.5] ��
	Histogramm* histo = new Histogramm(from, to, columns);
	histo->init(fw, 0, lastNonZeroPoint-1);
	double mean_att = histo->getMaximumValue();
	delete histo;
	return mean_att;
}
//------------------------------------------------------------------------------------------------------------
// ��������� ������ �������� ����������� �� ������ ������ ��� : ���� ������� ������ ���,
// ��� ������ ��� ���������, �� �� ������� ��� �������, � ��������� �����������
// ��������������, ��� ��� ������� ���� �� ������� ���� �� ������ !
void InitialAnalysis::correctAllConnectorsFronts(double *arr)
{	if( events->getLength() < 2 )
return;
	for(int n=0; n<events->getLength(); n++)
    {   EventParams* ev1 = (EventParams*)(*events)[n];
        if( ev1->type != EventParams::CONNECTOR )// ���� �� ����� �� ����������
    continue;
    	// ���� ����� �� ������ ���������� �����, ��� �� ����� �� �� - �����, � ������ - �� ������
        int i_begin = ev1->begin, i_end = ev1->end;
        int i_max = i_begin;// ����� ���� �����
        double f_max = data[i_max];
		int i;
        for( i=i_begin; i<i_end; i++ ) // ���� ��������
        {	if(data[i]>f_max) {i_max = i; f_max = data[i];}
        }
		int i_x = -1; // x - ������� �����;
        double f_lmax = data[i_begin];
        for( i=i_begin; i<i_max; i++ )
        { 	if(f_lmax <= data[i])
        	{	f_lmax = data[i];
            	if(i_x == -1)
                {	i_x = i;
                }
            }
        	if(	i_x!=-1 && data[i]<data[i_x] )
            {	i_x = -1;
            }
        }
        if( i_x==-1 )
        {	i_x = i_begin;
        }
		else
        { // ��������� ������� �� 0.02 (�������� �� ������� �������� ����� �����������)
          double f_min = data[i_begin];
          double f_x = f_min + (f_max-f_min)*0.02;
          for( int i=i_x; i<i_max; i++ )
          {	if(data[i]>f_x)
          	{ i_x = i;
          break;
            }
          }
        }
        double ev1_beg_old = ev1->begin;
        ev1->begin = i_x - 1;
		// ������ ������� ������� ����� (���� ��� ����)���� ��������
        if(n>0)
        { EventParams* ev2 = (EventParams*)(*events)[n-1];
          // ������� ������ ���� ������� "���������"
          if(fabs(ev2->end - ev1_beg_old)<2){ ev2->end = ev1->begin; }
        }
    }
}
//------------------------------------------------------------------------------------------------------------
// �������� ��������������� ��������-������ ��� ��������� ��������� ������
void InitialAnalysis::correctAllSpliceCoords()
{	for(int n=1; n<events->getLength(); n++)
    {	EventParams* ev = (EventParams*)(*events)[n];
    	if(ev->type == EventParams::SPLICE)
        {	correctSpliceCoords(n);
        }
    }
}
//------------------------------------------------------------------------------------------------------------
// �-� ������ ������� ����� !  (��� ��� ���������� ��� �� ������ ��� �������� ������ �� ������ ��������)
// ��������� ����� ������ ������ ������, �� ����� �� ���������
void InitialAnalysis::correctSpliceCoords(int n)
{   EventParams* ev_lp = (EventParams*)(*events)[n];
    EventParams& ev = *ev_lp;
	// ���� ��� �� ������, �� �����
    if(ev.type != EventParams::SPLICE)
return;
	//prf_b("correctSpliceCoords: enter");
	const double noise_factor = 0;  // 0 , ���� �� �� ��������� ��� � �������� �������
    const double angle_factor = 1.8; // ���������� ��������� ������ ��� ������ �� �������������� ����� �� ������� ���������
	const double factor = 1.1; // ��������� �������������� ����������
	const int nscale = 20; // ���������� ������ ���������
	int width = wlet_width; // frame-width: ������ ���� (������������ ������� �������), � ������� �� �������� �������������� ������
	int w_l = ev.begin, w_r = ev.end; // w_l - wavelet_left: ������� �������-������ �� ������� ��������
    int left_cross = (int )(w_l+width*angle_factor), right_cross = (int )(w_r-width*angle_factor); // ����� ����������� �������� ������� ��� �� (�� ���� ������������ ����������� ��������, ��� ������� ��� ���������, ������ ��� ����� X, ��� ������� ������ �����, ������������ ��������)

	int i;
#ifdef debug_lines
    int coln=-1,a=0xFFFFFF,b=0x0000FF,c=0x00FF00,d=0xFF7733,color[]={a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d};
#endif
    // ����������� ��� ������� ���������
	for(int step=1; step<=nscale; step++)
    {   width = (int )(wlet_width/pow(factor,step));// ����� �� ����������� ������
    	if(width<=1)
    break;
		//prf_b("correctSpliceCoords: getWLetNorma");
	    wn = getWLetNorma(width);
		//prf_b("correctSpliceCoords: performTransformationAndCenter");
	   	performTransformationAndCenter(data, w_l, w_r+1, f_wlet, width, wn);
		//prf_b("correctSpliceCoords: processing");
		// ������� ������� � ���� ( ������� �������������� �� ����������� ������ �� ��������� )
		// ������� ���� ��������� ���������� ��� ������ ��������
        int i_max = w_l;
        double f_max = f_wlet[i_max];
        for(i=w_l; i<w_r; i++)
        {	if( fabs(f_wlet[i])>fabs(f_max) ) // ��������� ����� � �������� ������ ������� ��������������, �� ����� �������� � ��������
        	{	i_max = i; f_max = f_wlet[i_max];
            }
        }
		double f_lmax = f_wlet[w_l], df_left = 0, df_right = 0;
        for(i=w_l; i<i_max; i++) // ������� ����� �� ����������� ������ �� ���������
        { if( fabs(f_wlet[i])>fabs(f_lmax) ) { f_lmax = f_wlet[i];}// ����� ��������  ����������
          if( df_left<fabs(f_lmax-f_wlet[i]) ) { df_left=fabs(f_lmax-f_wlet[i]);} // ����� ������������ ������� �������
        }
		f_lmax = f_wlet[w_r];
        for(i=w_r; i>i_max; i--) // ������� ������ �� ����������� ������ �� ���������
        { if(fabs(f_wlet[i])>fabs(f_lmax)) { f_lmax = f_wlet[i];}// ����� �������� ����������
          if(df_right<fabs(f_lmax-f_wlet[i])){ df_right=fabs(f_lmax-f_wlet[i]);} // ����� ������������ ������� �������
        }

		// ���� ����������� �����, ������� �������� ������� ����� ( �� ���� ���� i+width<=left_cross )
        for(i=w_l; i<w_r && i+width*angle_factor<=left_cross; i++)
        {	if(fabs(f_wlet[i])>=minimalWeld+noise[i]*noise_factor+df_left)
        	{	w_l=i-1;
	            if(w_l+width*angle_factor<left_cross){ left_cross = (int )(w_l+width*angle_factor);}
        break;
            }
        }
   		// ���� ����������� ������
        for(int j=w_r; j>w_l && j-width*angle_factor>=right_cross; j--) // j-width>=right_cross - ������� �������� � ��������� �� 45 ��
        {	if(fabs(f_wlet[j])>=minimalWeld+noise[j]*noise_factor+df_right)
        	{	w_r=j+1;
	            if(w_r-width*angle_factor>right_cross)
                { right_cross = (int )(w_r-width*angle_factor);}
        break;
            }
        }
#ifdef debug_lines //������ ������� ������ ��� ������� ��������
		{ coln++;
          for(int i=w_l-1; i<=w_r+1; i++)
          { double x1=i, y1=f_wlet[i], x2=i+1, y2=f_wlet[i+1];
            xs[cou]=x1*delta_x; ys[cou]=y1; xe[cou]=x2*delta_x; ye[cou]=y2;
            col[cou]=color[coln];
            if(i<left_cross || i>right_cross) col[cou] = 0x888888;
            cou++;
          }
        }
#endif
    }
	//prf_b("correctSpliceCoords: scales done");
	if( (ev.begin!=w_l || ev.end!=w_r) && (w_l<w_r))
    { 	ev.begin = w_l;
    	ev.end = w_r;
	    //������������ ������� �������
        if(n > 0)
        {	EventParams& ev_left = *(EventParams*)(*events)[n-1];
        	ev_left.end = ev.begin;
        }
		if(n < events->getLength()-1)
        {	EventParams& ev_right = *(EventParams*)(*events)[n+1];
	        ev_right.begin = ev.end;
        }
    }
#ifdef debug_VCL
    wn = getWLetNorma(wlet_width);
  	performTransformationAndCenter(data, 0, lastNonZeroPoint, f_wlet, wlet_width, wn);
#endif
	//prf_b("correctSpliceCoords: return");
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::excludeShortLinesBetweenConnectors(double* arr, int szc)
{   if(events->getLength()<2)
return;
    int cou1 =0;
	for(int n1=0, n2, n3; n1<events->getLength(); n1++)
	{   // ���� �� ����� �� ����������
        EventParams* ev1 = (EventParams*)(*events)[n1];
    	if(ev1->type != EventParams::CONNECTOR)
    continue;
        else {cou1++;}
		if(cou1 == 1) // ������ "���������" ��� ������ ����
    continue;
        n2 = n1+1;
        if(n2 >= events->getLength())
    break;
		EventParams* ev2 = (EventParams*)(*events)[n2];
        if(ev2->type != EventParams::LINEAR)
    continue;
        n3 = n2+1;
        if(n3 >= events->getLength())
    break;
	    EventParams* ev3 = (EventParams*)(*events)[n3];
        if(ev3->type != EventParams::CONNECTOR)
    continue;
    	if(ev2->end - ev2->begin < szc)
        { ev1->end = ev3->begin;
          events->slowRemove(n2); 
        }
    }//for
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::deleteAllEventsAfterLastConnector()
{   for(int i=events->getLength()-1; i>0; i--)
	{   EventParams* ev = (EventParams*)(*events)[i];
    	if( ev->type != EventParams::CONNECTOR)
        {   events->slowRemove(i);
        }
        else
     	{
    break;
        }
    }
}
//------------------------------------------------------------------------------------------------------------
// �����: ������������ ��� �������� ������� ��� ��� ������ ! (����� ����� ����������� ��������)
void InitialAnalysis::addLinearPartsBetweenEvents()
{   ArrList* events_new = new ArrList();
	for(int i=0; i<events->getLength(); i++)
    {   EventParams *cur = (EventParams*)(*events)[i];
        if (i>0)
        { EventParams *prev = (EventParams*)(*events)[i-1];
          if(prev->end < cur->begin)
          { EventParams *ep = new EventParams();
            ep->type = EventParams::LINEAR;
            ep->begin = prev->end;
            ep->end = cur->begin;
            events_new->add(ep);
          }
        }
        events_new->add(cur);
    }
    delete events;
    events = events_new;
}
//------------------------------------------------------------------------------------------------------------
// �������, ��� ���������� ����� ������� _���������_
void InitialAnalysis::calc_rms_line(double* arr, int beg, int end, double& a, double& b)
{	if(beg > end) { int temp = end; end = beg; beg = temp;}
    if(beg == end || beg < 0 || end > data_length)
    { a=0; b=0;
return;
    }
    double e1=0, e2=0, e3=0, e4=0;
    int n = end-beg+1;

    for(int i = beg; i <= end; i++)
    {   e1 += (double)i*i;// (double) �� ������������ int
        e2 += i;
        e3 += i * arr[i];
        e4 += arr[i];
    }
    a = (n*e3 - e2*e4)/(n*e1 - e2*e2);
    b = (e3 - a*e1)/e2;

    a /= delta_x;
}
//------------------------------------------------------------------------------------------------------------
