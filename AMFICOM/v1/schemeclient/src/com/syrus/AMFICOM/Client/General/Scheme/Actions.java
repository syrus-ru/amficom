package com.syrus.AMFICOM.Client.General.Scheme;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.jgraph.graph.*;
import com.jgraph.graph.Port;
import com.jgraph.pad.GPLibraryPanel;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.resource.BitmapImageResource;


class PortToolAction extends AbstractAction
{
	PortToolAction()
	{
		super(Constants.portTool);
	}

	public void actionPerformed(ActionEvent e)
	{
	}
}

class MarqeeAction extends AbstractAction
{
	SchemeGraph graph;
	MarqeeAction(SchemeGraph graph)
	{
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		((SchemeGraph.ShemeMarqueeHandler)graph.getMarqueeHandler()).settingProto = null;
	}
}

class DeleteAction extends AbstractAction
{
	ApplicationContext aContext;
	SchemeGraph graph;
	UgoPanel panel;

	DeleteAction(UgoPanel panel, ApplicationContext aContext)
	{
		super(Constants.deleteKey);
		this.aContext = aContext;
		this.panel = panel;
		this.graph = panel.getGraph();
	}

	public void actionPerformed(ActionEvent e)
	{
		Object[] cells = graph.getSelectionCells();
		ArrayList new_cells = new ArrayList(cells.length);
		if (cells != null)
		{
			if (graph.isTopLevelSchemeMode()) {
				int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
						"������� �������� �� ������������ ����?", "�������������",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					graph.getModel().remove(graph.getDescendants(cells));
					return;
				}
			}
			else for (int i = 0; i < cells.length; i++)
			{
				if (cells[i] instanceof DeviceGroup)
				{
					if (((DeviceGroup)cells[i]).getSchemeElementId() != null)
					{
						SchemeElement element = ((DeviceGroup)cells[i]).getSchemeElement();
						int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
								"�� ������������� ������ ������� ������� �� �����?", "�������������", JOptionPane.YES_NO_CANCEL_OPTION);
						if (ret == JOptionPane.CANCEL_OPTION || ret == JOptionPane.NO_OPTION)
							return;
						if (panel.getGraph().getScheme() != null)
						{
							Arrays.asList(panel.getGraph().getScheme().schemeElements()).remove(element);
							if (element.getEquipment() != null)
								ConfigurationStorableObjectPool.delete(element.getEquipment().getId());
							SchemeStorableObjectPool.delete(element.getId());
						}
					}
					/**
					 * @todo ��������� ������������ ����� �����
					 */
					else if (((DeviceGroup)cells[i]).getSchemeId() != null)
					{
						int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
								"�� ������������� ������ ������� �������� ����������� ����������� �����?", "�������������", JOptionPane.YES_NO_CANCEL_OPTION);
						if (ret == JOptionPane.CANCEL_OPTION || ret == JOptionPane.NO_OPTION)
							return;
						panel.getGraph().getScheme().setUgoCell(null);
					}
					new_cells.add(cells[i]);
				}
				else if (cells[i] instanceof DefaultCableLink)
				{
					SchemeCableLink link = ((DefaultCableLink)cells[i]).getSchemeCableLink();
					int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
							"�� ������������� ������ ������� ��������� ����� �� �����?", "�������������", JOptionPane.YES_NO_CANCEL_OPTION);
					if (ret == JOptionPane.CANCEL_OPTION || ret == JOptionPane.NO_OPTION)
						return;

					SchemeActions.disconnectSchemeCableLink (graph, (DefaultCableLink)cells[i], true);
					SchemeActions.disconnectSchemeCableLink (graph, (DefaultCableLink)cells[i], false);
					if (panel.getGraph().getScheme() != null)
						Arrays.asList(panel.getGraph().getScheme().schemeCableLinks()).remove(link);
					if (link.getLink() != null) {
							ConfigurationStorableObjectPool.delete(link.getLink().getId());
						}
					SchemeStorableObjectPool.delete(link.getId());
					new_cells.add(cells[i]);
				}
				else if (cells[i] instanceof DefaultLink)
				{
					SchemeLink link = ((DefaultLink)cells[i]).getSchemeLink();
					int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
									"�� ������������� ������ ������� ����� �� �����?", "�������������", JOptionPane.YES_NO_CANCEL_OPTION);
					if (ret == JOptionPane.CANCEL_OPTION || ret == JOptionPane.NO_OPTION)
						return;

					SchemeActions.disconnectSchemeLink (graph, (DefaultLink)cells[i], true);
					SchemeActions.disconnectSchemeLink (graph, (DefaultLink)cells[i], false);

					if (panel.getGraph().getScheme() != null)
						Arrays.asList(panel.getGraph().getScheme().schemeLinks()).remove(link);
					if(panel.getGraph().getSchemeElement() != null)
						Arrays.asList(panel.getGraph().getSchemeElement().schemeLinks()).remove(link);
					if (link.getLink() != null)
					{
						link.setLink(null);
						ConfigurationStorableObjectPool.delete(link.getLink().getId());
					}
					SchemeStorableObjectPool.delete(link.getId());
					new_cells.add(cells[i]);
				}
				else if (cells[i] instanceof BlockPortCell)
				{
					for (Enumeration en = ((BlockPortCell)cells[i]).children(); en.hasMoreElements();)
					{
						Port p = (Port)en.nextElement();
						for (Iterator it = p.edges(); it.hasNext();)
							new_cells.add(it.next());
					}
					new_cells.add(cells[i]);
				}
				else if (!GraphActions.hasGroupedParent(cells[i]))
				{
					if (cells[i] instanceof PortCell)
					{
						SchemePort port = ((PortCell)cells[i]).getSchemePort();
						if (port.getPort() != null) {
							ConfigurationStorableObjectPool.delete(port.getPort().getId());
						}
						SchemeStorableObjectPool.delete(port.getId());
						new_cells.add(cells[i]);
						for (Enumeration en = ((PortCell)cells[i]).children(); en.hasMoreElements();)
						{
							Port p = (Port)en.nextElement();
							for (Iterator it = p.edges(); it.hasNext();)
							{
								DefaultEdge edge = (DefaultEdge)it.next();
								new_cells.add(edge);

								removePortFromParent((DefaultPort)edge.getSource(), port);
								removePortFromParent((DefaultPort)edge.getTarget(), port);

								deleteConnections(edge, (DefaultPort)edge.getSource());
								deleteConnections(edge, (DefaultPort)edge.getTarget());
							}
						}
					}
					else if (cells[i] instanceof CablePortCell)
					{
						SchemeCablePort port = ((CablePortCell)cells[i]).getSchemeCablePort();
						if (port.getPort() != null) {
							ConfigurationStorableObjectPool.delete(port.getPort().getId());
						}
						SchemeStorableObjectPool.delete(port.getId());
						new_cells.add(cells[i]);
						for (Enumeration en = ((CablePortCell)cells[i]).children(); en.hasMoreElements();)
						{
							Port p = (Port)en.nextElement();
							for (Iterator it = p.edges(); it.hasNext();)
							{
								DefaultEdge edge = (DefaultEdge)it.next();
								new_cells.add(edge);

								removeCablePortFromParent((DefaultPort)edge.getSource(), ((CablePortCell)cells[i]).getSchemeCablePort());
								removeCablePortFromParent((DefaultPort)edge.getTarget(), ((CablePortCell)cells[i]).getSchemeCablePort());

								deleteConnections(edge, (DefaultPort)edge.getSource());
								deleteConnections(edge, (DefaultPort)edge.getTarget());
							}
						}
					}
					else if (cells[i] instanceof DefaultEdge)
					{
						DefaultEdge edge = (DefaultEdge)cells[i];
						new_cells.add(edge);

						if (edge.getSource() instanceof DefaultPort)
						{
							Object p = ((DefaultPort)edge.getSource()).getParent();
							if (p instanceof PortCell)
							{
								removePortFromParent((DefaultPort)edge.getTarget(), ((PortCell)p).getSchemePort());
								new_cells.add(p);
							}
							else if (p instanceof CablePortCell)
							{
								removeCablePortFromParent((DefaultPort)edge.getTarget(), ((CablePortCell)p).getSchemeCablePort());
								new_cells.add(p);
							}
							else
							{
								p = ((DefaultPort)edge.getTarget()).getParent();
								if (p instanceof PortCell)
								{
									removePortFromParent((DefaultPort)edge.getSource(), ((PortCell)p).getSchemePort());
									new_cells.add(p);
								}
								else if (p instanceof CablePortCell)
								{
									removeCablePortFromParent((DefaultPort)edge.getSource(), ((CablePortCell)p).getSchemeCablePort());
									new_cells.add(p);
								}
							}
							deleteConnections(edge, (DefaultPort)edge.getSource());
							deleteConnections(edge, (DefaultPort)edge.getTarget());
						}
					}
					else
						new_cells.add(cells[i]);
				}
			}
			cells = DefaultGraphModel.getDescendants(graph.getModel(),
					new_cells.toArray(new Object[new_cells.size()])).toArray();
			graph.getModel().remove(cells);
			graph.selectionNotify();
			aContext.getDispatcher().notify(new SchemeElementsEvent(this, graph, SchemeElementsEvent.SCHEME_CHANGED_EVENT));
		}
	}

	private void deleteConnections(DefaultEdge edge, DefaultPort port)
	{
		if (port != null)
		{
			DefaultGraphCell cell = (DefaultGraphCell)port.getParent();
			if (cell != null)
			{
				GraphActions.disconnectEdge(graph, edge, port, false);
				GraphActions.removePort(graph, cell, port, cell.getAttributes());
			}
		}
	}

	private void removePortFromParent(DefaultPort port, SchemePort sp)
	{
		if (port != null)
		{
			DefaultGraphCell parent = (DefaultGraphCell)port.getParent();
			if (parent instanceof DeviceCell)
			{
				SchemeDevice dev = ((DeviceCell)parent).getSchemeDevice();
				if (dev != null)
					Arrays.asList(dev.schemePorts()).remove(sp);
			}
		}
	}

	private void removeCablePortFromParent(DefaultPort port, SchemeCablePort scp)
	{
		if (port != null)
		{
			DefaultGraphCell parent = (DefaultGraphCell)port.getParent();
			if (parent instanceof DeviceCell)
			{
				SchemeDevice dev = ((DeviceCell)parent).getSchemeDevice();
				if (dev != null)
					Arrays.asList(dev.schemeCablePorts()).remove(scp);
			}
		}
	}
}

