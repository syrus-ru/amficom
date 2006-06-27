// Dispatcher.h: interface for the Dispatcher class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_DISPATCHER_H__1E9D94AC_6908_4CCC_9D0C_C1B13FED9511__INCLUDED_)
#define AFX_DISPATCHER_H__1E9D94AC_6908_4CCC_9D0C_C1B13FED9511__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "Vector.h"


class Dispatcher  
{
public:
	Dispatcher();
	virtual ~Dispatcher();
	

public: //Functions;
	void regist(void * obj);
	void notify(int commandName);

private: //variables;

	Vector *listeners;
	

};

#endif // !defined(AFX_DISPATCHER_H__1E9D94AC_6908_4CCC_9D0C_C1B13FED9511__INCLUDED_)
