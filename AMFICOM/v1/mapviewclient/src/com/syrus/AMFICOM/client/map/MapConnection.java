/**
 * $Id: MapConnection.java,v 1.17 2005/08/29 12:27:24 krupenn Exp $
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
 * @version $Revision: 1.17 $, $Date: 2005/08/29 12:27:24 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public abstract class MapConnection {
	
	protected List<MapConnectionListener> listeners = new LinkedList<MapConnectionListener>();
	
	private static Map<String,Boolean> LAYER_SEARCHABILITIES = new HashMap<String,Boolean>();
	static{
		LAYER_SEARCHABILITIES.put("Msk_Bridges_a",true);
		LAYER_SEARCHABILITIES.put("Msk_Cemetry_a",true);		
		LAYER_SEARCHABILITIES.put("Msk_house_a",false);
		LAYER_SEARCHABILITIES.put("Msk_house_p",false);
		LAYER_SEARCHABILITIES.put("Msk_hydrography_la",true);
		LAYER_SEARCHABILITIES.put("Msk_metro_title_l",true);
		LAYER_SEARCHABILITIES.put("Msk_metro_title_p",true);
		LAYER_SEARCHABILITIES.put("Msk_municip_a",true);
		LAYER_SEARCHABILITIES.put("Msk_quarter_a",true);
		LAYER_SEARCHABILITIES.put("Msk_railway_l",true);
		LAYER_SEARCHABILITIES.put("Msk_railway_p",true);
		LAYER_SEARCHABILITIES.put("Msk_region_a",true);
		LAYER_SEARCHABILITIES.put("Msk_road_a",false);
		LAYER_SEARCHABILITIES.put("Msk_square_a",true);
		LAYER_SEARCHABILITIES.put("Msk_Street_l",true);
		LAYER_SEARCHABILITIES.put("Msk_Street_l_1",true);
		LAYER_SEARCHABILITIES.put("Msk_vegetation_a",true);
		LAYER_SEARCHABILITIES.put("Msk_Bridges_a",true);
		LAYER_SEARCHABILITIES.put("Msk_Bridges_a",true);
		LAYER_SEARCHABILITIES.put("Msk_Bridges_a",true);
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
