/**
 * $Id: MapStrategy.java,v 1.9 2005/03/02 12:35:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Strategy;

import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.map.MapElement;

/**
 * @version $Revision: 1.9 $, $Date: 2005/03/02 12:35:40 $
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
