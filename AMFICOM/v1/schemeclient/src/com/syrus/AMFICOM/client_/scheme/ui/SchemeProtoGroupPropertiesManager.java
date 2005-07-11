/*-
 * $Id: SchemeProtoGroupPropertiesManager.java,v 1.1 2005/07/11 12:31:40 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.SchemeProtoGroupWrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/07/11 12:31:40 $
 * @module schemeclient_v1
 */

public class SchemeProtoGroupPropertiesManager implements VisualManager {
	private static SchemeProtoGroupPropertiesManager instance;
	private SchemeProtoGroupGeneralPanel generalPanel;
	private EmptyStorableObjectEditor charPanel;
	private EmptyStorableObjectEditor ugoPanel;
	
	private SchemeProtoGroupPropertiesManager() {
		// empty
	}
	
	public static SchemeProtoGroupPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemeProtoGroupPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new SchemeProtoGroupGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new EmptyStorableObjectEditor();
		if (ugoPanel == null)
			ugoPanel = new EmptyStorableObjectEditor();
	}
	
	/**
	 * @return SchemeProtoElementGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return SchemeProtoElementCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
	}

	/**
	 * @return SchemeProtoElementController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return SchemeProtoGroupWrapper.getInstance();
	}

	/**
	 * @return SchemeProtoElementUgoPanel
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return ugoPanel;
	}
}


