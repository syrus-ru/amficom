package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapAlarmMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapEventMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.VoidMapElement;

import java.util.HashMap;

public final class MapStrategyManager 
{
	private MapStrategyManager()
	{
	}

	private static java.util.Map strategyMap = new HashMap();

	static
	{
		strategyMap.put(MapAlarmMarker.class,
			MapAlarmMarkerStrategy.getInstance());
		strategyMap.put(MapCablePathElement.class,
			MapCablePathElementStrategy.getInstance());
		strategyMap.put(MapEventMarker.class,
			MapEventMarkerStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.Mark.class,
			MapMarkElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.NodeLink.class,
			MapNodeLinkElementStrategy.getInstance());
		strategyMap.put(MapMarker.class,
			MapMarkerStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement.class,
			MapPathElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			MapPhysicalLinkElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.TopologicalNode.class,
			MapPhysicalNodeElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.SiteNode.class,
			MapSiteNodeElementStrategy.getInstance());
		strategyMap.put(MapUnboundNodeElement.class,
			MapUnboundNodeElementStrategy.getInstance());
		strategyMap.put(VoidMapElement.class,
			MapVoidElementStrategy.getInstance());
		strategyMap.put(MapSelection.class,
			MapSelectionElementStrategy.getInstance());
	}
	
	public static MapStrategy getStrategy(MapElement me)
	{
		MapStrategy strategy = (MapStrategy )strategyMap.get(me.getClass());
		if(strategy != null)
			strategy.setMapElement(me);
		return strategy;
	}
}
