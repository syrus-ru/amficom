package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.Serializable;
import java.util.*;

//import com.syrus.AMFICOM.CORBA.Scheme.PortThreadMap_Transferable;

public class PortThreadMap implements Serializable
{
	public static final String typ = "cablechannelingitem";
	private static final long serialVersionUID = 01L;

	private Map portsMap = new HashMap();
	private Map threadsMap = new HashMap();

	private class Mapping
	{
		SchemePort port;
		SchemeCableThread thread;
		Mapping(SchemePort port, SchemeCableThread thread)
		{
			this.port = port;
			this.thread = thread;
		}
	}

//	public PortThreadMap(PortThreadMap_Transferable[] tr)
//	{
//		for (int i = 0; i < tr.length; i++)
//		{
//			SchemeCableLink cable = (SchemeCableLink)Pool.get(SchemeCableLink.typ, tr[i].cable_link_id);
//			SchemeCableThread thread = cable.getCableThread(tr[i].thread_id);
//		}
//	}

	public void add(SchemePort port, SchemeCableThread thread)
	{
		Mapping map = new Mapping(port, thread);
		portsMap.put(port, map);
		threadsMap.put(thread, map);
	}

	public SchemeCableThread get(SchemePort port)
	{
		Mapping map = (Mapping)portsMap.get(port);
		return map != null ? map.thread : null;
	}

	public SchemePort get(SchemeCableThread thread)
	{
		Mapping map = (Mapping)threadsMap.get(thread);
		return map != null ? map.port : null;
	}

	public void remove(SchemePort port)
	{
		Mapping map = (Mapping)portsMap.get(port);
		if (map != null)
		{
			threadsMap.remove(map.thread);
			portsMap.remove(port);
		}
	}

	public void remove(SchemeCableThread thread)
	{
		Mapping map = (Mapping)threadsMap.get(thread);
		if (map != null)
		{
			portsMap.remove(map.port);
			threadsMap.remove(thread);
		}
	}

	public void clear()
	{
		portsMap.clear();
		threadsMap.clear();
	}

	public boolean isEmpty()
	{
		return portsMap.isEmpty();
	}

	public int size()
	{
		return portsMap.size();
	}

	public Set getAllPorts()
	{
		return portsMap.keySet();
	}

	public Set getAllThreads()
	{
		return threadsMap.keySet();
	}

//	public PortThreadMap_Transferable[] getTransferable()
//	{
//		PortThreadMap_Transferable[] tr = new PortThreadMap_Transferable[portsMap.size()];
//		Iterator it = portsMap.values().iterator();
//		for (int i = 0; i < tr.length; i++)
//		{
//			PortThreadMap_Transferable transferable = new PortThreadMap_Transferable();
//			Mapping map = (Mapping)it.next();
//			transferable.cable_link_id = map.thread.cable_link_id;
//			transferable.port_id = map.port.getId();
//			transferable.thread_id = map.thread.getId();
//		}
//		return tr;
//	}
}
