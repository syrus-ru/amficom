/**
 * $Id: UnboundNodeVisualManager.java,v 1.3 2005/05/27 15:14:58 krupenn Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ???????
 */
package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

public class UnboundNodeVisualManager implements VisualManager {

	private static UnboundNodeVisualManager instance;

	private static UnboundNodeEditor generalPanel;
	private static MapElementCharacteristicsEditor charPanel;

	public static UnboundNodeVisualManager getInstance() {
		if (instance == null) 
			instance = new UnboundNodeVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new UnboundNodeEditor();
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
