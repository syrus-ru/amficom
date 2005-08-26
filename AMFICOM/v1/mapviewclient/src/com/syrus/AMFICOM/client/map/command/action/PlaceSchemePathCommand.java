/**
 * $Id: PlaceSchemePathCommand.java,v 1.42 2005/08/26 15:39:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

/**
 * Разместить элемент типа mpe на карте. используется при переносе (drag/drop),
 * в точке point (в экранных координатах)
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.42 $, $Date: 2005/08/26 15:39:54 $
 * @module mapviewclient
 */
public class PlaceSchemePathCommand extends MapActionCommandBundle {
	SiteNode startNode = null;
	SiteNode endNode = null;

	MeasurementPath measurementPath = null;

	SchemePath schemePath = null;

	MapView mapView;

	public PlaceSchemePathCommand(SchemePath path) {
		super();
		this.schemePath = path;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | "
				+ "place scheme path "
				+ this.schemePath.getName()
				+ " (" + this.schemePath.getId() + ")", 
			Level.FINEST);

		this.mapView = this.logicalNetLayer.getMapView();
		try {
			this.startNode = this.mapView.getStartNode(this.schemePath);
			this.endNode = this.mapView.getEndNode(this.schemePath);
			if(this.startNode == null || this.endNode == null) {
				setResult(Command.RESULT_NO);
				return;
			}
			this.measurementPath = this.mapView.findMeasurementPath(this.schemePath);
			if(this.measurementPath == null)
				this.measurementPath = super.createMeasurementPath(
						this.schemePath, 
						this.startNode, 
						this.endNode);

			this.measurementPath.sortPathElements();
			// операция закончена - оповестить слушателей
			this.logicalNetLayer.setCurrentMapElement(this.measurementPath);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
