/**
 * $Id: MeasurementPathVisualManager.java,v 1.2 2005/04/28 12:57:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public class MeasurementPathVisualManager implements VisualManager {

	private static MeasurementPathVisualManager instance;

	private static MeasurementPathEditor generalPanel;
	private static MapElementCharacteristicsEditor charPanel;
	
	public static MeasurementPathVisualManager getInstance() {
		if (instance == null) 
			instance = new MeasurementPathVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new MeasurementPathEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new MapElementCharacteristicsEditor();
		return charPanel;
	}

	public ObjectResourceController getController() {
//		MeasurementPathController mpc = (MeasurementPathController)MeasurementPathController.getInstance();
//		if (key.equals(PROPERTY_COLOR))
//		{
//			result = mpc.getColor(path);
//		}
//		else
//		if (key.equals(PROPERTY_STYLE))
//		{
//			result = mpc.getStyle(path);
//		}
//		else
//		if (key.equals(PROPERTY_THICKNESS))
//		{
//			result = String.valueOf(mpc.getLineSize(path));
//		}
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
