/*-
 * $Id: SchemePortPropertiesManager.java,v 1.2 2005/04/27 08:47:29 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.scheme.SchemePortController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/27 08:47:29 $
 * @module schemeclient_v1
 */

public class SchemePortPropertiesManager implements VisualManager {
	private static SchemePortPropertiesManager instance;
	private SchemePortGeneralPanel generalPanel;
	private SchemePortCharacteristicsPanel charPanel;
	private EmptyStorableObjectEditor emptyPanel;
	
	private SchemePortPropertiesManager() {
		// empty
	}
	
	public static SchemePortPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemePortPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}

	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new SchemePortGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new SchemePortCharacteristicsPanel();
		charPanel.setContext(aContext);
		if (emptyPanel == null)
			emptyPanel = new EmptyStorableObjectEditor();
	}
	
	/**
	 * @return SchemePortGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return SchemePortCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
	}

	/**
	 * @return SchemeCablePortController
	 * @see VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return SchemePortController.getInstance();
	}
	
	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return emptyPanel;
	}
}
