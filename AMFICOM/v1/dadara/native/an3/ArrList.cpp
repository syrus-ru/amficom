#include <stdlib.h>
#include <assert.h>
#include <string.h> // memcpy()
#include <stdlib.h> // qsort()
#include "ArrList.h"

ArrList::ArrList()
{
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
			delete storage[i];
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

void ArrList::add(void *obj)
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

	storage[used++] = obj;
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
