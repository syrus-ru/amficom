package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.AMFICOM.scheme.corba.PathElement_TransferablePackage.DataPackage.Kind;
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

	private boolean exploreScheme(SchemePath path, Scheme scheme)
	{
		if (path.getPathElements().isEmpty())
			return false;

		while(true)
		{
			PathElement pe = (PathElement)path.getPathElements().last();
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

			if (pe.getKind() == Kind.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
//				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port instanceof SchemePort)
				{
					SchemeLink link = ((SchemePort)port).getSchemeLink();
					if (link == null)
						return false;
					newPE = addLink(path, (SchemePort)port, link);
				} else if (port instanceof SchemeCablePort)
				{
					SchemeCablePort cport = (SchemeCablePort)port;
					SchemeCableLink clink = cport.getSchemeCableLink();
					if (clink == null)
						return false;
					newPE = addCableLink(path, cport, clink,
							((SchemePort)pe.getStartAbstractSchemePort()).getSchemeCableThread());
				}
				if (newPE == null)
					return false;
			} else
			{
				PathElement newPE = null;
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port != null)
				{
					SchemeElement se = SchemeUtils.getSchemeElementByDevice(scheme, port.getParentSchemeDevice());
					if (se == null)
						return false;
					newPE = addSchemeElement(path, se);
				}
				if (newPE == null)
					return false;
			}
		}
	}

	private boolean exploreSchemeElement(SchemePath path, SchemeElement scheme_element)
	{
		if (path.getPathElements().isEmpty())
			return false;

		while(true)
		{
			PathElement pe = (PathElement)path.getPathElements().last();
			if (pe.getEndAbstractSchemePort() == null)
				return false;

			if (pe.getKind() == Kind.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
//				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port instanceof SchemePort)
				{
					SchemeLink link = ((SchemePort)port).getSchemeLink();
					if (link == null)
						return false;
					newPE = addLink(path, (SchemePort)port, link);
				} else if (port instanceof SchemeCablePort)
				{
					SchemeCablePort cport = (SchemeCablePort)port;
					SchemeCableLink clink = cport.getSchemeCableLink();
					if (clink == null)
						return false;
					newPE = addCableLink(path, cport, clink,
							((SchemePort)pe.getStartAbstractSchemePort()).getSchemeCableThread());
				}
				if (newPE == null)
					return false;
			} else
			{
				PathElement newPE = null;
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port != null)
				{
					SchemeElement se = SchemeUtils.getSchemeElementByDevice(scheme_element, port.getParentSchemeDevice());
					if (se == null)
						return false;
					newPE = addSchemeElement(path, se);
				}
				if (newPE == null)
					return false;
			}
		}
	}


	public boolean explore(Scheme scheme, SchemePath path)
	{
		if (path.getPathElements().isEmpty())
		{
			if (path.getStartSchemeElement() == null)
				return false;
			SchemeElement se = path.getStartSchemeElement();
			PathElement pe = addSchemeElement(path, se);
			if (pe == null)
				return false;
		}

		while(true)
		{
			PathElement pe = (PathElement)path.getPathElements().last();
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


			if (pe.getKind() == Kind.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
//				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port instanceof SchemePort)
				{
					SchemeLink link = ((SchemePort)port).getSchemeLink();
					if (link == null)
						return false;
					newPE = addLink(path, (SchemePort)port, link);
				} else if (port instanceof SchemeCablePort)
				{
					SchemeCablePort cport = (SchemeCablePort)port;
					SchemeCableLink clink = cport.getSchemeCableLink();
					if (clink == null)
						return false;
					newPE = addCableLink(path, cport, clink,
							((SchemePort)pe.getStartAbstractSchemePort()).getSchemeCableThread());
				}
				if (newPE == null)
					return false;
			} else
			{
				PathElement newPE = null;
				AbstractSchemePort port = pe.getEndAbstractSchemePort();
				if (port != null)
				{
					SchemeElement se = SchemeUtils.getSchemeElementByDevice(scheme, port.getParentSchemeDevice());
					if (se == null)
						return false;
					newPE = addSchemeElement(path, se);
				}
				if (newPE == null)
					return false;
			}
		}
	}

	public PathElement addSchemeElement(SchemePath path, SchemeElement se)
	{
		PathElement newPE = null;
		Object last = path.getPathElements().last();
		if (last != null)
		{
			if (se.getScheme() != null)
			{
				Scheme scheme = se.getScheme();
				exploreScheme(path, scheme);
				return (PathElement)path.getPathElements().last();
			} else if (!se.getSchemeElements().isEmpty())
			{
				exploreSchemeElement(path, se);
				return (PathElement)path.getPathElements().last();
			}

			PathElement pe = (PathElement)path.getPathElements().last();

			try {
				newPE = PathElement.createInstance(creatorId, path, pe.getEndAbstractSchemePort(), null);
			} 
			catch (CreateObjectException e) {
				Log.errorMessage("Can't create PathElement object " + e.getMessage());
				return null;
			}

			if (pe.getKind() == Kind.SCHEME_LINK)
			{
//				SchemeLink link = (SchemeLink)pe.abstractSchemeElement();
				SchemePort startPort = (SchemePort)pe.getEndAbstractSchemePort();
				if (!SchemeUtils.isSchemeElementContainsPort(se, startPort)) //нет общих портов
					return null;
				// searching for ports with opposite direction
				List ports;
				List cports;
				SchemeDevice dev = startPort.getParentSchemeDevice();
				if (startPort.getDirectionType().equals(AbstractSchemePortDirectionType._IN))
				{
					cports = findCablePorts(dev, AbstractSchemePortDirectionType._OUT);
					ports = findPorts(dev, AbstractSchemePortDirectionType._OUT);
				} else
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
			} else if (pe.getKind() == Kind.SCHEME_CABLE_LINK)
			{
//				SchemeCableLink link = (SchemeCableLink)pe.abstractSchemeElement();
				SchemeCablePort startPort = (SchemeCablePort)pe.getEndAbstractSchemePort();
				if (!SchemeUtils.isSchemeElementContainsPort(se, startPort)) //нет общих портов
					return null;
				// searching for ports with opposite direction
				List ports;
				List cports;
				SchemeDevice dev = startPort.getParentSchemeDevice();
				if (startPort.getDirectionType().equals(AbstractSchemePortDirectionType._IN))
				{
					cports = findCablePorts(dev, AbstractSchemePortDirectionType._OUT);
					ports = findPorts(dev, AbstractSchemePortDirectionType._OUT);
				} else
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
			} else
				return null;
		} else //first element
		{
			int accessPorts = 0;
			SchemePort port = null;
			// must be at least one access port
			if (se.getScheme() != null)
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Начальным устройством не может быть схема", "Ошибка", JOptionPane.OK_OPTION);
				return null;
			}

			for (Iterator it = se.getSchemePorts().iterator(); it.hasNext();)
			{
				SchemePort p = (SchemePort)it.next();
				final MeasurementPort measurementPort = p.getMeasurementPort();
				if (measurementPort != null && measurementPort.getType() != null) {
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
				newPE = PathElement.createInstance(creatorId, path, null, null);
			} 
			catch (CreateObjectException e) {
				Log.errorMessage("Can't create PathElement object " + e.getMessage());
				return null;
			}
			newPE.setAbstractSchemeElement(se);
			newPE.setParentScheme(se.getParentScheme());
			if (accessPorts == 1)
				newPE.setEndAbstractSchemePort(port);
		}
		return newPE;
	}

	public PathElement addLink(SchemePath path, SchemeLink link)
	{
		Object last = path.getPathElements().last();
		if (last != null)
		{
			PathElement pe = (PathElement)last;
			if (pe.getKind() == Kind.SCHEME_ELEMENT)
			{
				SchemeElement se = (SchemeElement)pe.getAbstractSchemeElement();

				//если у предыдущего эл-та проставлен endPortId, ищем по нему
				if (pe.getEndAbstractSchemePort() != null)
				{
					for (Iterator it = se.getSchemePorts().iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.equals(pe.getEndAbstractSchemePort()))
							return addLink(path, port, link);
					}
				} else //в противном случае ищем по общему порту предыдущего эл-та и линка
				{
					for (Iterator it = se.getSchemePorts().iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.equals(link.getSourceSchemePort()) ||
								port.equals(link.getTargetSchemePort()))
						{
							pe.setEndAbstractSchemePort(port);
							return addLink(path, port, link);
						}
					}
				}
			}
		}
		return null;
	}

	private PathElement addLink(SchemePath path, SchemePort port, SchemeLink link) {
		PathElement newPE;
		try {
			newPE = PathElement.createInstance(creatorId, path, link);
		} catch (CreateObjectException e) {
			Log.errorMessage("Can't create PathElement object " + e.getMessage());
			return null;
		}

		if (port.equals(link.getSourceSchemePort())) {
			newPE.setStartAbstractSchemePort(link.getSourceSchemePort());
			newPE.setEndAbstractSchemePort(link.getTargetSchemePort());
		} 
		else if (port.equals(link.getTargetSchemePort())) {
			newPE.setStartAbstractSchemePort(link.getTargetSchemePort());
			newPE.setEndAbstractSchemePort(link.getSourceSchemePort());
		} 
		else
			// нет общих портов
			return null;

		return newPE;
	}

	public PathElement addCableLink(SchemePath path, SchemeCableLink link)
	{
		Object last = path.getPathElements().last();
		if (last != null)
		{
			PathElement pe = (PathElement)last;
			if (pe.getKind() == Kind.SCHEME_ELEMENT)
			{
				SchemeElement se = (SchemeElement)pe.getAbstractSchemeElement();
				//если у предыдущего эл-та проставлен endPortId, ищем по нему
				if (pe.getEndAbstractSchemePort() != null)
				{
					for (Iterator it = se.getSchemeCablePorts().iterator(); it.hasNext();)
					{
						SchemeCablePort port = (SchemeCablePort)it.next();
						if (port.equals(pe.getEndAbstractSchemePort()))
						{
							if (pe.getStartAbstractSchemePort() instanceof SchemePort)
							{
								SchemePort startPort = (SchemePort)pe.getStartAbstractSchemePort();
								return addCableLink(path, port, link, startPort.getSchemeCableThread());
							}
						}
					}
				} else //в противном случае ищем по общему порту предыдущего эл-та и линка
				{
					for (Iterator it = se.getSchemeCablePorts().iterator(); it.hasNext();)
					{
						SchemeCablePort port = (SchemeCablePort)it.next();
						if (port.equals(link.getSourceSchemeCablePort()) ||
								port.equals(link.getTargetSchemeCablePort()))
						{
							pe.setEndAbstractSchemePort(port);
							if (pe.getStartAbstractSchemePort() instanceof SchemePort)
							{
								SchemePort startPort = (SchemePort)pe.getStartAbstractSchemePort();
								return addCableLink(path, port, link, startPort.getSchemeCableThread());
							}
						}
					}
				}
			}
		}
		return null;
	}

	private PathElement addCableLink(SchemePath path, SchemeCablePort port, SchemeCableLink link, SchemeCableThread thread)
	{
		if (thread == null)
		{
			PathElement pe = (PathElement)path.getPathElements().last();
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							"Пожалуйста, проверьте коммутацию в элементе " + pe.getName(),
							"Ошибка", JOptionPane.OK_OPTION);
			return null;
		}

		PathElement newPE;
		try {
			newPE = PathElement.createInstance(creatorId, path, thread);
		} 
		catch (CreateObjectException e) {
			Log.errorMessage("Can't create PathElement object " + e.getMessage());
			return null;
		}

		if (port.equals(link.getSourceSchemeCablePort()))
		{
			newPE.setStartAbstractSchemePort(link.getSourceSchemeCablePort());
			newPE.setEndAbstractSchemePort(link.getTargetSchemeCablePort());
		} else if (port.equals(link.getTargetSchemeCablePort()))
		{
			newPE.setStartAbstractSchemePort(link.getTargetSchemeCablePort());
			newPE.setEndAbstractSchemePort(link.getSourceSchemeCablePort());
		} else //нет общих портов
			return null;

		return newPE;
	}

	private SchemeCablePort getCablePortByThread(List cableports, SchemeCableThread thread)
	{
		for (Iterator it = cableports.iterator(); it.hasNext(); )
		{
			SchemeCablePort port = (SchemeCablePort)it.next();
			if (port.getSchemeCableLink() != null)
			{
				if (port.getSchemeCableLink().getSchemeCableThreads().contains(thread))
					return port;
			}
		}
		return null;
	}


	private List findPorts(SchemeDevice dev, AbstractSchemePortDirectionType direction)
	{
		List ports = new ArrayList();
		for (Iterator it = dev.getSchemePorts().iterator(); it.hasNext();)
		{
			SchemePort p = (SchemePort)it.next();
			if (p.getDirectionType().equals(direction))
				ports.add(p);
		}
		return ports;
	}

	private List findCablePorts(SchemeDevice dev, AbstractSchemePortDirectionType direction)
	{
		List ports = new ArrayList();
		for (Iterator it = dev.getSchemePorts().iterator(); it.hasNext();)
		{
			SchemeCablePort p = (SchemeCablePort)it.next();
			if (p.getDirectionType().equals(direction))
				ports.add(p);
		}
		return ports;
	}
}
