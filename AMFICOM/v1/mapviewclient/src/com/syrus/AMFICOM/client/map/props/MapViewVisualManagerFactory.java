/*-
 * $$Id: MapViewVisualManagerFactory.java,v 1.7 2005/09/30 16:08:40 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.ui.MapTreeModel;
import com.syrus.AMFICOM.client.map.ui.MapViewTreeModel;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * @version $Revision: 1.7 $, $Date: 2005/09/30 16:08:40 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapViewVisualManagerFactory
		implements VisualManagerFactory {

	public VisualManager getVisualManager(Item item) {
		Object object = item.getObject();
		if (object instanceof String) {
			String s = (String)object;
			if(s.equals(MapViewTreeModel.MAP_BRANCH)
				|| s.equals(MapTreeModel.MAPS_BRANCH))
				return MapVisualManager.getInstance();
			else if(s.equals(MapViewController.ELEMENT_SITENODE))
				return SiteNodeVisualManager.getInstance();
			else if(s.equals(MapViewController.ELEMENT_PHYSICALLINK))
				return PhysicalLinkVisualManager.getInstance();
			else if(s.equals(MapViewController.ELEMENT_TOPOLOGICALNODE))
				return TopologicalNodeVisualManager.getInstance();
			else if(s.equals(MapViewController.ELEMENT_COLLECTOR))
				return CollectorVisualManager.getInstance();
			return null;
		}
		else if(object instanceof MapView)
			return MapViewVisualManager.getInstance();
		else if(object instanceof Map)
			return MapVisualManager.getInstance();
		else if(object instanceof SiteNode)
			return SiteNodeVisualManager.getInstance();
		else if(object instanceof SiteNodeType)
			return SiteNodeVisualManager.getInstance();
		else if(object instanceof PhysicalLink)
			return PhysicalLinkVisualManager.getInstance();
		else if(object instanceof TopologicalNode)
			return TopologicalNodeVisualManager.getInstance();
		else if(object instanceof Collector)
			return CollectorVisualManager.getInstance();

		throw new UnsupportedOperationException("Unknown object " + object); //$NON-NLS-1$
	}

}
