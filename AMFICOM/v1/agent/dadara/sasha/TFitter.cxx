// @(#)root/minuit:$Name:  $:$Id: TFitter.cxx,v 1.1.1.1 2004/04/05 14:41:03 arseniy Exp $
// Author: Rene Brun   31/08/99
/*************************************************************************
 * Copyright (C) 1995-2000, Rene Brun and Fons Rademakers.               *
 * All rights reserved.                                                  *
 *                                                                       *
 * For the licensing terms see $ROOTSYS/LICENSE.                         *
 * For the list of contributors see $ROOTSYS/README/CREDITS.             *
 *************************************************************************/
#include "TMinuit.h"
#include "TFitter.h"
#include "math.h"

//______________________________________________________________________________
TFitter::TFitter(int maxpar)
{
//*-*-*-*-*-*-*-*-*-*-*default constructor*-*-*-*-*-*-*-*-*-*-*-*-*
//*-*                  ===================

   gMinuit = new TMinuit(maxpar);
   fNlog = 0;
   fSumLog = 0;
}

//______________________________________________________________________________
TFitter::~TFitter()
{
//*-*-*-*-*-*-*-*-*-*-*default destructor*-*-*-*-*-*-*-*-*-*-*-*-*-*
//*-*                  ==================
   delete gMinuit;
   if (fSumLog) delete [] fSumLog;
}

//______________________________________________________________________________
void TFitter::Clear(Option_t *)
{
   // reset the fitter environment

   gMinuit->mncler();

}

//______________________________________________________________________________
int TFitter::ExecuteCommand(const char *command, double *args, int nargs)
{
   // Execute a fitter command;
   //   command : command string
   //   args    : list of nargs command arguments

   int ierr = 0;
   gMinuit->mnexcm(command,args,nargs,ierr);
   return ierr;
}

//______________________________________________________________________________
void TFitter::FixParameter(int ipar)
{
   // Fix parameter ipar.

   gMinuit->FixParameter(ipar);
}

//______________________________________________________________________________
int TFitter::GetErrors(int ipar,double &eplus, double &eminus, double &eparab, double &globcc)
{
   // return current errors for a parameter
   //   ipar     : parameter number
   //   eplus    : upper error
   //   eminus   : lower error
   //   eparab   : parabolic error
   //   globcc   : global correlation coefficient


   int ierr = 0;
   gMinuit->mnerrs(ipar, eplus,eminus,eparab,globcc);
   return ierr;
}


//______________________________________________________________________________
int TFitter::GetParameter(int ipar,char *parname,double &value,double &verr,double &vlow, double &vhigh)
{
   // return current values for a parameter
   //   ipar     : parameter number
   //   parname  : parameter name
   //   value    : initial parameter value
   //   verr     : initial error for this parameter
   //   vlow     : lower value for the parameter
   //   vhigh    : upper value for the parameter

   int ierr = 0;
   TString pname;
   gMinuit->mnpout(ipar, pname,value,verr,vlow,vhigh,ierr);
   strcpy(parname,pname.Data());
   return ierr;
}

//______________________________________________________________________________
int TFitter::GetStats(double &amin, double &edm, double &errdef, int &nvpar, int &nparx)
{
   // return global fit parameters
   //   amin     : chisquare
   //   edm      : estimated distance to minimum
   //   errdef
   //   nvpar    : number of variable parameters
   //   nparx    : total number of parameters

   int ierr = 0;
   gMinuit->mnstat(amin,edm,errdef,nvpar,nparx,ierr);
   return ierr;
}

//______________________________________________________________________________
double TFitter::GetSumLog(int n)
{
   // return Sum(log(i) i=0,n
   // used by log likelihood fits

   if (n < 0) return 0;
   if (n > fNlog) {
      if (fSumLog) delete [] fSumLog;
      fNlog = 2*n+1000;
      fSumLog = new double[fNlog+1];
      double fobs = 0;
      for (int j=0;j<=fNlog;j++) {
         if (j > 1) fobs += log(j);
         fSumLog[j] = fobs;
      }
   }
   if (fSumLog) return fSumLog[n];
   return 0;
}

//______________________________________________________________________________
void  TFitter::PrintResults(int level, double amin) const
{
   // Print fit results

   gMinuit->mnprin(level,amin);
}

//______________________________________________________________________________
void TFitter::ReleaseParameter(int ipar)
{
   // Release parameter ipar.

   gMinuit->Release(ipar);
}


//______________________________________________________________________________
void TFitter::SetFCN(void (*fcn)(int &, double *, double &f, double *, int))
{
   // Specify the address of the fitting algorithm

   gMinuit->SetFCN(fcn);
}

//______________________________________________________________________________
int TFitter::SetParameter(int ipar,const char *parname,double value,double verr,double vlow, double vhigh)
{
   // set initial values for a parameter
   //   ipar     : parameter number
   //   parname  : parameter name
   //   value    : initial parameter value
   //   verr     : initial error for this parameter
   //   vlow     : lower value for the parameter
   //   vhigh    : upper value for the parameter

   int ierr = 0;
   gMinuit->mnparm(ipar,parname,value,verr,vlow,vhigh,ierr);
   return ierr;
}
