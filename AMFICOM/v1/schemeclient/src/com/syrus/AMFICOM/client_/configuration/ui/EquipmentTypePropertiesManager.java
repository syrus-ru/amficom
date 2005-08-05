/*-
 * $Id: EquipmentTypePropertiesManager.java,v 1.5 2005/08/05 12:39:58 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.ui.EmptyStorableObjectEditor;
import com.syrus.AMFICOM.configuration.EquipmentTypeWrapper;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/08/05 12:39:58 $
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
		if (this.generalPanel == null) {
			this.generalPanel = new EquipmentTypeGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new EquipmentTypeCharacteristicsPanel();
		}
		this.charPanel.setContext(aContext);
		if (this.emptyPanel == null) {
			this.emptyPanel = new EmptyStorableObjectEditor();
		}
	}
	
	/**
	 * @return EquipmentTypeGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPanel;
	}

	/**
	 * @return EquipmentTypeCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return this.charPanel;
	}

	/**
	 * @return EquipmentTypeController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return EquipmentTypeWrapper.getInstance();
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return this.emptyPanel;
	}
}