class GroupSEAction extends AbstractAction
{
	SchemeGraph graph;

	GroupSEAction(SchemeGraph graph)
	{
		super(Constants.groupKey);
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		Object[] cells = graph.getGraphLayoutCache().order(graph.getSelectionCells());
		if (cells != null && cells.length > 0)
		{
			ArrayList new_cells = new ArrayList();
			for(int i = 0; i < cells.length; i++)
			{
				if (cells[i] instanceof DeviceCell)
				{
					DeviceCell box = (DeviceCell)cells[i];
					if (box.getChildCount() > 1)
					{
						new_cells.add(box);
						for (Enumeration it = box.children(); it.hasMoreElements(); )
						{
							DefaultPort dev_port = (DefaultPort)it.nextElement();
							if (GraphConstants.getOffset(dev_port.getAttributes()) != null)
							{
								if (dev_port.edges().hasNext())
								{
									DefaultEdge edge = (DefaultEdge)dev_port.edges().next();
									new_cells.add(edge);
									Object ell = DefaultGraphModel.getTargetVertex(graph.getModel(), edge);
									if (ell instanceof PortCell)
									{
										if (((PortCell)ell).getSchemePort().getPortType() == null)
										{
											JOptionPane.showMessageDialog(
													Environment.getActiveWindow(),
													"�� ���������� ��� �����",
													"������",
													JOptionPane.OK_OPTION);
											System.out.println("Error! Port type not set.");
											return;
										}
										new_cells.add(ell);
									}

									else if (ell instanceof CablePortCell)
									{
										if (((CablePortCell)ell).getSchemeCablePort().getPortType() == null)
										{
											JOptionPane.showMessageDialog(
													Environment.getActiveWindow(),
													"�� ���������� ��� ���������� �����",
													"������",
													JOptionPane.OK_OPTION);
											System.out.println("Error! Cableport type not set.");
											return;
										}
										new_cells.add(ell);
									}
								}
							}
						}
					}
				}
				else if (cells[i] instanceof DeviceGroup || cells[i] instanceof DefaultLink)
				{
					new_cells.add(cells[i]);
				}
			}
			if (new_cells.isEmpty())
				return;

			DeviceGroup group = new DeviceGroup();
			SchemeElement scheme_el;
			if (graph.getSchemeElement() != null)
			{
				scheme_el = graph.getSchemeElement();
				if (graph.getScheme() != null)
					graph.setSchemeElement(null);
			}
			else
				scheme_el = SchemeElement.createInstance();

			group.setSchemeElementId(scheme_el.getId());
			group.setSchemeId(scheme_el.internalScheme().getId());

			if (graph.getScheme() != null)
			{
				List elements = Arrays.asList(graph.getScheme().schemeElements());
				if (!elements.contains(scheme_el))
				{
					elements.add(scheme_el);
					scheme_el.scheme(graph.getScheme());
				}
			}

			cells = new_cells.toArray();
			Map viewMap = new HashMap();

			for (int i = 0; i < cells.length; i++)
			{
				if (cells[i] instanceof DeviceGroup)
				{
					SchemeElement se = ((DeviceGroup)cells[i]).getSchemeElement();
					if (se != null)
					{
						java.util.List schemeElements = Arrays.asList(scheme_el.schemeElements());
						if (!schemeElements.contains(se))
							schemeElements.add(se);
					}
					else
					{
						SchemeElement sel = null;
						if (((DeviceGroup)cells[i]).getProtoElement() != null)
							sel = SchemeElement.createInstance(((DeviceGroup)cells[i]).getProtoElement());
						else
							sel = SchemeElement.createInstance();
						java.util.List schemeElements = Arrays.asList(scheme_el.schemeElements());
						if (!schemeElements.contains(sel))
							schemeElements.add(sel);
					}
				}
				else if (cells[i] instanceof DeviceCell)
				{
					java.util.List schemeDevices = Arrays.asList(scheme_el.schemeDevices());
					if (!schemeDevices.contains(((DeviceCell)cells[i]).getSchemeDevice()))
						schemeDevices.add(((DeviceCell)cells[i]).getSchemeDevice());
				}
				else if (cells[i] instanceof DefaultLink)
				{
					java.util.List schemeLinks = Arrays.asList(scheme_el.schemeLinks());
					if (!schemeLinks.contains(((DefaultLink)cells[i]).getSchemeLink()))
						schemeLinks.add(((DefaultLink)cells[i]).getSchemeLink());
				}
			}

			//make group created unresizable
			Map m = GraphConstants.createMap();
			GraphConstants.setSizeable(m, false);
			viewMap.put(group, m);

			ParentMap map = new ParentMap();
			for (int i = 0; i < cells.length; i++)
				map.addEntry(cells[i], group);
			graph.getGraphLayoutCache().insert(new Object[] { group }, viewMap, null, map, null);
			graph.setSelectionCell(group);

			scheme_el.getSchemeCell().setData((List)graph.getArchiveableState(new Object[]{group}));
		}
	}
}

