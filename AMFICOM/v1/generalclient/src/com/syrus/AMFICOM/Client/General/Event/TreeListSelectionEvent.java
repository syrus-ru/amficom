/*
Уважаемые Бузаманы!!!
По просьбе Стасика теперь существует один единственный event на все
случаи жизни. А именно TreeListSelectionEvent.

селекта: new TreeListSelectionEvent(ObjectResource obj,
TreeListSelectionEvent.SELECT_EVENT, boolean search, boolean
searchall);

Рефрэш: new TreeListSelectionEvent(ObjectResource obj,
TreeListSelectionEvent.REFRESH_EVENT);

Деселект: new TreeListSelectionEvent(ObjectResource obj,
TreeListSelectionEvent.DESELECT_EVENT);

Напоминаю, что в случае селекта два последних параметра по умолчанию
false. По умолчанию поиск
выделяемого объекта производится в текущей выделенной ветке дерева.
Параметр select указывает, проводить или нет дополнительный поиск по
дереву.

select   selectall |
------------------ |
true     false     |  поиск по зарег.нодам и в текущ. ветке
true     true      |  поиск по всему дереву

Сорри, первый параметр - Object.
Т.е. можно посылать как String, так и OR.

Желающие могут посылать составной event.
Тогда второй параметр будет выглядеть, например, так:
TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.DESELECT_EVENT

или, что менее смешно, но более продуктивно
TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT

Подписываться нужно только на TreeListSelectionEvent.
*/
package com.syrus.AMFICOM.Client.General.Event;


public class TreeListSelectionEvent extends OperationEvent
{
	public boolean SELECT = false;
	public boolean REFRESH = false;
	public boolean DESELECT = false;

	public boolean search = false;
	public boolean searchAll = false;

	public static final long SELECT_EVENT = 0x00000001;
	public static final long REFRESH_EVENT = 0x00000010;
	public static final long DESELECT_EVENT = 0x00000100;
	public static final String typ = "treelistselectionevent";

	public TreeListSelectionEvent(Object source, long type, boolean search, boolean searchall )
	{
		super(source, 0, typ);
		typ_processing(type);
		this.search = search;
		this.searchAll = searchall;
	}

	public TreeListSelectionEvent(Object source, long type, boolean search)
	{
		super(source, 0, typ);
		typ_processing(type);
		this.search = search;
	}

	public TreeListSelectionEvent(Object source, long type)
	{
		super(source, 0, typ);
		typ_processing(type);
	}

	void typ_processing(long typ)
	{
		if((typ & SELECT_EVENT) != 0)
			SELECT = true;
		if((typ & REFRESH_EVENT) != 0)
			REFRESH = true;
		if((typ & DESELECT_EVENT) != 0)
			DESELECT = true;
	}
}
