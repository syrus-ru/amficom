#ifndef _EPLIST2_H
#define _EPLIST2_H

#include "../Common/EventParams.h"

class EPELEM_;

class EPLIST
{
private:
	EPELEM_ *begin_;
	EPELEM_ *end_;
	void removeAll();
public:
	EPLIST();
	~EPLIST();
	class iterator
	{
	protected:
		EPELEM_ *elem;
		EPLIST *list;
	protected:
		iterator(EPELEM_ *elem_, EPLIST *list);
	public:
		iterator();
		operator ==(iterator &that)
		{
			return elem == that.elem;
		}
		operator !=(iterator &that)
		{
			return elem != that.elem;
		}
		int isNull()
		{
			return elem == 0;
		}
		EventParams &operator*();
		void operator++();
		void operator--();
		void operator++(int);
		void operator--(int);
		EventParams *operator->();
	friend class EPLIST;
	};
	friend class EPLIST::iterator;
	EPLIST::iterator begin();
	EPLIST::iterator last();
	EPLIST::iterator end();
	void removeNext(EPLIST::iterator it);
	void clear();
	int size();
	void copy_push_back(EventParams &ep); // creates a copy of ep and attaches it to the end
	EventParams &back();
};

class EPELEM_
{
protected:
	EventParams data;
	EPELEM_ *next;
	EPELEM_ *prev;
friend class EPLIST;
friend class EPLIST::iterator;
};

#endif
