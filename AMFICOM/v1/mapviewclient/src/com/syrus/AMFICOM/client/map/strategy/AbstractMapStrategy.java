/*-
 * $$Id: AbstractMapStrategy.java,v 1.12 2005/09/30 16:08:41 krupenn Exp $$
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
 * ����� ��������� ��������� ��������� ����� ��� ���������� �������� �� �����
 * � ����������� �� ����������� ��������� �����. ��������� ����� ������������
 * ������� ������ ������������ � ������, ������� �����������, ����������
 * ��� ���������� ��������� ���� � ����������.
 * 
 * @version $Revision: 1.12 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
abstract class AbstractMapStrategy implements MapStrategy
{
	/**
	 * ���������� ����.
	 */
	protected LogicalNetLayer logicalNetLayer;

	protected NetMapViewer netMapViewer;
	/**
	 * �������� ����������.
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
		Log.debugMessage(getClass().getName() + "::" + "doContextChanges(" + mouseEvent + ")" + " | " + "method call", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		
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
