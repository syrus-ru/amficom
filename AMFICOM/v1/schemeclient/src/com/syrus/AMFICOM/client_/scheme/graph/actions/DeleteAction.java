/*
 * $Id: DeleteAction.java,v 1.35 2006/02/16 13:59:13 stas Exp $
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
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.Port;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.LangModelGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortEdge;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortEdge;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.35 $, $Date: 2006/02/16 13:59:13 $
 * @module schemeclient
 */

public class DeleteAction extends AbstractAction {
	private static final long serialVersionUID = -8819605194892661368L;
	ElementsTabbedPane pane;
	private static Set<DefaultGraphCell> cellsToDelete = new HashSet<DefaultGraphCell>();
	private static Set<Identifier> objectsToDelete = new HashSet<Identifier>();
	
	public DeleteAction(ElementsTabbedPane pane) {
		super(Constants.DELETE);
		this.pane = pane; 
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = this.pane.getGraph();
				
		Object[] cells = graph.getDescendants(graph.getSelectionCells());
		if (cells != null) {
			if (this.pane instanceof SchemeTabbedPane) {
				ElementsPanel panel = ((SchemeTabbedPane)this.pane).getCurrentPanel();
				if (panel.isTopLevelSchemeMode()) {
					int ret = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(),
							LangModelGraph.getString("remove_elements_schematic"),  //$NON-NLS-1$
							LangModelGraph.getString("confirm"), //$NON-NLS-1$
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (ret == JOptionPane.YES_OPTION) {
						graph.getModel().remove(cells);
					}
					return;
				} 
			}

			int ret = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(),
					LangModelGraph.getString("remove_elements"),  //$NON-NLS-1$
					LangModelGraph.getString("confirm"),  //$NON-NLS-1$
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (ret != JOptionPane.YES_OPTION) {
				return;
			}

			DefaultGraphCell[] cells1 = GraphActions.getTopLevelCells(cells);

			delete(this.pane.getCurrentPanel(), cells1);
			
			this.pane.getContext().getDispatcher().firePropertyChange(new SchemeEvent(this, Identifier.VOID_IDENTIFIER, SchemeEvent.DELETE_OBJECT));
			graph.selectionNotify();
			
//			if (this.pane instanceof SchemeTabbedPane) {
//				SchemeResource res = ((SchemeTabbedPane)this.pane).getCurrentPanel().getSchemeResource();
//				if (res.getCellContainer() instanceof Identifiable) {
//					this.pane.getContext().getDispatcher().firePropertyChange(new SchemeEvent(this, ((Identifiable)res.getCellContainer()).getId(), SchemeEvent.SCHEME_CHANGED));
//				}
//			}
		}
	}
	
	public static void delete(ElementsPanel panel, Object cell) {
		SchemeGraph graph = panel.getGraph();
		deleteCell(graph, (DefaultGraphCell)cell);

		StorableObjectPool.delete(objectsToDelete);
		objectsToDelete.clear();
		
		Object[] cells = DefaultGraphModel.getDescendants(graph.getModel(), cellsToDelete.toArray()).toArray();
		cellsToDelete.clear();
		graph.getModel().remove(cells);
	}
	
