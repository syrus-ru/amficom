#if !defined(AFX_ACQSETTINGS_H__B03B1054_41DA_4497_8006_A0250E9A5B20__INCLUDED_)
#define AFX_ACQSETTINGS_H__B03B1054_41DA_4497_8006_A0250E9A5B20__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// ACQSettings.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CACQSettings dialog

class CACQSettings : public CDialog
{
// Construction
public:
	CACQSettings(CWnd* pParent = NULL);   // standard constructor
    void SetAppDataPointer(PAPP_DATA pAppData);
    void CACQSettings::UpdateView(void);

// Dialog Data
	//{{AFX_DATA(CACQSettings)
	enum { IDD = IDD_DIALOG_SETTINGS };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CACQSettings)
	public:
	virtual int DoModal();
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
    PAPP_DATA pAD;
    CURRENT_SETTINGS cs;

	// Generated message map functions
	//{{AFX_MSG(CACQSettings)
	virtual BOOL OnInitDialog();
	virtual void OnOK();
	virtual void OnCancel();
	afx_msg void OnSelchangeComboWave();
	afx_msg void OnSelchangeComboCard();
	afx_msg void OnSelchangeComboPulse();
	afx_msg void OnRadioRealTime();
	afx_msg void OnRadioAverage();
	afx_msg void OnSelchangeComboRange();
	afx_msg void OnSelchangeComboPs();
	afx_msg void OnSelchangeComboBuc();
	afx_msg void OnSelchangeComboAvg();
	afx_msg void OnCheckFilter();
	afx_msg void OnCheckGainSplice();
	afx_msg void OnCheckLfd();
	afx_msg void OnSelchangeComboPort();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_ACQSETTINGS_H__B03B1054_41DA_4497_8006_A0250E9A5B20__INCLUDED_)
