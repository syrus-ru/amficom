/*
 * $Id: CreateGroup.java,v 1.2 2005/05/26 07:40:51 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.*;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.scheme.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/05/26 07:40:51 $
 * @module schemeclient_v1
 */

public class CreateGroup extends AbstractAction {
	UgoTabbedPane pane;
		
	private DeviceGroup group;
	private Set childSchemeElements = new HashSet();
	private Set childSchemeProtoElements = new HashSet();
	private Set childSchemeLinks = new HashSet();
	private Set childSchemePorts = new HashSet();
	private Set childSchemeCablePorts = new HashSet();
	private Set childSchemeDevices = new HashSet();

	public CreateGroup(UgoTabbedPane pane) {
		super(Constants.GROUP);
		this.pane = pane;
	}

	public DeviceGroup getCreatedDeviceGroup() {
		return group;
	}
	
	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = pane.getGraph();
		Object[] cells = graph.getGraphLayoutCache().order(graph.getSelectionCells());
		
		if (cells != null) {
			List new_cells = new ArrayList();
			
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
									Object ellipse = DefaultGraphModel.getTargetVertex(graph.getModel(), edge);
									if (ellipse instanceof PortCell) {
										if (((PortCell) ellipse).getSchemePort().getPortType() == null) {
											JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
													"Не установлен тип порта", "Ошибка", JOptionPane.OK_OPTION);
											System.out.println("Error! Port type not set.");
											return;
										}
										new_cells.add(ellipse);
									}
									else if (ellipse instanceof CablePortCell) {
										if (((CablePortCell) ellipse).getSchemeCablePort().getPortType() == null) {
											JOptionPane.showMessageDialog(Environment.getActiveWindow(),
													"Не установлен тип кабельного порта", "Ошибка",
													JOptionPane.OK_OPTION);
											System.out.println("Error! Cableport type not set.");
											return;
										}
										new_cells.add(ellipse);
									}
								}
							}
						}
					}
				} 
				else if (cells[i] instanceof DeviceGroup || cells[i] instanceof DefaultLink) {
					new_cells.add(cells[i]);
				}
			}
			if (new_cells.isEmpty())
				return;

			SchemeElement element = pane.getCurrentPanel().getSchemeResource().getSchemeElement();
			SchemeProtoElement protoElement = pane.getCurrentPanel().getSchemeResource().getSchemeProtoElement();
			
			// next create group
			Map viewMap = new HashMap();
			group = element == null ?
					DeviceGroup.createInstance(null, viewMap, protoElement) : 
					DeviceGroup.createInstance(null, viewMap, element);

			// make group created unresizable
			Map m = GraphConstants.createMap();
			GraphConstants.setSizeable(m, false);
			viewMap.put(group, m);

			ParentMap map = new ParentMap();
			for (int i = 0; i < cells.length; i++)
				map.addEntry(cells[i], group);
			graph.getGraphLayoutCache().insert(new Object[] { group }, viewMap, null,
					map, null);
			graph.setSelectionCell(group);
				
			// at last determine what elements it consists of
			cells = new_cells.toArray();
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] instanceof DeviceGroup) {
					SchemeElement se = ((DeviceGroup) cells[i]).getSchemeElement();
					if (se != null) {
						childSchemeElements.add(se);
					}
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
				else if (cells[i] instanceof PortCell) {
					SchemePort port = ((PortCell) cells[i]).getSchemePort();
					if (port != null) {
						childSchemePorts.add(port);
					}
				}
				else if (cells[i] instanceof CablePortCell) {
					SchemeCablePort port = ((CablePortCell) cells[i]).getSchemeCablePort();
					if (port != null) {
						childSchemeCablePorts.add(port);
					}
				}
			}
			
			
		}
	}
	
	public Set getChildSchemeDevices() {
		return childSchemeDevices;
	}
	
	public Set getChildSchemeElements() {
		return childSchemeElements;
	}
	
	public Set getChildSchemeLinks() {
		return childSchemeLinks;
	}
	
	public Set getChildSchemeProtoElements() {
		return childSchemeProtoElements;
	}
	
	public Set getChildSchemeCablePorts() {
		return childSchemeCablePorts;
	}
	
	public Set getChildSchemePorts() {
		return childSchemePorts;
	}
}
