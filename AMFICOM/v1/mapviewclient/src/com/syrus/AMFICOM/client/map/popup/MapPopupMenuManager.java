package com.syrus.AMFICOM.client.map.popup;

import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.mapview.VoidElement;

import java.util.HashMap;

public final class MapPopupMenuManager {
	private MapPopupMenuManager() {
		// empty
	}

	private static java.util.Map popupMap = new HashMap();

	static {
		popupMap.put(CablePath.class,
			CablePathPopupMenu.getInstance());
		popupMap.put(PhysicalLink.class,
			LinkPopupMenu.getInstance());
		popupMap.put(Mark.class,
			MarkPopupMenu.getInstance());
		popupMap.put(NodeLink.class,
			NodeLinkPopupMenu.getInstance());
		popupMap.put(TopologicalNode.class,
			NodePopupMenu.getInstance());
		popupMap.put(Selection.class,
			SelectionPopupMenu.getInstance());
		popupMap.put(SiteNode.class,
			SitePopupMenu.getInstance());
		popupMap.put(UnboundNode.class,
			UnboundPopupMenu.getInstance());
		popupMap.put(UnboundLink.class,
			UnboundLinkPopupMenu.getInstance());
		popupMap.put(VoidElement.class,
			VoidElementPopupMenu.getInstance());
		popupMap.put(Marker.class,
			MarkerPopupMenu.getInstance());
		popupMap.put(MeasurementPath.class,
			MeasurementPathPopupMenu.getInstance());
	}
	
	public static MapPopupMenu getPopupMenu(MapElement me) {
		MapPopupMenu menu = (MapPopupMenu )popupMap.get(me.getClass());
		return menu;
	}

}
