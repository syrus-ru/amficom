//#include <assert.h>

#include <stdlib.h>
#include <stdio.h>
#include "../Common/assert.h"

#include "EPList2.h"


EPLIST::iterator::iterator(EPELEM_ *elem_, EPLIST *list_)
{
	elem = elem_;
	list = list_;
}

EPLIST::iterator::iterator()
{
	elem = 0;
}

EventParams& EPLIST::iterator::operator *()
{
	assert(elem);
	return elem->data;
}

void EPLIST::iterator::operator++()
{
	assert(elem);
	elem = elem->next;
};

void EPLIST::iterator::operator--()
{
	if (elem)
		elem = elem->prev;
	else
		elem = list->end_;
	assert(elem);
};

void EPLIST::iterator::operator++(int)
{
	assert(elem);
	elem = elem->next;
};

void EPLIST::iterator::operator--(int)
{
	if (elem)
		elem = elem->prev;
	else
		elem = list->end_;
	assert(elem);
};

EventParams *EPLIST::iterator::operator->()
{
	return &elem->data;
}

EPLIST::EPLIST()
{
	begin_ = end_ = 0;
}

EPLIST::~EPLIST()
{
	removeAll();
}
void EPLIST::removeAll()
{
	EPELEM_* t = begin_;
	while (t)
	{
		EPELEM_* next = t->next;
		delete t;
		t = next;
	}
	begin_ = end_ = 0;
}

void EPLIST::removeNext(EPLIST::iterator it)
{
	EPELEM_ *prev = it.elem;
	assert(prev);
	EPELEM_ *cur = prev->next;
	assert(cur);
	EPELEM_ *next = cur->next;
	prev->next = next;
	if (next)
		next->prev = prev;
	delete cur;
}

void EPLIST::clear()
{
	removeAll();
}

int EPLIST::size()
{
	EPELEM_ *t;
	int sz = 0;
	for (t = begin_; t; t = t->next)
		sz++;
	return sz;
}

void EPLIST::copy_push_back(EventParams &ep)
{
	EPELEM_ *cur = new EPELEM_;
	cur->data = ep; // operator = is assumed to be correct
	cur->next = 0;
	cur->prev = end_;
	if (end_)
	{
		assert(end_->next == 0);
		end_->next = cur;
	}
	else
		begin_ = cur;
	end_ = cur;
}

EventParams &EPLIST::back()
{
	assert(end_);
	return end_->data;
}

EPLIST::iterator EPLIST::begin()
{
	return iterator(begin_, this);
}
EPLIST::iterator EPLIST::last()
{
	return iterator(end_, this);
}
EPLIST::iterator EPLIST::end()
{
	return iterator(0, this);
}
