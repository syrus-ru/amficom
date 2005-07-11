/*
 * $Id: UgoPanel.java,v 1.8 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.event.UndoableEditEvent;

import com.jgraph.graph.CellView;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.GraphUndoManager;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;

/**
 * @author $Author: stas $
 * @version $Revision: 1.8 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class UgoPanel implements Printable, PropertyChangeListener {
	protected ApplicationContext aContext;
	protected SchemeGraph graph;
		
	protected GraphUndoManager undoManager = new GraphUndoManager() {
		public void undoableEditHappened(UndoableEditEvent e) {
			super.undoableEditHappened(e);
			((SchemeMarqueeHandler)graph.getMarqueeHandler()).updateHistoryButtons(this);
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, graph, 
					SchemeEvent.SCHEME_CHANGED), false);
		}
	};
	
	protected UgoPanel(ApplicationContext aContext) {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setContext(aContext);
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(SchemeEvent.TYPE, this);
		}
		if (aContext != null) {
			this.aContext = aContext;
			this.aContext.getDispatcher().addPropertyChangeListener(SchemeEvent.TYPE, this);
			this.graph.setContext(aContext);
		}
	}
	
	private void jbInit() throws Exception {
		this.graph = new SchemeGraph(new DefaultGraphModel(), aContext);
		this.graph.setGridEnabled(true);
		this.graph.setGridVisible(false);
		this.graph.setGridVisibleAtActualSize(false);
		this.graph.setGridColor(Color.lightGray);
		this.graph.setBorderVisible(false);
		this.graph.setPortsVisible(false);
		this.graph.setRequestFocusEnabled(false);
		this.graph.setDragEnabled(false);
		this.graph.setDropEnabled(false);
		this.graph.setCloneable(false);
		// this.graph.setEditable(false);
		// this.graph.setEnabled(false);
		this.graph.setBendable(true);
	}

	public String getReportTitle() {
		return LangModelSchematics.getString("elementsUGOTitle");
	}

	public SchemeGraph getGraph() {
		return graph;
	}
	
	public GraphUndoManager getGraphUndoManager() {
		return undoManager;
	}
	
	void updateGroup(DeviceGroup group, String text) {
		if (group.getChildCount() > 0) {
			for (Enumeration en = group.children(); en.hasMoreElements();) {
				Object child = en.nextElement();
				if (child instanceof DeviceCell) {
					GraphActions.setText(this.graph, child, text);
					break;
				}
			}
		} 
		else {
			GraphActions.setText(this.graph, group, text);
		}
	}
	
	void updateGroup(DeviceGroup group, ImageIcon icon) {
		if (group.getChildCount() > 0) {
			for (Enumeration en = group.children(); en.hasMoreElements();) {
				Object child = en.nextElement();
				if (child instanceof DeviceCell) {
					GraphActions.setImage(this.graph, ((DeviceCell) child), icon);
					break;
				}
			}
		} 
		else {
			GraphActions.setImage(this.graph, group, icon);
		}
	}
	
	public void propertyChange(PropertyChangeEvent ev) {
		if (ev.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent see = (SchemeEvent)ev;
			if (see.isType(SchemeEvent.UPDATE_OBJECT)) {
				Object obj = see.getObject();
				if (obj instanceof SchemeElement) {
					SchemeElement se = (SchemeElement)obj;
					DeviceGroup group = SchemeActions.findGroupById(this.graph, se.getId());
					if (group != null) {
						if (se.getLabel() != null)
							updateGroup(group, se.getLabel());
						if (se.getSymbol() != null)
							updateGroup(group, new ImageIcon(se.getSymbol().getImage()));
					}
				} else if (obj instanceof SchemeProtoElement) {
					SchemeProtoElement proto = (SchemeProtoElement)obj;
					DeviceGroup group = SchemeActions.findGroupById(this.graph, proto.getId());
					if (group != null) {
						if (proto.getLabel() != null)
							updateGroup(group, proto.getLabel());
						if (proto.getSymbol() != null)
							updateGroup(group, new ImageIcon(proto.getSymbol().getImage()));
					}
				} else if (obj instanceof SchemeCableLink) {
					SchemeCableLink link = (SchemeCableLink)obj;
					DefaultGraphCell cell = SchemeActions.findSchemeCableLinkById(this.graph, link.getId());
					if (cell != null) {
						GraphActions.setText(this.graph, cell, link.getName());
					}
				} else if (obj instanceof SchemeLink) {
					SchemeLink link = (SchemeLink)obj;
					DefaultGraphCell cell = SchemeActions.findSchemeLinkById(this.graph, link.getId());
					if (cell != null) {
						GraphActions.setText(this.graph, cell, link.getName());
					}
				} else if (obj instanceof SchemePort) {
					SchemePort port = (SchemePort)obj;
					DefaultGraphCell cell = SchemeActions.findPortCellById(this.graph, port.getId());
					if (cell != null) {
						GraphActions.setText(this.graph, cell, port.getName());
						GraphActions.setObjectBackColor(this.graph, cell, SchemeActions.determinePortColor(port));
					}
					cell = SchemeActions.findBlockPortCellById(this.graph, port.getId());
					if (cell != null) {
						GraphActions.setText(this.graph, cell, port.getName());
					}
				} else if (obj instanceof SchemeCablePort) {
					SchemeCablePort port = (SchemeCablePort)obj;
					DefaultGraphCell cell = SchemeActions.findCablePortCellById(this.graph, port.getId());
					if (cell != null) {
						GraphActions.setText(this.graph, cell, port.getName());
						
						if (port.getAbstractSchemeLink() != null)
							GraphActions.setObjectBackColor(this.graph, cell, Color.WHITE);
						else
							GraphActions.setObjectBackColor(this.graph, cell, Color.YELLOW);
					}
					cell = SchemeActions.findBlockPortCellById(this.graph, port.getId());
					if (cell != null) {
						GraphActions.setText(this.graph, cell, port.getName());
					}
				}
			}
		}
	}

	protected Map insertCell(List serialized, Point p, boolean clone) {
		if (serialized != null) {
			if (clone) {
				Map clones = this.graph.copyFromArchivedState(serialized, p);
				assignClonedIds(clones.values().toArray());
				return clones;
			}
			return this.graph.setFromArchivedState(serialized);
		}
		return null;
	}

	public static void assignClonedIds(Object[] cells) {
		for (int i = 0; i < cells.length; i++) {
			Object cloned_cell = cells[i];
			if (cloned_cell instanceof DeviceGroup) {
				DeviceGroup dev = (DeviceGroup)cloned_cell;
				Identifier or_id = dev.getElementId();
				
				if (dev.getType() == DeviceGroup.PROTO_ELEMENT) {
					Identifier new_id = (Identifier) Pool.get("clonedids", or_id.getIdentifierString());
					if (new_id != null) {
						dev.setProtoElementId(new_id);
					} else {
						new_id = (Identifier) Pool.get("proto2schemeids", or_id.getIdentifierString());
						if (new_id != null)
							dev.setSchemeElementId(new_id);
					}
					
				} else {
					Identifier new_id = (Identifier) Pool.get("clonedids", or_id.getIdentifierString());
					if (new_id != null)
						dev.setSchemeElementId(new_id);
				}
			} else if (cloned_cell instanceof DeviceCell) {
				Identifier c_id = (Identifier) Pool.get("clonedids",
						((DeviceCell) cloned_cell).getSchemeDeviceId()
								.getIdentifierString());
				if (c_id == null) {
					// SchemeDevice dev = ((DeviceCell)cells[i]).getSchemeDevice();
					// SchemeDevice c_dev =
					// (SchemeDevice)dev.clone(aContext.getDataSourceInterface());
					c_id = ((DeviceCell) cloned_cell).getSchemeDeviceId();
				}
				((DeviceCell) cloned_cell).setSchemeDeviceId(c_id);
			} else if (cloned_cell instanceof PortCell) {
				Identifier id = ((PortCell) cloned_cell).getSchemePortId();
				Identifier new_id = (Identifier) Pool.get("clonedids", id
						.getIdentifierString());
				if (new_id != null)
					((PortCell) cloned_cell).setSchemePortId(new_id);
			} else if (cloned_cell instanceof CablePortCell) {
				Identifier id = ((CablePortCell) cloned_cell).getSchemeCablePortId();
				Identifier new_id = (Identifier) Pool.get("clonedids", id
						.getIdentifierString());
				if (new_id != null)
					((CablePortCell) cloned_cell).setSchemeCablePortId(new_id);
			} else if (cloned_cell instanceof DefaultCableLink) {
				Identifier id = ((DefaultCableLink) cloned_cell).getSchemeCableLinkId();
				Identifier new_id = (Identifier) Pool.get("clonedids", id
						.getIdentifierString());
				if (new_id != null)
					((DefaultCableLink) cloned_cell).setSchemeCableLinkId(new_id);

				// ((DefaultCableLink)cloned_cell).setSchemePathId("");
				// String path_id = ((DefaultCableLink)cloned_cell).getSchemePathId();
				// String new_path_id = (String)Pool.get("clonedids", id);
				// if (new_path_id != null)
				// ((DefaultCableLink)cloned_cell).setSchemePathId(new_path_id);
			} else if (cloned_cell instanceof DefaultLink) {
				Identifier id = ((DefaultLink) cloned_cell).getSchemeLinkId();
				Identifier new_id = (Identifier) Pool.get("clonedids", id
						.getIdentifierString());
				if (new_id != null)
					((DefaultLink) cloned_cell).setSchemeLinkId(new_id);

				// ((DefaultLink)cloned_cell).setSchemePathId("");
				// String path_id = ((DefaultLink)cloned_cell).getSchemePathId();
				// String new_path_id = (String)Pool.get("clonedids", id);
				// if (new_path_id != null)
				// ((DefaultLink)cloned_cell).setSchemePathId(new_path_id);
			} else if (cloned_cell instanceof BlockPortCell) {
				BlockPortCell bpc = (BlockPortCell) cloned_cell;
				Identifier p_id = (Identifier) Pool.get("clonedids", bpc
						.getAbstractSchemePortId().getIdentifierString());
				if (bpc.isCablePort()) {
					if (p_id != null)
						bpc.setAbstractSchemePortId(p_id);
				} else {
					if (p_id != null)
						bpc.setAbstractSchemePortId(p_id);
				}
			}
		}
	}

	public int print(Graphics g, PageFormat pf, int pi) {
		if (pi > 0)
			return Printable.NO_SUCH_PAGE;

		this.graph.printAll(g);
		return Printable.PAGE_EXISTS;
	}

	public void setSelectionAttributes(Map map) {
		Object[] cells = DefaultGraphModel.getDescendants(this.graph.getModel(),
				this.graph.getSelectionCells()).toArray();
		map = GraphConstants.cloneMap(map);
		map.remove(GraphConstants.BOUNDS);
		map.remove(GraphConstants.POINTS);
		if (cells != null && cells.length > 0 && !map.isEmpty()) {
			CellView[] views = this.graph.getGraphLayoutCache().getMapping(cells);
			Map viewMap = new HashMap();
			for (int i = 0; i < views.length; i++)
				viewMap.put(views[i], GraphConstants.cloneMap(map));
			this.graph.getGraphLayoutCache().edit(viewMap, null, null, null);
		}
	}

	public void setGraphSize(Dimension d) {
		this.graph.setActualSize(d);
	}
}
