package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.Serializable;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Scheme.PortThreadMap_Transferable;

public class PortThreadMap implements Serializable
{
	public static final String typ = "portthreadmap";
	private static final long serialVersionUID = 01L;

	private transient boolean changed = false;
	private Map portsMap;
	private Map threadsMap;

	public PortThreadMap_Transferable[] transferables;

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

	public PortThreadMap()
	{
		portsMap = new HashMap();
		threadsMap = new HashMap();
	}

	public PortThreadMap(PortThreadMap_Transferable[] transferables)
	{
		this.transferables = transferables;

		setLocalFromTransferable();
	}

	public void setTransferableFromLocal()
	{
		if (!changed && transferables != null && transferables.length == portsMap.size())
			return;

		transferables = new PortThreadMap_Transferable[portsMap.size()];
		Iterator it = portsMap.values().iterator();
		for (int i = 0; i < transferables.length; i++)
		{
			PortThreadMap_Transferable transferable = new PortThreadMap_Transferable();
			Mapping map = (Mapping) it.next();
			transferable.cableLinkId = map.thread.cableLinkId;
//			transferable.de
			transferable.portId = map.port.getId();
			transferable.threadId = map.thread.getId();
		}
		changed = false;
	}

	public void setLocalFromTransferable()
	{
		portsMap = new HashMap(transferables.length);
		threadsMap = new HashMap(transferables.length);
	}

	public PortThreadMap_Transferable[] getTransferable()
	{
		return transferables;
	}

	public void updateLocalFromTransferable()
	{
		for (int i = 0; i < transferables.length; i++)
		{
//			SchemeCableLink cable = (SchemeCableLink)Pool.get(SchemeCableLink.typ, transferables[i].cableLinkId);
//			SchemeCableThread thread = cable.getCableThread(transferables[i].threadId);
		}
	}

	public void add(SchemePort port, SchemeCableThread thread)
	{
		Mapping map = new Mapping(port, thread);
		portsMap.put(port, map);
		threadsMap.put(thread, map);
		changed = true;
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
		changed = true;
	}

	public void remove(SchemeCableThread thread)
	{
		Mapping map = (Mapping)threadsMap.get(thread);
		if (map != null)
		{
			portsMap.remove(map.port);
			threadsMap.remove(thread);
		}
		changed = true;
	}

	public void clear()
	{
		portsMap.clear();
		threadsMap.clear();
		changed = true;
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
}