class GroupAction extends AbstractAction
{
	SchemeGraph graph;

	GroupAction(SchemeGraph graph)
	{
		super(Constants.groupKey);
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		Object[] cells = graph.getGraphLayoutCache().order(graph.getSelectionCells());
		String text = "";
		if (cells != null && cells.length > 0)
		{
			ArrayList new_cells = new ArrayList();
			for(int i = 0; i < cells.length; i++)
			{
				if (cells[i] instanceof DeviceCell)
				{
					DeviceCell box = (DeviceCell)cells[i];
					if (box.getChildCount() > 1)
					{
						new_cells.add(box);
						text = (String)box.getUserObject();
						for (Enumeration enumeration = box.children(); enumeration.hasMoreElements(); )
						{
							DefaultPort dev_port = (DefaultPort)enumeration.nextElement();
							if (GraphConstants.getOffset(dev_port.getAttributes()) != null)
							{
								if (dev_port.edges().hasNext())
								{
									DefaultEdge edge = (DefaultEdge)dev_port.edges().next();
									new_cells.add(edge);
									Object ell = DefaultGraphModel.getTargetVertex(graph.getModel(), edge);
									if (ell instanceof PortCell)
									{
										if (((PortCell)ell).getSchemePort().getPortType() == null)
										{
											JOptionPane.showMessageDialog(
													Environment.getActiveWindow(),
													"�� ���������� ��� �����",
													"������",
													JOptionPane.OK_OPTION);
											System.out.println("Error! Port type not set.");
											return;
										}
										new_cells.add(ell);
									}

									else if (ell instanceof CablePortCell)
									{
										if (((CablePortCell)ell).getSchemeCablePort().getPortType() == null)
										{
											JOptionPane.showMessageDialog(
													Environment.getActiveWindow(),
													"�� ���������� ��� ���������� �����",
													"������",
													JOptionPane.OK_OPTION);
											System.out.println("Error! Cableport type not set.");
											return;
										}
										new_cells.add(ell);
									}
								}
							}
						}
					}
				}
				else if (cells[i] instanceof DeviceGroup || cells[i] instanceof DefaultLink)
				{
					new_cells.add(cells[i]);
				}
			}
			if (new_cells.isEmpty())
				return;

			Identifier user_id = new Identifier(((RISDSessionInfo)graph.aContext.getSessionInterface()).getAccessIdentifier().user_id);
			DeviceGroup group = new DeviceGroup();
			EquipmentType eqt = null;
			try {
				eqt = EquipmentType.createInstance(
						user_id,
						"",
						"",
						"",
						"",
						"");
			}
			catch (CreateObjectException ex) {
				ex.printStackTrace();
			}
			SchemeProtoElement proto = SchemeProtoElement.createInstance();
			proto.setEquipmentType(eqt);
			try {
				ConfigurationStorableObjectPool.putStorableObject(eqt);
				SchemeStorableObjectPool.putStorableObject(proto);
			}
			catch (IllegalObjectEntityException ex) {
				ex.printStackTrace();
			}
			group.setProtoElementId(proto.getId());
			proto.setLabel(text);

			cells = new_cells.toArray();
			Map viewMap = new HashMap();
			List protos = Arrays.asList(proto.protoElements());
			List devices = Arrays.asList(proto.devices());
			List links = Arrays.asList(proto.links());
			for (int i = 0; i < cells.length; i++)
			{
				if (cells[i] instanceof DeviceGroup)
					protos.add(((DeviceGroup)cells[i]).getProtoElement());
				else if (cells[i] instanceof DeviceCell)
					devices.add(((DeviceCell)cells[i]).getSchemeDevice());
				else if (cells[i] instanceof DefaultLink)
					links.add(((DefaultLink)cells[i]).getSchemeLink());
			}

			//make group created unresizable
			Map m = GraphConstants.createMap();
			GraphConstants.setSizeable(m, false);
			viewMap.put(group, m);

			ParentMap map = new ParentMap();
			for (int i = 0; i < cells.length; i++)
				map.addEntry(cells[i], group);
			graph.getGraphLayoutCache().insert(new Object[] { group }, viewMap, null, map, null);
			graph.setSelectionCell(group);

			proto.getSchemeCell().setData((List)graph.getArchiveableState(new Object[]{group}));
		}
	}
}

class UngroupAction extends AbstractAction
{
	SchemeGraph graph;

	UngroupAction(SchemeGraph graph)
	{
		super(Constants.ungroupKey);
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		Object[] cells = graph.getSelectionCells();
		if (cells != null)
		{
			ArrayList groups = new ArrayList();
			ArrayList children = new ArrayList();
			for (int i = 0; i < cells.length; i++)
			{
				if (graph.isGroup(cells[i]))
				{
					groups.add(cells[i]);
					for (int j = 0; j < graph.getModel().getChildCount(cells[i]); j++)
					{
						Object child = graph.getModel().getChild(cells[i], j);
						if (!(child instanceof Port))
							children.add(child);
					}
					if (cells[i] instanceof DeviceGroup)
					{
						DeviceGroup group = (DeviceGroup)cells[i];
						graph.getGraphResource().schemeelement = group.getSchemeElement();
					}
				}
			}
			graph.getModel().remove(groups.toArray());
			graph.setSelectionCells(children.toArray());
		}
	}
}

class UndoAction extends AbstractAction
{
	SchemeGraph graph;
	GraphUndoManager undoManager;
	UndoAction(SchemeGraph graph, GraphUndoManager undoManager)
	{
		super(Constants.undoKey);
		this.graph = graph;
		this.undoManager = undoManager;
	}

	public void actionPerformed(ActionEvent e)
	{
		try
		{
			undoManager.undo(graph.getGraphLayoutCache());
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		finally
		{
			((SchemeGraph.ShemeMarqueeHandler)graph.getMarqueeHandler()).updateHistoryButtons(undoManager);
		}
	}
}

class RedoAction extends AbstractAction
{
	SchemeGraph graph;
	GraphUndoManager undoManager;

	RedoAction(SchemeGraph graph, GraphUndoManager undoManager)
	{
		super(Constants.redoKey);
		this.graph = graph;
		this.undoManager = undoManager;
	}

	public void actionPerformed(ActionEvent e)
	{
		try
		{
			undoManager.redo(graph.getGraphLayoutCache());
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			ex.printStackTrace();
		}
		finally
		{
			((SchemeGraph.ShemeMarqueeHandler)graph.getMarqueeHandler()).updateHistoryButtons(undoManager);
		}
	}
}

class ZoomInAction extends AbstractAction
{
	SchemeGraph graph;

