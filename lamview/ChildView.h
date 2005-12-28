// ChildView.h : interface of the CChildView class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_CHILDVIEW_H__062BD109_50B8_440F_87C4_4BE35FC89EA1__INCLUDED_)
#define AFX_CHILDVIEW_H__062BD109_50B8_440F_87C4_4BE35FC89EA1__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

/////////////////////////////////////////////////////////////////////////////
// CChildView window

UINT RunningThread( LPVOID pChildView );

#define DAU_STATE_NONINITIALIZED 0
#define DAU_STATE_DISCOVERED 1
#define DAU_STATE_INITIALIZED 2
#define DAU_STATE_RUNNING 3
#define DAU_STATE_IDLE DAU_STATE_INITIALIZED

class CChildView : public CWnd
{
private:
	double xL;
	double xR;
	double yT;
	double yB;
// Construction
public:
	CChildView();

// Attributes
public:
	CACQSettings dlgSettings;
    CTraceEvents dlgTraceEvents;
    DWORD dwUpdateCount;
    WORD wDauState;
    BOOL bGoodTrace;
    APP_DATA ad;
    CRITICAL_SECTION csStopDataCollect;
    int stopDataCollect;
	CFileDialog *fileDlg;

// Operations
public:
    CStatusBar  *m_wndStatusBar;
    int GetStatus(void);

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CChildView)
	public:
	virtual BOOL DestroyWindow();
	protected:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CChildView();
	void UpdTrace(int rescale);

private:
	void limitXS();
	void limitYS();
	void removeBGS();

	// Generated message map functions
protected:
	//{{AFX_MSG(CChildView)
	afx_msg void OnPaint();
	afx_msg void OnSettingsAcqsettings();
	afx_msg void OnSettingsInitialize();
	afx_msg void OnSettingsStart();
	afx_msg void OnSettingsStop();
	afx_msg void OnAnalysisDisplayanalysis();
	afx_msg void OnFileSaveAs();
	afx_msg void OnKeyLeft();
	afx_msg void OnKeyRight();
	afx_msg void OnKeyUp();
	afx_msg void OnKeyDown();
	afx_msg void OnKeyCLeft();
	afx_msg void OnKeyCRight();
	afx_msg void OnKeyCUp();
	afx_msg void OnKeyCDown();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_CHILDVIEW_H__062BD109_50B8_440F_87C4_4BE35FC89EA1__INCLUDED_)
