/**
 * $Id: SiteNodeVisualManager.java,v 1.3 2005/04/29 14:09:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public class SiteNodeVisualManager implements VisualManager {

	private static SiteNodeVisualManager instance;

	private static SiteNodeEditor generalPanel;
	private static SiteNodeAddEditor addPanel;
	private static MapElementCharacteristicsEditor charPanel;

	public static SiteNodeVisualManager getInstance() {
		if (instance == null) 
			instance = new SiteNodeVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new SiteNodeEditor();
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

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		if (addPanel == null)
			addPanel = new SiteNodeAddEditor();
		return addPanel;
	}

}
