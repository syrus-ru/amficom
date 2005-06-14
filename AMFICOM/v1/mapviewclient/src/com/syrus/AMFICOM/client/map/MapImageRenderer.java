/**
 * $Id: MapImageRenderer.java,v 1.4 2005/06/14 14:23:35 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.map.DoublePoint;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/14 14:23:35 $
 * @author $Author: peskovsky $
 * @module mapviewclient
 */
public interface MapImageRenderer {
	void sizeChanged() throws MapConnectionException, MapDataException;
	void setCenter(DoublePoint newCenter) throws MapConnectionException, MapDataException;
	void setScale(double newScale) throws MapConnectionException, MapDataException;
	void analyzeMouseLocation(MouseEvent event) throws MapDataException, MapConnectionException;
	void refreshLayers() throws MapConnectionException, MapDataException;
	Image getImage() throws MapConnectionException, MapDataException;
	Dimension getImageSize();
	void cancel();
	/**
	 * Используется при задании нового центра для карты.
	 * Выдаёт ближайший к указанной точке центр для дискретного режима перемещения 
	 * @param point 
	 * @return Ближайший центр
	 */
	public DoublePoint getNearestCenter(DoublePoint point);
}
