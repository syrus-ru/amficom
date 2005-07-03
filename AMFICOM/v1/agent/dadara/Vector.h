// Vector.h: interface for the Vector class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_VECTOR_H__EB72B4EA_E435_4439_ABF5_DD98179CBB2C__INCLUDED_)
#define AFX_VECTOR_H__EB72B4EA_E435_4439_ABF5_DD98179CBB2C__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class Vector  
{
public:
	Vector();
	virtual ~Vector();


public:
	void  add(void *);
	void *get(int n);
	int   getSize();
	bool  isEmpty();
	void *lastElement();
//	void killElements();

private:
	void **elements;
	int size;
	int elementsSize;

};

#endif // !defined(AFX_VECTOR_H__EB72B4EA_E435_4439_ABF5_DD98179CBB2C__INCLUDED_)

