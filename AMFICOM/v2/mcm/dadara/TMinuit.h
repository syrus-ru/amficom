

#ifndef ROOT_TMinuit
#define ROOT_TMinuit

#include"TString.h"
//#include"STDARG.H"

class TMinuit
{


public:
        double     fAmin;             //Minimum value found for FCN
        double     fUp;               //FCN+-UP defines errors (for chisquare fits UP=1)
        double     fEDM;              //Estimated vertical distance to the minimum
        double     fFval3;            //
        double     fEpsi;             //
        double     fApsi;             //
        double     fDcovar;           //Relative change in covariance matrix
        double     fEpsmac;           //machine precision for floating points:
        double     fEpsma2;           //sqrt(fEpsmac)
        double     fVlimlo;           //
        double     fVlimhi;           //
        double     fUndefi;           //Undefined number = -54321
        double     fBigedm;           //Big EDM = 123456
        double     fUpdflt;           //
        double     fXmidcr;           //
        double     fYmidcr;           //
        double     fXdircr;           //
        double     fYdircr;           //
        
        double     *fU;               //External (visible to user in FCN) value of parameters
        double     *fAlim;            //Lower limits for parameters. If zero no limits
        double     *fBlim;            //Upper limits for parameters
        double     *fErp;             //Positive Minos errors if calculated
        double     *fErn;             //Negative Minos errors if calculated
        double     *fWerr;            //External parameters error (standard deviation, defined by UP)
        double     *fGlobcc;          //Global Correlation Coefficients
        double     *fX;               //Internal parameters values
        double     *fXt;              //Internal parameters values X saved as Xt
        double     *fDirin;           //(Internal) step sizes for current step
        double     *fXs;              //Internal parameters values saved for fixed params
        double     *fXts;             //Internal parameters values X saved as Xt for fixed params
        double     *fDirins;          //(Internal) step sizes for current step for fixed params
        double     *fGrd;             //First derivatives
        double     *fG2;              //
        double     *fGstep;           //Step sizes
        double     *fGin;             //
        double     *fDgrd;            //Uncertainties
        double     *fGrds;            //
        double     *fG2s;             //
        double     *fGsteps;          //
        double     *fVhmat;           //(Internal) error matrix stored as Half MATrix, since it is symmetric
        double     *fVthmat;          //VHMAT is sometimes saved in VTHMAT, especially in MNMNOT
        double     *fP;               //
        double     *fPstar;           //
        double     *fPstst;           //
        double     *fPbar;            //
        double     *fPrho;            //Minimum point of parabola
        double     *fWord7;           //
        double     *fXpt;             //X array of points for contours
        double     *fYpt;             //Y array of points for contours
        
        double     *fCONTgcc;         //[kMAXDIM] array used in mncont
        double     *fCONTw;           //[kMAXDIM] array used in mncont
        double     *fFIXPyy;          //[kMAXDIM] array used in mnfixp
        double     *fGRADgf;          //[kMAXDIM] array used in mngrad
        double     *fHESSyy;          //[kMAXDIM] array used in mnhess
        double     *fIMPRdsav;        //[kMAXDIM] array used in mnimpr
        double     *fIMPRy;           //[kMAXDIM] array used in mnimpr
        double     *fMATUvline;       //[kMAXDIM] array used in mnmatu
        double     *fMIGRflnu;        //[kMAXDIM] array used in mnmigr
        double     *fMIGRstep;        //[kMAXDIM] array used in mnmigr
        double     *fMIGRgs;          //[kMAXDIM] array used in mnmigr
        double     *fMIGRvg;          //[kMAXDIM] array used in mnmigr
        double     *fMIGRxxs;         //[kMAXDIM] array used in mnmigr
        double     *fMNOTxdev;        //[kMAXDIM] array used in mnmnot
        double     *fMNOTw;           //[kMAXDIM] array used in mnmnot
        double     *fMNOTgcc;         //[kMAXDIM] array used in mnmnot
        double     *fPSDFs;           //[kMAXDIM] array used in mnpsdf
        double     *fSEEKxmid;        //[kMAXDIM] array used in mnseek
        double     *fSEEKxbest;       //[kMAXDIM] array used in mnseek
        double     *fSIMPy;           //[kMAXDIM] array used in mnsimp
        double     *fVERTq;           //[kMAXDIM] array used in mnvert
        double     *fVERTs;           //[kMAXDIM] array used in mnvert
        double     *fVERTpp;          //[kMAXDIM] array used in mnvert
        double     *fCOMDplist;       //[kMAXP]   array used in mncomd
        double     *fPARSplist;       //[kMAXP]   array used in mnpars
        
