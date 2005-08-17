/**
 * $Id: MapAlarmMarkerStrategy.java,v 1.21 2005/08/17 14:14:20 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.AlarmMarker;
import com.syrus.AMFICOM.mapview.Selection;

/**
 * Стратегия управления маркером сигнала тревоги.
 * @author $Author: arseniy $
 * @version $Revision: 1.21 $, $Date: 2005/08/17 14:14:20 $
 * @module mapviewclient
 */
public final class MapAlarmMarkerStrategy extends AbstractMapStrategy 
{
	/**
	 * Маркер сигнала тревоги.
	 */
	AlarmMarker marker;
	
	/**
	 * instance
	 */
	private static MapAlarmMarkerStrategy instance = new MapAlarmMarkerStrategy();

	/**
	 * Private constructor.
	 */
	private MapAlarmMarkerStrategy()
	{//empty
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapAlarmMarkerStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMapElement(MapElement me)
	{
		this.marker = (AlarmMarker)me;
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

