package com.syrus.AMFICOM.Client.General.Scheme;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.Serializable;
import java.util.*;

import javax.swing.*;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;
import oracle.jdeveloper.layout.*;

public class UgoPanel extends JPanel
		implements Printable, OperationListener
{
	public Scheme scheme;
	Map commands = new Hashtable();
	ToolBarPanel toolbar;
	SchemeGraph graph;
	ApplicationContext aContext;
	Dispatcher dispatcher;

	protected boolean insert_ugo = true;
	public boolean ignore_loading = false;

	protected SchemePath editing_path = null;

	protected static String[] buttons = new String[]
	{
		Constants.marqueeTool
	};

	public UgoPanel(ApplicationContext aContext)
	{
		this.aContext = aContext;

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		init_module();
	}

	public void init_module()
	{
		dispatcher = aContext.getDispatcher();
	//	dispatcher.register(this, TreeDataSelectionEvent.type);
		dispatcher.register(this, SchemeElementsEvent.type);
		dispatcher.register(this, CreatePathEvent.typ);
	}

	private void jbInit() throws Exception
	{
		DefaultGraphModel model = new DefaultGraphModel();

		graph = new SchemeGraph(model, aContext, this);
		graph.can_be_editable = true;

		JScrollPane graphView = new JScrollPane(graph);

		this.setLayout(new BorderLayout());
		this.add(graphView, BorderLayout.CENTER);

		toolbar = createToolBar();
		add(toolbar, BorderLayout.NORTH);
		toolbar.setVisible(false);

		graph.setGridEnabled(true);
		graph.setGridVisible(false);
		graph.setGridVisibleAtActualSize(false);
		graph.setGridColor(Color.lightGray);
		graph.setBorderVisible(false);
		graph.setPortsVisible(false);
		graph.setRequestFocusEnabled(false);
		graph.setDragEnabled(false);
		graph.setDropEnabled(false);
		graph.setCloneable(false);
		//graph.setEditable(false);
		//graph.setEnabled(false);
		graph.setBendable(true);
	}

	public String getReportTitle()
	{
		return LangModelSchematics.getString("elementsUGOTitle");
	}

	public void setUgoInsertable (boolean b)
	{
		this.insert_ugo = b;
	}

	protected ToolBarPanel createToolBar()
	{
		ToolBarPanel toolbar = new ToolBarPanel(this);
		commands.putAll(toolbar.createGraphButtons(this));

		String[] buttons = getButtons();
		for (int i = 0; i < buttons.length; i++)
		{
			if (buttons[i].equals(Constants.separator))
				toolbar.insert(new JToolBar.Separator());
			else
				toolbar.insert((Component)commands.get(buttons[i]));
		}
		return toolbar;
	}

	public SchemeGraph getGraph()
	{
		return graph;
	}

	protected String[] getButtons()
	{
		return buttons;
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(SchemeElementsEvent.type))
		{
			SchemeElementsEvent see = (SchemeElementsEvent)ae;
			if (see.OPEN_PRIMARY_SCHEME)
			{
				if (!ignore_loading)
				{
					Scheme sch = (Scheme)see.obj;
					GraphActions.clearGraph(graph);
					openScheme(sch);
				}
			}
			if (see.OPEN_ELEMENT)
			{
				if (!ignore_loading)
				{
					graph.skip_notify = true;
					Object obj = see.obj;
					if (obj instanceof ProtoElement)
					{
						ProtoElement proto = (ProtoElement)obj;
						setProtoCell(proto);
					}
					graph.skip_notify = false;
				}
			}
			if (see.OPEN_SCHEME)
			{
				if (!ignore_loading)
				{
					graph.skip_notify = true;
					Object obj = see.obj;
					if (obj instanceof Scheme)
					{
						Scheme scheme = (Scheme)obj;
						setSchemeCell(scheme);
					}
					graph.skip_notify = false;
				}
			}
			if (see.CLOSE_SCHEME)
			{
				graph.skip_notify = true;
				Object obj = see.obj;
				if (obj instanceof Scheme)
				{
					Scheme scheme = (Scheme)obj;
					removeScheme(scheme);
					graph.setGraphChanged(false);
				}
				graph.skip_notify = false;
			}
			if (see.UGO_TEXT_UPDATE)
			{
				String id = (String)see.getSource();
				String text = (String)see.obj;
				DeviceGroup[] groups = GraphActions.findTopLevelGroups(graph, graph.getSelectionCells());
				if (groups.length == 1)
				{
					DeviceGroup cell = (DeviceGroup)groups[0];
					if (cell.getProtoElementId().equals(id) || cell.getSchemeElementId().equals(id) || cell.getSchemeId().equals(id))
					{
						if (cell.getChildCount() > 0)
						{
							for (Enumeration en = cell.children(); en.hasMoreElements();)
							{
								Object child = en.nextElement();
								if (child instanceof DeviceCell)
								{
									GraphActions.setText(graph, ((DeviceCell)child), text);
									break;
								}
							}
						}
						else
							GraphActions.setText(graph, cell, text);
					}
				}
			}
			if (see.UGO_ICON_UPDATE)
			{
				String id = (String)see.getSource();
				ImageIcon icon = (ImageIcon)see.obj;
				DeviceGroup[] groups = GraphActions.findTopLevelGroups(graph, graph.getSelectionCells());
				if (groups.length == 1)
				{
					DeviceGroup cell = (DeviceGroup)groups[0];
					if (cell.getProtoElementId().equals(id) || cell.getSchemeElementId().equals(id) || cell.getSchemeId().equals(id))
					{
						if (cell.getChildCount() > 0)
						{
							for (Enumeration en = cell.children(); en.hasMoreElements();)
							{
								Object child = en.nextElement();
								if (child instanceof DeviceCell)
								{
									GraphActions.setImage(graph, ((DeviceCell)child), icon);
									break;
								}
							}
						}
						else
							GraphActions.setImage(graph, cell, icon);
					}
				}
			}
			if (see.UGO_CREATE)
			{
				ArrayList v = (ArrayList)see.obj;
				new CreateUgoAction(graph).create(v, aContext.getDataSourceInterface());
			}
			if (see.SCHEME_UGO_CREATE)
			{
				ArrayList v = (ArrayList)see.obj;
				new CreateSchemeUgoAction(graph).create(v);
			}
			if (see.CABLE_PORT_NAME_UPDATE)
			{
				String id = (String)see.getSource();
				String text = (String)see.obj;

				Object[] cells = graph.getAll();
				for (int i = 0; i < cells.length; i++)
				{
					if (cells[i] instanceof CablePortCell &&
							((CablePortCell)cells[i]).getSchemeCablePortId().equals(id))
						GraphActions.setText(graph, cells[i], text);
					else if (cells[i] instanceof BlockPortCell &&
							((BlockPortCell)cells[i]).getSchemeCablePortId().equals(id))
						GraphActions.setText(graph, cells[i], text);
				}
			}
			if (see.PORT_NAME_UPDATE)
			{
				String id = (String)see.getSource();
				String text = (String)see.obj;

				Object[] cells = graph.getAll();
				for (int i = 0; i < cells.length; i++)
				{
					if (cells[i] instanceof PortCell &&
							((PortCell)cells[i]).getSchemePortId().equals(id))
						GraphActions.setText(graph, cells[i], text);
					else if (cells[i] instanceof BlockPortCell &&
						((BlockPortCell)cells[i]).getSchemePortId().equals(id))
						GraphActions.setText(graph, cells[i], text);
				}
			}
			if (see.CABLE_LINK_NAME_UPDATE)
			{
				String id = (String)see.getSource();
				String text = (String)see.obj;

				Object[] cells = graph.getAll();
				for (int i = 0; i < cells.length; i++)
				{
					if (cells[i] instanceof DefaultCableLink &&
							((DefaultCableLink)cells[i]).getSchemeCableLinkId().equals(id))
					{
						GraphActions.setText(graph, cells[i], text);
						break;
					}
				}
			}
			if (see.LINK_NAME_UPDATE)
			{
				String id = (String)see.getSource();
				String text = (String)see.obj;

				Object[] cells = graph.getAll();
				for (int i = 0; i < cells.length; i++)
				{
					if (cells[i] instanceof DefaultLink &&
							((DefaultLink)cells[i]).getSchemeLinkId().equals(id))
					{
						GraphActions.setText(graph, cells[i], text);
						break;
					}
				}
			}
			if (see.PORT_TYPE_UPDATE)
			{
				SchemePort[] ports = (SchemePort[])see.getSource();
				Object[] cells = graph.getAll();

				ArrayList connected_ports = new ArrayList();
				ArrayList non_connected_ports = new ArrayList();
				for (int i = 0; i < cells.length; i++)
					if (cells[i] instanceof PortCell)
					{
						SchemePort port = ((PortCell)cells[i]).getSchemePort();
						for (int j = 0; j < ports.length; j++)
							if (ports[j].equals(port))
							{
								if (port.link_id.equals(""))
									non_connected_ports.add(cells[i]);
								else
									connected_ports.add(cells[i]);
							}
					}
				Color color = Color.white;
				if (((PortType)see.obj).p_class.equals("splice"))
					color = Color.black;

				GraphActions.setObjectsBackColor(graph, connected_ports.toArray(new PortCell[connected_ports.size()]), color);
				GraphActions.setObjectsBackColor(graph, non_connected_ports.toArray(new PortCell[non_connected_ports.size()]), Color.yellow);
			}
			if (see.CABLE_PORT_TYPE_UPDATE)
			{
				SchemeCablePort[] ports = (SchemeCablePort[])see.getSource();
				Object[] cells = graph.getAll();

				ArrayList connected_ports = new ArrayList();
				ArrayList non_connected_ports = new ArrayList();
				for (int i = 0; i < cells.length; i++)
					if (cells[i] instanceof CablePortCell)
					{
					SchemeCablePort port = ((CablePortCell)cells[i]).getSchemeCablePort();
					for (int j = 0; j < ports.length; j++)
						if (ports[j].equals(port))
						{
						if (port.cable_link_id.equals(""))
							non_connected_ports.add(cells[i]);
						else
							connected_ports.add(cells[i]);
					}
				}
				GraphActions.setObjectsBackColor(graph, connected_ports.toArray(new CablePortCell[connected_ports.size()]), Color.white);
				GraphActions.setObjectsBackColor(graph, non_connected_ports.toArray(new CablePortCell[non_connected_ports.size()]), Color.yellow);
			}

			if (see.OBJECT_TYPE_UPDATE)
			{
				ObjectResource res = (ObjectResource)ae.getSource();

				Object[] cells = graph.getSelectionCells();
				ArrayList new_cells = new ArrayList(cells.length);
				for (int i = 0; i < cells.length; i++)
					if (!(cells[i] instanceof DefaultEdge) || cells[i] instanceof DefaultLink || cells[i] instanceof DefaultCableLink)
						new_cells.add(cells[i]);
				Object[] ncells = new_cells.toArray();

				if (ncells.length == 0)
					return;
				Object obj = ncells[0];

				if (obj instanceof DeviceGroup)
				{
					if (ncells.length != 1)
						return;
					EquipmentType new_eqt;
					if (res instanceof ProtoElement)
					{
						ProtoElement proto = (ProtoElement) res;
						new_eqt = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id);
					}
					else
						new_eqt = (EquipmentType)res;

					ProtoElement p = ((DeviceGroup)obj).getProtoElement();
					p.equipment_type_id = new_eqt.getId();
				}
				if (obj instanceof PortCell)
				{
					int counter = 0;
					for (int i = 0; i < ncells.length; i++)
						if (ncells[i] instanceof PortCell)
							counter++;
					if (counter == ncells.length)
					{
						Color color = Color.white;
						if (res instanceof PortType)
							if (((PortType)res).p_class.equals("splice"))
								color = Color.black;

						ArrayList connected_ports = new ArrayList();
						ArrayList non_connected_ports = new ArrayList();
						for (int i = 0; i < ncells.length; i++)
							if (((PortCell)ncells[i]).getSchemePort().link_id.equals(""))
								non_connected_ports.add(ncells[i]);
						else
							connected_ports.add(ncells[i]);

						GraphActions.setObjectsBackColor(graph, connected_ports.toArray(new PortCell[connected_ports.size()]), color);
						GraphActions.setObjectsBackColor(graph, non_connected_ports.toArray(new PortCell[non_connected_ports.size()]), Color.yellow);
						for (int i = 0; i < ncells.length; i++)
							((PortCell)ncells[i]).getSchemePort().port_type_id = res.getId();
					}
				}
				if (obj instanceof CablePortCell)
				{
					int counter = 0;
					for (int i = 0; i < ncells.length; i++)
						if (ncells[i] instanceof CablePortCell)
							counter++;
					if (counter == ncells.length)
						GraphActions.setObjectsBackColor(graph, ncells, Color.white);
					for (int i = 0; i < ncells.length; i++)
						((CablePortCell)ncells[i]).getSchemeCablePort().cable_port_type_id = res.getId();
				}
				if (obj instanceof DefaultLink)
				{
					int counter = 0;
					for (int i = 0; i < ncells.length; i++)
						if (ncells[i] instanceof DefaultLink)
							counter++;
					if (counter == ncells.length)
					{
						for (int i = 0; i < ncells.length; i++)
							((DefaultLink)ncells[i]).getSchemeLink().link_type_id = res.getId();
					}
				}
				if (obj instanceof DefaultCableLink)
				{
					int counter = 0;
					for (int i = 0; i < ncells.length; i++)
						if (ncells[i] instanceof DefaultCableLink)
							counter++;
					if (counter == ncells.length)
					{
						for (int i = 0; i < ncells.length; i++)
							((DefaultCableLink)ncells[i]).getSchemeCableLink().cable_link_type_id = res.getId();
					}
				}
			}
		}
		else if (ae.getActionCommand().equals(CreatePathEvent.typ))
		{
			CreatePathEvent cpe = (CreatePathEvent)ae;
			if (cpe.SAVE_PATH)
			{
				SchemePath[] paths = (SchemePath[])cpe.cells;
				Hashtable lp = new Hashtable();
				for (int i = 0; i < paths.length; i++)
				{
					for (int j = 0; j < paths[i].links.size(); j++)
					{
						PathElement pe = (PathElement)paths[i].links.get(j);
						lp.put(pe.link_id, paths[i].getId());
					}
				}

				Object[] cells = graph.getAll();
				for (int i = 0; i < cells.length; i++)
				{
					if (cells[i] instanceof DefaultCableLink)
					{
						DefaultCableLink clink = (DefaultCableLink)cells[i];
						String p_id = (String)lp.get(clink.getSchemeCableLinkId());
						if (p_id != null)
							clink.setSchemePathId(p_id);
					}
					else if (cells[i] instanceof DefaultLink)
					{
						DefaultLink link = (DefaultLink)cells[i];
						String p_id = (String)lp.get(link.getSchemeLinkId());
						if (p_id != null)
							link.setSchemePathId(p_id);
					}
				}
			}
		}
	}

	public Scheme updateScheme()
	{
		if (scheme != null)
		{
			scheme.serializable_ugo = graph.getArchiveableState(graph.getRoots());
			boolean res = scheme.pack();
			if (!res)
				return null;
		}
		return scheme;
	}

	protected void setProtoCell (ProtoElement proto)
	{
		if (!insert_ugo)
			return;
		if (proto != null)
		{
			GraphActions.clearGraph(graph);
			//proto.unpack();
			//insertCell (proto.serializable_ugo, true);
		}
		else
			showNoSelection();

		repaint();
	}

	protected void setSchemeCell (Scheme scheme)
	{
	/*	if (scheme != null)
		{
			scheme.unpack();
			insertCell (scheme.serializable_ugo);
		}
		else
			showNoSelection();

		repaint();*/
	}

