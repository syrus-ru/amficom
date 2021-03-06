// Segment.h: interface for the Segment class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_BYTESEGMENT_H__364DFF5B_43B4_4D00_8F48_BF9E153BFC4F__INCLUDED_)
#define AFX_BYTESEGMENT_H__364DFF5B_43B4_4D00_8F48_BF9E153BFC4F__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "akpdefs.h"

class Segment {
public:
	Segment();
	virtual ~Segment();

	static Segment* createFromData(unsigned int length, char* data);
	static SegmentType getSegmentType(char* data);

	SegmentType getType() const;
	char* getData() const;
	unsigned int getLength() const;

protected:
	char* data;
	unsigned int length;
};

#endif // !defined(AFX_BYTESEGMENT_H__364DFF5B_43B4_4D00_8F48_BF9E153BFC4F__INCLUDED_)

