// ResultSegment.h: interface for the ResultSegment class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_RESULTBYTESEGMENT_H__D500AD93_5CEF_4893_B9AA_E904FE9AE275__INCLUDED_)
#define AFX_RESULTBYTESEGMENT_H__D500AD93_5CEF_4893_B9AA_E904FE9AE275__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "Segment.h"
#include "Parameter.h"

class ResultSegment : public Segment {
public:
	ResultSegment(ByteArray* measurement_id,
			  unsigned int parnumber, Parameter** parameters);
	ResultSegment(unsigned int length, char* data);
	virtual ~ResultSegment();
	ByteArray* getMeasurementId() const;
	unsigned int getParnumber() const;
	Parameter** getParameters() const;

private:
	ByteArray* measurement_id;
	unsigned int parnumber;
	Parameter** parameters;
	void createSegment();
	void parseSegment();
};

#endif // !defined(AFX_RESULTBYTESEGMENT_H__D500AD93_5CEF_4893_B9AA_E904FE9AE275__INCLUDED_)
