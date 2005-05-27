/**
 * $Id: CollectorVisualManager.java,v 1.3 2005/05/27 15:14:57 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.3 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class CollectorVisualManager implements VisualManager {

	private static CollectorVisualManager instance;

	private static CollectorEditor generalPanel;
	private static MapElementCharacteristicsEditor charPanel;

	public static CollectorVisualManager getInstance() {
		if (instance == null) 
			instance = new CollectorVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new CollectorEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new MapElementCharacteristicsEditor();
		return charPanel;
	}

	public StorableObjectWrapper getController() {
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
