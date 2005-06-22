/**
 * $Id: AbstractMapStrategy.java,v 1.5 2005/06/22 08:43:48 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.util.Log;

/**
 * Класс стратегии изменения состояния карты или выполнения действий на карте
 * в зависимости от внетреннего состояния карты. Состояние карты определяется
 * режимом работы пользователя с картой, режимом отображения, состоянием
 * или изменением состояния мыши и клавиатуры.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/06/22 08:43:48 $
 * @module mapviewclient_v1
 */
abstract class AbstractMapStrategy implements MapStrategy
{
	/**
	 * логический слой.
	 */
	protected LogicalNetLayer logicalNetLayer;

	protected NetMapViewer netMapViewer;
	/**
	 * контекст приложения.
	 */
	protected ApplicationContext aContext;

	/**
	 * @inheritDoc
	 */
	public abstract void setMapElement(MapElement mapElement);

	/**
	 * @inheritDoc
	 */
	public void setNetMapViewer(NetMapViewer netMapViewer)
	{
		this.netMapViewer = netMapViewer;
		this.logicalNetLayer = netMapViewer.getLogicalNetLayer();
		this.aContext = this.logicalNetLayer.getContext();
	}

	/**
	 * @inheritDoc
	 */
	public void doContextChanges(MouseEvent mapElement)
		throws MapConnectionException, MapDataException
	{
		Log.debugMessage(getClass().getName() + "::" + "doContextChanges(" + mapElement + ")" + " | " + "method call", Log.FINER);
		
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
		throws MapConnectionException, MapDataException 
	{//empty
	}

	/**
	 * Process left mouse dragged.
	 * @param mapState map state
	 * @param point new point
	 */
	protected void leftMouseDragged(MapState mapState, Point point)
		throws MapConnectionException, MapDataException 
	{//empty
	}

	/**
	 * Process left mouse released.
	 * @param mapState map state
	 * @param point new point
	 */
	protected void leftMouseReleased(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{//empty
	}
}
