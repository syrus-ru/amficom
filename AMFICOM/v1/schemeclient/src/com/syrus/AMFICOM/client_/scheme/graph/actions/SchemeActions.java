/*
 * $Id: SchemeActions.java,v 1.1 2005/04/05 14:07:53 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:53 $
 * @module schemeclient_v1
 */

public class SchemeActions {
	private SchemeActions () {
		// empty
	}
	
	public static DeviceGroup createTopLevelElement(SchemeGraph graph,
			Object userObject, Rectangle bounds, SchemeElement element) {
		Map viewMap = new HashMap();
		DeviceGroup cell = DeviceGroup.createInstance(userObject, viewMap, element);

		Object[] insert = new Object[] { cell };
		graph.getGraphLayoutCache().insert(insert, viewMap, null, null, null);
		graph.setSelectionCells(insert);
		return cell;
	}

	public static TopLevelCableLink createTopLevelCableLink(SchemeGraph graph, Port firstPort,
			Port port, Point p, Point p2) {
		Map viewMap = new HashMap();
		ConnectionSet cs = new ConnectionSet();
		TopLevelCableLink cell = TopLevelCableLink.createInstance("", firstPort, 
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

					DeviceGroup newGroup = createTopLevelElement(graph, "", r, group.getSchemeElement());

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
				}
				else if (b1) {
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
				}
				else if (b2) {
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
		if (group.getScheme().getSchemeKind().equals(SchemeKind.CABLE_SUBNETWORK))
			return true;
		return false;
	}
	
	public static SchemeCableLink createCableLink(SchemeGraph graph, PortView firstPort,
			PortView port, Point p, Point p2) {
		try {
			SchemeCableLink link = SchemeObjectsFactory.createCableLink();
			
			ConnectionSet cs = new ConnectionSet();
			Map viewMap = new HashMap();
			
			Object[] cells = graph.getAll();
			int counter = 0;
			for (int i = 0; i < cells.length; i++)
				if (cells[i] instanceof DefaultCableLink)
					counter++;
			String name = "cl" + String.valueOf(counter+1);
			
			DefaultCableLink cell = DefaultCableLink.createInstance(name, firstPort, port, p, p2, viewMap, cs, link);
			graph.getModel().insert(new Object[] { cell }, viewMap, cs, null, null);
			graph.setSelectionCell(cell);
			return link;
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			return null;
		}
	}

	public static DeviceCell createDevice(SchemeGraph graph, Object userObject, Rectangle bounds) {
		try {
			SchemeDevice device = SchemeObjectsFactory.createDevice();
			
			Map viewMap = new HashMap();
			DeviceCell cell = DeviceCell.createInstance(userObject, bounds, viewMap, device);
			Object[] insert = new Object[] { cell };
			graph.getGraphLayoutCache().insert(insert, viewMap, null, null, null);
			graph.setSelectionCells(insert);
			return cell;
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			return null;
		}
	}
	
	public static SchemeLink createLink(SchemeGraph graph, PortView firstPort, 
			PortView port, Point p, Point p2) {
		try {
			SchemeLink link = SchemeObjectsFactory.createLink();
			
			ConnectionSet cs = new ConnectionSet();
			Map viewMap = new HashMap();
			
			Object[] cells = graph.getAll();
			int counter = 0;
			for (int i = 0; i < cells.length; i++)
				if (cells[i] instanceof DefaultLink)
					counter++;
			String name = "l" + String.valueOf(counter+1);
			
			DefaultLink cell = DefaultLink.createInstance(name, firstPort, port, p, p2, viewMap, cs, link);
			graph.getModel().insert(new Object[] { cell }, viewMap, cs, null, null);
			graph.setSelectionCell(cell);
			return link;
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			return null;
		}
	}
	
	public static DefaultGraphCell createAbstractPort(SchemeGraph graph, DeviceCell deviceCell, Point p, AbstractSchemePort schemePort) {
		DefaultGraphCell visualPort;
		DefaultPort ellipsePort;
		Port devPort;
		
		Map m = graph.getModel().getAttributes(deviceCell);
		Rectangle dev_bounds = GraphConstants.getBounds(m);
		
		int u = GraphConstants.PERCENT;
		int distance = (schemePort.getAbstractSchemePortDirectionType().equals(AbstractSchemePortDirectionType._OUT) ?
				(p.x - (dev_bounds.x + dev_bounds.width)) / graph.getGridSize() + 1 :
				(dev_bounds.x - p.x) / graph.getGridSize());
		Point labelPosition = (schemePort.getAbstractSchemePortDirectionType().equals(AbstractSchemePortDirectionType._OUT) ? 
				new Point (-u / distance, 0) : 
				new Point (u + (u / distance), 0));
		Rectangle portCellBounds = (schemePort.getAbstractSchemePortDirectionType().equals(AbstractSchemePortDirectionType._OUT) ? 
				new Rectangle(p.x - 6, p.y - 3, 7, 7) : 
				new Rectangle(p.x, p.y - 3, 7, 7));
		Point devportPos = (schemePort.getAbstractSchemePortDirectionType().equals(AbstractSchemePortDirectionType._OUT) ?
				new Point(u, (int)(u * ( (double)(p.y + 1 - dev_bounds.y) / (double)dev_bounds.height))) :		
				new Point(0, (int)(u * ( (double)(p.y + 1 - dev_bounds.y) / (double)dev_bounds.height))));
		Point ellipseportPos = (schemePort.getAbstractSchemePortDirectionType().equals(AbstractSchemePortDirectionType._OUT) ?
				new Point(0, u / 2) :
				new Point(u, u / 2));
		
		Map viewMap = new HashMap();
	
		if (schemePort instanceof SchemePort) { // port
			visualPort = PortCell.createInstance("", portCellBounds, 
					viewMap, schemePort.getAbstractSchemePortDirectionType(), (SchemePort)schemePort);
		}
		else { // cableport
			visualPort = CablePortCell.createInstance("", portCellBounds, 
					viewMap, (SchemeCablePort)schemePort);
		}
		graph.getGraphLayoutCache().insert(new Object[] { visualPort }, viewMap, null, null, null);
		devPort = GraphActions.addPort (graph, "", deviceCell, devportPos);
		ellipsePort = GraphActions.addPort (graph, "", visualPort, ellipseportPos);
		
		ConnectionSet cs = new ConnectionSet();
		PortEdge edge = PortEdge.createInstance(schemePort.getName(), devPort, ellipsePort, p, new Point(dev_bounds.x
					+ dev_bounds.width, p.y), labelPosition, viewMap, cs);

		graph.getModel().insert(new Object[] { edge }, viewMap, cs, null, null);
		graph.addSelectionCell(visualPort);

		return visualPort;
	}
	
	public static DefaultGraphCell createAbstractPort(SchemeGraph graph, Point p, boolean isCable) {
		DeviceCell deviceCell = null;
		Object[] cells = graph.getSelectionCells();

		int counter = 0;
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DeviceCell) {
				deviceCell = (DeviceCell) cells[i];
				counter++;
			}
		if (counter != 1) // TODO: Message "must be selected one device"
			return null;
		
		/**
		 * @todo Message "device must be ungrouped"
		 * and next possibility to add ports to grouped element
		 */
		if (GraphActions.hasGroupedParent(deviceCell)) {
			Log.errorMessage("can't create PortCell as DeviceCell has parent group");
			return null;
		}
		
		Map m = graph.getModel().getAttributes(deviceCell);
		Rectangle dev_bounds = GraphConstants.getBounds(m);	
		if (dev_bounds.y > p.y || dev_bounds.y + dev_bounds.height < p.y) {
			Log.errorMessage("can't create PortCell out of vertical bounds of DeviceCell");
			return null;
		}
		
		AbstractSchemePort schemePort;
		try {
			schemePort = isCable ? SchemeObjectsFactory.createCablePort()
					: SchemeObjectsFactory.createPort();
		} catch (CreateObjectException e) {
			Log.errorException(e);
			return null;
		}
		String name = String.valueOf(((DeviceCell) cells[0]).getChildCount());
		schemePort.setName(name);	
		
		return createAbstractPort(graph, deviceCell, p, schemePort);
	}
		
