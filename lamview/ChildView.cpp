// ChildView.cpp : implementation of the CChildView class
//

#include "stdafx.h"
#include "OEMDemo.h"
#include "ACQSettings.h"
#include "TraceEvents.h"
#include "ChildView.h"
#include <math.h>
#include <stdlib.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

#define round(x) ((int)((x<0.5)?floor(x):ceil(x)))

#ifdef _DEBUG
#define TEST 1
#endif

#ifdef TEST
const int TEST_iDZ = 0;
const int TEST_iNPTS = 5;
const int TEST_iDynamicRange = 30;
#endif
/////////////////////////////////////////////////////////////////////////////
// CChildView

CChildView::CChildView()
{
    dwUpdateCount = DAU_STATE_NONINITIALIZED;
    wDauState = DAU_STATE_NONINITIALIZED;
    memset(&ad, 0, sizeof(ad));
    bGoodTrace = FALSE;
    InitializeCriticalSection( &csStopDataCollect );

    EnterCriticalSection( &csStopDataCollect );
    stopDataCollect = FALSE;
    LeaveCriticalSection( &csStopDataCollect );

	fileDlg = new CFileDialog( FALSE);

	xL = xR = yT = yB = 0;
}

CChildView::~CChildView()
{
	removeBGS();
}


BEGIN_MESSAGE_MAP(CChildView,CWnd )
	//{{AFX_MSG_MAP(CChildView)
	ON_WM_PAINT()
	ON_COMMAND(ID_SETTINGS_ACQSETTINGS, OnSettingsAcqsettings)
	ON_COMMAND(ID_SETTINGS_INITIALIZE, OnSettingsInitialize)
	ON_COMMAND(ID_SETTINGS_START, OnSettingsStart)
	ON_COMMAND(ID_SETTINGS_STOP, OnSettingsStop)
	ON_COMMAND(ID_ANALYSIS_DISPLAYANALYSIS, OnAnalysisDisplayanalysis)
	ON_COMMAND(ID_FILE_SAVE_AS, OnFileSaveAs)
	ON_COMMAND(ID_VIEW_LEFT, OnKeyLeft)
	ON_COMMAND(ID_VIEW_RIGHT, OnKeyRight)
	ON_COMMAND(ID_VIEW_UP, OnKeyUp)
	ON_COMMAND(ID_VIEW_DOWN, OnKeyDown)
	ON_COMMAND(ID_VIEW_CLEFT, OnKeyCLeft)
	ON_COMMAND(ID_VIEW_CRIGHT, OnKeyCRight)
	ON_COMMAND(ID_VIEW_CUP, OnKeyCUp)
	ON_COMMAND(ID_VIEW_CDOWN, OnKeyCDown)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()


/////////////////////////////////////////////////////////////////////////////
// CChildView message handlers

BOOL CChildView::PreCreateWindow(CREATESTRUCT& cs) 
{
	if (!CWnd::PreCreateWindow(cs))
		return FALSE;

	//cs.dwExStyle |= WS_EX_CLIENTEDGE;
	//cs.style &= ~WS_BORDER;
	cs.lpszClass = AfxRegisterWndClass(CS_HREDRAW|CS_VREDRAW|CS_DBLCLKS, 
		::LoadCursor(NULL, IDC_ARROW), HBRUSH(COLOR_WINDOW+1), NULL);

	return TRUE;
}

