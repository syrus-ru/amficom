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
// * ��������: ����� DataSet ��������� ��������� ��� ������ ��������,     * //
// *           ���������� �� ������ ���������, � �������� �� ����         * //
// *           ��������� ������� � �� �������������� �������              * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 22 jan 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\DataSet.java                                  * //
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
import java.util.Vector;

public class DataSet extends Object
{
	private Vector objects = new Vector();

	public DataSet()
	{
	}

	public DataSet(Enumeration enum)
	{
		while(enum.hasMoreElements())
			objects.add(enum.nextElement());
	}

	public DataSet(Vector vec)
	{
		if(vec != null)
		{
			Enumeration enum = vec.elements();
			while(enum.hasMoreElements())
				objects.add(enum.nextElement());
		}
	}

	public DataSet(Hashtable hash)
	{
		if(hash != null)
		{
			Enumeration enum = hash.elements();
			while(enum.hasMoreElements())
				objects.add(enum.nextElement());
		}
	}

	public DataSet(Object[] objs)
	{
		for(int i = 0; i < objs.length; i++)
			objects.add(objs[i]);
	}

	public ObjectResource get(String obj_id)
	{
		for(int i = 0; i < objects.size(); i++)
		{
			ObjectResource or = (ObjectResource )objects.get(i);
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
		for(int i = 0; i < ds.size(); i++)
		{
			ObjectResource or = (ObjectResource )ds.get(i);
			add(or);
		}
	}

	public void insertAt(ObjectResource obj, int obj_i)
	{
		if(!objects.contains(obj))
			objects.insertElementAt(obj, obj_i);
	}

	public void remove(ObjectResource obj)
	{
		objects.remove(obj);
	}

	public void remove(String obj_id)
	{
		for(int i = 0; i < objects.size(); i++)
		{
			ObjectResource or = (ObjectResource )objects.get(i);
			if(or.getId().equals(obj_id))
			{
				objects.remove(or);
				return;
			}
		}
	}

	public void removeAt(int obj_i)
	{
			objects.removeElementAt(obj_i);
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
		return objects.elements();
	}

	public void clear()
	{
		objects.clear();
	}

//	public void setFilter(ObjectResourceFilter filter)
//	{
//	}
}
