package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.Marker;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Map.mapview.Selection;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;
import com.syrus.AMFICOM.Client.Map.mapview.VoidElement;

import java.util.HashMap;

public final class MapPopupMenuManager 
{
	private MapPopupMenuManager()
	{
	}

	private static java.util.Map popupMap = new HashMap();

	static
	{
		popupMap.put(com.syrus.AMFICOM.Client.Map.mapview.CablePath.class,
			CablePathPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			LinkPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.map.Mark.class,
			MarkPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.map.NodeLink.class,
			NodeLinkPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.map.TopologicalNode.class,
			NodePopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.Client.Map.mapview.Selection.class,
			SelectionPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.map.SiteNode.class,
			SitePopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.Client.Map.mapview.UnboundNode.class,
			UnboundPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.Client.Map.mapview.UnboundLink.class,
			UnboundLinkPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.Client.Map.mapview.VoidElement.class,
			VoidElementPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.Client.Map.mapview.Marker.class,
			MarkerPopupMenu.getInstance());
		popupMap.put(com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath.class,
			MeasurementPathPopupMenu.getInstance());
	}
	
	public static MapPopupMenu getPopupMenu(MapElement me)
	{
		MapPopupMenu menu = (MapPopupMenu )popupMap.get(me.getClass());
		return menu;
	}
	
}
