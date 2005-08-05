/*
 * $Id: SchemeActions.java,v 1.17 2005/08/05 12:39:59 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.Port;
import com.jgraph.graph.PortView;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.LangModelGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortEdge;
import com.syrus.AMFICOM.client_.scheme.graph.objects.TopLevelCableLink;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.17 $, $Date: 2005/08/05 12:39:59 $
 * @module schemeclient_v1
 */

public class SchemeActions {
	private static final String EMPTY = ""; //$NON-NLS-1$
	
	private SchemeActions () {
		// empty
	}
	
	public static DeviceGroup createTopLevelElement(SchemeGraph graph,
			Object userObject, Rectangle bounds, SchemeElement element) {
		Map viewMap = new HashMap();
		DeviceGroup cell = DeviceGroup.createInstance(userObject, viewMap, element.getId(), DeviceGroup.SCHEME_ELEMENT);

		Object[] insert = new Object[] { cell };
		graph.getGraphLayoutCache().insert(insert, viewMap, null, null, null);
		graph.setSelectionCells(insert);
		return cell;
	}

	public static TopLevelCableLink createTopLevelCableLink(SchemeGraph graph, Port firstPort,
			Port port, Point p, Point p2) {
		Map viewMap = new HashMap();
		ConnectionSet cs = new ConnectionSet();
		TopLevelCableLink cell = TopLevelCableLink.createInstance(EMPTY, firstPort, 
				port, p, p2, viewMap, cs);
		
		graph.getModel().insert(new Object[] {cell}, viewMap, cs, null, null);

		graph.setSelectionCell(cell);
		return cell;
	}
	
