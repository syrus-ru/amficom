/*
 * $Id: MeasurementTypePropertiesManager.java,v 1.4 2005/03/30 13:33:39 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.measurement.MeasurementTypeController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/03/30 13:33:39 $
 * @module schemeclient_v1
 */

public class MeasurementTypePropertiesManager implements VisualManager {

	private static MeasurementTypePropertiesManager instance;
	private static MeasurementTypeGeneralPanel generalPanel;
	private static MeasurementTypeCharacteristicsPanel charPanel;
	
	public static MeasurementTypePropertiesManager getInstance() {
		if (instance == null) 
			instance = new MeasurementTypePropertiesManager();
		return instance;
	}
	
	/**
	 * @return AbstractLinkTypeGeneralPanel
	 * @see com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new MeasurementTypeGeneralPanel();
		return generalPanel;
	}

	/**
	 * @return LinkTypeCharacteristicsPanel
	 * @see com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new MeasurementTypeCharacteristicsPanel();
		return charPanel;
	}

	/**
	 * @return MeasurementTypeController
	 * @see com.syrus.AMFICOM.client_.general.ui_.VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return MeasurementTypeController.getInstance();
	}

}
