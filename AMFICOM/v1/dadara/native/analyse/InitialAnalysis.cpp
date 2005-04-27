//----------------------------------------
#include "InitialAnalysis.h"
#include "../common/MathRef.h"
#include "Histogramm.h"

#include <math.h>
#include <assert.h>
#include <stdlib.h>
#include "../an2/findLength.h"
#include "../an2/findNoise.h"
#include "../wavelet/wavelet.h"

#include "../common/prf.h"

#define xsign(f) ((f)>=0?1:-1) 
//------------------------------------------------------------------------------------------------------------
// Construction/Destruction
InitialAnalysis::InitialAnalysis(
	double* data,				//����� ��������������
	int data_length,			//����� �����
	double delta_x,				//���������� ����� ������� (�)
	double minimalThreshold,	//����������� ������� �������
	double minimalWeld,			//����������� ������� ���������������� �������
	double minimalConnector,	//����������� ������� �������������� �������
	double minimalEnd,			//����������� ������� ��������� � ����� �������
	double noiseFactor,			// ��������� ��� ������ ���� (����� 2.0)
	int reflectiveSize,			//����������� ����� �������������� �������
	int nonReflectiveSize,		//����������� ����� ���������������� �������
	int lengthTillZero,			//����������� ������� ����� ( ==0 -> ����� �����)
	double *externalNoise)		//����������� ������� ��� ( ==0 -> ���� ����)
{
#ifdef DEBUG_INITIAL_ANALYSIS
	logf = fopen(DEBUG_INITIAL_WIN_LOGF, "a");
	assert(logf);
	fprintf(logf, "=== IA invoked\n"
		"len %d deltaX %g minTh %g minWeld %g minConn %g noiseFactor %g\n",
		data_length, delta_x, minimalThreshold, minimalWeld, minimalConnector, noiseFactor);
	fprintf(logf, "refSize %d nRefSize %d lTZ %d extNoise %s\n",
		reflectiveSize, nonReflectiveSize, lengthTillZero, externalNoise ? "present" : "absent");
	fflush(logf);
#endif

#ifdef debug_lines
    cou = 0;
    for(int i=0; i<sz; i++){col[i] = -1;}
#endif
	this->delta_x				= delta_x;
	this->minimalThreshold		= minimalThreshold;
	this->minimalWeld			= minimalWeld;
	this->minimalConnector		= minimalConnector;
    this->minimalEnd			= minimalEnd;
	this->data_length			= data_length;
	this->data					= data;
    this->reflectiveSize		= reflectiveSize;
    this->wlet_width			= nonReflectiveSize;

    events = new ArrList();

	if (lengthTillZero <= 0)
		lastPoint = getLastPoint();
	else
		lastPoint = lengthTillZero - 1;

	f_wlet	= new double[lastPoint];
#ifdef debug_VCL
	f_tmp   = new double[lastPoint];
#endif
	noise	= new double[lastPoint];
	type	= new double[data_length];

	// ���� ������ � ������� ���� �� ����� �����,
	// ���� ������������ IA �� ������ ��� ������,
	// ������� ��� ����
	if (externalNoise == 0 || lengthTillZero <= 0)
	{	prf_b("IA: noise");
		// ��������� ������� ����
		{ const int sz = lastPoint;
		  //fillNoiseArray(data, data_length, sz, 1 + width/20, noise);
		  fillNoiseArray(data, data_length, sz, 1.0, noiseFactor, noise);
		}
	}
	else
	{	int i;
		for (i = 0; i < lastPoint; i++)
		{	noise[i] = externalNoise[i] * noiseFactor;
        }
	}
	prf_b("IA: analyse");
	performAnalysis();
	prf_b("IA: done");
#ifdef DEBUG_INITIAL_ANALYSIS
	fprintf(logf, "IA: resulting nEvents = %d\n", (int)(getEvents().getLength()));
#endif
}
//------------------------------------------------------------------------------------------------------------
InitialAnalysis::~InitialAnalysis()
{	delete[] type;
	delete[] noise;
    delete[] f_wlet;

    events->disposeAll();

    delete events;
#ifdef DEBUG_INITIAL_ANALYSIS
	fclose(logf);
#endif
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performAnalysis()
{	// ��������� �������-��������������
	// f_wlet - �������-����� �������, wlet_width - ������ ��������, wn - ����� ��������
    wn = getWLetNorma(wlet_width);
    performTransformationOnly(data, 0, lastPoint, f_wlet, wlet_width, wn);
	calcAverageFactor(f_wlet, wlet_width, wn);
	centerWletImageOnly(f_wlet, wlet_width, 0, lastPoint, wn);// �������� �� ������������� ��������������(��) ���������� ������������
#if 0
	{	FILE *f = fopen ("noise2.tmp", "w");assert(f);
		int i;
		for (i = 0; i < lastPoint; i++)
			fprintf(f,"%d %g %g %g\n", i, data[i], f_wlet[i], noise[i]);
		fclose(f);
	}
#endif
	{ // ���� ��� �������� �������-������
      ArrList splashes; // ������� ������ ArrList
      findAllWletSplashes(f_wlet, splashes); // ��������� ������ splashes ���������
	  if(splashes.getLength() == 0){
return;}
      findEventsBySplashes(splashes); // �� ���������� ��������� ���������� ������� (�� ���� - ������������� ���������)
      // ���������� ArrList � ��� �������
      splashes.disposeAll(); // ������� ������ ArrList
    } // ������� ������ ������ splashes

    correctAllConnectorsFronts(data);// � ������ ����� ��������������� ��������� ��������� � �����_������� 
    processEndOfTrace();// ���� �� ������ ���������� �� ����� �������, �� �������� ��� �������
    excludeShortLinesBetweenConnectors(data, wlet_width);
    correctAllSpliceCoords();// ��������� ��������� ������� �������� �������, �� � ����� ������� �������� ������� ����� ��� ������������ ( ������� ����� ����� addLinearPartsBetweenEvents() )
    addLinearPartsBetweenEvents();
	trimAllEvents(); // ��������� �� ������������ ���������� �� ���� ����� ����� � ������ �������, �� ��� ����� ��������� ���� �� ����� �� ���� ����� - ��� ���������, �� �� �� ������������ ��� ������� � ����������� ������ ��������� � ��� 
	verifyResults(); // ��������� ������
}
//-------------------------------------------------------------------------------------------------
void InitialAnalysis::findEventsBySplashes(ArrList& splashes)
{//* ������ ���� ����  ���� �����
    int shift = 0;
    if( splashes.getLength() <=2 )
return;
	Splash* sp1 = (Splash*)splashes[0];
    Splash* sp2;
    shift = processDeadZone(splashes);// ���� ������ ����
	// ���� ��������� ����������  � ������
    for(int i = shift+1; i<splashes.getLength()-1; i++)
    { int len = processIfIsConnector(i, splashes);
      if(len !=0)// ���� ��������� ��� ������
      { i+= len;
    continue;
      }
      sp1 = (Splash*)splashes[i];
      sp2 = (Splash*)splashes[i+1];
      int dist = abs(sp2->begin_weld_n - sp1->end_weld_n);
      // ��� ������ "+" � "-" ����� ������
      if( dist<reflectiveSize/2			// ���� �������� ����� ������
          && (sp1->sign>0 && sp2->sign<0) // ������ �������������, � ������ - �������������
          && ( sp1->begin_weld_n != -1 && sp2->begin_weld_n != -1)// � ��� ���� ��� ������� ���������
        )
      {   EventParams *ep = new EventParams;
          setUnrecognizedParamsBySplashes((EventParams&)*ep, (Splash&)*sp1, (Splash&)*sp2 );
          events->add(ep);
          i++;// ������ ��� ������� �� ���� ���������
	continue;
      }
      // ������
      if( sp1->begin_weld_n != -1 && fabs(sp1->end_weld_n-sp1->begin_weld_n)>1) //������
      {	EventParams *ep = new EventParams;
        setSpliceParamsBySplash( (EventParams&)*ep, (Splash&)*sp1 );
        events->add(ep);
	continue;
      }
    }
}
// -------------------------------------------------------------------------------------------------
int InitialAnalysis::processDeadZone(ArrList& splashes)
{   int i, shift = 0;
	int begin = 0, end = -1;
    int n_max = 0, n_min = 0;
    double f_max = 0, f_min = 0;
	Splash* sp1 = (Splash*)splashes[0];
    Splash* sp2 = (Splash*)splashes[1];
    if(sp1->sign<0) // ���� ������ ����� ����, �� �������, ��� ��� ���������� � ����
    { begin = 0;
      end  = sp1->end_thr;
    }
    else // ����� �� ���������� reflectiveSize �� ������ ���� ������������ ������� ����� � ���� ���� ����������� ������� ����
    { for(i = 0; sp2->begin_thr<reflectiveSize && i+1<splashes.getLength(); i++)
      { sp1 = (Splash*)splashes[i];
        sp2 = (Splash*)splashes[i+1];
        if(sp1->sign>0 && f_max<sp1->f_extr )
        { f_max = sp1->f_extr;
          begin = sp1->begin_thr;
		  // ���������� ����������� ����� ������, ��� ��� �������� ��������� ������ 	
          end = -1;
          f_min = 0;
        }
        if(sp2->sign<0 && f_min>sp2->f_extr && sp2->end_thr>begin)
        { f_min = sp2->f_extr;
          end = sp2->end_thr;
          shift = i+1;
        }
      }
    }
    if(end == -1)// ���� �� ����� ��������� �����-���� � �������� reflectiveSize , �� ���� ������ ����
    { for( ; i<splashes.getLength(); i++)
      { sp1 = (Splash*)splashes[i];
        if(sp1->sign<0)
        { end = sp1->end_thr;
          shift = i;
        }
      }
    }
    if(end !=-1 )
    { EventParams *ep = new EventParams;
      ep->type = EventParams::DEADZONE;
      ep->begin = begin; ep->end = end;
      if(ep->end > lastPoint){ ep->end = lastPoint;}
      events->add(ep);
    }
    return shift;
}
// -------------------------------------------------------------------------------------------------
// ����������, ���� �� ���-�� ������� �� ��������� , ���� ������ � i-�� ��������, � ���� ���� - ���������� �
// ��������, ������� �������� i � ������ �����; ���� ������ �� �����, �� ����� ����� 0.
// � ������ ������ �������������� ���:
// ���� �� ��������� �� ����� ������� ��������� ������������ ������� ����� � � �������� reflSize ���������
// ������������, �� �� � ������. ���� ������������� ���, �� ���� ���� �� ��������� ����, �� � ��������
// ���������� ������ ����� ������� ( � �������� reflSize ) ���������.
int InitialAnalysis::processIfIsConnector(int i, ArrList& splashes)
{   int shift = 0;
    int rsz = reflectiveSize;
    Splash* sp1 = (Splash*)splashes[i];
    Splash* sp2, *sp_tmp;
    // ���� ���������� � �������� �������� �����, �� ����, ��� �� ������ ����
    if(sp1->begin_conn_n !=-1 && sp1->sign>0)
    { // ���� �� ����� ���������� ������������ �������, �� ��� ������ ������ �� ���������
      for(int j=i+1; j<splashes.getLength(); j++)
      { sp2 = (Splash*)splashes[j];
        if(fabs(sp2->begin_weld_n - sp1->begin_conn_n) > rsz){ // !!! �������, ��� ����������� ������ �������� ��������, �� ���� weld_n != -1
      break;}
        if(sp2->begin_conn_n != -1 && sp2->sign < 0) // ���� ����� ������� ����, �� ������ ��������� �����������
        { shift = j-i;
      break;
        }
      }
      // ���� ������������ ���� ��� � �� ����, ��
      if(shift==0)
      { for(int j=i+1; j<splashes.getLength(); j++)
        { sp_tmp = (Splash*)splashes[j];
          if(fabs(sp_tmp->begin_weld_n - sp1->begin_conn_n) > rsz){ // !!! �������, ��� ����������� ������ �������� ��������, �� ���� weld_n != -1
      break;}
          if(sp_tmp->begin_weld_n != -1 && sp_tmp->sign < 0)//���� ��������� ��������� ���� �� ������� reflSize
          { shift = j-i;
            sp2 = (Splash*)splashes[i+shift];
          }
          if(sp_tmp->begin_conn_n != -1 && sp_tmp->sign > 0)//���� ����� ������������ �����, �� ������ ������� ������ ������ ����������
          { shift = j-i;
            sp2 = (Splash*)splashes[i+shift];
            shift--;// ����� ���� �� ������� ��� ������� ���������� ����������
      break;
          }
        }
      }
      //  ���� ���� ����� ���������, �� ��������� ��� � �������
      if(shift!=0)
      { EventParams *ep = new EventParams;
        setConnectorParamsBySplashes((EventParams&)*ep, (Splash&)*sp1, (Splash&)*sp2 );
        events->add(ep);
#ifdef debug_lines
		double begin = ep->begin, end = ep->end;
        xs[cou] = begin*delta_x; xe[cou] = end*delta_x; ys[cou] = minimalConnector*2*1.1;  ye[cou] = minimalConnector*2*1.5;  col[cou]=0x00FFFF; cou++;
#endif

      }
    }
    return shift;//���� ���������� �� �����, �� shift = 0
}
// -------------------------------------------------------------------------------------------------
// � ����� ������ �� ������������, ��� ������ f_wlet ������� ��� �������� ������� wlet_width � �� ��������
void InitialAnalysis::setSpliceParamsBySplash( EventParams& ep, Splash& sp)
{   if(sp.sign>0) { ep.type = EventParams::GAIN; }
    else 		  { ep.type = EventParams::LOSS; }
    ep.begin = sp.begin_weld;
    ep.end = sp.end_weld+1;
    if(ep.end>lastPoint){ep.end = lastPoint;}
    if(ep.begin<0){ep.begin=0;}
    double max = -1;
	for(int i=sp.begin_weld_n; i<sp.end_weld_n; i++)
    { double res = (fabs(f_wlet[i])-minimalWeld)/noise[i] - 1;
      if(max<res) max = res;
    }
    ep.R = max;
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::setConnectorParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2 )
{  ep.type = EventParams::CONNECTOR;
   ep.begin = sp1.begin_thr;
   if(ep.begin<0){ep.begin=0;}
   ep.end = sp2.end_thr;
   if(sp2.begin_conn_n != -1 && sp2.sign > 0)// ���� ��� ������ ������ ����������
   { ep.end = sp2.begin_thr;// ���� ��� ���������� �����, �� ����� ������� ��������� � ������� ���������� 
   }
   if(ep.end>lastPoint)
   {ep.end = lastPoint;
   }
   double max = -1;
   int i;
   for(i=sp1.begin_conn_n ; i<sp1.end_conn_n; i++)
   { double res = (f_wlet[i]-minimalConnector)/noise[i] - 1;
     if(max<res) max = res;
   }
   ep.R1 = max;
   max = -1;
   for(i=sp2.begin_thr ; i<sp2.end_thr; i++)
   { double res = fabs(f_wlet[i])/minimalThreshold - 1;
     if(max<res) { max = res;}         
   }
   ep.R2 = max;
   double l = sp2.begin_thr - sp1.end_conn_n, l_max = reflectiveSize*2;
   assert(l>=-1);// -1 ����� ���� ��� ��� �� ���������� ��������� �� ���� ����� ������ ������� (������ �� ������, � ����� ����� )
   ep.R3 = 2*ep.R2*(l_max-l)/(l+wlet_width)*(wlet_width/l_max);
   // ����� �� ���� "���������" ���� ������ �������
   if(sp1.sign>0 && sp1.f_extr>= minimalEnd)
   { ep.can_be_endoftrace = true;
   }
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::setUnrecognizedParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2 )
{  ep.type = EventParams::UNRECOGNIZED;
   ep.begin = sp1.begin_thr;
   if(ep.begin<0){ep.begin=0;}
   ep.end = sp2.end_thr;
   if(ep.end == 9642)
   {	int o=0; 
   }
   if(ep.end>lastPoint){ep.end = lastPoint;}
}
// -------------------------------------------------------------------------------------------------
// �����: �������, ��� minimalThreshold < minimalWeld < minimalConnector
void InitialAnalysis::findAllWletSplashes(double* f_wlet, ArrList& splashes)
{   //minimalThreshold,	//����������� ������� �������
	//minimalWeld,		//����������� ������� ���������������� �������
	//minimalConnector,	//����������� ������� �������������� �������
    for(int i=1; i<lastPoint; i++)// 1 �.�. i-1
    {   if(fabs(f_wlet[i])<=minimalThreshold)
     continue;
		Splash& spl = (Splash&)(*(new Splash()));// ��� �� ��������� ���� �� ���� �����, �� ������ ��� ������ ���� ������;
        spl.begin_thr = i;
        spl.f_extr = f_wlet[i];
		int sign, sign_cur;
		sign = xsign(f_wlet[i]);
        for(  ; i<lastPoint; i++)
        {   sign_cur = xsign(f_wlet[i]);
        	// ����������� �� ����
        	if( fabs(f_wlet[i])>= minimalThreshold+noise[i] && spl.begin_thr_n == -1)
            {	spl.begin_thr_n = i-1;
            }
            if( spl.begin_thr_n != -1 && spl.end_thr_n == -1
            	&& ( fabs(f_wlet[i])<= minimalThreshold+noise[i] || sign_cur!=sign || i==lastPoint-1)
	          )
            {	spl.end_thr_n = i;
            }
			// ���������
            if( fabs(f_wlet[i])>= minimalWeld && spl.begin_weld == -1)
            {	spl.begin_weld = i-1;
            }
            if( spl.begin_weld != -1 && spl.end_weld == -1
            	&& ( fabs(f_wlet[i])<= minimalWeld || sign_cur!=sign || i==lastPoint-1)
              )
            {	spl.end_weld = i;
            }
            if( fabs(f_wlet[i])>= minimalWeld+noise[i] && spl.begin_weld_n == -1)
            {	spl.begin_weld_n = i-1;
            }
            if( spl.begin_weld_n != -1 && spl.end_weld_n == -1
            	&& (fabs(f_wlet[i])<= minimalWeld+noise[i] || sign_cur!=sign || i==lastPoint-1)
              )
            {	spl.end_weld_n = i;
            }
            // ������������
            if( fabs(f_wlet[i])>= minimalConnector && spl.begin_conn == -1)
            {	spl.begin_conn = i-1;
            }
            if( spl.begin_conn != -1 && spl.end_conn == -1
            	&& ( fabs(f_wlet[i])<= minimalConnector || sign_cur!=sign || i==lastPoint-1)
              )
            {	spl.end_conn = i;
            }
            if( fabs(f_wlet[i])>= minimalConnector+noise[i] && spl.begin_conn_n == -1)
            {	spl.begin_conn_n = i-1;
            }
            if( spl.begin_conn_n != -1 && spl.end_conn_n == -1
            	&& (fabs(f_wlet[i])<= minimalConnector+noise[i] || sign_cur!=sign || i==lastPoint-1)
              )
            {	spl.end_conn_n = i;
            }
			// ����������� �� ����
            if( fabs(f_wlet[i])<=minimalThreshold || sign_cur!=sign || i==lastPoint-1)
        	{	spl.end_thr = i;
     	break;
     		}
            if(fabs(spl.f_extr)<fabs(f_wlet[i])){ spl.f_extr = f_wlet[i];}
        }
		spl.sign = sign;

        spl.begin_thr--;
        if( spl.begin_thr < spl.end_thr // begin>end ������ ���� ����� ��� � �� ������ �� ���� ������� �����
	        && spl.begin_weld_n != -1 // !!!  ��������� ������ ������������ �������� ( ���� ��� �������� ������, �� ������������� ����������� ���� ��������, ��� ��� ���� ����� ����� ������������� ���������� ����� �������� ��������� ���������� ������� �����, �� ���������� ��������� �� ����� ! )
           )
        {  splashes.add(&spl);
#ifdef debug_lines
           double begin = spl.begin_thr, end = spl.end_thr, s = spl.sign;
           xs[cou] = begin*delta_x; xe[cou] = begin*delta_x; ys[cou] = -minimalConnector*2*0.9; ye[cou] = minimalConnector*2*1.1; cou++;
           xs[cou] = end*delta_x; xe[cou] = end*delta_x; ys[cou] = -minimalConnector*2*1.1; 	ye[cou] = minimalConnector*2*0.9; col[cou]= 0xFFFFFF; cou++;
           double c = spl.begin_conn_n==-1 ? 0xFF0000 : 0x0000FF;
           if(s>0){	xs[cou] = begin*delta_x; xe[cou] = end*delta_x; ys[cou] = minimalConnector*2*1.1;  ye[cou] = minimalConnector*2*0.9;  col[cou]=c; cou++;}
           else   {	xs[cou] = begin*delta_x; xe[cou] = end*delta_x; ys[cou] = -minimalConnector*2*0.9; ye[cou] = -minimalConnector*2*1.1; col[cou]=c; cou++;}
#endif
        }
        else
        {  delete &spl; // ���� ����� �� ������, �� ����� ������ ������, ��� ��� �������� ������ �� spl, �������  ���� ��������� � splashes 
        }
	}
    if(splashes.getLength() == 0)
return;
	// ���������� ��������� ��������� ������� ���� ��� ��� �� �� ������� ����� �� ���� � ����� ������� ����� �� ������ ���� ���� ���������� � ����� �� ����� ���������
	Splash* splend = (Splash*)splashes[splashes.getLength()-1];
    if(splend->sign > 0)
    {   Splash* spl = new Splash();
    	spl->begin_thr 		= lastPoint+1; spl->begin_thr_n 	= lastPoint+1; spl->begin_weld 	= lastPoint+1;
        spl->begin_weld_n 	= lastPoint+1; spl->begin_conn 	= lastPoint+1; spl->begin_conn_n	= lastPoint+1;
        spl->end_thr 		= lastPoint+2; spl->end_thr_n	= lastPoint+2; spl->end_weld		= lastPoint+2;
        spl->end_weld_n 	= lastPoint+2; spl->end_conn 	= lastPoint+2; spl->end_conn_n 	= lastPoint+2;
		spl->sign			= -1;
        splashes.add(spl);
    }
#ifdef debug_lines
    // ���������� ������
    xs[cou] = 0; ys[cou] =  minimalThreshold; xe[cou] = lastPoint*delta_x; ye[cou] =  minimalThreshold; col[cou] = 0x004444; cou++;
    xs[cou] = 0; ys[cou] = -minimalThreshold; xe[cou] = lastPoint*delta_x; ye[cou] = -minimalThreshold; col[cou] = 0x004444; cou++;
    xs[cou] = 0; ys[cou] =  minimalWeld; 	  xe[cou] = lastPoint*delta_x; ye[cou] =  minimalWeld;      col[cou] = 0x009999; cou++;
    xs[cou] = 0; ys[cou] = -minimalWeld; 	  xe[cou] = lastPoint*delta_x; ye[cou] = -minimalWeld;	   col[cou] = 0x009999; cou++;
    xs[cou] = 0; ys[cou] =  minimalConnector; xe[cou] = lastPoint*delta_x; ye[cou] =  minimalConnector; col[cou] = 0x00FFFF; cou++;
    xs[cou] = 0; ys[cou] = -minimalConnector; xe[cou] = lastPoint*delta_x; ye[cou] = -minimalConnector; col[cou] = 0x00FFFF; cou++;
#endif
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::calcAverageFactor(double* fw, int scale, double norma1)
{	double f_wlet_avrg = calcWletMeanValue(fw, -0.5, 0, 500);
	average_factor = f_wlet_avrg * norma1 / getWLetNorma2(scale);
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::fillNoiseArray(double *y, int data_length, int N, double Neff, double noiseFactor, double *outNoise)
{	findNoiseArray(y, outNoise, data_length, N); // external function from findNoise.cpp
	int i;
	for (i = 0; i < N; i++)
	{	// ��� ������, ��� sqrt(Neff) � ����� ������ ������ ��
		// �������������� ��������, ���� ������ ������ ������.
		outNoise[i] *= noiseFactor * 3 / sqrt(Neff);
	}
}
//------------------------------------------------------------------------------------------------------------
int InitialAnalysis::getLastPoint()
{   int lastPoint = findReflectogramLength(data, data_length) - 1;
	if (lastPoint < 0)
	{	lastPoint = 0;
    }
	//if(lastPoint + 10 < data_length) lastPoint += 10; �� �������, ����� ��� ������ ���� 
    return lastPoint;
}
//------------------------------------------------------------------------------------------------------------
// f- �������� �-���,
// f_wlet - �������-�����
void InitialAnalysis::performTransformationAndCenter(double* f, int begin, int end, double* f_wlet, int scale, double norma)
{	// transform
	performTransformationOnly(f, begin, end, f_wlet, scale, norma);
	centerWletImageOnly(f_wlet, scale, begin, end, norma);
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::centerWletImageOnly(double* f_wlet, int scale, int begin, int end, double norma1)
{   // shift (calcAverageFactor must be performed by now!)
	double f_wlet_avrg = average_factor * getWLetNorma2(scale) / norma1;
	for(int i=begin; i<end; i++)
    {	f_wlet[i] -= f_wlet_avrg;
    }
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performTransformationOnly(double* f, int begin, int end, double* f_wlet, int freq, double norma)
{
	//int len = end - begin;
	SineWavelet wavelet;
	wavelet.transform(freq, f, lastPoint, begin, end - 1, f_wlet + begin, norma);
}
//------------------------------------------------------------------------------------------------------------
// ��������� ������� �������� �������-������ 
double InitialAnalysis::calcWletMeanValue(double *fw, double from, double to, int columns)
{   //��������� ��������� ��������� � �������� [0; -0.5] ��
	Histogramm* histo = new Histogramm(from, to, columns);
	histo->init(fw, 0, lastPoint-1);
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
	// � ������� ���������� ( ������ ����) �������������� ������ (�������, ��� ����� ������ ����� ������ ��� )
	for(int n=1; n<events->getLength(); n++)
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
        EventParams* ev2 = (EventParams*)(*events)[n-1];  // �� �����, ��� "for(int n=1...."
        // ������� ������ ���� ������� "���������"
        if(fabs(ev2->end - ev1_beg_old)<2){ ev2->end = ev1->begin; }// ������ <2, ����� ���� ������ ��������� �� ���������, �� � ������-�� ��� �� ������ ...  

    }
}
//------------------------------------------------------------------------------------------------------------
// �������� ��������������� ��������-������ ��� ��������� ��������� ������
void InitialAnalysis::correctAllSpliceCoords()
{	for(int n=1; n<events->getLength(); n++)
    {	EventParams* ev = (EventParams*)(*events)[n];
    	if(ev->type == EventParams::GAIN || ev->type == EventParams::LOSS)
        {	correctSpliceCoords(n);
        }
    }
}
//------------------------------------------------------------------------------------------------------------
// �-� ������ ������� ����� !  (��� ��� ���������� ��� �� ������ ��� �������� ������ �� ������ ��������)
// ��������� ����� ������ ������ ������, �� ����� �� ���������
void InitialAnalysis::correctSpliceCoords(int n)
{	EventParams* ev_lp = (EventParams*)(*events)[n];
    EventParams& ev = *ev_lp;
	// ���� ��� �� ������, �� �����
    if( !(ev.type == EventParams::GAIN || ev.type == EventParams::LOSS) )
return;
	//prf_b("correctSpliceCoords: enter");
	const double noise_factor = 0;  // 0 , ���� �� �� ��������� ��� � �������� �������
    const double angle_factor = 1.8; // ���������� ��������� ������ ��� ������ �� �������������� ����� �� ������� ���������
	const double factor = 1.2; // ��������� �������������� ����������
	const int nscale = 20; // ���������� ������ ���������
	int width = wlet_width; // frame-width: ������ ���� (������������ ������� �������), � ������� �� �������� �������������� ������
	int w_l = ev.begin, w_r = ev.end; // w_l - wavelet_left: ������� �������-������ �� ������� ��������
    int left_cross = (int )(w_l+width*angle_factor), right_cross = (int )(w_r-width*angle_factor); // ����� ����������� �������� ������� ��� �� (�� ���� ������������ ����������� ��������, ��� ������� ��� ���������, ������ ��� ����� X, ��� ������� ������ �����, ������������ ��������)

	int i;
#ifdef debug_lines
    int coln=-1,a=0xFFFFFF,b=0x0000FF,c=0x00FF00,d=0xFF7733,color[]={a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d,a,b,c,d};
#endif
    // ����������� ��� ������� ���������
	for(int step=0; step<=nscale; step++)
    {   width = (int)(width/factor);//(int )(wlet_width/pow(factor,step) +0.5);// ����� �� ����������� ������
    	if(width<=1)
    break;
		//prf_b("correctSpliceCoords: getWLetNorma");
	    wn = getWLetNorma(width);
		//prf_b("correctSpliceCoords: performTransformationAndCenter");
#ifdef debug_lines
		performTransformationAndCenter(data, ev.begin, ev.end+1, f_wlet, width, wn);
#else
		performTransformationAndCenter(data, w_l, w_r+1, f_wlet, width, wn);
#endif
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
        	{	w_l=i-1;//w_l=i;
	            if(w_l+width*angle_factor<left_cross){ left_cross = (int )(w_l+width*angle_factor);}
        break;
            }
        }
   		// ���� ����������� ������
        for(int j=w_r; j>w_l && j-width*angle_factor>=right_cross; j--) // j-width>=right_cross - ������� �������� � ��������� �� 45 ��
        {	if(fabs(f_wlet[j])>=minimalWeld+noise[j]*noise_factor+df_right)
        	{	w_r=j+1;//w_r=j;
	            if(w_r-width*angle_factor>right_cross)
                { right_cross = (int )(w_r-width*angle_factor);}
        break;
            }
        }
#ifdef debug_lines //������ ������� ������ ��� ������� ��������
		{ coln++;
          for(int i=ev.begin; i<ev.end; i++)
          { double x1=i, y1=f_wlet[i], x2=i+1, y2=f_wlet[i+1];
            xs[cou]=x1*delta_x; ys[cou]=y1; xe[cou]=x2*delta_x; ye[cou]=y2;
            col[cou]=color[coln];
            if(i<w_l || i>w_r) col[cou] = 0x888888;
            cou++;
          }
        }
#endif
    }
	//prf_b("correctSpliceCoords: scales done");
	if( w_l < w_r  )
    {   double old_left = ev.begin;
        double old_right = ev.end;
		// ����� ������ ������ �������
    	if(w_l>old_left && w_l<old_right ) { ev.begin = w_l;}
    	if(w_r<old_right && w_r>old_left)  { ev.end = w_r;}
    }
#ifdef debug_VCL
    wn = getWLetNorma(wlet_width);
  	performTransformationAndCenter(data, 0, lastPoint, f_wlet, wlet_width, wn);
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
// ������� ��� ������� ����� ���������� �������������� � ������������� ������������� � "����� �������"
void InitialAnalysis::processEndOfTrace()
{   for(int i=events->getLength()-1; i>0; i--)
	{   EventParams* ev = (EventParams*)(*events)[i];
    	if( !ev->can_be_endoftrace)  // true  ����� ���� ������ � �������������� �������
        {   events->slowRemove(i);
        }
        else
     	{	ev->type = EventParams::ENDOFTRACE;
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
// ��-�� ���������� ��������� ������� ���� ������� ��������� ���� �� �����, ����������� ��
void InitialAnalysis::trimAllEvents()
{	int prevEnd = 0;
	for(int i=0; i<events->getLength(); i++)
    {	EventParams& ev = *(EventParams*)(*events)[i];
		if(ev.begin < 0)
        { ev.begin = 0;
        }
		if(ev.begin != prevEnd && i != 0)
        {   if( fabs(ev.begin - prevEnd) > 2)
        	{   bool const GAP_BETWEEN_EVENTS_NOT_TOO_LARGE = false;
            	assert(GAP_BETWEEN_EVENTS_NOT_TOO_LARGE);
            }
        	ev.begin = prevEnd;
        }
		if(ev.end <= ev.begin)
        { ev.end = ev.begin+1;
        }
		if(ev.end > lastPoint)
        { ev.end = lastPoint;
        }
		prevEnd = ev.end;
    }
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::verifyResults()
{	int prevEnd = 0;
	for(int i=0; i<events->getLength(); i++)
    {	EventParams& ev = *(EventParams*)(*events)[i];
//*//#ifndef debug_VCL
		assert(ev.begin >= 0);
		assert(ev.end <= lastPoint);
		assert(ev.end >= ev.begin);
		assert(ev.end > ev.begin); // >, not just >=
		assert(ev.begin == prevEnd || i == 0); // XXX
//#else
/*/
//���� ���������� �����������
		if(!(ev.begin >= 0))
        { int o=0;}
		if(!(ev.end <= lastPoint))
        { int o=0;}
		if(!(ev.end >= ev.begin))
        { int o=0;}
		if(!(ev.end > ev.begin)) // >, not just >=
        { int o=0;}
		if(!(ev.begin == prevEnd || i == 0)) // XXX
        { int o=0;}
//*/
//#endif
		prevEnd = ev.end;

    }
}
