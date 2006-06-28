/*-
 * $$Id: MapPopupMenuManager.java,v 1.18 2006/02/22 10:54:45 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.util.HashMap;

import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.mapview.VoidElement;

/**
 * @version $Revision: 1.18 $, $Date: 2006/02/22 10:54:45 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapPopupMenuManager {
	private MapPopupMenuManager() {
		// empty
	}

	private static java.util.Map<Class, MapPopupMenu> popupMap = new HashMap<Class, MapPopupMenu>();
	private static java.util.Map<Class, MapPopupMenu> alwaysShownPopupMap = new HashMap<Class, MapPopupMenu>();

	static {
		popupMap.put(CablePath.class,
			CablePathPopupMenu.getInstance());
		popupMap.put(SiteNode.class,
				SitePopupMenu.getInstance());
		popupMap.put(PhysicalLink.class,
			LinkPopupMenu.getInstance());
		popupMap.put(TopologicalNode.class,
				NodePopupMenu.getInstance());
		popupMap.put(NodeLink.class,
				NodeLinkPopupMenu.getInstance());
		popupMap.put(UnboundNode.class,
				UnboundPopupMenu.getInstance());
		popupMap.put(UnboundLink.class,
				UnboundLinkPopupMenu.getInstance());
		popupMap.put(VoidElement.class,
				VoidElementPopupMenu.getInstance());
		popupMap.put(Selection.class,
				SelectionPopupMenu.getInstance());
		
		alwaysShownPopupMap.put(Mark.class,
			MarkPopupMenu.getInstance());
		alwaysShownPopupMap.put(Marker.class,
			MarkerPopupMenu.getInstance());
		alwaysShownPopupMap.put(MeasurementPath.class,
			MeasurementPathPopupMenu.getInstance());
	}
	
	public static MapPopupMenu getPopupMenu(MapElement me, boolean editable) {
//	 for noneditable search in one map
		MapPopupMenu menu = alwaysShownPopupMap.get(me.getClass());
		if (menu == null && editable) { // for editable search in two maps
			menu = popupMap.get(me.getClass());
		}
		return menu;
	}

}
