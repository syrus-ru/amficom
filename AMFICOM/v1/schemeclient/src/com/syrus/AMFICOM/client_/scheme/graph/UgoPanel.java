/*
 * $Id: UgoPanel.java,v 1.1 2005/04/05 14:07:53 stas Exp $
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
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:53 $
 * @module schemeclient_v1
 */

public class UgoPanel implements Printable, OperationListener {
	protected ApplicationContext aContext;
	protected SchemeGraph graph;
	protected SchemeResource schemeResource;
	
	protected GraphUndoManager undoManager = new GraphUndoManager() {
		public void undoableEditHappened(UndoableEditEvent e) {
			super.undoableEditHappened(e);
			((ShemeMarqueeHandler)graph.getMarqueeHandler()).updateHistoryButtons(this);
			aContext.getDispatcher().notify(new SchemeEvent(this, graph, null, 
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
	
	public void operationPerformed(OperationEvent ae) {
		if (ae.getActionCommand().equals(SchemeEvent.TYPE)) {
			SchemeEvent see = (SchemeEvent) ae;
			if (see.isType(SchemeEvent.UGO_TEXT_UPDATE)) {
				Identifier id = (Identifier) see.getObject();
				String text = (String)see.getValue();
				DeviceGroup[] groups = GraphActions.findTopLevelGroups(this.graph,
						this.graph.getSelectionCells());
				if (groups.length == 1) {
					DeviceGroup cell = groups[0];
					if (id.equals(cell.getProtoElementId()) || id.equals(cell.getSchemeElementId())) {
						if (cell.getChildCount() > 0) {
							for (Enumeration en = cell.children(); en.hasMoreElements();) {
								Object child = en.nextElement();
								if (child instanceof DeviceCell) {
									GraphActions.setText(this.graph, child, text);
									break;
								}
							}
						} 
						else
							GraphActions.setText(this.graph, cell, text);
					}
				}
			}
			if (see.isType(SchemeEvent.UGO_ICON_UPDATE)) {
				Identifier id = (Identifier)see.getObject();
				ImageIcon icon = (ImageIcon)see.getValue();
				DeviceGroup[] groups = GraphActions.findTopLevelGroups(this.graph,
						this.graph.getSelectionCells());
				if (groups.length == 1) {
					DeviceGroup cell = groups[0];
					if (cell.getProtoElementId().equals(id)
							|| cell.getSchemeElementId().equals(id)) {
						if (cell.getChildCount() > 0) {
							for (Enumeration en = cell.children(); en.hasMoreElements();) {
								Object child = en.nextElement();
								if (child instanceof DeviceCell) {
									GraphActions.setImage(this.graph, ((DeviceCell) child), icon);
									break;
								}
							}
						} else
							GraphActions.setImage(this.graph, cell, icon);
					}
				}
			}
			if (see.isType(SchemeEvent.CABLE_PORT_NAME_UPDATE)) {
				Identifier id = (Identifier)see.getObject();
				String text = (String)see.getValue();

				Object[] cells = this.graph.getAll();
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof CablePortCell
							&& ((CablePortCell) cells[i]).getSchemeCablePortId().equals(id))
						GraphActions.setText(this.graph, cells[i], text);
					else if (cells[i] instanceof BlockPortCell
							&& ((BlockPortCell) cells[i]).getSchemeCablePortId().equals(id))
						GraphActions.setText(this.graph, cells[i], text);
				}
			}
			if (see.isType(SchemeEvent.PORT_NAME_UPDATE)) {
				Identifier id = (Identifier)see.getObject();
				String text = (String)see.getValue();

				Object[] cells = this.graph.getAll();
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof PortCell
							&& ((PortCell) cells[i]).getSchemePortId().equals(id))
						GraphActions.setText(this.graph, cells[i], text);
					else if (cells[i] instanceof BlockPortCell
							&& ((BlockPortCell) cells[i]).getSchemePortId().equals(id))
						GraphActions.setText(this.graph, cells[i], text);
				}
			}
			if (see.isType(SchemeEvent.CABLE_LINK_NAME_UPDATE)) {
				Identifier id = (Identifier)see.getObject();
				String text = (String)see.getValue();

				Object[] cells = this.graph.getAll();
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof DefaultCableLink
							&& ((DefaultCableLink) cells[i]).getSchemeCableLinkId()
									.equals(id)) {
						GraphActions.setText(this.graph, cells[i], text);
						break;
					}
				}
			}
			if (see.isType(SchemeEvent.LINK_NAME_UPDATE)) {
				Identifier id = (Identifier)see.getObject();
				String text = (String)see.getValue();

				Object[] cells = this.graph.getAll();
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof DefaultLink
							&& ((DefaultLink) cells[i]).getSchemeLinkId().equals(id)) {
						GraphActions.setText(this.graph, cells[i], text);
						break;
					}
				}
			}
			if (see.isType(SchemeEvent.PORT_TYPE_UPDATE)) {
				Collection portIds = (Collection)see.getObject();
				PortType type = (PortType)see.getValue();
				
				Object[] cells = this.graph.getAll();
				List connected_ports = new LinkedList();
				List non_connected_ports = new LinkedList();
				for (int i = 0; i < cells.length; i++)
					if (cells[i] instanceof PortCell) {
						PortCell cell = (PortCell)cells[i];
						Identifier portId = cell.getSchemePortId();
						if (portIds.contains(portId)) {
							if (cell.getSchemePort().getAbstractSchemeLink() == null)
								non_connected_ports.add(cells[i]);
							else
								connected_ports.add(cells[i]);
						}
					}
				Color color = Color.white;
				if (type.getSort().equals(PortTypeSort.PORTTYPESORT_THERMAL))
					color = Color.black;

				GraphActions.setObjectsBackColor(this.graph, connected_ports
						.toArray(new PortCell[connected_ports.size()]), color);
				GraphActions.setObjectsBackColor(this.graph, non_connected_ports
						.toArray(new PortCell[non_connected_ports.size()]), Color.yellow);
			}
			if (see.isType(SchemeEvent.CABLE_PORT_TYPE_UPDATE)) {
				Collection portIds = (Collection)see.getObject();
				
				Object[] cells = this.graph.getAll();
				List connected_ports = new LinkedList();
				List non_connected_ports = new LinkedList();
				for (int i = 0; i < cells.length; i++)
					if (cells[i] instanceof CablePortCell) {
						CablePortCell cell = (CablePortCell)cells[i];
						Identifier portId = cell.getSchemeCablePortId();
						if (portIds.contains(portId)) {
							if (cell.getSchemeCablePort().getAbstractSchemeLink() == null)
								non_connected_ports.add(cells[i]);
							else
								connected_ports.add(cells[i]);
						}
					}
				GraphActions.setObjectsBackColor(this.graph, connected_ports
						.toArray(new CablePortCell[connected_ports.size()]), Color.white);
				GraphActions.setObjectsBackColor(this.graph, non_connected_ports
						.toArray(new CablePortCell[non_connected_ports.size()]),
						Color.yellow);
			}

			if (see.isType(SchemeEvent.OBJECT_TYPE_UPDATE)) {
				/*
				Object res = ae.getSource();

				Object[] cells = this.graph.getSelectionCells();
				ArrayList new_cells = new ArrayList(cells.length);
				for (int i = 0; i < cells.length; i++)
					if (!(cells[i] instanceof DefaultEdge)
							|| cells[i] instanceof DefaultLink
							|| cells[i] instanceof DefaultCableLink)
						new_cells.add(cells[i]);
				Object[] ncells = new_cells.toArray();

				if (ncells.length == 0)
					return;
				Object obj = ncells[0];

				if (obj instanceof DeviceGroup) {
					if (ncells.length != 1)
						return;
					EquipmentTypeImpl new_eqt;
					if (res instanceof SchemeProtoElement) {
						SchemeProtoElement proto = (SchemeProtoElement) res;
						new_eqt = proto.equipmentTypeImpl();
					} else
						new_eqt = (EquipmentType) res;

					SchemeProtoElement p = ((DeviceGroup) obj).getProtoElement();
					p.equipmentType() = new_eqt.getId();
				}
				if (obj instanceof PortCell) {
					int counter = 0;
					for (int i = 0; i < ncells.length; i++)
						if (ncells[i] instanceof PortCell)
							counter++;
					if (counter == ncells.length) {
						Color color = Color.white;
						if (res instanceof PortType)
							if (((PortType) res).pClass.equals("splice"))
								color = Color.black;

						ArrayList connected_ports = new ArrayList();
						ArrayList non_connected_ports = new ArrayList();
						for (int i = 0; i < ncells.length; i++)
							if (((PortCell) ncells[i]).getSchemePort().linkId.equals(""))
								non_connected_ports.add(ncells[i]);
							else
								connected_ports.add(ncells[i]);

						GraphActions.setObjectsBackColor(this.graph, connected_ports
								.toArray(new PortCell[connected_ports.size()]), color);
						GraphActions.setObjectsBackColor(this.graph, non_connected_ports
								.toArray(new PortCell[non_connected_ports.size()]),
								Color.yellow);
						for (int i = 0; i < ncells.length; i++)
							((PortCell) ncells[i]).getSchemePort().portTypeId = res.getId();
					}
				}
				if (obj instanceof CablePortCell) {
					int counter = 0;
					for (int i = 0; i < ncells.length; i++)
						if (ncells[i] instanceof CablePortCell)
							counter++;
					if (counter == ncells.length)
						GraphActions.setObjectsBackColor(this.graph, ncells, Color.white);
					for (int i = 0; i < ncells.length; i++)
						((CablePortCell) ncells[i]).getSchemeCablePort().cablePortTypeId = res
								.getId();
				}
				if (obj instanceof DefaultLink) {
					int counter = 0;
					for (int i = 0; i < ncells.length; i++)
						if (ncells[i] instanceof DefaultLink)
							counter++;
					if (counter == ncells.length) {
						for (int i = 0; i < ncells.length; i++)
							((DefaultLink) ncells[i]).getSchemeLink().linkTypeId = res
									.getId();
					}
				}
				if (obj instanceof DefaultCableLink) {
					int counter = 0;
					for (int i = 0; i < ncells.length; i++)
						if (ncells[i] instanceof DefaultCableLink)
							counter++;
					if (counter == ncells.length) {
						for (int i = 0; i < ncells.length; i++)
							((DefaultCableLink) ncells[i]).getSchemeCableLink().cableLinkTypeId = res
									.getId();
					}
				}*/
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
						.getSchemePortId().getIdentifierString());
				if (bpc.isCablePort()) {
					if (p_id != null)
						bpc.setSchemeCablePortId(p_id);
				} else {
					if (p_id != null)
						bpc.setSchemePortId(p_id);
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
