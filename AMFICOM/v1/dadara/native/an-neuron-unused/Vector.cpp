// Vector.cpp: implementation of the Vector class.
//
//////////////////////////////////////////////////////////////////////

#include "Vector.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

Vector::Vector()
{
	size = 0;
	elementsSize = 10;
	elements = new void*[elementsSize];

}

Vector::~Vector()
{
	delete []elements;

}

void Vector::add(void *obj)
{
	if(size == elementsSize)
	{
		elementsSize+=10;
		void **tmpElements = new void*[elementsSize];
		for(int i=0; i<elementsSize-10; i++)
		{
			tmpElements[i] = elements[i];
		}
		delete []elements;
		elements = tmpElements;
	}

	elements[size] = obj;
	size++;
}

int Vector::getSize()
{
	return size;
}

void *Vector::get(int n)
{
	return elements[n];
}


bool Vector::isEmpty()
{
	if(size == 0) return true;
	else return false;
}

void * Vector::lastElement()
{
		return elements[size-1];
}


void Vector::killElements()
{
	for(int i=0; i<size; i++)
	{
		delete elements[i];
	}
	size = 0;
	return;
}