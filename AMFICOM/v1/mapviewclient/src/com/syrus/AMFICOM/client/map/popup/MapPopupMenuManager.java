package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.VoidMapElement;

import java.util.HashMap;

public final class MapPopupMenuManager 
{
	private MapPopupMenuManager()
	{
	}

	private static java.util.Map popupMap = new HashMap();

	static
	{
		popupMap.put(MapCablePathElement.class,
			CablePathPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			LinkPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.map.Mark.class,
			MarkPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.map.NodeLink.class,
			NodeLinkPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.map.TopologicalNode.class,
			NodePopupMenu.getInstance());
		popupMap.put(MapSelection.class,
			SelectionPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.map.SiteNode.class,
			SitePopupMenu.getInstance());
		popupMap.put(MapUnboundNodeElement.class,
			UnboundPopupMenu.getInstance());
		popupMap.put(MapUnboundLinkElement.class,
			UnboundLinkPopupMenu.getInstance());
		popupMap.put(VoidMapElement.class,
			VoidElementPopupMenu.getInstance());
		popupMap.put(MapMarker.class,
			MarkerPopupMenu.getInstance());
		popupMap.put(MapMeasurementPathElement.class,
			MeasurementPathPopupMenu.getInstance());
	}
	
	public static MapPopupMenu getPopupMenu(MapElement me)
	{
		MapPopupMenu menu = (MapPopupMenu )popupMap.get(me.getClass());
		return menu;
	}
	
}
