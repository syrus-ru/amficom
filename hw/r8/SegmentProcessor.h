// SegmentProcessor.h: interface for the SegmentProcessor class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_SEGMENTPROCESSOR_H__97965EAC_C441_4855_B70D_67285FF7536E__INCLUDED_)
#define AFX_SEGMENTPROCESSOR_H__97965EAC_C441_4855_B70D_67285FF7536E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


#include "Segment.h"

class SegmentProcessor {
	public:
//		SegmentProcessor();
//		virtual ~SegmentProcessor();

		virtual void addTaskSegment(const Segment* segment) = 0;
		virtual const Segment* removeReportSegment() = 0;
		virtual void reAddReportSegment(const Segment* segment) = 0;

};

#endif // !defined(AFX_SEGMENTPROCESSOR_H__97965EAC_C441_4855_B70D_67285FF7536E__INCLUDED_)
