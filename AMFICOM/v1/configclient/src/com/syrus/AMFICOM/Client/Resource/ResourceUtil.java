package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

import com.syrus.AMFICOM.Client.Resource.General.*;

public class ResourceUtil
{
	public ResourceUtil()
	{
	}

	/**
		* @deprecated remove this function after new map using
		* @param dataSource
		* @param attr
		* @return
		*/

	public static Map copyAttributes1(DataSourceInterface dataSource, Map attr)
	{
		Map ht = new Hashtable();

		for (Iterator it = attr.values().iterator(); it.hasNext(); )
		{
			ElementAttribute ea = (ElementAttribute) it.next();
			ElementAttribute ea2 = (ElementAttribute) ea.clone();
//			ea2.id = dataSource.GetUId("attribute");
			ht.put(ea2.getId(), ea2);
		}
		return ht;
	}


	public static Map copyAttributes(DataSourceInterface dataSource, Map attr)
	{
		Map ht = new HashMap();

		for(Iterator it = attr.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone();
//			ea2.id = dataSource.GetUId("attribute");
			ht.put(ea2.getId(), ea2);
		}
		return ht;
	}

	public static Map copyCharacteristics(DataSourceInterface dataSource, Map attr)
	{
		Map ht = new HashMap();

		for(Iterator it = attr.values().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic )it.next();
			Characteristic ch2 = (Characteristic )ch.clone();
//			ch2.id = dataSource.GetUId("characteristic");
			ht.put(ch2.getId(), ch2);
		}
		return ht;
	}

	public static int parseNumber(String str) {
		StringBuffer s = new StringBuffer(str);
		boolean key = true;
		while (key) {
			key = false;
			for (int i = 0; i < s.length(); i++)
				if (!Character.isDigit(s.charAt(i))) {
					key = true;
					s = s.deleteCharAt(i);
					break;
				}
		}
		int n = -1;
		try {
			n = Integer.parseInt(s.toString());
		}
		catch (NumberFormatException ex) {
		}
		return n;
	}

	public static List parseStrings(String s)
	{
		List ids = new LinkedList();
		int i = 0;
		while(i < s.length())
		{
			int space = s.indexOf(" ", i);
			String s1 = s.substring(i, space);
			if(s1.length() != 0)
				ids.add(s1);
			i = space + 1;
		}
		return ids;
	}

}

