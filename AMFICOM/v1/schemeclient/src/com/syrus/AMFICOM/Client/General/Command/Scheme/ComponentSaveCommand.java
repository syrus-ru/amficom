package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Scheme.DefaultCableLink;
import com.syrus.AMFICOM.Client.General.Scheme.DefaultLink;
import com.syrus.AMFICOM.Client.General.Scheme.DeviceGroup;
import com.syrus.AMFICOM.Client.General.Scheme.GraphActions;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CableLinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CablePortType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.PortType;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

import com.syrus.AMFICOM.Client.Schematics.Elements.SaveComponentDialog;

public class ComponentSaveCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeGraph cell_graph;
	SchemeGraph ugo_graph;

	public ComponentSaveCommand(ApplicationContext aContext, SchemeGraph cell_graph, SchemeGraph ugo_graph)
	{
		this.aContext = aContext;
		this.cell_graph = cell_graph;
		this.ugo_graph = ugo_graph;
	}

	public Object clone()
	{
		return new ComponentSaveCommand(aContext, cell_graph, ugo_graph);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return;

		Object[] cells = ugo_graph.getAll();
		if (cells != null && cells.length != 0)
		{
			DeviceGroup[] groups = GraphActions.findTopLevelGroups(cell_graph, cells);
			ProtoElement proto = groups[0].getProtoElement();
			if (proto.equipment_type_id.equals(""))
			{
				System.out.println("Error! Equipment_type_id is empty.");
				return;
			}
			proto.serializable_cell = cell_graph.getArchiveableState(cell_graph.getRoots());
			GraphActions.setResizable(ugo_graph, ugo_graph.getAll(), false);
			proto.serializable_ugo = ugo_graph.getArchiveableState(ugo_graph.getRoots());
			proto.pack();

			SaveComponentDialog frame = new SaveComponentDialog(aContext);
			frame.init(proto, aContext.getDataSourceInterface());
		}
		else
		{
			cells = cell_graph.getAll();
			if (cells != null && cells.length != 0)
			{
				for (int i = 0; i < cells.length; i++)
					if (cells[i] instanceof DefaultLink || cells[i] instanceof DefaultCableLink)
					{
					JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Все линии связи должны находиться внутри сгруппированного компонента",
					"Ошибка",
					JOptionPane.OK_OPTION);
					return;
				}

				DeviceGroup[] groups = GraphActions.findTopLevelGroups(cell_graph, cells);
				if (groups.length == 1)
				{
					ProtoElement proto = groups[0].getProtoElement();
					if (proto.equipment_type_id.equals(""))
					{
						System.out.println("Error! Equipment_type_id is empty.");
						return;
					}

					proto.serializable_cell = cell_graph.getArchiveableState(cell_graph.getRoots());
					GraphActions.setResizable(ugo_graph, ugo_graph.getAll(), false);
					proto.serializable_ugo = ugo_graph.getArchiveableState(ugo_graph.getRoots());
					proto.pack();

					SaveComponentDialog frame = new SaveComponentDialog(aContext);
					frame.init(proto, aContext.getDataSourceInterface());

					Pool.remove("serialized", "serialized");
					//DirectoryToFile.writeAll();
				}
				else if (groups.length > 1)
				{
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Компонент без УГО не может состоять более чем из одной группы элементов", "Ошибка", JOptionPane.OK_OPTION);
					return;
				}
				else
				{
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Перед сохранением необходимо сгруппировать элементы", "Ошибка", JOptionPane.OK_OPTION);
					return;
				}
			}
			else
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Невозможно сохранить пустой компонент", "Ошибка", JOptionPane.OK_OPTION);
				return;
			}
		}
	}

	public static void saveTypes(DataSourceInterface dsi, boolean save_all)
	{
		Hashtable port_types = (Pool.getHash(PortType.typ));
		if (port_types != null)
		{
			ArrayList types = new ArrayList();
			for (Enumeration en = port_types.elements(); en.hasMoreElements();)
			{
				PortType type = (PortType)en.nextElement();
				if (save_all || type.is_modified)
				{
					types.add(type.getId());
					type.is_modified = false;
				}
			}
			if (!types.isEmpty())
				dsi.SavePortTypes((String[])types.toArray(new String[types.size()]));
		}
		Hashtable cable_port_types = (Pool.getHash(CablePortType.typ));
		if (cable_port_types != null)
		{
			ArrayList types = new ArrayList();
			for (Enumeration en = cable_port_types.elements(); en.hasMoreElements();)
			{
				CablePortType type = (CablePortType)en.nextElement();
				if (save_all || type.is_modified)
				{
					types.add(type.getId());
					type.is_modified = false;
				}
			}
			if (!types.isEmpty())
				dsi.SaveCablePortTypes((String[])types.toArray(new String[types.size()]));
		}
		Hashtable link_types = (Pool.getHash(LinkType.typ));
		if (link_types != null)
		{
			ArrayList types = new ArrayList();
			for (Enumeration en = link_types.elements(); en.hasMoreElements();)
			{
				LinkType type = (LinkType)en.nextElement();
				if (save_all || type.is_modified)
				{
					types.add(type.getId());
					type.is_modified = false;
				}
			}
			if (!types.isEmpty())
				dsi.SaveLinkTypes((String[])types.toArray(new String[types.size()]));
		}
		Hashtable cable_link_types = (Pool.getHash(CableLinkType.typ));
		if (cable_link_types != null)
		{
			ArrayList types = new ArrayList();
			for (Enumeration en = cable_link_types.elements(); en.hasMoreElements();)
			{
				CableLinkType type = (CableLinkType)en.nextElement();
				if (save_all || type.is_modified)
				{
					types.add(type.getId());
					type.is_modified = false;
				}
			}
			if (!types.isEmpty())
				dsi.SaveCableLinkTypes((String[])types.toArray(new String[types.size()]));
		}
		Hashtable eq_types = (Pool.getHash(EquipmentType.typ));
		if (eq_types != null)
		{
			ArrayList types = new ArrayList();
			for (Enumeration en = eq_types.elements(); en.hasMoreElements();)
			{
				EquipmentType type = (EquipmentType)en.nextElement();
				if (save_all || type.is_modified)
				{
					types.add(type.getId());
					type.is_modified = false;
				}
			}
			if (!types.isEmpty())
				dsi.SaveEquipmentTypes((String[])types.toArray(new String[types.size()]));
		}
	}
}

