/*-
 * $Id: SchemePortPropertiesManager.java,v 1.6 2005/08/08 11:58:08 arseniy Exp $
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
import com.syrus.AMFICOM.scheme.SchemePortWrapper;


/**
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/08/08 11:58:08 $
 * @module schemeclient
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
		if (this.generalPanel == null) {
			this.generalPanel = new SchemePortGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new SchemePortCharacteristicsPanel();
		}
		this.charPanel.setContext(aContext);
		if (this.emptyPanel == null) {
			this.emptyPanel = new EmptyStorableObjectEditor();
		}
	}
	
	/**
	 * @return SchemePortGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPanel;
	}

	/**
	 * @return SchemePortCharacteristicsPanel
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
		return SchemePortWrapper.getInstance();
	}
	
	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return this.emptyPanel;
	}
}
