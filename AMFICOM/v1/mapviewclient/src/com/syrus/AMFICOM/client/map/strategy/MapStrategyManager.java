/**
 * $Id: MapStrategyManager.java,v 1.9 2005/02/01 16:16:13 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.map.MapElement;

import java.util.HashMap;

/**
 * Хранилище стратегий для элементов карты.
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/02/01 16:16:13 $
 * @module mapviewclient_v1
 */
public final class MapStrategyManager 
{
	/**
	 * Конструктор закрыт, так как в классе используются только статические
	 * методы.
	 */
	private MapStrategyManager()
	{
	}

	/**
	 * Таблица стратегий.
	 */
	private static java.util.Map strategyMap = new HashMap();

	static
	{
		strategyMap.put(com.syrus.AMFICOM.mapview.AlarmMarker.class,
			MapAlarmMarkerStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.CablePath.class,
			MapCablePathElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.EventMarker.class,
			MapEventMarkerStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.Mark.class,
			MapMarkElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.NodeLink.class,
			MapNodeLinkElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.Marker.class,
			MapMarkerStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.MeasurementPath.class,
			MapPathElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			MapPhysicalLinkElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.TopologicalNode.class,
			MapPhysicalNodeElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.SiteNode.class,
			MapSiteNodeElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.UnboundNode.class,
			MapUnboundNodeElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.VoidElement.class,
			MapVoidElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.Selection.class,
			MapSelectionElementStrategy.getInstance());
	}
	
	/**
	 * Получить стратегию действий с элементом карты.
	 * @param mapElement элемент карты
	 * @return стратегия
	 */
	public static MapStrategy getStrategy(MapElement mapElement)
	{
		MapStrategy strategy = (MapStrategy )strategyMap.get(mapElement.getClass());
		if(strategy != null)
			strategy.setMapElement(mapElement);
		return strategy;
	}
}
