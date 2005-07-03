
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Resource;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class Trash extends Object
{
	static protected Hashtable trashed = new Hashtable();
	static protected int restoration_index = 0;

	public Trash()
	{
	}

	static public Integer trash(Object o)
	{
		trashed.put(new Integer(++restoration_index), o);
		return new Integer(restoration_index);
	}

	static public Vector trash(Enumeration e)
	{
		Vector restoration_v = new Vector();
		while(e.hasMoreElements())
			restoration_v.add(trash(e.nextElement()));
		return restoration_v;
	}

	static public Vector trash(Vector v)
	{
		return trash(v.elements());
	}

	static public Vector trash(Hashtable h)
	{
		return trash(h.elements());
	}

	static public void purge()
	{
		trashed = new Hashtable();
		restoration_index = 0;
	}

	static public Object restore(Integer rest_i)
	{
		Object obj = trashed.get(rest_i);
		trashed.remove(rest_i);
		return obj;
	}

	static public Object restore(int rest_i)
	{
		return restore(new Integer(rest_i));
	}

	static public Vector restore(Enumeration e)
	{
		Vector restoration_v = new Vector();
		while(e.hasMoreElements())
			restoration_v.add(restore((Integer)e.nextElement()));
		return restoration_v;
	}

	static public Vector restore(Vector v)
	{
		return restore(v.elements());
	}

	static public Enumeration trashed()
	{
		return trashed.elements();
	}
}
