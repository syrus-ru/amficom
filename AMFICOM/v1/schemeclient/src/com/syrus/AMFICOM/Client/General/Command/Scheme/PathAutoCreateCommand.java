package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.*;
import java.util.*;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class PathAutoCreateCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeGraph graph;
	SchemePanel panel;

	public PathAutoCreateCommand(ApplicationContext aContext, SchemePanel panel)
	{
		this.aContext = aContext;
		this.panel = panel;
		this.graph = panel.getGraph();
	}

	public Object clone()
	{
		return new PathAutoCreateCommand(aContext, panel);
	}

	public void execute()
	{
		Scheme scheme = panel.scheme;
		SchemePath path = graph.currentPath;
		if (path.start_device_id.length() == 0 ||
				path.end_device_id.length() == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
																		"Ошибка",
																		"Не введено начальное и/или конечное устройство",
																		JOptionPane.OK_OPTION);
			return;
		}

		SchemeElement startSE = scheme.getTopLevelElement(path.start_device_id);
		SchemeElement endSE = scheme.getTopLevelElement(path.end_device_id);
		if (startSE == null || endSE == null)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
																		"Ошибка",
																		"Начальное или конечное устройство не найдено на схеме",
																		JOptionPane.OK_OPTION);
			return;
		}

		Scheme startScheme = scheme;
		if (startSE.scheme_id.length() != 0)
			startScheme = (Scheme)Pool.get(Scheme.typ, startSE.scheme_id);

		ArrayList links = new ArrayList();
		SchemeDevice endDevice;
		String id = "";
		while (!id.equals(endSE.getId()))
		{

		}
	}
}

class PathBuilder
{
	private PathBuilder()
			{}

	public static SchemePath addLink(Scheme scheme, SchemePath path, SchemeLink link)
	{
		PathElement newPE = new PathElement();
		newPE.is_cable = false;
		newPE.link_id = link.getId();
		newPE.n = path.links.size();

		PathElement lastPE = (PathElement)path.links.lastElement();
		SchemeElement lastSE;
		if (lastPE.is_cable)
			lastSE = scheme.getSchemeElementByCablePort(lastPE.end_port_id);
		else
			lastSE = scheme.getSchemeElementByPort(lastPE.end_port_id);

		for (Iterator it = lastSE.devices.iterator(); it.hasNext();)
		{
			SchemeDevice dev = (SchemeDevice)it.next();
			for (Iterator port_it = dev.ports.iterator(); port_it.hasNext();)
			{
				SchemePort port = (SchemePort)port_it.next();
				if (port.getId().equals(link.source_port_id))
				{
					newPE.start_port_id = link.source_port_id;
					newPE.end_port_id = link.target_port_id;
					break;
				}
				else if (port.getId().equals(link.target_port_id))
				{
					newPE.start_port_id = link.target_port_id;
					newPE.end_port_id = link.source_port_id;
					break;
				}
			}
		}
		path.links.add(newPE);
		return path;
	}

	public static SchemePath addCableLink(Scheme scheme, SchemePath path, SchemeCableLink link)
	{
		PathElement newPE = new PathElement();
		newPE.is_cable = true;
		newPE.link_id = link.getId();
		newPE.n = path.links.size();

		PathElement lastPE = (PathElement)path.links.lastElement();
		SchemeElement lastSE;
		if (lastPE.is_cable)
			lastSE = scheme.getSchemeElementByCablePort(lastPE.end_port_id);
		else
			lastSE = scheme.getSchemeElementByPort(lastPE.end_port_id);

		for (Iterator it = lastSE.devices.iterator(); it.hasNext();)
		{
			SchemeDevice dev = (SchemeDevice)it.next();
			for (Iterator port_it = dev.cableports.iterator(); port_it.hasNext();)
			{
				SchemeCablePort port = (SchemeCablePort)port_it.next();
				if (port.getId().equals(link.source_port_id))
				{
					newPE.start_port_id = link.source_port_id;
					newPE.end_port_id = link.target_port_id;
					break;
				}
				else if (port.getId().equals(link.target_port_id))
				{
					newPE.start_port_id = link.target_port_id;
					newPE.end_port_id = link.source_port_id;
					break;
				}
			}
		}
		path.links.add(newPE);
		return path;
	}

	public static SchemePath explore(Scheme scheme, SchemePath path)
	{
		if (path.start_device_id.length() == 0)
			return path;

		SchemeElement schel;
		if (path.links.isEmpty())
			schel = (SchemeElement)Pool.get(SchemeElement.typ, path.start_device_id);
		else
		{
			PathElement pe = (PathElement)path.links.lastElement();
			Scheme sch = (Scheme)Pool.get(Scheme.typ, pe.scheme_id);
			if (pe.is_cable)
				schel = sch.getSchemeElementByCablePort(pe.end_port_id);
			else
				schel = sch.getSchemeElementByPort(pe.end_port_id);
		}
		if (schel.getId().equals(path.end_device_id))
			return path;

		PathElement newPE = new PathElement();
		int links_found = 0;
		for (Iterator it = schel.devices.iterator(); it.hasNext();)
		{
			SchemeDevice dev = (SchemeDevice)it.next();
			for (Iterator port_it = dev.ports.iterator(); port_it.hasNext();)
			{
				SchemePort port = (SchemePort)port_it.next();
				if (port.direction_type.equals("out"))
				{
					for (Enumeration enum = scheme.getAllLinks(); enum.hasMoreElements();)
					{
						SchemeLink link = (SchemeLink)enum.nextElement();
						if (link.source_port_id.equals(port.getId()))
						{
							newPE.start_port_id = link.source_port_id;
							newPE.end_port_id = link.target_port_id;
							newPE.link_id = link.getId();
							links_found++;
						}
						else if (link.target_port_id.equals(port.getId()))
						{
							newPE.start_port_id = link.target_port_id;
							newPE.end_port_id = link.source_port_id;
							newPE.link_id = link.getId();
							links_found++;
						}
					}
					for (Enumeration enum = scheme.getAllCableLinks(); enum.hasMoreElements();)
					{
						SchemeCableLink link = (SchemeCableLink)enum.nextElement();
						if (link.source_port_id.equals(port.getId()))
						{
							newPE.start_port_id = link.source_port_id;
							newPE.end_port_id = link.target_port_id;
							newPE.link_id = link.getId();
							links_found++;
						}
						else if (link.target_port_id.equals(port.getId()))
						{
							newPE.start_port_id = link.target_port_id;
							newPE.end_port_id = link.source_port_id;
							newPE.link_id = link.getId();
							links_found++;
						}
					}
				}
			}
		}
		if (links_found == 1)
		{
			newPE.n = path.links.size();
			path.links.add(newPE);
			path = explore(scheme, path);
		}
		return path;
	}
}
