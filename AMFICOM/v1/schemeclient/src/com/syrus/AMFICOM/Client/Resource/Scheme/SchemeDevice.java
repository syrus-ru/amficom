package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class SchemeDevice extends StubResource implements Serializable
{
	public static final String typ = "schemedevice";
	private static final long serialVersionUID = 01L;
	public SchemeDevice_Transferable transferable;

	public String id = "";
	public String name = "";

	public Collection ports;
	public Collection cableports;

	private PortThreadMap crossroute;
	public Map attributes;

	public SchemeDevice(SchemeDevice_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemeDevice(String id)
	{
		this.id = id;
		ports = new ArrayList();
		cableports = new ArrayList();
		attributes = new HashMap();
		crossroute = new PortThreadMap();
		transferable = new SchemeDevice_Transferable();
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public PortThreadMap getCrossRoute()
	{
		return crossroute;
	}

	public void setCrossRoute(PortThreadMap crossroute)
	{
		this.crossroute = crossroute;
	}

	public SchemeCableThread getRoutedThread(SchemePort port)
	{
		if (!isCrossRouteValid())
			createDefaultCrossRoute();
		return crossroute.get(port);
	}

//	public String getRoutedThreadId(String port_id)
//	{
//		if (!isCrossRouteValid())
//			createDefaultCrossRoute();
//		return (String)crossroute.get(port_id);
//	}


	public SchemePort getRoutedPort(SchemeCableThread thread)
	{
		if (!isCrossRouteValid())
			createDefaultCrossRoute();
		return crossroute.get(thread);
	}


//	public String getRoutedPortId(String thread_id)
//	{
//		if (!isCrossRouteValid())
//			createDefaultCrossRoute();
//		for (Iterator it = crossroute.keySet().iterator(); it.hasNext();)
//		{
//			SchemePort port = (SchemePort)it.next();
//			if (crossroute.get(port).equals(thread_id))
//				return port.getId();
//		}
//		return null;
//	}


	public boolean isCrossRouteValid()
	{
		if (ports.isEmpty() || !hasCablesConnected())
			return crossroute.isEmpty() ? true : false;
		else if (crossroute.isEmpty())
			return false;

		//check ports
		for (Iterator it = crossroute.getAllPorts().iterator(); it.hasNext();)
		{
			SchemePort port = (SchemePort)it.next();
			if (!ports.contains(port))
				return false;
		}
		//check threads
		List cables = new LinkedList();
		Collection threads = new ArrayList(crossroute.size());
		for (Iterator it = cableports.iterator(); it.hasNext(); )
		{
			SchemeCablePort p = (SchemeCablePort)it.next();
			if (p.cable_link_id.length() != 0)
				cables.add(Pool.get(SchemeCableLink.typ, p.cable_link_id));
		}
		for (Iterator it = cables.iterator(); it.hasNext();)
		{
			SchemeCableLink cable = (SchemeCableLink)it.next();
			for (Iterator tit = cable.cable_threads.iterator(); tit.hasNext();)
				threads.add((SchemeCableThread)tit.next());
		}
		for (Iterator it = crossroute.getAllThreads().iterator(); it.hasNext();)
		{
			SchemeCableThread thread = (SchemeCableThread)it.next();
			if (!threads.contains(thread))
				return false;
		}
		return true;
	}

	public void createDefaultCrossRoute()
	{
		crossroute = new PortThreadMap();
		if (ports.isEmpty() || !hasCablesConnected())
			return;

		List inputCables = new LinkedList();
		List outputCables = new LinkedList();
		//для входных кабельных портов ищем выходные порты
		//для выходных кабельных портов ищем входные порты
		for (Iterator it = cableports.iterator(); it.hasNext(); )
		{
			SchemeCablePort p = (SchemeCablePort)it.next();
			if (p.cable_link_id.length() != 0)
			{
				if (p.direction_type.equals("out"))
					outputCables.add(Pool.get(SchemeCableLink.typ, p.cable_link_id));
				else
					inputCables.add(Pool.get(SchemeCableLink.typ, p.cable_link_id));
			}
		}
		if (!inputCables.isEmpty())
			findCrossRoute(inputCables, "out", crossroute);
		if (!outputCables.isEmpty())
			findCrossRoute(outputCables, "in", crossroute);
	}

	private boolean hasCablesConnected()
	{
		if (cableports.isEmpty())
			return false;
		for (Iterator it = cableports.iterator(); it.hasNext();)
		{
			SchemeCablePort p = (SchemeCablePort)it.next();
			if (p.cable_link_id.length() != 0)
				return true;
		}
		return false;
	}

	private static int parseNumber(String str)
	{
		StringBuffer s = new StringBuffer(str);
		boolean key = true;
		while (key)
		{
			key = false;
			for (int i = 0; i < s.length(); i++)
				if (!Character.isDigit(s.charAt(i)))
				{
					key = true;
					s = s.deleteCharAt(i);
					break;
				}
		}
		int n;
		try
		{
			n = Integer.parseInt(s.toString());
		}
		catch (NumberFormatException ex)
		{
			n = -1;
		}
		return n;
	}

	private static SchemePort getPortByNumber(Collection ports, int number)
	{
		for (Iterator it = ports.iterator(); it.hasNext();)
		{
			SchemePort port = (SchemePort)it.next();
			int num = parseNumber(port.getName());
			if (num == number)
				return port;
		}
		return null;
	}

	private void findCrossRoute(Collection cables, String direction, PortThreadMap crossroute)
	{
		List freePorts = new ArrayList(ports.size());
		List freeThreads = new ArrayList();
		for (Iterator it = ports.iterator(); it.hasNext(); )
		{
			SchemePort p = (SchemePort) it.next();
			if (p.direction_type.equals(direction))
				freePorts.add(p);
		}
		if (!freePorts.isEmpty())
		{
			for (Iterator it = cables.iterator(); it.hasNext(); )
			{
				SchemeCableLink cable = (SchemeCableLink) it.next();
				for (Iterator tit = cable.cable_threads.iterator(); tit.hasNext(); )
				{
					SchemeCableThread thread = (SchemeCableThread) tit.next();
					int num = parseNumber(SchemeCableLink.parseName(thread.getName()));
					SchemePort p = null;
					if (num != -1)
						p = getPortByNumber(freePorts, num);
					if (p != null)
					{
						crossroute.add(p, thread);
						freePorts.remove(p);
						if (freePorts.isEmpty())
							break;
					}
					else
						freeThreads.add(thread);
				}
			}
			if (!freePorts.isEmpty() && !freeThreads.isEmpty())
			{
				Iterator it = freeThreads.iterator();
				for (int i = 0; i < Math.min(freePorts.size(), freeThreads.size()); i++)
				{
					SchemeCableThread thread = (SchemeCableThread)it.next();
					crossroute.add((SchemePort)freePorts.get(i), thread);
				}
			}
		}
	}


	public Object getTransferable()
	{
		return transferable;
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;
		name = transferable.name;

		ports = new ArrayList(transferable.ports.length);
		cableports = new ArrayList(transferable.cableports.length);
		attributes = new HashMap(transferable.attributes.length);
		crossroute = new PortThreadMap();

		for (int i = 0; i < transferable.ports.length; i++)
			ports.add(new SchemePort(transferable.ports[i]));
		for (int i = 0; i < transferable.cableports.length; i++)
			cableports.add(new SchemeCablePort(transferable.cableports[i]));
		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));

//		for (int i = 0; i < transferable.portmap.length; i++)
//		{
//
//
//		}
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;

		transferable.ports = new SchemePort_Transferable[ports.size()];
		transferable.cableports = new SchemeCablePort_Transferable[cableports.size()];

		int counter = 0;
		for (Iterator it = ports.iterator(); it.hasNext();)
		{
			SchemePort port = (SchemePort)it.next();
			port.setTransferableFromLocal();
			transferable.ports[counter++] = (SchemePort_Transferable)port.getTransferable();
		}
		counter = 0;
		for (Iterator it = cableports.iterator(); it.hasNext();)
		{
			SchemeCablePort cableport = (SchemeCablePort)it.next();
			cableport.setTransferableFromLocal();
			transferable.cableports[counter++] = (SchemeCablePort_Transferable)cableport.getTransferable();
		}

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute)it.next();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
//		transferable.portmap = crossroute.getTransferable();
	}

	public void updateLocalFromTransferable()
	{
		crossroute = new PortThreadMap();
		for (Iterator it = ports.iterator(); it.hasNext();)
		{
			SchemePort port = (SchemePort)it.next();
			Pool.put(SchemePort.typ, port.getId(), port);
			port.updateLocalFromTransferable();
		}
		for (Iterator it = cableports.iterator(); it.hasNext();)
		{
			SchemeCablePort port = (SchemeCablePort)it.next();
			Pool.put(SchemeCablePort.typ, port.getId(), port);
			port.updateLocalFromTransferable();
		}
	}

	public String getPropertyPaneClassName()
	{
		return "";
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("clonedids", id);
		if (cloned_id != null)
		{
			SchemeDevice cloned = (SchemeDevice)Pool.get(SchemeDevice.typ, cloned_id);
			if (cloned == null)
				System.err.println("Scheme.clone() id not found: " + cloned_id);
			else
				return cloned;
		}

		SchemeDevice dev = new SchemeDevice(dataSource.GetUId(SchemeDevice.typ));
		dev.name = name;

		dev.ports = new ArrayList(ports.size());
		for (Iterator it = ports.iterator(); it.hasNext();)
		{
			SchemePort new_port = (SchemePort)((SchemePort)it.next()).clone(dataSource);
			new_port.device_id = dev.getId();
			dev.ports.add(new_port);
		}

		dev.cableports = new ArrayList(cableports.size());
		for (Iterator it = cableports.iterator(); it.hasNext();)
		{
			SchemeCablePort new_port = (SchemeCablePort)((SchemeCablePort)it.next()).clone(dataSource);
			new_port.device_id = dev.getId();
			dev.cableports.add(new_port);
		}

		dev.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(SchemeDevice.typ, dev.getId(), dev);
		Pool.put("clonedids", id, dev.id);
		return dev;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(ports);
		out.writeObject(cableports);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		ports = (Collection)in.readObject();
		cableports = (Collection)in.readObject();
		attributes = (Map)in.readObject();

		transferable = new SchemeDevice_Transferable();
		updateLocalFromTransferable();
	}
}

