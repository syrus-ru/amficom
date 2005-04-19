/**
 * $Id: CollectorVisualManager.java,v 1.1 2005/04/19 15:48:32 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

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

	public ObjectResourceController getController() {
		return null;
	}

}
