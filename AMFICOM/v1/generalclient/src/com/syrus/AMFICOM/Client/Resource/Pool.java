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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 
 * Класс позволяет хранить объекты, структурируя доступ к ним по типу объекта и
 * идентификатору объекта. Отдельно хранятся объекты и имена объектов.
 * 
 * @author Крупенников А.В.
 */
public class Pool extends Object {

	public static Random		rand	= new Random();	// поле для
	// генерирования
	// уникальных идентификаторов

	static private Hashtable	objHash	= new Hashtable();

	// хранилище для объектов
	//	static private Hashtable name_hash = new Hashtable();
	// хранилище для имен

	// конструктор не доступен
	protected Pool() {
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
			Hashtable hash2 = (Hashtable) objHash.get(objTypeId);
			// если такого списка нет, то есть нет ни одного объекта данного
			// типа, то возвращаем null
			//			if (hash2 == null)
			//					System.out
			//							.println("hash2 is null , objTypeId=" + objTypeId);
			result = (hash2 == null) ? null : hash2.get(objId); // вынимаем из
			// списка объект
			// с нужным
			// идентификатором
		}
		return result;
	}

	/**
	 * получить имя объекта типа objTypeId с идентификатором objId
	 * 
	 * @param objTypeId
	 *            тип объекта
	 * @param objId
	 *            идентификатор объекта
	 * @return String
	 */
	public static String getName(String objTypeId, String objId) {
		String result = null;
		if ((objTypeId != null) && (objId != null)) {
			// получаем список имен всех объектов типа obj_type_id
			Hashtable hash2 = (Hashtable) objHash.get(objTypeId);
			// если такого списка нет, то есть нет ни одного объекта данного
			// типа, то возвращаем null
			if (hash2 == null) return null;
			// вынимаем из списка имя объекта с нужным идентификатором
			try {
				ObjectResource or = (ObjectResource) hash2.get(objId);
				if (or == null)
					result = "";
				else
					result = or.getName();
			} catch (Exception ex) {
				result = hash2.get(objId).toString();
			}
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
		if ((objTypeId != null) && (objId != null) && (obj != null)) {
			// получаем список всех объектов типа obj_type_id
			Hashtable hash2 = (Hashtable) objHash.get(objTypeId);
			if (hash2 == null) // если такого списка нет, то есть
			{ // нет ни одного объекта данного
				hash2 = new Hashtable(); // типа, то создаем список объектов
				objHash.put(objTypeId, hash2); // и заносим в хранилище
				// для объектов
			}
			// добавляем объект
			hash2.put(objId, obj);
		}
	}

	//	// внести в хранилище имя name объекта типа obj_type_id с идентификатором
	// obj_id
	//	public static void putName(String obj_type_id, String obj_id, String
	// name)
	//	{
	//		// получаем список имен всех объектов типа obj_type_id
	//		Hashtable hash2 = (Hashtable )name_hash.get(obj_type_id);
	//		if(hash2 == null) // если такого списка нет, то есть
	//			{ // нет ни одного объекта данного
	//				hash2 = new Hashtable(); // типа, то создаем список имен
	//				name_hash.put(obj_type_id, hash2); // и заносим в хранилище
	//													// для имен объектов
	//			}
	//		// добавляем имя объекта
	//		hash2.put(obj_id, name);
	//	}

	/**
	 * получить список объектов типа objTypeId
	 * 
	 * @param objTypeId
	 *            тип объектов
	 */
	public static Hashtable getHash(String objTypeId) {
		Hashtable result = null;
		if (objTypeId != null) result = (Hashtable) objHash.get(objTypeId);
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
			Hashtable ht = (Hashtable) objHash.get(objTypeId);
			if (ht != null){
				result = new ArrayList();
				result.addAll(ht.values());
				result.trimToSize();
			}
		}		
		return result;
	}

	/**
	 * получить список измененных объектов ObjectResource типа objTypeId
	 * 
	 * @param objTypeId
	 *            тип объектов
	 * @return Hashtable
	 */
	public static Hashtable getChangedHash(String objTypeId) {
		Hashtable result = null;
		if (objTypeId != null) {
			Hashtable table = (Hashtable) objHash.get(objTypeId);
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

	//	// получить список имен объектов типа obj_type_id
	//	public static Hashtable getNameHash(String obj_type_id)
	//	{
	//		return (Hashtable )name_hash.get(obj_type_id);
	//	}

	//	// установить список имен объектов типа obj_type_id
	//	public static void putNameHash(String obj_type_id, Hashtable hash2)
	//	{
	//		name_hash.put(obj_type_id, hash2);
	//	}

	// установить список объектов типа obj_type_id
	public static void putHash(String obj_type_id, Hashtable hash2) {
		objHash.put(obj_type_id, hash2);
	}

	// удалить объект obj типа obj_type_id с идентификатором obj_id
	public static void remove(String obj_type_id, String obj_id) {
		// получаем список всех объектов типа obj_type_id
		Hashtable hash2 = (Hashtable) objHash.get(obj_type_id);
		if (hash2 == null) // если такого списка нет, то есть
		{ // нет ни одного объекта данного
			return; // типа, то ничего не делаем
		}
		// удаляем объект
		hash2.remove(obj_id);
		if (hash2.isEmpty()) objHash.remove(obj_type_id);
	}

	//	// удалить имя name объекта типа obj_type_id с идентификатором obj_id
	//	public static void removeName(String obj_type_id, String obj_id)
	//	{
	//		// получаем список имен всех объектов типа obj_type_id
	//		Hashtable hash2 = (Hashtable )name_hash.get(obj_type_id);
	//		if(hash2 == null) // если такого списка нет, то есть
	//			{ // нет ни одного объекта данного
	//				return; // типа, то ничего не делаем
	//			}
	//		// удаляем имя объекта
	//		hash2.remove(obj_id);
	//		if(hash2.isEmpty())
	//			name_hash.remove(obj_type_id);
	//	}

	/**
	 * удалить список объектов типа objTypeId
	 * 
	 * @param objTypeId
	 *            тип объектов
	 */
	public static void removeHash(String objTypeId) {
		// 
		objHash.remove(objTypeId);
	}

	//	  // удалить список имен объектов типа obj_type_id
	// public static void removeNameHash(String obj_type_id) {
	// name_hash.remove(obj_type_id); }
	//

	// удалить список имен объектов типа obj_type_id
	public static void remove(Object obj) {
		Enumeration enum4 = objHash.keys();
		for (Enumeration enum1 = objHash.elements(); enum1.hasMoreElements();) {
			String key2 = (String) enum4.nextElement();
			Hashtable ht = (Hashtable) enum1.nextElement();
			Enumeration enum3 = ht.keys();
			for (Enumeration enum2 = ht.elements(); enum2.hasMoreElements();) {
				String key = (String) enum3.nextElement();
				Object o = enum2.nextElement();
				if (o.equals(obj)) {
					ht.remove(key);
					remove(key2, key);
				}
			}
		}
	}

	// получить список объектов типа obj_type_id
	public static Enumeration getKeys() {
		return objHash.keys();
	}

}
