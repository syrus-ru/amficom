// ////////////////////////////////////////////////////////////////////////////
//* * //
//* Syrus Systems * //
//* Департамент Системных Исследований и Разработок * //
//* * //
//* Проект: АМФИКОМ - система Автоматизированного Многофункционального * //
//* Интеллектуального Контроля и Объектного Мониторинга * //
//* * //
//* реализация Интегрированной Системы Мониторинга * //
//* * //
//* Название: Класс Pool реализует хранилище для всех объектов, * //
//* загружаемых клиентом с РИСД, с доступом по типу хранимого * //
//* объекта и по идентификатору объекта * //
//* * //
//* Тип: Java 1.2.2 * //
//* * //
//* Автор:  * //
//* * //
//* Версия: 0.1 * //
//* От: 22 jan 2002 * //
//* Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\ * //
//* Client\Resource\Pool.java * //
//* * //
//* Среда разработки: Oracle JDeveloper 3.2.2 (Build 915) * //
//* * //
//* Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2) * //
//* * //
//* Статус: разработка * //
//* * //
//* Изменения: * //
//* Кем Верс Когда Комментарии * //
//* ----------- ----- ---------- -------------------------------------- * //
//* * //
//* * //
//* * //
//* * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

/**
 *
 * Класс позволяет хранить объекты, структурируя доступ к ним по типу объекта и
 * идентификатору объекта. Отдельно хранятся объекты и имена объектов.
 *
 * @author Крупенников А.В.
 */
public final class Pool {
	// хранилище для объектов
	static private Map objHash	= new HashMap();

	// конструктор не доступен
	private Pool() {
		//nothing
	}

	/**
	 * получить объект типа objTypeId с идентификатором objId
	 *
	 * @param objTypeId
	 *            тип объекта
	 * @param objId
	 *            идентификатор объекта
	 * @return Object
	 */
	public static Object get(String objTypeId, String objId) {
		Object result = null;
		if ((objTypeId != null) && (objId != null)) {
			// получаем список всех объектов типа obj_type_id
			Map hash2 = (Map) objHash.get(objTypeId);
			// если такого списка нет, то есть нет ни одного объекта данного
			// типа, то возвращаем null
			result = (hash2 == null) ? null : hash2.get(objId); // вынимаем из
			// списка объект с нужным идентификатором
		}
		return result;
	}

	/**
	 * внести в хранилище объект obj типа objTypeId с идентификатором objId
	 *
	 * @param objTypeId
	 *            тип объекта
	 * @param objId
	 *            идентификатор объекта
	 * @param obj
	 *            объект
	 */
	public static void put(String objTypeId, String objId, Object obj) {
		if (objTypeId == null || objId == null || obj == null)
			return;

		// получаем список всех объектов типа obj_type_id
		Map hash2 = (Map)objHash.get(objTypeId);
		if (hash2 == null) 								// если такого списка нет, то есть
		{ 																// нет ни одного объекта данного
			hash2 = new HashMap(); 				  // типа, то создаем список объектов
			objHash.put(objTypeId, hash2);	// и заносим в хранилище
																			// для объектов
		}
		hash2.put(objId, obj);						// добавляем объект
	}

	/**
	 * получить список объектов типа objTypeId
	 * @deprecated use {@link Pool#getMap() getMap()}
	 * @param objTypeId
	 *            тип объектов
	 */
	public static Hashtable getHash(String objTypeId) {
		Hashtable result = null;
		if (objTypeId != null)
		{
			Map res = (Map)objHash.get(objTypeId);
			if (res != null)
				result = new Hashtable(res);
		}
		return result;
	}

	/**
	 * получить список объектов типа objTypeId
	 *
	 * @param objTypeId
	 *            тип объектов
	 */
	public static Map getMap(String objTypeId) {
		Map result = null;
		if (objTypeId != null)
			result = (Map) objHash.get(objTypeId);
		return result;
	}


	/**
	 * получить список объектов типа objTypeId
	 *
	 * @param objTypeId
	 *            тип объектов
	 */
	public static List getList(String objTypeId) {
		ArrayList result = null;
		if (objTypeId != null) {
			Map ht = (Map) objHash.get(objTypeId);
			if (ht != null) {
				result = new ArrayList();
				result.addAll(ht.values());
				result.trimToSize();
			}
			result.addAll(ht.values());
		}
		return result;
}

