#if !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
#define AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_

#include <stdio.h>
#include "../Common/EventParams.h"

#include "debug.h"

//#include "EPList2.h"
#include "../Common/ArrList.h"

//---------------------------------------------------------------------------------------------------------------
class InitialAnalysis
{
public:
#ifdef debug_lines
	static const int sz = 1000000;
	int cou;
    double xs[sz], xe[sz], ys[sz], ye[sz], col[sz];//�������������� ��� ������� ��� ���������� ������
#endif
	InitialAnalysis(
		double *data, int data_length,
		double delta_x,
		double minimalThreshold,
		double minimalWeld,
		double minimalConnector,
		double minimalEndingSplash,
		double maximalNoise,
		int waveletType,
		double formFactor,
		int reflectiveSize,
		int nonReflectiveSize);

	~InitialAnalysis();

	int getEventSize();
	int getEventsCount();
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
    double f_wlet_avrg; // ������� �������� �������-������ (�� ����, �� �������� �������� �� �������)
#ifdef debug_VCL
    double* f_tmp; //!!! ������ ��� ���������� �������� ������������ �������������� ( ����������� ��� ������� )
#endif
    ArrList* events; // ������ ���� �������
//Parameters of the analysis (criteria);
	double minimalThreshold;
	double minimalWeld;
	double minimalConnector;
	double minimalEndingSplash;
	double maximalNoise;
	int	   waveletType;
	double formFactor;

    int wlet_width; // ������ ��������
    int reflectiveSize;// ������������ ������ ����������

	int lastNonZeroPoint;
	double wletMeanValue; // ������� �������� ������ ��������������

	void performAnalysis();
	int getLastPoint();

	// ��������� ����� "a" � "b" ������ y=ax+b, �������������� RMS
    void calc_rms_line(double *arr, int beg, int end, double& a, double& b);// (c) Vit

	// ��������� ������ ���� (��� �� ���������, ������� ���������� ������ ��� �������� ������� ���� � ������ �����)
	void fillNoiseArray(double *y, int N, double Neff, double *outNoise);
	void getNoise(double *noise, int freq);

	void performTransformation(double *y, int begin, int end, double *trans, int freq, double norma);
	double calcWletMeanValue(double* f, double from, double to, int columns);// ��������� ����� ���������� �������� �-���
	void centerWletImage(double* f_wlet);
    void findAllWletSplashes(double* f_wlet, ArrList& splashes);
    void findEventsBySplashes(ArrList&  splashes);
    void deleteAllEventsAfterLastConnector();
    void addLinearPartsBetweenEvents();
    void correctAllConnectorsFronts(double *arr);
    void correctAllSpliceCoords(); // �-� ������ ������� ����� !  (��� ��� ���������� ��� �� ������ ��� �������� ������ �� ������ ��������)
    void correctSpliceCoords(int n);
    void excludeShortLinesBetweenConnectors(double* data, int evSizeC);

//Wavelet constants;
private:
    double wn;// ����� �������� (����� ������������� ! �� ���������� ! )
};
//====================================================================================================
class Splash
{ public:
 	int begin; 		// ������ �������� (��� ��������� ����������� �����)
	int end;        // ����� �������� (������ ����������� ���� ������)
   	int begin_nonoise;  // �� �� , ��� � begin, �� ��� ���������� � ������ ����
	int end_nonoise;
    int sign; 		// "+1" ���� ������� ����, "-1" ���� ������� ����

	double f_extr; 	// �������� � ����� ����������
    int x_extr; 	// ��������� ����� ���������
    double square;  // ������� ��� ���������

    Splash(int begin, int end, int begin_nonoise, int end_nonoise, int sign, double f_extr, int x_extr, double square)
    {	this->begin 		= begin;
		this->end 			= end;
        this->begin_nonoise	= begin_nonoise;
		this->end_nonoise	= end_nonoise;
		this->sign 			= sign;
		this->f_extr 		= f_extr;
		this->x_extr 		= x_extr;
        this->square		= square;
    }
};
//====================================================================================================
#endif // !defined(AFX_INITIALANALYSIS_H__017F9246_0344_404F_8231_CC3B33AB54DA__INCLUDED_)
