// MCMInfoSegment.h: interface for the MCMInfoSegment class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_MCMINFOSEGMENT_H__30A72FBB_44A6_4EFA_9D17_EDC10ACDD372__INCLUDED_)
#define AFX_MCMINFOSEGMENT_H__30A72FBB_44A6_4EFA_9D17_EDC10ACDD372__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "Segment.h"
#include "ByteArray.h"

class MCMInfoSegment : public Segment {
public:
	MCMInfoSegment(ByteArray* mcm_id);
	MCMInfoSegment(unsigned int length, char* data);
	virtual ~MCMInfoSegment();

	ByteArray* getMCMId() const;

private:
	ByteArray* mcm_id;

	void createSegment();
	void parseSegment();
};

#endif // !defined(AFX_MCMINFOSEGMENT_H__30A72FBB_44A6_4EFA_9D17_EDC10ACDD372__INCLUDED_)
