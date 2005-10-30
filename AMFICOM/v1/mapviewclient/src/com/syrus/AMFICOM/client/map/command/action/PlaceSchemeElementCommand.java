/*-
 * $$Id: PlaceSchemeElementCommand.java,v 1.42 2005/10/30 16:31:18 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * Разместить c[tvysq элемент на карте в соответствии с привязкой или по
 * координатам
 * 
 * @version $Revision: 1.42 $, $Date: 2005/10/30 16:31:18 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class PlaceSchemeElementCommand extends MapActionCommandBundle {
	/**
	 * Размещеный узел
	 */
	SiteNode site = null;

	/**
	 * созданный непривязанный элемент
	 */
	UnboundNode unbound = null;

	/**
	 * размещаемый схемный элемент
	 */
	SchemeElement schemeElement = null;

	Map map;

	/**
	 * экранная точка, в которой размещается элемент
	 */
	Point point = null;

	/**
	 * географическая точка, в которой размещается элемент
	 */
	DoublePoint coordinatePoint = null;

	public PlaceSchemeElementCommand(
			SchemeElement schemeElement,
			DoublePoint dpoint) {
		this.schemeElement = schemeElement;
		this.coordinatePoint = dpoint;
	}

	public PlaceSchemeElementCommand(SchemeElement se, Point point) {
		this.schemeElement = se;
		this.point = point;
	}

	@Override
	public void execute() {
		assert Log.debugMessage("place scheme element " //$NON-NLS-1$
				+ this.schemeElement.getName()
				+ " (" + this.schemeElement.getId() + ")", //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		if ( !this.aContext.getApplicationModel()
				.isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			return;
		
		try {
			long t1 = System.currentTimeMillis();
			// если географическая точка не задана, получить ее из экранной точки
			if(this.coordinatePoint == null) {
				this.coordinatePoint = this.logicalNetLayer.getConverter().convertScreenToMap(this.point);
			}
			if(this.point == null) {
				this.point = this.logicalNetLayer.getConverter().convertMapToScreen(this.coordinatePoint);
			}
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			long t2 = System.currentTimeMillis();
			this.site = mapView.findElement(this.schemeElement);
			long t3 = System.currentTimeMillis();
			long t4 = System.currentTimeMillis();
			long t5 = System.currentTimeMillis();
			if(this.site == null) {
				MapElement mapElement = this.logicalNetLayer.getMapElementAtPoint(this.point, this.netMapViewer.getVisibleBounds());
				t4 = System.currentTimeMillis();
				
				if(mapElement instanceof SiteNode
						&& !(mapElement instanceof UnboundNode)) {
					this.site = (SiteNode )mapElement;
					this.schemeElement.setSiteNode(this.site);
				}
				else {
					this.unbound = super.createUnboundNode(this.coordinatePoint, this.schemeElement);
					this.site = this.unbound;
				}
				t5 = System.currentTimeMillis();
				
			}
			// операция закончена - оповестить слушателей
			this.logicalNetLayer.setCurrentMapElement(this.site);
			super.setUndoable(false);
			long t6 = System.currentTimeMillis();
			assert Log.debugMessage("PlaceSchemeElementCommand :: calculate coordinates " + (t2 - t1) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			assert Log.debugMessage("PlaceSchemeElementCommand :: find scheme element " + (t3 - t2) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			assert Log.debugMessage("PlaceSchemeElementCommand :: get map element at point " + (t4 - t3) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			assert Log.debugMessage("PlaceSchemeElementCommand :: create unbound node " + (t5 - t4) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			assert Log.debugMessage("PlaceSchemeElementCommand :: notify " + (t6 - t5) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			assert Log.debugMessage(e, Level.SEVERE);
		}

	}
}
