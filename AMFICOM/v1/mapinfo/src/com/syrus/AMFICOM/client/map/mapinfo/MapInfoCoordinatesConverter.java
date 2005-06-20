/*
 * $Id: MapInfoCoordinatesConverter.java,v 1.1.2.1 2005/06/20 15:31:23 peskovsky Exp $
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
import com.syrus.AMFICOM.map.DoublePoint;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.1.2.1 $, $Date: 2005/06/20 15:31:23 $
 * @module mapinfo_v1
 */
public class MapInfoCoordinatesConverter implements MapCoordinatesConverter
{
	private final MapInfoConnection connection;
	
	public MapInfoCoordinatesConverter(MapInfoConnection connection)
	{
		this.connection = connection;
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapCoordinatesConverter#convertMapToScreen(com.syrus.AMFICOM.map.DoublePoint)
	 */
	public Point convertMapToScreen(DoublePoint point)
			throws MapConnectionException, MapDataException
	{
		try
		{
			com.mapinfo.util.DoublePoint mapdp = new com.mapinfo.util.DoublePoint(
					point.getX(),
					point.getY());
			com.mapinfo.util.DoublePoint screendp =
				this.connection.getLocalMapJ().transformNumericToScreen(mapdp);
			return new Point((int)Math.round(screendp.x), (int)Math.round(screendp.y));
		}
		catch(Exception exc)
		{
			throw new MapDataException("convertMapToScreen - failed for point: " + point);
		}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapCoordinatesConverter#convertScreenToMap(java.awt.Point)
	 */
	public DoublePoint convertScreenToMap(Point point)
			throws MapConnectionException, MapDataException
	{
		try
		{
			com.mapinfo.util.DoublePoint screendp = new com.mapinfo.util.DoublePoint(
					point.x,
					point.y);
			com.mapinfo.util.DoublePoint mapdp = 
				this.connection.getLocalMapJ().transformScreenToNumeric(screendp);
			return new DoublePoint(mapdp.x, mapdp.y);
		}
		catch(Exception exc)
		{
			throw new MapDataException("convertScreenToMap - failed for point: " + point);
		}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapCoordinatesConverter#pointAtDistance(com.syrus.AMFICOM.map.DoublePoint, com.syrus.AMFICOM.map.DoublePoint, double)
	 */
	public DoublePoint pointAtDistance(
			DoublePoint startPoint,
			DoublePoint endPoint,
			double dist) throws MapConnectionException, MapDataException
	{
		double len = distance(startPoint, endPoint);

		DoublePoint point = new DoublePoint(
				startPoint.getX() + (endPoint.getX() - startPoint.getX()) / len
						* dist,
				startPoint.getY() + (endPoint.getY() - startPoint.getY()) / len
						* dist);

		return point;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapCoordinatesConverter#distance(com.syrus.AMFICOM.map.DoublePoint, com.syrus.AMFICOM.map.DoublePoint)
	 */
	public double distance(DoublePoint from, DoublePoint to)
			throws MapConnectionException, MapDataException
	{
		try
		{
			return this.connection.getLocalMapJ().sphericalDistance(
					new com.mapinfo.util.DoublePoint(from.getX(), from.getY()),
					new com.mapinfo.util.DoublePoint(to.getX(), to.getY()));
		}
		catch(Exception e)
		{
			throw new MapDataException("Cannot calculate spherical distance", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapCoordinatesConverter#getMapConnection()
	 */
	public MapConnection getMapConnection() throws MapConnectionException
	{
		return this.connection;
	}
}
