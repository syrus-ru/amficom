#ifndef _Analyse_h
#define _Analyse_h

#include "ArrList.h"

enum Event_Type
{
	Event_Type_LINEAR = 0,
	Event_Type_SOMETHING = 1
};

struct InEvent
{
	Event_Type type;
	int x0;
	int scale;
	double value;
	int begin;
	int end;
	int special; // специальное использование (мертвая зона)
	int dup; // найден дважды
	static int f_xSort(const void **a, const void **b);
	void initAsRgStart();
};

enum OutEventType
{
	OET_LINEAR,
	OET_SPLICE,
	OET_CONNECTOR_NOT_EOF,
	OET_CONNECTOR_OR_EOF,
	OET_UNKNOWN
};

struct OutEvent
{
	OutEventType oet;
	int begin;
	int end;
	int front;
	int tail;
	static int fcmp_begin(const void **a, const void **b)
	{
		const OutEvent* aa = *(const OutEvent **)a;
		const OutEvent* bb = *(const OutEvent **)b;
		int diff = aa->begin - bb->begin;
		return diff <= 0 ? diff < 0 ? -1 : 0 : 1;
	}
};

void analyse_fill_events(double *data, int size, ArrList &outEvents);

#endif

