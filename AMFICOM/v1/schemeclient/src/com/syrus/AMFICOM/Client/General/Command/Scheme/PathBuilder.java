package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeFactory;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;

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
			if (pe.endAbstractSchemePort() == null)
			{
				if (state == PathBuilder.MULTIPLE_PORTS)
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Через элемент " +
							pe.name() +
							" невозможно однозначно провести путь.\nПожалуйста, введите следующий элемент пути вручную.",
							"Ошибка", JOptionPane.OK_OPTION);

				state = OK;
				return false;
			}

			if (pe.type() == Type.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.endAbstractSchemePort();
				if (port instanceof SchemePort)
				{
					SchemeLink link = ((SchemePort)port).schemeLink();
					if (link == null)
						return false;
					newPE = addLink(pes, (SchemePort)port, link);
				}
				else if (port instanceof SchemeCablePort)
				{
					SchemeCablePort cport = (SchemeCablePort)port;
					SchemeCableLink clink = cport.schemeCableLink();
					if (clink == null)
						return false;
					newPE = addCableLink(pes, cport, clink,
							((SchemePort)pe.startAbstractSchemePort()).schemeCableThread());
				}
				if (newPE == null)
					return false;
			}
			else
			{
				PathElement newPE = null;
				AbstractSchemePort port = pe.endAbstractSchemePort();
				if (port != null)
				{
					SchemeElement se = SchemeUtils.getSchemeElementByDevice(scheme, port.schemeDevice());
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
			if (pe.endAbstractSchemePort() == null)
				return false;

			if (pe.type() == Type.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.endAbstractSchemePort();
				if (port instanceof SchemePort)
				{
					SchemeLink link = ((SchemePort)port).schemeLink();
					if (link == null)
						return false;
					newPE = addLink(pes, (SchemePort)port, link);
				}
				else if (port instanceof SchemeCablePort)
				{
					SchemeCablePort cport = (SchemeCablePort)port;
					SchemeCableLink clink = cport.schemeCableLink();
					if (clink == null)
						return false;
					newPE = addCableLink(pes, cport, clink,
							((SchemePort)pe.startAbstractSchemePort()).schemeCableThread());
				}
				if (newPE == null)
					return false;
			}
			else
			{
				PathElement newPE = null;
				AbstractSchemePort port = pe.endAbstractSchemePort();
				if (port != null)
				{
					SchemeElement se = SchemeUtils.getSchemeElementByDevice(scheme_element, port.schemeDevice());
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
		List links = Arrays.asList(path.links());

		if (links.isEmpty())
		{
			if (path.startDevice() == null)
				return false;
			SchemeElement se = path.startDevice();
			PathElement pe = addSchemeElement(links, se);
			if (pe == null)
				return false;
		}

		while(true)
		{
			PathElement pe = (PathElement)links.listIterator(links.size()).previous();
			if (pe.abstractSchemeElement().equals(path.endDevice()))
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"Построение пути успешно завершено", "Сообшение", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}

			if (pe.endAbstractSchemePort() == null)
			{
				if (state == PathBuilder.MULTIPLE_PORTS)
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							"Через элемент " + pe.name() +
							" невозможно однозначно провести путь.\nПожалуйста, введите следующий элемент пути вручную.",
							"Ошибка", JOptionPane.OK_OPTION);

				state = OK;
				return false;
			}


			if (pe.type() == Type.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.endAbstractSchemePort();
				if (port instanceof SchemePort)
				{
					SchemeLink link = ((SchemePort)port).schemeLink();
					if (link == null)
						return false;
					newPE = addLink(links, (SchemePort)port, link);
				}
				else if (port instanceof SchemeCablePort)
				{
					SchemeCablePort cport = (SchemeCablePort)port;
					SchemeCableLink clink = cport.schemeCableLink();
					if (clink == null)
						return false;
					newPE = addCableLink(links, cport, clink,
							((SchemePort)pe.startAbstractSchemePort()).schemeCableThread());
				}
				if (newPE == null)
					return false;
			}
			else
			{
				PathElement newPE = null;
				AbstractSchemePort port = pe.endAbstractSchemePort();
				if (port != null)
				{
					SchemeElement se = SchemeUtils.getSchemeElementByDevice(scheme, port.schemeDevice());
					if (se == null)
						return false;
					newPE = addSchemeElement(links, se);
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
			if (se.internalScheme() != null)
			{
				Scheme scheme = se.internalScheme();
				exploreScheme(pes, scheme);
				return (PathElement)pes.get(pes.size() - 1);
			}
			else if (se.schemeElements().length != 0)
			{
				exploreSchemeElement(pes, se);
				return (PathElement)pes.get(pes.size() - 1);
			}

			PathElement pe = (PathElement)lit.previous();

			newPE = SchemeFactory.createPathElement();
			newPE.type(Type.SCHEME_ELEMENT);
			newPE.abstractSchemeElement(se);
			newPE.scheme(se.scheme());
			newPE.sequentialNumber(pe.sequentialNumber() + 1);
			newPE.startAbstractSchemePort(pe.endAbstractSchemePort());

			if (pe.type() == Type.SCHEME_LINK)
			{
				SchemeLink link = (SchemeLink)pe.abstractSchemeElement();
				SchemePort startPort = (SchemePort)pe.endAbstractSchemePort();
				if (!SchemeUtils.isSchemeElementContainsPort(se, startPort)) //нет общих портов
					return null;
				// searching for ports with opposite direction
				List ports;
				List cports;
				SchemeDevice dev = startPort.schemeDevice();
				if (startPort.directionType().equals(DirectionType._IN))
				{
					cports = findCablePorts(dev, DirectionType._OUT);
					ports = findPorts(dev, DirectionType._OUT);
				}
				else
				{
					cports = findCablePorts(dev, DirectionType._IN);
					ports = findPorts(dev, DirectionType._IN);
				}
				if (ports.size() == 0)
				{
					if (cports.size() == 1) // if the only cable port with opposite direction
						newPE.endAbstractSchemePort((SchemeCablePort)cports.get(0));
					else //searching what thread start port routed with
					{
						SchemeCableThread thread = startPort.schemeCableThread();
						if (thread != null)
						{
							SchemeCablePort port = getCablePortByThread(cports, thread);
							if (port != null)
								newPE.endAbstractSchemePort(port);
						}
					}
				}
				// the only port with opposite direction
				else if (ports.size() == 1 && cports.size() == 0)
					newPE.endAbstractSchemePort((SchemePort)ports.get(0));
				else if (ports.size() > 1)
					state = MULTIPLE_PORTS;
				// else we couldn't go further
			}
			else if (pe.type() == Type.SCHEME_CABLE_LINK)
			{
				SchemeCableLink link = (SchemeCableLink)pe.abstractSchemeElement();
				SchemeCablePort startPort = (SchemeCablePort)pe.endAbstractSchemePort();
				if (!SchemeUtils.isSchemeElementContainsPort(se, startPort)) //нет общих портов
					return null;
				// searching for ports with opposite direction
				List ports;
				List cports;
				SchemeDevice dev = startPort.schemeDevice();
				if (startPort.directionType().equals(DirectionType._IN))
				{
					cports = findCablePorts(dev, DirectionType._OUT);
					ports = findPorts(dev, DirectionType._OUT);
				}
				else
				{
					cports = findCablePorts(dev, DirectionType._IN);
					ports = findPorts(dev, DirectionType._IN);
				}
				// must be the only cable port with opposite direction
				if (ports.size() == 0 && cports.size() == 1)
				{
					newPE.endAbstractSchemePort((SchemeCablePort)cports.get(0));
				}
				// or any positive number of ports
				else if (ports.size() > 0 && cports.size() == 0)
				{
					SchemePort port = pe.schemeCableThread().getSchemePort(dev);
					if (port != null)
						newPE.endAbstractSchemePort(port);
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
			if (se.internalScheme() != null)
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Начальным устройством не может быть схема", "Ошибка", JOptionPane.OK_OPTION);
				return null;
			}

			for (Iterator it = SchemeUtils.getPorts(se).iterator(); it.hasNext();)
			{
				SchemePort p = (SchemePort)it.next();
				if (p.measurementPortType() != null)
				{
					port = p;
					accessPorts++;
				}
			}
			if (accessPorts == 0)
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "У начального устройства должен быть тестовый порт\nс которого должен начинаться маршрут тестирования", "Ошибка", JOptionPane.OK_OPTION);
				return null;
			}
			newPE = SchemeFactory.createPathElement();
			newPE.type(Type.SCHEME_ELEMENT);
			newPE.abstractSchemeElement(se);
			newPE.scheme(se.scheme());
			newPE.sequentialNumber(1);
			if (accessPorts == 1)
				newPE.endAbstractSchemePort(port);
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
			if (pe.type() == Type.SCHEME_ELEMENT)
			{
				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();

				//если у предыдущего эл-та проставлен endPortId, ищем по нему
				if (pe.endAbstractSchemePort() != null)
				{
					for (Iterator it = SchemeUtils.getPorts(se).iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.equals(pe.endAbstractSchemePort()))
							return addLink(pes, port, link);
					}
				}
				else //в противном случае ищем по общему порту предыдущего эл-та и линка
				{
					for (Iterator it = SchemeUtils.getPorts(se).iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.equals(link.sourceSchemePort()) ||
								port.equals(link.targetSchemePort()))
						{
							pe.endAbstractSchemePort(port);
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
			number = ((PathElement)it.previous()).sequentialNumber() + 1;

		PathElement newPE = SchemeFactory.createPathElement();
		newPE.type(Type.SCHEME_LINK);
		newPE.abstractSchemeElement(link);
		newPE.scheme(link.scheme());
		newPE.sequentialNumber(number);

		if (port.equals(link.sourceSchemePort()))
		{
			newPE.startAbstractSchemePort(link.sourceSchemePort());
			newPE.endAbstractSchemePort(link.targetSchemePort());
		}
		else if (port.equals(link.targetSchemePort()))
		{
			newPE.startAbstractSchemePort(link.targetSchemePort());
			newPE.endAbstractSchemePort(link.sourceSchemePort());
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
			if (pe.type() == Type.SCHEME_ELEMENT)
			{
				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				//если у предыдущего эл-та проставлен endPortId, ищем по нему
				if (pe.endAbstractSchemePort() != null)
				{
					for (Iterator it = SchemeUtils.getCablePorts(se).iterator(); it.hasNext();)
					{
						SchemeCablePort port = (SchemeCablePort)it.next();
						if (port.equals(pe.endAbstractSchemePort()))
						{
							if (pe.startAbstractSchemePort() instanceof SchemePort)
							{
								SchemePort startPort = (SchemePort)pe.startAbstractSchemePort();
								return addCableLink(pes, port, link, startPort.schemeCableThread());
							}
						}
					}
				}
				else //в противном случае ищем по общему порту предыдущего эл-та и линка
				{
					for (Iterator it = SchemeUtils.getCablePorts(se).iterator(); it.hasNext();)
					{
						SchemeCablePort port = (SchemeCablePort)it.next();
						if (port.equals(link.sourceSchemeCablePort()) ||
								port.equals(link.targetSchemeCablePort()))
						{
							pe.endAbstractSchemePort(port);
							if (pe.startAbstractSchemePort() instanceof SchemePort)
							{
								SchemePort startPort = (SchemePort)pe.startAbstractSchemePort();
								return addCableLink(pes, port, link, startPort.schemeCableThread());
							}
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
							"Пожалуйста, проверьте коммутацию в элементе " + pe.name(),
							"Ошибка", JOptionPane.OK_OPTION);
			return null;
		}

		int number = 0;
		ListIterator it = pes.listIterator(pes.size());
		if (it.hasPrevious())
			number = ((PathElement)it.previous()).sequentialNumber() + 1;

		PathElement newPE = SchemeFactory.createPathElement();
		newPE.type(Type.SCHEME_CABLE_LINK);
		newPE.abstractSchemeElement(link);
		newPE.schemeCableThread(thread);
		newPE.scheme(link.scheme());
		newPE.sequentialNumber(number);

		if (port.equals(link.sourceSchemeCablePort()))
		{
			newPE.startAbstractSchemePort(link.sourceSchemeCablePort());
			newPE.endAbstractSchemePort(link.targetSchemeCablePort());
		}
		else if (port.equals(link.targetSchemeCablePort()))
		{
			newPE.startAbstractSchemePort(link.targetSchemeCablePort());
			newPE.endAbstractSchemePort(link.sourceSchemeCablePort());
		}
		else //нет общих портов
			return null;

		pes.add(newPE);
		return newPE;
	}

	private static SchemeCablePort getCablePortByThread(List cableports, SchemeCableThread thread)
	{
		for (Iterator it = cableports.iterator(); it.hasNext(); )
		{
			SchemeCablePort port = (SchemeCablePort)it.next();
			if (port.schemeCableLink() != null)
			{
				if (Arrays.asList(port.schemeCableLink().schemeCableThreads()).contains(thread))
					return port;
			}
		}
		return null;
	}


	private static List findPorts(SchemeDevice dev, DirectionType direction)
	{
		List ports = new ArrayList();
		SchemePort[] schemePorts = dev.schemePorts();
		for (int i = 0; i < schemePorts.length; i++)
		{
			if (schemePorts[i].directionType().equals(direction))
				ports.add(schemePorts[i]);
		}
		return ports;
	}

	private static List findCablePorts(SchemeDevice dev, DirectionType direction)
	{
		List ports = new ArrayList();
		SchemeCablePort[] schemeCablePorts = dev.schemeCablePorts();
		for (int i = 0; i < schemeCablePorts.length; i++)
		{
			if (schemeCablePorts[i].directionType().equals(direction))
				ports.add(schemeCablePorts[i]);
		}
		return ports;
	}
}
