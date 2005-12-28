// TraceEvents.cpp : implementation file
//

#include "stdafx.h"
#include "OEMDemo.h"
#include "TraceEvents.h"
#include <string.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CTraceEvents dialog


CTraceEvents::CTraceEvents(CWnd* pParent /*=NULL*/)
	: CDialog(CTraceEvents::IDD, pParent)
{
	//{{AFX_DATA_INIT(CTraceEvents)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT

    pAD = NULL;
}


void CTraceEvents::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CTraceEvents)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}

void CTraceEvents::SetAppDataPointer(PAPP_DATA pAppData)
{
    pAD = pAppData;
}

BEGIN_MESSAGE_MAP(CTraceEvents, CDialog)
	//{{AFX_MSG_MAP(CTraceEvents)
	ON_NOTIFY(NM_DBLCLK, IDC_LIST_TRACE_EVENTS, OnDblclkListTraceEvents)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CTraceEvents message handlers
void CTraceEvents::RunFAS(void)
{
    DWORD index;

	// TODO: Add extra initialization here
    if( pAD->dau[pAD->cs.wCard].events != NULL )
    {
        delete [] pAD->dau[pAD->cs.wCard].events;
        pAD->dau[pAD->cs.wCard].events = NULL;
        pAD->dau[pAD->cs.wCard].dwEventCount = 0;
    }

    listEvents->DeleteAllItems();
    QPOTDRFAS(pAD->wCardIndexes[pAD->cs.wCard]);
    pAD->dau[pAD->cs.wCard].dwEventCount = QPOTDRGetEventCount(pAD->wCardIndexes[pAD->cs.wCard]);
    pAD->dau[pAD->cs.wCard].events = new QPOTDREVENT[pAD->dau[pAD->cs.wCard].dwEventCount];
    QPOTDRGetEvents(pAD->wCardIndexes[pAD->cs.wCard],
                    pAD->dau[pAD->cs.wCard].dwEventCount,
                    pAD->dau[pAD->cs.wCard].events);

    for( index = 0; index < pAD->dau[pAD->cs.wCard].dwEventCount; index++ )
    {
        AddEvent(   index, &pAD->dau[pAD->cs.wCard].events[index] );
    }

    AfxGetMainWnd()->Invalidate();
}