void CChildView::OnPaint()
{
 	CPaintDC dc(this); // device context for painting

	// TODO: Add your message handler code here
#ifdef TEST
    if( xL != xR && yT != yB )
#else
    if( ad.bDrawTrace == TRUE && xL != xR && yT != yB )
#endif
    {
        TEXTMETRIC	tm;
        CRect		rect;
        double		dPointSpacing;

		//double		dDynamicRange;
        //int			iDynamicRange;
		//double		dDX, dDY, dX;

		int				j;
        //int			iXStart, iXEnd, iYStart, iYEnd, i, x, iZKM;
		int				iCYPrint, iCXPrint, iFZCXPrint;
        //double		dYScale, dXScale;

        CPen		TracePen, TickPen, *OldPen;
		//cPen		EventPan;
        //double		dIncrement, m, dDistance; 
        char		cbuf[300];
        //int			iPointCount;

        dc.GetTextMetrics(&tm);
        iCYPrint = tm.tmHeight + tm.tmExternalLeading;
		iCXPrint = tm.tmMaxCharWidth + tm.tmOverhang;
		iFZCXPrint = round( 2.5 * iCXPrint);
        GetClientRect( rect );

		TracePen.CreatePen(PS_SOLID,1,RGB(0,0,0)); /* create a black pen 1 pix wide */
		TickPen.CreatePen(PS_SOLID,1,RGB(128,128,255)); /* create a blue pen 1 pix wide */

#ifdef TEST
		dPointSpacing = 2.0;
#else
        dPointSpacing = ad.dau[ad.cs.wCard].fPointSpacings[ad.cs.wPointSpacing];
#endif

		// where to plot
		int sXBeg = round(rect.left + 3.0 * iCXPrint);
		int sXEnd = round(rect.right - 3.0 * iCXPrint);
        int sYEnd = round(rect.bottom - 2.0 * iCYPrint);
        int sYStart = round(rect.top + 1.0 * iCYPrint);

		// calc Physical->Graph scales
		double xp0 = xL;
		double xg0 = sXBeg;
		double xsc = (sXEnd - sXBeg) / (xR - xL);
		double yp0 = yB;
		double yg0 = sYEnd;
		double ysc = (sYStart - sYEnd) / (yT - yB);

		// range to plot
#ifdef TEST
		int iDZ = TEST_iDZ;
		const int iNPTS = TEST_iNPTS;
        int iDynamicRange = TEST_iDynamicRange;
#else
		int iDZ = ad.waveFormHeader.FPOffset >> 16;
		int iNPTS = ad.waveFormHeader.NumPts;
        int iDynamicRange = ad.dau[ad.cs.wCard].waveInfo[ad.cs.wWave].piPulses[ad.cs.wPulse].wDynamicRange;
#endif

		int iOfs = iDZ;
		int i0 = (int) xL;
		if (i0 < 0)
			i0 = 0;
		int i1 = (int) (xR + 1);
		if (i1 > iNPTS - iOfs - 1)
			i1 = iNPTS - iOfs - 1;

#ifdef TEST
		QPOTDRWaveformData dataArray[iNPTS] = { 0, 1000, 1000, 2000, 0 };
		QPOTDRWaveformData *data = dataArray;
#else
		QPOTDRWaveformData *data = ad.waveFormData;
#endif

#define XP2S(x) ((int)(((x)-xp0)*xsc+xg0+.5))
#define YP2S(y) ((int)(((y)-yp0)*ysc+yg0+.5))

#define XS2P(x) (((x)-xg0)/xsc+xp0)
#define YS2P(y) (((y)-yg0)/ysc+yp0)

        OldPen = dc.SelectObject(&TickPen);
		// X grid
		int nGridsXMaj = 4;
		for (j = 0; j <= nGridsXMaj; j++)
		{
			double fXS = sXBeg + (sXEnd - sXBeg) * 1.0 * j / nGridsXMaj;
			double fXP = dPointSpacing * XS2P(fXS) / 1000.0;
			int x = (int)(fXS + 0.5);
			dc.MoveTo(x, sYStart);
			dc.LineTo(x, sYEnd + iCXPrint / 2);
			sprintf(cbuf, "%.3f", fXP);
			dc.TextOut(x, rect.bottom - iCYPrint, cbuf, lstrlen(cbuf));
		}
		// Y grid
		int nGridsYMaj = 4;
		for (j = 0; j <= nGridsYMaj; j++)
		{
			double fYS = sYStart + (sYEnd - sYStart) * 1.0 * j / nGridsYMaj;
			double fYP = iDynamicRange - YS2P(fYS) / 1000.0;
			int x = (int)(fYS + 0.5);
			dc.MoveTo(sXBeg - iCYPrint / 2, x);
			dc.LineTo(sXEnd, x);
			sprintf(cbuf, "%.2f", fYP);
			dc.TextOut(sXBeg - iFZCXPrint, x - iCYPrint / 2, cbuf, lstrlen(cbuf));
		}

        // draw trace
        dc.SelectObject(&TracePen);
		dc.MoveTo(XP2S(i0), YP2S(data[i0 + iOfs]));
		const int maxpoints = 4096;
		int jstep = (i1 - i0) / maxpoints + 1;
		for (j = i0 + 1; j <= i1; j += jstep) {
			dc.LineTo(XP2S(j), YP2S(data[j + iOfs]));
		}

        dc.SelectObject(OldPen);
    }
#ifdef _DEBUG
	else
	{
#ifdef TEST
		UpdTrace(1);
#else
		dc.MoveTo(10,10);
		dc.LineTo(30,30);
#endif
	}
#endif
	// Do not call CWnd::OnPaint() for painting messages
}

