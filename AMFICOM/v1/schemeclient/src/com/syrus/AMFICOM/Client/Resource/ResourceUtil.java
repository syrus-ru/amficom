package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class ResourceUtil
{
	public ResourceUtil()
	{
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

