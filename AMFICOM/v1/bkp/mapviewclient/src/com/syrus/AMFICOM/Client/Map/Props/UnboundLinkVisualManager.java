/**
 * $Id: UnboundLinkVisualManager.java,v 1.1 2005/04/19 15:48:57 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public class UnboundLinkVisualManager implements VisualManager {

	private static UnboundLinkVisualManager instance;

	private static UnboundLinkEditor generalPanel;
	private static MapElementCharacteristicsEditor charPanel;

	public static UnboundLinkVisualManager getInstance() {
		if (instance == null) 
			instance = new UnboundLinkVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new UnboundLinkEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new MapElementCharacteristicsEditor();
		return charPanel;
	}

	public ObjectResourceController getController() {
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

}