	ZoomInAction(SchemeGraph graph)
	{
		super(Constants.zoomInKey);
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		graph.setScale(graph.getScale() * 1.25);
		Dimension size = graph.getPreferredSize();
		graph.setPreferredSize(new Dimension((int)(size.width * 1.25), (int)(size.height * 1.25)));
		Point loc =  graph.getLocation();
		graph.setLocation (loc.x - (int)(graph.getWidth() * 0.125),
											 loc.y - (int)(graph.getHeight() * 0.125));
		if (graph.getScale() >= .5)
			graph.setGridVisible(graph.isGridVisibleAtActualSize());
	}
}

class ZoomOutAction extends AbstractAction
{
	SchemeGraph graph;

	ZoomOutAction(SchemeGraph graph)
	{
		super(Constants.zoomOutKey);
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		graph.setScale(graph.getScale() * .8);
		Dimension size = graph.getPreferredSize();
		graph.setPreferredSize(new Dimension((int)(size.width * .8), (int)(size.height * .8)));
		Point loc = graph.getLocation();
		graph.setLocation (Math.min(0, loc.x + (int)(graph.getWidth() * 0.1)),
											 Math.min(0, loc.y + (int)(graph.getHeight() * 0.1)));
		if (graph.getScale() < .5)
			graph.setGridVisible(false);
	}
}

class ZoomActualAction extends AbstractAction
{
	SchemeGraph graph;
	ZoomActualAction(SchemeGraph graph)
	{
		super(Constants.zoomActualKey);
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		graph.setScale(1);
		graph.setPreferredSize(graph.actualSize);
		graph.setGridVisible(graph.isGridVisibleAtActualSize());
		//graph.setLocation(0, 0);
	}
}

class CreateTopLevelElementAction extends AbstractAction
{
	SchemeGraph graph;

	CreateTopLevelElementAction(SchemeGraph graph)
	{
		super(Constants.createTopLevelElementKey);
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		Object[] cells = graph.getAll();
		DeviceGroup[] groups = GraphActions.findTopLevelGroups(graph, cells);

		if (groups.length == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "�� ������� �� ������ ����������", "������", JOptionPane.OK_OPTION);
			return;
		}

		ArrayList blockports_in = new ArrayList();
		ArrayList blockports_out = new ArrayList();
		BlockPortCell[] bpcs = GraphActions.findTopLevelPorts(graph, cells);

		for (int i = 0; i < bpcs.length; i++)
		{
				if (bpcs[i].isCablePort())
				{
					SchemeCablePort port = bpcs[i].getSchemeCablePort();
					if (port.directionType().equals(DirectionType._IN))
						blockports_in.add(bpcs[i]);
					else
						blockports_out.add(bpcs[i]);
				}
				else
				{
					SchemePort port = bpcs[i].getSchemePort();
					if (port.directionType().equals(DirectionType._IN))
						blockports_in.add(bpcs[i]);
					else
						blockports_out.add(bpcs[i]);
				}
		}
		if (blockports_in.size() == 0 && blockports_out.size() == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "�� ������� �� ������ ����� �������� ������", "������", JOptionPane.OK_OPTION);
			return;
		}

		//Object[] edges = GraphActions.findAllVertexEdges(graph, bpcs);
		//ArrayList v = new ArrayList();
		/*for (int i = 0; i < cells.length; i++)
			v.add(cells[i]);
		for (int i = 0; i < bpcs.length; i++)
			v.add(bpcs[i]);
		for (int i = 0; i < edges.length; i++)
			v.add(edges[i]);*/

		// Save group as serializable_cell
		Rectangle oldrect = graph.getCellBounds(cells);
		SchemeProtoElement proto;

		if (groups.length == 1)
		{
			proto = groups[0].getProtoElement();
		}
		else
		{
			Identifier user_id = new Identifier(((RISDSessionInfo)graph.aContext.getSessionInterface()).getAccessIdentifier().user_id);
			proto = SchemeProtoElement.createInstance();
			EquipmentType eqt = null;
			try {
				eqt = EquipmentType.createInstance(
						user_id,
						"",
						"",
						"",
						"",
						"");
			}
			catch (CreateObjectException ex) {
				ex.printStackTrace();
			}
			proto.setEquipmentType(eqt);
			List protos = Arrays.asList(proto.protoElements());
			for (int i = 0; i < groups.length; i++)
				protos.add(groups[i].getProtoElement());
			DefaultLink[] _links = GraphActions.findTopLevelLinks(graph, cells);
			List links = Arrays.asList(proto.links());
			for (int i = 0; i < _links.length; i++)
				links.add(_links[i].getSchemeLink());

			SchemeDevice new_dev = SchemeDevice.createInstance();
			List cablePorts = new ArrayList();
			List ports = new ArrayList();
			for (Iterator it = blockports_in.iterator(); it.hasNext();)
			{
				BlockPortCell bpc = (BlockPortCell)it.next();
				if (bpc.isCablePort())
					cablePorts.add(bpc.getSchemeCablePort());
				else
					ports.add(bpc.getSchemePort());
			}
			for (Iterator it = blockports_out.iterator(); it.hasNext();)
			{
				BlockPortCell bpc = (BlockPortCell)it.next();
				if (bpc.isCablePort())
					cablePorts.add(bpc.getSchemeCablePort());
				else
					ports.add(bpc.getSchemePort());
			}
			new_dev.schemeCablePorts((SchemeCablePort[])cablePorts.toArray(new SchemeCablePort[cablePorts.size()]));
			new_dev.schemePorts((SchemePort[])ports.toArray(new SchemePort[ports.size()]));

			Arrays.asList(proto.devices()).add(new_dev);
			try {
				ConfigurationStorableObjectPool.putStorableObject(eqt);
				SchemeStorableObjectPool.putStorableObject(proto);
			}
			catch (IllegalObjectEntityException ex) {
				ex.printStackTrace();
			}
		}

//		proto.serializable_cell = graph.getArchiveableState(groups);

		// And remove old cells
		//graph.setSelectionCells(new Object[0]);
		//graph.getModel().remove(graph.getDescendants(cells));

		ArrayList v = new ArrayList();
		v.add(blockports_in);
		v.add(blockports_out);
		v.add(oldrect);
		v.add(proto);

		graph.dispatcher.notify(new SchemeElementsEvent(this, v, SchemeElementsEvent.UGO_CREATE_EVENT));
	}
}

class CreateUgoAction
{
	SchemeGraph graph;

	CreateUgoAction(SchemeGraph graph)
	{
		this.graph = graph;
	}

