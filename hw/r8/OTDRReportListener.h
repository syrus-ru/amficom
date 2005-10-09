// OTDRReportListener.h: interface for the OTDRReportListener class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_OTDRREPORTLISTENER_H__EA382A2C_F01A_4FA8_87D9_FE389DA540D7__INCLUDED_)
#define AFX_OTDRREPORTLISTENER_H__EA382A2C_F01A_4FA8_87D9_FE389DA540D7__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


#include "Segment.h"

class OTDRReportListener {
	public:
//		OTDRReportListener();
//		virtual ~OTDRReportListener();

		virtual void acceptOTDRReport(const Segment* segment) = 0;

};

#endif // !defined(AFX_OTDRREPORTLISTENER_H__EA382A2C_F01A_4FA8_87D9_FE389DA540D7__INCLUDED_)
