package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.VoidMapElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;

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
		popupMap.put(MapPhysicalLinkElement.class,
			LinkPopupMenu.getInstance());
		popupMap.put(MapMarkElement.class,
			MarkPopupMenu.getInstance());
		popupMap.put(MapNodeLinkElement.class,
			NodeLinkPopupMenu.getInstance());
		popupMap.put(MapPhysicalNodeElement.class,
			NodePopupMenu.getInstance());
		popupMap.put(MapSelection.class,
			SelectionPopupMenu.getInstance());
		popupMap.put(MapSiteNodeElement.class,
			SitePopupMenu.getInstance());
		popupMap.put(MapUnboundNodeElement.class,
			UnboundPopupMenu.getInstance());
		popupMap.put(VoidMapElement.class,
			VoidElementPopupMenu.getInstance());
//		popupMap.put(MapAlarmMarker.class,
//			MapAlarmMarkerPopupMenu.getInstance());
//		popupMap.put(MapEventMarker.class,
//			MapEventMarkerPopupMenu.getInstance());
//		popupMap.put(MapMarker.class,
//			MapMarkerPopupMenu.getInstance());
//		popupMap.put(MapPathElement.class,
//			MapPathPopupMenu.getInstance());
	}
	
	public static MapPopupMenu getPopupMenu(MapElement me)
	{
		MapPopupMenu menu = (MapPopupMenu )popupMap.get(me.getClass());
		return menu;
	}
	
}
