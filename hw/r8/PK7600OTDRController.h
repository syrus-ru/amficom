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


#define PARAMETER_NAME_WAVELENGTH "ref_wvlen"
#define PARAMETER_NAME_TRACE_LENGTH "ref_trclen"
#define PARAMETER_NAME_RESOLUTION "ref_res"
#define PARAMETER_NAME_PULSE_WIDTH "ref_pulswd"
#define PARAMETER_NAME_IOR "ref_ior"
#define PARAMETER_NAME_SCANS "ref_scans"

class PK7600OTDRController : public OTDRController {
	private:
		/*	Тип платы PK7600 (pk76otdr.h) */
		tCardType cardType;

	public:
		PK7600OTDRController(const OTDRId otdrId,
			const OTDRReportListener* otdrReportListener,
			const unsigned int timewait,
			const tCardType cardType);
		virtual ~PK7600OTDRController();

		/*	Получить модель рефлектометра.
		 * 	Реализация виртуальной функции класса OTDRController.*/
		OTDRModel getOTDRModel() const;

		/*	Получить тип платы PK7600*/
		tCardType getCardType() const;

	private:
		/*	Достать сведения о плате рефлектометра.
		 * 	Реализация виртуальной функции класса OTDRController.*/
		void retrieveOTDRPluginInfo();

		/*	Распечатать допустимые параметры измерений.
		 * 	Реализация виртуальной функции класса OTDRController.*/
		void printAvailableParameters() const;

		BOOL setMeasurementParameters0(const ParametersMapT parametersMap) const;
};

#endif // !defined(AFX_PK7600OTDRCONTROLLER_H__0C8FAC67_365A_4C54_BA86_C6F9D6ED4E7B__INCLUDED_)
