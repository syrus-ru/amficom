/*-
 * $$Id: AbstractMapStrategy.java,v 1.14 2005/10/30 16:31:18 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.logging.Level;

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
 * @version $Revision: 1.14 $, $Date: 2005/10/30 16:31:18 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
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
	public void doContextChanges(MouseEvent mouseEvent)
		throws MapConnectionException, MapDataException
	{
		assert Log.debugMessage(mouseEvent + " | " + "method call", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$
		
		MapState mapState = this.logicalNetLayer.getMapState();

		int mouseMode = mapState.getMouseMode();

		Point point = mouseEvent.getPoint();

		if(SwingUtilities.isLeftMouseButton(mouseEvent))
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
	@SuppressWarnings("unused")
	protected void leftMousePressed(MapState mapState, Point point)
		throws MapConnectionException, MapDataException 
	{//empty
	}

	/**
	 * Process left mouse dragged.
	 * @param mapState map state
	 * @param point new point
	 */
	@SuppressWarnings("unused")
	protected void leftMouseDragged(MapState mapState, Point point)
		throws MapConnectionException, MapDataException 
	{//empty
	}

	/**
	 * Process left mouse released.
	 * @param mapState map state
	 * @param point new point
	 */
	@SuppressWarnings("unused")
	protected void leftMouseReleased(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{//empty
	}
}
