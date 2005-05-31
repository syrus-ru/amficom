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
	int nonReflectiveSize,		//����������� ����� ���������������� �������
	double rACrit,				// ����� "��������" ����������
	int rSSmall,				// ����. ����� ��� ���������� ����������
	int rSBig,					// ����. ����� ��� �������� ����������
	int lengthTillZero,			//����������� ������� ����� ( ==0 -> ����� �����)
	double *externalNoise)		//����������� ������� ��� ( ==0 -> ���� ����)
{
#ifdef DEBUG_INITIAL_ANALYSIS
	logf = fopen(DEBUG_INITIAL_WIN_LOGF, "a");
	assert(logf);
	fprintf(logf, "=== IA invoked\n"
		"len %d deltaX %g minTh %g minWeld %g minConn %g minEnd %g noiseFactor %g\n",
		data_length, delta_x, minimalThreshold, minimalWeld, minimalConnector, minimalEnd, noiseFactor);
	fprintf(logf, "rSBig %d rSSmall %d nRefSize %d lTZ %d extNoise %s\n",
		rSBig, rSSmall, nonReflectiveSize, lengthTillZero, externalNoise ? "present" : "absent");
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
    this->noiseFactor			= noiseFactor;
	this->data_length			= data_length;
	this->data					= data;
    this->rACrit 				= rACrit;
    this->rSSmall				= rSSmall;
    this->rSBig					= rSBig;
    this->scaleB				= nonReflectiveSize;

    events = new ArrList();

	if (lengthTillZero <= 0){
		lastPoint = getLastPoint();
    }
	else{
		lastPoint = lengthTillZero - 1;
    }
	f_wletB	= new double[lastPoint];
	noise	= new double[lastPoint];

#ifdef debug_VCL
	f_tmp   = new double[lastPoint];
	type	= new double[data_length];
#endif

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
{
#ifdef debug_VCL
	delete[] type;
#endif
	delete[] noise;
    delete[] f_wletB;

    events->disposeAll();

    delete events;
#ifdef DEBUG_INITIAL_ANALYSIS
	fclose(logf);
#endif
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::performAnalysis()
{	// ======= ������ ���� ������� - ���������� =======

	// ��������� �������-�������������� �� ��������� ��������, ���������� ������, ������� �������-�����
	// f_wlet - �������-����� �������, wlet_width - ������ ��������, wn - ����� ��������
    double wn = getWLetNorma(scaleB);
    performTransformationOnly(data, 0, lastPoint, f_wletB, scaleB, wn);
	calcAverageFactor(f_wletB, scaleB, wn);
	centerWletImageOnly(f_wletB, scaleB, 0, lastPoint, wn);// �������� �� ������������� ��������������(��) ���������� ������������

	// ������������ ������ �� ������ �������� ������� � ���������� �������� ��������
    shiftThresholds();// �������� ������ 

#if 0
	{	FILE *f = fopen ("noise2.tmp", "w");assert(f);
		int i;
		for (i = 0; i < lastPoint; i++)
			fprintf(f,"%d %g %g %g\n", i, data[i], f_wletB[i], noise[i]);
		fclose(f);
	}
#endif

	{	// ======= ������ ���� ������� - ����������� ��������� =======
		// ���� ��� �������� �������-������.
		// �� ����� - �������-�����, ������, ���. �� ������ - ������� splash
		ArrList splashes; // ������� ������ ArrList
		findAllWletSplashes(f_wletB, scaleB, splashes); // ��������� ������ splashes ���������
		if(splashes.getLength() == 0){
return;}
		// ======= ������ ���� ������� - ����������� ������� �� ��������� =======
		findEventsBySplashes(f_wletB, scaleB, splashes); // �� ���������� ��������� ���������� ������� (�� ���� - ������������� ���������)
		// ���������� ArrList � ��� �������
		splashes.disposeAll(); // ������� ������ ArrList
    } // ������� ������ ������ splashes

	// ====== ��������� ���� ������� - ��������� ������� =======
    processEndOfTrace();// ���� �� ������ ���������� �� ����� �������, �� �������� ��� �������
    excludeShortLinesBetweenConnectors(data, scaleB);
    addLinearPartsBetweenEvents();
	trimAllEvents(); // ��������� �� ������������ ���������� �� ���� ����� ����� � ������ �������, �� ��� ����� ��������� ���� �� ����� �� ���� ����� - ��� ���������, �� �� �� ������������ ��� ������� � ����������� ������ ��������� � ��� 
	verifyResults(); // ��������� ������
}
// -------------------------------------------------------------------------------------------------
//
// ======= ������� ������� ����� ������� - ���������� =======
//
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::calcAverageFactor(double* fw, int scale, double norma1)
{	double f_wlet_avrg = calcWletMeanValue(fw, -0.5, 0, 500);
	average_factor = f_wlet_avrg * norma1 / getWLetNorma2(scale);
}
//------------------------------------------------------------------------------------------------------------
// ��������� ������� �������� �������-������
double InitialAnalysis::calcWletMeanValue(double *fw, double from, double to, int columns)
{   // ��������� ��������� ��������� � �������� [0; -0.5] ��
	Histogramm* histo = new Histogramm(from, to, columns);
	histo->init(fw, 0, lastPoint-1);
	double mean_att = histo->getMaximumValue();
	delete histo;
	return mean_att;
}
//------------------------------------------------------------------------------------------------------------
// �������� ������ � ������������ � ������������ ���� � �������� ������������� ��������
void InitialAnalysis::shiftThresholds()
{   double f_wlet_avrg = average_factor * getWLetNorma2(scaleB) / getWLetNorma(scaleB); // ������� ������
	double thres_factor = 0.2;// ������� ������� ������ ������� �� ����� �������
    minimalThreshold += fabs(f_wlet_avrg)*thres_factor;
    if(minimalThreshold > 0.9*minimalWeld)
    {  minimalThreshold = 0.9*minimalWeld;
    }
	//minimalWeld += fabs(f_wlet_avrg)*thres_factor;
}
// -------------------------------------------------------------------------------------------------
//
// ======= ������� ������� ����� ������� - ����������� ��������� =======
//
// -------------------------------------------------------------------------------------------------
// �����: �������, ��� minimalThreshold < minimalWeld < minimalConnector
void InitialAnalysis::findAllWletSplashes(double* f_wlet, int wlet_width, ArrList& splashes)
{   //minimalThreshold,	//����������� ������� �������
	//minimalWeld,		//����������� ������� ���������������� �������
	//minimalConnector,	//����������� ������� �������������� �������
	double minimal_threshold_noise_factor = 0.4;  // XXX - ���� �� ��� ������� ��������
    for(int i=1; i<lastPoint; i++)// 1 �.�. i-1
    {   if( fabs(f_wlet[i])<=calcThresh(minimalThreshold,noise[i]*minimal_threshold_noise_factor) )
     continue;
		Splash& spl = (Splash&)(*(new Splash(wlet_width)));// ��� �� ��������� ���� �� ���� �����, �� ������ ��� ������ ���� ������;
        spl.begin_thr = i;
        spl.f_extr = f_wlet[i];
		int sign, sign_cur;
		sign = xsign(f_wlet[i]);
        for(  ; i<lastPoint-1; i++)
        {   sign_cur = xsign(f_wlet[i]);
        	// ����������� �� ����
        	if( fabs(f_wlet[i])>= calcThresh(minimalThreshold,noise[i]*minimal_threshold_noise_factor) && spl.begin_thr == -1){
				spl.begin_thr = i-1;
            }
            if( fabs(f_wlet[i])>= calcThresh(minimalWeld, noise[i]) && spl.begin_weld == -1)
            {	spl.begin_weld = i-1;
            }
            //if( spl.begin_weld_n != -1 && spl.end_weld_n == -1 && (fabs(f_wlet[i])<= calcThresh(minimalThreshold, noise[i]*noise_factor) || sign_cur!=sign || i==lastPoint-1) )
            if( fabs(f_wlet[i])>calcThresh(minimalWeld, noise[i]) && sign_cur==sign)
            {	spl.end_weld = i+1;
            }
            if( fabs(f_wlet[i])>= calcThresh(minimalConnector, noise[i]) && spl.begin_conn == -1)
            {	spl.begin_conn= i-1;
            }
            //if( spl.begin_conn_n != -1 && spl.end_conn_n == -1 && (fabs(f_wlet[i])<= calcThresh(minimalConnector,noise[i]) || sign_cur!=sign || i==lastPoint-1))
            if( fabs(f_wlet[i]) > calcThresh(minimalConnector,noise[i]) && sign_cur==sign)
            {	spl.end_conn = i+1;
            }
			// ����������� �� ����
            if( fabs(f_wlet[i])<=calcThresh(minimalThreshold,noise[i]*minimal_threshold_noise_factor) || sign_cur!=sign )
        	{	spl.end_thr = i;
     	break;
     		}
            if(fabs(spl.f_extr)<fabs(f_wlet[i])){ spl.f_extr = f_wlet[i];}
        }
		spl.end_thr = i; // �� ������ ������ �� ����� �� �� break � �� ������� � for   
		spl.sign = sign;
		fillSplashRParameters(spl, f_wlet, wlet_width);
        if( spl.begin_thr < spl.end_thr // begin>end ������ ���� ����� ��� � �� ������ �� ���� ������� �����
	        && spl.begin_weld != -1 // !!!  ��������� ������ ������������ �������� ( ���� ��� �������� ������, �� ������������� ����������� ���� ��������, ��� ��� ���� ����� ����� ������������� ���������� ����� �������� ��������� ���������� ������� �����, �� ���������� ��������� �� ����� ! )
           )
        {  splashes.add(&spl);
#ifdef debug_lines
           double begin = spl.begin_thr, end = spl.end_thr, s = spl.sign;
           xs[cou] = begin*delta_x; xe[cou] = begin*delta_x; ys[cou] = -minimalConnector*2*0.9; ye[cou] = minimalConnector*2*1.1; cou++;
           xs[cou] = end*delta_x; xe[cou] = end*delta_x; ys[cou] = -minimalConnector*2*1.1; 	ye[cou] = minimalConnector*2*0.9; col[cou]= 0xFFFFFF; cou++;
           double c = spl.begin_conn ==-1 ? 0xFF0000 : 0x0000FF;
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
    {   Splash* spl = new Splash(wlet_width);
    	spl->begin_thr 		= lastPoint+1; spl->begin_weld 	= lastPoint+1; spl->begin_conn 	= lastPoint+1;
        spl->end_thr 		= lastPoint+2; spl->end_weld 	= lastPoint+2; spl->end_conn 	= lastPoint+2;
		spl->sign			= -1;
		fillSplashRParameters(*spl, f_wlet, wlet_width);
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
//
// ======= ������� �������� ����� ������� - ����������� ������� �� ��������� =======
//
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::findEventsBySplashes(double *f_wlet, int wlet_width, ArrList& splashes)
{//* ������ ���� ����  ���� �����
    int shift = 0;
    if( splashes.getLength() <=2 )
return;
	Splash* sp1 = (Splash*)splashes[0];
    Splash* sp2;
    shift = processDeadZone(splashes);// ���� ������ ����
	// ���� ��������� ����������  � ������
    for(int i = shift+1; i<splashes.getLength()-1; i++)
    { int len = processIfIsConnector(f_wlet, wlet_width, i, splashes);
      if(len != -1)// ���� ��������� ��� ������
      { i+= len;
    continue;
      }
      sp1 = (Splash*)splashes[i];
      sp2 = (Splash*)splashes[i+1];
      int dist = abs(sp2->begin_weld - sp1->end_weld);
      // ��� ������ "+" � "-" ����� ������
      if( dist<rSSmall			// ���� �������� ����� ������
          && (sp1->sign>0 && sp2->sign<0) // ������ �������������, � ������ - �������������
          && ( sp1->begin_weld != -1 && sp2->begin_weld != -1)// � ��� ���� ��� ������� ���������
        )
      {   EventParams *ep = new EventParams;
          setUnrecognizedParamsBySplashes((EventParams&)*ep, (Splash&)*sp1, (Splash&)*sp2 );
          events->add(ep);
          i++;// ������ ��� ������� �� ���� ���������
	continue;
      }
      // ������
      if( sp1->begin_weld!= -1 && fabs(sp1->end_weld-sp1->begin_weld)>1) //������
      {	EventParams *ep = new EventParams;
        setSpliceParamsBySplash((EventParams&)*ep, (Splash&)*sp1 );
        correctSpliceCoords(f_wlet, wlet_width, ep);
        events->add(ep);
	continue;
      }
    }
}
//-------------------------------------------------------------------------------------------------
int InitialAnalysis::processDeadZone(ArrList& splashes)
{   int i, shift = 0;
	int begin = 0, end = -1;
    double f_max = 0, f_min = 0;
	Splash* sp1 = (Splash*)splashes[0];
    Splash* sp2 = (Splash*)splashes[1];
    if(sp1->sign<0) // ���� ������ ����� ����, �� �������, ��� ��� ���������� � ����
    { begin = 0;
      end  = sp1->end_thr;
    }
    else // ����� �� ���������� rSBig �� ������ ���� ������������ ������� ����� � ��c�� ���� ����������� ������� ����
    { for(i = 0; sp2->begin_thr<rSBig && i+1<splashes.getLength(); i++)
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
    if(end == -1)// ���� �� ����� ��������� �����-���� � �������� rSBig, �� ���� ������ ����
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
// ��������, ������� �������� i � ������ �����; ���� ������ �� �����, �� ����� ����� -1.
// � ������ ������ �������������� ���:
// ���� �� ��������� �� ����� ������� ��������� ������������ ������� ����� � � �������� reflSize ���������
// ������������ ����, �� �� � ������. ���� ������������� ���, �� ���� ���� �� ��������� ����, �� � ��������
// ���������� ������ ����� ������� ( � �������� reflSize ) ���������.
int InitialAnalysis::processIfIsConnector(double* f_wlet, int wlet_width, int i, ArrList& splashes)
{   int shift = -1;
    Splash* sp1 = (Splash*)splashes[i];
    Splash* sp2, *sp_tmp;
    // ���� ���������� � �������� �������� �����, �� ����, ��� �� ������ ����
    if(sp1->begin_conn !=-1 && sp1->sign>0)
    { // ���� �� ����� ���������� ������������ �������, �� ��� ������ ������ �� ���������
      for(int j=i+1; j<splashes.getLength(); j++)
      { sp2 = (Splash*)splashes[j];
      	if(sp2->begin_conn == -1){// ���� ������ ������������ ����
      continue;}
        // ���� �������� ������ ���� �� ����� , �� ��� �� ���������
        double dist = fabs(sp2->begin_weld - sp1->end_conn);
        if(dist > rSBig){
      break;}
        if(sp2->begin_conn!=-1 && sp2->sign<0 && dist<=rSSmall ) // ���� ����� ������� ����, �� ������ ��������� �����������
        { shift = j-i;
      break;
        }
        if(fabs(sp1->f_extr)>=rACrit && dist<=rSBig && sp2->begin_conn!=-1 && sp2->sign<0 ) // ���� �������� ������, �� ��� ����� �������
        { shift = j-i;
      break;
        }
        if(sp2->begin_conn!=-1 && sp2->sign>0)// ���� ����� ������������ �����, �� ������ ������� ������ ������ ����������
        { shift = j-i;
          shift--;// ����� ���� �� ������� ��� ������� ���������� ����������
    break;
        }
      }
      // ���� ������������ �� ���� �� ����� ��� � �� ����, �� ���� ��������� ���������
      if(shift==-1)
      { for(int j=i+1; j<splashes.getLength(); j++)
        { sp_tmp = (Splash*)splashes[j];
		  if(sp_tmp->begin_weld == -1){
        continue;}
		  double dist = fabs(sp_tmp->begin_weld - sp1->end_conn);
          if(dist > rSBig){
        break;}
          if(fabs(sp1->f_extr)>=rACrit && dist<rSBig && sp_tmp->begin_weld!=-1 && sp_tmp->sign<0)// ���� ��������� ��������� ���� �� ������� rsBig
          { shift = j-i;
            sp2 = (Splash*)splashes[i+shift];
        continue;
          }
          else if(fabs(sp1->f_extr)<rACrit && dist<rSSmall && sp_tmp->begin_weld!=-1 && sp_tmp->sign<0)// ���� ��������� ��������� ���� �� ������� rSSmall
          { shift = j-i;
            sp2 = (Splash*)splashes[i+shift];
        continue;
          }
        }
      }
      //  ���� ���� ����� ���������, �� ��������� ��� � �������
      if(shift!=-1 )
      { EventParams *ep = new EventParams;
        setConnectorParamsBySplashes(wlet_width, (EventParams&)*ep, (Splash&)*sp1, (Splash&)*sp2 );
        correctConnectorFront(ep); // �������� ����� ���������
        events->add(ep);
#ifdef debug_lines
		double begin = ep->begin, end = ep->end;
        xs[cou] = begin*delta_x; xe[cou] = end*delta_x; ys[cou] = minimalConnector*2*1.1;  ye[cou] = minimalConnector*2*1.5;  col[cou]=0x00FFFF; cou++;
#endif
      }
    }
    return shift;//���� ���������� �� �����, �� shift = -1
}
// -------------------------------------------------------------------------------------------------
// � ����� ������ �� ������������, ��� ������ f_wlet ������� ��� �������� ��� ������, �� ������� ������ ���� ����������
void InitialAnalysis::setSpliceParamsBySplash(EventParams& ep, Splash& sp)
{   if(sp.sign>0) { ep.type = EventParams::GAIN; }
    else 		  { ep.type = EventParams::LOSS; }

    ep.begin = sp.begin_thr; // sp.begin_weld; - thr - � ������� �� ��, ��� ����� �������
    ep.end = sp.end_thr; // sp.end_weld+1;
    if(ep.end>lastPoint){ep.end = lastPoint;}
    if(ep.begin<0){ep.begin=0;}

    //// remove this
	//double max = -1;
	//for(int i=sp.begin_weld; i<sp.end_weld; i++)
    //{ double res = (fabs(f_wlet[i])-minimalWeld)/noise[i];
    //  if(max<res){ max = res;}
    //}
    //ep.R = max;
	ep.R = sp.r_weld;
}
//------------------------------------------------------------------------------------------------------------
// �������� ��������������� ��������-������ ��� ��������� ��������� ������
// �-� ������ f_wlet - ������� ����� !  (��� ��� ���������� ��� �� ������ ��� �������� ������ �� ������ ��������)
// ��������� ����� ������ ������ ������, �� ����� �� ���������
void InitialAnalysis::correctSpliceCoords(double* f_wlet /*TEMP space*/, int wlet_width, EventParams* splice)
{   EventParams& ev = *splice;
	// ���� ��� �� ������, �� �����
    if( !(ev.type == EventParams::GAIN || ev.type == EventParams::LOSS) )
return;
	//prf_b("correctSpliceCoords: enter");
	const double level_factor = 0.1; // ������� �� ��������� ������� , ��� ������ �������, ��� ������ ����
	const double noise_factor = 0.5;  // 0 , ���� �� �� ��������� ��� � �������� �������
    const double angle_factor = 1.5; // ���������� ��������� ������ ��� ������ �� �������������� ����� �� ������� ���������
	const double factor = 1.2; // ��������� �������������� ����������
	const int nscale = 20; // ���������� ������ ���������
	int width = wlet_width; // frame-width: ������ ���� (������������ ������� �������), � ������� �� �������� �������������� ������
	int w_l = ev.begin, w_r = ev.end; // w_l - wavelet_left: ������� �������-������ �� ������� ��������
    int left_cross = (int)(w_l+width*angle_factor), right_cross = (int)(w_r-width*angle_factor); // ����� ����������� �������� ������� ��� �� (�� ���� ������������ ����������� ��������, ��� ������� ��� ���������, ������ ��� ����� X, ��� ������� ������ �����, ������������ ��������)

	int i;
#ifdef debug_lines
    int coln=-1,color[]={0xFFFFFF,0x0000FF,0x00FF00,0xFF7733};
    int csz = sizeof(color)/sizeof(int);
#endif
    bool w_lr_ch = false; // ������ !!! ��� ������ !!! ���������� ������� ������ �� ��� ���������, �� ������� ������� ����������

    // ����������� ��� ������� ���������
	for(int step=0; step<=nscale; step++)
    {   width = (int)(width/factor);//(int )(wlet_width/pow(factor,step) +0.5);// ����� �� ����������� ������
    	if(width<=1)
    break;
	    double wn = getWLetNorma(width);
		performTransformationAndCenter(data, w_l, w_r+1, f_wlet, width, wn);
		// ������� ���� ��������� ���������� ��� ������ ��������
        int i_max = w_l;
        double f_max = f_wlet[i_max];
        for(i=w_l; i<w_r; i++)
        {	if( fabs(f_wlet[i])>fabs(f_max) ) // ��������� ����� � �������� ������ ������� ��������������, �� ����� �������� � ��������
        	{	i_max = i; f_max = f_wlet[i_max];
            }
        }
        // ������� ������� � ���� ( ������� �������������� �� ����������� ������ �� ��������� )
		double f_lmax = f_wlet[w_l], df_left = 0, df_right = 0;// df - ������� ���������� �� ������������ ������� (����� ����������� ������ �-� ���� ����������, ��� ������ ����� ��������� ��� � ���������� )
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
        {	//if(fabs(f_wlet[i])>= minimalThreshold+noise[i]*noise_factor+df_left)
	        if(fabs(f_wlet[i]) >= level_factor*fabs(f_max) && fabs(f_wlet[i]) > fabs(df_left)/(0.5*level_factor) )// &&... - ������ ������ ��������� ���� ��� ( �� ��� ��������� ������� �������������� )
        	{	w_l=i-1;//w_l=i;
            	w_lr_ch = true;
	            if(w_l+width*angle_factor<left_cross){ left_cross = (int)(w_l+width*angle_factor);}
        break;
            }
        }
   		// ���� ����������� ������
        for(int j=w_r; j>w_l && j-width*angle_factor>=right_cross; j--) // j-width>=right_cross - ������� �������� � ��������� �� 45 ��
        {	//if(fabs(f_wlet[j])>=minimalThreshold+noise[j]*noise_factor+df_right)
            if(fabs(f_wlet[j])>= level_factor*fabs(f_max) && fabs(f_wlet[i]) > fabs(df_right)/(0.5*level_factor) )// &&... - ������ ������ ��������� ���� ��� ( �� ��� ��������� ������� �������������� )
        	{	w_r=j+1;//w_r=j;
                w_lr_ch = true;
	            if(w_r-width*angle_factor>right_cross)
                { right_cross = (int)(w_r-width*angle_factor);}
        break;
            }
        }
#ifdef debug_lines //������ ������� ������ ��� ������� ��������
		{ coln++;
          for(int i=ev.begin; i<ev.end; i++)
          { double x1=i, y1=f_wlet[i], x2=i+1, y2=f_wlet[i+1];
            xs[cou]=x1*delta_x; ys[cou]=y1; xe[cou]=x2*delta_x; ye[cou]=y2;
            col[cou]=color[coln%csz];
            if(i<w_l || i>w_r || !w_lr_ch) col[cou] = 0x888888;
            cou++;
          }
        }
#endif
		w_lr_ch = false;
    }  // for(int step=0; step<=nscale;...
	//prf_b("correctSpliceCoords: scales done");
	if( w_l < w_r  )
    {   double old_left = ev.begin;
        double old_right = ev.end;
		// ����� ������ ������ �������
    	if(w_l>old_left && w_l<old_right ) { ev.begin = w_l;}
    	if(w_r<old_right && w_r>old_left)  { ev.end = w_r;}
    }
#ifdef debug_VCL
    double wn = getWLetNorma(wlet_width);
  	performTransformationAndCenter(data, 0, lastPoint, f_wlet, wlet_width, wn);
#endif
	//prf_b("correctSpliceCoords: return");
}
// -------------------------------------------------------------------------------------------------
// ���������� f_wlet (�� ����� ��������?)
void InitialAnalysis::setConnectorParamsBySplashes(int wlet_width, EventParams& ep, Splash& sp1, Splash& sp2 )
{   double r1s, r1b, r2, r3s, r3b, rmin;
    ep.type = EventParams::CONNECTOR;
    ep.begin = sp1.begin_thr;
    if(ep.begin<0){ep.begin=0;}
    ep.end = sp2.end_thr;
    if(sp2.begin_conn != -1 && sp2.sign > 0)// ���� ��� ������ ������ ����������
    { ep.end = sp2.begin_thr;// ���� ��� ���������� �����, �� ����� ������� ��������� � ������� ����������
    }
    if(ep.end>lastPoint)
    { ep.end = lastPoint;
    }
	//// remove this
    //double max1 = -1, max2 = -1, max3 = -1;
    //int i;
    //for(i=sp1.begin_conn ; i<sp1.end_conn; i++)
    //{ double res = (f_wlet[i]-minimalConnector)/noise[i]; // WONDER: ����� f_wlet - �� �������� ��������?
    //  if(max1<res) { max1 = res;}
    //  res = (f_wlet[i]-rACrit)/noise[i];
    //  if(max2<res) { max2 = res;}
    //  res = (f_wlet[i]-minimalWeld)/noise[i];
    //  if(max3<res) {max3 = res;}
    //}
    //r1s = max1;
    //r1b = max2;
    //r2  = max3;

	r1s = sp1.r_conn;
	r1b = sp1.r_acrit;
	r2  = sp1.r_weld;

    double l = sp2.begin_thr - sp1.end_conn;
    assert(l>=-1);// -1 ����� ���� ��� ��� �� ���������� ��������� �� ���� ����� ������ ������� (������ �� ������, � ����� ����� )
    r3s = r2*(rSSmall - l)/wlet_width;
    r3b = r2*(rSBig - l)/wlet_width;
    // ����� �� ���� "���������" ���� ������ �������
    if(sp1.sign>0 && sp1.f_extr>= minimalEnd)
    { ep.can_be_endoftrace = true;
    }
	double t1 = r1s<r3b ? r1s:r3b, t2 = r3s>r1b ? r3s:r1b;
    double t3 = r2<t2 ? r2:t2;
	rmin = t1<t3 ? t1:t3;
    ep.R = rmin;
}
//------------------------------------------------------------------------------------------------------------
void InitialAnalysis::correctConnectorFront(EventParams* ev)
{	if( ev->type != EventParams::CONNECTOR )// ���� �� ����� �� ����������
return;
    // ���� ����� �� ������ ���������� �����, ��� �� ����� �� �� - �����, � ������ - �� ������
    int i_begin = ev->begin, i_end = ev->end;
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
    double ev_beg_old = ev->begin;
    ev->begin = i_x - 1;
}
// -------------------------------------------------------------------------------------------------
void InitialAnalysis::setUnrecognizedParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2 )
{  ep.type = EventParams::UNRECOGNIZED;
   ep.begin = sp1.begin_thr;
   if(ep.begin<0){ep.begin=0;}
   ep.end = sp2.end_thr;
   if(ep.end>lastPoint){ep.end = lastPoint;}
}
// -------------------------------------------------------------------------------------------------
//
// ====== ������� ���������� ����� ������� - ��������� ������� =======
//
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
void InitialAnalysis::excludeShortLinesBetweenConnectors(double* arr, int szc)
{   if(events->getLength()<2)
return;
	for(int n1=0, n2, n3; n1<events->getLength(); n1++)
	{   // ���� �� ����� �� ����������
        EventParams* ev1 = (EventParams*)(*events)[n1];
    	if(ev1->type != EventParams::CONNECTOR)
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
// -------------------------------------------------------------------------------------------------
//
// ====== ����� � ��������������� ������� ������� =======
//
// -------------------------------------------------------------------------------------------------
// ����� �� ������ ���� ����, ����� ������ �������� ��������� ������� ������� � ��������� ��������
double InitialAnalysis::calcThresh(double thres, double noise)
{	return thres>noise ? thres : noise;// = max(thres, noise)
	//return thres+noise;
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
{	//int len = end - begin;
	SineWavelet wavelet;
	wavelet.transform(freq, f, lastPoint, begin, end - 1, f_wlet + begin, norma);
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
// ������ ���������� ����� ���������� ��������� ���������� splash (begin/end)
void InitialAnalysis::fillSplashRParameters(Splash &spl, double *f_wlet, int wlet_width)
{
	int sign = spl.sign;
	// ��� ������������ �������
	spl.r_acrit = spl.r_conn = spl.r_weld = -1;
    int i;
    for(i=spl.begin_conn; i<spl.end_conn; i++) // ���� begin_conn � end_conn �� ����������, �� ������ �� ������
    { double res;
	  res = (f_wlet[i]*sign-minimalConnector)/noise[i];
      if(spl.r_conn<res) { spl.r_conn = res;}
      res = (f_wlet[i]*sign-rACrit)/noise[i];
      if(spl.r_acrit<res) { spl.r_acrit = res;}
    }
	// ��� ��������� �������
    for(i=spl.begin_weld; i<spl.end_weld; i++) // ���� begin_conn � end_conn �� ����������, �� ������ �� ������
    { double res;
      res = (f_wlet[i]*sign-minimalWeld)/noise[i];
      if(spl.r_weld<res) {spl.r_weld = res;}
    }
}
