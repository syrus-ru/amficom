/**
 * $Id: MapConnection.java,v 1.12 2005/08/11 12:43:29 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ 
 */
package com.syrus.AMFICOM.client.map;

import java.util.List;
import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/08/11 12:43:29 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public abstract class MapConnection {
	public abstract boolean connect() throws MapConnectionException;

	public abstract boolean release() throws MapConnectionException;

	public abstract void setPath(String path);

	public abstract void setView(String name);

	public abstract void setURL(String url);

	public abstract String getURL();

	public abstract String getPath();

	public abstract String getView();
	
	public abstract MapImageLoader createImageLoader() throws MapConnectionException;
	
	public abstract MapCoordinatesConverter createCoordinatesConverter();
	
	public abstract MapContext createMapContext();

	/**
	 * Получить список географических слоев.
	 * @return список слоев &lt;{@link SpatialLayer}&gt;
	 */
	public abstract List<SpatialLayer> getLayers() throws MapDataException;
	
	/**
	 * Получить список названий доступных видов.
	 * 
	 * @return Список видов &lt;{@link String}&gt;
	 */
	public abstract List<String> getAvailableViews() throws MapDataException;

	public static MapConnection create(final String connectionClass) throws MapConnectionException {
		Log.debugMessage("method call MapConnection.create()", Level.FINER);

		MapConnection connection = null;
		try {
			connection = (MapConnection) Class.forName(connectionClass).newInstance();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			throw new MapConnectionException("MapConnection.create() throws ClassNotFoundException");
		} catch (InstantiationException ie) {
			ie.printStackTrace();
			throw new MapConnectionException("MapConnection.create() throws InstantiationException");
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
			throw new MapConnectionException("MapConnection.create() throws IllegalAccessException");
		}

		return connection;
	}
}
