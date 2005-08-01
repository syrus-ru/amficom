/*
 * $Id: DeleteAction.java,v 1.8 2005/08/01 07:52:28 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.Port;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortEdge;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortEdge;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;

/**
 * @author $Author: stas $
 * @version $Revision: 1.8 $, $Date: 2005/08/01 07:52:28 $
 * @module schemeclient_v1
 */

public class DeleteAction extends AbstractAction {
	UgoTabbedPane pane;
	private static Set<DefaultGraphCell> cellsToDelete = new HashSet<DefaultGraphCell>();
	private static Set<Identifier> objectsToDelete = new HashSet<Identifier>();
	
	public DeleteAction(UgoTabbedPane pane) {
		super(Constants.DELETE);
		this.pane = pane; 
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = pane.getGraph();
		
		Object[] cells = graph.getDescendants(graph.getSelectionCells());
		if (cells != null) {
			if (graph.isTopLevelSchemeMode()) {
				int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
						"Удалить элементы со схематичного вида?", "Подтверждение",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					graph.getModel().remove(cells);
					return;
				}
			} else {
				int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
						"Удалить все эти элементы?",
						"Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION);
				if (ret != JOptionPane.YES_OPTION)
					return;
			}
			delete(graph, cells);
			
			graph.selectionNotify();
			pane.getContext().getDispatcher().firePropertyChange(new SchemeEvent(this, graph, SchemeEvent.SCHEME_CHANGED));
		}
	}
	
	public static void delete(SchemeGraph graph, Object cell) {
		deleteCell(graph, (DefaultGraphCell)cell);

		StorableObjectPool.delete(objectsToDelete);
		objectsToDelete.clear();
		
		Object[] cells = DefaultGraphModel.getDescendants(graph.getModel(), cellsToDelete.toArray()).toArray();
		cellsToDelete.clear();
		graph.getModel().remove(cells);
	}
	
	public static void delete(SchemeGraph graph, Object[] cells) {
		for (int i = 0; i < cells.length; i++) {
			deleteCell(graph, (DefaultGraphCell)cells[i]);
		}
		StorableObjectPool.delete(objectsToDelete);
		objectsToDelete.clear();
		
		cells = DefaultGraphModel.getDescendants(graph.getModel(), cellsToDelete.toArray()).toArray();
		cellsToDelete.clear();
		graph.getModel().remove(cells);
	}
	
	static void deleteCell(SchemeGraph graph, DefaultGraphCell cell) {
		if (cell instanceof DeviceGroup) {
			deleteDeviceGroup((DeviceGroup)cell);
		}
		else if (cell instanceof DeviceCell) {
			deleteDeviceCell(graph, (DeviceCell)cell);
		}
		else if (cell instanceof DefaultCableLink) {
			deleteCableLink(graph, (DefaultCableLink)cell);
		} 
		else if (cell instanceof DefaultLink) {
			deleteLink(graph, (DefaultLink)cell);
		} 
		else if (cell instanceof BlockPortCell) {
			deleteBlockPortCell(graph, (BlockPortCell)cell);
		} 
		else if (cell instanceof PortCell) {
			deletePort(graph, (PortCell)cell);
		} 
		else if (cell instanceof CablePortCell) {
			deleteCablePort(graph, (CablePortCell)cell);
		}
		else if (cell instanceof DefaultEdge) {
			DefaultEdge edge = (DefaultEdge) cell;
			if (edge.getSource() instanceof DefaultPort) {
				Object p = ((DefaultPort) edge.getSource()).getParent();
				if (p instanceof PortCell) {
					deletePort(graph, (PortCell)p);
				} 
				else if (p instanceof CablePortCell) {
					deleteCablePort(graph, (CablePortCell)p);
				}
			} else {
				cellsToDelete.add(edge);
				deleteConnections(graph, edge, (DefaultPort) edge.getSource());
			}
			if (edge.getTarget() instanceof DefaultPort) {
				Object p = ((DefaultPort) edge.getTarget()).getParent();
				if (p instanceof PortCell) {
					deletePort(graph, (PortCell)p);
				} 
				else if (p instanceof CablePortCell) {
					deleteCablePort(graph, (CablePortCell)p);
				}
			} else {
				cellsToDelete.add(edge);
				deleteConnections(graph, edge, (DefaultPort) edge.getTarget());
			}
		} else {
			cellsToDelete.add(cell);
		}
	}
		
	static void deleteDeviceCell(SchemeGraph graph, DeviceCell cell) {
		cellsToDelete.add(cell);
		
		for (Enumeration en = cell.children(); en.hasMoreElements();) {
			DefaultPort port = (DefaultPort)en.nextElement();
			for (Iterator it = port.edges(); it.hasNext();) {
				DefaultEdge edge = (DefaultEdge)it.next();
				Object obj = ((DefaultPort)edge.getSource()).getParent();
				if (obj instanceof PortCell) {
					deletePort(graph, (PortCell)obj);
				} else if (obj instanceof CablePortCell) {
					deleteCablePort(graph, (CablePortCell)obj);
				}

				obj = ((DefaultPort)edge.getTarget()).getParent();
				if (obj instanceof PortCell) {
					deletePort(graph, (PortCell)obj);
				} else if (obj instanceof CablePortCell) {
					deleteCablePort(graph, (CablePortCell)obj);
				}
			}
		}
	}
	
	static void deleteDeviceGroup(DeviceGroup group) {
		cellsToDelete.add(group);
		if (group.getType() == DeviceGroup.SCHEME_ELEMENT) {
			SchemeElement element = group.getSchemeElement();
			if(element.getEquipment() != null)
				objectsToDelete.add(element.getEquipment().getId());
			objectsToDelete.add(element.getId());
			for (Iterator it = element.getSchemeLinks().iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemeLink)it.next()).getId());
			}
			for (Iterator it = element.getSchemePortsRecursively().iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemePort)it.next()).getId());
			}
			for (Iterator it = element.getSchemeCablePortsRecursively().iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemeCablePort)it.next()).getId());
			}
			for (Iterator it = element.getSchemeDevices().iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemeDevice)it.next()).getId());
			}
			for (Iterator it = element.getSchemeElements().iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemeElement)it.next()).getId());
			}
		} else if (group.getType() == DeviceGroup.PROTO_ELEMENT) {
			SchemeProtoElement element = group.getProtoElement();
			objectsToDelete.add(element.getId());
			for (Iterator it = element.getSchemeLinks().iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemeLink)it.next()).getId());
			}
			for (Iterator it = element.getSchemePortsRecursively().iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemePort)it.next()).getId());
			}
			for (Iterator it = element.getSchemeCablePortsRecursively().iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemeCablePort)it.next()).getId());
			}
			for (Iterator it = element.getSchemeDevices().iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemeDevice)it.next()).getId());
			}
			// FIXME can't check childs while object itself not saved
