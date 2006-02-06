/*-
 * $Id: ProtoEquipmentPropertiesManager.java,v 1.2 2005/09/29 13:20:49 stas Exp $
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
import com.syrus.AMFICOM.configuration.ProtoEquipmentWrapper;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/09/29 13:20:49 $
 * @module schemeclient
 */

public class ProtoEquipmentPropertiesManager implements VisualManager {
	private static ProtoEquipmentPropertiesManager instance;
	private ProtoEquipmentGeneralPanel generalPanel;
	private ProtoEquipmentCharacteristicsPanel charPanel;
	private EmptyStorableObjectEditor emptyPanel;
	
	private ProtoEquipmentPropertiesManager() {
		// empty
	}
	
	public static ProtoEquipmentPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null) 
			instance = new ProtoEquipmentPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.generalPanel == null) {
			this.generalPanel = new ProtoEquipmentGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new ProtoEquipmentCharacteristicsPanel();
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
		return ProtoEquipmentWrapper.getInstance();
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return this.emptyPanel;
	}
}
