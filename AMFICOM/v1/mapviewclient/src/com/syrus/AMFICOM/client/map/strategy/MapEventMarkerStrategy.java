/*-
 * $$Id: MapEventMarkerStrategy.java,v 1.22 2005/09/30 16:08:41 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.EventMarker;
import com.syrus.AMFICOM.mapview.Selection;

/**
 * Стратегия управления маркером события.
 * 
 * @version $Revision: 1.22 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapEventMarkerStrategy extends AbstractMapStrategy 
{
	/**
	 * Маркер события.
	 */
	EventMarker marker;

	private static MapEventMarkerStrategy instance = new MapEventMarkerStrategy();

	private MapEventMarkerStrategy()
	{//empty
	}

	public static MapEventMarkerStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMapElement(MapElement me)
	{
		this.marker = (EventMarker)me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMousePressed(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();
		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = super.logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
			{
				Selection sel = (Selection)mel;
				sel.add(this.marker);
			}
			else
			{
				Selection sel = new Selection();
				sel.addAll(super.logicalNetLayer.getSelectedElements());
				super.logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			super.logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		super.logicalNetLayer.getMapView().getMap().setSelected(this.marker, true);
//		this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
	}
}

