// PK7600OTDRController.h: interface for the PK7600OTDRController class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_PK7600OTDRCONTROLLER_H__0C8FAC67_365A_4C54_BA86_C6F9D6ED4E7B__INCLUDED_)
#define AFX_PK7600OTDRCONTROLLER_H__0C8FAC67_365A_4C54_BA86_C6F9D6ED4E7B__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "OTDRController.h"
#include "pk76otdr.h"


#define PARAMETER_NAME_WAVELENGTH (const char*) "ref_wvlen"
#define PARAMETER_NAME_TRACE_LENGTH (const char*) "ref_trclen"
#define PARAMETER_NAME_RESOLUTION (const char*) "ref_res"
#define PARAMETER_NAME_PULSE_WIDTH_M (const char*) "ref_pulswd_m"
#define PARAMETER_NAME_IOR (const char*) "ref_ior"
#define PARAMETER_NAME_SCANS (const char*) "ref_scans"
#define PARAMETER_NAME_SMOOTH_FILTER (const char*) "ref_smooth_filter"

class PK7600OTDRController : public OTDRController {
	private:
		/*	��� ����� PK7600 (pk76otdr.h) */
		tCardType cardType;

		/*	��������� �������� ���������.*/
		unsigned short waveLengthM;
		float traceLengthM;
		float resolutionM;
		float pulseWidthM;
		double iorM;
		unsigned short scansM;
		short smoothFilterM;

	public:
		PK7600OTDRController(const OTDRId otdrId,
			OTDRReportListener* otdrReportListener,
			const unsigned int timewait,
			const tCardType cardType);
		virtual ~PK7600OTDRController();

		/*	�������� ������ �������������.
		 * 	���������� ����������� ������� ������ OTDRController.*/
		OTDRModel getOTDRModel() const;

		/*	���������� ��������� ���������.
		 * 	� ������ ������������ �������� ���������� FALSE.
		 * 	���������� ����������� ������� ������ OTDRController.*/
		BOOL setMeasurementParameters(const Parameter** parameters, const unsigned int parNumber);

		/*	�������� ��� ����� PK7600.*/
		tCardType getCardType() const;

	private:
		/*	������� �������� � ����� �������������.
		 * 	���������� ����������� ������� ������ OTDRController.*/
		void retrieveOTDRPluginInfo();

		/*	����������� ���������� ��������� ���������.
		 * 	���������� ����������� ������� ������ OTDRController.*/
		void printAvailableParameters() const;

		/*	�������� ���������.
		 * 	� ������ ������ ���������� ������ ��������� ������ ������. ����� - NULL.
		 * 	���������� ����������� ������� ������ OTDRController.*/
		BellcoreStructure* runMeasurement() const;

		void fillBellcoreStructure(BellcoreStructure* bellcoreStructure,
			OTDRWaveformHeader* waveFormHeader,
			OTDRWaveformData*  waveFormData) const;
};

#endif // !defined(AFX_PK7600OTDRCONTROLLER_H__0C8FAC67_365A_4C54_BA86_C6F9D6ED4E7B__INCLUDED_)
