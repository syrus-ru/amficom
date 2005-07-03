/**
 * $Id: SiteNodeVisualManager.java,v 1.5 2005/06/06 12:20:34 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

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

	public StorableObjectWrapper getController() {
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		if (addPanel == null)
			addPanel = new SiteNodeAddEditor();
		return addPanel;
	}

}
