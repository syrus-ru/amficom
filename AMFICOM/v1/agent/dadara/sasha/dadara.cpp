#include "dadara.h"
//#include "com_syrus_AMFICOM_analysis_AnalysisManager.h"
#include "com_syrus_AMFICOM_analysis_dadara_DadaraAnalysisManager.h"
#include "InitialAnalysis.h"
#include "stdio.h"
#include <math.h>



JNIEXPORT jdoubleArray JNICALL 
//Java_com_syrus_AMFICOM_analysis_AnalysisManager_analyse
Java_com_syrus_AMFICOM_analysis_dadara_DadaraAnalysisManager_ana
                     (JNIEnv* env, jobject obj, 
					  jint waveletType,             //type of the WaveLet transformation applied.
					  jdoubleArray y,               //the refl. itself
					  jdouble delta_x,              //dx
		              		jint  findEnd_,              //char. event size
					  jdouble connFallParams,       // Param. to descr. the behav. of conn. at fall
					  jdouble min_level,            // ?
					  jdouble max_level_noise,      // ?
					  jdouble min_level_to_find_end,// ? 
					  jdouble min_weld,             // ?
					  jdouble min_connector,
					  jint strategy) //set sratrategy  4 - smart fitting.
{ 


//Работа с Java Native Interface
	data    = (env)->GetDoubleArrayElements(y,NULL);
	jsize sz = (env)->GetArrayLength(y);	
	int data_l = sz;
	jdoubleArray ret;
	int i;
	int findEnd = findEnd_;
	chi2norma = 0.003;


	double arglist[100];
	double emptyParam1, emptyParam2, emptyParam3;

	
	if(min_level<0.01) min_level = 0.01;
	if(max_level_noise<0.05) max_level_noise = 0.05;
	if(max_level_noise>2.) max_level_noise = 2.;
	if(connFallParams>0.49) connFallParams = 0.49;
	if(connFallParams<0.001) connFallParams = 0.001;
	if(min_weld<0.01) min_weld = 0.01;
	if(min_connector<0.05) min_connector = 0.05;
	if(min_level_to_find_end<1.) min_level_to_find_end = 1.;


	//Beep(100, 100);
	for(i=0; i<100; i++) arglist[i] = 0.; // Creating of the argument list;


	

	InitialAnalysis *ia = new InitialAnalysis(data, data_l, 
											  delta_x, 
											  min_level, 
											  min_weld, 
											  min_connector, 
											  min_level_to_find_end,
											  max_level_noise,
											  waveletType,
											  connFallParams, 
											  findEnd);
	ep = ia->ep;
	sigmaSquared = ia->sigmaSquared;

	int nEvents = ia->numberOfFoundEvents;
	int eventSize = ia->eventSize;

	int l = ep[nEvents-1]->beginConnector+eventSize*3;


if(strategy>=0)	
{

	TFitter *linearFitter = new TFitter(2);
	linearFitter->SetFCN(fcnLinear);
	
	for(i=0; i<nEvents; i++)
	{
		nEvent = i;
		if(ep[i]->linearEvent == 1) // Linear fitting //	
		{
			 linearFitter->SetParameter(0, "a", 
										ep[i]->a_linear,
										0.1,
										ep[i]->a_linear-10., 
										ep[i]->a_linear+10.);
			 linearFitter->SetParameter(1, "k", 
										ep[i]->b_linear,
										0.01,
										-1., 
										0.);
			 arglist[0] = 1;
			 linearFitter->ExecuteCommand("CALL FCN", arglist, 1);
/*			 arglist[0] = -1;

//			 linearFitter->ExecuteCommand("SET PRINT", arglist, 1);
//			 arglist[0] = 0;

//			 linearFitter->ExecuteCommand("MINI", arglist, 0);
//			 linearFitter->ExecuteCommand("MINOS", arglist, 0);
//			 if(strategy > 1)
//			 {
//				linearFitter->ExecuteCommand("IMProve", arglist, 0);
//				linearFitter->ExecuteCommand("MINOS", arglist, 0); 
			 }
//			 arglist[0] = 3;
//			 linearFitter->ExecuteCommand("CALL FCN", arglist, 1);

			 linearFitter->GetErrors(0, emptyParam1, emptyParam2, 
									ep[i]->a_linearError, emptyParam3); 
									*/

			 linearFitter->GetErrors(1, emptyParam1, emptyParam2, 
	 							ep[i]->b_linearError, emptyParam3); 

		}
	}
	delete linearFitter;

	TFitter *weldFitter = new TFitter(5);
	weldFitter->SetFCN(fcnWeld);
	for(i=0; i<nEvents; i++)
	{
		nEvent = i;

		if(ep[i]->weldEvent == 1)
		{
			weldFitter->SetParameter(0, "boost of weld", 
									 ep[i]->boost_weld, 
									 0.02,
									 -6., 
									 6.);

			weldFitter->SetParameter(1, "width of weld", 
									 ep[i]->width_weld, 
									 1., 
									 1., 
									 (double)(ep[i]->endLinear - ep[i]->beginLinear));

			weldFitter->SetParameter(2, "centre of weld", 
									 ep[i]->center_weld, 
									 1., 
									 (double)(ep[i]->beginLinear), 
									 (double)(ep[i]->endLinear));

			weldFitter->SetParameter(3, "base amplitude of weld", 
									 ep[i]->a_weld, 
									 0.1, 
									 ep[i]->a_weld-3., 
									 ep[i]->a_weld+3.);

			weldFitter->SetParameter(4, "k",
									 ep[i]->b_weld, 
									 0.01,
									 -1., 
									 0.);

			arglist[0] = 1;
			weldFitter->ExecuteCommand("CALL FCN", arglist, 1);
			arglist[0] = -1;
			weldFitter->ExecuteCommand("SET PRINT", arglist, 1);
			arglist[0] = 0;


//			weldFitter->FixParameter(1);
//			weldFitter->FixParameter(2);




			weldFitter->ExecuteCommand("MINI", arglist, 0);

			 if(strategy > 1)
			 {
				weldFitter->ExecuteCommand("MINI", arglist, 0);
				weldFitter->ExecuteCommand("MINOS", arglist, 0);
			 }

			arglist[0] = 3;
			weldFitter->ExecuteCommand("CALL FCN", arglist, 1);

			weldFitter->GetErrors(0, emptyParam1, emptyParam2, ep[i]->boost_weldError, 
									 emptyParam3);
			weldFitter->GetErrors(1, emptyParam1, emptyParam2, ep[i]->width_weldError, 
									 emptyParam3);
			weldFitter->GetErrors(2, emptyParam1, emptyParam2, ep[i]->center_weldError, 
									 emptyParam3);
			weldFitter->GetErrors(3, emptyParam1, emptyParam2, ep[i]->a_weldError, 
									 emptyParam3);
			weldFitter->GetErrors(4, emptyParam1, emptyParam2, ep[i]->b_weldError, 
									 emptyParam3);
		}
	}
	delete weldFitter;

	TFitter *connectorFitter = new TFitter(9);
	connectorFitter->SetFCN(fcnConnector);
	for(i=0; i<nEvents; i++)
	{
		nEvent = i;
		if(ep[i]->connectorEvent == 1)
		{

			connectorFitter->SetParameter(0, "a1", 
				                          ep[i]->a1_connector, 
										  0.1, 
										  ep[i]->a1_connector-1., 
										  ep[i]->a1_connector+1.);

			connectorFitter->SetParameter(1, "a2", 
				                          ep[i]->a2_connector, 
										  0.1, 
										  ep[i]->a2_connector-1., 
										  ep[i]->a2_connector+1.);

			connectorFitter->SetParameter(2, "aLet", 
										  ep[i]->aLet_connector, 
										  0.1, 
										  min_level, 
										  ep[i]->aLet_connector+1.);

			connectorFitter->SetParameter(3, "w", 
										  ep[i]->width_connector, 
										  1., 
										  eventSize*.5, 
										  ep[i]->width_connector*2);

			connectorFitter->SetParameter(4, "s1", 
				                          ep[i]->sigma1_connector, 
										  1., 
										  0.01, 
										  ep[i]->width_connector);

			connectorFitter->SetParameter(5, "s2", 
				                          ep[i]->sigma2_connector, 
										  1., 
										  0.01, 
										  ep[i]->width_connector*5.);

			connectorFitter->SetParameter(6, "c", 
				                          ep[i]->center_connector, 
										  1.,
										  ((double)ep[i]->beginConnector), 
										  ((double)ep[i]->endConnector));

			connectorFitter->SetParameter(7, "k", 
				                          ep[i]->k_connector,  
										  0.01,
										  0., 
										  .5);

			connectorFitter->SetParameter(8, "stf", 
				                          ep[i]->sigmaFit_connector, 
										  1.,
										  0.01, 
										  ep[i]->width_connector*5.);
										
        arglist[0] = 1;
        connectorFitter->ExecuteCommand("CALL FCN", arglist, 1);
        arglist[0] = -1;
        connectorFitter->ExecuteCommand("SET PRINT", arglist, 1);
		arglist[0] = 0;


        connectorFitter->ExecuteCommand("MINI", arglist, 0);

		 if(strategy >=1)
		 {
			connectorFitter->ExecuteCommand("MIGrad", arglist, 0);
		 }

		 if(strategy >=2)
		 {
			connectorFitter->ExecuteCommand("IMProve", arglist, 0);
		 }

		 if(strategy >=3)
		 {
			connectorFitter->ExecuteCommand("MINOS", arglist, 0);
		 }

		 if(strategy >=4)
		 {
			 connectorFitter->ExecuteCommand("MINOS", arglist, 0);
		 }

        
		arglist[0] = 3;
        connectorFitter->ExecuteCommand("CALL FCN", arglist, 1);

		connectorFitter->GetErrors(0, emptyParam1, emptyParam2, 
			                          ep[i]->a1_connectorError, emptyParam3);
		connectorFitter->GetErrors(1, emptyParam1, emptyParam2, 
			                          ep[i]->a2_connectorError, emptyParam3);
		connectorFitter->GetErrors(2, emptyParam1, emptyParam2, 
			                          ep[i]->aLet_connectorError, emptyParam3);
		connectorFitter->GetErrors(3, emptyParam1, emptyParam2, 
			                          ep[i]->width_connectorError, emptyParam3);
		connectorFitter->GetErrors(4, emptyParam1, emptyParam2, 
			                          ep[i]->sigma1_connectorError, emptyParam3);
		connectorFitter->GetErrors(5, emptyParam1, emptyParam2, 
			                          ep[i]->sigma2_connectorError, emptyParam3);
		connectorFitter->GetErrors(6, emptyParam1, emptyParam2, 
			                          ep[i]->center_connectorError, emptyParam3);
		connectorFitter->GetErrors(7, emptyParam1, emptyParam2, 
			                          ep[i]->k_connectorError, emptyParam3);
		connectorFitter->GetErrors(8, emptyParam1, emptyParam2, 
			                          ep[i]->sigmaFit_connectorError, emptyParam3);

		}
	}
	delete connectorFitter;
}
	

	l = ep[nEvents-1]->beginConnector+eventSize*3;

	int Ret_Length;
	double *RET = setParRet(nEvents, ep, Ret_Length); 
	ret = (env)->NewDoubleArray(Ret_Length);

	delete ia;


//Освобождение массивов в Java Native Interface
	(env)->ReleaseDoubleArrayElements(y,data,JNI_ABORT);
	(env)->SetDoubleArrayRegion(ret,0,Ret_Length,RET);
//(env)->ReleaseDoubleArrayElements(ret,final_ret_struct,JNI_ABORT);

	return ret;
}



