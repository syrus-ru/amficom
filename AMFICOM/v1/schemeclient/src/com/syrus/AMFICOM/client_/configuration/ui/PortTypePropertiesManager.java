/*
 * $Id: PortTypePropertiesManager.java,v 1.5 2005/04/28 16:02:36 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.client_.scheme.ui.EmptyStorableObjectEditor;
import com.syrus.AMFICOM.configuration.PortTypeController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/04/28 16:02:36 $
 * @module schemeclient_v1
 */

public class PortTypePropertiesManager implements VisualManager {
	private static PortTypePropertiesManager instance;
	private PortTypeGeneralPanel generalPanel;
	private PortTypeCharacteristicsPanel charPanel;
	private EmptyStorableObjectEditor emptyPanel;
	
	private PortTypePropertiesManager() {
		// empty
	}
	
	public static PortTypePropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null) 
			instance = new PortTypePropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new PortTypeGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new PortTypeCharacteristicsPanel();
		charPanel.setContext(aContext);
		if (emptyPanel == null)
			emptyPanel = new EmptyStorableObjectEditor();
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
	 * @return PortTypeController
	 * @see com.syrus.AMFICOM.client_.general.ui_.VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return PortTypeController.getInstance();
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return emptyPanel;
	}
}
