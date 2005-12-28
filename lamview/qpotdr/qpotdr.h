// ONT.UT.QPOTDR.H Defines and exports

#if !defined(_ONT_UT_QPOTDR_H_INCLUDED_)
#define _ONT_UT_QPOTDR_H_INCLUDED_

#ifdef __cplusplus
extern "C" {
#endif

// Defines

#if defined(_DEBUG) && defined(QP_NEED_DUMMY_IMPL)
#define QP_DUMMY_IMPL_INT { return 0; }
#define QP_DUMMY_IMPL_FLOAT { return 0.0; }
#define QP_DUMMY_IMPL_BYTE { return 0; }
#define QP_DUMMY_IMPL_WORD { return 0; }
#define QP_DUMMY_IMPL_VOIDP { return 0; }
#define QP_DUMMY_IMPL_VOID { }
#else
#define QP_DUMMY_IMPL_INT ;
#define QP_DUMMY_IMPL_FLOAT ;
#define QP_DUMMY_IMPL_BYTE ;
#define QP_DUMMY_IMPL_WORD ;
#define QP_DUMMY_IMPL_VOIDP ;
#define QP_DUMMY_IMPL_VOID ;
#endif

#ifdef _DEBUG
// debug-only (no OTDR onboard) code
#if !defined QP_API_
#define QP_API_(type) type
#endif
#if !defined QP_API
#define QP_API
#endif
#else
// normal-mode code
#if !defined QP_API_
#define QP_API_(type) __declspec(dllimport) type WINAPI
#endif
#if !defined QP_API
#define QP_API __declspec(dllimport) HRESULT WINAPI
#endif

#endif

#define MAX_CARDS           8
#define MAX_WAVES           4
#define MAX_RANGES          16
#define MAX_PULSES          16
#define MAX_AVERAGES        3
#define MAX_SPACINGS        13
#define MAX_DIST_RANGES     10
#define	MAX_WFM_POINTS      0x40000
#define REAL_TIME_SCAN      1
#define LIVE_FIBER_DETECT   0x00010000
#define GAIN_SPLICE_ON      2

#define EV_TYPE_JUNK       -1  
#define EV_TYPE_LAUNCH      0   // 1st event, index 0.
#define EV_TYPE_REFL        1
#define EV_TYPE_NONREFL     2
#define EV_TYPE_GROUPED     3
#define EV_TYPE_END         4
#define EV_TYPE_QT_END      5

#define EV_SATURATED         0X00000001  // Saturated.
#define EV_END_OF_FIBER      0X00000002  // End of fiber event.
#define EV_LOSS_EXCEEDED     0X00000004  // Loss exceeds Loss Threshold.
#define EV_REFL_EXCEEDED     0X00000008  // Refl exceeds Refl Threshold.
#define EV_END_OF_DATA       0X00000100  // For launch event : no backscatter or multiple adjacent reflections.
#define EV_OUT_OF_RANGE      0X00000200  // Dynamic range exceeded.
#define EV_OUT_OF_DIST       0X00000400  // Distance range exceeded.
#define EV_RISING_EDGE       0X00002000  // Event ended on a rising edge.
#define EV_BACKSCATTER       0X00004000  // Launch or refl event ended on backscatter.
#define EV_REFL_CLAMPED      0X00040000  // Reflection is clamped.
#define EV_PW_RESO_ERR       0X00080000  // PW/Reso combination causes poorly resolved reflections.
#define EV_END_NTH_REFL      0X01000000  // End set to Nth Refl with Refl>=EndNthReflThreshold.

// Waveform data format
typedef unsigned short  QPOTDRWaveformData;

// Waveform signFlags format
typedef unsigned long   QPOTDRWaveformSignFlags;

// Waveform header, contains information about waveform
typedef struct
{
    unsigned short          updateData;         // Update data flag (TRUE=buffer is updated) 
    DWORD                   FPOffset;           // Front panel distance(m) from first data point.
    double                  BaseLine;			// Baseline
    double                  Reference;			// Reference
    unsigned long           Averages;           // # of acquisitions   
    double                  Noise;              // dB Rms noise * 1000
    int                     NumPts;             // # of data points 
    QPOTDRWaveformSignFlags   *SignFlags;         // array of sign flags
    size_t                  SignFlagSize;       // size of sign flag array
    BYTE                    gainSpliceFailed;   // Gain splice failed flag
} QPOTDRWaveformHeader;

// Plugin Info, information about plugin
typedef struct _PluginInfo
{
    char ModelNumber[32];						// Model number
    char SerialNumber[32];						// Serial Number
	char szLastCalibrationDate[16];				// Last Cal Date
	char szPartNumber[16];						// Part number / Revision
	char szModel[16];							// Model name
   	char szManufacturer[20];					// Manufacturer name
	char opt_file_version[10];					// OPT file version
	char dll_version[22];						// QPOTDR Version
} QPPluginInfo, *PQPPluginInfo;

// Plugin / DAU specific info
typedef struct 
{
	DWORD dwMaxDataPoints;						// Maximum data points supported
	float fBaseReso;							// Base reso
	WORD wRevision;								// Revision ID
	QPPluginInfo pluginInfo;					// Information about plugin
	DWORD dwMaxFastAvg;							// Maximum number of hardware averages
	DWORD dwFastScanCount;						// Number of averages in hardware per scan
} QPPluginData;

// Fiber Analysis Event info
typedef struct _QPOTDREVENT
{
    int iType;									// Event type
    DWORD dwStart;								// Event start data point
    DWORD dwEnd;								// Event end data point
    double dLoss;								// Event loss
    double dRefl;								// Event reflectance
    DWORD dwFlags;								// Special flags
} QPOTDREVENT, *PQPOTDREVENT;

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetMaxWaves
// card - card index
// Returns maximum number of waves for card
QP_API_(int) QPOTDRGetMaxWaves( int card ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetMaxPointSpacings
// card - card index
// Returns maximum number of point spacings for card
QP_API_(int) QPOTDRGetMaxPointSpacings( int card ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetMaxPulses
// card - card index
// wave - wavelength
// Returns maximum number of pulses for wave
QP_API_(int) QPOTDRGetMaxPulses( int card, float wave ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetMaxRanges
// card - card index
// wave - wavelength
// Returns maximum number of ranges for wave
QP_API_(int) QPOTDRGetMaxRanges( int card, float wave ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetMaxAverages
// card - card index
// wave - wavelength
// Returns maximum number of averages for wave
QP_API_(int) QPOTDRGetMaxAverages( int card, float wave ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetAvailWaves
// Gets available wavelengths 
// card - dau card index
// wavelengths - array of floats, element count = MAX_WAVES
QP_API_(int) QPOTDRGetAvailWaves( int card, float *wavelengths ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetAvailRanges
// Gets available ranges
// card - dau card index
// wave - wavelength
// ranges - array of floats, element count = MAX_RANGES
QP_API_(int) QPOTDRGetAvailRanges( int card, float wave, float *ranges) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetAvailPulses
// Gets available pulses
// card - dau card index
// wave - wavelength
// pulsewidths - array of DWORDs, element count = MAX_PULSES
QP_API_(int) QPOTDRGetAvailPulses( int card, float wave, DWORD *pulsewidths ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetDynamicRange
// Gets dynamic range for selected pulse
// card - dau card index
// wave - wavelength
// pulse - pulse index
// pulsewidths - array of DWORDs, element count = MAX_PULSES
QP_API_(int) QPOTDRGetDynamicRange( int card, float wave, int pulse, WORD *wDynamicRange ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetAvailAverages
// Gets available averages counts
// card - dau card index
// wave - wavelength index
// averages - array of DWORDs, element count = MAX_AVERAGES
QP_API_(int) QPOTDRGetAvailAverages( int card, float wave, DWORD *averages ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetAvailSpacings
// Gets available point spasings
// card - dau card index
// point_spacings - array of floats, element count = MAX_SPACINGS
QP_API_(int) QPOTDRGetAvailSpacings( int card, float *point_spacings ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetDefaultIOR
// wCard - card index
// wave - wavelength
// returns DefaultIOR
QP_API_(float) QPOTDRGetDefaultIOR(WORD wCard, float wave) QP_DUMMY_IMPL_FLOAT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetDefaultBSC
// wCard - card index
// wave - wavelength
// returns DefaultBSC
QP_API_(float) QPOTDRGetDefaultBSC(WORD wCard, float wave) QP_DUMMY_IMPL_FLOAT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetFiberType
// wCard - card index
// wave - wavelength
// returns Fiber type [0 = MM or 1 = SM]
QP_API_(BYTE) QPOTDRGetFiberType(WORD wCard, float wave) QP_DUMMY_IMPL_BYTE

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetCards
// wCardIndexes - card indexes array of type WORD element count = MAX_CARDS
// returns card count
QP_API_(WORD) QPOTDRGetCards(WORD *wCardIndexes) QP_DUMMY_IMPL_WORD

////////////////////////////////////////////////////////////////////////////////
// QPOTDRInitialize
// Used to initialize the hardware.  In addition, the plugin is read,
// and memory is cleared.  This function must be called before using
// the card.
// Return error codes
// -1   Bad CardId
// -2   Unable to get card status
// -4   Unable to get Xilinx bitstream
// -5   LSR_PWR Initialize Failed
// -6   APD_VOLT Initialize Failed
// -7   PREAMP Initialize Failed
// -8   LSR_LTS Initialize Failed
// -9   LSR Initialize Failed
// -10  LTS_FREQ Initialize Failed
// -11  LTS_PWR Initialize Failed
// -12  PM_VFL Initialize Failed
// -13  LSR_COOL Initialize Failed
// -15  Xilinx General filure
// -16  Xilinx Bad parameter
// -17  Xilinx Bad CardID
// -18  Xilinx Data Block Start
// -19  Xilinx Data Block Middle
// -20  Xilinx Data Block End
// -22  Xilinx Self Test
// -23  EEPROM read error/bad CRC16
// -24  OPT version incorrect
// -25  Unable to determine interleave
// -26  Memory allocation error
// -27  Memory allocation error
// -28  Memory allocation error
// -29  Memory allocation error
// -30  Memory allocation error
// -31  Memory allocation error
// -32  Memory allocation error
// -33  CD OPT structure bad CRC16
// 0    Success
QP_API_(int) QPOTDRInitialize( int cardid ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetOTDRLaserCount
// wCard - card index
// Returns number of lasers in DAU
QP_API_(BYTE) QPOTDRGetOTDRLaserCount( WORD wCard ) QP_DUMMY_IMPL_BYTE

////////////////////////////////////////////////////////////////////////////////
// QPOTDRDataCollectInfo
// card - card index
// pPluginData - pointer to  QPOTDRPluginData structure
QP_API_(void) QPOTDRDataCollectInfo(WORD card, QPPluginData *pPluginData) QP_DUMMY_IMPL_VOID

////////////////////////////////////////////////////////////////////////////////
// QPOTDRAcqSetParams
// wCard - card index
// wAverages - number of averages (number of fast scan counts)
// wWave - wave index
// dwRange - range (number of data points)
// wPointSpacing - point spacing index
// wPulse - pulse index
// wFilter - filter on/off flag
// wUpdateCount - update count (multiple of fast scan count)
// Returns:
// 0  - Success
// -1 - bad card index
// -2 - bad number of averages
// -3 - bad wave index
// -4 - bad pulse index
// -5 - bad range
// -6 - bad point spacing index
QP_API_(int) QPOTDRAcqSetParams(  WORD    wCard,
                                    WORD    wAverages,
                                    WORD    wWave,
                                    DWORD   dwRange,
                                    WORD    wPointSpacing,
                                    WORD    wPulse,
                                    WORD    wUpdateCount) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRAcqStart
// Starts DAU
// card - card index
// avgPts - average points (array of 20 elements) [has to be passed, but not currently used]
// flags - flags []
QP_API_(HANDLE *) QPOTDRAcqStart( int card, DWORD flags) QP_DUMMY_IMPL_VOIDP

////////////////////////////////////////////////////////////////////////////////
// QPOTDRRetrieveWaveformSync
// card - card index
// h - Waveform header
// w - Waveform data
// lastTrace - boolean for last trace notification
QP_API_(int) QPOTDRRetrieveWaveformSync( int card, QPOTDRWaveformHeader *h, QPOTDRWaveformData *w, BOOL lastTrace ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRAcqStop
// Stops DAU
// card - card index
QP_API_(int) QPOTDRAcqStop( int card ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRFilterLastWaveform
// Filters last waveform using boxcar filter
// card - card index
// w - Waveform data
QP_API_(int) QPOTDRFilterLastWaveform( WORD card, QPOTDRWaveformData *w ) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRSetFASParms
// wCard - Card index
// dLossThreshDB - Loss threshold
// dReflThreshDB - Reflectance threshold
// dBreakThreshDB - Fiber Break Threshold
// nNumBreaks - End set to Nth event >= Fiber Break Threshold.
// nEndAtNthRefl - <= 0 then no change of end >0   then set end to Nth Refl event
QP_API_(void) QPOTDRSetFASParms(WORD wCard,
                                double dLossThreshDB,
                                double dReflThreshDB,
                                double dBreakThreshDB,
                                LONG   nNumBreaks,
                                LONG   nEndAtNthRefl,
                                double dNthReflThresh) QP_DUMMY_IMPL_VOID

////////////////////////////////////////////////////////////////////////////////
// QPOTDRFAS
// Runs Fiber Analysis Software : finds features and events
// return 0 if success
QP_API_(int) QPOTDRFAS(WORD wCard) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetEvents
// Fills in event info
// wCard - card index
// events - array of events (call QPOTDRGetEventCount for size)
// element_count - count of elements in events array
// return number of events
QP_API_(int) QPOTDRGetEvents(WORD wCard, DWORD element_count, PQPOTDREVENT events) QP_DUMMY_IMPL_INT

////////////////////////////////////////////////////////////////////////////////
// QPOTDRGetEventCount
// wCard - card index
// returns number of events
QP_API_(int) QPOTDRGetEventCount(WORD wCard) QP_DUMMY_IMPL_INT

#ifdef __cplusplus
}
#endif

#endif // _ONT_UT_QPOTDR_H_INCLUDED_
