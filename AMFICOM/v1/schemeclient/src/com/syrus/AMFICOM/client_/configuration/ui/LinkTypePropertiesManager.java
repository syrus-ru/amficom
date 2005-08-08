/*
 * $Id: LinkTypePropertiesManager.java,v 1.9 2005/08/08 11:58:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.ui.EmptyStorableObjectEditor;
import com.syrus.AMFICOM.configuration.LinkTypeWrapper;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.9 $, $Date: 2005/08/08 11:58:06 $
 * @module schemeclient
 */

public class LinkTypePropertiesManager implements VisualManager {
	private static LinkTypePropertiesManager instance;
	private LinkTypeGeneralPanel generalPanel;
	private LinkTypeCharacteristicsPanel charPanel;
	private EmptyStorableObjectEditor emptyPanel;
	
	private LinkTypePropertiesManager() {
		// empty
	}
	
	public static LinkTypePropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null) 
			instance = new LinkTypePropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.generalPanel == null) {
			this.generalPanel = new LinkTypeGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new LinkTypeCharacteristicsPanel();
		}
		this.charPanel.setContext(aContext);
		if (this.emptyPanel == null) {
			this.emptyPanel = new EmptyStorableObjectEditor();
		}
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
	 * @return LinkTypeController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return LinkTypeWrapper.getInstance();
	}
	
	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return this.emptyPanel;
	}
}
