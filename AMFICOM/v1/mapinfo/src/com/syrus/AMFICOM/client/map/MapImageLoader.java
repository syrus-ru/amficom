/**
 * $Id: MapImageLoader.java,v 1.1.2.2 2005/06/02 12:14:04 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map;

import javax.swing.ImageIcon;

public interface MapImageLoader
{
	/**
	 * Посылает запрос на сервер на остановку рендеринга.
	 */
	void stopRenderingAtServer();
	
	/**
	 * Посылает запрос на рендеринг изображения на сервере
	 */
	ImageIcon renderMapImageAtServer(TopologicalRequest request)
        throws MapConnectionException, MapDataException;
}