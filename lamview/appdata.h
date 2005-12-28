// OEM OTDR APP DATA

#if !defined(APPDATA_H_INCLUDED_)
#define APPDATA_H_INCLUDED_

#include "bgswitcher.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct _PulseInfo
{
    WORD wPulse;
    BOOL bGainSplice;
    BOOL bFilter;
    BOOL bHiRes;
    WORD wDynamicRange;
} PulseInfo, *PPulseInfo;

// Info about Laser
typedef struct _LaserInfo
{
	float fWave;
    float fRanges[MAX_RANGES];
    PulseInfo piPulses[MAX_PULSES];
    DWORD dwAverages[MAX_AVERAGES];
	float fDefaultIOR;
	float fDefaultBSC;
	BYTE byFiberType;
} LaserInfo, *PLaserInfo;

// Info about DAU
typedef struct _DAU
{
    QPPluginData        pluginData;
    LaserInfo           waveInfo[MAX_WAVES];
    WORD                wLaserCount;
    float               fPointSpacings[MAX_SPACINGS];
    PQPOTDREVENT        events;
    DWORD               dwEventCount;
    PQPOTDREVENT        pCurrentEvent;
} DAU, *PDAU;

// Current settings
typedef struct
{
    WORD    wCard;
    WORD    wAverages;
    WORD    wWave;
    WORD    wRange;
    WORD    wPointSpacing;
    WORD    wPulse;
    WORD    wUpdateCount;
    BOOL    bScanMode;
    WORD    wFilter;
    WORD    wGainSplice;
	WORD    wLifeFiberDetect;
	WORD	wOTAUPort;
} CURRENT_SETTINGS, *PCURRENT_SETTINGS;

typedef struct
{
    CURRENT_SETTINGS cs;
    DAU     dau[MAX_CARDS];
    WORD    wDAUCount;
    WORD    wCardIndexes[MAX_CARDS];
    QPOTDRWaveformData waveFormData[MAX_WFM_POINTS];
    QPOTDRWaveformHeader waveFormHeader;
    HANDLE *hEvents;
    BOOL bDrawTrace;
	BgSwitcher *bgs;
} APP_DATA, *PAPP_DATA;

#ifdef __cplusplus
}
#endif

#endif // APPDATA_H_INCLUDED_