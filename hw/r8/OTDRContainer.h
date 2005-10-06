// OTDRContainer.h: interface for the OTDRContainer class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_OTDRCONTAINER_H__730323D5_9B3C_45AE_A34C_57DE9E12766B__INCLUDED_)
#define AFX_OTDRCONTAINER_H__730323D5_9B3C_45AE_A34C_57DE9E12766B__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


#include "OTDRController.h"


class OTDRContainer {
	public:
//		OTDRContainer();
//		virtual ~OTDRContainer();

		virtual void registerOTDRController(const OTDRController* otdrController) = 0;

};

#endif // !defined(AFX_OTDRCONTAINER_H__730323D5_9B3C_45AE_A34C_57DE9E12766B__INCLUDED_)
