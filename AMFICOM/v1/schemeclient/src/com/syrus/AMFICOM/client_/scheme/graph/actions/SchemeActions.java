/*
 * $Id: SchemeActions.java,v 1.48 2005/10/30 14:49:20 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.Port;
import com.jgraph.graph.PortView;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.IdentifiableCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortEdge;
import com.syrus.AMFICOM.client_.scheme.graph.objects.Rack;
import com.syrus.AMFICOM.client_.scheme.graph.objects.TopLevelCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.TopLevelElement;
import com.syrus.AMFICOM.client_.scheme.utils.NumberedComparator;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.AbstractSchemeLink;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeCableThreadWrapper;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemePortWrapper;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.48 $, $Date: 2005/10/30 14:49:20 $
 * @module schemeclient
 */

public class SchemeActions {
	private static final String EMPTY = ""; //$NON-NLS-1$
	
	private SchemeActions () {
		// empty
	}
	
	public static void fixImages(SchemeGraph graph) {
		try {
		DeviceGroup[] groups = GraphActions.findAllGroups(graph, graph.getRoots());
		for (int i = 0; i < groups.length; i++) {
			switch (groups[i].getType()) {
			case DeviceGroup.SCHEME_ELEMENT:
				SchemeElement se = groups[i].getSchemeElement();
				DeviceCell cell = GraphActions.getMainCell(groups[i]);
				if (cell != null && se != null) {
					GraphActions.setText(graph, cell, se.getLabel());
					ImageIcon icon = null;
					if (se.getSymbol() != null)
						icon = new ImageIcon(se.getSymbol().getImage());
					GraphActions.setImage(graph, cell, icon);
				}
				break;
			case DeviceGroup.PROTO_ELEMENT:
				SchemeProtoElement proto = groups[i].getProtoElement();
				cell = GraphActions.getMainCell(groups[i]);
				if (cell != null && proto != null) {
					GraphActions.setText(graph, cell, proto.getLabel());
					ImageIcon icon = null;
					if (proto.getSymbol() != null)
						icon = new ImageIcon(proto.getSymbol().getImage());
					GraphActions.setImage(graph, cell, icon);
				}
				break;
			case DeviceGroup.SCHEME:
				Scheme scheme = groups[i].getScheme();
				cell = GraphActions.getMainCell(groups[i]);
				if (cell != null && scheme != null) {
					GraphActions.setText(graph, cell, scheme.getLabel());
					ImageIcon icon = null;
					if (scheme.getSymbol() != null)
						icon = new ImageIcon(scheme.getSymbol().getImage());
					GraphActions.setImage(graph, cell, icon);
				}
				break;
			}
		}
		}catch (Exception e) {
			Log.errorMessage(e);
		}
	}
	
	public static TopLevelElement createTopLevelElement(SchemeGraph graph,
			Object userObject, Rectangle bounds, Scheme scheme) {
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
		TopLevelElement cell = TopLevelElement.createInstance(userObject, bounds, viewMap, scheme.getId());

		Object[] insert = new Object[] { cell };
		graph.getGraphLayoutCache().insert(insert, viewMap, null, null, null);
		graph.setSelectionCells(insert);
		return cell;
	}

	public static TopLevelCableLink createTopLevelCableLink(SchemeGraph graph, Port firstPort,
			Port port, Point p, Point p2) {
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
		ConnectionSet cs = new ConnectionSet();
		TopLevelCableLink cell = TopLevelCableLink.createInstance(EMPTY, firstPort, 
				port, p, p2, viewMap, cs);
		
		graph.getModel().insert(new Object[] {cell}, viewMap, cs, null, null);

		graph.setSelectionCell(cell);
		return cell;
	}

