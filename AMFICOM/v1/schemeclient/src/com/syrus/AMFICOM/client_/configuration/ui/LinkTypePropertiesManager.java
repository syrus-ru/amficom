/*
 * $Id: LinkTypePropertiesManager.java,v 1.1 2005/03/10 08:09:08 stas Exp $
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

public class LinkTypePropertiesManager implements PropertiesMananager {

	private static LinkTypePropertiesManager instance;
	private static AbstractLinkTypeGeneralPanel generalPanel;
	private static LinkTypeCharacteristicsPanel charPanel;
	
	public static LinkTypePropertiesManager getInstance() {
		if (instance == null) 
			instance = new LinkTypePropertiesManager();
		return instance;
	}
	
	/**
	 * @return AbstractLinkTypeGeneralPanel
	 * @see com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager#getGeneralPropertiesPanel()
	 */
	public ObjectResourcePropertiesPane getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new AbstractLinkTypeGeneralPanel();
		return generalPanel;
	}

	/**
	 * @return LinkTypeCharacteristicsPanel
	 * @see com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager#getCharacteristicPropertiesPanel()
	 */
	public ObjectResourcePropertiesPane getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new LinkTypeCharacteristicsPanel();
		return charPanel;
	}

}
