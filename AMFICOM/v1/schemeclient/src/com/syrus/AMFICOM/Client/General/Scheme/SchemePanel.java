package com.syrus.AMFICOM.Client.General.Scheme;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.jgraph.graph.DefaultGraphModel;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class SchemePanel extends ElementsPanel
{
	public HashSet schemes_to_save = new HashSet();

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
		Constants.backgroundSize,
		Constants.separator,
		Constants.linkMode,
		Constants.pathMode
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
		dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	public String getReportTitle()
	{
		return LangModelSchematics.getString("schemeMainTitle");
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			TreeDataSelectionEvent ev = (TreeDataSelectionEvent)ae;
			Class cl = ev.getDataClass();
			java.util.List ds = ev.getList();
			if (ev.getSelectionNumber() != -1)
			{
				if (cl.equals(SchemePath.class))
				{
					SchemePath path = (SchemePath)ds.get(ev.getSelectionNumber());
//					if (scheme.paths.contains(path))
					{
						Object[] cells = graph.getPathElements(path);
						graph.skip_notify = true;
						graph.setSelectionCells(cells);
						graph.skip_notify = false;
					}
				}
				else if (cl.equals(SchemeElement.class))
				{
					SchemeElement se = (SchemeElement)ds.get(ev.getSelectionNumber());
					if (scheme.elements.contains(se))
					{
						Object cell = SchemeActions.findSchemeElementById(graph, se.getId());
						graph.skip_notify = true;
						graph.setSelectionCell(cell);
						graph.skip_notify = false;
					}
				}
				else if (cl.equals(SchemeLink.class))
				{
					SchemeLink l = (SchemeLink)ds.get(ev.getSelectionNumber());
					if (scheme.links.contains(l))
					{
						Object cell = SchemeActions.findSchemeLinkById(graph, l.getId());
						graph.skip_notify = true;
						graph.setSelectionCell(cell);
						graph.skip_notify = false;
					}
				}
				else if (cl.equals(SchemeCableLink.class))
				{
					SchemeCableLink l = (SchemeCableLink)ds.get(ev.getSelectionNumber());
					if (scheme.cablelinks.contains(l))
					{
						Object cell = SchemeActions.findSchemeCableLinkById(graph, l.getId());
						graph.skip_notify = true;
						graph.setSelectionCell(cell);
						graph.skip_notify = false;
					}
				}

			}
		}
		super.operationPerformed(ae);
	}

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
		if (scheme != null && sch != null && sch.getId().equals(scheme.getId()))
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
		if (s instanceof List)
		{
			Map clones = graph.copyFromArchivedState(s, p);

			List v = (List) s;
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
		if (sch.serializable_ugo != null && sch.serializable_ugo instanceof List)
		{
			Map clones = graph.copyFromArchivedState(sch.serializable_ugo, new Point(0, 0));
			//graph.setFromArchivedState(scheme.serializable_ugo);
			List v = (List) sch.serializable_ugo;
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
		scheme.paths = new ArrayList();
//		for (Iterator it = scheme.paths.iterator(); it.hasNext();)
//		{
//			boolean b = removePathFromScheme((SchemePath)it.next());
//			if (!b)
//				return false;
//		}
		return true;
	}

	public boolean updatePathsAtScheme(Collection paths)
	{
		boolean b = removeAllPathsFromScheme();
		if (!b)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка", "(1)Ошибка обновления путей на схеме " +
																		scheme.getName(), JOptionPane.OK_OPTION);
			return false;
		}
		for (Iterator it = paths.iterator(); it.hasNext();)
		{
			b = insertPathToScheme((SchemePath)it.next());
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

/*		Iterator s_to_save;
		Iterator se_to_save;

		Hashtable lp = new Hashtable();
		for (Iterator it = path.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			lp.put(pe.link_id, "");
		}

		//first of all find internal schemes to insert path
		for (se_to_save = findSchemeElementsToInsertPath (scheme, lp).iterator(); se_to_save.hasNext();)
		{
			SchemeElement se = (SchemeElement)se_to_save.next();
			insertPathToSchemeElement (se, lp);
		}

		for (s_to_save = findSchemeToInsertPath (scheme, lp).iterator(); s_to_save.hasNext();)
		{
			Scheme s = (Scheme)s_to_save.next();
//			if (!s.equals(scheme))
			boolean b = insertPathToScheme(s, lp);
			if (!b)
				return false;
			//aContext.getDataSourceInterface().SaveScheme(s.getId());
		}

		for (Iterator it = s_to_save; it.hasNext();)
		{
			Scheme s = (Scheme)it.next();
			schemes_to_save.add(s);
		}*/
		return true;
	}
