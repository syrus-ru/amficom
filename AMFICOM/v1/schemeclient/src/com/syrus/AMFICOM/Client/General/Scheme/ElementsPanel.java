package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.scheme.corba.*;

public class ElementsPanel extends UgoPanel
		implements KeyListener
{
//	public SchemeElement scheme_elemement;
	// Undo Manager
	protected GraphUndoManager undoManager = new GraphUndoManager()
	{
		public void undoableEditHappened(UndoableEditEvent e) {
			super.undoableEditHappened(e);
			((SchemeGraph.ShemeMarqueeHandler)getGraph().getMarqueeHandler()).updateHistoryButtons(this);
			aContext.getDispatcher().notify(new SchemeElementsEvent(this, graph, SchemeElementsEvent.SCHEME_CHANGED_EVENT));
		}
	};

	private static String[] buttons = new String[]
	{
		Constants.marqueeTool,
		Constants.separator,
		Constants.rectangleTool,//
		Constants.ellipseTool,//
		Constants.lineTool,
		Constants.textTool,
		Constants.separator,
		Constants.deviceTool,
		Constants.portOutKey,
		Constants.portInKey,
		Constants.linkTool,
		Constants.separator,
//		Constants.hierarchyUpKey,
//		Constants.separator,
//		Constants.insertIntoLibraryKey,
		Constants.blockPortKey,
		Constants.createTopLevelElementKey,
		Constants.groupKey,
		Constants.ungroupKey,
		Constants.separator,
		Constants.deleteKey,
	//	Constants.undoKey,
	//	Constants.redoKey,
		Constants.separator,
		//Constants.iconTool,//
		//Constants.zoomTool,//
		Constants.zoomInKey,
		Constants.zoomOutKey,
		Constants.zoomActualKey,
	};

	public ElementsPanel(ApplicationContext aContext)
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
	}

	public void init_module()
	{
		super.init_module();
		dispatcher.register(this, SchemeNavigateEvent.type);
	}

	private void jbInit() throws Exception
	{
		GraphModel model = graph.getModel();
		model.addUndoableEditListener(undoManager);

		graph.addKeyListener(this);
		graph.can_be_editable = true;

		toolbar.setVisible(true);

		graph.setGridEnabled(true);
		graph.setGridVisible(true);
		graph.setGridVisibleAtActualSize(true);
		graph.setRequestFocusEnabled(true);
		graph.setBendable(true);
		graph.setEditable(true);
		graph.setEnabled(true);
	}

	protected UgoPanel.ToolBarPanel createToolBar()
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

	protected String[] getButtons()
	{
		return buttons;
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			SchemeNavigateEvent ev = (SchemeNavigateEvent)ae;
			if (ev.SCHEME_PATH_SELECTED)
			{
				SchemePath path = (SchemePath) ( (Object[]) ev.getSource())[0];
				if (path != null)
				{
					getGraph().setSelectionCells(getGraph().getGraphResource().getPathElements(path));
					getGraph().setCurrentPath(path);
				}
			}
			else
			{
				if (SchemeGraph.path_creation_mode != Constants.CREATING_PATH_MODE)
					getGraph().setCurrentPath(null);
			}
			if (SchemeGraph.skip_notify)
				return;

			SchemeGraph.skip_notify = true;
			//graph.removeSelectionCells();

			if (ev.SCHEME_ALL_DESELECTED)
			{
				getGraph().removeSelectionCells();
			}
			else if (ev.SCHEME_ELEMENT_SELECTED)
			{
				SchemeElement element = (SchemeElement)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findSchemeElementById(getGraph(), element.id()));
			}
			else if (ev.SCHEME_PROTO_ELEMENT_SELECTED)
			{
				SchemeProtoElement proto = (SchemeProtoElement)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findProtoElementById(getGraph(), proto.id()));
			}
			else if (ev.SCHEME_PORT_SELECTED)
			{
				SchemePort port = (SchemePort)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findSchemePortById(getGraph(), port.id()));
			}
			else if (ev.SCHEME_CABLE_PORT_SELECTED)
			{
				SchemeCablePort port = (SchemeCablePort)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findSchemeCablePortById(getGraph(), port.id()));
			}
			else if (ev.SCHEME_LINK_SELECTED)
			{
				SchemeLink link = (SchemeLink)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findSchemeLinkById(getGraph(), link.id()));
			}
			else if (ev.SCHEME_CABLE_LINK_SELECTED)
			{
				SchemeCableLink link = (SchemeCableLink)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findSchemeCableLinkById(getGraph(), link.id()));

		/*		String sp = link.source_port_id;
				SchemeElement se = ((SchemePanel)this).scheme.getSchemeElementByCablePort(sp);
				SchemeElement tse = ((SchemePanel)this).scheme.getTopLevelNonSchemeElement(se);

				String tp = link.target_port_id;
				SchemeElement te = ((SchemePanel)this).scheme.getSchemeElementByCablePort(tp);
				SchemeElement tte = ((SchemePanel)this).scheme.getTopLevelNonSchemeElement(te);
				tp = link.target_port_id;*/
			}
			SchemeGraph.skip_notify = false;
		}
		else if (ae.getActionCommand().equals(CreatePathEvent.typ))
		{
			CreatePathEvent cpe = (CreatePathEvent)ae;
			if (cpe.DELETE_PATH)
			{
			}
			if (cpe.EDIT_PATH)
			{
				if (getGraph().getCurrentPath() != null)
					editing_path = getGraph().getCurrentPath().cloneInstance();
			}
		}
		if (ae.getActionCommand().equals(SchemeElementsEvent.type))
		{
			SchemeElementsEvent see = (SchemeElementsEvent)ae;
			if (see.UGO_CREATE)
				return;
			if (see.SCHEME_UGO_CREATE)
				return;
		}
		super.operationPerformed(ae);
	}


	public void openSchemeElement(SchemeElement se)
	{
		getGraph().setScheme(null);
		getGraph().setSchemeElement(se);
//		Map clones = getGraph().copyFromArchivedState(se.schemeCellImpl().getData(), new java.awt.Point(0, 0));
//		getGraph().setGraphChanged(false);
		getGraph().selectionNotify();
		//assignClonedIds(clones);
	}

	protected void setProtoCell(SchemeProtoElement proto, Point p)
	{
		if (proto != null)
		{
			SchemeProtoElement new_proto = proto.cloneInstance();
			insertCell(new_proto.schemeCellImpl().getData(), false, p);
		}
	}

	protected void setSchemeCell(Scheme scheme)
	{
	}


	public void keyReleased(KeyEvent e) { }
	public void keyTyped(KeyEvent e) { }
	public void keyPressed(KeyEvent e)
	{
		// Execute Remove Action on Delete Key Press
		if (e.getKeyCode() == KeyEvent.VK_DELETE)
		{
			if (!getGraph().isSelectionEmpty())
				new DeleteAction(this, aContext).actionPerformed(new ActionEvent(this, 0, ""));
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			GraphActions.alignToGrid(getGraph(), getGraph().getSelectionCells());
		}
		// CTRL + ...
		if (e.getModifiers() == InputEvent.CTRL_MASK)
		{
			if (e.getKeyCode() == KeyEvent.VK_Z)
			{
				if (undoManager.canUndo(getGraph().getGraphLayoutCache()))
					new UndoAction(getGraph(), undoManager).actionPerformed(new ActionEvent(this, 0, ""));
			}
			if (e.getKeyCode() == KeyEvent.VK_SUBTRACT)
			{
				new ZoomOutAction(getGraph()).actionPerformed(new ActionEvent(this, 0, ""));
			}
			if (e.getKeyCode() == KeyEvent.VK_ADD)
			{
				new ZoomInAction(getGraph()).actionPerformed(new ActionEvent(this, 0, ""));
			}
			if (e.getKeyCode() == KeyEvent.VK_MULTIPLY)
			{
				new ZoomActualAction(getGraph()).actionPerformed(new ActionEvent(this, 0, ""));
			}
		}
		// CTRL + SHIFT + ...
		if (e.getModifiers() == InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK)
		{
			if (e.getKeyCode() == KeyEvent.VK_Z)
			{
				if (undoManager.canRedo(getGraph().getGraphLayoutCache()))
					new RedoAction(getGraph(), undoManager).actionPerformed(new ActionEvent(this, 0, ""));
			}
		}
		// CTRL + ALT + ...
		if (e.getModifiers() == InputEvent.CTRL_MASK + InputEvent.ALT_MASK)
		{
			if (e.getKeyCode() == KeyEvent.VK_D)
			{
				getGraph().is_debug = !getGraph().is_debug;
			}
		}
	}

