package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
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
		strategyMap.put(MapMarkElement.class,
			MapMarkElementStrategy.getInstance());
		strategyMap.put(MapNodeLinkElement.class,
			MapNodeLinkElementStrategy.getInstance());
		strategyMap.put(MapMarker.class,
			MapMarkerStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement.class,
			MapPathElementStrategy.getInstance());
		strategyMap.put(MapPhysicalLinkElement.class,
			MapPhysicalLinkElementStrategy.getInstance());
		strategyMap.put(MapPhysicalNodeElement.class,
			MapPhysicalNodeElementStrategy.getInstance());
		strategyMap.put(MapSiteNodeElement.class,
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
