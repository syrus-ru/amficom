package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.util.Log;

public class PathBuilder
{
	private Identifier creatorId;
	private static PathBuilder instance;
	private static final int OK = 0;
	private static final int MULTIPLE_PORTS = 1;
	private static int state = OK;

	private PathBuilder() {
	}
	
	public static PathBuilder getInstance(Identifier creatorId) {
		if (instance == null)
			instance = new PathBuilder();
		instance.creatorId = creatorId;
		return instance;
	}

	private boolean exploreScheme(List pes, Scheme scheme)
	{
		if (pes.size() == 0)
			return false;

		while(true)
		{
			PathElement pe = (PathElement)pes.listIterator(pes.size()).previous();
			if (pe.getEndAbstractSchemePort() == null)
			{
				if (state == PathBuilder.MULTIPLE_PORTS)
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Через элемент " +
							pe.getName() +
							" невозможно однозначно провести путь.\nПожалуйста, введите следующий элемент пути вручную.",
							"Ошибка", JOptionPane.OK_OPTION);

				state = OK;
				return false;
			}

			if (pe.getPathElementKind() == PathElementKind.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
//				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port instanceof SchemePort)
				{
					SchemeLink link = ((SchemePort)port).getSchemeLink();
					if (link == null)
						return false;
					newPE = addLink(pes, (SchemePort)port, link);
				}
				else if (port instanceof SchemeCablePort)
				{
					SchemeCablePort cport = (SchemeCablePort)port;
					SchemeCableLink clink = cport.getSchemeCableLink();
					if (clink == null)
						return false;
					newPE = addCableLink(pes, cport, clink,
							((SchemePort)pe.getStartAbstractSchemePort()).getSchemeCableThread());
				}
				if (newPE == null)
					return false;
			}
			else
			{
				PathElement newPE = null;
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port != null)
				{
					SchemeElement se = SchemeUtils.getSchemeElementByDevice(scheme, port.getParentSchemeDevice());
					if (se == null)
						return false;
					newPE = addSchemeElement(pes, se);
				}
				if (newPE == null)
					return false;
			}
		}
	}

	private boolean exploreSchemeElement(List pes, SchemeElement scheme_element)
	{
		if (pes.size() == 0)
			return false;

		while(true)
		{
			PathElement pe = (PathElement)pes.listIterator(pes.size()).previous();
			if (pe.getEndAbstractSchemePort() == null)
				return false;

			if (pe.getPathElementKind() == PathElementKind.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
//				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port instanceof SchemePort)
				{
					SchemeLink link = ((SchemePort)port).getSchemeLink();
					if (link == null)
						return false;
					newPE = addLink(pes, (SchemePort)port, link);
				}
				else if (port instanceof SchemeCablePort)
				{
					SchemeCablePort cport = (SchemeCablePort)port;
					SchemeCableLink clink = cport.getSchemeCableLink();
					if (clink == null)
						return false;
					newPE = addCableLink(pes, cport, clink,
							((SchemePort)pe.getStartAbstractSchemePort()).getSchemeCableThread());
				}
				if (newPE == null)
					return false;
			}
			else
			{
				PathElement newPE = null;
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port != null)
				{
					SchemeElement se = SchemeUtils.getSchemeElementByDevice(scheme_element, port.getParentSchemeDevice());
					if (se == null)
						return false;
					newPE = addSchemeElement(pes, se);
				}
				if (newPE == null)
					return false;
			}
		}
	}


	public boolean explore(Scheme scheme, SchemePath path)
	{
		List links = Arrays.asList(path.getPathElementsAsArray());

		if (links.isEmpty())
		{
			if (path.getStartSchemeElement() == null)
				return false;
			SchemeElement se = path.getStartSchemeElement();
			PathElement pe = addSchemeElement(links, se);
			if (pe == null)
				return false;
		}

		while(true)
		{
			PathElement pe = (PathElement)links.listIterator(links.size()).previous();
			if (pe.getAbstractSchemeElement().equals(path.getEndSchemeElement()))
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						"Построение пути успешно завершено", "Сообшение", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}

			if (pe.getEndAbstractSchemePort() == null)
			{
				if (state == PathBuilder.MULTIPLE_PORTS)
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							"Через элемент " + pe.getName() +
							" невозможно однозначно провести путь.\nПожалуйста, введите следующий элемент пути вручную.",
							"Ошибка", JOptionPane.OK_OPTION);

				state = OK;
				return false;
			}


			if (pe.getPathElementKind() == PathElementKind.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
//				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port instanceof SchemePort)
				{
					SchemeLink link = ((SchemePort)port).getSchemeLink();
					if (link == null)
						return false;
					newPE = addLink(links, (SchemePort)port, link);
				}
				else if (port instanceof SchemeCablePort)
				{
					SchemeCablePort cport = (SchemeCablePort)port;
					SchemeCableLink clink = cport.getSchemeCableLink();
					if (clink == null)
						return false;
					newPE = addCableLink(links, cport, clink,
							((SchemePort)pe.getStartAbstractSchemePort()).getSchemeCableThread());
				}
				if (newPE == null)
					return false;
			}
			else
			{
				PathElement newPE = null;
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port != null)
				{
					SchemeElement se = SchemeUtils.getSchemeElementByDevice(scheme, port.getParentSchemeDevice());
					if (se == null)
						return false;
					newPE = addSchemeElement(links, se);
				}
				if (newPE == null)
					return false;
			}
		}
	}

	public PathElement addSchemeElement(List pes, SchemeElement se)
	{
		PathElement newPE = null;
		ListIterator lit = pes.listIterator(pes.size());
		if (lit.hasPrevious())
		{
			if (se.getScheme() != null)
			{
				Scheme scheme = se.getScheme();
				exploreScheme(pes, scheme);
				return (PathElement)pes.get(pes.size() - 1);
			}
			else if (se.getSchemeElementsAsArray().length != 0)
			{
				exploreSchemeElement(pes, se);
				return (PathElement)pes.get(pes.size() - 1);
			}

			PathElement pe = (PathElement)lit.previous();

			try {
				newPE = PathElement.createInstance(creatorId, PathElementKind.SCHEME_ELEMENT);
			} 
			catch (CreateObjectException e) {
				Log.errorMessage("Can't create PathElement object " + e.getMessage());
				return null;
			}
			newPE.setAbstractSchemeElement(se);
			newPE.setParentScheme(se.getParentScheme());
			newPE.setSequentialNumber(pe.getSequentialNumber() + 1);
			newPE.setStartAbstractSchemePort(pe.getEndAbstractSchemePort());

			if (pe.getPathElementKind() == PathElementKind.SCHEME_LINK)
			{
//				SchemeLink link = (SchemeLink)pe.abstractSchemeElement();
				SchemePort startPort = (SchemePort)pe.getEndAbstractSchemePort();
				if (!SchemeUtils.isSchemeElementContainsPort(se, startPort)) //нет общих портов
					return null;
				// searching for ports with opposite direction
				List ports;
				List cports;
				SchemeDevice dev = startPort.getParentSchemeDevice();
				if (startPort.getAbstractSchemePortDirectionType().equals(AbstractSchemePortDirectionType._IN))
				{
					cports = findCablePorts(dev, AbstractSchemePortDirectionType._OUT);
					ports = findPorts(dev, AbstractSchemePortDirectionType._OUT);
				}
				else
				{
					cports = findCablePorts(dev, AbstractSchemePortDirectionType._IN);
					ports = findPorts(dev, AbstractSchemePortDirectionType._IN);
				}
				if (ports.size() == 0)
				{
					if (cports.size() == 1) // if the only cable port with opposite direction
						newPE.setEndAbstractSchemePort((SchemeCablePort)cports.get(0));
					else //searching what thread start port routed with
					{
						SchemeCableThread thread = startPort.getSchemeCableThread();
						if (thread != null)
						{
							SchemeCablePort port = getCablePortByThread(cports, thread);
							if (port != null)
								newPE.setEndAbstractSchemePort(port);
						}
					}
				}
				// the only port with opposite direction
				else if (ports.size() == 1 && cports.size() == 0)
					newPE.setEndAbstractSchemePort((SchemePort)ports.get(0));
				else if (ports.size() > 1)
					state = MULTIPLE_PORTS;
				// else we couldn't go further
			}
			else if (pe.getPathElementKind() == PathElementKind.SCHEME_CABLE_LINK)
			{
//				SchemeCableLink link = (SchemeCableLink)pe.abstractSchemeElement();
				SchemeCablePort startPort = (SchemeCablePort)pe.getEndAbstractSchemePort();
				if (!SchemeUtils.isSchemeElementContainsPort(se, startPort)) //нет общих портов
					return null;
				// searching for ports with opposite direction
				List ports;
				List cports;
				SchemeDevice dev = startPort.getParentSchemeDevice();
				if (startPort.getAbstractSchemePortDirectionType().equals(AbstractSchemePortDirectionType._IN))
				{
					cports = findCablePorts(dev, AbstractSchemePortDirectionType._OUT);
					ports = findPorts(dev, AbstractSchemePortDirectionType._OUT);
				}
				else
				{
					cports = findCablePorts(dev, AbstractSchemePortDirectionType._IN);
					ports = findPorts(dev, AbstractSchemePortDirectionType._IN);
				}
				// must be the only cable port with opposite direction
				if (ports.size() == 0 && cports.size() == 1)
				{
					newPE.setEndAbstractSchemePort((SchemeCablePort)cports.get(0));
				}
				// or any positive number of ports
				else if (ports.size() > 0 && cports.size() == 0)
				{
					SchemePort port = pe.getSchemeCableThread().getSchemePort(dev);
					if (port != null)
						newPE.setEndAbstractSchemePort(port);
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
			if (se.getScheme() != null)
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Начальным устройством не может быть схема", "Ошибка", JOptionPane.OK_OPTION);
				return null;
			}

			for (Iterator it = SchemeUtils.getPorts(se).iterator(); it.hasNext();)
			{
				SchemePort p = (SchemePort)it.next();
				if (p.getMeasurementPortType() != null)
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
			try {
				newPE = PathElement.createInstance(creatorId, PathElementKind.SCHEME_ELEMENT);
			} 
			catch (CreateObjectException e) {
				Log.errorMessage("Can't create PathElement object " + e.getMessage());
				return null;
			}
			newPE.setAbstractSchemeElement(se);
			newPE.setParentScheme(se.getParentScheme());
			newPE.setSequentialNumber(1);
			if (accessPorts == 1)
				newPE.setEndAbstractSchemePort(port);
		}
		if (newPE != null)
			pes.add(newPE);
		return newPE;
	}

	public PathElement addLink(List pes, SchemeLink link)
	{
		ListIterator lit = pes.listIterator(pes.size());
		if (lit.hasPrevious())
		{
			PathElement pe = (PathElement)lit.previous();
			if (pe.getPathElementKind() == PathElementKind.SCHEME_ELEMENT)
			{
				SchemeElement se = (SchemeElement)pe.getAbstractSchemeElement();

				//если у предыдущего эл-та проставлен endPortId, ищем по нему
				if (pe.getEndAbstractSchemePort() != null)
				{
					for (Iterator it = SchemeUtils.getPorts(se).iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.equals(pe.getEndAbstractSchemePort()))
							return addLink(pes, port, link);
					}
				}
				else //в противном случае ищем по общему порту предыдущего эл-та и линка
				{
					for (Iterator it = SchemeUtils.getPorts(se).iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.equals(link.getSourceSchemePort()) ||
								port.equals(link.getTargetSchemePort()))
						{
							pe.setEndAbstractSchemePort(port);
							return addLink(pes, port, link);
						}
					}
				}
			}
		}
		return null;
	}

	private PathElement addLink(List pes, SchemePort port, SchemeLink link)
	{
		int number = 0;
		ListIterator it = pes.listIterator(pes.size());
		if (it.hasPrevious())
			number = ((PathElement)it.previous()).getSequentialNumber() + 1;

		PathElement newPE;
		try {
			newPE = PathElement.createInstance(creatorId, PathElementKind.SCHEME_LINK);
		} 
		catch (CreateObjectException e) {
			Log.errorMessage("Can't create PathElement object " + e.getMessage());
			return null;
		}
		newPE.setAbstractSchemeElement(link);
		newPE.setParentScheme(link.getParentScheme());
		newPE.setSequentialNumber(number);

		if (port.equals(link.getSourceSchemePort()))
		{
			newPE.setStartAbstractSchemePort(link.getSourceSchemePort());
			newPE.setEndAbstractSchemePort(link.getTargetSchemePort());
		}
		else if (port.equals(link.getTargetSchemePort()))
		{
			newPE.setStartAbstractSchemePort(link.getTargetSchemePort());
			newPE.setEndAbstractSchemePort(link.getSourceSchemePort());
		}
		else //нет общих портов
			return null;

		pes.add(newPE);
		return newPE;
	}

	public PathElement addCableLink(List pes, SchemeCableLink link)
	{
		ListIterator lit = pes.listIterator(pes.size());
		if (lit.hasPrevious())
		{
			PathElement pe = (PathElement)lit.previous();
			if (pe.getPathElementKind() == PathElementKind.SCHEME_ELEMENT)
			{
				SchemeElement se = (SchemeElement)pe.getAbstractSchemeElement();
				//если у предыдущего эл-та проставлен endPortId, ищем по нему
				if (pe.getEndAbstractSchemePort() != null)
				{
					for (Iterator it = SchemeUtils.getCablePorts(se).iterator(); it.hasNext();)
					{
						SchemeCablePort port = (SchemeCablePort)it.next();
						if (port.equals(pe.getEndAbstractSchemePort()))
						{
							if (pe.getStartAbstractSchemePort() instanceof SchemePort)
							{
								SchemePort startPort = (SchemePort)pe.getStartAbstractSchemePort();
								return addCableLink(pes, port, link, startPort.getSchemeCableThread());
							}
						}
					}
				}
				else //в противном случае ищем по общему порту предыдущего эл-та и линка
				{
					for (Iterator it = SchemeUtils.getCablePorts(se).iterator(); it.hasNext();)
					{
						SchemeCablePort port = (SchemeCablePort)it.next();
						if (port.equals(link.getSourceSchemeCablePort()) ||
								port.equals(link.getTargetSchemeCablePort()))
						{
							pe.setEndAbstractSchemePort(port);
							if (pe.getStartAbstractSchemePort() instanceof SchemePort)
							{
								SchemePort startPort = (SchemePort)pe.getStartAbstractSchemePort();
								return addCableLink(pes, port, link, startPort.getSchemeCableThread());
							}
						}
					}
				}
			}
		}
		return null;
	}

	private PathElement addCableLink(List pes, SchemeCablePort port, SchemeCableLink link, SchemeCableThread thread)
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
			number = ((PathElement)it.previous()).getSequentialNumber() + 1;

		PathElement newPE;
		try {
			newPE = PathElement.createInstance(creatorId, PathElementKind.SCHEME_CABLE_LINK);
		} 
		catch (CreateObjectException e) {
			Log.errorMessage("Can't create PathElement object " + e.getMessage());
			return null;
		}
		newPE.setAbstractSchemeElement(link);
		newPE.setSchemeCableThread(thread);
		newPE.setParentScheme(link.getParentScheme());
		newPE.setSequentialNumber(number);

		if (port.equals(link.getSourceSchemeCablePort()))
		{
			newPE.setStartAbstractSchemePort(link.getSourceSchemeCablePort());
			newPE.setEndAbstractSchemePort(link.getTargetSchemeCablePort());
		}
		else if (port.equals(link.getTargetSchemeCablePort()))
		{
			newPE.setStartAbstractSchemePort(link.getTargetSchemeCablePort());
			newPE.setEndAbstractSchemePort(link.getSourceSchemeCablePort());
		}
		else //нет общих портов
			return null;

		pes.add(newPE);
		return newPE;
	}

	private SchemeCablePort getCablePortByThread(List cableports, SchemeCableThread thread)
	{
		for (Iterator it = cableports.iterator(); it.hasNext(); )
		{
			SchemeCablePort port = (SchemeCablePort)it.next();
			if (port.getSchemeCableLink() != null)
			{
				if (Arrays.asList(port.getSchemeCableLink().getSchemeCableThreadsAsArray()).contains(thread))
					return port;
			}
		}
		return null;
	}


	private List findPorts(SchemeDevice dev, AbstractSchemePortDirectionType direction)
	{
		List ports = new ArrayList();
		SchemePort[] schemePorts = dev.getSchemePortsAsArray();
		for (int i = 0; i < schemePorts.length; i++)
		{
			if (schemePorts[i].getAbstractSchemePortDirectionType().equals(direction))
				ports.add(schemePorts[i]);
		}
		return ports;
	}

	private List findCablePorts(SchemeDevice dev, AbstractSchemePortDirectionType direction)
	{
		List ports = new ArrayList();
		SchemeCablePort[] schemeCablePorts = dev.getSchemeCablePortsAsArray();
		for (int i = 0; i < schemeCablePorts.length; i++)
		{
			if (schemeCablePorts[i].getAbstractSchemePortDirectionType().equals(direction))
				ports.add(schemeCablePorts[i]);
		}
		return ports;
	}
	
}