//SVP void CChildView::OnPaint() 
//SVP {
//SVP 	CPaintDC dc(this); // device context for painting
//SVP 
//SVP 	// TODO: Add your message handler code here
//SVP     if( ad.bDrawTrace == TRUE || 1 )
//SVP     {
//SVP         TEXTMETRIC tm;
//SVP         CRect   rect;
//SVP         int     xstart, yend, xend, ystart, j, xdivs, i, x, cyprint, ydivs, zkm, cxprint;
//SVP         float   yscale, xscale;
//SVP         CPen TracePen, TickPen, EventPan, *OldPen;
//SVP         double  inc, m, dist; 
//SVP         char    cbuf[300];
//SVP         float   fPS, fDR;
//SVP         int     iDR;
//SVP         int     iPts;
//SVP 
//SVP         fPS = 4;//ad.dau[ad.cs.wCard].fPointSpacings[ad.cs.wPointSpacing];
//SVP         iDR = 40;//ad.dau[ad.cs.wCard].waveInfo[ad.cs.wWave].piPulses[ad.cs.wPulse].wDynamicRange;
//SVP         fDR = (float)iDR;
//SVP         zkm = 0;//ad.waveFormHeader.FPOffset >> 16;
//SVP         iPts = 1000;//ad.waveFormHeader.NumPts;
//SVP 
//SVP         dc.GetTextMetrics(&tm);
//SVP         cyprint = tm.tmHeight + tm.tmExternalLeading;
//SVP 		cxprint = tm.tmMaxCharWidth + tm.tmOverhang;
//SVP 
//SVP         TickPen.CreatePen(PS_SOLID,1,RGB(0,0,0)); /* create a black pen 1 pix wide */
//SVP         TracePen.CreatePen(PS_SOLID,1,RGB(0,0,255)); /* create a blue pen 1 pix wide */
//SVP         EventPan.CreatePen(PS_SOLID,2,RGB(255,0,0)); /* create a red pen 1 pix wide */
//SVP         GetClientRect( rect );
//SVP 
//SVP         xstart = (int)floor(rect.right * 0.01 + 0.5 + 3 * cxprint);
//SVP         xend = (int)floor(rect.right * 0.99 + 0.5 - cxprint);
//SVP         yend = (int)floor(rect.bottom * 0.99 + 0.5 - cyprint);
//SVP         ystart = (int)floor(rect.bottom * 0.01 + 0.5 + cyprint);
//SVP 
//SVP         xscale = (float)(iPts - zkm)/(float)(xend - xstart); // pt/pix   
//SVP         yscale = (yend - ystart)/(fDR * 1000.0f);
//SVP 
//SVP         inc = (double)(xscale * (xend - xstart)/10.0 * fPS );
//SVP         xdivs = (int)floor((xend - xstart)/10.0 + 0.5);  // pix/div
//SVP         ydivs = (int)floor( (yend - ystart)/fDR + 0.5);  
//SVP 
//SVP         // draw trace
//SVP         OldPen = dc.SelectObject(&TracePen);
//SVP         //dc.MoveTo(xstart, yend);
//SVP         //for(j = xstart, m = zkm; (int)m < iPts && j <= xend;  m = (j-xstart)*xscale + zkm, j++ )
//SVP         //{
//SVP         //    dc.LineTo(j, (yend) - (int)(ad.waveFormData[(int)m] * yscale + 0.5) );
//SVP         //}
//SVP 
//SVP         dc.SelectObject(&TickPen);
//SVP         // draw verticale grid
//SVP         dist = (iPts - zkm) * fPS;
//SVP         dist /= 10.0f;
//SVP         for( i = 0, x = xstart; i <= 10; x += xdivs, i++ )
//SVP         {
//SVP             dc.MoveTo(x, ystart);
//SVP             dc.LineTo(x, yend );
//SVP 
//SVP             if( i > 0 )
//SVP             { 
//SVP 				sprintf(cbuf, "%.01f", ( (dist + inc*(i-1))/1000.0 ) );
//SVP 				dc.TextOut(x, rect.bottom - cyprint,cbuf, lstrlen(cbuf)); 
//SVP             }
//SVP             else
//SVP             {
//SVP                 sprintf(cbuf, "%.01f", 0.0f );
//SVP                 dc.TextOut(x, rect.bottom - cyprint,cbuf, lstrlen(cbuf)); 
//SVP             }
//SVP         }             
//SVP 
//SVP         // draw horizontal grid
//SVP         //dc.SetTextAlign(TA_RIGHT|TA_NOUPDATECP);
//SVP         for( i=0, x = yend; i <= iDR; x -= ydivs, i++ )
//SVP         {
//SVP 			dc.MoveTo(xstart-10, x);
//SVP 			dc.LineTo(xend, x );
//SVP 			//if( i & 1 )
//SVP 			{
//SVP 				sprintf(cbuf, "%3d", i - iDR );
//SVP 				dc.TextOut(xstart - 3 * cxprint, x, cbuf, lstrlen(cbuf)); 
//SVP 			}
//SVP         }
//SVP 
//SVP         if( bGoodTrace )
//SVP         {
//SVP             if( ad.dau[ad.cs.wCard].pCurrentEvent != NULL )
//SVP             {
//SVP                 dc.SelectObject(&EventPan);
//SVP                 j = (int)((ad.dau[ad.cs.wCard].pCurrentEvent->dwStart - zkm) / xscale + xstart);
//SVP                 dc.MoveTo(j, ystart);
//SVP                 dc.LineTo(j, yend);
//SVP             }
//SVP         }
//SVP 
//SVP         dc.SelectObject(OldPen);
//SVP     }
//SVP 	// Do not call CWnd::OnPaint() for painting messages
//SVP }

