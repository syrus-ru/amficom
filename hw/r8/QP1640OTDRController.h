#ifndef QP1640OTDRCONTROLLER_H_
#define QP1640OTDRCONTROLLER_H_

#include <windows.h>
#include "OTDRController.h"
#include "qpotdr.h"

#define PARAMETER_NAME_WAVELENGTH (const char*) "ref_wvlen"
#define PARAMETER_NAME_TRACE_LENGTH (const char*) "ref_trclen"
#define PARAMETER_NAME_RESOLUTION (const char*) "ref_res"
#define PARAMETER_NAME_PULSE_WIDTH_LOW_RES (const char*) "ref_pulswd_low_res"
#define PARAMETER_NAME_PULSE_WIDTH_HIGH_RES (const char*) "ref_pulswd_high_res"
#define PARAMETER_NAME_IOR (const char*) "ref_ior"
#define PARAMETER_NAME_SCANS (const char*) "ref_scans"
#define PARAMETER_NAME_FLAG_GAIN_SPLICE_ON (const char*) "ref_flag_gain_splice_on"
#define PARAMETER_NAME_FLAG_LIVE_FIBER_DETECT (const char*) "ref_flag_life_fiber_detect"

class QP1640OTDRController : public OTDRController {
	private:
		/*	Параметры текущего измерения.*/
		float waveLengthM;
		float traceLengthM;
		float resolutionM;
		DWORD pulseWidthM;
		float iorM;
		DWORD scansM;
		DWORD filterFlagsM;

		/*	Этот параметр, возможно, надо перенести в структуру OTDRPluginInfo.*/
		/*	Number of averages in hardware per scan.*/
		DWORD fastScanCount;

	public:
		QP1640OTDRController(const OTDRId otdrId,
			OTDRReportListener* otdrReportListener,
			const unsigned int timewait);
		virtual ~QP1640OTDRController();

		/*	Получить модель рефлектометра.
		 * 	Реализация виртуальной функции класса OTDRController.*/
		OTDRModel getOTDRModel() const;

		/*	Установить параметры измерения.
		 * 	В случае неправильных значений возвращает FALSE.
		 * 	Реализация виртуальной функции класса OTDRController.*/
		BOOL setMeasurementParameters(const Parameter** parameters, const unsigned int parNumber);

	private:
		/*	Достать сведения о плате рефлектометра.
		 * 	Реализация виртуальной функции класса OTDRController.*/
		void retrieveOTDRPluginInfo();

		/*	Распечатать допустимые параметры измерений.
		 * 	Реализация виртуальной функции класса OTDRController.*/
		void printAvailableParameters() const;

		/*	Определить индекс параметра в массиве.*/
		int getWaveIndex(const int waveLength);
		int getRangeIndex(const double traceLength, const float wave);
		int getPointSpacingIndex(const double resolution);
		int getPulseWidthIndex(const short pulseWidth, const int flagPulseWidthLowRes, const float wave);
		BOOL iorIsValid(const double ior, const float wave);
		int getAveragesIndex(const double scans, const float wave);

		/*	Определить индекс числа в массиве.*/
		static int getIndexInArray(float val, float* array, int arraySize);
		static int getIndexInArray(DWORD val, DWORD* array, int arraySize);

		/*	Провести измерение.
		 * 	В случае успеха возвращает заново созданный объект Белкор. Иначе - NULL.
		 * 	Реализация виртуальной функции класса OTDRController.*/
		BellcoreStructure* runMeasurement() const;

		void fillBellcoreStructure(BellcoreStructure* bellcoreStructure,
			QPOTDRWaveformHeader* waveFormHeader,
			QPOTDRWaveformData*  waveFormData) const;
};

#endif /*QP1640OTDRCONTROLLER_H_*/
