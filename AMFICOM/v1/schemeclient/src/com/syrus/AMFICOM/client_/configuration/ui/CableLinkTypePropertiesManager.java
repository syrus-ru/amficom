/*
 * $Id: CableLinkTypePropertiesManager.java,v 1.5 2005/04/28 16:02:36 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.configuration.CableLinkTypeController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/04/28 16:02:36 $
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
	 * @see com.syrus.AMFICOM.client_.general.ui_.VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return CableLinkTypeController.getInstance();
	}

	/**
	 * @return CableLinkTypeLayout
	 * @see com.syrus.AMFICOM.client_.general.ui_.VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return layout;
	}

}