/*



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
double *setParRet(int n_events, EventParams **ep, int &Ret_Length)
{
	int i;
	int ret_size = n_events*44;
	int counter = 0;
	
    double *ret = new double[ret_size];

	Ret_Length = ret_size;

	EventParams *eVp;


	for(i=0; i<ret_size; i+=44)
	{
		eVp = ep[counter];
//linear
	     ret[i]   = eVp->a_linear;
		 ret[i+1] = eVp->a_linearError;
	     ret[i+2] = eVp->b_linear;
		 ret[i+3] = eVp->b_linearError;
	     ret[i+4] = eVp->beginLinear;
		 ret[i+5] = eVp->endLinear;
	     ret[i+6] = eVp->chi2Linear;
		 if(eVp->linearEvent == 1)
			ret[i+7] = 1.;
		 else
			ret[i+7] = 0.;
//weld
	     ret[i+8]  = eVp->a_weld;
		 ret[i+9]  = eVp->a_weldError;
	     ret[i+10] = eVp->b_weld;
		 ret[i+11] = eVp->b_weldError;
	     ret[i+12] = eVp->boost_weld;
		 ret[i+13] = eVp->boost_weldError;
	     ret[i+14] = eVp->center_weld;
		 ret[i+15] = eVp->center_weldError;
	     ret[i+16] = eVp->width_weld;
		 ret[i+17] = eVp->width_weldError;
	     ret[i+18] = eVp->beginWeld;
		 ret[i+19] = eVp->endWeld;
	     ret[i+20] = eVp->chi2Weld;
		 if(eVp->weldEvent == 1)
		 ret[i+21] = 1.;
		 else
	     ret[i+21] = 0.;

//connector
		 
		 ret[i+22] = eVp->a1_connector;
		 ret[i+23] = eVp->a1_connectorError;
		 ret[i+24] = eVp->a2_connector;
		 ret[i+25] = eVp->a2_connectorError;
		 ret[i+26] = eVp->aLet_connector;
		 ret[i+27] = eVp->aLet_connectorError;
		 ret[i+28] = eVp->width_connector;
		 ret[i+29] = eVp->width_connectorError;
		 ret[i+30] = eVp->center_connector;
		 ret[i+31] = eVp->center_connectorError;
		 ret[i+32] = eVp->sigma1_connector;
		 ret[i+33] = eVp->sigma1_connectorError;
		 ret[i+34] = eVp->sigma2_connector;
		 ret[i+35] = eVp->sigma2_connectorError;
		 ret[i+36] = eVp->sigmaFit_connector;
		 ret[i+37] = eVp->sigmaFit_connectorError;
		 ret[i+38] = eVp->k_connector;
		 ret[i+39] = eVp->k_connectorError;
		 ret[i+40] = eVp->beginConnector;
		 ret[i+41] = eVp->endConnector;
		 ret[i+42] = eVp->chi2Connector;
		 if(eVp->connectorEvent == 1)
		 ret[i+43] = 1.;
		 else
		 ret[i+43] = 0.;


//		 printf("Connector chi2 = %f, weld chi2 = %f, linear chi2 = %f\n", ep[counter]->chi2Connector, 
//			 ep[counter]->chi2Weld, ep[counter]->chi2Linear);

		 counter = counter+1;


	}

	return ret;
}
*/
double *setParRet(int n_events, EventParams **ep, int &Ret_Length)
{
	int i;
	int ret_size = n_events*38;
	
    double *ret = new double[ret_size];

	Ret_Length = ret_size;

	int counter = 0;
	EventParams *eVp;

	for(i=0; i<ret_size; i+=38)
	{
		eVp = ep[counter++];
		
		if (eVp->linearEvent == 1)
			ret[i] = 1;
		else if (eVp->weldEvent == 1)
			ret[i] = 2;
		else if (eVp->connectorEvent == 1)
			ret[i] = 3;
		
		ret[i+1] = eVp->beginLinear;
		ret[i+2] = eVp->endLinear;
//linear
	    ret[i+3]   = eVp->a_linear;
		ret[i+4] = eVp->a_linearError;
	    ret[i+5] = eVp->b_linear;
		ret[i+6] = eVp->b_linearError;
	    ret[i+7] = eVp->chi2Linear;
//weld
	    ret[i+8]  = eVp->a_weld;
		ret[i+9]  = eVp->a_weldError;
	    ret[i+10] = eVp->b_weld;
		ret[i+11] = eVp->b_weldError;
	    ret[i+12] = eVp->boost_weld;
		ret[i+13] = eVp->boost_weldError;
	    ret[i+14] = eVp->center_weld;
		ret[i+15] = eVp->center_weldError;
	    ret[i+16] = eVp->width_weld;
		ret[i+17] = eVp->width_weldError;
	    ret[i+18] = eVp->chi2Weld;
//connector
		ret[i+19] = eVp->a1_connector;
		ret[i+20] = eVp->a1_connectorError;
		ret[i+21] = eVp->a2_connector;
		ret[i+22] = eVp->a2_connectorError;
		ret[i+23] = eVp->aLet_connector;
		ret[i+24] = eVp->aLet_connectorError;
		ret[i+25] = eVp->width_connector;
		ret[i+26] = eVp->width_connectorError;
		ret[i+27] = eVp->center_connector;
		ret[i+28] = eVp->center_connectorError;
		ret[i+29] = eVp->sigma1_connector;
		ret[i+30] = eVp->sigma1_connectorError;
		ret[i+31] = eVp->sigma2_connector;
		ret[i+32] = eVp->sigma2_connectorError;
		ret[i+33] = eVp->sigmaFit_connector;
		ret[i+34] = eVp->sigmaFit_connectorError;
		ret[i+35] = eVp->k_connector;
		ret[i+36] = eVp->k_connectorError;
		ret[i+37] = eVp->chi2Connector;
	}

	return ret;
}



