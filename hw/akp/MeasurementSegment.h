// MeasurementSegment.h: interface for the MeasurementSegment class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_TESTBYTESEGMENT_H__7DA1E548_28EB_41C4_A50B_059B4F49BA5D__INCLUDED_)
#define AFX_TESTBYTESEGMENT_H__7DA1E548_28EB_41C4_A50B_059B4F49BA5D__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "Segment.h"
#include "Parameter.h"

class MeasurementSegment : public Segment {
public:
	MeasurementSegment(ByteArray* measurement_id,
			ByteArray* measurement_type_id,
			ByteArray* local_address,
			unsigned int parnumber, Parameter** parameters);
	MeasurementSegment(unsigned int length, char* data);
	virtual ~MeasurementSegment();
	ByteArray* getMeasurementId() const;
	ByteArray* getMeasurementTypeId() const;
	ByteArray* getLocalAddress() const;
	unsigned int getParnumber() const;
	Parameter** getParameters() const;

private:
	ByteArray* measurement_id;
	ByteArray* measurement_type_id;
	ByteArray* local_address;
	unsigned int parnumber;
	Parameter** parameters;
	void createSegment();
	void parseSegment();
};

#endif // !defined(AFX_TESTBYTESEGMENT_H__7DA1E548_28EB_41C4_A50B_059B4F49BA5D__INCLUDED_)

