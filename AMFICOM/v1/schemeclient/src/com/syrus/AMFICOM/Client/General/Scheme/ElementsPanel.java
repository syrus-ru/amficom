package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.Network.Port;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

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

	protected static String[] buttons = new String[]
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
		dispatcher.register(this, CatalogNavigateEvent.type);
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

	protected String[] getButtons()
	{
		return buttons;
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(CatalogNavigateEvent.type))
		{
			if (SchemeGraph.skip_notify)
				return;
			SchemeGraph.skip_notify = true;
			CatalogNavigateEvent ev = (CatalogNavigateEvent)ae;
			//graph.removeSelectionCells();
			if (ev.CATALOG_EQUIPMENT_SELECTED)
			{
				Equipment eq = (Equipment)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findEquipmentById(getGraph(), eq.getId()));
			}
			else if (ev.CATALOG_ACCESS_PORT_SELECTED)
			{
				AccessPort aport = (AccessPort)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findAccessPortById(getGraph(), aport.getId()));
			}
			else if (ev.CATALOG_PORT_SELECTED)
			{
				Port port = (Port)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findPortById(getGraph(), port.getId()));
			}
			else if (ev.CATALOG_CABLE_PORT_SELECTED)
			{
				CablePort port = (CablePort)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findCablePortById(getGraph(), port.getId()));
			}
			else if (ev.CATALOG_LINK_SELECTED)
			{
				Link link = (Link)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findLinkById(getGraph(), link.getId()));
			}
			else if (ev.CATALOG_CABLE_LINK_SELECTED)
			{
				CableLink link = (CableLink)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findCableLinkById(getGraph(), link.getId()));
			}
			else if (ev.CATALOG_PATH_SELECTED)
			{
				TransmissionPath path = (TransmissionPath)((Object[])ev.getSource())[0];
				getGraph().setSelectionCells(getGraph().getGraphResource().getPathElements(path));
			}
			SchemeGraph.skip_notify = false;
		}
		else if (ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			SchemeNavigateEvent ev = (SchemeNavigateEvent)ae;
			if (ev.SCHEME_PATH_SELECTED)
			{
				SchemePath path = (SchemePath) ( (Object[]) ev.getSource())[0];
				if (path != null)
				{
					getGraph().setSelectionCells(getGraph().getGraphResource().getPathElements(path));
					getGraph().getGraphResource().currentPath = path;
				}
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
				getGraph().setSelectionCell(SchemeActions.findSchemeElementById(getGraph(), element.getId()));
			}
			else if (ev.SCHEME_PROTO_ELEMENT_SELECTED)
			{
				ProtoElement proto = (ProtoElement)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findProtoElementById(getGraph(), proto.getId()));
			}
			else if (ev.SCHEME_PORT_SELECTED)
			{
				SchemePort port = (SchemePort)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findSchemePortById(getGraph(), port.getId()));
			}
			else if (ev.SCHEME_CABLE_PORT_SELECTED)
			{
				SchemeCablePort port = (SchemeCablePort)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findSchemeCablePortById(getGraph(), port.getId()));
			}
			else if (ev.SCHEME_LINK_SELECTED)
			{
				SchemeLink link = (SchemeLink)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findSchemeLinkById(getGraph(), link.getId()));
			}
			else if (ev.SCHEME_CABLE_LINK_SELECTED)
			{
				SchemeCableLink link = (SchemeCableLink)((Object[])ev.getSource())[0];
				getGraph().setSelectionCell(SchemeActions.findSchemeCableLinkById(getGraph(), link.getId()));

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
				if (getGraph().getGraphResource().currentPath != null)
					editing_path = new SchemePath(getGraph().getGraphResource().currentPath);
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
		se.unpack();
		Map clones = getGraph().copyFromArchivedState(se.serializable_cell, new java.awt.Point(0, 0));
//		getGraph().setGraphChanged(false);
		getGraph().selectionNotify();
		//assignClonedIds(clones);
	}

	protected void setProtoCell(ProtoElement proto, Point p)
	{
		if (proto != null)
		{
			proto.unpack();

			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			ProtoElement new_proto = (ProtoElement) proto.clone(dataSource);

			insertCell(new_proto.serializable_cell, false, p);
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
		if (e.getModifiers() == KeyEvent.CTRL_MASK)
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
		if (e.getModifiers() == KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK)
		{
			if (e.getKeyCode() == KeyEvent.VK_Z)
			{
				if (undoManager.canRedo(getGraph().getGraphLayoutCache()))
					new RedoAction(getGraph(), undoManager).actionPerformed(new ActionEvent(this, 0, ""));
			}
		}
		// CTRL + ALT + ...
		if (e.getModifiers() == KeyEvent.CTRL_MASK + KeyEvent.ALT_MASK)
		{
			if (e.getKeyCode() == KeyEvent.VK_D)
			{
				getGraph().is_debug = !getGraph().is_debug;
			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				Object[] cells = getGraph().getSelectionCells();
				if (cells.length == 1)
				{
					if (cells[0] instanceof CablePortCell)
					{
						String id = JOptionPane.showInputDialog("Новый идентификатор кабельного порта:",
								((CablePortCell)cells[0]).getSchemeCablePortId());
						if (id != null && !id.equals(""))
							((CablePortCell)cells[0]).setSchemeCablePortId(id);
					}
					if (cells[0] instanceof PortCell)
					{
						String id = JOptionPane.showInputDialog("Новый идентификатор порта:",
								((PortCell)cells[0]).getSchemePortId());
						if (id != null && !id.equals(""))
							((PortCell)cells[0]).setSchemePortId(id);
					}
					if (cells[0] instanceof DefaultLink)
					{
						String id = JOptionPane.showInputDialog("Новый идентификатор линка:",
								((DefaultLink)cells[0]).getSchemeLinkId());
						if (id != null && !id.equals(""))
						{
							if (this instanceof SchemePanel)
							{
								int ret = JOptionPane.showConfirmDialog(null, "Заменить старый линк новым?");
								if (ret == JOptionPane.YES_OPTION)
								{
									Scheme scheme = getGraph().getScheme();
									for (Iterator it = scheme.links.iterator(); it.hasNext();)
									{
										SchemeLink sl = (SchemeLink)it.next();
										if (sl.getId().equals(((DefaultLink)cells[0]).getSchemeLinkId()))
										{
											scheme.links.remove(sl);
											break;
										}
									}
									boolean contains_new = false;
									for (Iterator it = scheme.links.iterator(); it.hasNext();)
									{
										SchemeLink sl = (SchemeLink)it.next();
										if (sl.getId().equals(id))
										{
											contains_new = true;
											break;
										}
									}
									if (!contains_new)
									{
										SchemeLink sl = (SchemeLink)Pool.get(SchemeLink.typ, id);
										if (sl == null)
											JOptionPane.showMessageDialog(null, "Схемный линк не найден " + id);
										else
											scheme.links.add(sl);
									}
								}
							}
							((DefaultLink)cells[0]).setSchemeLinkId(id);
						}
						/*id = JOptionPane.showInputDialog("Новый идентификатор пути:",
								((DefaultLink)cells[0]).getSchemePathId());
						if (id != null && !id.equals(""))
							((DefaultLink)cells[0]).setSchemePathId(id);*/
					}
					if (cells[0] instanceof DefaultCableLink)
					{
						String id = JOptionPane.showInputDialog("Новый идентификатор кабельного линка:",
								((DefaultCableLink)cells[0]).getSchemeCableLinkId());
						if (id != null && !id.equals(""))
						{
							if (this instanceof SchemePanel)
							{
								int ret = JOptionPane.showConfirmDialog(null, "Заменить старый кабельный линк новым?");
								if (ret == JOptionPane.YES_OPTION)
								{
									Scheme scheme = getGraph().getScheme();
									for (Iterator it = scheme.cablelinks.iterator(); it.hasNext();)
									{
										SchemeCableLink sl = (SchemeCableLink)it.next();
										if (sl.getId().equals(((DefaultCableLink)cells[0]).getSchemeCableLinkId()))
										{
											scheme.cablelinks.remove(sl);
											break;
										}
									}
									boolean contains_new = false;
									for (Iterator it = scheme.cablelinks.iterator(); it.hasNext();)
									{
										SchemeCableLink sl = (SchemeCableLink)it.next();
										if (sl.getId().equals(id))
										{
											contains_new = true;
											break;
										}
									}
									if (!contains_new)
									{
										SchemeCableLink sl = (SchemeCableLink)Pool.get(SchemeCableLink.typ, id);
										if (sl == null)
											JOptionPane.showMessageDialog(null, "Схемный кабельный линк не найден " + id);
										else
											scheme.cablelinks.add(sl);
									}
								}
							}
							((DefaultCableLink)cells[0]).setSchemeCableLinkId(id);
						}
						/*id = JOptionPane.showInputDialog("Новый идентификатор пути:",
								((DefaultCableLink)cells[0]).getSchemePathId());
						if (id != null && !id.equals(""))
							((DefaultCableLink)cells[0]).setSchemePathId(id);*/
					}
					if (cells[0] instanceof DeviceGroup)
					{
						String id = JOptionPane.showInputDialog("Новый идентификатор схемного элемента:",
								((DeviceGroup)cells[0]).getSchemeElementId());
						if (id != null && !id.equals(""))
						{
							if (this instanceof SchemePanel)
							{
								int ret = JOptionPane.showConfirmDialog(null, "Заменить старый схемный элемент новым?");
								if (ret == JOptionPane.YES_OPTION)
								{
									Scheme scheme = getGraph().getScheme();
									for (Iterator it = scheme.elements.iterator(); it.hasNext();)
									{
										SchemeElement se = (SchemeElement)it.next();
										if (se.getId().equals(((DeviceGroup)cells[0]).getSchemeElementId()))
										{
											scheme.elements.remove(se);
											break;
										}
									}
									boolean contains_new = false;
									for (Iterator it = scheme.elements.iterator(); it.hasNext();)
									{
										SchemeElement se = (SchemeElement)it.next();
										if (se.getId().equals(id))
										{
											contains_new = true;
											break;
										}
									}
									if (!contains_new)
									{
										SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, id);
										if (se == null)
											JOptionPane.showMessageDialog(null, "Схемный элемент не найден " + id);
										else
											scheme.elements.add(se);
									}
								}
							}
							((DeviceGroup)cells[0]).setSchemeElementId(id);
						}
						id = JOptionPane.showInputDialog("Новый идентификатор схемы:",
								((DeviceGroup)cells[0]).getSchemeId());
						if (id != null && !id.equals(""))
							((DeviceGroup)cells[0]).setSchemeId(id);
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_L)
			{
				Object[] cells = getGraph().getSelectionCells();
				if (cells.length == 1)
				{
					if (cells[0] instanceof DeviceGroup)
					{
						SchemeElement se = ((DeviceGroup)cells[0]).getSchemeElement();
						if (se != null)
						{
							int ret = JOptionPane.showConfirmDialog(null,
									"Ввести в картинку идентификаторы из схемного элемента?",
									"Question",
									JOptionPane.YES_NO_OPTION);
							if (ret == JOptionPane.YES_OPTION)
							{
								se.unpack();
								int counter = 0;

								DefaultGraphModel model = new DefaultGraphModel();
								SchemeGraph virtual_graph = new SchemeGraph(model, aContext);
								Object[] _cells = virtual_graph.setFromArchivedState(se.serializable_cell);
								_cells = virtual_graph.getDescendants(_cells);

								java.util.HashSet hs = new java.util.HashSet();
								//for (Enumeration en = se.links.elements(); en.hasMoreElements();)
								hs.addAll(se.links);

								for (int i = 0; i < _cells.length; i++)
								{
									;
									if (counter < se.links.size())
										if (_cells[i] instanceof DefaultLink)
										{
											//SchemeLink sl = (SchemeLink)se.links.get(counter);
											for (Iterator it = hs.iterator(); it.hasNext();)
											{
												SchemeLink sl = (SchemeLink)it.next();
												if (sl.getName().equals(((DefaultLink)_cells[i]).getUserObject()) || !it.hasNext())
												{
													((DefaultLink)_cells[i]).setSchemeLinkId(sl.getId());
													it.remove();
													break;
												}
											}
											counter++;
										}
								}
								se.serializable_cell = virtual_graph.getArchiveableState(virtual_graph.getRoots());
								se.pack();
							}
						}
					}
				}
			}
		}
	}

class ToolBarPanel extends UgoPanel.ToolBarPanel
{
	protected Map createGraphButtons (ElementsPanel p)
	{
		Map buttons = new HashMap();

		if (getGraph().getMarqueeHandler() instanceof SchemeGraph.ShemeMarqueeHandler)
		{
			SchemeGraph.ShemeMarqueeHandler mh = (SchemeGraph.ShemeMarqueeHandler) getGraph().getMarqueeHandler();

			buttons.put(Constants.marqueeTool,
									createToolButton(mh.s, btn_size, null, null,
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pointer.gif")),
									new MarqeeAction(getGraph()), true));
			buttons.put("s_cell", mh.s_cell);
			buttons.put(Constants.deviceTool,
									createToolButton(mh.dev, btn_size, null, "устройство",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/device.gif")),
									null, true));
			buttons.put(Constants.rectangleTool,
									createToolButton(mh.r, btn_size, null, "прямоугольник",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/rectangle.gif")),
									null, true));
			buttons.put(Constants.ellipseTool,
									createToolButton(mh.c, btn_size, null, "эллипс",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/ellipse.gif")),
									null, true));
			buttons.put(Constants.textTool,
									createToolButton(mh.t, btn_size, null, "текст",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/text.gif")),
									null, true));
			buttons.put(Constants.iconTool,
									createToolButton(mh.i, btn_size, "иконка", "иконка",
									null, null, true));
			buttons.put(Constants.lineTool,
									createToolButton(mh.l, btn_size, null, "линия",
									 new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/line.gif")),
									null, true));
			buttons.put(Constants.cableTool,
									createToolButton(mh.ce, btn_size, null, "кабель",
									 new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/thick_edge.gif")),
									null, true));
			buttons.put(Constants.linkTool,
									createToolButton(mh.e, btn_size, null, "связь",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/edge.gif")),
									null, true));
			buttons.put(Constants.zoomTool,
									createToolButton(mh.z, btn_size, "зум", "зум",
									null, null, true));
			buttons.put(Constants.portOutKey,
									createToolButton(mh.p1, btn_size, null, "порт",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/port.gif")),
									new PortToolAction(), false));
			buttons.put(Constants.portInKey,
									createToolButton(mh.p2, btn_size, null, "кабельный порт",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/cableport.gif")),
									new PortToolAction(), false));
			buttons.put(Constants.groupKey,
									createToolButton(mh.gr, btn_size, null, "создать компонент",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/group.gif")),
									new GroupAction(getGraph()), false));
			buttons.put(Constants.ungroupKey,
									createToolButton(mh.ugr, btn_size, null, "разобрать",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/ungroup.gif")),
									new UngroupAction(getGraph()), false));
			buttons.put(Constants.undoKey,
									createToolButton(mh.undo, btn_size, null, "отменить",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/undo.gif")),
									new UndoAction(getGraph(), undoManager), false));
			buttons.put(Constants.redoKey,
									createToolButton(mh.redo, btn_size, null, "вернуть",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/redo.gif")),
									new RedoAction(getGraph(), undoManager), false));
			buttons.put(Constants.zoomInKey,
									createToolButton(mh.zi, btn_size, null, "увеличить",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_in.gif")),
									new ZoomInAction(getGraph()), true));
									//new SaveLibraryAction(getGraph(), libPanel), true));
			buttons.put(Constants.zoomOutKey,
									createToolButton(mh.zo, btn_size, null, "уменьшить",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_out.gif")),
									new ZoomOutAction(getGraph()), true));
									//new OpenLibraryAction(getGraph(), libPanel), true));
			buttons.put(Constants.zoomActualKey,
									createToolButton(mh.za, btn_size, null, "фактический размер",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_actual.gif")),
									new ZoomActualAction(getGraph()), true));
			buttons.put(Constants.deleteKey,
									createToolButton(mh.del, btn_size, null, "удалить",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/delete.gif")),
									new DeleteAction(ElementsPanel.this, aContext), false));
			buttons.put(Constants.hierarchyUpKey,
									createToolButton(mh.hup, btn_size, null, "вверх",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/hand.gif")),
									new HierarchyUpAction (getGraph()), true));
//			buttons.put(Constants.insertIntoLibraryKey,
//									createToolButton(mh.addlib, btn_size, null, "сохранить компонент",
//									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/library.gif")),
//									new InsertIntoLibraryAction (getGraph()), false));
			buttons.put(Constants.createTopLevelElementKey,
									createToolButton(mh.ugo, btn_size, null, "создать УГО",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/component_ugo.gif")),
									new CreateTopLevelElementAction (getGraph()), false));
			buttons.put(Constants.blockPortKey,
									createToolButton(mh.bp, btn_size, null, "связной порт",
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/hierarchy_port.gif")),
									new CreateBlockPortAction (getGraph()), false));


			ButtonGroup group = new ButtonGroup();
			for (Iterator it = buttons.values().iterator(); it.hasNext();)
			{
				AbstractButton button = (AbstractButton)it.next();
				group.add(button);
			}
			mh.s.doClick();
		}
		return buttons;
	}
}
}