void fcnLinear(int &, double *, double &chi2, double *par, int iflag)
{
	double a, k, chisq;
	int i, begin, end;

	a = par[0];
	k = par[1];
	chisq = 0.;
	begin = ep[nEvent]->beginLinear;
	end   = ep[nEvent]->endLinear;

	double sq;

		for(i=begin; i<=end; i++)
		{
			sq = ep[nEvent]->linearFunction(a, k, i)-data[i];

			chisq = chisq + sq*sq/chi2norma;
		}


     if(iflag == 3)
	 {
		 ep[nEvent]->a_linear  = a;
		 ep[nEvent]->b_linear  = k;
		 ep[nEvent]->chi2Linear= chisq/((double)(end-begin));
	 }

	chi2 = chisq;

//    printf("chi2 = %f\n", chi2);

}




void fcnWeld(int &, double *, double &chi2, double *par, int iflag)
{
	double a_shift, w, c, a_base, k, chisq;
	int i, begin, end;

	a_shift = par[0];
	w = par[1];
	c = par[2];
	a_base = par[3];
	k = par[4];
    chisq = 0.;

	begin = ep[nEvent]->beginWeld;
	end   = ep[nEvent]->endWeld;

	double sq;

		for(i=begin; i<=end; i++)
		{
			sq = (ep[nEvent]->weldFunction(a_shift, c, w, a_base, k, i)-data[i]);

			chisq = chisq + sq*sq/chi2norma;
		}

    if(iflag == 3)
	{
		ep[nEvent]->a_weld      = a_base;
		ep[nEvent]->boost_weld  = a_shift;
		ep[nEvent]->center_weld = c;
		ep[nEvent]->b_weld      = k;
		ep[nEvent]->width_weld  = w;
		ep[nEvent]->chi2Weld    = chisq/((double)(end-begin));

//		printf("chi2 = %f\n", ep[nEvent]->chi2Weld);

	}

     chi2 = chisq;
}




