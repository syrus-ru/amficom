package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;

import oracle.jdeveloper.layout.*;

public class UgoPanel extends JPanel
		implements Printable, OperationListener
{
//	static Scheme primary_scheme;
//	public Scheme scheme;

	Map commands = new HashMap();
	ToolBarPanel toolbar;
	ApplicationContext aContext;
	Dispatcher dispatcher;

	protected SchemeGraph graph;	//текущий активный граф
	protected boolean insert_ugo = true;
	public boolean ignore_loading = false;

	protected SchemePath editing_path = null;

	private static String[] buttons = new String[]
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

		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext)
	{
		if (aContext != null)
		{
			this.aContext = aContext;
			init_module();
		}
	}

	public void init_module()
	{
		dispatcher = aContext.getDispatcher();
		dispatcher.register(this, SchemeElementsEvent.type);
	}

	private void jbInit() throws Exception
	{
		setLayout(new BorderLayout());
		add(createGraph(), BorderLayout.CENTER);

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

	protected Component createGraph()
	{
		DefaultGraphModel model = new DefaultGraphModel();
		graph = new SchemeGraph(model, aContext);
		graph.can_be_editable = true;
		JScrollPane graphView = new JScrollPane(graph);
		return graphView;
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
		ToolBarPanel toolBarPanel = new ToolBarPanel();
		commands.putAll(toolBarPanel.createGraphButtons(this));

		for (int i = 0; i < buttons.length; i++)
		{
			if (buttons[i].equals(Constants.separator))
				toolBarPanel.insert(new JToolBar.Separator());
			else
				toolBarPanel.insert((Component)commands.get(buttons[i]));
		}
		return toolBarPanel;
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
			if (see.UGO_TEXT_UPDATE)
			{
				String id = (String)see.getSource();
				String text = (String)see.obj;
				DeviceGroup[] groups = GraphActions.findTopLevelGroups(getGraph(), getGraph().getSelectionCells());
				if (groups.length == 1)
				{
					DeviceGroup cell = groups[0];
					if (cell.getProtoElementId().equals(id) || cell.getSchemeElementId().equals(id) || cell.getSchemeId().equals(id))
					{
						if (cell.getChildCount() > 0)
						{
							for (Enumeration en = cell.children(); en.hasMoreElements();)
							{
								Object child = en.nextElement();
								if (child instanceof DeviceCell)
								{
									GraphActions.setText(getGraph(), child, text);
									break;
								}
							}
						}
						else
							GraphActions.setText(getGraph(), cell, text);
					}
				}
			}
			if (see.UGO_ICON_UPDATE)
			{
				String id = (String)see.getSource();
				ImageIcon icon = (ImageIcon)see.obj;
				DeviceGroup[] groups = GraphActions.findTopLevelGroups(getGraph(), getGraph().getSelectionCells());
				if (groups.length == 1)
				{
					DeviceGroup cell = groups[0];
					if (cell.getProtoElementId().equals(id) || cell.getSchemeElementId().equals(id) || cell.getSchemeId().equals(id))
					{
						if (cell.getChildCount() > 0)
						{
							for (Enumeration en = cell.children(); en.hasMoreElements();)
							{
								Object child = en.nextElement();
								if (child instanceof DeviceCell)
								{
									GraphActions.setImage(getGraph(), ((DeviceCell)child), icon);
									break;
								}
							}
						}
						else
							GraphActions.setImage(getGraph(), cell, icon);
					}
				}
			}
			if (see.UGO_CREATE)
			{
				ArrayList v = (ArrayList)see.obj;
				new CreateUgoAction(getGraph()).create(v);
			}
			if (see.SCHEME_UGO_CREATE)
			{
				ArrayList v = (ArrayList)see.obj;
				new CreateSchemeUgoAction(getGraph()).create(v);
			}
			if (see.CABLE_PORT_NAME_UPDATE)
			{
				String id = (String)see.getSource();
				String text = (String)see.obj;

				Object[] cells = getGraph().getAll();
				for (int i = 0; i < cells.length; i++)
				{
					if (cells[i] instanceof CablePortCell &&
							((CablePortCell)cells[i]).getSchemeCablePortId().equals(id))
						GraphActions.setText(getGraph(), cells[i], text);
					else if (cells[i] instanceof BlockPortCell &&
							((BlockPortCell)cells[i]).getSchemeCablePortId().equals(id))
						GraphActions.setText(getGraph(), cells[i], text);
				}
			}
			if (see.PORT_NAME_UPDATE)
			{
				String id = (String)see.getSource();
				String text = (String)see.obj;

				Object[] cells = getGraph().getAll();
				for (int i = 0; i < cells.length; i++)
				{
					if (cells[i] instanceof PortCell &&
							((PortCell)cells[i]).getSchemePortId().equals(id))
						GraphActions.setText(getGraph(), cells[i], text);
					else if (cells[i] instanceof BlockPortCell &&
						((BlockPortCell)cells[i]).getSchemePortId().equals(id))
						GraphActions.setText(getGraph(), cells[i], text);
				}
			}
			if (see.CABLE_LINK_NAME_UPDATE)
			{
				String id = (String)see.getSource();
				String text = (String)see.obj;

				Object[] cells = getGraph().getAll();
				for (int i = 0; i < cells.length; i++)
				{
					if (cells[i] instanceof DefaultCableLink &&
							((DefaultCableLink)cells[i]).getSchemeCableLinkId().equals(id))
					{
						GraphActions.setText(getGraph(), cells[i], text);
						break;
					}
				}
			}
			if (see.LINK_NAME_UPDATE)
			{
				String id = (String)see.getSource();
				String text = (String)see.obj;

				Object[] cells = getGraph().getAll();
				for (int i = 0; i < cells.length; i++)
				{
					if (cells[i] instanceof DefaultLink &&
							((DefaultLink)cells[i]).getSchemeLinkId().equals(id))
					{
						GraphActions.setText(getGraph(), cells[i], text);
						break;
					}
				}
			}
			if (see.PORT_TYPE_UPDATE)
			{
				SchemePort[] ports = (SchemePort[])see.getSource();
				Object[] cells = getGraph().getAll();

				ArrayList connected_ports = new ArrayList();
				ArrayList non_connected_ports = new ArrayList();
				for (int i = 0; i < cells.length; i++)
					if (cells[i] instanceof PortCell)
					{
						SchemePort port = ((PortCell)cells[i]).getSchemePort();
						for (int j = 0; j < ports.length; j++)
							if (ports[j].equals(port))
							{
								if (port.abstractSchemeLink() == null)
									non_connected_ports.add(cells[i]);
								else
									connected_ports.add(cells[i]);
							}
					}
				Color color = Color.white;
				if (((PortType)see.obj).getSort().equals(PortTypeSort.PORTTYPESORT_THERMAL))
					color = Color.black;

				GraphActions.setObjectsBackColor(getGraph(), connected_ports.toArray(new PortCell[connected_ports.size()]), color);
				GraphActions.setObjectsBackColor(getGraph(), non_connected_ports.toArray(new PortCell[non_connected_ports.size()]), Color.yellow);
			}
			if (see.CABLE_PORT_TYPE_UPDATE)
			{
				SchemeCablePort[] ports = (SchemeCablePort[])see.getSource();
				Object[] cells = getGraph().getAll();

				ArrayList connected_ports = new ArrayList();
				ArrayList non_connected_ports = new ArrayList();
				for (int i = 0; i < cells.length; i++)
					if (cells[i] instanceof CablePortCell)
					{
					SchemeCablePort port = ((CablePortCell)cells[i]).getSchemeCablePort();
					for (int j = 0; j < ports.length; j++)
						if (ports[j].equals(port))
						{
						if (port.abstractSchemeLink() == null)
							non_connected_ports.add(cells[i]);
						else
							connected_ports.add(cells[i]);
					}
				}
				GraphActions.setObjectsBackColor(getGraph(), connected_ports.toArray(new CablePortCell[connected_ports.size()]), Color.white);
				GraphActions.setObjectsBackColor(getGraph(), non_connected_ports.toArray(new CablePortCell[non_connected_ports.size()]), Color.yellow);
			}

			if (see.OBJECT_TYPE_UPDATE)
			{
				/*Object res = ae.getSource();

				Object[] cells = getGraph().getSelectionCells();
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
					EquipmentTypeImpl new_eqt;
					if (res instanceof SchemeProtoElement)
					{
						SchemeProtoElement proto = (SchemeProtoElement)res;
						new_eqt = proto.equipmentTypeImpl();
					}
					else
						new_eqt = (EquipmentType)res;

					SchemeProtoElement p = ((DeviceGroup)obj).getProtoElement();
					p.equipmentType() = new_eqt.getId();
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
							if (((PortType)res).pClass.equals("splice"))
								color = Color.black;

						ArrayList connected_ports = new ArrayList();
						ArrayList non_connected_ports = new ArrayList();
						for (int i = 0; i < ncells.length; i++)
							if (((PortCell)ncells[i]).getSchemePort().linkId.equals(""))
								non_connected_ports.add(ncells[i]);
						else
							connected_ports.add(ncells[i]);

						GraphActions.setObjectsBackColor(getGraph(), connected_ports.toArray(new PortCell[connected_ports.size()]), color);
						GraphActions.setObjectsBackColor(getGraph(), non_connected_ports.toArray(new PortCell[non_connected_ports.size()]), Color.yellow);
						for (int i = 0; i < ncells.length; i++)
							((PortCell)ncells[i]).getSchemePort().portTypeId = res.getId();
					}
				}
				if (obj instanceof CablePortCell)
				{
					int counter = 0;
					for (int i = 0; i < ncells.length; i++)
						if (ncells[i] instanceof CablePortCell)
							counter++;
					if (counter == ncells.length)
						GraphActions.setObjectsBackColor(getGraph(), ncells, Color.white);
					for (int i = 0; i < ncells.length; i++)
						((CablePortCell)ncells[i]).getSchemeCablePort().cablePortTypeId = res.getId();
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
							((DefaultLink)ncells[i]).getSchemeLink().linkTypeId = res.getId();
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
							((DefaultCableLink)ncells[i]).getSchemeCableLink().cableLinkTypeId = res.getId();
					}
				}*/
			}
		}
	}

	public Scheme updateScheme()
	{
		Scheme scheme = getGraph().getScheme();
		if (scheme != null)
			scheme.ugoCellImpl().setData((List)getGraph().getArchiveableState(getGraph().getRoots()));
		return scheme;
	}

	public SchemeElement updateSchemeElement()
	{
		SchemeElement scheme_element = getGraph().getSchemeElement();
		if (scheme_element != null)
			scheme_element.schemeCellImpl().setData((List)getGraph().getArchiveableState(getGraph().getRoots()));
		return scheme_element;
	}

	protected void setProtoCell (SchemeProtoElement proto, Point p)
	{
		if (!insert_ugo)
			return;
		GraphActions.clearGraph(getGraph());
		repaint();
	}

	protected void setSchemeCell (Scheme scheme)
	{
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

	public Map insertCell (List serializable_cell, boolean remove_old, Point p)
	{
		if (remove_old)
			GraphActions.clearGraph(getGraph());

		if (serializable_cell != null)
		{
			Map clones = getGraph().copyFromArchivedState(serializable_cell, p);

			List v = serializable_cell;
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
		return null;
	}

	public static void assignClonedIds(Object[] cells)
	{
		for (int i = 0; i < cells.length; i++)
		{
			Object cloned_cell = cells[i];
			if (cloned_cell instanceof DeviceGroup)
			{
				Identifier or_id = ((DeviceGroup)cloned_cell).getProtoElementId();
				Identifier new_id = (Identifier)Pool.get("clonedids", or_id.identifierString());
				if (new_id != null)
					((DeviceGroup)cloned_cell).setProtoElementId(new_id);

				or_id = ((DeviceGroup)cloned_cell).getProtoElementId();
				new_id = (Identifier)Pool.get("proto2schemeids", or_id.identifierString());
				if (new_id != null)
					((DeviceGroup)cloned_cell).setSchemeElementId(new_id);

				or_id = ((DeviceGroup)cloned_cell).getSchemeElementId();
				new_id = (Identifier)Pool.get("clonedids", or_id.identifierString());
				if (new_id != null)
					((DeviceGroup)cloned_cell).setSchemeElementId(new_id);

				or_id = ((DeviceGroup)cloned_cell).getSchemeId();
				new_id = (Identifier)Pool.get("clonedids", or_id.identifierString());
				if (new_id != null)
					((DeviceGroup)cloned_cell).setSchemeId(new_id);
			}
			else if (cloned_cell instanceof DeviceCell)
			{
				Identifier c_id = (Identifier)Pool.get("clonedids", ((DeviceCell)cloned_cell).getSchemeDeviceId().identifierString());
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
				Identifier id = ((PortCell)cloned_cell).getSchemePortId();
				Identifier new_id = (Identifier)Pool.get("clonedids", id.identifierString());
				if (new_id != null)
					((PortCell)cloned_cell).setSchemePortId(new_id);
			}
			else if (cloned_cell instanceof CablePortCell)
			{
				Identifier id = ((CablePortCell)cloned_cell).getSchemeCablePortId();
				Identifier new_id = (Identifier)Pool.get("clonedids", id.identifierString());
				if (new_id != null)
					((CablePortCell)cloned_cell).setSchemeCablePortId(new_id);
			}
			else if (cloned_cell instanceof DefaultCableLink)
			{
				Identifier id = ((DefaultCableLink)cloned_cell).getSchemeCableLinkId();
				Identifier new_id = (Identifier)Pool.get("clonedids", id.identifierString());
				if (new_id != null)
					((DefaultCableLink)cloned_cell).setSchemeCableLinkId(new_id);

//				((DefaultCableLink)cloned_cell).setSchemePathId("");
//				String path_id = ((DefaultCableLink)cloned_cell).getSchemePathId();
//				String new_path_id = (String)Pool.get("clonedids", id);
//				if (new_path_id != null)
//					((DefaultCableLink)cloned_cell).setSchemePathId(new_path_id);
			}
			else if (cloned_cell instanceof DefaultLink)
			{
				Identifier id = ((DefaultLink)cloned_cell).getSchemeLinkId();
				Identifier new_id = (Identifier)Pool.get("clonedids", id.identifierString());
				if (new_id != null)
					((DefaultLink)cloned_cell).setSchemeLinkId(new_id);

//				((DefaultLink)cloned_cell).setSchemePathId("");
//				String path_id = ((DefaultLink)cloned_cell).getSchemePathId();
//				String new_path_id = (String)Pool.get("clonedids", id);
//				if (new_path_id != null)
//					((DefaultLink)cloned_cell).setSchemePathId(new_path_id);
			}
			else if (cloned_cell instanceof BlockPortCell)
			{
				BlockPortCell bpc = (BlockPortCell)cloned_cell;
				Identifier p_id = (Identifier)Pool.get("clonedids", bpc.getSchemePortId().identifierString());
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

	public int print(Graphics g, PageFormat pf, int pi)
	{
		if (pi > 0)
			return Printable.NO_SUCH_PAGE;

		getGraph().printAll(g);
		return Printable.PAGE_EXISTS;
	}

	public void setSelectionAttributes(Map map) {
		Object[] cells = DefaultGraphModel.getDescendants(getGraph().getModel(),
				getGraph().getSelectionCells()).toArray();
		map = GraphConstants.cloneMap(map);
		map.remove(GraphConstants.BOUNDS);
		map.remove(GraphConstants.POINTS);
		if (cells != null && cells.length > 0 && !map.isEmpty()) {
			CellView[] views = getGraph().getGraphLayoutCache().getMapping(cells);
			Map viewMap = new HashMap();
			for (int i = 0; i < views.length; i++)
				viewMap.put(views[i], GraphConstants.cloneMap(map));
			getGraph().getGraphLayoutCache().edit(viewMap, null, null, null);
		}
	}

	public void setGraphSize (Dimension d)
	{
		getGraph().setPreferredSize(d);
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
			JComponent source = getGraph();
			e = new ActionEvent(source, e.getID(), e.getActionCommand(), e.getModifiers());
			action.actionPerformed(e);
		}
	}

	class ToolBarPanel extends JPanel
	{
		public final Dimension btn_size = new Dimension(24, 24);
		protected int position = 0;

		public ToolBarPanel ()
		{

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

		protected Map createGraphButtons (UgoPanel p)
		{
			Map bttns = new HashMap();

			if (getGraph().getMarqueeHandler() instanceof SchemeGraph.ShemeMarqueeHandler)
			{
				SchemeGraph.ShemeMarqueeHandler mh = (SchemeGraph.ShemeMarqueeHandler) getGraph().getMarqueeHandler();

				bttns.put(Constants.marqueeTool,
										createToolButton(mh.s, btn_size, null, null,
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pointer.gif")),
										new MarqeeAction(getGraph()), true));
				ButtonGroup group = new ButtonGroup();
				for (Iterator it = bttns.values().iterator(); it.hasNext();)
					group.add((AbstractButton)it.next());
				mh.s.doClick();
			}
			return bttns;
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
				b.setActionCommand(Action.NAME);
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