	/**
	 * получить список измененных объектов ObjectResource типа objTypeId
	 *
	 * @param objTypeId
	 *            тип объектов
	 * @return Map
	 */
	public static Map getChangedMap(String objTypeId) {
		Map result = null;
		if (objTypeId != null) {
			Map table = (Map) objHash.get(objTypeId);
			if (table != null) {
				result = new HashMap();
				for (Iterator it = table.values().iterator(); it.hasNext(); ) {
					Object obj = it.next();
					if (obj instanceof ObjectResource) {
						ObjectResource or = (ObjectResource) obj;
						if (or.isChanged())
							result.put(or.getId(), or);
					}
				}
			}
		}
		return result;
}

	/**
	 * получить список измененных объектов ObjectResource типа objTypeId
	 * @deprecated use {@link Pool#getChangedMap() getChangedMap()}
	 * @param objTypeId
	 *            тип объектов
	 * @return Hashtable
	 */
	public static Hashtable getChangedHash(String objTypeId) {
		Hashtable result = null;
		if (objTypeId != null) {
			Map table = (Map) objHash.get(objTypeId);
			if (table == null)
				result = null;
			else {
				result = new Hashtable();
				for (Iterator it = table.keySet().iterator(); it.hasNext();) {
					Object obj = table.get(it.next());
					if (obj instanceof ObjectResource) {
						ObjectResource or = (ObjectResource) obj;
						if (or.isChanged()) result.put(or.getId(), or);
					}
				}
			}
		}
		return result;
	}

	/**
	 * установить список объектов типа objTypeId
	 * @deprecated use {@link Pool#putMap() putMap()}
	 * @param objTypeId
	 *            тип объектов
	 */
	public static void putHash(String objTypeId, Hashtable hash2) {
		objHash.put(objTypeId, hash2);
	}

	/**
	 * установить список объектов типа objTypeId
	 *
	 * @param objTypeId тип объектов
	 * @param map список объектов
	 *
	 */
	public static void putMap(String objTypeId, Map map)
	{
		objHash.put(objTypeId, map);
	}

	/**
	 * удалить объект obj типа objTypeId с идентификатором objId
	 *
	 * @param objTypeId
	 *            тип объекта
	 * @param objId
	 *            идентификатор объекта
	 */

	public static void remove(String objTypeId, String objId)
	{
		// получаем список всех объектов типа obj_type_id
		Map hash2 = (Map)objHash.get(objTypeId);
		if (hash2 == null)	// если такого списка нет, то есть нет ни одного
			return;						// объекта данного типа, то ничего не делаем

		hash2.remove(objId);// удаляем объект
		if (hash2.isEmpty())
			objHash.remove(objTypeId);
	}

	/**
	 * удалить список объектов типа objTypeId
	 * @deprecated use {@link Pool#removeMap() removeMap()}
	 * @param objTypeId
	 *            тип объектов
	 */
	public static void removeHash(String objTypeId) {
		objHash.remove(objTypeId);
	}

	/**
	 * удалить список объектов типа objTypeId
	 *
	 * @param objTypeId
	 *            тип объектов
	 */
	public static void removeMap(String objTypeId) {
		objHash.remove(objTypeId);
	}

	/**
	 * удалить объект obj
	 *
	 * @param obj
	 *            удаляемый объект
	 */
	public static void remove(Object obj)
	{
		Iterator enum4 = objHash.keySet().iterator();
		for (Iterator enum1 = objHash.values().iterator(); enum1.hasNext();) {
			String key2 = (String) enum4.next();
			Map ht = (Map) enum1.next();
			Iterator enum3 = ht.keySet().iterator();
			for (Iterator enum2 = ht.values().iterator(); enum2.hasNext();) {
				String key = (String) enum3.next();
				Object o = enum2.next();
				if (o.equals(obj)) {
					ht.remove(key);
					remove(key2, key);
				}
			}
		}
	}

	// получить список объектов типа obj_type_id
	public static Set getKeys() {
		return objHash.keySet();
	}

}