void CChildView::OnSettingsAcqsettings() 
{
	// TODO: Add your command handler code here
    CString message;
    int ret;

    if( wDauState < DAU_STATE_DISCOVERED )
    {
        AfxMessageBox("DAU не проинициализирован");
        return;
    }
    else if(wDauState > DAU_STATE_IDLE)
    {
        AfxMessageBox("Необходимо сначала остановить DAU");
        return;
    }

    // Settings dialog needs pointer to cs before it's displayed
    dlgSettings.SetAppDataPointer(&ad);
    ret = dlgSettings.DoModal(); 
	if( ret == IDOK )
    {
        message.Format("Новые настройки установлены");
        m_wndStatusBar->SetWindowText(message);
        wDauState = DAU_STATE_INITIALIZED;
        AfxGetMainWnd()->GetMenu()->GetSubMenu(1)->CheckMenuItem( ID_SETTINGS_ACQSETTINGS, MF_BYCOMMAND | MF_CHECKED );
    }
    else
    {
        if( wDauState == DAU_STATE_INITIALIZED )
        {
            message.Format("Установлены старые настройки");
            m_wndStatusBar->SetWindowText(message);
        }
    }
}

UINT RunningThread( LPVOID pChildView )
{
    CChildView* pView = (CChildView *)pChildView;
    int count = 1, ret;
    DWORD flags, dwLastUpdate;
    CString str;

    // Initialize data and header to 0s
    memset( pView->ad.waveFormData, 0, sizeof(pView->ad.waveFormData));
    memset( &pView->ad.waveFormHeader, 0, sizeof(pView->ad.waveFormHeader));

    // Set flags
    flags = 0;
    if( !pView->ad.cs.bScanMode )
    {
        flags |= REAL_TIME_SCAN;
    }
    
    // Do life fiber detect to prevent
	if( pView->ad.cs.wLifeFiberDetect >> 1 )
	{
		flags |= LIVE_FIBER_DETECT;
	}

    if( pView->ad.cs.wGainSplice >> 1 )
    {
        flags |= GAIN_SPLICE_ON;
    }

	// Process OTAU connection
	{
		int port = pView->ad.cs.wOTAUPort;
		if (port != 0) {
			if (pView->ad.bgs != 0) {
				pView->ad.bgs->c_switchTo(port);
			}
		}
	}

    // Start DAU
    // Events are
    // WAIT_OBJECT_0 = Average Complete
    // WAIT_OBJECT_0 + 1 = New waveform
    // WAIT_OBJECT_0 + 2 = Life fiber / critical error
    pView->ad.hEvents = QPOTDRAcqStart( pView->ad.wCardIndexes[pView->ad.cs.wCard], flags ); 
    count = 0;
    pView->ad.bDrawTrace = FALSE;
    dwLastUpdate = 0;

    do
    { 
        // Wait for notification event from DAU
        ret = WaitForMultipleObjects( 3, pView->ad.hEvents, FALSE, INFINITE );

        // New waveform
        if( ret == WAIT_OBJECT_0 + 1 ) 
        {
            // Get trace data
            EnterCriticalSection( &pView->csStopDataCollect );

            QPOTDRRetrieveWaveformSync( pView->ad.wCardIndexes[pView->ad.cs.wCard],
                                        &pView->ad.waveFormHeader,
                                        pView->ad.waveFormData,
                                        pView->stopDataCollect );

            LeaveCriticalSection( &pView->csStopDataCollect );

            if( pView->ad.waveFormHeader.updateData )
            {
                //pView->InvalidateRect(NULL);
				pView->UpdTrace(dwLastUpdate == 0);
                dwLastUpdate = pView->ad.waveFormHeader.Averages;
            }

            str.Format("%d Averages; [%s] Last Update on %d;",
                pView->ad.waveFormHeader.Averages,
                pView->ad.waveFormHeader.updateData ? "Updated" : "Not updated",
                dwLastUpdate);
            pView->m_wndStatusBar->SetWindowText(str);
            pView->ad.bDrawTrace = TRUE;
        }
        // Life fiber / critical error
        else if( ret == WAIT_OBJECT_0 + 2 )
        {
            str.Format("Life fiber detected! Test aborted!");
            pView->m_wndStatusBar->SetWindowText(str);
            count = 0;
        }
        // Time out waiting for data
        else if( ret == WAIT_TIMEOUT )
        {
            count = 0;
        }
        // Thread or interrupt crashed / invalid
        else if( ret != 0 )
        {
            count = 0;
        }
        else
        {
            count = 0;
        }
    } while( ret == WAIT_OBJECT_0 + 1 );
 

    if( ret == WAIT_OBJECT_0 )
    {
        pView->ad.waveFormHeader.updateData = TRUE;
    }

    if( pView->ad.waveFormHeader.updateData )
    {
        if( pView->ad.cs.wFilter >> 1 )
        {
            QPOTDRFilterLastWaveform( pView->ad.wCardIndexes[pView->ad.cs.wCard], pView->ad.waveFormData );
        }

        str.Format("%d Averages; [%s] Last Update on %d;",
            pView->ad.waveFormHeader.Averages,
            pView->ad.waveFormHeader.updateData ? "Updated" : "Not updated",
            dwLastUpdate);
        pView->m_wndStatusBar->SetWindowText(str);
        pView->ad.bDrawTrace = TRUE;
        pView->bGoodTrace = TRUE;
        //pView->InvalidateRect(NULL);
		pView->UpdTrace(0);
    }
    QPOTDRAcqStop(pView->ad.wCardIndexes[pView->ad.cs.wCard]);

	if (pView->ad.bgs != 0 && pView->ad.cs.wOTAUPort != 0)
	{
		pView->ad.bgs->c_switchToAsync(-1); // plan to switch off
	}

	pView->wDauState = DAU_STATE_IDLE;
    AfxGetMainWnd()->GetMenu()->GetSubMenu(1)->CheckMenuItem( ID_SETTINGS_START, MF_BYCOMMAND | MF_UNCHECKED );

    AfxEndThread(0);
    return 0;
}

