/**
 * $Id: MapImageLoader.java,v 1.1.2.1 2005/05/05 10:22:15 krupenn Exp $
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
	void renderMapImageAtServer(TopologicalRequest request)  throws MapConnectionException;

	/**
	 * Подгружает изображение с сервера по HTTP-запросу
	 * @return Изображение
	 */
	ImageIcon getServerMapImage();
}