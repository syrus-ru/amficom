/*-
 * $Id: MapViewPlaceManager.java,v 1.1 2006/06/29 08:18:32 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.awt.geom.Rectangle2D.Double;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.util.SynchronousWorker;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class MapViewPlaceManager {
	NetMapViewer netMapViewer;
	Executor executor = null;
	
	public MapViewPlaceManager(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}
	
	public void scanElements(final Set<SchemeElement> schemeElements) {
		final NetMapViewer netMapViewer1 = this.netMapViewer;
		final LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		final MapView mapView = logicalNetLayer.getMapView();
		
		final SynchronousWorker<Set<UnboundNode>> worker = new SynchronousWorker<Set<UnboundNode>>(this.executor,
				I18N.getString("Message.Information.please_wait"), 
				I18N.getString("Message.Information.create_sites"), true) {
			@Override
			public Set<UnboundNode> construct() throws Exception {
				try {
					final Double visibleBounds = netMapViewer1.getVisibleBounds();
					for (SchemeElement schemeElement : schemeElements) {
						SiteNode node = mapView.findElement(schemeElement);
						if(node == null) {
							Equipment equipment = schemeElement.getEquipment();
							if(equipment != null  && (equipment.getLongitude() != 0.0D || equipment.getLatitude() != 0.0D) ) {
								final DoublePoint point = new DoublePoint(equipment.getLongitude(), equipment.getLatitude());
								
								MapElement mapElement = logicalNetLayer.getMapElementAtPoint(
										logicalNetLayer.getConverter().convertMapToScreen(point), 
										visibleBounds);
								
								if(mapElement instanceof SiteNode && !(mapElement instanceof UnboundNode)) {
									SiteNode site = (SiteNode)mapElement;
									schemeElement.setSiteNode(site);
								} else {
									MapObjectsFactory.createUnboundNode(schemeElement, point);
								}
							}
						}
					}
					return MapObjectsFactory.getUnboundNodesCreated();
				} catch (MapConnectionException e) {
					throw new CreateObjectException(e);
				} catch (MapDataException e) {
					throw new CreateObjectException(e);
				}
			}
		};
		
		try {
			final Set<UnboundNode> unboundNodes = worker.execute();
			if (!unboundNodes.isEmpty()) {
				MapObjectsFactory.placeObjectsCreated(logicalNetLayer);
				MapObjectsFactory.clear();
			}
		} catch (ExecutionException e1) {
			Log.errorMessage(e1);
		}
	}
	
	public void scanCables(final Set<SchemeCableLink> schemeCableLinks) {
		final LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		final MapView mapView = logicalNetLayer.getMapView();
		
		final SynchronousWorker<Set<CablePath>> worker = new SynchronousWorker<Set<CablePath>>(this.executor, 
				I18N.getString("Message.Information.please_wait"), 
				I18N.getString("Message.Information.create_cables"), true) {
			@Override
			public Set<CablePath> construct() throws Exception {
				for (SchemeCableLink schemeCableLink : schemeCableLinks) {
					CablePath cp = mapView.findCablePath(schemeCableLink);
					if(cp == null) {
						SiteNode cableStartNode = mapView.getStartNode(schemeCableLink);
						SiteNode cableEndNode = mapView.getEndNode(schemeCableLink);
						if(cableStartNode != null && cableEndNode != null) {
							MapObjectsFactory.createCablePath(schemeCableLink, cableStartNode, cableEndNode);
						} else {
							Log.errorMessage("Can't place schemeCableLink '" + schemeCableLink.getName() 
									+ "' (" + schemeCableLink + "); cableStartNode is '" + cableStartNode 
									+ "'; cableEndNode is '" + cableEndNode + "'") ;
						}
					}
				}
				return MapObjectsFactory.getCablePathsCreated();
			}
		};
		
		try {
			final Set<CablePath> cabePaths = worker.execute();
			if (!cabePaths.isEmpty()) {
				MapObjectsFactory.placeObjectsCreated(logicalNetLayer);
				MapObjectsFactory.clear();
			}
		} catch (ExecutionException e1) {
			Log.errorMessage(e1);
		}
	}
	
	public void scanPaths(final Set<SchemePath> schemePaths) {
		final LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		final MapView mapView = logicalNetLayer.getMapView();

		final SynchronousWorker<Set<MeasurementPath>> worker = new SynchronousWorker<Set<MeasurementPath>>(this.executor, 
				I18N.getString("Message.Information.please_wait"), 
				I18N.getString("Message.Information.create_paths"), true) {
			@Override
			public Set<MeasurementPath> construct() throws Exception {
				for (SchemePath schemePath : schemePaths) {
					MeasurementPath mp = mapView.findMeasurementPath(schemePath);
					if(mp == null) {
						SiteNode pathStartNode = mapView.getStartNode(schemePath);
						SiteNode pathEndNode = mapView.getEndNode(schemePath);
						if(pathStartNode != null && pathEndNode != null) {
							MapObjectsFactory.createMeasurementPath(schemePath, pathStartNode, pathEndNode, mapView);
						} else {
							Log.errorMessage("Can't place schemePath '" + schemePath.getName() 
									+ "' (" + schemePath + "); pathStartNode is '" + pathStartNode 
									+ "'; pathEndNode is '" + pathEndNode + "'") ;
						}
					}
				}
				return MapObjectsFactory.getMeasurementPathsCreated();
			}
		};
		
		try {
			final Set<MeasurementPath> measurementPaths = worker.execute();
			if (!measurementPaths.isEmpty()) {
				MapObjectsFactory.placeObjectsCreated(logicalNetLayer);
				MapObjectsFactory.clear();
			}
		} catch (ExecutionException e1) {
			Log.errorMessage(e1);
		}
	}
}
