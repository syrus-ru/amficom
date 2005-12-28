// ACQSettings.cpp : implementation file
//

#include "stdafx.h"
#include "OEMDemo.h"
#include "ACQSettings.h"
#include "TraceEvents.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CACQSettings dialog


CACQSettings::CACQSettings(CWnd* pParent /*=NULL*/)
	: CDialog(CACQSettings::IDD, pParent)
{
	//{{AFX_DATA_INIT(CACQSettings)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT

    pAD = NULL;
    memset(&cs, 0, sizeof(CURRENT_SETTINGS));
}


void CACQSettings::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CACQSettings)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CACQSettings, CDialog)
	//{{AFX_MSG_MAP(CACQSettings)
	ON_CBN_SELCHANGE(IDC_COMBO_WAVE, OnSelchangeComboWave)
	ON_CBN_SELCHANGE(IDC_COMBO_CARD, OnSelchangeComboCard)
	ON_CBN_SELCHANGE(IDC_COMBO_PULSE, OnSelchangeComboPulse)
	ON_BN_CLICKED(IDC_RADIO_REAL_TIME, OnRadioRealTime)
	ON_BN_CLICKED(IDC_RADIO_AVERAGE, OnRadioAverage)
	ON_CBN_SELCHANGE(IDC_COMBO_RANGE, OnSelchangeComboRange)
	ON_CBN_SELCHANGE(IDC_COMBO_PS, OnSelchangeComboPs)
	ON_CBN_SELCHANGE(IDC_COMBO_BUC, OnSelchangeComboBuc)
	ON_CBN_SELCHANGE(IDC_COMBO_AVG, OnSelchangeComboAvg)
	ON_BN_CLICKED(IDC_CHECK_FILTER, OnCheckFilter)
	ON_BN_CLICKED(IDC_CHECK_GAIN_SPLICE, OnCheckGainSplice)
	ON_BN_CLICKED(IDC_CHECK_LFD, OnCheckLfd)
	ON_CBN_SELCHANGE(IDC_COMBO_PORT, OnSelchangeComboPort)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CACQSettings message handlers

