/*
 * $Id: MapInfoContext.java,v 1.7 2006/06/22 11:47:14 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import com.mapinfo.util.DoubleRect;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapContext;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2006/06/22 11:47:14 $
 * @module mapinfo
 */
public class MapInfoContext implements MapContext {
	private final MapInfoConnection connection;

	public MapInfoContext(final MapInfoConnection connection) {
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapContext#setCenter(com.syrus.AMFICOM.map.DoublePoint)
	 */
	public void setCenter(final DoublePoint center) throws MapConnectionException {
		// DoublePoint nearestDescreteCenter = center;
		// if ( MapPropertiesManager.isDescreteNavigation()
		// &&MapPropertiesManager.isTopologicalImageCache()
		// &&(this.imageCache != null))
		// nearestDescreteCenter = this.imageCache.getNearestCenter(center);
		try {
			this.connection.getLocalMapJ().setCenter(new com.mapinfo.util.DoublePoint(center.getX(), center.getY()));
		} catch (Exception exc) {
			Log.errorMessage("MILNL - Failed setting center.");
			throw new MapConnectionException("Cannot set center", exc);
		}
		// if ( MapPropertiesManager.isTopologicalImageCache()
		// &&(this.imageCache != null))
		// this.imageCache.setCenter(center);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapContext#getCenter()
	 */
	public DoublePoint getCenter() throws MapConnectionException {
		com.mapinfo.util.DoublePoint center = null;
		try {
			center = this.connection.getLocalMapJ().getCenter();
			return new DoublePoint(center.x, center.y);
		} catch (Exception exc) {
			Log.errorMessage("MapInfoContext - Failed getting center.");
			throw new MapConnectionException("MapInfoContext - Failed getting center.", exc);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapContext#getScale()
	 */
	public double getScale() throws MapConnectionException {
		double currentZoom = 0.0D;
		try {
			currentZoom = this.connection.getLocalMapJ().getZoom();
		} catch (Exception exc) {
			Log.errorMessage("MapInfoContext - Failed setting scale.");
			throw new MapConnectionException("MapInfoContext - Failed setting scale.", exc);
		}

		return currentZoom;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapContext#setScale(double)
	 */
	public void setScale(final double scale) throws MapConnectionException {
		try {
			this.connection.getLocalMapJ().setZoom(scale);
		} catch (Exception exc) {
			Log.errorMessage("MapInfoContext - Failed setting scale.");
			throw new MapConnectionException("MapInfoContext - Failed setting scale.", exc);
		}
// if(this.aContext != null) {
//			Dispatcher disp = this.aContext.getDispatcher();
//			if(disp != null) {
//				Double p = new Double(getScale());
//				disp.firePropertyChange(new MapEvent(p, MapEvent.MAP_VIEW_SCALE_CHANGED));
//			}
//		}

//		if (	MapPropertiesManager.isTopologicalImageCache()
//				&&(this.imageCache != null))
//			this.imageCache.setScale(z);
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapContext#scaleTo(double)
	 */
	public void scaleTo(final double scaleCoef) throws MapConnectionException {
		this.setScale(this.getScale() * scaleCoef);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapContext#zoomIn()
	 */
	public void zoomIn() throws MapConnectionException {
		this.scaleTo(1.0D / ZOOM_FACTOR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapContext#zoomOut()
	 */
	public void zoomOut() throws MapConnectionException {
		this.scaleTo(ZOOM_FACTOR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapContext#zoomToBox(com.syrus.AMFICOM.map.DoublePoint,
	 *      com.syrus.AMFICOM.map.DoublePoint)
	 */
	public void zoomToBox(final DoublePoint from, final DoublePoint to) throws MapConnectionException {
		try {
			this.connection.getLocalMapJ().setBounds(new DoubleRect(from.getX(), from.getY(), to.getX(), to.getY()));
			// updateZoom();
		} catch (Exception e) {
			Log.errorMessage("MapInfoContext - Failed zooming to box.");
			throw new MapConnectionException("MapInfoContext - Failed zooming to box.", e);

		}
		// if(this.aContext != null) {
		// Dispatcher disp = this.aContext.getDispatcher();
		// if(disp != null) {
		// Double p = new Double(getScale());
		// disp.firePropertyChange(new MapEvent(p,
		// MapEvent.MAP_VIEW_SCALE_CHANGED));
		// }
		// }
		// this.imageCache.setScale(this.getScale());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapContext#getMapConnection()
	 */
	public MapConnection getMapConnection() {
		return this.connection;
	}

}