	public void create(ArrayList v)
	{
		ArrayList blockports_in = (ArrayList)v.get(0);
		ArrayList blockports_out = (ArrayList)v.get(1);
//		Rectangle oldrect = (Rectangle)v.get(2);
		SchemeProtoElement proto = (SchemeProtoElement)v.get(3);

		Object[] old_devs = graph.getRoots();
		if (old_devs.length == 1 && old_devs[0] instanceof DeviceGroup)
		{
			SchemeProtoElement old_proto = ((DeviceGroup)old_devs[0]).getProtoElement();
			if (old_proto != null)
			{
				proto.setCharacteristics(old_proto.getCharacteristics());
				proto.setSymbol(old_proto.getSymbol());
				proto.setLabel(old_proto.getLabel());
//				proto.scheme_proto_group = old_proto.scheme_proto_group;
				proto.setName(old_proto.getName());
				proto.setEquipmentType(old_proto.getEquipmentType());
				EquipmentType eqt = proto.getEquipmentType();
				EquipmentType old_eqt = old_proto.getEquipmentType();
				eqt.setDescription(old_eqt.getDescription());
//				eqt.eqClass = old_eqt.eqClass;
				eqt.setCharacteristics(old_eqt.getCharacteristics());
//				eqt.setManufacturer(old_eqt.getManufacturer());
//				eqt.setManufacturerCode(old_eqt.getManufacturerCode());
//				eqt.imageId = old_eqt.imageId;
				eqt.setName(old_eqt.getName());
			}
			else
			{
				for (Enumeration en = ((DeviceGroup)old_devs[0]).children(); en.hasMoreElements(); ) {
					Object child = en.nextElement();
					if (child instanceof DeviceCell) {
						proto.setLabel((String)((DeviceCell)child).getUserObject());
						break;
					}
				}
			}
		}

		//remove old cells
		graph.setSelectionCells(new Object[0]);
		graph.getModel().remove(graph.getDescendants(graph.getAll()));

		// create new element
		int grid = graph.getGridSize();
		int max = Math.max(3, Math.max(blockports_in.size(), blockports_out.size()));
		Rectangle bounds = new Rectangle(
				graph.snap(new Point(grid*2, grid*2)),//oldrect.x, oldrect.y
				graph.snap(new Dimension(grid*4, grid*(max+1))));
		DefaultGraphCell cell = GraphActions.CreateDeviceAction(graph, proto.getLabel(), bounds, false, Color.black);
		graph.setSelectionCell(cell);
		for (int i = 0; i < blockports_out.size(); i++)
		{
			BlockPortCell b = (BlockPortCell)blockports_out.get(i);
			String name = (String)b.getUserObject();
			Point p = graph.snap(new Point(grid*2+grid*5, grid*2 + grid*((max - blockports_out.size()) / 2 + 1 + i)));
			if (b.isCablePort())
			{
				CablePortCell newport = (CablePortCell)GraphActions.CreateVisualPortAction(graph, p, false, name);
				SchemeCablePort cport = b.getSchemeCablePort();
				newport.setSchemeCablePortId(cport.getId());
				cport.setName(name);
				GraphActions.setObjectsBackColor(graph, new Object[] {newport}, Color.white);
			}
			else
			{
				PortCell newport = (PortCell)GraphActions.CreateVisualPortAction(graph, p, true, name);
				SchemePort port = b.getSchemePort();
				newport.setSchemePortId(port.getId());
				Color c = Color.white;
				PortType ptype = port.getPortType();
				if (ptype.getSort().equals(PortTypeSort.PORTTYPESORT_THERMAL))
					c = Color.black;
				port.setName(name);
				GraphActions.setObjectsBackColor(graph, new Object[] {newport}, c);
			}
		}
		for (int i = 0; i < blockports_in.size(); i++)
		{
			BlockPortCell b = (BlockPortCell)blockports_in.get(i);
			String name = (String)b.getUserObject();
			Point p = graph.snap(new Point(grid*2-grid, grid*2+ grid*((max - blockports_in.size()) / 2 + 1 + i)));
			if (b.isCablePort())
			{
				CablePortCell newport = (CablePortCell)GraphActions.CreateVisualPortAction(graph, p, false, (String)b.getUserObject());
				SchemeCablePort cport = b.getSchemeCablePort();
				cport.setName(name);
				newport.setSchemeCablePortId(cport.getId());
				GraphActions.setObjectsBackColor(graph, new Object[] {newport}, Color.white);
			}
			else
			{
				PortCell newport = (PortCell)GraphActions.CreateVisualPortAction(graph, p, true, (String)b.getUserObject());
				SchemePort port = b.getSchemePort();
				port.setName(name);
				newport.setSchemePortId(port.getId());
				Color c = Color.white;
				PortType ptype = port.getPortType();
				if (ptype.getSort().equals(PortTypeSort.PORTTYPESORT_THERMAL))
					c = Color.black;
				GraphActions.setObjectsBackColor(graph, new Object[] {newport}, c);
			}
		}
		new GroupAction(graph).actionPerformed(new ActionEvent(graph, 0, ""));
		Object[] cells = graph.getSelectionCells();
		DeviceGroup group = (DeviceGroup)cells[0];

		if (old_devs.length == 1 && old_devs[0] instanceof DeviceGroup)
		{
			DeviceGroup old_dev = (DeviceGroup)old_devs[0];

			for (Enumeration enumeration = group.children(); enumeration.hasMoreElements();)
			{
				Object child = enumeration.nextElement();
				if (child instanceof DeviceCell)
				{
//					((DeviceCell)child).getSchemeDevice().schemePorts() = new ArrayList();
//					((DeviceCell)child).getSchemeDevice().cableports = new ArrayList();
					if (proto.getSymbol() != null)
					for (Enumeration en = old_dev.children(); en.hasMoreElements();)
					{
						Object ch = en.nextElement();
						if (ch instanceof DeviceCell)
						{
							ImageIcon icon = GraphActions.getImage(graph, (DeviceCell)ch);
							if (icon != null)
							{
								GraphActions.setImage(graph, (DeviceCell)child, new ImageIcon(icon.getImage()));
								break;
							}
						}
					}
					break;
				}
			}
		}
		SchemeProtoElement cloned_proto = group.getProtoElement();
		if (proto.devices().length == 0)
			proto.devices(cloned_proto.devices());
		group.setProtoElementId(proto.getId());
		proto.getUgoCell().setData((List)graph.getArchiveableState(cells));
		graph.setSelectionCells(new Object[0]);
		//Notifier.selectionNotify(graph.dispatcher, cells, true);
	}
}

class SetLinkModeAction extends AbstractAction
{
	SchemePanel panel;
	SchemeGraph graph;

	SetLinkModeAction(SchemePanel panel)
	{
		super(Constants.LINK_MODE);
		this.panel = panel;
		this.graph = panel.getGraph();
	}

	public void actionPerformed(ActionEvent e)
	{
		graph.mode = Constants.LINK_MODE;
	}
}

class SetPathModeAction extends AbstractAction
{
	SchemePanel panel;
	SchemeGraph graph;

	SetPathModeAction(SchemePanel panel)
	{
		super(Constants.PATH_MODE);
		this.panel = panel;
		this.graph = panel.getGraph();
	}

	public void actionPerformed(ActionEvent e)
	{
		graph.mode = Constants.PATH_MODE;
	}
}

class SetBackgroundSizeAction extends AbstractAction
{
	SchemePanel panel;
	SchemeGraph graph;
	ApplicationContext aContext;

	SetBackgroundSizeAction(ApplicationContext aContext, SchemePanel panel)
	{
		super(Constants.backgroundSize);
		this.aContext = aContext;
		this.panel = panel;
		this.graph = panel.getGraph();
	}

	public void actionPerformed(ActionEvent e)
	{
		GraphSizeFrame f = new GraphSizeFrame();
		Scheme scheme = panel.getGraph().getScheme();
		if (f.init(scheme))
		{
			scheme.width(f.newsize.width);
			scheme.height(f.newsize.height);
			aContext.getDispatcher().notify(new SchemeElementsEvent(this, graph, SchemeElementsEvent.SCHEME_CHANGED_EVENT));
			graph.setActualSize(f.newsize);
			graph.update();
		}
	}
}

class SetTopLevelModeAction extends AbstractAction
{
	SchemePanel panel;
	SchemeGraph graph;

