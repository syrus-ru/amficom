package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.*;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class PathBuilder
{
	private PathBuilder()
			{}

	public static boolean explore(Scheme scheme, SchemePath path)
	{
		if (path.links.isEmpty())
		{
			if (path.start_device_id.length() == 0)
				return false;
			SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, path.start_device_id);
			PathElement pe = addSchemeElement(path.links, se);
			if (pe == null)
				return false;
		}

		while(true)
		{
			PathElement pe = (PathElement)path.links.listIterator(path.links.size()).previous();
			if (pe.end_port_id.length() == 0)
				return false;

			if (pe.getType() == PathElement.SCHEME_ELEMENT)
			{
				PathElement newPE;
				SchemePort port = (SchemePort)Pool.get(SchemePort.typ, pe.end_port_id);
				if (port != null)
				{
					SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, port.link_id);
					if (link == null)
						return false;
					newPE = addLink(path.links, port, link);
				}
				else
				{
					SchemeCablePort cport = (SchemeCablePort)Pool.get(SchemeCablePort.typ, pe.end_port_id);
					if (cport == null)
						return false;
					SchemeCableLink clink = (SchemeCableLink)Pool.get(SchemeCableLink.typ, cport.cable_link_id);
					if (clink == null)
						return false;
					newPE = addCableLink(path.links, cport, clink);
				}
				if (newPE == null)
					return false;
			}
			else
			{
				PathElement newPE;
				SchemePort port = (SchemePort)Pool.get(SchemePort.typ, pe.end_port_id);
				if (port != null)
				{
					SchemeElement se = scheme.getSchemeElementByPort(port.getId());
					if (se == null)
						return false;
					newPE = addSchemeElement(path.links, se);
				}
				else
				{
					SchemeCablePort cport = (SchemeCablePort)Pool.get(SchemeCablePort.typ, pe.end_port_id);
					if (cport == null)
						return false;
					SchemeElement se = scheme.getSchemeElementByCablePort(cport.getId());
					if (se == null)
						return false;
					newPE = addSchemeElement(path.links, se);
				}
				if (newPE == null)
					return false;

				if (newPE.scheme_element_id.equals(path.end_device_id))
					return true;
			}
		}
	}

	/*
		находит тред в кабельном линке по принципу:
		1. Берется последний элемент из PathElements
		2. Если это не связанный с SchemeElement'ом PE - выходим
		3. Берется SE и смотрится
			а) если pe.start_port_id - порт: номер в названии порта на который приходит
				предыдущий PE
			б) если pe.start_port_id - кабельный порт: вытаскиваем предыдущий эл-т и если он
				PE связанный с кабелем смотрим в нем имя у его треда
		4. По этому номеру ищется тред с тем же номером в названии (предполагается,
				что порт с тредом соединены 1 - 1)
	*/
	private static String getNextThreadId(List pes, SchemeCableLink cl)
	{
		ListIterator lit = pes.listIterator(pes.size());
		if (lit.hasPrevious())
		{
			PathElement pe = (PathElement)lit.previous();
			if (pe.getType() == PathElement.SCHEME_ELEMENT)
			{
				int number = -1;
				SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, pe.scheme_element_id);
				for (Iterator it = getPorts(se).iterator(); it.hasNext();)
				{
					SchemePort port = (SchemePort)it.next();
					if (port.getId().equals(pe.start_port_id))
					{
						number = parseNumber(port.getName());
						if (number > 0)
						{
							SchemeCableThread thread = getThreadByNumber(cl, number);
							if (thread != null)
								return thread.getId();
						}
					}
				}
				for (Iterator it = getCablePorts(se).iterator(); it.hasNext();)
				{
					SchemeCablePort cport = (SchemeCablePort)it.next();
					if (cport.getId().equals(pe.start_port_id))
					{
						if (lit.hasPrevious())
						{
							PathElement lastpe = (PathElement)lit.previous();
							if (lastpe.getType() == PathElement.CABLE_LINK)
							{
								number = getThreadNumber(lastpe);
								if (number > 0)
								{
									SchemeCableThread thread = getThreadByNumber(cl, number);
									if (thread != null)
										return thread.getId();
								}
							}
						}
					}
				};
			}
		}
		return "";
	}

	private static SchemePort getNextPort(List pes, SchemeElement se)
	{
		ListIterator lit = pes.listIterator(pes.size());
		if (lit.hasPrevious())
		{
			PathElement pe = (PathElement)lit.previous();
			if (pe.getType() == PathElement.CABLE_LINK)
			{
				int number = getThreadNumber(pe);
				return getPortByNumber(se, number);
			}
		}
		return null;
	}

	private static int getThreadNumber(PathElement pe)
	{
		if (pe.getType() == PathElement.CABLE_LINK)
		{
			SchemeCableLink cl = (SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
			SchemeCableThread thread = cl.getCableThread(pe.thread_id);
			if (thread != null)
				return parseNumber(thread.getName());
		}
		return -1;
	}

	private static SchemeCableThread getThreadByNumber(SchemeCableLink cl, int number)
	{
		for (Iterator it = cl.cable_threads.iterator(); it.hasNext();)
		{
			SchemeCableThread thread = (SchemeCableThread)it.next();
			int ctn = parseNumber(thread.getName());
			if (ctn == number)
				return thread;
		}
		return null;
	}

	private static SchemePort getPortByNumber(SchemeElement se, int number)
	{
		for (Iterator it = getPorts(se).iterator(); it.hasNext();)
		{
			SchemePort port = (SchemePort)it.next();
			int num = parseNumber(port.getName());
			if (num == number)
				return port;
		}
		return null;
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

	public static PathElement addSchemeElement(List pes, SchemeElement se)
	{
		PathElement newPE = null;
		ListIterator lit = pes.listIterator(pes.size());
		if (lit.hasPrevious())
		{
			PathElement pe = (PathElement)lit.previous();

			newPE = new PathElement();
			newPE.setType(PathElement.SCHEME_ELEMENT);
			newPE.scheme_element_id = se.getId();
			newPE.n = pe.n + 1;
			newPE.start_port_id = pe.end_port_id;

			if (pe.getType() == PathElement.LINK)
			{
				SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, pe.link_id);
				SchemePort start_port = se.getPort(pe.end_port_id);
				// searching for ports with opposite direction
				List ports;
				List cports;
				if (start_port.direction_type.equals("in"))
				{
					cports = findCablePorts(se, "out");
					ports = findPorts(se, "out");
				}
				else
				{
					cports = findCablePorts(se, "in");
					ports = findPorts(se, "in");
				}
				// must be the only port with opposite direction
				if (ports.size() == 0 && cports.size() == 1)
					newPE.end_port_id = ((SchemeCablePort)cports.get(0)).getId();
				else if (ports.size() == 1 && cports.size() == 0)
					newPE.end_port_id = ((SchemePort)ports.get(0)).getId();

			}
			else
			{
				SchemeCableLink link = (SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
				SchemeCablePort start_port = se.getCablePort(pe.end_port_id);
				// searching for ports with opposite direction
				List ports;
				List cports;
				if (start_port.direction_type.equals("in"))
				{
					cports = findCablePorts(se, "out");
					ports = findPorts(se, "out");
				}
				else
				{
					cports = findCablePorts(se, "in");
					ports = findPorts(se, "in");
				}
				// must be the only cable port with opposite direction
				if (ports.size() == 0 && cports.size() == 1)
					newPE.end_port_id = ((SchemeCablePort)cports.get(0)).getId();
				// or any positive number of ports
				else if (ports.size() > 0 && cports.size() == 0)
				{
					SchemePort port = getNextPort(pes, se);
					if (port != null)
						newPE.end_port_id = port.getId();
				}
			}
		}
		else //first element
		{
			int access_ports = 0;
			SchemePort port = null;
			// must be at least one access port
			for (Iterator it = getPorts(se).iterator(); it.hasNext();)
			{
				SchemePort p = (SchemePort)it.next();
				if (p.access_port_type_id.length() != 0)
				{
					port = p;
					access_ports++;
				}
			}
			if (access_ports == 0)
			{
				//типа низзя
				return null;
			}
			newPE = new PathElement();
			newPE.setType(PathElement.SCHEME_ELEMENT);
			newPE.scheme_element_id = se.getId();
			newPE.n = 1;
			if (access_ports == 1)
				newPE.end_port_id = port.getId();
		}
		if (newPE != null)
			pes.add(newPE);
		return newPE;
	}

	public static PathElement addLink(List pes, SchemeLink link)
	{
		ListIterator lit = pes.listIterator(pes.size());
		if (lit.hasPrevious())
		{
			PathElement pe = (PathElement)lit.previous();
			if (pe.getType() == PathElement.SCHEME_ELEMENT)
			{
				SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, pe.scheme_element_id);

				//если у предыдущего эл-та проставлен end_port_id, ищем по нему
				if (pe.end_port_id.length() != 0)
				{
					for (Iterator it = getPorts(se).iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.getId().equals(pe.end_port_id))
							return addLink(pes, port, link);
					}
				}
				else //в противном случае ищем по общему порту предыдущего эл-та и линка
				{
					for (Iterator it = getPorts(se).iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.getId().equals(link.source_port_id) ||
								port.getId().equals(link.target_port_id))
						{
							pe.end_port_id = port.getId();
							return addLink(pes, port, link);
						}
					}
				}
			}
		}
		return null;
	}

	private static PathElement addLink(List pes, SchemePort port, SchemeLink link)
	{
		int number = 0;
		ListIterator it = pes.listIterator(pes.size());
		if (it.hasPrevious())
			number = ((PathElement)it.previous()).n;

		PathElement newPE = new PathElement();
		newPE.setType(PathElement.LINK);
		newPE.link_id = link.getId();
		newPE.n = number;

		if (port.getId().equals(link.source_port_id))
		{
			newPE.start_port_id = link.source_port_id;
			newPE.end_port_id = link.target_port_id;
		}
		else if (port.getId().equals(link.target_port_id))
		{
			newPE.start_port_id = link.target_port_id;
			newPE.end_port_id = link.source_port_id;
		}
		pes.add(newPE);
		return newPE;
	}

	public static PathElement addCableLink(List pes, SchemeCableLink link)
	{
		ListIterator lit = pes.listIterator(pes.size());
		if (lit.hasPrevious())
		{
			PathElement pe = (PathElement)lit.previous();
			if (pe.getType() == PathElement.SCHEME_ELEMENT)
			{
				SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, pe.scheme_element_id);
				//если у предыдущего эл-та проставлен end_port_id, ищем по нему
				if (pe.end_port_id.length() != 0)
				{
					for (Iterator it = getCablePorts(se).iterator(); it.hasNext();)
					{
						SchemeCablePort port = (SchemeCablePort)it.next();
						if (port.getId().equals(pe.end_port_id))
							return addCableLink(pes, port, link);
					}
				}
				else //в противном случае ищем по общему порту предыдущего эл-та и линка
				{
					for (Iterator it = getCablePorts(se).iterator(); it.hasNext();)
					{
						SchemeCablePort port = (SchemeCablePort)it.next();
						if (port.getId().equals(link.source_port_id) ||
								port.getId().equals(link.target_port_id))
						{
							pe.end_port_id = port.getId();
							return addCableLink(pes, port, link);
						}
					}
				}
			}
		}
		return null;
	}

	private static PathElement addCableLink(List pes, SchemeCablePort port, SchemeCableLink link)
	{
		String thread_id = getNextThreadId(pes, link);
		if (thread_id.length() == 0)
			return null;

		int number = 0;
		ListIterator it = pes.listIterator(pes.size());
		if (it.hasPrevious())
			number = ((PathElement)it.previous()).n;

		PathElement newPE = new PathElement();
		newPE.setType(PathElement.CABLE_LINK);
		newPE.link_id = link.getId();
		newPE.thread_id = thread_id;
		newPE.n = number;

		if (port.getId().equals(link.source_port_id))
		{
			newPE.start_port_id = link.source_port_id;
			newPE.end_port_id = link.target_port_id;
		}
		else if (port.getId().equals(link.target_port_id))
		{
			newPE.start_port_id = link.target_port_id;
			newPE.end_port_id = link.source_port_id;
		}
		pes.add(newPE);
		return newPE;
	}

	private static List getPorts(SchemeElement se)
	{
		List ports = new ArrayList();
		for (Iterator it = se.devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for (Iterator it2 = sd.ports.iterator(); it2.hasNext();)
				ports.add(it2.next());
		}
		return ports;
	}

	private static List getCablePorts(SchemeElement se)
	{
		List ports = new ArrayList();
		for (Iterator it = se.devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for (Iterator it2 = sd.cableports.iterator(); it2.hasNext();)
				ports.add(it2.next());
		}
		return ports;
	}

	private static List findPorts(SchemeElement se, String direction)
	{
		List ports = new ArrayList();
		for (Iterator it = se.devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for (Iterator it2 = sd.ports.iterator(); it2.hasNext();)
			{
				SchemePort port = (SchemePort)it2.next();
				if (port.direction_type.equals(direction))
					ports.add(port);
			}
		}
		return ports;
	}

	private static List findCablePorts(SchemeElement se, String direction)
	{
		List ports = new ArrayList();
		for (Iterator it = se.devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for (Iterator it2 = sd.cableports.iterator(); it2.hasNext();)
			{
				SchemeCablePort port = (SchemeCablePort)it2.next();
				if (port.direction_type.equals(direction))
					ports.add(port);
			}
		}
		return ports;
	}

}
