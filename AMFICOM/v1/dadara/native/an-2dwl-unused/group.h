#ifndef _group_h
#define _group_h

#include "ArrList.h"

struct InEvent;

// результат зависит от сортировки событий на входе
// скорее всего, сортировка должна идти по координате
// на выходе результат не сортирован - надо отсортировать
void group_events(InEvent *ie, int len, ArrList &outEvents);

#endif