/*	private void insertCell (Serializable serializable_ugo)
	{
		graph.skip_notify = true;
		graph.setSelectionCells(new Object[0]);
		Object[] cells = graph.getAll();
		graph.getModel().remove(cells);
		graph.skip_notify = false;

		if (serializable_ugo != null)
			graph.setFromArchivedState (serializable_ugo);
		else
			showNoSelection();
	}*/

	public boolean removeScheme(Scheme sch)
	{
		if (scheme != null && sch.getId().equals(scheme.getId()))
		{
			GraphActions.clearGraph(graph);
			graph.setGraphChanged(false);
			return true;
		}
		return false;
	}

	public void openScheme(Scheme sch)
	{
		scheme = sch;
		GraphActions.clearGraph(graph);
		sch.unpack();

		Map clones = graph.copyFromArchivedState(sch.serializable_ugo, new java.awt.Point(0, 0));
		graph.selectionNotify();
	//	assignClonedIds(clones);
	}

	public Map insertCell (Serializable serializable_cell, boolean remove_old)
	{
		if (remove_old)
			GraphActions.clearGraph(graph);

		if (serializable_cell != null && serializable_cell instanceof Vector)
		{
			Map clones = graph.copyFromArchivedState(serializable_cell, new Point(0, 0));

			Vector v = (Vector) serializable_cell;
			Object[] cells = (Object[]) v.get(0);
			ArrayList new_cells = new ArrayList();

			for (int i = 0; i < cells.length; i++)
			{
				if (cells[i] instanceof DeviceGroup)
					new_cells.add(cells[i]);
			}
			DeviceGroup[] groups = (DeviceGroup[])new_cells.toArray(new DeviceGroup[new_cells.size()]);

			if (groups.length != 0)
				assignClonedIds(clones.values().toArray());

			return clones;
		}
		showNoSelection();
		return null;
	}

	public void assignClonedIds(Object[] cells)
	{
		for (int i = 0; i < cells.length; i++)
		{
			Object cloned_cell = cells[i];
			if (cloned_cell instanceof DeviceGroup)
			{
				String or_id = ((DeviceGroup)cloned_cell).getProtoElementId();
				String new_id = (String)Pool.get("clonedids", or_id);
				if (new_id != null)
					((DeviceGroup)cloned_cell).setProtoElementId(new_id);

				or_id = ((DeviceGroup)cloned_cell).getProtoElementId();
				new_id = (String)Pool.get("proto2schemeids", or_id);
				if (new_id != null)
					((DeviceGroup)cloned_cell).setSchemeElementId(new_id);

				or_id = ((DeviceGroup)cloned_cell).getSchemeElementId();
				new_id = (String)Pool.get("clonedids", or_id);
				if (new_id != null)
					((DeviceGroup)cloned_cell).setSchemeElementId(new_id);

				or_id = ((DeviceGroup)cloned_cell).getSchemeId();
				new_id = (String)Pool.get("clonedids", or_id);
				if (new_id != null)
					((DeviceGroup)cloned_cell).setSchemeId(new_id);
			}
			else if (cloned_cell instanceof DeviceCell)
			{
				String c_id = (String)Pool.get("clonedids", ((DeviceCell)cloned_cell).getSchemeDeviceId());
				if (c_id == null)
				{
					//SchemeDevice dev = ((DeviceCell)cells[i]).getSchemeDevice();
					//SchemeDevice c_dev = (SchemeDevice)dev.clone(aContext.getDataSourceInterface());
					c_id = ((DeviceCell)cloned_cell).getSchemeDeviceId();
				}
				((DeviceCell)cloned_cell).setSchemeDeviceId(c_id);
			}
			else if (cloned_cell instanceof PortCell)
			{
				String id = ((PortCell)cloned_cell).getSchemePortId();
				String new_id = (String)Pool.get("clonedids", id);
				if (new_id != null)
					((PortCell)cloned_cell).setSchemePortId(new_id);
			}
			else if (cloned_cell instanceof CablePortCell)
			{
				String id = ((CablePortCell)cloned_cell).getSchemeCablePortId();
				String new_id = (String)Pool.get("clonedids", id);
				if (new_id != null)
					((CablePortCell)cloned_cell).setSchemeCablePortId(new_id);
			}
			else if (cloned_cell instanceof DefaultCableLink)
			{
				String id = ((DefaultCableLink)cloned_cell).getSchemeCableLinkId();
				String new_id = (String)Pool.get("clonedids", id);
				if (new_id != null)
					((DefaultCableLink)cloned_cell).setSchemeCableLinkId(new_id);

				((DefaultCableLink)cloned_cell).setSchemePathId("");
//				String path_id = ((DefaultCableLink)cloned_cell).getSchemePathId();
//				String new_path_id = (String)Pool.get("clonedids", id);
//				if (new_path_id != null)
//					((DefaultCableLink)cloned_cell).setSchemePathId(new_path_id);
			}
			else if (cloned_cell instanceof DefaultLink)
			{
				String id = ((DefaultLink)cloned_cell).getSchemeLinkId();
				String new_id = (String)Pool.get("clonedids", id);
				if (new_id != null)
					((DefaultLink)cloned_cell).setSchemeLinkId(new_id);

				((DefaultLink)cloned_cell).setSchemePathId("");
//				String path_id = ((DefaultLink)cloned_cell).getSchemePathId();
//				String new_path_id = (String)Pool.get("clonedids", id);
//				if (new_path_id != null)
//					((DefaultLink)cloned_cell).setSchemePathId(new_path_id);
			}
			else if (cloned_cell instanceof BlockPortCell)
			{
				BlockPortCell bpc = (BlockPortCell)cloned_cell;
				String p_id = (String)Pool.get("clonedids", bpc.getSchemePortId());
				if (bpc.isCablePort())
				{
					if (p_id != null)
						bpc.setSchemeCablePortId(p_id);
				}
				else
				{
					if (p_id != null)
						bpc.setSchemePortId(p_id);
				}
			}
		}
	}


	void showNoSelection ()
	{
		Object[] cells = graph.getRoots();
		if (cells != null && cells.length != 0)
		{
			cells = DefaultGraphModel.getDescendants(graph.getModel(), cells).toArray();
			graph.getModel().remove(cells);
			graph.setScale(1);
		}
	}

	public int print(Graphics g, PageFormat pf, int pi) throws PrinterException
	{
		if (pi > 0)
			return Printable.NO_SUCH_PAGE;

		graph.printAll(g);
		return Printable.PAGE_EXISTS;
	}

	public void setSelectionAttributes(Map map) {
		Object[] cells = DefaultGraphModel.getDescendants(graph.getModel(),
				graph.getSelectionCells()).toArray();
		map = GraphConstants.cloneMap(map);
		map.remove(GraphConstants.BOUNDS);
		map.remove(GraphConstants.POINTS);
		if (cells != null && cells.length > 0 && !map.isEmpty()) {
			CellView[] views = graph.getGraphLayoutCache().getMapping(cells);
			Map viewMap = new Hashtable();
			for (int i = 0; i < views.length; i++)
				viewMap.put(views[i], GraphConstants.cloneMap(map));
			graph.getGraphLayoutCache().edit(viewMap, null, null, null);
		}
	}

	public void setGraphSize (Dimension d)
	{
		graph.setPreferredSize(d);
	}

