/**
 * $Id: MapConnection.java,v 1.13 2005/08/22 15:11:04 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ 
 */
package com.syrus.AMFICOM.client.map;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2005/08/22 15:11:04 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public abstract class MapConnection {
	
	protected List<MapConnectionListener> listeners = new LinkedList<MapConnectionListener>();

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

	public void addMapConnectionListener(MapConnectionListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeMapConnectionListener(MapConnectionListener listener) {
		this.listeners.remove(listener);
	}
	
	protected void fireMapConnectionChanged() throws MapConnectionException {
		synchronized(this.listeners) {
			for(MapConnectionListener listener : this.listeners) {
				listener.mapConnectionChanged();
			}
		}
	}
	
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
