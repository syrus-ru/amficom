/*
 * $Id: PortTypePropertiesManager.java,v 1.2 2005/03/14 13:36:18 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/14 13:36:18 $
 * @module schemeclient_v1
 */

public class PortTypePropertiesManager implements PropertiesMananager {

	private static PortTypePropertiesManager instance;
	private static PortTypeGeneralPanel generalPanel;
	private static PortTypeCharacteristicsPanel charPanel;
	
	public static PortTypePropertiesManager getInstance() {
		if (instance == null) 
			instance = new PortTypePropertiesManager();
		return instance;
	}
	
	/**
	 * @return AbstractLinkTypeGeneralPanel
	 * @see com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new PortTypeGeneralPanel();
		return generalPanel;
	}

	/**
	 * @return LinkTypeCharacteristicsPanel
	 * @see com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new PortTypeCharacteristicsPanel();
		return charPanel;
	}

}
