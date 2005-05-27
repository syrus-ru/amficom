/**
 * $Id: MapViewVisualManagerFactory.java,v 1.2 2005/05/27 15:14:58 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.UI.MapTreeModel;
import com.syrus.AMFICOM.Client.Map.UI.MapViewTreeModel;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.MapView;
/**
 * @version $Revision: 1.2 $, $Date: 2005/05/27 15:14:58 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
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

		throw new UnsupportedOperationException("Unknown object " + object);
	}

}
