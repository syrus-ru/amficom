/**
 * $Id: MapConnection.java,v 1.18 2005/09/08 18:22:41 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ 
 */
package com.syrus.AMFICOM.client.map;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.18 $, $Date: 2005/09/08 18:22:41 $
 * @author $Author: bass $
 * @module mapviewclient
 */
public abstract class MapConnection {
	
	protected List<MapConnectionListener> listeners = new LinkedList<MapConnectionListener>();
	
	private static Map<String,Boolean> LAYER_SEARCHABILITIES = new HashMap<String,Boolean>();
	static{
		LAYER_SEARCHABILITIES.put("Msk_Bridges_a", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_Cemetry_a", Boolean.valueOf(true));		
		LAYER_SEARCHABILITIES.put("Msk_house_a", Boolean.valueOf(false));
		LAYER_SEARCHABILITIES.put("Msk_house_p", Boolean.valueOf(false));
		LAYER_SEARCHABILITIES.put("Msk_hydrography_la", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_metro_title_l", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_metro_title_p", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_municip_a", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_quarter_a", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_railway_l", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_railway_p", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_region_a", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_road_a", Boolean.valueOf(false));
		LAYER_SEARCHABILITIES.put("Msk_square_a", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_Street_l", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_Street_l_1", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_vegetation_a", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_Bridges_a", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_Bridges_a", Boolean.valueOf(true));
		LAYER_SEARCHABILITIES.put("Msk_Bridges_a", Boolean.valueOf(true));
	}

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
	 * @return true, если доступен поиск по слою;
	 */
	@SuppressWarnings("unused")
	public boolean searchIsAvailableForLayer(SpatialLayer layer) throws MapDataException{
		boolean returnValue = true;
		
		Boolean tableValue = LAYER_SEARCHABILITIES.get(layer.getName());
		if (tableValue != null)
			returnValue = tableValue.booleanValue();
		
		return returnValue;
	}
	
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
		Log.debugMessage("method call MapConnection.create()", Level.FINE);

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
