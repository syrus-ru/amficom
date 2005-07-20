/**
 * $Id: CablePathVisualManager.java,v 1.7 2005/07/20 13:22:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.7 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class CablePathVisualManager implements VisualManager {

	private static CablePathVisualManager instance;

	private static CablePathEditor generalPanel;
	private static CablePathAddEditor addPanel;
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

	public StorableObjectWrapper getController() {
//		CableController cc = (CableController)CableController.getInstance();
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
		if (addPanel == null)
			addPanel = new CablePathAddEditor();
		return addPanel;
	}

}
