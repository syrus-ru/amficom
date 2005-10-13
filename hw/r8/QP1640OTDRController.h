#ifndef QP1640OTDRCONTROLLER_H_
#define QP1640OTDRCONTROLLER_H_

#include <windows.h>
#include "OTDRController.h"
#include "qpotdr.h"

class QP1640OTDRController : public OTDRController {
	public:
		QP1640OTDRController(const OTDRId otdrId,
			const OTDRReportListener* otdrReportListener,
			const unsigned int timewait);
		virtual ~QP1640OTDRController();

		/*	Получить модель рефлектометра.
		 * 	Реализация виртуальной функции класса OTDRController.*/
		OTDRModel getOTDRModel() const;

	private:
		/*	Достать сведения о плате рефлектометра.
		 * 	Реализация виртуальной функции класса OTDRController.*/
		void retrieveOTDRPluginInfo();

		/*	Распечатать допустимые параметры измерений.
		 * 	Реализация виртуальной функции класса OTDRController.*/
		void printAvailableParameters() const;

		BOOL setMeasurementParameters0(const ParametersMapT parametersMap) const;
};

#endif /*QP1640OTDRCONTROLLER_H_*/
