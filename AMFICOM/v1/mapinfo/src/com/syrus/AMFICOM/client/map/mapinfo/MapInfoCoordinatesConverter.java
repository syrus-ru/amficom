/*
 * $Id: MapInfoCoordinatesConverter.java,v 1.6 2005/08/12 15:04:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Point;

import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/08/12 15:04:32 $
 * @module mapinfo
 */
public class MapInfoCoordinatesConverter implements MapCoordinatesConverter {
	private final MapInfoConnection connection;

	public MapInfoCoordinatesConverter(final MapInfoConnection connection) {
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapCoordinatesConverter#convertMapToScreen(com.syrus.AMFICOM.map.DoublePoint)
	 */
	public Point convertMapToScreen(final DoublePoint point) throws MapConnectionException, MapDataException {
		try {
			final com.mapinfo.util.DoublePoint mapdp = new com.mapinfo.util.DoublePoint(point.getX(), point.getY());
			final com.mapinfo.util.DoublePoint screendp = this.connection.getLocalMapJ().transformNumericToScreen(mapdp);
			return new Point((int) Math.round(screendp.x), (int) Math.round(screendp.y));
		} catch (Exception exc) {
			throw new MapDataException("convertMapToScreen - failed for point: " + point);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapCoordinatesConverter#convertScreenToMap(java.awt.Point)
	 */
	public DoublePoint convertScreenToMap(final Point point) throws MapConnectionException, MapDataException {
		try {
			final com.mapinfo.util.DoublePoint screendp = new com.mapinfo.util.DoublePoint(point.x, point.y);
			final com.mapinfo.util.DoublePoint mapdp = this.connection.getLocalMapJ().transformScreenToNumeric(screendp);
			return new DoublePoint(mapdp.x, mapdp.y);
		} catch (Exception exc) {
			throw new MapDataException("convertScreenToMap - failed for point: " + point);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapCoordinatesConverter#pointAtDistance(com.syrus.AMFICOM.map.DoublePoint,
	 *      com.syrus.AMFICOM.map.DoublePoint, double)
	 */
	public DoublePoint pointAtDistance(final DoublePoint startPoint, final DoublePoint endPoint, final double dist)
			throws MapConnectionException,
				MapDataException {
		final double len = distance(startPoint, endPoint);

		final DoublePoint point = new DoublePoint(startPoint.getX() + (endPoint.getX() - startPoint.getX()) / len * dist, startPoint.getY()
				+ (endPoint.getY() - startPoint.getY())
				/ len
				* dist);

		return point;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapCoordinatesConverter#distance(com.syrus.AMFICOM.map.DoublePoint,
	 *      com.syrus.AMFICOM.map.DoublePoint)
	 */
	public double distance(final DoublePoint from, final DoublePoint to) throws MapConnectionException, MapDataException {
		try {
			return this.connection.getLocalMapJ().sphericalDistance(new com.mapinfo.util.DoublePoint(from.getX(), from.getY()),
					new com.mapinfo.util.DoublePoint(to.getX(), to.getY()));
		} catch (Exception e) {
			throw new MapDataException("Cannot calculate spherical distance", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapCoordinatesConverter#getMapConnection()
	 */
	public MapConnection getMapConnection() throws MapConnectionException {
		return this.connection;
	}
}
