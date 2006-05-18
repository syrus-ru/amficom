/*
 * $Id: CreateGroup.java,v 1.22.4.1 2006/05/18 17:50:00 bass Exp $
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
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.LangModelGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.UgoPanel;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.Rack;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.22.4.1 $, $Date: 2006/05/18 17:50:00 $
 * @module schemeclient
 */

public class CreateGroup extends AbstractAction {
	private static final long serialVersionUID = -7128458304825807589L;
	UgoTabbedPane pane;
	private static int counter = 1;
	private int type;
	
	public CreateGroup(UgoTabbedPane pane, final int type) {
		super(Constants.GROUP);
		this.pane = pane;
		this.type = type;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = this.pane.getGraph();

		for (Object cell : graph.getSelectionCells()) {
			if (cell instanceof DefaultCableLink) {
				Log.debugMessage(LangModelGraph.getString("Error.group.cable"), Level.WARNING); //$NON-NLS-1$
				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
						LangModelGraph.getString("Error.group.cable"), //$NON-NLS-1$
						LangModelGraph.getString("error"), //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				return;
			} else if (cell instanceof Rack) {
				Log.debugMessage(LangModelGraph.getString("Error.group.scheme"), Level.WARNING); //$NON-NLS-1$
				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
						LangModelGraph.getString("Error.group.rack"), //$NON-NLS-1$
						LangModelGraph.getString("error"), //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				return;
			} else if (cell instanceof DeviceGroup) {
				DeviceGroup group = (DeviceGroup)cell;
				if (group.getType() == DeviceGroup.SCHEME) {
					Log.debugMessage(LangModelGraph.getString("Error.group.scheme"), Level.WARNING); //$NON-NLS-1$
					JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
							LangModelGraph.getString("Error.group.scheme"), //$NON-NLS-1$
							LangModelGraph.getString("error"), //$NON-NLS-1$
							JOptionPane.OK_OPTION);
					return;
				}
			}
		}
		
		Object[] cells = getCellsToAdd(graph);
		if (cells.length == 0)
			return;

		// next create group
		SchemeProtoElement protoElement = null;
		SchemeElement element = null;

		if (this.type == DeviceGroup.PROTO_ELEMENT) {
			try {
				protoElement = SchemeObjectsFactory.createSchemeProtoElement();
			} catch (CreateObjectException e1) {
				Log.errorMessage(e1);
				return;
			}
			counter++;
			createProtoGroup(graph, cells, protoElement);
		} else {
			UgoPanel p = this.pane.getCurrentPanel();
			if (p instanceof ElementsPanel) {
				SchemeResource res = ((ElementsPanel)p).getSchemeResource();
				try {
					element = SchemeObjectsFactory.createSchemeElement(res.getScheme());
				} catch (ApplicationException e1) {
					Log.errorMessage(e1);
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
											JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
													LangModelGraph.getString("error_port_type_not_set"), //$NON-NLS-1$
													LangModelGraph.getString("error"), //$NON-NLS-1$
													JOptionPane.OK_OPTION);
											Log.debugMessage("Port type not set for " + ellipse.getSchemePort().getName(), Level.WARNING); //$NON-NLS-1$
											return new Object[0];
										}
										new_cells.add(ellipse);
									} else if (obj instanceof CablePortCell) {
										CablePortCell ellipse = (CablePortCell)obj;
										if (ellipse.getSchemeCablePort().getPortType() == null) {
											JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
													LangModelGraph.getString("error_port_type_not_set"), //$NON-NLS-1$
													LangModelGraph.getString("error"), //$NON-NLS-1$
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
		Set<SchemeProtoElement> childSchemeProtoElements = new HashSet<SchemeProtoElement>();
		Set<SchemeLink> childSchemeLinks = new HashSet<SchemeLink>();
		Set<SchemeDevice> childSchemeDevices = new HashSet<SchemeDevice>();
		
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
		
		try {
			proto.setSchemeDevices(childSchemeDevices);
			proto.setSchemeLinks(childSchemeLinks);
			proto.setSchemeProtoElements(childSchemeProtoElements);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		return group;
	}
	
	public static DeviceGroup createElementsGroup(SchemeGraph graph, Object[] cells, SchemeElement element) {
		DeviceGroup group = createGroup(graph, cells, element.getId(), DeviceGroup.SCHEME_ELEMENT);
		
		// at last determine what elements it consists of
		Set<SchemeElement> childSchemeElements = new HashSet<SchemeElement>();
		Set<SchemeLink> childSchemeLinks = new HashSet<SchemeLink>();
		Set<SchemeDevice> childSchemeDevices = new HashSet<SchemeDevice>();
		
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
		
		try {
			for (SchemeDevice device : childSchemeDevices) {
				device.setParentSchemeElement(element);
			}
			for (SchemeLink link : childSchemeLinks) {
				link.setParentSchemeElement(element);
			}
			for (SchemeElement element2 : childSchemeElements) {
				element2.setParentSchemeElement(element);
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		return group;
	}
	
	static DeviceGroup createGroup(SchemeGraph graph, Object[] cells, Identifier id, int type) {
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
		DeviceGroup group = DeviceGroup.createInstance(null, viewMap, id, type);
		
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