	public static void generateTopLevelScheme(SchemeGraph graph)
	{
		Map oldToNewMap = new HashMap();
		Map map2 = new HashMap();
		Object[] cells = graph.getRoots();

		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DeviceGroup)
			{
				DeviceGroup group = (DeviceGroup)cells[i];
				if (!isCableGroup(group))
				{
					Rectangle r = graph.toScreen(new Rectangle(GraphActions.getGroupBounds(graph, group).getLocation(),
							 new Dimension(4 * graph.getGridSize(), 6 * graph.getGridSize())));

					DeviceGroup newGroup = createTopLevelElement(graph, EMPTY, r, group.getSchemeElement());

					if (newGroup != null)
						oldToNewMap.put(group, newGroup);
				}
			}
		}
		graph.getModel().remove(graph.getDescendants(cells));
		
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] instanceof DefaultCableLink) {
				DefaultCableLink link = (DefaultCableLink)cells[i];
				DeviceGroup start = null;
				DeviceGroup end = null;
				if (link.getSource() instanceof DefaultPort &&
						((DefaultPort)link.getSource()).getParent().getParent() instanceof DeviceGroup)
					start = (DeviceGroup)((DefaultPort)link.getSource()).getParent().getParent();
				if (link.getTarget() instanceof DefaultPort &&
						((DefaultPort)link.getTarget()).getParent().getParent() instanceof DeviceGroup)
					end = (DeviceGroup)((DefaultPort)link.getTarget()).getParent().getParent();

				boolean b1 = !isCableGroup(start);
				boolean b2 = !isCableGroup(end);
				if (b1 && b2) {
					DeviceGroup newStart = (DeviceGroup)oldToNewMap.get(start);
					DeviceGroup newEnd = (DeviceGroup)oldToNewMap.get(end);
					createTopLevelCableLink(graph,
							(Port)newStart.getFirstChild(),
							(Port)newEnd.getFirstChild(),
							graph.getCellBounds(newStart).getLocation(),
							graph.getCellBounds(newEnd).getLocation());
				} else if (b1) {
					DeviceGroup newStart = (DeviceGroup)oldToNewMap.get(start);
					DeviceGroup newEnd = (DeviceGroup)map2.get(end);
					if (newEnd == null)
						map2.put(start, newStart);
					else
						createTopLevelCableLink(graph,
							(Port)newStart.getFirstChild(),
							(Port)newEnd.getFirstChild(),
							graph.getCellBounds(newStart).getLocation(),
							graph.getCellBounds(newEnd).getLocation());
				} else if (b2) {
					DeviceGroup newEnd = (DeviceGroup)oldToNewMap.get(end);
					DeviceGroup newStart = (DeviceGroup)map2.get(start);
					if (newStart == null)
						map2.put(end, newEnd);
					else
						createTopLevelCableLink(graph,
							(Port)newStart.getFirstChild(),
							(Port)newEnd.getFirstChild(),
							graph.getCellBounds(newStart).getLocation(),
							graph.getCellBounds(newEnd).getLocation());
				}
			}
		}
	}

	static boolean isCableGroup(DeviceGroup group)
	{
		if (group.getScheme() == null)
			return false;
		if (group.getScheme().getKind().equals(IdlKind.CABLE_SUBNETWORK))
			return true;
		return false;
	}
	
	public static DefaultCableLink[] splitCableLink(SchemeGraph graph, DefaultCableLink cell) {//DefaultCableLink[]
//		DefaultPort source = (DefaultPort)cell.getSource(); 
//		DefaultPort target = (DefaultPort)cell.getTarget();
//		PortView source = (PortView)cell.getSource(); 
//		PortView target = (PortView)cell.getTarget();
		int grid = graph.getGridSize();
		
		SchemeCableLink cableLink = cell.getSchemeCableLink();
		try {
			// TODO create 2 SchemeCableLinks and init them with cableLink properties and characteristics
			// clone???
			SchemeCableLink cl1 = SchemeObjectsFactory.createSchemeCableLink(cableLink.getName(), cableLink.getParentScheme());
			SchemeCableLink cl2 = SchemeObjectsFactory.createSchemeCableLink(cableLink.getName(), cableLink.getParentScheme());

			cl1.setAbstractLinkType(cableLink.getAbstractLinkType());
			cl1.setDescription(cableLink.getDescription());
			cl1.setOpticalLength(cableLink.getOpticalLength() / 2);
			cl1.setPhysicalLength(cableLink.getPhysicalLength() / 2);
			cl1.setOpticalLength(cableLink.getOpticalLength() / 2);

			cl2.setAbstractLinkType(cableLink.getAbstractLinkType());
			cl2.setDescription(cableLink.getDescription());
			cl2.setOpticalLength(cableLink.getOpticalLength() / 2);
			cl2.setPhysicalLength(cableLink.getPhysicalLength() / 2);
			cl2.setOpticalLength(cableLink.getOpticalLength() / 2);

			
			EdgeView view = (EdgeView)graph.getGraphLayoutCache().getMapping(cell, false);
			PortView source = view.getSource();
			PortView target = view.getTarget();
			List<Point> points = view.getPoints();
			Point left = graph.snap(points.get(0));
			Point right = graph.snap(points.get(points.size() - 1));
			int x = (left.x + right.x) / 2;
			int y = (left.y + right.y) / 2;
			Point middle1 = graph.snap(new Point (x - 3 * grid, y));
			Point middle2 = graph.snap(new Point (x + 3 * grid, y));
			
			DeleteAction.delete(graph, cell);
			DefaultCableLink cell1 = createCableLink(graph, source, null, left, middle1, cl1.getId());
			DefaultCableLink cell2 = createCableLink(graph, null, target, middle2, right, cl2.getId());
//			if (source != null && source.getParent() instanceof CablePortCell) {
//				CablePortCell port = (CablePortCell)source.getParent();
//				connectSchemeCableLink(graph, cell1, port, true);
//			}
//			if (target != null && target.getParent() instanceof CablePortCell) {
//				CablePortCell port = (CablePortCell)target.getParent();
//				connectSchemeCableLink(graph, cell2, port, false);
//			}
			
			GraphActions.setText(graph, cell1, cl1.getName());
			GraphActions.setText(graph, cell2, cl2.getName());

			return new DefaultCableLink[] { cell1, cell2 };
		} catch (CreateObjectException e) {
			Log.errorException(e);
			return null;
		}
	}
	
	public static DefaultCableLink createCableLink(SchemeGraph graph, PortView firstPort,
			PortView port, Point p, Point p2, Identifier linkId) {
		ConnectionSet cs = new ConnectionSet();
		Map viewMap = new HashMap();
		
		Object[] cells = graph.getAll();
		int counter = 0;
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DefaultCableLink)
				counter++;
		String name = "cl" + String.valueOf(counter+1);
		
		DefaultCableLink cell = DefaultCableLink.createInstance(name, firstPort, port, p, p2, viewMap, cs);
		cell.setSchemeCableLinkId(linkId);
		graph.getModel().insert(new Object[] { cell }, viewMap, cs, null, null);
		graph.setSelectionCell(cell);
		return cell;
	}

	public static DeviceCell createDevice(SchemeGraph graph, Object userObject, Rectangle bounds, Identifier deviceId) {
		Map viewMap = new HashMap();
		DeviceCell cell = DeviceCell.createInstance(userObject, bounds, viewMap);
		cell.setSchemeDeviceId(deviceId);
		Object[] insert = new Object[] { cell };
		graph.getGraphLayoutCache().insert(insert, viewMap, null, null, null);
		graph.setSelectionCells(insert);
		return cell;
	}
	
	public static DefaultLink createLink(SchemeGraph graph, PortView firstPort, 
			PortView port, Point p, Point p2, Identifier linkId) {
		ConnectionSet cs = new ConnectionSet();
		Map viewMap = new HashMap();
		
		Object[] cells = graph.getAll();
		int counter = 0;
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DefaultLink)
				counter++;
		String name = "l" + String.valueOf(counter+1);
		
		DefaultLink cell = DefaultLink.createInstance(name, firstPort, port, p, p2, viewMap, cs);
		cell.setSchemeLinkId(linkId);
		graph.getModel().insert(new Object[] { cell }, viewMap, cs, null, null);
		graph.setSelectionCell(cell);
		return cell;
	}

	public static PortCell createPort(SchemeGraph graph, DeviceCell deviceCell, Point p, String name, IdlDirectionType direction, Color color, Identifier portId) {
		DefaultGraphCell port = createAbstractPort(graph, deviceCell, p, name, direction, false, color, portId);
		return (PortCell)port;
	}
	
	public static CablePortCell createCablePort(SchemeGraph graph, DeviceCell deviceCell, Point p, String name, IdlDirectionType direction, Color color, Identifier portId) {
		DefaultGraphCell port = createAbstractPort(graph, deviceCell, p, name, direction, true, color, portId);
		return (CablePortCell)port;
	}
	
	private static DefaultGraphCell createAbstractPort(SchemeGraph graph, DeviceCell deviceCell, Point p, String name, IdlDirectionType direction, boolean isCable, Color color, Identifier portId) {
		DefaultGraphCell visualPort;
		DefaultPort ellipsePort;
		Port devPort;
		
		Map m = graph.getModel().getAttributes(deviceCell);
		Rectangle dev_bounds = GraphConstants.getBounds(m);
		
		int u = GraphConstants.PERCENT;
		int distance = (direction.equals(IdlDirectionType._OUT) ?
				(p.x - (dev_bounds.x + dev_bounds.width)) / graph.getGridSize() + 1 :
				(dev_bounds.x - p.x) / graph.getGridSize());
		Point labelPosition = (direction.equals(IdlDirectionType._OUT) ? 
				new Point (-2 * u / distance, 0) : 
				new Point (u + (int)(1.5 * u / distance), 0));
		Rectangle portCellBounds = (direction.equals(IdlDirectionType._OUT) ? 
				new Rectangle(p.x - 6, p.y - 3, 7, 7) : 
				new Rectangle(p.x, p.y - 3, 7, 7));
		Point devportPos = (direction.equals(IdlDirectionType._OUT) ?
				new Point(u, (int)(u * ( (double)(p.y + 1 - dev_bounds.y) / (double)dev_bounds.height))) :		
				new Point(0, (int)(u * ( (double)(p.y + 1 - dev_bounds.y) / (double)dev_bounds.height))));
		Point ellipseportPos = (direction.equals(IdlDirectionType._OUT) ?
				new Point(0, u / 2) :
				new Point(u, u / 2));
		
		Map viewMap = new HashMap();
	
		if (!isCable) {
			visualPort = PortCell.createInstance(EMPTY, portCellBounds, viewMap, direction, color);
			((PortCell)visualPort).setSchemePortId(portId);
		} else { // cableport
			visualPort = CablePortCell.createInstance(EMPTY, portCellBounds, viewMap, direction, color);
			((CablePortCell)visualPort).setSchemeCablePortId(portId);
		}
		graph.getGraphLayoutCache().insert(new Object[] { visualPort }, viewMap, null, null, null);
		devPort = GraphActions.addPort (graph, EMPTY, deviceCell, devportPos); //$NON-NLS-1$
		ellipsePort = GraphActions.addPort (graph, EMPTY, visualPort, ellipseportPos); //$NON-NLS-1$
		
		ConnectionSet cs = new ConnectionSet();
		PortEdge edge = PortEdge.createInstance(name, devPort, ellipsePort, p, new Point(dev_bounds.x
					+ dev_bounds.width, p.y), labelPosition, viewMap, cs);
		

		graph.getModel().insert(new Object[] { edge }, viewMap, cs, null, null);
		graph.addSelectionCell(visualPort);

		return visualPort;
	}
	
	public static final long SCHEME_EMPTY	=									0x00000001;
	public static final long SCHEME_HAS_UNGROUPED_DEVICE	=	0x00000002;
	public static final long SCHEME_HAS_LINK	=							0x00000004;
	public static final long SCHEME_HAS_HIERARCHY_PORT	=		0x00000008;
	public static final long SCHEME_HAS_DEVICE_GROUP	=		0x00000010;
	
	public static long getGraphState(SchemeGraph graph) {
		long status = 0;
		
		Object[] cells = graph.getAll();

		if (cells == null || cells.length == 0) {
			status |= SCHEME_EMPTY;
		} else {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] instanceof DefaultLink) {
					status |= SCHEME_HAS_LINK;
				} else if (cells[i] instanceof BlockPortCell) {
					status |= SCHEME_HAS_HIERARCHY_PORT;
				} else if (cells[i] instanceof DeviceCell && !GraphActions.hasGroupedParent(cells[i])) {
					status |= SCHEME_HAS_UNGROUPED_DEVICE;
				} else if (cells[i] instanceof DeviceGroup) {
					status |= SCHEME_HAS_DEVICE_GROUP;
				} 
			}
		}
		return status;
	}
	
	public static Color determinePortColor(AbstractSchemePort port) {
		if (port.getAbstractSchemeLink() == null)
			return Color.YELLOW;
		if (port.getPortType().getSort().equals(PortTypeSort.PORTTYPESORT_THERMAL))
			return Color.BLACK;
		return Color.WHITE;
	}
	
	public static JPopupMenu createElementPopup(final ApplicationContext aContext,
			final SchemeGraph graph, DeviceGroup group) {
		final SchemeElement se = group.getSchemeElement();

		JPopupMenu pop = new JPopupMenu();
		if (group.getScheme() != null) {
			final Scheme sc = group.getScheme();

			JMenuItem menu1 = new JMenuItem(new AbstractAction() {
				private static final long serialVersionUID = 8641829415106895132L;

				public void actionPerformed(ActionEvent ev) {
					aContext.getDispatcher().firePropertyChange(
							new SchemeEvent(this, sc, SchemeEvent.OPEN_SCHEME));

					if (se != null && se.isAlarmed())
						aContext.getDispatcher().firePropertyChange(
								new SchemeEvent(this, se,
										SchemeEvent.CREATE_ALARMED_LINK));
				}
			});
			menu1.setText(LangModelGraph.getString("open_scheme")); //$NON-NLS-1$
			pop.add(menu1);
		}
		
		if (group.getType() == DeviceGroup.SCHEME_ELEMENT) {
			List v = null;
			if (se.getUgoCell() != null)
				v = se.getUgoCell().getData();
			if (!se.getSchemeElements().isEmpty()
					&& (v != null && v.size() != 0 && ((Object[]) v.get(0)).length != 0)) {
				JMenuItem menu1 = new JMenuItem(new AbstractAction() {
					private static final long serialVersionUID = 7612382099522511230L;

					public void actionPerformed(ActionEvent ev) {
						aContext.getDispatcher().firePropertyChange(
								new SchemeEvent(this, se, SchemeEvent.OPEN_SCHEMEELEMENT));

						if (se.isAlarmed())
							aContext.getDispatcher().firePropertyChange(
									new SchemeEvent(this, se,
											SchemeEvent.CREATE_ALARMED_LINK));
					}
				});
				menu1.setText(LangModelGraph.getString("open_component")); //$NON-NLS-1$
				pop.addSeparator();
				pop.add(menu1);
			}
		}
		return pop;
	}
	
	public static Object search(SchemeGraph graph, String str) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DefaultGraphCell) {
				Object obj = ((DefaultGraphCell)cells[i]).getUserObject();
				if (obj instanceof String) {
					String objstr = (String)obj;
					if (objstr.startsWith(str))
						return cells[i];
				}
			}
		return null;
	}

	public static DeviceGroup findGroupById(SchemeGraph graph,
			Identifier id) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DeviceGroup)
				if (id.equals(((DeviceGroup)cells[i]).getElementId()))
					return (DeviceGroup) cells[i];
		return null;
	}

	public static PortCell findPortCellById(SchemeGraph graph, Identifier schemePortId) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof PortCell)
				if (((PortCell) cells[i]).getSchemePortId().equals(schemePortId))
					return (PortCell) cells[i];
		return null;
	}
	
	public static BlockPortCell findBlockPortCellById(SchemeGraph graph, Identifier id) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof BlockPortCell) {
				BlockPortCell bpc = (BlockPortCell)cells[i];
				if (id.equals(bpc.getAbstractSchemePortId()))
					return (BlockPortCell) cells[i];
			}
		return null;
	}

	public static PortCell findAccessPortById(SchemeGraph graph, Identifier aportId) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof PortCell)
				if (!((PortCell) cells[i]).getSchemePortId().equals(EMPTY))
					if (((PortCell) cells[i]).getSchemePort().getMeasurementPort() != null
							&& ((PortCell) cells[i]).getSchemePort().getMeasurementPort()
									.getId().equals(aportId))
						return (PortCell) cells[i];
		return null;
	}

	public static CablePortCell findCablePortCellById(SchemeGraph graph,
			Identifier scheme_cablePortId) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof CablePortCell)
				if (((CablePortCell) cells[i]).getSchemeCablePortId().equals(
						scheme_cablePortId))
					return (CablePortCell) cells[i];
		return null;
	}

	public static DefaultLink findSchemeLinkById(SchemeGraph graph,
			Identifier schemeLinkId) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DefaultLink)
				if (((DefaultLink) cells[i]).getSchemeLinkId().equals(schemeLinkId))
					return (DefaultLink) cells[i];
		return null;
	}

	public static DefaultCableLink findSchemeCableLinkById(SchemeGraph graph,
			Identifier scheme_cableLinkId) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DefaultCableLink)
				if (((DefaultCableLink) cells[i]).getSchemeCableLinkId().equals(
						scheme_cableLinkId))
					return (DefaultCableLink) cells[i];
		return null;
	}

	public static boolean connectSchemeLink(SchemeGraph graph, DefaultLink link,
			PortCell port, boolean is_source) {
		SchemePort sp = port.getSchemePort();
		if (sp == null) {
			System.err.println("GraphActions.connectSchemeLink() port not found "
					+ port.getSchemePortId());
			return false;
		}
		SchemeLink sl = link.getSchemeLink();
		if (sl == null) {
			System.err.println("GraphActions.connectSchemeLink() link not found "
					+ link.getSchemeLinkId());
			return false;
		}

		// TODO externalyze
		if (sp.getAbstractSchemeLink() != null && !sp.getAbstractSchemeLink().equals(sl)) {
			String message = "К порту " + sp.getName()
					+ " уже подключена линия связи " + sp.getAbstractSchemeLink().getName() + ".\n";
			message += "Изменить подключенную линию на " + sl.getName() + "?";
			// int res = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
			// message, "Подтверждение", JOptionPane.YES_NO_OPTION);
			// if (res == JOptionPane.NO_OPTION)
			// return false;
		}

		if (is_source)
			sl.setSourceAbstractSchemePort(sp);
		else
			sl.setTargetAbstractSchemePort(sp);

		PortType pt = sp.getPortType();
		if (pt == null)
			GraphActions.setObjectBackColor(graph, port, Color.red);
		else if (pt.getSort().equals(PortTypeSort.PORTTYPESORT_THERMAL))
			GraphActions.setObjectBackColor(graph, port, Color.black);
		else
			GraphActions.setObjectBackColor(graph, port, Color.white);

		return true;
	}

	public static void disconnectSchemeLink(SchemeGraph graph, DefaultLink link,
			PortCell port, boolean is_source) {
		SchemeLink sl = link.getSchemeLink();
		if (sl == null) {
			System.err.println("GraphActions.disconnectSchemeLink() link not found "
					+ link.getSchemeLinkId());
			return;
		}
		SchemePort sp;
		if (is_source) {
			sp = sl.getSourceAbstractSchemePort();
			sl.setSourceAbstractSchemePort(null);
		} 
		else {
			sp = sl.getTargetAbstractSchemePort();
			sl.setTargetAbstractSchemePort(null);
		}
		if (sp != null && port != null) {
			PortType pt = sp.getPortType();
			if (pt == null)
				GraphActions.setObjectBackColor(graph, port, Color.red);
			else
				GraphActions.setObjectBackColor(graph, port, Color.yellow);
		}
	}

	public static boolean connectSchemeCableLink(SchemeGraph graph,
			DefaultCableLink link, CablePortCell port, boolean is_source) {
		SchemeCablePort sp = port.getSchemeCablePort();
		if (sp == null) {
			System.err
					.println("GraphActions.connectSchemeCableLink() port not found "
							+ port.getSchemeCablePortId());
			return false;
		}
		SchemeCableLink sl = link.getSchemeCableLink();
		if (sl == null) {
			System.err
					.println("GraphActions.connectSchemeCableLink() link not found "
							+ link.getSchemeCableLinkId());
			return false;
		}
		// TODO externalyze
		if (sp.getAbstractSchemeLink() != null && !sp.getAbstractSchemeLink().equals(sl)) {
			String message = "К порту " + sp.getName()
					+ " уже подключена линия связи " + sp.getAbstractSchemeLink().getName()
					+ ".\n";
			message += "Изменить подключенную линию на " + sl.getName() + "?";

			// int res = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
			// message, "Предупреждение", JOptionPane.YES_NO_OPTION);
			// if (res == JOptionPane.NO_OPTION)
			// return false;
		}

		if (is_source)
			sl.setSourceAbstractSchemePort(sp);
		else
			sl.setTargetAbstractSchemePort(sp);

		PortType pt = sp.getPortType();
		if (pt == null)
			GraphActions.setObjectBackColor(graph, port, Color.red);
		else
			GraphActions.setObjectBackColor(graph, port, Color.white);

		return true;
	}

	public static void disconnectSchemeCableLink(SchemeGraph graph,
			DefaultCableLink link, CablePortCell port, boolean is_source) {
		SchemeCableLink sl = link.getSchemeCableLink();
		if (sl == null) {
			System.err
					.println("GraphActions.disconnectSchemeCableLink() link not found "
							+ link.getSchemeCableLinkId());
			return;
		}
		SchemeCablePort sp;
		if (is_source) {
			sp = sl.getSourceAbstractSchemePort();
			sl.setSourceAbstractSchemePort(null);
		} else {
			sp = sl.getTargetAbstractSchemePort();
			sl.setTargetAbstractSchemePort(null);
		}

		PortType pt = sp.getPortType();
		if (pt == null && port != null)
			GraphActions.setObjectBackColor(graph, port, Color.red);
		else
			GraphActions.setObjectBackColor(graph, port, Color.yellow);
	}
}
