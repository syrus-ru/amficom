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
import java.util.*;

/**
 * @deprecated use Collection, Map according to the task 
 */
public class DataSet extends ArrayList
{
	public DataSet()
	{
	}

	public DataSet(Enumeration en)
	{
		while(en.hasMoreElements())
			super.add(en.nextElement());
	}

	public DataSet(Iterator it)
	{
		while(it.hasNext())
			super.add(it.next());
	}

	public DataSet(Vector vec)
	{
		if(vec != null)
		{
			Enumeration en = vec.elements();
			while(en.hasMoreElements())
				super.add(en.nextElement());
		}
	}

	public DataSet(List list)
	{
		if(list != null)
		{
			for(Iterator it = list.iterator(); it.hasNext();)
				super.add(it.next());
		}
	}

	public DataSet(Hashtable hash)
	{
		if(hash != null)
		{
			Enumeration en = hash.elements();
			while(en.hasMoreElements())
				super.add(en.nextElement());
		}
	}

	public DataSet(Object[] objs)
	{
		for(int i = 0; i < objs.length; i++)
			super.add(objs[i]);
	}

	public ObjectResource get(String obj_id)
	{
		for(Iterator it = super.iterator(); it.hasNext();)
		{
			ObjectResource or = (ObjectResource )it.next();
			if(or.getId().equals(obj_id))
				return or;
		}
		return null;
	}

	public Object get(int obj_i)
	{
		return super.get(obj_i);
	}

	public void add(ObjectResource obj)
	{
		if(!super.contains(obj))
			super.add(obj);
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
		if(!super.contains(obj))
			super.add(obj_i, obj);
	}

	public void remove(ObjectResource obj)
	{
		super.remove(obj);
	}

	public void remove(String obj_id)
	{
		for(Iterator it = super.iterator(); it.hasNext();)
		{
			ObjectResource or = (ObjectResource )it.next();
			if(or.getId().equals(obj_id))
			{
				super.remove(or);
				return;
			}
		}
	}

	public void removeAt(int obj_i)
	{
			super.remove(obj_i);
	}

	public int size()
	{
		return super.size();
	}

	public int indexOf(Object o)
	{
		return super.indexOf(o);
	}

	public Enumeration elements()
	{
		return Collections.enumeration(this);
	}

	public Iterator iterator()
	{
		return super.iterator();
	}

	public void clear()
	{
		super.clear();
	}

}
