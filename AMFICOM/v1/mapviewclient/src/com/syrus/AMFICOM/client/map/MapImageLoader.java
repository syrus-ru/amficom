/**
 * $Id: MapImageLoader.java,v 1.13 2005/08/29 11:29:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.map.TopologicalImageQuery;

/**
 * @version $Revision: 1.13 $, $Date: 2005/08/29 11:29:40 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public interface MapImageLoader {
	MapConnection getMapConnection() throws MapConnectionException;

	/**
	 * Посылает запрос на рендеринг изображения на сервере
	 */
	Image renderMapImage(final TopologicalImageQuery query) throws MapConnectionException, MapDataException;

	/**
	 * Посылает запрос на сервер на остановку рендеринга.
	 */
	void stopRendering() throws MapConnectionException, MapDataException;

	/**
	 * Произвести поиск географических объектов по подстроке в указанном слое.
	 * @param searchText текст поиска
	 * @return список найденных объектов ({@link SpatialObject})
	 */
	List<SpatialObject> findSpatialObjects(final SpatialLayer layer, final String searchText) throws MapConnectionException, MapDataException;

	/**
	 * Произвести поиск географических объектов в указанной области.
	 * @return список найденных объектов ({@link SpatialObject})
	 */
	List<SpatialObject> findSpatialObjects(final SpatialLayer layer, final Rectangle2D.Double bounds) throws MapConnectionException, MapDataException;
}
