//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: ������� - ������� ������������������� ��������������������   * //
// *         ����������������� �������� � ���������� �����������          * //
// *                                                                      * //
// *         ���������� ��������������� ������� �����������               * //
// *                                                                      * //
// * ��������: ����� Pool ��������� ��������� ��� ���� ��������,          * //
// *           ����������� �������� � ����, � �������� �� ���� ���������  * //
// *           ������� � �� �������������� �������                        * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 22 jan 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\Pool.java                                     * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ��������:                                                            * //
// *       ����� ��������� ������� �������, ������������ ������ � ��� ��  * //
// *       ���� ������� � �������������� �������. �������� ��������       * //
// *       ������� � ����� ��������.                                      * //
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
	public static Random rand = new Random();	// ���� ��� �������������
												// ���������� ���������������

	static private Hashtable obj_hash = new Hashtable();
												// ��������� ��� ��������
//	static private Hashtable name_hash = new Hashtable();
												// ��������� ��� ����

	// ����������� �� ��������
	protected Pool()
	{
	}

	// �������� ������ ���� obj_type_id � ��������������� obj_id
	public static Object get(String obj_type_id, String obj_id)
	{
		// �������� ������ ���� �������� ���� obj_type_id
		Hashtable hash2 = (Hashtable )obj_hash.get(obj_type_id);
		// ���� ������ ������ ���, �� ���� ��� �� ������ ������� �������
		// ����, �� ���������� null
		if(hash2 == null)
			return null;
		// �������� �� ������ ������ � ������ ���������������
		return hash2.get(obj_id);
	}

	// �������� ��� ������� ���� obj_type_id � ��������������� obj_id
	public static String getName(String obj_type_id, String obj_id)
	{
		// �������� ������ ���� ���� �������� ���� obj_type_id
		Hashtable hash2 = (Hashtable )obj_hash.get(obj_type_id);
		// ���� ������ ������ ���, �� ���� ��� �� ������ ������� �������
		// ����, �� ���������� null
		if(hash2 == null)
			return null;
		// �������� �� ������ ��� ������� � ������ ���������������
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

	// ������ � ��������� ������ obj ���� obj_type_id � ��������������� obj_id
	public static void put(String obj_type_id, String obj_id, Object obj)
	{
		// �������� ������ ���� �������� ���� obj_type_id
		Hashtable hash2 = (Hashtable )obj_hash.get(obj_type_id);
		if(hash2 == null)					// ���� ������ ������ ���, �� ����
			{								// ��� �� ������ ������� �������
				hash2 = new Hashtable();	// ����, �� ������� ������ ��������
				obj_hash.put(obj_type_id, hash2);	// � ������� � ���������
													// ��� ��������
			}
		// ��������� ������
		hash2.put(obj_id, obj);
	}

/*
	// ������ � ��������� ��� name ������� ���� obj_type_id � ��������������� obj_id
	public static void putName(String obj_type_id, String obj_id, String name)
	{
		// �������� ������ ���� ���� �������� ���� obj_type_id
		Hashtable hash2 = (Hashtable )name_hash.get(obj_type_id);
		if(hash2 == null)					// ���� ������ ������ ���, �� ����
			{								// ��� �� ������ ������� �������
				hash2 = new Hashtable();	// ����, �� ������� ������ ����
				name_hash.put(obj_type_id, hash2);	// � ������� � ���������
													// ��� ���� ��������
			}
		// ��������� ��� �������
		hash2.put(obj_id, name);
	}
*/
	// �������� ������ �������� ���� obj_type_id
	public static Hashtable getHash(String obj_type_id)
	{
		return (Hashtable )obj_hash.get(obj_type_id);
	}

	// �������� ������ ���������� �������� ���� obj_type_id
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
	// �������� ������ ���� �������� ���� obj_type_id
	public static Hashtable getNameHash(String obj_type_id)
	{
		return (Hashtable )name_hash.get(obj_type_id);
	}
*/
	// ���������� ������ �������� ���� obj_type_id
	public static void putHash(String obj_type_id, Hashtable hash2)
	{
		obj_hash.put(obj_type_id, hash2);
	}
/*
	// ���������� ������ ���� �������� ���� obj_type_id
	public static void putNameHash(String obj_type_id, Hashtable hash2)
	{
		name_hash.put(obj_type_id, hash2);
	}
*/
	// ������� ������ obj ���� obj_type_id � ��������������� obj_id
	public static void remove(String obj_type_id, String obj_id)
	{
		// �������� ������ ���� �������� ���� obj_type_id
		Hashtable hash2 = (Hashtable )obj_hash.get(obj_type_id);
		if(hash2 == null)					// ���� ������ ������ ���, �� ����
			{								// ��� �� ������ ������� �������
				return;						// ����, �� ������ �� ������
			}
		// ������� ������
		hash2.remove(obj_id);
		if(hash2.isEmpty())
			obj_hash.remove(obj_type_id);
	}
/*
	// ������� ��� name ������� ���� obj_type_id � ��������������� obj_id
	public static void removeName(String obj_type_id, String obj_id)
	{
		// �������� ������ ���� ���� �������� ���� obj_type_id
		Hashtable hash2 = (Hashtable )name_hash.get(obj_type_id);
		if(hash2 == null)					// ���� ������ ������ ���, �� ����
			{								// ��� �� ������ ������� �������
				return;						// ����, �� ������ �� ������
			}
		// ������� ��� �������
		hash2.remove(obj_id);
		if(hash2.isEmpty())
			name_hash.remove(obj_type_id);
	}
*/
	// ������� ������ �������� ���� obj_type_id
	public static void removeHash(String obj_type_id)
	{
		obj_hash.remove(obj_type_id);
	}
/*
	// ������� ������ ���� �������� ���� obj_type_id
	public static void removeNameHash(String obj_type_id)
	{
		name_hash.remove(obj_type_id);
	}
*/
	// ������� ������ ���� �������� ���� obj_type_id
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

	// �������� ������ �������� ���� obj_type_id
	public static Enumeration getKeys()
	{
		return obj_hash.keys();
	}

}


