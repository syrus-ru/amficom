/**
 * $Id: CablePathVisualManager.java,v 1.3 2005/04/28 12:57:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public class CablePathVisualManager implements VisualManager {

	private static CablePathVisualManager instance;

	private static CablePathEditor generalPanel;
	private static MapElementCharacteristicsEditor charPanel;
	
	public static CablePathVisualManager getInstance() {
		if (instance == null) 
			instance = new CablePathVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new CablePathEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new MapElementCharacteristicsEditor();
		return charPanel;
	}

	public ObjectResourceController getController() {
//		CableController cc = (CableController)com.syrus.AMFICOM.Client.Map.Controllers.CableController.getInstance();
//		if (key.equals(PROPERTY_COLOR))
//		{
//			result = cc.getColor(path);
//		}
//		else
//		if (key.equals(PROPERTY_STYLE))
//		{
//			result = cc.getStyle(path);
//		}
//		else
//		if (key.equals(PROPERTY_THICKNESS))
//		{
//			result = String.valueOf(cc.getLineSize(path));
//		}
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
