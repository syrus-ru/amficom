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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 
 * ����� ��������� ������� �������, ������������ ������ � ��� �� ���� ������� �
 * �������������� �������. �������� �������� ������� � ����� ��������.
 * 
 * @author ����������� �.�.
 */
public class Pool extends Object {

	public static Random		rand	= new Random();	// ���� ���
	// �������������
	// ���������� ���������������

	static private Hashtable	objHash	= new Hashtable();

	// ��������� ��� ��������
	//	static private Hashtable name_hash = new Hashtable();
	// ��������� ��� ����

	// ����������� �� ��������
	protected Pool() {
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
			Hashtable hash2 = (Hashtable) objHash.get(objTypeId);
			// ���� ������ ������ ���, �� ���� ��� �� ������ ������� �������
			// ����, �� ���������� null
			//			if (hash2 == null)
			//					System.out
			//							.println("hash2 is null , objTypeId=" + objTypeId);
			result = (hash2 == null) ? null : hash2.get(objId); // �������� ��
			// ������ ������
			// � ������
			// ���������������
		}
		return result;
	}

	/**
	 * �������� ��� ������� ���� objTypeId � ��������������� objId
	 * 
	 * @param objTypeId
	 *            ��� �������
	 * @param objId
	 *            ������������� �������
	 * @return String
	 */
	public static String getName(String objTypeId, String objId) {
		String result = null;
		if ((objTypeId != null) && (objId != null)) {
			// �������� ������ ���� ���� �������� ���� obj_type_id
			Hashtable hash2 = (Hashtable) objHash.get(objTypeId);
			// ���� ������ ������ ���, �� ���� ��� �� ������ ������� �������
			// ����, �� ���������� null
			if (hash2 == null) return null;
			// �������� �� ������ ��� ������� � ������ ���������������
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
		if ((objTypeId != null) && (objId != null) && (obj != null)) {
			// �������� ������ ���� �������� ���� obj_type_id
			Hashtable hash2 = (Hashtable) objHash.get(objTypeId);
			if (hash2 == null) // ���� ������ ������ ���, �� ����
			{ // ��� �� ������ ������� �������
				hash2 = new Hashtable(); // ����, �� ������� ������ ��������
				objHash.put(objTypeId, hash2); // � ������� � ���������
				// ��� ��������
			}
			// ��������� ������
			hash2.put(objId, obj);
		}
	}

	//	// ������ � ��������� ��� name ������� ���� obj_type_id � ���������������
	// obj_id
	//	public static void putName(String obj_type_id, String obj_id, String
	// name)
	//	{
	//		// �������� ������ ���� ���� �������� ���� obj_type_id
	//		Hashtable hash2 = (Hashtable )name_hash.get(obj_type_id);
	//		if(hash2 == null) // ���� ������ ������ ���, �� ����
	//			{ // ��� �� ������ ������� �������
	//				hash2 = new Hashtable(); // ����, �� ������� ������ ����
	//				name_hash.put(obj_type_id, hash2); // � ������� � ���������
	//													// ��� ���� ��������
	//			}
	//		// ��������� ��� �������
	//		hash2.put(obj_id, name);
	//	}

	/**
	 * �������� ������ �������� ���� objTypeId
	 * 
	 * @param objTypeId
	 *            ��� ��������
	 */
	public static Hashtable getHash(String objTypeId) {
		Hashtable result = null;
		if (objTypeId != null) result = (Hashtable) objHash.get(objTypeId);
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
	 * �������� ������ ���������� �������� ObjectResource ���� objTypeId
	 * 
	 * @param objTypeId
	 *            ��� ��������
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

	//	// �������� ������ ���� �������� ���� obj_type_id
	//	public static Hashtable getNameHash(String obj_type_id)
	//	{
	//		return (Hashtable )name_hash.get(obj_type_id);
	//	}

	//	// ���������� ������ ���� �������� ���� obj_type_id
	//	public static void putNameHash(String obj_type_id, Hashtable hash2)
	//	{
	//		name_hash.put(obj_type_id, hash2);
	//	}

	// ���������� ������ �������� ���� obj_type_id
	public static void putHash(String obj_type_id, Hashtable hash2) {
		objHash.put(obj_type_id, hash2);
	}

	// ������� ������ obj ���� obj_type_id � ��������������� obj_id
	public static void remove(String obj_type_id, String obj_id) {
		// �������� ������ ���� �������� ���� obj_type_id
		Hashtable hash2 = (Hashtable) objHash.get(obj_type_id);
		if (hash2 == null) // ���� ������ ������ ���, �� ����
		{ // ��� �� ������ ������� �������
			return; // ����, �� ������ �� ������
		}
		// ������� ������
		hash2.remove(obj_id);
		if (hash2.isEmpty()) objHash.remove(obj_type_id);
	}

	//	// ������� ��� name ������� ���� obj_type_id � ��������������� obj_id
	//	public static void removeName(String obj_type_id, String obj_id)
	//	{
	//		// �������� ������ ���� ���� �������� ���� obj_type_id
	//		Hashtable hash2 = (Hashtable )name_hash.get(obj_type_id);
	//		if(hash2 == null) // ���� ������ ������ ���, �� ����
	//			{ // ��� �� ������ ������� �������
	//				return; // ����, �� ������ �� ������
	//			}
	//		// ������� ��� �������
	//		hash2.remove(obj_id);
	//		if(hash2.isEmpty())
	//			name_hash.remove(obj_type_id);
	//	}

	/**
	 * ������� ������ �������� ���� objTypeId
	 * 
	 * @param objTypeId
	 *            ��� ��������
	 */
	public static void removeHash(String objTypeId) {
		// 
		objHash.remove(objTypeId);
	}

	//	  // ������� ������ ���� �������� ���� obj_type_id
	// public static void removeNameHash(String obj_type_id) {
	// name_hash.remove(obj_type_id); }
	//

	// ������� ������ ���� �������� ���� obj_type_id
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

	// �������� ������ �������� ���� obj_type_id
	public static Enumeration getKeys() {
		return objHash.keys();
	}

}
