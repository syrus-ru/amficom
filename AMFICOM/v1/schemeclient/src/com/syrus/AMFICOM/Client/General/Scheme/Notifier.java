package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class Notifier
{
	public static boolean DEBUG = false;

	private static long selectedType = 0;

	private static Object[] selectedObjects;

//	private static final int aports = new ArrayList();

	public static void selectionNotify(Dispatcher dispatcher, ObjectResource[] cells, boolean isEditable)
	{
		if (cells.length == 0)
		{
			dispatcher.notify(new SchemeNavigateEvent(cells,
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
	}

	public static void selectionNotify(Dispatcher dispatcher, SchemeGraph graph, boolean isEditable)
	{
		DEBUG = graph.is_debug;
		Object[] cells = graph.getSelectionCells();

		if (cells.length == 0)
		{
			dispatcher.notify(new SchemeNavigateEvent(new Object[0],
					SchemeNavigateEvent.SCHEME_ALL_DESELECTED_EVENT, isEditable));
			return;
		}
		selectedType = 0;

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
						selectedObjects = new SchemeElement[1];
						selectedObjects[0] = dev.getSchemeElement();
						selectedType = SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT;
					}
					else if (!dev.getProtoElementId().equals(""))
					{
						selectedObjects = new ProtoElement[1];
						selectedObjects[0] = dev.getProtoElement();
						selectedType = SchemeNavigateEvent.SCHEME_PROTO_ELEMENT_SELECTED_EVENT;
					}
					else if (!dev.getSchemeId().equals(""))
					{
						selectedObjects = new Scheme[1];
						selectedObjects[0] = dev.getScheme();
						selectedType = SchemeNavigateEvent.SCHEME_SELECTED_EVENT;
					}
				}
				else if (obj instanceof DeviceCell)
				{
					SchemeDevice dev = ((DeviceCell)obj).getSchemeDevice();
					if (dev != null)
					{
						selectedObjects = new SchemeDevice[1];
						selectedObjects[0] = dev;
						selectedType = SchemeNavigateEvent.SCHEME_DEVICE_SELECTED_EVENT;
					}
				}
				else if (obj instanceof DefaultLink)
				{
					SchemeLink link = ((DefaultLink)obj).getSchemeLink();
					if (graph.mode.equals(Constants.LINK_MODE) || SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
					{
						selectedObjects = new SchemeLink[1];
						selectedObjects[0] = link;
						selectedType = SchemeNavigateEvent.SCHEME_LINK_SELECTED_EVENT;
					}
					else if (graph.mode.equals(Constants.PATH_MODE))
					{
						for (Iterator it = graph.getScheme().paths.iterator(); it.hasNext();)
						{
							SchemePath path = (SchemePath)it.next();
							if (path.isElementInPath(link.getId()))
							{
								selectedObjects = new SchemePath[1];
								selectedObjects[0] = path;
								selectedType = SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT;
							}
						}
					}
				}
				else if (obj instanceof DefaultCableLink)
				{
					SchemeCableLink link = ((DefaultCableLink)obj).getSchemeCableLink();
					if (graph.mode.equals(Constants.LINK_MODE) || SchemeGraph.path_creation_mode == Constants.CREATING_PATH_MODE)
					{
						selectedObjects = new SchemeCableLink[1];
						selectedObjects[0] = link;
						selectedType = SchemeNavigateEvent.SCHEME_CABLE_LINK_SELECTED_EVENT;
					}
					else if (graph.mode.equals(Constants.PATH_MODE))
					{
						for (Iterator it = graph.getScheme().paths.iterator(); it.hasNext();)
						{
							SchemePath path = (SchemePath)it.next();
							if (path.isElementInPath(link.getId()))
							{
								selectedObjects = new SchemePath[1];
								selectedObjects[0] = path;
								selectedType = SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT;
							}
						}
					}
				}
				else if (obj instanceof PortCell)
				{
					selectedObjects = new SchemePort[1];
					selectedObjects[0] = ((PortCell)obj).getSchemePort();
					selectedType = SchemeNavigateEvent.SCHEME_PORT_SELECTED_EVENT;
				}
				else if (obj instanceof CablePortCell)
				{
					selectedObjects = new SchemeCablePort[1];
					selectedObjects[0] = ((CablePortCell)obj).getSchemeCablePort();
					selectedType = SchemeNavigateEvent.SCHEME_CABLE_PORT_SELECTED_EVENT;
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
						selectedObjects = new SchemeLink[cells.length];
						selectedType = SchemeNavigateEvent.SCHEME_LINK_SELECTED_EVENT;
						for (int i = 0; i < cells.length; i++)
						{
							isEditable = isEditable && !GraphActions.hasGroupedParent(cells[i]);
							selectedObjects[i] = ((DefaultLink)cells[i]).getSchemeLink();
						}
					}
				}
				else if (cells[0] instanceof DefaultCableLink)
				{
					int counter = 0;
					for (int i = 0; i < cells.length; i++)
						if (cells[i] instanceof DefaultCableLink)
							counter++;

					if (cells.length == counter)
					{
						selectedObjects = new SchemeCableLink[cells.length];
						selectedType = SchemeNavigateEvent.SCHEME_CABLE_LINK_SELECTED_EVENT;
						for (int i = 0; i < cells.length; i++)
						{
							isEditable = isEditable && !GraphActions.hasGroupedParent(cells[i]);
							selectedObjects[i] = ((DefaultCableLink)cells[i]).getSchemeCableLink();
						}
					}
				}
				else if (cells[0] instanceof PortCell)
				{
					int counter = 0;
					for (int i = 0; i < cells.length; i++)
						if (cells[i] instanceof PortCell)
							counter++;

					if (cells.length == counter)
					{
						selectedObjects = new SchemePort[cells.length];
						selectedType = SchemeNavigateEvent.SCHEME_PORT_SELECTED_EVENT;
						for (int i = 0; i < cells.length; i++)
						{
							isEditable = isEditable && !GraphActions.hasGroupedParent(cells[i]);
							selectedObjects[i] = ((PortCell)cells[i]).getSchemePort();
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
						selectedObjects = new SchemeCablePort[cells.length];
						selectedType = SchemeNavigateEvent.SCHEME_CABLE_PORT_SELECTED_EVENT;
						for (int i = 0; i < cells.length; i++)
						{
							isEditable = isEditable && !GraphActions.hasGroupedParent(cells[i]);
							selectedObjects[i] = ((CablePortCell)cells[i]).getSchemeCablePort();
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
			dispatcher.notify(new SchemeNavigateEvent(selectedObjects, selectedType, isEditable));

			if (DEBUG)
				printDebug();
		}
		catch (Exception e)
		{
			System.err.println("Exception trying select object " + e.getMessage());
			e.printStackTrace();
		}
	}

	static void printDebug()
	{
		if (selectedType == SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT)
		{
			SchemeElement[] o = (SchemeElement[])selectedObjects;
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("SchemeElement: " + o[i].getId());
				System.out.println("\t proto_id = \"" + o[i].proto_element_id + "\"");
				System.out.println("\t internal_scheme_id = \"" + o[i].getInternalSchemeId() + "\"");
				System.out.println("\t scheme_id = \"" + o[i].getSchemeId() + "\"");
				System.out.println("\t equipment_id = \"" + o[i].equipment_id + "\"");
				System.out.print("\t device_id =");
				for (Iterator it = o[i].devices.iterator(); it.hasNext();)
					System.out.print(" \"" + ((SchemeDevice)it.next()).getId() + "\"");
				System.out.println();
			}
		}
		if (selectedType == SchemeNavigateEvent.SCHEME_DEVICE_SELECTED_EVENT)
		{
			SchemeDevice[] o = (SchemeDevice[])selectedObjects;
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("SchemeDevice: " + o[i].getId());
				System.out.println();
			}
		}
		if (selectedType == SchemeNavigateEvent.SCHEME_PROTO_ELEMENT_SELECTED_EVENT)
		{
			ProtoElement[] o = (ProtoElement[])selectedObjects;
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("ProtoElement: " + o[i].getId());
				System.out.println("\t equipment_type_id = \"" + o[i].equipment_type_id + "\"");
				System.out.print("\t device_id =");
				for (Iterator it = o[i].devices.iterator(); it.hasNext();)
					System.out.print(" \"" + ((SchemeDevice)it.next()).getId() + "\"");
				System.out.println();
			}
		}
		if (selectedType == SchemeNavigateEvent.SCHEME_SELECTED_EVENT)
		{
			Scheme[] o = (Scheme[])selectedObjects;
			for (int i = 0; i < o.length; i++)
			{
				if (o[i] != null)
					System.out.println("Scheme: " + o[i].getId());
			}
		}
		if (selectedType == SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT)
		{
			SchemePath[] o = (SchemePath[])selectedObjects;
			for (int i = 0; i < o.length; i++)
			{
				System.out.println("SchemePath: " + o[i].getId());
				System.out.println("\t start_device_id = \"" + o[i].start_device_id + "\"");
				System.out.println("\t end_device_id = \"" + o[i].end_device_id + "\"");
				System.out.println("\t path_id = \"" + o[i].path_id + "\"");
			}
		}
		if (selectedType == SchemeNavigateEvent.SCHEME_LINK_SELECTED_EVENT)
		{
			SchemeLink[] o = (SchemeLink[])selectedObjects;
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
		if (selectedType == SchemeNavigateEvent.SCHEME_CABLE_LINK_SELECTED_EVENT)
		{
			SchemeCableLink[] o = (SchemeCableLink[])selectedObjects;
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
		if (selectedType == SchemeNavigateEvent.SCHEME_PORT_SELECTED_EVENT)
		{
			SchemePort[] o = (SchemePort[])selectedObjects;
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
		if (selectedType == SchemeNavigateEvent.SCHEME_CABLE_PORT_SELECTED_EVENT)
		{
			SchemeCablePort[] o = (SchemeCablePort[])selectedObjects;
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
}
