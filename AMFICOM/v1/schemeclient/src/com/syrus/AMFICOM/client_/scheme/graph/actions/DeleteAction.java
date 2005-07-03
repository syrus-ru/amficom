/*
 * $Id: DeleteAction.java,v 1.5 2005/06/22 15:05:19 bass Exp $
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
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/22 15:05:19 $
 * @module schemeclient_v1
 */

public class DeleteAction extends AbstractAction {
	ApplicationContext aContext;
	UgoTabbedPane pane;
	
	public DeleteAction(UgoTabbedPane pane, ApplicationContext aContext) {
		super(Constants.DELETE);
		this.aContext = aContext;
		this.pane = pane; 
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = pane.getGraph();
		SchemeResource resource = pane.getCurrentPanel().getSchemeResource(); 
		
		Object[] cells = graph.getSelectionCells();
		Set new_cells = new HashSet(cells.length);
		if (cells != null) {
			if (graph.isTopLevelSchemeMode()) {
				int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
						"Удалить элементы со схематичного вида?", "Подтверждение",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					graph.getModel().remove(graph.getDescendants(cells));
					return;
				}
			} else {
				int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
						"Удалить все эти элементы?",
						"Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION);
				if (ret != JOptionPane.YES_OPTION)
					return;
					
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof DeviceGroup) {
						new_cells.add(deleteDeviceGroup(graph, resource, (DeviceGroup)cells[i]));
					} 
					else if (cells[i] instanceof DefaultCableLink) {
						new_cells.add(deleteCableLink(graph, resource, (DefaultCableLink)cells[i]));
					} 
					else if (cells[i] instanceof DefaultLink) {
						new_cells.add(deleteLink(graph, resource, (DefaultLink)cells[i]));
					} 
					else if (cells[i] instanceof BlockPortCell) {
						for (Enumeration en = ((BlockPortCell) cells[i]).children(); en.hasMoreElements();) {
							Port p = (Port) en.nextElement();
							for (Iterator it = p.edges(); it.hasNext();) {
								DefaultEdge edge = (DefaultEdge) it.next();
								deleteConnections(graph, edge, (DefaultPort) edge.getSource());
								deleteConnections(graph, edge, (DefaultPort) edge.getTarget());
								new_cells.add(edge);
							}
						}
						new_cells.add(cells[i]);
					} else if (!GraphActions.hasGroupedParent(cells[i])) {
						if (cells[i] instanceof PortCell) {
							new_cells.add(deletePort(graph, resource, (PortCell)cells[i]));
						} else if (cells[i] instanceof CablePortCell) {
							new_cells.add(deleteCablePort(graph, resource, (CablePortCell)cells[i]));
						} 
						else if (cells[i] instanceof DefaultEdge) {
							DefaultEdge edge = (DefaultEdge) cells[i];
							if (edge.getSource() instanceof DefaultPort) {
								Object p = ((DefaultPort) edge.getSource()).getParent();
								if (p instanceof PortCell) {
									new_cells.add(deletePort(graph, resource, (PortCell)p));
								} 
								else if (p instanceof CablePortCell) {
									new_cells.add(deleteCablePort(graph, resource, (CablePortCell)p));
								}
							} else {
								new_cells.add(edge);
								deleteConnections(graph, edge, (DefaultPort) edge.getSource());
							}
							if (edge.getSource() instanceof DefaultPort) {
								Object p = ((DefaultPort) edge.getSource()).getParent();
								if (p instanceof PortCell) {
									new_cells.add(deletePort(graph, resource, (PortCell)p));
								} 
								else if (p instanceof CablePortCell) {
									new_cells.add(deleteCablePort(graph, resource, (CablePortCell)p));
								}
							} else {
								new_cells.add(edge);
								deleteConnections(graph, edge, (DefaultPort) edge.getTarget());
							}
							
						} 
						else
							new_cells.add(cells[i]);
					}
				}
			}
			
			cells = DefaultGraphModel.getDescendants(graph.getModel(),
					new_cells.toArray(new Object[new_cells.size()])).toArray();
			graph.getModel().remove(cells);
			graph.selectionNotify();
			aContext.getDispatcher().firePropertyChange(
					new SchemeEvent(this, graph, SchemeEvent.SCHEME_CHANGED));
		}
	}
	
	Collection deleteDeviceGroup(SchemeGraph graph, SchemeResource resource, DeviceGroup group) {
		if (group.getSchemeElementId() != null) {
			SchemeElement element = group.getSchemeElement();
			if (resource.getScheme() != null) {
				resource.getScheme().removeSchemeElement(element);
				if (element.getEquipment() != null)
					StorableObjectPool.delete(element.getEquipment().getId());
				StorableObjectPool.delete(element.getId());
			}
		}
		return Arrays.asList(new DeviceGroup[] {group});
	}
	
	Collection deleteCableLink(SchemeGraph graph, SchemeResource resource, DefaultCableLink cell) {
		SchemeCableLink link = cell.getSchemeCableLink();
		SchemeActions.disconnectSchemeCableLink(graph, cell, true);
		SchemeActions.disconnectSchemeCableLink(graph, cell, false);
		if (resource.getScheme() != null)
			resource.getScheme().removeSchemeCableLink(link);
		if (link.getAbstractLink() != null) {
			ConfigurationStorableObjectPool.delete(link.getAbstractLink().getId());
		}
		SchemeStorableObjectPool.delete(link.getId());
		return Arrays.asList(new DefaultCableLink[] { cell });
	}
	
	Collection deleteLink(SchemeGraph graph, SchemeResource resource, DefaultLink cell) {
		SchemeLink link = cell.getSchemeLink();
		SchemeActions.disconnectSchemeLink(graph, cell, true);
		SchemeActions.disconnectSchemeLink(graph, cell, false);

		if (resource.getScheme() != null)
			resource.getScheme().removeSchemeLink(link);
		if (resource.getSchemeElement() != null)
			resource.getSchemeElement().removeSchemeLink(link);
		if (link.getAbstractLink() != null) {
			link.setAbstractLink(null);
			ConfigurationStorableObjectPool.delete(link.getAbstractLink().getId());
		}
		SchemeStorableObjectPool.delete(link.getId());
		return Arrays.asList(new DefaultLink[] { cell });
	}
	
	Collection deletePort(SchemeGraph graph, SchemeResource resource, PortCell cell) {
		Collection objects = new LinkedList();
		objects.add(cell);
		SchemePort port = cell.getSchemePort();
		if (port.getPort() != null) {
			ConfigurationStorableObjectPool.delete(port.getPort().getId());
		}
		SchemeStorableObjectPool.delete(port.getId());
		for (Enumeration en = cell.children(); en.hasMoreElements();) {
			Port p = (Port) en.nextElement();
			for (Iterator it = p.edges(); it.hasNext();) {
				DefaultEdge edge = (DefaultEdge) it.next();
				objects.add(edge);

				removePortFromParent((DefaultPort) edge.getSource(), port);
				removePortFromParent((DefaultPort) edge.getTarget(), port);

				deleteConnections(graph, edge, (DefaultPort) edge.getSource());
				deleteConnections(graph, edge, (DefaultPort) edge.getTarget());
			}
		}
		return objects;
	}
	
	Collection deleteCablePort(SchemeGraph graph, SchemeResource resource, CablePortCell cell) {
		Collection objects = new LinkedList();
		objects.add(cell);
		SchemeCablePort port = cell.getSchemeCablePort();
		if (port.getPort() != null) {
			ConfigurationStorableObjectPool.delete(port.getPort().getId());
		}
		SchemeStorableObjectPool.delete(port.getId());
		for (Enumeration en = cell.children(); en.hasMoreElements();) {
			Port p = (Port) en.nextElement();
			for (Iterator it = p.edges(); it.hasNext();) {
				DefaultEdge edge = (DefaultEdge) it.next();
				objects.add(edge);

				removeCablePortFromParent((DefaultPort) edge.getSource(), port);
				removeCablePortFromParent((DefaultPort) edge.getTarget(), port);

				deleteConnections(graph, edge, (DefaultPort) edge.getSource());
				deleteConnections(graph, edge, (DefaultPort) edge.getTarget());
			}
		}
		return objects;
	}

	private void deleteConnections(SchemeGraph graph, DefaultEdge edge, DefaultPort port) {
		if (port != null) {
			DefaultGraphCell cell = (DefaultGraphCell) port.getParent();
			if (cell != null) {
				GraphActions.disconnectEdge(graph, edge, port, false);
				GraphActions.removePort(graph, cell, port, cell.getAttributes());
			}
		}
	}

	private void removePortFromParent(DefaultPort port, SchemePort sp) {
		if (port != null) {
			DefaultGraphCell parent = (DefaultGraphCell) port.getParent();
			if (parent instanceof DeviceCell) {
				SchemeDevice dev = ((DeviceCell) parent).getSchemeDevice();
				if (dev != null)
					dev.removeSchemePort(sp);
			}
		}
	}

	private void removeCablePortFromParent(DefaultPort port, SchemeCablePort scp) {
		if (port != null) {
			DefaultGraphCell parent = (DefaultGraphCell) port.getParent();
			if (parent instanceof DeviceCell) {
				SchemeDevice dev = ((DeviceCell) parent).getSchemeDevice();
				if (dev != null)
					dev.removeSchemeCablePort(scp);
			}
		}
	}
}
