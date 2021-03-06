#include <stdlib.h>
#include <assert.h>
#include <string.h> // memcpy()
#include <stdlib.h> // qsort()
#include "ArrList.h"

#ifdef __unix__
// XXX:it seems that g++ don't like to delete void*
#define DELETEVOIDPTR(x) ((void)free(x))
#else
#define DELETEVOIDPTR(x) ((void)delete(x))
#endif

ArrList::ArrList()
{
	storage = 0;
	allocated = 0;
	used = 0;
}

ArrList::ArrList(ArrList &that)
{
	assert(0);
	storage = 0;
    allocated = 0;
    used = 0;
}

ArrList::~ArrList()
{
	if (allocated)
		delete[] storage;
}

void ArrList::disposeAll()
{
	int i;
	for (i = 0; i < used; i++)
		if (storage[i])
			DELETEVOIDPTR(storage[i]);
}

void *ArrList::operator[] (int id)
{
	assert (id >= 0);
	assert (id < used);
	return storage[id];
}

void ArrList::set(int id, void *obj)
{
	assert (id >= 0);
	assert (id < used);
	storage[id] = obj;
}

void ArrList::extendToUsedPlusOne()
{
	if (used == allocated)
	{
		// extend array
		int new_size = used * 3 / 2;
		new_size = (new_size + 32) & ~31;
		void **new_storage = new void*[new_size];
		assert (new_storage);
		if (used)
			memcpy(new_storage, storage, used * sizeof(void*));
		if (allocated)
			delete storage;
		storage = new_storage;
		allocated = new_size;
	}
	assert (used < allocated);
}

void ArrList::add(void *obj)
{
	extendToUsedPlusOne();
	storage[used++] = obj;
}

void ArrList::slowInsert(int pos, void *obj)
{
	assert(pos >= 0);
	assert(pos <= used);
	int i;
	extendToUsedPlusOne();
	for (i = used; i > pos; i--)
		storage[i] = storage[i - 1];
	storage[pos] = obj;
    used++;
}
void ArrList::slowRemove(int pos)
{
	assert(pos >= 0);
	assert(pos < used);
	int i;
    if (storage[pos])
    	DELETEVOIDPTR(storage[pos]);
    used--;
	for (i = pos; i < used; i++)
		storage[i] = storage[i + 1];
}

int ArrList::getLength()
{
	return used;
}

void ArrList::qsort(int(*fcmp)(const void**, const void**))
{
	if (used > 1)
		::qsort(storage, used, sizeof(void*), (int(*)(const void*, const void*) )fcmp);
}

