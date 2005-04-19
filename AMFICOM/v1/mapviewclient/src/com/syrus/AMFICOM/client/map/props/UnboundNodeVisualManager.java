/**
 * $Id: UnboundNodeVisualManager.java,v 1.1 2005/04/19 15:48:57 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

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

	public ObjectResourceController getController() {
		return null;
	}

}
