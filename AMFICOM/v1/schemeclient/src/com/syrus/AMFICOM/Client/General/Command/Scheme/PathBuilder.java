package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;
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
			if (pe.endAbstractSchemePort() == null)
			{
				if (state == PathBuilder.MULTIPLE_PORTS)
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), "����� ������� " +
							pe.getName() +
							" ���������� ���������� �������� ����.\n����������, ������� ��������� ������� ���� �������.",
							"������", JOptionPane.OK_OPTION);

				state = OK;
				return false;
			}

			if (pe.type() == Type.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
//				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.endAbstractSchemePort();
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
							((SchemePort)pe.startAbstractSchemePort()).getSchemeCableThread());
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
			if (pe.endAbstractSchemePort() == null)
				return false;

			if (pe.type() == Type.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
//				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.endAbstractSchemePort();
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
							((SchemePort)pe.startAbstractSchemePort()).getSchemeCableThread());
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
						"���������� ���� ������� ���������", "���������", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}

			if (pe.endAbstractSchemePort() == null)
			{
				if (state == PathBuilder.MULTIPLE_PORTS)
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							"����� ������� " + pe.getName() +
							" ���������� ���������� �������� ����.\n����������, ������� ��������� ������� ���� �������.",
							"������", JOptionPane.OK_OPTION);

				state = OK;
				return false;
			}


			if (pe.type() == Type.SCHEME_ELEMENT)
			{
				PathElement newPE = null;
//				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				AbstractSchemePort port = pe.endAbstractSchemePort();
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
							((SchemePort)pe.startAbstractSchemePort()).getSchemeCableThread());
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
				newPE = PathElement.createInstance(creatorId);
			} 
			catch (CreateObjectException e) {
				Log.errorMessage("Can't create PathElement object " + e.getMessage());
				return null;
			}
			newPE.type(Type.SCHEME_ELEMENT);
			newPE.abstractSchemeElement(se);
			newPE.scheme(se.getParentScheme());
			newPE.sequentialNumber(pe.sequentialNumber() + 1);
			newPE.startAbstractSchemePort(pe.endAbstractSchemePort());

			if (pe.type() == Type.SCHEME_LINK)
			{
//				SchemeLink link = (SchemeLink)pe.abstractSchemeElement();
				SchemePort startPort = (SchemePort)pe.endAbstractSchemePort();
				if (!SchemeUtils.isSchemeElementContainsPort(se, startPort)) //��� ����� ������
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
						newPE.endAbstractSchemePort((SchemeCablePort)cports.get(0));
					else //searching what thread start port routed with
					{
						SchemeCableThread thread = startPort.getSchemeCableThread();
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
//				SchemeCableLink link = (SchemeCableLink)pe.abstractSchemeElement();
				SchemeCablePort startPort = (SchemeCablePort)pe.endAbstractSchemePort();
				if (!SchemeUtils.isSchemeElementContainsPort(se, startPort)) //��� ����� ������
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
			if (se.getScheme() != null)
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "��������� ����������� �� ����� ���� �����", "������", JOptionPane.OK_OPTION);
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
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "� ���������� ���������� ������ ���� �������� ����\n� �������� ������ ���������� ������� ������������", "������", JOptionPane.OK_OPTION);
				return null;
			}
			try {
				newPE = PathElement.createInstance(creatorId);
			} 
			catch (CreateObjectException e) {
				Log.errorMessage("Can't create PathElement object " + e.getMessage());
				return null;
			}
			newPE.type(Type.SCHEME_ELEMENT);
			newPE.abstractSchemeElement(se);
			newPE.scheme(se.getParentScheme());
			newPE.sequentialNumber(1);
			if (accessPorts == 1)
				newPE.endAbstractSchemePort(port);
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
			if (pe.type() == Type.SCHEME_ELEMENT)
			{
				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();

				//���� � ����������� ��-�� ���������� endPortId, ���� �� ����
				if (pe.endAbstractSchemePort() != null)
				{
					for (Iterator it = SchemeUtils.getPorts(se).iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.equals(pe.endAbstractSchemePort()))
							return addLink(pes, port, link);
					}
				}
				else //� ��������� ������ ���� �� ������ ����� ����������� ��-�� � �����
				{
					for (Iterator it = SchemeUtils.getPorts(se).iterator(); it.hasNext();)
					{
						SchemePort port = (SchemePort)it.next();
						if (port.equals(link.getSourceSchemePort()) ||
								port.equals(link.getTargetSchemePort()))
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

	private PathElement addLink(List pes, SchemePort port, SchemeLink link)
	{
		int number = 0;
		ListIterator it = pes.listIterator(pes.size());
		if (it.hasPrevious())
			number = ((PathElement)it.previous()).sequentialNumber() + 1;

		PathElement newPE;
		try {
			newPE = PathElement.createInstance(creatorId);
		} 
		catch (CreateObjectException e) {
			Log.errorMessage("Can't create PathElement object " + e.getMessage());
			return null;
		}
		newPE.type(Type.SCHEME_LINK);
		newPE.abstractSchemeElement(link);
		newPE.scheme(link.getParentScheme());
		newPE.sequentialNumber(number);

		if (port.equals(link.getSourceSchemePort()))
		{
			newPE.startAbstractSchemePort(link.getSourceSchemePort());
			newPE.endAbstractSchemePort(link.getTargetSchemePort());
		}
		else if (port.equals(link.getTargetSchemePort()))
		{
			newPE.startAbstractSchemePort(link.getTargetSchemePort());
			newPE.endAbstractSchemePort(link.getSourceSchemePort());
		}
		else //��� ����� ������
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
			if (pe.type() == Type.SCHEME_ELEMENT)
			{
				SchemeElement se = (SchemeElement)pe.abstractSchemeElement();
				//���� � ����������� ��-�� ���������� endPortId, ���� �� ����
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
								return addCableLink(pes, port, link, startPort.getSchemeCableThread());
							}
						}
					}
				}
				else //� ��������� ������ ���� �� ������ ����� ����������� ��-�� � �����
				{
					for (Iterator it = SchemeUtils.getCablePorts(se).iterator(); it.hasNext();)
					{
						SchemeCablePort port = (SchemeCablePort)it.next();
						if (port.equals(link.getSourceSchemeCablePort()) ||
								port.equals(link.getTargetSchemeCablePort()))
						{
							pe.endAbstractSchemePort(port);
							if (pe.startAbstractSchemePort() instanceof SchemePort)
							{
								SchemePort startPort = (SchemePort)pe.startAbstractSchemePort();
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
							"����������, ��������� ���������� � �������� " + pe.getName(),
							"������", JOptionPane.OK_OPTION);
			return null;
		}

		int number = 0;
		ListIterator it = pes.listIterator(pes.size());
		if (it.hasPrevious())
			number = ((PathElement)it.previous()).sequentialNumber() + 1;

		PathElement newPE;
		try {
			newPE = PathElement.createInstance(creatorId);
		} 
		catch (CreateObjectException e) {
			Log.errorMessage("Can't create PathElement object " + e.getMessage());
			return null;
		}
		newPE.type(Type.SCHEME_CABLE_LINK);
		newPE.abstractSchemeElement(link);
		newPE.schemeCableThread(thread);
		newPE.scheme(link.getParentScheme());
		newPE.sequentialNumber(number);

		if (port.equals(link.getSourceSchemeCablePort()))
		{
			newPE.startAbstractSchemePort(link.getSourceSchemeCablePort());
			newPE.endAbstractSchemePort(link.getTargetSchemeCablePort());
		}
		else if (port.equals(link.getTargetSchemeCablePort()))
		{
			newPE.startAbstractSchemePort(link.getTargetSchemeCablePort());
			newPE.endAbstractSchemePort(link.getSourceSchemeCablePort());
		}
		else //��� ����� ������
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
				if (Arrays.asList(port.getSchemeCableLink().schemeCableThreads()).contains(thread))
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
