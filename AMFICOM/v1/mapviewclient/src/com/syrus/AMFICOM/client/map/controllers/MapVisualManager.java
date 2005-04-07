/**
 * $Id: MapVisualManager.java,v 1.1 2005/04/07 14:14:29 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/07 14:14:29 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapVisualManager implements VisualManager {

	static MapVisualManager instance;
	
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
