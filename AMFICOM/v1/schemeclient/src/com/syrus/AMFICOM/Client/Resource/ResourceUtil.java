package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import java.util.Enumeration;
import java.util.Hashtable;

public class ResourceUtil 
{
	public ResourceUtil()
	{
	}

	public static Hashtable copyAttributes(DataSourceInterface dataSource, Hashtable attr)
	{
		Hashtable ht = new Hashtable();

		for(Enumeration enum = attr.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			ElementAttribute ea2 = (ElementAttribute )ea.clone();
//			ea2.id = dataSource.GetUId("attribute");
			ht.put(ea2.getId(), ea2);
		}
		return ht;

	}
	
	public static Hashtable copyCharacteristics(DataSourceInterface dataSource, Hashtable attr)
	{
		Hashtable ht = new Hashtable();

		for(Enumeration enum = attr.elements(); enum.hasMoreElements();)
		{
			Characteristic ch = (Characteristic )enum.nextElement();
			Characteristic ch2 = (Characteristic )ch.clone();
//			ch2.id = dataSource.GetUId("characteristic");
			ht.put(ch2.getId(), ch2);
		}
		return ht;

	}
	
}

