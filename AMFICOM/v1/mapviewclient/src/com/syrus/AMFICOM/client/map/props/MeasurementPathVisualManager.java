/*-
 * $$Id: MeasurementPathVisualManager.java,v 1.5 2005/09/30 16:08:40 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/30 16:08:40 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
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

	public StorableObjectWrapper getController() {
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