	SetTopLevelModeAction(SchemePanel panel)
	{
		super(Constants.TOP_LEVEL_SCHEME_MODE);
		this.panel = panel;
		this.graph = panel.getGraph();
	}

	public void actionPerformed(ActionEvent e)
	{
		String topLevel = "����������� �����������";
		String normalLevel = "������ �����������";
		AComboBox box = new AComboBox(new String[] {normalLevel, topLevel});
		int res = JOptionPane.showConfirmDialog(
				panel,
				box,
				"����� ������ �����:",
				JOptionPane.OK_CANCEL_OPTION);

		if (res == JOptionPane.OK_OPTION)
			graph.setTopLevelSchemeMode(box.getSelectedItem().equals(topLevel));
	}
}

class CreateTopLevelSchemeAction extends AbstractAction
{
	SchemeGraph graph;
	SchemePanel panel;

	CreateTopLevelSchemeAction(SchemeGraph graph, SchemePanel panel)
	{
		super(Constants.createTopLevelElementKey);
		this.graph = graph;
		this.panel = panel;
	}

	public void actionPerformed(ActionEvent e)
	{
		Object[] cells = graph.getAll();
		DeviceGroup[] groups = GraphActions.findTopLevelGroups(graph, cells);

		if (groups.length == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "�� ������� �� ������ ����������", "������", JOptionPane.OK_OPTION);
			return;
		}

		ArrayList blockports_in = new ArrayList();
		ArrayList blockports_out = new ArrayList();
		BlockPortCell[] bpcs = GraphActions.findTopLevelPorts(graph, cells);

		for (int i = 0; i < bpcs.length; i++)
		{
			if (bpcs[i].isCablePort())
			{
			SchemeCablePort port = bpcs[i].getSchemeCablePort();
			if (port.directionType().equals(DirectionType._IN))
				blockports_in.add(bpcs[i]);
			else
				blockports_out.add(bpcs[i]);
		}
		else
		{
			SchemePort port = bpcs[i].getSchemePort();
			if (port.directionType().equals(DirectionType._IN))
				blockports_in.add(bpcs[i]);
			else
				blockports_out.add(bpcs[i]);
		}
		}

		// Save group as serializable_cell
		Rectangle oldrect = graph.getCellBounds(cells);
		Scheme scheme = panel.getGraph().getScheme();

		ArrayList v = new ArrayList();
		v.add(blockports_in);
		v.add(blockports_out);
		v.add(oldrect);
		v.add(scheme);

		graph.dispatcher.notify(new SchemeElementsEvent(this, v, SchemeElementsEvent.SCHEME_UGO_CREATE_EVENT));
	}
}

class CreateSchemeUgoAction
{
	SchemeGraph graph;

	CreateSchemeUgoAction(SchemeGraph graph)
	{
		this.graph = graph;
	}

	public void create(ArrayList v)
	{
		ArrayList blockports_in = (ArrayList)v.get(0);
		ArrayList blockports_out = (ArrayList)v.get(1);
//		Rectangle oldrect = (Rectangle)v.get(2);
		Scheme scheme = (Scheme)v.get(3);

		//remove old cells
		graph.setSelectionCells(new Object[0]);
		graph.getModel().remove(graph.getDescendants(graph.getAll()));

		// create new element
		int grid = graph.getGridSize();
		int max = Math.max(3, Math.max(blockports_in.size(), blockports_out.size()));
		Rectangle bounds = new Rectangle(
				graph.snap(new Point(grid*2, grid*2)),//oldrect.x, oldrect.y
				graph.snap(new Dimension(grid*4, grid*(max+1))));
		DefaultGraphCell cell = GraphActions.CreateDeviceAction(graph, scheme.label(), bounds, false, Color.black);
		graph.setSelectionCell(cell);
		for (int i = 0; i < blockports_out.size(); i++)
		{
			BlockPortCell b = (BlockPortCell)blockports_out.get(i);
			String name = (String)b.getUserObject();
			Point p = graph.snap(new Point(grid*2 + grid*5, grid*2 + grid*((max - blockports_out.size()) / 2 + 1 + i)));
			if (b.isCablePort())
			{
				CablePortCell newport = (CablePortCell)GraphActions.CreateVisualPortAction(graph, p, false, name);
				SchemeCablePort cport = b.getSchemeCablePort();
				newport.setSchemeCablePortId(cport.getId());
				cport.setName(name);
				GraphActions.setObjectsBackColor(graph, new Object[] {newport}, Color.white);
			}
			else
			{
				PortCell newport = (PortCell)GraphActions.CreateVisualPortAction(graph, p, true, name);
				SchemePort port = b.getSchemePort();
				newport.setSchemePortId(port.getId());
				Color c = Color.white;
				PortType ptype = port.getPortType();
				if (ptype.getSort().equals(PortTypeSort.PORTTYPESORT_THERMAL))
					c = Color.black;
				port.setName(name);
				GraphActions.setObjectsBackColor(graph, new Object[] {newport}, c);
			}
		}
		for (int i = 0; i < blockports_in.size(); i++)
		{
			BlockPortCell b = (BlockPortCell)blockports_in.get(i);
			Point p = graph.snap(new Point(grid*2-grid, grid*2+ grid*((max - blockports_in.size()) / 2 + 1 + i)));
			if (b.isCablePort())
			{
				CablePortCell newport = (CablePortCell)GraphActions.CreateVisualPortAction(graph, p, false, (String)b.getUserObject());
				SchemeCablePort cport = b.getSchemeCablePort();
				newport.setSchemeCablePortId(cport.getId());
				GraphActions.setObjectsBackColor(graph, new Object[] {newport}, Color.white);
			}
			else
			{
				PortCell newport = (PortCell)GraphActions.CreateVisualPortAction(graph, p, true, (String)b.getUserObject());
				SchemePort port = b.getSchemePort();
				newport.setSchemePortId(port.getId());
				Color c = Color.white;
				PortType ptype = port.getPortType();
				if (ptype.getSort().equals(PortTypeSort.PORTTYPESORT_THERMAL))
					c = Color.black;
				GraphActions.setObjectsBackColor(graph, new Object[] {newport}, c);
			}
		}

		scheme.getUgoCell().setData(createSchemeUgo());

		Object[] cells = graph.getRoots();
		DeviceGroup group = (DeviceGroup)cells[0];
		group.setSchemeId(scheme.getId());
		if (scheme.getSymbol() != null)
		{
			for (Enumeration en = group.children(); en.hasMoreElements();)
			{
				Object child = en.nextElement();
				if (child instanceof DeviceCell)
				{
					BitmapImageResource ir = scheme.getSymbol();
					if (ir != null)
					{
						ImageIcon icon = new ImageIcon(ir.getImage());
						if (icon.getIconWidth() > 20 || icon.getIconHeight() > 20)
							icon = new ImageIcon (icon.getImage().getScaledInstance(20,	20,	Image.SCALE_SMOOTH));
						GraphActions.setImage(graph, (DeviceCell)child, icon);
					}
					break;
				}
			}
		}

		graph.setSelectionCells(new Object[0]);
	}