	public static JPopupMenu createElementPopup(final ApplicationContext aContext,
			final SchemeGraph graph, DeviceGroup group) {
		final SchemeElement se = group.getSchemeElement();

		JPopupMenu pop = new JPopupMenu();
		if (group.getScheme() != null) {
			final Scheme sc = group.getScheme();

			JMenuItem menu1 = new JMenuItem(new AbstractAction() {
				public void actionPerformed(ActionEvent ev) {
					aContext.getDispatcher().notify(
							new OperationEvent(sc.getId(), 0, "addschemeevent"));

					if (se != null && se.isAlarmed())
						aContext.getDispatcher().notify(
								new SchemeEvent(this, se, se.getAlarmedPathElement(),
										SchemeEvent.CREATE_ALARMED_LINK));
				}
			});
			menu1.setText("Открыть схему");
			pop.add(menu1);
			pop.addSeparator();
		}
		
		if (group.getSchemeElementId() != null) {
			List v = se.getUgoCell().getData();
			if (se.getSchemeElements().isEmpty()
					|| (v != null && v.size() != 0 && ((Object[]) v.get(0)).length != 0)) {
				JMenuItem menu1 = new JMenuItem(new AbstractAction() {
					public void actionPerformed(ActionEvent ev) {
						aContext.getDispatcher().notify(
								new OperationEvent(se.getId(), 0, "addschemeelementevent"));

						if (se.isAlarmed())
							aContext.getDispatcher().notify(
									new SchemeEvent(this, se, se.getAlarmedPathElement(),
											SchemeEvent.CREATE_ALARMED_LINK));
					}
				});
				menu1.setText("Открыть компонент");
				pop.add(menu1);
			}
		}
		return pop;
	}

