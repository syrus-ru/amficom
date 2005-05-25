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

    double* type; // ������ � ������� ��������� ���� ��� ������������ �����
	double *noise;

    double* f_wlet;
    double average_factor; // �� ������� �� �� ���������� �������� ��������, �� �� ��������� ����� (� ������ �� ��. ������� �� �/�)
#ifdef debug_VCL
    double* f_tmp; //!!! ������ ��� ���������� �������� ������������ �������������� ( ����������� ��� ������� )
#endif
    ArrList* events; // ������ ���� �������
//Parameters of the analysis (criteria);
	double minimalThreshold;
	double minimalWeld;
	double minimalConnector;
    double minimalEnd;
    double noiseFactor;

    int wlet_width; // ������� ������ ��������
    int reflectiveSize; // ������������ ������ ����������
    double rACrit;  	// ����� "��������" ����������
	int rSSmall;		// ����. ����� ��� ���������� ����������
	int rSBig;          // ����. ����� ��� �������� ����������

	int lastPoint;
	double wletMeanValue; // ������� �������� ������ ��������������

	void performAnalysis();
	int getLastPoint();

	// ��������� ����� "a" � "b" ������ y=ax+b, �������������� RMS
    void calc_rms_line(double *arr, int beg, int end, double& a, double& b);// (c) Vit

	// ��������� ������ ���� (��� �� ���������, ������� ���������� ������ ��� �������� ������� ���� � ������ �����)
	void fillNoiseArray(double *y, int data_length, int N, double Neff, double NoiseFactor, double *outNoise);
	void getNoise(double *noise, int freq);
	double calcThresh(double thres, double noise); // ����� �� ������ ���� ����, ����� ������ �������� ��������� ������� ������� � ��������� ��������

	// ���������� �������� ��������
	double calcWletMeanValue(double* fw, double from, double to, int columns);// ��������� ����� ���������� �������� �-��� fw
	void calcAverageFactor(double* fw, int scale, double norma1);

	// ���������� �������-��������������
	void performTransformationOnly(double *y, int begin, int end, double *trans, int freq, double norma);
	void performTransformationAndCenter(double *y, int begin, int end, double *trans, int freq, double norma);
	void centerWletImageOnly(double* f_wlet, int scale, int begin, int end, double norma1);

	// ������
	void shiftThresholds();// �������� ������� ������� � ������������ �� ������� ��������� �������� 
    void findAllWletSplashes(double* f_wlet, ArrList& splashes);
    void findEventsBySplashes(ArrList&  splashes);
	int	 processDeadZone(ArrList& splashes);
    int  processIfIsConnector(int i, ArrList& splashes);// ����������, ���� �� ���-�� ������� �� ��������� , ���� ������ � i-�� ��������, � ���� ���� - ���������� � ��������, ������� �������� i � ������ �����; ���� ������ �� �����, �� ����� ����� 0
    void setSpliceParamsBySplash( EventParams& ep, Splash& sp1);
    void setConnectorParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2);
    void setUnrecognizedParamsBySplashes( EventParams& ep, Splash& sp1, Splash& sp2);
    void processEndOfTrace();  // ������� ��� ������� ����� ���������� �������������� � ������������� ������������� � "����� �������"
    void addLinearPartsBetweenEvents();
    void correctAllConnectorsFronts(double *arr);
    void correctAllSpliceCoords(); // �-� ������ ������� ����� !  (��� ��� ���������� ��� �� ������ ��� �������� ������ �� ������ ��������)
    void correctSpliceCoords(int n);
    void excludeShortLinesBetweenConnectors(double* data, int evSizeC);
    void trimAllEvents(); // ��-�� ���������� ��������� ������� ���� ������� ��������� ���� �� �����, ����������� ��
    void verifyResults();

// Wavelet constants;
private:
    double wn;// ����� �������� (����� ������������� ! �� ���������� ! )
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

	double f_extr;		// ������������� �������� ��������
    int sign;  // ���� ��������

	// �������������� �������������� ���������� ()
    Splash()
    {	begin_thr 		= -1;
    	end_thr 		= -1;
		begin_weld		= -1;
    	end_weld 		= -1;
		begin_conn 		= -1;
    	end_conn 		= -1;
        f_extr 			= 0;
    	sign 			= 0;
    }
};
//====================================================================================================


#endif // !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