	private List createSchemeUgo()
	{
		Object[] cells = graph.getGraphLayoutCache().order(graph.getSelectionCells());

		if (cells != null && cells.length > 0)
		{
			ArrayList new_cells = new ArrayList();
			for(int i = 0; i < cells.length; i++)
			{
				if (cells[i] instanceof DeviceCell)
				{
					DeviceCell box = (DeviceCell)cells[i];
					//if (box.getChildCount() > 1)
					{
						new_cells.add(box);
						for (Enumeration enumeration = box.children(); enumeration.hasMoreElements(); )
						{
							DefaultPort dev_port = (DefaultPort)enumeration.nextElement();
							if (GraphConstants.getOffset(dev_port.getAttributes()) != null)
							{
								if (dev_port.edges().hasNext())
								{
									DefaultEdge edge = (DefaultEdge)dev_port.edges().next();
									new_cells.add(edge);
									Object ell = DefaultGraphModel.getTargetVertex(graph.getModel(), edge);
									if (ell instanceof PortCell)
										new_cells.add(ell);
									else if (ell instanceof CablePortCell)
										new_cells.add(ell);
								}
							}
						}
					}
				}
				else if (cells[i] instanceof DeviceGroup || cells[i] instanceof DefaultLink)
					new_cells.add(cells[i]);
			}
			if (new_cells.isEmpty())
				return null;

			cells = new_cells.toArray();

			DeviceGroup group = new DeviceGroup();
			Map viewMap = new HashMap();
			//make group created unresizable
			Map m = GraphConstants.createMap();
			GraphConstants.setSizeable(m, false);
			viewMap.put(group, m);

			ParentMap map = new ParentMap();
			for (int i = 0; i < cells.length; i++)
				map.addEntry(cells[i], group);
			graph.getGraphLayoutCache().insert(new Object[] { group }, viewMap, null, map, null);

			return (List)graph.getArchiveableState(new Object[]{group});
		}
		return null;
	}
}

class CreateBlockPortAction extends AbstractAction
{
	SchemeGraph graph;

	CreateBlockPortAction(SchemeGraph graph)
	{
		super(Constants.blockPortKey);
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		Object[] cells = graph.getSelectionCells();
		if (cells.length != 1)
			return;
		Object cell = cells[0];
		if (!(cell instanceof PortCell || cell instanceof CablePortCell))
			return;

		int grid = graph.getGridSize();
		DefaultPort vport = null;
		String direction = null;
		Rectangle bounds = null;
		com.syrus.AMFICOM.general.Identifier port_id = null;
		String name = "";
		boolean is_cable = false;

		if (cell instanceof PortCell)
		{
			PortCell port = (PortCell)cell;
			SchemePort sport = port.getSchemePort();
			port_id = sport.getId();
			name = sport.getName();
			for (Enumeration enumeration = port.children(); enumeration.hasMoreElements(); )
			{
				DefaultPort p = (DefaultPort)enumeration.nextElement();
				if (p.getUserObject().equals("Center"))
				{
					vport = p;
					break;
				}
			}
			if (vport == null)
				return;

			Rectangle _bounds = graph.getCellBounds(cell);
			if (sport.directionType().equals(DirectionType._IN))
				bounds = new Rectangle(
						new Point(_bounds.x - 6*grid, _bounds.y - 2),
						new Dimension(grid*3, _bounds.height + 4));
			else
				bounds = new Rectangle(
						new Point(_bounds.x + _bounds.width + 3*grid, _bounds.y - 2),
						new Dimension(grid*3, _bounds.height + 4));
		}
		else if (cell instanceof CablePortCell)
		{
			CablePortCell port = (CablePortCell)cell;
			SchemeCablePort scport = port.getSchemeCablePort();
			port_id = scport.getId();
			name = scport.getName();
			for (Enumeration enumeration = port.children(); enumeration.hasMoreElements(); )
			{
				DefaultPort p = (DefaultPort)enumeration.nextElement();
				if (p.getUserObject().equals("Center"))
				{
					vport = p;
					break;
				}
			}
			if (vport == null)
				return;

			Rectangle _bounds = graph.getCellBounds(cell);
			if (scport.directionType().equals(DirectionType._IN))
				bounds = new Rectangle(
						new Point(_bounds.x - 6*grid, _bounds.y - 2),
						new Dimension(grid*3, _bounds.height + 4));
			else
				bounds = new Rectangle(
						new Point(_bounds.x + _bounds.width + 3*grid, _bounds.y - 2),
						new Dimension(grid*3, _bounds.height + 4));
			is_cable = true;
		}

		BlockPortCell blockport = addVisualBlockPort(graph, name, bounds, is_cable, port_id, direction);
		Map edgemap = GraphConstants.createMap();
		int u = GraphConstants.PERCENT;
		Point p;
		if (direction.equals("in"))
			p = new Point (u, u / 2);
		else
			p = new Point (0, u / 2);
		Port bpcPort = GraphActions.addPort (graph, "", blockport, p);

		ArrayList list = new ArrayList();
		list.add(p);
		list.add(new Point(bounds.x + bounds.width, p.y));

		GraphConstants.setPoints(edgemap, list);
		GraphConstants.setLineEnd(edgemap, GraphConstants.ARROW_NONE);
		GraphConstants.setEndFill(edgemap, false);
		GraphConstants.setDisconnectable(edgemap, false);

		Map viewMap = new HashMap();
		DefaultEdge edge = new DefaultEdge("");

		viewMap.put(edge, edgemap);
		Object[] insert = new Object[] { edge };
		ConnectionSet cs = new ConnectionSet();
		cs.connect(edge, bpcPort, false);
		cs.connect(edge, vport, true);

		graph.getModel().insert(insert, viewMap, cs, null, null);
	}

	static BlockPortCell addVisualBlockPort(
			SchemeGraph graph,
			Object userObject,
			Rectangle bounds,
			boolean is_cable,
			com.syrus.AMFICOM.general.Identifier scheme_port_id,
			String direction)
	{
		Font f = graph.getFont();

		Map viewMap = new HashMap();
		Map map;

		// Create Vertex
		BlockPortCell cell = new BlockPortCell(userObject, is_cable);
		cell.setSchemePortId(scheme_port_id);

		map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setBackground(map, new Color(220, 255, 255));
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setFontName(map, f.getName());
		GraphConstants.setFontSize(map, f.getSize() - 2);
		GraphConstants.setFontStyle(map, f.getStyle());
		GraphConstants.setBorderColor(map, graph.defaultBorderColor);
		viewMap.put(cell, map);

		int u = GraphConstants.PERCENT;
		DefaultPort port = new DefaultPort("");
		map = GraphConstants.createMap();
		Point p;
		if (direction.equals("in"))
			p = new Point(u, u / 2);
		else
			p = new Point(0, u / 2);
		GraphConstants.setOffset(map, p);
		GraphConstants.setConnectable(map, false);
		GraphConstants.setDisconnectable(map, false);
		viewMap.put(port, map);
		cell.add(port);

		graph.getGraphLayoutCache().insert(new Object[]{cell}, viewMap, null, null, null);
		return cell;
	}
}

class HierarchyUpAction extends AbstractAction
{
	SchemeGraph graph;

