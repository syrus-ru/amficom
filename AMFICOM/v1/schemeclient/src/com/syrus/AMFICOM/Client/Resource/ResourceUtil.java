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

}

