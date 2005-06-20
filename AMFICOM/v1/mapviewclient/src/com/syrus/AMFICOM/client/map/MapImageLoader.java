/**
 * $Id: MapImageLoader.java,v 1.5 2005/06/20 15:18:10 peskovsky Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/06/20 15:18:10 $
 * @author $Author: peskovsky $
 * @module mapviewclient
 */
public interface MapImageLoader
{
	public abstract MapConnection getMapConnection()
		throws MapConnectionException;

	/**
	 * Посылает запрос на рендеринг изображения на сервере
	 */
	Image renderMapImage(TopologicalImageQuery query)
        throws MapConnectionException, MapDataException;

	/**
	 * Посылает запрос на сервер на остановку рендеринга.
	 */
	void stopRendering()
		throws MapConnectionException, MapDataException;

	/**
	 * Произвести поиск географических объектов по подстроке.
	 * @param searchText текст поиска
	 * @return список найденных объектов ({@link SpatialObject})
	 */
	List findSpatialObjects(String searchText)
		throws MapConnectionException, MapDataException;
	
}