	HierarchyUpAction (SchemeGraph graph)
	{
		super(Constants.hierarchyUpKey);
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		Object[] cells = graph.getSelectionCells();
		if (cells.length == 1 && cells[0] instanceof DeviceGroup)
		{
			DeviceGroup group = (DeviceGroup)cells[0];
			SchemeProtoElement proto = group.getProtoElement();
			//if (!proto.extended_state)
			{
				graph.setSelectionCells(new Object[0]);
				graph.getModel().remove(graph.getDescendants(graph.getAll()));
				cells = graph.setFromArchivedState(proto.getSchemeCell().getData());
			//proto.extended_state = true;
			}
		}
	}
}
/*
class InsertIntoLibraryAction extends AbstractAction
{
	SchemeGraph graph;

	InsertIntoLibraryAction(SchemeGraph graph)
	{
		super(Constants.insertIntoLibraryKey);
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		Object[] cells = graph.getSelectionCells();
		if (cells != null && cells.length == 1)
		{
			if (cells[0] instanceof DeviceGroup)
			{
				ProtoElement proto = (ProtoElement)((DeviceGroup)cells[0]).getProtoElement();
				if (proto.equipment_type_id.equals(""))
				{
					System.out.println("Error! Equipment_type_id is empty.");
					return;
				}
				/*if (proto.extended_state)
				{
					proto.serializable_cell = graph.getArchiveableState(cells);
					proto.schemecell = proto.pack(proto.serializable_cell);
				}
				else
				{
					proto.serializable_ugo = graph.getArchiveableState(cells);
					proto.ugo = proto.pack(proto.serializable_ugo);
				}/
			//	proto.pack_cell();

				com.syrus.AMFICOM.Client.Schematics.Elements.SaveComponentDialog frame =
						new com.syrus.AMFICOM.Client.Schematics.Elements.SaveComponentDialog(graph.aContext);
				frame.init(proto, graph.aContext.getDataSourceInterface());
				return;

				//insert();
//			new SaveObjectAction(proto.serializable_cell).execute();
//			DeviceGroup cell = (DeviceGroup)new LoadObjectAction(graph).execute();
				//DirectoryToFile.writeAll();
			}
			else
				return;

		}
	}

	void insert()
	{
/*		com.jgraph.plaf.basic.TransferHandler th = graph.getTransferHandler();
		if (th instanceof BasicGraphUI.GraphTransferHandler)
		{
			BasicGraphUI.GraphTransferHandler gth = (BasicGraphUI.GraphTransferHandler) th;
			Transferable t = gth.createTransferable();
			if (t != null && libraryPanel.getTransferHandler() != null)
				libraryPanel.getTransferHandler().importData(libraryPanel,t);
		}/
	}
}
*/
class AbstractIOAction extends AbstractAction
{
	public AbstractIOAction (String name)
	{
		super(name);
	}

	public void actionPerformed(ActionEvent e)
	{
	}

	static ObjectInputStream createInputStream(String filename) throws Exception
	{
		InputStream f = new FileInputStream(filename);
		f = new GZIPInputStream(f);
		return new ObjectInputStream(f);
	}

	static ObjectOutputStream createOutputStream(String filename) throws Exception
	{
		OutputStream f = new FileOutputStream(filename);
		f = new GZIPOutputStream(f);
		return new ObjectOutputStream(f);
	}
}

class OpenLibraryAction extends AbstractIOAction
{
	SchemeGraph graph;
	GPLibraryPanel libraryPanel;
	OpenLibraryAction(SchemeGraph graph, GPLibraryPanel libraryPanel)
	{
		super(Constants.openLibraryKey);
		this.graph = graph;
		this.libraryPanel = libraryPanel;
	}

	public void actionPerformed(ActionEvent e)
	{
		JFileChooser f = new JFileChooser();
		int ret = f.showOpenDialog(null);
		if (ret == JFileChooser.APPROVE_OPTION)
		{
			String name = f.getSelectedFile().getAbsolutePath();

			if (name != null)
				try
			{
				ObjectInputStream in = createInputStream(name);
				libraryPanel.openLibrary(in.readObject());
				in.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				graph.repaint();
			}
		}
	}
}

class SaveLibraryAction extends AbstractIOAction
{
	SchemeGraph graph;
	GPLibraryPanel libraryPanel;

	SaveLibraryAction(SchemeGraph graph, GPLibraryPanel libraryPanel)
	{
		super(Constants.saveLibraryKey);
		this.graph = graph;
		this.libraryPanel = libraryPanel;
	}

	public void actionPerformed(ActionEvent e)
	{
		JFileChooser f = new JFileChooser();
		int ret = f.showSaveDialog(null);
		if (ret == JFileChooser.APPROVE_OPTION)
		{
			String name = f.getSelectedFile().getAbsolutePath();
			if (name != null)
			{
				GPLibraryPanel.ScrollablePanel panel = libraryPanel.getPanel();
				if (panel != null)
				{
					Serializable s = panel.getArchiveableState();
					try
					{
						ObjectOutputStream out = createOutputStream(name);
						out.writeObject(s);
						out.flush();
						out.close();
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
					finally
					{
						graph.invalidate();
					}
				}
			}
		}
	}
}

class RenameLibraryAction extends AbstractAction
{
	SchemeGraph graph;
	GPLibraryPanel libraryPanel;

	RenameLibraryAction(SchemeGraph graph, GPLibraryPanel libraryPanel)
	{
		super(Constants.renameLibraryKey);
		this.graph = graph;
		this.libraryPanel = libraryPanel;
	}

	public void actionPerformed(ActionEvent e)
	{
		String name = libraryPanel.getPanel().toString();
		name = JOptionPane.showInputDialog(Environment.getActiveWindow(), "Rename " + name);
		if (name != null && name.length() > 0)
		{
			libraryPanel.getPanel().setName(name);
			graph.repaint();
		}
	}
}

class CloseLibraryAction extends AbstractAction
{
	SchemeGraph graph;
	GPLibraryPanel libraryPanel;

	CloseLibraryAction(SchemeGraph graph, GPLibraryPanel libraryPanel)
	{
		super(Constants.closeLibraryKey);
		this.graph = graph;
		this.libraryPanel = libraryPanel;
	}

	public void actionPerformed(ActionEvent e)
	{
		libraryPanel.closeLibrary();
	}
}

class SaveAction extends AbstractIOAction
{
	SchemeGraph graph;

	SaveAction(SchemeGraph graph)
	{
		super(Constants.saveKey);
		this.graph = graph;
	}

	SaveAction(SchemeGraph graph, String nm)
	{
		super(nm);
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent e)
	{
		JFileChooser f = new JFileChooser();
		int ret = f.showSaveDialog(null);
		if (ret == JFileChooser.APPROVE_OPTION)
		{
			String name = f.getSelectedFile().getAbsolutePath();
			if (name != null)
			{
				try
				{
					ObjectOutputStream out = createOutputStream(name);
					out.writeObject(graph.getArchiveableState());
					out.flush();
					out.close();
//					graph.modified = false;
				}
				catch (Exception ex)
				{
					ex.printStackTrace() ;
				}
				finally
				{
//					graph.update();
					graph.invalidate();
				}
			}
		}
	}
}

class SaveObjectAction extends AbstractIOAction
{
	Serializable obj;

	SaveObjectAction(Serializable obj)
	{
		super(Constants.saveKey);
		this.obj = obj;
	}

	public void execute()
	{
		JFileChooser f = new JFileChooser();
		int ret = f.showSaveDialog(null);
		if (ret == JFileChooser.APPROVE_OPTION)
		{
			String name = f.getSelectedFile().getAbsolutePath();
			if (name != null)
			{
				try
				{
					ObjectOutputStream out = createOutputStream(name);
					out.writeObject(obj);
					out.flush();
					out.close();
//					graph.modified = false;
				}
				catch (Exception ex)
				{
					ex.printStackTrace() ;
				}
				finally
				{
				}
			}
		}
	}
}

