package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class PathBuilder
{
	private static final int OK = 0;
	private static final int MULTIPLE_PORTS = 1;
	private static int state = OK;

	private PathBuilder()
			{}

	private static boolean exploreScheme(List pes, Scheme scheme)
	{
		if (pes.size() == 0)
			return false;

		while(true)
		{
			PathElement pe = (PathElement)pes.listIterator(pes.size()).previous();
			if (pe.endPortId.length() == 0)
			{
				if (state == PathBuilder.MULTIPLE_PORTS)
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Через элемент " +
							pe.getName() +
							" невозможно однозначно провести путь.\nПожалуйста, введите следующий элемент пути вручную.",
							"Ошибка", JOptionPane.OK_OPTION);

				state = OK;
				return false;
			}

			if (pe.getType() == PathElement.SCHEME_ELEMENT)
			{
				PathElement newPE;
				SchemeElement se = pe.getSchemeElement();
				SchemePort port = se.getPort(pe.endPortId);
				if (port != null)
				{
					SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, port.linkId);
					if (link == null)
						return false;
					newPE = addLink(pes, port, link);
				}
				else
				{
					SchemeCablePort cport = se.getCablePort(pe.endPortId);
					if (cport == null)
						return false;
					SchemeCableLink clink = (SchemeCableLink)Pool.get(SchemeCableLink.typ, cport.cableLinkId);
					if (clink == null)
						return false;
					SchemeDevice dev = getDeviceByCablePort(se, cport);
					newPE = addCableLink(pes, cport, clink, dev.getRoutedThread(se.getPort(pe.startPortId)));
				}
				if (newPE == null)
					return false;
			}
			else
			{
				PathElement newPE;
				SchemePort port = (SchemePort)Pool.get(SchemePort.typ, pe.endPortId);
				if (port != null)
				{
					SchemeElement se = scheme.getSchemeElementByPort(port.getId());
					if (se == null)
						return false;
					newPE = addSchemeElement(pes, se);
				}
				else
				{
					SchemeCablePort cport = (SchemeCablePort)Pool.get(SchemeCablePort.typ, pe.endPortId);
					if (cport == null)
						return false;
					SchemeElement se = scheme.getSchemeElementByCablePort(cport.getId());
					if (se == null)
						return false;
					newPE = addSchemeElement(pes, se);
				}
				if (newPE == null)
					return false;
			}
		}
	}

	private static boolean exploreSchemeElement(List pes, SchemeElement scheme_element)
	{
		if (pes.size() == 0)
			return false;

		while(true)
		{
			PathElement pe = (PathElement)pes.listIterator(pes.size()).previous();
			if (pe.endPortId.length() == 0)
				return false;

			if (pe.getType() == PathElement.SCHEME_ELEMENT)
			{
				PathElement newPE;
				SchemeElement se = pe.getSchemeElement();
				SchemePort port = se.getPort(pe.endPortId);
				if (port != null)
				{
					SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, port.linkId);
					if (link == null)
						return false;
					newPE = addLink(pes, port, link);
				}
				else
				{
					SchemeCablePort cport = se.getCablePort(pe.endPortId);
					if (cport == null)
						return false;
					SchemeCableLink clink = (SchemeCableLink)Pool.get(SchemeCableLink.typ, cport.cableLinkId);
					if (clink == null)
						return false;
					SchemeDevice dev = getDeviceByCablePort(se, cport);
					newPE = addCableLink(pes, cport, clink, dev.getRoutedThread(se.getPort(pe.startPortId)));
				}
				if (newPE == null)
					return false;
			}
			else
			{
				PathElement newPE;
				SchemePort port = (SchemePort)Pool.get(SchemePort.typ, pe.endPortId);
				if (port != null)
				{
					SchemeElement se = scheme_element.getSchemeElementByPort(port.getId());
					if (se == null)
						return false;
					newPE = addSchemeElement(pes, se);
				}
				else
				{
					SchemeCablePort cport = (SchemeCablePort)Pool.get(SchemeCablePort.typ, pe.endPortId);
					if (cport == null)
						return false;
					SchemeElement se = scheme_element.getSchemeElementByCablePort(cport.getId());
					if (se == null)
						return false;
					newPE = addSchemeElement(pes, se);
				}
				if (newPE == null)
					return false;
			}
		}
	}


	public static boolean explore(Scheme scheme, SchemePath path)
	{
		if (path.links.isEmpty())
		{
			if (path.startDeviceId.length() == 0)
				return false;
			SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, path.startDeviceId);
			PathElement pe = addSchemeElement(path.links, se);
			if (pe == null)
				return false;
		}

		while(true)
		{
			PathElement pe = (PathElement)path.links.listIterator(path.links.size()).previous();
			if (path.endDeviceId.equals(pe.getObjectId()))
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"Построение пути успешно завершено", "Сообшение", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}

			if (pe.endPortId.length() == 0)
			{
				if (state == PathBuilder.MULTIPLE_PORTS)
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							"Через элемент " + pe.getName() +
							" невозможно однозначно провести путь.\nПожалуйста, введите следующий элемент пути вручную.",
							"Ошибка", JOptionPane.OK_OPTION);

				state = OK;
				return false;
			}


			if (pe.getType() == PathElement.SCHEME_ELEMENT)
			{
				PathElement newPE;
				SchemeElement se = pe.getSchemeElement();
				SchemePort port = se.getPort(pe.endPortId);
				if (port != null)
				{
					SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, port.linkId);
					if (link == null)
						return false;
					newPE = addLink(path.links, port, link);
				}
				else
				{
					SchemeCablePort cport = se.getCablePort(pe.endPortId);
					if (cport == null)
						return false;
					SchemeCableLink clink = (SchemeCableLink)Pool.get(SchemeCableLink.typ, cport.cableLinkId);
					if (clink == null)
						return false;
					SchemeDevice dev = getDeviceByCablePort(se, cport);
					newPE = addCableLink(path.links, cport, clink, dev.getRoutedThread(se.getPort(pe.startPortId)));
				}
				if (newPE == null)
					return false;
			}
			else
			{
				PathElement newPE;
				SchemePort port = (SchemePort)Pool.get(SchemePort.typ, pe.endPortId);
				if (port != null)
				{
					SchemeElement se = scheme.getSchemeElementByPort(port.getId());
					if (se == null)
						return false;
					newPE = addSchemeElement(path.links, se);
				}
				else
				{
					SchemeCablePort cport = (SchemeCablePort)Pool.get(SchemeCablePort.typ, pe.endPortId);
					if (cport == null)
						return false;
					SchemeElement se = scheme.getSchemeElementByCablePort(cport.getId());
					if (se == null)
						return false;
					newPE = addSchemeElement(path.links, se);
				}
				if (newPE == null)
					return false;
			}
		}
	}

	public static PathElement addSchemeElement(List pes, SchemeElement se)
	{
		PathElement newPE = null;
		ListIterator lit = pes.listIterator(pes.size());
		if (lit.hasPrevious())
		{
			if (se.getInternalSchemeId().length() != 0)
			{
				Scheme scheme = se.getInternalScheme();
				exploreScheme(pes, scheme);
				return (PathElement)pes.get(pes.size() - 1);
			}
			else if (!se.elementIds.isEmpty())
			{
				exploreSchemeElement(pes, se);
				return (PathElement)pes.get(pes.size() - 1);
			}

			PathElement pe = (PathElement)lit.previous();

			newPE = new PathElement();
			newPE.setType(PathElement.SCHEME_ELEMENT);
			newPE.setSchemeElement(se);
			newPE.schemeId = se.getSchemeId();
			newPE.n = pe.n + 1;
			newPE.startPortId = pe.endPortId;

			if (pe.getType() == PathElement.LINK)
			{
				SchemeLink link = pe.getSchemeLink();
				SchemePort startPort = se.getPort(pe.endPortId);
				if (startPort == null) //нет общих портов
					return null;
				// searching for ports with opposite direction
				List ports;
				List cports;
				SchemeDevice dev = getDeviceByPort(se, startPort);
				if (startPort.directionType.equals("in"))
				{
					cports = findCablePorts(dev, "out");
					ports = findPorts(dev, "out");
				}
				else
				{
					cports = findCablePorts(dev, "in");
					ports = findPorts(dev, "in");
				}
				if (ports.size() == 0)
				{
					if (cports.size() == 1) // if the only cable port with opposite direction
						newPE.endPortId = ((SchemeCablePort)cports.get(0)).getId();
					else //searching what thread start port routed with
					{
						SchemeCableThread thread = dev.getRoutedThread(startPort);
						if (thread != null)
						{
							SchemeCablePort port = getCablePortByThread(cports, thread);
							if (port != null)
								newPE.endPortId = port.getId();
						}
					}
				}
				// the only port with opposite direction
				else if (ports.size() == 1 && cports.size() == 0)
					newPE.endPortId = ((SchemePort)ports.get(0)).getId();
				else if (ports.size() > 1)
					state = MULTIPLE_PORTS;
				// else we couldn't go further
			}
			else if (pe.getType() == PathElement.CABLE_LINK)
			{
				SchemeCableLink link = pe.getSchemeCableLink();
				SchemeCablePort startPort = se.getCablePort(pe.endPortId);
				if (startPort == null) //нет общих портов
					return null;
				// searching for ports with opposite direction
				List ports;
				List cports;
				SchemeDevice dev = getDeviceByCablePort(se, startPort);
				if (startPort.directionType.equals("in"))
				{
					cports = findCablePorts(dev, "out");
					ports = findPorts(dev, "out");
				}
				else
				{
					cports = findCablePorts(dev, "in");
					ports = findPorts(dev, "in");
				}
				// must be the only cable port with opposite direction
				if (ports.size() == 0 && cports.size() == 1)
				{
					newPE.endPortId = ((SchemeCablePort)cports.get(0)).getId();
				}
				// or any positive number of ports
				else if (ports.size() > 0 && cports.size() == 0)
				{
					SchemePort port = dev.getRoutedPort(link.getCableThread(pe.threadId));
					if (port != null)
						newPE.endPortId = port.getId();
				}
			}
			else
				return null;
		}
		else //first element
		{
			int accessPorts = 0;
			SchemePort port = null;
			// must be at least one access port
			if (se.getInternalSchemeId().length() != 0)
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Начальным устройством не может быть схема", "Ошибка", JOptionPane.OK_OPTION);
				return null;
			}

			for (Iterator it = getPorts(se).iterator(); it.hasNext();)
			{
				SchemePort p = (SchemePort)it.next();
				if (p.measurementPortTypeId.length() != 0)
				{
					port = p;
					accessPorts++;
				}
			}
			if (accessPorts == 0)
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "У начального устройства должен быть тестовый порт\nс которого должен начинаться маршрут тестирования", "Ошибка", JOptionPane.OK_OPTION);
				//типа низзя
				return null;
			}
			newPE = new PathElement();
			newPE.setType(PathElement.SCHEME_ELEMENT);
			newPE.setSchemeElement(se);
			newPE.schemeId = se.getSchemeId();
			newPE.n = 1;
			if (accessPorts == 1)
				newPE.endPortId = port.getId();
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
				SchemeElement se = pe.getSchemeElement();

				//если у предыдущего эл-та проставлен endPortId, ищем по нему
				if (pe.endPortId.length() != 0)
				{
					for (Iterator it = getPorts(se).iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.getId().equals(pe.endPortId))
							return addLink(pes, port, link);
					}
				}
				else //в противном случае ищем по общему порту предыдущего эл-та и линка
				{
					for (Iterator it = getPorts(se).iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.getId().equals(link.sourcePortId) ||
								port.getId().equals(link.targetPortId))
						{
							pe.endPortId = port.getId();
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
			number = ((PathElement)it.previous()).n + 1;

		PathElement newPE = new PathElement();
		newPE.setType(PathElement.LINK);
		newPE.setSchemeLink(link);
		newPE.schemeId = link.getSchemeId();
		newPE.n = number;

		if (port.getId().equals(link.sourcePortId))
		{
			newPE.startPortId = link.sourcePortId;
			newPE.endPortId = link.targetPortId;
		}
		else if (port.getId().equals(link.targetPortId))
		{
			newPE.startPortId = link.targetPortId;
			newPE.endPortId = link.sourcePortId;
		}
		else //нет общих портов
			return null;

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
				SchemeElement se = pe.getSchemeElement();
				//если у предыдущего эл-та проставлен endPortId, ищем по нему
				if (pe.endPortId.length() != 0)
				{
					for (Iterator it = getCablePorts(se).iterator(); it.hasNext();)
					{
						SchemeCablePort port = (SchemeCablePort)it.next();
						if (port.getId().equals(pe.endPortId))
						{
							SchemeDevice dev = getDeviceByCablePort(se, port);
							return addCableLink(pes, port, link, dev.getRoutedThread(se.getPort(pe.startPortId)));
						}
					}
				}
				else //в противном случае ищем по общему порту предыдущего эл-та и линка
				{
					for (Iterator it = getCablePorts(se).iterator(); it.hasNext();)
					{
						SchemeCablePort port = (SchemeCablePort)it.next();
						if (port.getId().equals(link.sourcePortId) ||
								port.getId().equals(link.targetPortId))
						{
							pe.endPortId = port.getId();
							SchemeDevice dev = getDeviceByCablePort(se, port);
							return addCableLink(pes, port, link, dev.getRoutedThread(se.getPort(pe.startPortId)));
						}
					}
				}
			}
		}
		return null;
	}

	private static PathElement addCableLink(List pes, SchemeCablePort port, SchemeCableLink link, SchemeCableThread thread)
	{
		if (thread == null)
		{
			PathElement pe = (PathElement)pes.listIterator(pes.size()).previous();
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							"Пожалуйста, проверьте коммутацию в элементе " + pe.getName(),
							"Ошибка", JOptionPane.OK_OPTION);
			return null;
		}

		int number = 0;
		ListIterator it = pes.listIterator(pes.size());
		if (it.hasPrevious())
			number = ((PathElement)it.previous()).n + 1;

		PathElement newPE = new PathElement();
		newPE.setType(PathElement.CABLE_LINK);
		newPE.setSchemeCableLink(link);
		newPE.threadId = thread.getId();
		newPE.schemeId = link.getSchemeId();
		newPE.n = number;

		if (port.getId().equals(link.sourcePortId))
		{
			newPE.startPortId = link.sourcePortId;
			newPE.endPortId = link.targetPortId;
		}
		else if (port.getId().equals(link.targetPortId))
		{
			newPE.startPortId = link.targetPortId;
			newPE.endPortId = link.sourcePortId;
		}
		else //нет общих портов
			return null;

		pes.add(newPE);
		return newPE;
	}

	private static Collection getPorts(SchemeElement se)
	{
		if (se.devices.size() == 1)
		{
			SchemeDevice sd = (SchemeDevice)se.devices.iterator().next();
			return sd.ports;
		}

		List ports = new ArrayList();
		for (Iterator it = se.devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for (Iterator it2 = sd.ports.iterator(); it2.hasNext();)
				ports.add(it2.next());
		}
		return ports;
	}

	private static SchemeDevice getDeviceByCablePort(SchemeElement se, SchemeCablePort port)
	{
		for (Iterator it = se.devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			if (sd.cableports.contains(port))
				return sd;
		}
		return null;
	}

	private static SchemeDevice getDeviceByPort(SchemeElement se, SchemePort port)
	{
		for (Iterator it = se.devices.iterator(); it.hasNext(); )
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			if (sd.ports.contains(port))
				return sd;
		}
		return null;
	}

	private static SchemeCablePort getCablePortByThread(List cableports, SchemeCableThread thread)
	{
		for (Iterator it = cableports.iterator(); it.hasNext(); )
		{
			SchemeCablePort port = (SchemeCablePort)it.next();
			if (port.cableLinkId.length() != 0)
			{
				SchemeCableLink link = (SchemeCableLink)Pool.get(SchemeCableLink.typ, port.cableLinkId);
				if (link.cableThreads.contains(thread))
					return port;
			}
		}
		return null;
	}


	private static Collection getCablePorts(SchemeElement se)
	{
		if (se.devices.size() == 1)
		{
			SchemeDevice sd = (SchemeDevice) se.devices.iterator().next();
			return sd.cableports;
		}

		List ports = new ArrayList();
		for (Iterator it = se.devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for (Iterator it2 = sd.cableports.iterator(); it2.hasNext();)
				ports.add(it2.next());
		}
		return ports;
	}

	private static List findPorts(SchemeDevice dev, String direction)
	{
		List ports = new ArrayList();
		for (Iterator it2 = dev.ports.iterator(); it2.hasNext();)
		{
			SchemePort port = (SchemePort)it2.next();
			if (port.directionType.equals(direction))
				ports.add(port);
		}
		return ports;
	}

	private static List findCablePorts(SchemeDevice dev, String direction)
	{
		List ports = new ArrayList();
		for (Iterator it2 = dev.cableports.iterator(); it2.hasNext();)
		{
			SchemeCablePort port = (SchemeCablePort)it2.next();
			if (port.directionType.equals(direction))
				ports.add(port);
		}
		return ports;
	}
}
