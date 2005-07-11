/*
 * $Id: CreateGroup.java,v 1.4 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.ParentMap;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.UgoPanel;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class CreateGroup extends AbstractAction {
	UgoTabbedPane pane;
	private static int counter = 1;
	private int type;
	
	public CreateGroup(UgoTabbedPane pane, final int type) {
		super(Constants.GROUP);
		this.pane = pane;
		this.type = type;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = pane.getGraph();

		Object[] cells = getCellsToAdd(graph);
		if (cells.length == 0)
			return;

		// next create group
		SchemeProtoElement protoElement = null;
		SchemeElement element = null;

		if (type == DeviceGroup.PROTO_ELEMENT) {
			try {
				protoElement = SchemeObjectsFactory.createSchemeProtoElement();
			} catch (CreateObjectException e1) {
				Log.errorException(e1);
				return;
			}
			counter++;
			createProtoGroup(graph, cells, protoElement);
		} else {
			UgoPanel p = pane.getCurrentPanel();
			if (p instanceof ElementsPanel) {
				SchemeResource res = ((ElementsPanel)p).getSchemeResource();
				try {
					element = SchemeObjectsFactory.createSchemeElement(res.getScheme());
				} catch (CreateObjectException e1) {
					Log.errorException(e1);
					return;
				}
				counter++;
				createElementsGroup(graph, cells, element);
			}
		}
	}
	
	
	public static Object[] getCellsToAdd(SchemeGraph graph) {
		Object[] cells = graph.getGraphLayoutCache().order(graph.getSelectionCells());
		
		if (cells != null) {
			List<DefaultGraphCell> new_cells = new ArrayList<DefaultGraphCell>();
			
			// at first searching elements sholud be added to group
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] instanceof DeviceCell) {
					DeviceCell box = (DeviceCell) cells[i];
					if (box.getChildCount() > 1) {
						new_cells.add(box);
						for (Enumeration it = box.children(); it.hasMoreElements();) {
							DefaultPort dev_port = (DefaultPort) it.nextElement();
							if (GraphConstants.getOffset(dev_port.getAttributes()) != null) {
								if (dev_port.edges().hasNext()) {
									DefaultEdge edge = (DefaultEdge) dev_port.edges().next();
									new_cells.add(edge);
									Object obj = DefaultGraphModel.getTargetVertex(graph.getModel(), edge);
									if (obj instanceof PortCell) {
										PortCell ellipse = (PortCell)obj;
										if (ellipse.getSchemePort().getPortType() == null) {
											JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
													"Не установлен тип порта", "Ошибка", JOptionPane.OK_OPTION);
											Log.debugMessage("Port type not set for " + ellipse.getSchemePort().getName(), Level.WARNING); //$NON-NLS-1$
											return new Object[0];
										}
										new_cells.add(ellipse);
									} else if (obj instanceof CablePortCell) {
										CablePortCell ellipse = (CablePortCell)obj;
										if (ellipse.getSchemeCablePort().getPortType() == null) {
											JOptionPane.showMessageDialog(Environment.getActiveWindow(),
													"Не установлен тип кабельного порта", "Ошибка",
													JOptionPane.OK_OPTION);
											Log.debugMessage("Port type not set for " + ellipse.getSchemeCablePort().getName(), Level.WARNING); //$NON-NLS-1$
											return new Object[0];
										}
										new_cells.add(ellipse);
									}
								}
							}
						}
					}
				} 
				else if (cells[i] instanceof DeviceGroup) {
					new_cells.add((DeviceGroup)cells[i]);
				}
				else if(cells[i] instanceof DefaultLink) {
					new_cells.add((DefaultLink)cells[i]);
				}
			}
			return new_cells.toArray();
		}
		return new Object[0];
	}
	
	public static DeviceGroup createProtoGroup(SchemeGraph graph, Object[] cells, SchemeProtoElement proto) {
		DeviceGroup group = createGroup(graph, cells, proto.getId(), DeviceGroup.PROTO_ELEMENT);
		
		// at last determine what elements it consists of
		Set childSchemeProtoElements = new HashSet();
		Set childSchemeLinks = new HashSet();
		Set childSchemeDevices = new HashSet();
		
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] instanceof DeviceGroup) {
				SchemeProtoElement spe = ((DeviceGroup) cells[i]).getProtoElement();
				if (spe != null) {
					childSchemeProtoElements.add(spe);
				}
			} 
			else if (cells[i] instanceof DeviceCell) {
				SchemeDevice dev = ((DeviceCell) cells[i]).getSchemeDevice();
				if (dev != null) {
					childSchemeDevices.add(dev);
				}
			} 
			else if (cells[i] instanceof DefaultLink) {
				SchemeLink link = ((DefaultLink) cells[i]).getSchemeLink();
				if (link != null) {
					childSchemeLinks.add(link);
				}
			}
		}
		
		for (Iterator it = childSchemeDevices.iterator(); it.hasNext();) {
			SchemeDevice dev = (SchemeDevice) it.next();
			dev.setParentSchemeProtoElement(proto);
		}
		for (Iterator it = childSchemeLinks.iterator(); it.hasNext();) {
			SchemeLink link = (SchemeLink) it.next();
			link.setParentSchemeProtoElement(proto);
		}
		proto.setSchemeProtoElements(childSchemeProtoElements);
		
//		for (Iterator it = childSchemeProtoElements.iterator(); it.hasNext();) {
//			SchemeProtoElement childProto = (SchemeProtoElement) it.next();
//			childProto.setParentSchemeProtoElement(proto);
//		}
		return group;
	}
	
	public static DeviceGroup createElementsGroup(SchemeGraph graph, Object[] cells, SchemeElement element) {
		DeviceGroup group = createGroup(graph, cells, element.getId(), DeviceGroup.SCHEME_ELEMENT);
		
		// at last determine what elements it consists of
		Set childSchemeElements = new HashSet();
		Set childSchemeLinks = new HashSet();
		Set childSchemeDevices = new HashSet();
		
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] instanceof DeviceGroup) {
				SchemeElement se = ((DeviceGroup) cells[i]).getSchemeElement();
				if (se != null) {
					childSchemeElements.add(se);
				}
			} 
			else if (cells[i] instanceof DeviceCell) {
				SchemeDevice dev = ((DeviceCell) cells[i]).getSchemeDevice();
				if (dev != null) {
					childSchemeDevices.add(dev);
				}
			} 
			else if (cells[i] instanceof DefaultLink) {
				SchemeLink link = ((DefaultLink) cells[i]).getSchemeLink();
				if (link != null) {
					childSchemeLinks.add(link);
				}
			}
		}
		
		element.setSchemeDevices(childSchemeDevices);
		element.setSchemeLinks(childSchemeLinks);
		element.setSchemeElements(childSchemeElements);
		return group;
	}
	
	private static DeviceGroup createGroup(SchemeGraph graph, Object[] cells, Identifier id, int type) {
		Map viewMap = new HashMap();
		DeviceGroup group = DeviceGroup.createInstance(null, viewMap, id, type);

		//make group created unresizable
		Map m = GraphConstants.createMap();
		GraphConstants.setSizeable(m, false);
		viewMap.put(group, m);
		
		ParentMap map = new ParentMap();
		for (int i = 0; i < cells.length; i++)
			map.addEntry(cells[i], group);
		graph.getGraphLayoutCache().insert(new Object[] { group }, viewMap, null,
				map, null);
		graph.setSelectionCell(group);
		graph.selectionNotify();
		return group;
	}
}