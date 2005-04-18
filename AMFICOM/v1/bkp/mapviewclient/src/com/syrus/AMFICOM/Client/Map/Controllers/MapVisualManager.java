/**
 * $Id: MapVisualManager.java,v 1.3 2005/04/18 16:15:15 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Controllers;

import java.util.HashMap;

import com.syrus.AMFICOM.Client.Map.Props.CablePathVisualManager;
import com.syrus.AMFICOM.Client.Map.Props.PhysicalLinkVisualManager;
import com.syrus.AMFICOM.Client.Map.Props.SiteNodeVisualManager;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.map.MapElement;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/18 16:15:15 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapVisualManager implements VisualManager {

	static MapVisualManager instance;
	
	private static java.util.Map vmMap = new HashMap();

	static
	{
		vmMap.put(com.syrus.AMFICOM.mapview.CablePath.class,
			CablePathVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			PhysicalLinkVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.map.SiteNode.class,
				SiteNodeVisualManager.getInstance());
	}

	public static VisualManager getVisualManager(MapElement mapElement) {
		return (VisualManager) vmMap.get(mapElement.getClass());
	}
	
	protected MapVisualManager() {
		// empty
	}
	
	public static MapVisualManager getInstance() {
		if(instance == null)
			instance = new MapVisualManager();
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client_.general.ui_.VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client_.general.ui_.VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client_.general.ui_.VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		// TODO Auto-generated method stub
		return null;
	}

}
