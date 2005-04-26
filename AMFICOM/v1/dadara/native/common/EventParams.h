// EventParams.h: interface for the EventParams class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_EVENTPARAMS_H__4602CAF5_15BD_4327_AD1F_01E97ED71701__INCLUDED_)
#define AFX_EVENTPARAMS_H__4602CAF5_15BD_4327_AD1F_01E97ED71701__INCLUDED_

#include "MathRef.h"

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#define EventParams_LINEAR 10
#define EventParams_GAIN 11
#define EventParams_LOSS 12
#define EventParams_CONNECTOR 21
#define EventParams_DEADZONE 22
#define EventParams_ENDOFTRACE 23
#define EventParams_UNRECOGNIZED 30

class EventParams
{
public:
	EventParams();
	virtual ~EventParams();
	void operator = (const EventParams& ep);

	static const int LINEAR;
	static const int GAIN;
	static const int LOSS;
	static const int CONNECTOR;
	static const int DEADZONE;
	static const int ENDOFTRACE;
    static const int UNRECOGNIZED;

public:
	int type;
	int begin;
	int end;
    // SPLICE params
    double R; // см листочек с пояснениями или алгоритм вычисления этой величины
    // CONNECTOR params
    double R1, R2, R3; // см листочек с пояснениями или алгоритм вычисления этих величин
};

#endif // !defined(AFX_EVENTPARAMS_H__4602CAF5_15BD_4327_AD1F_01E97ED71701__INCLUDED_)
