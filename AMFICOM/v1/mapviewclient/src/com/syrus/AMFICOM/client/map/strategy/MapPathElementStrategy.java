/**
 * $Id: MapPathElementStrategy.java,v 1.2 2004/09/21 14:59:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления путем
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/21 14:59:20 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapPathElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	MapMeasurementPathElement path;

	private static MapPathElementStrategy instance = new MapPathElementStrategy();

	private MapPathElementStrategy()
	{
	}

	public static MapPathElementStrategy getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.path = (MapMeasurementPathElement)me;
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}

	public void doContextChanges(MouseEvent me)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "doContextChanges()");
		
		MapState mapState = logicalNetLayer.getMapState();
		Map map = path.getMap();

		int mouseMode = mapState.getMouseMode();
		int actionMode = mapState.getActionMode();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if ((actionMode != MapState.SELECT_ACTION_MODE) &&
				(actionMode != MapState.MOVE_ACTION_MODE))
			{
				logicalNetLayer.getMapView().deselectAll();
//				map.deselectAll();
			}
			path.setSelected(true);
		}
	}
}

