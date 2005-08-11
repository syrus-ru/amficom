/**
 * $Id: MapImageLoader.java,v 1.8 2005/08/11 13:55:41 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Image;
import java.util.List;

import com.syrus.AMFICOM.map.TopologicalImageQuery;

/**
 * @version $Revision: 1.8 $, $Date: 2005/08/11 13:55:41 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public interface MapImageLoader {
	public abstract MapConnection getMapConnection() throws MapConnectionException;

	/**
	 * Посылает запрос на рендеринг изображения на сервере
	 */
	Image renderMapImage(final TopologicalImageQuery query) throws MapConnectionException, MapDataException;

	/**
	 * Посылает запрос на сервер на остановку рендеринга.
	 */
	void stopRendering() throws MapConnectionException, MapDataException;

	/**
	 * Произвести поиск географических объектов по подстроке.
	 * @param searchText текст поиска
	 * @return список найденных объектов ({@link SpatialObject})
	 */
	List<SpatialObject> findSpatialObjects(final String searchText) throws MapConnectionException, MapDataException;
	
}
