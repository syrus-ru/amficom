// Parameter.h: interface for the Parameter class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_PARAMETER_H__A52964EC_5D35_4A45_80FA_250F9826327E__INCLUDED_)
#define AFX_PARAMETER_H__A52964EC_5D35_4A45_80FA_250F9826327E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "ByteArray.h"

class Parameter {
public:
	Parameter(ByteArray* name, ByteArray* value);
	virtual ~Parameter();
	char* getSegment() const;
	unsigned int getLength() const;
	ByteArray* getName() const;
	ByteArray* getValue() const;

private:
	ByteArray* name;
	ByteArray* value;
	unsigned int length;
};

int operator == (const Parameter& p1, const Parameter& p2);
int operator != (const Parameter& p1, const Parameter& p2);

#endif // !defined(AFX_PARAMETER_H__A52964EC_5D35_4A45_80FA_250F9826327E__INCLUDED_)