BOOL CTraceEvents::OnInitDialog() 
{
	CDialog::OnInitDialog();
    char *str[] = {"###", "Type", "Start", "End", "Loss", "Refl", "Flags"};
    LVCOLUMN column;
    int index;

    listEvents = (CListCtrl *)GetDlgItem(IDC_LIST_TRACE_EVENTS);
    listEvents->SetExtendedStyle(LVS_EX_FULLROWSELECT);
    listEvents->DeleteAllItems();

    column.mask = LVCF_FMT | LVCF_SUBITEM | LVCF_TEXT | LVCF_WIDTH;
    column.fmt = LVCFMT_LEFT;

    for( index = 0; index < 7; index++ )
    {
        column.iSubItem = index;
        column. cx = listEvents->GetStringWidth(str[index]) * 2;
        column.pszText = str[index];
        column.cchTextMax = strlen(str[index]);
        listEvents->InsertColumn(index, &column);
    }

    SetDlgItemText(IDC_EDIT_LOSS, "0.0300");
    SetDlgItemText(IDC_EDIT_REFL, "-40.0000");
    SetDlgItemText(IDC_EDIT_FIBER_BREAK, "3.0000");
    SetDlgItemText(IDC_EDIT_NTH_END, "1");
    SetDlgItemText(IDC_EDIT_NTH_REFL_END, "0");
    SetDlgItemText(IDC_EDIT_NTH_REFL_THRSH, "-40.0000");

    return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

void CTraceEvents::AddEvent(int index, PQPOTDREVENT event)
{
    LVITEM item;
    int colWidth[7], temp, zkm;
    double startKM, endKM;
    int type;
    DWORD start;
    DWORD end;
    double loss;
    double refl;
    DWORD flags;

    type = event->iType;
    start = event->dwStart;
    end = event->dwEnd;
    loss = event->dLoss;
    refl = event->dRefl;
    flags = event->dwFlags;
    
    zkm = pAD->waveFormHeader.FPOffset >> 16;
    startKM = ((start - zkm) * pAD->dau[pAD->cs.wCard].fPointSpacings[pAD->cs.wPointSpacing]) / 1000.0f;
    endKM = ((end - zkm) * pAD->dau[pAD->cs.wCard].fPointSpacings[pAD->cs.wPointSpacing]) / 1000.0f;

    colWidth[0] = listEvents->GetColumnWidth(0);
    colWidth[1] = listEvents->GetColumnWidth(1);
    colWidth[2] = listEvents->GetColumnWidth(2);
    colWidth[3] = listEvents->GetColumnWidth(3);
    colWidth[4] = listEvents->GetColumnWidth(4);
    colWidth[5] = listEvents->GetColumnWidth(5);
    colWidth[6] = listEvents->GetColumnWidth(6);

    sprintf(buffer, "%3d", index);
    item.mask = LVIF_TEXT;
    item.iItem = index;
    item.iSubItem = 0;
    item.pszText = buffer;
    item.cchTextMax = strlen(buffer);
    item.lParam = (LPARAM)event;
    listEvents->InsertItem(&item);
    temp = listEvents->GetStringWidth(buffer) * 2;
    listEvents->SetItemData(index, (LPARAM)event);

    if( colWidth[0] < temp )
    {
        colWidth[0] = temp;
    }

    switch( type )
    {
    case EV_TYPE_JUNK:
        strcpy(buffer, "Junk");
        break;
    case EV_TYPE_LAUNCH:
        strcpy(buffer, "Launch");
        break;
    case EV_TYPE_REFL:
        strcpy(buffer, "Refl");
        break;
    case EV_TYPE_NONREFL:
        strcpy(buffer, "NonRefl");
        break;
    case EV_TYPE_GROUPED:
        strcpy(buffer, "Grouped");
        break;
    case EV_TYPE_END:
        strcpy(buffer, "End");
        break;
    case EV_TYPE_QT_END:
        strcpy(buffer, "? End");
        break;
    default:
        strcpy(buffer, "???");
        break;
    }

    item.iSubItem = 1;
    item.cchTextMax = strlen(buffer);
    listEvents->SetItem(&item);
    temp = listEvents->GetStringWidth(buffer) * 2;
    if( colWidth[1] < temp )
    {
        colWidth[1] = temp;
    }

    sprintf(buffer, "%f", startKM);
    item.iSubItem = 2;
    item.cchTextMax = strlen(buffer);
    listEvents->SetItem(&item);
    temp = listEvents->GetStringWidth(buffer) * 2;
    if( colWidth[2] < temp )
    {
        colWidth[2] = temp;
    }
 
    sprintf(buffer, "%f", endKM);
    item.iSubItem = 3;
    item.cchTextMax = strlen(buffer);
    listEvents->SetItem(&item);
    temp = listEvents->GetStringWidth(buffer) * 2;
    if( colWidth[3] < temp )
    {
        colWidth[3] = temp;
    }

    sprintf(buffer, "%f", loss);
    item.iSubItem = 4;
    item.cchTextMax = strlen(buffer);
    listEvents->SetItem(&item);
    temp = listEvents->GetStringWidth(buffer) * 2;
    if( colWidth[4] < temp )
    {
        colWidth[4] = temp;
    }

    sprintf(buffer, "%f", refl);
    item.iSubItem = 5;
    item.cchTextMax = strlen(buffer);
    listEvents->SetItem(&item);
    temp = listEvents->GetStringWidth(buffer) * 2;
    if( colWidth[5] < temp )
    {
        colWidth[5] = temp;
    }

    sprintf(buffer, "0x%x ", flags);
    if( flags & EV_SATURATED )
        strcat(buffer, "Saturated. ");
    if( flags & EV_END_OF_FIBER )
        strcat(buffer, "End of Fiber. ");
    if( flags & EV_LOSS_EXCEEDED )
        strcat(buffer, ">Loss. ");
    if( flags & EV_REFL_EXCEEDED )
        strcat(buffer, ">Refl. ");
    if( flags & EV_END_OF_DATA )
        strcat(buffer, "No backscatter or multiple adjacent reflections. ");
    if( flags & EV_OUT_OF_RANGE )
        strcat(buffer, "Dynamic range exceeded. ");
    if( flags & EV_OUT_OF_DIST )
        strcat(buffer, "Distance range exceeded. ");
    if( flags & EV_RISING_EDGE )
        strcat(buffer, "On a rising edge. ");
    if( flags & EV_BACKSCATTER )
        strcat(buffer, "On backscatter. ");
    if( flags & EV_REFL_CLAMPED )
        strcat(buffer, "Clamped. ");
    if( flags & EV_PW_RESO_ERR )
        strcat(buffer, "Bad PW/Reso. ");
    if( flags & EV_END_NTH_REFL )
        strcat(buffer, "End set to Nth Refl. ");

    item.iSubItem = 6;
    item.cchTextMax = strlen(buffer);
    listEvents->SetItem(&item);
    temp = listEvents->GetStringWidth(buffer) * 2;
    if( colWidth[6] < temp )
    {
        colWidth[6] = temp;
    }

    listEvents->SetColumnWidth(0, colWidth[0]);
    listEvents->SetColumnWidth(1, colWidth[1]);
    listEvents->SetColumnWidth(2, colWidth[2]);
    listEvents->SetColumnWidth(3, colWidth[3]);
    listEvents->SetColumnWidth(4, colWidth[4]);
    listEvents->SetColumnWidth(5, colWidth[5]);
    listEvents->SetColumnWidth(6, colWidth[6]);
}

void CTraceEvents::OnOK() 
{
	// TODO: Add extra validation here
    double dLossThreshDB;
    double dReflThreshDB;
    double dBreakThreshDB;
    LONG   nNumBreaks;
    LONG   nEndAtNthRefl;
    double dNthReflThresh;

    CString str;

    GetDlgItemText(IDC_EDIT_LOSS, str);
    dLossThreshDB = atof(str);
    GetDlgItemText(IDC_EDIT_REFL, str);
    dReflThreshDB = atof(str);
    GetDlgItemText(IDC_EDIT_FIBER_BREAK, str);
    dBreakThreshDB = atof(str);
    GetDlgItemText(IDC_EDIT_NTH_END, str);
    nNumBreaks = atoi(str);
    GetDlgItemText(IDC_EDIT_NTH_REFL_END, str);
    nEndAtNthRefl = atoi(str);
    GetDlgItemText(IDC_EDIT_NTH_REFL_THRSH, str);
    dNthReflThresh = atof(str);

    QPOTDRSetFASParms(  pAD->wCardIndexes[pAD->cs.wCard],
                        dLossThreshDB,
                        dReflThreshDB,
                        dBreakThreshDB,
                        nNumBreaks,
                        nEndAtNthRefl,
                        dNthReflThresh);

                                
    RunFAS();
}

void CTraceEvents::OnCancel() 
{
	// TODO: Add extra cleanup here
	
	CDialog::OnCancel();
}

void CTraceEvents::OnDblclkListTraceEvents(NMHDR* pNMHDR, LRESULT* pResult) 
{
	// TODO: Add your control notification handler code here

    int index;
    
    index = listEvents->GetNextItem(-1, LVNI_SELECTED);
    pAD->dau[pAD->cs.wCard].pCurrentEvent = (PQPOTDREVENT)listEvents->GetItemData(index);
    
    AfxGetMainWnd()->Invalidate();
	*pResult = 0;
}
