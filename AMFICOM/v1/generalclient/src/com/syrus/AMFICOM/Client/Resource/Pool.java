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
// * Название: Класс Pool реализует хранилище для всех объектов,          * //
// *           загружаемых клиентом с РИСД, с доступом по типу хранимого  * //
// *           объекта и по идентификатору объекта                        * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\Pool.java                                     * //
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
import java.util.Random;

public class Pool extends Object
{
	public static Random rand = new Random();	// поле для генерирования
												// уникальных идентификаторов

	static private Hashtable obj_hash = new Hashtable();
												// хранилище для объектов
//	static private Hashtable name_hash = new Hashtable();
												// хранилище для имен

	// конструктор не доступен
	protected Pool()
	{
	}

	// получить объект типа obj_type_id с идентификатором obj_id
	public static Object get(String obj_type_id, String obj_id)
	{
		// получаем список всех объектов типа obj_type_id
		Hashtable hash2 = (Hashtable )obj_hash.get(obj_type_id);
		// если такого списка нет, то есть нет ни одного объекта данного
		// типа, то возвращаем null
		if(hash2 == null)
			return null;
		// вынимаем из списка объект с нужным идентификатором
		return hash2.get(obj_id);
	}

	// получить имя объекта типа obj_type_id с идентификатором obj_id
	public static String getName(String obj_type_id, String obj_id)
	{
		// получаем список имен всех объектов типа obj_type_id
		Hashtable hash2 = (Hashtable )obj_hash.get(obj_type_id);
		// если такого списка нет, то есть нет ни одного объекта данного
		// типа, то возвращаем null
		if(hash2 == null)
			return null;
		// вынимаем из списка имя объекта с нужным идентификатором
		try 
        {
            ObjectResource or = (ObjectResource )hash2.get(obj_id);
			if(or == null)
				return "";
			return or.getName();
        } 
		catch (Exception ex) 
        {
            return hash2.get(obj_id).toString();
        }
	}

	// внести в хранилище объект obj типа obj_type_id с идентификатором obj_id
	public static void put(String obj_type_id, String obj_id, Object obj)
	{
		// получаем список всех объектов типа obj_type_id
		Hashtable hash2 = (Hashtable )obj_hash.get(obj_type_id);
		if(hash2 == null)					// если такого списка нет, то есть
			{								// нет ни одного объекта данного
				hash2 = new Hashtable();	// типа, то создаем список объектов
				obj_hash.put(obj_type_id, hash2);	// и заносим в хранилище
													// для объектов
			}
		// добавляем объект
		hash2.put(obj_id, obj);
	}

/*
	// внести в хранилище имя name объекта типа obj_type_id с идентификатором obj_id
	public static void putName(String obj_type_id, String obj_id, String name)
	{
		// получаем список имен всех объектов типа obj_type_id
		Hashtable hash2 = (Hashtable )name_hash.get(obj_type_id);
		if(hash2 == null)					// если такого списка нет, то есть
			{								// нет ни одного объекта данного
				hash2 = new Hashtable();	// типа, то создаем список имен
				name_hash.put(obj_type_id, hash2);	// и заносим в хранилище
													// для имен объектов
			}
		// добавляем имя объекта
		hash2.put(obj_id, name);
	}
*/
	// получить список объектов типа obj_type_id
	public static Hashtable getHash(String obj_type_id)
	{
		return (Hashtable )obj_hash.get(obj_type_id);
	}

	// получить список измененных объектов типа obj_type_id
	public static Hashtable getChangedHash(String obj_type_id)
	{
		Hashtable ht = (Hashtable )obj_hash.get(obj_type_id);
		Hashtable retht = new Hashtable();
		for(Enumeration enum1 = ht.elements(); enum1.hasMoreElements();)
		{
			Object obj = enum1.nextElement();
			if(obj instanceof ObjectResource)
			{
				ObjectResource or = (ObjectResource )obj;
				if(or.isChanged())
					retht.put(or.getId(), or);
			}
		}
		return retht;
	}

/*
	// получить список имен объектов типа obj_type_id
	public static Hashtable getNameHash(String obj_type_id)
	{
		return (Hashtable )name_hash.get(obj_type_id);
	}
*/
	// установить список объектов типа obj_type_id
	public static void putHash(String obj_type_id, Hashtable hash2)
	{
		obj_hash.put(obj_type_id, hash2);
	}
/*
	// установить список имен объектов типа obj_type_id
	public static void putNameHash(String obj_type_id, Hashtable hash2)
	{
		name_hash.put(obj_type_id, hash2);
	}
*/
	// удалить объект obj типа obj_type_id с идентификатором obj_id
	public static void remove(String obj_type_id, String obj_id)
	{
		// получаем список всех объектов типа obj_type_id
		Hashtable hash2 = (Hashtable )obj_hash.get(obj_type_id);
		if(hash2 == null)					// если такого списка нет, то есть
			{								// нет ни одного объекта данного
				return;						// типа, то ничего не делаем
			}
		// удаляем объект
		hash2.remove(obj_id);
		if(hash2.isEmpty())
			obj_hash.remove(obj_type_id);
	}
/*
	// удалить имя name объекта типа obj_type_id с идентификатором obj_id
	public static void removeName(String obj_type_id, String obj_id)
	{
		// получаем список имен всех объектов типа obj_type_id
		Hashtable hash2 = (Hashtable )name_hash.get(obj_type_id);
		if(hash2 == null)					// если такого списка нет, то есть
			{								// нет ни одного объекта данного
				return;						// типа, то ничего не делаем
			}
		// удаляем имя объекта
		hash2.remove(obj_id);
		if(hash2.isEmpty())
			name_hash.remove(obj_type_id);
	}
*/
	// удалить список объектов типа obj_type_id
	public static void removeHash(String obj_type_id)
	{
		obj_hash.remove(obj_type_id);
	}
/*
	// удалить список имен объектов типа obj_type_id
	public static void removeNameHash(String obj_type_id)
	{
		name_hash.remove(obj_type_id);
	}
*/
	// удалить список имен объектов типа obj_type_id
	public static void remove(Object obj)
	{
		Enumeration enum4 = obj_hash.keys();
		for(Enumeration enum1 = obj_hash.elements(); enum1.hasMoreElements();)
		{
			String key2 = (String )enum4.nextElement();
			Hashtable ht = (Hashtable )enum1.nextElement();
			Enumeration enum3 = ht.keys();
			for(Enumeration enum2 = ht.elements(); enum2.hasMoreElements();)
			{
				String key = (String )enum3.nextElement();
				Object o = (Object )enum2.nextElement();
				if(o.equals(obj))
				{
					ht.remove(key);
					remove(key2, key);
				}
			}
		}
	}

	// получить список объектов типа obj_type_id
	public static Enumeration getKeys()
	{
		return obj_hash.keys();
	}

}


