/*-
 * $$Id: MapDropTargetListener.java,v 1.47 2005/10/31 15:29:31 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

/**
 * Обработчик событий drag/drop в окне карты
 * 
 * @version $Revision: 1.47 $, $Date: 2005/10/31 15:29:31 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapDropTargetListener implements DropTargetListener {
	NetMapViewer netMapViewer;

	public MapDropTargetListener(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	// Здесь мы получаем объект который пользователь переносит с панели
	public void drop(DropTargetDropEvent dtde) {
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		Object or = null;

		Point point = dtde.getLocation();

		System.out.println("drop at point " + point.getX() + ", " + point.getY());

		if(logicalNetLayer.getMapView() != null) {
			DataFlavor[] df = dtde.getCurrentDataFlavors();
			Transferable transferable = dtde.getTransferable();
			for(int i = 0; i < df.length; i++) {
				try {
					System.out.println("drop name - " + df[i].getHumanPresentableName());
					System.out.println("transferable is " + transferable);
					System.out.println("dropped element is " + transferable.getTransferData(df[(i)]));
					if(df[i].getHumanPresentableName().equals("ElementLabel")) //$NON-NLS-1$
					{
						Identifier id = (Identifier) transferable
								.getTransferData(df[(i)]);
						SiteNodeType mpe = StorableObjectPool
								.getStorableObject(id, false);

						System.out.println("dropped site node type " + mpe.getName());
						mapElementDropped(mpe, point);
					} else if(df[i].getHumanPresentableName().equals(
							LogicalTreeUI.TRANSFERABLE_OBJECTS)) //$NON-NLS-1$
					{
						ArrayList items = (ArrayList) transferable
								.getTransferData(df[i]);
						for(Iterator iter = items.iterator(); iter.hasNext();) {
							or = iter.next();

							if(or instanceof Identifiable) {
								Identifiable identifiable = (Identifiable) or;
								StorableObject storableObject = 
									StorableObjectPool.getStorableObject(identifiable.getId(), true);
							
								if(storableObject instanceof SiteNodeType) {
									SiteNodeType snt = (SiteNodeType) storableObject;
									System.out.println("dropped site node type " + snt.getName());
									mapElementDropped(snt, point);
								}
								if(storableObject instanceof SchemeElement) {
									SchemeElement se = (SchemeElement) storableObject;
									System.out.println("dropped scheme element " + se.getName());
									schemeElementDropped(se, point);
								} else if(storableObject instanceof SchemeCableLink) {
									SchemeCableLink scl = (SchemeCableLink) storableObject;
									System.out.println("dropped scheme cable link " + scl.getName());
									schemeCableLinkDropped(scl);
								} else if(storableObject instanceof SchemePath) {
									SchemePath sp = (SchemePath) storableObject;
									System.out.println("dropped scheme path " + sp.getName());
									schemePathDropped(sp);
								} else if(storableObject instanceof Scheme) {
									Scheme sc = (Scheme) storableObject;
									SchemeElement se = sc
											.getParentSchemeElement();
									schemeElementDropped(se, point);
								}
							}
						}
					} else {
						dtde.rejectDrop();
						return;
					}

					dtde.acceptDrop(DnDConstants.ACTION_MOVE);
					dtde.getDropTargetContext().dropComplete(true);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void schemePathDropped(SchemePath schemePath) {
		LogicalNetLayer logicalNetLayer = this.netMapViewer
			.getLogicalNetLayer();
		MapView mapView = logicalNetLayer.getMapView();
		Map map = mapView.getMap();
		MeasurementPath measurementPath = mapView.findMeasurementPath(schemePath);
		if(measurementPath != null) {
			map.setSelected(measurementPath, true);
			logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this,
					MapEvent.SELECTION_CHANGED,
					map.getSelectedElements()));
		}
	}

	protected void mapElementDropped(SiteNodeType nodeType, Point point) {
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(), 
					I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION), 
					I18N.getString(MapEditorResourceKeys.ERROR), 
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		CreateSiteCommandAtomic cmd = new CreateSiteCommandAtomic(
				nodeType,
				point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		logicalNetLayer.getCommandList().add(cmd);
		logicalNetLayer.getCommandList().execute();
		logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
	}

	protected void schemeElementDropped(SchemeElement schemeElement, Point point) {
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_BINDING)) {
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(), 
					I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION), 
					I18N.getString(MapEditorResourceKeys.ERROR), 
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		MapView mapView = logicalNetLayer.getMapView();
		Map map = mapView.getMap();
		SiteNode site = mapView.findElement(schemeElement);
		if(site != null) {
			if(site instanceof UnboundNode) {
				try {
					logicalNetLayer.deselectAll();
					map.setSelected(site, true);
					Point pt = logicalNetLayer.getConverter()
							.convertMapToScreen(site.getLocation());
					MoveSelectionCommandBundle cmd = new MoveSelectionCommandBundle(
							pt);
					cmd.setNetMapViewer(this.netMapViewer);
					cmd.setParameter(
							MoveSelectionCommandBundle.END_POINT,
							point);
					logicalNetLayer.getCommandList().add(cmd);
					logicalNetLayer.getCommandList().execute();
					logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
				} catch(MapConnectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch(MapDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				map.setSelected(site, true);
				logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this,
						MapEvent.SELECTION_CHANGED,
						map.getSelectedElements()));
			}
		} else {
			PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(
					schemeElement,
					point);
			cmd.setNetMapViewer(this.netMapViewer);
			logicalNetLayer.getCommandList().add(cmd);
			logicalNetLayer.getCommandList().execute();
			logicalNetLayer.getCommandList().flush();
			logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);

			logicalNetLayer.getMapViewController().scanCables(
					schemeElement.getParentScheme());
		}
	}

	protected void schemeCableLinkDropped(SchemeCableLink schemeCableLink) {
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_BINDING)) {
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(), 
					I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION), 
					I18N.getString(MapEditorResourceKeys.ERROR), 
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		LogicalNetLayer logicalNetLayer = this.netMapViewer
				.getLogicalNetLayer();
		MapView mapView = logicalNetLayer.getMapView();
		Map map = mapView.getMap();
		CablePath cablePath = mapView.findCablePath(schemeCableLink);
		if(cablePath != null) {
			map.setSelected(cablePath, true);
			logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this,
					MapEvent.SELECTION_CHANGED,
					map.getSelectedElements()));
		} else {
			SiteNode startNode = logicalNetLayer.getMapView().getStartNode(
					schemeCableLink);
			SiteNode endNode = logicalNetLayer.getMapView().getEndNode(
					schemeCableLink);

			if(startNode == null || endNode == null) {
				JOptionPane
						.showMessageDialog(
								Environment.getActiveWindow(),
								I18N
										.getString(MapEditorResourceKeys.ERROR_CABLE_END_ELEMENTS_NOT_PLACED),
								I18N
										.getString(MapEditorResourceKeys.MESSAGE_UNABLE_TO_PLACE_CABLE),
								JOptionPane.ERROR_MESSAGE);
				return;
			}

			PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(
					schemeCableLink);
			cmd.setNetMapViewer(this.netMapViewer);
			logicalNetLayer.getCommandList().add(cmd);
			logicalNetLayer.getCommandList().execute();
			logicalNetLayer.getCommandList().execute();
			logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);

			logicalNetLayer.getMapViewController().scanPaths(
					schemeCableLink.getParentScheme());
		}
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		// empty
	}

	public void dragExit(DropTargetEvent dte) {
		// empty
	}

	public void dragOver(DropTargetDragEvent dtde) {
		// empty
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		// empty
	}

}
