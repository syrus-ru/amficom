// ByteArray.h: interface for the ByteArray class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_BYTEARRAY_H__A0420B47_08CD_44A8_8EE8_8882A826F8C3__INCLUDED_)
#define AFX_BYTEARRAY_H__A0420B47_08CD_44A8_8EE8_8882A826F8C3__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class ByteArray {
public:
	ByteArray(unsigned int length, char* data);
	ByteArray(unsigned int length);
	virtual ~ByteArray();
	unsigned int getLength() const;
	char* getSegment() const;
	char* getData() const; //---
	ByteArray* clone() const;
	ByteArray* getReversed() const;

private:
	unsigned int length;
	char* data;

};

int operator == (const ByteArray& ba1, const ByteArray& ba2);
int operator != (const ByteArray& ba1, const ByteArray& ba2);

#endif // !defined(AFX_BYTEARRAY_H__A0420B47_08CD_44A8_8EE8_8882A826F8C3__INCLUDED_)
