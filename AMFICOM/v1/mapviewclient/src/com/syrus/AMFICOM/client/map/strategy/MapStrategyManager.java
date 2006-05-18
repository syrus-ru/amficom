/*-
 * $$Id: MapStrategyManager.java,v 1.16 2005/09/30 16:08:41 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.AlarmMarker;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.EventMarker;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.mapview.VoidElement;

/**
 * Хранилище стратегий для элементов карты.
 * 
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @version $Revision: 1.16 $, $Date: 2005/09/30 16:08:41 $
 * @module mapviewclient
 */
public final class MapStrategyManager {
	/**
	 * Конструктор закрыт, так как в классе используются только статические
	 * методы.
	 */
	private MapStrategyManager() {
		// empty
	}

	/**
	 * Таблица стратегий.
	 */
	private static Map<Class, MapStrategy> strategyMap = new HashMap<Class, MapStrategy>();

	static {
		strategyMap.put(AlarmMarker.class, MapAlarmMarkerStrategy.getInstance());
		strategyMap.put(CablePath.class, MapCablePathElementStrategy.getInstance());
		strategyMap.put(EventMarker.class, MapEventMarkerStrategy.getInstance());
		strategyMap.put(Mark.class, MapMarkElementStrategy.getInstance());
		strategyMap.put(NodeLink.class, MapNodeLinkElementStrategy.getInstance());
		strategyMap.put(Marker.class, MapMarkerStrategy.getInstance());
		strategyMap.put(MeasurementPath.class, MapPathElementStrategy.getInstance());
		strategyMap.put(PhysicalLink.class, MapPhysicalLinkElementStrategy.getInstance());
		strategyMap.put(TopologicalNode.class, MapPhysicalNodeElementStrategy.getInstance());
		strategyMap.put(SiteNode.class, MapSiteNodeElementStrategy.getInstance());
		strategyMap.put(UnboundNode.class, MapUnboundNodeElementStrategy.getInstance());
		strategyMap.put(VoidElement.class, MapVoidElementStrategy.getInstance());
		strategyMap.put(Selection.class, MapSelectionElementStrategy.getInstance());
	}

	/**
	 * Получить стратегию действий с элементом карты.
	 * 
	 * @param mapElement элемент карты
	 * @return стратегия
	 */
	public static MapStrategy getStrategy(MapElement mapElement) {
		MapStrategy strategy = strategyMap.get(mapElement.getClass());
		if(strategy != null) {
			strategy.setMapElement(mapElement);
		}
		return strategy;
	}
}
