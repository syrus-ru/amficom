package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.*;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class PathBuilder
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

	static ArrayList findPorts(SchemeElement se, String direction)
	{
		ArrayList ports = new ArrayList();
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

	static ArrayList findCablePorts(SchemeElement se, String direction)
	{
		ArrayList ports = new ArrayList();
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
/*
	public static boolean explore2(Scheme scheme, SchemePath path, String end_device_id)
	{
		if (path.start_device_id.length() == 0)
			return false;

		if (path.links.isEmpty())
		{
			// Create the first PE
			PathElement pe = new PathElement();
			pe.setType(PathElement.SCHEME_ELEMENT);
			pe.scheme_element_id = path.start_device_id;
			pe.n = 1;
			pe.scheme_id = scheme.getSchemeBySchemeElement(path.start_device_id).getId();

			// searching for the only output port
			ArrayList outPorts = findPorts((SchemeElement)Pool.get(SchemeElement.typ, path.start_device_id), "out");
			ArrayList outCablePorts = findCablePorts((SchemeElement)Pool.get(SchemeElement.typ, path.start_device_id), "out");
			// if one connect (cable)link to it
			if (outPorts.size() + outCablePorts.size() == 1)
			{
				if (!outPorts.isEmpty())
					pe.end_port_id = ((SchemePort)outPorts.get(0)).getId();
				else
					pe.end_port_id = ((SchemeCablePort)outCablePorts.get(0)).getId();
				// continue making path
				explore2(scheme, path, end_device_id);
			}
			else // if more cannot continue without operator point
				return false;
		}
		else // already has pathelements
		{
			PathElement last_pe = (PathElement)path.links.lastElement();
			Scheme sch = (Scheme)Pool.get(Scheme.typ, last_pe.scheme_id);

			if (last_pe.getType() == PathElement.SCHEME_ELEMENT)
			{
				//add Link or Cable
			}
			else
			{
				// add SE
				SchemeElement se;
				if (last_pe.getType() == PathElement.CABLE_LINK)
					se = sch.getSchemeElementByCablePort(last_pe.end_port_id);
				else
					se = sch.getSchemeElementByPort(last_pe.end_port_id);

				if (se.getId().equals(end_device_id))
					return true;

				PathElement pe = new PathElement();
				pe.start_port_id = last_pe.end_port_id;
				pe.setType(PathElement.SCHEME_ELEMENT);
				pe.scheme_element_id = se.getId();
				pe.n = last_pe.n + 1;
				pe.scheme_id = scheme.getSchemeBySchemeElement(se.getId()).getId();
			}
		}
	}
*/
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
					for (Iterator it2 = scheme.getAllLinks().iterator(); it2.hasNext();)
					{
						SchemeLink link = (SchemeLink)it2.next();
						if (link.source_port_id.equals(port.getId()))
						{
							newPE.start_port_id = link.source_port_id;
							newPE.end_port_id = link.target_port_id;
							newPE.link_id = link.getId();
							newPE.scheme_id = scheme.getSchemeByLink(link.getId()).getId();
							newPE.is_cable = false;
							links_found++;
						}
						else if (link.target_port_id.equals(port.getId()))
						{
							newPE.start_port_id = link.target_port_id;
							newPE.end_port_id = link.source_port_id;
							newPE.link_id = link.getId();
							newPE.scheme_id = scheme.getSchemeByLink(link.getId()).getId();
							newPE.is_cable = false;
							links_found++;
						}
					}
				}
			}
			for (Iterator port_it = dev.cableports.iterator(); port_it.hasNext();)
			{
				SchemeCablePort port = (SchemeCablePort)port_it.next();
				if (port.direction_type.equals("out"))
				{
					for (Iterator it2 = scheme.getAllCableLinks().iterator(); it2.hasNext();)
					{
						SchemeCableLink link = (SchemeCableLink)it2.next();
						if (link.source_port_id.equals(port.getId()))
						{
							newPE.start_port_id = link.source_port_id;
							newPE.end_port_id = link.target_port_id;
							newPE.link_id = link.getId();
							newPE.scheme_id = scheme.getSchemeByCableLink(link.getId()).getId();
							newPE.is_cable = true;
							links_found++;
						}
						else if (link.target_port_id.equals(port.getId()))
						{
							newPE.start_port_id = link.target_port_id;
							newPE.end_port_id = link.source_port_id;
							newPE.link_id = link.getId();
							newPE.scheme_id = scheme.getSchemeByCableLink(link.getId()).getId();
							newPE.is_cable = true;
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