/*
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
*/
	/*
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
	}*/

	public boolean insertPathToScheme(SchemePath path)
	{
		if (editing_path != null && editing_path.getId().equals(path.getId()))
		{
			boolean b = removePathFromScheme(editing_path);
			editing_path = null;
		}

		if (!scheme.paths.contains(path))
			scheme.paths.add(path);

	/*	Hashtable lp = new Hashtable();
		for (Iterator it = path.links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			lp.put(pe.link_id, path.getId());
		}

		//first of all find internal schemes to insert path
		for (Iterator it = findSchemeElementsToInsertPath (scheme, lp).iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement)it.next();
			insertPathToSchemeElement (se, lp);
		}

		for (Iterator it = findSchemeToInsertPath (scheme, lp).iterator(); it.hasNext();)
		{
			Scheme s = (Scheme)it.next();
//			if (!s.equals(scheme))
			boolean b = insertPathToScheme(s, lp);
			if (!b)
				return false;
//			aContext.getDataSourceInterface().SaveScheme(s.getId());
			schemes_to_save.add(s);
		}*/
		return true;
	}
/*
	protected Collection findSchemeToInsertPath (Scheme sc, Hashtable lp)
	{
		HashSet schemes_to_save = new HashSet();
		for (int i = 0; i < sc.links.size(); i++)
		{
			String p_id = (String)lp.get(((SchemeLink)sc.links.get(i)).getId());
			if (p_id != null)
				schemes_to_save.add(sc);
		}
		for (int i = 0; i < sc.cablelinks.size(); i++)
		{
			String p_id = (String)lp.get(((SchemeCableLink)sc.cablelinks.get(i)).getId());
			if (p_id != null)
				schemes_to_save.add(sc);
		}

		for (int i = 0; i < sc.elements.size(); i++)
		{
			SchemeElement se = (SchemeElement)sc.elements.get(i);
			if (!se.scheme_id.equals(""))
			{
				Scheme inner = (Scheme)Pool.get(Scheme.typ, se.scheme_id);
				for (Iterator it = findSchemeToInsertPath (inner, lp).iterator(); it.hasNext();)
				{
					Scheme s = (Scheme)it.next();
					schemes_to_save.add(s);
				}
			}
		}
		return schemes_to_save;
	}
*/
/*	protected Collection findSchemeElementsToInsertPath (Scheme sc, Hashtable lp)
	{
		HashSet schemeelements_to_save = new HashSet();

		for (int i = 0; i < sc.elements.size(); i++)
		{
			SchemeElement se = (SchemeElement)sc.elements.get(i);
			if (!se.scheme_id.equals(""))
			{
				Scheme inner = (Scheme)Pool.get(Scheme.typ, se.scheme_id);
				for (Iterator it = findSchemeElementsToInsertPath (inner, lp).iterator(); it.hasNext();)
				{
					SchemeElement s = (SchemeElement)it.next();
					schemeelements_to_save.add(s);
				}
			}
			else
			{
				for (int j = 0; j < se.links.size(); j++)
				{
					String p_id = (String)lp.get(((SchemeLink)se.links.get(j)).getId());
					if (p_id != null)
						schemeelements_to_save.add(se);
				}

				for (Iterator it = se.getAllChilds().iterator(); it.hasNext();)
				{
					SchemeElement child = (SchemeElement)it.next();
					for (int j = 0; j < child.links.size(); j++)
					{
						String p_id = (String)lp.get(((SchemeLink)child.links.get(j)).getId());
						if (p_id != null)
							schemeelements_to_save.add(child);
					}
				}
			}
		}
		return schemeelements_to_save;
	}*/

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

				buttons.put(Constants.linkMode,
										createToolButton(mh.linkButt, btn_size, null, "режим линий",
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/linkmode.gif")),
										new SetLinkModeAction (SchemePanel.this), true));
				buttons.put(Constants.pathMode,
										createToolButton(mh.pathButt, btn_size, null, "режим путей",
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pathmode.gif")),
										new SetPathModeAction (SchemePanel.this), true));


				ButtonGroup group = new ButtonGroup();
				group.add(mh.linkButt);
				group.add(mh.pathButt);
				mh.linkButt.doClick();
//				for (Enumeration enum = buttons.elements(); enum.hasMoreElements();)
//				{
//					AbstractButton button = (AbstractButton)enum.nextElement();
//					group.add(button);
//				}
				mh.s.doClick();
			}
			return buttons;
		}
	}
}