	public static DeviceGroup findSchemeElementById(SchemeGraph graph,
			Identifier scheme_elementId) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DeviceGroup)
				if (((DeviceGroup) cells[i]).getSchemeElementId().equals(
						scheme_elementId))
					return (DeviceGroup) cells[i];
		return null;
	}

	public static DeviceGroup findProtoElementById(SchemeGraph graph, Identifier protoId) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DeviceGroup)
				if (((DeviceGroup) cells[i]).getProtoElementId().equals(protoId))
					return (DeviceGroup) cells[i];
		return null;
	}

	public static PortCell findSchemePortById(SchemeGraph graph, Identifier schemePortId) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof PortCell)
				if (((PortCell) cells[i]).getSchemePortId().equals(schemePortId))
					return (PortCell) cells[i];
		return null;
	}

	public static PortCell findAccessPortById(SchemeGraph graph, Identifier aportId) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof PortCell)
				if (!((PortCell) cells[i]).getSchemePortId().equals(""))
					if (((PortCell) cells[i]).getSchemePort().getMeasurementPort() != null
							&& ((PortCell) cells[i]).getSchemePort().getMeasurementPort()
									.getId().equals(aportId))
						return (PortCell) cells[i];
		return null;
	}

	public static CablePortCell findSchemeCablePortById(SchemeGraph graph,
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

		if (sp.getSchemeLink() != null && !sp.getSchemeLink().equals(sl)) {
			String message = "К порту " + sp.getName()
					+ " уже подключена линия связи " + sp.getSchemeLink().getName() + ".\n";
			message += "Изменить подключенную линию на " + sl.getName() + "?";
			// int res = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
			// message, "Подтверждение", JOptionPane.YES_NO_OPTION);
			// if (res == JOptionPane.NO_OPTION)
			// return false;
		}

		if (is_source)
			sl.setSourceSchemePort(sp);
		else
			sl.setTargetSchemePort(sp);

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
			boolean is_source) {
		SchemeLink sl = link.getSchemeLink();
		if (sl == null) {
			System.err.println("GraphActions.disconnectSchemeLink() link not found "
					+ link.getSchemeLinkId());
			return;
		}
		SchemePort sp;
		if (is_source) {
			sp = sl.getSourceSchemePort();
			sl.setSourceSchemePort(null);
		} 
		else {
			sp = sl.getTargetSchemePort();
			sl.setTargetSchemePort(null);
		}
		if (sp != null) {
			PortType pt = sp.getPortType();
			if (pt == null)
				GraphActions.setObjectBackColor(graph, sp, Color.red);
			else
				GraphActions.setObjectBackColor(graph, sp, Color.yellow);
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
		if (sp.getSchemeCableLink() != null && !sp.getSchemeCableLink().equals(sl)) {
			String message = "К порту " + sp.getName()
					+ " уже подключена линия связи " + sp.getSchemeCableLink().getName()
					+ ".\n";
			message += "Изменить подключенную линию на " + sl.getName() + "?";

			// int res = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
			// message, "Предупреждение", JOptionPane.YES_NO_OPTION);
			// if (res == JOptionPane.NO_OPTION)
			// return false;
		}

		if (is_source)
			sl.setSourceSchemeCablePort(sp);
		else
			sl.setTargetSchemeCablePort(sp);

		PortType pt = sp.getPortType();
		if (pt == null)
			GraphActions.setObjectBackColor(graph, port, Color.red);
		else
			GraphActions.setObjectBackColor(graph, port, Color.white);

		return true;
	}

	public static void disconnectSchemeCableLink(SchemeGraph graph,
			DefaultCableLink link, boolean is_source) {
		SchemeCableLink sl = link.getSchemeCableLink();
		if (sl == null) {
			System.err
					.println("GraphActions.disconnectSchemeCableLink() link not found "
							+ link.getSchemeCableLinkId());
			return;
		}
		SchemeCablePort sp;
		if (is_source) {
			sp = sl.getSourceSchemeCablePort();
			sl.setSourceSchemeCablePort(null);
		} else {
			sp = sl.getTargetSchemeCablePort();
			sl.setTargetSchemeCablePort(null);
		}

		PortType pt = sp.getPortType();
		if (pt == null)
			GraphActions.setObjectBackColor(graph, sp, Color.red);
		else
			GraphActions.setObjectBackColor(graph, sp, Color.yellow);
	}
}
