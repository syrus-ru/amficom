/**
 * $Id: TopologicalNodeVisualManager.java,v 1.4 2005/06/06 12:20:34 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

public class TopologicalNodeVisualManager implements VisualManager {

	private static TopologicalNodeVisualManager instance;

	private static TopologicalNodeEditor generalPanel;
	private static MapElementCharacteristicsEditor charPanel;

	public static TopologicalNodeVisualManager getInstance() {
		if (instance == null) 
			instance = new TopologicalNodeVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new TopologicalNodeEditor();
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