void CChildView::OnSettingsInitialize() 
{
	// TODO: Add your command handler code here
    WORD index, wTemp;
    int  status, pwInd;
    CString message;
    WORD dauCount;
    WORD cardIndexes[MAX_CARDS];
    float fTemp[MAX_WAVES];
    DWORD dwPulse[MAX_PULSES];

    if( wDauState >= DAU_STATE_DISCOVERED )
    {
        AfxMessageBox("Аппаратура DAU уже определена");
        return;
    }

	message.Format("Определение модулей...");
	m_wndStatusBar->SetWindowText(message);

    ad.wDAUCount = 0;
    dauCount = QPOTDRGetCards(cardIndexes);
    for( index = 0; index < dauCount; index++ )
    {
        message.Format("Инициализация модуля %d...", cardIndexes[index]);
        m_wndStatusBar->SetWindowText(message);
        status = QPOTDRInitialize( cardIndexes[index] );

        switch( status )
        {
        case 0: // Success
            // Remember card index.
            // Note that first card might be nonzero if it's used by another app
            message.Format("Получение параметров DAU модуля %d...", cardIndexes[index]);
            m_wndStatusBar->SetWindowText(message);

            ad.wCardIndexes[ad.wDAUCount] = cardIndexes[index];

            // Get Wavelength of current card
            QPOTDRGetAvailWaves( ad.wCardIndexes[ad.wDAUCount], fTemp);
            ad.dau[ad.wDAUCount].wLaserCount = QPOTDRGetOTDRLaserCount(ad.wCardIndexes[ad.wDAUCount]);

            // Get wavelength specific data from the card
            for( wTemp = 0; wTemp < ad.dau[ad.wDAUCount].wLaserCount; wTemp++ )
            {
                // Wavelength value
                ad.dau[ad.wDAUCount].waveInfo[wTemp].fWave = fTemp[wTemp];

                // Ranges for this wavelength
                QPOTDRGetAvailRanges(  ad.wCardIndexes[ad.wDAUCount],
                                        ad.dau[ad.wDAUCount].waveInfo[wTemp].fWave,
                                        ad.dau[ad.wDAUCount].waveInfo[wTemp].fRanges);

                // Pulses for this wavelength
                QPOTDRGetAvailPulses(  ad.wCardIndexes[ad.wDAUCount],
                                        ad.dau[ad.wDAUCount].waveInfo[wTemp].fWave,
                                        dwPulse);
                for( pwInd = 0; pwInd < MAX_PULSES; pwInd++ )
                {
                    if( dwPulse[pwInd] )
                    {
                        ad.dau[ad.wDAUCount].waveInfo[wTemp].piPulses[pwInd].wPulse = (WORD)(dwPulse[pwInd] >> 16);
                        ad.dau[ad.wDAUCount].waveInfo[wTemp].piPulses[pwInd].bFilter = dwPulse[pwInd] & 0x00000002;
                        ad.dau[ad.wDAUCount].waveInfo[wTemp].piPulses[pwInd].bGainSplice = dwPulse[pwInd] & 0x00000004;
                        ad.dau[ad.wDAUCount].waveInfo[wTemp].piPulses[pwInd].bHiRes = !(dwPulse[pwInd] & 0x00000001);
                        QPOTDRGetDynamicRange( ad.wCardIndexes[ad.wDAUCount],
                                                ad.dau[ad.wDAUCount].waveInfo[wTemp].fWave,
                                                pwInd,
                                                &ad.dau[ad.wDAUCount].waveInfo[wTemp].piPulses[pwInd].wDynamicRange );
                    }
                }

                // Averages for this wavelength
                QPOTDRGetAvailAverages(ad.wCardIndexes[ad.wDAUCount],
                                        ad.dau[ad.wDAUCount].waveInfo[wTemp].fWave,
                                        ad.dau[ad.wDAUCount].waveInfo[wTemp].dwAverages);

                // Default IOR for this wavelength
                ad.dau[ad.wDAUCount].waveInfo[wTemp].fDefaultIOR = 
                QPOTDRGetDefaultIOR(   ad.wCardIndexes[ad.wDAUCount],
                                       ad.dau[ad.wDAUCount].waveInfo[wTemp].fWave);

                // Default BSC for this wavelength
                ad.dau[ad.wDAUCount].waveInfo[wTemp].fDefaultBSC = 
                QPOTDRGetDefaultBSC(   ad.wCardIndexes[ad.wDAUCount],
                                       ad.dau[ad.wDAUCount].waveInfo[wTemp].fWave);

                // Fiber type for this wavelength
                ad.dau[ad.wDAUCount].waveInfo[wTemp].byFiberType = 
                QPOTDRGetFiberType(   ad.wCardIndexes[ad.wDAUCount],
                                      ad.dau[ad.wDAUCount].waveInfo[wTemp].fWave);

            }
            
            // Point spacings for this card
            QPOTDRGetAvailSpacings(ad.wCardIndexes[ad.wDAUCount], ad.dau[ad.wDAUCount].fPointSpacings);

            // Get Data collect info
            QPOTDRDataCollectInfo(ad.wCardIndexes[ad.wDAUCount], &ad.dau[ad.wDAUCount].pluginData);

            // Notify user that card initialized successfuly
            ad.wDAUCount++;
            break;
        case -1: // Errors from here down.
            message.Format("QPOTDRInitialize for card %d returned %d [Bad CardId]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -2:
            message.Format("QPOTDRInitialize for card %d returned %d [Unable to get card status]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -4:
            message.Format("QPOTDRInitialize for card %d returned %d [Unable to get Xilinx bitstream]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -5:
            message.Format("QPOTDRInitialize for card %d returned %d [LSR_PWR Initialize Failed]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -6:
            message.Format("QPOTDRInitialize for card %d returned %d [APD_VOLT Initialize Failed]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -7:
            message.Format("QPOTDRInitialize for card %d returned %d [PREAMP Initialize Failed]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -8:
            message.Format("QPOTDRInitialize for card %d returned %d [LSR_LTS Initialize Failed]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -9:
            message.Format("QPOTDRInitialize for card %d returned %d [LSR Initialize Failed]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -10:
            message.Format("QPOTDRInitialize for card %d returned %d [LTS_FREQ Initialize Failed]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -11:
            message.Format("QPOTDRInitialize for card %d returned %d [LTS_PWR Initialize Failed]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -12:
            message.Format("QPOTDRInitialize for card %d returned %d [PM_VFL Initialize Failed]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -13:
            message.Format("QPOTDRInitialize for card %d returned %d [LSR_COOL Initialize Failed]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -15:
            message.Format("QPOTDRInitialize for card %d returned %d [Xilinx General filure]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -16:
            message.Format("QPOTDRInitialize for card %d returned %d [Xilinx Bad parameter]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -17:
            message.Format("QPOTDRInitialize for card %d returned %d [Xilinx Bad CardID]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -18:
            message.Format("QPOTDRInitialize for card %d returned %d [Xilinx Data Block Start]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -19:
            message.Format("QPOTDRInitialize for card %d returned %d [Xilinx Data Block Middle]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -20:
            message.Format("QPOTDRInitialize for card %d returned %d [Xilinx Data Block End]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -22:
            message.Format("QPOTDRInitialize for card %d returned %d [Xilinx Self Test]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -23:
            message.Format("QPOTDRInitialize for card %d returned %d [EEPROM read error/bad CRC16]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -24:
            message.Format("QPOTDRInitialize for card %d returned %d [OPT version incorrect]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -25:
            message.Format("QPOTDRInitialize for card %d returned %d [Unable to determine interleave]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -26:
            message.Format("QPOTDRInitialize for card %d returned %d [Memory allocation error]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -27:
            message.Format("QPOTDRInitialize for card %d returned %d [Memory allocation error]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -28:
            message.Format("QPOTDRInitialize for card %d returned %d [Memory allocation error]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -29:
            message.Format("QPOTDRInitialize for card %d returned %d [Memory allocation error]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -30:
            message.Format("QPOTDRInitialize for card %d returned %d [Memory allocation error]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -31:
            message.Format("QPOTDRInitialize for card %d returned %d [Memory allocation error]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -32:
            message.Format("QPOTDRInitialize for card %d returned %d [Memory allocation error]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        case -33:
            message.Format("QPOTDRInitialize for card %d returned %d [CD OPT structure bad CRC16]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
            break;
        default:
            message.Format("QPOTDRInitialize for card %d returned %d [Unknown error code]", index, status);
            AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
        }
    }

	// Discover OTAU
	message.Format("Инициализация OTAU...");
	m_wndStatusBar->SetWindowText(message);
	removeBGS();
	BgSwitcher *bgs = new BgSwitcher(2, 0, false); // COM2, OTAU00, soft init
	if (bgs->c_isError()) {
		delete bgs;
		ad.bgs = 0;
	} else {
		ad.bgs = bgs;
	}

    // Report number of "good" cards
    message.Format("Модули проинициализированы и готовы");
    m_wndStatusBar->SetWindowText(message);
    message.Format("Найдено %d модулей OTDR и %d OTAU", ad.wDAUCount, bgs->c_isError() ? 0 : 1);
    if( ad.wDAUCount == 0 )
    {
        AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONSTOP);
    }
    else
    {
        AfxMessageBox(message, MB_OK | MB_APPLMODAL | MB_ICONINFORMATION);
        wDauState = DAU_STATE_DISCOVERED;
        AfxGetMainWnd()->GetMenu()->GetSubMenu(1)->CheckMenuItem( ID_SETTINGS_INITIALIZE, MF_BYCOMMAND | MF_CHECKED );
    }
}

void CChildView::OnSettingsStart() 
{
	// TODO: Add your command handler code here
    if( wDauState < DAU_STATE_INITIALIZED )
    {
        AfxMessageBox("Сначала надо провести инициализацию и установить параметры");
        return;
    }
    else if( wDauState == DAU_STATE_RUNNING )
    {
        AfxMessageBox("Уже запущен");
        return;
    }

    bGoodTrace = FALSE;
    ad.dau[ad.cs.wCard].pCurrentEvent = NULL;
    wDauState = DAU_STATE_RUNNING;
    AfxGetMainWnd()->GetMenu()->GetSubMenu(1)->CheckMenuItem( ID_SETTINGS_START, MF_BYCOMMAND | MF_CHECKED );

    EnterCriticalSection( &csStopDataCollect );
    stopDataCollect = FALSE;
    LeaveCriticalSection( &csStopDataCollect );
    
    AfxBeginThread(	RunningThread, this );
}

void CChildView::OnSettingsStop() 
{
	// TODO: Add your command handler code here
    if( wDauState != DAU_STATE_RUNNING )
    {
        AfxMessageBox("DAU не запущен");
        return;
    }

    EnterCriticalSection( &csStopDataCollect );
    stopDataCollect = TRUE;
    LeaveCriticalSection( &csStopDataCollect );
}

BOOL CChildView::DestroyWindow() 
{
	// TODO: Add your specialized code here and/or call the base class
	
	return CWnd ::DestroyWindow();
}

int CChildView::GetStatus(void) 
{
    return wDauState;
}


void CChildView::OnAnalysisDisplayanalysis() 
{
	// TODO: Add your command handler code here
    if( !bGoodTrace )
    {
        AfxMessageBox("Сначала надо снять рефлектограмму");
        return;
    }
    
    dlgTraceEvents.SetAppDataPointer(&ad);
    dlgTraceEvents.DoModal();
}

void CChildView::OnFileSaveAs() 
{
	// TODO: Add your command handler code here
    if( !bGoodTrace )
    {
        AfxMessageBox("Сначала надо снять рефлектограмму");
        return;
    }

	if( fileDlg )
	{
		if( fileDlg->DoModal() == IDOK )
		{
			int iZKM = ad.waveFormHeader.FPOffset >> 16;
	        int iPointCount = ad.waveFormHeader.NumPts;
			const char *fname = (LPCTSTR)(fileDlg->GetPathName());
			FILE *f = fopen(fname, "w");
			if (f == 0) {
				MessageBox("Не удалось открыть файл на запись");
			} else {
				// write trace
				int i;
				double deltaX = ad.dau[ad.cs.wCard].fPointSpacings[ad.cs.wPointSpacing];
				for (i = 0; i < iPointCount - iZKM; i++) {
					fprintf(f, "%g %g\n", deltaX * i, ad.waveFormData[i + iZKM] / 1000.0);
				}
				fclose(f);
				char sbuf[64];
				sprintf(sbuf, "Записано %d точек", iPointCount - iZKM);
				MessageBox(sbuf);
			}
		}
	}
}

const double LR_amount = 0.25;
const double LR_rescaler = 1.5;
const double UD_amount = LR_amount;
const double UD_rescaler = LR_rescaler;

void CChildView::OnKeyLeft()
{
	double dX = (xR - xL) * LR_amount;
	xL -= dX;
	xR -= dX;
	limitXS();
	UpdTrace(0);
}
void CChildView::OnKeyRight()
{
	double dX = (xR - xL) * LR_amount;
	xL += dX;
	xR += dX;
	limitXS();
	UpdTrace(0);
}
void CChildView::OnKeyUp()
{
	double dY = (yT - yB) * UD_amount;
	yT += dY;
	yB += dY;
	limitYS();
	UpdTrace(0);
}
void CChildView::OnKeyDown()
{
	double dY = (yT - yB) * UD_amount;
	yT -= dY;
	yB -= dY;
	limitYS();
	UpdTrace(0);
}
void CChildView::OnKeyCRight()
{
	double dX1 = (xR - xL);
	double dX2 = dX1 / LR_rescaler;
	xR += (dX2 - dX1) / 2;
	xL -= (dX2 - dX1) / 2;
	limitXS();
	UpdTrace(0);
}
void CChildView::OnKeyCLeft()
{
	double dX1 = (xR - xL);
	double dX2 = dX1 * LR_rescaler;
	xR += (dX2 - dX1) / 2;
	xL -= (dX2 - dX1) / 2;
	limitXS();
	UpdTrace(0);
}
void CChildView::OnKeyCUp()
{
	double dY1 = (yT - yB);
	double dY2 = dY1 / UD_rescaler;
	yT += (dY2 - dY1) / 2;
	yB -= (dY2 - dY1) / 2;
	limitYS();
	UpdTrace(0);
}
void CChildView::OnKeyCDown()
{
	double dY1 = (yT - yB);
	double dY2 = dY1 * UD_rescaler;
	yT += (dY2 - dY1) / 2;
	yB -= (dY2 - dY1) / 2;
	limitYS();
	UpdTrace(0);
}

void CChildView::UpdTrace(int rescale)
{
	if (rescale) {
#ifdef TEST
		int iDynamicRange = TEST_iDynamicRange;
		int iZKM = TEST_iDZ;
		int iPointCount = TEST_iNPTS;
#else
        int iDynamicRange = ad.dau[ad.cs.wCard].waveInfo[ad.cs.wWave].piPulses[ad.cs.wPulse].wDynamicRange;
        int iZKM = ad.waveFormHeader.FPOffset >> 16;
        int iPointCount = ad.waveFormHeader.NumPts;
#endif
		xL = 0;
		xR = iPointCount - iZKM - 1;
		yT = 1000.0 * iDynamicRange;
		yB = 0.0;
	}
	InvalidateRect(NULL);
}

void CChildView::limitXS() {
	if (xL < 0)
		xL = 0;
#ifdef TEST
		int iZKM = TEST_iDZ;
		int iPointCount = TEST_iNPTS;
#else
        int iZKM = ad.waveFormHeader.FPOffset >> 16;
        int iPointCount = ad.waveFormHeader.NumPts;
#endif
	int maxX = iPointCount - iZKM - 1;
	if (xR > maxX)
		xR = maxX;
}
void CChildView::limitYS() {
#ifdef TEST
		int iDynamicRange = TEST_iDynamicRange;
#else
        int iDynamicRange = ad.dau[ad.cs.wCard].waveInfo[ad.cs.wWave].piPulses[ad.cs.wPulse].wDynamicRange;
#endif
	double maxY = 1000.0 * iDynamicRange;
	if (yT > maxY)
		yT = maxY;
	if (yB < 0)
		yB = 0;
}

void CChildView::removeBGS() {
	if (ad.bgs != 0) {
		delete ad.bgs;
		ad.bgs = 0;
	}
}
