// OEMDemo.h : main header file for the OEMDEMO application
//

#if !defined(AFX_OEMDEMO_H__F575016A_6766_498A_A45E_3AC31BC6FBBB__INCLUDED_)
#define AFX_OEMDEMO_H__F575016A_6766_498A_A45E_3AC31BC6FBBB__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"       // main symbols

/////////////////////////////////////////////////////////////////////////////
// COEMDemoApp:
// See OEMDemo.cpp for the implementation of this class
//

class COEMDemoApp : public CWinApp
{
public:
	COEMDemoApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(COEMDemoApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation

public:
	//{{AFX_MSG(COEMDemoApp)
	afx_msg void OnAppAbout();
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_OEMDEMO_H__F575016A_6766_498A_A45E_3AC31BC6FBBB__INCLUDED_)
