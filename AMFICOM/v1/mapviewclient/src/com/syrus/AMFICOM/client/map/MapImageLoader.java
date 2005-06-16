/**
 * $Id: MapImageLoader.java,v 1.4 2005/06/16 14:39:05 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.util.List;

import javax.swing.ImageIcon;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/16 14:39:05 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public interface MapImageLoader
{
	public abstract MapConnection getMapConnection()
		throws MapConnectionException;

	/**
	 * Посылает запрос на рендеринг изображения на сервере
	 */
	ImageIcon renderMapImage(TopologicalRequest request)
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
