/**
 * $Id: MapViewVisualManagerFactory.java,v 1.1 2005/04/07 14:14:08 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.Map.Controllers.CollectorController;
import com.syrus.AMFICOM.Client.Map.Controllers.MapVisualManager;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewVisualManager;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.Client.Map.Controllers.PhysicalLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.Client.Map.Controllers.TopologicalNodeController;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.general.ui_.tree_.VisualManagerFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.MapView;
/**
 * @version $Revision: 1.1 $, $Date: 2005/04/07 14:14:08 $
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
				|| s.equals(MapViewTreeModel.MAPS_BRANCH))
				return MapVisualManager.getInstance();
			else if(s.equals(MapViewController.ELEMENT_SITENODE))
				return (VisualManager )NodeTypeController.getInstance();
			else if(s.equals(MapViewController.ELEMENT_PHYSICALLINK))
				return (VisualManager )PhysicalLinkController.getInstance();
			else if(s.equals(MapViewController.ELEMENT_TOPOLOGICALNODE))
				return (VisualManager )TopologicalNodeController.getInstance();
			else if(s.equals(MapViewController.ELEMENT_COLLECTOR))
				return (VisualManager )CollectorController.getInstance();
			return null;
		}
		else if(object instanceof MapView)
			return MapViewVisualManager.getInstance();
		else if(object instanceof Map)
			return MapVisualManager.getInstance();
		else if(object instanceof SiteNode)
			return (VisualManager )SiteNodeController.getInstance();
		else if(object instanceof SiteNodeType)
			return (VisualManager )NodeTypeController.getInstance();
		else if(object instanceof PhysicalLink)
			return (VisualManager )PhysicalLinkController.getInstance();
		else if(object instanceof TopologicalNode)
			return (VisualManager )TopologicalNodeController.getInstance();
		else if(object instanceof Collector)
			return (VisualManager )CollectorController.getInstance();

		throw new UnsupportedOperationException("Unknown object " + object);
	}

}
