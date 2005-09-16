/**
 * $Id: CreateMarkerCommandAtomic.java,v 1.34 2005/09/16 14:53:32 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.map.controllers.MeasurementPathController;
import com.syrus.AMFICOM.client.map.controllers.NodeLinkController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * Команда создания метки на линии
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.34 $, $Date: 2005/09/16 14:53:32 $
 * @module mapviewclient
 */
public class CreateMarkerCommandAtomic extends MapActionCommand {
	/**
	 * созданный элемент метки
	 */
	Marker marker;

	/**
	 * Выбранный фрагмент линии
	 */
	MeasurementPath path;

	MapView mapView;

	/**
	 * дистанция от начала линии, на которой создается метка
	 */
	double distance;

	/**
	 * точка, в которой создается новый топологический узел
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
			Log.debugMessage(
				getClass().getName() + "::execute() | "  //$NON-NLS-1$
					+ "create marker at path " + this.path.getName()  //$NON-NLS-1$
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
				CableController cc = (CableController)mapViewController.getController(cablePath);
				boolean found = false;
				if(cc.isMouseOnElement(cablePath, this.point)) {
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

							this.marker = Marker.createInstance(
									LoginManager.getUserId(),
									this.mapView, 
									node,
									nodeLink.getOtherNode(node),
									nodeLink,
									this.path,
									mpc.getMonitoredElementId(this.path),
									dpoint);
							this.marker.setCablePath(cablePath);
							this.mapView.addMarker(this.marker);

							MarkerController mc = (MarkerController )
									mapViewController.getController(this.marker);
							mc.updateScaleCoefficient(this.marker);
							mc.notifyMarkerCreated(this.marker);

							this.logicalNetLayer.setCurrentMapElement(this.marker);
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
//						e.printStackTrace();
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
			e.printStackTrace();
		}
	}

}
