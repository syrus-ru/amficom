/**
 * $Id: PhysicalLinkTypeVisualManager.java,v 1.1 2005/08/08 09:59:41 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

public class PhysicalLinkTypeVisualManager implements VisualManager {

	private static PhysicalLinkTypeVisualManager instance;

	private static PhysicalLinkTypeEditor generalPanel;
	private static MapElementCharacteristicsEditor charPanel;

	public static PhysicalLinkTypeVisualManager getInstance() {
		if (instance == null) 
			instance = new PhysicalLinkTypeVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new PhysicalLinkTypeEditor();
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