class ToolBarPanel extends UgoPanel.ToolBarPanel
{
	protected Map createGraphButtons (ElementsPanel p)
	{
		Map bttns = new HashMap();

		if (getGraph().getMarqueeHandler() instanceof SchemeGraph.ShemeMarqueeHandler)
		{
			SchemeGraph.ShemeMarqueeHandler mh = (SchemeGraph.ShemeMarqueeHandler) getGraph().getMarqueeHandler();

			bttns.put(Constants.marqueeTool,
									createToolButton(mh.s, btn_size, null, null,
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pointer.gif")),
									new MarqeeAction(getGraph()), true));
			bttns.put("s_cell", mh.s_cell);
			bttns.put(Constants.deviceTool,
									createToolButton(mh.dev, btn_size, null, "устройство",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/device.gif")),
									null, true));
			bttns.put(Constants.rectangleTool,
									createToolButton(mh.r, btn_size, null, "прямоугольник",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/rectangle.gif")),
									null, true));
			bttns.put(Constants.ellipseTool,
									createToolButton(mh.c, btn_size, null, "эллипс",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/ellipse.gif")),
									null, true));
			bttns.put(Constants.textTool,
									createToolButton(mh.t, btn_size, null, "текст",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/text.gif")),
									null, true));
			bttns.put(Constants.iconTool,
									createToolButton(mh.i, btn_size, "иконка", "иконка",
									null, null, true));
			bttns.put(Constants.lineTool,
									createToolButton(mh.l, btn_size, null, "линия",
									 new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/line.gif")),
									null, true));
			bttns.put(Constants.cableTool,
									createToolButton(mh.ce, btn_size, null, "кабель",
									 new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/thick_edge.gif")),
									null, true));
			bttns.put(Constants.linkTool,
									createToolButton(mh.e, btn_size, null, "связь",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/edge.gif")),
									null, true));
			bttns.put(Constants.zoomTool,
									createToolButton(mh.z, btn_size, "зум", "зум",
									null, null, true));
			bttns.put(Constants.portOutKey,
									createToolButton(mh.p1, btn_size, null, "порт",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/port.gif")),
									new PortToolAction(), false));
			bttns.put(Constants.portInKey,
									createToolButton(mh.p2, btn_size, null, "кабельный порт",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/cableport.gif")),
									new PortToolAction(), false));
			bttns.put(Constants.groupKey,
									createToolButton(mh.gr, btn_size, null, "создать компонент",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/group.gif")),
									new GroupAction(getGraph()), false));
			bttns.put(Constants.ungroupKey,
									createToolButton(mh.ugr, btn_size, null, "разобрать",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/ungroup.gif")),
									new UngroupAction(getGraph()), false));
			bttns.put(Constants.undoKey,
									createToolButton(mh.undo, btn_size, null, "отменить",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/undo.gif")),
									new UndoAction(getGraph(), undoManager), false));
			bttns.put(Constants.redoKey,
									createToolButton(mh.redo, btn_size, null, "вернуть",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/redo.gif")),
									new RedoAction(getGraph(), undoManager), false));
			bttns.put(Constants.zoomInKey,
									createToolButton(mh.zi, btn_size, null, "увеличить",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_in.gif")),
									new ZoomInAction(getGraph()), true));
									//new SaveLibraryAction(getGraph(), libPanel), true));
			bttns.put(Constants.zoomOutKey,
									createToolButton(mh.zo, btn_size, null, "уменьшить",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_out.gif")),
									new ZoomOutAction(getGraph()), true));
									//new OpenLibraryAction(getGraph(), libPanel), true));
			bttns.put(Constants.zoomActualKey,
									createToolButton(mh.za, btn_size, null, "фактический размер",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_actual.gif")),
									new ZoomActualAction(getGraph()), true));
			bttns.put(Constants.deleteKey,
									createToolButton(mh.del, btn_size, null, "удалить",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/delete.gif")),
									new DeleteAction(ElementsPanel.this, aContext), false));
			bttns.put(Constants.hierarchyUpKey,
									createToolButton(mh.hup, btn_size, null, "вверх",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/hand.gif")),
									new HierarchyUpAction (getGraph()), true));
//			buttons.put(Constants.insertIntoLibraryKey,
//									createToolButton(mh.addlib, btn_size, null, "сохранить компонент",
//									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/library.gif")),
//									new InsertIntoLibraryAction (getGraph()), false));
			bttns.put(Constants.createTopLevelElementKey,
									createToolButton(mh.ugo, btn_size, null, "создать УГО",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/component_ugo.gif")),
									new CreateTopLevelElementAction (getGraph()), false));
			bttns.put(Constants.blockPortKey,
									createToolButton(mh.bp, btn_size, null, "связной порт",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/hierarchy_port.gif")),
									new CreateBlockPortAction (getGraph()), false));


			ButtonGroup group = new ButtonGroup();
			for (Iterator it = bttns.values().iterator(); it.hasNext();)
			{
				AbstractButton button = (AbstractButton)it.next();
				group.add(button);
			}
			mh.s.doClick();
		}
		return bttns;
	}
}
}