        int        *fNvarl;           //parameters flag (-1=undefined, 0=constant..)
        int        *fNiofex;          //Internal parameters number, or zero if not currently variable
        int        *fNexofi;          //External parameters number for currently variable parameters
        int        *fIpfix;           //List of fixed parameters
        int        fNpfix;            //Number of fixed parameters
        int        fEmpty;            //Initialization flag (1 = Minuit initialized)
        int        fMaxpar;           //Maximum number of parameters
        int        fMaxint;           //Maximum number of internal parameters
        int        fNpar;             //Number of free parameters (total number of pars = fNpar + fNfix)
        int        fMaxext;           //Maximum number of external parameters
        int        fMaxIterations;    //Maximum number of iterations
        int        fNu;               //
        int        fIsysrd;           //standardInput unit
        int        fIsyswr;           //standard output unit
        int        fIsyssa;           //
        int        fNpagwd;           //Page width
        int        fNpagln;           //Number of lines per page
        int        fNewpag;           //
        int        fIstkrd[10];       //
        int        fNstkrd;           //
        int        fIstkwr[10];       //
        int        fNstkwr;           //
        int        fISW[7];           //Array of switches
        int        fIdbg[11];         //Array of internal debug switches
        int        fNblock;           //Number of Minuit data blocks
        int        fIcomnd;           //Number of commands
        int        fNfcn;             //Number of calls to FCN
        int        fNfcnmx;           //Maximum number of calls to FCN
        int        fNfcnlc;           //
        int        fNfcnfr;           //
        int        fItaur;            //
        int        fIstrat;           //
        int        fNwrmes[2];        //
        int        fNfcwar[20];       //
        int        fIcirc[2];         //
        int        fStatus;           //Status flag for the last called Minuit function
        int        fKe1cr;            //
        int        fKe2cr;            //
        bool       fLwarn;            //true if warning messges are to be put out (default=true)
        bool       fLrepor;           //true if exceptional conditions are put out (default=false)
        bool       fLimset;           //true if a parameter is up against limits (for MINOS)
        bool       fLnolim;           //true if there are no limits on any parameters (not yet used)
        bool       fLnewmn;           //true if the previous process has unexpectedly improved FCN
        bool       fLphead;           //true if a heading should be put out for the next parameter definition
        char         *fChpt;            //Character to be plotted at the X,Y contour positions
        TString      *fCpnam;           //Array of parameters names
        TString      fCfrom;            //
        TString      fCstatu;           //
        TString      fCtitl;            //
        TString      fCword;            //
        TString      fCundef;           //
        TString      fCvrsn;            //
        TString      fCovmes[4];        //
        TString      *fOrigin;          //
        TString      *fWarmes;          //
        void         (*fFCN)(int &npar, double *gin, double &f, double *u, int flag);

// methods performed on TMinuit class
public:
                TMinuit();
                TMinuit(int maxpar);
 virtual       ~TMinuit();
 virtual void   BuildArrays(int maxpar=15);
 virtual int  Command(const char *command);
 virtual int  DefineParameter( int parNo, const char *name, double initVal, double initErr, double lowerLimit, double upperLimit );
 virtual void   DeleteArrays();
 virtual int  Eval(int npar, double *grad, double &fval, double *par, int flag);
 virtual int  FixParameter( int parNo );
 int          GetMaxIterations() {return fMaxIterations;}
 virtual int  GetNumFixedPars();
 virtual int  GetNumFreePars();
 virtual int  GetNumPars();
 virtual int  GetParameter( int parNo, double &currentValue, double &currentError );
 int          GetStatus() {return fStatus;}
 virtual int  Migrad();
 virtual void   mnamin();
 virtual void   mnbins(double a1, double a2, int naa, double &bl, double &bh, int &nb, double &bwid);
 virtual void   mncalf(double *pvec, double &ycalf);
 virtual void   mncler();
 virtual void   mncntr(int ke1, int ke2, int &ierrf);
 virtual void   mncomd(const char *crdbin, int &icondn);
 virtual void   mncont(int ke1, int ke2, int nptu, double *xptu, double *yptu, int &ierrf);
 virtual void   mncrck(TString crdbuf, int maxcwd, TString &comand, int &lnc
                    ,  int mxp, double *plist, int &llist, int &ierr, int isyswr);
 virtual void   mncros(double &aopt, int &iercr);
 virtual void   mncuve();
 virtual void   mnderi();
 virtual void   mndxdi(double pint, int ipar, double &dxdi);
 virtual void   mneig(double *a, int ndima, int n, int mits, double *work, double precis, int &ifault);
 virtual void   mnemat(double *emat, int ndim);
 virtual void   mnerrs(int number, double &eplus, double &eminus, double &eparab, double &gcc);
 virtual void   mneval(double anext, double &fnext, int &ierev);
 virtual void   mnexcm(const char *comand, double *plist, int llist, int &ierflg) ;
 virtual void   mnexin(double *pint);
 virtual void   mnfixp(int iint, int &ierr);
 virtual void   mnfree(int k);
 virtual void   mngrad();
 virtual void   mnhelp(TString comd);
 virtual void   mnhelp(const char *command="");
 virtual void   mnhess();
 virtual void   mnhes1();
 virtual void   mnimpr();
 virtual void   mninex(double *pint);
 virtual void   mninit(int i1, int i2, int i3);
 virtual void   mnlims();
 virtual void   mnline(double *start, double fstart, double *step, double slope, double toler);
 virtual void   mnmatu(int kode);
 virtual void   mnmigr();
 virtual void   mnmnos();
 virtual void   mnmnot(int ilax, int ilax2, double &val2pl, double &val2mi);
 virtual void   mnparm(int k, TString cnamj, double uk, double wk, double a, double b, int &ierflg);
 virtual void   mnpars(TString &crdbuf, int &icondn);
 virtual void   mnpfit(double *parx2p, double *pary2p, int npar2p, double *coef2p, double &sdev2p);
 virtual void   mnpint(double &pexti, int i, double &pinti);
 virtual void   mnplot(double *xpt, double *ypt, char *chpt, int nxypt, int npagwd, int npagln);
 virtual void   mnpout(int iuext, TString &chnam, double &val, double &err, double &xlolim, double &xuplim, int &iuint);
 virtual void   mnprin(int inkode, double fval);
 virtual void   mnpsdf();
 virtual void   mnrazz(double ynew, double *pnew, double *y, int &jh, int &jl);
 virtual void   mnrn15(double &val, int &inseed);
 virtual void   mnrset(int iopt);
 virtual void   mnsave();
 virtual void   mnscan();
 virtual void   mnseek();
 virtual void   mnset();
 virtual void   mnsimp();
 virtual void   mnstat(double &fmin, double &fedm, double &errdef, int &npari, int &nparx, int &istat);
 virtual void   mntiny(double epsp1, double &epsbak);
 bool         mnunpt(TString &cfname);
 virtual void   mnvert(double *a, int l, int m, int n, int &ifail);
 virtual void   mnwarn(const char *copt, const char *corg, const char *cmes);
 virtual void   mnwerr();
 virtual int  Release( int parNo );
 virtual int  SetErrorDef( double up );
 virtual void   SetFCN(void (*fcn)(int &, double *, double &f, double *, int));
 virtual void   SetMaxIterations(int maxiter=500) {fMaxIterations = maxiter;}
 virtual int  SetPrintLevel( int printLevel=0 );



double Min(double a1, double a2);
int Min(int a1, int a2);
double Max(double a1, double a2);
int Max(int a1, int a2);

};

#endif