BOOL CACQSettings::OnInitDialog() 
{
	CDialog::OnInitDialog();
	
    UpdateView();
	// TODO: Add extra initialization here
	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

void CACQSettings::SetAppDataPointer(PAPP_DATA pAppData)
{
    pAD = pAppData;
}


void CACQSettings::OnOK() 
{
	// TODO: Add extra validation here
    int status;
	
    // Try to set current parameters
    status = QPOTDRAcqSetParams(
        pAD->wCardIndexes[cs.wCard],
        (WORD)(pAD->dau[cs.wCard].waveInfo[cs.wWave].dwAverages[cs.wAverages] / pAD->dau[cs.wCard].pluginData.dwFastScanCount),
        cs.wWave,
        (DWORD)((pAD->dau[cs.wCard].waveInfo[cs.wWave].fRanges[cs.wRange] * 1000.0f) / min(pAD->dau[cs.wCard].fPointSpacings[cs.wPointSpacing], 4.0f)),
        cs.wPointSpacing,
        cs.wPulse,
        cs.wUpdateCount + 1);

    switch( status )
    {
    case 0:
        // AfxMessageBox("Setting parameters: success");
        break;
    case -1:
        AfxMessageBox("Setting parameters: bad card index");
        return;
    case -2:
        AfxMessageBox("Setting parameters: bad number of averages");
        return;
    case -3:
        AfxMessageBox("Setting parameters: bad wave index");
        return;
    case -4:
        AfxMessageBox("Setting parameters: bad pulse index");
        return;
    case -5:
        AfxMessageBox("Setting parameters: bad range");
        return;
    case -6:
        AfxMessageBox("Setting parameters: bad point spacing index");
        return;
    default:
        AfxMessageBox("Setting parameters: unknown error code");
        return;
    }

    memcpy(&pAD->cs, &cs, sizeof(CURRENT_SETTINGS));
	CDialog::OnOK();
}

void CACQSettings::OnCancel() 
{
	// TODO: Add extra cleanup here
	
	CDialog::OnCancel();
}

int CACQSettings::DoModal() 
{
	// TODO: Add your specialized code here and/or call the base class
	if( pAD == NULL )
    {
        AfxMessageBox("Set CurrentSettings pointer first");
        return -1;
    }

    memcpy(&cs, &pAD->cs, sizeof(CURRENT_SETTINGS));
	return CDialog::DoModal();
}

void CACQSettings::UpdateView(void)
{
    CString str;
    WORD index;
    CComboBox *cBox;
    CButton *btn;

    // Display current settings
    // or the one selected by user
    GetDlgItem(IDC_STATIC_MODEL_NUMBER)->SetWindowText(pAD->dau[cs.wCard].pluginData.pluginInfo.ModelNumber);
    GetDlgItem(IDC_STATIC_SERIAL_NUMBER)->SetWindowText(pAD->dau[cs.wCard].pluginData.pluginInfo.SerialNumber);
    GetDlgItem(IDC_STATIC_LAST_CAL_DATE)->SetWindowText(pAD->dau[cs.wCard].pluginData.pluginInfo.szLastCalibrationDate);
    GetDlgItem(IDC_STATIC_PART_NUM)->SetWindowText(pAD->dau[cs.wCard].pluginData.pluginInfo.szPartNumber);
    GetDlgItem(IDC_STATIC_MODEL)->SetWindowText(pAD->dau[cs.wCard].pluginData.pluginInfo.szModel);
    GetDlgItem(IDC_STATIC_MAN)->SetWindowText(pAD->dau[cs.wCard].pluginData.pluginInfo.szManufacturer);
    GetDlgItem(IDC_STATIC_OPT_VERSION)->SetWindowText(pAD->dau[cs.wCard].pluginData.pluginInfo.opt_file_version);
    GetDlgItem(IDC_STATIC_DLL_VERSION)->SetWindowText(pAD->dau[cs.wCard].pluginData.pluginInfo.dll_version);
    str.Format("%d", pAD->dau[cs.wCard].pluginData.dwMaxDataPoints);
    GetDlgItem(IDC_STATIC_MAX_DATA_PTS)->SetWindowText(str);
    str.Format("%f", pAD->dau[cs.wCard].pluginData.fBaseReso);
    GetDlgItem(IDC_STATIC_BASE_RESO)->SetWindowText(str);
    str.Format("%d", pAD->dau[cs.wCard].pluginData.wRevision);
    GetDlgItem(IDC_STATIC_REVISION)->SetWindowText(str);
    str.Format("%d", pAD->dau[cs.wCard].pluginData.dwMaxFastAvg);
    GetDlgItem(IDC_STATIC_MAX_FAST_AVG)->SetWindowText(str);
    str.Format("%d", pAD->dau[cs.wCard].pluginData.dwFastScanCount);
    GetDlgItem(IDC_STATIC_FAST_SCAN_COUNT)->SetWindowText(str);

    // Set settings
    // DAU Cards
    cBox = (CComboBox *)GetDlgItem(IDC_COMBO_CARD);
    cBox->ResetContent();
    for( index = 0; index < pAD->wDAUCount; index++ )
    {
        str.Format("DAU Card %d", pAD->wCardIndexes[index]);
        cBox->AddString(str);
    }
    cBox->SetCurSel(cs.wCard);

    // Lasers of current card
    cBox = (CComboBox *)GetDlgItem(IDC_COMBO_WAVE);
    cBox->ResetContent();
    for( index = 0; index < pAD->dau[cs.wCard].wLaserCount; index++ )
    {
        str.Format("%f", pAD->dau[cs.wCard].waveInfo[index].fWave);
        cBox->AddString(str);
    }
    cBox->SetCurSel(cs.wWave);

	str.Format("%f", pAD->dau[cs.wCard].waveInfo[cs.wWave].fDefaultIOR);
	GetDlgItem(IDC_STATIC_IOR)->SetWindowText(str);

	str.Format("%f", pAD->dau[cs.wCard].waveInfo[cs.wWave].fDefaultBSC);
	GetDlgItem(IDC_STATIC_BSC)->SetWindowText(str);

	if( pAD->dau[cs.wCard].waveInfo[cs.wWave].byFiberType == 1)
	{
		GetDlgItem(IDC_STATIC_FIBER_TYPE)->SetWindowText("SM");
	}
	else if( pAD->dau[cs.wCard].waveInfo[cs.wWave].byFiberType == 0)
	{
		GetDlgItem(IDC_STATIC_FIBER_TYPE)->SetWindowText("MM");
	}
	else
	{
		GetDlgItem(IDC_STATIC_FIBER_TYPE)->SetWindowText("??");
	}

    // Pulses of current laser
    cBox = (CComboBox *)GetDlgItem(IDC_COMBO_PULSE);
    cBox->ResetContent();
    for( index = 0; index < MAX_PULSES; index++ )
    {
        if( pAD->dau[cs.wCard].waveInfo[cs.wWave].piPulses[index].wPulse )
        {
            // Display Hi Res / Long Hall flags
            str.Format("%d %s",
                        pAD->dau[cs.wCard].waveInfo[cs.wWave].piPulses[index].wPulse,
                        pAD->dau[cs.wCard].waveInfo[cs.wWave].piPulses[index].bHiRes ? "Hi Res" : "Long Hall" );
            cBox->AddString(str);
        }
    }
    cBox->SetCurSel(cs.wPulse);

    // Preset default filter flag for current pulse
    btn = (CButton *)GetDlgItem(IDC_CHECK_GAIN_SPLICE);
    if( cs.wGainSplice == 0 )
    {
        if( pAD->dau[cs.wCard].waveInfo[cs.wWave].piPulses[cs.wPulse].bGainSplice )
        {
            btn->SetCheck(TRUE);
            cs.wGainSplice = 2;
        }
        else
        {
            btn->SetCheck(FALSE);
            cs.wGainSplice = 1;
        }
    }
    else
    {
        btn->SetCheck(cs.wGainSplice >> 1);
    }

    // Preset default filter flag for current pulse
    btn = (CButton *)GetDlgItem(IDC_CHECK_FILTER);
    if( cs.wFilter == 0 )
    {
        if( pAD->dau[cs.wCard].waveInfo[cs.wWave].piPulses[cs.wPulse].bFilter )
        {
            btn->SetCheck(TRUE);
            cs.wFilter = 2;
        }
        else
        {
            btn->SetCheck(FALSE);
            cs.wFilter = 1;
        }
    }
    else
    {
        btn->SetCheck(cs.wFilter >> 1);
    }

	// Preset default Fiber Check flag
	btn = (CButton *)GetDlgItem(IDC_CHECK_LFD);
    if( cs.wLifeFiberDetect == 0 )
    {
        btn->SetCheck(TRUE);
        cs.wLifeFiberDetect = 2;
    }
    else
    {
        btn->SetCheck(cs.wLifeFiberDetect >> 1);
    }

    // Scan mode
    btn = (CButton *)GetDlgItem(IDC_RADIO_REAL_TIME);
    if( cs.bScanMode )
    {
        btn->SetCheck(FALSE);
        btn = (CButton *)GetDlgItem(IDC_RADIO_AVERAGE);
        btn->SetCheck(TRUE);
    }
    else
    {
        btn->SetCheck(TRUE);
        btn = (CButton *)GetDlgItem(IDC_RADIO_AVERAGE);
        btn->SetCheck(FALSE);
    }

    // Ranges for current laser
    cBox = (CComboBox *)GetDlgItem(IDC_COMBO_RANGE);
    cBox->ResetContent();
    for( index = 0; index < MAX_RANGES; index++ )
    {
        if( pAD->dau[cs.wCard].waveInfo[cs.wWave].fRanges[index] )
        {
            str.Format("%f ךל", pAD->dau[cs.wCard].waveInfo[cs.wWave].fRanges[index]);
            cBox->AddString(str);
        }
    }
    cBox->SetCurSel(cs.wRange);

    // Point spacing for current DAU card
    cBox = (CComboBox *)GetDlgItem(IDC_COMBO_PS);
    cBox->ResetContent();
    for( index = 0; index < MAX_SPACINGS; index++ )
    {
        if( pAD->dau[cs.wCard].fPointSpacings[index] )
        {
            str.Format("%f ל", pAD->dau[cs.wCard].fPointSpacings[index]);
            cBox->AddString(str);
        }
    }
    cBox->SetCurSel(cs.wPointSpacing);

    // How offten should we get update of buffer for redraw
    cBox = (CComboBox *)GetDlgItem(IDC_COMBO_BUC);
    cBox->ResetContent();
    for( index = 0; index < 4; index++ )
    {
        str.Format("%d", (index + 1) * pAD->dau[cs.wCard].pluginData.dwFastScanCount);
        cBox->AddString(str);
    }
    cBox->SetCurSel(cs.wUpdateCount);

    // How many averages do we allow
    // !!! (MUST be multiple of pAD->dau[cs.wCard].pluginData.dwFastScanCount) !!!
    cBox = (CComboBox *)GetDlgItem(IDC_COMBO_AVG);
    cBox->ResetContent();
    for( index = 0; index < MAX_AVERAGES; index++ )
    {
        if( pAD->dau[cs.wCard].waveInfo[cs.wWave].dwAverages[index] )
            str.Format("%d", pAD->dau[cs.wCard].waveInfo[cs.wWave].dwAverages[index]);
            cBox->AddString(str);
    }
    cBox->SetCurSel(cs.wAverages);

    cBox = (CComboBox *)GetDlgItem(IDC_COMBO_PORT);
    cBox->ResetContent();
    const int N_PORTS = pAD->bgs != 0 && !pAD->bgs->c_isError() ? 8 : 0; // XXX: fixed number of ports
    for( index = 0; index <= N_PORTS; index++ )
    {
        if(index == 0)
            str.Format("Ïמנע OTDR");
        else
            str.Format("%d-י ןמנע OTAU", index);
        cBox->AddString(str);
    }
    cBox->SetCurSel(cs.wOTAUPort);
}

// User changed wavelength
void CACQSettings::OnSelchangeComboWave()
{
	// TODO: Add your control notification handler code here
    CComboBox *cBox;
    cBox = (CComboBox *)GetDlgItem(IDC_COMBO_WAVE);

    if( cBox->GetCurSel() != cs.wWave )
    {
        cs.wWave = cBox->GetCurSel();
        cs.wPulse = 0;
        cs.wRange = 0;
        cs.wAverages = 0;
        cs.wGainSplice = 0;
        cs.wFilter = 0;
		cs.wLifeFiberDetect = 0;
        UpdateView();
    }
}

// User changed DAU card
void CACQSettings::OnSelchangeComboCard() 
{
	// TODO: Add your control notification handler code here
    CComboBox *cBox;
    cBox = (CComboBox *)GetDlgItem(IDC_COMBO_CARD);

    if( cBox->GetCurSel() != cs.wCard )
    {
        cs.wCard = cBox->GetCurSel();
        cs.wWave = 0;
        cs.wPulse = 0;
        cs.wRange = 0;
        cs.wPointSpacing = 0;
        cs.wUpdateCount = 0;
        cs.wAverages = 0;
        cs.wGainSplice = 0;
        cs.wFilter = 0;
		cs.wLifeFiberDetect = 0;
        UpdateView();
    }	
}

// User changed pulse
void CACQSettings::OnSelchangeComboPulse() 
{
	// TODO: Add your control notification handler code here
    CComboBox *cBox;
    cBox = (CComboBox *)GetDlgItem(IDC_COMBO_PULSE);

    if( cBox->GetCurSel() != cs.wPulse )
    {
        cs.wPulse = cBox->GetCurSel();
        cs.wGainSplice = 0;
        cs.wFilter = 0;
		cs.wLifeFiberDetect = 0;
        UpdateView();
    }
}

// User changed scan mode
void CACQSettings::OnRadioRealTime() 
{
	// TODO: Add your control notification handler code here
    cs.bScanMode = FALSE;
}

void CACQSettings::OnRadioAverage() 
{
	// TODO: Add your control notification handler code here
    cs.bScanMode = TRUE;	
}

// User changed range
void CACQSettings::OnSelchangeComboRange() 
{
	// TODO: Add your control notification handler code here
    cs.wRange = ((CComboBox *)GetDlgItem(IDC_COMBO_RANGE))->GetCurSel();
}

// User changed point spacing
void CACQSettings::OnSelchangeComboPs() 
{
	// TODO: Add your control notification handler code here
    cs.wPointSpacing = ((CComboBox *)GetDlgItem(IDC_COMBO_PS))->GetCurSel();
}

// User changed update count
void CACQSettings::OnSelchangeComboBuc() 
{
	// TODO: Add your control notification handler code here
    cs.wUpdateCount = ((CComboBox *)GetDlgItem(IDC_COMBO_BUC))->GetCurSel();
}

// User changed average count
void CACQSettings::OnSelchangeComboAvg() 
{
	// TODO: Add your control notification handler code here
    cs.wAverages = ((CComboBox *)GetDlgItem(IDC_COMBO_AVG))->GetCurSel();
}

void CACQSettings::OnCheckFilter() 
{
	// TODO: Add your control notification handler code here
    CButton *btn;

    btn = (CButton *)GetDlgItem(IDC_CHECK_FILTER);
    cs.wFilter = btn->GetCheck() + 1;
}

void CACQSettings::OnCheckGainSplice() 
{
	// TODO: Add your control notification handler code here
    CButton *btn;

    btn = (CButton *)GetDlgItem(IDC_CHECK_GAIN_SPLICE);
    cs.wGainSplice = btn->GetCheck() + 1;
}

void CACQSettings::OnCheckLfd() 
{
	// TODO: Add your control notification handler code here
    CButton *btn;

    btn = (CButton *)GetDlgItem(IDC_CHECK_LFD);
    cs.wLifeFiberDetect = btn->GetCheck() + 1;
}

void CACQSettings::OnSelchangeComboPort() 
{
	// TODO: Add your control notification handler code here
	cs.wOTAUPort = ((CComboBox *)GetDlgItem(IDC_COMBO_PORT))->GetCurSel();
}
