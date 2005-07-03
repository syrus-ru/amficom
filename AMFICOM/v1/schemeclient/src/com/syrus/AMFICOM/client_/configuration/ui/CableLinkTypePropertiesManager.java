/*
 * $Id: CableLinkTypePropertiesManager.java,v 1.6 2005/05/26 07:40:51 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.configuration.CableLinkTypeWrapper;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/05/26 07:40:51 $
 * @module schemeclient_v1
 */

public class CableLinkTypePropertiesManager implements VisualManager {
	private static CableLinkTypePropertiesManager instance;
	private AbstractLinkTypeGeneralPanel generalPanel;
	private CableLinkTypeCharacteristicsPanel charPanel;
	private CableLinkTypeLayout layout;
	
	private CableLinkTypePropertiesManager() {
		// empty
	}
	
	public static CableLinkTypePropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null) 
			instance = new CableLinkTypePropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new CableLinkTypeGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new CableLinkTypeCharacteristicsPanel();
		charPanel.setContext(aContext);
		if (layout == null)
			layout = new CableLinkTypeLayout();
		layout.setContext(aContext);
	}
	
	/**
	 * @return AbstractLinkTypeGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return LinkTypeCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
	}

	/**
	 * @return CableLinkTypeController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return CableLinkTypeWrapper.getInstance();
	}

	/**
	 * @return CableLinkTypeLayout
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return layout;
	}
}