//******************************************************************************
//******************************** Actions *************************************
//******************************************************************************
	/* This will change the source of the actionevent to graph. */
	protected class EventRedirector implements ActionListener
	{
		protected Action action;

		public EventRedirector(Action a)
		{
			this.action = a;
		}

		public void actionPerformed(ActionEvent e)
		{
			JComponent source = graph;
			e = new ActionEvent(source, e.getID(), e.getActionCommand(), e.getModifiers());
			action.actionPerformed(e);
		}
	}

	class ToolBarPanel extends JPanel
	{
		public final Dimension btn_size = new Dimension(24, 24);
		protected int position = 0;
		UgoPanel panel;
		Hashtable buttons = new Hashtable();

		public ToolBarPanel (UgoPanel panel)
		{
			this.panel = panel;

			try
			{
				jbInit();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		private void jbInit() throws Exception
		{
			setLayout (new XYLayout());
		}

		protected Hashtable createGraphButtons (UgoPanel p)
		{
			Hashtable buttons = new Hashtable();

			if (graph.getMarqueeHandler() instanceof SchemeGraph.ShemeMarqueeHandler)
			{
				SchemeGraph.ShemeMarqueeHandler mh = (SchemeGraph.ShemeMarqueeHandler) graph.getMarqueeHandler();

				buttons.put(Constants.marqueeTool,
										createToolButton(mh.s, btn_size, null, null,
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pointer.gif")),
										new MarqeeAction(graph), true));
				ButtonGroup group = new ButtonGroup();
				for (Enumeration enum = buttons.elements(); enum.hasMoreElements();)
				{
					AbstractButton button = (AbstractButton)enum.nextElement();
					group.add(button);
				}
				mh.s.doClick();
			}
			return buttons;
		}

		AbstractButton createToolButton (
				AbstractButton b,
				Dimension preferred_size,
				String text,
				String tooltip,
				Icon icon,
				Action action,
				boolean isEnabled
				)
		{
			if (preferred_size != null)
				b.setPreferredSize(preferred_size);
			if (text != null)
				b.setText (text);
			if (tooltip != null)
				b.setToolTipText (tooltip);
			if (icon != null)
				b.setIcon(icon);
			if (action != null)
			{
				b.addActionListener(new EventRedirector(action));
				b.setActionCommand(action.NAME);
			}
			b.setEnabled(isEnabled);
			b.setFocusable(false);
			return b;
		}

		public void insert (Component c)
		{
			add (c, new XYConstraints(position, 0, -1, -1));
			position += c.getPreferredSize().width;
		}
	}
}

