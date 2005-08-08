/*
 * $Id: CableLinkTypePropertiesManager.java,v 1.9 2005/08/08 11:58:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.configuration.CableLinkTypeWrapper;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.9 $, $Date: 2005/08/08 11:58:06 $
 * @module schemeclient
 */

public class CableLinkTypePropertiesManager implements VisualManager {
	private static CableLinkTypePropertiesManager instance;
	private CableLinkTypeGeneralPanel generalPanel;
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
		if (this.generalPanel == null)
			this.generalPanel = new CableLinkTypeGeneralPanel();
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null)
			this.charPanel = new CableLinkTypeCharacteristicsPanel();
		this.charPanel.setContext(aContext);
		if (this.layout == null)
			this.layout = new CableLinkTypeLayout();
		this.layout.setContext(aContext);
	}
	
	/**
	 * @return AbstractLinkTypeGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPanel;
	}

	/**
	 * @return LinkTypeCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return this.charPanel;
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
		return this.layout;
	}
}
