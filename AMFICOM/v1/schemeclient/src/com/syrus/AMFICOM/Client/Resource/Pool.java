// ////////////////////////////////////////////////////////////////////////////
//* * //
//* Syrus Systems * //
//* ����������� ��������� ������������ � ���������� * //
//* * //
//* ������: ������� - ������� ������������������� �������������������� * //
//* ����������������� �������� � ���������� ����������� * //
//* * //
//* ���������� ��������������� ������� ����������� * //
//* * //
//* ��������: ����� Pool ��������� ��������� ��� ���� ��������, * //
//* ����������� �������� � ����, � �������� �� ���� ��������� * //
//* ������� � �� �������������� ������� * //
//* * //
//* ���: Java 1.2.2 * //
//* * //
//* �����:  * //
//* * //
//* ������: 0.1 * //
//* ��: 22 jan 2002 * //
//* ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\ * //
//* Client\Resource\Pool.java * //
//* * //
//* ����� ����������: Oracle JDeveloper 3.2.2 (Build 915) * //
//* * //
//* ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2) * //
//* * //
//* ������: ���������� * //
//* * //
//* ���������: * //
//* ��� ���� ����� ����������� * //
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
 * ����� ��������� ������� �������, ������������ ������ � ��� �� ���� ������� �
 * �������������� �������. �������� �������� ������� � ����� ��������.
 *
 * @author ����������� �.�.
 */
public final class Pool {
	// ��������� ��� ��������
	static private Map objHash	= new HashMap();

	// ����������� �� ��������
	private Pool() {
		//nothing
	}

	/**
	 * �������� ������ ���� objTypeId � ��������������� objId
	 *
	 * @param objTypeId
	 *            ��� �������
	 * @param objId
	 *            ������������� �������
	 * @return Object
	 */
	public static Object get(String objTypeId, String objId) {
		Object result = null;
		if ((objTypeId != null) && (objId != null)) {
			// �������� ������ ���� �������� ���� obj_type_id
			Map hash2 = (Map) objHash.get(objTypeId);
			// ���� ������ ������ ���, �� ���� ��� �� ������ ������� �������
			// ����, �� ���������� null
			result = (hash2 == null) ? null : hash2.get(objId); // �������� ��
			// ������ ������ � ������ ���������������
		}
		return result;
	}


	/**
	 * ������ � ��������� ������ obj ���� objTypeId � ��������������� objId
	 *
	 * @param objTypeId
	 *            ��� �������
	 * @param objId
	 *            ������������� �������
	 * @param obj
	 *            ������
	 */
	public static void put(String objTypeId, String objId, Object obj) {
		if (objTypeId == null || objId == null || obj == null)
			return;

		// �������� ������ ���� �������� ���� obj_type_id
		Map hash2 = (Map)objHash.get(objTypeId);
		if (hash2 == null) 								// ���� ������ ������ ���, �� ����
		{ 																// ��� �� ������ ������� �������
			hash2 = new HashMap(); 				  // ����, �� ������� ������ ��������
			objHash.put(objTypeId, hash2);	// � ������� � ���������
																			// ��� ��������
		}
		hash2.put(objId, obj);						// ��������� ������
	}


	/**
	 * �������� ������ �������� ���� objTypeId
	 *
	 * @param objTypeId
	 *            ��� ��������
	 */
	public static Map getMap(String objTypeId) {
		Map result = null;
		if (objTypeId != null)
			result = (Map) objHash.get(objTypeId);
		return result;
	}


	/**
	 * �������� ������ �������� ���� objTypeId
	 *
	 * @param objTypeId
	 *            ��� ��������
	 */
	public static List getList(String objTypeId) {
		ArrayList result = null;
		if (objTypeId != null) {
			Map ht = (Map) objHash.get(objTypeId);
			if (ht != null) {
				result = new ArrayList(ht.size());
				result.addAll(ht.values());
				result.trimToSize();
			}
		}
		return result;
}


	/**
	 * ���������� ������ �������� ���� objTypeId
	 *
	 * @param objTypeId ��� ��������
	 * @param map ������ ��������
	 *
	 */
	public static void putMap(String objTypeId, Map map)
	{
		objHash.put(objTypeId, map);
	}

	/**
	 * ������� ������ obj ���� objTypeId � ��������������� objId
	 *
	 * @param objTypeId
	 *            ��� �������
	 * @param objId
	 *            ������������� �������
	 */

	public static void remove(String objTypeId, String objId)
	{
		// �������� ������ ���� �������� ���� obj_type_id
		Map hash2 = (Map)objHash.get(objTypeId);
		if (hash2 == null)	// ���� ������ ������ ���, �� ���� ��� �� ������
			return;						// ������� ������� ����, �� ������ �� ������

		hash2.remove(objId);// ������� ������
		if (hash2.isEmpty())
			objHash.remove(objTypeId);
	}


	/**
	 * ������� ������ �������� ���� objTypeId
	 *
	 * @param objTypeId
	 *            ��� ��������
	 */
	public static void removeMap(String objTypeId) {
		objHash.remove(objTypeId);
	}

	/**
	 * ������� ������ obj
	 *
	 * @param obj
	 *            ��������� ������
	 */
	public static void remove(Object obj)
	{
		for (Iterator mapit = objHash.values().iterator(); mapit.hasNext();)
		{
			Map map = (Map)mapit.next();
			for (Iterator objit = map.values().iterator(); objit.hasNext(); )
				if (objit.next().equals(obj))
				{
					objit.remove();
					if (map.isEmpty())
						mapit.remove();
				}
		}
	}
/*
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
*/
	// �������� ������ �������� ���� obj_type_id
	public static Set getKeys() {
		return objHash.keySet();
	}

}
