/**
 * $Id: MapPathElementStrategy.java,v 1.12 2005/02/07 16:09:26 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.Selection;

import java.awt.Point;

/**
 * Стратегия управления измерительным путем.
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/02/07 16:09:26 $
 * @module mapviewclient_v1
 */
public final class MapPathElementStrategy extends MapStrategy 
{
	/**
	 * Измерительный путь.
	 */
	MeasurementPath path;

	/**
	 * Instance.
	 */
	private static MapPathElementStrategy instance = new MapPathElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapPathElementStrategy()
	{//empty
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapPathElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMapElement(MapElement me)
	{
		this.path = (MeasurementPath)me;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMousePressed(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();

		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = super.logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
			{
				Selection sel = (Selection)mel;
				sel.add(this.path);
			}
			else
			{
				Selection sel = new Selection(super.logicalNetLayer.getMapView().getMap());
				sel.addAll(super.logicalNetLayer.getSelectedElements());
				super.logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			super.logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		this.path.setSelected(true);
	}
}

