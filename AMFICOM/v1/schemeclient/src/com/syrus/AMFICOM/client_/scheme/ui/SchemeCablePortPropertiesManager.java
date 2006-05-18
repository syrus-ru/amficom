/*-
 * $Id: SchemeCablePortPropertiesManager.java,v 1.6 2005/08/08 11:58:07 arseniy Exp $
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
import com.syrus.AMFICOM.scheme.SchemeCablePortWrapper;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
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
		if (this.generalPanel == null) {
			this.generalPanel = new SchemeCablePortGeneralPanel();
		} 
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new SchemeCablePortCharacteristicsPanel();
		} 
		this.charPanel.setContext(aContext);
		if (this.emptyPanel == null) {
			this.emptyPanel = new EmptyStorableObjectEditor();
		}
	}
	
	/**
	 * @return SchemeCablePortGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPanel;
	}

	/**
	 * @return SchemeCablePortCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return this.charPanel;
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
		return this.emptyPanel;
	}
}
