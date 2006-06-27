#ifndef ROOT_TFitter
#define ROOT_TFitter

#include"TMinuit.h"

class TFitter
{
public:
	
   TMinuit *gMinuit;

private:
   int      fNlog;       //Number of elements in fSunLog
   double  *fSumLog;     //Sum of logs (array of fNlog elements)

   public:
   TFitter(int maxpar = 25);
   virtual ~TFitter();
   virtual void     Clear(Option_t *option="");
   virtual int    ExecuteCommand(const char *command, double *args, int nargs);
   virtual void     FixParameter(int ipar);
   virtual int    GetErrors(int ipar,double &eplus, double &eminus, double &eparab, double &globcc);
   virtual int    GetParameter(int ipar,char *name,double &value,double &verr,double &vlow, double &vhigh);
   virtual int    GetStats(double &amin, double &edm, double &errdef, int &nvpar, int &nparx);
   virtual double GetSumLog(int i);
   virtual void     PrintResults(int level, double amin) const;
   virtual void     ReleaseParameter(int ipar);
   virtual void     SetFCN(void (*fcn)(int &, double *, double &f, double *, int));
   virtual int    SetParameter(int ipar,const char *parname,double value,double verr,double vlow, double vhigh);

};

#endif
