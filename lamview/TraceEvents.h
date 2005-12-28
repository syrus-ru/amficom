#if !defined(AFX_TRACEEVENTS_H__2A95E7EF_2C43_4DD4_B6C2_A121253B5746__INCLUDED_)
#define AFX_TRACEEVENTS_H__2A95E7EF_2C43_4DD4_B6C2_A121253B5746__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// TraceEvents.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CTraceEvents dialog

class CTraceEvents : public CDialog
{
// Construction
public:
	CTraceEvents(CWnd* pParent = NULL);   // standard constructor
    void SetAppDataPointer(PAPP_DATA pAppData);
    void AddEvent(int index, PQPOTDREVENT event);
    void RunFAS(void);

// Dialog Data
	//{{AFX_DATA(CTraceEvents)
	enum { IDD = IDD_DIALOG_TRACE_EVENTS };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CTraceEvents)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
    PAPP_DATA pAD;
    CListCtrl *listEvents; //IDC_LIST_TRACE_EVENTS
    char buffer[1024];
	// Generated message map functions
	//{{AFX_MSG(CTraceEvents)
	virtual BOOL OnInitDialog();
	virtual void OnOK();
	virtual void OnCancel();
	afx_msg void OnDblclkListTraceEvents(NMHDR* pNMHDR, LRESULT* pResult);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_TRACEEVENTS_H__2A95E7EF_2C43_4DD4_B6C2_A121253B5746__INCLUDED_)
