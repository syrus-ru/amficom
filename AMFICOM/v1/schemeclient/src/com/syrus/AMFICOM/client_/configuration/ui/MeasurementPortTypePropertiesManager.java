/*
 * $Id: MeasurementPortTypePropertiesManager.java,v 1.1 2005/03/10 08:09:08 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/10 08:09:08 $
 * @module schemeclient_v1
 */

public class MeasurementPortTypePropertiesManager implements PropertiesMananager {

	private static MeasurementPortTypePropertiesManager instance;
	private static MeasurementPortTypeGeneralPanel generalPanel;
	private static MeasurementPortTypeCharacteristicsPanel charPanel;
	
	public static MeasurementPortTypePropertiesManager getInstance() {
		if (instance == null) 
			instance = new MeasurementPortTypePropertiesManager();
		return instance;
	}
	
	/**
	 * @return AbstractLinkTypeGeneralPanel
	 * @see com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager#getGeneralPropertiesPanel()
	 */
	public ObjectResourcePropertiesPane getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new MeasurementPortTypeGeneralPanel();
		return generalPanel;
	}

	/**
	 * @return LinkTypeCharacteristicsPanel
	 * @see com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager#getCharacteristicPropertiesPanel()
	 */
	public ObjectResourcePropertiesPane getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new MeasurementPortTypeCharacteristicsPanel();
		return charPanel;
	}

}
