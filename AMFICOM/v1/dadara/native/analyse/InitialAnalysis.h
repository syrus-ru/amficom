#if !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
#define AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_

#include <stdio.h>
#include "../common/EventParams.h"

#include "debug.h"

#include "../common/ArrList.h"

class Splash;
//---------------------------------------------------------------------------------------------------------------
class InitialAnalysis
{
private:
#ifdef DEBUG_INITIAL_ANALYSIS
	FILE *logf;
#endif

public:

#ifdef debug_lines
	static const int sz = 1000000;
	int cou;
    double xs[sz], xe[sz], ys[sz], ye[sz], col[sz];//�������������� ��� ������� ��� ���������� ������
#endif
#ifdef debug_VCL
	double* debug_f_wlet; // �������-����� ��� �������
    double* type; // ������ � ������� ��������� ���� ��� ������������ �����
    double* f_tmp; //!!! ������ ��� ���������� �������� ������������ �������������� ( ����������� ��� ������� )
#endif

	InitialAnalysis(
		double *data,
        int data_length,
		double delta_x,
		double minimalThreshold,
		double minimalWeld,
		double minimalConnector,
		double minimalEnd,
		double noiseFactor,
		int nonReflectiveSize,
		double rACrit,	// ����� "��������" ����������
		int rSSmall,	// ����. ����� ��� ���������� ����������
		int rSBig,		// ����. ����� ��� �������� ����������
		int lengthTillZero = 0,
		double *externalNoise = 0); // null to find automatically

	~InitialAnalysis();

    ArrList& getEvents(){return *events;}

#ifndef debug_VCL
private:
#endif

	double *data;
	int     data_length;
	double  delta_x;
	double *noise;

    double average_factor; // ������������� ��. ������� �� �/�, �� �������� �� � �������� ��������, �� � ��������� �����

    ArrList* events; // ������ ���� �������

//Parameters of the analysis (criteria);
	double minimalThreshold;
	double minimalWeld;
	double minimalConnector;
    double minimalEnd;
    double noiseFactor;

    int reflectiveSize; // ������������ ������ ����������
    double rACrit;  	// ����� "��������" ����������
	int rSSmall;		// ����. ����� ��� ���������� ����������
	int rSBig;          // ����. ����� ��� �������� ����������

	int lastPoint;
	double wletMeanValue; // ������� �������� ������ ��������������

	void performAnalysis(int scaleB);
	int getLastPoint();

	// ��������� ����� "a" � "b" ������ y=ax+b, �������������� RMS
    void calc_rms_line(double *arr, int beg, int end, double& a, double& b);// (c) Vit

	// ������ � �����
	static void fillNoiseArray(double *y, int data_length, int N, double Neff, double NoiseFactor, double *outNoise);
	static void getNoise(double *noise, int freq);
	static double calcThresh(double thres, double noise); // ����� �� ������ ���� ����, ����� ������ �������� ��������� ������� ������� � ��������� ��������

	// ���������� �������-��������������
	void performTransformationOnly(double *y, int begin, int end, double *trans, int freq, double norma);
	void performTransformationAndCenter(double *y, int begin, int end, double *trans, int freq, double norma);
	void centerWletImageOnly(double* f_wlet, int scale, int begin, int end, double norma1);

	// ������ R-���������� splash (� ������ ��� �����������, �.�. ����� ����� ������� �������-�����)
	void fillSplashRParameters(Splash &spl, double *f_wlet, int wlet_width);

	// ======= ������ ���� ������� - ���������� =======
	static double calcWletMeanValue(double* fw, int lastPoint, double from, double to, int columns);// ��������� ����� ���������� �������� �-��� fw
	void calcAverageFactor(double* fw, int scale, double norma1);
	void shiftThresholds(int scale);// �������� ������� ������� � ������������ �� ������� ��������� �������� 

	// ======= ������ ���� ������� - ����������� ��������� =======
	void findAllWletSplashes(double* f_wlet, int wlet_width, ArrList& splashes);

	// ======= ������ ���� ������� - ����������� ������� �� ��������� =======
    void findEventsBySplashes(double* f_wlet, int wlet_width, ArrList&  splashes);
	int	 processDeadZone(ArrList& splashes);
    int  processIfIsConnector(double* f_wlet, int wlet_width, int i, ArrList& splashes);// ����������, ���� �� ���-�� ������� �� ��������� , ���� ������ � i-�� ��������, � ���� ���� - ���������� � ��������, ������� �������� i � ������ �����; ���� ������ �� �����, �� ����� ����� 0
    void setSpliceParamsBySplash(EventParams& ep, Splash& sp1);
    void setConnectorParamsBySplashes(int wlet_width, EventParams& ep, Splash& sp1, Splash& sp2);
    void setUnrecognizedParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2);
	void correctSpliceCoords(double *f_wletTMP, int wlet_width, EventParams* splice);// �-� ������ ������� ����� !  (��� ��� ���������� ��� �� ������ ��� �������� ������ �� ������ ��������)
	void correctConnectorFront(EventParams* connector);

	// ====== ��������� ���� ������� - ��������� ������� =======
    void processEndOfTrace();  // ������� ��� ������� ����� ���������� �������������� � ������������� ������������� � "����� �������"
    void addLinearPartsBetweenEvents();
    void excludeShortLinesBetweenConnectors(double* data, int evSizeC);
    void trimAllEvents(); // ��-�� ���������� ��������� ������� ���� ������� ��������� ���� �� �����, ����������� ��
    void verifyResults();
};
//====================================================================================================
class Splash
{ public:
	int begin_thr;		// ������ ����������� ������������ ������
    int end_thr;
	int begin_weld;		// ������ ����������� ���������� ������
    int end_weld;
	int begin_conn;		// ����������� ������������� ������
    int end_conn;

	int scale;
	double r_conn;		// max{(f_wlet[i]-minimalConnector)/noise[i]} (<0, ���� ����� �� ���������)
	double r_acrit;		// max{(f_wlet[i]-rACrit)/noise[i]} (<0, ���� rACrit �� ���������)
	double r_weld;		// max{(f_wlet[i]-minimalWeld)/noise[i]} (<0, ���� ����� �� ���������)

	double f_extr;		// ������������� �������� ��������
    int sign;  // ���� ��������

	// �������������� �������������� ����������, ������ ������ ������� �����������
    Splash(int scale1)
    {	// NB: begin=end - ��� ���� ��� ����������� �� ������ ���������, ���������� ��� ��� ���
		begin_thr 		= -1;
    	end_thr 		= -1;
		begin_weld		= -1;
    	end_weld 		= -1;
		begin_conn 		= -1;
    	end_conn 		= -1;
        f_extr 			= 0;
    	sign 			= 0;
		scale			= scale1;
		r_conn			= -1;
		r_acrit			= -1;
		r_weld			= -1;
    }
};
//====================================================================================================
#endif // !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
