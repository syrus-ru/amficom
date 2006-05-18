/*-
 * $$Id: MapImageLoader.java,v 1.15 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.syrus.AMFICOM.map.TopologicalImageQuery;

/**
 * @version $Revision: 1.15 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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
