/**
 * $Id: MapAlarmMarkerStrategy.java,v 1.2 2004/10/01 16:36:55 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapAlarmMarker;

import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления маркером сигнала тревоги
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/01 16:36:55 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapAlarmMarkerStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	MapAlarmMarker marker;
	
	private static MapAlarmMarkerStrategy instance = new MapAlarmMarkerStrategy();

	private MapAlarmMarkerStrategy()
	{
	}

	public static MapAlarmMarkerStrategy getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.marker = (MapAlarmMarker )me;
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
		Map map = marker.getMap();

		int mouseMode = mapState.getMouseMode();
		int actionMode = mapState.getActionMode();

		Point point = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				if ((actionMode == MapState.SELECT_ACTION_MODE))
				{
					MapElement mel = logicalNetLayer.getCurrentMapElement();
					if(mel instanceof MapSelection)
					{
						MapSelection sel = (MapSelection )mel;
						sel.add(marker);
					}
					else
					{
						MapSelection sel = new MapSelection(logicalNetLayer);
						sel.addAll(logicalNetLayer.getSelectedElements());
						logicalNetLayer.setCurrentMapElement(sel);
					}
				}
				if ((actionMode != MapState.SELECT_ACTION_MODE) &&
					(actionMode != MapState.MOVE_ACTION_MODE) )
				{
					logicalNetLayer.deselectAll();
				}
				marker.setSelected(true);
			}
		}//SwingUtilities.isLeftMouseButton(me)
	}
}

