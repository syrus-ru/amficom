/*-
 * $Id: SchemeCablePortPropertiesManager.java,v 1.3 2005/05/26 07:40:52 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.SchemeCablePortWrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/05/26 07:40:52 $
 * @module schemeclient_v1
 */

public class SchemeCablePortPropertiesManager implements VisualManager {
	private static SchemeCablePortPropertiesManager instance;
	private SchemeCablePortGeneralPanel generalPanel;
	private SchemeCablePortCharacteristicsPanel charPanel;
	private EmptyStorableObjectEditor emptyPanel;
	
	private SchemeCablePortPropertiesManager() {
		// empty
	}
	
	public static SchemeCablePortPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemeCablePortPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}

	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new SchemeCablePortGeneralPanel(); 
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new SchemeCablePortCharacteristicsPanel(); 
		charPanel.setContext(aContext);
		if (emptyPanel == null)
			emptyPanel = new EmptyStorableObjectEditor();
	}
	
	/**
	 * @return SchemeCablePortGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return SchemeCablePortCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
	}

	/**
	 * @return SchemeCablePortController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return SchemeCablePortWrapper.getInstance();
	}
	
	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return emptyPanel;
	}
}
