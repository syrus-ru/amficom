package com.syrus.AMFICOM.Client.General.Scheme;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

import javax.swing.*;

import com.jgraph.graph.DefaultGraphModel;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class SchemePanel extends ElementsPanel
{
	public Hashtable schemes_to_save = new Hashtable();

	protected static String[] buttons = new String[]
	{
		Constants.marqueeTool,
		Constants.separator,
		Constants.rectangleTool,//
		Constants.ellipseTool,//
		Constants.lineTool,
		Constants.textTool,
		Constants.separator,
		Constants.linkTool,
		Constants.cableTool,
		Constants.separator,
		Constants.blockPortKey,
		Constants.createTopLevelSchemeKey,
		Constants.separator,
		Constants.deleteKey,
		//Constants.undoKey,
		//Constants.redoKey,
		Constants.separator,
		Constants.zoomInKey,
		Constants.zoomOutKey,
		Constants.zoomActualKey,
		Constants.separator,
		Constants.backgroundSize
	};


	public SchemePanel(ApplicationContext aContext)
	{
		super(aContext);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		scheme = new Scheme();
	}

	private void jbInit() throws Exception
	{
		graph.setBorderVisible(true);
//		graph.setPortsVisible(true);
		graph.setBendable(false);
		graph.setActualSize(Constants.A4);
		//graph.getSelectionModel().setChildrenSelectable(false);
	}

	protected UgoPanel.ToolBarPanel createToolBar()
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

	public void init_module()
	{
		super.init_module();
	}

	public String getReportTitle()
	{
		return LangModelSchematics.getString("schemeMainTitle");
	}
/*
	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(CreatePathEvent.typ))
		{
			CreatePathEvent cpe = (CreatePathEvent)ae;
			if (cpe.DELETE_PATH)
			{
				SchemePath[] paths = (SchemePath[])cpe.cells;
				for (int j = 0; j < paths.length; j++)
					scheme.paths.remove(paths[j]);
			}
		}
		super.operationPerformed(ae);
	}*/

	public void openScheme(Scheme sch)
	{
		scheme = sch;//(Scheme)sch.clone(aContext.getDataSourceInterface());
		scheme.unpack();
		Map clones = graph.copyFromArchivedState(scheme.serializable_cell, new java.awt.Point(0, 0));
		graph.setGraphChanged(false);
		graph.selectionNotify();
		graph.setActualSize(new Dimension(sch.width == 0 ? 840 : sch.width, sch.height == 0 ? 1190 : sch.height));
		//assignClonedIds(clones);
	}

	public boolean removeScheme(Scheme sch)
	{
		if (scheme != null && sch.getId().equals(scheme.getId()))
		{
			GraphActions.clearGraph(graph);
			return true;
		}
		return false;
	}


	public Scheme updateScheme()
	{
		if (scheme != null)
		{
			scheme.serializable_cell = graph.getArchiveableState(graph.getRoots());
			boolean res = scheme.pack();
			if (!res)
				return null;
		}
		return scheme;
	}

	public void openSchemeElement(SchemeElement se)
	{
		scheme = new Scheme();
		super.openSchemeElement(se);
	}

	protected void setProtoCell (ProtoElement proto, Point p)
	{
		if (proto != null)
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			proto.unpack();

			SchemeElement scheme_el = new SchemeElement(proto, dataSource);
			scheme_el.unpack();

			insertSchemeElement(scheme_el, proto.serializable_cell, proto.serializable_ugo, p);

			repaint();
		}
	}

	protected void setSchemeCell (Scheme sch)
	{
		if (sch != null)
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			sch.unpack();

			insertScheme(sch);

			repaint();
		}
	}

	private void insertSchemeElement (SchemeElement scheme_el, Serializable serializable_cell, Serializable serializable_ugo, Point p)
	{
		if (serializable_ugo != null)
		{
			Map clones = copySchemeElementFromArchivedState (scheme_el, serializable_ugo, p);
			if ((clones == null || clones.size() == 0) && serializable_cell != null)
				copySchemeElementFromArchivedState (scheme_el, serializable_cell, p);
			else if (serializable_cell != null)
				copySchemeElementFromArchivedState_virtual(scheme_el);
		}
		else if (serializable_cell != null)
			copySchemeElementFromArchivedState (scheme_el, serializable_cell, p);
/*
		for (int i = 0; i < scheme_el.element_ids.size(); i++)
		{
			SchemeElement inner = (SchemeElement)Pool.get(SchemeElement.typ, (String)scheme_el.element_ids.get(i));
			inner.unpack();
			copySchemeElementFromArchivedState_virtual(inner, inner.serializable_cell);
			inner.pack();
		}
*/
		scheme_el.pack();
	}

	public Map copySchemeElementFromArchivedState_virtual(SchemeElement element)
	{
	//	if (s instanceof ArrayList)
		{
			DefaultGraphModel model = new DefaultGraphModel();
			SchemeGraph virtual_graph = new SchemeGraph(model, aContext, this);
			Map clones = virtual_graph.copyFromArchivedState(element.serializable_cell, new Point(0, 0));

		//	ArrayList v = (ArrayList) s;
		//	Object[] cells = (Object[]) v.get(0);

		//  Pool.put(SchemeElement.typ, element.getId(), element);

			assignClonedIds(clones.values().toArray());

			element.serializable_cell = virtual_graph.getArchiveableState(virtual_graph.getRoots());
	 //   virtual_graph.removeAll();

			return clones;
		}
		//return null;
	}

	private Map copySchemeElementFromArchivedState(SchemeElement element, Object s, Point p)
	{
		if (s instanceof Vector)
		{
			Map clones = graph.copyFromArchivedState(s, p);

			Vector v = (Vector) s;
			Object[] cells = (Object[]) v.get(0);
			if (cells.length == 1 && cells[0] instanceof DeviceGroup)
			{
				DataSourceInterface dataSource = aContext.getDataSourceInterface();
				// клонируем схемеэлемент
				DeviceGroup group = (DeviceGroup)cells[0];
				ProtoElement proto = (ProtoElement)group.getProtoElement();
				//EquipmentType eqt = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id);

				//SchemeElement element = new SchemeElement(proto, dataSource);
				scheme.elements.add(element);

				element.serializable_cell = proto.serializable_cell;
				element.serializable_ugo = proto.serializable_ugo;
				element.pack();

				DeviceGroup clone = (DeviceGroup)clones.get(cells[0]);
				clone.setSchemeElementId(element.getId());
				Pool.put(SchemeElement.typ, element.getId(), element);

				assignClonedIds(clones.values().toArray());
			}
			return clones;
		}
		return null;
	}

	private void insertScheme (Scheme sch)
	{
		if (sch.serializable_ugo != null && sch.serializable_ugo instanceof Vector)
		{
			Map clones = graph.copyFromArchivedState(sch.serializable_ugo, new Point(0, 0));
			//graph.setFromArchivedState(scheme.serializable_ugo);

			Vector v = (Vector) sch.serializable_ugo;
			Object[] cells = (Object[]) v.get(0);
		if (cells.length == 1 && cells[0] instanceof DeviceGroup)
			{
				DataSourceInterface dataSource = aContext.getDataSourceInterface();

				DeviceGroup group = (DeviceGroup)cells[0];

				SchemeElement element = new SchemeElement(dataSource.GetUId(SchemeElement.typ));
				element.scheme_id = sch.getId();
				element.name = sch.getName();
				element.description = sch.description;
				scheme.elements.add(element);
				element.serializable_cell = sch.serializable_ugo;
				element.pack();

				DeviceGroup clone = (DeviceGroup)clones.get(cells[0]);
				clone.setSchemeElementId(element.getId());
				//((DeviceGroup)cells[0]).setSchemeElementId(element.getId());
				Pool.put(SchemeElement.typ, element.getId(), element);

			//	assignClonedIds(clones);
			}
		}
	}

	public boolean removeAllPathsFromScheme()
	{
		scheme.paths = new Vector();
		for (int i = 0; i < scheme.paths.size(); i++)
		{
			boolean b = removePathFromScheme((SchemePath)scheme.paths.get(i));
			if (!b)
				return false;
		}
		return true;
	}

	public boolean updatePathsAtScheme(Vector paths)
	{
		boolean b = removeAllPathsFromScheme();
		if (!b)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка", "(1)Ошибка обновления путей на схеме " +
																		scheme.getName(), JOptionPane.OK_OPTION);
			return false;
		}
		for (int i = 0; i < paths.size(); i++)
		{
			b = insertPathToScheme((SchemePath)paths.get(i));
			if (!b)
			{
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка", "(2)Ошибка обновления путей на схеме " +
																		scheme.getName(), JOptionPane.OK_OPTION);
				return false;
			}
		}
		return true;
	}

	public boolean removePathFromScheme(SchemePath path)
	{
		scheme.paths.remove(path);

		Enumeration s_to_save;
		Enumeration se_to_save;

		Hashtable lp = new Hashtable();
		for (int i = 0; i < path.links.size(); i++)
		{
			PathElement pe = (PathElement)path.links.get(i);
			lp.put(pe.link_id, "");
		}

		//first of all find internal schemes to insert path
		for (se_to_save = findSchemeElementsToInsertPath (scheme, lp).elements(); se_to_save.hasMoreElements();)
		{
			SchemeElement se = (SchemeElement)se_to_save.nextElement();
			insertPathToSchemeElement (se, lp);
		}

		for (s_to_save = findSchemeToInsertPath (scheme, lp).elements(); s_to_save.hasMoreElements();)
		{
			Scheme s = (Scheme)s_to_save.nextElement();
//			if (!s.equals(scheme))
			boolean b = insertPathToScheme(s, lp);
			if (!b)
				return false;
			//aContext.getDataSourceInterface().SaveScheme(s.getId());
		}

		for (Enumeration e = s_to_save; e.hasMoreElements();)
		{
			Scheme s = (Scheme)e.nextElement();
			schemes_to_save.put(s.getId(), s);
		}
		return true;
	}

	protected void insertPathToSchemeElement(SchemeElement se, Hashtable lp)
	{
		ApplicationContext ac = new ApplicationContext();
		ac.setDispatcher(new Dispatcher());
		SchemePanel virtual_panel = new SchemePanel(ac);
		virtual_panel.openSchemeElement(se);

		SchemeGraph g = virtual_panel.getGraph();
		Object[] cells = g.getAll();
		for (int i = 0; i < cells.length; i++)
		{
			;
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

		virtual_panel.updateSchemeElement();
	}

	protected boolean insertPathToScheme(Scheme sc, Hashtable lp)
	{
		ApplicationContext ac = new ApplicationContext();
		ac.setDispatcher(new Dispatcher());

		SchemeGraph g;
		SchemePanel virtual_panel;
		if (sc.getId().equals(scheme.getId()))
			virtual_panel = this;
		else
		{
			virtual_panel = new SchemePanel(ac);
			virtual_panel.openScheme(sc);
		}
		g = virtual_panel.getGraph();

		Object[] cells = g.getAll();
		for (int i = 0; i < cells.length; i++)
		{
			;
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
		sc = virtual_panel.updateScheme();
		if (sc == null)
			return false;

		return true;
	}

	public boolean insertPathToScheme(SchemePath path)
	{
		if (editing_path != null && editing_path.getId().equals(path.getId()))
		{
			boolean b = removePathFromScheme(editing_path);
			editing_path = null;
		}

		if (!scheme.paths.contains(path))
			scheme.paths.add(path);

		Hashtable lp = new Hashtable();
		for (int i = 0; i < path.links.size(); i++)
		{
			PathElement pe = (PathElement)path.links.get(i);
			lp.put(pe.link_id, path.getId());
		}

		//first of all find internal schemes to insert path
		for (Enumeration e = findSchemeElementsToInsertPath (scheme, lp).elements(); e.hasMoreElements();)
		{
			SchemeElement se = (SchemeElement)e.nextElement();
			insertPathToSchemeElement (se, lp);
		}

		for (Enumeration e = findSchemeToInsertPath (scheme, lp).elements(); e.hasMoreElements();)
		{
			Scheme s = (Scheme)e.nextElement();
//			if (!s.equals(scheme))
			boolean b = insertPathToScheme(s, lp);
			if (!b)
				return false;
//			aContext.getDataSourceInterface().SaveScheme(s.getId());
			schemes_to_save.put(s.getId(), s);
		}
		return true;
	}

	protected Hashtable findSchemeToInsertPath (Scheme sc, Hashtable lp)
	{
		Hashtable schemes_to_save = new Hashtable();
		for (int i = 0; i < sc.links.size(); i++)
		{
			String p_id = (String)lp.get(((SchemeLink)sc.links.get(i)).getId());
			if (p_id != null)
				schemes_to_save.put(sc.getId(), sc);
		}
		for (int i = 0; i < sc.cablelinks.size(); i++)
		{
			String p_id = (String)lp.get(((SchemeCableLink)sc.cablelinks.get(i)).getId());
			if (p_id != null)
				schemes_to_save.put(sc.getId(), sc);
		}

		for (int i = 0; i < sc.elements.size(); i++)
		{
			SchemeElement se = (SchemeElement)sc.elements.get(i);
			if (!se.scheme_id.equals(""))
			{
				Scheme inner = (Scheme)Pool.get(Scheme.typ, se.scheme_id);
				for (Enumeration e = findSchemeToInsertPath (inner, lp).elements(); e.hasMoreElements();)
				{
					Scheme s = (Scheme)e.nextElement();
					schemes_to_save.put(s.getId(), s);
				}
			}
		}
		return schemes_to_save;
	}

	protected Hashtable findSchemeElementsToInsertPath (Scheme sc, Hashtable lp)
	{
		Hashtable schemeelements_to_save = new Hashtable();

		for (int i = 0; i < sc.elements.size(); i++)
		{
			SchemeElement se = (SchemeElement)sc.elements.get(i);
			if (!se.scheme_id.equals(""))
			{
				Scheme inner = (Scheme)Pool.get(Scheme.typ, se.scheme_id);
				for (Enumeration e = findSchemeElementsToInsertPath (inner, lp).elements(); e.hasMoreElements();)
				{
					SchemeElement s = (SchemeElement)e.nextElement();
					schemeelements_to_save.put(s.getId(), s);
				}
			}
			else
			{
				for (int j = 0; j < se.links.size(); j++)
				{
					String p_id = (String)lp.get(((SchemeLink)se.links.get(j)).getId());
					if (p_id != null)
						schemeelements_to_save.put(se.getId(), se);
				}

				for (Iterator it = se.getAllChilds(); it.hasNext();)
				{
					SchemeElement child = (SchemeElement)it.next();
					for (int j = 0; j < child.links.size(); j++)
					{
						String p_id = (String)lp.get(((SchemeLink)child.links.get(j)).getId());
						if (p_id != null)
							schemeelements_to_save.put(child.getId(), child);
					}
				}
			}
		}
		return schemeelements_to_save;
	}

	public SchemePath getCurrentPath()
	{
		return graph.currentPath;
	}

	public boolean removeCurrentPathFromScheme()
	{
		if (graph.currentPath != null)
		{
			boolean b = removePathFromScheme(graph.currentPath);
			if (!b)
				return false;
			graph.setSelectionCells(new Object[0]);
		}
		return true;
	}

	protected String[] getButtons()
	{
		return buttons;
	}

	class ToolBarPanel extends ElementsPanel.ToolBarPanel
	{
		public ToolBarPanel (ElementsPanel panel)
		{
			super(panel);
		}

		protected Hashtable createGraphButtons (ElementsPanel p)
		{
			Hashtable buttons = super.createGraphButtons(p);

			if (graph.getMarqueeHandler() instanceof SchemeGraph.ShemeMarqueeHandler)
			{
				SchemeGraph.ShemeMarqueeHandler mh = (SchemeGraph.ShemeMarqueeHandler) graph.getMarqueeHandler();

				buttons.put(Constants.createTopLevelSchemeKey,
										createToolButton(mh.scheme_ugo, btn_size, null, "УГО схемы",
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/sheme_ugo.gif")),
										new CreateTopLevelSchemeAction (graph, SchemePanel.this), true));

				buttons.put(Constants.backgroundSize,
										createToolButton(mh.bSize, btn_size, null, "размер схемы",
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/sheme_size.gif")),
										new SetBackgroundSizeAction (SchemePanel.this), true));

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
	}
}


