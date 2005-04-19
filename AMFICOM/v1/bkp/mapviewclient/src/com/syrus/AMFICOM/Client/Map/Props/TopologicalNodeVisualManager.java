/**
 * $Id: TopologicalNodeVisualManager.java,v 1.1 2005/04/19 15:48:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

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

	public ObjectResourceController getController() {
		return null;
	}

}
