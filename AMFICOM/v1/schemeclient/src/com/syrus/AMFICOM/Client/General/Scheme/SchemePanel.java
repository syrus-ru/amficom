package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jgraph.graph.DefaultGraphModel;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.scheme.corba.*;

public class SchemePanel extends ElementsPanel
{
	protected static String[] buttons = new String[]
	{
		Constants.marqueeTool,
		Constants.separator,
		Constants.rectangleTool,//
		Constants.ellipseTool,//
		Constants.lineTool,
		Constants.textTool,
		Constants.separator,
//		Constants.deviceTool,
		Constants.portOutKey,
		Constants.portInKey,
		Constants.linkTool,
		Constants.cableTool,
		Constants.separator,
		Constants.blockPortKey,
		Constants.createTopLevelElementKey,
		Constants.groupSEKey,
		Constants.ungroupKey,
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
		Constants.LINK_MODE,
		Constants.PATH_MODE,
		Constants.separator,
		Constants.TOP_LEVEL_SCHEME_MODE
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
//		scheme = new Scheme();
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
		ToolBarPanel toolbar = new ToolBarPanel();
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
						Object[] cells = getGraph().getGraphResource().getPathElements(path);
						SchemeGraph.skip_notify = true;
						getGraph().setSelectionCells(cells);
						SchemeGraph.skip_notify = false;
					}
				}
				else if (cl.equals(SchemeElement.class))
				{
					SchemeElement se = (SchemeElement)ds.get(ev.getSelectionNumber());
					if (Arrays.asList(getGraph().getScheme().schemeElements()).contains(se))
					{
						Object cell = SchemeActions.findSchemeElementById(getGraph(), se.id());
						SchemeGraph.skip_notify = true;
						getGraph().setSelectionCell(cell);
						SchemeGraph.skip_notify = false;
					}
				}
				else if (cl.equals(SchemeLink.class))
				{
					SchemeLink l = (SchemeLink)ds.get(ev.getSelectionNumber());
					if (Arrays.asList(getGraph().getScheme().schemeLinks()).contains(l))
					{
						Object cell = SchemeActions.findSchemeLinkById(getGraph(), l.id());
						SchemeGraph.skip_notify = true;
						getGraph().setSelectionCell(cell);
						SchemeGraph.skip_notify = false;
					}
				}
				else if (cl.equals(SchemeCableLink.class))
				{
					SchemeCableLink l = (SchemeCableLink)ds.get(ev.getSelectionNumber());
					if (Arrays.asList(getGraph().getScheme().schemeCableLinks()).contains(l))
					{
						Object cell = SchemeActions.findSchemeCableLinkById(getGraph(), l.id());
						SchemeGraph.skip_notify = true;
						getGraph().setSelectionCell(cell);
						SchemeGraph.skip_notify = false;
					}
				}

			}
		}
		super.operationPerformed(ae);
	}

	void insertSchemeElement (SchemeElement scheme_el, List serializable_cell, List serializable_ugo, Point p)
	{
		if (serializable_ugo != null)
		{
			Map clones = copySchemeElementFromArchivedState (scheme_el, serializable_ugo, p);
			if ((clones == null || clones.size() == 0) && serializable_cell != null)
				copySchemeElementFromArchivedState (scheme_el, serializable_cell, p);
			else if (serializable_cell != null)
				copySchemeElementFromArchivedState_virtual(serializable_cell);
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
	}

	public static Map copySchemeElementFromArchivedState_virtual(List serializable)
	{
		DefaultGraphModel model = new DefaultGraphModel();
		SchemeGraph virtual_graph = new SchemeGraph(model, new ApplicationContext());
		Map clones = virtual_graph.copyFromArchivedState(serializable, new Point(0, 0));
		assignClonedIds(clones.values().toArray());
		serializable = (List)virtual_graph.getArchiveableState(virtual_graph.getRoots());
		return clones;
	}

	private Map copySchemeElementFromArchivedState(SchemeElement element, Object s, Point p)
	{
		if (s instanceof List)
		{
			Map clones = getGraph().copyFromArchivedState(s, p);

			List v = (List) s;
			Object[] cells = (Object[]) v.get(0);
			if (cells.length == 1 && cells[0] instanceof DeviceGroup)
			{
				// клонируем схемеэлемент
				DeviceGroup group = (DeviceGroup)cells[0];
				SchemeProtoElement proto = (SchemeProtoElement)group.getProtoElement();
				//EquipmentType eqt = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id);

				//SchemeElement element = new SchemeElement(proto, dataSource);
				Arrays.asList(getGraph().getScheme().schemeElements()).add(element);

				element.schemeCell(proto.schemeCell());
				element.ugoCell(proto.ugoCell());

				DeviceGroup clone = (DeviceGroup)clones.get(cells[0]);
				clone.setSchemeElementId(element.id());

				assignClonedIds(clones.values().toArray());
			}
			return clones;
		}
		return null;
	}

	public void openScheme(Scheme sch)
	{
		SchemeGraph graph = getGraph();
		graph.setScheme(sch); //(Scheme)sch.clone(aContext.getDataSourceInterface());
//		sch.schemeCell(null);
//		sch.ugoCell(null);
//		sch.unpack();
		Map clones = graph.copyFromArchivedState(sch.schemeCellImpl().getData(), new Point(0, 0));
//		graph.setGraphChanged(false);
		graph.selectionNotify();
		graph.setActualSize(new Dimension(sch.width() == 0 ? 840 : sch.width(),
				sch.height() == 0 ? 1190 : sch.height()));
		//assignClonedIds(clones);
	}

	protected void setProtoCell(SchemeProtoElement proto, Point p)
	{
		if (proto != null)
		{
			SchemeElement scheme_el = SchemeFactory.createSchemeElement();

			if (getGraph().getScheme() != null)
				scheme_el.scheme(getGraph().getScheme());
			else if (getGraph().getSchemeElement() != null)
				scheme_el.scheme(getGraph().getSchemeElement().scheme());

			insertSchemeElement(scheme_el, proto.schemeCellImpl().getData(), proto.ugoCellImpl().getData(), p);

			repaint();
		}
	}

	public Scheme updateScheme()
	{
		SchemeGraph graph = getGraph();
		Scheme scheme = graph.getScheme();
		if (scheme != null)
			scheme.schemeCellImpl().setData((List)graph.getArchiveableState(graph.getRoots()));
		return scheme;
	}


	protected void setSchemeCell(Scheme sch)
	{
		if (sch != null)
			insertScheme(sch);
	}


	void insertScheme (Scheme sch)
	{
		if (sch.ugoCell() != null)
		{
			Map clones = getGraph().copyFromArchivedState(sch.ugoCellImpl().getData(), new Point(0, 0));
			//graph.setFromArchivedState(scheme.serializable_ugo);
			List v = sch.ugoCellImpl().getData();
			Object[] cells = (Object[]) v.get(0);

			if (cells.length == 1 && cells[0] instanceof DeviceGroup)
			{
				DeviceGroup group = (DeviceGroup)cells[0];

				SchemeElement element = SchemeFactory.createSchemeElement();
				element.internalScheme(sch);
				element.scheme(getGraph().getScheme());
				element.name(sch.name());
				element.description(sch.description());
				Arrays.asList(getGraph().getScheme().schemeElements()).add(element);
				element.schemeCell(sch.ugoCell());

				DeviceGroup clone = (DeviceGroup)clones.get(cells[0]);
				clone.setSchemeElementId(element.id());
				//((DeviceGroup)cells[0]).setSchemeElementId(element.getId());

//				Pool.put(SchemeElement.typ, element.getId(), element);

			//	assignClonedIds(clones);
			}
		}
	}

	public void removeAllPathsFromScheme()
	{
		getGraph().getScheme().schemeMonitoringSolution().schemePaths(new SchemePath[0]);
	}

	public boolean updatePathsAtScheme(Collection paths)
	{
		removeAllPathsFromScheme();
		for (Iterator it = paths.iterator(); it.hasNext();)
			insertPathToScheme((SchemePath)it.next());
		return true;
	}

	public void removePathFromScheme(SchemePath path)
	{
		Arrays.asList(getGraph().getScheme().schemeMonitoringSolution().schemePaths()).remove(path);
	}

	public void insertPathToScheme(SchemePath path)
	{
		List paths = Arrays.asList(getGraph().getScheme().schemeMonitoringSolution().schemePaths());
		if (!paths.contains(path))
			paths.add(path);
		editing_path = null;
	}

	public void removeCurrentPathFromScheme()
	{
		if (getGraph().getCurrentPath() != null)
		{
			removePathFromScheme(getGraph().getCurrentPath());
			getGraph().setSelectionCells(new Object[0]);
		}
	}

	protected String[] getButtons()
	{
		return buttons;
	}

	class ToolBarPanel extends ElementsPanel.ToolBarPanel
	{
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);

		public ToolBarPanel ()
		{
			super();

			tabs = new JTabbedPane(JTabbedPane.TOP);
			tabs.addMouseListener(new MouseAdapter()
			{
				public void mouseReleased(MouseEvent e)
				{
					if (SwingUtilities.isRightMouseButton(e) && tabs.getTabCount() > 1)
					{
						JPopupMenu popup = new JPopupMenu();
						JMenuItem close = new JMenuItem(new AbstractAction()
						{
							public void actionPerformed(ActionEvent ae)
							{
								tabs.removeTabAt(tabs.getSelectedIndex());
							}
						});
						close.setText("Закрыть \"" + tabs.getTitleAt(tabs.getSelectedIndex()) + "\"");
						popup.add(close);
						popup.show(tabs, e.getX(), e.getY());
					}
				}
			});
		}


		protected Map createGraphButtons (ElementsPanel p)
		{
			Map buttons = super.createGraphButtons(p);

			if (getGraph().getMarqueeHandler() instanceof SchemeGraph.ShemeMarqueeHandler)
			{
				SchemeGraph.ShemeMarqueeHandler mh = (SchemeGraph.ShemeMarqueeHandler) getGraph().getMarqueeHandler();

				buttons.put(Constants.groupSEKey,
										createToolButton(mh.gr2, btn_size, null, "создать компонент",
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/group.gif")),
										new GroupSEAction(getGraph()), false));

				buttons.put(Constants.createTopLevelSchemeKey,
										createToolButton(mh.scheme_ugo, btn_size, null, "УГО схемы",
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/sheme_ugo.gif")),
										new CreateTopLevelSchemeAction (getGraph(), SchemePanel.this), true));
				buttons.put(Constants.backgroundSize,
										createToolButton(mh.bSize, btn_size, null, "размер схемы",
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/sheme_size.gif")),
										new SetBackgroundSizeAction(aContext, SchemePanel.this), true));

				buttons.put(Constants.LINK_MODE,
										createToolButton(mh.linkButt, btn_size, null, "режим линий",
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/linkmode.gif")),
										new SetLinkModeAction (SchemePanel.this), true));
				buttons.put(Constants.PATH_MODE,
										createToolButton(mh.pathButt, btn_size, null, "режим путей",
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pathmode.gif")),
										new SetPathModeAction (SchemePanel.this), true));

				buttons.put(Constants.TOP_LEVEL_SCHEME_MODE,
										createToolButton(mh.topModeButt, btn_size, null, "режим схематичного изображения",
										new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")),
										new SetTopLevelModeAction(SchemePanel.this), true));

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
