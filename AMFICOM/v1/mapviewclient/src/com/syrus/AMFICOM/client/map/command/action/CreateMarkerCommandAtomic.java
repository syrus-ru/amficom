/**
 * $Id: CreateMarkerCommandAtomic.java,v 1.31 2005/09/02 09:30:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.map.controllers.MeasurementPathController;
import com.syrus.AMFICOM.client.map.controllers.NodeLinkController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * Команда создания метки на линии
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.31 $, $Date: 2005/09/02 09:30:55 $
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
				getClass().getName() + "::execute() | " 
					+ "create marker at path " + this.path.getName() 
					+ " (" + this.path.getId() + ")", 
				Level.FINEST);

			if ( !getLogicalNetLayer().getContext().getApplicationModel()
					.isEnabled(MapApplicationModel.ACTION_USE_MARKER))
				return;
			MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
			this.mapView = this.logicalNetLayer.getMapView();
			AbstractNode node = this.path.getStartNode();
			this.path.sortPathElements();
			List nodeLinks = this.path.getSortedNodeLinks();
			for(Iterator it = nodeLinks.iterator(); it.hasNext();) {
				NodeLink nodeLink = (NodeLink)it.next();

				NodeLinkController nlc = (NodeLinkController )getLogicalNetLayer().getMapViewController().getController(nodeLink);
				MeasurementPathController mpc = (MeasurementPathController )getLogicalNetLayer().getMapViewController().getController(this.path);

				if(nlc.isMouseOnElement(nodeLink, this.point)) {
					DoublePoint dpoint = converter.convertScreenToMap(this.point);

					try {
						this.marker = Marker.createInstance(
								LoginManager.getUserId(),
								this.mapView, 
								node,
								nodeLink.getOtherNode(node),
								nodeLink,
								this.path,
								mpc.getMonitoredElementId(this.path),
								dpoint);

						this.mapView.addMarker(this.marker);

						MarkerController mc = (MarkerController )
								getLogicalNetLayer().getMapViewController().getController(this.marker);

						mc.updateScaleCoefficient(this.marker);

						mc.notifyMarkerCreated(this.marker);
					} catch (ApplicationException e) {
						e.printStackTrace();
					}

					break;
				}
				nlc.updateLengthLt(nodeLink);

				node = nodeLink.getOtherNode(node);
			}
			this.logicalNetLayer.setCurrentMapElement(this.marker);
			setResult(Command.RESULT_OK);
		} catch(Exception e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

}
