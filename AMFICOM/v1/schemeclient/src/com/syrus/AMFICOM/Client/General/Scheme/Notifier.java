package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.ArrayList;

import com.syrus.AMFICOM.Client.General.Event.CatalogNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.SchemeNavigateEvent;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISM.AccessPort;
import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.Network.CableLink;
import com.syrus.AMFICOM.Client.Resource.Network.CablePort;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.Network.Link;
import com.syrus.AMFICOM.Client.Resource.Network.Port;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCablePort;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeDevice;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePort;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class Notifier
{
	public static boolean DEBUG = false;

	public static void selectionNotify(Dispatcher dispatcher, Object[] cells, boolean isEditable, boolean is_debug)
	{
		DEBUG = is_debug;
		selectionNotify(dispatcher, cells, isEditable);
	}

	public static void selectionNotify(Dispatcher dispatcher, ObjectResource[] cells, boolean isEditable)
	{
		if (cells.length == 0)
		{
			dispatcher.notify(new SchemeNavigateEvent(new Object[0],
						SchemeNavigateEvent.SCHEME_ALL_DESELECTED_EVENT, isEditable));
			return;
		}

		if (cells[0] instanceof SchemeElement)
			dispatcher.notify(new SchemeNavigateEvent((SchemeElement[])cells,
					SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof ProtoElement)
			dispatcher.notify(new SchemeNavigateEvent((ProtoElement[])cells,
					SchemeNavigateEvent.SCHEME_PROTO_ELEMENT_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof Scheme)
			dispatcher.notify(new SchemeNavigateEvent((Scheme[])cells,
					SchemeNavigateEvent.SCHEME_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof SchemePath)
			dispatcher.notify(new SchemeNavigateEvent((SchemePath[])cells,
					SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof SchemeLink)
			dispatcher.notify(new SchemeNavigateEvent((SchemeLink[])cells,
					SchemeNavigateEvent.SCHEME_LINK_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof SchemeCableLink)
			dispatcher.notify(new SchemeNavigateEvent((SchemeCableLink[])cells,
					SchemeNavigateEvent.SCHEME_CABLE_LINK_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof SchemePort)
			dispatcher.notify(new SchemeNavigateEvent((SchemePort[])cells,
					SchemeNavigateEvent.SCHEME_PORT_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof SchemeCablePort)
			dispatcher.notify(new SchemeNavigateEvent((SchemeCablePort[])cells,
					SchemeNavigateEvent.SCHEME_CABLE_PORT_SELECTED_EVENT, isEditable));

		if (cells[0] instanceof Equipment)
			dispatcher.notify(new CatalogNavigateEvent(cells,
					CatalogNavigateEvent.CATALOG_EQUIPMENT_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof TransmissionPath)
			dispatcher.notify(new CatalogNavigateEvent(cells,
					CatalogNavigateEvent.CATALOG_PATH_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof Link)
			dispatcher.notify(new CatalogNavigateEvent(cells,
					CatalogNavigateEvent.CATALOG_LINK_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof CableLink)
			dispatcher.notify(new CatalogNavigateEvent(cells,
					CatalogNavigateEvent.CATALOG_CABLE_LINK_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof Port)
			dispatcher.notify(new CatalogNavigateEvent(cells,
					CatalogNavigateEvent.CATALOG_PORT_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof CablePort)
			dispatcher.notify(new CatalogNavigateEvent(cells,
					CatalogNavigateEvent.CATALOG_CABLE_PORT_SELECTED_EVENT, isEditable));
		if (cells[0] instanceof AccessPort)
			dispatcher.notify(new CatalogNavigateEvent(cells,
					CatalogNavigateEvent.CATALOG_ACCESS_PORT_SELECTED_EVENT, isEditable));
	}

	public static void selectionNotify(Dispatcher dispatcher, Object[] cells, boolean isEditable)
	{
		ArrayList elements = new ArrayList();
		ArrayList devices = new ArrayList();
		ArrayList protos = new ArrayList();
		ArrayList scheme = new ArrayList();
		ArrayList scheme_paths = new ArrayList();
		ArrayList scheme_links = new ArrayList();
		ArrayList scheme_clinks = new ArrayList();
		ArrayList scheme_ports = new ArrayList();
		ArrayList scheme_cports = new ArrayList();

		ArrayList equipment = new ArrayList();
		ArrayList paths = new ArrayList();
		ArrayList links = new ArrayList();
		ArrayList clinks = new ArrayList();
		ArrayList ports = new ArrayList();
		ArrayList cports = new ArrayList();
		ArrayList aports = new ArrayList();

		try
		{
			if (cells.length == 1)
			{
				Object obj = cells[0];
				isEditable = isEditable && !GraphActions.hasGroupedParent(obj);

				if (obj instanceof DeviceGroup)
				{
					DeviceGroup dev = (DeviceGroup)obj;
					if (!dev.getSchemeElementId().equals(""))
					{
						SchemeElement el = dev.getSchemeElement();
						elements.add(el);
						if (!el.equipment_id.equals(""))
						{
							Equipment eq = (Equipment)Pool.get(Equipment.typ, el.equipment_id);
							if (eq != null)
								equipment.add(eq);
							else
								equipment.add(Pool.get(KIS.typ, el.equipment_id));
						}
						else if (!el.scheme_id.equals(""))
						{
							Scheme sc = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
							scheme.add(sc);
						}
					}
					else if (!dev.getProtoElementId().equals(""))
						protos.add(dev.getProtoElement());
					else if (!dev.getSchemeId().equals(""))
					{
						scheme.add(Pool.get(Scheme.typ, dev.getSchemeId()));
					}
				}
				else if (cells[0] instanceof DeviceCell)
				{
					SchemeDevice dev = ((DeviceCell)cells[0]).getSchemeDevice();
					if (dev != null)
						devices.add(dev);
				}
//         obj_res.add(((DeviceCell)cells[i]).getSchemeDevice());
				else if (obj instanceof DefaultLink)
				{
					SchemeLink link = ((DefaultLink)obj).getSchemeLink();
					scheme_links.add(link);
					if (!link.link_id.equals(""))
						links.add(Pool.get(Link.typ, link.link_id));
				}
				else if (obj instanceof DefaultCableLink)
				{
					SchemeCableLink link = ((DefaultCableLink)obj).getSchemeCableLink();
					scheme_clinks.add(link);
					if (!link.cable_link_id.equals(""))
						clinks.add(Pool.get(CableLink.typ, link.cable_link_id));
				}
				else if (obj instanceof PortCell)
				{
					SchemePort port = ((PortCell)obj).getSchemePort();
					scheme_ports.add(port);
					if (!port.port_id.equals(""))
						ports.add(Pool.get(Port.typ, port.port_id));
					if (!port.access_port_id.equals(""))
						aports.add(Pool.get(AccessPort.typ, port.access_port_id));
				}
				else if (obj instanceof CablePortCell)
				{
					//if (((CablePortCell)obj).getSchemeCablePortId().equals("schcprt1012"))
				//				((CablePortCell)obj).setSchemeCablePortId("schcprt1005");
					SchemeCablePort port = ((CablePortCell)obj).getSchemeCablePort();
					scheme_cports.add(port);
					if (!port.cable_port_id.equals(""))
						cports.add(Pool.get(CablePort.typ, port.cable_port_id));
				}
				else if (obj instanceof DeviceCell)
				{
					if (GraphActions.hasGroupedParent(obj))
						dispatcher.notify(new SchemeNavigateEvent(new Object[0],
								SchemeNavigateEvent.SCHEME_ALL_DESELECTED_EVENT, isEditable));
				}
				else
				{
					//dispatcher.notify(new SchemeNavigateEvent(new Object[0],
					//	SchemeNavigateEvent.SCHEME_ALL_DESELECTED_EVENT, isEditable));
					return;
				}
			}
			else if (cells.length > 1)
			{
				if (cells[0] instanceof DefaultLink)
				{
					int counter = 0;
					for (int i = 0; i < cells.length; i++)
						if (cells[i] instanceof DefaultLink)
							counter++;

					if (cells.length == counter)
					{
						for (int i = 0; i < cells.length; i++)
						{
							isEditable = isEditable && !GraphActions.hasGroupedParent(cells[i]);
							SchemeLink link = ((DefaultLink)cells[i]).getSchemeLink();
							scheme_links.add(link);
							if (!link.link_id.equals(""))
								links.add(Pool.get(Link.typ, link.link_id));
						}
					}


					/*
					DefaultLink link = (DefaultLink)cells[0];
					if (!link.getSchemePathId().equals(""))
					{
//					if (hasIdenticalPathId(cells, link.getSchemePathId()))
					{
						SchemePath path = link.getSchemePath();
						//if (path.links.size() == cells.length)
						{
							scheme_paths.add(path);
							if (!path.path_id.equals(""))
								paths.add(Pool.get(TransmissionPath.typ, path.path_id));
						}
					}
					}*/
				}
				else if (cells[0] instanceof DefaultCableLink)
				{
					if (cells[0] instanceof DefaultCableLink)
					{
						int counter = 0;
						for (int i = 0; i < cells.length; i++)
							if (cells[i] instanceof DefaultCableLink)
								counter++;

						if (cells.length == counter)
						{
							for (int i = 0; i < cells.length; i++)
							{
								isEditable = isEditable && !GraphActions.hasGroupedParent(cells[i]);
								SchemeCableLink link = ((DefaultCableLink)cells[i]).getSchemeCableLink();
								scheme_clinks.add(link);
								if (!link.cable_link_id.equals(""))
									clinks.add(Pool.get(CableLink.typ, link.cable_link_id));
							}
						}
					}
					/*
					DefaultCableLink link = (DefaultCableLink)cells[0];
					if (!link.getSchemePathId().equals(""))
					{
//					if (hasIdenticalPathId(cells, link.getSchemePathId()))
					{
						SchemePath path = link.getSchemePath();
						//if (path.links.size() == cells.length)
						{
							scheme_paths.add(path);
							if (!path.path_id.equals(""))
								paths.add(Pool.get(TransmissionPath.typ, path.path_id));
						}
					}
					}*/
				}
				else if (cells[0] instanceof PortCell)
				{
					int counter = 0;
					for (int i = 0; i < cells.length; i++)
						if (cells[i] instanceof PortCell)
							counter++;

					if (cells.length == counter)
					{
						for (int i = 0; i < cells.length; i++)
						{
							isEditable = isEditable && !GraphActions.hasGroupedParent(cells[i]);
							SchemePort port = ((PortCell)cells[i]).getSchemePort();
							scheme_ports.add(port);
							if (!port.port_id.equals(""))
								ports.add(Pool.get(Port.typ, port.port_id));
						}
					}
				}
				else if (cells[0] instanceof CablePortCell)
				{
					int counter = 0;
					for (int i = 0; i < cells.length; i++)
						if (cells[i] instanceof CablePortCell)
							counter++;

					if (cells.length == counter)
					{
						for (int i = 0; i < cells.length; i++)
						{
							isEditable = isEditable && !GraphActions.hasGroupedParent(cells[i]);
							SchemeCablePort port = ((CablePortCell)cells[i]).getSchemeCablePort();
							scheme_cports.add(port);
							if (!port.cable_port_id.equals(""))
								cports.add(Pool.get(CablePort.typ, port.cable_port_id));
						}
					}
				}
				else
				{
					//dispatcher.notify(new SchemeNavigateEvent(new Object[0],
					//	SchemeNavigateEvent.SCHEME_ALL_DESELECTED_EVENT, isEditable));
					return;
				}
			}
			else
			{
				dispatcher.notify(new SchemeNavigateEvent(new Object[0],
						SchemeNavigateEvent.SCHEME_ALL_DESELECTED_EVENT, isEditable));
				return;
			}

			if (elements.size() != 0)
				dispatcher.notify(new SchemeNavigateEvent(elements.toArray(new SchemeElement[elements.size()]),
						SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT, isEditable));
			if (protos.size() != 0)
				dispatcher.notify(new SchemeNavigateEvent(protos.toArray(new ProtoElement[protos.size()]),
						SchemeNavigateEvent.SCHEME_PROTO_ELEMENT_SELECTED_EVENT, isEditable));
			if (scheme.size() != 0)
				dispatcher.notify(new SchemeNavigateEvent(scheme.toArray(new Scheme[scheme.size()]),
						SchemeNavigateEvent.SCHEME_SELECTED_EVENT, isEditable));
			if (scheme_paths.size() != 0)
				dispatcher.notify(new SchemeNavigateEvent(scheme_paths.toArray(new SchemePath[scheme_paths.size()]),
						SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT, isEditable));
			if (scheme_links.size() != 0)
				dispatcher.notify(new SchemeNavigateEvent(scheme_links.toArray(new SchemeLink[scheme_links.size()]),
						SchemeNavigateEvent.SCHEME_LINK_SELECTED_EVENT, isEditable));
			if (scheme_clinks.size() != 0)
				dispatcher.notify(new SchemeNavigateEvent(scheme_clinks.toArray(new SchemeCableLink[scheme_clinks.size()]),
						SchemeNavigateEvent.SCHEME_CABLE_LINK_SELECTED_EVENT, isEditable));
			if (scheme_ports.size() != 0)
				dispatcher.notify(new SchemeNavigateEvent(scheme_ports.toArray(new SchemePort[scheme_ports.size()]),
						SchemeNavigateEvent.SCHEME_PORT_SELECTED_EVENT, isEditable));
			if (scheme_cports.size() != 0)
				dispatcher.notify(new SchemeNavigateEvent(scheme_cports.toArray(new SchemeCablePort[scheme_cports.size()]),
						SchemeNavigateEvent.SCHEME_CABLE_PORT_SELECTED_EVENT, isEditable));

			if (equipment.size() != 0)
				dispatcher.notify(new CatalogNavigateEvent(equipment.toArray(new Equipment[equipment.size()]),
						CatalogNavigateEvent.CATALOG_EQUIPMENT_SELECTED_EVENT, isEditable));
			if (paths.size() != 0)
				dispatcher.notify(new CatalogNavigateEvent(paths.toArray(new TransmissionPath[paths.size()]),
						CatalogNavigateEvent.CATALOG_PATH_SELECTED_EVENT, isEditable));
			if (links.size() != 0)
				dispatcher.notify(new CatalogNavigateEvent(links.toArray(new Link[links.size()]),
						CatalogNavigateEvent.CATALOG_LINK_SELECTED_EVENT, isEditable));
			if (clinks.size() != 0)
				dispatcher.notify(new CatalogNavigateEvent(clinks.toArray(new CableLink[clinks.size()]),
						CatalogNavigateEvent.CATALOG_CABLE_LINK_SELECTED_EVENT, isEditable));
			if (ports.size() != 0)
				dispatcher.notify(new CatalogNavigateEvent(ports.toArray(new Port[ports.size()]),
						CatalogNavigateEvent.CATALOG_PORT_SELECTED_EVENT, isEditable));
			if (cports.size() != 0)
				dispatcher.notify(new CatalogNavigateEvent(cports.toArray(new CablePort[cports.size()]),
						CatalogNavigateEvent.CATALOG_CABLE_PORT_SELECTED_EVENT, isEditable));
			if (aports.size() != 0)
				dispatcher.notify(new CatalogNavigateEvent(aports.toArray(new AccessPort[aports.size()]),
						CatalogNavigateEvent.CATALOG_ACCESS_PORT_SELECTED_EVENT, isEditable));

			if (DEBUG)
				printDebug(
						elements,
						devices,
						protos,
						scheme,
						scheme_paths,
						scheme_links,
						scheme_clinks,
						scheme_ports,
						scheme_cports,

						equipment,
						paths,
						links,
						clinks,
						ports,
						cports,
						aports
						);
		}
		catch (Exception e)
		{
			System.err.println("Exception trying select object " + e.getMessage());
			e.printStackTrace();
		}
	}

	static void printDebug(
			ArrayList elements,
			ArrayList devices,
			ArrayList protos,
			ArrayList scheme,
			ArrayList scheme_paths,
			ArrayList scheme_links,
			ArrayList scheme_clinks,
			ArrayList scheme_ports,
			ArrayList scheme_cports,

			ArrayList equipment,
			ArrayList paths,
			ArrayList links,
			ArrayList clinks,
			ArrayList ports,
			ArrayList cports,
			ArrayList aports
			)
	{
		if (elements.size() != 0)
		{
			SchemeElement[] o = (SchemeElement[])elements.toArray(new SchemeElement[elements.size()]);
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("SchemeElement: " + o[i].getId());
				System.out.println("\t proto_id = \"" + o[i].proto_element_id + "\"");
				System.out.println("\t scheme_id = \"" + o[i].scheme_id + "\"");
				System.out.println("\t equipment_id = \"" + o[i].equipment_id + "\"");
				System.out.print("\t device_id =");
				for (int j = 0; j < o[i].devices.size(); j++)
					System.out.print(" \"" + ((SchemeDevice)o[i].devices.get(j)).getId() + "\"");
				System.out.println();
			}
		}
		if (devices.size() != 0)
		{
			SchemeDevice[] o = (SchemeDevice[])devices.toArray(new SchemeDevice[devices.size()]);
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("SchemeDevice: " + o[i].getId());
				System.out.println();
			}
		}
		if (protos.size() != 0)
		{
			ProtoElement[] o = (ProtoElement[])protos.toArray(new ProtoElement[protos.size()]);
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("ProtoElement: " + o[i].getId());
				System.out.println("\t equipment_type_id = \"" + o[i].equipment_type_id + "\"");
				System.out.print("\t device_id =");
				for (int j = 0; j < o[i].devices.size(); j++)
					System.out.print(" \"" + ((SchemeDevice)o[i].devices.get(j)).getId() + "\"");
				System.out.println();
			}
		}
		if (scheme.size() != 0)
		{
			Scheme[] o = (Scheme[])scheme.toArray(new Scheme[scheme.size()]);
			for (int i = 0; i < o.length; i++)
			{
				if (o[i] != null)
					System.out.println("Scheme: " + o[i].getId());
			}
		}
		if (scheme_paths.size() != 0)
		{
			SchemePath[] o = (SchemePath[])scheme_paths.toArray(new SchemePath[scheme_paths.size()]);
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("SchemePath: " + o[i].getId());
				System.out.println("\t start_device_id = \"" + o[i].start_device_id + "\"");
				System.out.println("\t end_device_id = \"" + o[i].end_device_id + "\"");
				System.out.println("\t path_id = \"" + o[i].path_id + "\"");
			}
		}
		if (scheme_links.size() != 0)
		{
			SchemeLink[] o = (SchemeLink[])scheme_links.toArray(new SchemeLink[scheme_links.size()]);
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("SchemeLink: " + o[i].getId());
				System.out.println("\t source_port_id = \"" + o[i].source_port_id + "\"");
				System.out.println("\t target_port_id = \"" + o[i].target_port_id + "\"");
				System.out.println("\t link_id = \"" + o[i].link_id + "\"");
				System.out.println("\t link_type_id = \"" + o[i].link_type_id + "\"");
				System.out.println("\t optical_length = \"" + o[i].optical_length + "\"");
				System.out.println("\t physical_length = \"" + o[i].physical_length + "\"");
			}
		}
		if (scheme_clinks.size() != 0)
		{
			SchemeCableLink[] o = (SchemeCableLink[])scheme_clinks.toArray(new SchemeCableLink[scheme_clinks.size()]);
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("SchemeCableLink: " + o[i].getId());
				System.out.println("\t source_port_id = \"" + o[i].source_port_id + "\"");
				System.out.println("\t target_port_id = \"" + o[i].target_port_id + "\"");
				System.out.println("\t cable_link_id = \"" + o[i].cable_link_id + "\"");
				System.out.println("\t cable_link_type_id = \"" + o[i].cable_link_type_id + "\"");
				System.out.println("\t optical_length = \"" + o[i].optical_length + "\"");
				System.out.println("\t physical_length = \"" + o[i].physical_length + "\"");
			}
		}
		if (scheme_ports.size() != 0)
		{
			SchemePort[] o = (SchemePort[])scheme_ports.toArray(new SchemePort[scheme_ports.size()]);
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("SchemePort: " + o[i].getId());
				System.out.println("\t access_port_id = \"" + o[i].access_port_id + "\"");
				System.out.println("\t access_port_type_id = \"" + o[i].access_port_type_id + "\"");
				System.out.println("\t device_id = \"" + o[i].device_id + "\"");
				System.out.println("\t direction_type = \"" + o[i].direction_type + "\"");
				System.out.println("\t link_id = \"" + o[i].link_id + "\"");
				System.out.println("\t port_id = \"" + o[i].port_id + "\"");
				System.out.println("\t port_type_id = \"" + o[i].port_type_id + "\"");
			}
		}
		if (scheme_cports.size() != 0)
		{
			SchemeCablePort[] o = (SchemeCablePort[])scheme_cports.toArray(new SchemeCablePort[scheme_cports.size()]);
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("SchemeCablePort: " + o[i].getId());
				System.out.println("\t access_port_id = \"" + o[i].access_port_id + "\"");
				System.out.println("\t access_port_type_id = \"" + o[i].access_port_type_id + "\"");
				System.out.println("\t device_id = \"" + o[i].device_id + "\"");
				System.out.println("\t direction_type = \"" + o[i].direction_type + "\"");
				System.out.println("\t cable_link_id = \"" + o[i].cable_link_id + "\"");
				System.out.println("\t cable_port_id = \"" + o[i].cable_port_id + "\"");
				System.out.println("\t cable_port_type_id = \"" + o[i].cable_port_type_id + "\"");
			}
		}
	}
/*
 static boolean hasIdenticalPathId(Object[] cells, String id)
 {
	for (int i = 0; i < cells.length; i++)
	{
	 if (cells[i] instanceof DefaultLink)
	 {
		isEditable = isEditable && !GraphActions.hasGroupedParent(cells[i]);
		if (!((DefaultLink)cells[i]).getSchemePathId().equals(id))
		 return false;
	 }
	 else if (cells[i] instanceof DefaultCableLink)
	 {
		isEditable = isEditable && !GraphActions.hasGroupedParent(cells[i]);
		if (!((DefaultCableLink)cells[i]).getSchemePathId().equals(id))
		 return false;
	 }
	 else return false;
	}
	return true;
 }*/
}

