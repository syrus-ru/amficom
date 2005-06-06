/**
 * $Id: MapStrategy.java,v 1.10 2005/06/06 12:20:34 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.strategy;

import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.map.MapElement;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/06 12:20:34 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public interface MapStrategy {
	/**
	 * Установить элемент карты, к которому применяется стратегия действий.
	 * @param mapElement элемент кары
	 */
	public abstract void setMapElement(MapElement mapElement);

	/**
	 * Установить ссылку на логический слой, на котором выполняются
	 * действия стратегии.
	 * @param logicalNetLayer логический слой
	 */
	public abstract void setLogicalNetLayer(LogicalNetLayer logicalNetLayer);

	/**
	 * Применить стратегию действий к элементу карты.
	 * @param mapElement элемент карты
	 */
	public abstract void doContextChanges(MouseEvent mapElement)
			throws MapConnectionException, MapDataException;
}
