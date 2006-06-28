/*-
 * $$Id: PlaceSchemePathCommand.java,v 1.51 2006/02/14 10:20:06 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
 * –азместить элемент типа mpe на карте. используетс€ при переносе (drag/drop),
 * в точке point (в экранных координатах)
 * 
 * @version $Revision: 1.51 $, $Date: 2006/02/14 10:20:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
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
		Log.debugMessage("place scheme path " //$NON-NLS-1$
				+ this.schemePath.getName()
				+ " (" + this.schemePath.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
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
			// операци€ закончена - оповестить слушателей
			this.logicalNetLayer.setCurrentMapElement(this.measurementPath);
			super.setUndoable(false);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			Log.errorMessage(e);
		}
	}
}
