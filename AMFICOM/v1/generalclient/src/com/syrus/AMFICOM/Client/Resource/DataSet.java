//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Класс DataSet реализует хранилище для набора объектов,     * //
// *           выбираемых из общего хранилища, с доступом по типу         * //
// *           хранимого объекта и по идентификатору объекта              * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\DataSet.java                                  * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *       Класс позволяет хранить объекты, структурируя доступ к ним по  * //
// *       типу объекта и идентификатору объекта. Отдельно хранятся       * //
// *       объекты и имена объектов.                                      * //
// *                                                                      * //
// *                                                                      * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Resource;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.*;

public class DataSet extends Object
{
	private ArrayList objects = new ArrayList();

	public DataSet()
	{
	}

	public DataSet(Enumeration en)
	{
		while(en.hasMoreElements())
			objects.add(en.nextElement());
	}

	public DataSet(Iterator it)
	{
		while(it.hasNext())
			objects.add(it.next());
	}

	public DataSet(Vector vec)
	{
		if(vec != null)
		{
			Enumeration en = vec.elements();
			while(en.hasMoreElements())
				objects.add(en.nextElement());
		}
	}

	public DataSet(List list)
	{
		if(list != null)
		{
			for(Iterator it = list.iterator(); it.hasNext();)
				objects.add(it.next());
		}
	}

	public DataSet(Hashtable hash)
	{
		if(hash != null)
		{
			Enumeration en = hash.elements();
			while(en.hasMoreElements())
				objects.add(en.nextElement());
		}
	}

	public DataSet(Object[] objs)
	{
		for(int i = 0; i < objs.length; i++)
			objects.add(objs[i]);
	}

	public ObjectResource get(String obj_id)
	{
		for(Iterator it = objects.iterator(); it.hasNext();)
		{
			ObjectResource or = (ObjectResource )it.next();
			if(or.getId().equals(obj_id))
				return or;
		}
		return null;
	}

	public ObjectResource get(int obj_i)
	{
		return (ObjectResource )objects.get(obj_i);
	}

	public void add(ObjectResource obj)
	{
		if(!objects.contains(obj))
			objects.add(obj);
	}

	public void add(DataSet ds)
	{
		for(Iterator it = ds.iterator(); it.hasNext();)
		{
			ObjectResource or = (ObjectResource )it.next();
			add(or);
		}
	}

	public void insertAt(ObjectResource obj, int obj_i)
	{
		if(!objects.contains(obj))
			objects.add(obj_i, obj);
	}

	public void remove(ObjectResource obj)
	{
		objects.remove(obj);
	}

	public void remove(String obj_id)
	{
		for(Iterator it = objects.iterator(); it.hasNext();)
		{
			ObjectResource or = (ObjectResource )it.next();
			if(or.getId().equals(obj_id))
			{
				objects.remove(or);
				return;
			}
		}
	}

	public void removeAt(int obj_i)
	{
			objects.remove(obj_i);
	}

	public int size()
	{
		return objects.size();
	}

	public int indexOf(Object o)
	{
		return objects.indexOf(o);
	}

	public Enumeration elements()
	{
		return Collections.enumeration(objects);
	}

	public Iterator iterator()
	{
		return objects.iterator();
	}

	public void clear()
	{
		objects.clear();
	}

}
