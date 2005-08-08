/**
 * $Id: SiteNodeTypeVisualManager.java,v 1.1 2005/08/08 09:59:41 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

public class SiteNodeTypeVisualManager implements VisualManager {

	private static SiteNodeTypeVisualManager instance;

	private static SiteNodeTypeEditor generalPanel;
	private static MapElementCharacteristicsEditor charPanel;

	public static SiteNodeTypeVisualManager getInstance() {
		if (instance == null) 
			instance = new SiteNodeTypeVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new SiteNodeTypeEditor();
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
		return null;
	}

}
