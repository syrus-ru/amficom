/*
 * $Id: LinkTypePropertiesManager.java,v 1.3 2005/03/30 13:33:39 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.configuration.LinkTypeController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/03/30 13:33:39 $
 * @module schemeclient_v1
 */

public class LinkTypePropertiesManager implements VisualManager {
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
	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new AbstractLinkTypeGeneralPanel();
		return generalPanel;
	}

	/**
	 * @return LinkTypeCharacteristicsPanel
	 * @see com.syrus.AMFICOM.client_.scheme.ui.PropertiesMananager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new LinkTypeCharacteristicsPanel();
		return charPanel;
	}

	/**
	 * @return LinkTypeController
	 * @see com.syrus.AMFICOM.client_.general.ui_.VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return LinkTypeController.getInstance();
	}

}