	public static void delete(ElementsPanel panel, Object[] cells) {
		SchemeGraph graph = panel.getGraph();		
		for (int i = 0; i < cells.length; i++) {
			deleteCell(graph, (DefaultGraphCell)cells[i]);
		}
		StorableObjectPool.delete(objectsToDelete);
		objectsToDelete.clear();
		
		cells = DefaultGraphModel.getDescendants(graph.getModel(), cellsToDelete.toArray()).toArray();
		cellsToDelete.clear();
		graph.clearSelection();
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
	
	static void deleteScheme(Scheme scheme) {
		try {
			SchemeElement se = scheme.getParentSchemeElement();
			scheme.setParentSchemeElement(null, false);
			if (se != null) {
				objectsToDelete.add(se.getId());
			} else {
				Log.debugMessage("DeleteAction.deleteScheme(): parent SchemeElement not found for scheme " + scheme.getName()+ "(" + scheme.getId() + ")" + "; only delete graph cell", Level.FINER);
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
		
	static void deleteSchemeElement(SchemeElement element) {
		objectsToDelete.add(element.getId());
		if (element.getKind() == IdlSchemeElementKind.SCHEME_ELEMENT_CONTAINER) {
			try {
				if(element.getEquipment() != null) {
					objectsToDelete.add(element.getEquipment().getId());
				}
				for (Iterator it = element.getSchemeLinks(false).iterator(); it.hasNext();) {
					objectsToDelete.add(((SchemeLink)it.next()).getId());
				}
				for (Iterator it = element.getSchemePortsRecursively(false).iterator(); it.hasNext();) {
					objectsToDelete.add(((SchemePort)it.next()).getId());
				}
				for (Iterator it = element.getSchemeCablePortsRecursively(false).iterator(); it.hasNext();) {
					objectsToDelete.add(((SchemeCablePort)it.next()).getId());
				}
				for (Iterator it = element.getSchemeDevices(false).iterator(); it.hasNext();) {
					objectsToDelete.add(((SchemeDevice)it.next()).getId());
				}
				for (Iterator it = element.getSchemeElements(false).iterator(); it.hasNext();) {
					objectsToDelete.add(((SchemeElement)it.next()).getId());
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
	}
	
	static void deleteSchemeProtoElement(SchemeProtoElement element) {
		objectsToDelete.add(element.getId());
		try {
			for (Iterator it = element.getSchemeLinks(false).iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemeLink)it.next()).getId());
			}
		} catch (ApplicationException e2) {
			Log.errorMessage(e2);
		}
		try {
			for (Iterator it = element.getSchemePortsRecursively(false).iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemePort)it.next()).getId());
			}
		} catch (ApplicationException e1) {
			Log.errorMessage(e1);
		}
		try {
			for (Iterator it = element.getSchemeCablePortsRecursively(false).iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemeCablePort)it.next()).getId());
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		try {
			for (Iterator it = element.getSchemeDevices(false).iterator(); it.hasNext();) {
				objectsToDelete.add(((SchemeDevice)it.next()).getId());
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
	
	static void deleteDeviceGroup(DeviceGroup group) {
		try {
			cellsToDelete.add(group);
			if (group.getType() == DeviceGroup.SCHEME_ELEMENT) {
				SchemeElement element = group.getSchemeElement();
				
				if (element != null) {
					if (element.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
						Scheme scheme = element.getScheme(false);
						deleteScheme(scheme);
					} else {
						deleteSchemeElement(element);
					}
				} else {
					Log.debugMessage("DeleteAction.deleteDeviceGroup(): no SchemeElement found with id " + group.getElementId() + "; only delete graph cell", Level.FINER);
				}
			} else if (group.getType() == DeviceGroup.PROTO_ELEMENT) {
				SchemeProtoElement element = group.getProtoElement();
				if (element != null) {
					deleteSchemeProtoElement(element);
				} else {
					Log.debugMessage("DeleteAction.deleteDeviceGroup(): no proto found with id " + group.getElementId() + "; only delete graph cell", Level.FINER);
				}
				// FIXME can't check childs while object itself not saved
//				for (Iterator it = element.getSchemeProtoElements().iterator(); it.hasNext();) {
//					this.objectsToDelete.add(((SchemeElement)it.next()).getId());
//				}
			} else if (group.getType() == DeviceGroup.SCHEME) {
				Scheme scheme = group.getScheme();
				if (scheme != null) {
					deleteScheme(scheme);
				} else {
					Log.debugMessage("DeleteAction.deleteDeviceGroup(): no scheme found with id " + group.getElementId() + "; only delete graph cell", Level.FINER);
				}
			}
		} catch (final Exception ae) {
			Log.errorMessage(ae);
		}
	}
	
	static void deleteCableLink(SchemeGraph graph, DefaultCableLink cell) {
		cellsToDelete.add(cell);
		SchemeCableLink link = cell.getSchemeCableLink();
		
		if (link != null) {
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
			try {
				for (SchemeCableThread thread : link.getSchemeCableThreads(false)) {
					objectsToDelete.add(thread.getId());
				}
				for (CableChannelingItem cci : link.getPathMembers()) {
					objectsToDelete.add(cci.getId());
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		} else {
			Log.debugMessage("DeleteAction.deleteCableLink(): no link found with id " + cell.getSchemeCableLinkId() + "; only delete graph cell", Level.FINER);
		}
	}
	
	static void deleteLink(SchemeGraph graph, DefaultLink cell) {
		cellsToDelete.add(cell);
		SchemeLink link = cell.getSchemeLink();

		if (link != null) {
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
		} else {
			Log.debugMessage("DeleteAction.deleteLink(): no link found with id " + cell.getSchemeLinkId() + "; only delete graph cell", Level.FINER);
		}
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
		if (port != null) {
			if (port.getPort() != null) {
				objectsToDelete.add(port.getPort().getId());
			}
			objectsToDelete.add(port.getId());
		} else {
			Log.debugMessage("DeleteAction.deletePort(): no port found with id " + cell.getSchemePortId() + "; only delete graph cell", Level.FINER);
		}
		deletePort1(graph, cell);
	}
	
	static void deleteCablePort(SchemeGraph graph, CablePortCell cell) {
		SchemeCablePort port = cell.getSchemeCablePort();
		if (port != null) {
			if (port.getPort() != null) {
				objectsToDelete.add(port.getPort().getId());
			}
			objectsToDelete.add(port.getId());
		} else {
			Log.debugMessage("DeleteAction.deleteCablePort(): no port found with id " + cell.getSchemeCablePortId() + "; only delete graph cell", Level.FINER);
		}
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
