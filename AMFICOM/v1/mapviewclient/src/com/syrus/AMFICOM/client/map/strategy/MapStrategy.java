/**
 * $Id: MapStrategy.java,v 1.7 2005/02/07 16:09:27 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Класс стратегии изменения состояния карты или выполнения действий на карте
 * в зависимости от внетреннего состояния карты. Состояние карты определяется
 * режимом работы пользователя с картой, режимом отображения, состоянием
 * или изменением состояния мыши и клавиатуры.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/02/07 16:09:27 $
 * @module mapviewclient_v1
 */
public abstract class MapStrategy
{
	/**
	 * логический слой.
	 */
	protected LogicalNetLayer logicalNetLayer;
	/**
	 * контекст приложения.
	 */
	protected ApplicationContext aContext;

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

	/**
	 * Применить стратегию действий к элементу карты.
	 * @param mapElement элемент карты
	 */
	public void doContextChanges(MouseEvent mapElement)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "doContextChanges(" + mapElement + ")");
		
		MapState mapState = this.logicalNetLayer.getMapState();

		int mouseMode = mapState.getMouseMode();

		Point point = mapElement.getPoint();

		if(SwingUtilities.isLeftMouseButton(mapElement))
		{
			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				leftMousePressed(mapState, point);
			}//MapState.MOUSE_PRESSED
			else
			if(mouseMode == MapState.MOUSE_DRAGGED)
			{
				leftMouseDragged(mapState, point);
			}//MapState.MOUSE_DRAGGED
			else
			if(mouseMode == MapState.MOUSE_RELEASED)
			{
				leftMouseReleased(mapState, point);
			}//MapState.MOUSE_RELEASED
		}//SwingUtilities.isLeftMouseButton(me)
	}

	/**
	 * Process left mouse pressed.
	 * @param mapState map state
	 * @param point new point
	 */
	protected void leftMousePressed(MapState mapState, Point point)
	{//empty
	}

	/**
	 * Process left mouse dragged.
	 * @param mapState map state
	 * @param point new point
	 */
	protected void leftMouseDragged(MapState mapState, Point point)
	{//empty
	}

	/**
	 * Process left mouse released.
	 * @param mapState map state
	 * @param point new point
	 */
	protected void leftMouseReleased(MapState mapState, Point point)
	{//empty
	}
}
