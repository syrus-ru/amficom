/*
 * $Id: UgoPanel.java,v 1.3 2005/04/22 07:32:50 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.*;
import java.awt.print.*;
import java.util.*;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.event.UndoableEditEvent;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client_.scheme.graph.actions.*;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/04/22 07:32:50 $
 * @module schemeclient_v1
 */

public class UgoPanel implements Printable, OperationListener {
	protected ApplicationContext aContext;
	protected SchemeGraph graph;
	protected SchemeResource schemeResource;
	
	protected GraphUndoManager undoManager = new GraphUndoManager() {
		public void undoableEditHappened(UndoableEditEvent e) {
			super.undoableEditHappened(e);
			((SchemeMarqueeHandler)graph.getMarqueeHandler()).updateHistoryButtons(this);
			aContext.getDispatcher().notify(new SchemeEvent(this, graph, 
					SchemeEvent.SCHEME_CHANGED));
		}
	};
	
	public UgoPanel(ApplicationContext aContext) {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		schemeResource = new SchemeResource(this.graph);
		setContext(aContext);
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().unregister(this, SchemeEvent.TYPE);
		}
		if (aContext != null) {
			this.aContext = aContext;
			this.aContext.getDispatcher().register(this, SchemeEvent.TYPE);
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
	
	public SchemeResource getSchemeResource() {
		return schemeResource;
	}
	
	public GraphUndoManager getGraphUndoManager() {
		return undoManager;
	}
	
	
	void updateGroup(DeviceGroup group, String text, ImageIcon icon) {
		if (group.getChildCount() > 0) {
			for (Enumeration en = group.children(); en.hasMoreElements();) {
				Object child = en.nextElement();
				if (child instanceof DeviceCell) {
					GraphActions.setText(this.graph, child, text);
					GraphActions.setImage(this.graph, ((DeviceCell) child), icon);
					break;
				}
			}
		} 
		else {
			GraphActions.setText(this.graph, group, text);
			GraphActions.setImage(this.graph, group, icon);
		}
	}
	
	public void operationPerformed(OperationEvent ae) {
		if (ae.getActionCommand().equals(SchemeEvent.TYPE)) {
			SchemeEvent see = (SchemeEvent) ae;
			if (see.isType(SchemeEvent.UPDATE_OBJECT)) {
				Object obj = see.getObject();
				if (obj instanceof SchemeElement) {
					SchemeElement se = (SchemeElement)obj;
					DeviceGroup group = SchemeActions.findGroupById(this.graph, se.getId());
					if (group != null)
						updateGroup(group, se.getLabel(), new ImageIcon(se.getSymbol().getImage()));
				}
				else if (obj instanceof SchemeProtoElement) {
					SchemeProtoElement proto = (SchemeProtoElement)obj;
					DeviceGroup group = SchemeActions.findGroupById(this.graph, proto.getId());
					if (group != null)
						updateGroup(group, proto.getLabel(), new ImageIcon(proto.getSymbol().getImage()));
				}
				else if (obj instanceof SchemeCableLink) {
					SchemeCableLink link = (SchemeCableLink)obj;
					DefaultGraphCell cell = SchemeActions.findSchemeCableLinkById(this.graph, link.getId());
					if (cell != null) {
						GraphActions.setText(this.graph, cell, link.getName());
					}
				}
				else if (obj instanceof SchemeLink) {
					SchemeLink link = (SchemeLink)obj;
					DefaultGraphCell cell = SchemeActions.findSchemeLinkById(this.graph, link.getId());
					if (cell != null) {
						GraphActions.setText(this.graph, cell, link.getName());
					}
				}
				else if (obj instanceof SchemePort) {
					SchemePort port = (SchemePort)obj;
					DefaultGraphCell cell = SchemeActions.findPortCellById(this.graph, port.getId());
					if (cell != null) {
						GraphActions.setText(this.graph, cell, port.getName());
						
						Color color = Color.WHITE;
						if (port.getPortType().getSort().equals(PortTypeSort.PORTTYPESORT_THERMAL))
							color = Color.BLACK;
						if (port.getAbstractSchemeLink() != null)
							GraphActions.setObjectBackColor(this.graph, cell, color);
						else
							GraphActions.setObjectBackColor(this.graph, cell, Color.YELLOW);
					}
					cell = SchemeActions.findBlockPortCellById(this.graph, port.getId());
					if (cell != null) {
						GraphActions.setText(this.graph, cell, port.getName());
					}
				}
				else if (obj instanceof SchemeCablePort) {
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
				Identifier or_id = ((DeviceGroup) cloned_cell).getProtoElementId();
				Identifier new_id = (Identifier) Pool.get("clonedids", or_id
						.getIdentifierString());
				if (new_id != null)
					((DeviceGroup) cloned_cell).setProtoElementId(new_id);

				or_id = ((DeviceGroup) cloned_cell).getProtoElementId();
				new_id = (Identifier) Pool.get("proto2schemeids", or_id
						.getIdentifierString());
				if (new_id != null)
					((DeviceGroup) cloned_cell).setSchemeElementId(new_id);

				or_id = ((DeviceGroup) cloned_cell).getSchemeElementId();
				new_id = (Identifier) Pool
						.get("clonedids", or_id.getIdentifierString());
				if (new_id != null)
					((DeviceGroup) cloned_cell).setSchemeElementId(new_id);

/*				or_id = ((DeviceGroup) cloned_cell).getSchemeId();
				new_id = (Identifier) Pool
						.get("clonedids", or_id.getIdentifierString());
				if (new_id != null)
					((DeviceGroup) cloned_cell).setSchemeId(new_id);*/
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
