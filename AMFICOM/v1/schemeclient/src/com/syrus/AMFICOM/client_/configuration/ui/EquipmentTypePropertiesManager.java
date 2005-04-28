/*-
 * $Id: EquipmentTypePropertiesManager.java,v 1.2 2005/04/28 16:02:36 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.client_.scheme.ui.EmptyStorableObjectEditor;
import com.syrus.AMFICOM.configuration.EquipmentTypeController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/28 16:02:36 $
 * @module schemeclient_v1
 */

public class EquipmentTypePropertiesManager implements VisualManager {
	private static EquipmentTypePropertiesManager instance;
	private EquipmentTypeGeneralPanel generalPanel;
	private EquipmentTypeCharacteristicsPanel charPanel;
	private EmptyStorableObjectEditor emptyPanel;
	
	private EquipmentTypePropertiesManager() {
		// empty
	}
	
	public static EquipmentTypePropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null) 
			instance = new EquipmentTypePropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new EquipmentTypeGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new EquipmentTypeCharacteristicsPanel();
		charPanel.setContext(aContext);
		if (emptyPanel == null)
			emptyPanel = new EmptyStorableObjectEditor();
	}
	
	/**
	 * @return EquipmentTypeGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return EquipmentTypeCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
	}

	/**
	 * @return EquipmentTypeController
	 * @see com.syrus.AMFICOM.client_.general.ui_.VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return EquipmentTypeController.getInstance();
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return emptyPanel;
	}
}
