/*
 * $Id: MeasurementTypePropertiesManager.java,v 1.2 2005/03/16 13:05:34 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/03/16 13:05:34 $
 * @module schemeclient_v1
 */

public class MeasurementTypePropertiesManager implements PropertiesMananager {

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

}