//			for (Iterator it = element.getSchemeProtoElements().iterator(); it.hasNext();) {
//				this.objectsToDelete.add(((SchemeElement)it.next()).getId());
//			}
		}
	}
	
	static void deleteCableLink(SchemeGraph graph, DefaultCableLink cell) {
		cellsToDelete.add(cell);
		SchemeCableLink link = cell.getSchemeCableLink();
		
		if (cell.getSource() != null) {
			DefaultPort p = (DefaultPort)cell.getSource();
			if(p.getParent() instanceof CablePortCell) {
				SchemeActions.disconnectSchemeCableLink(graph, cell, (CablePortCell)p.getParent(), true);		
			}
		}
		if (cell.getTarget() != null) {
			DefaultPort p = (DefaultPort)cell.getTarget();
			if(p.getParent() instanceof CablePortCell) {
				SchemeActions.disconnectSchemeCableLink(graph, cell, (CablePortCell)p.getParent(), false);		
			}
		}
		if (link.getAbstractLink() != null) {
			objectsToDelete.add(link.getAbstractLink().getId());
		}
		objectsToDelete.add(link.getId());
	}
	
	static void deleteLink(SchemeGraph graph, DefaultLink cell) {
		cellsToDelete.add(cell);
		SchemeLink link = cell.getSchemeLink();

		if (cell.getSource() != null) {
			DefaultPort p = (DefaultPort)cell.getSource();
			if(p.getParent() instanceof PortCell) {
				SchemeActions.disconnectSchemeLink(graph, cell, (PortCell)p.getParent(), true);		
			}
		}
		if (cell.getTarget() != null) {
			DefaultPort p = (DefaultPort)cell.getTarget();
			if(p.getParent() instanceof PortCell) {
				SchemeActions.disconnectSchemeLink(graph, cell, (PortCell)p.getParent(), false);		
			}
		}

		if (link.getAbstractLink() != null) {
			objectsToDelete.add(link.getAbstractLink().getId());
		}
		objectsToDelete.add(link.getId());
	}
	
	static void deleteBlockPortCell(SchemeGraph graph, BlockPortCell cell) {
		cellsToDelete.add(cell);
		for (Enumeration en = cell.children(); en.hasMoreElements();) {
			Port p = (Port) en.nextElement();
			for (Iterator it = p.edges(); it.hasNext();) {
				DefaultEdge edge = (DefaultEdge) it.next();
				deleteConnections(graph, edge, (DefaultPort) edge.getSource());
				deleteConnections(graph, edge, (DefaultPort) edge.getTarget());
				cellsToDelete.add(edge);
			}
		}
	}
	
	private static void deletePort1(SchemeGraph graph, DefaultGraphCell cell) {
		cellsToDelete.add(cell);
		for (Enumeration en = cell.children(); en.hasMoreElements();) {
			Port p = (Port) en.nextElement();
			for (Iterator it = p.edges(); it.hasNext();) {
				Object edge = it.next();
				if (edge instanceof DefaultLink && cell instanceof PortCell) {
					SchemeActions.disconnectSchemeLink(graph, (DefaultLink)edge, (PortCell)cell, true);
				} else if (edge instanceof DefaultCableLink && cell instanceof CablePortCell) {
					SchemeActions.disconnectSchemeCableLink(graph, (DefaultCableLink)edge, (CablePortCell)cell, true);
				} else if (edge instanceof BlockPortEdge) {
					BlockPortEdge bpEdge = (BlockPortEdge)edge;
					Object object = ((DefaultPort)bpEdge.getTarget()).getParent();
					if (object instanceof BlockPortCell)
						deleteBlockPortCell(graph, (BlockPortCell)object);
				}	else if (edge instanceof PortEdge) {
					PortEdge portEdge = (PortEdge)edge;
					cellsToDelete.add(portEdge);
					deleteConnections(graph, portEdge, (DefaultPort) portEdge.getSource());
					deleteConnections(graph, portEdge, (DefaultPort) portEdge.getTarget());	
				} else if (edge instanceof DefaultEdge) {
					DefaultEdge defaultEdge = (DefaultEdge)edge;
					deleteConnections(graph, defaultEdge, (DefaultPort)defaultEdge.getSource());
					deleteConnections(graph, defaultEdge, (DefaultPort)defaultEdge.getTarget());
				}
			}
		}
	}
	
	static void deletePort(SchemeGraph graph, PortCell cell) {
		SchemePort port = cell.getSchemePort();
		if (port.getPort() != null) {
			objectsToDelete.add(port.getPort().getId());
		}
		objectsToDelete.add(port.getId());
		deletePort1(graph, cell);
	}
	
	static void deleteCablePort(SchemeGraph graph, CablePortCell cell) {
		SchemeCablePort port = cell.getSchemeCablePort();
		if (port.getPort() != null) {
			objectsToDelete.add(port.getPort().getId());
		}
		objectsToDelete.add(port.getId());
		deletePort1(graph, cell);
	}
	
	private static void deleteConnections(SchemeGraph graph, DefaultEdge edge, DefaultPort port) {
		if (port != null) {
			DefaultGraphCell cell = (DefaultGraphCell) port.getParent();
			if (cell != null) {
				GraphActions.disconnectEdge(graph, edge, port, false);
				Iterator it = port.edges();
				it.next();
				if (!it.hasNext())
					cellsToDelete.add(port);
			}
		}
	}
}
