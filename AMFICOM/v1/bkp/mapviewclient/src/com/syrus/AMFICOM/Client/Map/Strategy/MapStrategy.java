/**
 * $Id: MapStrategy.java,v 1.4 2005/02/01 16:16:13 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.event.MouseEvent;

/**
 * Класс стратегии изменения состояния карты или выполнения действий на карте
 * в зависимости от внетреннего состояния карты. Состояние карты определяется
 * режимом работы пользователя с картой, режимом отображения, состоянием
 * или изменением состояния мыши и клавиатуры.
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/02/01 16:16:13 $
 * @module mapviewclient_v1
 */
public abstract class MapStrategy
{
	/**
	 * логический слой.
	 */
	protected LogicalNetLayer logicalNetLayer;
	/**
	 * контекст приложения
	 */
	protected ApplicationContext aContext;

	/**
	 * Применить стратегию действий к элементу карты.
	 * @param mapElement элемент карты
	 */
	public abstract void doContextChanges(MouseEvent mapElement);
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
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}

}
