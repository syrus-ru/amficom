package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.AlarmMarker;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.EventMarker;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.mapview.VoidElement;

import java.util.HashMap;
import com.syrus.AMFICOM.mapview.MeasurementPath;

public final class MapStrategyManager 
{
	private MapStrategyManager()
	{
	}

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
	
	public static MapStrategy getStrategy(MapElement me)
	{
		MapStrategy strategy = (MapStrategy )strategyMap.get(me.getClass());
		if(strategy != null)
			strategy.setMapElement(me);
		return strategy;
	}
}
