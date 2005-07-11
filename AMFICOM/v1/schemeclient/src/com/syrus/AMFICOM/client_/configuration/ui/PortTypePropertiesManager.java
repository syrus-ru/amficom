/*
 * $Id: PortTypePropertiesManager.java,v 1.7 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.ui.EmptyStorableObjectEditor;
import com.syrus.AMFICOM.configuration.PortTypeWrapper;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/07/11 12:31:38 $
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
	
	public static PortTypePropertiesManager getInstance(ApplicationContext aContext, PortTypeKind kind) {
		if (instance == null) 
			instance = new PortTypePropertiesManager();
		instance.setContext(aContext);
		instance.generalPanel.setPortKind(kind);
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
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return PortTypeWrapper.getInstance();
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return emptyPanel;
	}
}