void fcnConnector(int &, double *, double &chi2, double *par, int iflag)
{
	double a1, a2, aLet, w, s1, s2, c, k, stf, chisq;
	int i, begin, end;

	a1 = par[0];
	a2 = par[1];
	aLet = par[2];
	w = par[3];
	s1 = par[4];
	s2 = par[5];
	c = par[6];
	k = par[7];
	stf = par[8];
	chisq = 0.;

	begin = ep[nEvent]->beginConnector;
	end   = ep[nEvent]->endConnector;

	if(nEvent == 0) begin +=1; // For dead zone;

	double sq;

		for(i=begin; i<=end; i++)
		{
			sq = (ep[nEvent]->connectorFunction(a1,a2,aLet,w,s1,s2,k,c,stf,i) - data[i]);

			chisq = chisq + sq*sq/chi2norma;
//			chisq = chisq + sq*sq/sigmaSquared[i];
//			chisq = chisq + fabs(sq);
		}

	if(iflag == 3)
	{
		ep[nEvent]->a1_connector   = a1;
		ep[nEvent]->a2_connector   = a2;
		ep[nEvent]->aLet_connector = aLet;
		ep[nEvent]->center_connector = c;
		ep[nEvent]->k_connector      = k;
		ep[nEvent]->width_connector  = w;
		ep[nEvent]->sigma1_connector = s1;
		ep[nEvent]->sigma2_connector = s2;
		ep[nEvent]->sigmaFit_connector = stf;
		ep[nEvent]->chi2Connector = chisq/((double)(end-begin));
	}
	chi2 = chisq;

//	 printf("chi2 = %f\n", chi2);
}



