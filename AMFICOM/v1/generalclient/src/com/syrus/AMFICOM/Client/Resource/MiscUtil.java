package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

public class MiscUtil
{
	public static List convert(Map map)
	{
		List list = new LinkedList();
		list.addAll(map.values());
		return list;
	}

	public static void addToCollection(Collection vec, Object []objs)
	{
		for(int i = 0; i < objs.length; i++)
			vec.add(objs[i]);
	}

	static public double fourdigits(double d)
	{
		long l = (long )(d * 10000);
		double d2 = (double )l;
		return d2 / 10000;
	}

	public static boolean validName(String name)
	{
		if(name == null)
			return false;
		if(name.length() == 0)
			return false;
		if(name.trim().length() == 0)
			return false;
		return true;
	}

	public static double diagonale(double x1, double y1, double x1_, double y1_)
	{
		return Math.sqrt(
				(x1_ - x1) * (x1_ - x1) +
				(y1_ - y1) * (y1_ - y1) );
	}
}
