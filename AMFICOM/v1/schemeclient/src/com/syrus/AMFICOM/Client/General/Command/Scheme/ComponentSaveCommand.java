package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
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
			if (proto.equipmentTypeId.equals(""))
			{
				System.out.println("Error! Equipment_type_id is empty.");
				return;
			}
			proto.serializable_cell = cell_graph.getArchiveableState(cell_graph.getRoots());
			//GraphActions.setResizable(ugo_graph, ugo_graph.getAll(), false);
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
					if (proto.equipmentTypeId.equals(""))
					{
						System.out.println("Error! Equipment_type_id is empty.");
						return;
					}

					proto.serializable_cell = cell_graph.getArchiveableState(cell_graph.getRoots());
					//GraphActions.setResizable(ugo_graph, ugo_graph.getAll(), false);
					proto.serializable_ugo = ugo_graph.getArchiveableState(ugo_graph.getRoots());
					proto.pack();

					SaveComponentDialog frame = new SaveComponentDialog(aContext);
					frame.init(proto, aContext.getDataSourceInterface());

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
		Map port_types = Pool.getMap(PortType.typ);
		if (port_types != null)
		{
			ArrayList types = new ArrayList();
			for (Iterator it = port_types.values().iterator(); it.hasNext();)
			{
				PortType type = (PortType)it.next();
				if (save_all || type.is_modified)
				{
					types.add(type.getId());
					type.is_modified = false;
				}
			}
			if (!types.isEmpty())
				dsi.SavePortTypes((String[])types.toArray(new String[types.size()]));
		}
		Map cable_port_types = Pool.getMap(CablePortType.typ);
		if (cable_port_types != null)
		{
			ArrayList types = new ArrayList();
			for (Iterator it = cable_port_types.values().iterator(); it.hasNext();)
			{
				CablePortType type = (CablePortType)it.next();
				if (save_all || type.is_modified)
				{
					types.add(type.getId());
					type.is_modified = false;
				}
			}
			if (!types.isEmpty())
				dsi.SaveCablePortTypes((String[])types.toArray(new String[types.size()]));
		}
		Map link_types = Pool.getMap(LinkType.typ);
		if (link_types != null)
		{
			ArrayList types = new ArrayList();
			for (Iterator it = link_types.values().iterator(); it.hasNext();)
			{
				LinkType type = (LinkType)it.next();
				if (save_all || type.is_modified)
				{
					types.add(type.getId());
					type.is_modified = false;
				}
			}
			if (!types.isEmpty())
				dsi.SaveLinkTypes((String[])types.toArray(new String[types.size()]));
		}
		Map cable_link_types = Pool.getMap(CableLinkType.typ);
		if (cable_link_types != null)
		{
			ArrayList types = new ArrayList();
			for (Iterator it = cable_link_types.values().iterator(); it.hasNext();)
			{
				CableLinkType type = (CableLinkType)it.next();
				if (save_all || type.isChanged())
				{
					types.add(type.getId());
					type.setChanged(false);
				}
			}
			if (!types.isEmpty())
				dsi.SaveCableLinkTypes((String[])types.toArray(new String[types.size()]));
		}
		Map eq_types = Pool.getMap(EquipmentType.typ);
		if (eq_types != null)
		{
			ArrayList types = new ArrayList();
			for (Iterator it = eq_types.values().iterator(); it.hasNext();)
			{
				EquipmentType type = (EquipmentType)it.next();
				if (save_all || type.isChanged())
				{
					types.add(type.getId());
					type.setChanged(false);
				}
			}
			if (!types.isEmpty())
				dsi.SaveEquipmentTypes((String[])types.toArray(new String[types.size()]));
		}
	}
}

