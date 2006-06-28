/*-
 * $$Id: CreateMarkerCommandAtomic.java,v 1.47 2006/06/23 13:56:59 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.map.controllers.MeasurementPathController;
import com.syrus.AMFICOM.client.map.controllers.NodeLinkController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

/**
 *  оманда создани€ метки на линии
 * 
 * @version $Revision: 1.47 $, $Date: 2006/06/23 13:56:59 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateMarkerCommandAtomic extends MapActionCommand {
	/**
	 * созданный элемент метки
	 */
	Marker marker;

	/**
	 * ¬ыбранный фрагмент линии
	 */
	MeasurementPath path;

	MapView mapView;

	/**
	 * дистанци€ от начала линии, на которой создаетс€ метка
	 */
	double distance;

	/**
	 * точка, в которой создаетс€ новый топологический узел
	 */
	Point point;

	public CreateMarkerCommandAtomic(MeasurementPath path, Point point) {
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.path = path;
		this.point = point;
	}

	@Override
	public void execute() {
		try {
			Log.debugMessage("create marker at path " + this.path.getName()  //$NON-NLS-1$
					+ " (" + this.path.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

			if ( !getLogicalNetLayer().getContext().getApplicationModel()
					.isEnabled(MapApplicationModel.ACTION_USE_MARKER))
				return;
			MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
			this.mapView = this.logicalNetLayer.getMapView();
			AbstractNode node = this.path.getStartNode();
			this.path.sortPathElements();
			List<CablePath> cablePaths = this.path.getSortedCablePaths();

			MapViewController mapViewController = getLogicalNetLayer().getMapViewController();

			for(CablePath cablePath : cablePaths) {
				boolean found = false;
				if(mapViewController.isMouseOnElement(cablePath, this.point)) {
					List<NodeLink> nodeLinks = new LinkedList<NodeLink>(cablePath.getSortedNodeLinks());
					if(nodeLinks.size() == 0) {
						break;
					}
					NodeLink firstNodeLink = nodeLinks.iterator().next();
					if(!(firstNodeLink.getStartNode().equals(node)
						|| firstNodeLink.getEndNode().equals(node))) {
						Collections.reverse(nodeLinks);
					}
					for(NodeLink nodeLink : nodeLinks) {
						NodeLinkController nlc = (NodeLinkController )mapViewController.getController(nodeLink);
						if(nlc.isMouseOnElement(nodeLink, this.point)) {
							DoublePoint dpoint = converter.convertScreenToMap(this.point);
							MeasurementPathController mpc = (MeasurementPathController )mapViewController.getController(this.path);

							final Identifier monitoredElementId = mpc.getMonitoredElementId(this.path);
							this.marker = Marker.createInstance(
									LoginManager.getUserId(),
									this.mapView, 
									node,
									nodeLink.getOtherNode(node),
									nodeLink,
									this.path,
									monitoredElementId,
									dpoint);
							this.marker.setCablePath(cablePath);
							this.mapView.addMarker(this.marker);

							MarkerController mc = (MarkerController )
									mapViewController.getController(this.marker);
							mc.updateScaleCoefficient(this.marker);
							mc.notifyMarkerCreated(this.marker);

							this.logicalNetLayer.setCurrentMapElement(this.marker);
							
							final SchemePath schemePath = this.path.getSchemePath();
							this.aContext.getDispatcher().firePropertyChange(
									new MarkerEvent(this, MarkerEvent.MARKER_CREATED_EVENT, this.marker.getId(), 
											schemePath.getOpticalDistance(marker.getPhysicalDistance()), 
											null, schemePath.getId(), monitoredElementId));
							
							setResult(Command.RESULT_OK);
							found = true;
							break;
						}
						nlc.updateLengthLt(nodeLink);

						node = nodeLink.getOtherNode(node);
					}
				}
				else {
					node = cablePath.getOtherNode(node);
				}
				if(found)
					break;
			}
			super.setUndoable(false);
//			List<NodeLink> nodeLinks = this.path.getSortedNodeLinks();
//			for(NodeLink nodeLink : nodeLinks) {
//
//				NodeLinkController nlc = (NodeLinkController )mapViewController.getController(nodeLink);
//
//				if(nlc.isMouseOnElement(nodeLink, this.point)) {
//					DoublePoint dpoint = converter.convertScreenToMap(this.point);
//
//					MeasurementPathController mpc = (MeasurementPathController )mapViewController.getController(this.path);
//
//					try {
//						this.marker = Marker.createInstance(
//								LoginManager.getUserId(),
//								this.mapView, 
//								node,
//								nodeLink.getOtherNode(node),
//								nodeLink,
//								this.path,
//								mpc.getMonitoredElementId(this.path),
//								dpoint);
//
//						this.mapView.addMarker(this.marker);
//
//						MarkerController mc = (MarkerController )
//								mapViewController.getController(this.marker);
//
//						mc.updateScaleCoefficient(this.marker);
//
//						mc.notifyMarkerCreated(this.marker);
//					} catch (ApplicationException e) {
//						Log.errorMessage(e);
//					}
//
//					break;
//				}
//				nlc.updateLengthLt(nodeLink);
//
//				node = nodeLink.getOtherNode(node);
//			}
		} catch(Exception e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.errorMessage(e);
		}
	}

}
