/**
 * $Id: PhysicalLinkVisualManager.java,v 1.6 2005/06/06 12:20:34 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

public class PhysicalLinkVisualManager implements VisualManager {

	private static PhysicalLinkVisualManager instance;

	private static PhysicalLinkEditor generalPanel;
	private static PhysicalLinkAddEditor addPanel;
	private static MapElementCharacteristicsEditor charPanel;

	public static PhysicalLinkVisualManager getInstance() {
		if (instance == null) 
			instance = new PhysicalLinkVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new PhysicalLinkEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new MapElementCharacteristicsEditor();
		return charPanel;
	}

	public StorableObjectWrapper getController() {
//		PhysicalLinkController plc = (PhysicalLinkController)com.syrus.AMFICOM.Client.Map.Controllers.PhysicalLinkController.getInstance();
//		if (key.equals(PROPERTY_COLOR))
//		{
//			result = plc.getColor(link);
//		}
//		else
//		if (key.equals(PROPERTY_STYLE))
//		{
//			result = plc.getStyle(link);
//		}
//		else
//		if (key.equals(PROPERTY_THICKNESS))
//		{
//			result = String.valueOf(plc.getLineSize(link));
//		}
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		if (addPanel == null)
			addPanel = new PhysicalLinkAddEditor();
		return addPanel;
	}

}
