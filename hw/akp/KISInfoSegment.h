// KISInfoSegment.h: interface for the KISInfoSegment class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_KISINFOSEGMENT_H__CBBE3FAB_F22D_4585_9352_2CB5CB522174__INCLUDED_)
#define AFX_KISINFOSEGMENT_H__CBBE3FAB_F22D_4585_9352_2CB5CB522174__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "Segment.h"
#include "ByteArray.h"

class KISInfoSegment : public Segment {
public:
	KISInfoSegment(ByteArray* kis_id);
	KISInfoSegment(unsigned int length, char* data);
	virtual ~KISInfoSegment();

	ByteArray* getKISId() const;

private:
	ByteArray* kis_id;

	void createSegment();
	void parseSegment();
};

#endif // !defined(AFX_KISINFOSEGMENT_H__CBBE3FAB_F22D_4585_9352_2CB5CB522174__INCLUDED_)