	public static void generateTopLevelScheme(SchemeGraph graph)
	{
		Map<DeviceGroup, TopLevelElement> oldToNewMap = new HashMap<DeviceGroup, TopLevelElement>();
		Map<DeviceGroup, TopLevelElement> map2 = new HashMap<DeviceGroup, TopLevelElement>();
		Map<DeviceGroup, DeviceGroup> map3 = new HashMap<DeviceGroup, DeviceGroup>();
		Object[] cells = graph.getRoots();
		
		boolean tmp = graph.isMakeNotifications();
		graph.setMakeNotifications(false);

		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DeviceGroup)
			{
				DeviceGroup group = (DeviceGroup)cells[i];
				if (isSchemesGroup(group))
				{
					Rectangle r = graph.toScreen(new Rectangle(GraphActions.getGroupBounds(graph, group).getLocation(),
							 new Dimension(8 * graph.getGridSize(), 5 * graph.getGridSize())));

					Scheme scheme = group.getScheme();
					TopLevelElement newGroup = createTopLevelElement(graph, scheme.getName(), r, scheme);

					if (newGroup != null)
						oldToNewMap.put(group, newGroup);
				}
			}
		}
		
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

				boolean b1 = start == null ? false : isSchemesGroup(start);
				boolean b2 = end == null ? false : isSchemesGroup(end);
				if (b1 && b2) {
					TopLevelElement newStart = oldToNewMap.get(start);
					TopLevelElement newEnd = oldToNewMap.get(end);
					createTopLevelCableLink(graph,
							(Port)newStart.getFirstChild(),
							(Port)newEnd.getFirstChild(),
							graph.getCellBounds(newStart).getLocation(),
							graph.getCellBounds(newEnd).getLocation());
				} else if (b1) {
					TopLevelElement newStart = oldToNewMap.get(start);
					TopLevelElement newEnd = map2.get(end);
					if (newEnd == null) {
						DeviceGroup start2 = map3.get(end);
						if (start2 != null)
							newEnd = map2.get(start2);
					}
					
					if (newEnd == null)
						map2.put(start, newStart);
					else
						createTopLevelCableLink(graph,
							(Port)newStart.getFirstChild(),
							(Port)newEnd.getFirstChild(),
							graph.getCellBounds(newStart).getLocation(),
							graph.getCellBounds(newEnd).getLocation());
				} else if (b2) {
					TopLevelElement newEnd = oldToNewMap.get(end);
					TopLevelElement newStart = map2.get(start);
					if (newStart == null) {
						DeviceGroup end2 = map3.get(start);
						if (end2 != null)
							newEnd = map2.get(end2);
					}
					
					if (newStart == null)
						map2.put(end, newEnd);
					else
						createTopLevelCableLink(graph,
							(Port)newStart.getFirstChild(),
							(Port)newEnd.getFirstChild(),
							graph.getCellBounds(newStart).getLocation(),
							graph.getCellBounds(newEnd).getLocation());
				} else if (start != null && end != null) {
					TopLevelElement newStart = map2.get(start);
					if (newStart != null) {
						map2.put(end, newStart);
					}
					TopLevelElement newEnd = map2.get(end);
					if (newEnd != null) {
						map2.put(start, newStart);
					}
					if (newStart == null && newEnd == null) {
						map3.put(start, end);
						map3.put(end, start);
					}
				}
			}
		}
		graph.getModel().remove(graph.getDescendants(cells));
		
		graph.setGraphChanged(false);
		graph.setMakeNotifications(tmp);
	}

	static boolean isSchemesGroup(DeviceGroup group) {
		try {
			SchemeElement se = group.getSchemeElement();
			if (se != null && se.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
				IdlKind kind = se.getScheme(false).getKind();
				if (kind == IdlKind.BUILDING || kind == IdlKind.NETWORK)
					return true;
			}
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
		}
		return false;
	}
	
	public static DefaultCableLink[] splitCableLink(ElementsPanel panel, DefaultCableLink cell) throws ApplicationException {//DefaultCableLink[]
		SchemeGraph graph = panel.getGraph();
//		DefaultPort source = (DefaultPort)cell.getSource(); 
//		DefaultPort target = (DefaultPort)cell.getTarget();
//		PortView source = (PortView)cell.getSource(); 
//		PortView target = (PortView)cell.getTarget();
		int grid = graph.getGridSize();
		
		SchemeCableLink cableLink = cell.getSchemeCableLink();
		try {
			// TODO create 2 SchemeCableLinks and init them with cableLink properties and characteristics
			// FIXME clone!
			SchemeCableLink cl1 = SchemeObjectsFactory.createSchemeCableLink(cableLink.getName(), cableLink.getParentScheme());
			SchemeCableLink cl2 = SchemeObjectsFactory.createSchemeCableLink(cableLink.getName(), cableLink.getParentScheme());

			cl1.setAbstractLinkTypeExt(cableLink.getAbstractLinkType(), LoginManager.getUserId(), false);
			cl2.setAbstractLinkTypeExt(cableLink.getAbstractLinkType(), LoginManager.getUserId(), false);
			
			cl1.setDescription(cableLink.getDescription());
			cl1.setOpticalLength(cableLink.getOpticalLength() / 2);
			cl1.setPhysicalLength(cableLink.getPhysicalLength() / 2);
			cl1.setOpticalLength(cableLink.getOpticalLength() / 2);

			cl2.setDescription(cableLink.getDescription());
			cl2.setOpticalLength(cableLink.getOpticalLength() / 2);
			cl2.setPhysicalLength(cableLink.getPhysicalLength() / 2);
			cl2.setOpticalLength(cableLink.getOpticalLength() / 2);

			EdgeView view = (EdgeView)graph.getGraphLayoutCache().getMapping(cell, false);
			PortView source = view.getSource();
			PortView target = view.getTarget();
			List points = view.getPoints();
			
			Object leftObj = points.get(0);
			Object rightObj = points.get(points.size() - 1);
			Point left = graph.snap(leftObj instanceof Point ? (Point)leftObj : ((PortView)leftObj).getLocation(view));
			Point right = graph.snap(rightObj instanceof Point ? (Point)rightObj : ((PortView)rightObj).getLocation(view));
			
			int x = (left.x + right.x) / 2;
			int y = (left.y + right.y) / 2;
			Point middle1 = graph.snap(new Point (x - 3 * grid, y));
			Point middle2 = graph.snap(new Point (x + 3 * grid, y));
			
			DeleteAction.delete(panel, cell);
			DefaultCableLink cell1 = createCableLink(graph, source, null, left, middle1, cl1.getId(), true);
			DefaultCableLink cell2 = createCableLink(graph, null, target, middle2, right, cl2.getId(), true);
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
			Log.errorMessage(e);
			return null;
		}
	}
	
	public static void insertSEbyS(SchemeGraph graph, SchemeElement schemeElement, Point p, boolean doClone) {
		try {
			Scheme scheme = schemeElement.getScheme(false);
			Map<Identifier, Identifier>clonedIds = schemeElement.getClonedIdMap();
			SchemeImageResource res = scheme.getUgoCell();
			if (res == null) {
				Log.debugMessage("Can not insert scheme without ugo cell", Level.WARNING);
				return;
			}
			
			Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = openSchemeImageResource(graph, res, doClone, p, true);
			SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
				
			SchemeImageResource seRes = schemeElement.getUgoCell();
			if (seRes == null) {
				seRes = SchemeObjectsFactory.createSchemeImageResource();
				schemeElement.setUgoCell(seRes);
			}
			seRes.setData((List<Object>)graph.getArchiveableState(clonedObjects.values().toArray()));
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
	
	public static void insertSEbyPE(SchemeGraph graph, SchemeElement schemeElement, Map<Identifier, Identifier>clonedIds, Point p, boolean doClone) {
		SchemeImageResource ugoRes = schemeElement.getUgoCell();
		SchemeImageResource schemeRes = schemeElement.getSchemeCell();
		if (ugoRes == null) {
			try {
				ugoRes = SchemeObjectsFactory.createSchemeImageResource();
				schemeElement.setUgoCell(ugoRes);
			} catch (CreateObjectException e) {
				Log.errorMessage(e);
				return;
			}

			// open scheme cell and save to ugo
			ApplicationContext internalContext =  new ApplicationContext();
			internalContext.setDispatcher(new Dispatcher());
			UgoTabbedPane pane = new UgoTabbedPane(internalContext);
			SchemeGraph invisibleGraph = pane.getGraph();
			Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = openSchemeImageResource(invisibleGraph, schemeRes, doClone);
			SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
			ugoRes.setData((List)invisibleGraph.getArchiveableState());
		} else {
			// open scheme cell and save to scheme
			ApplicationContext internalContext =  new ApplicationContext();
			internalContext.setDispatcher(new Dispatcher());
			UgoTabbedPane pane = new UgoTabbedPane(internalContext);
			SchemeGraph invisibleGraph = pane.getGraph();
			Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = openSchemeImageResource(invisibleGraph, schemeRes, doClone);
			SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
			schemeRes.setData((List)invisibleGraph.getArchiveableState());

			// open ugo cell and save to ugo
			GraphActions.clearGraph(invisibleGraph);
			clonedObjects = openSchemeImageResource(invisibleGraph, ugoRes, doClone);
			SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
			ugoRes.setData((List)invisibleGraph.getArchiveableState());
		}
		// fanally open ugo to current graph
		openSchemeImageResource(graph, ugoRes, doClone, p, true); 
	}
	
	public static void putToGraph(Scheme scheme, SchemeTabbedPane pane) throws ApplicationException {
		int width = scheme.getWidth();
		int height = scheme.getHeight();
		
		ApplicationContext aContext =  pane.getContext();
		Dispatcher internalDispatcher = aContext.getDispatcher();
		SchemeGraph schemeGraph = pane.getGraph();
		schemeGraph.setActualSize(new Dimension(width, height));
		int grid = schemeGraph.getGridSize();
		internalDispatcher.firePropertyChange(new SchemeEvent(schemeGraph, scheme.getId(), SchemeEvent.OPEN_SCHEME));
		
		// determine bounds
		double xmin = 180, ymin = 90, xmax = -180, ymax = -90; 
		for (SchemeElement schemeElement : scheme.getSchemeElements(false)) {
			Equipment equipment = schemeElement.getEquipment();
			if (equipment != null) {
				double x0 = equipment.getLongitude();
				double y0 = equipment.getLatitude();
				xmin = Math.min(xmin, x0);
				ymin = Math.min(ymin, y0);
				xmax = Math.max(xmax, x0);
				ymax = Math.max(ymax, y0);
			}
		}
		
		double kx = (xmax - xmin == 0) ? 1 : (scheme.getWidth() - grid * 20) / (xmax - xmin);
		double ky = (ymax - ymin == 0) ? 1 : (scheme.getHeight() - grid * 20) / (ymax - ymin);
		
		Set<Identifier> placedObjectIds = getPlacedObjects(schemeGraph);
		
		for (SchemeElement schemeElement : scheme.getSchemeElements(false)) {
			if (!placedObjectIds.contains(schemeElement.getId())) {
				Equipment equipment = schemeElement.getEquipment();
				Point p;
				if (equipment != null) {
					double x0 = equipment.getLongitude();
					double y0 = equipment.getLatitude();
					p = new Point((int)(grid * 10 + (x0 - xmin) * kx), (grid * 5 + height - (int)((y0 - ymin) * ky))); 
				} else {
					Log.errorMessage("No equipment for " + schemeElement.getName() + " (" + schemeElement.getId() + ")");
					p = new Point((int)(width * Math.random()), (int)(height * Math.random()));
				}
				if (schemeElement.getKind().value() == IdlSchemeElementKind._SCHEME_CONTAINER) {
					internalDispatcher.firePropertyChange(new SchemeEvent(schemeGraph, schemeElement.getScheme(false).getId(), p, SchemeEvent.INSERT_SCHEME));	
				} else {
					internalDispatcher.firePropertyChange(new SchemeEvent(schemeGraph, schemeElement.getId(), p, SchemeEvent.INSERT_SCHEMEELEMENT));
				}
			}
		}
		
		for (SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks(false)) {
			if (!placedObjectIds.contains(schemeCableLink.getId())) {
			Point p = null;
			SchemeCablePort sourcePort = schemeCableLink.getSourceAbstractSchemePort();
			SchemeCablePort targetPort = schemeCableLink.getSourceAbstractSchemePort();
			if (sourcePort != null) {
				CablePortCell cell = SchemeActions.findCablePortCellById(schemeGraph, sourcePort.getId());
				if (cell != null) {
					try {
						DefaultPort source = SchemeActions.getSuitablePort(cell, schemeCableLink.getId());
						PortView sourceView = (PortView)schemeGraph.getGraphLayoutCache().getMapping(source, false);
						p = sourceView.getBounds().getLocation();
					} catch (CreateObjectException e) {
						Log.errorMessage(e);
						return;
					}
				} else {
					Log.errorMessage("No source found for " + schemeCableLink.getName() + " (" + schemeCableLink.getId() + ")");
				}
			} 
			if (p == null && targetPort != null) {
				CablePortCell cell = SchemeActions.findCablePortCellById(schemeGraph, targetPort.getId());
				if (cell != null) {
					try {
						DefaultPort source = SchemeActions.getSuitablePort(cell, schemeCableLink.getId());
						PortView sourceView = (PortView)schemeGraph.getGraphLayoutCache().getMapping(source, false);
						p = sourceView.getBounds().getLocation();
					} catch (CreateObjectException e) {
						Log.errorMessage(e.getMessage());
						return;
					}
				} else {
					Log.errorMessage("No target found for " + schemeCableLink.getName() + " (" + schemeCableLink.getId() + ")");
				}
			} 
			if (p == null) {
				Log.errorMessage("Both source and target not found for " + schemeCableLink.getName() + " (" + schemeCableLink.getId() + ")");
				p = new Point(10 * grid, 10 * grid); 
			}
			
			internalDispatcher.firePropertyChange(new SchemeEvent(schemeGraph, schemeCableLink.getId(), p, SchemeEvent.INSERT_SCHEME_CABLELINK));
			}
		}
		schemeGraph.setMakeNotifications(true);
	} 
	
	public static Set<Identifier> getPlacedObjects(SchemeGraph graph) {
		graph.setMakeNotifications(false);
		
		Set<Identifier> placedObjectIds = new HashSet<Identifier>();
		for (Object cell : graph.getAll()) {
			if (cell instanceof DeviceGroup) {
				placedObjectIds.add(((DeviceGroup)cell).getElementId());
			} else if (cell instanceof DefaultCableLink) {
				placedObjectIds.add(((DefaultCableLink)cell).getSchemeCableLinkId());
			} else if (cell instanceof DefaultLink) {
				placedObjectIds.add(((DefaultLink)cell).getSchemeLinkId());
			}
		}
		return placedObjectIds;
	}

	public static void writeClonedIds(SchemeGraph graph, SchemeImageResource imageResource, Map<Identifier, Identifier> clonedIds) {
		GraphActions.clearGraph(graph);
		Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(graph, imageResource, true);
		SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
		imageResource.setData((List)graph.getArchiveableState());
	}
	
	/**
	 * @param schemeImageResource Scheme or SchemeElement or SchemeProtoElement SchemeCell or UgoCell
	 * @param doClone create copy of objects or open themself
	 * @return Map of cloned DefaultGraphCells (oldCell, newCell) if doClone is true, empty map overwise  
	 */
	public static Map<DefaultGraphCell, DefaultGraphCell> openSchemeImageResource(SchemeGraph graph, SchemeImageResource schemeImageResource, boolean doClone) {
		return openSchemeImageResource(graph, schemeImageResource, doClone, null, false);
	}
	
	public static Map<DefaultGraphCell, DefaultGraphCell> openSchemeImageResource(SchemeGraph graph, SchemeImageResource schemeImageResource, boolean doClone, Point p, boolean isCenterCell) {
		Map<DefaultGraphCell, DefaultGraphCell> clones = Collections.emptyMap();
		boolean tmp = graph.isMakeNotifications();
		graph.setMakeNotifications(false);
//		GraphActions.clearGraph(graph);
		if (schemeImageResource != null) {
			clones = GraphActions.insertCell(graph, schemeImageResource.getData(), doClone, p, isCenterCell);
			fixImages(graph);
		}
		graph.setGraphChanged(false);
		graph.setMakeNotifications(tmp);
		return clones;
	}
	
	public static DefaultCableLink createCableLink(SchemeGraph graph, PortView firstPort,
			PortView port, Point p, Point p2, Identifier linkId, boolean allowUnconnected) throws CreateObjectException {
		ConnectionSet cs = new ConnectionSet();
		Map<Object, Map> viewMap = new HashMap<Object, Map>();

		DefaultCableLink cell = DefaultCableLink.createInstance(EMPTY, firstPort, port, p, p2, viewMap, cs, linkId, allowUnconnected);
		
		Object[] cells = graph.getAll();
		int counter = 0;
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DefaultCableLink)
				counter++;
		String name = LangModelScheme.getString("Title.cable") + String.valueOf(counter+1); //$NON-NLS-1$

		cell.setUserObject(name);
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
			PortView port, Point p, Point p2, Identifier linkId, boolean allowUnconnected) throws CreateObjectException {
		ConnectionSet cs = new ConnectionSet();
		Map viewMap = new HashMap();
		
		Object[] cells = graph.getAll();
		int counter = 0;
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DefaultLink)
				counter++;
		String name = LangModelScheme.getString("Title.link") + String.valueOf(counter+1); //$NON-NLS-1$
		
		DefaultLink cell = DefaultLink.createInstance(name, firstPort, port, p, p2, viewMap, cs, linkId, allowUnconnected);

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

	public static DefaultGraphCell createInternalPort(SchemeGraph graph, Point p, String name, IdlDirectionType direction, boolean isCable, Color color, Identifier portId) {
		Rectangle portCellBounds = new Rectangle(p.x - 3, p.y - 2, 5, 5);
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
		
		DefaultGraphCell visualPort;
		if (!isCable) {
			visualPort = PortCell.createInstance(name, portCellBounds, viewMap, direction, color);
			((PortCell)visualPort).setSchemePortId(portId);
		} else { // cableport
			visualPort = CablePortCell.createInstance(name, portCellBounds, viewMap, direction, color);
			((CablePortCell)visualPort).setSchemeCablePortId(portId);
		}
		graph.getGraphLayoutCache().insert(new Object[] { visualPort }, viewMap, null, null, null);
		return visualPort;
	}
	
	private static DefaultGraphCell createAbstractPort(SchemeGraph graph, DeviceCell deviceCell, Point p, String name, IdlDirectionType direction, boolean isCable, Color color, Identifier portId) {
		DefaultGraphCell visualPort;
		DefaultPort ellipsePort;
		Port devPort;
		
		Map m = graph.getModel().getAttributes(deviceCell);
		Rectangle dev_bounds = GraphConstants.getBounds(m);
		
		int u = GraphConstants.PERCENT;
		int distance = (direction == IdlDirectionType._OUT ?
				(p.x - (dev_bounds.x + dev_bounds.width) + 1) / graph.getGridSize() + 1 :
				(dev_bounds.x - p.x) / graph.getGridSize());
		Point labelPosition = (direction == IdlDirectionType._OUT ? 
				new Point ((int)(-1.6 * u / distance), 0) : 
				new Point (u + (int)(0.9 * u / distance), 0));
		Rectangle portCellBounds = (direction == IdlDirectionType._OUT ? 
				new Rectangle(p.x - 4, p.y - 2, 5, 5) : 
				new Rectangle(p.x, p.y - 2, 5, 5));
		Point devportPos = (direction == IdlDirectionType._OUT ?
				new Point(u, (int)(u * ( (double)(p.y + 1 - dev_bounds.y) / (double)dev_bounds.height))) :		
				new Point(0, (int)(u * ( (double)(p.y + 1 - dev_bounds.y) / (double)dev_bounds.height))));
		Point ellipseportPos = (direction == IdlDirectionType._OUT ?
				new Point(0, u / 2) :
				new Point(u, u / 2));
		
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
	
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
		
//		viewMap = new HashMap<Object, Map>();
//		Map map = GraphConstants.createMap();
//		map.put(Constants.SELECTABLE, new Boolean(false));
//		viewMap.put(edge, map);

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
	
	public static Color determinePortColor(AbstractSchemePort port, Object link) {
		if (link == null)
			return UIManager.getColor(SchemeResourceKeys.COLOR_PORT_NO_LINK);
		PortType type = port.getPortType();
		if (type == null)
			return UIManager.getColor(SchemeResourceKeys.COLOR_PORT_NO_TYPE);
		if (type.getSort().equals(PortTypeSort.PORTTYPESORT_THERMAL))
			return UIManager.getColor(SchemeResourceKeys.COLOR_PORT_TERMAL);
		return UIManager.getColor(SchemeResourceKeys.COLOR_PORT_COMMON);
	}
			
	public static Object[] getPathObjects(SortedSet<Identifier> ids, SchemeGraph graph) {
		Collection<Object> pathObjects = new ArrayList<Object>();
		
		Object[] cells = graph.getRoots();
		for (Object cell : cells) {
			Identifier id = null;
			if (cell instanceof DeviceGroup) {
				id = ((DeviceGroup)cell).getElementId();
			} else if (cell instanceof DefaultLink) {
				id = ((DefaultLink)cell).getSchemeLinkId();
			} else if (cell instanceof DefaultCableLink) {
				id = ((DefaultCableLink)cell).getSchemeCableLinkId();
			}
			if (id != null && ids.contains(id)) {
				pathObjects.add(cell);
			}
		}
		return pathObjects.toArray();
	}
	
	public static Set<SchemeElement> getSchemeElements(Object[] cells) {
		Set<SchemeElement> schemeElements = new HashSet<SchemeElement>();
		for (Object cell : cells) {
			if (cell instanceof DeviceGroup) {
				DeviceGroup group = (DeviceGroup)cell;
				if (group.getElementId().getMajor() == ObjectEntities.SCHEMEELEMENT_CODE) {
					schemeElements.add(group.getSchemeElement());
				}
			}
		}
		return schemeElements;
	}
	
	public static Set<SchemeCableLink> getSchemeCableLinks(Object[] cells) {
		Set<SchemeCableLink> schemeCableLinks = new HashSet<SchemeCableLink>();
		for (Object cell : cells) {
			if (cell instanceof DefaultCableLink) {
				schemeCableLinks.add(((DefaultCableLink)cell).getSchemeCableLink());
			}
		}
		return schemeCableLinks;
	}
	
	public static Set<SchemeLink> getSchemeLinks(Object[] cells) {
		Set<SchemeLink> schemeLinks = new HashSet<SchemeLink>();
		for (Object cell : cells) {
			if (cell instanceof DefaultLink) {
				schemeLinks.add(((DefaultLink)cell).getSchemeLink());
			}
		}
		return schemeLinks;
	}
	
	public static PathElement getSelectedPathElement(Object selected) {
		Identifier schemeObjectId = null;
		if (selected instanceof DeviceGroup) {
			schemeObjectId = ((DeviceGroup)selected).getElementId();
		} else if (selected instanceof DefaultLink) {
			schemeObjectId = ((DefaultLink)selected).getSchemeLinkId();
		} else if (selected instanceof DefaultCableLink) {
			schemeObjectId = ((DefaultCableLink)selected).getSchemeCableLinkId();
		}
		
		if (schemeObjectId != null) {
			LinkedIdsCondition condition = new LinkedIdsCondition(schemeObjectId, ObjectEntities.PATHELEMENT_CODE);
			try {
				Set<PathElement> pathElements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
				if (!pathElements.isEmpty()) {
					PathElement pe = pathElements.iterator().next();
					return pe;
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
		return null;
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

	public static DefaultGraphCell findGroupById(SchemeGraph graph,
			Identifier id) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] instanceof DeviceGroup) {
				if (id.equals(((DeviceGroup)cells[i]).getElementId())) {
					return (DeviceGroup) cells[i];
				}
			} else if (cells[i] instanceof Rack) {
				if (id.equals(((Rack)cells[i]).getId())) {
					return (Rack) cells[i];
				}
			}
		}
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

	public static PortCell findAccessPortById(final SchemeGraph graph, final Identifier measurementPortId) {
		for (final Object cell : graph.getAll()) {
			if (cell instanceof PortCell) {
				final PortCell portCell = (PortCell) cell;
				if (!portCell.getSchemePortId().isVoid()) {
					final Identifier measurementPortId1 = portCell.getSchemePort().getMeasurementPort().getId();
					if (!measurementPortId1.isVoid() && measurementPortId1.equals(measurementPortId)) {
						return portCell;
					}
				}
			}
		}
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
	
	public static DefaultGraphCell findObjectById(SchemeGraph graph,
			Identifier id) {
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof IdentifiableCell)
				if (((IdentifiableCell) cells[i]).getId().equals(id)) {
					if (cells[i] instanceof DefaultGraphCell) {
						return (DefaultGraphCell) cells[i];
					}
				}
					
		return null;
	}

	public static boolean connectSchemeLink(SchemeGraph graph, DefaultLink link,
			PortCell port, boolean is_source) {

		SchemePort sp = port.getSchemePort();
		if (sp == null) {
			Log.debugMessage("SchemePort is null for " + port.getId() + " during import it's normal otherwise not", Level.INFO);
			return false;
		}
		
		AbstractSchemeLink connectedLink = sp.getAbstractSchemeLink();
		if (connectedLink != null && link.getSchemeLinkId().equals(connectedLink.getId())) {
			return true;
		}
		SchemeLink sl = link.getSchemeLink();

		if (connectedLink != null) {
			Log.debugMessage("Port already has connected link", WARNING);
			return false;
		}

		if (is_source) {
			sl.setSourceAbstractSchemePort(sp);
		} else {
			sl.setTargetAbstractSchemePort(sp);
		}

		GraphActions.setObjectBackColor(graph, port, determinePortColor(sp, sl));

		return true;
	}

	public static boolean disconnectSchemeLink(SchemeGraph graph, DefaultLink link,
			PortCell port, boolean is_source) {
		
		SchemeLink sl = link.getSchemeLink();
		if (sl == null) {
			Log.debugMessage("GraphActions.disconnectSchemeLink() link not found " + link.getSchemeLinkId(), Level.WARNING); //$NON-NLS-1$
			return false;
		}
		
		// check for valid SchemePath changes
		PathElement pe = SchemeActions.getSelectedPathElement(link);
		if (pe != null) {
//			int res = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "Это изменит путь!", "Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION);
//			if (res != JOptionPane.YES_OPTION) {
//				return false;
//			}

			// switching between ports in optical switch is not lead to path change
			SchemePath path = pe.getParentPathOwner();
			try {
				PathElement lastPE = path.getPreviousPathElement(pe);
				SchemeElement lastSE = lastPE.getSchemeElement();
				if (!lastSE.getProtoEquipment().getType().equals(EquipmentType.OPTICAL_SWITCH)) {
					if (is_source) {
						pe.setParentPathOwner(null, true);
					} else {
						PathElement nextPE = path.getNextPathElement(pe);
						if (nextPE != null) {
							nextPE.setParentPathOwner(null, true);
						}
					}
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}

		SchemePort sp;
		if (is_source) {
			sp = sl.getSourceAbstractSchemePort();
			sl.setSourceAbstractSchemePort(null);
		} 
		else {
			sp = sl.getTargetAbstractSchemePort();
			Log.debugMessage("Link " + sl + " connected to port " + sp + "; disconnecting...", Level.FINER);
			sl.setTargetAbstractSchemePort(null);
			if (sp != null) {
				SchemeLink sl2 = sp.getAbstractSchemeLink();
				Log.debugMessage("Now port " + sp + " has connected link " + sl2, Level.FINER);
			}
		}
		if (sp != null) {
			GraphActions.setObjectBackColor(graph, port, determinePortColor(sp, null));
		}
		return true;
	}

	public static boolean connectSchemeCableLink(SchemeGraph graph,
			DefaultCableLink link, CablePortCell port, boolean is_source) {
		
		SchemeCablePort sp = port.getSchemeCablePort();
		if (sp == null) {
			Log.debugMessage("SchemePort is null for " + port.getId() + " during import it's normal otherwise not", Level.INFO);
			return false;
		}
		
		AbstractSchemeLink connectedLink = sp.getAbstractSchemeLink();
		if (connectedLink != null && link.getSchemeCableLinkId().equals(connectedLink.getId())) {
			return true;
		}
		SchemeCableLink sl = link.getSchemeCableLink();
		
		if (connectedLink != null) {
			Log.debugMessage("CablePort already has connected cable", WARNING);
			return false;
		}

		if (is_source) {
			sl.setSourceAbstractSchemePort(sp);
		} else {
			sl.setTargetAbstractSchemePort(sp);
		}

		try {
			performAutoCommutation(sp, sl, is_source);
			GraphActions.setObjectBackColor(graph, port, determinePortColor(sp, sl));
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		
		return true;
	}
	
	public static void performAutoCommutation(SchemeCablePort scp, SchemeCableLink scl, boolean is_source) throws ApplicationException {
		Log.debugMessage("create autocommutation", Level.FINER);
		IdlDirectionType direction = scp.getDirectionType() == IdlDirectionType._IN ? IdlDirectionType._OUT : IdlDirectionType._IN;
		
		List<SchemePort> ports = new ArrayList<SchemePort>(findPorts(scp.getParentSchemeDevice(), direction));
		Collections.sort(ports, new NumberedComparator<SchemePort>(SchemePortWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME));

		List<SchemeCableThread> threads = new ArrayList<SchemeCableThread>(scl.getSchemeCableThreads(false));	
		Collections.sort(threads, new NumberedComparator<SchemeCableThread>(SchemeCableThreadWrapper.getInstance(), StorableObjectWrapper.COLUMN_NAME));
		for (Iterator it1 = ports.iterator(), it2 = threads.iterator(); it1.hasNext() && it2.hasNext();) {
			SchemePort sport = (SchemePort)it1.next();
			SchemeCableThread thread = (SchemeCableThread)it2.next();
			if (is_source) {
				thread.setSourceSchemePort(sport);
			} else {
				thread.setTargetSchemePort(sport);
			}
		}
	}
	
	public static void disconnectSchemeCableLink(SchemeGraph graph,
			DefaultCableLink link, CablePortCell port, boolean is_source) {
		SchemeCableLink sl = link.getSchemeCableLink();

		try {
		PathElement pe = SchemeActions.getSelectedPathElement(link);
		if (pe != null) {
//			int res = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), "Это изменит путь!", "Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION);
//			if (res != JOptionPane.YES_OPTION) {
//				return false;
//			}
			if (is_source) {
				pe.setParentPathOwner(null, true);
			} else {
				SchemePath path = pe.getParentPathOwner();
				PathElement nextPE = path.getNextPathElement(pe);
				if (nextPE != null) {
					nextPE.setParentPathOwner(null, true);
				}
			}
		}
		
		SchemeCablePort sp;
		if (is_source) {
			sp = sl.getSourceAbstractSchemePort();
			sl.setSourceAbstractSchemePort(null);
		} else {
			sp = sl.getTargetAbstractSchemePort();
			sl.setTargetAbstractSchemePort(null);
		}

		if (sp != null) {
			GraphActions.setObjectBackColor(graph, port, determinePortColor(sp, null));
			
			try {
				for (SchemeCableThread thread : sl.getSchemeCableThreads(false)) {
					if (is_source) {
						thread.setSourceSchemePort(null);
					} else {
						thread.setTargetSchemePort(null);
					}
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
		} catch (final ApplicationException ae) {
			ae.printStackTrace();
		}
	}
	
	public static  DefaultPort getSuitablePort(CablePortCell sourcePortCell, Identifier cableId) throws CreateObjectException {
		if (sourcePortCell != null) {
			for (Object obj : sourcePortCell.getChildren()) {
				DefaultPort defaultPort = (DefaultPort)obj;
				if (defaultPort.getUserObject().equals("Center")) {
					Set edges = defaultPort.getEdges();
					if (edges.isEmpty()) {
						return defaultPort;
					} else if (edges.size() == 1) {
						 Object link = edges.iterator().next();
						 if (link instanceof DefaultCableLink && ((DefaultCableLink)link).getSchemeCableLinkId().equals(cableId)) {
							 JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
										LangModelScheme.getString("Message.information.schemecablelink_already_connected"),  //$NON-NLS-1$
										LangModelScheme.getString("Message.information"),  //$NON-NLS-1$
										JOptionPane.INFORMATION_MESSAGE);
							 throw new CreateObjectException("SchemeCableLink already connected");
						 }
						JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
									LangModelScheme.getString("Message.error.another_schemecablelink_connected"),  //$NON-NLS-1$
									LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
						throw new CreateObjectException("Another SchemeCableLink connected");
					} else {
						throw new CreateObjectException("Incorrect scheme! More then one links connected to port");
					}
				}
			}
		}
		return null;
	}
	
	public static Set<SchemePort> findPorts(SchemeDevice dev, IdlDirectionType direction) throws ApplicationException {
		Set<SchemePort> ports = new HashSet<SchemePort>();
		for (Iterator it = dev.getSchemePorts(false).iterator(); it.hasNext();) {
			SchemePort p = (SchemePort)it.next();
			if (p.getDirectionType().equals(direction))
				ports.add(p);
		}
		return ports;
	}

	public static Set<SchemeCablePort> findCablePorts(SchemeDevice dev, IdlDirectionType direction) throws ApplicationException {
		Set<SchemeCablePort> ports = new HashSet<SchemeCablePort>();
		for (Iterator it = dev.getSchemeCablePorts(false).iterator(); it.hasNext();) {
			SchemeCablePort p = (SchemeCablePort)it.next();
			if (p.getDirectionType().equals(direction))
				ports.add(p);
		}
		return ports;
	}
}
